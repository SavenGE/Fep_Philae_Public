/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.DBPClient;
import PHilae.enu.AXResult;
import PHilae.est.ESController;
import PHilae.model.AXRequest;
import PHilae.model.AXResponse;
import PHilae.model.AXSplit;
import PHilae.model.CBNode;
import PHilae.model.CNAccount;
import PHilae.model.TCSplit;
import com.neptunesoftware.supernova.ws.common.XAPIRequestBaseObject;
import com.neptunesoftwareplc.ci.account.service.AccountBalanceRequestData;
import com.neptunesoftwareplc.ci.account.service.AccountBalanceResponseData;

import com.neptunesoftwareplc.ci.account.service.AccountService;
import com.neptunesoftwareplc.ci.account.service.AccountServiceImplService;
import com.neptunesoftwareplc.ci.transfer.service.FundsTransferOutputData;
import com.neptunesoftwareplc.ci.transfer.service.FundsTransferRequestData;
import com.neptunesoftwareplc.ci.transfer.service.GlTransferOutputData;
import com.neptunesoftwareplc.ci.transfer.service.GlTransferRequestData;
import com.neptunesoftwareplc.ci.transfer.service.TransferService;
import com.neptunesoftwareplc.ci.transfer.service.TransferServiceImplService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ListIterator;
import java.util.Objects;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Pecherk
 */
public final class AXClient implements Serializable {

    private ATBox box;

//    private AccountWebServiceStub accountWebService;
//    private TransactionsWebServiceStub transactionsWebService;
//
//    private FundsTransferWebServiceStub fundsTransferWebService;
//    private TxnProcessWebServiceStub txnProcessWebService;
//    private ChannelAdminWebServiceStub channelAdminWebService;
//
//    private WorkflowWebServiceStub workflowWebService;
//    private CasemgmtWebServiceStub casemgmtWebService;
//    private CustomerWebServiceStub customerWebService;
//
//    private LoanAccountWebServiceStub loanAccountWebService;
//    private CreditAppWebServiceStub creditAppWebService;
    private AccountService accountWebService;
//    private TransactionsWebServiceStub transactionsWebService;
    private TransferService fundsTransferWebService;
//    private TxnProcessWebServiceStub txnProcessWebService;

//    private ChannelAdminWebServiceStub channelAdminWebService;
//
//    private WorkflowWebServiceStub workflowWebService;
//    private CasemgmtWebServiceStub casemgmtWebService;
//    private CustomerWebServiceStub customerWebService;
//
//    private LoanAccountWebServiceStub loanAccountWebService;
//    private CreditAppWebServiceStub creditAppWebService;
    private AXResponse response = new AXResponse();
    private static long creditLock = 0L;

    public AXClient(ATBox box) {
        setBox(box);
    }

    private <T> T getPort(Class<T> clazz) {
        T port = null;
        try {
            boolean errorFound = false;
            for (ListIterator<CBNode> iterator = APController.getXapiNodes().listIterator(); iterator.hasNext();) {
                CBNode cBNode = iterator.next();
                try {
                    String className = clazz.getSimpleName();
                    getLog().logEvent("getPort className", className);
                    switch (className) {
                        case "AccountService":
                            port = (T) (accountWebService == null
                                    ? accountWebService = new AccountServiceImplService(new URL(((CBNode) cBNode).getContextUrl() + "AccountService?wsdl")).getAccountServiceImplPort() : accountWebService);
                            break;
                        case "TransferService":
                            port = (T) (fundsTransferWebService == null
                                    ? fundsTransferWebService = new TransferServiceImplService(new URL(((CBNode) cBNode).getContextUrl() + "TransferService?wsdl")).getTransferServiceImplPort() : fundsTransferWebService);
                            break;
                    }
                    swapNodes(cBNode);
                    break;
                } catch (Exception ex) {
                    cBNode.setOnline(Boolean.FALSE);
                    getLog().logError(ex);
                    errorFound = true;
                }
            }
            if (errorFound) {
                getWorker().sortArrayList(APController.getXapiNodes(), false);
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }
        return port;
    }

    private void swapNodes(CBNode cBNode) {
        int y = APController.getXapiNodes().indexOf(cBNode);
        for (int i = y + 1; i < APController.getXapiNodes().size(); i++) {
            if (APController.getXapiNodes().get(i).isOnline()) {
                Collections.swap(APController.getXapiNodes(), y, i);
                y = i;
            }
        }
    }

    private XAPIRequestBaseObject getBaseRequest(XAPIRequestBaseObject requestData, AXRequest aXRequest) {
        requestData.setChannelId(aXRequest.getChannel().getId());
        requestData.setChannelCode(aXRequest.getChannel().getCode());
        requestData.setCardNumber(getWorker().isBlank(aXRequest.getAccessCode()) ? aXRequest.getReference() : aXRequest.getAccessCode());

        requestData.setTransmissionTime(System.currentTimeMillis());
        requestData.setOriginatorUserId(aXRequest.getRole().getUserId());
        requestData.setTerminalNumber(aXRequest.getModule());

        requestData.setReference(checkReference(aXRequest.getReference()));
        requestData.setSysUserId(aXRequest.getRole().getUserId());

        requestData.setUserRoleId(aXRequest.getRole().getUserRoleId());
        requestData.setUserLoginId(aXRequest.getRole().getUserName());
        requestData.setUserAccessCode(aXRequest.getAccessCode());

        requestData.setUserId(aXRequest.getRole().getUserId());
        requestData.setValidXapiRequest(true);
        return requestData;
    }

    public AXResponse queryDepositBalance(AXRequest aXRequest) {
        try {
            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, false, 1)) {
                setNewBalance(aXRequest).setResult(AXResult.Success);
            }
        } catch (Exception ex) {
            getResponse().setResult(processError(ex));
        }
        return getResponse();
    }

    public AXResponse queryLoanBalance(AXRequest aXRequest) {
        try {
            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, false, 1)) {
                getResponse().setBalance(getBox().getClient().queryLoanBalance(aXRequest.getAccount().getAccountNumber()));
                getResponse().setResult(AXResult.Success);
            }
        } catch (Exception ex) {
            getResponse().setResult(processError(ex));
        }
        return getResponse();
    }

