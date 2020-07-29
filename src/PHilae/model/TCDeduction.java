/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.acx.AXIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 *
 * @author Pecherk
 */
public class TCDeduction implements Serializable, Comparable<TCDeduction>, Cloneable {

    private Long recId;
    private String basis;
    private String description;
    private String account;
    private String type;
    private BigDecimal value;
    private HashMap<BigDecimal, AXTier> tiers = new HashMap<>();

    /**
     * @return the recId
     */
    public Long getRecId() {
        return recId;
    }

    /**
     * @param recId the recId to set
     */
    public void setRecId(Long recId) {
        this.recId = recId;
    }

    /**
     * @return the basis
     */
    public String getBasis() {
        return basis;
    }

    /**
     * @param basis the basis to set
     */
    public void setBasis(String basis) {
        this.basis = basis;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the value
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * @return the tiers
     */
    public HashMap<BigDecimal, AXTier> getTiers() {
        return tiers;
    }

    /**
     * @param tiers the tiers to set
     */
    public void setTiers(HashMap<BigDecimal, AXTier> tiers) {
        this.tiers = tiers;
    }

    @Override
    public int compareTo(TCDeduction o) {
        return getBasis().compareTo(o.getBasis());
    }

    @Override
    @AXIgnore
    public String toString() {
        return getRecId() + "~" + getDescription();
    }

    @Override
    public TCDeduction clone() throws CloneNotSupportedException {
        return (TCDeduction) super.clone();
    }
}
