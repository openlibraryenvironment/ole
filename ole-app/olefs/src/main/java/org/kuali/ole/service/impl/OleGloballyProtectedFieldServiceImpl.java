package org.kuali.ole.service.impl;

import org.kuali.ole.service.OleGloballyProtectedFieldService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * This OleGloballyProtectedFieldServiceImpl is the implementation for the Service OleGloballyProtectedFieldService
 */
public class OleGloballyProtectedFieldServiceImpl extends MaintenanceDocumentServiceImpl implements OleGloballyProtectedFieldService {


    /**
     * @see org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl#setupMaintenanceObject
     */
    /**
     * This method creates maintenance object for delete operation using maintenanceAction.
     * @param document
     * @param maintenanceAction
     * @param requestParameters
     */
    public void setupMaintenanceObjectForDelete(MaintenanceDocument document, String maintenanceAction,
                                                Map<String, String[]> requestParameters) {
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);
        Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

        document.getOldMaintainableObject().setDataObject(oldDataObject);
        document.getNewMaintainableObject().setDataObject(newDataObject);
    }



}
