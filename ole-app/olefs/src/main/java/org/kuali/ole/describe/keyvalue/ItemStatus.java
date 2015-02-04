package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ItemStatus used to render the values for ItemStatus dropdown control.
 */
public class ItemStatus extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValue,
     * ConcreteKeyValue has two arguments itemAvailableStatusCode and
     * itemAvailableStatusName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleItemAvailableStatus> oleItemAvailableStatuses = KRADServiceLocator.getBusinessObjectService().findAll(OleItemAvailableStatus.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleItemAvailableStatus type : oleItemAvailableStatuses) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getItemAvailableStatusCode(), type.getItemAvailableStatusName()));
            }
        }
        return options;
    }
}
