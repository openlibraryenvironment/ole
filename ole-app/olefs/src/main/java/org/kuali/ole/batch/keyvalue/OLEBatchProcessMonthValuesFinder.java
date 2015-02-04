package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 7/26/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessMonthValuesFinder extends KeyValuesBase {

    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        for(int monthDay=1;monthDay<=31;monthDay++){
            keyValues.add(new ConcreteKeyValue(""+monthDay,""+monthDay));
        }
        return keyValues;
    }
}
