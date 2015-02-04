package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OleTemporaryCirculationHistory;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * OleTemporaryCirculationRecordsForm is the Form class for temporary circulation history records.
 */
public class OleTemporaryCirculationRecordsForm extends UifFormBase {

    private OlePatronDocument olePatronDocument;
    private List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords;
    private String patronName;
    private List<OleLoanDocument> oleLoanDocuments;

    public List<OleLoanDocument> getOleLoanDocuments() {
        return oleLoanDocuments;
    }

    public void setOleLoanDocuments(List<OleLoanDocument> oleLoanDocuments) {
        this.oleLoanDocuments = oleLoanDocuments;
    }

    public List<OleTemporaryCirculationHistory> getOleTemporaryCirculationHistoryRecords() {
        return oleTemporaryCirculationHistoryRecords;
    }

    public void setOleTemporaryCirculationHistoryRecords(List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords) {
        this.oleTemporaryCirculationHistoryRecords = oleTemporaryCirculationHistoryRecords;
    }

    public OlePatronDocument getOlePatronDocument() {

        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public String getPatronName() {
        return patronName;
    }

    public void setPatronName(String patronName) {
        this.patronName = patronName;
    }
}
