package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for perpetualAccess complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="perpetualAccess">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="perpetualAccessStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="perpetualAccessStartVolume" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="perpetualAccessStartIssue" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="perpetualAccessEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="perpetualAccessEndVolume" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="perpetualAccessEndIssue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "perpetualAccess", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "perpetualAccessStartDate",
        "perpetualAccessStartVolume",
        "perpetualAccessStartIssue",
        "perpetualAccessEndDate",
        "perpetualAccessEndVolume",
        "perpetualAccessEndIssue",
        "perpetualAccessStartDateString",
        "perpetualAccessStartDateFormat",
        "perpetualAccessEndDateString",
        "perpetualAccessEndDateFormat"
})
@XStreamAlias("perpetualAccess")
public class PerpetualAccess {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String perpetualAccessStartDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String perpetualAccessStartVolume;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String perpetualAccessStartIssue;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String perpetualAccessEndDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String perpetualAccessEndVolume;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String perpetualAccessEndIssue;

    protected String perpetualAccessStartDateString;
    protected String perpetualAccessStartDateFormat;
    protected String perpetualAccessEndDateString;
    protected String perpetualAccessEndDateFormat;
    /**
     * Gets the value of the perpetualAccessStartDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessStartDate() {
        return perpetualAccessStartDate;
    }

    /**
     * Sets the value of the perpetualAccessStartDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessStartDate(String value) {
        this.perpetualAccessStartDate = value;
    }

    /**
     * Gets the value of the perpetualAccessStartVolume property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessStartVolume() {
        return perpetualAccessStartVolume;
    }

    /**
     * Sets the value of the perpetualAccessStartVolume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessStartVolume(String value) {
        this.perpetualAccessStartVolume = value;
    }

    /**
     * Gets the value of the perpetualAccessStartIssue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessStartIssue() {
        return perpetualAccessStartIssue;
    }

    /**
     * Sets the value of the perpetualAccessStartIssue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessStartIssue(String value) {
        this.perpetualAccessStartIssue = value;
    }

    /**
     * Gets the value of the perpetualAccessEndDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessEndDate() {
        return perpetualAccessEndDate;
    }

    /**
     * Sets the value of the perpetualAccessEndDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessEndDate(String value) {
        this.perpetualAccessEndDate = value;
    }

    /**
     * Gets the value of the perpetualAccessEndVolume property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessEndVolume() {
        return perpetualAccessEndVolume;
    }

    /**
     * Sets the value of the perpetualAccessEndVolume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessEndVolume(String value) {
        this.perpetualAccessEndVolume = value;
    }

    /**
     * Gets the value of the perpetualAccessEndIssue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPerpetualAccessEndIssue() {
        return perpetualAccessEndIssue;
    }

    /**
     * Sets the value of the perpetualAccessEndIssue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPerpetualAccessEndIssue(String value) {
        this.perpetualAccessEndIssue = value;
    }

    public String getPerpetualAccessStartDateString() {
        return perpetualAccessStartDateString;
    }

    public void setPerpetualAccessStartDateString(String perpetualAccessStartDateString) {
        this.perpetualAccessStartDateString = perpetualAccessStartDateString;
    }

    public String getPerpetualAccessStartDateFormat() {
        return perpetualAccessStartDateFormat;
    }

    public void setPerpetualAccessStartDateFormat(String perpetualAccessStartDateFormat) {
        this.perpetualAccessStartDateFormat = perpetualAccessStartDateFormat;
    }

    public String getPerpetualAccessEndDateString() {
        return perpetualAccessEndDateString;
    }

    public void setPerpetualAccessEndDateString(String perpetualAccessEndDateString) {
        this.perpetualAccessEndDateString = perpetualAccessEndDateString;
    }

    public String getPerpetualAccessEndDateFormat() {
        return perpetualAccessEndDateFormat;
    }

    public void setPerpetualAccessEndDateFormat(String perpetualAccessEndDateFormat) {
        this.perpetualAccessEndDateFormat = perpetualAccessEndDateFormat;
    }
}
