package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OlePrivacy;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Privacy used to render the values for Privacy dropdown control.
 */
public class Privacy extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments privacyCode as key and
     * privacyName  as value.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OlePrivacy> olePrivacies = KRADServiceLocator.getBusinessObjectService().findAll(OlePrivacy.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OlePrivacy type : olePrivacies) {
            options.add(new ConcreteKeyValue(type.getPrivacyCode(), type.getPrivacyName()));
        }
        return options;
    }
}
