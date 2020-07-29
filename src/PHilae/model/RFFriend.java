/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.model;

import java.util.Date;

/**
 *
 * @author Pecherk
 */
public class RFFriend {

    private Long recId;
    private Date createDt;
    private String module;
    private String firstNm;
    private String lastNm;
    private String phoneNo;
    private String gender;
    private String location;
    private String introducer;
    private String recSt;

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
     * @return the createDt
     */
    public Date getCreateDt() {
        return createDt;
    }

    /**
     * @param createDt the createDt to set
     */
    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
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
     * @return the firstNm
     */
    public String getFirstNm() {
        return firstNm;
    }

    /**
     * @param firstNm the firstNm to set
     */
    public void setFirstNm(String firstNm) {
        this.firstNm = firstNm;
    }

    /**
     * @return the lastNm
     */
    public String getLastNm() {
        return lastNm;
    }

    /**
     * @param lastNm the lastNm to set
     */
    public void setLastNm(String lastNm) {
        this.lastNm = lastNm;
    }

    /**
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the introducer
     */
    public String getIntroducer() {
        return introducer;
    }

    /**
     * @param introducer the introducer to set
     */
    public void setIntroducer(String introducer) {
        this.introducer = introducer;
    }

    /**
     * @return the recSt
     */
    public String getRecSt() {
        return recSt;
    }

    /**
     * @param recSt the recSt to set
     */
    public void setRecSt(String recSt) {
        this.recSt = recSt;
    }
}
