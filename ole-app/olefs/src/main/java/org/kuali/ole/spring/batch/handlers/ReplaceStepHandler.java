package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.oleng.OleNGConstants;
import org.marc4j.marc.Record;

import java.util.StringTokenizer;

/**
 * Created by SheikS on 1/7/2016.
 */
public class ReplaceStepHandler extends StepHandler {

    /*Eg of source field -  001 or 050 $a$b$c or any thing.
     Eg of constant value - ocm or ocm,onc or anything.
        */
    @Override
    public void processSteps(Record marcRecord) {
        String sourceFieldString = getBatchProfileDataTransformer().getSourceField();

        String sourceFieldStringArray[] = sourceFieldString.split(OleNGConstants.SPACE_SPLIT);
        String sourceField = sourceFieldStringArray[0];

        String constantField = getBatchProfileDataTransformer().getConstant();
        if (StringUtils.isBlank(constantField)) {
            constantField = "";
        }
        if (getMarcRecordUtil().isControlField(sourceField)) {
            getMarcRecordUtil().updateControlFieldValue(marcRecord, sourceField, constantField);
        } else {
            String sourceSubField = (sourceFieldStringArray.length > 1 ? sourceFieldStringArray[1] : "");
            getMarcRecordUtil().updateDataFieldValue(marcRecord, sourceField, sourceSubField, constantField);
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.REPLACE);
    }
}
