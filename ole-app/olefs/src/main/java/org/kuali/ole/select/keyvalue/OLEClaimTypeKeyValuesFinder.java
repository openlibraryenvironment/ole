package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEClaimType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OLEClaimTypeKeyValuesFinder used to render the values for ItemStatus dropdown control.
 */
public class OLEClaimTypeKeyValuesFinder extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValue,
     * ConcreteKeyValue has two arguments oleClaimTypeCode and
     * oleClaimTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OLEClaimType> oleClaimTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLEClaimType.class);
     //   options.add(new ConcreteKeyValue("", ""));
        for (OLEClaimType type : oleClaimTypes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getOleClaimTypeId(), type.getOleClaimTypeName()));
            }
        }
        return options;
    }
}
