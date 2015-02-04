package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleAddressSourceBo;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * OleSourceKeyValue returns OleSourceId and OleSourceName for OleSourceBo.
 */
public class OleAddressSourceKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleAddressSourceBo> oleAddressSourceBos = KRADServiceLocator.getBusinessObjectService().findAll(OleAddressSourceBo.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleAddressSourceBo oleAddressSourceType : oleAddressSourceBos) {
            if (oleAddressSourceType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleAddressSourceType.getOleAddressSourceId(), oleAddressSourceType.getOleAddressSourceName()));
            }
        }
        return keyValues;
    }
}
