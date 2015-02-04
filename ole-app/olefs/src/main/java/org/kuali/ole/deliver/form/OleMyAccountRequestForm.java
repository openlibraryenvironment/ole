package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * The OleLoanForm is the form class that defines all the loan fields required for a loan processing using getters and setters
 * and is involved in passing the data to the UI layer
 */
public class OleMyAccountRequestForm extends UifFormBase {

    private String olePatronId;
    private OlePatronDocument olePatronDocument;
    private List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
    private String requestMessage;

    public String getOlePatronId() {
        return olePatronId;
    }

    public void setOlePatronId(String olePatronId) {
        this.olePatronId = olePatronId;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public List<OleDeliverRequestBo> getOleDeliverRequestBos() {
        return oleDeliverRequestBos;
    }

    public void setOleDeliverRequestBos(List<OleDeliverRequestBo> oleDeliverRequestBos) {
        this.oleDeliverRequestBos = oleDeliverRequestBos;
    }
}
