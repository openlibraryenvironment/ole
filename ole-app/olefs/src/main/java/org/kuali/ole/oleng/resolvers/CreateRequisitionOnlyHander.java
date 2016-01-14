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
    public boolean isInterested(List<String> options, Boolean matchedRecords, Boolean unMatchedRecords) {
        return (options.contains("11") && matchedRecords) || (options.contains("21") && unMatchedRecords);
    }

    @Override
    public CreateReqAndPOBaseServiceHandler getCreateReqOrPOServiceHandler() {
        CreateReqOnlyServiceHandler createReqOnlyServiceHandler = new CreateReqOnlyServiceHandler();
        createReqOnlyServiceHandler.setOleNGRequisitionService(getOleNGRequisitionService());
        return createReqOnlyServiceHandler;
    }


}
