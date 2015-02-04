package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.List;

/**
 * OlePatronRequestedRecordsForm is the Form class for requested items record
 */
public class OlePatronRequestedRecordsForm extends UifFormBase {

    private OlePatronDocument olePatronDocument;
    private List<OleDeliverRequestBo> requestBos;
    private String patronName;

    public List<OleDeliverRequestBo> getRequestBos() {
        return requestBos;
    }

    public void setRequestBos(List<OleDeliverRequestBo> requestBos) {
        this.requestBos = requestBos;
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
