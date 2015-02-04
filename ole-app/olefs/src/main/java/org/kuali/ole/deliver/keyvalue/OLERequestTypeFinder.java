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
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 7/15/13
 * Time: 6:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLERequestTypeFinder extends KeyValuesBase {


    @Override
    public List<KeyValue> getKeyValues() {

        Collection<OleDeliverRequestType> oleDeliverRequestTypeCollection = KRADServiceLocator.getBusinessObjectService().findAll(OleDeliverRequestType.class);
        List<KeyValue> requestKeyLabels = new ArrayList<KeyValue>();
        requestKeyLabels.add(new ConcreteKeyValue("", ""));
        for (OleDeliverRequestType requestType : oleDeliverRequestTypeCollection) {
            if (requestType.isActive()) {
                requestKeyLabels.add(new ConcreteKeyValue(requestType.getRequestTypeCode(), requestType.getRequestTypeCode()));
            }

        }
        return requestKeyLabels;
    }

}

