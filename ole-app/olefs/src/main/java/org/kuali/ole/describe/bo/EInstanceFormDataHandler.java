package org.kuali.ole.describe.bo;

import org.kuali.ole.describe.form.WorkEInstanceOlemlForm;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.LocationLevel;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 9/6/13
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EInstanceFormDataHandler {
    private HoldingOlemlRecordProcessor holdingOlemlRecordProcessor = new HoldingOlemlRecordProcessor();
    public void buildLocationLevels(WorkEInstanceOlemlForm workEInstanceOlemlForm) {
        OleHoldings eHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
        Location location = new Location();
        LocationLevel locationLevel = new LocationLevel();

        String holdingsLocationName = eHoldings.getLocation().getLocationLevel().getName();
        if (!holdingsLocationName.equalsIgnoreCase("")) {
            locationLevel = createLocationLevel(holdingsLocationName, locationLevel);
            location.setLocationLevel(locationLevel);
            location.setPrimary("true");
            location.setStatus("permanent");
            eHoldings.setLocation(location);
        } else {
            eHoldings.setLocation(null);
        }

    }

    public LocationLevel createLocationLevel(String locationName, LocationLevel locationLevel) {
        if (locationName != null && !locationName.equalsIgnoreCase("")) {
            BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
            String[] names = locationName.split("/");
            Map parentCriteria = new HashMap();
            parentCriteria.put("locationCode", names[0]);
            OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
            String locationCode = oleLocationCollection.getLocationCode();
            String levelCode = oleLocationCollection.getOleLocationLevel().getLevelName();
            locationLevel.setName(locationCode);
            locationLevel.setLevel(levelCode);
            String locName = "";
            if (locationName.contains("/"))
                locName = locationName.replace(names[0] + "/", "");
            else
                locName = locationName.replace(names[0], "");
            if (locName != null && !locName.equals("")) {
                LocationLevel newLocationLevel = new LocationLevel();
                locationLevel.setLocationLevel(createLocationLevel(locName, newLocationLevel));
            }
        }
        return locationLevel;
    }

    public String buildEInstanceRecordForDocStore(WorkEInstanceOlemlForm workEInstanceOlemlForm) {

        String content = holdingOlemlRecordProcessor.toXML(workEInstanceOlemlForm.getSelectedEHoldings());
        return content;
    }



    public void setLocationDetails(WorkEInstanceOlemlForm workEInstanceOlemlForm) {
        OleHoldings eHoldings = workEInstanceOlemlForm.getSelectedEHoldings();
        if (eHoldings != null) {
            Location oleHoldingsLocation = eHoldings.getLocation();
            if (oleHoldingsLocation != null) {
                LocationLevel holdingsLocationLevel = oleHoldingsLocation.getLocationLevel();
                String holdingLocationCode = getLocationCode(holdingsLocationLevel);
                if (holdingsLocationLevel != null) {
                    eHoldings.getLocation().getLocationLevel().setName(holdingLocationCode);
                }

            }
        }

    }

    private String getLocationCode(LocationLevel locationLevel) {
        String locationCode = "";
        while (locationLevel != null) {
            String name = locationLevel.getName();
            if (name != null) {
                BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
                Map parentCriteria = new HashMap();
                parentCriteria.put("locationCode", name);
                OleLocation oleLocationCollection = businessObjectService.findByPrimaryKey(OleLocation.class, parentCriteria);
                if (oleLocationCollection != null) {
                    String code = oleLocationCollection.getLocationCode();
                    if (locationCode.equalsIgnoreCase("")) {
                        locationCode = code;
                    } else {
                        locationCode = locationCode + "/" + code;
                    }
                }
            }
            locationLevel = locationLevel.getLocationLevel();
        }
        return locationCode;
    }

}
