package org.kuali.ole.deliver.rule;


import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.deliver.bo.OleCirculationDeskLocation;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleCirculationDeskRule validates maintenance object for Circulation Desk Maintenance Document
 */
public class OleCirculationDeskRule extends MaintenanceDocumentRuleBase {

    /**
     * @param document
     * @return boolean
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleCirculationDesk circulationDesk = (OleCirculationDesk) document.getNewMaintainableObject().getDataObject();
        String circulationDeskAction = document.getOldMaintainableObject().getMaintenanceAction();

        isValid &= validateCirculationDeskCode(circulationDeskAction, circulationDesk);
        isValid &= validateCirculationDeskBeforeEdit(circulationDeskAction, circulationDesk);
        return isValid;
    }

    /**
     * This method  validates duplicate circulation Desk Code and return boolean value.
     *
     * @param circulationDesk
     * @return boolean
     */
    private boolean validateCirculationDeskCode(String circulationDeskAction, OleCirculationDesk circulationDesk) {
        if (circulationDesk.getCirculationDeskCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD, circulationDesk.getCirculationDeskCode());
            List<OleCirculationDesk> circulationDeskInDatabase = (List<OleCirculationDesk>) getBoService().findMatching(OleCirculationDesk.class, criteria);
            if ((circulationDeskInDatabase.size() > 0)) {
                for (OleCirculationDesk circulationDeskObj : circulationDeskInDatabase) {
                    String circulationDeskId = circulationDeskObj.getCirculationDeskId();
                    if (null == circulationDesk.getCirculationDeskId() || !(circulationDesk.getCirculationDeskId().equalsIgnoreCase(circulationDeskId))) {
                        this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CODE, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_CD_EXIST);
                        return false;
                    }
                }
            }
        }
        if (circulationDesk.getOleCirculationDeskLocations().size() == 0) {
            this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_ERROR);
            return false;
        }
        List<String> locationCodes = new ArrayList<String>();
        for (OleCirculationDeskLocation oleCirculationDeskLocation : circulationDesk.getOleCirculationDeskLocations()) {
            if (oleCirculationDeskLocation.getCirculationDeskLocation() == null) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(OLEConstants.LOC_CD, oleCirculationDeskLocation.getCirculationLocationCode());
                OleLocation oleLocation = getBoService().findByPrimaryKey(OleLocation.class, map);
                oleCirculationDeskLocation.setLocation(oleLocation);
                oleCirculationDeskLocation.setCirculationDeskLocation(oleLocation != null ? oleLocation.getLocationId() : null);
            }
            locationCodes.add(oleCirculationDeskLocation.getCirculationLocationCode());
        }
        List<OleLocation> oleLocations = (List<OleLocation>) getBoService().findAll(OleLocation.class);
        List<String> validLocationCodes = new ArrayList<String>();
        for (OleLocation oleLocation : oleLocations) {
            for (String locationCode : locationCodes) {
                if (oleLocation.getLocationCode().equals(locationCode))
                    validLocationCodes.add(locationCode);
            }
        }
        if (validLocationCodes.size() != locationCodes.size()) {
            this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_VALID_ERROR);
            return false;
        }
        for (String locationCode : locationCodes) {
            boolean duplicate = false;
            for (String dupLocationCode : validLocationCodes) {
                if (locationCode.equalsIgnoreCase(dupLocationCode)) {
                    if (duplicate) {
                        this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR);
                        return false;
                    }
                    duplicate = true;
                }
            }
        }
        for (OleCirculationDeskLocation oleCirculationDeskLocation : circulationDesk.getOleCirculationDeskLocations()) {
            Map<String, String> circDeskLocation = new HashMap<String, String>();
            circDeskLocation.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, oleCirculationDeskLocation.getCirculationDeskLocation());
            List<OleCirculationDeskLocation> circulationDeskLocationInDatabase = (List<OleCirculationDeskLocation>) getBoService().findMatching(OleCirculationDeskLocation.class, circDeskLocation);
            if ((circulationDeskLocationInDatabase.size() > 0)) {
                if (!circulationDeskAction.equalsIgnoreCase(OLEConstants.OleCirculationDesk.COPY)) {
                    if (!(circulationDeskLocationInDatabase.get(0).getCirculationDeskId().equalsIgnoreCase(oleCirculationDeskLocation.getCirculationDeskId()))) {
                        this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_MAPPED_ERROR);
                        return false;
                    }
                } else {
                    this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_MAPPED_ERROR);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This method is to check whether the circulation desk is having any dependency to the operator before de-activating the document
     *
     * @param circulationDeskAction
     * @param circulationDesk
     * @return boolen
     */
    private boolean validateCirculationDeskBeforeEdit(String circulationDeskAction, OleCirculationDesk circulationDesk) {
        if (circulationDeskAction.equalsIgnoreCase(OLEConstants.OleCirculationDesk.EDIT)) {
            Map<String, String> circulationDeskIdMap = new HashMap<String, String>();
            circulationDeskIdMap.put(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_ID, circulationDesk.getCirculationDeskId());
            List<OleCirculationDeskDetail> circulationDeskDetailInDatabase = (List<OleCirculationDeskDetail>) getBoService().findMatching(OleCirculationDeskDetail.class, circulationDeskIdMap);
            if ((circulationDeskDetailInDatabase.size() > 0)) {
                if (!circulationDesk.isActive()) {
                    this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_MAP, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_MAP_ERROR);
                    return false;
                }
            }
            List<String> locationCodes = new ArrayList<String>();
            if (circulationDesk.getOleCirculationDeskLocations() != null && circulationDesk.getOleCirculationDeskLocations().size() > 0 && locationCodes.size() == 0) {
                locationCodes.add(circulationDesk.getOleCirculationDeskLocations().get(0).getCirculationLocationCode());
            }

            for (int i = 1; i < circulationDesk.getOleCirculationDeskLocations().size(); i++) {

                for (int j = 0; j < locationCodes.size() - 1; j++) {

                    if (locationCodes.get(j).equals(circulationDesk.getOleCirculationDeskLocations().get(i).getCirculationLocationCode())) {
                        this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_DUPLICATE_ERROR);
                        return false;
                    } else {
                        locationCodes.add(circulationDesk.getOleCirculationDeskLocations().get(i).getCirculationLocationCode());
                        j++;
                    }
                }
            }
            List<OleLocation> oleLocations = (List<OleLocation>) getBoService().findAll(OleLocation.class);
            List<String> validLocationCodes = new ArrayList<String>();
            for (OleLocation oleLocation : oleLocations) {
                for (String locationCode : locationCodes) {
                    if (oleLocation.getLocationCode().equals(locationCode))
                        validLocationCodes.add(locationCode);
                }
            }
            if (validLocationCodes.size() != locationCodes.size()) {
                this.putFieldError(OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION, OLEConstants.OleCirculationDesk.OLE_CIRCULATION_DESK_LOCATION_VALID_ERROR);
                return false;
            }

        }
        return true;
    }
}




