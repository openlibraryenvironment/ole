package org.kuali.ole.deliver.keyvalue;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created by maheswarang on 2/9/16.
 */
public class OleCirculationDeskPickUpLocationKeyValues extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map<String, String> criteriaMap = new HashMap<String, String>();
        criteriaMap.put(OLEConstants.PICKUP_LOCATION, "true");
        Collection<OleCirculationDesk> oleCirculationDesks = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, criteriaMap);
        if(oleCirculationDesks.size()>0){
            options.add(new ConcreteKeyValue("",""));
            for(OleCirculationDesk oleCirculationDesk : oleCirculationDesks){
                options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(),oleCirculationDesk.getCirculationDeskCode()));
            }
        }
            return options;
    }
}
