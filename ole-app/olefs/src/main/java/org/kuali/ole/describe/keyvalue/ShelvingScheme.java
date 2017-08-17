package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ShelvingScheme used to render the values for ShelvingScheme dropdown control.
 */
public class ShelvingScheme extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments shelvingSchemeCode and
     * shelvingSchemeName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleShelvingScheme> oleShelvingSchemes = KRADServiceLocator.getBusinessObjectService().findAll(OleShelvingScheme.class);
         options.add(new ConcreteKeyValue("", ""));
        for (OleShelvingScheme type : oleShelvingSchemes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getShelvingSchemeCode(), type.getShelvingSchemeName()));
            }
        }
        return options;
    }
}
