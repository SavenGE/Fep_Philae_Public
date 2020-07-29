/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.acx.APLog;
import PHilae.acx.ATBox;
import PHilae.acx.AXFile;
import PHilae.acx.AXProperties;
import PHilae.acx.AXWorker;
import PHilae.acx.TRItem;
import PHilae.enu.ALHeader;
import PHilae.enu.AXResult;
import PHilae.enu.TXType;
import PHilae.model.AXChannel;
import PHilae.model.AXSetting;
import PHilae.model.CNAccount;
import PHilae.model.USRole;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 *
 * @author Pecherk
 */
public class VXController {

    public static final String module = "VMA";
    private static ArrayList<TRItem> treeLog = new ArrayList();
    public static String homeContext = "/vma/home", disburseNetOfFee = "Yes";

    private static ArrayList<ALHeader> headers = new ArrayList();
    public static Integer retryCount = 3, loanOneTerm = 30, loanTwoTerm = 30, loanThreeTerm = 180;
    public static Integer maxClientSessions = 200, timeoutMinutes = 5, serverPort = 8900;

    public static Long channelId = 9L, schemeId = 12L, systemUserId = -99L, nationalIdentityId = 391L;
    public static Long customerTypeId = 771L, relationshipOfficerId = 1020L, industryId = 2200L;
    public static Long sourceOfFundId = 315L, countryId = 689L, marketingCampaignId = 352L;

    public static String secureSocketProtocol = "TLSv1.2", keyFactoryAlgorithm = "SunX509";
    public static String keystoreFile = "vma/cert/philae.ks", keystorePassword = "2DERAbnw0qs=";
    public static String keystoreType = "PKCS12", locale = "en_US", vmaWsContext = "/vma/vserve";

    private static final AXProperties narrations = new AXProperties();
    private static final AXProperties messages = new AXProperties();
    private static final AXProperties advices = new AXProperties();

    public static String usersSpoolTime = "2020-04-22 14:57:52", updateMpesaUrl = "No", creditCustomScreen = "CREDIT_APP";
    public static String enableHttps = "Yes", customerCategory = "PER", mpesaDepositContext = "/vma/cb";
    public static final String confDir = "vma/conf", workDir = "vma/work";

    public static ArrayList<Long> shareProducts = new ArrayList<>();
    public static ArrayList<Long> savingProducts = new ArrayList<>();
    public static ArrayList<Long> nwdProducts = new ArrayList<>();

    public static ArrayList<Long> loanOneProducts = new ArrayList<>();
    public static ArrayList<Long> loanTwoProducts = new ArrayList<>();
    public static ArrayList<Long> loanThreeProducts = new ArrayList<>();

    public static CNAccount mpesaDepositGL = new CNAccount(), MpesaSuspenseGL = new CNAccount();
    public static Integer loanTwoIncomeMonths = 6, ministatementTxnCount = 5;
    public static CNAccount spotcashSettlementAccount = new CNAccount();

    public static Long mobileContactModeId = 237L, creditRatingAgencyId = 11L, approveApplicationEventId = 1008871L;
    public static Long propertyTypeId = 392L, taxGroupId = 382L, openningReasonId = 594L;
    public static Long addressTypeId = 61L, customerSegmentId = 371L, nationalityId = 221L;

    private static TreeMap<String, AXSetting> settings = new TreeMap<>();
    public static String certificateAlias = "philae", defaultRegion = "Embu", defaultDistrict = "Embu";
    public static Long missTitleId = 263L, msTitleId = 262L, mrTitleId = 261L;

    public static Long creditTypeId = 971L, creditPortfolioId = 11L, emailContactModeId = 235L;
    public static Long purposeOfCreditId = 701L, riskClassId = 552L, taxStatusId = 441L;
    public static String riskCode = "200", serviceName = "VMA", mpesaValidatorContext = "/vma/cv", operator = "Spotcash";

    public static Integer loanOneRecoveryDay = 30, loanTwoRecoveryDay = 30;
    public static Integer loanThreeRecoveryDay = 30, loanThreeBalanceMonth = 1;
    public static Integer loanDefaultPardonDays = 180;

    public static Integer loanOneDepositMonths = 3, loanTwoDepositMonths = 6, loanThreeDepositMonths = 0;
    public static Long defaultProductId = 33L, approveAccountEventId = 1007783L, blockReasonId = 761L;
    public static String chargeScheme = "V01", loanOneTermCode = "D", loanTwoTermCode = "D", loanThreeTermCode = "D";

    public static BigDecimal loanOneMaximumAmount = new BigDecimal(5000), loanOneMinimumAmount = new BigDecimal(500);
    public static BigDecimal loanTwoMaximumAmount = new BigDecimal(5000), loanTwoMinimumAmount = new BigDecimal(500);
    public static BigDecimal loanThreeMaximumAmount = new BigDecimal(5000), loanThreeMinimumAmount = new BigDecimal(500);

