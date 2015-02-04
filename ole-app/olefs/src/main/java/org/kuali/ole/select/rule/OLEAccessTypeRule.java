package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEAccessType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/21/13
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEAccessTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEAccessType OLEAccessType = (OLEAccessType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleMaterialTypeCode(OLEAccessType);
        return isValid;
    }


    private boolean validateOleMaterialTypeCode(OLEAccessType OLEAccessType) {

        if (OLEAccessType.getOleAccessTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleAccessType.ACCESS_TYPE_NAME, OLEAccessType.getOleAccessTypeName());
            List<OLEAccessType> savedOLEAccessType = (List<OLEAccessType>) getBoService().findMatching(OLEAccessType.class, criteria);
            if ((savedOLEAccessType.size() > 0)) {
                for (OLEAccessType AccessType : savedOLEAccessType) {
                    String accessTypeId = AccessType.getOleAccessTypeId();
                    if (null == OLEAccessType.getOleAccessTypeId() || (!OLEAccessType.getOleAccessTypeId().equalsIgnoreCase(accessTypeId))) {
                        this.putFieldError(OLEConstants.OleAccessType.ACCESS_TYPE_FIELD, OLEConstants.OleAccessType.ERROR_ACCESS_TYPE_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
