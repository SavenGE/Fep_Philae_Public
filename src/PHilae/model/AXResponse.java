/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.enu.AXResult;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Pecherk
 */
public final class AXResponse implements Serializable {

    private Long txnId;
    private String data;
    private String message;

    private Long chargeId;
    private String reference;
    private AXResult result = AXResult.Success;
    private Long applId;

    private BigDecimal balance;
    private String accountNumber;
    private String customerNumber;
    private String xapiCode;

    public AXResponse() {
    }

    public AXResponse(AXResult result) {
        setResult(result);
    }

    /**
     * @return the txnId
     */
    public Long getTxnId() {
        return txnId;
    }

    /**
     * @param txnId the txnId to set
     */
    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the chargeId
     */
    public Long getChargeId() {
        return chargeId;
    }

    /**
     * @param chargeId the chargeId to set
     */
    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    /**
     * @return the result
     */
    public AXResult getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(AXResult result) {
        this.result = result;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    /**
     * @return the customerNumber
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * @param customerNumber the customerNumber to set
     */
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * @return the xapiCode
     */
    public String getXapiCode() {
        return xapiCode;
    }

    /**
     * @param xapiCode the xapiCode to set
     */
    public void setXapiCode(String xapiCode) {
        this.xapiCode = xapiCode;
    }

    /**
     * @return the applId
     */
    public Long getApplId() {
        return applId;
    }

    /**
     * @param applId the applId to set
     */
    public void setApplId(Long applId) {
        this.applId = applId;
    }
}
