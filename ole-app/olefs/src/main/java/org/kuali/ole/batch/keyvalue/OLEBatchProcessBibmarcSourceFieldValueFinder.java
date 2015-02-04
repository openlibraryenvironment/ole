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
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessBibmarcSourceFieldValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        /*keyValues.add(new ConcreteKeyValue("title","Title"));
        keyValues.add(new ConcreteKeyValue("author","Author"));
        keyValues.add(new ConcreteKeyValue("subject","Subject"));
        keyValues.add(new ConcreteKeyValue("description","Description"));
        keyValues.add(new ConcreteKeyValue("date_of_publication","Date of Publication"));
        keyValues.add(new ConcreteKeyValue("publisher","Publisher"));
        keyValues.add(new ConcreteKeyValue("format_language","Format Language"));
        keyValues.add(new ConcreteKeyValue("publisher","Publisher"));
        keyValues.add(new ConcreteKeyValue("isbn","ISBN/ISSN"));*/

        return keyValues;

    }
}