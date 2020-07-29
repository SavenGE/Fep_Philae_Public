package PHilae.vma;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.acx.APLog;
import PHilae.acx.ATBox;
import PHilae.acx.AXCaller;
import PHilae.acx.AXClient;
import PHilae.acx.AXConstant;
import PHilae.enu.AXResult;
import PHilae.acx.AXWorker;
import PHilae.acx.TRItem;
import PHilae.enu.TVField;
import PHilae.enu.ALHeader;
import PHilae.enu.ARType;
import PHilae.enu.LNType;
import PHilae.model.AXRequest;
import PHilae.model.AXResponse;
import PHilae.model.AXTxn;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import PHilae.model.AXActivity;
import PHilae.model.CNBranch;
import java.math.BigDecimal;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import PHilae.model.CNAccount;
import PHilae.model.CNCustomer;
import PHilae.model.CNUser;
import java.math.RoundingMode;
import static PHilae.enu.TVField.Block_Reversal;
import static PHilae.enu.TVField.Check_Account;
import static PHilae.enu.TVField.Check_Amount;
import static PHilae.enu.TVField.Check_Contra;
import static PHilae.enu.TVField.Check_Exists;
import static PHilae.enu.TVField.Check_Duplicate;
import static PHilae.enu.TVField.Check_Email;
import static PHilae.enu.TVField.Check_Forbidden;
import static PHilae.enu.TVField.Check_Linked;
import static PHilae.enu.TVField.Check_Match;
import static PHilae.enu.TVField.Check_Product;
import static PHilae.enu.TVField.Check_User;
import static PHilae.enu.TVField.Score_Arrears;
import static PHilae.enu.TVField.Score_Balance;
import static PHilae.enu.TVField.Score_Deposit;
import static PHilae.enu.TVField.Score_Exists;
import static PHilae.enu.TVField.Score_Minimum;
import PHilae.enu.TXGroup;
import PHilae.enu.TXType;
import PHilae.ipn.CBRequest;
import PHilae.model.ACProduct;
import PHilae.model.RFFriend;
import java.io.Serializable;
import javax.xml.bind.DatatypeConverter;
import javax.xml.ws.Holder;
import schemas.dynamics.microsoft.codeunit.mobilebanking.MobileBanking;
import static PHilae.enu.TVField.Score_Limit;
import PHilae.sms.ALSender;
import static PHilae.enu.TVField.Check_Default;

public final class VXProcessor implements Serializable {

    private String callId;
    private boolean locked = false;
    private AXTxn txn = new AXTxn();

    private ALSender sender = new ALSender();
    private AXRequest request = new AXRequest();
    private AXResponse response = new AXResponse();
    private Long startTime = System.currentTimeMillis();
    private ATBox box = new ATBox(new AXCaller());

    private final Holder<Object> holder = new Holder<>();
    private final AXActivity activity = new AXActivity();
    private TRItem treeItem = new TRItem();

    public VXProcessor(VXCaller caller) {
        setCallId(getTxn().setCaller(caller).getCallId());
    }

    public void execute(Holder<String> codeField, Holder<String> messageField,
            Holder<String> errorField) {
        prepare();
        if (setLock(true)) {
            processTxn();
        }

        try {
            codeField.value = getResponse().getResult().getCode();
            errorField.value = replaceMasks(VXController.getMessage(getResponse().getResult()));
            messageField.value = getWorker().isBlank(messageField.value) ? errorField.value : messageField.value;
        } catch (Exception ex) {
            getLog().logEvent(ex);
        }
        new Thread(this::windup).start();
        setLock(false);
    }

    private boolean prepare() {
        try {
            getRequest().setType(getCaller().getTxn());
            getRequest().setClient(getCaller().getClient());
            getRequest().setReference(getWorker().checkBlank(getCaller().getReqId(), getCallId()));

            getRequest().setModule(VXController.module);
            getRequest().setRole(VXController.getRole());
            getRequest().setChannel(VXController.getChannel());

            getRequest().setAccessCode(getCaller().getMsisdn());
            getRequest().setCurrency(APController.getCurrency());
            getRequest().getCharge().setScheme(VXController.chargeScheme);

            getRequest().setBranch(VXController.getChannel().getBranch());
            getRequest().setInverted(getRequest().getType().getGroup() == TXGroup.Deposit);
            /*if transaction is Mpesa Verify or Mpesa Deposit, don't check if customer/user is registerd*/
            if ((getCaller().getTxn() == TXType.MpesaVerify) || (getCaller().getTxn() == TXType.MpesaDeposit)) {
                getRequest().setUser(getClient().queryUserByAccount(getCaller().getAccountNumber()));
            } else {
                getRequest().setUser(getClient().queryChannelUser(VXController.schemeId, getCaller().getMsisdn()));
            }

            getRequest().setCustomer(getClient().queryCustomer(getUser().getCustId()));

            getActivity().setModule(VXController.module);
            getActivity().setIdentity(getRequest().getAccessCode());
            getActivity().setName(getUser().getAccessName());

            getActivity().setTxnId(getRequest().getReference());
            getActivity().setActivity(getRequest().getType().name());
            APMain.apFrame.getVmaMeter().showSignal(getActivity(), getRequest().getType().getCode(), true);
            return true;
        } catch (Exception ex) {
            processError(ex);
        }
        return false;
    }

