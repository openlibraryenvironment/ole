package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleBorrowerType;
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
public class OleBorrowerKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleBorrowerType> oleBorrowerTypes = KRADServiceLocator.getBusinessObjectService().findAllOrderBy(OleBorrowerType.class, "borrowerTypeName", true);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleBorrowerType oleBorrowerType : oleBorrowerTypes) {
            if (oleBorrowerType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleBorrowerType.getBorrowerTypeId(), oleBorrowerType.getBorrowerTypeName()));
            }
        }
        return keyValues;
    }
}
