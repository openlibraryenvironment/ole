package org.kuali.ole.describe.keyvalue;


import org.kuali.ole.describe.bo.OleAccessMethod;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AdditionalAccessMethodInformation used to render the values for AccessMethod dropdown control.
 */
public class AdditionalAccessMethodInformation extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValue,
     * ConcreteKeyValue has two arguments accessMethodCode  and
     * accessMethodName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleAccessMethod> oleAccessMethods =
                KRADServiceLocator.getBusinessObjectService().findAll(OleAccessMethod.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleAccessMethod type : oleAccessMethods) {
            options.add(new ConcreteKeyValue(type.getAccessMethodCode(), type.getAccessMethodName()));
        }
        return options;
    }
}