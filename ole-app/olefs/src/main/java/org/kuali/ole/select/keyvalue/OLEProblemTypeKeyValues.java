package org.kuali.ole.select.keyvalue;

import org.kuali.ole.select.bo.OLEProblemType;
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
public class OLEProblemTypeKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEProblemType> oleProblemTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLEProblemType.class);
        for (OLEProblemType oleProblemType : oleProblemTypes) {
            if (oleProblemType.isActive()) {
                keyValues.add(new ConcreteKeyValue(oleProblemType.getProblemTypeId(), oleProblemType.getProblemTypeName()));
            }
        }
        return keyValues;
    }
}
