package org.kuali.ole.deliver.bo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/15/12
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OlePatronConfig {
    List<OleConfigDocument> documents;

    /**
     * Gets the value of documents property
     *
     * @return documents
     */
    public List<OleConfigDocument> getDocuments() {
        return documents;
    }

    /**
     * Sets the value for documents property
     *
     * @param documents
     */
    public void setDocuments(List<OleConfigDocument> documents) {
        this.documents = documents;
    }
}
