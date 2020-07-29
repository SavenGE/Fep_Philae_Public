/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import PHilae.acx.AXIgnore;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Pecherk
 */
public class AXTier implements Serializable, Comparable<AXTier>, Cloneable {

    private Long tierId;
    private String type;
    private BigDecimal value;
    private BigDecimal tierMax = BigDecimal.ZERO;
    private BigDecimal tierMin = BigDecimal.ZERO;

    /**
     * @return the tierId
     */
    public Long getTierId() {
        return tierId;
    }

    /**
     * @param tierId the tierId to set
     */
    public void setTierId(Long tierId) {
        this.tierId = tierId;
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
     * @return the tierMax
     */
    public BigDecimal getTierMax() {
        return tierMax;
    }

    /**
     * @param tierMax the tierMax to set
     */
    public void setTierMax(BigDecimal tierMax) {
        this.tierMax = tierMax;
    }

    /**
     * @return the tierMin
     */
    public BigDecimal getTierMin() {
        return tierMin;
    }

    /**
     * @param tierMin the tierMin to set
     */
    public void setTierMin(BigDecimal tierMin) {
        this.tierMin = tierMin;
    }

    @Override
    public int compareTo(AXTier o) {
        return getTierMax().compareTo(o.getTierMax());
    }

    @Override
    @AXIgnore
    public String toString() {
        return "Tier [ " + getTierMin().toPlainString() + " - " + getTierMax().toPlainString() + " ]";
    }

    @Override
    public AXTier clone() throws CloneNotSupportedException {
        return (AXTier) super.clone();
    }
}
