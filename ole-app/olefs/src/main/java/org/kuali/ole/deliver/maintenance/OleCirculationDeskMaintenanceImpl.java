package org.kuali.ole.deliver.maintenance;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 2/6/15
 * Time: 1:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationDeskMaintenanceImpl extends MaintainableImpl {

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        Object object = super.retrieveObjectForEditOrCopy(document, dataObjectKeys);
        if (object != null && object instanceof OleCirculationDesk) {
            OleCirculationDesk oleCirculationDesk = (OleCirculationDesk) object;
            List<OleCirculationDeskLocation> oleCirculationDeskLocationList = new ArrayList<OleCirculationDeskLocation>();
            List<OleCirculationDeskLocation> olePickupCirculationDeskLocations = new ArrayList<OleCirculationDeskLocation>();
            if (CollectionUtils.isNotEmpty(oleCirculationDesk.getOleCirculationDeskLocations())) {
                for (OleCirculationDeskLocation oleCirculationDeskLocation : oleCirculationDesk.getOleCirculationDeskLocations()) {
                    if (StringUtils.isNotBlank(oleCirculationDeskLocation.getCirculationPickUpDeskLocation())) {
                        olePickupCirculationDeskLocations.add(oleCirculationDeskLocation);
                    } else {
                        oleCirculationDeskLocationList.add(oleCirculationDeskLocation);
                    }
                }
            }
            oleCirculationDesk.setOleCirculationDeskLocationList(oleCirculationDeskLocationList);
            oleCirculationDesk.setOlePickupCirculationDeskLocations(olePickupCirculationDeskLocations);
        }
        return object;
    }
}
