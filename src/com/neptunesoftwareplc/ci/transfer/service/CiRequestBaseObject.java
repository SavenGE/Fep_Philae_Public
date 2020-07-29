
package com.neptunesoftwareplc.ci.transfer.service;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ciRequestBaseObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ciRequestBaseObject">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.transfer.ci.neptunesoftwareplc.com/}baseRequestData">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="action" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chargeFee" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="chnUserCustName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="code" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contraAccountShortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="credentials" type="{http://service.transfer.ci.neptunesoftwareplc.com/}credentials" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="custChannelUserId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="custRelshipType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerChannelId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="customerId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="customerNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerPhoneNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="effectiveDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="errorMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorSeverity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="excludePinValidationFg" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localEquivalentAmt" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="offlineMode" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="operationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originatorUserId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="processingDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="requestDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="requestPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestReferenceNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceChannelCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="svceChannelSchemeId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="txnAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="userAccessCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userPin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userRoleId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="wfNextActivityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workItemId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="workflowRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ciRequestBaseObject", propOrder = {
    "accountNo",
    "accountShortName",
    "action",
    "chargeFee",
    "chnUserCustName",
    "code",
    "contraAccountShortName",
    "credentials",
    "currencyCode",
    "custChannelUserId",
    "custRelshipType",
    "customerChannelId",
    "customerId",
    "customerNo",
    "customerPhoneNo",
    "effectiveDate",
    "errorMsg",
    "errorSeverity",
    "excludePinValidationFg",
    "localEquivalentAmt",
    "offlineMode",
    "operationCode",
    "originalReferenceNo",
    "originatorUserId",
    "processingDate",
    "requestDate",
    "requestPassword",
    "requestReferenceNo",
    "serviceChannelCode",
    "strAmount",
    "svceChannelSchemeId",
    "txnAmount",
    "userAccessCode",
    "userId",
    "userName",
    "userPin",
    "userRoleId",
    "wfNextActivityType",
    "workItemId",
    "workflowRequired"
})
public class CiRequestBaseObject
    extends BaseRequestData
{

    protected String accountNo;
    protected String accountShortName;
    protected String action;
    protected BigDecimal chargeFee;
    protected String chnUserCustName;
    protected String code;
    protected String contraAccountShortName;
    protected Credentials credentials;
    protected String currencyCode;
    protected Long custChannelUserId;
    protected String custRelshipType;
    protected Long customerChannelId;
    protected Long customerId;
    protected String customerNo;
    protected String customerPhoneNo;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar effectiveDate;
    protected String errorMsg;
    protected String errorSeverity;
    protected Boolean excludePinValidationFg;
    protected BigDecimal localEquivalentAmt;
    protected boolean offlineMode;
    protected String operationCode;
    protected String originalReferenceNo;
    protected Long originatorUserId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar processingDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar requestDate;
    protected String requestPassword;
    protected String requestReferenceNo;
    protected String serviceChannelCode;
    protected String strAmount;
    protected Long svceChannelSchemeId;
    protected BigDecimal txnAmount;
    protected String userAccessCode;
    protected String userId;
    protected String userName;
    protected String userPin;
    protected Long userRoleId;
    protected String wfNextActivityType;
    protected Long workItemId;
    protected boolean workflowRequired;

    /**
     * Gets the value of the accountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * Sets the value of the accountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNo(String value) {
        this.accountNo = value;
    }

    /**
     * Gets the value of the accountShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountShortName() {
        return accountShortName;
    }

    /**
     * Sets the value of the accountShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountShortName(String value) {
        this.accountShortName = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the chargeFee property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getChargeFee() {
        return chargeFee;
    }

    /**
     * Sets the value of the chargeFee property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setChargeFee(BigDecimal value) {
        this.chargeFee = value;
    }

    /**
     * Gets the value of the chnUserCustName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChnUserCustName() {
        return chnUserCustName;
    }

    /**
     * Sets the value of the chnUserCustName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChnUserCustName(String value) {
        this.chnUserCustName = value;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the contraAccountShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContraAccountShortName() {
        return contraAccountShortName;
    }

    /**
     * Sets the value of the contraAccountShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContraAccountShortName(String value) {
        this.contraAccountShortName = value;
    }

    /**
     * Gets the value of the credentials property.
     * 
     * @return
     *     possible object is
     *     {@link Credentials }
     *     
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * Sets the value of the credentials property.
     * 
     * @param value
     *     allowed object is
     *     {@link Credentials }
     *     
     */
    public void setCredentials(Credentials value) {
        this.credentials = value;
    }

    /**
     * Gets the value of the currencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the value of the currencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCode(String value) {
        this.currencyCode = value;
    }

    /**
     * Gets the value of the custChannelUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustChannelUserId() {
        return custChannelUserId;
    }

    /**
     * Sets the value of the custChannelUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustChannelUserId(Long value) {
        this.custChannelUserId = value;
    }

    /**
     * Gets the value of the custRelshipType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustRelshipType() {
        return custRelshipType;
    }

    /**
     * Sets the value of the custRelshipType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustRelshipType(String value) {
        this.custRelshipType = value;
    }

    /**
     * Gets the value of the customerChannelId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustomerChannelId() {
        return customerChannelId;
    }

    /**
     * Sets the value of the customerChannelId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerChannelId(Long value) {
        this.customerChannelId = value;
    }

    /**
     * Gets the value of the customerId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the value of the customerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCustomerId(Long value) {
        this.customerId = value;
    }

    /**
     * Gets the value of the customerNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * Sets the value of the customerNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerNo(String value) {
        this.customerNo = value;
    }

    /**
     * Gets the value of the customerPhoneNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    /**
     * Sets the value of the customerPhoneNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerPhoneNo(String value) {
        this.customerPhoneNo = value;
    }

    /**
     * Gets the value of the effectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the value of the effectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEffectiveDate(XMLGregorianCalendar value) {
        this.effectiveDate = value;
    }

    /**
     * Gets the value of the errorMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Sets the value of the errorMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMsg(String value) {
        this.errorMsg = value;
    }

    /**
     * Gets the value of the errorSeverity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorSeverity() {
        return errorSeverity;
    }

    /**
     * Sets the value of the errorSeverity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorSeverity(String value) {
        this.errorSeverity = value;
    }

    /**
     * Gets the value of the excludePinValidationFg property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExcludePinValidationFg() {
        return excludePinValidationFg;
    }

    /**
     * Sets the value of the excludePinValidationFg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExcludePinValidationFg(Boolean value) {
        this.excludePinValidationFg = value;
    }

    /**
     * Gets the value of the localEquivalentAmt property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLocalEquivalentAmt() {
        return localEquivalentAmt;
    }

    /**
     * Sets the value of the localEquivalentAmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLocalEquivalentAmt(BigDecimal value) {
        this.localEquivalentAmt = value;
    }

    /**
     * Gets the value of the offlineMode property.
     * 
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }

    /**
     * Sets the value of the offlineMode property.
     * 
     */
    public void setOfflineMode(boolean value) {
        this.offlineMode = value;
    }

    /**
     * Gets the value of the operationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperationCode() {
        return operationCode;
    }

    /**
     * Sets the value of the operationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperationCode(String value) {
        this.operationCode = value;
    }

    /**
     * Gets the value of the originalReferenceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalReferenceNo() {
        return originalReferenceNo;
    }

    /**
     * Sets the value of the originalReferenceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalReferenceNo(String value) {
        this.originalReferenceNo = value;
    }

    /**
     * Gets the value of the originatorUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getOriginatorUserId() {
        return originatorUserId;
    }

    /**
     * Sets the value of the originatorUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setOriginatorUserId(Long value) {
        this.originatorUserId = value;
    }

    /**
     * Gets the value of the processingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProcessingDate() {
        return processingDate;
    }

    /**
     * Sets the value of the processingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setProcessingDate(XMLGregorianCalendar value) {
        this.processingDate = value;
    }

    /**
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRequestDate(XMLGregorianCalendar value) {
        this.requestDate = value;
    }

    /**
     * Gets the value of the requestPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestPassword() {
        return requestPassword;
    }

    /**
     * Sets the value of the requestPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestPassword(String value) {
        this.requestPassword = value;
    }

    /**
     * Gets the value of the requestReferenceNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestReferenceNo() {
        return requestReferenceNo;
    }

    /**
     * Sets the value of the requestReferenceNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestReferenceNo(String value) {
        this.requestReferenceNo = value;
    }

    /**
     * Gets the value of the serviceChannelCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceChannelCode() {
        return serviceChannelCode;
    }

    /**
     * Sets the value of the serviceChannelCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceChannelCode(String value) {
        this.serviceChannelCode = value;
    }

    /**
     * Gets the value of the strAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrAmount() {
        return strAmount;
    }

    /**
     * Sets the value of the strAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrAmount(String value) {
        this.strAmount = value;
    }

    /**
     * Gets the value of the svceChannelSchemeId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSvceChannelSchemeId() {
        return svceChannelSchemeId;
    }

    /**
     * Sets the value of the svceChannelSchemeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSvceChannelSchemeId(Long value) {
        this.svceChannelSchemeId = value;
    }

    /**
     * Gets the value of the txnAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTxnAmount() {
        return txnAmount;
    }

    /**
     * Sets the value of the txnAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTxnAmount(BigDecimal value) {
        this.txnAmount = value;
    }

    /**
     * Gets the value of the userAccessCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserAccessCode() {
        return userAccessCode;
    }

    /**
     * Sets the value of the userAccessCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserAccessCode(String value) {
        this.userAccessCode = value;
    }

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the userPin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserPin() {
        return userPin;
    }

    /**
     * Sets the value of the userPin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserPin(String value) {
        this.userPin = value;
    }

    /**
     * Gets the value of the userRoleId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getUserRoleId() {
        return userRoleId;
    }

    /**
     * Sets the value of the userRoleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setUserRoleId(Long value) {
        this.userRoleId = value;
    }

    /**
     * Gets the value of the wfNextActivityType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWfNextActivityType() {
        return wfNextActivityType;
    }

    /**
     * Sets the value of the wfNextActivityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWfNextActivityType(String value) {
        this.wfNextActivityType = value;
    }

    /**
     * Gets the value of the workItemId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getWorkItemId() {
        return workItemId;
    }

    /**
     * Sets the value of the workItemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setWorkItemId(Long value) {
        this.workItemId = value;
    }

    /**
     * Gets the value of the workflowRequired property.
     * 
     */
    public boolean isWorkflowRequired() {
        return workflowRequired;
    }

    /**
     * Sets the value of the workflowRequired property.
     * 
     */
    public void setWorkflowRequired(boolean value) {
        this.workflowRequired = value;
    }

}
