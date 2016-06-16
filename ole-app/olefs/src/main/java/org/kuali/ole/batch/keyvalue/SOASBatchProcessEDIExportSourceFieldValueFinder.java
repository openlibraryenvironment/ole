package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: gayathria
 * Date: 5/23/14
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class SOASBatchProcessEDIExportSourceFieldValueFinder extends KeyValuesBase {
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("",""));
        keyValues.add(new ConcreteKeyValue("Default Segment", "Default Segment"));
        keyValues.add(new ConcreteKeyValue("Interchange Details", "Interchange Details"));
        keyValues.add(new ConcreteKeyValue("Unique Ref Number", "Unique Ref Number"));
        keyValues.add(new ConcreteKeyValue("Purap Document Identifier", "Purap Document Identifier"));
        keyValues.add(new ConcreteKeyValue("Current Date", "Current Date"));
        keyValues.add(new ConcreteKeyValue("Vendor Username", "Vendor Username"));
        keyValues.add(new ConcreteKeyValue("Vendor SAN", "Vendor SAN"));
        keyValues.add(new ConcreteKeyValue("Currency Code", "Currency Code"));
        keyValues.add(new ConcreteKeyValue("Line Item", "Line Item"));
        keyValues.add(new ConcreteKeyValue("Purchase Line Item", "Purchase Line Item"));
        keyValues.add(new ConcreteKeyValue("Item Price", "Item Price"));
        keyValues.add(new ConcreteKeyValue("Item Author", "Item Author"));
        keyValues.add(new ConcreteKeyValue("Item Title", "Item Title"));
        keyValues.add(new ConcreteKeyValue("Item Publisher", "Item Publisher"));
        keyValues.add(new ConcreteKeyValue("Item Publisher Date", "Item Publisher Date"));
        keyValues.add(new ConcreteKeyValue("Item Quantity", "Item Quantity"));
        keyValues.add(new ConcreteKeyValue("Reference Qualifier 1", "Reference Qualifier 1"));
        keyValues.add(new ConcreteKeyValue("Reference Qualifier 2", "Reference Qualifier 2"));
        keyValues.add(new ConcreteKeyValue("Item Location", "Item Location"));
        keyValues.add(new ConcreteKeyValue("Delivery Address & username", "Delivery Address & username"));
        keyValues.add(new ConcreteKeyValue("Other Address & username", "Other Address & username"));
        keyValues.add(new ConcreteKeyValue("Section Identification", "Section Identification"));
        keyValues.add(new ConcreteKeyValue("Control Qualifier 1", "Control Qualifier 1"));
        keyValues.add(new ConcreteKeyValue("Control Qualifier 2", "Control Qualifier 2"));
        keyValues.add(new ConcreteKeyValue("Note", "Note"));
        keyValues.add(new ConcreteKeyValue("Number of Segments", "Number of Segments"));
        keyValues.add(new ConcreteKeyValue("Interchange Control count", "Interchange Control count"));

       /* Collections.sort(keyValues, new Comparator<KeyValue>() {
            public int compare(KeyValue keyValue1, KeyValue keyValue2) {
                return keyValue1.getValue().compareTo(keyValue2.getValue());

            }
        });*/
        return keyValues;
    }
}
