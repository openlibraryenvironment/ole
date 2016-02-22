package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class PrependHandler extends StepHandler {

    @Override
    public void processSteps(Record marcRecord) {

        String sourceField = getBatchProfileDataTransformer().getDataField();
        String ind1 = getBatchProfileDataTransformer().getInd1();
        String ind2 = getBatchProfileDataTransformer().getInd2();
        String subField = getBatchProfileDataTransformer().getSubField();

        String destinationField = getBatchProfileDataTransformer().getDestDataField();
        String destinationInd1 = getBatchProfileDataTransformer().getDestInd1();
        String destinationInd2 = getBatchProfileDataTransformer().getDestInd2();
        String destinationSubField = getBatchProfileDataTransformer().getDestSubField();

        String value = null;
        if (StringUtils.isNotBlank(sourceField)) {
            if(getMarcRecordUtil().isControlField(sourceField)) {
                value = getMarcRecordUtil().getControlFieldValue(marcRecord, sourceField);
            } else {
                value = getMarcRecordUtil().getDataFieldValueWithIndicators(marcRecord,sourceField,ind1,ind2,subField);
            }
        }

        if(StringUtils.isBlank(value)){
            value = getBatchProfileDataTransformer().getConstant();
        }

        if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(destinationField)) {
            if (!getMarcRecordUtil().isControlField(destinationField) && StringUtils.isNotBlank(destinationSubField)) {
                String dataFieldValue = getMarcRecordUtil().getDataFieldValueWithIndicators(marcRecord, destinationField, destinationInd1, destinationInd2, destinationSubField);
                dataFieldValue = "(" + value + ")" + dataFieldValue;
                getMarcRecordUtil().updateDataFieldValue(marcRecord,destinationField,destinationInd1, destinationInd2, destinationSubField,dataFieldValue);
            } else if(getMarcRecordUtil().isControlField(destinationField)){
                String dataFieldValue = getMarcRecordUtil().getControlFieldValue(marcRecord, destinationField);
                dataFieldValue = "(" + value + ")" + dataFieldValue;
                getMarcRecordUtil().updateControlFieldValue(marcRecord,destinationField, dataFieldValue);
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.PREPEND_WITH_PREFIX);
    }
}
