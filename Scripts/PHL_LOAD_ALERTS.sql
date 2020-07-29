CREATE OR REPLACE PROCEDURE CHANNELMANAGER.PHL_LOAD_ALERTS 
(
    P_CODE IN VARCHAR2,
    P_TYPE IN VARCHAR2,
    P_DAYS IN NUMBER,
    P_CURRENCY IN VARCHAR2,
    P_FILTER_BY IN VARCHAR2,
    P_FILTER IN VARCHAR2
)
AS
    V_TXN_ID      NUMBER;
    V_BU_ID       NUMBER;
    V_CUST_ID     NUMBER;
    V_CUST_NAME   VARCHAR2(120 BYTE);
    V_ACCT_NO     VARCHAR2(60 BYTE);
    V_CONTRA     VARCHAR2(60 BYTE);
    V_CHRG_ACCT   VARCHAR2(60 BYTE);
    V_TXN_DATE    DATE;
    V_CURRENCY    VARCHAR2(10 BYTE);
    V_TXN_AMT     NUMBER(38,10);
    V_TXN_CHG     NUMBER(38,10);
    V_TXN_DESC    VARCHAR2(300 BYTE);
    V_BALANCE    NUMBER(38,10);
    V_CONTACT     VARCHAR2(100 BYTE);
    V_ORIGINATOR  VARCHAR2(100 BYTE);
    V_FLT_TEXT    VARCHAR2(10000);
    V_SQL_TEXT    VARCHAR2(10000);
    DATA_CURSOR   EB_PACKAGE.REF_CURSOR;
 
