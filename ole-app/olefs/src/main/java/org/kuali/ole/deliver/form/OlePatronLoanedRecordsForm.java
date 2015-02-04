package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleLoanDocument;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * OlePatronLoanedRecordsForm is the Form class for Loaned items record
 */
public class OlePatronLoanedRecordsForm extends UifFormBase {

    private OlePatronDocument olePatronDocument;
    private List<OleLoanDocument> loanDocuments;
    private String patronName;

    public List<OleLoanDocument> getLoanDocuments() {
        return loanDocuments;
    }

    public void setLoanDocuments(List<OleLoanDocument> loanDocuments) {
        this.loanDocuments = loanDocuments;
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
