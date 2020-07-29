
package com.neptunesoftwareplc.ci.account.service;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for loanAccountRequestCIData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="loanAccountRequestCIData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.account.ci.neptunesoftwareplc.com/}ciRequestBaseObject">
 *       &lt;sequence>
 *         &lt;element name="accountName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acctId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="applId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="autoReclassifyFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buResponsibilityCentreId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="capitalisedEventDueDateOption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="closedDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currencyId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="disbChargeSettlement" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disbrsmntSetlmntAcctId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="disbursementLimit" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="finalRepaymentDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="intMarginCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lastReviewDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loanCycle" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="loanFeeAccountId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="loanFeeAccountNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mainBranchId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="maturityDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="multiCurrency" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="multipleRepaymentsAcctFlag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nextReviewDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oldAccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="originalMaturityDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="portfolioId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="primaryOfficerId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="productCategoryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="productSubCategoryType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restructureEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restructureId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="riskClassId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="riskCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondaryOfficerId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="secondarySetlmntAcctId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="singleDisbursement" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="startDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statementFrequencyCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statementFrequencyValue" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="statementId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="statusEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termValue" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "loanAccountRequestCIData", propOrder = {
    "accountName",
    "acctId",
    "applId",
    "autoReclassifyFlag",
    "buResponsibilityCentreId",
    "capitalisedEventDueDateOption",
    "closedDate",
    "currencyId",
    "disbChargeSettlement",
    "disbrsmntSetlmntAcctId",
    "disbursementLimit",
    "finalRepaymentDate",
    "intMarginCount",
    "lastReviewDate",
    "loanCycle",
    "loanFeeAccountId",
    "loanFeeAccountNo",
    "mainBranchId",
    "maturityDate",
    "multiCurrency",
    "multipleRepaymentsAcctFlag",
    "nextReviewDate",
    "number",
    "oldAccountNumber",
    "originalMaturityDate",
    "portfolioId",
    "primaryOfficerId",
    "productCategoryId",
    "productId",
    "productSubCategoryType",
    "restructureEffectiveDate",
    "restructureId",
    "riskClassId",
    "riskCode",
    "secondaryOfficerId",
    "secondarySetlmntAcctId",
    "singleDisbursement",
    "startDate",
    "statementFrequencyCode",
    "statementFrequencyValue",
    "statementId",
    "statusEffectiveDate",
    "termCode",
    "termValue"
})
public class LoanAccountRequestCIData
    extends CiRequestBaseObject
{

    protected String accountName;
    protected Long acctId;
    protected Long applId;
    protected String autoReclassifyFlag;
    protected Long buResponsibilityCentreId;
    protected String capitalisedEventDueDateOption;
    protected String closedDate;
    protected Long currencyId;
    protected String disbChargeSettlement;
    protected Long disbrsmntSetlmntAcctId;
    protected BigDecimal disbursementLimit;
    protected String finalRepaymentDate;
    protected Integer intMarginCount;
    protected String lastReviewDate;
    protected Long loanCycle;
    protected Long loanFeeAccountId;
    protected String loanFeeAccountNo;
    protected Long mainBranchId;
    protected String maturityDate;
    protected boolean multiCurrency;
    protected boolean multipleRepaymentsAcctFlag;
    protected String nextReviewDate;
    protected String number;
    protected String oldAccountNumber;
    protected String originalMaturityDate;
    protected Long portfolioId;
    protected Long primaryOfficerId;
    protected String productCategoryId;
    protected Long productId;
    protected String productSubCategoryType;
    protected String restructureEffectiveDate;
    protected Long restructureId;
    protected Long riskClassId;
    protected String riskCode;
    protected Long secondaryOfficerId;
    protected Long secondarySetlmntAcctId;
    protected boolean singleDisbursement;
    protected String startDate;
    protected String statementFrequencyCode;
    protected Long statementFrequencyValue;
    protected Long statementId;
    protected String statusEffectiveDate;
    protected String termCode;
    protected Integer termValue;

    /**
     * Gets the value of the accountName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Sets the value of the accountName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountName(String value) {
        this.accountName = value;
    }

    /**
     * Gets the value of the acctId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getAcctId() {
        return acctId;
    }

    /**
     * Sets the value of the acctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setAcctId(Long value) {
        this.acctId = value;
    }

    /**
     * Gets the value of the applId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getApplId() {
        return applId;
    }

    /**
     * Sets the value of the applId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setApplId(Long value) {
        this.applId = value;
    }

    /**
     * Gets the value of the autoReclassifyFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutoReclassifyFlag() {
        return autoReclassifyFlag;
    }

    /**
     * Sets the value of the autoReclassifyFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutoReclassifyFlag(String value) {
        this.autoReclassifyFlag = value;
    }

    /**
     * Gets the value of the buResponsibilityCentreId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getBuResponsibilityCentreId() {
        return buResponsibilityCentreId;
    }

    /**
     * Sets the value of the buResponsibilityCentreId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setBuResponsibilityCentreId(Long value) {
        this.buResponsibilityCentreId = value;
    }

    /**
     * Gets the value of the capitalisedEventDueDateOption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCapitalisedEventDueDateOption() {
        return capitalisedEventDueDateOption;
    }

    /**
     * Sets the value of the capitalisedEventDueDateOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCapitalisedEventDueDateOption(String value) {
        this.capitalisedEventDueDateOption = value;
    }

    /**
     * Gets the value of the closedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClosedDate() {
        return closedDate;
    }

    /**
     * Sets the value of the closedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClosedDate(String value) {
        this.closedDate = value;
    }

    /**
     * Gets the value of the currencyId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCurrencyId() {
        return currencyId;
    }

    /**
     * Sets the value of the currencyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCurrencyId(Long value) {
        this.currencyId = value;
    }

    /**
     * Gets the value of the disbChargeSettlement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisbChargeSettlement() {
        return disbChargeSettlement;
    }

    /**
     * Sets the value of the disbChargeSettlement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisbChargeSettlement(String value) {
        this.disbChargeSettlement = value;
    }

    /**
     * Gets the value of the disbrsmntSetlmntAcctId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getDisbrsmntSetlmntAcctId() {
        return disbrsmntSetlmntAcctId;
    }

    /**
     * Sets the value of the disbrsmntSetlmntAcctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setDisbrsmntSetlmntAcctId(Long value) {
        this.disbrsmntSetlmntAcctId = value;
    }

    /**
     * Gets the value of the disbursementLimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDisbursementLimit() {
        return disbursementLimit;
    }

    /**
     * Sets the value of the disbursementLimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDisbursementLimit(BigDecimal value) {
        this.disbursementLimit = value;
    }

    /**
     * Gets the value of the finalRepaymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFinalRepaymentDate() {
        return finalRepaymentDate;
    }

    /**
     * Sets the value of the finalRepaymentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFinalRepaymentDate(String value) {
        this.finalRepaymentDate = value;
    }

    /**
     * Gets the value of the intMarginCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIntMarginCount() {
        return intMarginCount;
    }

    /**
     * Sets the value of the intMarginCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIntMarginCount(Integer value) {
        this.intMarginCount = value;
    }

    /**
     * Gets the value of the lastReviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastReviewDate() {
        return lastReviewDate;
    }

    /**
     * Sets the value of the lastReviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastReviewDate(String value) {
        this.lastReviewDate = value;
    }

    /**
     * Gets the value of the loanCycle property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLoanCycle() {
        return loanCycle;
    }

    /**
     * Sets the value of the loanCycle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLoanCycle(Long value) {
        this.loanCycle = value;
    }

    /**
     * Gets the value of the loanFeeAccountId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLoanFeeAccountId() {
        return loanFeeAccountId;
    }

    /**
     * Sets the value of the loanFeeAccountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLoanFeeAccountId(Long value) {
        this.loanFeeAccountId = value;
    }

    /**
     * Gets the value of the loanFeeAccountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanFeeAccountNo() {
        return loanFeeAccountNo;
    }

    /**
     * Sets the value of the loanFeeAccountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanFeeAccountNo(String value) {
        this.loanFeeAccountNo = value;
    }

    /**
     * Gets the value of the mainBranchId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getMainBranchId() {
        return mainBranchId;
    }

    /**
     * Sets the value of the mainBranchId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setMainBranchId(Long value) {
        this.mainBranchId = value;
    }

    /**
     * Gets the value of the maturityDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaturityDate() {
        return maturityDate;
    }

    /**
     * Sets the value of the maturityDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaturityDate(String value) {
        this.maturityDate = value;
    }

    /**
     * Gets the value of the multiCurrency property.
     * 
     */
    public boolean isMultiCurrency() {
        return multiCurrency;
    }

    /**
     * Sets the value of the multiCurrency property.
     * 
     */
    public void setMultiCurrency(boolean value) {
        this.multiCurrency = value;
    }

    /**
     * Gets the value of the multipleRepaymentsAcctFlag property.
     * 
     */
    public boolean isMultipleRepaymentsAcctFlag() {
        return multipleRepaymentsAcctFlag;
    }

    /**
     * Sets the value of the multipleRepaymentsAcctFlag property.
     * 
     */
    public void setMultipleRepaymentsAcctFlag(boolean value) {
        this.multipleRepaymentsAcctFlag = value;
    }

    /**
     * Gets the value of the nextReviewDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextReviewDate() {
        return nextReviewDate;
    }

    /**
     * Sets the value of the nextReviewDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextReviewDate(String value) {
        this.nextReviewDate = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the oldAccountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldAccountNumber() {
        return oldAccountNumber;
    }

    /**
     * Sets the value of the oldAccountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldAccountNumber(String value) {
        this.oldAccountNumber = value;
    }

    /**
     * Gets the value of the originalMaturityDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalMaturityDate() {
        return originalMaturityDate;
    }

    /**
     * Sets the value of the originalMaturityDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalMaturityDate(String value) {
        this.originalMaturityDate = value;
    }

    /**
     * Gets the value of the portfolioId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPortfolioId() {
        return portfolioId;
    }

    /**
     * Sets the value of the portfolioId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPortfolioId(Long value) {
        this.portfolioId = value;
    }

    /**
     * Gets the value of the primaryOfficerId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPrimaryOfficerId() {
        return primaryOfficerId;
    }

    /**
     * Sets the value of the primaryOfficerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPrimaryOfficerId(Long value) {
        this.primaryOfficerId = value;
    }

    /**
     * Gets the value of the productCategoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductCategoryId() {
        return productCategoryId;
    }

    /**
     * Sets the value of the productCategoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductCategoryId(String value) {
        this.productCategoryId = value;
    }

    /**
     * Gets the value of the productId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * Sets the value of the productId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProductId(Long value) {
        this.productId = value;
    }

    /**
     * Gets the value of the productSubCategoryType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductSubCategoryType() {
        return productSubCategoryType;
    }

    /**
     * Sets the value of the productSubCategoryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductSubCategoryType(String value) {
        this.productSubCategoryType = value;
    }

    /**
     * Gets the value of the restructureEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestructureEffectiveDate() {
        return restructureEffectiveDate;
    }

    /**
     * Sets the value of the restructureEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestructureEffectiveDate(String value) {
        this.restructureEffectiveDate = value;
    }

    /**
     * Gets the value of the restructureId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRestructureId() {
        return restructureId;
    }

    /**
     * Sets the value of the restructureId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRestructureId(Long value) {
        this.restructureId = value;
    }

    /**
     * Gets the value of the riskClassId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRiskClassId() {
        return riskClassId;
    }

    /**
     * Sets the value of the riskClassId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRiskClassId(Long value) {
        this.riskClassId = value;
    }

    /**
     * Gets the value of the riskCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskCode() {
        return riskCode;
    }

    /**
     * Sets the value of the riskCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskCode(String value) {
        this.riskCode = value;
    }

    /**
     * Gets the value of the secondaryOfficerId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecondaryOfficerId() {
        return secondaryOfficerId;
    }

    /**
     * Sets the value of the secondaryOfficerId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecondaryOfficerId(Long value) {
        this.secondaryOfficerId = value;
    }

    /**
     * Gets the value of the secondarySetlmntAcctId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecondarySetlmntAcctId() {
        return secondarySetlmntAcctId;
    }

    /**
     * Sets the value of the secondarySetlmntAcctId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecondarySetlmntAcctId(Long value) {
        this.secondarySetlmntAcctId = value;
    }

    /**
     * Gets the value of the singleDisbursement property.
     * 
     */
    public boolean isSingleDisbursement() {
        return singleDisbursement;
    }

    /**
     * Sets the value of the singleDisbursement property.
     * 
     */
    public void setSingleDisbursement(boolean value) {
        this.singleDisbursement = value;
    }

    /**
     * Gets the value of the startDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the value of the startDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartDate(String value) {
        this.startDate = value;
    }

    /**
     * Gets the value of the statementFrequencyCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatementFrequencyCode() {
        return statementFrequencyCode;
    }

    /**
     * Sets the value of the statementFrequencyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatementFrequencyCode(String value) {
        this.statementFrequencyCode = value;
    }

    /**
     * Gets the value of the statementFrequencyValue property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStatementFrequencyValue() {
        return statementFrequencyValue;
    }

    /**
     * Sets the value of the statementFrequencyValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStatementFrequencyValue(Long value) {
        this.statementFrequencyValue = value;
    }

    /**
     * Gets the value of the statementId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getStatementId() {
        return statementId;
    }

    /**
     * Sets the value of the statementId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setStatementId(Long value) {
        this.statementId = value;
    }

    /**
     * Gets the value of the statusEffectiveDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusEffectiveDate() {
        return statusEffectiveDate;
    }

    /**
     * Sets the value of the statusEffectiveDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusEffectiveDate(String value) {
        this.statusEffectiveDate = value;
    }

    /**
     * Gets the value of the termCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermCode() {
        return termCode;
    }

    /**
     * Sets the value of the termCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermCode(String value) {
        this.termCode = value;
    }

    /**
     * Gets the value of the termValue property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTermValue() {
        return termValue;
    }

    /**
     * Sets the value of the termValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTermValue(Integer value) {
        this.termValue = value;
    }

}
