
package com.neptunesoftwareplc.ci.transfer.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fundsTransferOutputData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fundsTransferOutputData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.transfer.ci.neptunesoftwareplc.com/}baseResponseData">
 *       &lt;sequence>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="txnJournalId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="txnReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fundsTransferOutputData", propOrder = {
    "accountNo",
    "strAmount",
    "txnJournalId",
    "txnReference"
})
public class FundsTransferOutputData
    extends BaseResponseData
{

    protected String accountNo;
    protected String strAmount;
    protected Long txnJournalId;
    protected String txnReference;

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
     * Gets the value of the txnJournalId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTxnJournalId() {
        return txnJournalId;
    }

    /**
     * Sets the value of the txnJournalId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTxnJournalId(Long value) {
        this.txnJournalId = value;
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
