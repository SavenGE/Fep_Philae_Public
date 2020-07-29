/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Pecherk
 */
public class CNAccount implements Serializable, Comparable<CNAccount> {

    private Long custId;
    private Long acctId;
    private String status;
    private Long productId;
    private String shortName;
    private String accountNumber;
    private String accountType;
    private String accountName;
    private CNBranch branch = new CNBranch();
    private CNCurrency currency = new CNCurrency();
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal arrears = BigDecimal.ZERO;
    private Date openDate;
    private Date endDate;
    private String custCat;

    public CNAccount() {
        this(null);
    }

    public CNAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the acctId
     */
    public Long getAcctId() {
        return acctId;
    }

    /**
     * @param acctId the acctId to set
     */
    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    /**
     * @return the shortName
     */
    public String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the accountType
     */
    public String getAccountType() {
        return accountType;
    }

    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * @return the custId
     */
    public Long getCustId() {
        return custId;
    }

    /**
     * @param custId the custId to set
     */
    public void setCustId(Long custId) {
        this.custId = custId;
    }

    /**
     * @return the currency
     */
    public CNCurrency getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(CNCurrency currency) {
        this.currency = currency;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the branch
     */
    public CNBranch getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(CNBranch branch) {
        this.branch = branch;
    }

    /**
     * @return the balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * @return the custCat
     */
    public String getCustCat() {
        return custCat;
    }

    /**
     * @param custCat the custCat to set
     */
    public void setCustCat(String custCat) {
        this.custCat = custCat;
    }

    @Override
    public int compareTo(CNAccount o) {
        return getAccountNumber().compareTo(o.getAccountNumber());
    }

    /**
     * @return the openDate
     */
    public Date getOpenDate() {
        return openDate;
    }

    /**
     * @param openDate the openDate to set
     */
    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the arrears
     */
    public BigDecimal getArrears() {
        return arrears;
    }

    /**
     * @param arrears the arrears to set
     */
    public void setArrears(BigDecimal arrears) {
        this.arrears = arrears;
    }

    public boolean isLoan() {
        return Objects.equals(getAccountType(), "LN");
    }

    public boolean isDeposit() {
        return Objects.equals(getAccountType(), "DP");
    }

    public boolean isLedger() {
        return getAccountNumber() != null && getAccountNumber().split("-").length >= 2;
    }
}