    /*Safaricom Mpesa Params*/
    public static String mpesaConsumerKey = "45GKYb4MgsMqws9oaGtwqQXwPK5vFmvP", mpesaPaybillNumber = "505368";
    public static String mpesaConsumerSecret = "vZGStQZOPYpdexFp", mpesaTimeoutAction = "Completed";
    public static String mpesaTokenUrl = "https://sandbox.safaricom.co.ke/oauth/v1/generate";
    public static String mpesaRegisterUrl = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl";

    /*deployed by FEP or Sacco Integrating MPesa*/
    public static String mpesaConfirmationUrl = "https://b4fc6b29131e.ngrok.io/vma/cb";
    public static String mpesaValidationUrl = "https://b4fc6b29131e.ngrok.io/vma/cv";

    public static BigDecimal loanOneNwdBalance = new BigDecimal(5000), loanOneNwdPercentage = new BigDecimal(50);
    public static BigDecimal loanTwoNwdBalance = new BigDecimal(5000), loanTwoNwdPercentage = new BigDecimal(33.33);
    public static BigDecimal loanThreeNwdBalance = new BigDecimal(5000), loanThreeNwdPercentage = new BigDecimal(25);

    public static ArrayList<Long> loanTwoForbidProducts = new ArrayList<>();
    public static final HashMap<String, String> lock = new HashMap<>(5);
    public static final HashMap<String, String> calls = new HashMap<>(5);

    private static final ATBox box = new ATBox(getLog());
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
        setSettings(getWorker().configure(VXController.class, module));
        getWorker().loadProperties(new File(confDir, "narrations.prp"), getNarrations());
        getWorker().loadProperties(new File(confDir, "messages.prp"), getMessages());
        getWorker().loadProperties(new File(confDir, "advices.prp"), getAdvices());
        return !getSettings().isEmpty();
    }

    public static void saveNarration(String txnCode, String narration) {
        getNarrations().putIfAbsent(txnCode.toUpperCase(), narration.trim());
    }

    public static void saveNarrations() {
        try {
            if (getNarrations() != null) {
                getNarrations().storeToXML(new FileOutputStream(new File(confDir, "narrations.prp")), module + " Narrations");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    private static void setHeaders() {
        getHeaders().add(ALHeader.TxnCode);
        getHeaders().add(ALHeader.TranId);
        getHeaders().add(ALHeader.ReqId);

        getHeaders().add(ALHeader.TxnDate);
        getHeaders().add(ALHeader.TranType);
        getHeaders().add(ALHeader.BankCode);

        getHeaders().add(ALHeader.VmaNumber);
        getHeaders().add(ALHeader.Msisdn);
        getHeaders().add(ALHeader.VmaAcct);

        getHeaders().add(ALHeader.ShortCode);
        getHeaders().add(ALHeader.TimeStamp);
        getHeaders().add(ALHeader.LinkType);

        getHeaders().add(ALHeader.Account);
        getHeaders().add(ALHeader.Contra);
        getHeaders().add(ALHeader.Currency);

        getHeaders().add(ALHeader.Amount);
        getHeaders().add(ALHeader.Description);
        getHeaders().add(ALHeader.Charge);

        getHeaders().add(ALHeader.DueDate);
        getHeaders().add(ALHeader.MaxAmt);
        getHeaders().add(ALHeader.State);

        getHeaders().add(ALHeader.Receipt);
        getHeaders().add(ALHeader.Balance);
        getHeaders().add(ALHeader.XapiCode);

        getHeaders().add(ALHeader.XapiMsg);
        getHeaders().add(ALHeader.Result);
        getHeaders().add(ALHeader.TxnId);

        getHeaders().add(ALHeader.ChgId);
        getHeaders().add(ALHeader.Response);
        getHeaders().add(ALHeader.RecSt);
    }

    public static void readTreeLog() {
        try {
            File arc = new File("vma/arc/tree.arc");
            if (arc.exists()) {
                Object log = new ObjectInputStream(new FileInputStream(arc)).readObject();
                if (log instanceof ArrayList) {
                    setTreeLog((ArrayList) log);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent("<exception realm=\"vma\">unable to load vma tree log ~ [" + ex.getMessage() + "]</exception>");
        }
    }

    public static void saveTreeLog() {
        if (getTreeLog() != null) {
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getFile().checkParent(new File("vma/arc/tree.arc"))))) {
                os.writeObject(getTreeLog());
                os.flush();
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
    }

    public static String getMessage(AXResult aXResult) {
        return getMessages().getProperty(aXResult.name(), aXResult.getMessage());
    }

    public static String getAdvice(TXType txnType) {
        return getAdvices().getProperty(txnType.name());
    }

    public static void shutdown() {
        saveTreeLog();
    }

    public static boolean isSuspended() {
        return getWorker().isYes(APController.suspendMobile);
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

    private static AXFile getFile() {
        return getBox().getFile();
    }

    public static APLog getLog() {
        return APMain.vmaLog;
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
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
     * @return the narrations
     */
    public static AXProperties getNarrations() {
        return narrations;
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

    /**
     * @return the messages
     */
    public static AXProperties getMessages() {
        return messages;
    }

    /**
     * @return the advices
     */
    public static AXProperties getAdvices() {
        return advices;
    }
}
