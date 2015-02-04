package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEPlatformStatus;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenchulakshmig on 9/12/14.
 * OLEPlatformStatusKeyValues returns platformStatusId and platformStatusName for OLEPlatformStatus.
 */
public class OLEPlatformStatusKeyValues extends KeyValuesBase {

    /**
     * This method will populate the id as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEPlatformStatus> olePlatformStatusList = KRADServiceLocator.getBusinessObjectService().findAll(OLEPlatformStatus.class);
        for (OLEPlatformStatus olePlatformStatus : olePlatformStatusList) {
            if (olePlatformStatus.isActive()) {
                keyValues.add(new ConcreteKeyValue(olePlatformStatus.getPlatformStatusId(), olePlatformStatus.getPlatformStatusName()));
            }
        }
        return keyValues;
    }
}
