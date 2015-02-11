package org.kuali.ole.bo.serachRetrieve;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 5/27/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUResponseRecordData {
    public BibMarcRecords  bibMarcRecords;
    public String bibliographicRecord;
    public List<OleSRUInstanceDocument> holdings;

    public String extraRequestData;

    public String getBibliographicRecord() {
        return bibliographicRecord;
    }

    public void setBibliographicRecord(String bibliographicRecord) {
        this.bibliographicRecord = bibliographicRecord;
    }

    public List<OleSRUInstanceDocument> getHoldings() {
        return holdings;
    }

    public void setHoldings(List<OleSRUInstanceDocument> holdings) {
        this.holdings = holdings;
    }

    public String getExtraRequestData() {
        return extraRequestData;
    }

    public void setExtraRequestData(String extraRequestData) {
        this.extraRequestData = extraRequestData;
    }

    @Override
    public String toString() {
        return "OleSRUResponseRecordData{" +
                "bibliographicRecord='" + bibliographicRecord + '\'' +
                ", holdings=" + holdings +
                ", extraRequestData='" + extraRequestData + '\'' +
                '}';
    }

    public BibMarcRecords getBibMarcRecords() {
        return bibMarcRecords;
    }

    public void setBibMarcRecords(BibMarcRecords bibMarcRecords) {
        this.bibMarcRecords = bibMarcRecords;
    }
}
