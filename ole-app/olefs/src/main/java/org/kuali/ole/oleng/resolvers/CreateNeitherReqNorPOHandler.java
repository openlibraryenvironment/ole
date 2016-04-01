package org.kuali.ole.oleng.resolvers;

import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.oleng.handler.CreateNeitherReqNorPOServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;

/**
 * Created by SheikS on 2/16/2016.
 */
public class CreateNeitherReqNorPOHandler extends OrderProcessHandler {

    @Override
    public boolean isInterested(String type) {
        return type.equalsIgnoreCase(OleNGConstants.BatchProcess.CREATE_NEITHER_REQ_NOR_PO);
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateNeitherReqNorPOServiceHandler createNeitherReqNorPOHandler = new CreateNeitherReqNorPOServiceHandler();
        return createNeitherReqNorPOHandler;
    }
}