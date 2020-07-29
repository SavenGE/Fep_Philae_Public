CREATE OR REPLACE FUNCTION BERHANLIVE.CALCULATE_AVAIL_BALANCE_EB (P_ACCT_ID NUMBER)
   RETURN NUMBER
IS
   V_CLEARED_BAL                   NUMBER;
   V_OD_LIMIT                      NUMBER;
   V_RESERVED_FUND                 NUMBER;
   V_EARMARKED_FUND                NUMBER;
   V_COLLATERAL_LIEN               NUMBER;
   V_DR_PENDING_BAL                NUMBER;
   V_CR_PENDING_BAL                NUMBER;
   V_DR_INT_ACCRUED                NUMBER;
   V_MINIMUMBAL                    NUMBER := 0;
   V_MINIMUMLDGRBAL                NUMBER;
   V_MINIMUMCLRDBAL                NUMBER;
   V_MINIMUMAVAILBAL               NUMBER;
   V_ACTUAL_CHRG_AMT               NUMBER := 0;
   V_ACTUAL_TAX_AMT                NUMBER := 0;
   V_PENDING_REPMNT                NUMBER := 0;
   V_CHRG_AMT                      NUMBER := 0;
   V_CRNCY_ID                      NUMBER;
   V_CHRG_AMT_CRNCY_ID             NUMBER;
   V_ACCT_CRNCY_ID                 NUMBER;
   V_DEBITTXNAMOUNT                NUMBER := 0;
   V_FUTUREDATED_TXN_AMT           NUMBER := 0;
   V_CUR_PROC_DATE                 DATE;
   V_AVAIL_BAL                     NUMBER;
   V_AVAL_BAL_DESC                 CTRL_PARAMETER.PARAM_VALUE%TYPE;
   V_ACCRUED_INT_RESRV_FUNDS_FG    CHAR (1);
   V_ACCRUED_CHRG_RESRV_FUNDS_FG   CHAR (1);
   V_LOAN_REPMNT_RESRV_FUNDS_FG    CHAR (1);

   CURSOR CYCLIC_CHRG_CUR
   IS
      SELECT NVL (CHRG_AMT, 0), CHRG_AMT_CRNCY_ID
        FROM V_ACCOUNT_CYCLIC_CHARGE
       WHERE ACCT_ID = P_ACCT_ID;

   CURSOR LN_REPY_EVENT_CUR
   IS
      SELECT NVL (AMT_UNPAID, 0), ACCT_CRNCY_ID
        FROM V_LN_ACCT_REPY_EVENT
       WHERE     FUNDING_ACCT_ID = P_ACCT_ID
             AND DUE_DT <= V_CUR_PROC_DATE
             AND REC_ST IN ('N', 'P')
             AND LN_REC_ST NOT IN ('I', 'S', 'C', 'L')
             AND FUNDING_TY IN ('OWN', 'OTHER')
             AND AMT_UNPAID > 0
             AND AUTO_RESRV_REPMNT_FUNDS_FG = 'Y';
