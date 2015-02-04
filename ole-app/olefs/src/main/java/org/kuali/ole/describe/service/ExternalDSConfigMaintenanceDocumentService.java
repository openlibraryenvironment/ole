package org.kuali.ole.describe.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 10/12/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExternalDSConfigMaintenanceDocumentService {
    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters);
}
