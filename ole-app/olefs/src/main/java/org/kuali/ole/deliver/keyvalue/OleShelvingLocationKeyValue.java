package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleBorrowerKeyValue returns BorrowerTypeId and BorrowerTypeName for OleBorrowerType.
 */
public class OleShelvingLocationKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleLocation> oleLocations = KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleLocation oleLocation : oleLocations) {
            if ("SHELVING".equalsIgnoreCase(oleLocation.getLevelCode()))
                keyValues.add(new ConcreteKeyValue(oleLocation.getLocationCode(), oleLocation.getLocationCode() + "-" + oleLocation.getLocationName()));

        }
        return keyValues;
    }
}
