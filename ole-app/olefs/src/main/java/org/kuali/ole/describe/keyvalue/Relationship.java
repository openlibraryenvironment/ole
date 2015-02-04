package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleElectronicLocationAndAccessRelationship;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Relationship used to render the values for Relationship dropdown control.
 */
public class Relationship extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments elaRelationshipCode and
     * elaRelationshipName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleElectronicLocationAndAccessRelationship> oleElectronicLocationAndAccessRelationships =
                KRADServiceLocator.getBusinessObjectService().findAll(OleElectronicLocationAndAccessRelationship.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleElectronicLocationAndAccessRelationship type : oleElectronicLocationAndAccessRelationships) {
            options.add(new ConcreteKeyValue(type.getElaRelationshipCode(), type.getElaRelationshipName()));
        }
        return options;
    }
}
