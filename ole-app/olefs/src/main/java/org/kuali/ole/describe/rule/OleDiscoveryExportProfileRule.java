package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleCirculationDeskDetail;
import org.kuali.ole.describe.bo.OleDiscoveryExportMappingFields;
import org.kuali.ole.describe.bo.OleDiscoveryExportProfile;
import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/8/13
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDiscoveryExportProfileRule extends MaintenanceDocumentRuleBase {

    /**
     * This method validates the duplicate entry in ITEm and MARC field
     *
     * @param document
     * @return boolean
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleDiscoveryExportProfile oleDiscoveryExportProfile = (OleDiscoveryExportProfile) document.getNewMaintainableObject().getDataObject();
        isValid &= validateForDuplicateMARCField(oleDiscoveryExportProfile);
        isValid &= validateForDuplicateItemField(oleDiscoveryExportProfile);
        return isValid;
    }

    /**
     * This method restrict the MARC Field duplicate values and restrict the document to submit without adding an item
     *
     * @param oleDiscoveryExportProfile
     * @return true if there is no duplication in the MARC Field value and there should be atleast one item added to the document otherwise return false
     */
    private boolean validateForDuplicateMARCField(OleDiscoveryExportProfile oleDiscoveryExportProfile) {
        List<String> MARCItemlist = new ArrayList<String>();
        if (oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields() == null || oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().size() == 0) {
            this.putFieldError(OLEConstants.OleDiscoveryExportProfile.OLE_EXP_ADD_ITEM, OLEConstants.OleDiscoveryExportProfile.OLE_EXP_ADD_ITEM_ERROR);
            return false;
        }
        for (int i = 0; i < oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().size(); i++) {
            MARCItemlist.add(oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(i).getMarcField());
        }
        for (int j = 0; j < oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().size(); j++) {
            int count = 0;
            for (int k = 0; k < MARCItemlist.size(); k++) {
                if (oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(j).getMarcField().equalsIgnoreCase(MARCItemlist.get(k))) {
                    count++;
                }
            }
            if (count > 1) {
                this.putFieldError(OLEConstants.OleDiscoveryExportProfile.OLE_EXP_MARC_FIELD, OLEConstants.OleDiscoveryExportProfile.OLE_EXP_MARC_DUPLICATE_ERROR);
                return false;
            }
        }
        return true;
    }

    /**
     * This method restrict the Item Field duplicate values
     *
     * @param oleDiscoveryExportProfile
     * @return true if there is no duplication in the Item Field value otherwise return false
     */
    private boolean validateForDuplicateItemField(OleDiscoveryExportProfile oleDiscoveryExportProfile) {
        List<String> MARCItemlist = new ArrayList<String>();
        for (int i = 0; i < oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().size(); i++) {
            MARCItemlist.add(oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(i).getItemField());
        }
        for (int j = 0; j < oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().size(); j++) {
            int count = 0;
            for (int k = 0; k < MARCItemlist.size(); k++) {
                if (oleDiscoveryExportProfile.getOleDiscoveryExportMappingFields().get(j).getItemField().equalsIgnoreCase(MARCItemlist.get(k))) {
                    count++;
                }
            }
            if (count > 1) {
                this.putFieldError(OLEConstants.OleDiscoveryExportProfile.OLE_EXP_ITEM_FIELD, OLEConstants.OleDiscoveryExportProfile.OLE_EXP_ITEM_DUPLICATE_ERROR);
                return false;
            }
        }
        return true;
    }
}


