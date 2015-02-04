package org.kuali.ole.alert.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertDocumentType;
import org.kuali.ole.sys.ObjectUtil;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arunag on 12/29/14.
 */
public class AlertDocumentTypeRule  extends MaintenanceDocumentRuleBase {
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        MaintenanceDocument newDocument = null;
        AlertDocumentType alertDocumentType = (AlertDocumentType) document.getNewMaintainableObject().getDataObject();
        Map<String, String> alertMap = new HashMap<String, String>();
        alertMap.put("alertDocumentTypeName", alertDocumentType.getAlertDocumentTypeName());
        List<AlertDocumentType> alertDocumentTypes = (List<AlertDocumentType>) KRADServiceLocator.getBusinessObjectService().findMatching(AlertDocumentType.class, alertMap);
        if (alertDocumentTypes.size()==0) {
            org.kuali.rice.krad.service.DocumentService documentService = GlobalResourceLoader.getService(OLEConstants.DOCUMENT_HEADER_SERVICE);
            try {
                newDocument = (MaintenanceDocument) documentService.getNewDocument(alertDocumentType.getAlertDocumentTypeName());

                alertDocumentType.setAlertDocumentClass(newDocument.getDocumentDataObject().getClass().getName());


            } catch (Exception e) {
                this.putFieldError(OLEConstants.ALERT_DOC_CLASS_NAME, "error.invalid.doctype.name");
                isValid &= false;
                e.printStackTrace();
            }
        }else{
            this.putFieldError(OLEConstants.ALERT_DOC_TYPE_EXIST, "doctype.already.exist");
            isValid &= false;
        }


        return isValid;
    }
}
