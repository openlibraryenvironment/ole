package org.kuali.ole.batch.keyvalue;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: adityas
 * Date: 7/25/13
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessReqsForItemValuesFinder extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue("One Requisition Per Title","One Requisition Per Title"));
        keyValues.add(new ConcreteKeyValue("One Requisition With All Titles","One Requisition With All Titles"));
        return keyValues;
    }
}
