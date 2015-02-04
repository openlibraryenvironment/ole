package org.kuali.ole.docstore.common.document.content.bib.marc;



import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.document.ids.BibId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for orderBibMarcRecord complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="orderBibMarcRecord">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bibId" type="{}bib" minOccurs="0"/>
 *         &lt;element name="bibMarcRecord" type="{}bibMarcRecord" minOccurs="0"/>
 *         &lt;element name="failureReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderBibMarcRecord", propOrder = {
        "bibId",
        "bibMarcRecord",
        "failureReason"
})
public class OrderBibMarcRecord {

    protected BibId bibId;
    protected BibMarcRecord bibMarcRecord;
    protected String failureReason;

    public BibId getBibId() {
        return bibId;
    }

    public void setBibId(BibId bibId) {
        this.bibId = bibId;
    }

    public BibMarcRecord getBibMarcRecord() {
        return bibMarcRecord;
    }

    public void setBibMarcRecord(BibMarcRecord bibMarcRecord) {
        this.bibMarcRecord = bibMarcRecord;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
