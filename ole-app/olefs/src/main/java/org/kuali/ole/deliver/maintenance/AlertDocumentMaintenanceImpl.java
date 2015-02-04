package org.kuali.ole.deliver.maintenance;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.alert.bo.AlertDocument;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Date;
import java.util.Map;

/**
 * Created by angelind on 12/24/14.
 */
public class AlertDocumentMaintenanceImpl extends MaintainableImpl {

    @Override
    public void processAfterNew(MaintenanceDocument document,
                                Map<String, String[]> requestParameters) {
        super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_ALERT_DESCRIPTION);
        AlertDocument alertDocument = (AlertDocument)document.getNewMaintainableObject().getDataObject();
        if(GlobalVariables.getUserSession()!=null){
        alertDocument.setAlertDocumentCreatorId(GlobalVariables.getUserSession().getPrincipalId());
        }
    }


    /**
     * This method will set the description when copy is selected
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterCopy(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.COPY_ALERT_DESCRIPTION);
    }

    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.EDIT_ALERT_DESCRIPTION);
    }
}
