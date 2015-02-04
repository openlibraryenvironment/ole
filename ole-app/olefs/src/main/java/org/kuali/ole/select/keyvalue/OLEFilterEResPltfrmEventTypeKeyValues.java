package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEEResPltfrmEventType;
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
public class OLEFilterEResPltfrmEventTypeKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEEResPltfrmEventType> oleeResPltfrmEventTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLEEResPltfrmEventType.class);
        keyValues.add(new ConcreteKeyValue("All", "All"));
        for (OLEEResPltfrmEventType oleeResPltfrmEventType : oleeResPltfrmEventTypes) {
            if (oleeResPltfrmEventType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleeResPltfrmEventType.geteResPltfrmEventTypeId(), oleeResPltfrmEventType.geteResPltfrmEventTypeName()));
            }
        }
        return keyValues;
    }
}
