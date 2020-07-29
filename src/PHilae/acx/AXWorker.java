/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.DBPClient;
import static PHilae.acx.AXConstant.HUNDRED;
import PHilae.enu.AXResult;
import PHilae.model.AXCharge;
import PHilae.model.AXRequest;
import PHilae.model.AXSetting;
import PHilae.model.AXTier;
import PHilae.model.CNAccount;
import PHilae.model.CNActivity;
import PHilae.model.CNBranch;
import PHilae.model.LTPage;
import PHilae.model.TCDeduction;
import PHilae.model.TCSplit;
import PHilae.model.TCValue;
import PHilae.model.TCWaiver;
import com.neptunesoftware.supernova.ws.common.XAPIErrorData;
import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftwareplc.ci.transfer.service.CiErrorData;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RootPaneContainer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.xml.serializer.OutputPropertiesFactory;
import org.json.JSONObject;

/**
 *
 * @author Pecherk
 */
public final class AXWorker implements Serializable {

    private ATBox box;
    private final Pattern holderPattern = Pattern.compile("\\{.*?\\}");
    private final Pattern namePattern = Pattern.compile("^[a-zA-Z\\s]*$");
    public final DecimalFormat decimalFormat = new DecimalFormat("#,##0.##");
    private final HashMap<String, String> escapeXters = new HashMap<>();
    private final HashMap<String, String> unescapeXters = new HashMap<>();
    private final Pattern rTrimPattern = Pattern.compile("\\s+$");
    private final Pattern lTrimPattern = Pattern.compile("^\\s+");
    private final Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

    public AXWorker(ATBox box) {
        setBox(box);
        initialize();
    }

    private void initialize() {
        escapeXters.put("&", "&amp;");
        unescapeXters.put("&lt;", "<");
        unescapeXters.put("&gt;", ">");
        unescapeXters.put("&amp;", "&");
        unescapeXters.put("&apos;", "'");
        unescapeXters.put("&quot;", "\"");
    }

    public void loadProperties(File file, AXProperties properties) {
        try (InputStream pin = new FileInputStream(file)) {
            properties.loadFromXML(pin);
        } catch (Exception ex) {
            getLog().logEvent(file.getName(), ex);
        }
    }

    public <T> T getSetting(AXProperties settings, String code, Class<T> clazz, Class<?> itemType) {
        if (settings.containsKey(code)) {
            try {
                String value = settings.getProperty(code);
                return clazz != ArrayList.class ? (T) clazz.getConstructor(String.class).newInstance(value) : createList(value, clazz, itemType);
            } catch (Exception ex) {
                getLog().logEvent(code, ex);
            }
        }
        return null;
    }

    public <T> T getSetting(TreeMap<String, AXSetting> settings, String code, Class<T> clazz, T defaultValue) {
        return getSetting(settings, code, clazz, String.class, defaultValue);
    }

    public <T> T getSetting(TreeMap<String, AXSetting> settings, String code, Class<T> clazz, Class<?> itemType, T defaultValue) {
        if (settings.containsKey(code)) {
            try {
                String value = settings.get(code).getValue();
                if (clazz == ArrayList.class) {
                    return createList(value, clazz, itemType);
                } else if (clazz == CNAccount.class) {
                    return (T) getClient().queryAnyAccount(value);
                } else if (clazz == CNBranch.class) {
                    return (T) getClient().queryBranch(Long.valueOf(value));
                } else {
                    return (T) clazz.getConstructor(String.class).newInstance(value);
                }
            } catch (Exception ex) {
                getLog().logEvent(code, ex);
            }
        }
        return defaultValue;
    }

    public TreeMap<String, AXSetting> configure(Class<?> clazz, String module) {
        TreeMap<String, AXSetting> settings = getClient().querySettings(module);
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isPrivate(field.getModifiers())) {
                    try {
                        if (isSettings(field)) {
                            field.set(null, settings);
                        } else if (!settings.containsKey(field.getName())) {
                            getClient().saveSetting(createSetting(field, module));
                        } else {
                            field.set(null, getSetting(settings, field.getName(), field.getType(), getItemType(field), null));
                        }
                    } catch (Exception ex) {
                        getLog().logEvent(field.getName(), ex);
                    }
                    getLog().logDebug(field.getName() + "=" + field.get(null));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }

