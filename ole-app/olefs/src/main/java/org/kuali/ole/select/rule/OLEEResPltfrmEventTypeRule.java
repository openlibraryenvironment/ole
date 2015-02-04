package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEEResPltfrmEventType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEEResPltfrmEventTypeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEEResPltfrmEventType eventType = (OLEEResPltfrmEventType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateEResPltfrmEventTypeName(eventType);
        return isValid;
    }

    /**
     * This method  validates duplicate eresPltfrmEventTypeId and return boolean value.
     *
     * @param eventType
     * @return boolean
     */
    private boolean validateEResPltfrmEventTypeName(OLEEResPltfrmEventType eventType) {
        if (eventType.geteResPltfrmEventTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLEEResPltfrmEventType.ERES_PLTFRM_EVNT_TYP_NAME, eventType.geteResPltfrmEventTypeName());
            List<OLEEResPltfrmEventType> oleeResPltfrmEventTypeList = (List<OLEEResPltfrmEventType>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEEResPltfrmEventType.class, criteria);
            if ((oleeResPltfrmEventTypeList.size() > 0)) {
                for (OLEEResPltfrmEventType oleeResPltfrmEventType : oleeResPltfrmEventTypeList) {
                    String eresPltfrmEventTypeId = oleeResPltfrmEventType.geteResPltfrmEventTypeId();
                    if (null == eventType.geteResPltfrmEventTypeId() || (!eventType.geteResPltfrmEventTypeId().equalsIgnoreCase(eresPltfrmEventTypeId))) {
                        this.putFieldError(OLEConstants.OLEEResPltfrmEventType.ERES_PLTFRM_EVNT_TYP_NAME_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
