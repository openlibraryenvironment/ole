package org.kuali.ole.spring.batch.handlers;

import org.marc4j.marc.Record;

/**
 * Created by pvsubrah on 12/21/15.
 */
public class AddDeleteOperationStepHandler extends AddOperationStepHandler {


    @Override
    protected void deleteContent(Record marcRecord, String sourceField, String sourceSubField, String value) {
        if (getMarcRecordUtil().isControlField(sourceField)) {
            getMarcRecordUtil().replaceContentInControlField(marcRecord, sourceField, value, "");
        } else {
            getMarcRecordUtil().replaceContentInDataField(marcRecord,sourceField,sourceSubField,value,"");
        }
    }
}
