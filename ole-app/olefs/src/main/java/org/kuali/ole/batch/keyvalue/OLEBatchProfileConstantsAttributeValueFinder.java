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
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProfileConstantsAttributeValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("accountno","Account Number"));
        keyValues.add(new ConcreteKeyValue("vendorName","Vendor Name"));

        return keyValues;
    }
}