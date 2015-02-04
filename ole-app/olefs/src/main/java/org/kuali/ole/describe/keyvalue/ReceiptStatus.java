package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleReceiptStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ReceiptStatus used to render the values for ReceiptStatus dropdown control.
 */
public class ReceiptStatus extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValues,
     * ConcreteKeyValue has two arguments  receiptStatusCode and
     * receiptStatusName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleReceiptStatus> oleReceiptStatuses = KRADServiceLocator.getBusinessObjectService().findAll(OleReceiptStatus.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleReceiptStatus type : oleReceiptStatuses) {
            if(type.isActive()){
                options.add(new ConcreteKeyValue(type.getReceiptStatusCode(), type.getReceiptStatusName()));
            }
        }
        return options;
    }
}
