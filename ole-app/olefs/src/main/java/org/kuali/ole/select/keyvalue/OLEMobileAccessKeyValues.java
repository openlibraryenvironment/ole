package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEMobileAccess;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEMobileAccessKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEMobileAccess> oleMobileAccesses = KRADServiceLocator.getBusinessObjectService().findAll(OLEMobileAccess.class);
        for (OLEMobileAccess oleMobileAccess : oleMobileAccesses) {
            if (oleMobileAccess.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleMobileAccess.getMobileAccessId(), oleMobileAccess.getMobileAccessName()));
            }
        }
        return keyValues;
    }
}
