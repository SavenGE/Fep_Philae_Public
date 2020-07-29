
package com.neptunesoftwareplc.ci.account.service;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountStmtOutputCIData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountStmtOutputCIData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="chequeNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayDrCrFg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="drCrFg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="narrative" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stmtBalance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strTxnAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strTxnDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strValueDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="txnCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnDate" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountStmtOutputCIData", propOrder = {
    "accountNo",
    "chequeNumber",
    "currencyCode",
    "displayDrCrFg",
    "drCrFg",
    "narrative",
    "reference",
    "stmtBalance",
    "strTxnAmount",
    "strTxnDate",
    "strValueDate",
    "txnAmount",
    "txnCode",
    "txnDate"
})
public class AccountStmtOutputCIData {

    protected String accountNo;
    protected String chequeNumber;
    protected String currencyCode;
    protected String displayDrCrFg;
    protected String drCrFg;
    protected String narrative;
    protected String reference;
    protected String stmtBalance;
    protected String strTxnAmount;
    protected String strTxnDate;
    protected String strValueDate;
    protected BigDecimal txnAmount;
    protected String txnCode;
    protected Long txnDate;

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
     * Gets the value of the chequeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChequeNumber() {
        return chequeNumber;
    }

    /**
     * Sets the value of the chequeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChequeNumber(String value) {
        this.chequeNumber = value;
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
     * Gets the value of the displayDrCrFg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayDrCrFg() {
        return displayDrCrFg;
    }

    /**
     * Sets the value of the displayDrCrFg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayDrCrFg(String value) {
        this.displayDrCrFg = value;
    }

    /**
     * Gets the value of the drCrFg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrCrFg() {
        return drCrFg;
    }

    /**
     * Sets the value of the drCrFg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrCrFg(String value) {
        this.drCrFg = value;
    }

    /**
     * Gets the value of the narrative property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNarrative() {
        return narrative;
    }

    /**
     * Sets the value of the narrative property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNarrative(String value) {
        this.narrative = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReference(String value) {
        this.reference = value;
    }

    /**
     * Gets the value of the stmtBalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStmtBalance() {
        return stmtBalance;
    }

    /**
     * Sets the value of the stmtBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStmtBalance(String value) {
        this.stmtBalance = value;
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
     * Gets the value of the strTxnDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrTxnDate() {
        return strTxnDate;
    }

    /**
     * Sets the value of the strTxnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrTxnDate(String value) {
        this.strTxnDate = value;
    }

    /**
     * Gets the value of the strValueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrValueDate() {
        return strValueDate;
    }

    /**
     * Sets the value of the strValueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrValueDate(String value) {
        this.strValueDate = value;
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
     * Gets the value of the txnCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnCode() {
        return txnCode;
    }

    /**
     * Sets the value of the txnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnCode(String value) {
        this.txnCode = value;
    }

    /**
     * Gets the value of the txnDate property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTxnDate() {
        return txnDate;
    }

    /**
     * Sets the value of the txnDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTxnDate(Long value) {
        this.txnDate = value;
    }

}
