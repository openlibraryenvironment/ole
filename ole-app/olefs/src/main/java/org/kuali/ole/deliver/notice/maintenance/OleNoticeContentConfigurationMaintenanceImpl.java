package org.kuali.ole.deliver.notice.maintenance;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.sql.Date;
import java.util.Map;

/**
 * Created by maheswarang on 7/9/15.
 */
public class OleNoticeContentConfigurationMaintenanceImpl extends MaintainableImpl {


    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_NOTICE_CONTENT_CONFIG_DOC);
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
        document.getDocumentHeader().setDocumentDescription(OLEConstants.COPY_NOTICE_CONTENT_CONFIG_DOC);

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
        document.getDocumentHeader().setDocumentDescription(OLEConstants.EDIT_NOTICE_CONTENT_CONFIG_DOC);
    }
}
