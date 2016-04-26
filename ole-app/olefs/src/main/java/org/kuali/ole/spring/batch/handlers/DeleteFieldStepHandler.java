package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by SheikS on 1/6/2016.
 */
public class DeleteFieldStepHandler extends StepHandler {

    /*Eg of source field -  001 or 050 or any thing.*/
    @Override
    public void processSteps(Record marcRecord) {
        String sourceField = getBatchProfileDataTransformer().getDataField();
        if (StringUtils.isNotBlank(sourceField)) {
            getMarcRecordUtil().removeFieldFromRecord(marcRecord,sourceField);
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.DELETE_FIELD);
    }
}
