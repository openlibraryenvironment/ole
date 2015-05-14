package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEMarcUpdateFrequency;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maheswarang on 5/12/15.
 */
public class OLEMarcUpdateFrequencyKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEMarcUpdateFrequency> oleMarcUpdateFrequencies = KRADServiceLocator.getBusinessObjectService().findAll(OLEMarcUpdateFrequency.class);
        for (OLEMarcUpdateFrequency oleMarcUpdateFrequency : oleMarcUpdateFrequencies) {
            if (oleMarcUpdateFrequency.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleMarcUpdateFrequency.getMarcUpdateFrequencyId(), oleMarcUpdateFrequency.getMarcUpdateFrequencyName()));
            }
        }
        return keyValues;
    }
}