package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleSpecificRetentionPolicyType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * PolicyType used to render the values for PolicyType dropdown control.
 */
public class PolicyType extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments specificRetentionPolicyTypeCode  and
     * specificRetentionPolicyTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleSpecificRetentionPolicyType> oleSpecificRetentionPolicyTypes =
                KRADServiceLocator.getBusinessObjectService().findAll(OleSpecificRetentionPolicyType.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleSpecificRetentionPolicyType type : oleSpecificRetentionPolicyTypes) {
            options.add(new ConcreteKeyValue(type.getSpecificRetentionPolicyTypeCode(), type.getSpecificRetentionPolicyTypeName()));
        }
        return options;
    }
}
