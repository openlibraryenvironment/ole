package org.kuali.ole.oleng.resolvers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.callable.POCallable;
import org.kuali.ole.oleng.handler.CreateReqAndPOBaseServiceHandler;
import org.kuali.ole.oleng.handler.CreateReqAndPOServiceHandler;
import org.marc4j.marc.Record;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

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
