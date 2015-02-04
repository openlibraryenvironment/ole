package org.kuali.ole.select.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.select.bo.OLEContentType;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: divyaj
 * Date: 6/21/13
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEContentTypeRule extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        OLEContentType OLEContentType = (OLEContentType) document.getNewMaintainableObject().getDataObject();

        isValid &= validateOleMaterialTypeCode(OLEContentType);
        return isValid;
    }


    private boolean validateOleMaterialTypeCode(OLEContentType OLEContentType) {

        if (OLEContentType.getOleContentTypeName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleContentType.CONTENT_TYPE_NAME, OLEContentType.getOleContentTypeName());
            List<OLEContentType> savedOLEContentType = (List<OLEContentType>) getBoService().findMatching(OLEContentType.class, criteria);
            if ((savedOLEContentType.size() > 0)) {
                for (OLEContentType ContentType : savedOLEContentType) {
                    String contentTypeId = ContentType.getOleContentTypeId();
                    if (null == OLEContentType.getOleContentTypeId() || (!OLEContentType.getOleContentTypeId().equalsIgnoreCase(contentTypeId))) {
                        this.putFieldError(OLEConstants.OleContentType.CONTENT_TYPE_FIELD, OLEConstants.OleContentType.ERROR_CONTENT_TYPE_NAME);
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
