package org.kuali.ole.deliver.keyvalue.drools;

import org.kuali.ole.deliver.drools.DroolsConstants;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/15/15.
 */
public class InNotInOperatorKeyValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("in", "in"));
        keyValues.add(new ConcreteKeyValue("not in", "not in"));
        return keyValues;
    }
}
