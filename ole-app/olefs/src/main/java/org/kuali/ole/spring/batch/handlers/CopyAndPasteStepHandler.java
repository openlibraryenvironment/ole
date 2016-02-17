package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by SheikS on 2/16/2016.
 */
public class CopyAndPasteStepHandler extends StepHandler {
    @Override
    public void processSteps(Record marcRecord) {
        String sourceField = getBatchProfileDataTransformer().getDataField();

        String destinationField = getBatchProfileDataTransformer().getDestDataField();

        if (StringUtils.isNotBlank(sourceField) && StringUtils.isNotBlank(destinationField)) {
            if(getMarcRecordUtil().isControlField(sourceField) && getMarcRecordUtil().isControlField(destinationField)) {
                getMarcRecordUtil().copyControlField(marcRecord,sourceField,destinationField);
            } else if(!getMarcRecordUtil().isControlField(sourceField) && !getMarcRecordUtil().isControlField(destinationField)){
                getMarcRecordUtil().copyDataField(marcRecord,sourceField,destinationField);
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.COPY_PASTE);
    }
}
