/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae;

import PHilae.acx.APLog;
import PHilae.acx.ATBox;
import PHilae.acx.AXConstant;
import PHilae.acx.AXCypher;
import PHilae.model.AXTxn;
import PHilae.acx.AXWorker;
import PHilae.acx.PLAdapter;
import PHilae.enu.LNType;
import PHilae.est.ESController;
import PHilae.model.ACProduct;
import PHilae.model.AXBank;
import PHilae.model.AXChannel;
import PHilae.model.AXCharge;
import PHilae.model.AXPayment;
import PHilae.model.TCScheme;
import PHilae.model.AXSetting;
import PHilae.model.AXSplit;
import PHilae.model.CNActivity;
import PHilae.model.AXTier;
import PHilae.model.TCWaiver;
import PHilae.model.TCValue;
import PHilae.model.CFValue;
import PHilae.model.CNAccount;
import PHilae.model.CNBranch;
import PHilae.model.CNCurrency;
import PHilae.model.CNCustomer;
import PHilae.model.CNUser;
import PHilae.model.CLItem;
import PHilae.model.AXTerminal;
import PHilae.model.AXUser;
import PHilae.model.CAEvent;
import PHilae.model.CNScheme;
import PHilae.model.GPMember;
import PHilae.model.LNDetail;
import PHilae.model.MXAlert;
import PHilae.model.MXMessage;
import PHilae.model.TCDeduction;
import PHilae.model.BNUser;
import PHilae.model.CMChannel;
import PHilae.model.CMImage;
import PHilae.model.CNCountry;
import PHilae.model.CNIdentity;
import PHilae.model.DHRecord;
import PHilae.model.ESRecord;
import PHilae.model.ESTask;
import PHilae.model.LHRecord;
import PHilae.model.MURecord;
import PHilae.model.RFFriend;
import PHilae.model.RLCustomer;
import PHilae.model.TLDrawer;
import PHilae.model.URLimit;
import PHilae.model.USRole;
import PHilae.model.WFRecord;
import PHilae.sms.ALController;
import PHilae.vma.VXCaller;
import PHilae.vma.VXController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;
import oracle.ucp.UniversalConnectionPoolException;

/**
 *
 * @author Pecherk
 */
public final class DBPClient implements Serializable {

    private ATBox box;
    private static boolean wait = false;

    public DBPClient(ATBox box) {
        setBox(box);
    }

