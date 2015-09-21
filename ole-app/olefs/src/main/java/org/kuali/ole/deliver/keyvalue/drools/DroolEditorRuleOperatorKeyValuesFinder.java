package org.kuali.ole.deliver.keyvalue.drools;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/13/15.
 */
public class DroolEditorRuleOperatorKeyValuesFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("=","="));
        keyValues.add(new ConcreteKeyValue("!=","!="));
        keyValues.add(new ConcreteKeyValue(">",">"));
        keyValues.add(new ConcreteKeyValue(">=",">="));
        keyValues.add(new ConcreteKeyValue("<","<"));
        keyValues.add(new ConcreteKeyValue("<=","<="));
        keyValues.add(new ConcreteKeyValue("!=null","!=null"));
        keyValues.add(new ConcreteKeyValue("=null","=null"));
        return keyValues;
    }
}
