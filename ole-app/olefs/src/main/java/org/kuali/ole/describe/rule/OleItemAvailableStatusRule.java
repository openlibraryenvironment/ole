package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.describe.bo.InstanceEditorFormDataHandler;
import org.kuali.ole.describe.bo.OleItemAvailableStatus;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleItemAvailableStatusRule validates maintenance object for Instance Item Available Status Maintenance Document
 */
public class OleItemAvailableStatusRule extends MaintenanceDocumentRuleBase {

    private InstanceEditorFormDataHandler instanceEditorFormDataHandler;

    /**
     * Gets the InstanceEditorFormDataHandler attribute.
     *
     * @return Returns InstanceEditorFormDataHandler.
     */
    private InstanceEditorFormDataHandler getInstanceEditorFormDataHandler() {
        if (null == instanceEditorFormDataHandler) {
            instanceEditorFormDataHandler = new InstanceEditorFormDataHandler();
        }
        return instanceEditorFormDataHandler;
    }

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleItemAvailableStatus oleItemAvailableStatus = (OleItemAvailableStatus) document.getNewMaintainableObject().getDataObject();

        isValid &= validateItemAvailableStatusCode(oleItemAvailableStatus);
        return isValid;
    }

    /**
     * This method  validates duplicate Item Available Status Id and return boolean value.
     *
     * @param oleItemAvailableStatus
     * @return boolean
     */
    private boolean validateItemAvailableStatusCode(OleItemAvailableStatus oleItemAvailableStatus) {

        String itemProperty = getInstanceEditorFormDataHandler()
                .getParameter(OLEConstants.DESC_NMSPC, OLEConstants.DESCRIBE_COMPONENT, OLEParameterConstants.ITEM_STATUS_READONLY);
        String[] itemArray = itemProperty.split(",");
        for (String ItemStatus : itemArray) {
            if (!(oleItemAvailableStatus.isActive())) {
                if (ItemStatus.equalsIgnoreCase(oleItemAvailableStatus.getItemAvailableStatusCode())) {
                    this.putFieldError(OLEConstants.OleItemAvailableStatus.ITEM_ACTIVE_INDICATOR, "error.activeIndicator.false");
                }
            }
        }
        if (oleItemAvailableStatus.getItemAvailableStatusCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CD, oleItemAvailableStatus.getItemAvailableStatusCode());

            List<OleItemAvailableStatus> itemAvailableStatusCodeInDatabase = (List<OleItemAvailableStatus>) getBoService().findMatching(OleItemAvailableStatus.class, criteria);

            if ((itemAvailableStatusCodeInDatabase.size() > 0)) {
                for (OleItemAvailableStatus oleItemAvailableStatusObj : itemAvailableStatusCodeInDatabase) {
                    String itemAvailableStatusId = oleItemAvailableStatusObj.getItemAvailableStatusId();

                    if (null == oleItemAvailableStatus.getItemAvailableStatusId() ||
                            !itemAvailableStatusId.equalsIgnoreCase(oleItemAvailableStatus.getItemAvailableStatusId())) {
                        this.putFieldError(OLEConstants.OleItemAvailableStatus.ITEM_AVAILABLE_STATUS_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}