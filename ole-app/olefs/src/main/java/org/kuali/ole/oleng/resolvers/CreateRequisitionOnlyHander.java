package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqOnlyServiceHandler;
import org.kuali.ole.select.document.OleRequisitionDocument;
import org.marc4j.marc.Record;

import java.util.List;
import java.util.Map;

/**
 * Created by SheikS on 1/13/2016.
 */
public class CreateRequisitionOnlyHander extends OrderProcessHandler{

    @Override
    public boolean isInterested(String type) {
        return type.equalsIgnoreCase("Create Requisition Only");
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateReqOnlyServiceHandler createReqOnlyServiceHandler = new CreateReqOnlyServiceHandler();
        createReqOnlyServiceHandler.setOleNGRequisitionService(getOleNGRequisitionService());
        return createReqOnlyServiceHandler;
    }


}
