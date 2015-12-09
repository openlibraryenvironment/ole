package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEProblemType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenchulakshmig on 11/11/14.
 */
public class OLEProblemTypeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEProblemType problemType = (OLEProblemType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateProblemTypeName(problemType);
        return isValid;
    }

    /**
     * This method  validates duplicate problemTypeId and return boolean value.
     *
     * @param problemType
     * @return boolean
     */
    private boolean validateProblemTypeName(OLEProblemType problemType) {
        if (problemType.getProblemTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLEProblemType.PRBLM_TYPE_NAME, problemType.getProblemTypeName());
            List<OLEProblemType> oleProblemTypeList = (List<OLEProblemType>) KRADServiceLocator.getBusinessObjectService().findMatching(OLEProblemType.class, criteria);
            if ((oleProblemTypeList.size() > 0)) {
                for (OLEProblemType oleProblemType : oleProblemTypeList) {
                    String problemTypeId = oleProblemType.getProblemTypeId();
                    if (null == problemType.getProblemTypeId() || (!problemType.getProblemTypeId().equalsIgnoreCase(problemTypeId))) {
                        this.putFieldError(OLEConstants.OLEProblemType.PRBLM_TYPE_NAME_FIELD, OLEConstants.DUPLICATE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
