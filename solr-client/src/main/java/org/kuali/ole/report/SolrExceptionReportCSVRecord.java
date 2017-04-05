package org.kuali.ole.report;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.io.Serializable;

/**
 * Created by sheiks on 14/02/17.
 */
@CsvRecord(generateHeaderColumns = true, separator = ",", quoting = true, crlf = "UNIX", skipFirstLine = true)
public class SolrExceptionReportCSVRecord  implements Serializable {

    @DataField(pos = 1, columnName = "Document Type")
    private String docType;
    @DataField(pos = 2, columnName = "Bib Id")
    private String bibId;
    @DataField(pos = 3, columnName = "Holdings Id")
    private String holdingId;
    @DataField(pos = 4, columnName = "Item Id")
    private String itemId;
    @DataField(pos = 5, columnName = "Exception Message")
    private String exceptionMessage;

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getBibId() {
        return bibId;
    }

    public void setBibId(String bibId) {
        this.bibId = bibId;
    }

    public String getHoldingId() {
        return holdingId;
    }

    public void setHoldingId(String holdingId) {
        this.holdingId = holdingId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}

