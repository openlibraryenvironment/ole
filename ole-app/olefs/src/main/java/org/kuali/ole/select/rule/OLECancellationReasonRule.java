package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLECancellationReason;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 12/9/13
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLECancellationReasonRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLECancellationReason oleCancellationReason = (OLECancellationReason) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOLECancellationReasonName(oleCancellationReason);
        return isValid;
    }

    /**
     * This method  validates duplicate Cancel Reason Name and return boolean value.
     *
     * @param oleCancellationReason
     * @return boolean
     */
    private boolean validateOLECancellationReasonName(OLECancellationReason oleCancellationReason) {

        if (oleCancellationReason.getCancelReasonName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OLECancellationReasonRule.CNCL_RSN_NAME, oleCancellationReason.getCancelReasonName());
            List<OLECancellationReason> savedOLECancellationReason = (List<OLECancellationReason>) getBoService().findMatching(OLECancellationReason.class, criteria);
            if ((savedOLECancellationReason.size() > 0)) {
                for (OLECancellationReason cancellationReason : savedOLECancellationReason) {
                    String cancelReasonId = cancellationReason.getCancelReasonId();
                    if (null == oleCancellationReason.getCancelReasonId() || (!oleCancellationReason.getCancelReasonId().equalsIgnoreCase(cancelReasonId))) {
                        this.putFieldError(OLEConstants.OLECancellationReasonRule.CNCL_RSN_NAME_FIELD, "error.duplicate.name");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
