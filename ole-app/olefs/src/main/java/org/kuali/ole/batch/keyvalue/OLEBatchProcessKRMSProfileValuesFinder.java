package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/6/13
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessKRMSProfileValuesFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(" "," "));
        keyValues.add(new ConcreteKeyValue("YBP","YBP"));
        keyValues.add(new ConcreteKeyValue("Barnes & Nobels","Barnes & Nobels"));

        return keyValues;
    }
}
