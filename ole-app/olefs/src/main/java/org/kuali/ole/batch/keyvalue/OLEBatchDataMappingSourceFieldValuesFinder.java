package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/25/13
 * Time: 2:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchDataMappingSourceFieldValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        /*keyValues.add(new ConcreteKeyValue("049$$a","049$$a"));
        keyValues.add(new ConcreteKeyValue("035$$a","035$$a"));*/

        return keyValues;
    }
}