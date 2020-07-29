SET DEFINE OFF;
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (33, 'A07', TO_DATE('05/13/2020 13:53:37', 'MM/DD/YYYY HH24:MI:SS'), 'GA', 'Guarantor Alert', 
    2, NULL, 'Any Time', '01', 'Real-Time', 
    NULL, TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/01/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AL', 
    'A');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (36, 'A08', TO_DATE('05/15/2020 18:11:53', 'MM/DD/YYYY HH24:MI:SS'), 'LF', 'Loan Default Alert', 
    2, NULL, 'Any Time', '01', 'Daily', 
    '7', TO_DATE('05/16/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/10/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'A');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (25, 'A01', TO_DATE('09/12/2017 15:26:59', 'MM/DD/YYYY HH24:MI:SS'), 'CR', 'Credit Alert', 
    2, NULL, 'Any Time', '01', 'Real-Time', 
    NULL, TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/01/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'L');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (26, 'A02', TO_DATE('09/12/2017 15:27:17', 'MM/DD/YYYY HH24:MI:SS'), 'DR', 'Debit Alert', 
    2, NULL, 'Any Time', '01', 'Real-Time', 
    NULL, TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/01/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'L');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (27, 'A03', TO_DATE('02/07/2018 11:51:39', 'MM/DD/YYYY HH24:MI:SS'), 'CV', 'Credit Reversal Alert', 
    2, NULL, 'Any Time', '02', 'Real-Time', 
    NULL, TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/01/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'A');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (28, 'A04', TO_DATE('02/07/2018 11:52:00', 'MM/DD/YYYY HH24:MI:SS'), 'DV', 'Debit Reversal Alert', 
    2, NULL, 'Any Time', '02', 'Real-Time', 
    NULL, TO_DATE('05/14/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/14/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/01/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'A');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (31, 'A05', TO_DATE('12/10/2018 14:14:29', 'MM/DD/YYYY HH24:MI:SS'), 'LD', 'Loan Due Alert', 
    2, NULL, 'Any Time', '01', 'Daily', 
    '0,3', TO_DATE('05/11/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/10/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/10/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'A');
Insert into PHA_ALERT
   (REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, 
    PRIORITY, MIN_AMT, RUN_TIME, CHRG_CODE, FREQUENCY, 
    DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, 
    REC_ST)
 Values
   (32, 'A06', TO_DATE('12/10/2018 14:14:44', 'MM/DD/YYYY HH24:MI:SS'), 'LA', 'Loan Arrears Alert', 
    2, NULL, 'Any Time', '01', 'Daily', 
    '7', TO_DATE('05/16/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('05/15/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('12/10/2035 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AP', 
    'A');
COMMIT;

SET DEFINE OFF;
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (104, 'SB', TO_DATE('04/27/2020 17:42:43', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Saving Balance Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/29/2020 11:08:00', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (105, 'CB', TO_DATE('04/27/2020 17:42:49', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Capital Balance Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:22:41', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (106, 'LB', TO_DATE('04/27/2020 17:42:52', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Loan Balance Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:22:29', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (107, 'NB', TO_DATE('04/27/2020 17:42:54', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'NWD Balance Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:23:17', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (108, 'MS', TO_DATE('04/27/2020 17:42:57', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'MiniStatement Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/29/2020 11:08:21', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (109, 'DP', TO_DATE('04/27/2020 17:42:59', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Deposit Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('05/01/2020 10:53:41', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (110, 'WD', TO_DATE('04/27/2020 17:43:01', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Withdrawal Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/27/2020 17:43:01', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (111, 'AT', TO_DATE('04/27/2020 17:43:03', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Airtime Purchase Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/29/2020 11:07:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (112, 'TR', TO_DATE('04/27/2020 17:43:06', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Fund Transfer Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/29/2020 11:08:48', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (113, 'SR', TO_DATE('04/27/2020 17:43:08', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Statement Request Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:18:50', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (114, 'AL', TO_DATE('04/27/2020 17:43:10', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Apply Loan Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:27:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (115, 'VC', TO_DATE('04/27/2020 17:43:12', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Vervee Cash Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:08:13', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (116, 'GT', TO_DATE('04/27/2020 17:43:14', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'GOTV Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:25:13', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (117, 'KP', TO_DATE('04/27/2020 17:43:18', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Kenya Power Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:24:15', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (118, 'KW', TO_DATE('04/27/2020 17:43:21', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Kisumu Water Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:23:37', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (119, 'NW', TO_DATE('04/27/2020 17:43:23', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Nairobi Water Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:20:18', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (120, 'NH', TO_DATE('04/27/2020 17:43:26', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'NHIF Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:20:51', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (121, 'ZK', TO_DATE('04/27/2020 17:43:28', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'ZUKU Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:17:51', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (122, 'MC', TO_DATE('04/27/2020 17:43:31', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'MultiChoice Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:22:07', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (123, 'SM', TO_DATE('04/27/2020 17:43:34', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'StarTimes Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:19:07', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (124, 'PL', TO_DATE('04/27/2020 17:43:36', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'PesaLink Transfer Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:19:47', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (125, 'MD', TO_DATE('04/27/2020 17:43:39', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Mpesa Deposit Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/27/2020 17:43:39', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (126, 'DL', TO_DATE('04/27/2020 17:43:41', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Deposit To Loan Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:25:45', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (127, 'TL', TO_DATE('04/27/2020 17:43:45', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Transfer To Loan Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'Consultant Neptune', TO_DATE('04/29/2020 11:09:08', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (46, 'ML', TO_DATE('01/23/2018 14:10:26', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Mpesa To Loan Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:21:47', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (132, 'SF', TO_DATE('05/01/2020 14:06:41', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Safaricom Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:19:21', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (128, '01', TO_DATE('04/29/2020 13:21:42', 'MM/DD/YYYY HH24:MI:SS'), 'E01', 'Statement Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'P', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 16:03:51', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (129, '01', TO_DATE('04/29/2020 13:22:10', 'MM/DD/YYYY HH24:MI:SS'), 'S01', 'SMS Alert Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:28:33', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (130, '02', TO_DATE('04/29/2020 13:25:15', 'MM/DD/YYYY HH24:MI:SS'), 'S01', 'Zero Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:28:41', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (131, '02', TO_DATE('04/29/2020 13:27:06', 'MM/DD/YYYY HH24:MI:SS'), 'E01', 'Zero Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'P', 'Consultant Neptune', TO_DATE('04/29/2020 13:27:43', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (133, 'KL', TO_DATE('05/01/2020 14:07:22', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'KPLC Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:24:51', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
Insert into PHC_CHARGE
   (REC_ID, REC_CD, CREATE_DT, SCHEME, DESCRIPTION, 
    ACCOUNT, LEDGER, BASIS, SYS_USER, SYS_DATE, 
    REC_ST)
 Values
   (134, 'OP', TO_DATE('05/01/2020 14:07:26', 'MM/DD/YYYY HH24:MI:SS'), 'V01', 'Other Payment Charge', 
    'S', '1-01-***-OPS-4213-4213004', 'T', 'BENJAMIN KITUNGU', TO_DATE('05/07/2020 13:20:01', 'MM/DD/YYYY HH24:MI:SS'), 
    'A');
COMMIT;

SET DEFINE OFF;
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (699, '494', 'E', 'Statement Request Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (700, '495', 'E', 'StarTimes Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (709, '499', 'E', 'Nairobi Water Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (716, '503', 'C', 'Loan Balance Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (718, '504', 'E', 'Capital Balance Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (722, '506', 'C', 'Kisumu Water Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (723, '506', 'E', 'Kisumu Water Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (728, '509', 'E', 'GOTV Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (731, '510', 'E', 'Deposit To Loan Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (695, '491', 'C', 'Vervee Cash Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (698, '494', 'C', 'Statement Request Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (701, '495', 'C', 'StarTimes Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (702, '496', 'E', 'Safaricom Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (703, '496', 'C', 'Safaricom Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (704, '497', 'C', 'PesaLink Transfer Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (707, '498', 'C', 'Other Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (708, '499', 'C', 'Nairobi Water Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (710, '500', 'C', 'NHIF Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (711, '500', 'E', 'NHIF Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (714, '502', 'E', 'MultiChoice Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (724, '507', 'C', 'Kenya Power Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (725, '507', 'E', 'Kenya Power Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (726, '508', 'E', 'KPLC Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (727, '508', 'C', 'KPLC Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (736, '513', 'C', 'Zero Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (694, '491', 'E', 'Vervee Cash Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (712, '501', 'E', 'Mpesa To Loan Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (713, '501', 'C', 'Mpesa To Loan Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (715, '502', 'C', 'MultiChoice Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (729, '509', 'C', 'GOTV Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (734, '512', 'C', 'SMS Alert Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (737, '513', 'E', 'Zero Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (717, '503', 'E', 'Loan Balance Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (720, '505', 'C', 'NWD Balance Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (721, '505', 'E', 'NWD Balance Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (733, '511', 'E', 'Apply Loan Commission', '1-01-100-FIN-1212-1212018', 
    'C', 50);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (735, '512', 'E', 'SMS Alert Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (647, '467', 'E', 'MiniStatement Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (648, '467', 'C', 'MiniStatement Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (649, '468', 'E', 'Fund Transfer Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (650, '468', 'C', 'Fund Transfer Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (651, '469', 'C', 'Transfer To Loan Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (652, '469', 'E', 'Transfer To Loan Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (719, '504', 'C', 'Capital Balance Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (730, '510', 'C', 'Deposit To Loan Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (732, '511', 'C', 'Apply Loan Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (738, '515', 'C', 'Statement Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (739, '515', 'E', 'Statement Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (611, '459', 'C', 'Mpesa Deposit Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (612, '459', 'E', 'Mpesa Deposit Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (706, '498', 'E', 'Other Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (643, '465', 'C', 'Airtime Purchase Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (644, '465', 'E', 'Airtime Purchase Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (645, '466', 'E', 'Saving Balance Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (678, '483', 'C', 'Deposit Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (679, '483', 'E', 'Deposit Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (705, '497', 'E', 'PesaLink Transfer Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (696, '493', 'C', 'ZUKU Payment Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (697, '493', 'E', 'ZUKU Payment Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (635, '444', 'E', 'Withdrawal Commission', '1-01-100-FIN-1212-1212018', 
    'C', 10);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (636, '444', 'C', 'Withdrawal Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (646, '466', 'C', 'Saving Balance Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (673, '480', 'E', 'Zero Commission', '1-01-100-FIN-1212-1212018', 
    'C', 0);
Insert into PHC_DEDUCTION
   (REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, 
    TYPE, VALUE)
 Values
   (674, '480', 'C', 'Zero Excise Duty', '1-01-***-OPS-2216-2216009', 
    'P', 12);
COMMIT;

SET DEFINE OFF;
Insert into PHC_SCHEME
   (REC_CD, CREATE_DT, MODULE, DESCRIPTION, SYS_USER, 
    SYS_DATE, REC_ST)
 Values
   ('E01', TO_DATE('02/22/2018 15:35:23', 'MM/DD/YYYY HH24:MI:SS'), 'EST', 'Mailer Charges', 'Consultant Neptune', 
    TO_DATE('04/29/2020 15:06:18', 'MM/DD/YYYY HH24:MI:SS'), 'A');
Insert into PHC_SCHEME
   (REC_CD, CREATE_DT, MODULE, DESCRIPTION, SYS_USER, 
    SYS_DATE, REC_ST)
 Values
   ('S01', TO_DATE('08/21/2017 18:08:11', 'MM/DD/YYYY HH24:MI:SS'), 'SMS', 'SMS Charges', 'Consultant Neptune', 
    TO_DATE('08/21/2017 18:08:11', 'MM/DD/YYYY HH24:MI:SS'), 'A');
Insert into PHC_SCHEME
   (REC_CD, CREATE_DT, MODULE, DESCRIPTION, SYS_USER, 
    SYS_DATE, REC_ST)
 Values
   ('V01', TO_DATE('08/02/2017 10:37:41', 'MM/DD/YYYY HH24:MI:SS'), 'VMA', 'Mobile Charges', 'BENJAMIN KITUNGU', 
    TO_DATE('05/07/2020 13:07:23', 'MM/DD/YYYY HH24:MI:SS'), 'A');
COMMIT;

SET DEFINE OFF;
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (502, '122', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (505, '107', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (508, '133', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (510, '126', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (513, '130', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (494, '113', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (498, '134', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (499, '119', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (500, '120', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (503, '106', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (501, '46', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (512, '129', 'KES', 'C', 11.2, 
    11.2, 11.2);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (491, '115', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (504, '105', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (507, '117', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (509, '116', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (511, '114', 'KES', 'C', 56, 
    56, 56);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (467, '108', 'KES', 'C', 33.6, 
    33.6, 33.6);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (468, '112', 'KES', 'C', 11.2, 
    11.2, 11.2);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (469, '127', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (506, '118', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (444, '110', 'KES', 'C', 76, 
    76, 76);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (496, '132', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (493, '121', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (515, '128', 'KES', 'C', 11.2, 
    11.2, 11.2);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (497, '124', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (459, '125', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (495, '123', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (466, '104', 'KES', 'C', 22.4, 
    22.4, 22.4);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (465, '111', 'KES', 'C', 11.2, 
    11.2, 11.2);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (483, '109', 'KES', 'C', 0, 
    0, 0);
Insert into PHC_VALUE
   (REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, 
    MAX_VALUE, VALUE)
 Values
   (480, '131', 'KES', 'C', 0, 
    0, 0);
COMMIT;

SET DEFINE OFF;
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (123, 'VMA', 'MpesaTokenUrl', 'https://api.safaricom.co.ke/oauth/v1/generate', 'Mpesa Token Url', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (124, 'VMA', 'MpesaRegisterUrl', 'https://api.safaricom.co.ke/mpesa/c2b/v1/registerurl', 'Mpesa Register Url', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (125, 'VMA', 'MpesaConfirmationUrl', 'https://197.248.86.106:8900/vma/cb', 'Mpesa Confirmation Url', 
    'Consultant Neptune', TO_DATE('04/29/2020 11:03:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (126, 'VMA', 'MpesaValidationUrl', 'https://197.248.86.106:8900/vma/cv', 'Mpesa Validation Url', 
    'Consultant Neptune', TO_DATE('04/29/2020 11:04:27', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (127, 'VMA', 'UpdateMpesaUrl', 'No', 'Update Mpesa Url', 
    'SYSTEM', TO_DATE('05/01/2020 13:46:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (128, 'VMA', 'MinistatementTxnCount', '5', 'Ministatement Txn Count', 
    'SYSTEM', TO_DATE('05/01/2020 13:46:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (136, 'VMA', 'LoanTwoTerm', '30', 'Loan Two Term', 
    'SYSTEM', TO_DATE('05/12/2020 09:40:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (130, 'VMA', 'CreditCustomScreen', 'CREDIT_APP', 'Credit Custom Screen', 
    'SYSTEM', TO_DATE('05/07/2020 11:17:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (131, 'VMA', 'ApproveApplicationEventId', '1008848', 'Approve Application Event Id', 
    'SYSTEM', TO_DATE('05/07/2020 11:17:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (135, 'VMA', 'LoanOneTerm', '30', 'Loan One Term', 
    'SYSTEM', TO_DATE('05/12/2020 09:40:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (137, 'VMA', 'LoanThreeTerm', '180', 'Loan Three Term', 
    'SYSTEM', TO_DATE('05/12/2020 09:40:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (132, 'EST', 'AddressFieldId', '75', 'Address Field Id', 
    'SYSTEM', TO_DATE('05/07/2020 11:55:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (133, 'EST', 'SslProtocols', 'TLSv1.2', 'Ssl Protocols', 
    'SYSTEM', TO_DATE('05/08/2020 17:00:28', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (134, 'EST', 'TransportProtocol', 'smtps', 'Transport Protocol', 
    'SYSTEM', TO_DATE('05/08/2020 17:00:28', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (145, 'VMA', 'LoanOneProducts', '289', 'Loan One Products', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (146, 'VMA', 'LoanTwoProducts', '280', 'Loan Two Products', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (147, 'VMA', 'LoanThreeProducts', '279', 'Loan Three Products', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (148, 'VMA', 'LoanTwoIncomeMonths', '6', 'Loan Two Income Months', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (149, 'VMA', 'LoanOneRecoveryDay', '30', 'Loan One Recovery Day', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (150, 'VMA', 'LoanTwoRecoveryDay', '30', 'Loan Two Recovery Day', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (151, 'VMA', 'LoanThreeRecoveryDay', '30', 'Loan Three Recovery Day', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (152, 'VMA', 'LoanThreeBalanceMonth', '1', 'Loan Three Balance Month', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (153, 'VMA', 'LoanDefaultPardonDays', '180', 'Loan Default Pardon Days', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (154, 'VMA', 'LoanOneDepositMonths', '3', 'Loan One Deposit Months', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (155, 'VMA', 'LoanTwoDepositMonths', '6', 'Loan Two Deposit Months', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (156, 'VMA', 'LoanThreeDepositMonths', '0', 'Loan Three Deposit Months', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (157, 'VMA', 'LoanOneTermCode', 'D', 'Loan One Term Code', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (158, 'VMA', 'LoanTwoTermCode', 'D', 'Loan Two Term Code', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (159, 'VMA', 'LoanThreeTermCode', 'D', 'Loan Three Term Code', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (160, 'VMA', 'LoanOneMaximumAmount', '5000', 'Loan One Maximum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (161, 'VMA', 'LoanOneMinimumAmount', '500', 'Loan One Minimum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (162, 'VMA', 'LoanTwoMaximumAmount', '5000', 'Loan Two Maximum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (163, 'VMA', 'LoanTwoMinimumAmount', '500', 'Loan Two Minimum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (164, 'VMA', 'LoanThreeMaximumAmount', '5000', 'Loan Three Maximum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (165, 'VMA', 'LoanThreeMinimumAmount', '500', 'Loan Three Minimum Amount', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (166, 'VMA', 'LoanOneNwdBalance', '5000', 'Loan One Nwd Balance', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (167, 'VMA', 'LoanOneNwdPercentage', '50', 'Loan One Nwd Percentage', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (168, 'VMA', 'LoanTwoNwdBalance', '5000', 'Loan Two Nwd Balance', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (169, 'VMA', 'LoanTwoNwdPercentage', '33.333', 'Loan Two Nwd Percentage', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (170, 'VMA', 'LoanThreeNwdBalance', '5000', 'Loan Three Nwd Balance', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (171, 'VMA', 'LoanThreeNwdPercentage', '25', 'Loan Three Nwd Percentage', 
    'SYSTEM', TO_DATE('05/12/2020 16:20:33', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (172, 'VMA', 'LoanTwoForbidProducts', '[]', 'Loan Two Forbid Products', 
    'BENJAMIN KITUNGU', TO_DATE('05/15/2020 17:34:29', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (1, 'SMS', 'ChannelId', '9', 'Channel Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (2, 'SMS', 'SystemUserId', '-99', 'System User Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (3, 'SMS', 'SchemeId', '12', 'Scheme Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (4, 'SMS', 'SendAlertsURL', 'http://api.prsp.tangazoletu.com/', 'Send Alerts URL', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (5, 'SMS', 'WhiteList', '*', 'White List', 
    'BENJAMIN KITUNGU', TO_DATE('05/09/2020 17:36:40', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (6, 'SMS', 'UserId', '1392', 'User Id', 
    'BENJAMIN KITUNGU', TO_DATE('05/12/2020 19:36:31', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (7, 'SMS', 'PassKey', '681ELT5DWW', 'Pass Key', 
    'BENJAMIN KITUNGU', TO_DATE('05/12/2020 18:38:39', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (8, 'SMS', 'Service', '1', 'Service', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (9, 'SMS', 'Sender', 'NAWIRISACCO', 'Sender', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (12, 'SMS', 'MinimumAlertAmount', '200', 'Minimum Alert Amount', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (13, 'SMS', 'ChargeScheme', 'S01', 'Charge Scheme', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (14, 'SMS', 'AlertType', 'Notification', 'Alert Type', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (15, 'VMA', 'HomeContext', '/vma/home', 'Home Context', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (16, 'VMA', 'DisburseNetOfFee', 'No', 'Disburse Net Of Fee', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (17, 'VMA', 'RetryCount', '3', 'Retry Count', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (18, 'VMA', 'TimeoutMinutes', '5', 'Timeout Minutes', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (20, 'VMA', 'MaxClientSessions', '200', 'Max Client Sessions', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (22, 'VMA', 'ServerPort', '8900', 'Server Port', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (23, 'VMA', 'ChannelId', '9', 'Channel Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (24, 'VMA', 'SchemeId', '12', 'Scheme Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (25, 'VMA', 'SystemUserId', '-99', 'System User Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (26, 'VMA', 'CustomerTypeId', '771', 'Customer Type Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (27, 'VMA', 'RelationshipOfficerId', '1020', 'Relationship Officer Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (28, 'VMA', 'NationalIdentityId', '391', 'National Identity Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (29, 'VMA', 'IndustryId', '2200', 'Industry Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (30, 'VMA', 'SecureSocketProtocol', 'TLSv1.2', 'Secure Socket Protocol', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (31, 'VMA', 'KeyFactoryAlgorithm', 'SunX509', 'Key Factory Algorithm', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (32, 'VMA', 'KeystoreFile', 'vma/cert/philae.ks', 'Keystore File', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (33, 'VMA', 'KeystorePassword', '2DERAbnw0qs=', 'Keystore Password', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (34, 'VMA', 'KeystoreType', 'PKCS12', 'Keystore Type', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (35, 'VMA', 'Locale', 'en_US', 'Locale', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (36, 'VMA', 'VmaWsContext', '/vma/vserve', 'Vma Ws Context', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (37, 'VMA', 'SourceOfFundId', '315', 'Source Of Fund Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (38, 'VMA', 'CountryId', '689', 'Country Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (39, 'VMA', 'MarketingCampaignId', '352', 'Marketing Campaign Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (40, 'VMA', 'UsersSpoolTime', '2020-04-22 14:57:52', 'Users Spool Time', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (41, 'VMA', 'EnableHttps', 'Yes', 'Enable Https', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (42, 'VMA', 'CustomerCategory', 'PER', 'Customer Category', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (43, 'VMA', 'MpesaDepositContext', '/vma/cb', 'Paybill Context', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (44, 'VMA', 'ShareProducts', '49', 'Share Products', 
    'Consultant Neptune', TO_DATE('04/27/2020 11:13:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (45, 'VMA', 'SavingProducts', '[]', 'Saving Products', 
    'Consultant Neptune', TO_DATE('04/27/2020 11:16:10', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (46, 'VMA', 'NwdProducts', '49', 'Non-Withdrawable Products', 
    'Consultant Neptune', TO_DATE('04/27/2020 11:17:27', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (48, 'VMA', 'PropertyTypeId', '392', 'Property Type Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (49, 'VMA', 'TaxGroupId', '382', 'Tax Group Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (50, 'VMA', 'OpenningReasonId', '594', 'Openning Reason Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (51, 'VMA', 'AddressTypeId', '61', 'Address Type Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (52, 'VMA', 'CustomerSegmentId', '371', 'Customer Segment Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (53, 'VMA', 'NationalityId', '221', 'Nationality Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (54, 'VMA', 'MobileContactModeId', '237', 'Mobile Contact Mode Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (55, 'VMA', 'CreditRatingAgencyId', '11', 'Credit Rating Agency Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (56, 'VMA', 'CertificateAlias', 'philae', 'Certificate Alias', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (57, 'VMA', 'DefaultRegion', 'Embu', 'Default Region', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (58, 'VMA', 'DefaultDistrict', 'Embu', 'Default District', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (59, 'VMA', 'MissTitleId', '263', 'Miss Title Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (60, 'VMA', 'MsTitleId', '262', 'Ms Title Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (61, 'VMA', 'MrTitleId', '261', 'Mr Title Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (62, 'VMA', 'CreditTypeId', '52', 'Credit Type Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (63, 'VMA', 'CreditPortfolioId', '11', 'Credit Portfolio Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (64, 'VMA', 'EmailContactModeId', '235', 'Email Contact Mode Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (65, 'VMA', 'PurposeOfCreditId', '477', 'Purpose Of Credit Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (66, 'VMA', 'RiskClassId', '552', 'Risk Class Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (67, 'VMA', 'TaxStatusId', '441', 'Tax Status Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (68, 'VMA', 'RiskCode', '200', 'Risk Code', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (69, 'VMA', 'ServiceName', 'VMA', 'Service Name', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (70, 'VMA', 'MpesaValidatorContext', '/vma/cv', 'Mpesa Validator Context', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (73, 'VMA', 'ChargeScheme', 'V01', 'Charge Scheme', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (75, 'VMA', 'Operator', 'Spotcash', 'Operator', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:24', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (76, 'VMA', 'DefaultProductId', '33', 'Default Product Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (77, 'VMA', 'ApproveAccountEventId', '1007783', 'Approve Account Event Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (78, 'VMA', 'BlockReasonId', '761', 'Block Reason Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (79, 'VMA', 'MpesaDepositGL', '1-01-100-FIN-2224-2224095', 'Paybill Deposit GL', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (80, 'VMA', 'MpesaSuspenseGL', '1-01-100-FIN-2224-2224096', 'Paybill Suspense GL', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (81, 'VMA', 'SpotcashSettlementAccount', '1-01-100-FIN-1212-1212018', 'Spotcash Settlement Account', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (83, 'EST', 'ChannelId', '15', 'Channel Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (84, 'EST', 'SystemUserId', '-99', 'System User Id', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (92, 'EST', 'EmailSubject', 'Nawiri Account Statement', 'Email Subject', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (93, 'EST', 'ChargeScheme', 'E01', 'Charge Scheme', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (94, 'EST', 'SenderAlias', 'NAWIRI', 'Sender Alias', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (95, 'EST', 'SmtpHost', 'smtp.gmail.com', 'Smtp Host', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (96, 'EST', 'StatementFileName', 'Nawiri Account Statement', 'Statement File Name', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (97, 'EST', 'SslTrust', '*', 'Ssl Trust', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (98, 'EST', 'SmtpUsername', 'nawirisaccoestatement@gmail.com', 'Smtp Username', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (99, 'EST', 'SmtpPassword', '4TNFqq3nLb6XjPRdV2Bnfg==', 'Smtp Password', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (100, 'EST', 'SmtpAuth', 'true', 'Smtp Auth', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (101, 'EST', 'StarttlsEnable', 'false', 'Starttls Enable', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (102, 'EST', 'SslEnable', 'true', 'Ssl Enable', 
    'SYSTEM', TO_DATE('04/24/2020 17:25:25', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (103, 'EST', 'WhiteList', '*', 'White List', 
    'Consultant Neptune', TO_DATE('04/29/2020 15:43:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (109, 'SMS', 'CreditLimitFieldId', '65', 'Credit Limit Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (110, 'SMS', 'DebitLimitFieldId', '66', 'Debit Limit Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (111, 'EST', 'CycleFieldId', '68', 'Cycle Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (112, 'EST', 'SentDateFieldId', '76', 'Sent Date Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (113, 'EST', 'StartDateFieldId', '69', 'Start Date Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (114, 'EST', 'EndDateFieldId', '70', 'End Date Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (115, 'EST', 'NowFieldId', '67', 'Now Field Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (116, 'EST', 'YesValueId', '154', 'Yes Value Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (117, 'EST', 'NoValueId', '153', 'No Value Id', 
    'SYSTEM', TO_DATE('04/27/2020 11:11:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (119, 'VMA', 'MpesaConsumerKey', 'm5AhlOLqAu6lhAbAtTcLbOHGrKLDEYcc', 'Mpesa Consumer Key', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (120, 'VMA', 'MpesaPaybillNumber', '505368', 'Mpesa Paybill Number', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (121, 'VMA', 'MpesaConsumerSecret', '8eQtez9bD7Ex4oGo', 'Mpesa Consumer Secret', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
Insert into PHL_SETTING
   (REC_ID, MODULE, CODE, VALUE, DESCRIPTION, 
    SYS_USER, SYS_DATE)
 Values
   (122, 'VMA', 'MpesaTimeoutAction', 'Completed', 'Mpesa Timeout Action', 
    'SYSTEM', TO_DATE('04/29/2020 10:48:43', 'MM/DD/YYYY HH24:MI:SS'));
COMMIT;

SET DEFINE OFF;
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (113, 'A05', 'SMS', 'KW', 'Dear {NAME}, Your loan is due for repayment on {DATE}, Amount: {CURRENCY} {AMOUNT}. Balance is {CURRENCY} {BALANCE}.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (122, 'A02', 'SMS', 'EN', 'Dear {NAME}, {CURRENCY} {AMOUNT} has been debited from your account No: {ACCOUNT} on: {DATE}. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (112, 'A05', 'SMS', 'EN', 'Dear {NAME}, Your loan is due for repayment on {DATE}, Amount: {CURRENCY} {AMOUNT}. Balance is {CURRENCY} {BALANCE}.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (123, 'A02', 'SMS', 'KW', 'Dear {NAME}, {CURRENCY} {AMOUNT} has been debited from your account No: {ACCOUNT} on: {DATE}. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (136, 'A01', 'SMS', 'EN', 'Dear {NAME}, {CURRENCY} {AMOUNT} has been credited to your account No: {ACCOUNT} on: {DATE}. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (126, 'A04', 'SMS', 'EN', 'Dear {NAME}, {CURRENCY} {AMOUNT} debited from your account No: {ACCOUNT} on: {DATE} has been reversed. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (127, 'A04', 'SMS', 'KW', 'Dear {NAME}, {CURRENCY} {AMOUNT} debited from your account No: {ACCOUNT} on: {DATE} has been reversed. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (137, 'A01', 'SMS', 'KW', 'Dear {NAME}, {CURRENCY} {AMOUNT} has been credited to your account No: {ACCOUNT} on: {DATE}. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (97, 'A03', 'SMS', 'EN', 'Dear {NAME}, {CURRENCY} {AMOUNT} credited to your account No: {ACCOUNT} on: {DATE} has been reversed. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (99, 'A03', 'SMS', 'KW', 'Dear {NAME}, {CURRENCY} {AMOUNT} credited to your account No: {ACCOUNT} on: {DATE} has been reversed. Detail: {DESCRIPTION}, Balance: {CURRENCY} {BALANCE}');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (141, 'A08', 'SMS', 'EN', 'Dear {NAME}, Your have defaulted on your loan which was to be cleared by {DATE}. Loan balance is {CURRENCY} {BALANCE}. Please pay to avoid recovery penalties.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (142, 'A08', 'SMS', 'KW', 'Dear {NAME}, Your have defaulted on your loan which was to be cleared by {DATE}. Loan balance is {CURRENCY} {BALANCE}. Please pay to avoid recovery penalties.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (115, 'A06', 'SMS', 'KW', 'Dear {NAME}, Your loan is now in arrears of {CURRENCY} {AMOUNT} by {DAYS} days. Balance is {CURRENCY} {BALANCE}. Please pay to avoid penalties.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (128, 'A07', 'SMS', 'EN', 'Dear {NAME}, You have guaranteed {BENEFICIARY} for a loan application of {CURRENCY} {AMOUNT} on {DATE}.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (114, 'A06', 'SMS', 'EN', 'Dear {NAME}, Your loan is now in arrears of {CURRENCY} {AMOUNT} by {DAYS} days. Balance is {CURRENCY} {BALANCE}. Please pay to avoid penalties.');
Insert into PHL_TEMPLATE
   (REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE)
 Values
   (129, 'A07', 'SMS', 'KW', 'Dear {NAME}, You have guaranteed {BENEFICIARY} for a loan application of {CURRENCY} {AMOUNT} on {DATE}.');
COMMIT;

SET DEFINE OFF;
Insert into PHS_TASK
   (REC_ID, REC_CD, CREATE_DT, DOCUMENT, RANGE, 
    DESCRIPTION, RUN_TIME, CHARGE, CYCLE, NEXT_DATE, 
    LAST_DATE, EXPIRY_DATE, FILTER_BY, REC_ST)
 Values
   (1, 'S01', TO_DATE('03/01/2018 18:23:08', 'MM/DD/YYYY HH24:MI:SS'), 'LP', 'Y', 
    'One-Off Statement', 'A', '01', 'O', TO_DATE('05/14/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 
    TO_DATE('05/14/2020 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), TO_DATE('03/01/2040 00:00:00', 'MM/DD/YYYY HH24:MI:SS'), 'AC', 'A');
COMMIT;
