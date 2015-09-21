package org.kuali.ole.deliver.keyvalue.drools;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.bo.OleBorrowerType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 7/8/15.
 */
public class BorrowerTypeKeyValuesFinder extends KeyValuesBase {
    @Override
    public List getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        List<ConcreteKeyValue> concreteKeyValues = new ArrayList<ConcreteKeyValue>();
        Collection<OleBorrowerType> oleBorrowerTypes = KRADServiceLocator.getBusinessObjectService().findAll(OleBorrowerType.class);
        if(CollectionUtils.isNotEmpty(oleBorrowerTypes)){
            for (OleBorrowerType oleBorrowerType : oleBorrowerTypes) {
                if (oleBorrowerType.isActive()) {
                    concreteKeyValues.add(new ConcreteKeyValue(oleBorrowerType.getBorrowerTypeCode(), oleBorrowerType.getBorrowerTypeCode()));
                }
            }
        }
        Collections.sort(concreteKeyValues);
        keyValues.addAll(concreteKeyValues);
        return keyValues;
    }
}
