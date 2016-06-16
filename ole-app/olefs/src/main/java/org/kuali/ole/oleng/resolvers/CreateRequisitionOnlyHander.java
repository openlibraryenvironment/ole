package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqOnlyServiceHandler;

/**
 * Created by SheikS on 1/13/2016.
 */
public class CreateRequisitionOnlyHander extends OrderProcessHandler{

    @Override
    public boolean isInterested(String type) {
        return type.equalsIgnoreCase(OleNGConstants.BatchProcess.CREATE_REQ_ONLY);
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateReqOnlyServiceHandler createReqOnlyServiceHandler = new CreateReqOnlyServiceHandler();
        createReqOnlyServiceHandler.setOleNGRequisitionService(getOleNGRequisitionService());
        return createReqOnlyServiceHandler;
    }


}
