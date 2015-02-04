package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleElectronicLocationAndAccessRelationship;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleElectronicLocationAndAccessRelationshipRule validates maintenance object for Electronic Location And Access Relationship Maintenance Document
 */
public class OleElectronicLocationAndAccessRelationshipRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleElectronicLocationAndAccessRelationship oleElectronicLocationAndAccessRelationship = (OleElectronicLocationAndAccessRelationship) document.getNewMaintainableObject().getDataObject();

        isValid &= validateElectronicLocationAndAccessRelationshipCode(oleElectronicLocationAndAccessRelationship);
        return isValid;
    }

    /**
     * This method  validates duplicate elaRelationship Id and return boolean value.
     *
     * @param oleElectronicLocationAndAccessRelationship
     *
     * @return boolean
     */
    private boolean validateElectronicLocationAndAccessRelationshipCode(OleElectronicLocationAndAccessRelationship oleElectronicLocationAndAccessRelationship) {

        if (oleElectronicLocationAndAccessRelationship.getElaRelationshipCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleElectronicLocationAndAccessRelationship.ELECTRONIC_LOCATION_AND_ACCESS_RELATIONSHIP_CD, oleElectronicLocationAndAccessRelationship.getElaRelationshipCode());


            List<OleElectronicLocationAndAccessRelationship> electronicLocationAndAccessRelationshipInDatabase = (List<OleElectronicLocationAndAccessRelationship>) getBoService().findMatching(OleElectronicLocationAndAccessRelationship.class, criteria);

            if ((electronicLocationAndAccessRelationshipInDatabase.size() > 0)) {
                for (OleElectronicLocationAndAccessRelationship oleElAObject : electronicLocationAndAccessRelationshipInDatabase) {
                    Integer elaRelationshipId = oleElAObject.getElaRelationshipId();
                    if (null == oleElectronicLocationAndAccessRelationship.getElaRelationshipId() || oleElectronicLocationAndAccessRelationship.getElaRelationshipId().intValue() != elaRelationshipId) {
                        this.putFieldError(OLEConstants.OleElectronicLocationAndAccessRelationship.ELECTRONIC_LOCATION_AND_ACCESS_RELATIONSHIP_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}