package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEPlatformAdminUrlType;
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
 * OLEPlatformAdminUrlTypeKeyValues returns platformAdminUrlTypeId and platformAdminUrlTypeName for OLEPlatformAdminUrlType.
 */
public class OLEPlatformAdminUrlTypeKeyValues extends KeyValuesBase {

    /**
     * This method will populate the id as a key and name as a value and return it as list
     *
     * @return keyValues(list)
     */
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEPlatformAdminUrlType> olePlatformAdminUrlTypeList = KRADServiceLocator.getBusinessObjectService().findAll(OLEPlatformAdminUrlType.class);
        for (OLEPlatformAdminUrlType olePlatformAdminUrlType : olePlatformAdminUrlTypeList) {
            if (olePlatformAdminUrlType.isActive()) {
                keyValues.add(new ConcreteKeyValue(olePlatformAdminUrlType.getPlatformAdminUrlTypeId(), olePlatformAdminUrlType.getPlatformAdminUrlTypeName()));
            }
        }
        return keyValues;
    }
}
