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
public class OleNoticeTypeKeyValuesFinder  extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue(OLEConstants.RECALL_NOTICE,"Recall Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.ONHOLD_NOTICE,"OnHold Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_HOLD_COURTESY,"Hold Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.REQUEST_EXPIRATION_NOTICE,"Request Expiration Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.COURTESY_NOTICE,"Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.OVERDUE_NOTICE,"Overdue Notice"));
        keyValues.add(new ConcreteKeyValue(OLEConstants.NOTICE_LOST,"Lost Notice"));
        return keyValues;
    }
}