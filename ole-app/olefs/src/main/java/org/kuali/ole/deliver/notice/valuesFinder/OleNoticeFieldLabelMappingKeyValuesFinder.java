package org.kuali.ole.deliver.notice.valuesFinder;

import org.kuali.ole.OLEConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 7/7/15.
 */
public class OleNoticeFieldLabelMappingKeyValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(OLEConstants.PATRON_INFORMATION,OLEConstants.PATRON_INFORMATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.PATRON_NAME,OLEConstants.PATRON_NAME));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ADDRESS,OLEConstants.NOTICE_ADDRESS));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_EMAIL,OLEConstants.NOTICE_EMAIL));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_PHONE_NUMBER,OLEConstants.NOTICE_PHONE_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.TITLE_ITEM_INFORMATION,OLEConstants.TITLE_ITEM_INFORMATION));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME,OLEConstants.CIRCULATION_LOCATION_LIBRARY_NAME));
        keyValues.add(new ConcreteKeyValue(OLEConstants.CIRCULATION_REPLY_TO_EMAIL,OLEConstants.CIRCULATION_REPLY_TO_EMAIL));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_TITLE,OLEConstants.NOTICE_TITLE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_AUTHOR,OLEConstants.NOTICE_AUTHOR));
        keyValues.add(new ConcreteKeyValue(OLEConstants.VOLUME_ISSUE_COPY,OLEConstants.VOLUME_ISSUE_COPY));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_CALL_NUMBER,OLEConstants.NOTICE_CALL_NUMBER));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_ITEM_BARCODE,OLEConstants.NOTICE_ITEM_BARCODE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ORIGINAL_DUE_DATE,OLEConstants.ORIGINAL_DUE_DATE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NEW_DUE_DATE,OLEConstants.NEW_DUE_DATE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ITEM_WAS_DUE,OLEConstants.ITEM_WAS_DUE));
        keyValues.add(new ConcreteKeyValue(OLEConstants.HOLD_EXPIRATION_DATE,OLEConstants.HOLD_EXPIRATION_DATE));
        return keyValues;
    }
}