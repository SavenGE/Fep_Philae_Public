/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import PHilae.acx.AXIgnore;

/**
 *
 * @author Pecherk
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class MXMessage implements Serializable {

    @XmlElement(name = "recId")
    private Long recId = System.currentTimeMillis();
    @XmlElement(name = "createDt")
    private Date createDt;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "txnId")
    private String txnId;
    @XmlElement(name = "custId")
    private long custId;
    @XmlElement(name = "custName")
    private String custName;
    @XmlElement(name = "acctNo")
    private String acctNo;
    @XmlElement(name = "contra")
    private String contra;
    @XmlElement(name = "chargeAcct")
    private String chargeAcct;
    @XmlElement(name = "txnDate")
    private Date txnDate;
    @XmlElement(name = "currency")
    private String currency;
    @XmlElement(name = "txnAmt")
    private BigDecimal txnAmt = BigDecimal.ZERO;
    @XmlElement(name = "txnChg")
    private BigDecimal txnChg = BigDecimal.ZERO;
    @XmlElement(name = "chargeAmount")
    private BigDecimal chargeAmount = BigDecimal.ZERO;
    @XmlElement(name = "chargeId")
    private Long chargeId;
    @XmlElement(name = "txnDesc")
    private String txnDesc;
    @XmlElement(name = "schemeId")
    private long schemeId;
    @XmlElement(name = "accessCode")
    private String accessCode;
    @XmlElement(name = "originator")
    private String originator;
    @XmlElement(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;
    @XmlElement(name = "msisdn")
    private String msisdn;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "status")
    private String status;
    @XmlElement(name = "result")
    private String result;
    @XmlElement(name = "responseid")
    private String responseId;
    @XmlTransient
    private String password;
    @XmlTransient
    private LNDetail lNDetail = new LNDetail();

    /**
     * @return the recId
     */
    public Long getRecId() {
        return recId;
    }

    /**
     * @param recId the recId to set
     */
    public void setRecId(Long recId) {
        this.recId = recId;
    }

    /**
     * @return the createDt
     */
    public Date getCreateDt() {
        return createDt;
    }

    /**
     * @param createDt the createDt to set
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the txnId
     */
    public String getTxnId() {
        return txnId;
    }

    /**
     * @param txnId the txnId to set
     */
    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    /**
     * @return the custId
     */
    public long getCustId() {
        return custId;
    }

    /**
     * @param custId the custId to set
     */
    public void setCustId(long custId) {
        this.custId = custId;
    }

    /**
     * @return the custName
     */
    public String getCustName() {
        return custName;
    }

    /**
     * @param custName the custName to set
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * @return the acctNo
     */
    public String getAcctNo() {
        return acctNo;
    }

    /**
     * @param acctNo the acctNo to set
     */
    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    /**
     * @return the chargeAcct
     */
    public String getChargeAcct() {
        return chargeAcct;
    }

    /**
     * @param chargeAcct the chargeAcct to set
     */
    public void setChargeAcct(String chargeAcct) {
        this.chargeAcct = chargeAcct;
    }

    /**
     * @return the txnDate
     */
    public Date getTxnDate() {
        return txnDate == null ? new Date() : txnDate;
    }

    /**
     * @param txnDate the txnDate to set
     */
    public void setTxnDate(Date txnDate) {
        this.txnDate = txnDate;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the txnAmt
     */
    public BigDecimal getTxnAmt() {
        return txnAmt == null ? BigDecimal.ZERO : txnAmt;
    }

    /**
     * @param txnAmt the txnAmt to set
     */
    public void setTxnAmt(BigDecimal txnAmt) {
        this.txnAmt = txnAmt;
    }

    /**
     * @return the chargeAmount
     */
    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the txnDesc
     */
    public String getTxnDesc() {
        return txnDesc;
    }

    /**
     * @param txnDesc the txnDesc to set
     */
    public void setTxnDesc(String txnDesc) {
        this.txnDesc = txnDesc;
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
    public final void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the responseId
     */
    public String getResponseId() {
        return responseId;
    }

    /**
     * @param responseId the responseId to set
     */
    public void setResponseId(String responseId) {
        this.responseId = responseId;
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
     * @return the txnChg
     */
    public BigDecimal getTxnChg() {
        return txnChg;
    }

    /**
     * @param txnChg the txnChg to set
     */
    public void setTxnChg(BigDecimal txnChg) {
        this.txnChg = txnChg;
    }

    /**
     * @return the lNDetail
     */
    public LNDetail getlNDetail() {
        return lNDetail;
    }

    /**
     * @param lNDetail the lNDetail to set
     */
    public void setlNDetail(LNDetail lNDetail) {
        this.lNDetail = lNDetail;
    }

    /**
     * @param message the message to set
     */
    public final void setMessage(String message) {
        this.message = message;
    }

    @Override
    @AXIgnore
    public String toString() {
        return getRecId() + "~" + getMsisdn();
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the contra
     */
    public String getContra() {
        return contra;
    }

    /**
     * @param contra the contra to set
     */
    public void setContra(String contra) {
        this.contra = contra;
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
     * @return the schemeId
     */
    public long getSchemeId() {
        return schemeId;
    }

    /**
     * @param schemeId the schemeId to set
     */
    public void setSchemeId(long schemeId) {
        this.schemeId = schemeId;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the accessCode
     */
    public String getAccessCode() {
        return accessCode;
    }

    /**
     * @param accessCode the accessCode to set
     */
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    /**
     * @return the originator
     */
    public String getOriginator() {
        return originator;
    }

    /**
     * @param originator the originator to set
     */
    public void setOriginator(String originator) {
        this.originator = originator;
    }
}
