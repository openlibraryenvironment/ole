package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.marc4j.marc.Record;

import java.util.StringTokenizer;

/**
 * Created by SheikS on 1/6/2016.
 */
public class DeleteTagStepHandler extends StepHandler {

    /*Eg of source field -  001 or 050 or any thing.*/
    @Override
    public void processSteps(Record marcRecord) {
        String sourceFieldString = getBatchProfileDataTransformer().getSourceField();

        String sourceFieldStringArray[] = sourceFieldString.split("[' ']");
        String sourceField = sourceFieldStringArray[0];
        getMarcRecordUtil().removeFieldFromRecord(marcRecord,sourceField);
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase("Delete Tag");
    }
}