    private void processTxn() {
        try {
            getClass().getMethod(getCaller().getMethodName(), getCaller().getParamTypes()).invoke(this, getCaller().getParamValues());
        } catch (Exception ex) {
            processError(ex);
        }
    }

//    public void spotcash(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        switch (getRequest().getType()) {
//            case NWDBalance:
//            case SavingBalance:
//            case CapitalBalance:
//                processDepositBalance(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case LoanBalance:
//                processLoanBalance(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case MiniStatement:
//                processMiniStatement(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case Deposit:
//                processDeposit(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case Withdrawal:
//                processWithdrawal(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case Transfer:
//                processAccountTransfer(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case Statement:
//                processGetAccountStatement(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case PesaLink:
//                processOutwardTransfer(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            case Airtime:
//            case VerveeCash:
//            case GoTV:
//            case KenyaPower:
//            case KisumuWater:
//            case NairobiWater:
//            case NHIF:
//            case ZUKU:
//            case MultiChoice:
//            case StarTimes:
//            case Safaricom:
//            case OtherPayment:
//                processPayment(requestId, phoneNo, transactionType, amount, trnxCharges, accountNumber, crAccount, status, fKey, balance, message, response, responseMessage, customerType, description, startDate, endDate, startTime, endTime, emailaddress);
//                break;
//            default:
//                processUnsupported(response, responseMessage);
//                break;
//        }
//        if (approved()) {
//            balance.value = getWorker().formatAmount(getResponse().getBalance());
//        }
//    }
//
//    public void memberRegistration(Holder<String> iDNo, Holder<String> firstName, Holder<String> middleName, Holder<String> branchCode, Holder<String> surname, Holder<String> pinNo, Holder<String> address, Holder<String> gender, Holder<String> occupation, Holder<String> phoneNo, Holder<String> email, Holder<String> passportPhoto, Holder<String> frontID, Holder<String> backID, Holder<String> signature, Holder<String> dateOfBirth, Holder<String> maritalStatus, Holder<String> applicationNo, Holder<String> hudumaNumber, Holder<String> residence, Holder<String> alternativeContact, Holder<String> refererPhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        check(getWorker().isBlank(iDNo.value), AXResult.Invalid_Identity);
//        check(getWorker().isBlank(phoneNo.value), AXResult.Invalid_Mobile);
//        check(getWorker().isBlank(firstName.value), AXResult.Missing_Name);
//
//        check(getWorker().isBlank(surname.value), AXResult.Missing_Name);
//        check(getWorker().isBlank(dateOfBirth.value), AXResult.Invalid_Date);
//        check(getWorker().isBlank(getClient().queryBranch(branchCode.value).getBuId()), AXResult.Invalid_Branch);
//
//        getRequest().setItem(getWorker().extractSoapObject(VXController.calls.get(getCallId() + "I"), MobileBanking.class.getPackage().getName()));
//        setResponse(validateFields(Block_Reversal) ? getBox().getXapi().createCustomer(getRequest()) : getResponse());
//        if (approved()) {
//            applicationNo.value = getResponse().getCustomerNumber();
//        }
//    }
//
//    public void getMemberAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        JSONArray accounts = new JSONArray();
//        if (validateFields(Check_User)) {
//            for (CNAccount account : getClient().queryCustomerAccounts(getUser().getCustId())) {
//                accounts.put(new JSONObject().put("isLoanAccount", account.isLoan() ? "Yes" : "No")
//                        .put("isSavingsAccount", VXController.savingProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("balance", account.getBalance().toPlainString())
//                        .put("accountName", account.getAccountName())
//                        .put("maxDeposit", account.isLoan() ? account.getBalance().toPlainString() : AXConstant.MILLION.toPlainString())
//                        .put("canWithdraw", VXController.nwdProducts.contains(account.getProductId()) ? "No" : "Yes")
//                        .put("maxWithdrawable", account.isLoan() ? BigDecimal.ZERO.toPlainString() : account.getBalance().toPlainString())
//                        .put("accountNo", account.getAccountNumber())
//                        .put("canDeposit", "Yes")
//                        .put("isNWD", VXController.nwdProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("isShareCapital", VXController.shareProducts.contains(account.getProductId()) ? "Yes" : "No")
//                );
//            }
//            responseMessage.value = new JSONObject().put("member", new JSONObject().put("nationalId", getRequest().getCustomer().getIdentity()).put("name", getRequest().getCustomer().getCustName())).put("accounts", accounts).toString();
//        }
//    }
//
//    public void validateAccountDetails(Holder<String> accountNumber, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        getRequest().setAccount(getClient().queryCustomerAccount(accountNumber.value));
//        if (validateFields(Check_Account)) {
//            CNCustomer customer = getClient().queryCustomer(getRequest().getAccount().getCustId());
//            responseMessage.value = new JSONObject().put("IDNo", customer.getIdentity()).put("OwnerName", customer.getCustName()).put("AccountName", getRequest().getAccount().getAccountName()).put("MobileNo", customer.getMobileNumber()).put("MemberImage", DatatypeConverter.printBase64Binary(getWorker().checkBlank(getClient().queryCustomerPhoto(getRequest().getAccount().getCustId()).getBytes(), new byte[0]))).toString();
//        }
//    }
//
//    public void getMemberImage(Holder<String> idNumber, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        processUnsupported(responseCode, responseMessage);
//    }
//
//    public void getLoanAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        JSONArray accounts = new JSONArray();
//        if (validateFields(Check_User)) {
//            for (CNAccount account : getClient().queryLoanAccounts(getUser().getCustId())) {
//                if (BigDecimal.ZERO.compareTo(account.getBalance()) != 0) {
//                    accounts.put(new JSONObject().put("isLoanAccount", account.isLoan() ? "Yes" : "No")
//                            .put("isSavingsAccount", VXController.savingProducts.contains(account.getProductId()) ? "Yes" : "No")
//                            .put("balance", account.getBalance().toPlainString())
//                            .put("accountName", account.getAccountName())
//                            .put("maxDeposit", account.getBalance().toPlainString())
//                            .put("canWithdraw", "No")
//                            .put("maxWithdrawable", "0")
//                            .put("accountNo", account.getAccountNumber())
//                            .put("canDeposit", "Yes")
//                            .put("isNWD", VXController.nwdProducts.contains(account.getProductId()) ? "Yes" : "No")
//                            .put("isShareCapital", VXController.shareProducts.contains(account.getProductId()) ? "Yes" : "No")
//                            .put("createdDate", getWorker().formatDate(AXConstant.spotDateFormat, account.getOpenDate()))
//                            .put("tofinishdate", getWorker().formatDate(AXConstant.spotDateFormat, account.getEndDate()))
//                            .put("installmentAmount", account.getArrears().toPlainString())
//                    );
//                }
//            }
//            responseMessage.value = new JSONObject().put("member", new JSONObject().put("nationalId", getRequest().getCustomer().getIdentity()).put("name", getRequest().getCustomer().getCustName())).put("accounts", accounts).toString();
//        }
//    }
//
//    public void getSavingsAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        JSONArray accounts = new JSONArray();
//        if (validateFields(Check_User)) {
//            for (CNAccount account : getClient().queryDepositAccounts(getUser().getCustId(), VXController.savingProducts)) {
//                accounts.put(new JSONObject().put("isLoanAccount", account.isLoan() ? "Yes" : "No")
//                        .put("isSavingsAccount", VXController.savingProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("balance", account.getBalance().toPlainString())
//                        .put("accountName", account.getAccountName())
//                        .put("maxDeposit", AXConstant.MILLION.toPlainString())
//                        .put("canWithdraw", VXController.nwdProducts.contains(account.getProductId()) ? "No" : "Yes")
//                        .put("maxWithdrawable", account.getBalance().toPlainString())
//                        .put("accountNo", account.getAccountNumber())
//                        .put("canDeposit", "Yes")
//                        .put("isNWD", VXController.nwdProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("isShareCapital", VXController.shareProducts.contains(account.getProductId()) ? "Yes" : "No")
//                );
//            }
//            responseMessage.value = new JSONObject().put("member", new JSONObject().put("nationalId", getRequest().getCustomer().getIdentity()).put("name", getRequest().getCustomer().getCustName())).put("accounts", accounts).toString();
//        }
//    }
//
//    public void getShareCapitalAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        JSONArray accounts = new JSONArray();
//        if (validateFields(Check_User)) {
//            for (CNAccount account : getClient().queryDepositAccounts(getUser().getCustId(), VXController.shareProducts)) {
//                accounts.put(new JSONObject().put("isLoanAccount", account.isLoan() ? "Yes" : "No")
//                        .put("isSavingsAccount", VXController.savingProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("balance", account.getBalance().toPlainString())
//                        .put("accountName", account.getAccountName())
//                        .put("maxDeposit", AXConstant.MILLION.toPlainString())
//                        .put("canWithdraw", VXController.nwdProducts.contains(account.getProductId()) ? "No" : "Yes")
//                        .put("maxWithdrawable", account.getBalance().toPlainString())
//                        .put("accountNo", account.getAccountNumber())
//                        .put("canDeposit", "Yes")
//                        .put("isNWD", VXController.nwdProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("isShareCapital", VXController.shareProducts.contains(account.getProductId()) ? "Yes" : "No")
//                );
//            }
//            responseMessage.value = new JSONObject().put("member", new JSONObject().put("nationalId", getRequest().getCustomer().getIdentity()).put("name", getRequest().getCustomer().getCustName())).put("accounts", accounts).toString();
//        }
//    }
//
//    public void getNWDAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
//        JSONArray accounts = new JSONArray();
//        if (validateFields(Check_User)) {
//            for (CNAccount account : getUser().getNwdAccounts()) {
//                accounts.put(new JSONObject().put("isLoanAccount", account.isLoan() ? "Yes" : "No")
//                        .put("isSavingsAccount", VXController.savingProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("balance", account.getBalance().toPlainString())
//                        .put("accountName", account.getAccountName())
//                        .put("maxDeposit", AXConstant.MILLION.toPlainString())
//                        .put("canWithdraw", VXController.nwdProducts.contains(account.getProductId()) ? "No" : "Yes")
//                        .put("maxWithdrawable", account.getBalance().toPlainString())
//                        .put("accountNo", account.getAccountNumber())
//                        .put("canDeposit", "Yes")
//                        .put("isNWD", VXController.nwdProducts.contains(account.getProductId()) ? "Yes" : "No")
//                        .put("isShareCapital", VXController.shareProducts.contains(account.getProductId()) ? "Yes" : "No")
//                );
//            }
//            responseMessage.value = new JSONObject().put("member", new JSONObject().put("nationalId", getRequest().getCustomer().getIdentity()).put("name", getRequest().getCustomer().getCustName())).put("accounts", accounts).toString();
//        }
//    }
//
//    public void geteLoanTypes(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        if (validateFields()) {
//            JSONArray products = new JSONArray();
//            for (ACProduct product : getClient().queryProducts(combineloanProducts())) {
//                products.put(new JSONObject().put("LoanCode", product.getCode()).put("LoanName", product.getDescription()));
//            }
//            getResponse().setResult(products.length() > 0 ? AXResult.Success : AXResult.Not_Found);
//            responseMessage.value = new JSONObject().put("eloanTypes", products).toString();
//        }
//    }
//
//    public void getStatementTransactions(Holder<String> accountNumber, Holder<javax.xml.datatype.XMLGregorianCalendar> startDate, Holder<javax.xml.datatype.XMLGregorianCalendar> endDate, Holder<javax.xml.datatype.XMLGregorianCalendar> startTime, Holder<javax.xml.datatype.XMLGregorianCalendar> endTime, Holder<String> emailaddress, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<String> returnValue) {
//        processUnsupported(responseCode, responseMessage);
//    }
//
//    public void getMemberEligibility(Holder<String> phoneNo, Holder<String> eLoanCode, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        setLoanFields(eLoanCode.value);
//        getRequest().setAmount(BigDecimal.ZERO);
//        setResponse(validateFields(Check_User, Check_Product, Check_Forbidden, Check_Default, Score_Exists, Score_Balance, Score_Arrears, Score_Deposit, Score_Limit) ? getResponse() : getResponse());
//        if (approved()) {
//            responseMessage.value = getWorker().formatAmount((BigDecimal) holder.value);
//        }
//    }
//
//    public void applyLoan(Holder<String> requestid, Holder<String> phoneNo, Holder<String> eLoanCode, Holder<java.math.BigDecimal> amount, Holder<java.math.BigDecimal> installments, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        setLoanFields(eLoanCode.value);
//        getRequest().setAmount(amount.value);
//        getRequest().setContra(getWorker().findAccount(getUser().getAccounts(), getRequest().getCurrency().getCode(), false));
//        setResponse(validateFields(Check_User, Check_Product, Check_Amount, Check_Contra, Check_Forbidden, Check_Default, Score_Exists, Score_Balance, Score_Arrears, Score_Deposit, Score_Minimum, Score_Limit) ? getXapi().processLoanRequest(getRequest()) : getResponse());
//        if (approved()) {
//            if (getRequest().getLoanType() != LNType.Three) {
//                getClient().updateAccrualDate(getRequest().getAccount().getAccountNumber(), getRequest().getLoanTerm());
//            }
//            getResponse().setResult(AXResult.Success_Fosa);
//        }
//    }
//
//    public void getBranchCodes(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        if (validateFields()) {
//            JSONArray branches = new JSONArray();
//            for (CNBranch branch : getClient().queryBusinessUnits()) {
//                branches.put(new JSONObject().put("BranchCode", branch.getBuCode()).put("BranchName", branch.getBuName()));
//            }
//            responseMessage.value = new JSONObject().put("BranchDetails", branches).toString();
//            getResponse().setResult(branches.length() > 0 ? AXResult.Success : AXResult.Failed);
//        }
//    }
//
//    public void referAFriend(Holder<String> firstName, Holder<String> lastName, Holder<String> phoneNo, Holder<String> gender, Holder<String> location, Holder<String> introducerPhone, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        check(getWorker().isBlank(firstName.value), AXResult.Missing_Name);
//        check(getWorker().isBlank(lastName.value), AXResult.Missing_Name);
//        check(getWorker().isBlank(phoneNo.value), AXResult.Invalid_Mobile);
//
//        if (validateFields(Block_Reversal, Check_User)) {
//            RFFriend friend = new RFFriend();
//            friend.setModule(VXController.module);
//            friend.setFirstNm(firstName.value);
//
//            friend.setGender(gender.value);
//            friend.setIntroducer(introducerPhone.value);
//            friend.setLastNm(lastName.value);
//
//            friend.setLocation(location.value);
//            friend.setPhoneNo(phoneNo.value);
//            getResponse().setResult(getClient().saveFriend(friend) ? getSender().sendMessage(phoneNo.value, getWorker().replaceAll(getWorker().replaceAll(getWorker().replaceAll(VXController.getAdvice(getRequest().getType()), "{NAME}", getWorker().capitalize(firstName.value)), "{MOBILE}", introducerPhone.value), "{USER}", getWorker().capitalize(getUser().getAccessName())), getLog()) : AXResult.Failed);
//        }
//    }
//
//    public void spotcashRegistration(javax.xml.ws.Holder<java.lang.String> responseCode, javax.xml.ws.Holder<java.lang.String> responseMessage, javax.xml.ws.Holder<java.lang.String> errorMessage, javax.xml.ws.Holder<java.lang.String> newMembersPayload) {
//        if (validateFields(Block_Reversal)) {
//            JSONArray users = new JSONArray();
//            String currentTime = getClient().queryTimeText();
//            for (CNCustomer user : getClient().queryUpdatedUsers()) {
//                users.put(new JSONObject().put("customer_name", user.getCustName()).put("msisdn", getWorker().formatMsisdn(user.getMobileNumber(), false)).put("idno", user.getIdentity()));
//            }
//            if (users.length() > 0) {
//                getClient().updateSetting(VXController.getSettings().get("UsersSpoolTime").setValue(VXController.usersSpoolTime = currentTime));
//            }
//            newMembersPayload.value = new JSONObject().put("NewRegistrations", users).toString();
//            getResponse().setResult(currentTime != null ? AXResult.Success : AXResult.Failed);
//        }
//    }
//
//    public void updateMPESADetails(javax.xml.ws.Holder<java.lang.String> spotCashID, javax.xml.ws.Holder<java.lang.String> mPESAID, javax.xml.ws.Holder<java.lang.String> phoneNo, javax.xml.ws.Holder<java.lang.String> responseCode, javax.xml.ws.Holder<java.lang.String> responseMessage, javax.xml.ws.Holder<java.lang.String> errorMessage) {
//        if (validateFields(Block_Reversal, Check_Match)) {
//            getClient().updateReceipt(VXController.channelId, spotCashID.value, mPESAID.value);
//        }
//    }
//
//    public void processReversal(Holder<String> requestID, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        getRequest().setReversal(true);
//        setTxn(getBox().getClient().queryTxn(VXController.channelId, requestID.value));
//        if (validateFields(Check_Exists) && prepare()) {
//            processTxn();
//        }
//    }
//
//    private void processDepositBalance(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        setResponse(validateFields(Check_User, Check_Account, Check_Duplicate) ? getBox().getXapi().queryDepositBalance(getRequest()) : getResponse());
//        if (approved()) {
//            responseMessage.value = getWorker().formatAmount(getResponse().getBalance());
//        }
//    }
//
//    private void processLoanBalance(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setAccount(getClient().queryLoanAccount(getUser().getCustId(), accountNumber.value));
//        setResponse(validateFields(Check_User, Check_Account, Check_Duplicate) ? getBox().getXapi().queryLoanBalance(getRequest()) : getResponse());
//        if (approved()) {
//            getResponse().setBalance(getRequest().getAccount().getBalance());
//            responseMessage.value = balance.value = getWorker().formatAmount(getRequest().getAccount().getBalance());
//        }
//    }
//
//    private void processMiniStatement(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setCount(VXController.ministatementTxnCount);
//        getRequest().setAccount(getClient().queryCustomerAccount(accountNumber.value));
//        setResponse(validateFields(Check_User, Check_Account, Check_Duplicate) ? getRequest().getAccount().isLoan() ? getBox().getXapi().queryLoanMinistatement(getRequest()) : getBox().getXapi().queryDepositMinistatement(getRequest()) : getResponse());
//        if (approved()) {
//            responseMessage.value = getResponse().getData();
//        }
//    }
//
//    private void processDeposit(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setReceipt(fKey.value);
//        getRequest().setDetail(fKey.value + "~" + phoneNo.value);
//        getRequest().setAccount(getClient().queryCustomerAccount(accountNumber.value));
//
//        getRequest().setContra(getWorker().getSetting(VXController.getSettings(), getRequest().getType().name() + "DebitAccount", CNAccount.class, VXController.spotcashSettlementAccount));
//        getRequest().setAmount(getWorker().convertToType(amount.value, BigDecimal.class));
//        getRequest().setType(getRequest().getAccount().isLoan() ? TXType.DepositLoan : getRequest().getType());
//        setResponse(validateFields(Check_Account, Check_Amount, Check_Contra, Check_Duplicate) ? getRequest().getContra().isLedger() ? (getRequest().getAccount().isLoan() ? getXapi().processLedgerRepayment(request) : getXapi().postGLToDepositTransfer(getRequest(), 1)) : (getRequest().getAccount().isLoan() ? getXapi().processRepaymentTransfer(request, 1) : getXapi().postDepositTransfer(getRequest(), 1)) : getResponse());
//    }
//
//    private void processWithdrawal(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        getRequest().setContra(getWorker().getSetting(VXController.getSettings(), getRequest().getType().name() + "CreditAccount", CNAccount.class, VXController.spotcashSettlementAccount));
//        getRequest().setAmount(getWorker().convertToType(amount.value, BigDecimal.class));
//        setResponse(validateFields(Check_User, Check_Account, Check_Linked, Check_Amount, Check_Contra, Check_Duplicate) ? getRequest().getContra().isLedger() ? getXapi().postDepositToGLTransfer(getRequest(), 1) : getXapi().postDepositTransfer(getRequest(), 1) : getResponse());
//    }
//
//    private void processPayment(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setDetail(getWorker().checkBlank(crAccount.value, getWorker().checkBlank(fKey.value, description.value)));
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        getRequest().setContra(getWorker().getSetting(VXController.getSettings(), getRequest().getType().name() + "CreditAccount", CNAccount.class, VXController.spotcashSettlementAccount));
//        getRequest().setAmount(getWorker().convertToType(amount.value, BigDecimal.class));
//        setResponse(validateFields(Check_User, Check_Account, Check_Linked, Check_Amount, Check_Contra, Check_Duplicate) ? getRequest().getContra().isLedger() ? getXapi().postDepositToGLTransfer(getRequest(), 1) : getXapi().postDepositTransfer(getRequest(), 1) : getResponse());
//    }
//
//    private void processAccountTransfer(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setDetail(accountNumber.value + ">" + crAccount.value);
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        getRequest().setAmount(getWorker().convertToType(amount.value, BigDecimal.class));
//
//        getRequest().setContra(getClient().queryCustomerAccount(crAccount.value));
//        getRequest().setType(getRequest().getContra().isLoan() ? TXType.TransferLoan : getRequest().getType());
//        setResponse(validateFields(Check_User, Check_Account, Check_Linked, Check_Amount, Check_Contra, Check_Duplicate) ? (getRequest().getContra().isLoan() ? getXapi().processRepaymentTransfer(getRequest(), 1) : getXapi().postDepositTransfer(getRequest(), 1)) : getResponse());
//    }
//
//    private void processGetAccountStatement(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setEmail(emailaddress.value);
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        getRequest().setStartDate(getWorker().parseDate(AXConstant.standardDateFormat, getWorker().cleanDateText(startDate.value, '-')));
//        getRequest().setEndDate(getWorker().parseDate(AXConstant.standardDateFormat, getWorker().cleanDateText(endDate.value, '-')));
//        setResponse(validateFields(Block_Reversal, Check_User, Check_Account, Check_Email, Check_Duplicate) ? getBox().getXapi().requestForStatement(getRequest()) : getResponse());
//    }
//
//    private void processOutwardTransfer(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
//        getRequest().setDetail(message.value + "~" + crAccount.value);
//        getRequest().setAccount(getClient().queryDepositAccount(getUser().getCustId(), accountNumber.value));
//        getRequest().setContra(getWorker().getSetting(VXController.getSettings(), getRequest().getType().name() + "CreditAccount", CNAccount.class, VXController.spotcashSettlementAccount));
//        getRequest().setAmount(getWorker().convertToType(amount.value, BigDecimal.class));
//        setResponse(validateFields(Check_User, Check_Account, Check_Linked, Check_Amount, Check_Contra, Check_Duplicate) ? getRequest().getContra().isLedger() ? getXapi().postDepositToGLTransfer(getRequest(), 1) : getXapi().postDepositTransfer(getRequest(), 1) : getResponse());
//    }
//
    public void mpesaVerify(CBRequest mpesaRequest, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        getRequest().setReceipt(mpesaRequest.getTxnId());
        getRequest().setDetail(mpesaRequest.getMsisdn() + "~" + getWorker().capitalize(mpesaRequest.getFirstName() + " " + mpesaRequest.getLastName()));
        getRequest().setAccount(getClient().queryCustomerAccount(mpesaRequest.getBillRef()));

        getRequest().setContra(VXController.mpesaDepositGL);
        getRequest().setAmount(mpesaRequest.getAmount());
        setResponse(validateFields(Check_Account) ? getResponse() : getResponse());
    }

