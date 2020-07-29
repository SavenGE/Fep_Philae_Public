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
import PHilae.acx.AXCaller;
import PHilae.acx.AXClient;
import PHilae.acx.AXConstant;
import PHilae.acx.AXCypher;
import PHilae.enu.AXResult;
import PHilae.acx.AXWorker;
import PHilae.acx.TRItem;
import PHilae.enu.TXType;
import PHilae.enu.ALHeader;
import PHilae.model.AXRequest;
import PHilae.model.CNAccount;
import PHilae.model.CNCustomer;
import PHilae.model.MXAlert;
import PHilae.model.MXMessage;
import PHilae.model.TCSplit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Pecherk
 */
public class ALWorker extends Thread {

    private long time = 0L;
    private int sentItems = 0;
    private final MXAlert alert;

    private static final String tag = "alert";
    private ATBox box = new ATBox(APMain.smsLog);
    private final ALSender sender = new ALSender();

    public ALWorker(MXAlert mXAlert) {
        this.alert = mXAlert;
    }

    @Override
    public void run() {
        try {
            prepare();
            if (executeAlert()) {
                pushAlert();
            }
            windup();
        } catch (Throwable ex) {
            APMain.smsLog.logEvent(ex);
        }
    }

    private boolean executeAlert() {
        boolean empty = true;
        try {
            if (Objects.equals(getAlert().getFilterBy(), "MN")) {
                for (String list : getAlert().getFilters()) {
                    try {
                        for (String msisdn : list.split(",")) {
                            empty = false;
                            setLog(new AXCaller(tag));
                            MXMessage mXRequest = new MXMessage();
                            mXRequest.setRecId(System.currentTimeMillis());

                            mXRequest.setCode(getAlert().getCode());
                            mXRequest.setType(getAlert().getType());
                            mXRequest.setMsisdn(msisdn);

                            AXRequest axRequest = new AXRequest();
                            axRequest.getCharge().setWaive(true);

                            if (checkExit()) {
                                return false;
                            }
                            if (processRequest(axRequest, mXRequest)) {
                                raiseSentItems();
                            }
                        }
                    } catch (Exception ex) {
                        APMain.smsLog.logEvent(ex);
                    }
                }
            } else {
                getClient().prepareAlerts(getAlert());
                ArrayList<MXMessage> pendingAlerts = getClient().queryPendingAlerts(getAlert());
                for (MXMessage message : pendingAlerts) {
                    try {
                        empty = false;
                        setLog(new AXCaller(tag));
                        BigDecimal limit = BigDecimal.ZERO;

                        CNAccount chargeAccount = getClient().queryDepositAccount(message.getChargeAcct());
                        AXRequest aXRequest = new AXRequest();
                        aXRequest.setRole(ALController.getRole());

                        aXRequest.setReference(String.valueOf(message.getRecId()));
                        aXRequest.setSchemeId(ALController.schemeId);
                        aXRequest.setChannel(ALController.getChannel());

                        aXRequest.setModule(ALController.module);
                        aXRequest.setAccount(chargeAccount);
                        aXRequest.getCharge().setScheme(ALController.chargeScheme);

                        aXRequest.setType(TXType.Alert);
                        aXRequest.setAccessCode(message.getMsisdn());
                        aXRequest.setCurrency(aXRequest.getAccount().getCurrency());

                        aXRequest.setBranch(aXRequest.getAccount().getBranch());
                        aXRequest.setAlertCode(getAlert().getCode());
                        aXRequest.setCustomer(getClient().queryCustomer(aXRequest.getAccount().getCustId()));

                        aXRequest.getCharge().setCode(getAlert().getChargeCode());
                        getWorker().setCharge(aXRequest, new HashMap<>());

                        getLog().setAlert(message.getCode());
                        getLog().setTxnId(aXRequest.getReference());
                        getLog().setNarration(message.getDescription());

                        if (getAlert().isTxn()) {
                            limit = getWorker().convertToType(
                                    (getClient().queryCustomField("CR".equals(getAlert().getType())
                                            || "DV".equals(getAlert().getType())
                                            ? ALController.creditLimitFieldId
                                            : ALController.debitLimitFieldId,
                                            getClient().queryDepositAccount(message.getAcctNo()).getAcctId())
                                            .getFieldValue()), BigDecimal.class, ALController.minimumAlertAmount);
                            limit = ALController.minimumAlertAmount.compareTo(limit) >= 0 ? ALController.minimumAlertAmount : limit;
                        }

                        if (limit.compareTo(message.getTxnAmt()) > 0
                                || message.getChargeAmount().compareTo(message.getTxnAmt()) >= 0) {
                            message.setStatus("L");
                        } else {
                            ArrayList<CNCustomer> members = new ArrayList<>();
                            if (Objects.equals(chargeAccount.getCustCat(), "GRP")) {
                                members = getClient().queryGroupMembers(message.getCustId());
                                aXRequest.getCharge().setChargeAmount(aXRequest.getCharge().getChargeAmount().multiply(new BigDecimal(members.size())));
                                for (TCSplit split : aXRequest.getCharge().getSplitList()) {
                                    split.setAmount(split.getAmount().multiply(new BigDecimal(members.size())));
                                }
                            }
                            getLog().setRequest(aXRequest);
                            if (Objects.equals(chargeAccount.getCustCat(), "GRP")) {
                                int sentCount = 0;
                                message.setMsisdn("GROUP");
                                for (CNCustomer member : members) {
                                    setLog(new AXCaller(tag));
                                    getLog().setRequest(aXRequest);
                                    MXMessage mxRequest = getWorker().cloneObject(message, MXMessage.class);

                                    mxRequest.setCustId(member.getCustId());
                                    mxRequest.setCustName(member.getCustName());
                                    mxRequest.setMsisdn(member.getMobileNumber());
                                    mxRequest.setChargeId(message.getChargeId());

                                    if (checkExit()) {
                                        return false;
                                    }
                                    if (processRequest(aXRequest, mxRequest)) {
                                        message.setChargeId(mxRequest.getChargeId());
                                        sentCount++;
                                    }
                                }

                                members.clear();

                                if (sentCount > 0) {
                                    raiseSentItems();
                                }
                                message.setStatus(sentCount > 0 ? "S" : "F");
                            } else {
                                if (checkExit()) {
                                    return false;
                                }
                                if (processRequest(aXRequest, message)) {
                                    raiseSentItems();
                                }
                            }
                        }
                    } catch (Exception ex) {
                        APMain.smsLog.logEvent(ex);
                    }
                    getClient().updateAlert(message);
                }

                pendingAlerts.clear();
            }
        } catch (Exception ex) {
            APMain.smsLog.logEvent(ex);
        }
        return empty || sentItems > 0;
    }

