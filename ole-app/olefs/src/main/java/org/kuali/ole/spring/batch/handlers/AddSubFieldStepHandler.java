package org.kuali.ole.spring.batch.handlers;

import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.constants.OleNGConstants;
import org.marc4j.marc.Record;

/**
 * Created by SheikS on 2/16/2016.
 */
public class AddSubFieldStepHandler extends StepHandler {
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

        if(StringUtils.isNotBlank(sourceField)) {
            if(!getMarcRecordUtil().isControlField(sourceField)) {
                value = getMarcRecordUtil().getDataFieldValueWithIndicators(marcRecord, sourceField, ind1, ind2, subField);
            } else {
                value = getMarcRecordUtil().getControlFieldValue(marcRecord, sourceField);
            }
        }

        if(StringUtils.isBlank(value)){
            value = getBatchProfileDataTransformer().getConstant();
        }

        if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(destinationField)) {
            if (!getMarcRecordUtil().isControlField(destinationField) && StringUtils.isNotBlank(destinationSubField)) {
                getMarcRecordUtil().addSubField(marcRecord, destinationField, destinationInd1, destinationInd2, destinationSubField, value);
            }
        }
    }

    @Override
    public Boolean isInterested(String operation) {
        return operation.equalsIgnoreCase(OleNGConstants.ADD_SUBFIELD);

    }
}
