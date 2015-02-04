package org.kuali.ole.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.MaintenanceDocumentService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/17/12
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleCheckListMaintenanceDocumentService extends MaintenanceDocumentService{

    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters);
}
