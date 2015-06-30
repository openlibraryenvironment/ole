package org.kuali.ole.select.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelind on 6/29/15.
 */
public class OLEEResourceWorkflowSelectorKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("Role","Role"));
        keyValues.add(new ConcreteKeyValue("Person","Person"));
        return keyValues;
    }
}
