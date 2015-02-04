package org.kuali.ole.select.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 2/10/14
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingDocuments {
    private List<OLESerialReceivingDocument> oleSerialReceivingDocuments = new ArrayList<OLESerialReceivingDocument>();

    public List<OLESerialReceivingDocument> getOleSerialReceivingDocuments() {
        return oleSerialReceivingDocuments;
    }

    public void setOleSerialReceivingDocuments(List<OLESerialReceivingDocument> oleSerialReceivingDocuments) {
        this.oleSerialReceivingDocuments = oleSerialReceivingDocuments;
    }
}
