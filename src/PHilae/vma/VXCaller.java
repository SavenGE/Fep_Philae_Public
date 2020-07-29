/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.enu.TXType;
import java.io.Serializable;

/**
 *
 * @author Pecherk
 */
public final class VXCaller implements Serializable {

    private TXType txn;
    private String client;
    private String reqId;
    private String msisdn;
    private String accountNumber;

    private String callId;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] paramValues;

    public VXCaller(String client, TXType txnType, String reqId, String msisdn,
            String accountNumber, String callId, String methodName,
            Class<?>[] paramTypes, Object... paramValues) {
        setTxn(txnType);
        setReqId(reqId);
        setClient(client);
        setMsisdn(msisdn);
        setAccountNumber(accountNumber);

        setCallId(callId);
        setMethodName(methodName);
        setParamTypes(paramTypes);
        setParamValues(paramValues);
    }

    /**
     * @return the txn
     */
    public TXType getTxn() {
        return txn;
    }

    /**
     * @param txn the txn to set
     */
    public void setTxn(TXType txn) {
        this.txn = txn;
    }

    /**
     * @return the reqId
     */
    public String getReqId() {
        return reqId;
    }

    /**
     * @param reqId the reqId to set
     */
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    /**
     * @return the msisdn
     */
    public String getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the paramTypes
     */
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    /**
     * @param paramTypes the paramTypes to set
     */
    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    /**
     * @return the paramValues
     */
    public Object[] getParamValues() {
        return paramValues;
    }

    /**
     * @param paramValues the paramValues to set
     */
    public void setParamValues(Object... paramValues) {
        this.paramValues = paramValues;
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
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
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
