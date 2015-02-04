package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleBibliographicRecordStatus;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/27/12
 * Time: 1:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleBibliographicRecordStatusRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleBibliographicRecordStatus oleBibliographicRecordStatusStatus = (OleBibliographicRecordStatus) document.getNewMaintainableObject().getDataObject();

        isValid &= validateBibliographicRecordStatusCode(oleBibliographicRecordStatusStatus);
        return isValid;
    }

    private boolean validateBibliographicRecordStatusCode(OleBibliographicRecordStatus oleBibliographicRecordStatus) {

        if (oleBibliographicRecordStatus.getBibliographicRecordStatusCode() != null) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put(OLEConstants.OleBibliographicRecordStatus.BIBLIOGRAPHICRECORD_STATUS_CD, oleBibliographicRecordStatus.getBibliographicRecordStatusCode());

            List<OleBibliographicRecordStatus> biblographicRecordStatusCodeInDatabase = (List<OleBibliographicRecordStatus>) getBoService().findMatching(OleBibliographicRecordStatus.class, criteria);

            if ((biblographicRecordStatusCodeInDatabase.size() > 0)) {

                for (OleBibliographicRecordStatus biblographicRecordStatusObj : biblographicRecordStatusCodeInDatabase) {
                    String biblographicRecordStatusId = biblographicRecordStatusObj.getBibliographicRecordStatusId();
                    if (null == oleBibliographicRecordStatus.getBibliographicRecordStatusId() ||
                            !(biblographicRecordStatusId.equalsIgnoreCase(oleBibliographicRecordStatus.getBibliographicRecordStatusId()))) {
                        this.putFieldError(OLEConstants.OleBibliographicRecordStatus.BIBLIOGRAPHICRECORD_STATUS_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
