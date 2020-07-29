package schemas.dynamics.microsoft.codeunit.mobilebanking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="request_id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="phone_no" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="transaction_type" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="trnx_charges" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="account__number" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cr_account" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="status" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="f_key" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="balance" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="response" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="response_message" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="customerType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="endDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="startTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="endTime" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="emailaddress" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder
        = {
            "requestId",
            "phoneNo",
            "transactionType",
            "amount",
            "trnxCharges",
            "accountNumber",
            "crAccount",
            "status",
            "fKey",
            "balance",
            "message",
            "response",
            "responseMessage",
            "customerType",
            "description",
            "startDate",
            "endDate",
            "startTime",
            "endTime",
            "emailaddress"
        })
@XmlRootElement(name = "Spotcash")
public class Spotcash {

    @XmlElement(name = "request_id", required = true)
    protected String requestId;
    @XmlElement(name = "phone_no", required = true)
    protected String phoneNo;
    @XmlElement(name = "transaction_type")
    protected int transactionType;
    @XmlElement(required = true)
    protected String amount;
    @XmlElement(name = "trnx_charges", required = true)
    protected String trnxCharges;
    @XmlElement(name = "account__number", required = true)
    protected String accountNumber;
    @XmlElement(name = "cr_account", required = true)
    protected String crAccount;
    @XmlElement(required = true)
    protected String status;
    @XmlElement(name = "f_key", required = true)
    protected String fKey;
    @XmlElement(required = true)
    protected String balance;
    @XmlElement(required = true)
    protected String message;
    @XmlElement(required = true)
    protected String response;
    @XmlElement(name = "response_message", required = true)
    protected String responseMessage;
    @XmlElement(required = true)
    protected String customerType;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected String startDate;
    @XmlElement(required = true)
    protected String endDate;
    @XmlElement(required = true)
    protected String startTime;
    @XmlElement(required = true)
    protected String endTime;
    @XmlElement(required = true)
    protected String emailaddress;

    /**
     * Gets the value of the requestId property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the value of the requestId property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRequestId(String value) {
        this.requestId = value;
    }

    /**
     * Gets the value of the phoneNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Sets the value of the phoneNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPhoneNo(String value) {
        this.phoneNo = value;
    }

    /**
     * Gets the value of the transactionType property.
     *
     */
    public int getTransactionType() {
        return transactionType;
    }

    /**
     * Sets the value of the transactionType property.
     *
     */
    public void setTransactionType(int value) {
        this.transactionType = value;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setAmount(String value) {
        this.amount = value;
    }

    /**
     * Gets the value of the trnxCharges property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getTrnxCharges() {
        return trnxCharges;
    }

    /**
     * Sets the value of the trnxCharges property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setTrnxCharges(String value) {
        this.trnxCharges = value;
    }

    /**
     * Gets the value of the accountNumber property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the crAccount property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCrAccount() {
        return crAccount;
    }

    /**
     * Sets the value of the crAccount property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCrAccount(String value) {
        this.crAccount = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the fKey property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFKey() {
        return fKey;
    }

    /**
     * Sets the value of the fKey property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFKey(String value) {
        this.fKey = value;
    }

    /**
     * Gets the value of the balance property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setBalance(String value) {
        this.balance = value;
    }

    /**
     * Gets the value of the message property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMessage(String value) {
        this.message = value;
    }

    /**
     * Gets the value of the response property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setResponse(String value) {
        this.response = value;
    }

    /**
     * Gets the value of the responseMessage property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

    /**
     * Gets the value of the customerType property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the value of the customerType property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setCustomerType(String value) {
        this.customerType = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the startDate property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the endDate property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setEndDate(String value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the startTime property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setStartTime(String value) {
        this.startTime = value;
    }

    /**
     * Gets the value of the endTime property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Sets the value of the endTime property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setEndTime(String value) {
        this.endTime = value;
    }

    /**
     * Gets the value of the emailaddress property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEmailaddress() {
        return emailaddress;
    }

    /**
     * Sets the value of the emailaddress property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setEmailaddress(String value) {
        this.emailaddress = value;
    }

}
