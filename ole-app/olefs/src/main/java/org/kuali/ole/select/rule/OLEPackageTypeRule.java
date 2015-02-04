package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPackageType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/21/13
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPackageTypeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEPackageType packageMethod = (OLEPackageType) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(packageMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate authenticationMethod Id and return boolean value.
     *
     * @param packageMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OLEPackageType packageMethod) {
        if (packageMethod.getOlePackageTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OlePackageType.PACKG_TYPE_NAME, packageMethod.getOlePackageTypeName());
            List<OLEPackageType> authenticationMethodInDatabase = (List<OLEPackageType>) getBoService().findMatching(OLEPackageType.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEPackageType authenticationMethodObj : authenticationMethodInDatabase) {
                    String olePackageTypeId = authenticationMethodObj.getOlePackageTypeId();
                    if (null == packageMethod.getOlePackageTypeId() || (!packageMethod.getOlePackageTypeId().equalsIgnoreCase(olePackageTypeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OlePackageType.PACKG_TYPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
