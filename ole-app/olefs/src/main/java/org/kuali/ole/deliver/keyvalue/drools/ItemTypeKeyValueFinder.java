package org.kuali.ole.deliver.keyvalue.drools;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.describe.bo.OleInstanceItemType;
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
public class ItemTypeKeyValueFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();
        Collection<OleInstanceItemType> instanceItemTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleInstanceItemType.class);
        if (CollectionUtils.isNotEmpty(instanceItemTypes)) {
            for (OleInstanceItemType oleInstanceItemType : instanceItemTypes) {
                keyValues.add(new ConcreteKeyValue(oleInstanceItemType.getInstanceItemTypeCode(), oleInstanceItemType.getInstanceItemTypeCode()));
            }
        }
        Collections.sort(concreteKeyValues);
        keyValues.addAll(concreteKeyValues);
        return keyValues;
    }
}
