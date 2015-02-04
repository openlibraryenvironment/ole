package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleCountryCodes;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CountryCode used to render the values for CountryCode dropdown control.
 */
public class CountryCode extends KeyValuesBase {
    /**
     * This method returns the List of  ConcreteKeyValue,
     * ConcreteKeyValue has two arguments CountryCode and
     * countryName.
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleCountryCodes> oleCountryCodes = KRADServiceLocator.getBusinessObjectService().findAll(OleCountryCodes.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleCountryCodes type : oleCountryCodes) {
            options.add(new ConcreteKeyValue(type.getCountryCode(), type.getCountryName()));
        }
        return options;
    }
}
