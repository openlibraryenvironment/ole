package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEMaterialType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/21/13
 * Time: 11:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class OLEMaterialTypeCodeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEMaterialType OLEMaterialType = (OLEMaterialType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleMaterialTypeName(OLEMaterialType);
        return isValid;
    }


    private boolean validateOleMaterialTypeName(OLEMaterialType OLEMaterialType) {

        if (OLEMaterialType.getOleMaterialTypeId() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleMaterialTypeCode.MATERIAL_TYPE_NAME, OLEMaterialType.getOleMaterialTypeName());
            List<OLEMaterialType> savedOLEMaterialType = (List<OLEMaterialType>) getBoService().findMatching(OLEMaterialType.class, criteria);
            if ((savedOLEMaterialType.size() > 0)) {
                for (OLEMaterialType materialType : savedOLEMaterialType) {
                    String materialTypeId = materialType.getOleMaterialTypeId();
                    if (null == OLEMaterialType.getOleMaterialTypeId() || (!OLEMaterialType.getOleMaterialTypeId().equalsIgnoreCase(materialTypeId))) {
                        this.putFieldError(OLEConstants.OleMaterialTypeCode.MATERIAL_TYPE_FIELD, OLEConstants.OleMaterialTypeCode.ERROR_MATERIAL_TYPE_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

