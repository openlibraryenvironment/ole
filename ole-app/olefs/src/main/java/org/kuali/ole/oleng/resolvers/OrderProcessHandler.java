package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.service.OleNGRequisitionService;
import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;

import java.util.List;

/**
 * Created by SheikS on 1/13/2016.
 */
public abstract class OrderProcessHandler {

    protected OleNGRequisitionService oleNGRequisitionService;

    public abstract boolean isInterested(List<String> options);

    public abstract Integer processOrder(OleOrderRecord oleOrderRecord) throws Exception;

    protected OleRequisitionDocument createAndSaveRequisitionDocument(OleOrderRecord oleOrderRecord) throws Exception {
        OleRequisitionDocument newRequisitionDocument = getOleNGRequisitionService().createNewRequisitionDocument();
        newRequisitionDocument = getOleNGRequisitionService().setValueToRequisitionDocuemnt(newRequisitionDocument, oleOrderRecord);
        return getOleNGRequisitionService().saveRequsitionDocument(newRequisitionDocument);
    }

    public OleNGRequisitionService getOleNGRequisitionService() {
        return oleNGRequisitionService;
    }

    public void setOleNGRequisitionService(OleNGRequisitionService oleNGRequisitionService) {
        this.oleNGRequisitionService = oleNGRequisitionService;
    }
}
