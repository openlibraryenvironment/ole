package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 8/1/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessHoldingSourceFieldValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Local Identifier","Local Identifier"));
        keyValues.add(new ConcreteKeyValue("Call Number","Call Number"));
        keyValues.add(new ConcreteKeyValue("Call Number Prefix","Call Number Prefix"));
        keyValues.add(new ConcreteKeyValue("Call Number Type","Call Number Type"));
        keyValues.add(new ConcreteKeyValue("Copy Number","Copy Number"));
        keyValues.add(new ConcreteKeyValue("Date Created","Date Created"));
        keyValues.add(new ConcreteKeyValue("Location Level1","Location Level1"));
        keyValues.add(new ConcreteKeyValue("Location Level2","Location Level2"));
        keyValues.add(new ConcreteKeyValue("Location Level3","Location Level3"));
        keyValues.add(new ConcreteKeyValue("Location Level4","Location Level4"));
        keyValues.add(new ConcreteKeyValue("Location Level5","Location Level5"));
        /*keyValues.add(new ConcreteKeyValue("Call Number Type Code","Call Number Type Code"));
        keyValues.add(new ConcreteKeyValue("Call Number Type Name","Call Number Type Name"));
        keyValues.add(new ConcreteKeyValue("Shelving Order","Shelving Order"));
        keyValues.add(new ConcreteKeyValue("URI","URI"));
        keyValues.add(new ConcreteKeyValue("Holding Note","Holding Note"));
        keyValues.add(new ConcreteKeyValue("Receipt Status Code","Receipt Status Code"));
        keyValues.add(new ConcreteKeyValue("Location","Location"));*/

        return keyValues;

    }
}
