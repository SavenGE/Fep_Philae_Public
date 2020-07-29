/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 *
 * @author Pecherk
 */
public class TXCharge implements Serializable {

    private String code;
    private String narration;
    private String channelLedger;

    private boolean waive;
    private Integer factor = 1;
    private CNAccount chargeAccount = new CNAccount();
    private BigDecimal chargeAmount = BigDecimal.ZERO;

    private ArrayList<TCSplit> splitList = new ArrayList<>();
    private String incomeLedger;
    private String scheme;

    /**
     * @return the incomeLedger
     */
    public String getIncomeLedger() {
        return incomeLedger;
    }

    /**
     * @param incomeLedger the incomeLedger to set
     */
    public void setIncomeLedger(String incomeLedger) {
        this.incomeLedger = incomeLedger;
    }

    /**
     * @return the channelLedger
     */
    public String getChannelLedger() {
        return channelLedger;
    }

    /**
     * @param channelLedger the channelLedger to set
     */
    public void setChannelLedger(String channelLedger) {
        this.channelLedger = channelLedger;
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
     * @return the chargeAccount
     */
    public CNAccount getChargeAccount() {
        return chargeAccount;
    }

    /**
     * @param chargeAccount the chargeAccount to set
     */
    public void setChargeAccount(CNAccount chargeAccount) {
        this.chargeAccount = chargeAccount;
    }

    /**
     * @return the chargeAmount
     */
    public BigDecimal getChargeAmount() {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public void setChargeAmount(BigDecimal chargeAmount) {
        this.chargeAmount = chargeAmount.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @return the splitList
     */
    public ArrayList<TCSplit> getSplitList() {
        return splitList;
    }

    /**
     * @param splitList the splitList to set
     */
    public void setSplitList(ArrayList<TCSplit> splitList) {
        this.splitList = splitList;
    }

    /**
     * @return the scheme
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * @param scheme the scheme to set
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the factor
     */
    public Integer getFactor() {
        return factor;
    }

    /**
     * @param factor the factor to set
     */
    public void setFactor(Integer factor) {
        this.factor = factor;
    }

    /**
     * @return the waive
     */
    public boolean isWaive() {
        return waive;
    }

    /**
     * @param waive the waive to set
     */
    public void setWaive(boolean waive) {
        this.waive = waive;
    }
}
