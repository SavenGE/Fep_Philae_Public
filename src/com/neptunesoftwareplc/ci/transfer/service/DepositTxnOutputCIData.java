
package com.neptunesoftwareplc.ci.transfer.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for depositTxnOutputCIData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="depositTxnOutputCIData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="drcrFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strAvailableBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strTxnAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnCcyISOCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnChequeNumber" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="txnDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="txnDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "depositTxnOutputCIData", propOrder = {
    "accountId",
    "drcrFlag",
    "strAvailableBalance",
    "strTxnAmount",
    "txnCcyISOCode",
    "txnChequeNumber",
    "txnDate",
    "txnDescription",
    "txnReference"
})
public class DepositTxnOutputCIData {

    protected Long accountId;
    protected String drcrFlag;
    protected String strAvailableBalance;
    protected String strTxnAmount;
    protected String txnCcyISOCode;
    protected Long txnChequeNumber;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar txnDate;
    protected String txnDescription;
    protected String txnReference;

    /**
     * Gets the value of the accountId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAccountId(Long value) {
        this.accountId = value;
    }

    /**
     * Gets the value of the drcrFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrcrFlag() {
        return drcrFlag;
    }

    /**
     * Sets the value of the drcrFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrcrFlag(String value) {
        this.drcrFlag = value;
    }

    /**
     * Gets the value of the strAvailableBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrAvailableBalance() {
        return strAvailableBalance;
    }

    /**
     * Sets the value of the strAvailableBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrAvailableBalance(String value) {
        this.strAvailableBalance = value;
    }

    /**
     * Gets the value of the strTxnAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrTxnAmount() {
        return strTxnAmount;
    }

    /**
     * Sets the value of the strTxnAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrTxnAmount(String value) {
        this.strTxnAmount = value;
    }

    /**
     * Gets the value of the txnCcyISOCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnCcyISOCode() {
        return txnCcyISOCode;
    }

    /**
     * Sets the value of the txnCcyISOCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnCcyISOCode(String value) {
        this.txnCcyISOCode = value;
    }

    /**
     * Gets the value of the txnChequeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTxnChequeNumber() {
        return txnChequeNumber;
    }

    /**
     * Sets the value of the txnChequeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTxnChequeNumber(Long value) {
        this.txnChequeNumber = value;
    }

    /**
     * Gets the value of the txnDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTxnDate() {
        return txnDate;
    }

    /**
     * Sets the value of the txnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTxnDate(XMLGregorianCalendar value) {
        this.txnDate = value;
    }

    /**
     * Gets the value of the txnDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnDescription() {
        return txnDescription;
    }

    /**
     * Sets the value of the txnDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnDescription(String value) {
        this.txnDescription = value;
    }

    /**
     * Gets the value of the txnReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnReference() {
        return txnReference;
    }

    /**
     * Sets the value of the txnReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnReference(String value) {
        this.txnReference = value;
    }

}
