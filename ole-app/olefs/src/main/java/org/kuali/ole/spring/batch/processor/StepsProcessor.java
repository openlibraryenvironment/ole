package org.kuali.ole.spring.batch.processor;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
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
    Map<String, List<StepHandler>> steps;

    public void processSteps(Record marcRecord, BatchProcessProfile batchProcessProfile) {

        List<BatchProfileDataTransformer> batchProfileDataTransformerList = batchProcessProfile.getBatchProfileDataTransformerList();

        Collections.sort(batchProfileDataTransformerList, new BatchProfileDataTransformerComparator());

        for (Iterator<BatchProfileDataTransformer> iterator = batchProfileDataTransformerList.iterator(); iterator.hasNext(); ) {
            BatchProfileDataTransformer batchProfileDataTransformer = iterator.next();
            String destinationFieldString = batchProfileDataTransformer.getDestinationField();
            if(StringUtils.isEmpty(destinationFieldString)){
               destinationFieldString = batchProfileDataTransformer.getConstant();
            }

            if (StringUtils.isNotEmpty(destinationFieldString)) {
                StringTokenizer stringTokenizer = new StringTokenizer(destinationFieldString, ",");
                while(stringTokenizer.hasMoreTokens()){
                    String destinationField = stringTokenizer.nextToken();
                    if(getSteps().containsKey(destinationField)){
                        getSteps().get(destinationField).add(resolveStepHandler(batchProfileDataTransformer));
                    } else {
                        ArrayList<StepHandler> stepHandlers = new ArrayList<>();
                        stepHandlers.add(resolveStepHandler(batchProfileDataTransformer));
                        getSteps().put(destinationField, stepHandlers);
                    }
                }
            }
        }


        List<StepHandler> handlersToProcess = new ArrayList<>();
        for(int i = 1; i <= steps.size(); i++){
            for (Iterator iterator = getSteps().keySet().iterator(); iterator.hasNext(); ) {
                String key = (String) iterator.next();
                List<StepHandler> stepHandlers = steps.get(key);
//                Collections.sort(stepHandlers, new StepHandlerComparator());
                for (Iterator<StepHandler> stepHandlerIterator = stepHandlers.iterator(); stepHandlerIterator.hasNext(); ) {
                    StepHandler stepHandler = stepHandlerIterator.next();
                    if(stepHandler.getBatchProfileDataTransformer().getStep() == i){
                        handlersToProcess.add(stepHandler);
                    }
                }
            }
            for (Iterator<StepHandler> iterator = handlersToProcess.iterator(); iterator.hasNext(); ) {
                StepHandler handler = iterator.next();
                handler.processSteps(marcRecord);
            }
        }



        System.out.println();

    }

    private StepHandler resolveStepHandler(BatchProfileDataTransformer batchProfileDataTransformer) {
        for (Iterator<StepHandler> iterator = getStepHandlerList().iterator(); iterator.hasNext(); ) {
            StepHandler stepHandler = iterator.next();
            if(stepHandler.isInterested(batchProfileDataTransformer.getOperation())){
                stepHandler.setBatchProfileDataTransformer(batchProfileDataTransformer);
                return stepHandler;
            }
        }
        return null;
    }

    public List<StepHandler> getStepHandlerList() {
        if (null == stepHandlerList) {
            stepHandlerList = new ArrayList<>();
            stepHandlerList.add(new AddDeleteOperationStepHandler());
            stepHandlerList.add(new PrependHandler());
            stepHandlerList.add(new RemoveHandler());
        }
        return stepHandlerList;
    }

    public void setStepHandlerList(List<StepHandler> stepHandlerList) {
        this.stepHandlerList = stepHandlerList;
    }


    public Map<String, List<StepHandler>> getSteps() {
        if (null == steps) {
            steps = new TreeMap<>();
        }

        return steps;
    }

    public void setSteps(Map<String, List<StepHandler>> steps) {
        this.steps = steps;
    }
}
