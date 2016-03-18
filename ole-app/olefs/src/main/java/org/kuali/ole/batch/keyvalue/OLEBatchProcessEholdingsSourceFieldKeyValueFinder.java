package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rajeshbabuk
 * Date: 10/4/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessEholdingsSourceFieldKeyValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Access Status","Access Status"));
        keyValues.add(new ConcreteKeyValue("Call Number","Call Number"));
        keyValues.add(new ConcreteKeyValue("Call Number Type","Call Number Type"));
        keyValues.add(new ConcreteKeyValue("Location Level1","Location Level1"));
        keyValues.add(new ConcreteKeyValue("Location Level2","Location Level2"));
        keyValues.add(new ConcreteKeyValue("Location Level3","Location Level3"));
        keyValues.add(new ConcreteKeyValue("Location Level4","Location Level4"));
        keyValues.add(new ConcreteKeyValue("Location Level5","Location Level5"));
        keyValues.add(new ConcreteKeyValue("URL","URL"));
        keyValues.add(new ConcreteKeyValue("Persistent Link","Persistent Link"));
        keyValues.add(new ConcreteKeyValue("Link Text","Link Text"));
        keyValues.add(new ConcreteKeyValue("Public Display Note","Public Display Note"));
        keyValues.add(new ConcreteKeyValue("Coverage Start Date","Coverage Start Date"));
        keyValues.add(new ConcreteKeyValue("Coverage Start Issue","Coverage Start Issue"));
        keyValues.add(new ConcreteKeyValue("Coverage Start Volume","Coverage Start Volume"));
        keyValues.add(new ConcreteKeyValue("Coverage End Date","Coverage End Date"));
        keyValues.add(new ConcreteKeyValue("Coverage End Issue","Coverage End Issue"));
        keyValues.add(new ConcreteKeyValue("Coverage End Volume","Coverage End Volume"));
        keyValues.add(new ConcreteKeyValue("Statistical Code","Statistical Code"));
        keyValues.add(new ConcreteKeyValue("Platform","Platform"));
        keyValues.add(new ConcreteKeyValue("Publisher","Publisher"));
        keyValues.add(new ConcreteKeyValue("Donor Code","Donor Code"));
        keyValues.add(new ConcreteKeyValue("Donor Public Display","Donor Public Display"));
        keyValues.add(new ConcreteKeyValue("Donor Note","Donor Note"));
        keyValues.add(new ConcreteKeyValue("EResource Name","EResource Name"));
        keyValues.add(new ConcreteKeyValue("EResource Id","EResource Id"));
        keyValues.add(new ConcreteKeyValue("Materials Specified","Materials Specified"));
        keyValues.add(new ConcreteKeyValue("First Indicator","First Indicator"));
        keyValues.add(new ConcreteKeyValue("Second Indicator","Second Indicator"));
        return keyValues;
    }
}
