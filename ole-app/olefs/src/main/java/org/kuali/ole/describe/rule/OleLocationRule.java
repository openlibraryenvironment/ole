package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.impl.role.RoleServiceImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleLocationRule validates maintenance object for Ole Location Maintenance Document
 */

public class OleLocationRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleLocation oleLocation = (OleLocation) document.getNewMaintainableObject().getDataObject();
        isValid &= validateLocationCode(oleLocation);
        return isValid;
    }

    /**
     * This method  validates duplicate oleLocation code and return boolean value.
     *
     * @param oleLocation
     * @return boolean
     */
    private boolean validateLocationCode(OleLocation oleLocation) {
        if (oleLocation.getLocationCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.LOC_CD, oleLocation.getLocationCode());
            List<OleLocation> oleLocationInDatabase = (List<OleLocation>) getBoService().findMatching(OleLocation.class, criteria);
            if ((oleLocationInDatabase.size() > 0)) {
                for (OleLocation oleLocationObj : oleLocationInDatabase) {
                    String locationId = oleLocationObj.getLocationId();
                    if (null == oleLocation.getLocationId() || (!oleLocation.getLocationId().equals(locationId))) {
                        this.putFieldError(OLEConstants.LOC_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        if (!oleLocation.getLevelId().equals("5") || !(oleLocation.getLevelCode() != null && oleLocation.getLevelCode().equals(OLEConstants.LOC_LEVEL_SHELVING))) {
            RoleServiceImpl roleServce = new RoleServiceImpl();
            List<String> roleIdList = new ArrayList<String>();
            Role role = roleServce.getRoleByNamespaceCodeAndName(OLEConstants.OlePatron.PATRON_NAMESPACE, OLEConstants.LOC_ADMIN);

            roleIdList.add(role.getId());

            boolean havingRole = roleServce.principalHasRole(GlobalVariables.getUserSession().getPrincipalId(), roleIdList, new HashMap<String, String>());
            if (!havingRole) {
                this.putFieldError(OLEConstants.LOC_LEVEl_ID, OLEConstants.LOC_LEVEL_ERROR);
                return false;
            }

        }
        return true;
    }
}