//    public AXResponse queryDepositMinistatement(AXRequest aXRequest) {
//        try {
//            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, false, 1)) {
//                TransactionInquiryRequest inquiryRequest = (TransactionInquiryRequest) getBaseRequest(new TransactionInquiryRequest(), aXRequest);
//                inquiryRequest.setAccountNumber(aXRequest.getAccount().getAccountNumber());
//
//                getLog().setCall(getCallTag(aXRequest, true), inquiryRequest);
////                DepositTxnOutputData[] statTxns = getPort(TransactionsWebServiceStub.class).findDepositMiniStatement(inquiryRequest).getDepositTxnOutputData();
//                List<DepositTxnOutputData> statTxns = getPort(TransactionsWebServiceStub.class).findDepositMiniStatement(inquiryRequest);
//                getLog().setCall(getCallTag(aXRequest, false), statTxns, getWorker().prepareDuration(inquiryRequest.getTransmissionTime()));
//
//                int i = 0;
//                StringBuilder buffer = new StringBuilder();
//                for (DepositTxnOutputData statTxn : statTxns) {
//                    buffer.append(getWorker().formatDate(AXConstant.cbsDateFormat, new Date(statTxn.getTxnDate()))).append(" ").append(getWorker().mapNarration(statTxn.getTxnDescription(), statTxn.getDrcrFlag())).append(" ").append(statTxn.getTxnCcyISOCode()).append(":").append(getWorker().formatAmount(statTxn.getTxnAmount())).append(i < aXRequest.getCount() ? "|" : "");
//                    if (i >= aXRequest.getCount()) {
//                        break;
//                    }
//                }
//
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//                getResponse().setData(buffer.toString().trim());
//            } else if (AXResult.Insufficient_Funds == getResponse().getResult()) {
//                setNewBalance(aXRequest).setResult(AXResult.Insufficient_Funds);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//    public AXResponse queryLoanMinistatement(AXRequest aXRequest) {
//        try {
//            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, false, 1)) {
//                LoanAccountHistoryRequest historyRequest = (LoanAccountHistoryRequest) getBaseRequest(new LoanAccountHistoryRequest(), aXRequest);
//                historyRequest.setAccountNumber(aXRequest.getAccount().getAccountNumber());
//                historyRequest.setIsFirstTime(Boolean.TRUE);
//
//                getLog().setCall(getCallTag(aXRequest, true), historyRequest);
////                LoanAccountHistoryOutputData[] statTxns = getPort(LoanAccountWebServiceStub.class).findLoanAccountHistory(historyRequest).getLoanAccountHistoryOutputData();
//                List<LoanAccountHistoryOutputData> statTxns = getPort(LoanAccountWebServiceStub.class).findLoanAccountHistory(historyRequest);
//                getLog().setCall(getCallTag(aXRequest, false), statTxns, getWorker().prepareDuration(historyRequest.getTransmissionTime()));
//
//                int i = 0;
//                StringBuilder buffer = new StringBuilder();
//                for (LoanAccountHistoryOutputData statTxn : statTxns) {
//                    buffer.append(statTxn.getTxnDate()).append(" ").append(getWorker().mapNarration(statTxn.getDescription(), statTxn.getDebitCreditIndicator())).append(" ").append(statTxn.getCurrency()).append(":").append(getWorker().formatAmount(getWorker().cleanAmount(statTxn.getTxnAmount(), BigDecimal.ZERO))).append(i < aXRequest.getCount() ? "|" : "");
//                    if (i >= aXRequest.getCount()) {
//                        break;
//                    }
//                }
//
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//                getResponse().setData(buffer.toString().trim());
//            } else if (AXResult.Insufficient_Funds == getResponse().getResult()) {
//                setNewBalance(aXRequest).setResult(AXResult.Insufficient_Funds);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//    public AXResponse postDepositTransfer(AXRequest aXRequest, Integer repeatCount) {
//        try {
//            if (repeatCount > 1 || AXResult.Success == checkBalance(aXRequest).getResult()) {
//                FundsTransferRequestData transferRequest = (FundsTransferRequestData) getBaseRequest(new FundsTransferRequestData(), aXRequest);
//                transferRequest.setFromAccountNumber(aXRequest.isReversal() == aXRequest.isInverted() ? aXRequest.getAccount().getAccountNumber() : aXRequest.getContra().getAccountNumber());
//                transferRequest.setToAccountNumber(aXRequest.isReversal() == aXRequest.isInverted() ? aXRequest.getContra().getAccountNumber() : aXRequest.getAccount().getAccountNumber());
//
//                transferRequest.setTxnDescription(aXRequest.getNarration());
//                transferRequest.setAcquiringInstitutionCode("");
//                transferRequest.setTransactionAmount(aXRequest.getAmount());
//
//                transferRequest.setAmount(aXRequest.getAmount());
//                transferRequest.setFromCurrencyCode(aXRequest.getCurrency().getCode());
//                transferRequest.setToCurrencyCode(aXRequest.getContra().getCurrency().getCode());
//
//                transferRequest.setForwardingInstitutionCode("");
//                transferRequest.setTrack2Data(aXRequest.getReference());
//                transferRequest.setRetrievalReferenceNumber(aXRequest.getReference());
//
//                transferRequest.setCurrBUId(aXRequest.getBranch().getBuId());
//                transferRequest.setUserBusinessRoleId(aXRequest.getRole().getBuRoleId());
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(getClient().getProcessingDate());
//
//                transferRequest.setLocalTransactionTime(createXmlGregorianCalendar(getClient().getProcessingDate()));
//                getLog().setCall(getCallTag(aXRequest, true), transferRequest, repeatCount > 1);
//
//                FundsTransferOutputData fundsTransferOutputData = getPort(FundsTransferWebServiceStub.class).internalDepositAccountTransfer(transferRequest);
//                getLog().setCall(getCallTag(aXRequest, false), fundsTransferOutputData, getWorker().prepareDuration(transferRequest.getTransmissionTime()));
//                getResponse().setResult(mapCode(fundsTransferOutputData.getResponseCode()));
//
//                if (Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode())) {
//                    getResponse().setTxnId(findTxnId(transferRequest, fundsTransferOutputData.getRetrievalReferenceNumber()));
//                    if (processCharge(aXRequest, true, 1) || aXRequest.isInverted() || aXRequest.isReversal()) {
//                        setNewBalance(aXRequest).setResult(AXResult.Success);
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            if (isRepeatable(repeatCount, ex)) {
//                return postDepositTransfer(aXRequest, ++repeatCount);
//            } else {
//                getResponse().setResult(processError(ex));
//            }
//        }
//        return getResponse();
//    }
//    public AXResponse processLoanRequest(AXRequest aXRequest) {
//        try {
//            if (createCreditApplication(aXRequest) && createLoanAccount(aXRequest)) {
//                aXRequest.setAccount(getClient().queryLoanAccount(getResponse().getAccountNumber()));
//                if (disburseLoan(aXRequest, 1)) {
//                    aXRequest.getAccount().setBalance(getClient().queryLoanBalance(aXRequest.getAccount().getAccountNumber()));
//                }
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//    private boolean createCreditApplication(AXRequest aXRequest) {
//        long callerId = (creditLock = (creditLock % 999999) + 1);
//        ArrayList<Long> mandatoryFields = getClient().queryMandatoryFields(VXController.creditCustomScreen);
//        try {
//            String currentDate = AXConstant.cbsDateFormat.format(getClient().getProcessingDate());
//            CreditApplRequestData creditApplRequestData = (CreditApplRequestData) getBaseRequest(new CreditApplRequestData(), aXRequest);
//            creditApplRequestData.setCurrencyId(aXRequest.getCurrency().getId());
//
//            creditApplRequestData.setApplAvailableAmount(aXRequest.getAmount());
//            creditApplRequestData.setApprovedAmount(aXRequest.getAmount());
//            creditApplRequestData.setAmount(aXRequest.getAmount());
//
//            creditApplRequestData.setApprovedCurrencyId(aXRequest.getCurrency().getId());
//            creditApplRequestData.setApprovedTermCode(aXRequest.getTermCode());
//            creditApplRequestData.setApprovedTermValue(Long.valueOf(aXRequest.getLoanTerm()));
//
//            creditApplRequestData.setProductId(aXRequest.getProductId());
//            creditApplRequestData.setTermCode(aXRequest.getTermCode());
//            creditApplRequestData.setTermValue(Long.valueOf(aXRequest.getLoanTerm()));
//
//            creditApplRequestData.setBuId(aXRequest.getBranch().getBuId());
//            creditApplRequestData.setBusinessUnitId(aXRequest.getBranch().getBuId());
//            creditApplRequestData.setBankOfficerId(VXController.relationshipOfficerId);
//
//            creditApplRequestData.setCrUtilizationMethodCode("SINGLEDISB");
//            creditApplRequestData.setCreditTypeId(VXController.creditTypeId);
//            creditApplRequestData.setCreditPortfolioId(VXController.creditPortfolioId);
//
//            creditApplRequestData.setCustomerNumber(aXRequest.getCustomer().getCustNo());
//            creditApplRequestData.setDepositAcctId(aXRequest.getContra().getAcctId());
//            creditApplRequestData.setProductCombinationOption("SING_PROD");
//
//            creditApplRequestData.setStrFromDate(currentDate);
//            creditApplRequestData.setStrRequiredDate(currentDate);
//            creditApplRequestData.setPurposeOfCreditId(VXController.purposeOfCreditId);
//
//            creditApplRequestData.setStrApplicationDate(currentDate);
//            creditApplRequestData.setReferenceNumber(aXRequest.getReference());
//            creditApplRequestData.setRepaySourceAcctId(aXRequest.getContra().getAcctId());
//
//            creditApplRequestData.setCurrBUId(aXRequest.getBranch().getBuId());
//            creditApplRequestData.setCustomerId(aXRequest.getCustomer().getCustId());
//
//            creditApplRequestData.setRequiredDate(createXmlGregorianCalendar(getClient().getProcessingDate()));
//            creditApplRequestData.setValidXapiRequest(true);
//            getLog().setCall("creditapplreq", creditApplRequestData);
//
//            updateCustomFields(mandatoryFields, creditLock, callerId, false);
//            getPort(CreditAppWebServiceStub.class).createCreditApplication(creditApplRequestData);
//            getResponse().setApplId(getClient().queryApplId(aXRequest.getUser().getCustId()));
//
//            if (!getWorker().isBlank(getResponse().getApplId())) {
//                aXRequest.setEventId(VXController.approveApplicationEventId);
//                approveCreditApplication(aXRequest);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//
//        updateCustomFields(mandatoryFields, creditLock, callerId, true);
//        return !getWorker().isBlank(getResponse().getApplId());
//    }
    private void updateCustomFields(ArrayList<Long> mandatoryFields, Long locker, long callerId, boolean mandatory) {
        mandatoryFields.stream().filter((field) -> (callerId == locker)).forEach((field)
                -> {
            getClient().updateCustomType(field, mandatory);
        });
    }

//    private boolean createLoanAccount(AXRequest aXRequest) {
//        try {
//            LoanAccountRequestData loanAccountRequestData = (LoanAccountRequestData) getBaseRequest(new LoanAccountRequestData(), aXRequest);
//            loanAccountRequestData.setAccountName(aXRequest.getCustomer().getCustName());
//            loanAccountRequestData.setAutoReclassifyFlag("Y");
//
//            loanAccountRequestData.setApplId(getResponse().getApplId());
//            loanAccountRequestData.setCurrencyId(aXRequest.getCurrency().getId());
//            loanAccountRequestData.setCustomerId(aXRequest.getUser().getCustId());
//
//            loanAccountRequestData.setCapitalisedEventDueDateOption("AMORTISE");
//            loanAccountRequestData.setDisbChargeSettlement(getWorker().isYes(VXController.disburseNetOfFee) ? "NET_OFF" : "LN_ACCT");
//            loanAccountRequestData.setDisbursementLimit(aXRequest.getAmount());
//
//            loanAccountRequestData.setDisbrsmntSetlmntAcctId(aXRequest.getContra().getAcctId());
//            loanAccountRequestData.setLoanFeeAccountId(aXRequest.getContra().getAcctId());
//            loanAccountRequestData.setMainBranchId(aXRequest.getBranch().getBuId());
//
//            loanAccountRequestData.setPortfolioId(VXController.creditPortfolioId);
//            loanAccountRequestData.setPrimaryOfficerId(VXController.relationshipOfficerId);
//            loanAccountRequestData.setProductId(aXRequest.getProductId());
//
//            loanAccountRequestData.setStartDate(AXConstant.cbsDateFormat.format(getClient().getProcessingDate()));
//            loanAccountRequestData.setRiskClassId(VXController.riskClassId);
//            loanAccountRequestData.setTermCode(aXRequest.getTermCode());
//
//            loanAccountRequestData.setTermValue(aXRequest.getLoanTerm());
//            loanAccountRequestData.setMultiCurrency(false);
//
//            getLog().setCall("crtlnacctreq", loanAccountRequestData);
//            LoanAccountSummaryOutputData loanAccountSummaryOutputData = getPort(LoanAccountWebServiceStub.class).createAndActivateLoanAccount(loanAccountRequestData);
//            getLog().setCall("crtlnacctres", loanAccountSummaryOutputData);
//
//            getResponse().setAccountNumber(loanAccountSummaryOutputData.getAccountNo());
//            return !getWorker().isBlank(loanAccountSummaryOutputData.getAccountNo());
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return false;
//    }
//    private boolean disburseLoan(AXRequest aXRequest, Integer repeatCount) {
//        try {
//            XAPIBaseTxnRequestData txnRequestData = (XAPIBaseTxnRequestData) getBaseRequest(new XAPIBaseTxnRequestData(), aXRequest);
//            txnRequestData.setAcctNo(aXRequest.getAccount().getAccountNumber());
//            txnRequestData.setContraAcctNo(aXRequest.getContra().getAccountNumber());
//
//            txnRequestData.setTxnDescription(aXRequest.getNarration());
//            txnRequestData.setTxnAmount(aXRequest.getAmount());
//            txnRequestData.setTxnCurrencyCode(aXRequest.getCurrency().getCode());
//
//            getLog().setCall("disbloanreq", txnRequestData);
//            TxnResponseOutputData responseOutputData = getPort(TransferService.class).postLoanDisbursementRequest(txnRequestData);
//            getLog().setCall("disbloanres", responseOutputData);
//
//            if (Objects.equals(AXConstant.XAPI_APPROVED, responseOutputData.getResponseCode())) {
//                getResponse().setTxnId(findTxnId(txnRequestData, responseOutputData.getRetrievalReferenceNumber()));
//                processCharge(aXRequest, true, 1);
//                aXRequest.setDisbAmount(aXRequest.getAmount().subtract(aXRequest.getCharge().getChargeAmount()));
//                return true;
//            }
//        } catch (Exception ex) {
//            if (isRepeatable(repeatCount, ex)) {
//                return disburseLoan(aXRequest, ++repeatCount);
//            } else {
//                getResponse().setResult(processError(ex));
//            }
//        }
//        return false;
//    }
//    public AXResponse processLedgerRepayment(AXRequest aXRequest) {
//        try {
//            CNAccount loanAccount = aXRequest.getAccount();
//            CNAccount contraAccount = aXRequest.getContra();
//            aXRequest.setAccount(getClient().queryRepaymentAccount(loanAccount.getAccountNumber()));
//            aXRequest.getCharge().setWaive(true);
//
//            if (postGLToDepositTransfer(aXRequest, 1).getResult() == AXResult.Success) {
//                aXRequest.getCharge().setWaive(false);
//                aXRequest.setContra(aXRequest.getAccount());
//                aXRequest.setAccount(loanAccount);
//
//                processRepaymentTransfer(aXRequest, 1);
//                aXRequest.setContra(contraAccount);
//                getResponse().setResult(AXResult.Success);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//    public AXResponse processRepaymentTransfer(AXRequest aXRequest, Integer repeatCount) {
//        try {
//            XAPIBaseTxnRequestData txnRequestData = (XAPIBaseTxnRequestData) getBaseRequest(new XAPIBaseTxnRequestData(), aXRequest);
//            txnRequestData.setAcctNo(aXRequest.getAccount().isLoan() ? aXRequest.getAccount().getAccountNumber() : aXRequest.getContra().getAccountNumber());
//            txnRequestData.setContraAcctNo(aXRequest.getAccount().isLoan() ? aXRequest.getContra().getAccountNumber() : aXRequest.getAccount().getAccountNumber());
//
//            txnRequestData.setTxnDescription(aXRequest.getNarration());
//            txnRequestData.setTxnAmount(aXRequest.getAmount());
//            txnRequestData.setTxnCurrencyCode(aXRequest.getCurrency().getCode());
//
//            getLog().setCall(getCallTag(aXRequest, true), txnRequestData, repeatCount > 1);
//            TxnResponseOutputData responseOutputData = getPort(TransferService.class).postLoanRepaymentByAccountTransfer(txnRequestData);
//            getLog().setCall(getCallTag(aXRequest, false), responseOutputData, getWorker().prepareDuration(responseOutputData.getTransmissionTime()));
//
//            getResponse().setResult(mapCode(responseOutputData.getResponseCode()));
//            if (Objects.equals(AXConstant.XAPI_APPROVED, responseOutputData.getResponseCode())) {
//                getResponse().setTxnId(findTxnId(txnRequestData, responseOutputData.getRetrievalReferenceNumber()));
//                processCharge(aXRequest, false, 1);
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//            }
//        } catch (Exception ex) {
//            if (isRepeatable(repeatCount, ex)) {
//                return processRepaymentTransfer(aXRequest, ++repeatCount);
//            } else {
//                getResponse().setResult(processError(ex));
//            }
//        }
//        return getResponse();
//    }
    public AXResponse postDepositToGLTransfer(AXRequest aXRequest, Integer repeatCount) {
        try {
            if (repeatCount > 1 || AXResult.Success == checkBalance(aXRequest).getResult()) {
                FundsTransferRequestData fundsTransferRequestData = new FundsTransferRequestData();
                XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

                fundsTransferRequestData.setChannelId(requestData.getChannelId());
                fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
                fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
                fundsTransferRequestData.setTxnReference(requestData.getReference());

                fundsTransferRequestData.setAccountNo(aXRequest.getAccount().getAccountNumber());
                fundsTransferRequestData.setContraAcctNo(aXRequest.getContra().getAccountNumber());

                fundsTransferRequestData.setNarrative(aXRequest.getNarration());
                fundsTransferRequestData.setTxnAmount(aXRequest.getAmount());
                fundsTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
                fundsTransferRequestData.setTxnReference(aXRequest.getReference());

                getLog().setCall(getCallTag(aXRequest, true), requestData, repeatCount > 1);
                FundsTransferOutputData fundsTransferOutputData = aXRequest.isReversal()
                        ? getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData)
                        : getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData);
                getLog().setCall(getCallTag(aXRequest, false), fundsTransferOutputData, getWorker().prepareDuration(requestData.getTransmissionTime()));

                getResponse().setResult(mapCode(fundsTransferOutputData.getResponseCode()));
                if (Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode())) {
                    getResponse().setTxnId(fundsTransferOutputData.getTxnJournalId());
                    if (processCharge(aXRequest, true, 1) || aXRequest.isReversal()) {
                        setNewBalance(aXRequest).setResult(AXResult.Success);
                    }
                }
            }
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, ex)) {
                return postDepositToGLTransfer(aXRequest, ++repeatCount);
            } else {
                getResponse().setResult(processError(ex));
            }
        }
        return getResponse();
    }

    public AXResponse postGLToDepositTransfer(AXRequest aXRequest, Integer repeatCount) {
        try {
            FundsTransferRequestData fundsTransferRequestData = new FundsTransferRequestData();
            XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

            fundsTransferRequestData.setChannelId(requestData.getChannelId());
            fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
            fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
            fundsTransferRequestData.setTxnReference(requestData.getReference());

            fundsTransferRequestData.setAccountNo(aXRequest.getAccount().getAccountNumber());
            fundsTransferRequestData.setContraAcctNo(aXRequest.getContra().getAccountNumber());

            fundsTransferRequestData.setNarrative(aXRequest.getNarration());
            fundsTransferRequestData.setTxnAmount(aXRequest.getAmount());
            fundsTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
            fundsTransferRequestData.setTxnReference(aXRequest.getReference());

            getLog().setCall(getCallTag(aXRequest, true), fundsTransferRequestData, repeatCount > 1);
            FundsTransferOutputData fundsTransferOutputData = aXRequest.isReversal()
                    ? getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData)
                    : getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData);
            getLog().setCall(getCallTag(aXRequest, false), fundsTransferOutputData, getWorker().prepareDuration(fundsTransferRequestData.getTransmissionTime()));

            if (Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode())) {
                getResponse().setTxnId(fundsTransferOutputData.getTxnJournalId());
                processCharge(aXRequest, true, 1);
                setNewBalance(aXRequest).setResult(AXResult.Success);
            }
            getResponse().setResult(mapCode(fundsTransferOutputData.getResponseCode()));
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, ex)) {
                return postGLToDepositTransfer(aXRequest, ++repeatCount);
            } else {
                getResponse().setResult(processError(ex));
            }
        }
        return getResponse();
    }

    public AXResponse postGLToGLTransfer(AXRequest aXRequest, Integer repeatCount) {
        try {
            GlTransferRequestData glTransferRequestData = new GlTransferRequestData();
            XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

            glTransferRequestData.setChannelId(requestData.getChannelId());
            glTransferRequestData.setChannelCode(requestData.getChannelCode());
            glTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
            glTransferRequestData.setTxnReference(requestData.getReference());

            glTransferRequestData.setAccountNo(aXRequest.isReversal() == aXRequest.isInverted() ? aXRequest.getAccount().getAccountNumber() : aXRequest.getContra().getAccountNumber());
            glTransferRequestData.setContraAcctNo(aXRequest.isReversal() == aXRequest.isInverted() ? aXRequest.getContra().getAccountNumber() : aXRequest.getAccount().getAccountNumber());

            glTransferRequestData.setTxnAmount(aXRequest.getAmount());
            glTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
            glTransferRequestData.setNarrative(aXRequest.getNarration());

            getLog().setCall(getCallTag(aXRequest, true), glTransferRequestData, repeatCount > 1);
            GlTransferOutputData gLTransferOutputData = getPort(TransferService.class).postGLToGLTransfer(glTransferRequestData);
            getLog().setCall(getCallTag(aXRequest, false), gLTransferOutputData, getWorker().prepareDuration(glTransferRequestData.getTransmissionTime()));

            if (Objects.equals(AXConstant.XAPI_APPROVED, gLTransferOutputData.getResponseCode())) {
                getResponse().setTxnId(gLTransferOutputData.getTxnJournalId());
                if (!getWorker().isBlank(getResponse().getTxnId())) {
                    getClient().updateTxnRefText(getResponse().getTxnId(), glTransferRequestData.getTxnReference());
                }
            }
            getResponse().setResult(mapCode(gLTransferOutputData.getResponseCode()));
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, ex)) {
                return postGLToGLTransfer(aXRequest, ++repeatCount);
            } else {
                getResponse().setResult(processError(ex));
            }
        }
        return getResponse();
    }

