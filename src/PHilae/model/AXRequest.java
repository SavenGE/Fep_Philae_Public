/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.enu.LNType;
import PHilae.enu.TXType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Pecherk
 */
public class AXRequest implements Serializable {

    private Long schemeId;
    private String module;
    private String reference;
    private String accessCode;
    private String narration;
    private String alertCode;
    private String terminalId;
    private String drawerNumber;
    private Long caseTypeId;
    private TXType type = TXType.Unknown;
    private CNAccount account = new CNAccount();
    private CNAccount contra = new CNAccount();
    private BigDecimal amount = BigDecimal.ZERO;
    private TXCharge charge = new TXCharge();
    private CNBranch branch = new CNBranch();
    private AXChannel channel = new AXChannel();
    private CNCurrency currency = new CNCurrency();
    private CNCustomer customer = new CNCustomer();
    private BigDecimal disbAmount = BigDecimal.ZERO;
    private LNType loanType = LNType.None;
    private Object item = new Object();
    private USRole role = new USRole();
    private Integer loanTerm;
    private Long productId;
    private String termCode;
    private Date startDate;
    private Date endDate;
    private String email;
    private String client;
    private boolean advice;
    private boolean reversal;
    private Integer count;
    private Integer pages = 0;
    private CNUser user = new CNUser();
    private Long eventId;
    private String detail;
    private String receipt;
    private String accessKey;
    private boolean setBalance = true;
    private boolean inverted;

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param txnAmount the amount to set
     */
    public void setAmount(BigDecimal txnAmount) {
        this.amount = txnAmount;
    }

    /**
     * @return the narration
     */
    public String getNarration() {
        return narration;
    }

    /**
     * @param narration the narration to set
     */
    public void setNarration(String narration) {
        this.narration = narration;
    }

    /**
     * @return the accessCode
     */
    public String getAccessCode() {
        return accessCode;
    }

    /**
     * @param accessCode the accessCode to set
     */
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    /**
     * @return the reversal
     */
    public boolean isReversal() {
        return reversal;
    }

    /**
     * @param reversal the reversal to set
     */
    public void setReversal(boolean reversal) {
        this.reversal = reversal;
    }

    /**
     * @return the account
     */
    public CNAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(CNAccount account) {
        this.account = account;
    }

    /**
     * @return the contra
     */
    public CNAccount getContra() {
        return contra;
    }

    /**
     * @param contra the contra to set
     */
    public void setContra(CNAccount contra) {
        this.contra = contra;
    }

    /**
     * @return the branch
     */
    public CNBranch getBranch() {
        return branch;
    }

    /**
     * @param cNBranch the branch to set
     */
    public void setBranch(CNBranch cNBranch) {
        this.branch = cNBranch;
    }

    /**
     * @return the currency
     */
    public CNCurrency getCurrency() {
        return currency;
    }

    /**
     * @param cNCurrency the currency to set
     */
    public void setCurrency(CNCurrency cNCurrency) {
        this.currency = cNCurrency;
    }

    /**
     * @return the charge
     */
    public TXCharge getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(TXCharge charge) {
        this.charge = charge;
    }

    /**
     * @return the advice
     */
    public boolean isAdvice() {
        return advice;
    }

    /**
     * @param advice the advice to set
     */
    public void setAdvice(boolean advice) {
        this.advice = advice;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
    }

    /**
     * @return the type
     */
    public TXType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TXType type) {
        this.type = type;
    }

    /**
     * @return the count
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * @return the schemeId
     */
    public Long getSchemeId() {
        return schemeId;
    }

    /**
     * @param schemeId the schemeId to set
     */
    public void setSchemeId(Long schemeId) {
        this.schemeId = schemeId;
    }

    /**
     * @return the caseTypeId
     */
    public Long getCaseTypeId() {
        return caseTypeId;
    }

    /**
     * @param caseTypeId the caseTypeId to set
     */
    public void setCaseTypeId(Long caseTypeId) {
        this.caseTypeId = caseTypeId;
    }

    /**
     * @return the alertCode
     */
    public String getAlertCode() {
        return alertCode;
    }

    /**
     * @param alertCode the alertCode to set
     */
    public void setAlertCode(String alertCode) {
        this.alertCode = alertCode;
    }

    /**
     * @return the terminalId
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * @param terminalId the terminalId to set
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the receipt
     */
    public String getReceipt() {
        return receipt;
    }

    /**
     * @param receipt the receipt to set
     */
    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    /**
     * @return the drawerNumber
     */
    public String getDrawerNumber() {
        return drawerNumber;
    }

    /**
     * @param drawerNumber the drawerNumber to set
     */
    public void setDrawerNumber(String drawerNumber) {
        this.drawerNumber = drawerNumber;
    }

    /**
     * @return the user
     */
    public CNUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(CNUser user) {
        this.user = user;
    }

    /**
     * @return the inverted
     */
    public boolean isInverted() {
        return inverted;
    }

    /**
     * @param inverted the inverted to set
     */
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * @return the setBalance
     */
    public boolean isSetBalance() {
        return setBalance;
    }

    /**
     * @param setBalance the setBalance to set
     */
    public void setSetBalance(boolean setBalance) {
        this.setBalance = setBalance;
    }

    /**
     * @return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * @param accessKey the accessKey to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * @return the client
     */
    public String getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * @return the eventId
     */
    public Long getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the customer
     */
    public CNCustomer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CNCustomer customer) {
        this.customer = customer;
    }

    /**
     * @return the pages
     */
    public Integer getPages() {
        return pages;
    }

    /**
     * @param pages the pages to set
     */
    public void setPages(Integer pages) {
        this.pages = pages;
    }

    /**
     * @return the role
     */
    public USRole getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(USRole role) {
        this.role = role;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @param <T>
     * @param clazz
     * @return the item
     */
    public <T> T getItem(Class<T> clazz) {
        return (T) item;
    }

    /**
     * @return the item
     */
    public Object getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Object item) {
        this.item = item;
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
     * @return the loanTerm
     */
    public Integer getLoanTerm() {
        return loanTerm;
    }

    /**
     * @param loanTerm the loanTerm to set
     */
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    /**
     * @return the termCode
     */
    public String getTermCode() {
        return termCode;
    }

    /**
     * @param termCode the termCode to set
     */
    public void setTermCode(String termCode) {
        this.termCode = termCode;
    }

    /**
     * @return the disbAmount
     */
    public BigDecimal getDisbAmount() {
        return disbAmount;
    }

    /**
     * @param disbAmount the disbAmount to set
     */
    public void setDisbAmount(BigDecimal disbAmount) {
        this.disbAmount = disbAmount;
    }

    /**
     * @return the channel
     */
    public AXChannel getChannel() {
        return channel;
    }

    /**
     * @param channel the channel to set
     */
    public void setChannel(AXChannel channel) {
        this.channel = channel;
    }

    /**
     * @return the loanType
     */
    public LNType getLoanType() {
        return loanType;
    }

    /**
     * @param loanType the loanType to set
     */
    public void setLoanType(LNType loanType) {
        this.loanType = loanType;
    }
}
