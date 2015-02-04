package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleInstanceItemType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ItemType used to render the values for ItemType dropdown control.
 */
public class ItemType extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,,
     * ConcreteKeyValue has two arguments instanceItemTypeCode and
     * instanceItemTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleInstanceItemType> oleInstanceItemTypes =
                KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleInstanceItemType.class,"instanceItemTypeName",true);
        options.add(new ConcreteKeyValue("", ""));
        for (OleInstanceItemType type : oleInstanceItemTypes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getInstanceItemTypeCode(), type.getInstanceItemTypeName()));
            }
        }
        return options;
    }
}
