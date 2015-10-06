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
        return keyValues;
    }
}