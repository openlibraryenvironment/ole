package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by SheikS on 1/7/2016.
 */
public class ReplaceStepHandler extends StepHandler {

    /*Eg of source field -  001 or 050 $a$b$c or any thing.
     Eg of constant value - ocm or ocm,onc or anything.
        */
    @Override
    public void processSteps(Record marcRecord) {

        String sourceField = getBatchProfileDataTransformer().getDataField();
        String ind1 = getBatchProfileDataTransformer().getInd1();
        String ind2 = getBatchProfileDataTransformer().getInd2();
        String subField = getBatchProfileDataTransformer().getSubField();

        String constantField = getBatchProfileDataTransformer().getConstant();
        if (StringUtils.isBlank(constantField)) {
            constantField = "";
        }
        if (StringUtils.isNotBlank(sourceField)) {
            if (getMarcRecordUtil().isControlField(sourceField)) {
                getMarcRecordUtil().updateControlFieldValue(marcRecord, sourceField, constantField);
            } else {
                getMarcRecordUtil().updateDataFieldValue(marcRecord, sourceField, ind1, ind2, subField, constantField);
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.REPLACE);
    }
}
