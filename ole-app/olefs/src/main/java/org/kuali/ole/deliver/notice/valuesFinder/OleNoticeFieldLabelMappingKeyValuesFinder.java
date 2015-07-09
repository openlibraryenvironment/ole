package org.kuali.ole.deliver.notice.valuesFinder;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 7/7/15.
 */
public class OleNoticeFieldLabelMappingKeyValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("Patron Information","Patron Information"));
        keyValues.add(new ConcreteKeyValue("Patron Name","Patron Name"));
        keyValues.add(new ConcreteKeyValue("Address","Address"));
        keyValues.add(new ConcreteKeyValue("Email","Email"));
        keyValues.add(new ConcreteKeyValue("Phone #","Phone #"));
        keyValues.add(new ConcreteKeyValue("Title/Item Information","Title/Item Information"));
        keyValues.add(new ConcreteKeyValue("Title","Title"));
        keyValues.add(new ConcreteKeyValue("Author","Author"));
        keyValues.add(new ConcreteKeyValue("Volume/Issue/Copy #","Volume/Issue/Copy #"));
        keyValues.add(new ConcreteKeyValue("Call #","Call #"));
        keyValues.add(new ConcreteKeyValue("Item Barcode","Item Barcode"));
        keyValues.add(new ConcreteKeyValue("Original Due Date","Original Due Date"));
        keyValues.add(new ConcreteKeyValue("New Due Date","New Due Date"));
        keyValues.add(new ConcreteKeyValue("Item was due","Item was due"));
        keyValues.add(new ConcreteKeyValue("Circulation Location/Library Name","Circulation Location/Library Name"));
        keyValues.add(new ConcreteKeyValue("Circulation Reply-To Email","Circulation Reply-To Email"));
        keyValues.add(new ConcreteKeyValue("Hold Expiration Date","Hold Expiration Date"));
        return keyValues;
    }
}