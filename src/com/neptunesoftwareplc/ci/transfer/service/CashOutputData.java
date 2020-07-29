
package com.neptunesoftwareplc.ci.transfer.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cashOutputData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cashOutputData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.transfer.ci.neptunesoftwareplc.com/}baseResponseData">
 *       &lt;sequence>
 *         &lt;element name="txnJournalId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="txnref" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cashOutputData", propOrder = {
    "txnJournalId",
    "txnref"
})
public class CashOutputData
    extends BaseResponseData
{

    protected Long txnJournalId;
    protected String txnref;

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
     * Gets the value of the txnref property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTxnref() {
        return txnref;
    }

    /**
     * Sets the value of the txnref property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTxnref(String value) {
        this.txnref = value;
    }

}
