package org.kuali.ole.select.bo;

import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 6/18/13
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEEResourceRequestor extends PersistableBusinessObjectBase {

    private String oleERSRequestorId;
    private String oleERSIdentifier;
    private String requestorId;
    private OlePatronDocument olePatronDocument;

    public String getOleERSRequestorId() {
        return oleERSRequestorId;
    }

    public void setOleERSRequestorId(String oleERSRequestorId) {
        this.oleERSRequestorId = oleERSRequestorId;
    }

    public String getOleERSIdentifier() {
        return oleERSIdentifier;
    }

    public void setOleERSIdentifier(String oleERSIdentifier) {
        this.oleERSIdentifier = oleERSIdentifier;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
    }

    public OlePatronDocument getOlePatronDocument() {
        return olePatronDocument;
    }

    public void setOlePatronDocument(OlePatronDocument olePatronDocument) {
        this.olePatronDocument = olePatronDocument;
    }
}
