package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jayabharathreddy on 7/2/15.
 */
public class OLEBatchProcessGokbValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("GOKb UID","GOKb UID"));
        keyValues.add(new ConcreteKeyValue("Start date","Start date "));
        keyValues.add(new ConcreteKeyValue("Start volume","Start volume"));
        keyValues.add(new ConcreteKeyValue("Start issue","Start issue"));
        keyValues.add(new ConcreteKeyValue("End date","End date"));
        keyValues.add(new ConcreteKeyValue("End volume","End volume"));
        keyValues.add(new ConcreteKeyValue("End issue","End issue"));
        keyValues.add(new ConcreteKeyValue("embargo","embargo"));
        keyValues.add(new ConcreteKeyValue("Platform.host.URL","Platform.host.URL"));
        keyValues.add(new ConcreteKeyValue("Publisher","Publisher"));
        keyValues.add(new ConcreteKeyValue("Imprint","Imprint"));
        return keyValues;
    }
}