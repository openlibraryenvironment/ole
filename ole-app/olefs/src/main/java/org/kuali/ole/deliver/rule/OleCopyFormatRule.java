package org.kuali.ole.deliver.rule;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.CopyFormat;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: anithaa
 * Date: 2/14/14
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCopyFormatRule extends MaintenanceDocumentRuleBase {

    private static final Logger LOG = Logger.getLogger(OleBatchJobRule.class);

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        CopyFormat copyformat = (CopyFormat) document.getNewMaintainableObject().getDataObject();
        CopyFormat oldCopyformat = (CopyFormat) document.getOldMaintainableObject().getDataObject();
        boolean processed = super.processCustomSaveDocumentBusinessRules(document);
        Map<String, String> copyFormatMap = new HashMap<String, String>();
        BusinessObjectService businessObjectService = KRADServiceLocator.getBusinessObjectService();
        copyFormatMap.put("code", copyformat.getCode());
        List<CopyFormat> copyFormatList = (List<CopyFormat>) businessObjectService.findMatching(CopyFormat.class, copyFormatMap);
        if ((copyFormatList.size()> 0)&& copyFormatList!=null ) {
         if(copyformat.getCode().equalsIgnoreCase(oldCopyformat.getCode())) {
            if(copyformat.getCopyFormatId()!=null){

            }
            else {
                isValid = false;
                GlobalVariables.getMessageMap().putError(OLEConstants.OleCirculationDesk.OLE_COPY_FORMAT_CODE, OLEConstants.OleCirculationDesk.OLE_COPY_FORMAT_CODE_ERROR);

            }
        }
        else {
            if (!copyformat.getCode().equalsIgnoreCase(oldCopyformat.getCode())) {
                isValid = false;
                GlobalVariables.getMessageMap().putError(OLEConstants.OleCirculationDesk.OLE_COPY_FORMAT_CODE, OLEConstants.OleCirculationDesk.OLE_COPY_FORMAT_CODE_ERROR);

            }

        }

        }
        return isValid;
    }

}