//    public AXResponse postTellerCashDeposit(AXRequest aXRequest, CPRequest cPRequest, Integer repeatCount) {
//        try {
//            CashRequestData cashRequestData = (CashRequestData) getBaseRequest(new CashRequestData(), aXRequest);
//            cashRequestData.setChannelId(aXRequest.getChannel().getId());//Use teller module id
//            cashRequestData.setAcctNo(aXRequest.getAccount().getAccountNumber());
//            cashRequestData.setTxnDescription(aXRequest.getNarration());
//
//            cashRequestData.setAmount(aXRequest.getAmount());
//            cashRequestData.setTxnAmount(aXRequest.getAmount());
//            cashRequestData.setTxnCurrencyCode(aXRequest.getCurrency().getCode());
//
//            cashRequestData.setReference(aXRequest.getReference());
//            cashRequestData.setTxnReference(aXRequest.getReference());
//            cashRequestData.setCashInAmount(aXRequest.getAmount());
//
//            cashRequestData.setDrawerUserId(aXRequest.getRole().getUserRoleId());
//
////            cashRequestData.setDenominationAmount(cPRequest.getDenominationAmount());
////            cashRequestData.setDenominationCount(cPRequest.getDenominationCount());
//            cashRequestData.setDrawerNumber(aXRequest.getDrawerNumber());
//            cashRequestData.setCurrBUId(aXRequest.getBranch().getBuId());
//            cashRequestData.setOrgBusinessUnitId(aXRequest.getBranch().getBuId());
//
//            cashRequestData.setOriginatorUserId(aXRequest.getRole().getUserId());
//            cashRequestData.setUserBusinessRoleId(aXRequest.getRole().getBuRoleId());
//            cashRequestData.setUserRoleId(aXRequest.getRole().getUserRoleId());
//
//            getLog().setCall(getCallTag(aXRequest, true), cashRequestData, repeatCount > 1);
//            TxnResponseOutputData txnResponseOutputData = getPort(TransferService.class).depositCashWithDrawerUpdate(cashRequestData);
//            getLog().setCall(getCallTag(aXRequest, false), txnResponseOutputData, getWorker().prepareDuration(cashRequestData.getTransmissionTime()));
//
//            if (Objects.equals(AXConstant.XAPI_APPROVED, txnResponseOutputData.getResponseCode())) {
//                getResponse().setTxnId(findTxnId(cashRequestData, txnResponseOutputData.getRetrievalReferenceNumber()));
//                processCharge(aXRequest, true, 1);
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//            }
//            getResponse().setResult(mapCode(txnResponseOutputData.getResponseCode()));
//        } catch (Exception ex) {
//            if (isRepeatable(repeatCount, ex)) {
//                return postTellerCashDeposit(aXRequest, cPRequest, ++repeatCount);
//            } else {
//                getResponse().setResult(processError(ex));
//            }
//        }
//        return getResponse();
//    }
//
//    public AXResponse postTellerCashWithdrawal(AXRequest aXRequest, CPRequest cPRequest, Integer repeatCount) {
//        try {
//            CashRequestData cashRequestData = (CashRequestData) getBaseRequest(new CashRequestData(), aXRequest);
//            cashRequestData.setChannelId(aXRequest.getChannel().getId());//Use teller module id
//            cashRequestData.setAcctNo(aXRequest.getAccount().getAccountNumber());
//            cashRequestData.setTxnDescription(aXRequest.getNarration());
//
//            cashRequestData.setAmount(aXRequest.getAmount());
//            cashRequestData.setTxnAmount(aXRequest.getAmount());
//            cashRequestData.setTxnCurrencyCode(aXRequest.getCurrency().getCode());
//
//            cashRequestData.setReference(aXRequest.getReference());
//            cashRequestData.setTxnReference(aXRequest.getReference());
//            cashRequestData.setCashOutAmount(aXRequest.getAmount());
//
//            cashRequestData.setDrawerUserId(aXRequest.getRole().getUserRoleId());
//
////            cashRequestData.setDenominationAmount(cPRequest.getDenominationAmount());
////            cashRequestData.setDenominationCount(cPRequest.getDenominationCount());
//            cashRequestData.setDrawerNumber(aXRequest.getDrawerNumber());
//            cashRequestData.setCurrBUId(aXRequest.getBranch().getBuId());
//            cashRequestData.setOrgBusinessUnitId(aXRequest.getBranch().getBuId());
//
//            cashRequestData.setOriginatorUserId(aXRequest.getRole().getUserId());
//            cashRequestData.setUserBusinessRoleId(aXRequest.getRole().getBuRoleId());
//            cashRequestData.setUserRoleId(aXRequest.getRole().getUserRoleId());
//
//            getLog().setCall(getCallTag(aXRequest, true), cashRequestData, repeatCount > 1);
//            TxnResponseOutputData txnResponseOutputData = getPort(TransferService.class).withdrawCashWithDrawerUpdate(cashRequestData);
//            getLog().setCall(getCallTag(aXRequest, false), txnResponseOutputData, getWorker().prepareDuration(cashRequestData.getTransmissionTime()));
//
//            if (Objects.equals(AXConstant.XAPI_APPROVED, txnResponseOutputData.getResponseCode())) {
//                getResponse().setTxnId(findTxnId(cashRequestData, txnResponseOutputData.getRetrievalReferenceNumber()));
//                processCharge(aXRequest, true, 1);
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//            }
//            getResponse().setResult(mapCode(txnResponseOutputData.getResponseCode()));
//        } catch (Exception ex) {
//            if (isRepeatable(repeatCount, ex)) {
//                return postTellerCashWithdrawal(aXRequest, cPRequest, ++repeatCount);
//            } else {
//                getResponse().setResult(processError(ex));
//            }
//        }
//        return getResponse();
//    }
//
//    public AXResponse createCustomer(AXRequest aXRequest) {
//        try {
//            MemberRegistration memberRegistration = aXRequest.getItem(MemberRegistration.class);
//            if (getWorker().isBlank(getClient().queryIdentity(VXController.nationalIdentityId, memberRegistration.getIDNo()).getCustId())) {
//                CNCountry cNCountry = getClient().queryCountry(VXController.countryId);
//                CustomerRequest customerRequest = (CustomerRequest) getBaseRequest(new CustomerRequest(), aXRequest);
//                customerRequest.setAddressCity(VXController.defaultRegion);
//
//                customerRequest.setAddressCountryId(cNCountry.getId());
//                customerRequest.setAddressLine1(getWorker().checkBlank(memberRegistration.getAddress(), cNCountry.getName()));
//                customerRequest.setAddressLine2(VXController.defaultDistrict);
//
//                customerRequest.setAddressLine3(getWorker().checkBlank(memberRegistration.getResidence(), VXController.defaultDistrict));
//                customerRequest.setAddressPropertyTypeId(VXController.propertyTypeId);
//                customerRequest.setAddressState(cNCountry.getName());
//
//                customerRequest.setAddressTypeId(VXController.addressTypeId);
//                customerRequest.setBusinessUnitCodeId(aXRequest.getBranch().getBuId());
//                customerRequest.setContactModeId(VXController.mobileContactModeId);
//
//                customerRequest.setCountryOfBirthId(cNCountry.getId());
//                customerRequest.setCountryOfResidenceId(cNCountry.getId());
//                customerRequest.setCreditRatingAgencyId(VXController.creditRatingAgencyId);
//
//                customerRequest.setLocale(VXController.locale);
//                customerRequest.setPrimaryAddress(Boolean.TRUE);
//                customerRequest.setCustContactModeId(VXController.mobileContactModeId);
//
//                customerRequest.setCustIdentificationId(VXController.nationalIdentityId);
//                customerRequest.setCustIdentificationNumber(memberRegistration.getIDNo());
//                customerRequest.setNationalIdNumber(memberRegistration.getIDNo());
//
//                customerRequest.setFirstName(memberRegistration.getFirstName());
//                customerRequest.setPreferredName(memberRegistration.getFirstName());
//                customerRequest.setCustShortName(memberRegistration.getFirstName());
//
//                customerRequest.setCustomerCategory(VXController.customerCategory);
//                customerRequest.setCountryOfRiskId(cNCountry.getId());
//                customerRequest.setCustomerName(getWorker().cleanSpaces(memberRegistration.getFirstName() + " " + memberRegistration.getSurname()));
//
//                customerRequest.setCustomerType(VXController.customerTypeId);
//                customerRequest.setGender(Objects.equals(memberRegistration.getGender(), "0") ? "M" : "F");
//                customerRequest.setIndustryId(VXController.industryId);
//
//                customerRequest.setLastName(memberRegistration.getSurname());
//                customerRequest.setMiddleName(memberRegistration.getMiddleName());
//                customerRequest.setMainBusinessUnitId(aXRequest.getBranch().getBuId());
//
//                customerRequest.setRiskCode(VXController.riskCode);
//                customerRequest.setTaxGroupId(VXController.taxGroupId);
//                customerRequest.setTaxStatusId(VXController.taxStatusId);
//
//                customerRequest.setStrDateOfBirth(AXConstant.cbsDateFormat.format(getWorker().parseDate(new SimpleDateFormat("yyyy-MM-dd"), getWorker().cleanDateText(memberRegistration.getDateOfBirth(), '-'))));
//                customerRequest.setOrganisationName(VXController.serviceName);
//                customerRequest.setStrFromDate(AXConstant.cbsDateFormat.format(getClient().getLowerDate()));
//
//                customerRequest.setResidentFlag(Boolean.TRUE);
//                customerRequest.setNationalityId(VXController.nationalityId);
//                customerRequest.setMaritalStatus(Objects.equals(memberRegistration.getMaritalStatus(), "Married") ? "M" : "S");
//
//                customerRequest.setTitleId(Objects.equals(customerRequest.getGender(), "M") ? VXController.mrTitleId : VXController.msTitleId);
//                customerRequest.setPrimaryRelationshipOfficerId(VXController.relationshipOfficerId);
//                customerRequest.setVerifiedFlag(Boolean.TRUE);
//
//                customerRequest.setCustomerSegmentId(VXController.customerSegmentId);
//                customerRequest.setOperationCurrencyId(aXRequest.getCurrency().getId());
//                customerRequest.setOpeningReasonId(VXController.openningReasonId);
//
//                customerRequest.setMarketingCampaignId(VXController.marketingCampaignId);
//                CustomerIdentificationInformation nationalIdentity = new CustomerIdentificationInformation();
//                nationalIdentity.setIdentityNumber(memberRegistration.getIDNo());
//                nationalIdentity.setIdentityTypeId(getClient().queryIdentityXref(VXController.customerTypeId, VXController.nationalIdentityId));
//
//                nationalIdentity.setStrIssueDate(AXConstant.cbsDateFormat.format(getWorker().parseDate(new SimpleDateFormat("yyyy-MM-dd"), getWorker().cleanDateText(memberRegistration.getDateOfBirth(), '-'))));
//                nationalIdentity.setStrExpiryDate("01/01/2100");
//                nationalIdentity.setCountryOfIssueId(cNCountry.getId());
//                nationalIdentity.setVerifiedFlag(true);
//
////                customerRequest.setIdentifications(new CustomerIdentificationInformation[]{
////                    nationalIdentity
////                });
//                customerRequest.getIdentifications().add(nationalIdentity);
//
//                ArrayList<CustomerContactInformation> contacts = new ArrayList<>();
//                CustomerContactInformation mobileContact = new CustomerContactInformation();
//                mobileContact.setContactModeTypeId(VXController.mobileContactModeId);
//                mobileContact.setContactModeCategoryCode("MOBPHONE");
//
//                mobileContact.setStatus("A");
//                mobileContact.setContactDetails(memberRegistration.getPhoneNo());
//                contacts.add(mobileContact);
//
//                if (!getWorker().isBlank(memberRegistration.getEmail())) {
//                    CustomerContactInformation emailContact = new CustomerContactInformation();
//                    emailContact.setContactModeTypeId(VXController.emailContactModeId);
//                    emailContact.setContactModeCategoryCode("EMAIL");
//
//                    emailContact.setStatus("A");
//                    emailContact.setContactDetails(memberRegistration.getEmail());
//                    contacts.add(emailContact);
//                    customerRequest.getContacts().add(emailContact);
//                }
////                customerRequest.setContacts(contacts.toArray(new CustomerContactInformation[0]));
//                customerRequest.getContacts().add(mobileContact);
//
//                CustomerImageInformation photo = new CustomerImageInformation();
//                photo.setBinaryImage(DatatypeConverter.parseBase64Binary(getWorker().checkBlank(memberRegistration.getPassportPhoto(), "")));
//                photo.setImageTypeCode("PHO");
//                photo.setImageType("JPEG");
//
//                CustomerImageInformation signature = new CustomerImageInformation();
//                signature.setBinaryImage(DatatypeConverter.parseBase64Binary(getWorker().checkBlank(memberRegistration.getSignature(), "")));
//                signature.setImageTypeCode("SIG");
//                signature.setImageType("JPEG");
//
////                customerRequest.setImages(new CustomerImageInformation[]{
////                    photo, signature
////                });
//                customerRequest.getImages().add(photo);
//                customerRequest.getImages().add(signature);
//                customerRequest.setStatus("A");
//
//                getLog().setCall("crtcustreq", customerRequest);
//                CustomerOutputData customerOutputData = getPort(CustomerWebServiceStub.class).createCustomer(customerRequest);
//                getLog().setCall("crtcustres", customerOutputData);
//                getResponse().setCustomerNumber(customerOutputData.getCustomerNumber());
//            }
//            if (!getWorker().isBlank(getResponse().getCustomerNumber())) {
//                aXRequest.setCustomer(getClient().queryCustomer(getResponse().getCustomerNumber()));
//                getClient().upsertCustomField(APController.languageFieldId, aXRequest.getCustomer().getCustId(), APController.defaultLanguage);
//                aXRequest.setProductId(VXController.defaultProductId);
//
//                if (getWorker().isBlank(aXRequest.getAccount().getAccountNumber()) && createDepositAccount(aXRequest)) {
//                    aXRequest.setAccount(getClient().queryDepositAccount(getResponse().getAccountNumber()));
//                    getClient().blockDebit(aXRequest.getAccount().getAcctId());
//                }
//
//                if (!getWorker().isBlank(aXRequest.getAccount().getAccountNumber())) {
//                    getResponse().setResult(mapCode(AXConstant.XAPI_APPROVED));
//                }
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//
//    public boolean createDepositAccount(AXRequest aXRequest) {
//        try {
//            DepositAccountRequestData depositAccountRequestData = (DepositAccountRequestData) getBaseRequest(new DepositAccountRequestData(), aXRequest);
//            depositAccountRequestData.setAccountTitle(aXRequest.getCustomer().getCustName());
//            depositAccountRequestData.setProductId(aXRequest.getProductId());
//
//            depositAccountRequestData.setCampaignRefId(VXController.marketingCampaignId);
//            depositAccountRequestData.setOpenningReasonId(VXController.openningReasonId);
//            depositAccountRequestData.setPrimaryCustomerNumber(aXRequest.getCustomer().getCustNo());
//
//            depositAccountRequestData.setRelationshipOfficerId(VXController.relationshipOfficerId);
//            depositAccountRequestData.setSourceOfFundId(VXController.sourceOfFundId);
//            depositAccountRequestData.setStrOpeningDate(AXConstant.cbsDateFormat.format(getClient().getLowerDate()));
//            depositAccountRequestData.setRiskClassId(VXController.riskClassId);
//
//            getLog().setCall("acctopenreq", depositAccountRequestData);
//            DepositAccountOutputData depositAccountOutputData = getPort(AccountWebServiceStub.class).createDepositAccount(depositAccountRequestData);
//            getLog().setCall("acctopenres", depositAccountOutputData);
//
//            if (!getWorker().isBlank(depositAccountOutputData.getPrimaryAccountNumber())) {
//                getResponse().setAccountNumber(depositAccountOutputData.getPrimaryAccountNumber());
//                aXRequest.setEventId(VXController.approveAccountEventId);
//                return true;
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return false;
//    }
//
//    public AXResponse enrollChannelUser(AXRequest aXRequest) {
//        try {
//            if (getClient().isAccountPermitted(aXRequest.getSchemeId(), aXRequest.getAccount().getAccountNumber())) {
//                CNScheme scheme = getClient().queryChannelScheme(aXRequest.getSchemeId());
//                CNCustomer cCCustomer = getClient().queryCustomer(aXRequest.getSchemeId(), aXRequest.getAccessCode());
//
//                if (getWorker().isBlank(cCCustomer.getCustNo()) || Objects.equals(aXRequest.getCustomer().getCustId(), cCCustomer.getCustId())) {
//                    CMChannel customerChannel = getClient().queryCustomerChannelId(scheme.getSchemeId(), aXRequest.getCustomer().getCustId());
//                    if (getWorker().isBlank(customerChannel.getCustChannelId())) {
//                        CustomerChannelCreationData customerChannelCreationData = (CustomerChannelCreationData) getBaseRequest(new CustomerChannelCreationData(), aXRequest);
//                        customerChannelCreationData.setAccessCode(aXRequest.getAccessCode());
//                        customerChannelCreationData.setAccountNo(aXRequest.getAccount().getAccountNumber());
//
//                        customerChannelCreationData.setChargeAcctNumber(aXRequest.getAccount().getAccountNumber());
//                        customerChannelCreationData.setCustomerFeeAcctNo(aXRequest.getAccount().getAccountNumber());
//                        customerChannelCreationData.setCustomerNumber(aXRequest.getCustomer().getCustNo());
//
//                        customerChannelCreationData.setChannelSchemeCode(scheme.getSchemeCode());
//                        getLog().setCall("crtcustchnlreq", customerChannelCreationData);
//                        getPort(ChannelAdminWebServiceStub.class).createCustomerChannel(customerChannelCreationData);
//
//                        getPort(ChannelAdminWebServiceStub.class).activateCustomerChannel(customerChannelCreationData);
//                        customerChannel = getClient().queryCustomerChannelId(scheme.getSchemeId(), aXRequest.getCustomer().getCustId());
//                    } else if (!Objects.equals(customerChannel.getStatus(), "A")) {
//                        CustomerChannelCreationData customerChannelCreationData = (CustomerChannelCreationData) getBaseRequest(new CustomerChannelCreationData(), aXRequest);
//                        customerChannelCreationData.setAccessCode(aXRequest.getAccessCode());
//                        customerChannelCreationData.setAccountNo(aXRequest.getAccount().getAccountNumber());
//
//                        customerChannelCreationData.setChargeAcctNumber(aXRequest.getAccount().getAccountNumber());
//                        customerChannelCreationData.setCustomerFeeAcctNo(aXRequest.getAccount().getAccountNumber());
//                        customerChannelCreationData.setCustomerNumber(aXRequest.getCustomer().getCustNo());
//
//                        customerChannelCreationData.setChannelSchemeCode(scheme.getSchemeCode());
//                        getLog().setCall("actvcustchnlreq", customerChannelCreationData);
//                        getPort(ChannelAdminWebServiceStub.class).activateCustomerChannel(customerChannelCreationData);
//                    }
//
//                    if (getWorker().isBlank(getClient().queryChannelAccount(scheme.getChannelId(), aXRequest.getCustomer().getCustId(), aXRequest.getAccount().getAcctId()).getAccountNumber())) {
//                        CustomerChannelAccountRequestData customerChannelAccountRequestData = (CustomerChannelAccountRequestData) getBaseRequest(new CustomerChannelAccountRequestData(), aXRequest);
////                        customerChannelAccountRequestData.setChannelAccounts(new long[]{
////                            aXRequest.getAccount().getAcctId()
////                        });
//                        customerChannelAccountRequestData.getChannelAccounts().add(aXRequest.getAccount().getAcctId());
//
//                        customerChannelAccountRequestData.setCustomerNumber(aXRequest.getCustomer().getCustNo());
//                        customerChannelAccountRequestData.setChannelSchemeCode(scheme.getSchemeCode());
//
//                        getLog().setCall("crtcustchnlacctreq", customerChannelAccountRequestData);
//                        getPort(ChannelAdminWebServiceStub.class).createCustomerChannelAccounts(customerChannelAccountRequestData);
//                    }
//
//                    if (!getWorker().isBlank(cCCustomer.getCustNo()) || AXResult.Success == checkBalance(aXRequest).getResult()) {
//                        if (getWorker().isBlank(cCCustomer.getCustNo())) {
//                            if (processCharge(aXRequest, true, 1)) {
//                                CustomerChannelUserRequestData customerChannelUserRequestData = (CustomerChannelUserRequestData) getBaseRequest(new CustomerChannelUserRequestData(), aXRequest);
//                                customerChannelUserRequestData.setAccessCode(aXRequest.getAccessCode());
//                                customerChannelUserRequestData.setUserAccessCode(aXRequest.getAccessCode());
//
//                                customerChannelUserRequestData.setCurrBUId(aXRequest.getBranch().getBuId());
//                                customerChannelUserRequestData.setPwdResetFlag(true);
//                                customerChannelUserRequestData.setUserStatus("A");
//
//                                customerChannelUserRequestData.setAccessName(aXRequest.getCustomer().getCustName());
//                                customerChannelUserRequestData.setCustomerId(aXRequest.getCustomer().getCustId());
//                                customerChannelUserRequestData.setCustomerChannelId(customerChannel.getCustChannelId());
//
//                                customerChannelUserRequestData.setAccessDurationValue(10L);
//                                customerChannelUserRequestData.setAccessDurationCd("Y");
//                                customerChannelUserRequestData.setChannelSchemeId(scheme.getSchemeId());
//
//                                customerChannelUserRequestData.setChannelUserCustomerId(aXRequest.getCustomer().getCustId());
//                                customerChannelUserRequestData.setUserCategoryCd("PER");
//                                customerChannelUserRequestData.setEffectiveDate(getWorker().formatDate(AXConstant.cbsDateFormat, getClient().getProcessingDate()));
//
//                                customerChannelUserRequestData.setLockedFlag(false);
//                                customerChannelUserRequestData.setAccessInfoDelivery("SMS");
//                                customerChannelUserRequestData.setContactModeId(VXController.mobileContactModeId);
//
//                                customerChannelUserRequestData.setPwdResetFlag(true);
//                                customerChannelUserRequestData.setRequestStatus("A");
//                                Calendar calendar = Calendar.getInstance();
//
//                                calendar.add(Calendar.YEAR, 10);
//                                customerChannelUserRequestData.setExpiryDate(getWorker().formatDate(AXConstant.cbsDateFormat, calendar.getTime()));//getWorker().toDate(getWorker().processingDatePlusYears(10))));
//
//                                getLog().setCall("crtchnluserreq", customerChannelUserRequestData);
//                                CustomerChannelUserOutputData customerChannelUserOutputData = getPort(ChannelAdminWebServiceStub.class).maintainCustomerChannelUser(customerChannelUserRequestData);
//                                getLog().setCall("crtchnluserres", customerChannelUserOutputData, getWorker().prepareDuration(customerChannelUserRequestData.getTransmissionTime()));
//
//                                getClient().moveCustomerWFItem(aXRequest.getCustomer().getCustId(), aXRequest.getBranch().getBuId());
//                                getClient().updateDevice(aXRequest.getSchemeId(), aXRequest.getAccessCode(), aXRequest.getTerminalId());
//                                getResponse().setData(aXRequest.getCustomer().getCustNo() + "|" + aXRequest.getCustomer().getCustName().trim());
//                                getResponse().setResult(AXResult.Success);
//                            }
//                        } else {
//                            getResponse().setData(aXRequest.getCustomer().getCustNo() + "|" + aXRequest.getCustomer().getCustName().trim());
//                            getResponse().setResult(AXResult.Success);
//                        }
//                    } else {
//                        getResponse().setResult(AXResult.Insufficient_Funds);
//                    }
//                } else {
//                    getResponse().setResult(AXResult.Invalid_Pan);
//                }
//            } else {
//                getResponse().setResult(AXResult.Invalid_Account);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
//
//    public AXResponse createCase(AXRequest aXRequest) {
//        try {
//            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, true, 1)) {
//                CaseRequestData caseRequestData = (CaseRequestData) getBaseRequest(new CaseRequestData(), aXRequest);
//                caseRequestData.setCaseDetail(aXRequest.getNarration());
//
//                caseRequestData.setSubject(getWorker().capitalize(aXRequest.getType().name()) + " Request");
//                caseRequestData.setCustomerNumber(aXRequest.getCustomer().getCustNo());
//                caseRequestData.setPriority("1");
//
////                caseRequestData.setFromDate(Calendar.getInstance());
////                caseRequestData.setDueDate(Calendar.getInstance());
//                caseRequestData.setFromDate(createXmlGregorianCalendar(getClient().getProcessingDate()));
//                caseRequestData.setDueDate(createXmlGregorianCalendar(getClient().getProcessingDate()));
//
//                caseRequestData.setCaseTypeId(aXRequest.getCaseTypeId());
//
//                caseRequestData.setStatus("N");
//                getLog().setCall(getCallTag(aXRequest, true), caseRequestData);
//                CaseOutputData caseResponse = getPort(CasemgmtWebServiceStub.class).createCase(caseRequestData);
//
//                getLog().setCall(getCallTag(aXRequest, false), caseResponse, getWorker().prepareDuration(caseRequestData.getTransmissionTime()));
//                getClient().moveCustomerWFItem(aXRequest.getCustomer().getCustId(), aXRequest.getBranch().getBuId());
//                setNewBalance(aXRequest).setResult(AXResult.Success);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//        return getResponse();
//    }
    public AXResponse requestForStatement(AXRequest aXRequest) {
        try {
            if (AXResult.Success == checkBalance(aXRequest).getResult() && processCharge(aXRequest, false, 1)) {
                getClient().upsertCustomField(ESController.startDateFieldId, aXRequest.getAccount().getAcctId(), getWorker().formatDate(AXConstant.cbsDateFormat, aXRequest.getStartDate()));
                getClient().upsertCustomField(ESController.endDateFieldId, aXRequest.getAccount().getAcctId(), getWorker().formatDate(AXConstant.cbsDateFormat, aXRequest.getEndDate()));
                getClient().upsertCustomField(ESController.nowFieldId, aXRequest.getAccount().getAcctId(), String.valueOf(ESController.yesValueId));
                setNewBalance(aXRequest).setResult(AXResult.Success);
            }
        } catch (Exception ex) {
            getResponse().setResult(processError(ex));
        }
        return getResponse();
    }

    public boolean processCharge(AXRequest aXRequest, boolean force, Integer repeatCount) {
        boolean chargable = false;
        FundsTransferRequestData fundsTransferRequestData = null;

        try {
            if (chargable = !aXRequest.getCharge().isWaive()
                    && (BigDecimal.ZERO.compareTo(aXRequest.getCharge().getChargeAmount()) < 0)) {
                fundsTransferRequestData = new FundsTransferRequestData();
                XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

                fundsTransferRequestData.setChannelId(requestData.getChannelId());
                fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
                fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
                fundsTransferRequestData.setTxnReference(requestData.getReference());

                fundsTransferRequestData.setAccountNo(aXRequest.getCharge().getChargeAccount().getAccountNumber());
                fundsTransferRequestData.setContraAcctNo(aXRequest.getCharge().getSplitList().isEmpty() ? aXRequest.getCharge().getIncomeLedger() : aXRequest.getCharge().getChannelLedger());

                fundsTransferRequestData.setNarrative((aXRequest.isReversal() && !aXRequest.getCharge().getNarration().startsWith("REV~") ? "REV~" : "") + aXRequest.getCharge().getNarration());
                fundsTransferRequestData.setTxnAmount(aXRequest.getCharge().getChargeAmount());
                fundsTransferRequestData.setCurrencyCode(aXRequest.getCharge().getChargeAccount().getCurrency().getCode());
                fundsTransferRequestData.setTxnReference(aXRequest.getReference());

                getLog().setCall(aXRequest.isReversal() ? "revtxnchrgreq" : "txnchrgreq", requestData, repeatCount > 1);
                FundsTransferOutputData fundsTransferOutputData = aXRequest.isReversal()
                        ? getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData)
                        : getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData);
                getLog().setCall(aXRequest.isReversal() ? "revtxnchrgres" : "txnchrgres", fundsTransferOutputData, getWorker().prepareDuration(requestData.getTransmissionTime()));

                getResponse().setResult(mapCode(fundsTransferOutputData.getResponseCode()));
                if (Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode())) {
                    getResponse().setChargeId(fundsTransferOutputData.getTxnJournalId());
                    processSplit(aXRequest);
                    return true;
                }
            }
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, ex)) {
                return processCharge(aXRequest, force, ++repeatCount);
            } else if (isRepeatable(repeatCount, repeatCount + 1, ex)) {
                force = true;
            } else {
                getResponse().setResult(processError(ex));
            }
        }

        if (force && chargable && fundsTransferRequestData != null) {
            AXSplit aXSplit = new AXSplit();
            aXSplit.setTxnRef(fundsTransferRequestData.getTxnReference());
            aXSplit.setModule(aXRequest.getModule());

            aXSplit.setAmount(fundsTransferRequestData.getTxnAmount());
            aXSplit.setCurrency(fundsTransferRequestData.getCurrencyCode());
            aXSplit.setDescription(fundsTransferRequestData.getNarrative());

            aXSplit.setDebitAccount(fundsTransferRequestData.getAccountNo());
            aXSplit.setCreditAccount(fundsTransferRequestData.getContraAcctNo());
            aXSplit.setReversal(aXRequest.isReversal() ? "Y" : "N");

            aXSplit.setLump("N");
            aXSplit.setField("C");
            aXSplit.setStatus("P");

            if (getClient().saveSplit(aXSplit)) {
                processSplit(aXRequest);
                return true;
            }
        }
        return !chargable;
    }

    public void processSplit(AXRequest aXRequest) {
        try {
            aXRequest.getCharge().getSplitList().stream().filter((split) -> (BigDecimal.ZERO.compareTo(split.getAmount()) < 0)).forEach((split)
                    -> {
                AXSplit aXSplit = new AXSplit();
                aXSplit.setTxnRef(aXRequest.getReference());
                aXSplit.setModule(aXRequest.getModule());

                aXSplit.setAmount(split.getAmount());
                aXSplit.setCurrency(aXRequest.getCurrency().getCode());
                aXSplit.setDescription((aXRequest.isReversal() && !split.getDescription().startsWith("REV~") ? "REV~" : "") + split.getDescription());

                aXSplit.setDebitAccount(aXRequest.isReversal() ? split.getAccount() : aXRequest.getCharge().getChannelLedger());
                aXSplit.setCreditAccount(aXRequest.isReversal() ? aXRequest.getCharge().getChannelLedger() : split.getAccount());
                aXSplit.setReversal(aXRequest.isReversal() ? "Y" : "N");

                try {
                    if (getWorker().isLedger(split.getAccount())) {
                        aXSplit.setStatus(APController.isEndOfYear() || getWorker().isYes(APController.splitOnFly) ? (splitLedgerToLedger(aXRequest, split, 1) ? "S" : "P") : "P");
                    } else {
                        aXSplit.setStatus(APController.isEndOfYear() || getWorker().isYes(APController.splitOnFly) ? (splitLedgerToDeposit(aXRequest, split, 1) ? "S" : "P") : "P");
                    }
                } catch (Exception ex) {
                    getLog().logError(ex);
                }
                getClient().saveSplit(aXSplit);
            });
        } catch (Exception ex) {
            getLog().logError(ex);
        }
    }

    private boolean splitDepositToLedger(AXRequest aXRequest, TCSplit split, Integer repeatCount) {
        try {
            FundsTransferRequestData fundsTransferRequestData = new FundsTransferRequestData();
            XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

            fundsTransferRequestData.setChannelId(requestData.getChannelId());
            fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
            fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
            fundsTransferRequestData.setTxnReference(requestData.getReference());

            fundsTransferRequestData.setAccountNo(split.getAccount());
            fundsTransferRequestData.setContraAcctNo(aXRequest.getCharge().getChannelLedger());

            fundsTransferRequestData.setNarrative(split.getDescription());
            fundsTransferRequestData.setTxnAmount(split.getAmount());
            fundsTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
            fundsTransferRequestData.setTxnReference(aXRequest.getReference() + split.getReference());

            getLog().setCall(aXRequest.isReversal() ? "revspltreq" : "spltreq", requestData, repeatCount > 1);
            FundsTransferOutputData fundsTransferOutputData = aXRequest.isReversal()
                    ? getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData)
                    : getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData);
            getLog().setCall(aXRequest.isReversal() ? "revspltres" : "spltres", fundsTransferOutputData,
                    getWorker().prepareDuration(requestData.getTransmissionTime()));
            return Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode());
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, 3, ex)) {
                return splitDepositToLedger(aXRequest, split, ++repeatCount);
            } else {
                getLog().logError(ex);
            }
        }
        return false;
    }

    private boolean splitLedgerToDeposit(AXRequest aXRequest, TCSplit split, Integer repeatCount) {
        try {
            FundsTransferRequestData fundsTransferRequestData = new FundsTransferRequestData();
            XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

            fundsTransferRequestData.setChannelId(requestData.getChannelId());
            fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
            fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
            fundsTransferRequestData.setTxnReference(requestData.getReference());

            fundsTransferRequestData.setAccountNo(split.getAccount());
            fundsTransferRequestData.setContraAcctNo(aXRequest.getCharge().getChannelLedger());

            fundsTransferRequestData.setNarrative(split.getDescription());
            fundsTransferRequestData.setTxnAmount(split.getAmount());
            fundsTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
            fundsTransferRequestData.setTxnReference(aXRequest.getReference() + split.getReference());

            getLog().setCall(aXRequest.isReversal() ? "revspltreq" : "spltreq", requestData, repeatCount > 1);
            FundsTransferOutputData fundsTransferOutputData = aXRequest.isReversal()
                    ? getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData)
                    : getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData);
            getLog().setCall(aXRequest.isReversal() ? "revspltres" : "spltres", fundsTransferOutputData, getWorker().prepareDuration(requestData.getTransmissionTime()));
            return Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode());
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, 3, ex)) {
                return splitLedgerToDeposit(aXRequest, split, ++repeatCount);
            } else {
                getLog().logError(ex);
            }
        }
        return false;
    }

    private boolean splitLedgerToLedger(AXRequest aXRequest, TCSplit split, Integer repeatCount) {
        try {
            GlTransferRequestData glTransferRequestData = new GlTransferRequestData();
            XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

            glTransferRequestData.setChannelId(requestData.getChannelId());
            glTransferRequestData.setChannelCode(requestData.getChannelCode());
            glTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
            glTransferRequestData.setTxnReference(requestData.getReference());

            glTransferRequestData.setAccountNo(aXRequest.isReversal() ? split.getAccount() : aXRequest.getCharge().getChannelLedger());
            glTransferRequestData.setContraAcctNo(aXRequest.isReversal() ? aXRequest.getCharge().getChannelLedger() : split.getAccount());

            glTransferRequestData.setTxnAmount(split.getAmount());
//            glTransferRequestData.setTransactionAmount(split.getAmount());

            glTransferRequestData.setCurrencyCode(aXRequest.getCurrency().getCode());
            glTransferRequestData.setNarrative(split.getDescription());

            getLog().setCall(aXRequest.isReversal() ? "revspltreq" : "spltreq", glTransferRequestData, repeatCount > 1);
            GlTransferOutputData glTransferOutputData = getPort(TransferService.class).postGLToGLTransfer(glTransferRequestData);
            getLog().setCall(aXRequest.isReversal() ? "revspltres" : "spltres", glTransferOutputData, getWorker().prepareDuration(glTransferRequestData.getTransmissionTime()));
            return Objects.equals(AXConstant.XAPI_APPROVED, glTransferOutputData.getResponseCode());
        } catch (Exception ex) {
            if (isRepeatable(repeatCount, 3, ex)) {
                return splitLedgerToLedger(aXRequest, split, ++repeatCount);
            } else {
                getLog().logError(ex);
            }
        }
        return false;
    }

    public AXResponse postSplit(AXRequest aXRequest, AXSplit split) {
        try {
            boolean debitLedger = getWorker().isLedger(split.getDebitAccount());
            boolean creditLedger = getWorker().isLedger(split.getCreditAccount());
            if (debitLedger && creditLedger) {
                GlTransferRequestData glTransferRequestData = new GlTransferRequestData();
                XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

                glTransferRequestData.setChannelId(requestData.getChannelId());
                glTransferRequestData.setChannelCode(requestData.getChannelCode());
                glTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
                glTransferRequestData.setTxnReference(requestData.getReference());

                glTransferRequestData.setAccountNo(split.getDebitAccount());
                glTransferRequestData.setContraAcctNo(split.getCreditAccount());
                glTransferRequestData.setTxnAmount(split.getAmount());
//                glTransferRequestData.setTransactionAmount(split.getAmount());

                glTransferRequestData.setContraAcctNo(split.getCurrency());
                glTransferRequestData.setNarrative(split.getDescription());

                getLog().setCall(aXRequest.isReversal() ? "revspltreq" : "spltreq", glTransferRequestData);
                GlTransferOutputData glTransferOutputData = getPort(TransferService.class).postGLToGLTransfer(glTransferRequestData);
                getLog().setCall(aXRequest.isReversal() ? "revspltres" : "spltres",
                        glTransferOutputData, getWorker().prepareDuration(glTransferRequestData.getTransmissionTime()));

                getResponse().setResult(mapCode(glTransferOutputData.getResponseCode()));
                if (Objects.equals(AXConstant.XAPI_APPROVED, glTransferOutputData.getResponseCode())) {
                    getResponse().setTxnId(glTransferOutputData.getTxnJournalId());
                }
            } else {
                FundsTransferRequestData fundsTransferRequestData = new FundsTransferRequestData();
                XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

                fundsTransferRequestData.setChannelId(requestData.getChannelId());
                fundsTransferRequestData.setChannelCode(requestData.getChannelCode());
                fundsTransferRequestData.setTransmissionTime(requestData.getTransmissionTime());
                fundsTransferRequestData.setTxnReference(requestData.getReference());

                fundsTransferRequestData.setAccountNo(debitLedger ? split.getCreditAccount() : split.getDebitAccount());
                fundsTransferRequestData.setContraAcctNo(debitLedger ? split.getDebitAccount() : split.getCreditAccount());
                fundsTransferRequestData.setTxnReference(checkReference(aXRequest.getReference()));

                fundsTransferRequestData.setNarrative(split.getDescription());
                fundsTransferRequestData.setTxnAmount(split.getAmount());
                fundsTransferRequestData.setCurrencyCode(split.getCurrency());

                getLog().setCall(Objects.equals(split.getReversal(), "Y") ? "revspltreq" : "spltreq", requestData);
                FundsTransferOutputData fundsTransferOutputData = debitLedger
                        ? getPort(TransferService.class).postGLToDepositAccountTransfer(fundsTransferRequestData)
                        : getPort(TransferService.class).postDepositToGLAccountTransfer(fundsTransferRequestData);
                getLog().setCall(Objects.equals(split.getReversal(), "Y") ? "revspltres" : "spltres", fundsTransferOutputData, getWorker().prepareDuration(requestData.getTransmissionTime()));

                getResponse().setResult(mapCode(fundsTransferOutputData.getResponseCode()));
                if (Objects.equals(AXConstant.XAPI_APPROVED, fundsTransferOutputData.getResponseCode())) {
                    getResponse().setTxnId(fundsTransferOutputData.getTxnJournalId());
                }
            }
        } catch (Exception ex) {
            getLog().logError(ex);
        }
        return getResponse();
    }

    public AXResponse setNewBalance(AXRequest aXRequest, boolean validating) {
        try {
            if (aXRequest.isSetBalance() || validating) {
                CNAccount account = (validating && aXRequest.isInverted()) ? aXRequest.getContra() : aXRequest.getAccount();
                account = account.isLoan() && validating ? getClient().queryRepaymentAccount(account.getAccountNumber()) : account;

                if (account.isLoan()) {
                    getResponse().setBalance(getBox().getClient().queryLoanBalance(account.getAccountNumber()));
                } else {
                    AccountBalanceRequestData accountBalanceRequestData = new AccountBalanceRequestData();
                    XAPIRequestBaseObject requestData = getBaseRequest(new XAPIRequestBaseObject(), aXRequest);

                    accountBalanceRequestData.setChannelId(requestData.getChannelId());
                    accountBalanceRequestData.setChannelCode(requestData.getChannelCode());
                    accountBalanceRequestData.setTransmissionTime(requestData.getTransmissionTime());
                    accountBalanceRequestData.setAccountNo(account.getAccountNumber());
                    getLog().setCall("acctbalreq", accountBalanceRequestData);

                    AccountBalanceResponseData accountBalanceResponseData = getPort(AccountService.class).findAccountBalance(accountBalanceRequestData);
                    getLog().setCall("acctbalres", accountBalanceResponseData, getWorker().prepareDuration(accountBalanceRequestData.getTransmissionTime()));
                    getResponse().setBalance(accountBalanceResponseData.getAvailableBalance());
                }
            }
            if (validating) {
                getResponse().setResult(AXResult.Success);
            }
        } catch (Exception ex) {
            if (validating) {
                getResponse().setResult(processError(ex));
            } else {
                getLog().logError(ex);
            }
        }
        return getResponse();
    }