    public void mpesaDeposit(CBRequest mpesaRequest, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        getRequest().setReceipt(mpesaRequest.getTxnId());
        String[] accountDetails = String.valueOf(mpesaRequest.getBillRef()).trim().split("\\s");
        getRequest().setDetail(mpesaRequest.getMsisdn() + "~" + getWorker().capitalize(mpesaRequest.getFirstName() + " " + mpesaRequest.getLastName()) + (accountDetails.length > 1 ? (">" + accountDetails[1]) : ""));
        getRequest().setAccount(getWorker().verifyAccount(getClient().queryCustomerAccount(accountDetails[0]), VXController.MpesaSuspenseGL));

        getRequest().setContra(VXController.mpesaDepositGL);
        getRequest().setAmount(mpesaRequest.getAmount());
        getRequest().setType(getRequest().getAccount().isLoan() ? TXType.MpesaLoan : getRequest().getType());
        setResponse(validateFields(Block_Reversal, Check_Account, Check_Amount, Check_Contra, Check_Duplicate)
                ? (getRequest().getAccount().isLedger() ? getXapi().postGLToGLTransfer(getRequest(), 1)
                : getXapi().postGLToDepositTransfer(getRequest(), 1)) : getResponse());

//        setResponse(validateFields(Block_Reversal, Check_Account, Check_Amount, Check_Contra, Check_Duplicate)
//                ? (getRequest().getAccount().isLoan() ? getXapi().processLedgerRepayment(getRequest())
//                : (getRequest().getAccount().isLedger() ? getXapi().postGLToGLTransfer(getRequest(), 1)
//                : getXapi().postGLToDepositTransfer(getRequest(), 1))) : getResponse());
        if (!approved()) {
            getRequest().setAccount(VXController.MpesaSuspenseGL);
            setResponse(validateFields(Block_Reversal, Check_Account, Check_Amount, Check_Contra, Check_Duplicate)
                    ? (getRequest().getAccount().isLedger() ? getXapi().postGLToGLTransfer(getRequest(), 1)
                    : getXapi().postGLToDepositTransfer(getRequest(), 1)) : getResponse());
//            setResponse(validateFields(Block_Reversal, Check_Account, Check_Amount, Check_Contra, Check_Duplicate)
//                    ? (getRequest().getAccount().isLoan() ? getXapi().processLedgerRepayment(getRequest())
//                    : (getRequest().getAccount().isLedger() ? getXapi().postGLToGLTransfer(getRequest(), 1)
//                    : getXapi().postGLToDepositTransfer(getRequest(), 1))) : getResponse());
        }
        if (approved()) {
            getSender().sendMessage(mpesaRequest.getMsisdn(), replaceMasks(getWorker().replaceAll(getWorker().replaceAll(VXController.getAdvice(getRequest().getType()), "{SENDER}", getWorker().capitalize(mpesaRequest.getFirstName())), "{RECEIVER}", (getRequest().getAccount().isLedger() ? "Suspense" : getWorker().capitalize(getRequest().getAccount().getAccountName())))), getLog());
        }
    }
//
//    public void recoverLoan(CNAccount loanAccount, CNAccount debitAccount, Holder<String> requestId, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
//        getRequest().setAccount(loanAccount);
//        getRequest().setContra(debitAccount);
//        getRequest().setAmount(loanAccount.getBalance().compareTo(debitAccount.getBalance()) > 0 ? debitAccount.getBalance() : loanAccount.getBalance());
//        setResponse(validateFields(Check_Account, Check_Amount, Check_Contra) ? getXapi().processRepaymentTransfer(getRequest(), 1) : getResponse());
//    }

