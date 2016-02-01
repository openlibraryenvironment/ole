package org.kuali.ole.spring.batch.processor;

import org.apache.commons.collections.map.HashedMap;
import org.kuali.ole.oleng.batch.profile.model.BatchProcessProfile;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileDataTransformer;
import org.kuali.ole.spring.batch.handlers.*;
import org.marc4j.marc.Record;

import java.util.*;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class StepsProcessor {

    List<StepHandler> stepHandlerList;

    public void processSteps(Record marcRecord, BatchProcessProfile batchProcessProfile) {

        List<BatchProfileDataTransformer> batchProfileDataTransformerList = batchProcessProfile.getBatchProfileDataTransformerList();

        Map<BatchProfileDataTransformer, StepHandler> handlersToProcess = new HashedMap();
        for (int i = 1; i <= batchProfileDataTransformerList.size(); i++) {
            for (Iterator<BatchProfileDataTransformer> iterator = batchProfileDataTransformerList.iterator(); iterator.hasNext(); ) {
                BatchProfileDataTransformer batchProfileDataTransformer = iterator.next();
                StepHandler stepHandler = resolveStepHandler(batchProfileDataTransformer);
                if (batchProfileDataTransformer.getStep() == i) {
                    handlersToProcess.put(batchProfileDataTransformer, stepHandler);
                }
            }

            for (Iterator<BatchProfileDataTransformer> iterator = handlersToProcess.keySet().iterator(); iterator.hasNext(); ) {
                BatchProfileDataTransformer batchProfileDataTransformer = iterator.next();
                StepHandler stepHandler = handlersToProcess.get(batchProfileDataTransformer);
                stepHandler.setBatchProfileDataTransformer(batchProfileDataTransformer);
                stepHandler.processSteps(marcRecord);
            }

            handlersToProcess.clear();
        }

    }

    private StepHandler resolveStepHandler(BatchProfileDataTransformer batchProfileDataTransformer) {
        for (Iterator<StepHandler> iterator = getStepHandlerList().iterator(); iterator.hasNext(); ) {
            StepHandler stepHandler = iterator.next();
            if (stepHandler.isInterested(batchProfileDataTransformer.getOperation())) {
                return stepHandler;
            }
        }
        return null;
    }

    public List<StepHandler> getStepHandlerList() {
        if (null == stepHandlerList) {
            stepHandlerList = new ArrayList<>();
            stepHandlerList.add(new NewStepHandler());
            stepHandlerList.add(new PrependHandler());
            stepHandlerList.add(new DeleteTagStepHandler());
            stepHandlerList.add(new MoveStepHandler());
            stepHandlerList.add(new DeleteValueStepHandler());
            stepHandlerList.add(new ReplaceStepHandler());
        }
        return stepHandlerList;
    }

    public void setStepHandlerList(List<StepHandler> stepHandlerList) {
        this.stepHandlerList = stepHandlerList;
    }

}