BEGIN
   ---Control Param Information---
   SELECT TO_DATE (DISPLAY_VALUE, 'DD/MM/YYYY')
     INTO V_CUR_PROC_DATE
     FROM CTRL_PARAMETER
    WHERE PARAM_CD = 'S02';

   SELECT PARAM_VALUE
     INTO V_AVAL_BAL_DESC
     FROM CTRL_PARAMETER
    WHERE PARAM_CD = 'S114';

   ---Deposit Account Summary Details---
   SELECT A.CLEARED_BAL,
          A.EARMARKED_FUND,
          A.CUMULATIVE_LIEN_AMT,
          A.DR_PENDING_BAL,
          A.CR_PENDING_BAL,
          A.DR_INT_ACCRUED,
          B.ACCRUED_INT_RESRV_FUNDS_FG,
          B.ACCRUED_CHRG_RESRV_FUNDS_FG,
          B.LOAN_REPMNT_RESRV_FUNDS_FG
     INTO V_CLEARED_BAL,
          V_EARMARKED_FUND,
          V_COLLATERAL_LIEN,
          V_DR_PENDING_BAL,
          V_CR_PENDING_BAL,
          V_DR_INT_ACCRUED,
          V_ACCRUED_INT_RESRV_FUNDS_FG,
          V_ACCRUED_CHRG_RESRV_FUNDS_FG,
          V_LOAN_REPMNT_RESRV_FUNDS_FG
     FROM DEPOSIT_ACCOUNT_SUMMARY A, DEPOSIT_PRODUCT_BASIC_INFO B
    WHERE A.PROD_ID = B.PROD_ID AND A.DEPOSIT_ACCT_ID = P_ACCT_ID;

    SELECT NVL(SUM(HOLD_AMT), 0) 
    INTO V_RESERVED_FUND 
    FROM ACCOUNT_HOLD 
    WHERE REC_ST='A' AND ACCT_ID=P_ACCT_ID;

   SELECT CRNCY_ID
     INTO V_CRNCY_ID
     FROM DEPOSIT_ACCOUNT
    WHERE ACCT_ID = P_ACCT_ID;

   ---OverDraft Limit Information---
   SELECT NVL (SUM (B.APPROVED_AMT), 0)
     INTO V_OD_LIMIT
     FROM CREDIT_APPL_OD_INFO A, CREDIT_APPL_OD_INFO_LIMIT B, CREDIT_APPL C
    WHERE     A.CREDIT_APPL_OD_INFO_ID = B.CREDIT_APPL_OD_INFO_ID
          AND A.DEPOSIT_ACCT_ID = P_ACCT_ID
          AND A.REC_ST = 'A'
          AND A.START_DT <= V_CUR_PROC_DATE
          AND A.EXPIRY_DT > V_CUR_PROC_DATE
          AND A.APPL_ID = C.APPL_ID
          AND C.REC_ST = 'A';

   ---Based on Available Balance Definition---
   IF (V_AVAL_BAL_DESC = 'ABD1DP')
   THEN
      SELECT NVL (SUM (RES_VALUE), 0)
        INTO V_MINIMUMLDGRBAL
        FROM V_ACCOUNT_RESTRICTION
       WHERE     ACCT_ID = P_ACCT_ID
             AND RES_SUB_TY_CD = 'PRDRES1'
             AND RES_APPL_SCOPE_CD = 'TP'
             AND EFFECTIVE_DT <= V_CUR_PROC_DATE;

      SELECT NVL (SUM (RES_VALUE), 0)
        INTO V_MINIMUMCLRDBAL
        FROM V_ACCOUNT_RESTRICTION
       WHERE     ACCT_ID = P_ACCT_ID
             AND RES_SUB_TY_CD = 'PRDRES17'
             AND RES_APPL_SCOPE_CD = 'TP'
             AND EFFECTIVE_DT <= V_CUR_PROC_DATE;

      SELECT NVL (SUM (RES_VALUE), 0)
        INTO V_MINIMUMAVAILBAL
        FROM V_ACCOUNT_RESTRICTION
       WHERE     ACCT_ID = P_ACCT_ID
             AND RES_SUB_TY_CD = 'PRDRES53'
             AND RES_APPL_SCOPE_CD = 'TP'
             AND EFFECTIVE_DT <= V_CUR_PROC_DATE;

      IF (V_MINIMUMLDGRBAL > V_MINIMUMCLRDBAL)
      THEN
         V_MINIMUMBAL := V_MINIMUMLDGRBAL;
      ELSE
         V_MINIMUMBAL := V_MINIMUMCLRDBAL;
      END IF;

      V_AVAIL_BAL :=
           V_CLEARED_BAL
         + V_OD_LIMIT
         + V_CR_PENDING_BAL
         - V_DR_PENDING_BAL
         - V_RESERVED_FUND
         - V_EARMARKED_FUND
         - V_COLLATERAL_LIEN
         - V_MINIMUMBAL
         - V_MINIMUMAVAILBAL;
      DBMS_OUTPUT.PUT_LINE ('AVAIL BAL:' || V_AVAIL_BAL);
   END IF;

   IF (V_AVAL_BAL_DESC = 'ABD2DP')
   THEN
      V_AVAIL_BAL :=
           V_CLEARED_BAL
         + V_OD_LIMIT
         + V_CR_PENDING_BAL
         - V_DR_PENDING_BAL
         - V_RESERVED_FUND
         - V_EARMARKED_FUND
         - V_COLLATERAL_LIEN;
   END IF;

   IF (V_AVAL_BAL_DESC = 'ABD3DP')
   THEN
      V_AVAIL_BAL :=
           V_CLEARED_BAL
         + V_OD_LIMIT
         + V_CR_PENDING_BAL
         - V_DR_PENDING_BAL
         - V_RESERVED_FUND
         - V_COLLATERAL_LIEN;
   END IF;

   IF (V_AVAL_BAL_DESC = 'ABD4DP')
   THEN
      V_AVAIL_BAL :=
           V_CLEARED_BAL
         + V_OD_LIMIT
         - V_RESERVED_FUND
         - V_EARMARKED_FUND
         - V_COLLATERAL_LIEN;
   END IF;

   ---Debit Interest Accrued---
   IF (V_ACCRUED_INT_RESRV_FUNDS_FG = 'Y' AND V_DR_INT_ACCRUED IS NOT NULL)
   THEN
      V_AVAIL_BAL := V_AVAIL_BAL - V_DR_INT_ACCRUED;
   END IF;

   ---Pending Charge and Pending Tax---
   IF (V_ACCRUED_CHRG_RESRV_FUNDS_FG = 'Y')
   THEN
      SELECT NVL (SUM (ACTUAL_CHRG_AMT), 0)
        INTO V_ACTUAL_CHRG_AMT
        FROM V_PENDING_EVENT_CHARGE_JOURNAL
       WHERE     CHRG_SETLMNT_ACCT_ID = P_ACCT_ID
             AND CHRG_SETLMNT_ACCT_TY_ID = 501
             AND CHARGE_JOURNAL_ST IN ('P', 'E')
             AND ACTUAL_CHRG_AMT > 0;

      IF (V_ACTUAL_CHRG_AMT IS NOT NULL)
      THEN
         V_AVAIL_BAL := V_AVAIL_BAL - V_ACTUAL_CHRG_AMT;
      END IF;

      SELECT NVL (SUM (ACTUAL_TAX_AMT), 0)
        INTO V_ACTUAL_TAX_AMT
        FROM V_EVENT_PENDING_TAX_JOURNAL
       WHERE     TAX_SETLMNT_ACCT_ID = P_ACCT_ID
             AND TAX_SETLMNT_ACCT_TY_ID = 501
             AND REC_ST IN ('P', 'E')
             AND ACTUAL_TAX_AMT > 0;

      IF (V_ACTUAL_TAX_AMT IS NOT NULL)
      THEN
         V_AVAIL_BAL := V_AVAIL_BAL - V_ACTUAL_TAX_AMT;
      END IF;
   END IF;

   ---Accrued Charges---
   IF (V_ACCRUED_CHRG_RESRV_FUNDS_FG = 'Y')
   THEN
      BEGIN
         OPEN CYCLIC_CHRG_CUR;

         LOOP
            FETCH CYCLIC_CHRG_CUR
            INTO V_CHRG_AMT, V_CHRG_AMT_CRNCY_ID;

            IF CYCLIC_CHRG_CUR%NOTFOUND
            THEN
               DBMS_OUTPUT.PUT_LINE ('RECORD NOT FOUND');
               EXIT;
            END IF;

            IF (V_CHRG_AMT_CRNCY_ID != V_CRNCY_ID)
            THEN
               RAISE_APPLICATION_ERROR (
                  -20000,
                  'Charge Currency is not Equal to Account Currency');
            END IF;

            V_AVAIL_BAL := V_AVAIL_BAL - V_CHRG_AMT;
         END LOOP;

         CLOSE CYCLIC_CHRG_CUR;
      END;
   END IF;

   ---Loan Arrears---
   IF (V_LOAN_REPMNT_RESRV_FUNDS_FG = 'Y')
   THEN
      BEGIN
         OPEN LN_REPY_EVENT_CUR;

         LOOP
            FETCH LN_REPY_EVENT_CUR
            INTO V_PENDING_REPMNT, V_ACCT_CRNCY_ID;

            IF LN_REPY_EVENT_CUR%NOTFOUND
            THEN
               DBMS_OUTPUT.PUT_LINE ('RECORD NOT FOUND');
               EXIT;
            END IF;

            IF (V_ACCT_CRNCY_ID != V_CRNCY_ID)
            THEN
               RAISE_APPLICATION_ERROR (
                  -20000,
                  'Loan Account Currency is not Equal to Account Currency');
            END IF;

            V_AVAIL_BAL := V_AVAIL_BAL - V_PENDING_REPMNT;
         END LOOP;

         CLOSE LN_REPY_EVENT_CUR;
      END;
   END IF;

   ---Unauthorised Debit---
   BEGIN
      SELECT NVL (UNAUTH_DR, 0)
        INTO V_DEBITTXNAMOUNT
        FROM AVAILABLE_BALANCE_COMPONENTS
       WHERE ACCT_ID = P_ACCT_ID;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         V_DEBITTXNAMOUNT := 0;


         IF V_DEBITTXNAMOUNT > 0
         THEN
            V_AVAIL_BAL := V_AVAIL_BAL - V_DEBITTXNAMOUNT;
         END IF;
   END;

   ---Future Dated Transaction Amount---
   BEGIN
      SELECT NVL (SUM (ACCT_AMT), 0)
        INTO V_FUTUREDATED_TXN_AMT
        FROM FUTURE_DATED_TXN_JOURNAL
       WHERE ACCT_ID = P_ACCT_ID AND VALUE_DT > V_CUR_PROC_DATE;
   EXCEPTION
      WHEN NO_DATA_FOUND
      THEN
         V_FUTUREDATED_TXN_AMT := 0;

         IF V_FUTUREDATED_TXN_AMT > 0
         THEN
            V_AVAIL_BAL := V_AVAIL_BAL - V_FUTUREDATED_TXN_AMT;
         END IF;
   END;

   RETURN V_AVAIL_BAL;
END; 
/

