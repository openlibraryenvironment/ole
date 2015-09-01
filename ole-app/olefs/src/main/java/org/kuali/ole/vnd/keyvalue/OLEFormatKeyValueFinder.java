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
        keyValues.add(new ConcreteKeyValue("All","All"));
        keyValues.add(new ConcreteKeyValue("Databases","Databases"));
        keyValues.add(new ConcreteKeyValue("Ebooks","Ebooks"));
        keyValues.add(new ConcreteKeyValue("Journals","Journals"));
        keyValues.add(new ConcreteKeyValue("Streaming Media","Streaming Media"));
        return keyValues;
    }
}