    public int getRowCount(ResultSet rs) {
        int records = 0;
        try {
            rs.last();
            records = rs.getRow();
            rs.beforeFirst();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return records;
    }

    public Object[][] executeQuery(String query) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, query)) {
            int row = 0, records = getRowCount(rs), fields = rs.getMetaData().getColumnCount();
            Object[][] results = (records == 0) ? new Object[0][0] : new Object[records][fields];
            while (rs.next()) {
                for (int col = 0; col < fields; col++) {
                    results[row][col] = rs.getObject(col + 1);
                }
                row++;
            }
            return results;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new Object[0][0];
    }

    public ResultSet executeQuery(final Statement statement, String query) throws SQLException {
        Long startTime = System.currentTimeMillis();
        //getLog().logDebug("query", query, getWorker().prepareDuration(startTime));
        ResultSet rs = statement.executeQuery(query);
        getLog().logDebug("query", query, getWorker().prepareDuration(startTime));
        return rs;
    }

    public boolean executeUpdate(String update) {
        return executeUpdate(update, false);
    }

    public boolean executeUpdate(String update, boolean unlimited) {
        Long startTime = System.currentTimeMillis();
        try (PLAdapter adapter = getAdapter(unlimited); Statement statement = createStatement(adapter.getConnection())) {
            statement.executeUpdate(update = cleanUpdate(update));
            getLog().logDebug("update", update, getWorker().prepareDuration(startTime));
            return true;
        } catch (Exception ex) {
            getLog().logEvent(update, ex);
        }
        return false;
    }

    public CNAccount unmaskLedger(String glAccount, CNBranch cNBranch) {
        CNAccount cNAccount = new CNAccount();
        if (!getWorker().isBlank(glAccount)) {
            glAccount = glAccount.trim();
            cNAccount = (glAccount.contains("***") && cNBranch.getGlPrefix() != null) ? queryLedgerAccount(cNBranch.getGlPrefix() + glAccount.substring(glAccount.indexOf("***") + 3)) : queryLedgerAccount(glAccount);
        }
        return cNAccount;
    }

    public CNBranch queryChannelBranch(Long channelId) {
        CNBranch cNBranch = new CNBranch();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT ORIGIN_BU_ID FROM " + APController.coreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelId)) {
            if (rs != null && rs.next()) {
                cNBranch = queryBranch(rs.getLong("ORIGIN_BU_ID"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNBranch;
    }

    public CNIdentity queryIdentity(Long custId, Long idType) {
        CNIdentity cNIdentity = new CNIdentity();
        if (custId != null) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_IDENT_ID, CUST_ID, IDENT_ID, IDENT_NO, IDENT_DESC, REC_ST FROM " + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION WHERE CUST_ID=" + custId + " AND IDENT_ID=" + idType + " AND REC_ST='A'")) {
                if (rs != null && rs.next()) {
                    cNIdentity.setCustIdentId(rs.getLong("CUST_IDENT_ID"));
                    cNIdentity.setCustId(rs.getLong("CUST_ID"));
                    cNIdentity.setIdentityId(rs.getLong("IDENT_ID"));
                    cNIdentity.setIdentityNumber(rs.getString("IDENT_NO"));
                    cNIdentity.setDescription(rs.getString("IDENT_DESC"));
                    cNIdentity.setStatus(rs.getString("REC_ST"));
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return cNIdentity;
    }

    public CNIdentity queryIdentity(Long idType, String identity) {
        CNIdentity cNIdentity = new CNIdentity();
        if (identity != null) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_IDENT_ID, CUST_ID, IDENT_ID, IDENT_NO, IDENT_DESC, REC_ST FROM " + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION WHERE IDENT_NO='" + identity + "' AND IDENT_ID=" + idType + " AND REC_ST='A'")) {
                if (rs != null && rs.next()) {
                    cNIdentity.setCustIdentId(rs.getLong("CUST_IDENT_ID"));
                    cNIdentity.setCustId(rs.getLong("CUST_ID"));
                    cNIdentity.setIdentityId(rs.getLong("IDENT_ID"));
                    cNIdentity.setIdentityNumber(rs.getString("IDENT_NO"));
                    cNIdentity.setDescription(rs.getString("IDENT_DESC"));
                    cNIdentity.setStatus(rs.getString("REC_ST"));
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return cNIdentity;
    }

    public Long queryIdentityXref(Long customerTypeId, Long identityId) {
        Long identityXrefId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_IDENT_XREF_ID FROM " + APController.coreSchemaName + ".CUSTOMER_IDENTIFICATION_XREF WHERE CUST_TY_ID=" + customerTypeId + " AND IDENT_ID=" + identityId)) {
            if (rs != null && rs.next()) {
                identityXrefId = rs.getLong("CUST_IDENT_XREF_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return identityXrefId;
    }

    public Long queryWorkflowItemId(Long custId) {
        Long workflowItemId = 0L;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT WORK_ITEM_ID FROM " + APController.coreSchemaName + ".WF_WORK_ITEM WHERE CUST_ID=" + custId + " ORDER BY WORK_ITEM_ID DESC")) {
            if (rs != null && rs.next()) {
                workflowItemId = rs.getLong("WORK_ITEM_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return workflowItemId;
    }

    public boolean checkExists(String query) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, query)) {
            return rs.next();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return false;
    }

    public AXUser loginUser(String loginId, String password, String role) {
        AXUser user = new AXUser();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.LOGIN_ID FROM " + APController.coreSchemaName + ".SYSUSER A, " + APController.coreSchemaName + ".SYSPWD_HIST B WHERE A.LOGIN_ID='" + loginId + "' AND B.SYSUSER_ID=A.SYSUSER_ID AND A.REC_ST=B.REC_ST AND B.PASSWD='" + getCypher().encrypt(password) + "' AND B.REC_ST='A'")) {
            if (rs != null && rs.next()) {
                user = queryUser(rs.getString("LOGIN_ID"), true);
            }
            if (getWorker().isBlank(user.getUserNumber()) && Objects.equals("AU", role) && !checkExists("SELECT EMP_NO FROM " + APController.cmSchemaName + ".PHU_ROLE WHERE ROLE='" + role + "'")) {
                user.setSysUser(loginId);
                user.setStaffName(loginId);
                user.setUserName(loginId);
                user.setUserNumber(loginId);
                user.getRoles().add(role);
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return user;
    }

    public <T> T queryParameter(String code, Class<T> clazz) {
        T value = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT PARAM_VALUE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = '" + code + "'")) {
            if (rs != null && rs.next()) {
                value = (T) clazz.getConstructor(String.class).newInstance(rs.getString("PARAM_VALUE"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return value;
    }

    public boolean isDuplicate(Long channelId, String accountNumber, String referenceNumber, BigDecimal amount, String debitCredit) {
        return checkExists("SELECT ACCT_NO FROM " + APController.coreSchemaName + ".DEPOSIT_ACCOUNT_HISTORY WHERE CHANNEL_ID=" + channelId + " AND ACCT_NO='" + accountNumber + "' AND TRAN_DT>=SYSDATE-30 AND TRAN_REF_TXT='" + referenceNumber + "' AND DR_CR_IND='" + debitCredit + "' AND TXN_AMT=" + amount);
    }

    public boolean isLedgerDuplicate(Long channelId, String accountNumber, String referenceNumber, BigDecimal amount, String debitCredit) {
        return checkExists("SELECT GL_ACCT_NO FROM " + APController.coreSchemaName + ".GL_ACCOUNT_HISTORY WHERE CHANNEL_ID=" + channelId + " AND GL_ACCT_NO='" + accountNumber + "' AND TRAN_DT>=SYSDATE-30 AND TRAN_REF_TXT='" + referenceNumber + "' AND DR_CR_IND='" + debitCredit + "' AND TXN_AMT=" + amount);
    }

    public boolean isAccountValid(String accountNumber) {
        return checkExists("SELECT ACCT_NO FROM " + APController.coreSchemaName + ".V_ACCOUNTS WHERE PROD_CAT_TY='DP' AND ACCT_NO='" + accountNumber + "' AND REC_ST IN ('A')");
    }

    public String queryTxnResponse(Long channelId, String txnRef) {
        String respCode = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT XAPI_CODE FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE TXN_REF='" + txnRef + "' AND TXN_DATE>=SYSDATE-30 AND CHANNEL_ID=" + channelId)) {
            if (rs != null && rs.next()) {
                respCode = rs.getString("XAPI_CODE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return respCode;
    }

    /*COPIED FROM queryChannelUser, allow mpesa verify and mpesadeposit without customer registration*/
    public CNUser queryUserByAccount(String accountNumber) {
        CNUser user = new CNUser();
        if (!getWorker().isBlank(accountNumber)) {
            try (PLAdapter adapter = getAdapter();
                    Statement statement = createStatement(adapter.getConnection());
                    ResultSet rs = executeQuery(statement, ""
                            + "SELECT A.ACCT_ID, A.ACCT_NO, C.CUST_ID, "
                            + "INITCAP(C.CUST_NM) AS ACCESS_NM FROM "
                            + "" + APController.coreSchemaName + ".CUSTOMER C,"
                            + "" + APController.coreSchemaName + ".ACCOUNT A "
                            + "WHERE A.CUST_ID = C.CUST_ID AND C.REC_ST = 'A' "
                            + "AND A.REC_ST = 'A' AND A.ACCT_NO = '" + accountNumber + "'"
                            + "")) {
                if (rs != null && getRowCount(rs) <= 1 && rs.next()) {
                    user.setUserId(rs.getLong("CUST_ID"));
                    user.setCustId(rs.getLong("CUST_ID"));
                    user.setAccessName(rs.getString("ACCESS_NM"));
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
                user.setError(true);
            }
        }
        return user;
    }

    public CNUser queryChannelUser(Long schemeId, String msisdn) {
        CNUser user = new CNUser();
        if (!getWorker().isBlank(msisdn)) {
            try (PLAdapter adapter = getAdapter();
                    Statement statement = createStatement(adapter.getConnection());
                    ResultSet rs = executeQuery(statement, "SELECT U.CUST_CHANNEL_USER_ID, "
                            + "U.CUST_ID, U.CUST_CHANNEL_ID, U.ACCESS_CD, "
                            + "INITCAP(NVL(U.ACCESS_NM, M.CUST_NM)) AS ACCESS_NM, "
                            + "U.PASSWORD, U.PWD_RESET_FG, U.CHANNEL_ID, "
                            + "U.CHANNEL_SCHEME_ID, C.CHRG_ACCT_ID, U.LOCKED_FG, "
                            + "NVL(U.RANDOM_SEED, 0) AS PIN_TRIES, "
                            + "NVL(U.RANDOM_NO_SEED, 0) AS PUK_TRIES, U.SECURITY_CD, "
                            + "U.QUIZ_CD FROM "
                            + "" + APController.coreSchemaName + ".CUSTOMER_CHANNEL C, "
                            + "" + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U, "
                            + "" + APController.coreSchemaName + ".CUSTOMER M "
                            + "WHERE C.CUST_ID=U.CUST_ID AND C.CUST_ID=M.CUST_ID "
                            + "AND C.CHANNEL_SCHEME_ID=U.CHANNEL_SCHEME_ID AND "
                            + "U.CHANNEL_SCHEME_ID=" + schemeId + " AND U.ACCESS_CD "
                            + "IN ('" + getWorker().formatMsisdn(msisdn, true) + "',"
                            + "'" + getWorker().formatMsisdn(msisdn, false) + "') "
                            + "AND U.REC_ST='A'")) {
                if (rs != null && getRowCount(rs) <= 1 && rs.next()) {
                    user.setUserId(rs.getLong("CUST_CHANNEL_USER_ID"));
                    user.setCustId(rs.getLong("CUST_ID"));
                    user.setCustChannelId(rs.getLong("CUST_CHANNEL_ID"));
                    user.setAccessCode(rs.getString("ACCESS_CD"));
                    user.setAccessName(rs.getString("ACCESS_NM"));
                    user.setPwdReset(getWorker().isYes(rs.getString("PWD_RESET_FG")));
                    user.setChannelId(rs.getLong("CHANNEL_ID"));
                    user.setSchemeId(rs.getLong("CHANNEL_SCHEME_ID"));
                    user.setLocked(getWorker().isYes(rs.getString("LOCKED_FG")));
                    user.setSecurityCode(rs.getString("SECURITY_CD"));
                    user.setAccessKey(rs.getString("QUIZ_CD"));
                    user.setPinAttempts(rs.getInt("PIN_TRIES"));
                    user.setPukAttempts(rs.getInt("PUK_TRIES"));
                    user.setChargeAccount(queryDepositAccount(rs.getLong("CHRG_ACCT_ID")));
                    user.setNwdAccounts(queryDepositAccounts(user.getCustId(), VXController.nwdProducts));
                    user.setAccounts(queryAccounts(user));
                    pushUserExpiry(user.getUserId());
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
                user.setError(true);
            }
        }
        return user;
    }

    public CNAccount queryCustomerAccount(String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, "
                        + "C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, "
                        + "INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, "
                        + "(CASE WHEN C.PROD_CAT_TY='LN' "
                        + "THEN (SELECT ABS(NVL(G.PAYOFF_AMOUNT,0)) "
                        + "FROM " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G "
                        + "WHERE G.ACCT_NO=C.ACCT_NO) ELSE "
                        + "NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) END) "
                        + "AS BALANCE, M.CUST_CAT, C.REC_ST FROM "
                        + "" + APController.coreSchemaName + ".ACCOUNT C, "
                        + "" + APController.coreSchemaName + ".CURRENCY E, "
                        + "" + APController.coreSchemaName + ".CUSTOMER M "
                        + "WHERE M.CUST_ID=C.CUST_ID AND C.ACCT_NO='" + accountNumber + "' "
                        + "AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public ArrayList<CNAccount> queryCustomerAccounts(Long custId) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, (CASE WHEN C.PROD_CAT_TY='LN' THEN (SELECT ABS(NVL(G.PAYOFF_AMOUNT,0)) FROM " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G WHERE G.ACCT_NO=C.ACCT_NO) ELSE NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) END) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.CUST_ID=" + custId + " AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public CNAccount queryDepositAccount(String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='DP' AND C.REC_ST NOT IN ('L','S','C','W') AND C.ACCT_NO='" + accountNumber + "' AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public CNAccount queryDepositAccount(Long accountId) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='DP' AND C.REC_ST NOT IN ('L','S','C','W') AND C.ACCT_ID=" + accountId + " AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public CNAccount queryDepositAccount(Long custId, String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='DP' AND C.REC_ST NOT IN ('L','S','C','W') AND C.CUST_ID=" + custId + " AND C.ACCT_NO='" + accountNumber + "' AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public ArrayList<CNAccount> queryDepositAccounts(Long custId) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='DP' AND C.REC_ST NOT IN ('L','S','C','W') AND C.CUST_ID=" + custId + " AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public ArrayList<CNAccount> queryDepositAccounts(Long custId, ArrayList<Long> productIds) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        if (!getWorker().isBlank(productIds) && !productIds.isEmpty()) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='DP' AND C.REC_ST NOT IN ('L','S','C','W') AND C.CUST_ID=" + custId + " AND C.PROD_ID IN (" + getWorker().createCsvList(productIds) + ") AND C.REC_ST='A' AND C.CRNCY_ID=E.CRNCY_ID")) {
                if (rs != null) {
                    while (rs.next()) {
                        CNAccount cNAccount = new CNAccount();
                        cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                        cNAccount.setCustId(rs.getLong("CUST_ID"));
                        cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                        cNAccount.setProductId(rs.getLong("PROD_ID"));
                        cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                        cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                        cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                        cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                        cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                        cNAccount.setShortName(cNAccount.getAccountName());
                        cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                        cNAccount.setCustCat(rs.getString("CUST_CAT"));
                        cNAccount.setStatus(rs.getString("REC_ST"));
                        accounts.add(cNAccount);
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return accounts;
    }

    public CNAccount queryLoanAccount(String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.ACCT_NO='" + accountNumber + "' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public CNAccount queryLoanAccount(Long accountId) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.ACCT_ID=" + accountId + " AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public CNAccount queryLoanAccount(Long custId, String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.CUST_ID=" + custId + " AND C.ACCT_NO='" + accountNumber + "' AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public ArrayList<CNAccount> queryLoanAccounts(Long custId) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.CUST_ID=" + custId + " AND C.CRNCY_ID=E.CRNCY_ID")) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public ArrayList<CNAccount> queryLoanAccounts(Long custId, ArrayList<Long> productIds) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        if (!getWorker().isBlank(productIds) && !productIds.isEmpty()) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.CUST_ID=" + custId + " AND C.PROD_ID IN (" + getWorker().createCsvList(productIds) + ") AND C.CRNCY_ID=E.CRNCY_ID")) {
                if (rs != null) {
                    while (rs.next()) {
                        CNAccount cNAccount = new CNAccount();
                        cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                        cNAccount.setCustId(rs.getLong("CUST_ID"));
                        cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                        cNAccount.setProductId(rs.getLong("PROD_ID"));
                        cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                        cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                        cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                        cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                        cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                        cNAccount.setShortName(cNAccount.getAccountName());
                        cNAccount.setCustCat(rs.getString("CUST_CAT"));
                        cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                        cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                        cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                        cNAccount.setStatus(rs.getString("REC_ST"));
                        accounts.add(cNAccount);
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return accounts;
    }

    public ArrayList<CNAccount> queryLoanAccounts(ArrayList<Long> productIds, Integer daysPast) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        if (!getWorker().isBlank(productIds) && !productIds.isEmpty()) {
            try (PLAdapter adapter = getAdapter(true); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, L.MATURITY_DT, (SELECT NVL(SUM(V.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT V WHERE V.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND V.DUE_DT<TRUNC(SYSDATE) AND V.ACCT_ID=C.ACCT_ID AND V.REC_ST IN ('P','N')) AS ARREARS, ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".LOAN_ACCOUNT L, " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G, " + APController.coreSchemaName + ".CURRENCY E, " + APController.coreSchemaName + ".CUSTOMER M WHERE M.CUST_ID=C.CUST_ID AND C.PROD_CAT_TY='LN' AND C.REC_ST NOT IN ('L','S','C','W') AND L.ACCT_ID=C.ACCT_ID AND G.ACCT_NO=C.ACCT_NO AND C.PROD_ID IN (" + getWorker().createCsvList(productIds) + ") AND C.CRNCY_ID=E.CRNCY_ID AND L.MATURITY_DT<=TRUNC(SYSDATE-" + daysPast + ") AND L.MATURITY_DT>=TRUNC(SYSDATE-" + (daysPast + 15) + ")")) {
                if (rs != null) {
                    while (rs.next()) {
                        CNAccount cNAccount = new CNAccount();
                        cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                        cNAccount.setCustId(rs.getLong("CUST_ID"));
                        cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                        cNAccount.setProductId(rs.getLong("PROD_ID"));
                        cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                        cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                        cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                        cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                        cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                        cNAccount.setShortName(cNAccount.getAccountName());
                        cNAccount.setCustCat(rs.getString("CUST_CAT"));
                        cNAccount.setArrears(rs.getBigDecimal("ARREARS"));
                        cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                        cNAccount.setEndDate(rs.getDate("MATURITY_DT"));
                        cNAccount.setStatus(rs.getString("REC_ST"));
                        accounts.add(cNAccount);
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return accounts;
    }

    public CNAccount queryRepaymentAccount(String loanAccount) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT P.PRINCIPAL_REPAY_ACCT_NO FROM " + APController.coreSchemaName + ".V_LOAN_ACCOUNT_PAYMENT P WHERE P.ACCT_NO='" + loanAccount + "'")) {
            if (rs != null && rs.next()) {
                return queryDepositAccount(rs.getString("PRINCIPAL_REPAY_ACCT_NO"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new CNAccount();
    }

    public CNAccount queryLedgerAccount(String accountNumber) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.GL_ACCT_ID, C.GL_ACCT_NO, C.ACCT_DESC, C.BU_ID, C.CREATE_DT, C.REC_ST FROM " + APController.coreSchemaName + ".GL_ACCOUNT C WHERE C.GL_ACCT_NO='" + accountNumber + "'")) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("GL_ACCT_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("BU_ID")));
                cNAccount.setAccountNumber(rs.getString("GL_ACCT_NO"));
                cNAccount.setShortName(getWorker().cleanSpaces(rs.getString("ACCT_DESC")));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_DESC")));
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCurrency(APController.getCurrency());
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public ArrayList<CNAccount> queryAccounts(CNUser cNUser) {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, "
                        + "C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, "
                        + "INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, "
                        + "NVL(A.SHORT_NAME, C.ACCT_NO) AS SHORT_NAME, C.CRNCY_ID, "
                        + "C.PROD_CAT_TY, "
                        + "NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) "
                        + "AS BALANCE, M.CUST_CAT, C.REC_ST FROM "
                        + "" + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT A, "
                        + "" + APController.coreSchemaName + ".ACCOUNT C, "
                        + "" + APController.coreSchemaName + ".CUSTOMER M, "
                        + "" + APController.coreSchemaName + ".CURRENCY E "
                        + "WHERE A.ACCT_ID=C.ACCT_ID AND A.REC_ST='A' AND "
                        + "A.CHANNEL_ID=" + cNUser.getChannelId() + " AND "
                        + "A.CUST_ID=C.CUST_ID AND M.CUST_ID=C.CUST_ID AND "
                        + "C.CRNCY_ID=E.CRNCY_ID AND A.CUST_ID=" + cNUser.getCustId())) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public CNAccount queryChannelAccount(Long channelId, Long custId, Long accountId) {
        CNAccount cNAccount = new CNAccount();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, NVL(A.SHORT_NAME, C.ACCT_NO) AS SHORT_NAME, C.CRNCY_ID, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT A, " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CUSTOMER M, " + APController.coreSchemaName + ".CURRENCY E WHERE A.ACCT_ID=C.ACCT_ID AND A.REC_ST='A' AND A.CHANNEL_ID=" + channelId + " AND A.CUST_ID=C.CUST_ID AND M.CUST_ID=C.CUST_ID AND C.CRNCY_ID=E.CRNCY_ID AND A.CUST_ID=" + custId + " AND A.ACCT_ID=" + accountId)) {
            if (rs != null && rs.next()) {
                cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                cNAccount.setCustId(rs.getLong("CUST_ID"));
                cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                cNAccount.setProductId(rs.getLong("PROD_ID"));
                cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                cNAccount.setShortName(cNAccount.getAccountName());
                cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                cNAccount.setCustCat(rs.getString("CUST_CAT"));
                cNAccount.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNAccount;
    }

    public ArrayList<CNAccount> queryCycleAccounts(ESTask task) {
        String filter = "";
        ArrayList<CNAccount> accounts = new ArrayList<>();
        switch (task.getFilterBy()) {
            case "AN":
                filter = " UNION SELECT ACCT_ID FROM " + APController.coreSchemaName + ".ACCOUNT WHERE ACCT_NO IN (" + getWorker().createCsvList(task.getFilters()) + ") AND REC_ST IN ('A','D') AND ACCT_ID NOT IN (SELECT D.PARENT_ID FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE D, " + APController.coreSchemaName + ".CUSTOM_LIST_ITEM L WHERE D.CUSTOM_LIST_TY_ID=L.CUSTOM_LIST_ID AND L.CUSTOM_LIST_ITEM_ID=D.FIELD_VALUE AND D.FIELD_ID=" + ESController.cycleFieldId + " AND D.REC_ST='A' AND L.CUSTOM_LIST_ITEM_CD='N')";
                break;
            case "AP":
                filter = " UNION SELECT ACCT_ID FROM " + APController.coreSchemaName + ".ACCOUNT WHERE PROD_ID IN (" + getWorker().createCsvList(task.getFilters()) + ") AND REC_ST IN ('A','D') AND ACCT_ID NOT IN (SELECT D.PARENT_ID FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE D, " + APController.coreSchemaName + ".CUSTOM_LIST_ITEM L WHERE D.CUSTOM_LIST_TY_ID=L.CUSTOM_LIST_ID AND L.CUSTOM_LIST_ITEM_ID=D.FIELD_VALUE AND D.FIELD_ID=" + ESController.cycleFieldId + " AND D.REC_ST='A' AND L.CUSTOM_LIST_ITEM_CD='N')";
                break;
        }
        try (PLAdapter adapter = getAdapter(true); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.CRNCY_ID, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CUSTOMER M, " + APController.coreSchemaName + ".CURRENCY E WHERE M.CUST_ID=C.CUST_ID AND C.CRNCY_ID=E.CRNCY_ID AND C.ACCT_ID IN (SELECT D.PARENT_ID FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE D, " + APController.coreSchemaName + ".CUSTOM_LIST_ITEM L WHERE D.CUSTOM_LIST_TY_ID=L.CUSTOM_LIST_ID AND L.CUSTOM_LIST_ITEM_ID=D.FIELD_VALUE AND D.FIELD_ID=" + ESController.cycleFieldId + " AND D.REC_ST='A' AND L.CUSTOM_LIST_ITEM_CD='" + task.getCycle() + "'" + filter + ")")) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public ArrayList<CNAccount> queryNowAccounts() {
        ArrayList<CNAccount> accounts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(true); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.ACCT_ID, C.CUST_ID, C.MAIN_BRANCH_ID, C.PROD_ID, C.CREATE_DT, C.ACCT_NO, INITCAP(C.ACCT_NM) AS ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY, NVL(" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(C.ACCT_ID),0) AS BALANCE, M.CUST_CAT, C.REC_ST FROM " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CUSTOMER M, " + APController.coreSchemaName + ".CURRENCY E WHERE M.CUST_ID=C.CUST_ID AND C.CRNCY_ID=E.CRNCY_ID AND C.ACCT_ID IN (SELECT A.PARENT_ID FROM " + APController.coreSchemaName + ".UDS_FIELD_VALUE A WHERE A.FIELD_ID=" + ESController.nowFieldId + " AND A.REC_ST='A' AND A.FIELD_VALUE='" + ESController.yesValueId + "')")) {
            if (rs != null) {
                while (rs.next()) {
                    CNAccount cNAccount = new CNAccount();
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setCustId(rs.getLong("CUST_ID"));
                    cNAccount.setBranch(queryBranch(rs.getLong("MAIN_BRANCH_ID")));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setBalance(rs.getBigDecimal("BALANCE"));
                    cNAccount.setAccountName(getWorker().cleanSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrency(queryCurrency(rs.getString("CRNCY_CD")));
                    cNAccount.setShortName(cNAccount.getAccountName());
                    cNAccount.setOpenDate(rs.getDate("CREATE_DT"));
                    cNAccount.setCustCat(rs.getString("CUST_CAT"));
                    cNAccount.setStatus(rs.getString("REC_ST"));
                    accounts.add(cNAccount);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public CMImage queryCustomerPhoto(Long custId) {
        CMImage cMImage = new CMImage();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT I.CUST_ID, I.BINARY_IMAGE, I.IMAGE_TY FROM " + APController.coreSchemaName + ".V_CUSTOMER_IMAGES I WHERE I.CUST_ID=" + custId + " AND I.IMAGE_TY='PHO' AND I.REC_ST='A' AND ROWNUM=1")) {
            if (rs != null && rs.next()) {
                cMImage.setCustId(rs.getLong("CUST_ID"));
                cMImage.setType(rs.getString("IMAGE_TY"));
                cMImage.setBytes(getWorker().convertBlobToBytes(rs.getBlob("BINARY_IMAGE")));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cMImage;
    }

    public ArrayList<CMImage> queryCustomerPhotos(Long custId) {
        ArrayList<CMImage> images = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT I.CUST_ID, I.BINARY_IMAGE, I.IMAGE_TY FROM " + APController.coreSchemaName + ".V_CUSTOMER_IMAGES I WHERE I.CUST_ID=" + custId + " AND I.IMAGE_TY='PHO' AND I.REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    CMImage cMImage = new CMImage();
                    cMImage.setCustId(rs.getLong("CUST_ID"));
                    cMImage.setType(rs.getString("IMAGE_TY"));
                    cMImage.setBytes(getWorker().convertBlobToBytes(rs.getBlob("BINARY_IMAGE")));
                    images.add(cMImage);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return images;
    }

    public boolean hasAccount(Long custId, Long productId) {
        return checkExists("SELECT C.ACCT_NO FROM " + APController.coreSchemaName + ".ACCOUNT C WHERE C.CUST_ID=" + custId + " AND C.PROD_ID IN (" + productId + ") AND C.REC_ST='A'");
    }

    public BigDecimal queryLoanBalance(String accountNumber) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT ABS(NVL(G.PAYOFF_AMOUNT,0)) AS BALANCE FROM " + APController.coreSchemaName + ".V_PH_LOAN_PAYOFF_BAL G WHERE G.ACCT_NO='" + accountNumber + "'")) {
            if (rs != null && rs.next()) {
                return rs.getBigDecimal("BALANCE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return BigDecimal.ZERO;
    }

    public LNDetail queryLoanDetail(CNAccount cNAccount) {
        LNDetail lNDetail = new LNDetail();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT * FROM (SELECT A.ACCT_NO, PI.PRINCIPAL_REPAY_ACCT_ID RPMT_ACCT_ID, (SELECT NVL(SUM(RT.REPMNT_AMT),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT RT WHERE RT.ACCT_ID=R.ACCT_ID AND RT.DUE_DT=R.DUE_DT AND RT.REC_ST IN ('N','P')) AS REPMNT_AMT, R.DUE_DT, Y.CRNCY_CD, (SELECT NVL(SUM(RM.AMT_UNPAID),0) FROM " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT RM WHERE RM.EVENT_TYPE IN('PRINCIPAL','CHARGE','INTEREST') AND RM.DUE_DT < (SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02') AND RM.ACCT_ID = A.ACCT_ID AND RM.REC_ST IN ('P','N')) AS AMT_UNPAID, NVL(Q.CLEARED_BAL,0) AS CLEARED_BAL FROM " + APController.coreSchemaName + ".ACCOUNT A LEFT OUTER JOIN " + APController.coreSchemaName + ".LOAN_ACCOUNT_PAYMENT_INFO PI ON PI.ACCT_ID=A.ACCT_ID, " + APController.coreSchemaName + ".CUSTOMER C, " + APController.coreSchemaName + ".LN_ACCT_REPMNT_EVENT R, " + APController.coreSchemaName + ".LOAN_ACCOUNT_SUMMARY Q, " + APController.coreSchemaName + ".BUSINESS_UNIT B, " + APController.coreSchemaName + ".CURRENCY Y WHERE C.CUST_ID = A.CUST_ID AND B.BU_ID = A.MAIN_BRANCH_ID AND A.ACCT_ID = R.ACCT_ID AND Y.CRNCY_ID = A.CRNCY_ID AND R.EVENT_TYPE IN('PRINCIPAL') AND R.REC_ST IN ('P','N') AND A.REC_ST in ('A','C','D','I') AND Q.LAST_DISBURSEMENT_DT IS NOT NULL AND Q.ACCT_ID = A.ACCT_ID AND A.ACCT_NO = '" + cNAccount.getAccountNumber() + "' ORDER BY R.DUE_DT ASC) WHERE ROWNUM=1")) {
            if (rs != null && rs.next()) {
                lNDetail.setPaymentDueDate(rs.getDate("DUE_DT"));
                lNDetail.setMobileNumber(queryMobileContact(cNAccount.getCustId()));
                lNDetail.setRepaymentAmount(rs.getBigDecimal("REPMNT_AMT"));
                lNDetail.setUnpaidAmount(rs.getBigDecimal("AMT_UNPAID"));
                lNDetail.setClearedBalance(rs.getBigDecimal("CLEARED_BAL"));
                lNDetail.setLoanAccount(queryLoanAccount(rs.getString("ACCT_NO")));
                lNDetail.setRepaymentAccount(queryDepositAccount(rs.getLong("RPMT_ACCT_ID")));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return lNDetail;
    }

    public CNCustomer queryCustomer(String customerNumber) {
        CNCustomer cNCustomer = new CNCustomer();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.CUST_ID, C.CUST_NO, C.MAIN_BRANCH_ID, C.CUST_CAT, INITCAP(C.CUST_NM) AS CUST_NM, (SELECT IDENT_NO FROM " + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION WHERE CUST_ID=C.CUST_ID AND IDENT_ID=" + VXController.nationalIdentityId + " AND REC_ST='A' AND ROWNUM=1) AS IDENT_NO FROM " + APController.coreSchemaName + ".CUSTOMER C WHERE TO_NUMBER(C.CUST_NO)=" + getWorker().convertToType(customerNumber, Long.class))) {
            if (rs != null && rs.next()) {
                cNCustomer.setCustId(rs.getLong("CUST_ID"));
                cNCustomer.setBuId(rs.getLong("MAIN_BRANCH_ID"));
                cNCustomer.setCustNo(rs.getString("CUST_NO"));
                cNCustomer.setCustCat(rs.getString("CUST_CAT"));
                cNCustomer.setCustName(rs.getString("CUST_NM"));
                cNCustomer.setMobileNumber(queryMobileContact(cNCustomer.getCustId()));
                cNCustomer.setIdentity(rs.getString("IDENT_NO"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCustomer;
    }

    public CNCustomer queryCustomer(Long custId) {
        CNCustomer cNCustomer = new CNCustomer();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT C.CUST_ID, C.CUST_NO, "
                        + "C.MAIN_BRANCH_ID, C.CUST_CAT, INITCAP(C.CUST_NM) AS CUST_NM, "
                        + "(SELECT IDENT_NO FROM "
                        + "" + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION "
                        + "WHERE CUST_ID=C.CUST_ID AND IDENT_ID=" + VXController.nationalIdentityId + " "
                        + "AND REC_ST='A' AND ROWNUM=1) AS IDENT_NO FROM "
                        + "" + APController.coreSchemaName + ".CUSTOMER C WHERE C.CUST_ID=" + custId)) {
            if (rs != null && rs.next()) {
                cNCustomer.setCustId(rs.getLong("CUST_ID"));
                cNCustomer.setBuId(rs.getLong("MAIN_BRANCH_ID"));
                cNCustomer.setCustNo(rs.getString("CUST_NO"));
                cNCustomer.setCustCat(rs.getString("CUST_CAT"));
                cNCustomer.setCustName(rs.getString("CUST_NM"));
                cNCustomer.setMobileNumber(queryMobileContact(cNCustomer.getCustId()));
                cNCustomer.setIdentity(rs.getString("IDENT_NO"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCustomer;
    }

    public ArrayList<CNCustomer> queryUpdatedUsers() {
        ArrayList<CNCustomer> customers = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(true); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.CUST_ID, C.CUST_NO, C.MAIN_BRANCH_ID, C.CUST_CAT, INITCAP(C.CUST_NM) AS CUST_NM, U.ACCESS_CD AS MOBILE, (SELECT IDENT_NO FROM " + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION WHERE CUST_ID=C.CUST_ID AND IDENT_ID=" + VXController.nationalIdentityId + " AND REC_ST='A' AND ROWNUM=1) AS IDENT_NO FROM " + APController.coreSchemaName + ".CUSTOMER C, " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U WHERE C.CUST_ID=U.CUST_ID AND U.CHANNEL_SCHEME_ID=" + VXController.schemeId + " AND U.REC_ST='A' AND (SELECT MAX(AUDIT_TS) FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER$AUD WHERE CUST_CHANNEL_USER_ID=U.CUST_CHANNEL_USER_ID AND REC_ST='A')>=TO_TIMESTAMP('" + VXController.usersSpoolTime + "', 'YYYY-MM-DD HH24:MI:SS')")) {
            if (rs != null) {
                while (rs.next()) {
                    CNCustomer cNCustomer = new CNCustomer();
                    cNCustomer.setCustId(rs.getLong("CUST_ID"));
                    cNCustomer.setBuId(rs.getLong("MAIN_BRANCH_ID"));
                    cNCustomer.setCustNo(rs.getString("CUST_NO"));
                    cNCustomer.setCustCat(rs.getString("CUST_CAT"));
                    cNCustomer.setCustName(rs.getString("CUST_NM"));
                    cNCustomer.setMobileNumber(rs.getString("MOBILE"));
                    cNCustomer.setIdentity(rs.getString("IDENT_NO"));
                    customers.add(cNCustomer);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customers;
    }

    public CNCustomer queryCustomer(Long schemeId, String accessCode) {
        CNCustomer cNCustomer = new CNCustomer();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.CUST_ID, C.CUST_NO, C.MAIN_BRANCH_ID, C.CUST_CAT, INITCAP(C.CUST_NM) AS CUST_NM, (SELECT IDENT_NO FROM " + APController.coreSchemaName + ".V_CUSTOMER_IDENTIFICATION WHERE CUST_ID=C.CUST_ID AND IDENT_ID=" + VXController.nationalIdentityId + " AND REC_ST='A' AND ROWNUM=1) AS IDENT_NO FROM " + APController.coreSchemaName + ".CUSTOMER C, " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U WHERE C.CUST_ID=U.CUST_ID AND U.CHANNEL_SCHEME_ID = " + schemeId + " AND U.ACCESS_CD='" + accessCode + "' AND U.REC_ST IN ('A','S')")) {
            if (rs != null && rs.next()) {
                cNCustomer.setCustId(rs.getLong("CUST_ID"));
                cNCustomer.setBuId(rs.getLong("MAIN_BRANCH_ID"));
                cNCustomer.setCustNo(rs.getString("CUST_NO"));
                cNCustomer.setCustCat(rs.getString("CUST_CAT"));
                cNCustomer.setCustName(rs.getString("CUST_NM"));
                cNCustomer.setMobileNumber(queryMobileContact(cNCustomer.getCustId()));
                cNCustomer.setIdentity(rs.getString("IDENT_NO"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCustomer;
    }

    public CNCountry queryCountry(Long countryId) {
        CNCountry cNCountry = new CNCountry();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CNTRY_ID, CNTRY_CD, CNTRY_CD_ISO3, CNTRY_NM, REC_ST FROM " + APController.coreSchemaName + ".COUNTRY WHERE CNTRY_ID=" + countryId)) {
            if (rs != null && rs.next()) {
                cNCountry.setId(rs.getLong("CNTRY_ID"));
                cNCountry.setCode(rs.getString("CNTRY_CD"));
                cNCountry.setIsoCode(rs.getString("CNTRY_CD_ISO3"));
                cNCountry.setName(rs.getString("CNTRY_NM"));
                cNCountry.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCountry;
    }

    public boolean updateCustomType(Long fieldId, boolean mandatory) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".USER_DEFINED_SCREEN_FIELD SET INFO_RQRMNT_CD='" + (mandatory ? "MAN" : "DES") + "', VERSION_NO=" + (mandatory ? "VERSION_NO-1" : "VERSION_NO+1") + " WHERE FIELD_ID=" + fieldId);
    }

    public Integer updateCustomField(Long fieldId, boolean mandatory, Integer version) {
        if (executeUpdate("UPDATE " + APController.coreSchemaName + ".USER_DEFINED_SCREEN_FIELD SET INFO_RQRMNT_CD='" + (mandatory ? "MAN" : "DES") + "', VERSION_NO=" + (mandatory ? "VERSION_NO-1" : "VERSION_NO+1") + " WHERE FIELD_ID=" + fieldId + (version > 0 ? " AND VERSION_NO=" + version : ""))) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT VERSION_NO FROM " + APController.coreSchemaName + ".USER_DEFINED_SCREEN_FIELD WHERE FIELD_ID=" + fieldId)) {
                if (rs != null && rs.next()) {
                    version = rs.getInt("VERSION_NO");
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return version;
    }

    public ArrayList<Long> queryMandatoryFields(String screenName) {
        ArrayList<Long> list = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT F.FIELD_ID FROM " + APController.coreSchemaName + ".V_UDS_FIELD V, " + APController.coreSchemaName + ".USER_DEFINED_SCREEN_FIELD F WHERE F.FIELD_ID=V.FIELD_ID AND F.INFO_RQRMNT_CD='MAN' AND V.PARENT_OBJECT_CD='" + screenName + "'")) {
            if (rs != null) {
                while (rs.next()) {
                    list.add(rs.getLong("FIELD_ID"));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return list;
    }

    public HashMap<Long, CLItem> defaultLanguageItems() {
        HashMap<Long, CLItem> itemsList = new HashMap<>();
        CLItem lang = new CLItem(1, 1L, APController.defaultLanguage, "ENGLISH");
        itemsList.put(lang.getItemId(), lang);

//        lang = new CLItem(2, 2L, "KW", "KISWAHILI");
//        itemsList.put(lang.getItemId(), lang);
        return itemsList;
    }

    public HashMap<Long, CLItem> queryCustomListItems(Long listId) {
        HashMap<Long, CLItem> itemsList = new HashMap<>();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT ROWNUM, "
                        + "CUSTOM_LIST_ITEM_ID, CUSTOM_LIST_ITEM_CD, "
                        + "INITCAP(CUSTOM_LIST_ITEM_DESC) AS CUSTOM_LIST_ITEM_DESC "
                        + "FROM " + APController.coreSchemaName + ".CUSTOM_LIST_ITEM "
                        + "WHERE CUSTOM_LIST_ID=" + listId)) {
            if (rs != null) {
                while (rs.next()) {
                    itemsList.put(rs.getLong("CUSTOM_LIST_ITEM_ID"),
                            new CLItem(rs.getInt("ROWNUM"),
                                    rs.getLong("CUSTOM_LIST_ITEM_ID"),
                                    rs.getString("CUSTOM_LIST_ITEM_CD"),
                                    rs.getString("CUSTOM_LIST_ITEM_DESC")));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return itemsList;
    }

    public boolean upsertCustomField(Long fieldId, Long parentId, String fieldValue) {
        if (executeUpdate("MERGE INTO " + APController.coreSchemaName + ".UDS_FIELD_VALUE D USING (SELECT " + queryCustomEntityId() + " AS UDS_FIELD_VALUE_ID, " + fieldId + " AS FIELD_ID, " + parentId + " AS PARENT_ID, '" + fieldValue + "' AS FIELD_VALUE, 'A' AS REC_ST, 1 AS VERSION_NO, SYSDATE AS ROW_TS, 'SYSTEM' AS USER_ID, SYSDATE AS CREATE_DT, 'SYSTEM' AS CREATED_BY, SYSDATE AS SYS_CREATE_TS FROM DUAL) S ON (D.FIELD_ID = S.FIELD_ID AND D.PARENT_ID=S.PARENT_ID) WHEN MATCHED THEN UPDATE SET D.FIELD_VALUE = S.FIELD_VALUE WHEN NOT MATCHED THEN INSERT (UDS_FIELD_VALUE_ID, FIELD_ID, PARENT_ID, FIELD_VALUE, REC_ST, VERSION_NO, ROW_TS, USER_ID, CREATE_DT, CREATED_BY, SYS_CREATE_TS) VALUES(S.UDS_FIELD_VALUE_ID, S.FIELD_ID, S.PARENT_ID, S.FIELD_VALUE, S.REC_ST, S.VERSION_NO, S.ROW_TS, S.USER_ID, S.CREATE_DT, S.CREATED_BY, S.SYS_CREATE_TS)")) {
            return updateCustomEntityId();
        }
        return false;
    }

    public CNAccount queryChannelLedger(Long channelId, CNBranch cNBranch) {
        String drContraGL = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT GL_DR_ACCT FROM " + APController.coreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelId)) {
            if (rs != null && rs.next()) {
                drContraGL = rs.getString("GL_DR_ACCT");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return unmaskLedger(drContraGL, cNBranch);
    }

    private boolean updateCustomEntityId() {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".ENTITY SET NEXT_NO=(SELECT MAX(UDS_FIELD_VALUE_ID)+1 FROM " + APController.coreSchemaName + ".UDS_FIELD_VALUE) WHERE ENTITY_NM = 'UDS_FIELD_VALUE'");
    }

    private Long queryCustomEntityId() {
        Long entityId = 0L;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT MAX(UDS_FIELD_VALUE_ID)+1 AS NEXT_NO FROM " + APController.coreSchemaName + ".UDS_FIELD_VALUE")) {
            if (rs != null && rs.next()) {
                entityId = rs.getLong("NEXT_NO");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return entityId;
    }

    private Long nextSequenceId(String sequence) {
        Long seqId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT " + APController.cmSchemaName + "." + sequence + ".NEXTVAL FROM DUAL")) {
            if (rs != null && rs.next()) {
                seqId = rs.getLong("NEXTVAL");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return seqId;
    }

    private boolean saveTiers(String type, Long parentKey, HashMap<BigDecimal, AXTier> tiers) {
        boolean RC = deleteTiers(type, parentKey);
        for (AXTier tier : tiers.values()) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_TIER(REC_ID, TYPE, PARENT, TIER_MAX, TIER_VALUE) VALUES(" + APController.cmSchemaName + ".SEQ_PHC_TIER.NEXTVAL, '" + tier.getType() + "', '" + parentKey + "', " + tier.getTierMax() + ", " + tier.getValue() + ")");
        }
        return RC;
    }

    private boolean deleteTiers(String type, Long parentKey) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_TIER WHERE TYPE='" + type + "' AND PARENT='" + parentKey + "'");
    }

    private HashMap<BigDecimal, AXTier> queryTiers(String type, Long parentKey) {
        HashMap<BigDecimal, AXTier> tiers = new HashMap<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, TYPE, PARENT, TIER_MAX, TIER_VALUE FROM " + APController.cmSchemaName + ".PHC_TIER WHERE TYPE='" + type + "' AND PARENT='" + parentKey + "' ORDER BY TIER_MAX ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXTier tier = new AXTier();
                    tier.setTierId(rs.getLong("REC_ID"));
                    tier.setType(rs.getString("TYPE"));
                    tier.setTierMax(rs.getBigDecimal("TIER_MAX"));
                    tier.setValue(rs.getBigDecimal("TIER_VALUE"));
                    tiers.put(tier.getTierMax(), tier);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return tiers;
    }

    private HashMap<Long, TCDeduction> queryDeductions(Long parentKey) {
        HashMap<Long, TCDeduction> deductions = new HashMap<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, TYPE, VALUE FROM " + APController.cmSchemaName + ".PHC_DEDUCTION WHERE PARENT='" + parentKey + "' ORDER BY REC_ID ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    TCDeduction deduction = new TCDeduction();
                    deduction.setRecId(rs.getLong("REC_ID"));
                    deduction.setBasis(rs.getString("BASIS"));
                    deduction.setDescription(rs.getString("DESCRIPTION"));
                    deduction.setAccount(rs.getString("ACCOUNT"));
                    deduction.setType(rs.getString("TYPE"));
                    deduction.setValue(rs.getBigDecimal("VALUE"));
                    deduction.setTiers(queryTiers("D", deduction.getRecId()));
                    deductions.put(deduction.getRecId(), deduction);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return deductions;
    }

    public boolean upsertAlert(MXAlert mXAlert) {
        return checkExists("SELECT CODE FROM " + APController.cmSchemaName + ".PHA_ALERT WHERE CODE='" + mXAlert.getCode() + "'") ? updateAlert(mXAlert) : saveAlert(mXAlert);
    }

    private boolean saveAlert(MXAlert mXAlert) {
        if (executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHA_ALERT(REC_ID, CODE, CREATE_DT, TYPE, DESCRIPTION, PRIORITY, RUN_TIME, CHRG_CODE, FREQUENCY, DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHA_ALERT.NEXTVAL, '" + mXAlert.getCode() + "', SYSDATE, '" + mXAlert.getType() + "', '" + mXAlert.getDescription() + "', " + mXAlert.getPriority() + ", '" + mXAlert.getRunTime() + "', '" + mXAlert.getChargeCode() + "', '" + mXAlert.getFrequency() + "', '" + mXAlert.getDays() + "', " + convertToOracleDate(mXAlert.getNextDate()) + ", " + convertToOracleDate(mXAlert.getPreviousDate()) + ", " + convertToOracleDate(mXAlert.getExpiryDate()) + ", '" + mXAlert.getFilterBy() + "', '" + mXAlert.getStatus() + "')")) {
            return saveTemplates(ALController.module, mXAlert.getCode(), mXAlert.getTemplates()) && saveFilters(ALController.module, mXAlert.getCode(), mXAlert.getFilters());
        }
        return false;
    }

    private boolean updateAlert(MXAlert mXAlert) {
        if (executeUpdate("UPDATE " + APController.cmSchemaName + ".PHA_ALERT SET CODE='" + mXAlert.getCode() + "', TYPE='" + mXAlert.getType() + "', DESCRIPTION='" + mXAlert.getDescription() + "', PRIORITY=" + mXAlert.getPriority() + ", RUN_TIME='" + mXAlert.getRunTime() + "', CHRG_CODE='" + mXAlert.getChargeCode() + "', FREQUENCY='" + mXAlert.getFrequency() + "', NEXT_DATE=" + convertToOracleDate(mXAlert.getNextDate()) + ", LAST_DATE=" + convertToOracleDate(mXAlert.getPreviousDate()) + ", EXPIRY_DATE=" + convertToOracleDate(mXAlert.getExpiryDate()) + ", FILTER_BY='" + mXAlert.getFilterBy() + "', REC_ST='" + mXAlert.getStatus() + "' WHERE CODE='" + mXAlert.getCode() + "'")) {
            return saveTemplates(ALController.module, mXAlert.getCode(), mXAlert.getTemplates()) && saveFilters(ALController.module, mXAlert.getCode(), mXAlert.getFilters());
        }
        return false;
    }

    public boolean pushAlert(MXAlert mXAlert) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHA_ALERT SET FREQUENCY='" + mXAlert.getFrequency() + "', NEXT_DATE=" + convertToOracleDate(mXAlert.getNextDate()) + ", LAST_DATE=" + convertToOracleDate(mXAlert.getPreviousDate()) + ", EXPIRY_DATE=" + convertToOracleDate(mXAlert.getExpiryDate()) + ", REC_ST='" + mXAlert.getStatus() + "' WHERE CODE='" + mXAlert.getCode() + "'");
    }

    public boolean updateStatus(MXAlert mXAlert) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHA_ALERT SET REC_ST='" + mXAlert.getStatus() + "' WHERE CODE='" + mXAlert.getCode() + "'");
    }

    public boolean upsertTask(ESTask eSTask) {
        return checkExists("SELECT REC_CD FROM " + APController.cmSchemaName + ".PHS_TASK WHERE REC_CD='" + eSTask.getCode() + "'") ? updateTask(eSTask) : saveTask(eSTask);
    }

    private boolean saveTask(ESTask eSTask) {
        if (executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHS_TASK(REC_ID, REC_CD, CREATE_DT, DOCUMENT, RANGE, DESCRIPTION, RUN_TIME, CHARGE, CYCLE, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHS_TASK.NEXTVAL, '" + eSTask.getCode() + "', SYSDATE, '" + eSTask.getDocument() + "', '" + eSTask.getRange() + "', '" + eSTask.getDescription() + "', '" + eSTask.getRunTime() + "', '" + eSTask.getCharge() + "', '" + eSTask.getCycle() + "', " + convertToOracleDate(eSTask.getNextDate()) + ", " + convertToOracleDate(eSTask.getPreviousDate()) + ", " + convertToOracleDate(eSTask.getExpiryDate()) + ", '" + eSTask.getFilterBy() + "', '" + eSTask.getStatus() + "')")) {
            return saveFilters(ESController.module, eSTask.getCode(), eSTask.getFilters());
        }
        return false;
    }

    private boolean updateTask(ESTask eSTask) {
        if (executeUpdate("UPDATE " + APController.cmSchemaName + ".PHS_TASK SET REC_CD='" + eSTask.getCode() + "', DOCUMENT='" + eSTask.getDocument() + "', RANGE='" + eSTask.getRange() + "', DESCRIPTION='" + eSTask.getDescription() + "', RUN_TIME='" + eSTask.getRunTime() + "', CHARGE='" + eSTask.getCharge() + "', CYCLE='" + eSTask.getCycle() + "', NEXT_DATE=" + convertToOracleDate(eSTask.getNextDate()) + ", LAST_DATE=" + convertToOracleDate(eSTask.getPreviousDate()) + ", EXPIRY_DATE=" + convertToOracleDate(eSTask.getExpiryDate()) + ", FILTER_BY='" + eSTask.getFilterBy() + "', REC_ST='" + eSTask.getStatus() + "' WHERE REC_CD='" + eSTask.getCode() + "'")) {
            return saveFilters(ESController.module, eSTask.getCode(), eSTask.getFilters());
        }
        return false;
    }

    public boolean pushTask(ESTask eSTask) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHS_TASK SET NEXT_DATE=" + convertToOracleDate(eSTask.getNextDate()) + ", LAST_DATE=" + convertToOracleDate(eSTask.getPreviousDate()) + ", REC_ST='" + eSTask.getStatus() + "' WHERE REC_CD='" + eSTask.getCode() + "'");
    }

    public boolean upsertSetting(AXSetting setting) {
        return checkExists("SELECT CODE FROM " + APController.cmSchemaName + ".PHL_SETTING WHERE REC_ID=" + setting.getRecId()) ? updateSetting(setting) : saveSetting(setting);
    }

    public boolean saveSetting(AXSetting setting) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_SETTING(REC_ID, CODE, VALUE, MODULE, DESCRIPTION, SYS_USER, SYS_DATE) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_SETTING.NEXTVAL, '" + setting.getCode() + "', '" + (!String.valueOf(setting.getValue()).contains(",") ? (setting.isEncrypted() && !getCypher().isEncrypted(setting.getValue()) ? getCypher().encrypt(setting.getValue()) : setting.getValue()) : "[]") + "', '" + setting.getModule() + "', '" + setting.getDescription() + "', '" + setting.getSysUser() + "', SYSDATE)") && (!String.valueOf(setting.getValue()).contains(",") || saveList(setting));
    }

    public boolean updateSetting(AXSetting setting) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHL_SETTING SET VALUE='" + (!String.valueOf(setting.getValue()).contains(",") ? (setting.isEncrypted() && !getCypher().isEncrypted(setting.getValue()) ? getCypher().encrypt(setting.getValue()) : setting.getValue()) : "[]") + "', DESCRIPTION='" + setting.getDescription() + "', SYS_USER='" + setting.getSysUser() + "', SYS_DATE=SYSDATE WHERE REC_ID=" + setting.getRecId()) && (!String.valueOf(setting.getValue()).contains(",") || saveList(setting));
    }

    public boolean deleteSetting(AXSetting setting) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHL_SETTING WHERE REC_ID=" + setting.getRecId()) && deleteList(setting);
    }

    private boolean saveList(AXSetting setting) {
        boolean RC = deleteList(setting);
        for (String value : getWorker().createArrayList(setting.getValue())) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_LIST(REC_ID, PARENT, VALUE) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_LIST.NEXTVAL, " + setting.getRecId() + ", '" + escape(value) + "')");
        }
        return RC;
    }

    private boolean deleteList(AXSetting setting) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHL_LIST WHERE PARENT=" + setting.getRecId());
    }

    public boolean upsertTxn(AXTxn axTxn) {
        return getWorker().isBlank(axTxn.getRecId()) ? saveTxn(axTxn) : updateTxn(axTxn);
    }

    private boolean saveTxn(AXTxn axTxn) {
        try (PLAdapter adapter = getAdapter()) {
            PreparedStatement psInsert = adapter.getConnection().prepareStatement(cleanUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_TXN_LOG(REC_ID, TXN_REF, TXN_DATE, CHANNEL_ID, MODULE, CLIENT, TXN_CODE, TXN_TYPE, BU_ID, ACQUIRER, TERMINAL, ADVICE, ONUS, ACCESS_CD, ACCOUNT, CONTRA, CURRENCY, AMOUNT, DESCRIPTION, DETAIL, RECEIPT, CHARGE_LEDGER, CHARGE, TXN_ID, CHG_ID, BALANCE, XAPI_CODE, RESULT, RESP_CODE, REC_ST, CALLER) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_TXN_LOG.NEXTVAL, '" + axTxn.getTxnRef() + "', SYSDATE, " + axTxn.getChannelId() + ", '" + axTxn.getModule() + "', '" + axTxn.getClient() + "', '" + axTxn.getTxnCode() + "', '" + axTxn.getTxnType() + "', " + axTxn.getBuId() + ", '" + axTxn.getAcquirer() + "', '" + axTxn.getTerminal() + "', '" + axTxn.getAdvice() + "', '" + axTxn.getOnus() + "', '" + axTxn.getAccessCd() + "', '" + axTxn.getAccount() + "', '" + axTxn.getContra() + "', '" + axTxn.getCurrency() + "', " + axTxn.getAmount() + ", '" + axTxn.getDescription() + "', '" + axTxn.getDetail() + "', '" + axTxn.getReceipt() + "', '" + axTxn.getChargeLedger() + "', " + axTxn.getCharge() + ", " + axTxn.getTxnId() + ", " + axTxn.getChgId() + ", " + axTxn.getBalance() + ", '" + axTxn.getXapiCode() + "', '" + axTxn.getResult() + "', '" + axTxn.getRespCode() + "', '" + axTxn.getRecSt() + "', ?)"));
            psInsert.setBytes(1, getWorker().convertToBytes(axTxn.getCaller()));
            return psInsert.execute();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return false;
    }

    public boolean updateTxn(AXTxn axTxn) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHL_TXN_LOG SET BALANCE=" + axTxn.getBalance() + ", RECEIPT='" + axTxn.getReceipt() + "', REC_ST='" + axTxn.getRecSt() + "' WHERE REC_ID=" + axTxn.getRecId());
    }

    public boolean updateReceipt(Long channelId, String txnRef, String receipt) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHL_TXN_LOG SET RECEIPT='" + receipt + "' WHERE CHANNEL_ID = " + channelId + " AND TXN_REF='" + txnRef + "' AND TXN_DATE>=SYSDATE-30");
    }

    public BigDecimal queryTxnAmount(Long txnId) {
        BigDecimal amount = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT TRAN_AMT FROM " + APController.coreSchemaName + ".TXN_JOURNAL WHERE TRAN_JOURNAL_ID=" + txnId)) {
            if (rs.next()) {
                amount = rs.getBigDecimal("TRAN_AMT");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return amount;
    }

    public Long queryApplId(Long custId) {
        Long applId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT APPL_ID FROM " + APController.coreSchemaName + ".CREDIT_APPL WHERE CUST_ID=" + custId + " ORDER BY APPL_ID DESC")) {
            if (rs != null && rs.next()) {
                applId = rs.getLong("APPL_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return applId;
    }

    public Long queryApplId(String acctNo) {
        Long applId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT APPL_ID FROM " + APController.cmSchemaName + ".VMT_LOAN WHERE ACCT_NO='" + acctNo + "' ORDER BY REC_ID DESC")) {
            if (rs != null && rs.next()) {
                applId = rs.getLong("APPL_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return applId;
    }

    public TreeMap<String, MXAlert> queryAlerts() {
        TreeMap<String, MXAlert> alerts = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT REC_ID, CODE, "
                        + "TYPE, DESCRIPTION, PRIORITY, RUN_TIME, CHRG_CODE, "
                        + "FREQUENCY, DAYS, NEXT_DATE, LAST_DATE, EXPIRY_DATE, "
                        + "FILTER_BY, REC_ST FROM "
                        + "" + APController.cmSchemaName + ".PHA_ALERT "
                        + " WHERE REC_ST = 'A' " /*ONLY Active Alerts*/
                        + "ORDER BY CODE ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    MXAlert mXAlert = new MXAlert();
                    mXAlert.setRecId(rs.getLong("REC_ID"));
                    mXAlert.setCode(rs.getString("CODE"));
                    mXAlert.setType(rs.getString("TYPE"));
                    mXAlert.setDescription(rs.getString("DESCRIPTION"));
                    mXAlert.setPriority(rs.getInt("PRIORITY"));
                    mXAlert.setRunTime(rs.getString("RUN_TIME"));
                    mXAlert.setDays(rs.getString("DAYS"));
                    mXAlert.setChargeCode(rs.getString("CHRG_CODE"));
                    mXAlert.setFrequency(rs.getString("FREQUENCY"));
                    mXAlert.setNextDate(rs.getDate("NEXT_DATE"));
                    mXAlert.setPreviousDate(rs.getDate("LAST_DATE"));
                    mXAlert.setExpiryDate(rs.getDate("EXPIRY_DATE"));
                    mXAlert.setFilterBy(rs.getString("FILTER_BY"));
                    mXAlert.setStatus(rs.getString("REC_ST"));
                    mXAlert.setTemplates(queryTemplates(ALController.module, mXAlert.getCode()));
                    mXAlert.setFilters(queryFilters(ALController.module, mXAlert.getCode()));
                    alerts.put(mXAlert.getCode(), mXAlert);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return alerts;
    }

    public TreeMap<String, ESTask> queryTasks() {
        TreeMap<String, ESTask> tasks = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, REC_CD, CREATE_DT, DOCUMENT, RANGE, DESCRIPTION, RUN_TIME, CHARGE, CYCLE, NEXT_DATE, LAST_DATE, EXPIRY_DATE, FILTER_BY, REC_ST FROM " + APController.cmSchemaName + ".PHS_TASK ORDER BY REC_CD ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    ESTask eSTask = new ESTask();
                    eSTask.setRecId(rs.getLong("REC_ID"));
                    eSTask.setCode(rs.getString("REC_CD"));
                    eSTask.setDocument(rs.getString("DOCUMENT"));
                    eSTask.setRange(rs.getString("RANGE"));
                    eSTask.setDescription(rs.getString("DESCRIPTION"));
                    eSTask.setRunTime(rs.getString("RUN_TIME"));
                    eSTask.setCharge(rs.getString("CHARGE"));
                    eSTask.setCycle(rs.getString("CYCLE"));
                    eSTask.setNextDate(rs.getDate("NEXT_DATE"));
                    eSTask.setPreviousDate(rs.getDate("LAST_DATE"));
                    eSTask.setExpiryDate(rs.getDate("EXPIRY_DATE"));
                    eSTask.setFilterBy(rs.getString("FILTER_BY"));
                    eSTask.setStatus(rs.getString("REC_ST"));
                    eSTask.setFilters(queryFilters(ESController.module, eSTask.getCode()));
                    tasks.put(eSTask.getCode(), eSTask);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return tasks;
    }

    public TreeMap<String, AXSetting> querySettings(String module) {
        TreeMap<String, AXSetting> settings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CODE, VALUE, MODULE, DESCRIPTION, SYS_USER, SYS_DATE FROM " + APController.cmSchemaName + ".PHL_SETTING WHERE MODULE LIKE '" + module + "' ORDER BY CODE ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXSetting setting = new AXSetting();
                    setting.setRecId(rs.getLong("REC_ID"));
                    setting.setCode(rs.getString("CODE"));
                    setting.setEncrypted(getCypher().isEncrypted(rs.getString("VALUE")));
                    setting.setModule(rs.getString("MODULE"));
                    setting.setDescription(rs.getString("DESCRIPTION"));
                    setting.setSysUser(rs.getString("SYS_USER"));
                    setting.setSysDate(rs.getDate("SYS_DATE"));
                    setting.setValue(setting.isEncrypted() ? getCypher().decrypt(rs.getString("VALUE")) : (Objects.equals(rs.getString("VALUE"), "[]") ? getWorker().createCsvList(queryList(setting)) : rs.getString("VALUE")));
                    settings.put(setting.getCode(), setting);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return settings;
    }

    private ArrayList<String> queryList(AXSetting setting) {
        ArrayList<String> list = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT DISTINCT VALUE FROM " + APController.cmSchemaName + ".PHL_LIST WHERE VALUE IS NOT NULL AND PARENT=" + setting.getRecId())) {
            if (rs != null) {
                while (rs.next()) {
                    list.add(rs.getString("VALUE"));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return list;
    }

    public boolean upsertTerminal(AXTerminal terminal) {
        return checkExists("SELECT REC_CD FROM " + APController.cmSchemaName + ".PHT_TERMINAL WHERE REC_CD='" + terminal.getTerminalId() + "' AND MODULE='" + terminal.getModule() + "'") ? updateTerminal(terminal) : saveTerminal(terminal);
    }

    private boolean saveTerminal(AXTerminal terminal) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHT_TERMINAL(REC_CD, MODULE, SCHEME, LOCATION, OPERATOR, BU_NO, SYS_USER, SYS_DATE, REC_ST) VALUES('" + terminal.getTerminalId() + "', '" + terminal.getModule() + "', '" + terminal.getScheme() + "', '" + escape(terminal.getLocation()) + "', '" + terminal.getOperator() + "', '" + terminal.getBuCode() + "', '" + terminal.getSysUser() + "', SYSDATE, '" + terminal.getStatus() + "')") && saveTerminalAccounts(terminal);
    }

    private boolean updateTerminal(AXTerminal terminal) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHT_TERMINAL SET MODULE='" + terminal.getModule() + "', SCHEME='" + terminal.getScheme() + "', LOCATION='" + terminal.getLocation() + "', OPERATOR='" + terminal.getOperator() + "', BU_NO='" + terminal.getBuCode() + "', SYS_USER='" + terminal.getSysUser() + "', SYS_DATE=SYSDATE, REC_ST='" + terminal.getStatus() + "' WHERE REC_CD='" + terminal.getTerminalId() + "'") ? saveTerminalAccounts(terminal) : false;
    }

    private boolean saveTerminalAccounts(AXTerminal terminal) {
        boolean RC = deleteTerminalAccounts(terminal);
        for (String currency : terminal.getAccounts().keySet()) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHT_ACCOUNT(REC_ID, PARENT, CURRENCY, ACCOUNT) VALUES(" + APController.cmSchemaName + ".SEQ_PHT_ACCOUNT.NEXTVAL, '" + terminal.getTerminalId() + "', '" + currency + "', '" + terminal.getAccounts().get(currency).getAccountNumber() + "')");
        }
        return RC;
    }

    private boolean deleteTerminalAccounts(AXTerminal terminal) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHT_ACCOUNT WHERE PARENT='" + terminal.getTerminalId() + "'");
    }

    public TreeMap<String, AXTerminal> queryTerminals(String module) {
        TreeMap<String, AXTerminal> terminals = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_CD, MODULE, SCHEME, LOCATION, OPERATOR, BU_NO, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHT_TERMINAL WHERE MODULE='" + module + "' ORDER BY REC_CD ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXTerminal terminal = new AXTerminal();
                    terminal.setTerminalId(rs.getString("REC_CD"));
                    terminal.setModule(rs.getString("MODULE"));
                    terminal.setScheme(rs.getString("SCHEME"));
                    terminal.setLocation(rs.getString("LOCATION"));
                    terminal.setOperator(rs.getString("OPERATOR"));
                    terminal.setBuCode(rs.getString("BU_NO"));
                    terminal.setSysUser(rs.getString("SYS_USER"));
                    terminal.setSysDate(rs.getDate("SYS_DATE"));
                    terminal.setStatus(rs.getString("REC_ST"));
                    terminals.put(terminal.getTerminalId(), terminal);
                }
            }
            terminals.values().stream().map((terminal)
                    -> {
                terminal.setAccounts(queryTerminalAccounts(terminal.getTerminalId(), true));
                return terminal;
            }).forEach((terminal)
                    -> {
                terminals.put(terminal.getTerminalId(), terminal);
            });
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return terminals;
    }

    public AXTerminal queryTerminal(String module, String terminalId, boolean retry) {
        AXTerminal terminal = new AXTerminal();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_CD, MODULE, SCHEME, LOCATION, OPERATOR, BU_NO, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHT_TERMINAL WHERE MODULE='" + module + "' AND REC_CD='" + terminalId + "' AND REC_ST='A' ORDER BY REC_CD ASC")) {
            if (rs != null && rs.next()) {
                terminal.setTerminalId(rs.getString("REC_CD"));
                terminal.setModule(rs.getString("MODULE"));
                terminal.setScheme(rs.getString("SCHEME"));
                terminal.setLocation(rs.getString("LOCATION"));
                terminal.setOperator(rs.getString("OPERATOR"));
                terminal.setBuCode(rs.getString("BU_NO"));
                terminal.setSysUser(rs.getString("SYS_USER"));
                terminal.setSysDate(rs.getDate("SYS_DATE"));
                terminal.setStatus(rs.getString("REC_ST"));
                terminal.setAccounts(queryTerminalAccounts(terminal.getTerminalId(), true));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return terminal;
    }

    public HashMap<String, CNAccount> queryTerminalAccounts(String terminalId, boolean retry) {
        HashMap<String, CNAccount> accounts = new HashMap<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CURRENCY, ACCOUNT FROM " + APController.cmSchemaName + ".PHT_ACCOUNT WHERE PARENT='" + terminalId + "' ORDER BY REC_ID ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    accounts.put(rs.getString("CURRENCY"), queryAnyAccount(rs.getString("ACCOUNT")));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return accounts;
    }

    public boolean isDue(Long accountId, Long datefieldId, Date minDate) {
        return checkExists("SELECT A.PARENT_ID FROM " + APController.coreSchemaName + ".UDS_FIELD_VALUE A WHERE A.PARENT_ID=" + accountId + " AND A.FIELD_ID=" + datefieldId + " AND A.REC_ST='A' AND (A.FIELD_VALUE IS NULL OR TO_DATE(A.FIELD_VALUE,'DD/MM/YYYY')<=" + convertToOracleDate(minDate) + ")");
    }

    public void updateXapiErrors() {
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT ERROR_CODE, ERROR_DESC FROM " + APController.coreSchemaName + ".ERROR_CODE_DESCRIPTION_REF ORDER BY ERROR_CODE ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    if (!APController.getCodes().containsKey(rs.getString("ERROR_CODE"))) {
                        APController.getCodes().put(rs.getString("ERROR_CODE"), rs.getString("ERROR_DESC"));
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public HashMap<String, String> queryTemplates(String module, String parentKey) {
        HashMap<String, String> templates = new HashMap<>();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT LANGUAGE, TEMPLATE "
                        + "FROM " + APController.cmSchemaName + ".PHL_TEMPLATE "
                        + "WHERE MODULE='" + module + "' AND "
                        + "PARENT='" + parentKey + "' ORDER BY LANGUAGE ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString("TEMPLATE") != null) {
                        templates.put(rs.getString("LANGUAGE"), rs.getString("TEMPLATE"));
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return templates;
    }

    public ArrayList<String> queryFilters(String module, String parentKey) {
        ArrayList<String> filters = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT DISTINCT FILTER FROM " + APController.cmSchemaName + ".PHL_FILTER WHERE MODULE='" + module + "' AND PARENT='" + parentKey + "'")) {
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString("FILTER") != null) {
                        filters.add(rs.getString("FILTER"));
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return filters;
    }

    private boolean saveTemplates(String module, String parentKey, HashMap<String, String> templates) {
        boolean RC = false;
        if (!templates.isEmpty()) {
            RC = deleteTemplates(module, parentKey);
        }
        for (String language : templates.keySet()) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_TEMPLATE(REC_ID, PARENT, MODULE, LANGUAGE, TEMPLATE) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_TEMPLATE.NEXTVAL, '" + parentKey + "', '" + module + "', '" + language + "', '" + String.valueOf(templates.get(language)).replace("'", "''") + "')");
        }
        return RC;
    }

    private boolean deleteTemplates(String module, String parentKey) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHL_TEMPLATE WHERE MODULE='" + module + "' AND PARENT='" + parentKey + "'");
    }

    private boolean saveFilters(String module, String parentKey, ArrayList<String> filters) {
        boolean RC = deleteFilters(module, parentKey);
        for (String filter : filters) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_FILTER(REC_ID, PARENT, MODULE, FILTER) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_FILTER.NEXTVAL, '" + parentKey + "', '" + module + "', '" + escape(filter) + "')");
        }
        return RC;
    }

    private boolean deleteFilters(String module, String parentKey) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHL_FILTER WHERE MODULE='" + module + "' AND PARENT='" + parentKey + "'");
    }

    public boolean deleteAlert(MXAlert mXAlert) {
        return deleteTemplates(ALController.module, mXAlert.getCode()) && deleteFilters(ALController.module, mXAlert.getCode()) && executeUpdate("DELETE " + APController.cmSchemaName + ".PHA_ALERT WHERE CODE='" + mXAlert.getCode() + "'");
    }

    public boolean deleteTask(ESTask eSTask) {
        return deleteTemplates(ALController.module, eSTask.getCode()) && deleteFilters(ALController.module, eSTask.getCode()) && executeUpdate("DELETE " + APController.cmSchemaName + ".PHS_TASK WHERE REC_CD='" + eSTask.getCode() + "'");
    }

    public boolean saveSplit(AXSplit split) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_SPLIT(REC_ID, TXN_DT, TXN_REF, MODULE, DR_ACCT, CR_ACCT, CURRENCY, AMOUNT, DESCRIPTION, REVERSAL, LUMP, FIELD, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHC_SPLIT.NEXTVAL, SYSDATE, '" + split.getTxnRef() + "', '" + split.getModule() + "', '" + split.getDebitAccount() + "', '" + split.getCreditAccount() + "', '" + split.getCurrency() + "', " + split.getAmount() + ", '" + split.getDescription() + "', '" + split.getReversal() + "', '" + split.getLump() + "', '" + split.getField() + "', '" + split.getStatus() + "')");
    }

    public boolean updateSplit(AXSplit split) {
        return Objects.equals(split.getLump(), "Y") ? executeUpdate("UPDATE " + APController.cmSchemaName + ".PHC_SPLIT SET REC_ST='" + split.getStatus() + "' WHERE LUMP='Y' AND MODULE='" + split.getModule() + "' AND DR_ACCT='" + split.getDebitAccount() + "' AND CR_ACCT='" + split.getCreditAccount() + "' AND CURRENCY='" + split.getCurrency() + "' AND DESCRIPTION='" + split.getDescription() + "' AND REVERSAL='" + split.getReversal() + "' AND TRUNC(TXN_DT)=" + convertToOracleDate(split.getTxnDate()), true) : executeUpdate("UPDATE " + APController.cmSchemaName + ".PHC_SPLIT SET REC_ST='" + split.getStatus() + "' WHERE REC_ID=" + split.getRecId(), true);
    }

    public boolean updateChargeId(String module, String refence, Long chargeId) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHL_TXN_LOG SET CHG_ID='" + chargeId + "' WHERE MODULE='" + module + "' AND TXN_REF='" + refence + "'");
    }

    public ArrayList<AXSplit> querySplits() {
        ArrayList<AXSplit> splits = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(true); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT MODULE, DR_ACCT, CR_ACCT, CURRENCY, SUM(AMOUNT) AS AMOUNT, DESCRIPTION, TRUNC(TXN_DT) AS TXN_DT, REVERSAL, LUMP FROM " + APController.cmSchemaName + ".PHC_SPLIT WHERE REC_ST IN ('P') AND LUMP='Y'" + (APController.isEndOfYear() || getWorker().isYes(APController.splitOnFly) ? "" : " AND TRUNC(TXN_DT)<=TRUNC(SYSDATE-1)") + " GROUP BY MODULE, DR_ACCT, CR_ACCT, CURRENCY, DESCRIPTION, TRUNC(TXN_DT), REVERSAL, LUMP")) {
            if (rs != null) {
                while (rs.next()) {
                    AXSplit split = new AXSplit();
                    split.setModule(rs.getString("MODULE"));
                    split.setDebitAccount(rs.getString("DR_ACCT"));
                    split.setCreditAccount(rs.getString("CR_ACCT"));
                    split.setCurrency(rs.getString("CURRENCY"));
                    split.setAmount(rs.getBigDecimal("AMOUNT"));
                    split.setDescription(rs.getString("DESCRIPTION"));
                    split.setReversal(rs.getString("REVERSAL"));
                    split.setTxnDate(rs.getDate("TXN_DT"));
                    split.setLump(rs.getString("LUMP"));
                    splits.add(split);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, TXN_DT, TXN_REF, MODULE, DR_ACCT, CR_ACCT, CURRENCY, AMOUNT, DESCRIPTION, REVERSAL, LUMP, FIELD, REC_ST FROM " + APController.cmSchemaName + ".PHC_SPLIT WHERE REC_ST IN ('P') AND LUMP='N' AND TXN_DT>=SYSDATE-" + APController.pendingTxnExpiryDays)) {
            if (rs != null) {
                while (rs.next()) {
                    AXSplit split = new AXSplit();
                    split.setRecId(rs.getLong("REC_ID"));
                    split.setTxnDate(rs.getDate("TXN_DT"));
                    split.setTxnRef(rs.getString("TXN_REF"));
                    split.setModule(rs.getString("MODULE"));
                    split.setDebitAccount(rs.getString("DR_ACCT"));
                    split.setCreditAccount(rs.getString("CR_ACCT"));
                    split.setCurrency(rs.getString("CURRENCY"));
                    split.setAmount(rs.getBigDecimal("AMOUNT"));
                    split.setDescription(rs.getString("DESCRIPTION"));
                    split.setReversal(rs.getString("REVERSAL"));
                    split.setLump(rs.getString("LUMP"));
                    split.setField(rs.getString("FIELD"));
                    split.setStatus(rs.getString("REC_ST"));
                    splits.add(split);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return splits;
    }

    public HashMap<String, WFRecord> queryGroupWorkflowRecords(String txnRef, String account, BigDecimal amount) {
        HashMap<String, WFRecord> records = new HashMap<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CREATE_DT, TXN_REF, ACCESS_CD, GROUP_NO, MEMBER_NO, ROLE, ACCOUNT, CURRENCY, AMOUNT, DETAIL, REC_ST FROM " + APController.cmSchemaName + ".PHG_WF_LOG WHERE TXN_REF='" + txnRef + "' AND ACCOUNT='" + account + "' AND AMOUNT=" + amount + " AND CREATE_DT>SYSDATE-7 AND REC_ST='A' ORDER BY REC_ID ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    WFRecord record = new WFRecord();
                    record.setRecId(rs.getLong("REC_ID"));
                    record.setCreateDt(rs.getDate("CREATE_DT"));
                    record.setTxnRef(rs.getString("TXN_REF"));
                    record.setAccessCd(rs.getString("ACCESS_CD"));
                    record.setGroupNo(rs.getString("GROUP_NO"));
                    record.setMemberNo(rs.getString("MEMBER_NO"));
                    record.setRole(rs.getString("ROLE"));
                    record.setAccount(rs.getString("ACCOUNT"));
                    record.setCurrency(rs.getString("CURRENCY"));
                    record.setAmount(rs.getBigDecimal("AMOUNT"));
                    record.setDetail(rs.getString("DETAIL"));
                    record.setStatus(rs.getString("REC_ST"));
                    records.put(record.getRole(), record);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return records;
    }

    public boolean moveCustomerWFItem(Long custId, Long newBuId) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".WF_WORK_ITEM SET BU_ID=" + newBuId + " WHERE WORK_ITEM_ID=(SELECT MAX(WORK_ITEM_ID) FROM " + APController.coreSchemaName + ".WF_WORK_ITEM WHERE CUST_ID=" + custId + ")");
    }

    public CFValue queryCustomField(Long fieldId, Long parentId) {
        CFValue cFValue = new CFValue();
        try (PLAdapter adapter = getAdapter();
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT A.UDS_FIELD_VALUE_ID, "
                        + "A.FIELD_ID, A.PARENT_ID, A.FIELD_VALUE, A.REC_ST "
                        + "FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE A "
                        + "WHERE A.FIELD_ID=" + fieldId + " "
                        + "AND A.PARENT_ID=" + parentId + " AND A.REC_ST='A'")) {
            if (rs != null && rs.next()) {
                cFValue.setValueId(rs.getLong("UDS_FIELD_VALUE_ID"));
                cFValue.setFieldId(rs.getLong("FIELD_ID"));
                cFValue.setParentId(rs.getLong("PARENT_ID"));
                cFValue.setFieldValue(rs.getString("FIELD_VALUE"));
                cFValue.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cFValue;
    }

    public <T> T queryCustomField(Long fieldId, Long parentId, T defaultValue) {
        T value = defaultValue;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.UDS_FIELD_VALUE_ID, A.FIELD_ID, A.PARENT_ID, A.FIELD_VALUE, A.REC_ST FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE A WHERE A.FIELD_ID=" + fieldId + " AND A.PARENT_ID=" + parentId + " AND A.REC_ST='A'")) {
            if (rs != null && rs.next()) {
                value = (T) rs.getObject("FIELD_VALUE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return getWorker().checkBlank(value, defaultValue);
    }

    public Date queryCustomDate(Long fieldId, Long parentId, Date defaultDate) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.UDS_FIELD_VALUE_ID, A.FIELD_ID, A.PARENT_ID, TO_DATE(A.FIELD_VALUE,'DD/MM/YYYY') AS FIELD_VALUE, A.REC_ST FROM " + APController.coreSchemaName + ".V_UDS_FIELD_VALUE A WHERE A.FIELD_ID=" + fieldId + " AND A.PARENT_ID=" + parentId + " AND A.REC_ST='A'")) {
            if (rs != null && rs.next()) {
                return getWorker().checkBlank(rs.getDate("FIELD_VALUE"), defaultDate);
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return defaultDate;
    }

    public ArrayList<String> queryContacts(Long custId, String contactType) {
        ArrayList<String> contacts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT O.CONTACT FROM " + APController.coreSchemaName + ".V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = " + custId + " AND O.CONTACT_MODE_CAT_CD IN ('" + contactType + "')")) {
            if (rs != null) {
                while (rs.next()) {
                    contacts.addAll(getWorker().createArrayList(rs.getString("CONTACT")));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return contacts;
    }

    public String queryTimeText() {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT TO_CHAR(SYSTIMESTAMP, 'YYYY-MM-DD HH24:MI:SS') AS NOW FROM DUAL")) {
            if (rs != null && rs.next()) {
                return rs.getString("NOW");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public String queryMobileContact(Long custId) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT NVL((SELECT U.ACCESS_CD FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U WHERE U.CUST_ID = " + custId + " AND U.CHANNEL_ID=" + VXController.channelId + " AND U.USER_CAT_CD='PER' AND U.REC_ST='A' AND ROWNUM=1),(SELECT O.CONTACT FROM " + APController.coreSchemaName + ".V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = " + custId + " AND O.CONTACT_MODE_CAT_CD IN ('MOBPHONE','TELPHONE') AND ROWNUM=1)) AS CONTACT FROM DUAL")) {
            if (rs != null && rs.next()) {
                return rs.getString("CONTACT");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public boolean verifyEmail(Long custId, String emailAddress) {
        return checkExists("SELECT O.CONTACT FROM " + APController.coreSchemaName + ".V_CUSTOMER_CONTACT_MODE O WHERE O.CUST_ID = " + custId + " AND O.CONTACT_MODE_CAT_CD IN ('EMAIL') AND UPPER(O.CONTACT) LIKE UPPER('%" + getWorker().checkBlank(emailAddress, "<>") + "%')");
    }

    public CNCurrency queryCurrency(Long id) {
        CNCurrency cNCurrency = new CNCurrency();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CRNCY_ID, CRNCY_CD, INITCAP(CRNCY_NM) AS CRNCY_NM, CRNCY_POSN FROM " + APController.coreSchemaName + ".CURRENCY WHERE CRNCY_ID=" + id + " AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                cNCurrency.setId(rs.getLong("CRNCY_ID"));
                cNCurrency.setCode(rs.getString("CRNCY_CD"));
                cNCurrency.setName(rs.getString("CRNCY_NM"));
                cNCurrency.setPoints(rs.getInt("CRNCY_POSN"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCurrency;
    }

    public CNCurrency queryCurrency(String code) {
        CNCurrency cNCurrency = new CNCurrency();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CRNCY_ID, CRNCY_CD, INITCAP(CRNCY_NM) AS CRNCY_NM, CRNCY_POSN FROM " + APController.coreSchemaName + ".CURRENCY WHERE CRNCY_CD='" + code + "' AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                cNCurrency.setId(rs.getLong("CRNCY_ID"));
                cNCurrency.setCode(rs.getString("CRNCY_CD"));
                cNCurrency.setName(rs.getString("CRNCY_NM"));
                cNCurrency.setPoints(rs.getInt("CRNCY_POSN"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNCurrency;
    }

    public ArrayList<CNCurrency> queryCurrencies() {
        ArrayList<CNCurrency> currencies = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CRNCY_ID, CRNCY_CD, INITCAP(CRNCY_NM) AS CRNCY_NM, CRNCY_POSN FROM " + APController.coreSchemaName + ".CURRENCY WHERE REC_ST='A' ORDER BY CRNCY_ID")) {
            if (rs != null) {
                while (rs.next()) {
                    CNCurrency cNCurrency = new CNCurrency();
                    cNCurrency.setId(rs.getLong("CRNCY_ID"));
                    cNCurrency.setCode(rs.getString("CRNCY_CD"));
                    cNCurrency.setName(rs.getString("CRNCY_NM"));
                    cNCurrency.setPoints(rs.getInt("CRNCY_POSN"));
                    currencies.add(cNCurrency);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return currencies;
    }

    public HashMap<String, BigDecimal> queryCustomerChannelLimits(Long custChannelId, Long currencyId) {
        HashMap<String, BigDecimal> limits = new HashMap<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT LIMIT_TY_CD, DAILY_LIMIT FROM " + APController.coreSchemaName + ".CUST_CHANNEL_LIMIT WHERE CUST_CHANNEL_ID=" + custChannelId + " AND DAILY_LIMIT IS NOT NULL AND LIMIT_CRNCY_ID=" + currencyId + " AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    limits.put(rs.getString("LIMIT_TY_CD"), rs.getBigDecimal("DAILY_LIMIT"));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return limits;
    }

    public BNUser queryBankUser(String userName, String password, Long channelId) {
        BNUser user = new BNUser();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.SYSUSER_ID, A.LOGIN_ID, A.FIRST_NM || ' ' || A.LAST_NM AS NAME, A.EMP_NO, A.MAIN_BRANCH_ID, A.BU_NM, A.ROLE_ID, A.ROLE_NM FROM " + APController.coreSchemaName + ".V_SYSUSER A, " + APController.coreSchemaName + ".SYSPWD_HIST B WHERE A.LOCKED_FG='N' AND A.LOGIN_ID='" + userName + "' AND B.SYSUSER_ID=A.SYSUSER_ID AND A.REC_ST=B.REC_ST AND B.PASSWD='" + getCypher().encrypt(password) + "' AND B.REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    user.setUserId(rs.getLong("SYSUSER_ID"));
                    user.setUserName(rs.getString("LOGIN_ID"));
                    user.setBranchId(rs.getLong("MAIN_BRANCH_ID"));
                    user.setBranchName(rs.getString("BU_NM"));
                    user.setStaffName(rs.getString("NAME"));
                    user.setRoleId(rs.getLong("ROLE_ID"));
                    user.setStaffNumber(rs.getString("EMP_NO"));
                    user.setRole(rs.getString("ROLE_NM"));
                    user.setRoles(queryUserRoles(user, channelId));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return user;
    }

    public ArrayList<USRole> queryUserRoles(BNUser user, Long channelId) {
        ArrayList<USRole> roles = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT USER_ID, LOGIN_ID, BU_ID, BU_CD, BU_NAME, USER_ROLE_ID, BU_ROLE_ID, SUPERVISOR_FLAG, BUSINESS_ROLE_ID, BU_ROLE_NAME, BUSINESS_ROLE_NM, DEFAULT_ROLE_FLAG FROM " + APController.coreSchemaName + ".V_USER_ROLE WHERE USER_ID=" + user.getUserId() + " AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    USRole role = new USRole();
                    role.setUserId(rs.getLong("USER_ID"));
                    role.setUserName(rs.getString("LOGIN_ID"));
                    role.setBranchId(rs.getLong("BU_ID"));
                    role.setBranchCode(rs.getString("BU_CD"));
                    role.setBranchName(rs.getString("BU_NAME"));
                    role.setUserRoleId(rs.getLong("USER_ROLE_ID"));
                    role.setBuRoleId(rs.getLong("BU_ROLE_ID"));
                    role.setSupervisor(rs.getString("SUPERVISOR_FLAG"));
                    role.setRoleId(rs.getLong("BUSINESS_ROLE_ID"));
                    role.setRole(rs.getString("BU_ROLE_NAME"));
                    if (getWorker().isYes(rs.getString("DEFAULT_ROLE_FLAG"))) {
                        user.setRoleId(role.getRoleId());
                        user.setRole(role.getRole());
                    }
                    role.setDrawers(queryUserDrawers(role.getUserId(), role.getUserRoleId()));
                    role.setLimits(queryRoleLimits(role.getRoleId(), channelId));
                    roles.add(role);
                }
            }
            if (roles.size() == 1) {
                user.setRoleId(roles.get(0).getRoleId());
                user.setRole(roles.get(0).getRole());
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return roles;
    }

    public ArrayList<USRole> queryUserRoles(Long userId, Long channelId, boolean activeOnly) {
        ArrayList<USRole> roles = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT USER_ID, LOGIN_ID, BU_ID, BU_CD, BU_NAME, USER_ROLE_ID, BU_ROLE_ID, SUPERVISOR_FLAG, BUSINESS_ROLE_ID, BU_ROLE_NAME, BUSINESS_ROLE_NM, DEFAULT_ROLE_FLAG FROM " + APController.coreSchemaName + ".V_USER_ROLE WHERE USER_ID=" + userId + (activeOnly ? " AND REC_ST='A'" : ""))) {
            if (rs != null) {
                while (rs.next()) {
                    USRole role = new USRole();
                    role.setUserId(rs.getLong("USER_ID"));
                    role.setUserName(rs.getString("LOGIN_ID"));
                    role.setBranchId(rs.getLong("BU_ID"));
                    role.setBranchCode(rs.getString("BU_CD"));
                    role.setBranchName(rs.getString("BU_NAME"));
                    role.setUserRoleId(rs.getLong("USER_ROLE_ID"));
                    role.setBuRoleId(rs.getLong("BU_ROLE_ID"));
                    role.setSupervisor(rs.getString("SUPERVISOR_FLAG"));
                    role.setRoleId(rs.getLong("BUSINESS_ROLE_ID"));
                    role.setRole(rs.getString("BU_ROLE_NAME"));
                    role.setDrawers(queryUserDrawers(role.getUserId(), role.getUserRoleId()));
                    role.setLimits(queryRoleLimits(role.getRoleId(), channelId));
                    roles.add(role);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return roles.isEmpty() && activeOnly ? queryUserRoles(userId, channelId, false) : roles;
    }

    public ArrayList<TLDrawer> queryUserDrawers(Long userId, Long userRoleId) {
        ArrayList<TLDrawer> drawers = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT V.DRAWER_ID, V.DRAWER_NO, R.GL_ACCT_NO, R.LAST_DRAWER_OPEN_DT, V.REC_ST FROM " + APController.coreSchemaName + ".V_DRAWER_USER_ROLES V, " + APController.coreSchemaName + ".DRAWER R WHERE R.DRAWER_ID=V.DRAWER_ID AND V.BU_ID=R.BU_ID AND V.USER_ROLE_ID=" + userRoleId + " AND V.SYSUSER_ID=" + userId + " AND V.DRAWER_ST='O' AND V.REC_ST='O'")) {
            if (rs != null) {
                while (rs.next()) {
                    TLDrawer drawer = new TLDrawer();
                    drawer.setDrawerId(rs.getLong("DRAWER_ID"));
                    drawer.setDrawerNumber(rs.getString("DRAWER_NO"));
                    drawer.setDrawerAccount(rs.getString("GL_ACCT_NO"));
                    drawer.setOpenDate(rs.getDate("LAST_DRAWER_OPEN_DT"));
                    drawer.setStatus(rs.getString("REC_ST"));
                    drawer.setCurrencies(queryDrawerCurrencies(drawer.getDrawerId()));
                    drawers.add(drawer);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return drawers;
    }

    public ArrayList<URLimit> queryRoleLimits(Long roleId, Long channelId) {
        ArrayList<URLimit> limits = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT ROLE_ID, CRNCY_CD_ISO, PREVENTIVE_CR_LIMIT, PREVENTIVE_DR_LIMIT FROM " + APController.coreSchemaName + ".V_BUSINESS_ROLE_CHANNEL_LIMIT WHERE REC_ST='A' AND CHANNEL_ID=" + channelId + " AND ROLE_ID=" + roleId)) {
            if (rs != null) {
                while (rs.next()) {
                    URLimit limit = new URLimit();
                    limit.setRoleId(rs.getLong("ROLE_ID"));
                    limit.setCurrency(rs.getString("CRNCY_CD_ISO"));
                    limit.setCreditLimit(rs.getBigDecimal("PREVENTIVE_CR_LIMIT"));
                    limit.setDebitLimit(rs.getBigDecimal("PREVENTIVE_DR_LIMIT"));
                    limits.add(limit);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return limits;
    }

    public ArrayList<CNCurrency> queryDrawerCurrencies(Long drawerId) {
        ArrayList<CNCurrency> currencies = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CRNCY_ID, CRNCY_CD, INITCAP(CRNCY_NM) AS CRNCY_NM, CRNCY_POSN FROM " + APController.coreSchemaName + ".CURRENCY WHERE CRNCY_ID IN (SELECT CRNCY_ID FROM " + APController.coreSchemaName + ".DRAWER_CURRENCY WHERE DRAWER_ID=" + drawerId + ") AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    CNCurrency currency = new CNCurrency();
                    currency.setId(rs.getLong("CRNCY_ID"));
                    currency.setCode(rs.getString("CRNCY_CD"));
                    currency.setName(rs.getString("CRNCY_NM"));
                    currency.setPoints(rs.getInt("CRNCY_POSN"));
                    currencies.add(currency);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return currencies;
    }

    public ArrayList<CNBranch> queryBusinessUnits() {
        ArrayList<CNBranch> branches = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT BU_ID, BU_NO, BU_NM, GL_PREFIX_CD, REC_ST FROM " + APController.coreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' ORDER BY BU_NO")) {
            if (rs != null) {
                while (rs.next()) {
                    CNBranch cNBranch = new CNBranch();
                    cNBranch.setBuId(rs.getLong("BU_ID"));
                    cNBranch.setBuCode(rs.getString("BU_NO"));
                    cNBranch.setBuName(rs.getString("BU_NM"));
                    cNBranch.setGlPrefix(rs.getString("GL_PREFIX_CD"));
                    cNBranch.setStatus(rs.getString("REC_ST"));
                    branches.add(cNBranch);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return branches;
    }

    public CNBranch queryBranch(Long buId) {
        CNBranch cNBranch = new CNBranch();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT BU_ID, BU_NO, BU_NM, GL_PREFIX_CD, REC_ST FROM " + APController.coreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' AND BU_ID=" + buId)) {
            if (rs != null && rs.next()) {
                cNBranch.setBuId(rs.getLong("BU_ID"));
                cNBranch.setBuCode(rs.getString("BU_NO"));
                cNBranch.setBuName(rs.getString("BU_NM"));
                cNBranch.setGlPrefix(rs.getString("GL_PREFIX_CD"));
                cNBranch.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNBranch;
    }

    public CNBranch queryBranch(String buNo) {
        CNBranch cNBranch = new CNBranch();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT BU_ID, BU_NO, BU_NM, GL_PREFIX_CD, REC_ST FROM " + APController.coreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' AND BU_NO='" + buNo + "'")) {
            if (rs != null && rs.next()) {
                cNBranch.setBuId(rs.getLong("BU_ID"));
                cNBranch.setBuCode(rs.getString("BU_NO"));
                cNBranch.setBuName(rs.getString("BU_NM"));
                cNBranch.setGlPrefix(rs.getString("GL_PREFIX_CD"));
                cNBranch.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNBranch;
    }

    public AXTxn queryTxn(Long channelId, String txnRef) {
        AXTxn axTxn = new AXTxn();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, TXN_REF, TXN_DATE, CHANNEL_ID, MODULE, CLIENT, TXN_CODE, TXN_TYPE, BU_ID, ACQUIRER, TERMINAL, ADVICE, ONUS, ACCESS_CD, ACCOUNT, CONTRA, CURRENCY, AMOUNT, DESCRIPTION, DETAIL, RECEIPT, CHARGE_LEDGER, CHARGE, TXN_ID, CHG_ID, BALANCE, XAPI_CODE, RESULT, RESP_CODE, REC_ST, CALLER FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE CHANNEL_ID = " + channelId + " AND TXN_REF='" + txnRef + "' AND TXN_DATE>=SYSDATE-30 AND REC_ST = 'A'")) {
            if (rs != null && rs.next()) {
                axTxn.setRecId(rs.getLong("REC_ID"));
                axTxn.setTxnRef(rs.getString("TXN_REF"));
                axTxn.setTxnDate(rs.getDate("TXN_DATE"));
                axTxn.setChannelId(rs.getLong("CHANNEL_ID"));
                axTxn.setModule(rs.getString("MODULE"));
                axTxn.setClient(rs.getString("CLIENT"));
                axTxn.setTxnCode(rs.getString("TXN_CODE"));
                axTxn.setTxnType(rs.getString("TXN_TYPE"));
                axTxn.setBuId(rs.getLong("BU_ID"));
                axTxn.setAcquirer(rs.getString("ACQUIRER"));
                axTxn.setTerminal(rs.getString("TERMINAL"));
                axTxn.setAdvice(rs.getString("ADVICE"));
                axTxn.setOnus(rs.getString("ONUS"));
                axTxn.setAccessCd(rs.getString("ACCESS_CD"));
                axTxn.setAccount(rs.getString("ACCOUNT"));
                axTxn.setContra(rs.getString("CONTRA"));
                axTxn.setCurrency(rs.getString("CURRENCY"));
                axTxn.setAmount(rs.getBigDecimal("AMOUNT"));
                axTxn.setDescription(rs.getString("DESCRIPTION"));
                axTxn.setDetail(rs.getString("DETAIL"));
                axTxn.setReceipt(rs.getString("RECEIPT"));
                axTxn.setChargeLedger(rs.getString("CHARGE_LEDGER"));
                axTxn.setCharge(rs.getBigDecimal("CHARGE"));
                axTxn.setTxnId(rs.getLong("TXN_ID"));
                axTxn.setChgId(rs.getLong("CHG_ID"));
                axTxn.setBalance(rs.getBigDecimal("BALANCE"));
                axTxn.setXapiCode(rs.getString("XAPI_CODE"));
                axTxn.setResult(rs.getString("RESULT"));
                axTxn.setRespCode(rs.getString("RESP_CODE"));
                axTxn.setRecSt(rs.getString("REC_ST"));
                axTxn.setCaller(getWorker().readObject(rs.getBinaryStream("CALLER"), VXCaller.class));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return axTxn;
    }

    public boolean isEndOfYear() {
        return checkExists("SELECT SYSDATE FROM DUAL WHERE TRUNC(SYSDATE)=ADD_MONTHS(TRUNC(SYSDATE ,'YEAR'),12)-1");
    }

    public boolean checkDuplicate(Long channelId, String txnRef, String txnCode, boolean reversed) {
        return checkExists("SELECT TXN_REF FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE CHANNEL_ID = " + channelId + " AND TXN_REF='" + txnRef + "' AND TXN_CODE='" + txnCode + "' AND TXN_DATE>=SYSDATE-30 AND REC_ST = '" + (reversed ? "R" : "A") + "'");
    }

    public boolean checkMatch(Long channelId, String txnRef) {
        return checkExists("SELECT TXN_REF FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE CHANNEL_ID = " + channelId + " AND TXN_REF='" + txnRef + "' AND TXN_DATE>=SYSDATE-30 AND REC_ST = 'A'");
    }

    public boolean checkTxn(Long channelId, Long custId, String txnCode, Integer days) {
        return checkExists("SELECT L.TXN_REF FROM " + APController.cmSchemaName + ".PHL_TXN_LOG L, " + APController.coreSchemaName + ".ACCOUNT A WHERE L.CHANNEL_ID = " + channelId + " AND L.TXN_CODE='" + txnCode + "' AND L.TXN_DATE>=SYSDATE-" + days + " AND L.ACCOUNT=A.ACCT_NO AND A.CUST_ID=" + custId + " AND L.REC_ST = 'A'");
    }

    public boolean checkMonthlyDeposits(Long custId, LNType type, Integer months) {
        for (int i = 1; i <= months; i++) {
            if (!checkExists("SELECT TRAN_DT FROM " + APController.coreSchemaName + ".DEPOSIT_ACCOUNT_HISTORY WHERE DR_CR_IND='CR'" + (type == LNType.Two ? " AND EVENT_ID IN (SELECT EVENT_ID FROM " + APController.coreSchemaName + ".EVENT WHERE EVENT_CD IN ('BATCHMLKCR', 'BATCHTEACR', 'BATCHSALCR', 'BONUSTEACR'))" : "") + " AND TRAN_DESC NOT LIKE 'REV~%' AND DEPOSIT_ACCT_ID IN (SELECT ACCT_ID FROM " + APController.coreSchemaName + ".ACCOUNT WHERE CUST_ID=" + custId + ") AND TRAN_DT>TRUNC(ADD_MONTHS((SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') AS PROC_DATE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'),-" + i + "))  AND TRAN_DT<=TRUNC(ADD_MONTHS((SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') AS PROC_DATE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'),-" + (i - 1) + ")) AND ROWNUM=1")) {
                return false;
            }
        }
        return true;
    }

    public BigDecimal queryIncomeAverage(Long custId, Integer months) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT (NVL(SUM(ACCT_AMT),0)/" + months + ") AS AVERAGE FROM " + APController.coreSchemaName + ".DEPOSIT_ACCOUNT_HISTORY WHERE EVENT_ID IN (SELECT EVENT_ID FROM " + APController.coreSchemaName + ".EVENT WHERE EVENT_CD IN ('BATCHMLKCR', 'BATCHTEACR', 'BATCHSALCR', 'BONUSTEACR')) AND TRAN_DT>=TRUNC(ADD_MONTHS((SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') AS PROC_DATE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'),-" + months + ")) AND DEPOSIT_ACCT_ID IN (SELECT ACCT_ID FROM " + APController.coreSchemaName + ".ACCOUNT WHERE CUST_ID=" + custId + ")")) {
            if (rs != null && rs.next()) {
                return rs.getBigDecimal("AVERAGE").setScale(2, RoundingMode.DOWN);
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal queryDepositBalance(Long acctId, Integer month) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT (" + APController.coreSchemaName + ".CALCULATE_AVAIL_BALANCE_EB(" + acctId + ")+(SELECT NVL(SUM(ACCT_AMT),0) AS DR FROM " + APController.coreSchemaName + ".DEPOSIT_ACCOUNT_HISTORY WHERE DEPOSIT_ACCT_ID=" + acctId + " AND DR_CR_IND='DR' AND TRAN_DT>ADD_MONTHS(TRUNC(SYSDATE ,'YEAR')," + month + ")-1)-(SELECT NVL(SUM(ACCT_AMT),0) AS CR FROM " + APController.coreSchemaName + ".DEPOSIT_ACCOUNT_HISTORY WHERE DEPOSIT_ACCT_ID=" + acctId + " AND DR_CR_IND='CR' AND TRAN_DT>ADD_MONTHS(TRUNC(SYSDATE ,'YEAR')," + month + ")-1)) AS BALANCE FROM DUAL")) {
            if (rs != null && rs.next()) {
                return rs.getBigDecimal("BALANCE").setScale(2, RoundingMode.DOWN);
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal queryDepositBalance(ArrayList<CNAccount> accounts, Integer month) {
        BigDecimal total = BigDecimal.ZERO;
        for (CNAccount account : accounts) {
            total = total.add(queryDepositBalance(account.getAcctId(), month)).setScale(2, RoundingMode.DOWN);
        }
        return total;
    }

    public boolean updateAccrualDate(String account, Integer days) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".LOAN_ACCOUNT_SUMMARY SET LAST_ACCRUAL_DT=TRUNC(SYSDATE+" + (days - 1) + "), CURRENT_ACCRUAL_DT=TRUNC(SYSDATE+" + (days - 1) + "),  NEXT_ACCRUAL_DT=TRUNC(SYSDATE+" + days + ") WHERE ACCT_NO='" + account + "'");
    }

    public boolean deleteInvalidChannelUsers() {
        return executeUpdate("DELETE " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT WHERE CREATED_BY='SYSTEM' AND CUST_ID NOT IN(SELECT CUST_ID FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER)") && executeUpdate("DELETE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL WHERE USER_ID='SYSTEM' AND CUST_ID NOT IN (SELECT CUST_ID FROM " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT)") && executeUpdate("DELETE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL WHERE USER_ID='SYSTEM' AND CUST_ID NOT IN (SELECT CUST_ID FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER)") && executeUpdate("DELETE " + APController.coreSchemaName + ".CUST_CHANNEL_SCHEME WHERE USER_ID='SYSTEM' AND CUST_ID NOT IN (SELECT CUST_ID FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER)");
    }

    public Long queryProductId(ArrayList products, Long currencyId) {
        Long productId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT PROD_ID FROM " + APController.coreSchemaName + ".PRODUCT WHERE PROD_ID IN (" + getWorker().createCsvList(products) + ") AND CRNCY_ID=" + currencyId + " AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                productId = rs.getLong("PROD_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return productId;
    }

    public Object[][] queryProducts() {
        return executeQuery("SELECT PROD_ID, PROD_DESC FROM " + APController.coreSchemaName + ".PRODUCT WHERE REC_ST='A' ORDER BY PROD_ID");
    }

    public Object[][] queryDepositProducts() {
        return executeQuery("SELECT PROD_ID, PROD_DESC FROM " + APController.coreSchemaName + ".PRODUCT WHERE PROD_CAT_TY='DP' AND REC_ST='A' ORDER BY PROD_ID");
    }

    public ACProduct queryProduct(String code) {
        ACProduct product = new ACProduct();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT PROD_ID, PROD_CD, PROD_DESC, PROD_CAT_TY, REC_ST FROM " + APController.coreSchemaName + ".PRODUCT WHERE PROD_CD='" + code + "' AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                product.setId(rs.getLong("PROD_ID"));
                product.setCode(rs.getString("PROD_CD"));
                product.setDescription(rs.getString("PROD_DESC"));
                product.setType(rs.getString("PROD_CAT_TY"));
                product.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return product;
    }

    public ArrayList<ACProduct> queryProducts(ArrayList<Long> productIds) {
        ArrayList<ACProduct> products = new ArrayList<>();
        if (!getWorker().isBlank(productIds) && !productIds.isEmpty()) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT PROD_ID, PROD_CD, PROD_DESC, PROD_CAT_TY, REC_ST FROM " + APController.coreSchemaName + ".PRODUCT WHERE PROD_ID IN (" + getWorker().createCsvList(productIds) + ") AND REC_ST='A' ORDER BY PROD_ID")) {
                if (rs != null) {
                    while (rs.next()) {
                        ACProduct product = new ACProduct();
                        product.setId(rs.getLong("PROD_ID"));
                        product.setCode(rs.getString("PROD_CD"));
                        product.setDescription(rs.getString("PROD_DESC"));
                        product.setType(rs.getString("PROD_CAT_TY"));
                        product.setStatus(rs.getString("REC_ST"));
                        products.add(product);
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return products;
    }

    public Object[][] queryBranches() {
        return executeQuery("SELECT BU_ID, BU_NM FROM " + APController.coreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' ORDER BY BU_ID");
    }

    public String queryProductName(Long productId) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT PROD_DESC FROM " + APController.coreSchemaName + ".PRODUCT WHERE REC_ST='A' AND PROD_ID=" + productId + " ORDER BY PROD_CD")) {
            if (rs != null && rs.next()) {
                return rs.getString("PROD_DESC");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public Object[][] queryChannelSchemes() {
        return executeQuery("SELECT SCHEME_ID, SCHEME_DESC FROM " + APController.coreSchemaName + ".SERVICE_CHANNEL_SCHEME WHERE REC_ST='A' ORDER BY SCHEME_ID ASC");
    }

    public Object[][] queryCustomerTypes() {
        return executeQuery("SELECT CUST_TY_ID, CUST_TY_DESC FROM " + APController.coreSchemaName + ".CUSTOMER_TYPE_REF WHERE REC_ST='A' ORDER BY CUST_TY_ID");
    }

    public ArrayList<CAEvent> queryAccountEvents(Long channelId) {
        ArrayList<CAEvent> events = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.CUST_CHANNEL_ACCT_ID, A.CUST_ID, M.CUST_NO, M.CUST_CAT, U.CHANNEL_USER_CUST_ID, U.ACCESS_CD, A.ACCT_ID, C.ACCT_NO, C.PROD_ID, NVL(A.SHORT_NAME, C.ACCT_NO) AS SHORT_NAME, A.AUDIT_ACTION FROM " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT$AUD A, " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CUSTOMER M, " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U WHERE A.CHANNEL_ID=U.CHANNEL_ID AND A.CUST_ID=U.CUST_ID AND A.CUST_ID=M.CUST_ID AND C.ACCT_ID=A.ACCT_ID AND U.REC_ST='A' AND A.FORWARDED IS NULL AND A.CHANNEL_ID=" + channelId + " ORDER BY A.AUDIT_TS ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    CAEvent event = new CAEvent();
                    event.setCustChannelAcctId(rs.getLong("CUST_CHANNEL_ACCT_ID"));
                    event.setCustId(rs.getLong("CUST_ID"));
                    event.setAcctId(rs.getLong("ACCT_ID"));
                    event.setProdId(rs.getLong("PROD_ID"));
                    event.setCustNo(rs.getString("CUST_NO"));
                    event.setAcctNo(rs.getString("ACCT_NO"));
                    event.setAccessCd(rs.getString("ACCESS_CD"));
                    event.setShortName(rs.getString("SHORT_NAME"));
                    event.setChannelUserCustId(rs.getLong("CHANNEL_USER_CUST_ID"));
                    event.setAuditAction(rs.getString("AUDIT_ACTION"));
                    event.setCustCat(rs.getString("CUST_CAT"));
                    events.add(event);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return events;
    }

    public ArrayList<CAEvent> queryGroupAccountEvents(Long channelId, Long memberCustId, String auditAction) {
        ArrayList<CAEvent> events = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.CUST_CHANNEL_ACCT_ID, A.CUST_ID, M.CUST_NO, M.CUST_CAT, U.CHANNEL_USER_CUST_ID, U.ACCESS_CD, A.ACCT_ID, C.ACCT_NO, C.PROD_ID, NVL(A.SHORT_NAME, C.ACCT_NO) AS SHORT_NAME, '" + auditAction + "' AS AUDIT_ACTION FROM " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT A, " + APController.coreSchemaName + ".ACCOUNT C, " + APController.coreSchemaName + ".CUSTOMER M, " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER U WHERE A.CHANNEL_ID=U.CHANNEL_ID AND A.CUST_ID=U.CUST_ID AND A.CUST_ID=M.CUST_ID AND C.ACCT_ID=A.ACCT_ID AND U.REC_ST='A' AND A.CHANNEL_ID=" + channelId + " AND A.CUST_ID IN (SELECT MAIN_CUST_ID FROM " + APController.coreSchemaName + ".V_CUSTOMER_RELATIONSHIP WHERE REL_CUST_ID=" + memberCustId + ")")) {
            if (rs != null) {
                while (rs.next()) {
                    CAEvent event = new CAEvent();
                    event.setCustChannelAcctId(rs.getLong("CUST_CHANNEL_ACCT_ID"));
                    event.setCustId(rs.getLong("CUST_ID"));
                    event.setAcctId(rs.getLong("ACCT_ID"));
                    event.setProdId(rs.getLong("PROD_ID"));
                    event.setCustNo(rs.getString("CUST_NO"));
                    event.setAcctNo(rs.getString("ACCT_NO"));
                    event.setAccessCd(rs.getString("ACCESS_CD"));
                    event.setShortName(rs.getString("SHORT_NAME"));
                    event.setChannelUserCustId(rs.getLong("CHANNEL_USER_CUST_ID"));
                    event.setAuditAction(rs.getString("AUDIT_ACTION"));
                    event.setCustCat(rs.getString("CUST_CAT"));
                    events.add(event);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return events;
    }

    public Date getProcessingDate() {
        Date currentDate = new Date();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') AS PROC_DATE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'")) {
            if (rs != null && rs.next()) {
                currentDate = rs.getDate("PROC_DATE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return currentDate;
    }

    public Date getSystemDate() {
        Date currentDate = new Date();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT SYSDATE AS SYS_DATE FROM DUAL")) {
            if (rs != null && rs.next()) {
                currentDate = rs.getTimestamp("SYS_DATE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return currentDate;
    }

    public Date getLowerDate() {
        Date lowerDate = new Date();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CASE WHEN PROC_DATE>SYS_DATE THEN SYS_DATE ELSE PROC_DATE END AS LOWER_DATE FROM (SELECT TRUNC(SYSDATE) AS SYS_DATE, TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') AS PROC_DATE FROM " + APController.coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02')")) {
            if (rs != null && rs.next()) {
                lowerDate = rs.getDate("LOWER_DATE");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return lowerDate;
    }

    public void prepareAlerts(MXAlert mXAlert) {
        if (!mXAlert.isTriggered() && (!mXAlert.isLoaded() || mXAlert.isRealTime())) {
            String[] days = !getWorker().isBlank(mXAlert.getDays()) ? mXAlert.getDays().split(",") : new String[1];
            try (PLAdapter adapter = getAdapter(true);
                    CallableStatement statement
                    = adapter.getConnection().prepareCall("{call "
                            + "" + APController.cmSchemaName + ".PHL_LOAD_ALERTS(?, ?, ?, ?, ?, ?)}")) {
                for (int i = 0; i < (mXAlert.isReminder() ? days.length : 1); i++) {
                    if (mXAlert.getFilters().isEmpty()) {
                        mXAlert.getFilters().add("ALL");
                    }
                    for (String filter : mXAlert.getFilters()) {
                        try {
                            statement.setString(1, mXAlert.getCode());
                            statement.setString(2, mXAlert.getType());

                            statement.setInt(3, (mXAlert.isReminder()
                                    ? getWorker().convertToType(days[i], Integer.class) : 1));
                            statement.setString(4, APController.getCurrency().getCode());
                            statement.setString(5, mXAlert.getFilterBy());

                            statement.setString(6, filter);
                            statement.execute();

                            if (!mXAlert.isLoaded()) {
                                mXAlert.setStatus("L");
                                updateStatus(mXAlert);
                            }
                        } catch (Exception ex) {
                            getLog().logEvent("======<" + mXAlert.getCode() + ", " + mXAlert.getType() + ", " + mXAlert.getFilterBy() + ", " + filter + ">======", ex);
                        }
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
    }

    public ArrayList<MXMessage> queryPendingAlerts(MXAlert mXAlert) {
        ArrayList<MXMessage> alerts = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(true);
                Statement statement = createStatement(adapter.getConnection());
                ResultSet rs = executeQuery(statement, "SELECT REC_ID, CREATE_DT, "
                        + "CODE, TYPE, TXN_ID, BU_ID, CUST_ID, CUST_NAME, ACCT_NO, "
                        + "CONTRA, CHRG_ACCT, TXN_DATE, CURRENCY, NVL(TXN_AMT,0) AS TXN_AMT, "
                        + "NVL(TXN_CHG,0) AS TXN_CHG, CHRG_AMT, CHG_ID, TXN_DESC, "
                        + "BALANCE, CONTACT, ORIGINATOR, SCHEME_ID, ACCESS_CD, "
                        + "PASSWORD, REC_ST FROM " + APController.cmSchemaName + ".PHA_ALERTS "
                        + "WHERE REC_ST='P' AND TYPE='" + mXAlert.getType() + "' "
                        + "AND (CODE='" + mXAlert.getCode() + "' OR CODE IS NULL) "
                        + ("CS".equals(mXAlert.getFilterBy()) ? " "
                        + "AND SCHEME_ID IN (" + getWorker().createCsvList(mXAlert.getFilters()) + ") " : "") + " "
                        + "AND CREATE_DT >= SYSDATE-1")) {
            if (rs != null) {
                while (rs.next()) {
                    MXMessage message = new MXMessage();
                    message.setRecId(rs.getLong("REC_ID"));
                    message.setCreateDt(rs.getDate("CREATE_DT"));
                    message.setCode(mXAlert.getCode());
                    message.setDescription(mXAlert.getDescription());
                    message.setType(rs.getString("TYPE"));
                    message.setTxnId(rs.getString("TXN_ID"));
                    message.setCustId(rs.getLong("CUST_ID"));
                    message.setSchemeId(rs.getLong("SCHEME_ID"));
                    message.setCustName(rs.getString("CUST_NAME"));
                    message.setAcctNo(rs.getString("ACCT_NO"));
                    message.setContra(rs.getString("CONTRA"));
                    message.setChargeAcct(rs.getString("CHRG_ACCT"));
                    message.setTxnDate(rs.getDate("TXN_DATE"));
                    message.setCurrency(rs.getString("CURRENCY"));
                    message.setTxnAmt(rs.getBigDecimal("TXN_AMT"));
                    message.setTxnChg(rs.getBigDecimal("TXN_CHG"));
                    message.setChargeId(rs.getLong("CHG_ID"));
                    message.setTxnDesc(rs.getString("TXN_DESC"));
                    message.setAccessCode(rs.getString("ACCESS_CD"));
                    message.setPassword(rs.getString("PASSWORD"));
                    message.setBalance(rs.getBigDecimal("BALANCE"));
                    message.setMsisdn(rs.getString("CONTACT"));
                    message.setStatus(rs.getString("REC_ST"));
                    alerts.add(message);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return alerts;
    }

    public TreeMap<String, AXCharge> queryCharges(String scheme) {
        TreeMap<String, AXCharge> charges = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, REC_CD, DESCRIPTION, ACCOUNT, LEDGER, SCHEME, SYS_USER, SYS_DATE, BASIS, REC_ST FROM " + APController.cmSchemaName + ".PHC_CHARGE WHERE SCHEME='" + scheme + "' ORDER BY REC_CD ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXCharge charge = new AXCharge();
                    charge.setRecId(rs.getLong("REC_ID"));
                    charge.setCode(rs.getString("REC_CD"));
                    charge.setDescription(rs.getString("DESCRIPTION"));
                    charge.setChargeAccount(rs.getString("ACCOUNT"));
                    charge.setChargeLedger(rs.getString("LEDGER"));
                    charge.setScheme(rs.getString("SCHEME"));
                    charge.setSysUser(rs.getString("SYS_USER"));
                    charge.setSysDate(rs.getDate("SYS_DATE"));
                    charge.setBasis(rs.getString("BASIS"));
                    charge.setStatus(rs.getString("REC_ST"));
                    charges.put(charge.getCode(), setValues(setWaivers(charge)));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return charges;
    }

    public AXCharge queryCharge(String scheme, String code) {
        AXCharge charge = new AXCharge();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, REC_CD, DESCRIPTION, ACCOUNT, LEDGER, SCHEME, SYS_USER, SYS_DATE, BASIS, REC_ST FROM " + APController.cmSchemaName + ".PHC_CHARGE WHERE SCHEME='" + scheme + "' AND REC_CD='" + code + "' AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                charge.setRecId(rs.getLong("REC_ID"));
                charge.setCode(rs.getString("REC_CD"));
                charge.setDescription(rs.getString("DESCRIPTION"));
                charge.setChargeAccount(rs.getString("ACCOUNT"));
                charge.setChargeLedger(rs.getString("LEDGER"));
                charge.setScheme(rs.getString("SCHEME"));
                charge.setSysUser(rs.getString("SYS_USER"));
                charge.setSysDate(rs.getDate("SYS_DATE"));
                charge.setBasis(rs.getString("BASIS"));
                charge.setStatus(rs.getString("REC_ST"));
                return setValues(setWaivers(charge));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return charge;
    }

    public AXCharge setValues(AXCharge charge) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CURRENCY, TYPE, MIN_VALUE, MAX_VALUE, VALUE FROM " + APController.cmSchemaName + ".PHC_VALUE WHERE PARENT='" + charge.getRecId() + "' ORDER BY CURRENCY ASC")) {
            charge.getValues().clear();
            if (rs != null) {
                while (rs.next()) {
                    TCValue tCValue = new TCValue();
                    tCValue.setRecId(rs.getLong("REC_ID"));
                    tCValue.setCurrency(rs.getString("CURRENCY"));
                    tCValue.setType(rs.getString("TYPE"));
                    tCValue.setMin(rs.getBigDecimal("MIN_VALUE"));
                    tCValue.setMax(rs.getBigDecimal("MAX_VALUE"));
                    tCValue.setValue(rs.getBigDecimal("VALUE"));
                    tCValue.setTiers(queryTiers("C", tCValue.getRecId()));
                    tCValue.setDeductions(queryDeductions(tCValue.getRecId()));
                    charge.getValues().put(tCValue.getCurrency(), tCValue);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return charge;
    }

    public AXCharge setWaivers(AXCharge charge) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, PROD_ID, MATCH_ACCT, WAIVED_PERC, CONDITION, THRESHOLD FROM " + APController.cmSchemaName + ".PHC_WAIVER WHERE PARENT='" + charge.getRecId() + "' ORDER BY PROD_ID ASC")) {
            charge.getWaivers().clear();
            if (rs != null) {
                while (rs.next()) {
                    TCWaiver tXWaiver = new TCWaiver();
                    tXWaiver.setWaiverId(rs.getLong("REC_ID"));
                    tXWaiver.setProductId(rs.getLong("PROD_ID"));
                    tXWaiver.setMatchAccount(rs.getString("MATCH_ACCT"));
                    tXWaiver.setWaivedPercentage(rs.getBigDecimal("WAIVED_PERC"));
                    tXWaiver.setWaiverCondition(rs.getString("CONDITION"));
                    tXWaiver.setThresholdValue(rs.getBigDecimal("THRESHOLD"));
                    charge.getWaivers().put(tXWaiver.getProductId(), tXWaiver);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return charge;
    }

    public TreeMap<String, TCScheme> querySchemes(String module) {
        TreeMap<String, TCScheme> schemes = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_CD, CREATE_DT, MODULE, DESCRIPTION, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHC_SCHEME WHERE MODULE='" + module + "' ORDER BY REC_CD ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    TCScheme scheme = new TCScheme();
                    scheme.setCode(rs.getString("REC_CD"));
                    scheme.setModule(rs.getString("MODULE"));
                    scheme.setDescription(rs.getString("DESCRIPTION"));
                    scheme.setSysUser(rs.getString("SYS_USER"));
                    scheme.setSysDate(rs.getDate("SYS_DATE"));
                    scheme.setStatus(rs.getString("REC_ST"));
                    schemes.put(scheme.getCode(), scheme);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return schemes;
    }

    public TreeMap<String, AXBank> queryBanks() {
        TreeMap<String, AXBank> banks = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT BIN, CREATE_DT, BANK_NAME, LORO_LEDGER, TRANSFER_LEDGER, SUSPENSE_LEDGER, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHL_BANK ORDER BY BIN ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXBank bank = new AXBank();
                    bank.setBin(rs.getString("BIN"));
                    bank.setName(rs.getString("BANK_NAME"));
                    bank.setLoroLedger(rs.getString("LORO_LEDGER"));
                    bank.setTransferLedger(rs.getString("TRANSFER_LEDGER"));
                    bank.setSuspenseLedger(rs.getString("SUSPENSE_LEDGER"));
                    bank.setSysUser(rs.getString("SYS_USER"));
                    bank.setSysDate(rs.getDate("SYS_DATE"));
                    bank.setStatus(rs.getString("REC_ST"));
                    banks.put(bank.getBin(), bank);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return banks;
    }

    public AXBank queryBank(String BIN) {
        AXBank bank = new AXBank();
        if (!getWorker().isBlank(BIN)) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT BIN, CREATE_DT, BANK_NAME, LORO_LEDGER, TRANSFER_LEDGER, SUSPENSE_LEDGER, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHL_BANK WHERE BIN='" + BIN + "'")) {
                if (rs != null && rs.next()) {
                    bank.setBin(rs.getString("BIN"));
                    bank.setName(rs.getString("BANK_NAME"));
                    bank.setLoroLedger(rs.getString("LORO_LEDGER"));
                    bank.setTransferLedger(rs.getString("TRANSFER_LEDGER"));
                    bank.setSuspenseLedger(rs.getString("SUSPENSE_LEDGER"));
                    bank.setSysUser(rs.getString("SYS_USER"));
                    bank.setSysDate(rs.getDate("SYS_DATE"));
                    bank.setStatus(rs.getString("REC_ST"));
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return bank;
    }

    public AXChannel queryChannel(Long channelId) {
        AXChannel channel = new AXChannel();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CHANNEL_ID, CHANNEL_CD, CHANNEL_DESC, GL_DR_ACCT, GL_CR_ACCT, PREV_DR_LIMIT, PREV_CR_LIMIT, ORIGIN_BU_ID, REC_ST FROM " + APController.coreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelId)) {
            if (rs != null && rs.next()) {
                channel.setId(rs.getLong("CHANNEL_ID"));
                channel.setCode(rs.getString("CHANNEL_CD"));
                channel.setName(rs.getString("CHANNEL_DESC"));
                channel.setDebitContra(rs.getString("GL_DR_ACCT"));
                channel.setCreditContra(rs.getString("GL_CR_ACCT"));
                channel.setDebitLimit(rs.getBigDecimal("PREV_DR_LIMIT"));
                channel.setCreditLimit(rs.getBigDecimal("PREV_CR_LIMIT"));
                channel.setBranch(queryBranch(rs.getLong("ORIGIN_BU_ID")));
                channel.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return channel;
    }

    public TreeMap<String, AXUser> queryUsers() {
        TreeMap<String, AXUser> users = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.EMP_NO, A.CREATE_DT, A.LOGIN_ID, A.STAFF_NAME, A.SYS_USER, A.SYS_DATE, A.REC_ST FROM " + APController.cmSchemaName + ".PHU_USER A, " + APController.coreSchemaName + ".SYSUSER B WHERE A.EMP_NO=B.EMP_NO AND B.REC_ST='A' ORDER BY A.EMP_NO ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXUser user = new AXUser();
                    user.setUserNumber(rs.getString("EMP_NO"));
                    user.setUserName(rs.getString("LOGIN_ID"));
                    user.setStaffName(rs.getString("STAFF_NAME"));
                    user.setSysUser(rs.getString("SYS_USER"));
                    user.setSysDate(rs.getDate("SYS_DATE"));
                    user.setStatus(rs.getString("REC_ST"));
                    users.put(user.getUserName(), setUserRoles(user));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return users;
    }

    public AXUser queryUser(String userName, boolean enrolled) {
        AXUser user = new AXUser();
        if (enrolled) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT A.EMP_NO, A.CREATE_DT, A.LOGIN_ID, A.STAFF_NAME, A.SYS_USER, A.SYS_DATE, A.REC_ST FROM " + APController.cmSchemaName + ".PHU_USER A, " + APController.coreSchemaName + ".SYSUSER B WHERE A.EMP_NO=B.EMP_NO AND A.LOGIN_ID='" + userName + "' AND B.REC_ST='A' ORDER BY A.EMP_NO ASC")) {
                if (rs != null && rs.next()) {
                    user.setUserNumber(rs.getString("EMP_NO"));
                    user.setUserName(rs.getString("LOGIN_ID"));
                    user.setStaffName(rs.getString("STAFF_NAME"));
                    user.setSysUser(rs.getString("SYS_USER"));
                    user.setSysDate(rs.getDate("SYS_DATE"));
                    user.setStatus(rs.getString("REC_ST"));
                    user = setUserRoles(user);
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        } else {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT EMP_NO, LOGIN_ID, FIRST_NM || ' ' || LAST_NM AS NAME, REC_ST FROM " + APController.coreSchemaName + ".SYSUSER WHERE LOGIN_ID='" + userName + "' AND REC_ST='A'")) {
                if (rs != null && rs.next()) {
                    user.setUserNumber(rs.getString("EMP_NO"));
                    user.setUserName(rs.getString("LOGIN_ID"));
                    user.setStaffName(rs.getString("NAME"));
                    user.setStatus(rs.getString("REC_ST"));
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return user;
    }

    public AXUser setUserRoles(AXUser user) {
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT ROLE FROM " + APController.cmSchemaName + ".PHU_ROLE WHERE EMP_NO='" + user.getUserNumber() + "'")) {
            user.getRoles().clear();
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString("ROLE") != null) {
                        user.getRoles().add(rs.getString("ROLE"));
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return user;
    }

    public CNScheme queryChannelScheme(Long schemeId) {
        CNScheme scheme = new CNScheme();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT SCHEME_ID, CHANNEL_ID, SCHEME_CD, SCHEME_DESC, REC_ST FROM " + APController.coreSchemaName + ".SERVICE_CHANNEL_SCHEME WHERE SCHEME_ID=" + schemeId)) {
            if (rs != null && rs.next()) {
                scheme.setSchemeId(rs.getLong("SCHEME_ID"));
                scheme.setChannelId(rs.getLong("CHANNEL_ID"));
                scheme.setSchemeCode(rs.getString("SCHEME_CD"));
                scheme.setName(rs.getString("SCHEME_DESC"));
                scheme.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return scheme;
    }

    public CMChannel queryCustomerChannelId(Long schemeId, Long custId) {
        CMChannel customerChannel = new CMChannel();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_CHANNEL_ID, REC_ST FROM " + APController.coreSchemaName + ".CUSTOMER_CHANNEL WHERE CHANNEL_SCHEME_ID=" + schemeId + " AND CUST_ID=" + custId)) {
            if (rs != null && rs.next()) {
                customerChannel.setCustChannelId(rs.getLong("CUST_CHANNEL_ID"));
                customerChannel.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customerChannel;
    }

    public Long queryTxnId(Long channelId, String reference) {
        Long txnId = null;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT MAX(TRAN_JOURNAL_ID) AS TRAN_JOURNAL_ID FROM " + APController.coreSchemaName + ".TXN_JOURNAL WHERE CHANNEL_ID=" + channelId + " AND TRAN_REF_TXT='" + reference + "' AND CREATE_DT>SYSDATE-2")) {
            if (rs != null && rs.next()) {
                txnId = rs.getLong("TRAN_JOURNAL_ID");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        if (txnId == null || txnId == 0L) {
            try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT MAX(TRAN_JOURNAL_ID) AS TRAN_JOURNAL_ID FROM " + APController.coreSchemaName + ".XAPI_ACTIVITY_HISTORY WHERE REQ_RECEIVED_TIME>SYSDATE-1 AND REQ_CHANNEL_ID=" + channelId + " AND REFERENCE = '" + reference + "'")) {
                if (rs != null && rs.next()) {
                    txnId = rs.getLong("TRAN_JOURNAL_ID");
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return txnId;
    }

    public boolean updateTxnRefText(Long txnId, String reference) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".GL_ACCOUNT_HISTORY SET TRAN_REF_TXT='" + reference + "' WHERE TRAN_JOURNAL_ID=" + txnId) && executeUpdate("UPDATE " + APController.coreSchemaName + ".TXN_JOURNAL SET TRAN_REF_TXT='" + reference + "' WHERE TRAN_JOURNAL_ID=" + txnId);
    }

    public boolean deleteXapiHistory() {
        return executeUpdate("DELETE " + APController.coreSchemaName + ".XAPI_ACTIVITY_HISTORY WHERE REQ_RECEIVED_TIME<SYSDATE-5", true);
    }

    public TreeMap<String, AXPayment> queryPayments(String module) {
        TreeMap<String, AXPayment> payments = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CREATE_DT, MODULE, TXN_CD, TYPE, DESCRIPTION, CODE_FIELD, POSITION, CODE, DETAIL_FIELD, ACCOUNT, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHB_PAYMENT WHERE MODULE='" + module + "' ORDER BY REC_ID ASC")) {
            if (rs != null) {
                while (rs.next()) {
                    AXPayment payment = new AXPayment();
                    payment.setRecId(rs.getLong("REC_ID"));
                    payment.setCode(rs.getString("CODE"));
                    payment.setCreateDt(rs.getDate("CREATE_DT"));
                    payment.setDescription(rs.getString("DESCRIPTION"));
                    payment.setModule(rs.getString("MODULE"));
                    payment.setCodeField(rs.getInt("CODE_FIELD"));
                    payment.setDetailField(rs.getInt("DETAIL_FIELD"));
                    payment.setPosition(rs.getString("POSITION"));
                    payment.setTxnCd(rs.getString("TXN_CD"));
                    payment.setType(rs.getString("TYPE"));
                    payment.setAccount(rs.getString("ACCOUNT"));
                    payment.setSysUser(rs.getString("SYS_USER"));
                    payment.setSysDate(rs.getDate("SYS_DATE"));
                    payment.setStatus(rs.getString("REC_ST"));
                    payments.put(payment.getCode(), payment);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return payments;
    }

    public AXPayment queryPayment(String module, String code) {
        AXPayment payment = new AXPayment();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT REC_ID, CREATE_DT, MODULE, TXN_CD, TYPE, DESCRIPTION, CODE_FIELD, POSITION, CODE, DETAIL_FIELD, ACCOUNT, SYS_USER, SYS_DATE, REC_ST FROM " + APController.cmSchemaName + ".PHB_PAYMENT WHERE MODULE='" + module + "' AND CODE='" + code + "' ORDER BY REC_ID ASC")) {
            if (rs != null && rs.next()) {
                payment.setRecId(rs.getLong("REC_ID"));
                payment.setCode(rs.getString("CODE"));
                payment.setCreateDt(rs.getDate("CREATE_DT"));
                payment.setDescription(rs.getString("DESCRIPTION"));
                payment.setModule(rs.getString("MODULE"));
                payment.setCodeField(rs.getInt("CODE_FIELD"));
                payment.setDetailField(rs.getInt("DETAIL_FIELD"));
                payment.setPosition(rs.getString("POSITION"));
                payment.setTxnCd(rs.getString("TXN_CD"));
                payment.setType(rs.getString("TYPE"));
                payment.setAccount(rs.getString("ACCOUNT"));
                payment.setSysUser(rs.getString("SYS_USER"));
                payment.setSysDate(rs.getDate("SYS_DATE"));
                payment.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return payment;
    }

    public ArrayList<CNCustomer> queryGroupMembers(long groupCustId) {
        ArrayList<CNCustomer> customers = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT C.CUST_NO FROM " + APController.coreSchemaName + ".GROUP_MEMBER G, " + APController.coreSchemaName + ".CUSTOMER C WHERE G.GROUP_CUST_ID=" + groupCustId + " AND C.CUST_ID=G.MEMBER_CUST_ID AND G.REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    customers.add(queryCustomer(rs.getString("CUST_NO")));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customers;
    }

    public ArrayList<GPMember> queryGroupMembers(Long groupCustId) {
        ArrayList<GPMember> members = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT GROUP_MEMBER_ID, GROUP_CUST_ID, MEMBERSHIP_REF_NO, MEMBER_CUST_ID, DEFLT_ACCT_NO, GROUP_RELSHIP_ID, SUB_GROUP_ID, REC_ST FROM " + APController.coreSchemaName + ".GROUP_MEMBER WHERE GROUP_CUST_ID=" + groupCustId + " AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    GPMember member = new GPMember();
                    member.setGroupMemberId(rs.getLong("GROUP_MEMBER_ID"));
                    member.setGroupCustId(rs.getLong("GROUP_CUST_ID"));
                    member.setMemberRefNo(rs.getString("MEMBERSHIP_REF_NO"));
                    member.setMemberCustId(rs.getLong("MEMBER_CUST_ID"));
                    member.setDefaultAcctNo(rs.getString("DEFLT_ACCT_NO"));
                    member.setGroupXshipId(rs.getLong("GROUP_RELSHIP_ID"));
                    member.setSubGroupId(rs.getLong("SUB_GROUP_ID"));
                    member.setStatus(rs.getString("REC_ST"));
                    members.add(member);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return members;
    }

    public GPMember queryGroupMember(Long groupCustId, Long memberCustId) {
        GPMember member = new GPMember();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT GROUP_MEMBER_ID, GROUP_CUST_ID, MEMBERSHIP_REF_NO, MEMBER_CUST_ID, DEFLT_ACCT_NO, GROUP_RELSHIP_ID, SUB_GROUP_ID, REC_ST FROM " + APController.coreSchemaName + ".GROUP_MEMBER WHERE GROUP_CUST_ID=" + groupCustId + " AND MEMBER_CUST_ID=" + memberCustId + " AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    member.setGroupMemberId(rs.getLong("GROUP_MEMBER_ID"));
                    member.setGroupCustId(rs.getLong("GROUP_CUST_ID"));
                    member.setMemberRefNo(rs.getString("MEMBERSHIP_REF_NO"));
                    member.setMemberCustId(rs.getLong("MEMBER_CUST_ID"));
                    member.setDefaultAcctNo(rs.getString("DEFLT_ACCT_NO"));
                    member.setGroupXshipId(rs.getLong("GROUP_RELSHIP_ID"));
                    member.setSubGroupId(rs.getLong("SUB_GROUP_ID"));
                    member.setStatus(rs.getString("REC_ST"));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return member;
    }

    public ArrayList<RLCustomer> queryXshipMembers(Long groupCustId) {
        ArrayList<RLCustomer> customers = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_RELSHIP_ID, CUST_RELSHIP_TY_ID, MAIN_CUST, MAIN_CUST_NM, REL_CUST_NO, REL_CUST_NM, MAIN_CUST_ID, REL_CUST_ID, REL_SHIP, RELSHIP_TY, CUST_RELSHIP_TY, CUST_CAT, REC_ST FROM " + APController.coreSchemaName + ".V_CUSTOMER_RELATIONSHIP_PHL WHERE MAIN_CUST_ID=" + groupCustId + " AND REC_ST='A'")) {
            if (rs != null) {
                while (rs.next()) {
                    RLCustomer customer = new RLCustomer();
                    customer.setMainCustId(rs.getLong("MAIN_CUST_ID"));
                    customer.setMainCustNo(rs.getString("MAIN_CUST"));
                    customer.setRelCustId(rs.getLong("REL_CUST_ID"));
                    customer.setRelCustNo(rs.getString("REL_CUST_NO"));
                    customer.setRelCustName(rs.getString("REL_CUST_NM"));
                    customer.setXshipTypeIId(rs.getLong("CUST_RELSHIP_TY_ID"));
                    customer.setStatus(rs.getString("REC_ST"));
                    customers.add(customer);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customers;
    }

    public RLCustomer queryXshipMember(Long groupCustId, Long relCustId) {
        RLCustomer customer = new RLCustomer();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_RELSHIP_ID, CUST_RELSHIP_TY_ID, MAIN_CUST, MAIN_CUST_NM, REL_CUST_NO, REL_CUST_NM, MAIN_CUST_ID, REL_CUST_ID, REL_SHIP, RELSHIP_TY, CUST_RELSHIP_TY, CUST_CAT, REC_ST FROM " + APController.coreSchemaName + ".V_CUSTOMER_RELATIONSHIP_PHL WHERE MAIN_CUST_ID=" + groupCustId + " AND REL_CUST_ID=" + relCustId + " AND REC_ST='A'")) {
            if (rs != null && rs.next()) {
                customer.setMainCustId(rs.getLong("MAIN_CUST_ID"));
                customer.setMainCustNo(rs.getString("MAIN_CUST"));
                customer.setRelCustId(rs.getLong("REL_CUST_ID"));
                customer.setRelCustNo(rs.getString("REL_CUST_NO"));
                customer.setRelCustName(rs.getString("REL_CUST_NM"));
                customer.setXshipTypeIId(rs.getLong("CUST_RELSHIP_TY_ID"));
                customer.setStatus(rs.getString("REC_ST"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customer;
    }

    public ArrayList<CNCustomer> queryGroupLoanBeneficiaries(long groupCustId, String accountNumber) {
        ArrayList<CNCustomer> customers = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT CUST_NO FROM " + APController.coreSchemaName + ".CUSTOMER WHERE CUST_ID IN (SELECT MEMBER_ID FROM " + APController.coreSchemaName + ".GROUP_LOAN_ALLOTMENT_MEMO WHERE GRP_CUST_ID=" + groupCustId + " AND APPL_ID=(SELECT APPL_ID FROM " + APController.coreSchemaName + ".LOAN_ACCOUNT WHERE ACCT_NO='" + accountNumber + "'))")) {
            if (rs != null) {
                while (rs.next()) {
                    customers.add(queryCustomer(rs.getString("CUST_NO")));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return customers;
    }

    public ArrayList<DHRecord> queryDepositAccountHistory(String accountsList, Long acctHistId, Long pageSize) {
        ArrayList<DHRecord> records = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT Q.* FROM (SELECT ACCT_HIST_ID, TRAN_JOURNAL_ID, SYS_CREATE_TS, EVENT_CD, ACCT_NO, ORIGIN_BU_ID, BU_NM, TRAN_DT, VALUE_DT, CHANNEL_ID,CHANNEL_DESC, TRAN_REF_TXT, CHQ_NO, TRAN_DESC, DR_CR_IND, ACCT_CRNCY_CD_ISO, ACCT_AMT, TXN_CONTRA_ACCT_NO, DEPOSITOR_PAYEE_NM, REV_TXN_FG, REV_TRAN_JOURNAL_ID, STMNT_BAL FROM " + APController.coreSchemaName + ".V_DEPOSIT_ACCOUNT_HISTORY_EXT WHERE ACCT_NO IN (" + getWorker().cleanCsvList(accountsList) + ") AND ACCT_HIST_ID>" + acctHistId + " AND CHRG_ID IS NULL ORDER BY ACCT_HIST_ID ASC) Q WHERE ROWNUM<=" + pageSize)) {
            if (rs != null) {
                rs.beforeFirst();
                while (rs.next()) {
                    DHRecord record = new DHRecord();
                    record.setAcctHistId(rs.getLong("ACCT_HIST_ID"));
                    record.setTranJournalId(rs.getLong("TRAN_JOURNAL_ID"));
                    record.setSysCreateTs(rs.getDate("SYS_CREATE_TS"));
                    record.setEventCd(rs.getString("EVENT_CD"));
                    record.setAcctNo(rs.getString("ACCT_NO"));
                    record.setOriginBuId(rs.getLong("ORIGIN_BU_ID"));
                    record.setBuNm(rs.getString("BU_NM"));
                    record.setTranDt(rs.getDate("TRAN_DT"));
                    record.setValueDt(rs.getDate("VALUE_DT"));
                    record.setChannelId(rs.getLong("CHANNEL_ID"));
                    record.setChannelDesc(rs.getString("CHANNEL_DESC"));
                    record.setTranRefTxt(rs.getString("TRAN_REF_TXT"));
                    record.setChqNo(rs.getString("CHQ_NO"));
                    record.setTranDesc(rs.getString("TRAN_DESC"));
                    record.setDrCrInd(rs.getString("DR_CR_IND"));
                    record.setAcctCrncyCdIso(rs.getString("ACCT_CRNCY_CD_ISO"));
                    record.setAcctAmt(rs.getBigDecimal("ACCT_AMT"));
                    record.setTxnContraAcctNo(rs.getString("TXN_CONTRA_ACCT_NO"));
                    record.setDepositorPayeeNm(rs.getString("DEPOSITOR_PAYEE_NM"));
                    record.setRevTranJournalId(rs.getLong("REV_TRAN_JOURNAL_ID"));
                    record.setRevTxnFg(rs.getString("REV_TXN_FG"));
                    record.setStmntBal(rs.getBigDecimal("STMNT_BAL"));
                    records.add(record);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return records;
    }

    public ArrayList<LHRecord> queryLedgerHistory(String accountsList, Long acctHistId, Long pageSize) {
        ArrayList<LHRecord> records = new ArrayList<>();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT Q.* FROM (SELECT ACCT_HIST_ID, TRAN_JOURNAL_ID, SYS_CREATE_TS, EVENT_CD, GL_ACCT_NO, ORIGIN_BU_ID, BU_NM, TRAN_DT, VALUE_DT, CHANNEL_ID,CHANNEL_DESC, TRAN_REF_TXT, CHQ_NO, TRAN_DESC, DR_CR_IND, CRNCY_CD_ISO, ACCT_AMT, CONTRA_ACCT_NO, STMNT_BAL FROM " + APController.coreSchemaName + ".V_GL_ACCOUNT_HISTORY WHERE GL_ACCT_NO IN (" + getWorker().cleanCsvList(accountsList) + ") AND ACCT_HIST_ID>" + acctHistId + " ORDER BY ACCT_HIST_ID ASC) Q WHERE ROWNUM<=" + pageSize)) {
            if (rs != null) {
                rs.beforeFirst();
                while (rs.next()) {
                    LHRecord record = new LHRecord();
                    record.setAcctHistId(rs.getLong("ACCT_HIST_ID"));
                    record.setTranJournalId(rs.getLong("TRAN_JOURNAL_ID"));
                    record.setSysCreateTs(rs.getDate("SYS_CREATE_TS"));
                    record.setEventCd(rs.getString("EVENT_CD"));
                    record.setGlAcctNo(rs.getString("GL_ACCT_NO"));
                    record.setOriginBuId(rs.getLong("ORIGIN_BU_ID"));
                    record.setBuNm(rs.getString("BU_NM"));
                    record.setTranDt(rs.getDate("TRAN_DT"));
                    record.setValueDt(rs.getDate("VALUE_DT"));
                    record.setChannelId(rs.getLong("CHANNEL_ID"));
                    record.setChannelDesc(rs.getString("CHANNEL_DESC"));
                    record.setTranRefTxt(rs.getString("TRAN_REF_TXT"));
                    record.setChqNo(rs.getString("CHQ_NO"));
                    record.setTranDesc(rs.getString("TRAN_DESC"));
                    record.setDrCrInd(rs.getString("DR_CR_IND"));
                    record.setCrncyCdIso(rs.getString("CRNCY_CD_ISO"));
                    record.setAcctAmt(rs.getBigDecimal("ACCT_AMT"));
                    record.setContraAcctNo(rs.getString("CONTRA_ACCT_NO"));
                    record.setStmntBal(rs.getBigDecimal("STMNT_BAL"));
                    records.add(record);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return records;
    }

    public boolean upsertPayment(AXPayment payment) {
        return checkExists("SELECT CODE FROM " + APController.cmSchemaName + ".PHB_PAYMENT WHERE MODULE='" + payment.getModule() + "' AND CODE='" + payment.getCode() + "'") ? updatePayment(payment) : savePayment(payment);
    }

    private boolean savePayment(AXPayment payment) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHB_PAYMENT(REC_ID, CREATE_DT, MODULE, TXN_CD, TYPE, DESCRIPTION, CODE_FIELD, POSITION, CODE, DETAIL_FIELD, ACCOUNT, SYS_USER, SYS_DATE, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHB_PAYMENT.NEXTVAL, SYSDATE, '" + payment.getModule() + "', '" + payment.getTxnCd() + "', '" + payment.getType() + "', '" + payment.getDescription() + "', " + payment.getCodeField() + ", '" + payment.getPosition() + "', '" + payment.getCode() + "', " + payment.getDetailField() + ", '" + payment.getAccount() + "', '" + payment.getSysUser() + "', SYSDATE, '" + payment.getStatus() + "')");
    }

    private boolean updatePayment(AXPayment payment) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHB_PAYMENT SET DESCRIPTION='" + payment.getDescription() + "', ACCOUNT='" + payment.getAccount() + "', TXN_CD='" + payment.getTxnCd() + "', TYPE='" + payment.getType() + "', CODE_FIELD=" + payment.getCodeField() + ", POSITION='" + payment.getPosition() + "', DETAIL_FIELD=" + payment.getDetailField() + ", SYS_USER='" + payment.getSysUser() + "', SYS_DATE=SYSDATE, REC_ST='" + payment.getStatus() + "' WHERE REC_ID=" + payment.getRecId());
    }

    public boolean saveFriend(RFFriend friend) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_FRIEND(REC_ID, CREATE_DT, MODULE, FIRST_NM, LAST_NM, PHONE_NO, GENDER, LOCATION, INTRODUCER, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_FRIEND.NEXTVAL, SYSDATE, '" + friend.getModule() + "', '" + friend.getFirstNm() + "', '" + friend.getLastNm() + "', '" + friend.getPhoneNo() + "', '" + friend.getGender() + "', '" + friend.getLocation() + "', '" + friend.getIntroducer() + "', 'A')");
    }

    public boolean saveGroupWorkflowRecord(WFRecord record) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHG_WF_LOG(REC_ID, CREATE_DT, TXN_REF, ACCESS_CD, GROUP_NO, MEMBER_NO, ROLE, ACCOUNT, CURRENCY, AMOUNT, DETAIL, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHG_WF_LOG.NEXTVAL, SYSDATE, '" + record.getTxnRef() + "', '" + record.getAccessCd() + "', '" + record.getGroupNo() + "', '" + record.getMemberNo() + "', '" + record.getRole() + "', '" + record.getAccount() + "', '" + record.getCurrency() + "', " + record.getAmount() + ", '" + record.getDetail() + "', '" + record.getStatus() + "')");
    }

    public boolean saveEnrollLog(MURecord record) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_ENRL_LOG(REC_ID, CREATE_DT, CHANNEL_ID, ADDRESS, ACCESS_CD, CUST_NO, ACCOUNT, ALIAS, ROLE, TYPE, STATE, RESULT) VALUES(" + APController.cmSchemaName + ".SEQ_PHL_ENRL_LOG.NEXTVAL, SYSDATE, " + record.getChannelId() + ", '" + record.getAddress() + "', '" + record.getAccessCd() + "', '" + record.getCustNo() + "', '" + record.getAccount() + "', '" + record.getAlias().replace("'", "''") + "', '" + record.getRole() + "', '" + record.getType() + "', '" + record.getState() + "', '" + record.getResult() + "')");
    }

    public boolean saveStatementLog(ESRecord record) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHS_EST_LOG(REC_ID, CREATE_DT, TASK, ACCOUNT, START_DT, END_DT, ADDRESS, CHARGE, CHG_ID, REC_ST) VALUES(" + record.getRecId() + ", SYSDATE, '" + record.getTask() + "', '" + record.getAccount() + "', " + convertToOracleDate(record.getStartDt()) + ", " + convertToOracleDate(record.getEndDt()) + ", '" + record.getAddress() + "', " + record.getCharge() + ", " + record.getChgId() + ", '" + record.getStatus() + "')");
    }

    public boolean isAccountPermitted(Long schemeId, String accountNumber) {
        return checkExists("SELECT ACCT_NO FROM " + APController.coreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber + "' AND PROD_ID IN (SELECT PROD_ID FROM " + APController.coreSchemaName + ".PRODUCT_CHANNEL_SCHEME WHERE SCHEME_ID=" + schemeId + ")");
    }

    public CNActivity queryMonthActivity(Long channelId, String accountNumber, String txnCode) {
        CNActivity cNActivity = new CNActivity();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT COUNT(*) AS COUNT, NVL(SUM(AMOUNT),0) AS TOTAL FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE REC_ST='A' AND CHANNEL_ID=" + channelId + " AND TXN_CODE='" + txnCode + "' AND ACCOUNT='" + accountNumber + "' AND TXN_DATE>=TRUNC(ADD_MONTHS(LAST_DAY(SYSDATE),-1)+1) AND TXN_DATE<=TRUNC(LAST_DAY(SYSDATE))")) {
            if (rs != null && rs.next()) {
                cNActivity.setCount(rs.getBigDecimal("COUNT"));
                cNActivity.setTotal(rs.getBigDecimal("TOTAL"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNActivity;
    }

    public CNActivity queryDayActivity(Long channelId, String accountNumber, String txnType) {
        CNActivity cNActivity = new CNActivity();
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, "SELECT COUNT(*) AS COUNT, NVL(SUM(AMOUNT),0) AS TOTAL FROM " + APController.cmSchemaName + ".PHL_TXN_LOG WHERE REC_ST='A' AND CHANNEL_ID=" + channelId + " AND TXN_TYPE='" + txnType + "' AND ACCOUNT='" + accountNumber + "' AND TXN_DATE>=TRUNC(SYSDATE)")) {
            if (rs != null && rs.next()) {
                cNActivity.setCount(rs.getBigDecimal("COUNT"));
                cNActivity.setTotal(rs.getBigDecimal("TOTAL"));
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return cNActivity;
    }

    public int countRecords(String query) {
        int count = 0;
        try (PLAdapter adapter = getAdapter(); Statement statement = createStatement(adapter.getConnection()); ResultSet rs = executeQuery(statement, query)) {
            count = getRowCount(rs);
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return count;
    }

    private boolean saveValues(AXCharge charge) {
        boolean RC = deleteValues(charge);
        for (TCValue tCValue : charge.getValues().values()) {
            tCValue.setRecId(nextValueId());
            if (executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_VALUE(REC_ID, PARENT, CURRENCY, TYPE, MIN_VALUE, MAX_VALUE, VALUE) VALUES(" + tCValue.getRecId() + ", '" + charge.getRecId() + "', '" + tCValue.getCurrency() + "', '" + tCValue.getType() + "', " + tCValue.getMin() + ", " + tCValue.getMax() + ", " + tCValue.getValue() + ")")) {
                RC = saveTiers("C", tCValue.getRecId(), tCValue.getTiers()) && saveDeductions(tCValue);
            }
        }
        return RC;
    }

    private boolean saveDeductions(TCValue value) {
        boolean RC = deleteDeductions(value.getRecId());
        for (TCDeduction deduction : value.getDeductions().values()) {
            deduction.setRecId(nextDeductionId());
            if (RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_DEDUCTION(REC_ID, PARENT, BASIS, DESCRIPTION, ACCOUNT, TYPE, VALUE) VALUES(" + deduction.getRecId() + ", '" + value.getRecId() + "', '" + deduction.getBasis() + "', '" + deduction.getDescription() + "', '" + deduction.getAccount() + "', '" + deduction.getType() + "', " + deduction.getValue() + ")")) {
                saveTiers("D", deduction.getRecId(), deduction.getTiers());
            }
        }
        return RC;
    }

    private boolean saveWaivers(AXCharge charge) {
        boolean RC = deleteWaivers(charge);
        for (TCWaiver waiver : charge.getWaivers().values()) {
            waiver.setWaiverId(nextWaiverId());
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_WAIVER(REC_ID, PARENT, PROD_ID, MATCH_ACCT, WAIVED_PERC, CONDITION, THRESHOLD) VALUES(" + waiver.getWaiverId() + ", '" + charge.getRecId() + "', " + waiver.getProductId() + ", '" + waiver.getMatchAccount() + "', " + waiver.getWaivedPercentage() + ", '" + waiver.getWaiverCondition() + "', " + waiver.getThresholdValue() + ")");
        }
        return RC;
    }

    public boolean moveCustomerWFItem(long custId, long newBuId) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".WF_WORK_ITEM SET BU_ID=" + newBuId + " WHERE WORK_ITEM_ID=(SELECT MAX(WORK_ITEM_ID) FROM " + APController.coreSchemaName + ".WF_WORK_ITEM WHERE CUST_ID=" + custId + ")");
    }

    public CNAccount queryAnyAccount(String accountNumber) {
        return getWorker().isLedger(accountNumber) ? queryLedgerAccount(accountNumber) : queryCustomerAccount(accountNumber);
    }

    private boolean deleteDeductions(Long parentKey) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_TIER WHERE TYPE='D' AND PARENT IN (SELECT REC_ID FROM " + APController.cmSchemaName + ".PHC_DEDUCTION WHERE PARENT='" + parentKey + "')") && executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_DEDUCTION WHERE PARENT='" + parentKey + "'");
    }

    private boolean deleteValues(AXCharge charge) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_TIER WHERE TYPE='D' AND PARENT IN (SELECT REC_ID FROM " + APController.cmSchemaName + ".PHC_VALUE WHERE PARENT='" + charge.getRecId() + "')") && executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_DEDUCTION WHERE PARENT IN (SELECT REC_ID FROM " + APController.cmSchemaName + ".PHC_VALUE WHERE PARENT='" + charge.getRecId() + "')") && executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_VALUE WHERE PARENT='" + charge.getRecId() + "'");
    }

    private boolean deleteWaivers(AXCharge charge) {
        return executeUpdate("DELETE " + APController.cmSchemaName + ".PHC_WAIVER WHERE PARENT='" + charge.getRecId() + "'");
    }

    public int countPendingAlerts() {
        return countRecords("SELECT REC_ST FROM " + APController.cmSchemaName + ".PHA_ALERTS WHERE REC_ST='P'");
    }

    public int countPendingAlerts(ArrayList<String> prefixes) {
        return countRecords("SELECT REC_ST FROM " + APController.cmSchemaName + ".PHA_ALERTS WHERE AND REC_ST='P'");
    }

    public boolean upsertCharge(AXCharge charge) {
        return checkExists("SELECT REC_ID FROM " + APController.cmSchemaName + ".PHC_CHARGE WHERE REC_ID=" + charge.getRecId()) ? updateCharge(charge) : saveCharge(charge);
    }

    private boolean saveCharge(AXCharge charge) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_CHARGE(REC_ID, REC_CD, CREATE_DT, DESCRIPTION, ACCOUNT, LEDGER, SCHEME, SYS_USER, SYS_DATE, BASIS, REC_ST) VALUES(" + charge.getRecId() + ", '" + charge.getCode() + "', SYSDATE, '" + charge.getDescription() + "', '" + charge.getChargeAccount() + "', '" + charge.getChargeLedger() + "', '" + charge.getScheme() + "', '" + charge.getSysUser() + "', SYSDATE, '" + charge.getBasis() + "', '" + charge.getStatus() + "')") ? saveValues(charge) && saveWaivers(charge) : false;
    }

    private boolean updateCharge(AXCharge charge) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHC_CHARGE SET REC_CD='" + charge.getCode() + "', DESCRIPTION='" + charge.getDescription() + "', ACCOUNT='" + charge.getChargeAccount() + "', LEDGER='" + charge.getChargeLedger() + "', SCHEME='" + charge.getScheme() + "', SYS_USER='" + charge.getSysUser() + "', SYS_DATE=SYSDATE, BASIS='" + charge.getBasis() + "', REC_ST='" + charge.getStatus() + "' WHERE REC_ID=" + charge.getRecId()) ? saveValues(charge) && saveWaivers(charge) : false;
    }

    public boolean upsertScheme(TCScheme scheme) {
        return checkExists("SELECT REC_CD FROM " + APController.cmSchemaName + ".PHC_SCHEME WHERE REC_CD='" + scheme.getCode() + "'") ? updateScheme(scheme) : saveScheme(scheme);
    }

    private boolean saveScheme(TCScheme scheme) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHC_SCHEME(REC_CD, CREATE_DT, MODULE, DESCRIPTION, SYS_USER, SYS_DATE, REC_ST) VALUES('" + scheme.getCode() + "', SYSDATE, '" + scheme.getModule() + "', '" + scheme.getDescription() + "', '" + scheme.getSysUser() + "', SYSDATE, '" + scheme.getStatus() + "')");
    }

    private boolean updateScheme(TCScheme scheme) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHC_SCHEME SET MODULE='" + scheme.getModule() + "', DESCRIPTION='" + scheme.getDescription() + "', SYS_USER='" + scheme.getSysUser() + "', SYS_DATE=SYSDATE, REC_ST='" + scheme.getStatus() + "' WHERE REC_CD='" + scheme.getCode() + "'");
    }

    public boolean upsertBank(AXBank bank) {
        return checkExists("SELECT BIN FROM " + APController.cmSchemaName + ".PHL_BANK WHERE BIN='" + bank.getBin() + "'") ? updateBank(bank) : saveBank(bank);
    }

    private boolean saveBank(AXBank bank) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHL_BANK(BIN, CREATE_DT, BANK_NAME, LORO_LEDGER, TRANSFER_LEDGER, SUSPENSE_LEDGER, SYS_USER, SYS_DATE, REC_ST) VALUES('" + bank.getBin() + "', SYSDATE, '" + bank.getName() + "', '" + bank.getLoroLedger() + "', '" + bank.getTransferLedger() + "', '" + bank.getSuspenseLedger() + "', '" + bank.getSysUser() + "', SYSDATE, '" + bank.getStatus() + "')");
    }

    private boolean updateBank(AXBank bank) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHL_BANK SET BANK_NAME='" + bank.getName() + "', LORO_LEDGER='" + bank.getLoroLedger() + "', TRANSFER_LEDGER='" + bank.getTransferLedger() + "', SUSPENSE_LEDGER='" + bank.getSuspenseLedger() + "', SYS_USER='" + bank.getSysUser() + "', SYS_DATE=SYSDATE, REC_ST='" + bank.getStatus() + "' WHERE BIN='" + bank.getBin() + "'");
    }

    public boolean upsertUser(AXUser user) {
        return checkExists("SELECT EMP_NO FROM " + APController.cmSchemaName + ".PHU_USER WHERE EMP_NO='" + user.getUserNumber() + "'") ? updateUser(user) : saveUser(user);
    }

    private boolean saveUser(AXUser user) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHU_USER(EMP_NO, CREATE_DT, LOGIN_ID, STAFF_NAME, SYS_USER, SYS_DATE, REC_ST) VALUES('" + user.getUserNumber() + "', SYSDATE, '" + user.getUserName() + "', '" + user.getStaffName() + "', '" + user.getSysUser() + "', SYSDATE, '" + user.getStatus() + "')") && saveUserRoles(user);
    }

    private boolean updateUser(AXUser user) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHU_USER SET LOGIN_ID='" + user.getUserName() + "', STAFF_NAME='" + user.getStaffName() + "', SYS_USER='" + user.getSysUser() + "', SYS_DATE=SYSDATE, REC_ST='" + user.getStatus() + "' WHERE EMP_NO='" + user.getUserNumber() + "'") && saveUserRoles(user);
    }

    private boolean saveUserRoles(AXUser user) {
        boolean RC = executeUpdate("DELETE " + APController.cmSchemaName + ".PHU_ROLE WHERE EMP_NO='" + user.getUserNumber() + "'");
        for (String role : user.getRoles()) {
            RC = executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHU_ROLE(EMP_NO, ROLE) VALUES('" + user.getUserNumber() + "', '" + role + "')");
        }
        return RC;
    }

    public boolean updateAccountEvent(CAEvent event) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT$AUD SET FORWARDED='Y' WHERE FORWARDED IS NULL AND CUST_CHANNEL_ACCT_ID=" + event.getCustChannelAcctId());
    }

    public boolean changeUserPin(Long userId, String newPin) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET PASSWORD='" + newPin + "', PWD_RESET_FG='N' WHERE CUST_CHANNEL_USER_ID=" + userId) && unlockChannelUser(userId);
    }

    public boolean updateUserKey(Long userId, String newKey) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET QUIZ_CD='" + newKey + "' WHERE CUST_CHANNEL_USER_ID=" + userId);
    }

    public boolean unlockChannelUser(Long userId) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET LOCKED_FG='N', RANDOM_SEED=0, SECURITY_CD=NULL, EXPIRY_DT=SYSDATE+180 WHERE CUST_CHANNEL_USER_ID=" + userId);
    }

    public boolean pushUserExpiry(Long userId) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET EXPIRY_DT=SYSDATE+180 WHERE CUST_CHANNEL_USER_ID=" + userId);
    }

    private boolean updateNextEntityId(String tableName, String columnName) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".ENTITY SET NEXT_NO=(SELECT MAX(" + columnName + ")+1 FROM " + APController.coreSchemaName + "." + tableName + ") WHERE ENTITY_NM = '" + tableName + "'");
    }

    public boolean saveChannelAccount(CNUser cNUser, CNAccount cNAccount) {
        return executeUpdate("INSERT INTO " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT (CUST_CHANNEL_ACCT_ID, CUST_ID, CHANNEL_ID, ACCT_ID, SHORT_NAME, REC_ST, VERSION_NO, ROW_TS, USER_ID, CREATE_DT, CREATED_BY, SYS_CREATE_TS, CUST_CHANNEL_ID) VALUES ((SELECT MAX(CUST_CHANNEL_ACCT_ID) + 1 FROM " + APController.coreSchemaName + ".CUST_CHANNEL_ACCOUNT), " + cNAccount.getCustId() + ", " + cNUser.getChannelId() + ", " + cNAccount.getAcctId() + ", NULL, 'A', 1, SYSDATE, 'SYSTEM', SYSDATE, 'SYSTEM', SYSDATE, " + cNUser.getCustChannelId() + ")") && updateNextEntityId("CUST_CHANNEL_ACCOUNT", "CUST_CHANNEL_ACCT_ID");
    }

    public boolean updateChannelAccount(CNUser cNUser, CNAccount cNAccount) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL SET CHRG_ACCT_ID=" + cNAccount.getAcctId() + " WHERE CUST_CHANNEL_ID=" + cNUser.getCustChannelId());
    }

    public boolean updateAccessName(CNUser cNUser) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET ACCESS_NM='" + cNUser.getAccessName() + "' WHERE CUST_CHANNEL_USER_ID=" + cNUser.getUserId());
    }

    public boolean updateDevice(Long schemeId, String accessCode, String deviceId) {
        return executeUpdate("UPDATE " + APController.coreSchemaName + ".CUSTOMER_CHANNEL_USER SET DEVICE_ID='" + deviceId + "' WHERE ACCESS_CD='" + accessCode + "' AND CHANNEL_SCHEME_ID=" + schemeId);
    }

    public boolean updateAlert(MXMessage mXMessage) {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHA_ALERTS SET CODE='" + mXMessage.getCode() + "', CONTACT='" + mXMessage.getMsisdn() + "', CHRG_AMT=" + mXMessage.getChargeAmount() + ", CHG_ID='" + mXMessage.getChargeId() + "', REC_ST='" + mXMessage.getStatus() + "' WHERE REC_ID=" + mXMessage.getRecId());
    }

    public boolean saveMessage(MXMessage mXMessage) {
        return executeUpdate("INSERT INTO " + APController.cmSchemaName + ".PHA_MESSAGE(REC_ID, CREATE_DT, PARENT, CODE, CUST_ID, CONTACT, MESSAGE, RESULT, RESP_ID, REC_ST) VALUES(" + APController.cmSchemaName + ".SEQ_PHA_MESSAGE.NEXTVAL, SYSDATE, " + mXMessage.getRecId() + ", '" + mXMessage.getCode() + "', " + mXMessage.getCustId() + ", '" + mXMessage.getMsisdn() + "', '" + String.valueOf(mXMessage.getMessage()).replace("'", "''") + "', '" + mXMessage.getResult() + "', '" + mXMessage.getResponseId() + "', '" + mXMessage.getStatus() + "')");
    }

    public boolean expireOldAlerts() {
        return executeUpdate("UPDATE " + APController.cmSchemaName + ".PHA_ALERTS SET REC_ST='E' WHERE CREATE_DT>SYSDATE-7 AND CREATE_DT<SYSDATE-5 AND REC_ST='P'", true);
    }

    public boolean blockDebit(Long accountId) {
        return executeUpdate("INSERT INTO " + APController.coreSchemaName + ".ACCOUNT_BLOCK (ACCT_BLOCK_ID, ACCT_ID, BLOCK_TY_CD, RSN_ID, CREATE_DT, START_DT, END_DT, NOTE, REC_ST, VERSION_NO, ROW_TS, USER_ID, SYS_CREATE_TS, CREATED_BY) VALUES ((SELECT NVL ((MIN(ACCT_BLOCK_ID)-1),-1) FROM " + APController.coreSchemaName + ".ACCOUNT_BLOCK), " + accountId + ", 'ALLDR', " + VXController.blockReasonId + ", TRUNC(SYSDATE), TRUNC(SYSDATE), NULL, 'DEBITS BLOCKED BY MOBILE WS', 'A', 1, SYSTIMESTAMP, 'SYSTEM', SYSTIMESTAMP, 'SYSTEM')");
    }

    public String queryBankName() {
        return getWorker().capitalize(queryParameter("S04", String.class));
    }

    public String querySwiftCode() {
        return getWorker().capitalize(queryParameter("S14", String.class));
    }

    public Statement createStatement(final Connection connection) throws SQLException {
        return connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    public Long nextChargeId() {
        return nextSequenceId("SEQ_PHC_CHARGE");
    }

    public Long nextDeductionId() {
        return nextSequenceId("SEQ_PHC_DEDUCTION");
    }

    public Long nextValueId() {
        return nextSequenceId("SEQ_PHC_VALUE");
    }

    public Long nextWaiverId() {
        return nextSequenceId("SEQ_PHC_WAIVER");
    }

    public Long nextStatementId() {
        return nextSequenceId("SEQ_PHS_EST_LOG");
    }

    public String cleanUpdate(String update) {
        return update.replaceAll("'null'", "NULL").replaceAll("'NULL'", "NULL");
    }

    public String convertToOracleDate(Date date) {
        return !getWorker().isBlank(date) ? "TO_DATE('" + AXConstant.standardDateFormat.format(date) + "','DD-MM-YYYY')" : null;
    }

    public String convertToOracleDate(LocalDate date) {
        return !getWorker().isBlank(date) ? "TO_DATE('" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "','DD-MM-YYYY')" : null;
    }

    public String escape(String value) {
        return value != null ? value.replaceAll("'", "''") : value;
    }

    public PLAdapter getAdapter() throws UniversalConnectionPoolException {
        return getAdapter(false);
    }

    /**
     * @param unlimited
     * @return the connection
     * @throws oracle.ucp.UniversalConnectionPoolException
     */
    public PLAdapter getAdapter(boolean unlimited) throws UniversalConnectionPoolException {
        try {
            while (wait) {
                getWorker().pauseThread(100);
            }
            return new PLAdapter(unlimited);
        } catch (UniversalConnectionPoolException ex) {
            if (ex.getErrorCode() == 29 || ex.getErrorCode() == 45064 || String.valueOf(ex.getMessage()).toLowerCase().contains("all connections in the universal connection pool are in use")) {
                wait = true;
                APMain.acxLog.logEvent("~~~<<<---pool--->>>~~~<<<---" + ex.getErrorCode() + "~" + ex.getMessage() + "--->>>~~~");
                getWorker().pauseThread(1000);
                APController.purgePool();
                return new PLAdapter(unlimited);
            }
            throw ex;
        } finally {
            wait = false;
        }
    }

    /**
     * @return the log
     */
    public APLog getLog() {
        return getBox().getLog();
    }

    /**
     * @return the worker
     */
    public AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the cypher
     */
    public AXCypher getCypher() {
        return getBox().getCypher();
    }

    /**
     * @return the box
     */
    public ATBox getBox() {
        return box;
    }

    /**
     * @param box the box to set
     */
    public void setBox(ATBox box) {
        this.box = box;
    }
}
