package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.pojo.OleOrderRecord;
import org.kuali.ole.select.document.OleRequisitionDocument;

import java.util.List;

/**
 * Created by SheikS on 1/13/2016.
 */
public class CreateRequisitionOnlyHander extends OrderProcessHandler{

    @Override
    public boolean isInterested(List<String> options) {
        return options.contains("11") || options.contains("21");
    }

    @Override
    public Integer processOrder(OleOrderRecord oleOrderRecord) throws Exception {
        OleRequisitionDocument savedRequisitionDocument = createAndSaveRequisitionDocument(oleOrderRecord);
        return savedRequisitionDocument.getPurapDocumentIdentifier();
    }

}
