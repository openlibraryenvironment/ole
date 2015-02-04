package org.kuali.ole.describe.keyvalue;


import org.kuali.ole.describe.bo.OleSpecificRetentionPolicyTypeUnit;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UnitType used to render the values for UnitType dropdown control..
 */
public class UnitType extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments SpecificPolicyUnitTypeCode and
     * SpecificPolicyUnitTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleSpecificRetentionPolicyTypeUnit> oleSpecificRetentionPolicyTypeUnits =
                KRADServiceLocator.getBusinessObjectService().findAll(OleSpecificRetentionPolicyTypeUnit.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleSpecificRetentionPolicyTypeUnit type : oleSpecificRetentionPolicyTypeUnits) {
            options.add(new ConcreteKeyValue(type.getSpecificPolicyUnitTypeCode(), type.getSpecificPolicyUnitTypeName()));
        }
        return options;
    }
}

