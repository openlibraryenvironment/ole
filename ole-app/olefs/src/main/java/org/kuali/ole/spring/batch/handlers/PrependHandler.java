package org.kuali.ole.spring.batch.handlers;

import org.marc4j.marc.Record;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class PrependHandler extends StepHandler {

    @Override
    public void processSteps(Record marcRecord) {

    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase("prepend");
    }
}
