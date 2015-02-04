package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEMarcRecordSourceType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEMarcRecordSourceTypeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEMarcRecordSourceType marcRecordSourceType = (OLEMarcRecordSourceType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateMarcRecordSourceTypeName(marcRecordSourceType);
        return isValid;
    }

    /**
     * This method  validates duplicate marcRecordSourceTypeId and return boolean value.
     *
     * @param marcRecordSourceType
     * @return boolean
     */
    private boolean validateMarcRecordSourceTypeName(OLEMarcRecordSourceType marcRecordSourceType) {
        if (marcRecordSourceType.getMarcRecordSourceTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLEMarcRecordSourceType.MARC_REC_SRC_TYP_NAME, marcRecordSourceType.getMarcRecordSourceTypeName());
            List<OLEMarcRecordSourceType> oleMarcRecordSourceTypeList = (List<OLEMarcRecordSourceType>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEMarcRecordSourceType.class, criteria);
            if ((oleMarcRecordSourceTypeList.size() > 0)) {
                for (OLEMarcRecordSourceType oleMarcRecordSourceType : oleMarcRecordSourceTypeList) {
                    String marcRecordSourceTypeId = oleMarcRecordSourceType.getMarcRecordSourceTypeId();
                    if (null == marcRecordSourceType.getMarcRecordSourceTypeId() || (!marcRecordSourceType.getMarcRecordSourceTypeId().equalsIgnoreCase(marcRecordSourceTypeId))) {
                        this.putFieldError(OLEConstants.OLEMarcRecordSourceType.MARC_REC_SRC_TYP_NAME_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
