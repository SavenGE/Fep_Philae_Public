/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import java.math.BigDecimal;

/**
 *
 * @author Pecherk
 */
public class CPRequest {

    private String account;
    private String currency;
    private BigDecimal amount;
    private BigDecimal[] denominationAmount;
    private BigDecimal[] denominationCount;
    private String narration;
    private USRole userRole;
    private TLDrawer drawer;

    /**
     * @return the account
     */
    public String getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
     * @return the userRole
     */
    public USRole getUserRole() {
        return userRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(USRole userRole) {
        this.userRole = userRole;
    }

    /**
     * @return the drawer
     */
    public TLDrawer getDrawer() {
        return drawer;
    }

    /**
     * @param drawer the drawer to set
     */
    public void setDrawer(TLDrawer drawer) {
        this.drawer = drawer;
    }

    /**
     * @return the denominationAmount
     */
    public BigDecimal[] getDenominationAmount() {
        return denominationAmount;
    }

    /**
     * @param denominationAmount the denominationAmount to set
     */
    public void setDenominationAmount(BigDecimal[] denominationAmount) {
        this.denominationAmount = denominationAmount;
    }

    /**
     * @return the denominationCount
     */
    public BigDecimal[] getDenominationCount() {
        return denominationCount;
    }

    /**
     * @param denominationCount the denominationCount to set
     */
    public void setDenominationCount(BigDecimal[] denominationCount) {
        this.denominationCount = denominationCount;
    }
}
