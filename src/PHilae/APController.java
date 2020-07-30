/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae;

import PHilae.acx.APLog;
import PHilae.acx.ATBox;
import PHilae.acx.AXCypher;
import PHilae.acx.AXProperties;
import PHilae.acx.AXWorker;
import PHilae.acx.PLAdapter;
import PHilae.est.ESController;

import PHilae.model.CBNode;
import PHilae.model.CLItem;
import PHilae.model.CNCurrency;
import PHilae.sms.ALController;
import PHilae.vma.VXController;
import com.mashape.unirest.http.Unirest;
import com.neptunesoftware.supernova.ws.common.XAPIErrorCodes;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import oracle.ucp.UniversalConnectionPool;
import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.UniversalConnectionPoolException;
import oracle.ucp.UniversalPooledConnection;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 *
 * @author Pecherk
 */
public class APController {

    private static String previousKey;
    private static final String confDir = "acx/conf";
    public static final String application = "PHilae";

    private static boolean endOfYear = false;
    public static Long languageFieldId = 55L, languageListId = 41L;
    private static final AXProperties settings = new AXProperties();

    private static final AXProperties codes = new AXProperties();
    private static final AXProperties results = new AXProperties();    
    private static final AXProperties fields = new AXProperties();

    public static String defaultLanguage = "EN", xapiUser = "biKsgfG84Onp9zM0zF2huA==", autoClearXapiHistory = "No";
    public static String coreSchemaName = "STAGING", xapiPassword = "ACdFgyoXow8uEp5NQ6xqXQ==";
    public static String xapiWsContext = "http://10.10.1.132:9001/supernovaws/";

    public static Integer displayLines = 1000, treeLogSize = 1000, restartHour = 3;
    public static String databaseUrl = "jdbc:oracle:thin:@//10.10.1.132:1521/pdbstaging", cmSchemaName = "CHANNELMANAGER";
    public static Integer yearsToKeepLogs = 2, countryCode = 254;

    public static String lockShutdown = "No", cmSchemaPassword = "k6MjR8pvTyRrwjLHPRk5FA==";
    public static String enableDebug = "No", displayConsole = "Yes", autoRestart = "No";
    private static HashMap<Long, CLItem> languages = new HashMap<>();

    private static final ArrayList<CBNode> xapiNodes = new ArrayList<>();
    public static String suspendMailer = "No", splitOnFly = "Yes";
    private static final ATBox box = new ATBox(APMain.acxLog);

    public static String suspendMobile = "No", suspendAlerts = "No";
    private static CNCurrency currency = new CNCurrency();
    public static Integer pendingTxnExpiryDays = 10;

    public static Integer databaseInitialPoolSize = 5;
    public static Integer databaseMinPoolSize = 5;
    public static Integer databaseMaxPoolSize = 25;

    public static Integer databaseTimeToLiveConnectionTimeout = 60;
    public static Integer databaseConnectionWaitTimeout = 15;
    public static Integer databaseMaxConnectionReuseCount = 100;

    public static Integer databaseMaxConnectionReuseTime = 1800;
    public static Integer databaseAbandonedConnectionTimeout = 30;
    public static Integer databaseTimeoutCheckInterval = 30;

    public static Integer databaseInactiveConnectionTimeout = 120;
    public static String databaseValidateConnectionOnBorrow = "Yes";
    public static Integer databaseMaxStatements = 5;

    public static String databaseDriverName = "oracle.jdbc.driver.OracleDriver";
    public static String datasourceFactoryName = "oracle.jdbc.pool.OracleDataSource";
    public static final PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
    private static UniversalConnectionPoolManager manager = null;
    private static UniversalConnectionPool connectionPool = null;

    public static void initialize() {
        if (configure()) {
            prepare();
            checkLicense();
            setLists();
        }
    }

