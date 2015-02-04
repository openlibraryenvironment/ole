package org.kuali.ole.docstore.model.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 3/30/12
 * Time: 10:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkInstanceDocument extends OleDocument {

    private String instanceIdentifier;
    private WorkHoldingsDocument holdingsDocument;
    private List<WorkItemDocument> itemDocumentList;
    private String bibIdentifier;

    public WorkInstanceDocument() {
        List<WorkItemDocument> itemDocumentList = new ArrayList<WorkItemDocument>();
    }


    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }


    public void setInstanceIdentifier(String instanceIdentifier) {
        this.instanceIdentifier = instanceIdentifier;
    }


    public WorkHoldingsDocument getHoldingsDocument() {
        return holdingsDocument;
    }

    public void setHoldingsDocument(WorkHoldingsDocument holdingsDocument) {
        this.holdingsDocument = holdingsDocument;
    }

    public List<WorkItemDocument> getItemDocumentList() {
        return itemDocumentList;
    }

    public void setItemDocumentList(List<WorkItemDocument> itemDocumentList) {
        this.itemDocumentList = itemDocumentList;
    }

    public String getBibIdentifier() {
        return bibIdentifier;
    }

    public void setBibIdentifier(String bibIdentifier) {
        this.bibIdentifier = bibIdentifier;
    }

    @Override
    public String toString() {
        return "WorkInstanceDocument{" +
                "instanceIdentifier='" + instanceIdentifier + '\'' +
                ", holdingsDocument=" + holdingsDocument +
                ", itemDocumentList=" + itemDocumentList +
                ", bibIdentifier=" + bibIdentifier +
                '}';
    }

}
