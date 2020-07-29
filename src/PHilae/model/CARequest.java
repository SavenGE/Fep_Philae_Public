/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

/**
 *
 * @author Pecherk
 */
public class CARequest {

    private Long productId;
    private String customerNumber;
    private String accountTitle;
    private Long campaignRefId;
    private Long relationshipOfficerId;
    private Long openingReasonId;
    private Long sourceOfFundId;
    private Long riskClassId;
    private USRole userRole;

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
     * @return the customerNumber
     */
    public String getCustomerNumber() {
        return customerNumber;
    }

    /**
     * @param customerNumber the customerNumber to set
     */
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**
     * @return the accountTitle
     */
    public String getAccountTitle() {
        return accountTitle;
    }

    /**
     * @param accountTitle the accountTitle to set
     */
    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    /**
     * @return the campaignRefId
     */
    public Long getCampaignRefId() {
        return campaignRefId;
    }

    /**
     * @param campaignRefId the campaignRefId to set
     */
    public void setCampaignRefId(Long campaignRefId) {
        this.campaignRefId = campaignRefId;
    }

    /**
     * @return the relationshipOfficerId
     */
    public Long getRelationshipOfficerId() {
        return relationshipOfficerId;
    }

    /**
     * @param relationshipOfficerId the relationshipOfficerId to set
     */
    public void setRelationshipOfficerId(Long relationshipOfficerId) {
        this.relationshipOfficerId = relationshipOfficerId;
    }

    /**
     * @return the openingReasonId
     */
    public Long getOpeningReasonId() {
        return openingReasonId;
    }

    /**
     * @param openingReasonId the openingReasonId to set
     */
    public void setOpeningReasonId(Long openingReasonId) {
        this.openingReasonId = openingReasonId;
    }

    /**
     * @return the sourceOfFundId
     */
    public Long getSourceOfFundId() {
        return sourceOfFundId;
    }

    /**
     * @param sourceOfFundId the sourceOfFundId to set
     */
    public void setSourceOfFundId(Long sourceOfFundId) {
        this.sourceOfFundId = sourceOfFundId;
    }

    /**
     * @return the riskClassId
     */
    public Long getRiskClassId() {
        return riskClassId;
    }

    /**
     * @param riskClassId the riskClassId to set
     */
    public void setRiskClassId(Long riskClassId) {
        this.riskClassId = riskClassId;
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
}
