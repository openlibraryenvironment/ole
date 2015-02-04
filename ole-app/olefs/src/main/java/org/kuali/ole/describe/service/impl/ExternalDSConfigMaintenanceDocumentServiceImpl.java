package org.kuali.ole.describe.service.impl;

import org.kuali.ole.describe.service.ExternalDSConfigMaintenanceDocumentService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 10/12/12
 * Time: 5:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExternalDSConfigMaintenanceDocumentServiceImpl
        extends MaintenanceDocumentServiceImpl
        implements ExternalDSConfigMaintenanceDocumentService {

    /**
     * @see org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl#setupMaintenanceObject
     */
    /**
     * This method creates maintenance object for delete operation using maintenanceAction.
     *
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
