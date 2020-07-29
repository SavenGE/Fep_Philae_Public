
package com.neptunesoftwareplc.ci.transfer.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for findDepositMiniStatementResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findDepositMiniStatementResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://service.transfer.ci.neptunesoftwareplc.com/}depositTxnEnquiryOutputCIData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findDepositMiniStatementResponse", propOrder = {
    "_return"
})
public class FindDepositMiniStatementResponse {

    @XmlElement(name = "return")
    protected DepositTxnEnquiryOutputCIData _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link DepositTxnEnquiryOutputCIData }
     *     
     */
    public DepositTxnEnquiryOutputCIData getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link DepositTxnEnquiryOutputCIData }
     *     
     */
    public void setReturn(DepositTxnEnquiryOutputCIData value) {
        this._return = value;
    }

}
