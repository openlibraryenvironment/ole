package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;

import org.kuali.ole.spring.batch.handlers.*;

import java.util.*;

/**
 * Created by SheikS on 11/19/2015.
 */
public class BatchProfileProcessor {

    private List<BatchProcessProfileHandler> batchProcessProfileHandlers;

    public void processProfile(OLEBatchProcessProfileBo oleBatchProcessProfileBo) {

        for (Iterator<BatchProcessProfileHandler> iterator = getBatchProcessProfileHandlers().iterator(); iterator.hasNext(); ) {
            BatchProcessProfileHandler handler = iterator.next();
            if(handler.isInterested(oleBatchProcessProfileBo)){
                handler.setOleBatchProcessProfileBo(oleBatchProcessProfileBo);
                handler.process();
            }
        }
    }

    public List<BatchProcessProfileHandler> getBatchProcessProfileHandlers() {
        if(CollectionUtils.isEmpty(batchProcessProfileHandlers)){
            batchProcessProfileHandlers = new ArrayList<>();
            batchProcessProfileHandlers.add(new BibStatusHandler());
            batchProcessProfileHandlers.add(new ConstantsAndDefaultValueHandler());
            batchProcessProfileHandlers.add(new DataMappingHandler());
            batchProcessProfileHandlers.add(new DeleteFieldHandler());
            batchProcessProfileHandlers.add(new GloballyProtectedFieldHandler());
            batchProcessProfileHandlers.add(new ImportBibliographicRecordHandler());
            batchProcessProfileHandlers.add(new MatchingOverlaysHandler());
            batchProcessProfileHandlers.add(new ProfileProtectedFieldHandler());
            batchProcessProfileHandlers.add(new RenameFieldHandler());
            batchProcessProfileHandlers.add(new StaffOnlyHandler());
        }
        return batchProcessProfileHandlers;
    }

    public void setBatchProcessProfileHandlers(List<BatchProcessProfileHandler> batchProcessProfileHandlers) {
        this.batchProcessProfileHandlers = batchProcessProfileHandlers;
    }
}
