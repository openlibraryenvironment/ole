package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleSeperateOrCompositeReport;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OleSeperateOrCompositeReportRule validates maintenance object for Seperate Or Composite Report Maintenance Document
 */
public class OleSeperateOrCompositeReportRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OleSeperateOrCompositeReport oleSeperateOrCompositeReport = (OleSeperateOrCompositeReport) document.getNewMaintainableObject().getDataObject();

        isValid &= validateSeperateOrCompositeReportCode(oleSeperateOrCompositeReport);
        return isValid;
    }

    /**
     * This method  validates duplicate seperateOrCompositeReport Id and return boolean value.
     *
     * @param oleSeperateOrCompositeReport
     * @return boolean
     */
    private boolean validateSeperateOrCompositeReportCode(OleSeperateOrCompositeReport oleSeperateOrCompositeReport) {

        if (oleSeperateOrCompositeReport.getSeperateOrCompositeReportCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleSeperateOrCompositeReport.SEPERATE_OR_COMPOSITE_REPORT_CD, oleSeperateOrCompositeReport.getSeperateOrCompositeReportCode());
            List<OleSeperateOrCompositeReport> seperateOrCompositeReportCodeInDatabase = (List<OleSeperateOrCompositeReport>) getBoService().findMatching(OleSeperateOrCompositeReport.class, criteria);
            if ((seperateOrCompositeReportCodeInDatabase.size() > 0)) {
                for (OleSeperateOrCompositeReport seperateOrCompositeReportObj : seperateOrCompositeReportCodeInDatabase) {
                    Integer seperateOrCompositeReportId = seperateOrCompositeReportObj.getSeperateOrCompositeReportId();
                    if (null == oleSeperateOrCompositeReport.getSeperateOrCompositeReportId() || (seperateOrCompositeReportId.intValue() != oleSeperateOrCompositeReport.getSeperateOrCompositeReportId().intValue())) {
                        this.putFieldError(OLEConstants.OleSeperateOrCompositeReport.SEPERATE_OR_COMPOSITE_REPORT_CODE, "error.duplicate.code");
                        return false;
                    }
                }
            }
        }

        return true;
    }
}