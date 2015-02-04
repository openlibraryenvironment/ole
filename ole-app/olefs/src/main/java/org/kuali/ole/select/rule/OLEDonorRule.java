package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEDonor;
import org.kuali.ole.select.bo.OLEPaymentType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 3/4/14
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEDonorRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEDonor oleDonor = (OLEDonor) document.getNewMaintainableObject().getDataObject();
        isValid &= validateDonorCode(oleDonor);
        return isValid;
    }

    private boolean validateDonorCode(OLEDonor oleDonor) {
        if (oleDonor.getDonorCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.DONOR_CODE, oleDonor.getDonorCode());
            List<OLEDonor> oleDonorList = (List<OLEDonor>) getLookupService().findCollectionBySearch(OLEDonor.class, criteria);
            if ((oleDonorList.size() > 0)) {
                for (OLEDonor donor : oleDonorList) {
                    String donorId = donor.getDonorId();
                    if (null == oleDonor.getDonorId() || (!oleDonor.getDonorId().equalsIgnoreCase(donorId))) {
                        this.putFieldError(OLEConstants.DONOR_CODE_FIELD, OLEConstants.ERROR_DONOR_CODE);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private LookupService getLookupService() {
        return KRADServiceLocatorWeb.getLookupService();
    }
}
