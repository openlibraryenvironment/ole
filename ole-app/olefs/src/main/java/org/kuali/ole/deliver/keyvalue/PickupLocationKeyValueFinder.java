package org.kuali.ole.deliver.keyvalue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;

import java.util.*;

/**
 * Created by angelind on 7/27/15.
 */
public class PickupLocationKeyValueFinder extends UifKeyValuesFinderBase {


    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put(OLEConstants.PICKUP_LOCATION, "true");
        Collection<OleCirculationDesk> oleCirculationDesks = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, userMap);
     if(oleCirculationDesks.size()>0){
         for(OleCirculationDesk oleCirculationDesk : oleCirculationDesks){
             options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(),oleCirculationDesk.getCirculationDeskCode()));
         }
     }
        return options;
    }
}
