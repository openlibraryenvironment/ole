package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleGeneralRetentionPolicy;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * GeneralRetentionPolicy used to render the values for GeneralRetentionPolicy dropdown control.
 */
public class GeneralRetentionPolicy extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValue,
     * ConcreteKeyValue has two arguments generalRetentionPolicyCode and
     * generalRetentionPolicyName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleGeneralRetentionPolicy> oleGeneralRetentionPolicies = KRADServiceLocator.getBusinessObjectService().findAll(OleGeneralRetentionPolicy.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleGeneralRetentionPolicy type : oleGeneralRetentionPolicies) {
            options.add(new ConcreteKeyValue(type.getGeneralRetentionPolicyCode(), type.getGeneralRetentionPolicyName()));
        }
        return options;
    }
}

