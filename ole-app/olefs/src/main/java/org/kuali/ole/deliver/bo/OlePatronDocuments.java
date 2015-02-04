package org.kuali.ole.deliver.bo;

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

    private List<OlePatronDocument> olePatronDocuments = new ArrayList<OlePatronDocument>();

    public List<OlePatronDocument> getOlePatronDocuments() {
        return olePatronDocuments;
    }

    public void setOlePatronDocuments(List<OlePatronDocument> olePatronDocuments) {
        this.olePatronDocuments = olePatronDocuments;
    }
}
