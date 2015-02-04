package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleLendingPolicy;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * LendingPolicy used to render the values for LendingPolicy dropdown control.
 */
public class LendingPolicy extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments lendingPolicyCode and
     * lendingPolicyName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleLendingPolicy> oleLendingPolicies = KRADServiceLocator.getBusinessObjectService().findAll(OleLendingPolicy.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleLendingPolicy type : oleLendingPolicies) {
            options.add(new ConcreteKeyValue(type.getLendingPolicyCode(), type.getLendingPolicyName()));
        }
        return options;
    }
}