package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleNotationType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TypeOfNotation used to render the values for TypeOfNotation dropdown control.
 */
public class TypeOfNotation extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments notationTypeCode and
     * notationTypeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleNotationType> oleNotationTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleNotationType.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleNotationType type : oleNotationTypes) {
            options.add(new ConcreteKeyValue(type.getNotationTypeCode(), type.getNotationTypeName()));
        }
        return options;
    }
}
