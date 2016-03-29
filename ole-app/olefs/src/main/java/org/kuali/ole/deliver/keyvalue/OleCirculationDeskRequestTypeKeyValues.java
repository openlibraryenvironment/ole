package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maheswarang on 2/9/16.
 */
public class OleCirculationDeskRequestTypeKeyValues extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {

        List<KeyValue> requestKeyLabels = new ArrayList<KeyValue>();
        Collection<OleDeliverRequestType> oleDeliverRequestTypeCollection = KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestType.class);
        if(oleDeliverRequestTypeCollection.size()>0){
            requestKeyLabels.add(new ConcreteKeyValue("",""));
            for(OleDeliverRequestType oleDeliverRequestType : oleDeliverRequestTypeCollection){
                requestKeyLabels.add(new ConcreteKeyValue(oleDeliverRequestType.getRequestTypeCode(),oleDeliverRequestType.getRequestTypeCode()));
            }
        }
    return requestKeyLabels;
    }
}
