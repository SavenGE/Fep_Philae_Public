/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.APController;
import PHilae.enu.TXType;
import com.sun.net.httpserver.HttpExchange;
import com.sun.xml.ws.developer.JAXWSProperties;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Pecherk
 */
@WebService(serviceName = "MobileBanking", portName = "MobileBanking_Port", endpointInterface = "schemas.dynamics.microsoft.codeunit.mobilebanking.MobileBankingPort", targetNamespace = "urn:microsoft-dynamics-schemas/codeunit/MobileBanking")
public class VXService {

    @Resource
    WebServiceContext context;

    public void spotcash(Holder<String> requestId, Holder<String> phoneNo, Holder<Integer> transactionType, Holder<String> amount, Holder<String> trnxCharges, Holder<String> accountNumber, Holder<String> crAccount, Holder<String> status, Holder<String> fKey, Holder<String> balance, Holder<String> message, Holder<String> response, Holder<String> responseMessage, Holder<String> customerType, Holder<String> description, Holder<String> startDate, Holder<String> endDate, Holder<String> startTime, Holder<String> endTime, Holder<String> emailaddress) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), mapTxn(transactionType.value),
                requestId.value, phoneNo.value, accountNumber.value, getCallId(),
                method.getName(), method.getParameterTypes(), requestId, phoneNo,
                transactionType, amount, trnxCharges, accountNumber, crAccount,
                status, fKey, balance, message, response, responseMessage,
                customerType, description, startDate, endDate, startTime, endTime,
                emailaddress)).execute(response, responseMessage, message);
    }

    public void memberRegistration(Holder<String> iDNo, Holder<String> firstName, Holder<String> middleName, Holder<String> branchCode, Holder<String> surname, Holder<String> pinNo, Holder<String> address, Holder<String> gender, Holder<String> occupation, Holder<String> phoneNo, Holder<String> email, Holder<String> passportPhoto, Holder<String> frontID, Holder<String> backID, Holder<String> signature, Holder<String> dateOfBirth, Holder<String> maritalStatus, Holder<String> applicationNo, Holder<String> hudumaNumber, Holder<String> residence, Holder<String> alternativeContact, Holder<String> refererPhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.Registration, null,
                phoneNo.value, "", getCallId(), method.getName(),
                method.getParameterTypes(), iDNo, firstName, middleName,
                branchCode, surname, pinNo, address, gender, occupation, phoneNo,
                email, passportPhoto, frontID, backID, signature, dateOfBirth,
                maritalStatus, applicationNo, hudumaNumber, residence,
                alternativeContact, refererPhoneNo, responseCode, responseMessage,
                errorMessage, returnValue)).execute(responseCode, responseMessage,
                errorMessage);
    }

    public void getMemberAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.MemberAccounts, null,
                mobilePhoneNo.value, "", getCallId(), method.getName(),
                method.getParameterTypes(), mobilePhoneNo, responseCode,
                responseMessage, errorMessage, returnValue)).execute(responseCode,
                responseMessage, errorMessage);
    }

    public void validateAccountDetails(Holder<String> accountNumber, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.AccountDetails, null,
                null, accountNumber.value, getCallId(), method.getName(),
                method.getParameterTypes(), accountNumber, responseCode,
                responseMessage, errorMessage)).execute(responseCode,
                responseMessage, errorMessage);
    }

    public void getMemberImage(Holder<String> idNumber, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.MemberImage, null, null, "", getCallId(), method.getName(), method.getParameterTypes(), idNumber, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getLoanAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.LoanAccounts, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getSavingsAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.SavingsAccounts, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getShareCapitalAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.CapitalAccounts, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getNWDAccounts(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<Integer> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.NWDAccounts, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void geteLoanTypes(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.LoanTypes, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getStatementTransactions(Holder<String> accountNumber, Holder<javax.xml.datatype.XMLGregorianCalendar> startDate, Holder<javax.xml.datatype.XMLGregorianCalendar> endDate, Holder<javax.xml.datatype.XMLGregorianCalendar> startTime, Holder<javax.xml.datatype.XMLGregorianCalendar> endTime, Holder<String> emailaddress, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<String> returnValue) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.StatementTransactions, null, null, "", getCallId(), method.getName(), method.getParameterTypes(), accountNumber, startDate, endDate, startTime, endTime, emailaddress, responseCode, responseMessage, errorMessage, returnValue)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getMemberEligibility(Holder<String> phoneNo, Holder<String> eLoanCode, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.MemberEligibility, null, phoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), phoneNo, eLoanCode, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    public void applyLoan(Holder<String> requestid, Holder<String> phoneNo, Holder<String> eLoanCode, Holder<java.math.BigDecimal> amount, Holder<java.math.BigDecimal> installments, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.ApplyLoan, requestid.value, phoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), requestid, phoneNo, eLoanCode, amount, installments, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    public void getBranchCodes(Holder<String> mobilePhoneNo, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.BranchCodes, null, mobilePhoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), mobilePhoneNo, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    public void referAFriend(Holder<String> firstName, Holder<String> lastName, Holder<String> phoneNo, Holder<String> gender, Holder<String> location, Holder<String> introducerPhone, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.ReferAFriend, null, introducerPhone.value, "", getCallId(), method.getName(), method.getParameterTypes(), firstName, lastName, phoneNo, gender, location, introducerPhone, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    public void spotcashRegistration(Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage, Holder<String> newMembersPayload) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.UsersQuery, null, null, "", getCallId(), method.getName(), method.getParameterTypes(), responseCode, responseMessage, errorMessage, newMembersPayload)).execute(responseCode, responseMessage, responseMessage);
    }

    public void updateMPESADetails(javax.xml.ws.Holder<java.lang.String> spotCashID, javax.xml.ws.Holder<java.lang.String> mPESAID, javax.xml.ws.Holder<java.lang.String> phoneNo, javax.xml.ws.Holder<java.lang.String> responseCode, javax.xml.ws.Holder<java.lang.String> responseMessage, javax.xml.ws.Holder<java.lang.String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.MpesaAdvice, spotCashID.value, phoneNo.value, "", getCallId(), method.getName(), method.getParameterTypes(), spotCashID, mPESAID, phoneNo, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, responseMessage);
    }

    public void processReversal(Holder<String> requestID, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getClient(), TXType.Reversal, requestID.value, null, "", getCallId(), method.getName(), method.getParameterTypes(), requestID, responseCode, responseMessage, errorMessage)).execute(responseCode, responseMessage, errorMessage);
    }

    private TXType mapTxn(Integer vendorId) {
        for (TXType txn : TXType.values()) {
            for (Integer vid : txn.getVids()) {
                if (Objects.equals(vendorId, vid)) {
                    return txn;
                }
            }
        }
        return TXType.Unknown;
    }

    public String getCallId() {
        List<String> headerField = (List<String>) ((Map) context.getMessageContext().get(MessageContext.HTTP_RESPONSE_HEADERS)).get("callId");
        return (headerField != null && !headerField.isEmpty()) ? headerField.get(0) : APController.generateKey();
    }

    private String getClient() {
        return ((HttpExchange) context.getMessageContext().get(JAXWSProperties.HTTP_EXCHANGE)).getRemoteAddress().getHostName();
    }
}
