package org.kuali.ole.deliver.notice.valuesFinder;

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
        keyValues.add(new ConcreteKeyValue("Recall Notice","Recall Notice"));
        keyValues.add(new ConcreteKeyValue("OnHold Notice","OnHold Notice"));
        keyValues.add(new ConcreteKeyValue("Hold Courtesy Notice","Hold Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue("Request Expiration Notice","Request Expiration Notice"));
        keyValues.add(new ConcreteKeyValue("Courtesy Notice","Courtesy Notice"));
        keyValues.add(new ConcreteKeyValue("Overdue Notice","Overdue Notice"));
        return keyValues;
    }
}