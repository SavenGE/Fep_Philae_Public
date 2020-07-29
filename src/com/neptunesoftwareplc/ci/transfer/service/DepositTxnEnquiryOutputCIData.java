
package com.neptunesoftwareplc.ci.transfer.service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for depositTxnEnquiryOutputCIData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="depositTxnEnquiryOutputCIData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.transfer.ci.neptunesoftwareplc.com/}ciOutputBaseObject">
 *       &lt;sequence>
 *         &lt;element name="depositTxnOutputData" type="{http://service.transfer.ci.neptunesoftwareplc.com/}depositTxnOutputCIData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "depositTxnEnquiryOutputCIData", propOrder = {
    "depositTxnOutputData"
})
public class DepositTxnEnquiryOutputCIData
    extends CiOutputBaseObject
{

    @XmlElement(nillable = true)
    protected List<DepositTxnOutputCIData> depositTxnOutputData;

    /**
     * Gets the value of the depositTxnOutputData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the depositTxnOutputData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDepositTxnOutputData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DepositTxnOutputCIData }
     * 
     * 
     */
    public List<DepositTxnOutputCIData> getDepositTxnOutputData() {
        if (depositTxnOutputData == null) {
            depositTxnOutputData = new ArrayList<DepositTxnOutputCIData>();
        }
        return this.depositTxnOutputData;
    }

}
