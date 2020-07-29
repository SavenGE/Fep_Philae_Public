
package com.neptunesoftwareplc.ci.transfer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cashRequestData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cashRequestData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.transfer.ci.neptunesoftwareplc.com/}baseRequestData">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cashInAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="cashOutAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="currencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="denominationAmount" type="{http://www.w3.org/2001/XMLSchema}decimal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="denominationCount" type="{http://www.w3.org/2001/XMLSchema}decimal" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="drawerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="drawerUserId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="txnDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cashRequestData", propOrder = {
    "accountNo",
    "cashInAmount",
    "cashOutAmount",
    "currencyCode",
    "denominationAmount",
    "denominationCount",
    "drawerNumber",
    "drawerUserId",
    "txnDescription"
})
public class CashRequestData
    extends BaseRequestData
{

    protected String accountNo;
    protected BigDecimal cashInAmount;
    protected BigDecimal cashOutAmount;
    protected String currencyCode;
    @XmlElement(nillable = true)
    protected List<BigDecimal> denominationAmount;
    @XmlElement(nillable = true)
    protected List<BigDecimal> denominationCount;
    protected String drawerNumber;
    protected Long drawerUserId;
    protected String txnDescription;

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
     * Gets the value of the cashInAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCashInAmount() {
        return cashInAmount;
    }

    /**
     * Sets the value of the cashInAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCashInAmount(BigDecimal value) {
        this.cashInAmount = value;
    }

    /**
     * Gets the value of the cashOutAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCashOutAmount() {
        return cashOutAmount;
    }

    /**
     * Sets the value of the cashOutAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCashOutAmount(BigDecimal value) {
        this.cashOutAmount = value;
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
     * Gets the value of the denominationAmount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the denominationAmount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDenominationAmount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigDecimal }
     * 
     * 
     */
    public List<BigDecimal> getDenominationAmount() {
        if (denominationAmount == null) {
            denominationAmount = new ArrayList<BigDecimal>();
        }
        return this.denominationAmount;
    }

    /**
     * Gets the value of the denominationCount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the denominationCount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDenominationCount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigDecimal }
     * 
     * 
     */
    public List<BigDecimal> getDenominationCount() {
        if (denominationCount == null) {
            denominationCount = new ArrayList<BigDecimal>();
        }
        return this.denominationCount;
    }

    /**
     * Gets the value of the drawerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDrawerNumber() {
        return drawerNumber;
    }

    /**
     * Sets the value of the drawerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDrawerNumber(String value) {
        this.drawerNumber = value;
    }

    /**
     * Gets the value of the drawerUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDrawerUserId() {
        return drawerUserId;
    }

    /**
     * Sets the value of the drawerUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDrawerUserId(Long value) {
        this.drawerUserId = value;
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

}