//    private void approveCreditApplication(AXRequest aXRequest) {
//        try {
//            Long workItemId = getClient().queryWorkflowItemId(aXRequest.getCustomer().getCustId());
//            if (workItemId != 0L) {
//                WFViewRequestData wfViewRequestData = (WFViewRequestData) getBaseRequest(new WFViewRequestData(), aXRequest);
//                wfViewRequestData.setWorkItemId(workItemId);
//                wfViewRequestData.setEventId(aXRequest.getEventId());
//
//                getLog().setCall("apprvwfitemreq", wfViewRequestData);
//                getPort(WorkflowWebServiceStub.class).saveCreditApplData(wfViewRequestData);
//            }
//        } catch (Exception ex) {
//            getResponse().setResult(processError(ex));
//        }
//    }
//    public boolean approveWorkFlowItem(AXRequest aXRequest) {
//        try {
//            Long workItemId = getClient().queryWorkflowItemId(aXRequest.getCustomer().getCustId());
//            if (workItemId != 0L) {
//                WFViewRequestData wFViewRequestData = (WFViewRequestData) getBaseRequest(new WFViewRequestData(), aXRequest);
//                wFViewRequestData.setEventId(aXRequest.getEventId());
//                wFViewRequestData.setWorkItemId(workItemId);
//
//                getLog().setCall("apprvwfitemreq", wFViewRequestData);
//                getPort(WorkflowWebServiceStub.class).saveData(wFViewRequestData);
//            }
//            return true;
//        } catch (Exception ex) {
//            processError(ex);
//        }
//        return false;
//    }
    public AXResponse checkBalance(AXRequest aXRequest) {
        getResponse().setResult(AXResult.Success);
        BigDecimal netAmount = aXRequest.getAmount().add(aXRequest.getCharge().getChargeAmount());
        if (!aXRequest.isReversal() && BigDecimal.ZERO.compareTo(netAmount) < 0) {
            if (setNewBalance(aXRequest, true).getResult() == AXResult.Success && (BigDecimal.ZERO.compareTo(getResponse().getBalance()) >= 0 || getResponse().getBalance().compareTo(netAmount) < 0)) {
                getResponse().setResult(AXResult.Insufficient_Funds);
            }
        }
        return getResponse();
    }

    public AXResponse queryTxnResponse(AXRequest aXRequest) {
        try {
            getResponse().setResult(mapCode(getClient().queryTxnResponse(aXRequest.getChannel().getId(), aXRequest.getReference())));
        } catch (Exception ex) {
            getResponse().setResult(processError(ex));
        }
        return getResponse();
    }

    public XMLGregorianCalendar createXmlGregorianCalendar(Date date) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private AXResult processError(Exception ex) {
        getLog().logError(ex);
        return mapCode(getWorker().extractError(ex));
    }

    private AXResult mapCode(String code) {
        getResponse().setXapiCode(code);
        getResponse().setMessage(APController.getCodes().getProperty(String.valueOf(code),
                AXResult.System_Error.name()));
        AXResult result = !Objects.equals(AXConstant.XAPI_APPROVED, code)
                ? AXResult.valueOf(APController.getResults().getProperty(String.valueOf(code),
                        AXResult.Failed.name())) : AXResult.Success;
        return result == AXResult.Success
                && !Objects.equals(AXConstant.XAPI_APPROVED, code)
                ? AXResult.Failed : result;
    }

    public Long findTxnId(XAPIRequestBaseObject requestBaseObject, String retrievalReference) {
        Long txnId = getWorker().extractXmlValue(retrievalReference, "TxnId", Long.class);
        return getWorker().isBlank(txnId) ? getClient().queryTxnId(requestBaseObject.getChannelId(), requestBaseObject.getReference()) : txnId;
    }

    public AXResponse setNewBalance(AXRequest aXRequest) {
        return setNewBalance(aXRequest, false);
    }

    private boolean isRepeatable(Integer repeatCount, Integer maxAttempts, Exception ex) {
        return repeatCount < maxAttempts && String.valueOf(ex.getMessage()).contains("OptimisticConcurrencyException");
    }

    private boolean isRepeatable(Integer repeatCount, Exception ex) {
        return isRepeatable(repeatCount, 10, ex);
    }

    private String getCallTag(AXRequest aXRequest, boolean request) {
        return (aXRequest.isReversal() ? "rev" : "") + aXRequest.getType().name().toLowerCase() + (request ? "req" : "res");
    }

    private String checkReference(String reference) {
        return String.valueOf(reference).length() > 30 ? reference.substring(reference.length() - 30) : reference;
    }

    /**
     * @return the response
     */
    public AXResponse getResponse() {
        return response;
    }

    /**
     * @param tXResponse the response to set
     */
    public void setResponse(AXResponse tXResponse) {
        this.response = tXResponse;
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
     * @return the dClient
     */
    public DBPClient getClient() {
        return getBox().getClient();
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
