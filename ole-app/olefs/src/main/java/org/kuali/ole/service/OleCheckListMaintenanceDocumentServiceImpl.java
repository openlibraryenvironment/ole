package org.kuali.ole.service;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.select.bo.OleCheckListBo;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.exception.DocumentTypeAuthorizationException;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.impl.MaintenanceDocumentServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.io.Serializable;
import java.util.Map;

/**
 *OleCheckListMaintenanceDocumentServiceImpl performs maintenance document operation.
 */
public class OleCheckListMaintenanceDocumentServiceImpl extends MaintenanceDocumentServiceImpl implements OleCheckListMaintenanceDocumentService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCheckListMaintenanceDocumentServiceImpl.class);

    /**
     *  This method creates new maintenance document instance for doc type
     * @param objectClassName
     * @param documentTypeName
     * @param maintenanceAction
     * @return MaintenanceDocument
     */
    @Override
    public MaintenanceDocument setupNewMaintenanceDocument(String objectClassName, String documentTypeName,
                                                           String maintenanceAction) {
        if (StringUtils.isEmpty(objectClassName) && StringUtils.isEmpty(documentTypeName)) {
            throw new IllegalArgumentException("Document type name or bo class not given!");
        }

        // get document type if not passed
        if (StringUtils.isEmpty(documentTypeName)) {
            try {
                documentTypeName =
                        getDocumentDictionaryService().getMaintenanceDocumentTypeName(Class.forName(objectClassName));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            if (StringUtils.isEmpty(documentTypeName)) {
                throw new RuntimeException(
                        "documentTypeName is empty; does this Business Object have a maintenance document definition? " +
                                objectClassName);
            }
        }

        // check doc type allows new or copy if that action was requested
        if (KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) ||
                KRADConstants.MAINTENANCE_COPY_ACTION.equals(maintenanceAction)) {
            Class<?> boClass =
                    getDocumentDictionaryService().getMaintenanceDataObjectClass(documentTypeName);
            boolean allowsNewOrCopy = getDataObjectAuthorizationService()
                    .canCreate(boClass, GlobalVariables.getUserSession().getPerson(), documentTypeName);
            if (!allowsNewOrCopy) {
                LOG.error("Document type " + documentTypeName + " does not allow new or copy actions.");
                throw new DocumentTypeAuthorizationException(
                        GlobalVariables.getUserSession().getPerson().getPrincipalId(), "newOrCopy", documentTypeName);
            }
        }

        // get new document from service
        try {
            return (MaintenanceDocument) getDocumentService().getNewDocument(documentTypeName);
        } catch (WorkflowException e) {
            LOG.error("Cannot get new maintenance document instance for doc type: " + documentTypeName, e);
            throw new RuntimeException("Cannot get new maintenance document instance for doc type: " + documentTypeName,
                    e);
        }
    }

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

    @Override
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction,
                                       Map<String, String[]> requestParameters) {

        super.setupMaintenanceObject(document,maintenanceAction,requestParameters);
        // set object instance for editing

        OleCheckListBo oldDataObject = (OleCheckListBo) document.getOldMaintainableObject().getDataObject();
        OleCheckListBo newDataObject = (OleCheckListBo) ObjectUtils.deepCopy((Serializable) oldDataObject);
        //newDataObject.setFileName(null);
        document.getOldMaintainableObject().setDataObject(oldDataObject);
        document.getNewMaintainableObject().setDataObject(newDataObject);
    }
}
