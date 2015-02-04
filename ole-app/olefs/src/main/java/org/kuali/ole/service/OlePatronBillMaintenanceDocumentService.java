package org.kuali.ole.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.MaintenanceDocumentService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/17/12
 * Time: 10:44 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronBillMaintenanceDocumentService  extends MaintenanceDocumentService {
    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters);
}
