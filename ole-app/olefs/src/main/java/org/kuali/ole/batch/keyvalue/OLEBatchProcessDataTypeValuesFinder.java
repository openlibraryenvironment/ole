package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 10/18/13
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessDataTypeValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {

        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Holdings"," Holdings"));
        keyValues.add(new ConcreteKeyValue("Item","Item "));
        keyValues.add(new ConcreteKeyValue("EHoldings","EHoldings"));
        return keyValues;
    }
}
