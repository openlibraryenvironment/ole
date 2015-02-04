package org.kuali.ole.select.maintenance;

import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.maintenance.MaintenanceViewPresentationControllerBase;

/**
 * Presentation Controller class for the License Request Document
 */
public class OleLicenseMaintenanceViewPresentationControllerBase extends MaintenanceViewPresentationControllerBase {
    /**
     * Overrided the method to check for save permission even the document is in enroute state
     *
     * @param document * @return boolean
     */

    @Override
    public boolean canSave(Document document) {
        return getDocumentPresentationController().canSave(document);
    }


}
