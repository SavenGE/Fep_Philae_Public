DROP VIEW NAWIRILIVE.V_PH_LOAN_PAYOFF_BAL;

/* Formatted on 15/05/2020 18:25:30 (QP5 v5.114.809.3010) */
CREATE OR REPLACE FORCE VIEW NAWIRILIVE.V_PH_LOAN_PAYOFF_BAL
(
   BU_NO,
   BU_NM,
   CUST_NM,
   ACCT_NO,
   ACCT_NM,
   PROD_DESC,
   CRNCY_CD_ISO,
   CLEARED_BAL,
   UNAPPLIED_FUNDS,
   PREPAID_INT_AMT,
   DR_INT_ACCRUED,
   CHARGE_DUE,
   PENDING_CHARGE,
   CYCLIC_CHARGE,
   TOTAL_ACCRUED_CHARGE,
   LATE_FEE_1,
   LATE_FEE_2,
   LATE_FEE_3,
   TOT_LATE_FEE,
   INSURANCE_1,
   INSURANCE_2,
   INSURANCE_3,
   TOT_INSURANCE,
   FUTURE_INTEREST,
   PAYOFF_AMOUNT
)
AS
     SELECT   i.bu_no,
              i.bu_nm,
              a.cust_nm,
              b.acct_no,
              b.ACCT_NM,
              d.PROD_DESC,
              e.CRNCY_CD_ISO,
              c.CLEARED_BAL,
              c.UNAPPLIED_FUNDS,
              c.PREPAID_INT_AMT,
              c.DR_INT_ACCRUED,
              DECODE (
                 c.CHRGS_DUE / ABS (DECODE (c.CHRGS_DUE, 0, 1, c.CHRGS_DUE)),
                 1,
                 c.CHRGS_DUE,
                 0
              )
                 AS Charge_Due,
              SUM (DECODE (f.rec_st, 'A', f.TRAN_AMT, 0)) AS Pending_charge,
              SUM (DECODE (g.rec_st, 'A', g.CHRG_AMT, 0)) AS Cyclic_charge,
              SUM (DECODE (f.rec_st, 'A', f.TRAN_AMT, 0))
              + SUM (DECODE (g.rec_st, 'A', g.CHRG_AMT, 0))
                 AS Total_accrued_Charge,
              SUM(DECODE (h.EVENT_TYPE,
                          'CHARGE', DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                          0))
                 AS late_fee_1,
              SUM(DECODE (h.EVENT_TYPE,
                          'CHARGE', DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                          0))
                 AS late_fee_2,
              SUM(DECODE (h.EVENT_TYPE,
                          'CHARGE', DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                          0))
                 AS late_fee_3,
              SUM(DECODE (h.EVENT_TYPE,
                          'CHARGE', DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                          0))
              + SUM(DECODE (h.EVENT_TYPE,
                            'CHARGE', DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                            0))
              + SUM(DECODE (h.EVENT_TYPE,
                            'CHARGE', DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                            0))
                 AS Tot_late_fee,
              SUM(DECODE (h.EVENT_TYPE,
                          'INSURANCE', DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                          0))
                 AS Insurance_1,
              SUM(DECODE (h.EVENT_TYPE,
                          'INSURANCE', DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                          0))
                 AS Insurance_2,
              SUM(DECODE (h.EVENT_TYPE,
                          'INSURANCE', DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                          0))
                 AS Insurance_3,
              SUM(DECODE (h.EVENT_TYPE,
                          'INSURANCE', DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                          0))
              + SUM(DECODE (h.EVENT_TYPE,
                            'INSURANCE',
                            DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                            0))
              + SUM(DECODE (h.EVENT_TYPE,
                            'INSURANCE',
                            DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                            0))
                 AS Tot_Insurance,
              CASE
                 WHEN (SELECT   LOAN_PAYOFF_FUTURE_INT_OPT
                         FROM   LOAN_PRODUCT_BASIC_INFO lpb
                        WHERE   lpb.prod_id = b.prod_id AND lpb.rec_st = 'A') =
                         'INT_EVNT'
                 THEN
                    NVL (
                       (SELECT   SUM (le.REPMNT_AMT)
                          FROM   ln_Acct_repmnt_event le
                         WHERE       le.acct_id = b.acct_id
                                 AND le.event_type = 'INTEREST'
                                 AND le.rec_st NOT IN ('C')
                                 AND le.due_dt >
                                       (SELECT   TO_DATE (display_value,
                                                          'dd/MM/yyyy')
                                          FROM   ctrl_parameter
                                         WHERE   param_cd = 'S02')),
                       0
                    )
                 ELSE
                    0
              END
                 AS future_interest,
              (  c.CLEARED_BAL
               + c.UNAPPLIED_FUNDS
               + c.PREPAID_INT_AMT
               - c.DR_INT_ACCRUED)
              - (DECODE (
                    c.CHRGS_DUE / ABS (DECODE (c.CHRGS_DUE, 0, 1, c.CHRGS_DUE)),
                    1,
                    c.CHRGS_DUE,
                    0
                 )
                 + SUM (DECODE (f.rec_st, 'A', f.TRAN_AMT, 0))
                 + SUM (DECODE (g.rec_st, 'A', g.CHRG_AMT, 0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'CHARGE',
                               DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                               0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'CHARGE',
                               DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                               0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'CHARGE',
                               DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                               0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'INSURANCE',
                               DECODE (h.rec_st, 'N', h.AMT_UNPAID, 0),
                               0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'INSURANCE',
                               DECODE (h.rec_st, 'P', h.AMT_UNPAID, 0),
                               0))
                 + SUM(DECODE (h.EVENT_TYPE,
                               'INSURANCE',
                               DECODE (h.rec_st, 'S', h.AMT_UNPAID, 0),
                               0))
                 + CASE
                      WHEN (SELECT   LOAN_PAYOFF_FUTURE_INT_OPT
                              FROM   LOAN_PRODUCT_BASIC_INFO lpb
                             WHERE   lpb.prod_id = b.prod_id
                                     AND lpb.rec_st = 'A') = 'INT_EVNT'
                      THEN
                         NVL (
                            (SELECT   SUM (le.REPMNT_AMT)
                               FROM   ln_Acct_repmnt_event le
                              WHERE       le.acct_id = b.acct_id
                                      AND le.event_type = 'INTEREST'
                                      AND le.rec_st NOT IN ('C')
                                      AND le.due_dt >
                                            (SELECT   TO_DATE (display_value,
                                                               'dd/MM/yyyy')
                                               FROM   ctrl_parameter
                                              WHERE   param_cd = 'S02')),
                            0
                         )
                      ELSE
                         0
                   END)
                 AS Payoff_amount
       FROM   Customer a,
              account b,
              loan_account_summary c,
              product d,
              currency e,
              PENDING_CHARGE f,
              ACCOUNT_CYCLIC_CHARGE g,
              LN_ACCT_REPMNT_EVENT h,
              business_unit i
      WHERE       a.cust_id = b.cust_id
              AND b.acct_id = c.acct_id
              AND b.prod_id = d.prod_id
              AND b.CRNCY_ID = e.crncy_id
              AND b.acct_id = f.acct_id(+)
              AND c.acct_id = g.acct_id(+)
              AND b.acct_id = h.acct_id(+)
              AND i.BU_ID = b.MAIN_BRANCH_ID
   GROUP BY   i.bu_no,
              i.bu_nm,
              a.cust_nm,
              b.acct_no,
              b.ACCT_NM,
              b.acct_id,
              b.prod_id,
              d.PROD_DESC,
              e.CRNCY_CD_ISO,
              c.CLEARED_BAL,
              c.UNAPPLIED_FUNDS,
              c.PREPAID_INT_AMT,
              c.DR_INT_ACCRUED,
              DECODE (
                 c.CHRGS_DUE / ABS (DECODE (c.CHRGS_DUE, 0, 1, c.CHRGS_DUE)),
                 1,
                 c.CHRGS_DUE,
                 0
              );


GRANT SELECT ON NAWIRILIVE.V_PH_LOAN_PAYOFF_BAL TO CHANNELMANAGER;

