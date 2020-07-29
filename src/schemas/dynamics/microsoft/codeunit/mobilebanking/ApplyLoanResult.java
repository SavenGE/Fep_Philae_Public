package schemas.dynamics.microsoft.codeunit.mobilebanking;

import java.math.BigDecimal;
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
 *         &lt;element name="requestid" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="phoneNo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="eLoanCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="amount" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="installments" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
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
            "requestid",
            "phoneNo",
            "eLoanCode",
            "amount",
            "installments",
            "responseCode",
            "responseMessage",
            "errorMessage"
        })
@XmlRootElement(name = "ApplyLoan_Result")
public class ApplyLoanResult {

    @XmlElement(required = true)
    protected String requestid;
    @XmlElement(required = true)
    protected String phoneNo;
    @XmlElement(required = true)
    protected String eLoanCode;
    @XmlElement(required = true)
    protected BigDecimal amount;
    @XmlElement(required = true)
    protected BigDecimal installments;
    @XmlElement(required = true)
    protected String responseCode;
    @XmlElement(required = true)
    protected String responseMessage;
    @XmlElement(required = true)
    protected String errorMessage;

    /**
     * Gets the value of the requestid property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getRequestid() {
        return requestid;
    }

    /**
     * Sets the value of the requestid property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setRequestid(String value) {
        this.requestid = value;
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
     * Gets the value of the eLoanCode property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getELoanCode() {
        return eLoanCode;
    }

    /**
     * Sets the value of the eLoanCode property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setELoanCode(String value) {
        this.eLoanCode = value;
    }

    /**
     * Gets the value of the amount property.
     *
     * @return possible object is {@link BigDecimal }
     *
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     *
     * @param value allowed object is {@link BigDecimal }
     *
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the installments property.
     *
     * @return possible object is {@link BigDecimal }
     *
     */
    public BigDecimal getInstallments() {
        return installments;
    }

    /**
     * Sets the value of the installments property.
     *
     * @param value allowed object is {@link BigDecimal }
     *
     */
    public void setInstallments(BigDecimal value) {
        this.installments = value;
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
