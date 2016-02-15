package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;

import java.util.List;

/**
 * Created by SheikS on 1/13/2016.
 */
public class CreateRequisitionAndPurchaseOrderHander extends OrderProcessHandler {

    @Override
    public boolean isInterested(String type) {
        return type.equalsIgnoreCase("Create Requisition and PO");
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateReqAndPOServiceHandler createReqAndPOServiceHandler = new CreateReqAndPOServiceHandler();
        createReqAndPOServiceHandler.setOleNGRequisitionService(getOleNGRequisitionService());
        return createReqAndPOServiceHandler;
    }


}