    public boolean processRequest(AXRequest aXRequest, MXMessage message) {
        try {
            message.setDescription(getAlert().getDescription());
            message.setMsisdn(getWorker().formatMsisdn(message.getMsisdn(), false));
            if (ALController.isWhiteListed(message.getMsisdn())) {
                String language = APController.defaultLanguage;
                try {
                    language = getWorker().checkBlank(
                            APController.getLanguages()
                                    .get(Long.parseLong(getClient().queryCustomField(APController.languageFieldId,
                                            message.getCustId()).getFieldValue())).getItemCode(),
                            APController.defaultLanguage);
                } catch (Exception ex) {
                    ex = null;
                }

                if (getAlert().isLoan()) {
                    message.setlNDetail(getClient().queryLoanDetail(getClient().queryLoanAccount(message.getAcctNo())));
                }

                String template = getAlert().getTemplates().get(language);
                if (!getWorker().isBlank(template)) {
                    boolean billable = !aXRequest.getCharge().isWaive() 
                            && (message.getChargeId() == 0 
                            || getWorker().isBlank(message.getChargeId()));
                    if (!billable || getXapi().processCharge(aXRequest, getAlert().isForce(), 1)) {
                        if (billable) {
                            message.setChargeId(getXapi().getResponse().getChargeId());
                        }

                        message.setMessage(replaceMasks(message, template));
                        AXResult result = getSender().sendMessage(message.getMsisdn(), message.getMessage(), getLog());

                        if (!retry(result)) {
                            message.setStatus(success(result) ? "S" : (result == AXResult.Duplicate ? "D" : "F"));
                        }

                        if (success(result) && signal()) {
                            APMain.apFrame.getSmsMeter().showSignal(message.getType(), true);
                            APMain.apFrame.getSmsMeter().showSignal(message.getType(), false);
                        }

                        if (billable && !success(result) && !retry(result)) {
                            aXRequest.setReversal(true);
                            getXapi().processCharge(aXRequest, true, 1);
                            aXRequest.setReversal(false);
                        }

                        if ("PC".equals(getAlert().getType())) {
                            message.setMessage(template.replaceAll("\\{SECCODE\\}", "XXXX"));
                            message.setMessage(template.replaceAll("\\{PIN\\}", "XXXX"));
                            message.setMessage(message.getMessage());
                        }
                    } else if (billable && AXResult.Insufficient_Funds == getXapi().getResponse().getResult()) {
                        message.setStatus("I");
                    } else {
                        message.setStatus("F");
                    }
                } else {
                    message.setStatus("T");
                }
            } else {
                message.setStatus("R");
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }
        try {
            switch (message.getStatus()) {
                case "F":
                    message.setResult("Failed");
                    break;
                case "I":
                    message.setResult("Insufficient Funds");
                    break;
                case "L":
                    message.setResult("Below Limit");
                    break;
                case "R":
                    message.setResult("Rejected");
                    break;
                case "S":
                    message.setResult("Sent");
                    break;
                case "T":
                    message.setResult("Invalid Template");
                    break;
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        } finally {
            writeToLog(message);
        }
        return Objects.equals(message.getStatus(), "S");
    }

    public void pushAlert() {
        LocalDate nextDate = getWorker().getSystemDate();
        getAlert().setPreviousDate(getClient().getSystemDate());
        if (getAlert().isLoaded()) {
            getAlert().setStatus("A");
        }
        if (getAlert().isBroadcast() && getAlert().isRealTime()) {
            getAlert().setFrequency("Once");
        }

        switch (getAlert().getFrequency()) {
            case "Once":
                getAlert().setStatus("C");
                break;
            case "Real-Time":
                getAlert().setNextDate(getClient().getSystemDate());
                break;
            case "Daily":
                getAlert().setNextDate(getWorker().toDate(nextDate.plusDays(1L)));
                break;
            case "Weekly":
                getAlert().setNextDate(getWorker().toDate(nextDate.plusWeeks(1L)));
                break;
            case "Monthly":
                getAlert().setNextDate(getWorker().toDate(nextDate.plusMonths(1L)));
                break;
            case "Annually":
                getAlert().setNextDate(getWorker().toDate(nextDate.plusYears(1L)));
                break;
            default:
                break;
        }
        getClient().pushAlert(getAlert());
    }

    public String replaceMasks(MXMessage message, String text) {
        try {
            if (!getWorker().isBlank(text)) {
                ArrayList<String> holdersList = getWorker().extractPlaceHolders(text);
                for (String placeHolder : holdersList) {
                    String replacement = "<>";
                    switch (placeHolder.toUpperCase()) {
                        case "{ACCOUNT}":
                            replacement = getWorker().protectField(getWorker().checkBlank(message.getAcctNo(), "<>"), 6, 2);
                            break;
                        case "{CONTRA}":
                            replacement = getWorker().protectField(getWorker().checkBlank(message.getContra(), "<>"), 6, 2);
                            break;
                        case "{NAME}":
                            replacement = getWorker().firstName(getWorker().checkBlank(message.getCustName(), "<>"));
                            break;
                        case "{BENEFICIARY}":
                            replacement = getWorker().checkBlank(getClient().queryAnyAccount(message.getContra()).getAccountName(), "<>");
                            break;
                        case "{AMOUNT}":
                            replacement = getWorker().formatAmount(message.getTxnAmt());
                            break;
                        case "{CHARGE}":
                            replacement = getWorker().formatAmount(message.getTxnChg());
                            break;
                        case "{DESCRIPTION}":
                            replacement = getWorker().capitalize(getWorker().checkBlank(message.getTxnDesc(), "<>"));
                            break;
                        case "{CODE}":
                            replacement = getWorker().checkBlank(message.getAccessCode(), "<>");
                            break;
                        case "{PIN}":
                            replacement = getWorker().checkBlank(getCypher().decrypt(message.getPassword()), "<>");
                            break;
                        case "{BALANCE}":
                            replacement = getWorker().formatAmount(message.getBalance());
                            break;
                        case "{DATE}":
                            replacement = getWorker().checkBlank(getWorker().formatDate(AXConstant.displayDateFormat, message.getTxnDate()), "<>");
                            break;
                        case "{CURRENCY}":
                            replacement = getWorker().checkBlank(message.getCurrency(), "<>");
                            break;
                        case "{ORIGINATOR}":
                            replacement = getWorker().firstName(getWorker().checkBlank("Self".equalsIgnoreCase(String.valueOf(message.getOriginator()).trim()) ? "you" : message.getOriginator(), "<>"));
                            break;
                        case "{DAYS}":
                            replacement = getWorker().checkBlank(String.valueOf(new BigDecimal(Math.floor(Math.abs((getClient().getSystemDate().getTime() - message.getTxnDate().getTime()) / 86400000))).longValue()), "<>");
                            break;
                    }
                    text = getWorker().replaceAll(text, placeHolder, replacement);
                }
                holdersList.clear();
                return text.replaceAll("~<>", "").replaceAll(" ~ <>", "").trim();
            }
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        return text;
    }

    private void setTree(MXMessage message) {
        try {
            TRItem treeItem = new TRItem();
            treeItem.setCode(message.getType());
            treeItem.setApproved(Objects.equals(message.getStatus(), "S"));

            treeItem.setText(message.getRecId() + "~" + message.getMsisdn());
            getWorker().extractFields(message, treeItem.getRequest());
            getWorker().extractFields(message, treeItem.getResponse());

            treeItem.getDetail().put(ALHeader.AlertId, message.getRecId());
            treeItem.getDetail().put(ALHeader.CreateDt, message.getCreateDt());
            treeItem.getDetail().put(ALHeader.AlertCode, message.getCode());

            treeItem.getDetail().put(ALHeader.AlertType, message.getType());
            treeItem.getDetail().put(ALHeader.Msisdn, message.getMsisdn());
            treeItem.getDetail().put(ALHeader.Description, message.getDescription());

            treeItem.getDetail().put(ALHeader.TxnId, message.getTxnId());
            treeItem.getDetail().put(ALHeader.CustId, message.getCustId());
            treeItem.getDetail().put(ALHeader.CustName, message.getCustName());

            treeItem.getDetail().put(ALHeader.Account, message.getAcctNo());
            treeItem.getDetail().put(ALHeader.Contra, message.getContra());
            treeItem.getDetail().put(ALHeader.ChargeAcct, message.getChargeAcct());

            treeItem.getDetail().put(ALHeader.TxnDate, message.getTxnDate());
            treeItem.getDetail().put(ALHeader.Currency, message.getCurrency());
            treeItem.getDetail().put(ALHeader.Amount, message.getTxnAmt());

            treeItem.getDetail().put(ALHeader.Charge, message.getTxnChg());
            treeItem.getDetail().put(ALHeader.ChgId, message.getChargeId());
            treeItem.getDetail().put(ALHeader.SchemeId, message.getSchemeId());

            treeItem.getDetail().put(ALHeader.AccessCode, message.getAccessCode());
            treeItem.getDetail().put(ALHeader.Balance, message.getBalance());
            treeItem.getDetail().put(ALHeader.Message, message.getMessage());

            treeItem.getDetail().put(ALHeader.Result, message.getResult());
            treeItem.getDetail().put(ALHeader.RecSt, message.getStatus());
            APMain.apFrame.insertTreeItem(treeItem, ALController.module);
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
    }

    private void writeToLog(MXMessage message) {
        try {
            if (APController.isDebugMode() || !(Objects.equals(message.getStatus(), "L") || Objects.equals(message.getStatus(), "R"))) {
                setTree(message);
                if (!Objects.equals(message.getStatus(), "P")) {
                    getClient().saveMessage(message);
                }
                getLog().setMessage(message);

                getLog().setResult(message.getStatus() + " ~ " + message.getResult());
                getLog().setDuration((System.currentTimeMillis() - time) + " Ms");
                APMain.smsLog.logEvent(getLog());
            }
        } catch (Exception ex) {
            APMain.smsLog.logEvent(ex);
        }
    }

    private boolean checkExit() {
        boolean exit;
        if (!(exit = (time != ALProcessor.wip.getOrDefault(getAlert().getCode(), 0L)))) {
            ALProcessor.wip.put(getAlert().getCode(), time = System.currentTimeMillis());
        }
        return exit;
    }

    private boolean signal() {
        return !getAlert().isBroadcast() || sentItems % 10 == 1;
    }

    private boolean success(AXResult result) {
        return Objects.equals(AXResult.Success, result);
    }

    private boolean retry(AXResult result) {
        return Objects.equals(AXResult.Retry, result);
    }

    private void prepare() {
        ALProcessor.wip.put(getAlert().getCode(), time = 0L);
    }

    private void windup() {
        ALProcessor.wip.remove(getAlert().getCode());
    }

    private void raiseSentItems() {
        sentItems = sentItems > 32500 ? 1 : sentItems + 1;
    }

    /**
     * @return the tDClient
     */
    public DBPClient getClient() {
        return getBox().getClient();
    }

    public AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the mXClient
     */
    public AXClient getXapi() {
        return getBox().getXapi();
    }

    private APLog getLog() {
        return getBox().getLog();
    }

    private void setLog(AXCaller caller) {
        getBox().setLog(caller);
    }

    /**
     * @return the sender
     */
    public ALSender getSender() {
        return sender;
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

    /**
     * @return the alert
     */
    public MXAlert getAlert() {
        return alert;
    }
}
