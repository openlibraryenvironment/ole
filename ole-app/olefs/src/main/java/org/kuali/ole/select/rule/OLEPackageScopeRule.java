package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEPackageScope;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 6/22/13
 * Time: 12:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPackageScopeRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEPackageScope scopeMethod = (OLEPackageScope) document.getNewMaintainableObject().getDataObject();
        isValid &= validateAccessMethodCode(scopeMethod);
        return isValid;
    }

    /**
     * This method  validates duplicate authenticationMethod Id and return boolean value.
     *
     * @param scopeMethod
     * @return boolean
     */
    private boolean validateAccessMethodCode(OLEPackageScope scopeMethod) {
        if (scopeMethod.getOlePackageScopeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OlePackageScope.PACKG_SCOPE_NAME, scopeMethod.getOlePackageScopeName());
            List<OLEPackageScope> authenticationMethodInDatabase = (List<OLEPackageScope>) getBoService().findMatching(OLEPackageScope.class, criteria);
            if ((authenticationMethodInDatabase.size() > 0)) {
                for (OLEPackageScope authenticationMethodObj : authenticationMethodInDatabase) {
                    String olePackageScopeId = authenticationMethodObj.getOlePackageScopeId();
                    if (null == scopeMethod.getOlePackageScopeId() || (!scopeMethod.getOlePackageScopeId().equalsIgnoreCase(olePackageScopeId))) {
                        GlobalVariables.getMessageMap().putError(OLEConstants.OlePackageScope.PACKG_SCOPE_NAME_FIELD, OLEConstants.ERROR_DUPLICATE_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