BEGIN
    IF (P_FILTER != 'ALL') THEN
    BEGIN
        IF (P_FILTER_BY = 'MN') THEN
            BEGIN
                V_FLT_TEXT := 'AND O.CONTACT IN ('||P_FILTER||')';
            END;
        ELSIF (P_FILTER_BY = 'AN') THEN
            BEGIN
                V_FLT_TEXT := 'AND A.ACCT_NO IN ('||P_FILTER||')';
            END;
        ELSIF (P_FILTER_BY = 'CN') THEN
            BEGIN
                V_FLT_TEXT := 'AND C.CUST_NO IN ('||P_FILTER||')';
            END;
        ELSIF (P_FILTER_BY = 'CT') THEN
            BEGIN
                V_FLT_TEXT := 'AND C.CUST_TY_ID IN ('||P_FILTER||')';
            END;
        ELSIF (P_FILTER_BY = 'AP') THEN
            BEGIN
                V_FLT_TEXT := 'AND A.PROD_ID IN ('||P_FILTER||')';
            END;
        ELSIF (P_FILTER_BY = 'BU') THEN
            BEGIN
                V_FLT_TEXT := 'AND B.BU_ID IN ('||P_FILTER||')';
            END;
        END IF;
    END;
    END IF;

    IF (P_TYPE = 'CR') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT 
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                NAWIRILIVE.CALCULATE_AVAIL_BALANCE_EB(A.ACCT_ID) AS CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                NVL(H.DEPOSITOR_PAYEE_NM, NVL((SELECT MAX(Z.ACCT_NM) FROM NAWIRILIVE.ACCOUNT Z WHERE Z.ACCT_NO=H.CONTRA_ACCT_NO), (SELECT MAX(D.ACCT_NM) FROM NAWIRILIVE.ACCOUNT D, NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY E WHERE D.ACCT_NO=E.ACCT_NO AND E.EVENT_JOURNAL_ID=H.EVENT_JOURNAL_ID AND E.DR_CR_IND=''DR''))) AS DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''CR''
                                AND H.CHRG_ID IS NULL
                                AND H.EVENT_ID NOT IN (3513,2088,2646)
                                AND H.TRAN_DESC NOT LIKE ''REV~%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%REVERSAL%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% CHARGE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%EXCISE DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% TAX%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEES''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENEVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENOVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%STAMP DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%COMMISSION%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%PROCESSING%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%FORM%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SINK%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%INSURANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%ADVANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SHARES%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%MAINTENANCE%''
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;            
         END;         
    ELSIF (P_TYPE = 'CV') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                NAWIRILIVE.CALCULATE_AVAIL_BALANCE_EB(A.ACCT_ID) AS CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                NVL(H.DEPOSITOR_PAYEE_NM, NVL((SELECT MAX(Z.ACCT_NM) FROM NAWIRILIVE.ACCOUNT Z WHERE Z.ACCT_NO=H.CONTRA_ACCT_NO), (SELECT MAX(D.ACCT_NM) FROM NAWIRILIVE.ACCOUNT D, NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY E WHERE D.ACCT_NO=E.ACCT_NO AND E.EVENT_JOURNAL_ID=H.EVENT_JOURNAL_ID AND E.DR_CR_IND=''DR''))) AS DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''DR''
                                AND H.CHRG_ID IS NULL
				AND H.EVENT_ID NOT IN (2646)
                                AND (H.TRAN_DESC LIKE ''REV~%'' OR UPPER(H.TRAN_DESC) LIKE ''%REVERSAL%'')
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% CHARGE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%EXCISE DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% TAX%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEES''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENEVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENOVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%STAMP DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%COMMISSION%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%PROCESSING%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%FORM%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SINK%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%INSURANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%ADVANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SHARES%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%MAINTENANCE%''
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;            
         END;         
    ELSIF (P_TYPE = 'DR') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                NAWIRILIVE.CALCULATE_AVAIL_BALANCE_EB(A.ACCT_ID) AS CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                H.DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''DR''
                                AND H.CHRG_ID IS NULL
				AND H.EVENT_ID NOT IN (2646)
                                AND H.TRAN_DESC NOT LIKE ''REV~%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%REVERSAL%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% CHARGE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%EXCISE DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% TAX%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEES''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENEVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENOVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%STAMP DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%COMMISSION%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%PROCESSING%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%FORM%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SINK%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%INSURANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%ADVANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SHARES%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%MAINTENANCE%''
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
                                            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'DV') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                NAWIRILIVE.CALCULATE_AVAIL_BALANCE_EB(A.ACCT_ID) AS CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                H.DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''CR''
                                AND H.CHRG_ID IS NULL
				AND H.EVENT_ID NOT IN (2646)
                                AND (H.TRAN_DESC LIKE ''REV~%'' OR UPPER(H.TRAN_DESC) LIKE ''%REVERSAL%'')
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% CHARGE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%EXCISE DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% TAX%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEES''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''% FEE''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENEVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%BENOVOLENT%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%STAMP DUTY%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%COMMISSION%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%PROCESSING%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%FORM%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SINK%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%INSURANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%ADVANCE%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%SHARES%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%MAINTENANCE%''
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
                                            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'LR') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                S.CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                NVL(H.DEPOSITOR_PAYEE_NM, NVL((SELECT MAX(Z.ACCT_NM) FROM NAWIRILIVE.ACCOUNT Z WHERE Z.ACCT_NO=H.CONTRA_ACCT_NO), (SELECT MAX(D.ACCT_NM) FROM NAWIRILIVE.ACCOUNT D, NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY E WHERE D.ACCT_NO=E.ACCT_NO AND E.EVENT_JOURNAL_ID=H.EVENT_JOURNAL_ID AND E.DR_CR_IND=''DR''))) AS DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.DEPOSIT_ACCOUNT_SUMMARY S
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND S.DEPOSIT_ACCT_ID = A.ACCT_ID
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''DR''
                                AND H.EVENT_ID IN (2646)
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;            
         END;         
    ELSIF (P_TYPE = 'LB') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                H.ACCT_HIST_ID,
                                B.BU_ID,
                                A.CUST_ID, 
                                A.ACCT_NM, 
                                H.ACCT_NO,
                                H.CONTRA_ACCT_NO,
                                H.ACCT_NO AS CHG_ACCT,
                                H.TRAN_DT,
                                Y.CRNCY_CD,
                                H.ACCT_AMT, 
                                NULL AS TXN_CHG, 
                                H.TRAN_DESC, 
                                S.CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT,
                                H.DEPOSITOR_PAYEE_NM
                            FROM NAWIRILIVE.DEPOSIT_ACCOUNT_HISTORY H, NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.DEPOSIT_ACCOUNT_SUMMARY S
                            WHERE 
                                H.ACCT_HIST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)
                                AND A.ACCT_ID = H.DEPOSIT_ACCT_ID 
                                AND S.DEPOSIT_ACCT_ID = A.ACCT_ID
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID
                                AND H.TRAN_DT >= TRUNC(SYSDATE - 1)
                                AND H.DR_CR_IND = ''DR''
                                AND H.TRAN_DESC NOT LIKE ''REV~%''
                                AND UPPER(H.TRAN_DESC) NOT LIKE ''%REVERSAL%''
                                AND (H.EVENT_ID IN (1006811,1006807) OR UPPER(H.TRAN_DESC) LIKE ''%DISBURSEMENT%'')
                                AND Y.CRNCY_ID = A.CRNCY_ID '||V_FLT_TEXT;
                                            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, ORIGINATOR, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT, V_ORIGINATOR;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'LD') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT DISTINCT 
                                A.ACCT_ID AS TXN_ID,
                                B.BU_ID,
                                A.CUST_ID,  
                                A.ACCT_NM, 
                                A.ACCT_NO,
                                NULL AS CONTRA_ACCT_NO,
                                (SELECT MAX(ACCT_NO) FROM NAWIRILIVE.ACCOUNT WHERE ACCT_ID IN (SELECT PRINCIPAL_REPAY_ACCT_ID FROM NAWIRILIVE.LOAN_ACCOUNT_PAYMENT_INFO WHERE ACCT_ID=A.ACCT_ID)) AS CHG_ACCT,
                                R.DUE_DT,
                                Y.CRNCY_CD,
                                (SELECT SUM(RM.AMT_UNPAID) FROM NAWIRILIVE.LN_ACCT_REPMNT_EVENT RM WHERE  RM.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'')
                                AND RM.DUE_DT = TRUNC(SYSDATE + '||P_DAYS||') AND RM.ACCT_ID = A.ACCT_ID AND RM.REC_ST IN (''P'',''N'')) AS AMT_UNPAID,
                                NULL AS TXN_CHG, 
                                ''LOAN AMOUNT DUE'' AS TXN_DESC, 
                                Q.CLEARED_BAL,
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                            FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.LN_ACCT_REPMNT_EVENT R, NAWIRILIVE.LOAN_ACCOUNT_SUMMARY Q, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.CURRENCY Y
                            WHERE 
                                A.ACCT_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND TRUNC(CREATE_DT) = TRUNC(SYSDATE))
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID 
                                AND A.ACCT_ID = R.ACCT_ID                                  
                                AND Y.CRNCY_ID = A.CRNCY_ID
                                AND R.DUE_DT = TRUNC(SYSDATE + '||P_DAYS||') 
                                AND R.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'') 
                                AND R.REC_ST IN (''P'',''N'') 
                                AND A.REC_ST <> ''L'' 
                                AND Q.LAST_DISBURSEMENT_DT IS NOT NULL 
                                AND Q.ACCT_ID = A.ACCT_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'LA') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT DISTINCT 
                                A.ACCT_ID AS TXN_ID,
                                B.BU_ID,
                                A.CUST_ID,  
                                A.ACCT_NM, 
                                A.ACCT_NO,
                                NULL AS CONTRA_ACCT_NO,
                                (SELECT MAX(ACCT_NO) FROM NAWIRILIVE.ACCOUNT WHERE ACCT_ID IN (SELECT PRINCIPAL_REPAY_ACCT_ID FROM NAWIRILIVE.LOAN_ACCOUNT_PAYMENT_INFO WHERE ACCT_ID=A.ACCT_ID)) AS CHG_ACCT,
                                R.DUE_DT,
                                Y.CRNCY_CD,
                                (SELECT SUM(RM.AMT_UNPAID) FROM NAWIRILIVE.LN_ACCT_REPMNT_EVENT RM WHERE  RM.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'')
                                AND RM.DUE_DT = TRUNC(SYSDATE - '||P_DAYS||') AND RM.ACCT_ID = A.ACCT_ID AND RM.REC_ST IN (''P'',''N'')) AS AMT_UNPAID,
                                NULL AS TXN_CHG, 
                                ''LOAN AMOUNT IN ARREARS'' AS TXN_DESC,
                                Q.CLEARED_BAL, 
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                            FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.LN_ACCT_REPMNT_EVENT R, NAWIRILIVE.LOAN_ACCOUNT_SUMMARY Q, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.CURRENCY Y
                            WHERE 
                                A.ACCT_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND TRUNC(CREATE_DT) = TRUNC(SYSDATE))
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID 
                                AND A.ACCT_ID = R.ACCT_ID                                  
                                AND Y.CRNCY_ID = A.CRNCY_ID
                                AND R.DUE_DT = TRUNC(SYSDATE - '||P_DAYS||') 
                                AND R.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'') 
                                AND R.REC_ST IN (''P'',''N'') 
                                AND A.REC_ST <> ''L'' 
                                AND Q.LAST_DISBURSEMENT_DT IS NOT NULL 
                                AND Q.ACCT_ID = A.ACCT_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'LF') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT DISTINCT 
                                A.ACCT_ID AS TXN_ID,
                                B.BU_ID,
                                A.CUST_ID,  
                                A.ACCT_NM, 
                                A.ACCT_NO,
                                NULL AS CONTRA_ACCT_NO,
                                (SELECT MAX(ACCT_NO) FROM NAWIRILIVE.ACCOUNT WHERE ACCT_ID IN (SELECT PRINCIPAL_REPAY_ACCT_ID FROM NAWIRILIVE.LOAN_ACCOUNT_PAYMENT_INFO WHERE ACCT_ID=A.ACCT_ID)) AS CHG_ACCT,
                                L.MATURITY_DT,
                                Y.CRNCY_CD,
                                (SELECT SUM(RM.AMT_UNPAID) FROM NAWIRILIVE.LN_ACCT_REPMNT_EVENT RM WHERE  RM.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'')
                                AND RM.DUE_DT <= TRUNC(SYSDATE - '||P_DAYS||') AND RM.ACCT_ID = A.ACCT_ID AND RM.REC_ST IN (''P'',''N'')) AS AMT_UNPAID,
                                NULL AS TXN_CHG, 
                                ''LOAN AMOUNT DEFAULTED'' AS TXN_DESC,
                                Q.CLEARED_BAL, 
                                NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                            FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.LN_ACCT_REPMNT_EVENT R, NAWIRILIVE.LOAN_ACCOUNT_SUMMARY Q, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.LOAN_ACCOUNT L
                            WHERE 
                                A.ACCT_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND TRUNC(CREATE_DT) = TRUNC(SYSDATE))
                                AND C.CUST_ID = A.CUST_ID
                                AND C.CUST_CAT = ''PER''
                                AND B.BU_ID = A.MAIN_BRANCH_ID 
                                AND A.ACCT_ID = R.ACCT_ID                                  
                                AND Y.CRNCY_ID = A.CRNCY_ID
                                AND L.MATURITY_DT = TRUNC(SYSDATE - '||P_DAYS||') 
                                AND R.EVENT_TYPE IN(''PRINCIPAL'',''CHARGE'',''INTEREST'') 
                                AND R.REC_ST IN (''P'',''N'') 
                                AND A.REC_ST <> ''L'' 
                                AND L.ACCT_ID = A.ACCT_ID
                                AND Q.LAST_DISBURSEMENT_DT IS NOT NULL 
                                AND Q.ACCT_ID = A.ACCT_ID '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, BALANCE, CONTACT, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_BALANCE, V_CONTACT, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_BALANCE, V_CONTACT;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'GA') THEN
        BEGIN
            V_SQL_TEXT := 'SELECT  
                                        G.CREDIT_APPL_GUARANTOR_ID AS TXN_ID,
                                        B.BU_ID AS BU_ID,
                                        A.CUST_ID AS CUST_ID, 
                                        A.ACCT_NM AS ACCT_NM, 
                                        A.ACCT_NO AS ACCT_NO,
                                        L.ACCT_NO AS CONTRA_ACCT_NO,
                                        A.ACCT_NO AS CHG_ACCT,
                                        G.CREATE_DT AS TRAN_DT,
                                        Y.CRNCY_CD AS CRNCY_CD,
                                        G.GUARANTEED_AMT AS TXN_AMT, 
                                        NULL AS TXN_CHG, 
                                        ''GUARANTOR ALERT'' AS TRAN_DESC,
                                        NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                                    FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B, NAWIRILIVE.CREDIT_APPL_GUARANTOR G, NAWIRILIVE.V_CREDIT_APPL L
                                    WHERE 
                                        G.CREDIT_APPL_GUARANTOR_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CREATE_DT >= SYSDATE-5)                                
                                        AND A.CUST_ID = C.CUST_ID
                                        AND G.GUARANTOR_ID = C.CUST_ID
                                        AND B.BU_ID = A.MAIN_BRANCH_ID
                                        AND Y.CRNCY_ID = A.CRNCY_ID 
                                        AND A.ACCT_NO = (SELECT MAX(X.ACCT_NO) FROM NAWIRILIVE.ACCOUNT X WHERE X.REC_ST=''A'' AND X.PROD_CAT_TY=''DP'' AND X.PROD_ID NOT IN (49,50) AND X.CUST_ID=C.CUST_ID)
                                        AND A.REC_ST = ''A'' AND A.PROD_CAT_TY = ''DP'' 
                                        AND G.CREATE_DT > SYSDATE-1
                                        AND L.APPL_ID = G.CREDIT_APPL_ID
                                        AND L.REC_ST = ''A''
                                        AND G.REC_ST = ''A'' '||V_FLT_TEXT;
            
            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_CONTACT;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, CONTACT, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_CONTACT, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_CONTACT;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    ELSIF (P_TYPE = 'BR') THEN
        BEGIN
            IF (P_FILTER_BY = 'AN') THEN
                BEGIN
                    V_SQL_TEXT := 'SELECT DISTINCT 
                                        A.ACCT_ID AS TXN_ID,
                                        B.BU_ID AS BU_ID,
                                        A.CUST_ID AS CUST_ID, 
                                        A.ACCT_NM AS ACCT_NM, 
                                        A.ACCT_NO AS ACCT_NO,
                                        NULL AS CONTRA_ACCT_NO,
                                        A.ACCT_NO AS CHG_ACCT,
                                        SYSDATE AS TRAN_DT,
                                        Y.CRNCY_CD AS CRNCY_CD,
                                        NULL AS TXN_AMT, 
                                        NULL AS TXN_CHG, 
                                        ''BROADCAST ALERT'' AS TRAN_DESC, 
                                        NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                                    FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                                    WHERE  
                                        A.ACCT_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CODE = '''||P_CODE||''' AND TRUNC(CREATE_DT) = TRUNC(SYSDATE))                                   
                                        AND A.CUST_ID = C.CUST_ID                                        
                                        AND B.BU_ID = A.MAIN_BRANCH_ID
                                        AND Y.CRNCY_ID = A.CRNCY_ID 
                                        AND A.REC_ST=''A'' AND A.PROD_CAT_TY=''DP'' '||V_FLT_TEXT;
                END;
            ELSE
                BEGIN
                    V_SQL_TEXT := 'SELECT DISTINCT 
                                        A.CUST_ID AS TXN_ID,
                                        B.BU_ID AS BU_ID,
                                        A.CUST_ID AS CUST_ID, 
                                        A.ACCT_NM AS ACCT_NM, 
                                        A.ACCT_NO AS ACCT_NO,
                                        NULL AS CONTRA_ACCT_NO,
                                        A.ACCT_NO AS CHG_ACCT,
                                        SYSDATE AS TRAN_DT,
                                        Y.CRNCY_CD AS CRNCY_CD,
                                        NULL AS TXN_AMT, 
                                        NULL AS TXN_CHG, 
                                        ''BROADCAST ALERT'' AS TRAN_DESC, 
                                        NVL((SELECT U.ACCESS_CD FROM NAWIRILIVE.CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = C.CUST_ID AND U.USER_CAT_CD=''PER'' AND U.CHANNEL_ID=9 AND U.REC_ST=''A'' AND ROWNUM=1),(SELECT O.CONTACT FROM NAWIRILIVE.V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = C.CUST_ID AND O.CONTACT_MODE_CAT_CD IN (''MOBPHONE'',''TELPHONE'') AND ROWNUM=1)) AS CONTACT
                                    FROM NAWIRILIVE.ACCOUNT A, NAWIRILIVE.CUSTOMER C, NAWIRILIVE.CURRENCY Y, NAWIRILIVE.BUSINESS_UNIT B
                                    WHERE 
                                        A.CUST_ID NOT IN (SELECT TXN_ID FROM CHANNELMANAGER.PHA_ALERTS WHERE TYPE = '''||P_TYPE||''' AND CODE = '''||P_CODE||''' AND TRUNC(CREATE_DT) = TRUNC(SYSDATE))                                
                                        AND A.CUST_ID = C.CUST_ID
                                        AND B.BU_ID = A.MAIN_BRANCH_ID
                                        AND Y.CRNCY_ID = A.CRNCY_ID 
                                        AND A.ACCT_NO = (SELECT MAX(X.ACCT_NO) FROM NAWIRILIVE.ACCOUNT X WHERE X.REC_ST=''A'' AND X.PROD_CAT_TY=''DP'' AND X.PROD_ID NOT IN (49,50) AND X.CUST_ID=C.CUST_ID GROUP BY X.CUST_ID)
                                        AND A.REC_ST=''A'' AND A.PROD_CAT_TY=''DP'' '||V_FLT_TEXT;
                END;
            END IF;

            OPEN DATA_CURSOR FOR V_SQL_TEXT;
                                
            FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_CONTACT;
            
            LOOP
                BEGIN
                    EXIT WHEN DATA_CURSOR%NOTFOUND;
                    
                    IF (LENGTH(V_CONTACT)>=9) THEN
                        BEGIN
                            INSERT INTO CHANNELMANAGER.PHA_ALERTS (REC_ID, CREATE_DT, CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, CONTRA, CHRG_ACCT, CHG_ID, TXN_DATE, CURRENCY, TXN_AMT, TXN_CHG, CHRG_AMT, TXN_DESC, CONTACT, REC_ST)
                            VALUES(CHANNELMANAGER.SEQ_PHA_ALERTS.NEXTVAL, SYSDATE, P_CODE, P_TYPE, V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, NULL, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, NULL, V_TXN_DESC, V_CONTACT, 'P');
                        END;
                    END IF;
                    
                    FETCH DATA_CURSOR INTO V_TXN_ID, V_BU_ID, V_CUST_ID, V_CUST_NAME, V_ACCT_NO, V_CONTRA, V_CHRG_ACCT, V_TXN_DATE, V_CURRENCY, V_TXN_AMT, V_TXN_CHG, V_TXN_DESC, V_CONTACT;
                END;
            END LOOP;

            CLOSE DATA_CURSOR;
         END;
    END IF;   

    COMMIT;
END;
/
