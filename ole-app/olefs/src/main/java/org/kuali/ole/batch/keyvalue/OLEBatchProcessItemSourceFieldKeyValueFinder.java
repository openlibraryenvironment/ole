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
 * Time: 2:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessItemSourceFieldKeyValueFinder  extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Local Identifier","Local Identifier"));
        keyValues.add(new ConcreteKeyValue("Holdings Location Level1","Holdings Location Level1"));
        keyValues.add(new ConcreteKeyValue("Holdings Location Level2","Holdings Location Level2"));
        keyValues.add(new ConcreteKeyValue("Holdings Location Level3","Holdings Location Level3"));
        keyValues.add(new ConcreteKeyValue("Holdings Location Level4","Holdings Location Level4"));
        keyValues.add(new ConcreteKeyValue("Holdings Location Level5","Holdings Location Level5"));
        keyValues.add(new ConcreteKeyValue("Holdings Call Number","Holdings Call Number"));
        keyValues.add(new ConcreteKeyValue("Holdings Call Number Type","Holdings Call Number Type"));
        keyValues.add(new ConcreteKeyValue("Holdings Call Number Prefix","Holdings Call Number Prefix"));
        keyValues.add(new ConcreteKeyValue("Holdings Copy Number","Holdings Copy Number"));
        keyValues.add(new ConcreteKeyValue("Location Level1","Location Level1"));
        keyValues.add(new ConcreteKeyValue("Location Level2","Location Level2"));
        keyValues.add(new ConcreteKeyValue("Location Level3","Location Level3"));
        keyValues.add(new ConcreteKeyValue("Location Level4","Location Level4"));
        keyValues.add(new ConcreteKeyValue("Location Level5","Location Level5"));
        keyValues.add(new ConcreteKeyValue("Call Number","Call Number"));
        keyValues.add(new ConcreteKeyValue("Call Number Prefix","Call Number Prefix"));
        keyValues.add(new ConcreteKeyValue("Call Number Type","Call Number Type"));
        keyValues.add(new ConcreteKeyValue("Item Barcode","Item Barcode"));
        keyValues.add(new ConcreteKeyValue("Item Type","Item Type"));
        keyValues.add(new ConcreteKeyValue("Copy Number","Copy Number"));
        keyValues.add(new ConcreteKeyValue("Donor Code","Donor Code"));
        keyValues.add(new ConcreteKeyValue("Donor Public Display","Donor Public Display"));
        keyValues.add(new ConcreteKeyValue("Donor Note","Donor Note"));
        keyValues.add(new ConcreteKeyValue("Item Status","Item Status"));
        keyValues.add(new ConcreteKeyValue("Enumeration","Enumeration"));
        keyValues.add(new ConcreteKeyValue("Chronology","Chronology"));
        keyValues.add(new ConcreteKeyValue("Vendor Line Item Identifier","Vendor Line Item Identifier"));
        /*keyValues.add(new ConcreteKeyValue("Call Number Type Code","Call Number Type Code"));
        keyValues.add(new ConcreteKeyValue("Call Number Type Name","Call Number Type Name"));
        keyValues.add(new ConcreteKeyValue("Shelving Order","Shelving Order"));
        keyValues.add(new ConcreteKeyValue("Item Identifier","Item Identifier"));
        keyValues.add(new ConcreteKeyValue("Item Status Code","Item Status Code"));
        keyValues.add(new ConcreteKeyValue("Item Uri","Item Uri"));
        keyValues.add(new ConcreteKeyValue("Purchase Order Line Item Identifier","Purchase Order Line Item Identifier"));
        keyValues.add(new ConcreteKeyValue("Vendor Line Item Identifier","Vendor Line Item Identifier"));
        keyValues.add(new ConcreteKeyValue("Barcode ARSL","Barcode ARSL"));
        keyValues.add(new ConcreteKeyValue("Statistical Searching Code","Statistical Searching Code"));
        keyValues.add(new ConcreteKeyValue("Statistical Searching Name","Statistical Searching Name"));
        keyValues.add(new ConcreteKeyValue("Item Type Name","Item Type Name"));
        keyValues.add(new ConcreteKeyValue("Item Type Code","Item Type Code"));
        keyValues.add(new ConcreteKeyValue("Enumeration","Enumeration"));
        keyValues.add(new ConcreteKeyValue("Chronology","Chronology"));
        keyValues.add(new ConcreteKeyValue("Location","Location"));
        keyValues.add(new ConcreteKeyValue("Date Entered","Date Entered"));*/

       return keyValues;

    }
}