    public void processUnsupported(Holder<String> responseCode, Holder<String> responseMessage) {
        getResponse().setResult(AXResult.Unknown_Txn);
    }

    public boolean validateFields(TVField... checkFields) {
        if (approved()) {
            for (TVField field : checkFields) {
                if (getRequest().isReversal()) {
                    switch (field) {
                        case Block_Reversal:
                            getResponse().setResult(getRequest().isReversal() ? AXResult.Not_Reversible : AXResult.Success);
                            break;
                        case Check_Exists:
                            getResponse().setResult(getWorker().isBlank(getTxn().getRecId()) ? AXResult.Txn_Not_Found : AXResult.Success);
                            break;
                    }
                } else {
                    switch (field) {
                        case Check_Exists:
                            getResponse().setResult(getWorker().isBlank(getTxn().getRecId()) ? AXResult.Txn_Not_Found : AXResult.Success);
                            break;
                        case Check_Duplicate:
                            getResponse().setResult(getClient().checkDuplicate(VXController.channelId, updateHolder(getRequest().getReference()), getRequest().getType().getCode(), getRequest().isReversal()) ? AXResult.Duplicate : AXResult.Success);
                            break;
                        case Check_Match:
                            getResponse().setResult(getClient().checkMatch(VXController.channelId, updateHolder(getRequest().getReference())) ? AXResult.Success : AXResult.Match_Not_Found);
                            break;
                        case Check_Limit:
                            getResponse().setResult(!getWorker().validateLimit(getRequest().getAmount(), updateHolder(getTxnLimit())) ? AXResult.High_Amount : (!getWorker().validateLimit(getClient().queryDayActivity(getRequest().getChannel().getId(), getRequest().getAccount().getAccountNumber(), getRequest().getType().name()).getTotal().add(getRequest().getAmount()), updateHolder(getDailyLimit())) ? AXResult.Daily_Limit : AXResult.Success));
                            break;
                        case Check_User:
                            getResponse().setResult(getWorker().isBlank(updateHolder(getUser().getAccessCode())) ? AXResult.Invalid_User : AXResult.Success);
                            break;
                        case Check_Product:
                            getResponse().setResult(getWorker().isBlank(getRequest().getProductId()) ? AXResult.Invalid_Product : AXResult.Success);
                            break;
                        case Check_Account:
                            getResponse().setResult(getWorker().isBlank(updateHolder(getRequest().getAccount().getAccountNumber())) ? AXResult.Invalid_Account : AXResult.Success);
                            break;
                        case Check_Linked:
                            getResponse().setResult(getWorker().isBlank(getWorker().findAccount(getUser().getAccounts(), updateHolder(getRequest().getAccount().getAccountNumber())).getAccountNumber()) ? AXResult.Not_Linked : AXResult.Success);
                            break;
                        case Check_Amount:
                            getResponse().setResult(!getWorker().validateAmount(updateHolder(getRequest().getAmount()), false) ? AXResult.Invalid_Amount : AXResult.Success);
                            break;
                        case Check_Contra:
                            getResponse().setResult(getWorker().isBlank(updateHolder(getRequest().getContra().getAccountNumber())) || Objects.equals(getRequest().getAccount().getAccountNumber(), getRequest().getContra().getAccountNumber()) ? AXResult.Invalid_Account : AXResult.Success);
                            break;
                        case Check_Email:
                            getResponse().setResult(!getWorker().validateEmail(updateHolder(getRequest().getEmail())) || !getClient().verifyEmail(getRequest().getUser().getCustId(), getRequest().getEmail()) ? AXResult.Invalid_Email : AXResult.Success);
                            break;
                        case Check_Forbidden:
                            getResponse().setResult(updateHolder(getWorker().sumBalances(getClient().queryLoanAccounts(getUser().getCustId(), (getRequest().getLoanType() == LNType.Two ? VXController.loanTwoForbidProducts : new ArrayList<>())))).compareTo(BigDecimal.ZERO) > 0 ? AXResult.Has_Loan : AXResult.Success);
                            break;
                        case Check_Default:
                            getResponse().setResult(getClient().checkTxn(VXController.channelId, getUser().getCustId(), TXType.LoanRecovery.getCode(), VXController.loanDefaultPardonDays) ? AXResult.Blacklisted : AXResult.Success);
                            break;
                        case Score_Exists:
                            getResponse().setResult(updateHolder(getWorker().sumBalances(getClient().queryLoanAccounts(getUser().getCustId(), combineloanProducts()))).compareTo(BigDecimal.ZERO) > 0 ? AXResult.Has_Loan : AXResult.Success);
                            break;
                        case Score_Balance:
                            getResponse().setResult(getWorker().sumBalances(getUser().getNwdAccounts()).compareTo(updateHolder(select(getRequest().getLoanType(), VXController.loanOneNwdBalance, VXController.loanTwoNwdBalance, VXController.loanThreeNwdBalance, BigDecimal.ZERO))) < 0 ? AXResult.Min_Balance : AXResult.Success);
                            break;
                        case Score_Arrears:
                            getResponse().setResult(updateHolder(getWorker().sumArrears(getClient().queryLoanAccounts(getUser().getCustId()))).compareTo(BigDecimal.ZERO) > 0 ? AXResult.Has_Arrears : AXResult.Success);
                            break;
                        case Score_Deposit:
                            getResponse().setResult(!getClient().checkMonthlyDeposits(getUser().getCustId(), getRequest().getLoanType(), updateHolder(select(getRequest().getLoanType(), VXController.loanOneDepositMonths, VXController.loanTwoDepositMonths, VXController.loanThreeDepositMonths, 0))) ? AXResult.Missed_Deposit : AXResult.Success);
                            break;
                        case Score_Minimum:
                            getResponse().setResult(getRequest().getAmount().compareTo(updateHolder(select(getRequest().getLoanType(), VXController.loanOneMinimumAmount, VXController.loanTwoMinimumAmount, VXController.loanThreeMinimumAmount, BigDecimal.ZERO))) < 0 ? AXResult.Below_Min : AXResult.Success);
                            break;
                        case Score_Limit:
                            getResponse().setResult(updateHolder(calculateLoanLimit()).compareTo(getRequest().getAmount()) < 0 ? AXResult.Credit_Limit : AXResult.Success);
                            break;
                    }
                }
                if (!approved()) {
                    break;
                }
            }
        }
        return ready();
    }

