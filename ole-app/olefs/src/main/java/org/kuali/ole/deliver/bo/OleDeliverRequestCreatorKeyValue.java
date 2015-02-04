package org.kuali.ole.deliver.bo;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/9/12
 * Time: 6:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDeliverRequestCreatorKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        keyValues.add(new ConcreteKeyValue("Patron", "Patron"));
        keyValues.add(new ConcreteKeyValue("Proxy Patron", "Proxy Patron"));
        keyValues.add(new ConcreteKeyValue("Operator", "Operator"));

        return keyValues;
    }

}