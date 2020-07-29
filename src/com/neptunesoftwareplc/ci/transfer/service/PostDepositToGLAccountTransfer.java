
package com.neptunesoftwareplc.ci.transfer.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for postDepositToGLAccountTransfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="postDepositToGLAccountTransfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="arg0" type="{http://service.transfer.ci.neptunesoftwareplc.com/}fundsTransferRequestData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "postDepositToGLAccountTransfer", propOrder = {
    "arg0"
})
public class PostDepositToGLAccountTransfer {

    protected FundsTransferRequestData arg0;

    /**
     * Gets the value of the arg0 property.
     * 
     * @return
     *     possible object is
     *     {@link FundsTransferRequestData }
     *     
     */
    public FundsTransferRequestData getArg0() {
        return arg0;
    }

    /**
     * Sets the value of the arg0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link FundsTransferRequestData }
     *     
     */
    public void setArg0(FundsTransferRequestData value) {
        this.arg0 = value;
    }

}
