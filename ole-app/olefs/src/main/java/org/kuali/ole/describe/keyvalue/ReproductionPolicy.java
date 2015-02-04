package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleReproductionPolicy;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ReproductionPolicy used to render the values for ReproductionPolicy dropdown control.
 */
public class ReproductionPolicy extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments reproductionPolicyCode and
     * reproductionPolicyName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleReproductionPolicy> oleLendingPolicies = KRADServiceLocator.getBusinessObjectService().findAll(OleReproductionPolicy.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleReproductionPolicy type : oleLendingPolicies) {
            options.add(new ConcreteKeyValue(type.getReproductionPolicyCode(), type.getReproductionPolicyName()));
        }
        return options;
    }
}
