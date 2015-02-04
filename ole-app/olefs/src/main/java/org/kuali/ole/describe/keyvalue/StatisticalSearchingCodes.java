package org.kuali.ole.describe.keyvalue;

import org.kuali.ole.describe.bo.OleStatisticalSearchingCodes;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * StatisticalSearchingCodes used to render the values for StatisticalSearchingCodes dropdown control.
 */
public class StatisticalSearchingCodes extends KeyValuesBase {
    /**
     * This method returns the List of ConcreteKeyValue,
     * ConcreteKeyValue has two arguments statisticalSearchingCode and
     * statisticalSearchingName .
     *
     * @return List<KeyValue>
     */
    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Collection<OleStatisticalSearchingCodes> oleStatisticalSearchingCodes = KRADServiceLocator.getBusinessObjectService().findAll(OleStatisticalSearchingCodes.class);
        options.add(new ConcreteKeyValue("", ""));
        for (OleStatisticalSearchingCodes type : oleStatisticalSearchingCodes) {
            if (type.isActive()) {
                options.add(new ConcreteKeyValue(type.getStatisticalSearchingCode(), type.getStatisticalSearchingName()));
            }
        }
        return options;
    }
}