package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

import java.util.StringTokenizer;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class RemoveValueStepHandler extends StepHandler {
    @Override
    public void processSteps(Record marcRecord) {
        String sourceField = getBatchProfileDataTransformer().getDataField();
        String ind1 = getBatchProfileDataTransformer().getInd1();
        String ind2 = getBatchProfileDataTransformer().getInd2();
        String subField = getBatchProfileDataTransformer().getSubField();

        String constant = getBatchProfileDataTransformer().getConstant();
        if (StringUtils.isNotBlank(sourceField) && StringUtils.isNotBlank(constant)) {
            StringTokenizer stringTokenizer = new StringTokenizer(constant, ",");
            while(stringTokenizer.hasMoreTokens()) {
                String value = stringTokenizer.nextToken();
                if (StringUtils.isNotBlank(value)) {
                    if(getMarcRecordUtil().isControlField(sourceField)) {
                        getMarcRecordUtil().replaceContentInControlField(marcRecord, sourceField, value, "");
                    } else {
                        getMarcRecordUtil().replaceContentInDataField(marcRecord, sourceField, ind1,ind2, subField, value, "");
                    }
                }
            }
        }

    }

    @Override
    public Boolean isInterested(String operation) {
       return operation.equalsIgnoreCase(OleNGConstants.REMOVE_VALUE);
    }
}