    public static void prepare() {
        try {
            dataSource.setConnectionFactoryClassName(datasourceFactoryName);
            dataSource.setURL(databaseUrl);
            dataSource.setUser(cmSchemaName);
            dataSource.setPassword(cmSchemaPassword);

            dataSource.setConnectionPoolName(application);
            dataSource.setInitialPoolSize(databaseInitialPoolSize);
            dataSource.setMinPoolSize(databaseMinPoolSize);
            dataSource.setMaxPoolSize(databaseMaxPoolSize);

            dataSource.setTimeToLiveConnectionTimeout(databaseTimeToLiveConnectionTimeout);
            dataSource.setConnectionWaitTimeout(databaseConnectionWaitTimeout);
            dataSource.setMaxConnectionReuseTime(databaseMaxConnectionReuseTime);
            dataSource.setAbandonedConnectionTimeout(databaseAbandonedConnectionTimeout);

            dataSource.setTimeoutCheckInterval(databaseTimeoutCheckInterval);
            dataSource.setInactiveConnectionTimeout(databaseInactiveConnectionTimeout);
            dataSource.setMaxStatements(databaseMaxStatements);
            dataSource.setValidateConnectionOnBorrow(getWorker().isYes(databaseValidateConnectionOnBorrow));
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        try {
            manager = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
            manager.createConnectionPool((UniversalConnectionPoolAdapter) dataSource);
            manager.startConnectionPool(dataSource.getConnectionPoolName());
            connectionPool = manager.getConnectionPool(dataSource.getConnectionPoolName());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        try {
            JRProperties.setProperty(JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
//        try {
//            Authenticator.setDefault(new BasicHTTPAuthenticator(xapiUser, xapiPassword));
//        } catch (Exception ex) {
//            getLog().logEvent(ex);
//        }
    }

    public static boolean configure() {
        try (InputStream sin = new FileInputStream(new File(confDir, "settings.prp"))) {
            getSettings().loadFromXML(sin);
            try {
                boolean updated = false;
                for (Field field : APController.class.getDeclaredFields()) {
                    if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isPrivate(field.getModifiers())) {
                        try {
                            if (!getSettings().containsKey(field.getName())) {
                                getSettings().setProperty(getWorker().capitalize(field.getName(), false), String.valueOf(field.get(null)));
                                updated = true;
                            } else {
                                field.set(null, getWorker().getSetting(getSettings(), field.getName(), field.getType(), getWorker().getItemType(field)));
                            }
                        } catch (Exception ex) {
                            getLog().logEvent(field.getName(), ex);
                        }
                    }
                }
                if (updated) {
                    saveSettings();
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
            setXapiNodes();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }

        EventQueue.invokeLater(()
                -> {
            APMain.consoleDialog.setVisible(getWorker().isYes(displayConsole));
        });

        getWorker().loadProperties(new File(confDir, "fields.prp"), getFields());
        getWorker().loadProperties(new File(confDir, "codes.prp"), getCodes());
        getWorker().loadProperties(new File(confDir, "results.prp"), getResults());
        return !getSettings().isEmpty();
    }

    public static void saveSettings() {
        try {
            getSettings().storeToXML(new FileOutputStream(new File(confDir, "settings.prp")), application + " Properties");
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static void updateSetting(String name, String value) {
        getSettings().setProperty(name, value);
        saveSettings();
    }

    private static void updateCodes() {
        getClient().updateXapiErrors();
        for (Field field : XAPIErrorCodes.class.getDeclaredFields()) {
            if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && !Modifier.isPrivate(field.getModifiers())) {
                try {
                    String code = String.valueOf(field.get(null));
                    if (!getCodes().containsKey(code)) {
                        getCodes().put(code, getWorker().capitalize(field.getName().replaceAll("_", " ")));
                    }
                } catch (Exception ex) {
                    getLog().logEvent(field.getName(), ex);
                }
            }
        }
        saveCodes();
    }

    public static void saveCodes() {
        try {
            if (getCodes() != null) {
                getCodes().storeToXML(new FileOutputStream(new File(confDir, "codes.prp")), "Xapi Codes");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static boolean checkLicense() {
        try (PLAdapter adapter = getClient().getAdapter(); Statement statement = getClient().createStatement(adapter.getConnection()); ResultSet rs = getClient().executeQuery(statement, "SELECT TO_NUMBER(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY')-(SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') FROM " + coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD='S02')) AS DAYS FROM " + coreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD='S769'")) {
            if (rs.next()) {
                if (rs.getDouble(1) < 0) {
                    JOptionPane.showMessageDialog(null, "Software license has expired. Login will be denied.", "License Expired", JOptionPane.ERROR_MESSAGE);
                    APMain.shutdown(false);
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return true;
    }

    public static void configure(String module) {
        switch (module) {
            case VXController.module:
                VXController.configure();
                break;
            case ALController.module:
                ALController.configure();
                break;
            case ESController.module:
                ESController.configure();
                break;
        }
    }

    public static void refresh() {
        try {
            long totalMemory = Runtime.getRuntime().totalMemory(), maxMemory = Runtime.getRuntime().maxMemory();
            if (getWorker().isYes(autoRestart) && (totalMemory >= maxMemory * 0.9 || Integer.parseInt(new SimpleDateFormat("HH").format(new Date())) == restartHour)) {
                getLog().logEvent("~~~<<<---amrs--->>>~~~<<<---" + totalMemory + "--->>>~~~");
                APMain.shutdown(true);
            } else if (totalMemory >= maxMemory / 2) {
                getLog().logEvent("~~~<<<---" + totalMemory + "--->>>~~~");
            }
        } catch (Throwable ex) {
            getLog().logEvent(ex);
        }
    }

    private static void setXapiNodes() {
        getXapiNodes().clear();
        for (String url : xapiWsContext.split("[|]")) {
            getXapiNodes().add(new CBNode(url));
        }
    }

    private static void setLists() {
        setCurrency(getClient().queryCurrency(getClient().queryParameter("S17", Long.class)));
        //setLanguages(getClient().queryCustomListItems(languageListId));
        setLanguages(getClient().defaultLanguageItems());
        updateCodes();
    }

    public static void deleteXapiHistory() {
        try {
            if (getWorker().isYes(autoClearXapiHistory)) {
                getClient().deleteXapiHistory();
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static void shutdown() {
        try {
            VXController.saveTreeLog();
            ALController.saveTreeLog();
            ESController.saveTreeLog();
            Unirest.shutdown();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static synchronized String generateKey() {
        String key;
        do {
            key = BigInteger.valueOf(System.currentTimeMillis()).toString(36).toUpperCase();
        } while (Objects.equals(previousKey, key));
        return (previousKey = key);
    }

    public static void purgePool() {
        try {
            manager.purgeConnectionPool(dataSource.getConnectionPoolName());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static void recyclePool() {
        try {
            manager.recycleConnectionPool(dataSource.getConnectionPoolName());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static void refreshPool() {
        try {
            manager.refreshConnectionPool(dataSource.getConnectionPoolName());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public static UniversalPooledConnection borrowConnection() throws UniversalConnectionPoolException {
        return connectionPool.borrowConnection(connectionPool.getConnectionRetrievalInfo());
    }

    public static void returnConnection(UniversalPooledConnection pooledConnection) throws UniversalConnectionPoolException {
        connectionPool.returnConnection(pooledConnection);
    }

    public static String getMessage(String xapiCode) {
        return xapiCode != null ? getCodes().getProperty(xapiCode, xapiCode) : xapiCode;
    }

    public static boolean isWait() {
        return !VXController.lock.isEmpty();
    }

    public static boolean isDebugMode() {
        return getWorker().isYes(APController.enableDebug);
    }

    public static DBPClient getClient() {
        return getBox().getClient();
    }

    public static APLog getLog() {
        return getBox().getLog();
    }

    /**
     * @return the codes
     */
    public static AXProperties getCodes() {
        return codes;
    }

    /**
     * @return the worker
     */
    public static AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the currency
     */
    public static CNCurrency getCurrency() {
        return currency;
    }

    /**
     * @param pCurrency the currency to set
     */
    public static void setCurrency(CNCurrency pCurrency) {
        currency = pCurrency;
    }

    /**
     * @return the languages
     */
    public static HashMap<Long, CLItem> getLanguages() {
        return languages;
    }

    /**
     * @param aLanguages the languages to set
     */
    public static void setLanguages(HashMap<Long, CLItem> aLanguages) {
        languages = aLanguages;
    }

    /**
     * @return the xapiNodes
     */
    public static ArrayList<CBNode> getXapiNodes() {
        return xapiNodes;
    }

    /**
     * @return the settings
     */
    public static AXProperties getSettings() {
        return settings;
    }

    /**
     * @return the cypher
     */
    public static AXCypher getCypher() {
        return getBox().getCypher();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }

    /**
     * @return the results
     */
    public static AXProperties getResults() {
        return results;
    }

    /**
     * @return the fields
     */
    public static AXProperties getFields() {
        return fields;
    }

    /**
     * @return the endOfYear
     */
    public static boolean isEndOfYear() {
        return endOfYear;
    }

    /**
     * @param aEndOfYear the endOfYear to set
     */
    public static void setEndOfYear(boolean aEndOfYear) {
        endOfYear = aEndOfYear;
    }
}
