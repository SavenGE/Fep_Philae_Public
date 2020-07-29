package schemas.dynamics.microsoft.codeunit.mobilebanking;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="iDNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="middleName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="branchCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="surname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="pinNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="gender" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="occupation" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="phoneNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="passportPhoto" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="frontID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="backID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dateOfBirth" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="maritalStatus" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="applicationNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="hudumaNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="residence" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="alternativeContact" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="refererPhoneNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="responseCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="responseMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder
        = {
            "idNo",
            "firstName",
            "middleName",
            "branchCode",
            "surname",
            "pinNo",
            "address",
            "gender",
            "occupation",
            "phoneNo",
            "email",
            "passportPhoto",
            "frontID",
            "backID",
            "signature",
            "dateOfBirth",
            "maritalStatus",
            "applicationNo",
            "hudumaNumber",
            "residence",
            "alternativeContact",
            "refererPhoneNo",
            "responseCode",
            "responseMessage",
            "errorMessage"
        })
@XmlRootElement(name = "MemberRegistration")
public class MemberRegistration {

    @XmlElement(name = "iDNo", required = true)
    protected String idNo;
    @XmlElement(required = true)
    protected String firstName;
    @XmlElement(required = true)
    protected String middleName;
    @XmlElement(required = true)
    protected String branchCode;
    @XmlElement(required = true)
    protected String surname;
    @XmlElement(required = true)
    protected String pinNo;
    @XmlElement(required = true)
    protected String address;
    @XmlElement(required = true)
    protected String gender;
    @XmlElement(required = true)
    protected String occupation;
    @XmlElement(required = true)
    protected String phoneNo;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    protected String passportPhoto;
    @XmlElement(required = true)
    protected String frontID;
    @XmlElement(required = true)
    protected String backID;
    @XmlElement(required = true)
    protected String signature;
    @XmlElement(required = true)
    protected String dateOfBirth;
    @XmlElement(required = true)
    protected String maritalStatus;
    @XmlElement(required = true)
    protected String applicationNo;
    @XmlElement(required = true)
    protected String hudumaNumber;
    @XmlElement(required = true)
    protected String residence;
    @XmlElement(required = true)
    protected String alternativeContact;
    @XmlElement(required = true)
    protected String refererPhoneNo;
    @XmlElement(required = true)
    protected String responseCode;
    @XmlElement(required = true)
    protected String responseMessage;
    @XmlElement(required = true)
    protected String errorMessage;

    /**
     * Gets the value of the idNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIDNo() {
        return idNo;
    }

    /**
     * Sets the value of the idNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIDNo(String value) {
        this.idNo = value;
    }

    /**
     * Gets the value of the firstName property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the middleName property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the branchCode property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * Sets the value of the branchCode property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setBranchCode(String value) {
        this.branchCode = value;
    }

    /**
     * Gets the value of the surname property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets the value of the surname property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setSurname(String value) {
        this.surname = value;
    }

    /**
     * Gets the value of the pinNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPinNo() {
        return pinNo;
    }

    /**
     * Sets the value of the pinNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPinNo(String value) {
        this.pinNo = value;
    }

    /**
     * Gets the value of the address property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the gender property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the occupation property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the value of the occupation property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setOccupation(String value) {
        this.occupation = value;
    }

    /**
     * Gets the value of the phoneNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * Sets the value of the phoneNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPhoneNo(String value) {
        this.phoneNo = value;
    }

    /**
     * Gets the value of the email property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the passportPhoto property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getPassportPhoto() {
        return passportPhoto;
    }

    /**
     * Sets the value of the passportPhoto property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setPassportPhoto(String value) {
        this.passportPhoto = value;
    }

    /**
     * Gets the value of the frontID property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getFrontID() {
        return frontID;
    }

    /**
     * Sets the value of the frontID property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setFrontID(String value) {
        this.frontID = value;
    }

    /**
     * Gets the value of the backID property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getBackID() {
        return backID;
    }

    /**
     * Sets the value of the backID property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setBackID(String value) {
        this.backID = value;
    }

    /**
     * Gets the value of the signature property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the maritalStatus property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the applicationNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getApplicationNo() {
        return applicationNo;
    }

    /**
     * Sets the value of the applicationNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setApplicationNo(String value) {
        this.applicationNo = value;
    }

    /**
     * Gets the value of the hudumaNumber property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getHudumaNumber() {
        return hudumaNumber;
    }

    /**
     * Sets the value of the hudumaNumber property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setHudumaNumber(String value) {
        this.hudumaNumber = value;
    }

    /**
     * Gets the value of the residence property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getResidence() {
        return residence;
    }

    /**
     * Sets the value of the residence property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setResidence(String value) {
        this.residence = value;
    }

    /**
     * Gets the value of the alternativeContact property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getAlternativeContact() {
        return alternativeContact;
    }

    /**
     * Sets the value of the alternativeContact property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setAlternativeContact(String value) {
        this.alternativeContact = value;
    }

    /**
     * Gets the value of the refererPhoneNo property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRefererPhoneNo() {
        return refererPhoneNo;
    }

    /**
     * Sets the value of the refererPhoneNo property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRefererPhoneNo(String value) {
        this.refererPhoneNo = value;
    }

    /**
     * Gets the value of the responseCode property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getResponseCode() {
        return responseCode;
    }

    /**
     * Sets the value of the responseCode property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setResponseCode(String value) {
        this.responseCode = value;
    }

    /**
     * Gets the value of the responseMessage property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * Sets the value of the responseMessage property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

    /**
     * Gets the value of the errorMessage property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }
}
