package org.kuali.ole.vnd.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srirams on 6/11/14.
 */
public class OLEFormatKeyValueFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("1","All"));
        keyValues.add(new ConcreteKeyValue("2","Databases"));
        keyValues.add(new ConcreteKeyValue("3","Ebooks"));
        keyValues.add(new ConcreteKeyValue("4","Journals"));
        keyValues.add(new ConcreteKeyValue("5 Media","Streaming Media"));
        return keyValues;
    }
}
