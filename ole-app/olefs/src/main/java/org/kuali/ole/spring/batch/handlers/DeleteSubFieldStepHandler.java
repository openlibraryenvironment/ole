package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by SheikS on 2/16/2016.
 */
public class DeleteSubFieldStepHandler extends StepHandler {

    @Override
    public void processSteps(Record marcRecord) {
        String sourceField = getBatchProfileDataTransformer().getDataField();
        String ind1 = getBatchProfileDataTransformer().getInd1();
        String ind2 = getBatchProfileDataTransformer().getInd2();
        String subField = getBatchProfileDataTransformer().getSubField();
        if (StringUtils.isNotBlank(sourceField) && StringUtils.isNotBlank(subField) &&
                !getMarcRecordUtil().isControlField(sourceField)) {
            getMarcRecordUtil().removeSubField(marcRecord,sourceField, ind1, ind2, subField);
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.DELETE_SUBFIELD);
    }
}
