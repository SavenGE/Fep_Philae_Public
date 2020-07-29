/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.sms;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.acx.APLog;
import PHilae.acx.ATBox;
import PHilae.acx.AXFile;
import PHilae.acx.AXWorker;
import PHilae.acx.TRItem;
import PHilae.enu.ALHeader;
import PHilae.model.AXChannel;
import PHilae.model.AXSetting;
import PHilae.model.USRole;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Pecherk
 */
public class ALController {

    public static final String module = "SMS";
    public static Long channelId = 9L, systemUserId = -99L, schemeId = 12L;
    public static final String confDir = "sms/conf", logsDir = "sms/logs";

    public static String sendAlertsURL = "https://api.prsp.tangazoletu.com/";
    public static TreeMap<String, AXSetting> settings = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    public static ArrayList<String> whiteList = new ArrayList<>();

    public static String userId = "1392", passKey = "681ELT5DWW", service = "1", sender = "NAWIRI";
    public static Long creditLimitFieldId = 56L, debitLimitFieldId = 57L;
    public static BigDecimal minimumAlertAmount = new BigDecimal(100);

    public static String chargeScheme = "S01", alertType = "Notification", alertOrgCode = "31";
    private static ArrayList<ALHeader> headers = new ArrayList();
    private static ArrayList<TRItem> treeLog = new ArrayList();

    private static final ATBox box = new ATBox(APMain.smsLog);
    private static AXChannel channel = new AXChannel();
    private static USRole role = new USRole();

    static {
        setHeaders();
    }

    public static void initialize() {
        if (configure()) {
            setRole(getClient().queryUserRoles(systemUserId, channelId, true).get(0));
            setChannel(getClient().queryChannel(channelId));
            readTreeLog();
        }
    }

    public static boolean configure() {
        setSettings(getWorker().configure(ALController.class, module));
        return !getSettings().isEmpty();
    }

    private static void setHeaders() {
        getHeaders().add(ALHeader.AlertId);
        getHeaders().add(ALHeader.CreateDt);
        getHeaders().add(ALHeader.AlertCode);

        getHeaders().add(ALHeader.AlertType);
        getHeaders().add(ALHeader.Msisdn);

        getHeaders().add(ALHeader.TxnId);
        getHeaders().add(ALHeader.CustId);
        getHeaders().add(ALHeader.CustName);

        getHeaders().add(ALHeader.Account);
        getHeaders().add(ALHeader.Contra);
        getHeaders().add(ALHeader.ChargeAcct);

        getHeaders().add(ALHeader.TxnDate);
        getHeaders().add(ALHeader.Currency);
        getHeaders().add(ALHeader.Amount);

        getHeaders().add(ALHeader.Description);
        getHeaders().add(ALHeader.Charge);
        getHeaders().add(ALHeader.ChgId);

        getHeaders().add(ALHeader.SchemeId);
        getHeaders().add(ALHeader.AccessCode);
        getHeaders().add(ALHeader.Balance);

        getHeaders().add(ALHeader.Message);
        getHeaders().add(ALHeader.Result);
        getHeaders().add(ALHeader.RecSt);
    }

    public static void readTreeLog() {
        try {
            File arc = new File("sms/arc/tree.arc");
            if (arc.exists()) {
                Object log = new ObjectInputStream(new FileInputStream(arc)).readObject();
                if (log instanceof ArrayList) {
                    setTreeLog((ArrayList) log);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent("<exception realm=\"sms\">unable to load sms tree log ~ [" + ex.getMessage() + "]</exception>");
        }
    }

    public static void saveTreeLog() {
        if (getTreeLog() != null) {
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getFile().checkParent(new File("sms/arc/tree.arc"))))) {
                os.writeObject(getTreeLog());
                os.flush();
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
    }

    public static void expireOldAlerts() {
        try {
            getClient().expireOldAlerts();
        } catch (Throwable ex) {
            APMain.acxLog.logEvent(ex);
        }
    }

    public static boolean isWhiteListed(String mobileNumber) {
        return !getWorker().isBlank(mobileNumber)
                && (getWhiteList().isEmpty() || getWhiteList().contains("*")
                || getWhiteList().contains(getWorker().formatMsisdn(mobileNumber, true))
                || getWhiteList().contains(getWorker().formatMsisdn(mobileNumber, false)));
    }

    public static boolean isSuspended() {
        return getWorker().isYes(APController.suspendAlerts);
    }

    /**
     * @return the settings
     */
    public static TreeMap<String, AXSetting> getSettings() {
        return settings;
    }

    /**
     * @param aSettings the settings to set
     */
    public static void setSettings(TreeMap<String, AXSetting> aSettings) {
        settings = aSettings;
    }

    /**
     * @return the worker
     */
    public static AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the dclient
     */
    public static DBPClient getClient() {
        return getBox().getClient();
    }

    /**
     * @return the role
     */
    public static USRole getRole() {
        return role;
    }

    /**
     * @param aSystemRole the role to set
     */
    public static void setRole(USRole aSystemRole) {
        role = aSystemRole;
    }

    private static AXFile getFile() {
        return getBox().getFile();
    }

    public static APLog getLog() {
        return getBox().getLog();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }

    /**
     * @return the headers
     */
    public static ArrayList<ALHeader> getHeaders() {
        return headers;
    }

    /**
     * @param aHeaders the headers to set
     */
    public static void setHeaders(ArrayList<ALHeader> aHeaders) {
        headers = aHeaders;
    }

    /**
     * @return the treeLog
     */
    public static ArrayList<TRItem> getTreeLog() {
        return treeLog;
    }

    /**
     * @param aTreeLog the treeLog to set
     */
    public static void setTreeLog(ArrayList<TRItem> aTreeLog) {
        treeLog = aTreeLog;
    }

    /**
     * @return the whiteList
     */
    public static ArrayList<String> getWhiteList() {
        return whiteList;
    }

    /**
     * @param aWhiteList the whiteList to set
     */
    public static void setWhiteList(ArrayList<String> aWhiteList) {
        whiteList = aWhiteList;
    }

    /**
     * @return the channel
     */
    public static AXChannel getChannel() {
        return channel;
    }

    /**
     * @param aChannel the channel to set
     */
    public static void setChannel(AXChannel aChannel) {
        channel = aChannel;
    }
}
