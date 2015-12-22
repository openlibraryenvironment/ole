package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.marc4j.marc.Record;

import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class RemoveHandler extends StepHandler {

    /*Eg of source field -  001 or 050 $a$b$c or any thing.
     Eg of constant value - ocm or ocm,onc or anything.
        */
    @Override
    public void processSteps(Record marcRecord) {
        String sourceFieldString = getBatchProfileDataTransformer().getSourceField();

        String sourceFieldStringArray[] = sourceFieldString.split("[' ']");
        String sourceField = sourceFieldStringArray[0];

        String constantField = getBatchProfileDataTransformer().getConstant();
        if(getMarcRecordUtil().isControlField(sourceField)) {
            if (StringUtils.isNotBlank(constantField)) {
                StringTokenizer stringTokenizer = new StringTokenizer(constantField, ",");
                while(stringTokenizer.hasMoreTokens()) {
                    String constant = stringTokenizer.nextToken();
                    getMarcRecordUtil().replaceContentInControlField(marcRecord, sourceField, constant, "");
                }
            }

        } else {
            if (StringUtils.isNotBlank(constantField)) {
                StringTokenizer stringTokenizer = new StringTokenizer(constantField, ",");
                while(stringTokenizer.hasMoreTokens()) {
                    String constant = stringTokenizer.nextToken();
                    String sourceSubField = (sourceFieldStringArray.length > 1 ?  sourceFieldStringArray[1] : "");
                    getMarcRecordUtil().replaceContentInDataField(marcRecord, sourceField, sourceSubField, constant, "");
                }
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
       return operation.equalsIgnoreCase("remove");
    }
}
