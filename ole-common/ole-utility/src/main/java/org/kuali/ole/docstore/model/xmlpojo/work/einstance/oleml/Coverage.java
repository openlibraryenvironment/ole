package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for coverage complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="coverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="coverageStartDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coverageStartVolume" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coverageStartIssue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coverageEndDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coverageEndVolume" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="coverageEndIssue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "coverage", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "coverageStartDate",
        "coverageStartVolume",
        "coverageStartIssue",
        "coverageEndDate",
        "coverageEndVolume",
        "coverageEndIssue"
})
@XStreamAlias("coverage")
public class Coverage {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageStartDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageStartVolume;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageStartIssue;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageEndDate;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageEndVolume;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String coverageEndIssue;

    protected String coverageStartDateString;
    protected String coverageStartDateFormat;
    protected String coverageEndDateString;
    protected String coverageEndDateFormat;

    /**
     * Gets the value of the coverageStartDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageStartDate() {
        return coverageStartDate;
    }

    /**
     * Sets the value of the coverageStartDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageStartDate(String value) {
        this.coverageStartDate = value;
    }

    /**
     * Gets the value of the coverageStartVolume property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageStartVolume() {
        return coverageStartVolume;
    }

    /**
     * Sets the value of the coverageStartVolume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageStartVolume(String value) {
        this.coverageStartVolume = value;
    }

    /**
     * Gets the value of the coverageStartIssue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageStartIssue() {
        return coverageStartIssue;
    }

    /**
     * Sets the value of the coverageStartIssue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageStartIssue(String value) {
        this.coverageStartIssue = value;
    }

    /**
     * Gets the value of the coverageEndDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageEndDate() {
        return coverageEndDate;
    }

    /**
     * Sets the value of the coverageEndDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageEndDate(String value) {
        this.coverageEndDate = value;
    }

    /**
     * Gets the value of the coverageEndVolume property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageEndVolume() {
        return coverageEndVolume;
    }

    /**
     * Sets the value of the coverageEndVolume property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageEndVolume(String value) {
        this.coverageEndVolume = value;
    }

    /**
     * Gets the value of the coverageEndIssue property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCoverageEndIssue() {
        return coverageEndIssue;
    }

    /**
     * Sets the value of the coverageEndIssue property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCoverageEndIssue(String value) {
        this.coverageEndIssue = value;
    }

    public String getCoverageStartDateString() {
        return coverageStartDateString;
    }

    public void setCoverageStartDateString(String coverageStartDateString) {
        this.coverageStartDateString = coverageStartDateString;
    }

    public String getCoverageStartDateFormat() {
        return coverageStartDateFormat;
    }

    public void setCoverageStartDateFormat(String coverageStartDateFormat) {
        this.coverageStartDateFormat = coverageStartDateFormat;
    }

    public String getCoverageEndDateString() {
        return coverageEndDateString;
    }

    public void setCoverageEndDateString(String coverageEndDateString) {
        this.coverageEndDateString = coverageEndDateString;
    }

    public String getCoverageEndDateFormat() {
        return coverageEndDateFormat;
    }

    public void setCoverageEndDateFormat(String coverageEndDateFormat) {
        this.coverageEndDateFormat = coverageEndDateFormat;
    }
}
