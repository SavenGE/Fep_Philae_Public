
package com.neptunesoftwareplc.ci.account.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for accountStatementOutputData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountStatementOutputData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.account.ci.neptunesoftwareplc.com/}baseResponseData">
 *       &lt;sequence>
 *         &lt;element name="accountClearedBalance" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="accountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountStmt" type="{http://service.account.ci.neptunesoftwareplc.com/}accountStmtOutputCIData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="closeBal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="openBal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="stmtType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountStatementOutputData", propOrder = {
    "accountClearedBalance",
    "accountNo",
    "accountStmt",
    "closeBal",
    "openBal",
    "stmtType"
})
public class AccountStatementOutputData
    extends BaseResponseData
{

    protected BigDecimal accountClearedBalance;
    protected String accountNo;
    @XmlElement(nillable = true)
    protected List<AccountStmtOutputCIData> accountStmt;
    protected BigDecimal closeBal;
    protected BigDecimal openBal;
    protected String stmtType;

    /**
     * Gets the value of the accountClearedBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAccountClearedBalance() {
        return accountClearedBalance;
    }

    /**
     * Sets the value of the accountClearedBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAccountClearedBalance(BigDecimal value) {
        this.accountClearedBalance = value;
    }

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
     * Gets the value of the accountStmt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accountStmt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccountStmt().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccountStmtOutputCIData }
     * 
     * 
     */
    public List<AccountStmtOutputCIData> getAccountStmt() {
        if (accountStmt == null) {
            accountStmt = new ArrayList<AccountStmtOutputCIData>();
        }
        return this.accountStmt;
    }

    /**
     * Gets the value of the closeBal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCloseBal() {
        return closeBal;
    }

    /**
     * Sets the value of the closeBal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCloseBal(BigDecimal value) {
        this.closeBal = value;
    }

    /**
     * Gets the value of the openBal property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOpenBal() {
        return openBal;
    }

    /**
     * Sets the value of the openBal property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOpenBal(BigDecimal value) {
        this.openBal = value;
    }

    /**
     * Gets the value of the stmtType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStmtType() {
        return stmtType;
    }

    /**
     * Sets the value of the stmtType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStmtType(String value) {
        this.stmtType = value;
    }

}
