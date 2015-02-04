package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 10/17/13
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBibImportHoldingSourceFieldValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Call Number","Call Number"));
        keyValues.add(new ConcreteKeyValue("Call Number Prefix","Call Number Prefix"));
        keyValues.add(new ConcreteKeyValue("Call Number Type","Call Number Type"));
        keyValues.add(new ConcreteKeyValue("Copy Number","Copy Number"));
        keyValues.add(new ConcreteKeyValue("Location Level1","Location Level1"));
        keyValues.add(new ConcreteKeyValue("Location Level2","Location Level2"));
        keyValues.add(new ConcreteKeyValue("Location Level3","Location Level3"));
        keyValues.add(new ConcreteKeyValue("Location Level4","Location Level4"));
        keyValues.add(new ConcreteKeyValue("Location Level5","Location Level5"));
        return keyValues;

    }
}
