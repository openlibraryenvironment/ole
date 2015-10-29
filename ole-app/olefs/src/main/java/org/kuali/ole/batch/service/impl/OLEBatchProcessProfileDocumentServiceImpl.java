package org.kuali.ole.batch.service.impl;

import org.kuali.ole.batch.bo.OLEBatchProcessProfileBo;
import org.kuali.ole.batch.xstream.OLEBatchProcessDeepCopyConverter;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.Map;

/**
 * Created by sheiksalahudeenm on 10/23/15.
 */
public class OLEBatchProcessProfileDocumentServiceImpl extends MaintenanceDocumentServiceImpl {

    @Override
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction, Map<String, String[]> requestParameters) {
        if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
            document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
            document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

            // if action is edit or copy first need to retrieve the old record
            if (!KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) &&
                    !KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
                Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);

                // TODO should we be using ObjectUtils? also, this needs dictionary
                // enhancement to indicate fields to/not to copy

                OLEBatchProcessProfileBo newDataObject = new OLEBatchProcessDeepCopyConverter().deepCopyOfOleBatchProcess((OLEBatchProcessProfileBo) oldDataObject);

                // set object instance for editing
                document.getOldMaintainableObject().setDataObject(oldDataObject);
                document.getNewMaintainableObject().setDataObject(newDataObject);

                // process further object preparations for copy action
                if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
                    processMaintenanceObjectForCopy(document, newDataObject, requestParameters);
                } else {
                    checkMaintenanceActionAuthorization(document, oldDataObject, maintenanceAction, requestParameters);
                }
            }

            // if new with existing we need to populate with passed in parameters
            if (KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
                Object newBO = document.getNewMaintainableObject().getDataObject();
                Map<String, String> parameters =
                        buildKeyMapFromRequest(requestParameters, document.getNewMaintainableObject().getDataObjectClass());
                ObjectPropertyUtils.copyPropertiesToObject(parameters, newBO);
                if (newBO instanceof PersistableBusinessObject) {
                    ((PersistableBusinessObject) newBO).refresh();
                }

                document.getNewMaintainableObject().setupNewFromExisting(document, requestParameters);
            } else if (KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction)) {
                document.getNewMaintainableObject().processAfterNew(document, requestParameters);
            }
        } else {
            super.setupMaintenanceObject(document, maintenanceAction, requestParameters);
        }
    }
}
