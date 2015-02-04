package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleCompleteness;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Completeness used to render the values for Completeness dropdown control.
 */
public class Completeness extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments completenessCode and
     * completenessName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleCompleteness> oleCompleteness = KRADServiceLocator.getBusinessObjectService().findAll(OleCompleteness.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleCompleteness type : oleCompleteness) {
            options.add(new ConcreteKeyValue(type.getCompletenessCode(), type.getCompletenessName()));
        }
        return options;
    }
}
