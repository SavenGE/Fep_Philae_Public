/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.APController;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 *
 * @author Pecherk
 */
public class TCValue implements Serializable, Comparable<TCValue>, Cloneable {

    private Long recId;
    private String type = "C";
    private BigDecimal min = BigDecimal.ZERO;
    private BigDecimal max = BigDecimal.ZERO;
    private BigDecimal value = BigDecimal.ZERO;
    private HashMap<BigDecimal, AXTier> tiers = new HashMap<>();
    private HashMap<Long, TCDeduction> deductions = new HashMap<>();
    private String currency = APController.getCurrency().getCode();

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
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the min
     */
    public BigDecimal getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(BigDecimal min) {
        this.min = min.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the max
     */
    public BigDecimal getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(BigDecimal max) {
        this.max = max.setScale(2, RoundingMode.HALF_UP);
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
     * @return the deductions
     */
    public HashMap<Long, TCDeduction> getDeductions() {
        return deductions;
    }

    /**
     * @param deductions the deductions to set
     */
    public void setDeductions(HashMap<Long, TCDeduction> deductions) {
        this.deductions = deductions;
    }

    @Override
    public int compareTo(TCValue o) {
        return getRecId().compareTo(o.getRecId());
    }

    @Override
    public TCValue clone() throws CloneNotSupportedException {
        TCValue clone = (TCValue) super.clone();
        clone.setTiers((HashMap) getTiers().clone());
        clone.setDeductions((HashMap) getDeductions().clone());
        return clone;
    }
}
