package org.kuali.ole.docstore.common.document.content.instance;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by hemalathas on 3/24/15.
 */

/**
 * (R)
 * Does not map to MFHD. Identifies types of locally defined statistical categories.
 * Example:
 * codeValue=STRVIDEO
 * fullValue=Streaming Video
 * typeOrSource=Can be a pointer to LOC to pull down pre-defined list
 * <p/>
 * <p/>
 * <p>Java class for statisticalSearchingCode complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="statisticalSearchingCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codeValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fullValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="typeOrSource" type="{http://ole.kuali.org/standards/ole-instance}typeOrSource"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemDamagedRecord", propOrder = {
        "damagedItemNote",
        "damagedItemDate",
        "operatorId",
        "patronBarcode",
        "itemId",
        "damagedPatronId",
        "damagedPatronUrl"
})
public class ItemDamagedRecord {

    @XmlElement(required = true)
    protected String damagedItemNote;
    @XmlElement(required = true)
    protected String damagedItemDate;
    @XmlElement(required = true)
    protected String operatorId;
    @XmlElement(required = true)
    protected String patronBarcode;
    @XmlElement(required = true)
    protected String itemId;
    @XmlElement(required = true)
    protected String damagedPatronId;
    protected String damagedPatronUrl;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDamagedItemNote() {
        return damagedItemNote;
    }

    public void setDamagedItemNote(String damagedItemNote) {
        this.damagedItemNote = damagedItemNote;
    }

    public String getDamagedItemDate() {
        return damagedItemDate;
    }

    public void setDamagedItemDate(String damagedItemDate) {
        this.damagedItemDate = damagedItemDate;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getPatronBarcode() {
        return patronBarcode;
    }

    public void setPatronBarcode(String patronBarcode) {
        this.patronBarcode = patronBarcode;
    }

    public String getDamagedPatronId() {
        return damagedPatronId;
    }

    public void setDamagedPatronId(String damagedPatronId) {
        this.damagedPatronId = damagedPatronId;
    }

    public String getDamagedPatronUrl() {
        return damagedPatronUrl;
    }

    public void setDamagedPatronUrl(String damagedPatronUrl) {
        this.damagedPatronUrl = damagedPatronUrl;
    }
}