    private void recordTxn() {
        try {
            if (!getRequest().isReversal()) {
                getTxn().setTxnRef(getRequest().getReference());
                getTxn().setTxnDate(new Date());
                getTxn().setChannelId(getRequest().getChannel().getId());

                getTxn().setModule(getRequest().getModule());
                getTxn().setClient(getRequest().getClient());
                getTxn().setTxnCode(getRequest().getType().getCode());

                getTxn().setTxnType(getRequest().getType().name());
                getTxn().setBuId(getRequest().getBranch().getBuId());
                getTxn().setAcquirer(VXController.operator.toUpperCase());

                getTxn().setTerminal(VXController.operator.toUpperCase());
                getTxn().setAdvice(getRequest().isAdvice() ? "Y" : "N");
                getTxn().setOnus(!getWorker().isBlank(getUser().getCustId()) ? "Y" : "N");

                getTxn().setAccessCd(getRequest().getAccessCode());
                getTxn().setAccount(getRequest().getAccount().getAccountNumber());
                getTxn().setContra(getRequest().getContra().getAccountNumber());

                getTxn().setCurrency(getRequest().getCurrency().getCode());
                getTxn().setAmount(getRequest().getAmount());
                getTxn().setDescription(getRequest().getNarration());

                getTxn().setDetail(getRequest().getDetail());
                getTxn().setReceipt(getRequest().getReceipt());
                getTxn().setChargeLedger(getRequest().getCharge().getIncomeLedger());
                getTxn().setCharge(getRequest().getCharge().getChargeAmount());

                getTxn().setTxnId(getResponse().getTxnId());
                getTxn().setChgId(getResponse().getChargeId());
                getTxn().setXapiCode(getResponse().getXapiCode());

                getTxn().setResult(getResponse().getResult().getMessage());
                getTxn().setRespCode(getResponse().getResult().getCode());
                getTxn().setRecSt(approved() ? "A" : "F");
            } else if (approved()) {
                getTxn().setRecSt("R");
            }

            getTxn().setBalance(getResponse().getBalance());

            if (approved() || (!getRequest().isReversal() && getWorker().isBlank(getTxn().getRecId()))) {
                getClient().upsertTxn(getTxn());
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }
    }

    private void setTree() {
        getTreeItem().setApproved(approved());
        getTreeItem().setCode(getRequest().getType().getCode());
        getTreeItem().setText(getRequest().getReference());

        getTreeItem().getDetail().put(ALHeader.ReqId, getRequest().getReference());
        getTreeItem().getDetail().put(ALHeader.TxnCode, getRequest().getType().getCode());
        getTreeItem().getDetail().put(ALHeader.TranType, getRequest().getType());

        getTreeItem().getDetail().put(ALHeader.TxnDate, new Date());
        getTreeItem().getDetail().put(ALHeader.Account, getRequest().getAccount().getAccountNumber());
        getTreeItem().getDetail().put(ALHeader.Contra, getRequest().getContra().getAccountNumber());
        getTreeItem().getDetail().put(ALHeader.Currency, getRequest().getCurrency().getCode());

        getTreeItem().getDetail().put(ALHeader.Amount, getRequest().getAmount());
        getTreeItem().getDetail().put(ALHeader.Description, getRequest().getNarration());
        getTreeItem().getDetail().put(ALHeader.Charge, getRequest().getCharge().getChargeAmount());

        getTreeItem().getDetail().put(ALHeader.Balance, getResponse().getBalance());
        getTreeItem().getDetail().put(ALHeader.XapiCode, getResponse().getXapiCode());
        getTreeItem().getDetail().put(ALHeader.XapiMsg, getResponse().getMessage());

        getTreeItem().getDetail().put(ALHeader.Result, getResponse().getResult());
        getTreeItem().getDetail().put(ALHeader.TxnId, getResponse().getTxnId());
        getTreeItem().getDetail().put(ALHeader.ChgId, getResponse().getChargeId());

        getTreeItem().getDetail().put(ALHeader.Response, getResponse().getResult().getCode());
        getTreeItem().getDetail().put(ALHeader.RecSt, approved() ? "S" : "F");
        APMain.apFrame.insertTreeItem(getTreeItem(), VXController.module);
    }

    private void windup() {
        try {
            APMain.apFrame.getVmaMeter().showSignal(getActivity(), getRequest().getType().getCode(), false);
            if (getRequest().getType().isRecord() && getResponse().getResult().getType() != ARType.Ignore) {
                recordTxn();
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }
        writeLog();
    }

    private void writeLog() {
        waitToLog();
        String reqCall = prepareCall(true), resCall = prepareCall(false);
        try {
            setTree();
            getLog().setTxnId(getRequest().getReference());
            getLog().setTxnType(getRequest().getType().getCode() + " ~ " + getRequest().getType().name());

            getLog().setAccount(getRequest().getAccount().getAccountNumber());
            getLog().setNarration(getRequest().getNarration());
            getLog().setRequest(getRequest());

            getLog().setResponse(getResponse());
            getLog().setResult(getResponse().getResult().getCode() + " ~ " + getResponse().getResult().getMessage());
            getLog().setDuration(System.currentTimeMillis() - getStartTime() + " Ms");
        } catch (Exception ex) {
            getLog().logError(ex);
        }
        APMain.vmaLog.logEvent("<transaction>" + "\r\n" + getWorker().indentLines((reqCall + "\r\n" + getLog() + "\r\n" + resCall).trim()) + "\r\n" + "</transaction>");
    }

    private void setLoanFields(String code) {
        getRequest().setProductId(getClient().queryProduct(updateHolder(code)).getId());
        getRequest().setLoanType(VXController.loanOneProducts.contains(getRequest().getProductId()) ? LNType.One : VXController.loanTwoProducts.contains(getRequest().getProductId()) ? LNType.Two : VXController.loanThreeProducts.contains(getRequest().getProductId()) ? LNType.Three : LNType.None);
        getRequest().setTermCode(select(getRequest().getLoanType(), VXController.loanOneTermCode, VXController.loanTwoTermCode, VXController.loanThreeTermCode));
        getRequest().setLoanTerm(select(getRequest().getLoanType(), VXController.loanOneTerm, VXController.loanTwoTerm, VXController.loanThreeTerm));
    }

    private BigDecimal getDailyLimit() {
        HashMap<String, BigDecimal> customerLimits = getClient().queryCustomerChannelLimits(getUser().getCustChannelId(), getRequest().getCurrency().getId());
        BigDecimal limit = getWorker().getSetting(VXController.getSettings(), "Daily" + getRequest().getType().getGroup() + "Limit" + getRequest().getCurrency().getCode(), BigDecimal.class, BigDecimal.ZERO);
        switch (getRequest().getType().getGroup()) {
            case Payment:
                limit = customerLimits.getOrDefault("SP_PAY", limit);
                break;
            case Withdrawal:
                limit = customerLimits.getOrDefault("CASH_WD", limit);
                break;
            case Transfer:
                limit = customerLimits.getOrDefault("3P_ACCTFR", limit);
                break;
            case Remittance:
                limit = customerLimits.getOrDefault("DM_FNDFR", limit);
                break;
        }
        customerLimits.clear();
        return limit;
    }

    public String replaceMasks(String text) {
        if (!getWorker().isBlank(text)) {
            ArrayList<String> holdersList = getWorker().extractPlaceHolders(text);
            for (String placeHolder : holdersList) {
                String replacement = "<>";
                switch (placeHolder.toUpperCase()) {
                    case "{TXNREF}":
                        replacement = getRequest().getReference();
                        break;
                    case "{ACCOUNT}":
                        replacement = getRequest().getAccount().getAccountNumber();
                        break;
                    case "{CONTRA}":
                        replacement = getRequest().getContra().getAccountNumber();
                        break;
                    case "{CURRENCY}":
                        replacement = getRequest().getCurrency().getCode();
                        break;
                    case "{AMOUNT}":
                        replacement = getWorker().formatAmount(getRequest().getAmount());
                        break;
                    case "{RECEIPT}":
                        replacement = getRequest().getReceipt();
                        break;
                    case "{MOBILE}":
                        replacement = getCaller().getMsisdn();
                        break;
                    case "{NAME}":
                        replacement = getWorker().firstName(getUser().getAccessName());
                        break;
                    case "{DETAIL}":
                        replacement = getRequest().getDetail();
                        break;
                    case "{HOLDER}":
                        replacement = String.valueOf(holder.value);
                        break;
                }
                text = getWorker().replaceAll(text, placeHolder, replacement);
            }
            holdersList.clear();
            return text.replaceAll("~<>", "").replaceAll(" ~ <>", "").trim();
        }
        return text;
    }

    private boolean setLock(boolean lock) {
        if (lock) {
            check(VXController.lock.containsKey(getRequest().getReference()), AXResult.Processing);
            if (approved()) {
                VXController.lock.put(getRequest().getReference(), getRequest().getReference());
            }
        } else if (isLocked()) {
            VXController.lock.remove(getRequest().getReference());
        }
        return setLocked(lock && approved()) || !lock;
    }

    private boolean ready() {
        getRequest().setNarration((getRequest().isReversal() ? "REV~" : "") + replaceMasks(VXController.getNarrations().getProperty(getRequest().getType().getCode())));
        getRequest().setBranch(!getWorker().isBlank(getRequest().getAccount().getAccountNumber()) ? getRequest().getAccount().getBranch() : getRequest().getBranch());
        getRequest().getCharge().setCode(getRequest().getType().getCode());
        getWorker().setCharge(getRequest(), new HashMap<>());
        return approved();
    }

    private String prepareCall(boolean inward) {
        String tag = inward ? "reguest" : "response", call = VXController.calls.remove(getCallId() + (inward ? "I" : "O"));
        getWorker().extractFields(getWorker().isJson(call) ? new JSONObject(call) : getWorker().extractSoapObject(call, MobileBanking.class.getPackage().getName()), inward ? getTreeItem().getRequest() : getTreeItem().getResponse());
        return "<" + tag + ">" + "\r\n" + getWorker().indentLines(getWorker().isJson(call) ? getWorker().formatJson(call) : getWorker().formatXml(call)) + "\r\n" + "</" + tag + ">";
    }

    private BigDecimal getTxnLimit() {
        return getWorker().getSetting(VXController.getSettings(), getRequest().getType().getGroup() + "TxnLimit" + getRequest().getCurrency().getCode(), BigDecimal.class, BigDecimal.ZERO);
    }

    private BigDecimal calculateLoanLimit() {
        switch (getRequest().getLoanType()) {
            case One:
                return getWorker().checkHigher(getWorker().checkLower(getWorker().sumBalances(getUser().getNwdAccounts()).multiply(VXController.loanOneNwdPercentage).divide(AXConstant.HUNDRED, 0, RoundingMode.DOWN), VXController.loanOneMaximumAmount, VXController.loanOneMaximumAmount), VXController.loanOneMinimumAmount, BigDecimal.ZERO);
            case Two:
                return getWorker().checkHigher(getWorker().checkLower(getClient().queryIncomeAverage(getUser().getCustId(), VXController.loanTwoIncomeMonths).multiply(VXController.loanTwoNwdPercentage).divide(AXConstant.HUNDRED, 0, RoundingMode.DOWN), VXController.loanTwoMaximumAmount, VXController.loanTwoMaximumAmount), VXController.loanTwoMinimumAmount, BigDecimal.ZERO);
            case Three:
                return getWorker().checkHigher(getWorker().checkLower(getWorker().checkLower(getClient().queryDepositBalance(getUser().getNwdAccounts(), VXController.loanThreeBalanceMonth), getWorker().sumBalances(getUser().getNwdAccounts()), BigDecimal.ZERO).multiply(VXController.loanThreeNwdPercentage).divide(AXConstant.HUNDRED, 0, RoundingMode.DOWN), VXController.loanThreeMaximumAmount, VXController.loanThreeMaximumAmount), VXController.loanThreeMinimumAmount, BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    private <T> T select(LNType type, T... options) {
        return options.length > type.getId() ? options[type.getId()] : null;
    }

    private void check(Boolean condition, AXResult result) {
        if (approved() && condition) {
            getResponse().setResult(result);
        }
    }

    private void waitToLog() {
        int i = 0;
        while (!VXController.calls.containsKey(getCallId() + "O") && ++i <= 100) {
            getWorker().pauseThread(50);
        }
    }

    private ArrayList<Long> combineloanProducts() {
        return getWorker().combineLists(VXController.loanOneProducts, VXController.loanTwoProducts, VXController.loanThreeProducts);
    }

    private void processError(Exception ex) {
        check(approved(), AXResult.System_Error);
        getLog().logEvent(ex);
    }

    private <T> T updateHolder(T value) {
        return (T) (holder.value = value);
    }

    private boolean approved() {
        return getResponse().getResult().getType() == ARType.Success;
    }

    private DBPClient getClient() {
        return getBox().getClient();
    }

    private AXWorker getWorker() {
        return getBox().getWorker();
    }

    private APLog getLog() {
        return getBox().getLog();
    }

    private CNUser getUser() {
        return getRequest().getUser();
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
     * @return the request
     */
    public AXRequest getRequest() {
        return request;
    }

    /**
     * @param request the request to set
     */
    public void setRequest(AXRequest request) {
        this.request = request;
    }

    /**
     * @return the xclient
     */
    public AXClient getXapi() {
        return getBox().getXapi();
    }

    /**
     * @return the txn
     */
    public AXTxn getTxn() {
        return txn;
    }

    /**
     * @param txn the txn to set
     */
    public void setTxn(AXTxn txn) {
        this.txn = txn;
    }

    /**
     * @return the response
     */
    public AXResponse getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(AXResponse response) {
        this.response = response;
    }

    /**
     * @return the treeItem
     */
    public TRItem getTreeItem() {
        return treeItem;
    }

    /**
     * @param treeItem the treeItem to set
     */
    public void setTreeItem(TRItem treeItem) {
        this.treeItem = treeItem;
    }

    /**
     * @return the startTime
     */
    public Long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the activity
     */
    public AXActivity getActivity() {
        return activity;
    }

    /**
     * @return the caller
     */
    public VXCaller getCaller() {
        return getTxn().getCaller();
    }

    /**
     * @return the locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * @param locked the locked to set
     * @return locked
     */
    public boolean setLocked(boolean locked) {
        return this.locked = locked;
    }

    /**
     * @return the callId
     */
    public String getCallId() {
        return callId;
    }

    /**
     * @param callId the callId to set
     */
    public void setCallId(String callId) {
        this.callId = callId;
    }

    /**
     * @return the sender
     */
    public ALSender getSender() {
        return sender;
    }

    /**
     * @param sender the sender to set
     */
    public void setSender(ALSender sender) {
        this.sender = sender;
    }
}
