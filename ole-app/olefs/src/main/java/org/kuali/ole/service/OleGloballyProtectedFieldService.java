package org.kuali.ole.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.MaintenanceDocumentService;

import java.util.Map;

/**
 * This OleGloballyProtectedFieldService is the Service Class for the BibProtectedField Maintenance Document
 */
public interface OleGloballyProtectedFieldService extends MaintenanceDocumentService {

    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters);
}
