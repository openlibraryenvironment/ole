package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.ImportBibUserPreferences;
import org.kuali.ole.describe.service.ImportBibService;
import org.kuali.rice.ken.service.UserPreferenceService;
import org.kuali.rice.ken.service.impl.UserPreferenceServiceImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 27/12/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleUserPreferencesRule
        extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        ImportBibUserPreferences userPreferences = (ImportBibUserPreferences) document.getNewMaintainableObject()
                .getDataObject();

        isValid &= validateDuplicateUserPrefName(userPreferences);
        isValid &= validateEmptyUserPrefName(userPreferences);
        isValid &= validateCallNumber(userPreferences);
        isValid &= validateProtectedNRemovalTags(userPreferences);
        return isValid;
    }


    /**
     * This method  validates duplicate UserPreference name, Id and return boolean value.
     *
     * @param userPreferences
     * @return boolean
     */
    private boolean validateDuplicateUserPrefName(ImportBibUserPreferences userPreferences) {

        if (userPreferences.getPrefName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleUserPreferences.USER_PREF_NM, userPreferences.getPrefName());
            List<ImportBibUserPreferences> dataSourceNameInDatabase = (List<ImportBibUserPreferences>) getBoService()
                    .findMatching(ImportBibUserPreferences.class, criteria);

            if ((dataSourceNameInDatabase.size() > 0)) {
                for (ImportBibUserPreferences dataSourceObj : dataSourceNameInDatabase) {
                    Integer dataSourceId = dataSourceObj.getPrefId();
                    if (null == userPreferences.getPrefId() || (userPreferences.getPrefId().intValue() != dataSourceId
                            .intValue())) {
                        this.putFieldError(OLEConstants.OleUserPreferences.USER_PREF_NAME, "error.duplicate.name");
                        return false;
                    }

                }
            }

        }
        return true;
    }

    /**
     * This method  validates empty UserPreference name and return boolean value.
     *
     * @param userPreferences
     * @return boolean
     */
    private boolean validateEmptyUserPrefName(ImportBibUserPreferences userPreferences) {

        if (userPreferences.getPrefName() == null || userPreferences.getPrefName().equalsIgnoreCase("")
                || userPreferences.getPrefName().length() == 0) {
            this.putFieldError(OLEConstants.OleUserPreferences.USER_PREF_NAME, "error.empty.name");
            return false;
        }
        return true;
    }


    /**
     * This method  validates UserPreference call number and return boolean value.
     *
     * @param userPreferences
     * @return boolean
     */
    private boolean validateCallNumber(ImportBibUserPreferences userPreferences) {
        ImportBibService importBibService = new ImportBibService();
        if (importBibService.callNumValidation(userPreferences)) {
            this.putFieldError(OLEConstants.OleUserPreferences.USER_PREF_CALL_NUMBER, "error.call.number");
            return false;
        }
        return true;
    }

    /**
     * This method  validates UserPreference ProtectedTags and Removal tags and return boolean value.
     *
     * @param userPreferences
     * @return boolean
     */

    private boolean validateProtectedNRemovalTags(ImportBibUserPreferences userPreferences) {
        ImportBibService importBibService = new ImportBibService();
        if (importBibService.proNRemTagValidation(userPreferences)) {
            this.putFieldError(OLEConstants.OleUserPreferences.USER_PREF_TAGS, "error.pro.rem.tags");
            return false;
        }
        return true;
    }
}

