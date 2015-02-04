package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleShelvingOrder;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ShelvingOrder used to render the values for ShelvingOrder dropdown control.
 */
public class ShelvingOrder extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments shelvingOrderCode and
     * shelvingOrderName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleShelvingOrder> oleShelvingOrders = KRADServiceLocator.getBusinessObjectService().findAll(OleShelvingOrder.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleShelvingOrder type : oleShelvingOrders) {
            options.add(new ConcreteKeyValue(type.getShelvingOrderCode(), type.getShelvingOrderName()));
        }
        return options;
    }
}