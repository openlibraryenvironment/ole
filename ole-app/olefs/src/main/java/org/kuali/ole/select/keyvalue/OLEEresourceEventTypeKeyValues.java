package org.kuali.ole.select.keyvalue;

import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.select.bo.OLEEResPltfrmEventType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by maheswarang on 11/19/15.
 */
public class OLEEresourceEventTypeKeyValues extends KeyValuesBase {
    private ParameterValueResolver parameterValueResolver ;

    public ParameterValueResolver getParameterValueResolver(){
        if(parameterValueResolver == null){
            parameterValueResolver = ParameterValueResolver.getInstance();
        }
        return parameterValueResolver;
    }

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        Collection<OLEEResPltfrmEventType> oleeResPltfrmEventTypes = KRADServiceLocator.getBusinessObjectService().findAll(OLEEResPltfrmEventType.class);
        String defaultEventType = getParameterValueResolver().getParameter("OLE","OLE-SELECT","Select","DEFAULT_EVENT_TYPE");
        OLEEResPltfrmEventType defaultOleeResPltfrmEventType = null;
        for (OLEEResPltfrmEventType oleeResPltfrmEventType : oleeResPltfrmEventTypes) {
            if (oleeResPltfrmEventType.isActive()) {
                if(defaultEventType !=null && defaultEventType.equals(oleeResPltfrmEventType.geteResPltfrmEventTypeName())){
                    defaultOleeResPltfrmEventType = oleeResPltfrmEventType;
                    continue;
                }
                keyValues.add(new ConcreteKeyValue(oleeResPltfrmEventType.geteResPltfrmEventTypeId(), oleeResPltfrmEventType.geteResPltfrmEventTypeName()));

                }
        }
        Collections.sort(keyValues,
                new Comparator<KeyValue>() {
            @Override
            public int compare(KeyValue keyValue, KeyValue keyValue2) {
                return keyValue.getValue().compareTo(keyValue2.getValue());
            }
            @Override
            public boolean equals(Object o) {
                return false;
            }
        });
        if(defaultOleeResPltfrmEventType!=null){
            keyValues.add(0,new ConcreteKeyValue(defaultOleeResPltfrmEventType.geteResPltfrmEventTypeId(), defaultOleeResPltfrmEventType.geteResPltfrmEventTypeName()));
        }
        return keyValues;
    }
}
