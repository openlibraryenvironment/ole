package org.kuali.ole.docstore.common.document.content.instance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by angelind on 3/24/15.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemClaimsReturnedRecord", propOrder = {
        "claimsReturnedFlagCreateDate",
        "claimsReturnedNote",
        "claimsReturnedPatronBarcode",
        "claimsReturnedOperatorId",
        "itemId",
        "claimsReturnedPatronId",
        "claimsReturnedPatronUrl"
})
public class ItemClaimsReturnedRecord {

    @XmlElement(required = true)
    protected String claimsReturnedFlagCreateDate;
    @XmlElement(required = true)
    protected String claimsReturnedNote;
    @XmlElement(required = true)
    protected String claimsReturnedPatronBarcode;
    @XmlElement(required = true)
    protected String claimsReturnedOperatorId;
    @XmlElement(required = true)
    protected String itemId;
    @XmlElement(required = true)
    protected String claimsReturnedPatronId;
    protected String claimsReturnedPatronUrl;

    public String getClaimsReturnedFlagCreateDate() {
        return claimsReturnedFlagCreateDate;
    }

    public void setClaimsReturnedFlagCreateDate(String claimsReturnedFlagCreateDate) {
        this.claimsReturnedFlagCreateDate = claimsReturnedFlagCreateDate;
    }

    public String getClaimsReturnedNote() {
        return claimsReturnedNote;
    }

    public void setClaimsReturnedNote(String claimsReturnedNote) {
        this.claimsReturnedNote = claimsReturnedNote;
    }

    public String getClaimsReturnedPatronBarcode() {
        return claimsReturnedPatronBarcode;
    }

    public void setClaimsReturnedPatronBarcode(String claimsReturnedPatronBarcode) {
        this.claimsReturnedPatronBarcode = claimsReturnedPatronBarcode;
    }

    public String getClaimsReturnedOperatorId() {
        return claimsReturnedOperatorId;
    }

    public void setClaimsReturnedOperatorId(String claimsReturnedOperatorId) {
        this.claimsReturnedOperatorId = claimsReturnedOperatorId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getClaimsReturnedPatronId() {
        return claimsReturnedPatronId;
    }

    public void setClaimsReturnedPatronId(String claimsReturnedPatronId) {
        this.claimsReturnedPatronId = claimsReturnedPatronId;
    }

    public String getClaimsReturnedPatronUrl() {
        return claimsReturnedPatronUrl;
    }

    public void setClaimsReturnedPatronUrl(String claimsReturnedPatronUrl) {
        this.claimsReturnedPatronUrl = claimsReturnedPatronUrl;
    }
}
