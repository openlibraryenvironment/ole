package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;

import java.util.List;

/**
 * Created by SheikS on 1/13/2016.
 */
public class CreateRequisitionAndPurchaseOrderHander extends OrderProcessHandler {

    @Override
    public boolean isInterested(List<String> options, Boolean matchedRecords, Boolean unMatchedRecords) {
        return (options.contains("12") && matchedRecords) || (options.contains("22") && unMatchedRecords);
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateReqAndPOServiceHandler createReqAndPOServiceHandler = new CreateReqAndPOServiceHandler();
        createReqAndPOServiceHandler.setOleNGRequisitionService(getOleNGRequisitionService());
        return createReqAndPOServiceHandler;
    }


}
