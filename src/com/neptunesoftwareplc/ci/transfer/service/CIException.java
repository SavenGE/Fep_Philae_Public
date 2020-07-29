
package com.neptunesoftwareplc.ci.transfer.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CIException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CIException">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="configErrorFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="errorList" type="{http://service.transfer.ci.neptunesoftwareplc.com/}ciErrorData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="errorCodes" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CIException", propOrder = {
    "configErrorFlag",
    "errorList",
    "errorCodes"
})
public class CIException {

    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean configErrorFlag;
    protected List<CiErrorData> errorList;
    protected List<String> errorCodes;

    /**
     * Gets the value of the configErrorFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConfigErrorFlag() {
        return configErrorFlag;
    }

    /**
     * Sets the value of the configErrorFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConfigErrorFlag(Boolean value) {
        this.configErrorFlag = value;
    }

    /**
     * Gets the value of the errorList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CiErrorData }
     * 
     * 
     */
    public List<CiErrorData> getErrorList() {
        if (errorList == null) {
            errorList = new ArrayList<CiErrorData>();
        }
        return this.errorList;
    }

    /**
     * Gets the value of the errorCodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorCodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorCodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getErrorCodes() {
        if (errorCodes == null) {
            errorCodes = new ArrayList<String>();
        }
        return this.errorCodes;
    }

}
