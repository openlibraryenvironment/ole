package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleSourceBo;
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
public class OleSourceKeyValue extends KeyValuesBase {
    /**
     * This method will populate the code as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OleSourceBo> oleSourceBos = KRADServiceLocator.getBusinessObjectService().findAll(OleSourceBo.class);
        keyValues.add(new ConcreteKeyValue("", ""));
        for (OleSourceBo oleSourceType : oleSourceBos) {
            if (oleSourceType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleSourceType.getOleSourceId(), oleSourceType.getOleSourceName()));
            }
        }
        return keyValues;
    }
}
