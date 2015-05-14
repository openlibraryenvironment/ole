package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestType;
import org.kuali.ole.select.bo.OLEMarcUpdateFrequency;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 5/12/15.
 */
public class OLEMarcUpdateFrequencyRule extends MaintenanceDocumentRuleBase {

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEMarcUpdateFrequency oleMarcUpdateFrequency = (OLEMarcUpdateFrequency) document.getNewMaintainableObject().getDataObject();

        isValid &= validateMarcUpdateFrequency(oleMarcUpdateFrequency);
        return isValid;
    }

    /**
     * This method  validates duplicate completeness Id and return boolean value.
     *
     * @param oleMarcUpdateFrequency
     * @return boolean
     */
    private boolean validateMarcUpdateFrequency(OLEMarcUpdateFrequency oleMarcUpdateFrequency) {

        if (oleMarcUpdateFrequency.getMarcUpdateFrequencyCode() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put("marcUpdateFrequencyCode", oleMarcUpdateFrequency.getMarcUpdateFrequencyCode());
            List<OLEMarcUpdateFrequency> oleMarcUpdateFrequencyCodeInDatabase = (List<OLEMarcUpdateFrequency>) getBoService().findMatching(OLEMarcUpdateFrequency.class, criteria);
            if ((oleMarcUpdateFrequencyCodeInDatabase.size() > 0)) {
                for (OLEMarcUpdateFrequency oleMarcUpdateFrequencyOjb : oleMarcUpdateFrequencyCodeInDatabase) {
                    if (null == oleMarcUpdateFrequencyOjb.getMarcUpdateFrequencyId() || (!oleMarcUpdateFrequency.getMarcUpdateFrequencyId().equals(oleMarcUpdateFrequencyOjb.getMarcUpdateFrequencyId()))) {
                        this.putFieldError("marcUpdateFrequencyCode", "error.duplicate.code");
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
