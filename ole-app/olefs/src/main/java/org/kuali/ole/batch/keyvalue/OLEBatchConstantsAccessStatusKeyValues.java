package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 3/15/14
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchConstantsAccessStatusKeyValues extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("active","active"));
        keyValues.add(new ConcreteKeyValue("inActive","inActive"));
        return keyValues;
    }
}
