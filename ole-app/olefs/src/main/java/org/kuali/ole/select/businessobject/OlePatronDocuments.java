package org.kuali.ole.select.businessobject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/19/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronDocuments {

    private List<OLERequestorPatronDocument> olePatronDocuments = new ArrayList<OLERequestorPatronDocument>();

    public List<OLERequestorPatronDocument> getOlePatronDocuments() {
        return olePatronDocuments;
    }

    public void setOlePatronDocuments(List<OLERequestorPatronDocument> olePatronDocuments) {
        this.olePatronDocuments = olePatronDocuments;
    }
}