        return settings;
    }

    private String convertToString(ArrayList parents, Object object, String tag) {
        tag = decapitalize(tag);
        String text = "", items = "", prevVal = "";
        Class<?> beanClass = !isBlank(object) ? object.getClass() : String.class;
        try {
            if (isBlank(object)) {
                return !isBlank(tag) ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object);
            }
            if (object instanceof JAXBElement) {
                return convertToString(parents, extractValue((JAXBElement) object), tag);
            }
            for (MethodDescriptor methodDescriptor : Introspector.getBeanInfo(beanClass).getMethodDescriptors()) {
                if ("toString".equalsIgnoreCase(methodDescriptor.getName()) && beanClass == methodDescriptor.getMethod().getDeclaringClass() && methodDescriptor.getMethod().getAnnotation(AXIgnore.class) == null) {
                    return !isBlank(tag) ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object);
                }
            }
            if (object instanceof byte[]) {
                return !isBlank(tag) ? (tag + "=<" + new String((byte[]) object) + ">") : new String((byte[]) object);
            }

            tag = isBlank(tag) ? beanClass.getSimpleName() : tag;

            if (object instanceof Collection) {
                for (Object item : ((Collection) object).toArray()) {
                    items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n" : "") + (prevVal = convertToString(parents, item, null)).trim();
                }
                return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
            } else if (object instanceof Map) {
                for (Object key : ((Map) object).keySet()) {
                    items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n" : "") + (prevVal = convertToString(parents, ((Map) object).get(key), String.valueOf(key))).trim();
                }
                return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
            } else if (beanClass.isArray()) {
                switch (beanClass.getSimpleName()) {
                    case "int[]":
                        return (tag + "=<" + Arrays.toString((int[]) object) + ">");
                    case "long[]":
                        return (tag + "=<" + Arrays.toString((long[]) object) + ">");
                    case "boolean[]":
                        return (tag + "=<" + Arrays.toString((boolean[]) object) + ">");
                    case "byte[]":
                        return (tag + "=<" + Arrays.toString((byte[]) object) + ">");
                    case "char[]":
                        return (tag + "=<" + Arrays.toString((char[]) object) + ">");
                    case "double[]":
                        return (tag + "=<" + Arrays.toString((double[]) object) + ">");
                    case "float[]":
                        return (tag + "=<" + Arrays.toString((float[]) object) + ">");
                    case "short[]":
                        return (tag + "=<" + Arrays.toString((short[]) object) + ">");
                }
                if (object instanceof Object[]) {
                    for (Object item : (Object[]) object) {
                        items += (isBlank(items) ? "" : ", ") + (prevVal.contains("\r\n") ? "\r\n\t" : "") + (prevVal = convertToString(parents, item, null)).trim();
                    }
                    return items.contains("\r\n") ? ("\r\n" + tag + "=<[" + "\r\n\t" + items + "\r\n" + "]>") : (tag + "=<[" + (!isBlank(items) ? " " + items + " " : "") + "]>");
                } else {
                    return (tag + "=<[" + String.valueOf(object) + "]>");
                }
            } else {
                Method readMethod;
                parents.add(beanClass);
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(beanClass).getPropertyDescriptors()) {
                    if ((readMethod = propertyDesc.getReadMethod()) != null) {
                        Object value = "@unknown";
                        try {
                            value = readMethod.invoke(object);
                        } catch (Exception ex) {
                            ex = null;
                        }
                        if (!(value instanceof Class)) {
                            text += (isBlank(text) ? "" : ", ") + convertToString(new ArrayList<>(parents), !parents.contains(value != null ? value.getClass() : null) ? value : String.valueOf(value), propertyDesc.getName());
                        }
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return (!isBlank(tag) ? (tag + "=<[ " + (isBlank(text) ? String.valueOf(object) : text) + " ]>") : (isBlank(text) ? String.valueOf(object) : text));
    }

    public JTable createObjectTable(Object object, Properties fieldNames) {
        DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Field", "Content", "Description"
                }
        ) {
            Class[] types = new Class[]{
                String.class, String.class, String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        try {
            for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
                String name = propertyDesc.getName();
                Object value = propertyDesc.getReadMethod().invoke(object);
                if (value != null && !"class".equalsIgnoreCase(propertyDesc.getName()) && !(value instanceof ArrayList)) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(name.substring(0, 1).toUpperCase() + name.substring(1));
                    arrayList.add(convertToString(value));
                    arrayList.add(fieldNames.getProperty(name.toUpperCase(), capitalize(name)));
                    tableModel.addRow(arrayList.toArray());
                }
            }
            JTable table = new JTable(tableModel);
            table.getColumnModel().getColumn(0).setMinWidth(100);
            table.getColumnModel().getColumn(1).setMinWidth(360);
            table.getColumnModel().getColumn(2).setMinWidth(360);
            return table;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new JTable(tableModel);
    }

    public void setCharge(AXRequest aXRequest, HashMap<String, String> splitAccounts) {
        AXCharge aXCharge = getClient().queryCharge(aXRequest.getCharge().getScheme(), aXRequest.getCharge().getCode());
        if (!isBlank(aXCharge) && !isBlank(aXCharge.getRecId())) {
            aXRequest.getCharge().setNarration((aXRequest.isReversal() ? "REV~" : "") + aXCharge.getDescription());
            aXRequest.getCharge().setChargeAccount("R".equals(aXCharge.getChargeAccount()) ? aXRequest.getContra() : aXRequest.getAccount());
            aXRequest.getCharge().setChargeAccount(aXRequest.getCharge().getChargeAccount().isLoan() ? getClient().queryRepaymentAccount(aXRequest.getCharge().getChargeAccount().getAccountNumber()) : aXRequest.getCharge().getChargeAccount());

            aXRequest.getCharge().setIncomeLedger(getClient().unmaskLedger(aXCharge.getChargeLedger(), aXRequest.getBranch()).getAccountNumber());
            aXRequest.getCharge().setChannelLedger(getClient().unmaskLedger(aXRequest.getChannel().getDebitContra(), aXRequest.getBranch()).getAccountNumber());

            final String currency = aXRequest.getCharge().getChargeAccount().getCurrency().getCode();
            TCValue value = aXCharge.getValues().getOrDefault(currency, new TCValue());
            BigDecimal chargeAmount = BigDecimal.ZERO;

            switch (value.getType()) {
                case "C":
                    aXRequest.getCharge().setChargeAmount(value.getValue());
                    break;
                case "O":
                    chargeAmount = value.getMin().add(aXRequest.getAmount().multiply(value.getValue()).divide(HUNDRED, 2, RoundingMode.HALF_UP));
                    aXRequest.getCharge().setChargeAmount(BigDecimal.ZERO.compareTo(value.getMax()) < 0 && chargeAmount.compareTo(value.getMax()) > 0 ? value.getMax() : chargeAmount);
                    break;
                case "P":
                    chargeAmount = aXRequest.getAmount().multiply(value.getValue()).divide(HUNDRED, 2, RoundingMode.HALF_UP);
                    aXRequest.getCharge().setChargeAmount(BigDecimal.ZERO.compareTo(value.getMax()) < 0 && chargeAmount.compareTo(value.getMax()) > 0 ? value.getMax() : (BigDecimal.ZERO.compareTo(value.getMin()) < 0 && chargeAmount.compareTo(value.getMin()) < 0 ? value.getMin() : chargeAmount));
                    break;
                case "T":
                    Object[] tiers = sortArray(value.getTiers().values().toArray(), true);
                    if (tiers.length > 0) {
                        aXRequest.getCharge().setChargeAmount(((AXTier) tiers[tiers.length - 1]).getValue());
                        for (int i = tiers.length - 1; i >= 0; i--) {
                            if (aXRequest.getAmount().compareTo(((AXTier) tiers[i]).getTierMax()) <= 0) {
                                aXRequest.getCharge().setChargeAmount(((AXTier) tiers[i]).getValue());
                            }
                        }
                    }
                    break;
            }

            switch (aXCharge.getBasis()) {
                case "P":
                    if (aXRequest.getPages() > 0) {
                        aXRequest.getCharge().setFactor(aXRequest.getPages());
                        aXRequest.getCharge().setNarration(aXRequest.getCharge().getNarration() + " [ " + aXRequest.getPages() + " Pages ]");
                    }
                    break;
            }

            aXRequest.getCharge().setChargeAmount(aXRequest.getCharge().getChargeAmount().multiply(new BigDecimal(aXRequest.getCharge().getFactor())));
            Object[] waivers = sortArray(aXCharge.getWaivers().values().toArray(), true);

            if (waivers.length > 0) {
                for (int i = waivers.length - 1; i >= 0; i--) {
                    TCWaiver waiver = (TCWaiver) waivers[i];
                    if (0 == waiver.getProductId() || Objects.equals(waiver.getProductId(), aXRequest.getCharge().getChargeAccount().getProductId()) || Objects.equals(waiver.getProductId(), aXRequest.getContra().getProductId())) {
                        if (0 == waiver.getProductId() || "A".equals(waiver.getMatchAccount()) || ("B".equals(waiver.getMatchAccount()) && Objects.equals(aXRequest.getCharge().getChargeAccount().getProductId(), aXRequest.getContra().getProductId())) || ("C".equals(waiver.getMatchAccount()) && Objects.equals(waiver.getProductId(), aXRequest.getCharge().getChargeAccount().getProductId()))) {
                            boolean waive = false;
                            CNActivity activity = getClient().queryMonthActivity(aXRequest.getChannel().getId(), aXRequest.getAccount().getAccountNumber(), aXRequest.getType().getCode());
                            switch (waiver.getWaiverCondition()) {
                                case "ALL":
                                    waive = true;
                                    break;
                                case "MCL":
                                    waive = activity.getCount().compareTo(waiver.getThresholdValue()) <= 0;
                                    break;
                                case "MCM":
                                    waive = activity.getCount().compareTo(waiver.getThresholdValue()) >= 0;
                                    break;
                                case "MTL":
                                    waive = activity.getTotal().compareTo(waiver.getThresholdValue()) <= 0;
                                    break;
                                case "MTM":
                                    waive = activity.getTotal().compareTo(waiver.getThresholdValue()) >= 0;
                                    break;
                            }
                            if (waive) {
                                aXRequest.getCharge().setChargeAmount((aXRequest.getCharge().getChargeAmount().multiply((HUNDRED.subtract(waiver.getWaivedPercentage())).divide(HUNDRED, 2, RoundingMode.HALF_UP))).setScale(2, RoundingMode.DOWN));
                                break;
                            }
                        }
                    }
                }
            }
            setDeductions(aXRequest, value, splitAccounts);
        }
    }

    private void setDeductions(AXRequest aXRequest, TCValue value, HashMap<String, String> splitAccounts) {
        String basis = "A";
        BigDecimal basisAmount = aXRequest.getCharge().getChargeAmount();
        BigDecimal balance = aXRequest.getCharge().getChargeAmount();
        HashMap<String, TCSplit> splitMap = new HashMap<>();

        for (TCDeduction deduction : sortArray(value.getDeductions().values().toArray(new TCDeduction[0]), true)) {
            TCSplit split = new TCSplit();
            split.setReference(deduction.getBasis());

            split.setDescription((aXRequest.isReversal() ? "REV~" : "") + deduction.getDescription().trim());
            split.setAccount(splitAccounts.getOrDefault(deduction.getAccount(), (isLedger(deduction.getAccount()) ? getClient().unmaskLedger(deduction.getAccount(), aXRequest.getBranch()).getAccountNumber() : deduction.getAccount())));

            if (!basis.equals(deduction.getBasis())) {
                basisAmount = balance;
            }
            basis = deduction.getBasis();

            switch (deduction.getType()) {
                case "C":
                    split.setAmount(deduction.getValue());
                    break;
                case "P":
                    switch (deduction.getBasis()) {
                        case "A":
                            split.setAmount(basisAmount.multiply(deduction.getValue().divide(HUNDRED, 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                            break;
                        case "B":
                            split.setAmount(basisAmount.multiply(deduction.getValue().divide(HUNDRED, 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                            break;
                        case "C":
                            split.setAmount(basisAmount.multiply(deduction.getValue().divide(HUNDRED.add(deduction.getValue()), 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                            break;
                        case "D":
                            split.setAmount(basisAmount.multiply(deduction.getValue().divide(HUNDRED.add(deduction.getValue()), 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                            break;
                        default:
                            split.setAmount(basisAmount.multiply(deduction.getValue().divide(HUNDRED, 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                            break;
                    }
                    break;
                case "O":
                    split.setAmount(aXRequest.getAmount().multiply(deduction.getValue().divide(HUNDRED, 16, RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "T":
                    Object[] tiers = sortArray(deduction.getTiers().values().toArray(), true);
                    if (tiers.length > 0) {
                        split.setAmount(((AXTier) tiers[tiers.length - 1]).getValue());
                        for (int i = tiers.length - 1; i >= 0; i--) {
                            if (aXRequest.getCharge().getChargeAmount().compareTo(((AXTier) tiers[i]).getTierMax()) <= 0) {
                                split.setAmount(((AXTier) tiers[i]).getValue());
                            }
                        }
                    }
                    break;
            }
            if (balance.compareTo(split.getAmount()) >= 0 && BigDecimal.ZERO.compareTo(split.getAmount()) < 0) {
                if (splitMap.containsKey(split.getAccount())) {
                    TCSplit spl = splitMap.get(split.getAccount());
                    spl.setAmount(spl.getAmount().add(split.getAmount()));
                    if (!Objects.equals(split.getDescription().toUpperCase(), spl.getDescription().toUpperCase())) {
                        spl.setDescription(spl.getDescription() + " | " + split.getDescription());
                    }
                    splitMap.put(spl.getAccount(), spl);
                } else {
                    splitMap.put(split.getAccount(), split);
                }
                balance = balance.subtract(split.getAmount());
            }
        }

        splitMap.values().stream().forEach((spl)
                -> {
            aXRequest.getCharge().getSplitList().add(spl);
        });
        splitMap.clear();

        if (!aXRequest.getCharge().getSplitList().isEmpty() && BigDecimal.ZERO.compareTo(balance) < 0) {
            TCSplit split = new TCSplit();
            split.setReference("Z");

            split.setDescription(aXRequest.getCharge().getNarration());
            split.setAccount(aXRequest.getCharge().getIncomeLedger());

            split.setAmount(balance);
            aXRequest.getCharge().getSplitList().add(split);
        }
    }

    public String mapNarration(String narration, String DC) {
        DC = "C".equalsIgnoreCase(DC.trim()) ? "CR" : DC.trim();
        DC = "D".equalsIgnoreCase(DC.trim()) ? "DR" : DC.trim();
        if (narration.toUpperCase().contains("REV~") || narration.toUpperCase().contains("REVERSAL") || narration.toUpperCase().contains("RETURN")) {
            return "Reversal";
        } else if (narration.toUpperCase().contains("CHARGE")) {
            return "Charge";
        } else if (narration.toUpperCase().contains("LOAN")) {
            return "Loan Pay";
        } else if (narration.toUpperCase().contains("PAYMENT")) {
            return "Payment";
        } else if (narration.toUpperCase().contains("WITHDRAWAL")) {
            return "Withdrawal";
        } else if (narration.toUpperCase().contains("DEPOSIT")) {
            return "Deposit";
        } else if (narration.toUpperCase().contains("TRANSFER")) {
            return "Transfer";
        }
        return "CR".equalsIgnoreCase(DC) ? "Deposit" : "Withdrawal";
    }

    public String formatMsisdn(String msisdn, boolean local) {
        if (!isBlank(msisdn) && msisdn.length() >= 9) {
            msisdn = msisdn.split("[/\\\\,;]")[0].replaceAll("[^\\d]", "");
            if (msisdn.length() == 9) {
                return (local ? "0" : APController.countryCode) + msisdn;
            } else if (msisdn.startsWith(String.valueOf(APController.countryCode))) {
                return local ? "0" + msisdn.substring(3) : msisdn;
            } else if (msisdn.startsWith("0")) {
                return local ? msisdn : APController.countryCode + msisdn.substring(1);
            }
        }
        return msisdn;
    }

    public String cleanText(String text) {
        String line;
        StringBuilder buffer = new StringBuilder();
        if (!isBlank(text)) {
            try (BufferedReader bis = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())))) {
                while ((line = bis.readLine()) != null) {
                    buffer.append(line.trim());
                }
            } catch (Exception ex) {
                getLog().logEvent(text, ex);
            }
        }
        return buffer.toString().replaceAll(">\\s+<", "><");
    }

    public BigDecimal cleanAmount(String amountText, BigDecimal defaultValue) {
        return convertToType(checkBlank(amountText, "").replaceAll("[^\\d.]", ""), BigDecimal.class, defaultValue);
    }

    public <T> T cloneObject(Object source, Class<T> clazz) {
        T result = null;
        if (source != null) {
            try {
                result = clazz.newInstance();
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors()) {
                    if (propertyDesc.getReadMethod() != null) {
                        Object value = propertyDesc.getReadMethod().invoke(source);
                        if (propertyDesc.getWriteMethod() != null) {
                            propertyDesc.getWriteMethod().invoke(result, value);
                        }
                    }
                }
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return result;
    }

    public void resetAllFields(Component component, Component... exceptions) {
        List<Component> list = Arrays.asList(exceptions);
        if (!list.contains(component)) {
            if (component instanceof JPanel) {
                if (((JPanel) component).getBorder() != null && ((JPanel) component).getBorder() instanceof CBBorder) {
                    ((JCheckBox) ((CBBorder) ((JPanel) component).getBorder()).getComponent()).setSelected(false);
                    component.repaint();
                }
                for (Component comp : ((JPanel) component).getComponents()) {
                    resetAllFields(comp, exceptions);
                }
            } else if (component instanceof JScrollPane) {
                resetAllFields(((JScrollPane) component).getViewport().getView(), exceptions);
            } else if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setText("");
            } else if (component instanceof JCheckBox && component.isEnabled()) {
                ((JCheckBox) component).setSelected(false);
            } else if (component instanceof JComboBox && component.isEnabled() && ((JComboBox) component).getItemCount() > 0) {
                ((JComboBox) component).setSelectedIndex(0);
            } else if (component instanceof JDateChooser && ((JDateChooser) component).isEnabled()) {
                ((JDateChooser) component).setDate(null);
            }
        }
    }

    public void setAllFields(Component component, boolean enabled) {
        if (component instanceof JPanel) {
            for (Component comp : ((JPanel) component).getComponents()) {
                setAllFields(comp, enabled);
            }
        }
        if (component instanceof JTextField) {
            ((JTextField) component).setEditable(enabled);
        }
        if (component instanceof JTextArea) {
            ((JTextArea) component).setEditable(enabled);
        }
        if (component instanceof JCheckBox) {
            ((JCheckBox) component).setEnabled(enabled);
        }
        if (component instanceof JComboBox) {
            ((JComboBox) component).setEnabled(enabled);
        }
        if (component instanceof JDateChooser) {
            ((JDateChooser) component).setEnabled(enabled);
        }
    }

    public void setCheckBorder(JPanel panel, boolean selectBoxes) {
        if (panel.getBorder() instanceof TitledBorder) {
            TitledBorder border = (TitledBorder) panel.getBorder();
            JCheckBox checkBox = new JCheckBox(border.getTitle());
            checkBox.setForeground(border.getTitleColor());
            checkBox.setFont(border.getTitleFont());
            checkBox.addActionListener((ActionEvent e)
                    -> {
                boolean selected = checkBox.isSelected();
                for (Component comp : panel.getComponents()) {
                    if (selectBoxes && comp instanceof JCheckBox) {
                        ((JCheckBox) comp).setSelected(selected);
                    } else {
                        comp.setEnabled(selected);
                    }
                }
            });
            checkBox.setFocusPainted(false);
            panel.setBorder(new CBBorder(checkBox, panel, border.getBorder()));
        }
    }

    public String convertToXml(Object object, boolean formatted) {
        if (!isBlank(object)) {
            try {
                Class clazz = object instanceof JAXBElement ? ((JAXBElement) object).getValue().getClass() : object.getClass();
                Marshaller marshaller = JAXBContext.newInstance(clazz).createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatted);

                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
                StringWriter writer = new StringWriter();
                marshaller.marshal(object, writer);
                return cleanXmlXters(writer.toString().trim());
            } catch (Exception ex) {
                getLog().logEvent(ex);
            }
        }
        return null;
    }

    public <T> T convertXmlToObject(String xml, String rootElement, Class<T> clazz) {
        try {
            return (T) JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(new ByteArrayInputStream(removeNameSpaces(extractXmlValue(xml, rootElement, true)).getBytes(StandardCharsets.ISO_8859_1)));
        } catch (Exception ex) {
            getLog().logEvent(xml, ex);
        }
        return null;
    }

    public <T> T extractSoapBody(String soapXml, Class<T> clazz) {
        try {
            return (T) JAXBContext.newInstance(clazz).createUnmarshaller().unmarshal(MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(soapXml.getBytes())).getSOAPBody().extractContentAsDocument());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public Object extractSoapObject(String soapXml, String packageName) {
        try {
            return JAXBContext.newInstance(packageName).createUnmarshaller().unmarshal(MessageFactory.newInstance().createMessage(null, new ByteArrayInputStream(soapXml.getBytes())).getSOAPBody().extractContentAsDocument());
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public CNAccount findAccount(ArrayList<CNAccount> accounts, String accountNumber) {
        for (CNAccount account : accounts) {
            if (Objects.equals(account.getAccountNumber(), accountNumber)) {
                return account;
            }
        }
        return new CNAccount();
    }

    public CNAccount findAccount(ArrayList<CNAccount> accounts, String currency, boolean includeLoan) {
        for (CNAccount account : accounts) {
            if (Objects.equals(account.getCurrency().getCode(), currency) && (includeLoan || account.isDeposit())) {
                return account;
            }
        }
        return new CNAccount();
    }

    public boolean hasAccount(ArrayList<CNAccount> accounts, String currency) {
        return accounts.stream().anyMatch((account) -> (Objects.equals(account.getCurrency().getCode(), currency)));
    }

    public JTable createItemTable(LinkedHashMap<String, Object> fieldMap) {
        DefaultTableModel tableModel = new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Field", "Content", "Description"
                }
        ) {
            Class[] types = new Class[]{
                String.class, String.class, String.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };
        try {
            fieldMap.keySet().stream().map((key)
                    -> {
                ArrayList arrayList = new ArrayList();
                arrayList.add(key);
                arrayList.add(fieldMap.get(key));
                arrayList.add(APController.getFields().getProperty(key, capitalize(spaceWords(key))));
                return arrayList;
            }).forEach((arrayList)
                    -> {
                tableModel.addRow(arrayList.toArray());
            });

            JTable table = new JTable(tableModel);
            table.getColumnModel().getColumn(0).setMinWidth(150);
            table.getColumnModel().getColumn(1).setMinWidth(300);
            table.getColumnModel().getColumn(2).setMinWidth(300);
            return table;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new JTable(tableModel);
    }

    public <T> T createList(String csvList, Class<T> clazz, Class<?> itemType) {
        T list = null;
        try {
            list = clazz.newInstance();
            if (csvList != null) {
                for (String listItem : csvList.replaceAll(";", ",").split(",")) {
                    ((List) list).add(convertToType(listItem.trim(), checkBlank(itemType, String.class)));
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(csvList, ex);
        }
        return list;
    }

    public Class<?> getItemType(Field listField) {
        try {
            return listField.getGenericType() instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) listField.getGenericType()).getActualTypeArguments()[0] : String.class;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return String.class;
    }

    public ArrayList<String> createArrayList(String csvList) {
        ArrayList<String> list = new ArrayList<>();
        if (!isBlank(csvList)) {
            for (String listItem : csvList.replaceAll(";", ",").split(",")) {
                if (!isBlank(listItem) && !list.contains(listItem)) {
                    list.add(listItem.trim());
                }
            }
        }
        return list;
    }

    public <T> ArrayList<T> combineLists(ArrayList<T>... lists) {
        ArrayList<T> list = new ArrayList<>();
        for (ArrayList listItem : lists) {
            if (!isBlank(listItem)) {
                list.addAll(listItem);
            }
        }
        return list;
    }

    public String cleanCsvList(String csvList) {
        ArrayList<String> list = new ArrayList<>();
        if (!isBlank(csvList)) {
            for (String listItem : csvList.replaceAll(";", ",").split(",")) {
                if (!isBlank(listItem) && !list.contains(listItem)) {
                    list.add(listItem.trim());
                }
            }
        }
        return createCsvList(list);
    }

    public ArrayList<LTPage> paginateMessage(String message, int asciiPageSize, String nextText) {
        String brx = "\n~. ,/ ";
        LTPage prevPage = new LTPage();
        ArrayList<LTPage> pages = new ArrayList<>();

        int ni, pg = 1, tl = nextText.length(), ps = isPureAscii(message) ? asciiPageSize : asciiPageSize / 2;
        while ((ni = nextPageBreak(message.trim(), brx, 0, ps, tl, 0)) > 0) {
            LTPage page = new LTPage(pg++, (message.substring(0, ni).trim() + (ni < message.length() ? nextText : "")));
            message = message.substring(ni);
            prevPage.setNextPage(page);

            pages.add(page);
            prevPage = page;
        }
        return pages;
    }

    private int nextPageBreak(String ussdText, String brx, int ni, int ps, int tl, int pi) {
        int in, def = ussdText.length() > ps ? ps - tl : ussdText.length();
        if (ussdText.length() > ps && brx.length() > 0) {
            String ch = brx.substring(0, 1);
            while ((in = ussdText.indexOf(ch, ni)) < ps - tl && in >= 0) {
                ni = in + 1;
            }
            ni = pi < ni && pi + 3 >= ni ? pi : ni;
            if (ni < (int) (ps * 0.85)) {
                return brx.length() > 1 ? nextPageBreak(ussdText, brx.substring(1), ni, ps, tl, ni) : def;
            }
            return ni <= 0 ? def : ni;
        }
        return def;
    }

    public String createCsvList(ArrayList list) {
        StringBuilder buffer = new StringBuilder();
        if (list != null) {
            list.stream().forEach((item)
                    -> {
                buffer.append(buffer.length() > 0 ? "," + item : item);
            });
        }
        return buffer.toString();
    }

    public String capitalize(String text, boolean convertAllXters) {
        if (text != null && text.length() > 0) {
            char p = '0';
            StringBuilder builder = new StringBuilder();
            for (char c : (convertAllXters ? text.toLowerCase() : text).toCharArray()) {
                builder.append(p = (Character.isLetter(p) ? c : Character.toUpperCase(c)));
            }
            return cleanSpaces(builder.toString());
        }
        return text;
    }

    public String decapitalize(String text) {
        if (text != null && text.length() > 0) {
            StringBuilder builder = new StringBuilder();
            for (String word : text.split("\\s")) {
                builder.append(word.substring(0, 1).toLowerCase()).append(word.substring(1)).append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    public String protectField(String fieldValue, int preSkip, int postSkip) {
        if (!isBlank(fieldValue)) {
            for (int i = preSkip; i < fieldValue.length() - postSkip; i++) {
                fieldValue = fieldValue.substring(0, i) + "X" + fieldValue.substring(i + 1);
            }
        }
        return fieldValue;
    }

    public String spaceWords(String text) {
        if (!isBlank(text)) {
            char p = ' ';
            StringBuilder builder = new StringBuilder();
            for (char c : text.toCharArray()) {
                builder.append((Character.isUpperCase(c) && !Character.isUpperCase(p)) || (Character.isDigit(c) && !Character.isDigit(p)) || (!Character.isDigit(c) && Character.isDigit(p)) ? (" " + c) : (builder.length() == 0 ? String.valueOf(c).toUpperCase() : c));
                p = c;
            }
            return builder.toString().trim();
        }
        return text;
    }

    public String formatXml(String xml) {
        try {
            if (!isBlank(xml)) {
                Writer out = new StringWriter();
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4");
                transformer.transform(new DOMSource(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(cleanText(xml).getBytes()))), new StreamResult(out));
                return out.toString().trim().replaceAll("    ", "\t");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return checkBlank(xml, "");
    }

    public String formatJson(String json) {
        try {
            if (!isBlank(json)) {
                return new JSONObject(json).toString(4).trim().replaceAll("    ", "\t");
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return checkBlank(json, "");
    }

    public void extractFields(Object object, LinkedHashMap<String, Object> fieldMap) {
        try {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                for (String key : (Set<String>) jsonObject.keySet()) {
                    fieldMap.put(capitalize(key, false), checkBlank(convertToString(jsonObject.get(key)), ""));
                }
            } else {
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(object.getClass()).getPropertyDescriptors()) {
                    Method readMethod = propertyDesc.getReadMethod();
                    if (readMethod != null && !Modifier.isNative(readMethod.getModifiers())) {
                        fieldMap.put(capitalize(propertyDesc.getName(), false), checkBlank(convertToString(readMethod.invoke(object)), ""));
                    }
                }
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    public boolean isJson(String text) {
        return !isBlank(text) && (text = text.trim()).startsWith("{") && text.endsWith("}");
    }

    public String indentLines(String text) {
        return indentLines(text, "\t");
    }

    public String indentLines(String text, String indent) {
        String line = "", buffer = "";
        try (BufferedReader bis = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes())))) {
            while (line != null) {
                buffer += indent + line + "\r\n";
                line = bis.readLine();
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }

        return indent + buffer.trim();
    }

    public String firstName(String name) {
        return name != null && name.trim().length() > 0 ? capitalize(name.trim().split("\\s")[0]) : name;
    }

    public String formatDate(SimpleDateFormat format, Date date) {
        return !isBlank(date) ? format.format(date) : null;
    }

    public void prepareScrollers(Component component) {
        if (component instanceof JScrollPane) {
            prepareScroller((JScrollPane) component);
        }
        if (component instanceof RootPaneContainer) {
            prepareScrollers(((RootPaneContainer) component).getContentPane());
        }
        if (component instanceof Container) {
            for (Component comp : ((Container) component).getComponents()) {
                prepareScrollers(comp);
            }
        }
    }

    public void prepareScroller(JScrollPane scroller) {
        JButton button = new JButton(new javax.swing.ImageIcon(getClass().getResource("/PHilae/ximg/corner.png")));
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.addActionListener((ActionEvent e)
                -> {
            JScrollBar scrollBar = scroller.getVerticalScrollBar();
            scrollBar.setValue(scrollBar.getMaximum());
        });
        scroller.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, button);
    }

    public boolean validateFields(JDialog dialog, JPanel panel, Component... exceptions) {
        boolean valid = true;
        List<Component> list = Arrays.asList(exceptions);
        for (Component component : panel.getComponents()) {
            if (component instanceof JComponent && component.isEnabled() && !list.contains(component)) {
                valid = (component instanceof JPanel && !(component instanceof JDateChooser)) ? validateFields(dialog, (JPanel) component, exceptions) : validateValue(dialog, (JComponent) component);
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    public boolean validateValue(JDialog dialog, JComponent component) {
        boolean invalid;
        if (invalid = (component instanceof JDateChooser ? isBlank(((JDateChooser) component).getDate())
                : (component instanceof JTextField ? isBlank(((JTextField) component).getText()) && ((JTextField) component).isEditable()
                        : (component instanceof JComboBox ? isBlank(((JComboBox) component).getSelectedItem()) : false)))) {
            JOptionPane.showMessageDialog(dialog, "Field Required [ " + component.getToolTipText() + " ]", "Missing Field", JOptionPane.ERROR_MESSAGE);
            component.requestFocus();
        }
        return !invalid;
    }

    public <T> T convertToType(String variable, Class<T> clazz) {
        return convertToType(variable, clazz, null);
    }

    public <T> T convertToType(String variable, Class<T> clazz, T defaultValue) {
        try {
            return (T) clazz.getConstructor(String.class).newInstance(variable.trim());
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public String cleanSpaces(String text) {
        return !isBlank(text) ? text.replaceAll("\\s+", " ").trim() : text;
    }

    public String cleanField(String text, boolean space) {
        if (!isBlank(text)) {
            String prev = "";
            StringBuilder buffer = new StringBuilder();
            for (String t : (space ? spaceWords(text) : text).split("\\s+")) {
                if (!prev.equalsIgnoreCase(t)) {
                    buffer.append(" ").append(t);
                    prev = t;
                }
            }
            return buffer.toString().trim();
        }
        return text;
    }

    public String replaceAll(String text, String placeHolder, String replacement) {
        return !isBlank(text) ? text.replaceAll(placeHolder.replace("{", "\\{").replace("}", "\\}"), cleanField(checkBlank(replacement, "<>"), false)) : text;
    }

    public String cleanDateText(String date, char separator) {
        return !isBlank(date) ? date.trim().replaceAll("[^\\d]", String.valueOf(separator)).replaceAll("[" + separator + "]+", String.valueOf(separator)) : date;
    }

    public String replaceXmlXters(String xml, boolean escape) {
        HashMap<String, String> xters = escape ? escapeXters : unescapeXters;
        for (String key : xters.keySet()) {
            xml = xml.replaceAll(key, xters.get(key));
        }
        return xml;
    }

    public byte[] convertBlobToBytes(java.sql.Blob image) {
        try {
            return image != null && image.length() > 0 ? image.getBytes(1, (int) image.length()) : new byte[0];
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new byte[0];
    }

    public <T> T readObject(InputStream is, Class<T> clazz) {
        try {
            return (T) new ObjectInputStream(is).readObject();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public byte[] convertToBytes(Object object) throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(object);
            os.flush();
            return baos.toByteArray();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return new byte[0];
    }

    public void expandAllNodes(JTree tree, Class clazz) {
        int selRow = 0;
        tree.updateUI();
        for (int i = 0; i < tree.getRowCount(); i++) {
            selRow = (selRow == 0 && clazz.isInstance(((DefaultMutableTreeNode) tree.getPathForRow(i).getLastPathComponent()).getUserObject())) ? i : selRow;
            tree.expandRow(i);
        }
        tree.setSelectionRow(selRow);
        tree.scrollPathToVisible(tree.getPathForRow(selRow));
        updateTreeUI(tree, true);
    }

    public void selectTreeNode(JTree tree, Object userObject) {
        if (!isBlank(userObject)) {
            DefaultMutableTreeNode currentNode = ((DefaultMutableTreeNode) tree.getModel().getRoot()).getFirstLeaf();
            while (currentNode != null) {
                if (Objects.equals(currentNode.getUserObject(), userObject) || Objects.equals(currentNode.getUserObject().toString().split("~")[0].toUpperCase().trim(), String.valueOf(userObject).split("~")[0].toUpperCase().trim())) {
                    if (!Objects.equals(currentNode.getUserObject(), getSelectedObject(tree, userObject.getClass()))) {
                        tree.scrollPathToVisible(new TreePath(currentNode.getPath()));
                        tree.setSelectionPath(new TreePath(currentNode.getPath()));
                        updateTreeUI(tree, false);
                    }
                    break;
                }
                DefaultMutableTreeNode nextNode = currentNode.isLeaf() ? currentNode.getNextSibling() : (DefaultMutableTreeNode) currentNode.getFirstChild();
                currentNode = (nextNode == null && currentNode.getParent() != null) ? ((DefaultMutableTreeNode) currentNode.getParent()).getNextSibling() : nextNode;
            }
        }
    }

    public <T> T getSelectedObject(JTree tree, Class<T> clazz) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode != null) {
            if (clazz.isInstance(selectedNode.getUserObject())) {
                return (T) selectedNode.getUserObject();
            }
        }
        return null;
    }

    public TreeMap<String, String> extractIsoSubFields(String extFieldText) {
        TreeMap<String, String> dataMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (!isBlank(extFieldText)) {
            extFieldText = extFieldText.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&quot;", "\"");
            while (extFieldText.length() > 0) {
                int lenWidth = Integer.parseInt(extFieldText.substring(0, 1));
                extFieldText = extFieldText.substring(1);
                int length = Integer.parseInt(extFieldText.substring(0, lenWidth));

                extFieldText = extFieldText.substring(lenWidth);
                String key = extFieldText.substring(0, length);
                extFieldText = extFieldText.substring(length);

                lenWidth = Integer.parseInt(extFieldText.substring(0, 1));
                extFieldText = extFieldText.substring(1);
                length = Integer.parseInt(extFieldText.substring(0, lenWidth));

                extFieldText = extFieldText.substring(lenWidth);
                String value = extFieldText.substring(0, length);
                extFieldText = extFieldText.substring(length);
                dataMap.put(key, value);
            }
        }
        return dataMap;
    }

    public String extractError(Exception ex) {
        try {
            List<String> errorCodes = null;
            if (ex instanceof com.neptunesoftwareplc.ci.account.service.CIException_Exception) {
                errorCodes = ((com.neptunesoftwareplc.ci.account.service.CIException_Exception) ex).getFaultInfo().getErrorCodes();
                List<com.neptunesoftwareplc.ci.account.service.CiErrorData> errorDataList = ((com.neptunesoftwareplc.ci.account.service.CIException_Exception) ex).getFaultInfo().getErrorList();
                return (!errorCodes.isEmpty() ? errorCodes.get(0) : (!errorDataList.isEmpty() ? errorDataList.get(0).getErrorCode() : AXResult.Failed.name()));

            } else if (ex instanceof com.neptunesoftwareplc.ci.transfer.service.CIException_Exception) {
                errorCodes = ((com.neptunesoftwareplc.ci.transfer.service.CIException_Exception) ex).getFaultInfo().getErrorCodes();
                List<com.neptunesoftwareplc.ci.transfer.service.CiErrorData> errorDataList = ((com.neptunesoftwareplc.ci.transfer.service.CIException_Exception) ex).getFaultInfo().getErrorList();
                return (!errorCodes.isEmpty() ? errorCodes.get(0) : (!errorDataList.isEmpty() ? errorDataList.get(0).getErrorCode() : AXResult.Failed.name()));
            }
        } catch (Exception e) {
            getLog().logEvent(e);
        }
        return AXResult.Failed.name();
    }

    public String extractErrorCode(List<String> errorCodes, List<XAPIErrorData> errorDataList) {
        try {
            if (errorCodes != null) {
                for (String errorCode : errorCodes) {
                    if (!isBlank(errorCode)) {
                        return errorCode;
                    }
                }
            }
            if (errorDataList != null) {
                for (XAPIErrorData errorData : errorDataList) {
                    if (!isBlank(errorData.getErrorCode())) {
                        return errorData.getErrorCode();
                    }
                }
            }
        } catch (Exception e) {
            getLog().logEvent(e);
        }
        return AXResult.Failed.name();
    }

//    public String extractError(Exception ex) {
//        try {
//            if (ex instanceof XAPIException) {
//                if (((XAPIException) ex).getErrorCodes() != null) {
//                    for (String errorCode : ((XAPIException) ex).getErrorCodes()) {
//                        if (!isBlank(errorCode)) {
//                            return errorCode;
//                        }
//                    }
//                }
//                if (((XAPIException) ex).getErrors() != null) {
//                    for (XAPIErrorData errorData : ((XAPIException) ex).getErrors()) {
//                        if (!isBlank(errorData.getErrorCode())) {
//                            return errorData.getErrorCode();
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            getLog().logEvent(e);
//        }
//        return AXResult.Failed.name();
//    }
    public AXSetting createSetting(Field field, String module) {
        AXSetting setting = new AXSetting();
        try {
            Class clazz = field.getType();
            Object value = field.get(null);

            if (!isBlank(value)) {
                if (clazz == ArrayList.class) {
                    value = createCsvList((ArrayList) value);
                } else if (clazz == CNAccount.class) {
                    value = ((CNAccount) value).getAccountNumber();
                } else if (clazz == CNBranch.class) {
                    value = ((CNBranch) value).getBuId();
                }
            }

            setting.setModule(module);
            setting.setCode(capitalize(field.getName(), false));
            setting.setDescription(spaceWords(field.getName()));

            setting.setValue(String.valueOf(value));
            setting.setSysDate(new Date());
            setting.setSysUser("SYSTEM");
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return setting;
    }

    public String extractXmlValue(String xml, String tag, boolean includeTag) {
        Matcher matcher = Pattern.compile("(((<)([/?\\w]+:)?)|(</?))(" + tag + ")((\\s+(.*?))?)(/?>)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL).matcher(xml = blankNull(xml));
        Integer start = matcher.find() ? includeTag ? matcher.start() : matcher.end() : 0, end = matcher.find() ? includeTag ? matcher.end() : matcher.start() : 0;
        return end > start ? xml.substring(start, end) : null;
    }

    public <T> T extractXmlValue(String xml, String tag, Class<T> clazz) {
        return !isBlank(xml) && !isBlank(tag) ? convertToType(extractXmlValue(xml, tag, false), clazz) : null;
    }

    public ArrayList<String> extractPlaceHolders(String text) {
        ArrayList<String> holdersList = new ArrayList<>();
        Matcher matcher = holderPattern.matcher(String.valueOf(text));
        while (matcher.find()) {
            holdersList.add(matcher.group(0));
        }
        return holdersList;
    }

    public void formatDecimalValue(JTextField field) {
        BigDecimal value = convertToType(field.getText().trim(), BigDecimal.class);
        if (!isBlank(value)) {
            field.setText(formatDecimal(value).toPlainString());
        }
    }

    public String lastText(String text, int count) {
        int length = text.length();
        if (length > count) {
            int startIndex = length - count;
            return text.substring(startIndex);
        }
        return text;
    }

    public Date parseDate(SimpleDateFormat format, String date) {
        try {
            return !isBlank(date) ? format.parse(date) : null;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public LocalDate parseLocalDate(DateTimeFormatter formatter, String date) {
        try {
            return !isBlank(date) ? LocalDate.parse(date, formatter) : null;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public void selectBoxValue(JComboBox box, String code) {
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).toString().split("~")[0].trim().equalsIgnoreCase(code)) {
                box.setSelectedIndex(i);
                return;
            }
        }
        box.setSelectedItem(code);
    }

    public boolean isNumber(String text) {
        try {
            return convertToType(text, Double.class) >= 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public String nextCode(TreeMap list, String prefix) {
        int i = 1;
        String code = prefix + String.format("%02d", i);
        while (list.containsKey(code)) {
            code = prefix + String.format("%02d", ++i);
        }
        return code;
    }

    public void updateTreeUI(JTree tree, boolean resetScrollers) {
        EventQueue.invokeLater(()
                -> {
            Container container = SwingUtilities.getAncestorOfClass(JScrollPane.class, tree);
            if (container instanceof JScrollPane) {
                JScrollPane scroller = (JScrollPane) container;
                scroller.getHorizontalScrollBar().setValue(0);
                if (resetScrollers) {
                    scroller.getVerticalScrollBar().setValue(0);
                }
                scroller.setBorder(null);
            }
            tree.updateUI();
        });
    }

    public boolean isSettings(Field field) {
        if (Map.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
            return Objects.equals(((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1], AXSetting.class);
        }
        return false;
    }

    public void pauseThread(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ie) {
            ie = null;
        }
    }

    public BigDecimal sumBalances(ArrayList<CNAccount> accounts) {
        BigDecimal balance = BigDecimal.ZERO;
        for (CNAccount account : accounts) {
            balance = balance.add(account.getBalance());
        }
        return balance;
    }

    public BigDecimal sumArrears(ArrayList<CNAccount> accounts) {
        BigDecimal arrears = BigDecimal.ZERO;
        for (CNAccount account : accounts) {
            arrears = arrears.add(account.getArrears());
        }
        return arrears;
    }

    public String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return null;
    }

    public String getHttpHeader(Map headerMap, String header) {
        List<String> headerField = (List<String>) headerMap.get(header);
        return (headerField != null && !headerField.isEmpty()) ? headerField.get(0) : null;
    }

    public <T> T setHttpHeader(Map headerMap, String header, T value) {
        headerMap.put(header, Collections.singletonList(value));
        return value;
    }

    public <T> ArrayList<T> sortArrayList(ArrayList<T> arrayList, boolean ascending) {
        Collections.sort(arrayList, ascending ? ((Comparator<T>) (T o1, T o2) -> ((Comparable) o1).compareTo(o2)) : ((Comparator<T>) (T o1, T o2) -> ((Comparable) o2).compareTo(o1)));
        return arrayList;
    }

    public <T> T[] sortArray(T[] array, boolean ascending) {
        Arrays.sort(array, ascending ? ((Comparator<T>) (T o1, T o2) -> ((Comparable) o1).compareTo(o2)) : ((Comparator<T>) (T o1, T o2) -> ((Comparable) o2).compareTo(o1)));
        return array;
    }

    public String formatEquinoxAccount(String acctNo) {
        acctNo = acctNo != null ? String.valueOf(acctNo.replaceAll("[^\\d]", "")) : String.valueOf(acctNo);
        return acctNo.trim().length() < 5 ? acctNo : acctNo.trim().substring(0, 3) + "-" + acctNo.trim().substring(3);
    }

    public String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public <T> T extractValue(JAXBElement<T> element) {
        return !isBlank(element) ? (T) element.getValue() : null;
    }

    public String prepareDuration(Long startTime) {
        return (System.currentTimeMillis() - startTime) + " Ms ~ ";
    }

    public String trimRight(String text) {
        return rTrimPattern.matcher(text).replaceAll("").replaceAll("\r\n", "");
    }

    public String trimLeft(String text) {
        return lTrimPattern.matcher(text).replaceAll("").replaceAll("\r\n", "");
    }

    public String formatIsoAmount(BigDecimal value) {
        return String.format("%012d", value.abs().setScale(2, RoundingMode.DOWN).multiply(AXConstant.HUNDRED).longValue());
    }

    public BigDecimal formatDecimal(BigDecimal value) {
        return !isBlank(value) ? value.setScale(2, RoundingMode.HALF_UP) : value;
    }

    public boolean validatePercentage(BigDecimal perc) {
        return !isBlank(perc) ? (BigDecimal.ZERO.compareTo(perc) <= 0 || AXConstant.HUNDRED.compareTo(perc) >= 0) : false;
    }

    public boolean validateAmount(BigDecimal amount, boolean allowZero) {
        return !isBlank(amount) && BigDecimal.ZERO.compareTo(amount) <= (allowZero ? 0 : -1);
    }

    public boolean validateLimit(BigDecimal amount, BigDecimal limit) {
        return isBlank(amount) || isBlank(limit) || BigDecimal.ZERO.compareTo(limit) == 0 || limit.compareTo(amount) >= 0;
    }

    public BigDecimal compareAmount(BigDecimal amountOne, BigDecimal amountTwo, boolean higher) {
        return higher ? (isBlank(amountOne) ? amountTwo : (isBlank(amountTwo) ? amountOne : amountOne.compareTo(amountTwo) > 0 ? amountOne : amountTwo)) : (isBlank(amountOne) ? amountOne : (isBlank(amountTwo) ? amountTwo : amountOne.compareTo(amountTwo) > 0 ? amountTwo : amountOne));
    }

    public BigDecimal checkLower(BigDecimal amountOne, BigDecimal amountTwo, BigDecimal failValue) {
        return isBlank(amountOne) ? amountOne : (isBlank(amountTwo) ? failValue : amountOne.compareTo(amountTwo) <= 0 ? amountOne : failValue);
    }

    public BigDecimal checkHigher(BigDecimal amountOne, BigDecimal amountTwo, BigDecimal failValue) {
        return isBlank(amountOne) ? failValue : (isBlank(amountTwo) ? amountOne : amountOne.compareTo(amountTwo) >= 0 ? amountOne : failValue);
    }

    public String getOrdinalFor(int day) {
        int modTen = day % 10;
        return day + (((day % 100) - (modTen) != 10) && modTen >= 1 && modTen <= 3 ? (modTen == 2 ? "nd" : (modTen == 3 ? "rd" : "st")) : "th");
    }

    public <T> T getBoxValue(JComboBox box, Class<T> clazz) {
        return convertToType(getBoxValue(box), clazz);
    }

    public String getBoxValue(JComboBox box) {
        return !isBlank(box.getSelectedItem()) ? box.getSelectedItem().toString().split("~")[0].trim() : "";
    }

    public boolean matchName(String name) {
        return !isBlank(name) && namePattern.matcher(name).matches();
    }

    public boolean validateEmail(String emailAddress) {
        return !isBlank(emailAddress) && emailPattern.matcher(emailAddress).matches();
    }

    public boolean isYes(String setting) {
        return checkBlank(setting, "No").trim().toUpperCase().startsWith("Y");
    }

    public BigDecimal checkZeroDivisor(BigDecimal decimal) {
        return BigDecimal.ZERO.compareTo(decimal) >= 0 ? BigDecimal.ONE : decimal;
    }

    public boolean isPureAscii(String message) {
        return StandardCharsets.US_ASCII.newEncoder().canEncode(message);
    }

    public String truncateText(String text, int length) {
        return !isBlank(text) ? (text.length() > length ? text.substring(0, length) : text) : text;
    }

    public LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Long monthsBetween(LocalDate startDate, LocalDate endDate) {
        return Math.abs(Period.between(startDate, endDate).toTotalMonths());
    }

    public Long yearsToDate(LocalDate startDate) {
        return Period.between(startDate, getSystemDate()).toTotalMonths() / 12;
    }

    public String getDayOfWeek(LocalDate localDate) {
        return capitalize(localDate.getDayOfWeek().toString());
    }

    public LocalDate nextDayOfWeek(DayOfWeek dayOfWeek) {
        return toLocalDate(getBox().getClient().getSystemDate()).with(nextOrSame(dayOfWeek));
    }

    public LocalDate nextDayOfMonth(int DAY_OF_MONTH) {
        return toLocalDate(getBox().getClient().getSystemDate()).plusMonths(getSystemDate().getDayOfMonth() > DAY_OF_MONTH ? 1 : 0).withDayOfMonth(DAY_OF_MONTH);
    }

    public LocalDate systemDatePlusDays(int days) {
        return toLocalDate(getBox().getClient().getSystemDate()).plusDays(days);
    }

    public LocalDate systemDatePlusWeeks(int weeks) {
        return toLocalDate(getBox().getClient().getSystemDate()).plusWeeks(weeks);
    }

    public LocalDate systemDatePlusMonths(int months) {
        return toLocalDate(getBox().getClient().getSystemDate()).plusMonths(months);
    }

    public LocalDate systemDatePlusYears(int years) {
        return toLocalDate(getBox().getClient().getSystemDate()).plusYears(years);
    }

    public LocalDate systemDateMinusDays(int days) {
        return toLocalDate(getBox().getClient().getSystemDate()).minusDays(days);
    }

    public LocalDate systemDateMinusWeeks(int weeks) {
        return toLocalDate(getBox().getClient().getSystemDate()).minusWeeks(weeks);
    }

    public LocalDate systemDateMinusMonths(int months) {
        return toLocalDate(getBox().getClient().getSystemDate()).minusMonths(months);
    }

    public LocalDate systemDateMinusYears(int years) {
        return toLocalDate(getBox().getClient().getSystemDate()).minusYears(years);
    }

    public LocalDate processingDatePlusYears(int years) {
        return toLocalDate(getBox().getClient().getProcessingDate()).plusYears(years);
    }

    public String convertToString(Object object) {
        return convertToString(new ArrayList<>(), object, null).trim();
    }

    public String formatAmount(BigDecimal amount) {
        return !isBlank(amount) ? decimalFormat.format(amount) : null;
    }

    public String escapeXmlXters(String xmlText) {
        return xmlText.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("\'", "&apos;").trim();
    }

    public String cleanXmlXters(String xmlText) {
        return xmlText.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&apos;", "\'").trim();
    }

    public String removeNameSpaces(String xml) {
        return xml != null ? xml.replaceAll("\\s*xmlns.*?=\".*?\"", "") : xml;
    }

    public String convertToXml(Object object, String rootElement, boolean formatted) {
        return object != null ? convertToXml(new JAXBElement(new QName(rootElement), object.getClass(), object), formatted) : null;
    }

    public String unwrapOptionalText(String text, boolean include) {
        return include ? text.replaceAll("[\\[\\]]", "") : text.replaceAll("\\[.*?\\]", "").replaceAll("\\s+", " ");
    }

    public <T> T convertXmlToObject(String xml, Class<T> clazz) {
        return convertXmlToObject(xml, null, clazz);
    }

    public <T> T cloneObject(Object source) {
        return source != null ? cloneObject(source, (Class<T>) source.getClass()) : null;
    }

    public String cleanUssdText(String ussdText) {
        return ussdText.replaceAll("\n", "~").replaceAll("~\\s*~", "~").replaceAll("\\s+", " ").replaceAll("~", "\n").trim().replaceAll("\n", "~");
    }

    public CNAccount verifyAccount(CNAccount account, CNAccount defaultAccount) {
        return isBlank(account) || isBlank(account.getAccountNumber()) ? defaultAccount : account;
    }

    public boolean isLedger(String accountNumber) {
        return !isBlank(accountNumber) && accountNumber.split("-").length >= 2;
    }

    public boolean isBlank(Object object) {
        return object == null || "".equals(String.valueOf(object).trim()) || "null".equals(String.valueOf(object).trim()) || String.valueOf(object).trim().toLowerCase().contains("---select");
    }

    public LocalDate getSystemDate() {
        return toLocalDate(getBox().getClient().getSystemDate());
    }

    public <T> T checkBlank(T value, T nillValue) {
        return isBlank(value) ? nillValue : value;
    }

    public String checkLength(String value, Integer maxLen) {
        return String.valueOf(value).length() > maxLen ? value.substring(value.length() - maxLen) : value;
    }

    public String blankNull(String value) {
        return checkBlank(value, "");
    }

    public String yesNo(boolean isYes) {
        return isYes ? "Yes" : "No";
    }

    public String capitalize(String text) {
        return capitalize(text, true);
    }

    public int randomInteger() {
        return (int) (Math.random() * 99627);
    }

    public String padText(String text, int length, boolean right) {
        return String.format("%" + (right ? "-" : "") + length + "s", text);
    }

    public String padNumber(Integer number, int length) {
        return String.format("%0" + length + "d", number);
    }

    public String nextCode(TreeMap list) {
        return nextCode(list, "");
    }

    public APLog getLog() {
        return getBox().getLog();
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

    private DBPClient getClient() {
        return getBox().getClient();
    }
}
