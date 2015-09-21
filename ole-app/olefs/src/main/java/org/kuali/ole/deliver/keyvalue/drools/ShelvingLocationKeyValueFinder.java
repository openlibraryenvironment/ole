package org.kuali.ole.deliver.keyvalue.drools;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/14/15.
 */
public class ShelvingLocationKeyValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();
        Collection<OleLocation> oleLocations = KRADServiceLocator.getBusinessObjectService().findAll(OleLocation.class);
        if (CollectionUtils.isNotEmpty(oleLocations)) {
            for (OleLocation oleLocation : oleLocations) {
                if(oleLocation.getLevelId().equalsIgnoreCase(String.valueOf(ItemInfoUtil.LEVEL_CODES.SHELVING.getId()))){
                    concreteKeyValues.add(new ConcreteKeyValue(oleLocation.getLocationCode(), oleLocation.getLocationCode()));
                }
            }
        }
        Collections.sort(concreteKeyValues);
        keyValues.addAll(concreteKeyValues);
        return keyValues;
    }
}