package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class DeleteValueStepHandler extends StepHandler {

    /*Eg of source field -  001 or 050 $a$b$c or any thing.
     Eg of constant value - ocm or ocm,onc or anything.
        */
    @Override
    public void processSteps(Record marcRecord) {
        String sourceFieldString = getBatchProfileDataTransformer().getSourceField();

        String sourceFieldStringArray[] = sourceFieldString.split(OleNGConstants.SPACE_SPLIT);
        String sourceField = sourceFieldStringArray[0];

        String constantField = getBatchProfileDataTransformer().getConstant();
        if(getMarcRecordUtil().isControlField(sourceField)) {
            if (StringUtils.isNotBlank(constantField)) {
                StringTokenizer stringTokenizer = new StringTokenizer(constantField, ",");
                while(stringTokenizer.hasMoreTokens()) {
                    String constant = stringTokenizer.nextToken();
                    getMarcRecordUtil().replaceContentInControlField(marcRecord, sourceField, constant, "");
                }
            } else {
                getMarcRecordUtil().replaceContentInControlField(marcRecord, sourceField, getMarcRecordUtil().getControlFieldValue(marcRecord, sourceField), "");
            }

        } else {
            String sourceSubField = (sourceFieldStringArray.length > 1 ?  sourceFieldStringArray[1] : "");
            if (StringUtils.isNotBlank(constantField)) {
                StringTokenizer stringTokenizer = new StringTokenizer(constantField, ",");
                while(stringTokenizer.hasMoreTokens()) {
                    String constant = stringTokenizer.nextToken();
                    getMarcRecordUtil().replaceContentInDataField(marcRecord, sourceField, sourceSubField, constant, "");
                }
            } else {
                String dataFieldValue = getMarcRecordUtil().getDataFieldValue(marcRecord, sourceField,sourceSubField);
                getMarcRecordUtil().replaceContentInDataField(marcRecord, sourceField, sourceSubField, dataFieldValue, "");
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
       return operation.equalsIgnoreCase(OleNGConstants.DELETE_VALUE);
    }
}
