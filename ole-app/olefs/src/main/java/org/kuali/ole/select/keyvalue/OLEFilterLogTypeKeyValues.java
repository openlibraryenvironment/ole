package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLELogType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by chenchulakshmig on 11/20/14.
 */
public class OLEFilterLogTypeKeyValues extends KeyValuesBase {

    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLELogType> oleLogTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLELogType.class);
        keyValues.add(new ConcreteKeyValue("All", "All"));
        for (OLELogType oleLogType : oleLogTypes) {
            keyValues.add(new ConcreteKeyValue(oleLogType.getLogTypeId(), oleLogType.getLogTypeName()));
        }
        return keyValues;
    }
}
