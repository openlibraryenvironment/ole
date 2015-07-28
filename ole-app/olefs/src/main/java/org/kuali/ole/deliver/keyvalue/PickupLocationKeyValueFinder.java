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

import java.util.*;

/**
 * Created by angelind on 7/27/15.
 */
public class PickupLocationKeyValueFinder extends KeyValuesBase {

    private LoanProcessor loanProcessor = new LoanProcessor();

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> options = new ArrayList<KeyValue>();
        Map<String, String> userMap = new HashMap<String, String>();
        userMap.put(OLEConstants.PICKUP_LOCATION, "true");
        Collection<OleCirculationDesk> oleCirculationDesks = KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, userMap);
        OleCirculationDesk circulationDesk = validateDefaultPickUpLocation();
        if (circulationDesk != null) {
            options.add(new ConcreteKeyValue(circulationDesk.getCirculationDeskId(),circulationDesk.getCirculationDeskCode()));
            for (OleCirculationDesk oleCirculationDesk : oleCirculationDesks) {
                if (!circulationDesk.getCirculationDeskId().equals(oleCirculationDesk.getCirculationDeskId())) {
                    options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk.getCirculationDeskCode()));
                }
            }
        } else {
            options = new ArrayList<KeyValue>();
            options.add(new ConcreteKeyValue("", ""));
            for (OleCirculationDesk oleCirculationDesk : oleCirculationDesks) {
                options.add(new ConcreteKeyValue(oleCirculationDesk.getCirculationDeskId(), oleCirculationDesk.getCirculationDeskCode()));
            }
        }
        return options;
    }

    private OleCirculationDesk validateDefaultPickUpLocation() {
        String parameterValue = loanProcessor.getParameter(OLEConstants.DEFAULT_PICK_UP_LOCATION);
        if (StringUtils.isNotBlank(parameterValue)) {
            Map<String, String> pickUpLocMap = new HashMap<>();
            pickUpLocMap.put(OLEConstants.PICKUP_LOCATION, "true");
            pickUpLocMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, parameterValue);
            List<OleCirculationDesk> oleCirculationDesks = (List<OleCirculationDesk>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCirculationDesk.class, pickUpLocMap);
            if (CollectionUtils.isNotEmpty(oleCirculationDesks)) {
                return oleCirculationDesks.get(0);
            }
        }
        return null;
    }
}
