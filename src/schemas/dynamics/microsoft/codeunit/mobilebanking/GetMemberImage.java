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
 *         &lt;element name="idNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
            "idNumber",
            "responseCode",
            "responseMessage",
            "errorMessage"
        })
@XmlRootElement(name = "GetMemberImage")
public class GetMemberImage {

    @XmlElement(required = true)
    protected String idNumber;
    @XmlElement(required = true)
    protected String responseCode;
    @XmlElement(required = true)
    protected String responseMessage;
    @XmlElement(required = true)
    protected String errorMessage;

    /**
     * Gets the value of the idNumber property.
     *
     * @return possible object is {@link String }
     *
     */
    public String getIdNumber() {
        return idNumber;
    }

    /**
     * Sets the value of the idNumber property.
     *
     * @param value allowed object is {@link String }
     *
     */
    public void setIdNumber(String value) {
        this.idNumber = value;
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
