/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.service.impl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.persistence.jta.TransactionalNoValidationExceptionRollback;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.dao.MaintenanceDocumentDao;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.exception.DocumentTypeAuthorizationException;
import org.kuali.rice.krad.maintenance.Maintainable;
import org.kuali.rice.krad.service.DataObjectAuthorizationService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.MaintenanceDocumentService;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;

/**
 * Service implementation for the MaintenanceDocument structure. This is the
 * default implementation, that is delivered with Kuali
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@TransactionalNoValidationExceptionRollback
public class MaintenanceDocumentServiceImpl implements MaintenanceDocumentService {
    protected static final Logger LOG = Logger.getLogger(MaintenanceDocumentServiceImpl.class);

    private MaintenanceDocumentDao maintenanceDocumentDao;
    private DataObjectAuthorizationService dataObjectAuthorizationService;
    private DocumentService documentService;
    private DataObjectMetaDataService dataObjectMetaDataService;
    private DocumentDictionaryService documentDictionaryService;

    /**
     * @see org.kuali.rice.krad.service.MaintenanceDocumentService#setupNewMaintenanceDocument(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unchecked")
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
    @Override
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction,
            Map<String, String[]> requestParameters) {
        document.getNewMaintainableObject().setMaintenanceAction(maintenanceAction);
        document.getOldMaintainableObject().setMaintenanceAction(maintenanceAction);

        // if action is edit or copy first need to retrieve the old record
        if (!KRADConstants.MAINTENANCE_NEW_ACTION.equals(maintenanceAction) &&
                !KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equals(maintenanceAction)) {
            Object oldDataObject = retrieveObjectForMaintenance(document, requestParameters);

            // TODO should we be using ObjectUtils? also, this needs dictionary
            // enhancement to indicate fields to/not to copy
            Object newDataObject = ObjectUtils.deepCopy((Serializable) oldDataObject);

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
    }

    /**
     * For the edit and delete maintenance actions checks with the
     * <code>BusinessObjectAuthorizationService</code> to check whether the
     * action is allowed for the record data. In action is allowed invokes the
     * custom processing hook on the <code>Maintainble</code>.
     *
     * @param document - document instance for the maintenance object
     * @param oldBusinessObject - the old maintenance record
     * @param maintenanceAction - type of maintenance action requested
     * @param requestParameters - map of parameters from the request
     */
    protected void checkMaintenanceActionAuthorization(MaintenanceDocument document, Object oldBusinessObject,
            String maintenanceAction, Map<String, String[]> requestParameters) {
        if (KRADConstants.MAINTENANCE_EDIT_ACTION.equals(maintenanceAction)) {
            boolean allowsEdit = getDataObjectAuthorizationService()
                    .canMaintain(oldBusinessObject, GlobalVariables.getUserSession().getPerson(),
                            document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
            if (!allowsEdit) {
                LOG.error("Document type " + document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName() +
                        " does not allow edit actions.");
                throw new DocumentTypeAuthorizationException(
                        GlobalVariables.getUserSession().getPerson().getPrincipalId(), "edit",
                        document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
            }

            // invoke custom processing method
            document.getNewMaintainableObject().processAfterEdit(document, requestParameters);
        }
        // 3070
        else if (KRADConstants.MAINTENANCE_DELETE_ACTION.equals(maintenanceAction)) {
            boolean allowsDelete = getDataObjectAuthorizationService()
                    .canMaintain((BusinessObject) oldBusinessObject, GlobalVariables.getUserSession().getPerson(),
                            document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
            if (!allowsDelete) {
                LOG.error("Document type " + document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName() +
                        " does not allow delete actions.");
                throw new DocumentTypeAuthorizationException(
                        GlobalVariables.getUserSession().getPerson().getPrincipalId(), "delete",
                        document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
            }
        }
    }

    /**
     * For the edit or copy actions retrieves the record that is to be
     * maintained
     *
     * <p>
     * Based on the persistence metadata for the maintenance object class
     * retrieves the primary key values from the given request parameters map
     * (if the class is persistable). With those key values attempts to find the
     * record using the <code>LookupService</code>.
     * </p>
     *
     * @param document - document instance for the maintenance object
     * @param requestParameters - Map of parameters from the request
     * @return Object the retrieved old object
     */
    protected Object retrieveObjectForMaintenance(MaintenanceDocument document,
            Map<String, String[]> requestParameters) {
        Map<String, String> keyMap =
                buildKeyMapFromRequest(requestParameters, document.getNewMaintainableObject().getDataObjectClass());

        Object oldDataObject = document.getNewMaintainableObject().retrieveObjectForEditOrCopy(document, keyMap);

        if (oldDataObject == null && !document.getOldMaintainableObject().isExternalBusinessObject()) {
            throw new RuntimeException(
                    "Cannot retrieve old record for maintenance document, incorrect parameters passed on maint url: " +
                            requestParameters);
        }

        if (document.getOldMaintainableObject().isExternalBusinessObject()) {
            if (oldDataObject == null) {
                try {
                    oldDataObject = document.getOldMaintainableObject().getDataObjectClass().newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException(
                            "External BO maintainable was null and unable to instantiate for old maintainable object.",
                            ex);
                }
            }

            populateMaintenanceObjectWithCopyKeyValues(KRADUtils.translateRequestParameterMap(requestParameters),
                    oldDataObject, document.getOldMaintainableObject());
            document.getOldMaintainableObject().prepareExternalBusinessObject((PersistableBusinessObject) oldDataObject);
            oldDataObject = document.getOldMaintainableObject().getDataObject();
        }

        return oldDataObject;
    }

    /**
     * For the copy action clears out primary key values for the old record and
     * does authorization checks on the remaining fields. Also invokes the
     * custom processing method on the <code>Maintainble</code>
     *
     * @param document - document instance for the maintenance object
     * @param maintenanceObject - the object instance being maintained
     * @param requestParameters - map of parameters from the request
     */
    protected void processMaintenanceObjectForCopy(MaintenanceDocument document, Object maintenanceObject,
            Map<String, String[]> requestParameters) {
        if (!document.isFieldsClearedOnCopy()) {
            Maintainable maintainable = document.getNewMaintainableObject();
            if (!getDocumentDictionaryService().getPreserveLockingKeysOnCopy(maintainable.getDataObjectClass())) {
                clearPrimaryKeyFields(maintenanceObject, maintainable.getDataObjectClass());
            }

            clearUnauthorizedNewFields(document);

            maintainable.processAfterCopy(document, requestParameters);

            // mark so that this clearing does not happen again
            document.setFieldsClearedOnCopy(true);
        }
    }

    /**
     * Clears the value of the primary key fields on the maintenance object
     *
     * @param document - document to clear the pk fields on
     * @param dataObjectClass - class to use for retrieving primary key metadata
     */
    protected void clearPrimaryKeyFields(Object maintenanceObject, Class<?> dataObjectClass) {
        List<String> keyFieldNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);
        for (String keyFieldName : keyFieldNames) {
            ObjectPropertyUtils.setPropertyValue(maintenanceObject, keyFieldName, null);
        }
    }

    /**
     * Used as part of the Copy functionality, to clear any field values that
     * the user making the copy does not have permissions to modify. This will
     * prevent authorization errors on a copy.
     *
     * @param document - document to be adjusted
     */
    protected void clearUnauthorizedNewFields(MaintenanceDocument document) {
        // get a reference to the current user
        Person user = GlobalVariables.getUserSession().getPerson();

        // get a new instance of MaintenanceDocumentAuthorizations for context
        // TODO: rework for KRAD
//        MaintenanceDocumentRestrictions maintenanceDocumentRestrictions =
//                getBusinessObjectAuthorizationService().getMaintenanceDocumentRestrictions(document, user);
//
//        clearBusinessObjectOfRestrictedValues(maintenanceDocumentRestrictions);
    }

    /**
     * Based on the maintenance object class retrieves the key field names from
     * the <code>BusinessObjectMetaDataService</code> (or alternatively from the
     * request parameters), then retrieves any matching key value pairs from the
     * request parameters
     *
     * @param requestParameters - map of parameters from the request
     * @param dataObjectClass - class to use for checking security parameter restrictions
     * @return Map<String, String> key value pairs
     */
    protected Map<String, String> buildKeyMapFromRequest(Map<String, String[]> requestParameters,
            Class<?> dataObjectClass) {
        List<String> keyFieldNames = null;

        // translate request parameters
        Map<String, String> parameters = KRADUtils.translateRequestParameterMap(requestParameters);

        // are override keys listed in the request? If so, then those need to be
        // our keys, not the primary key fields for the BO
        if (!StringUtils.isBlank(parameters.get(KRADConstants.OVERRIDE_KEYS))) {
            String[] overrideKeys =
                    parameters.get(KRADConstants.OVERRIDE_KEYS).split(KRADConstants.FIELD_CONVERSIONS_SEPARATOR);
            keyFieldNames = Arrays.asList(overrideKeys);
        } else {
            keyFieldNames = getDataObjectMetaDataService().listPrimaryKeyFieldNames(dataObjectClass);
        }

        return KRADUtils.getParametersFromRequest(keyFieldNames, dataObjectClass, parameters);
    }

    /**
     * Looks for a special request parameters giving the names of the keys that
     * should be retrieved from the request parameters and copied to the
     * maintenance object
     *
     * @param parameters - map of parameters from the request
     * @param oldBusinessObject - the old maintenance object
     * @param oldMaintainableObject - the old maintainble object (used to get object class for
     * security checks)
     */
    protected void populateMaintenanceObjectWithCopyKeyValues(Map<String, String> parameters, Object oldBusinessObject,
            Maintainable oldMaintainableObject) {
        List<String> keyFieldNamesToCopy = null;
        Map<String, String> parametersToCopy = null;

        if (!StringUtils.isBlank(parameters.get(KRADConstants.COPY_KEYS))) {
            String[] copyKeys =
                    parameters.get(KRADConstants.COPY_KEYS).split(KRADConstants.FIELD_CONVERSIONS_SEPARATOR);
            keyFieldNamesToCopy = Arrays.asList(copyKeys);
            parametersToCopy = KRADUtils
                    .getParametersFromRequest(keyFieldNamesToCopy, oldMaintainableObject.getDataObjectClass(),
                            parameters);
        }

        if (parametersToCopy != null) {
            // TODO: make sure we are doing formatting here eventually
            ObjectPropertyUtils.copyPropertiesToObject(parametersToCopy, oldBusinessObject);
        }
    }

    /**
     * @see org.kuali.rice.krad.service.MaintenanceDocumentService#getLockingDocumentId(org.kuali.rice.krad.maintenance.MaintenanceDocument)
     */
    public String getLockingDocumentId(MaintenanceDocument document) {
        return getLockingDocumentId(document.getNewMaintainableObject(), document.getDocumentNumber());
    }

    /**
     * @see org.kuali.rice.krad.service.MaintenanceDocumentService#getLockingDocumentId(org.kuali.rice.krad.maintenance.Maintainable,
     *      java.lang.String)
     */
    public String getLockingDocumentId(Maintainable maintainable, String documentNumber) {
        String lockingDocId = null;
        List<MaintenanceLock> maintenanceLocks = maintainable.generateMaintenanceLocks();
        for (MaintenanceLock maintenanceLock : maintenanceLocks) {
            lockingDocId = maintenanceDocumentDao
                    .getLockingDocumentNumber(maintenanceLock.getLockingRepresentation(), documentNumber);
            if (StringUtils.isNotBlank(lockingDocId)) {
                break;
            }
        }
        return lockingDocId;
    }

    /**
     * @see org.kuali.rice.krad.service.MaintenanceDocumentService#deleteLocks(String)
     */
    public void deleteLocks(String documentNumber) {
        maintenanceDocumentDao.deleteLocks(documentNumber);
    }

    /**
     * @see org.kuali.rice.krad.service.MaintenanceDocumentService#saveLocks(List)
     */
    public void storeLocks(List<MaintenanceLock> maintenanceLocks) {
        maintenanceDocumentDao.storeLocks(maintenanceLocks);
    }

    public MaintenanceDocumentDao getMaintenanceDocumentDao() {
        return maintenanceDocumentDao;
    }

    public void setMaintenanceDocumentDao(MaintenanceDocumentDao maintenanceDocumentDao) {
        this.maintenanceDocumentDao = maintenanceDocumentDao;
    }

    protected DataObjectAuthorizationService getDataObjectAuthorizationService() {
        if (dataObjectAuthorizationService == null) {
            this.dataObjectAuthorizationService = KRADServiceLocatorWeb.getDataObjectAuthorizationService();
        }
        return dataObjectAuthorizationService;
    }

    public void setDataObjectAuthorizationService(DataObjectAuthorizationService dataObjectAuthorizationService) {
        this.dataObjectAuthorizationService = dataObjectAuthorizationService;
    }

    protected DocumentService getDocumentService() {
        return this.documentService;
    }

    public void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        if (dataObjectMetaDataService == null) {
            dataObjectMetaDataService = KRADServiceLocatorWeb.getDataObjectMetaDataService();
        }
        return dataObjectMetaDataService;
    }

    public void setDataObjectMetaDataService(DataObjectMetaDataService dataObjectMetaDataService) {
        this.dataObjectMetaDataService = dataObjectMetaDataService;
    }

    public DocumentDictionaryService getDocumentDictionaryService() {
        if (documentDictionaryService == null) {
            this.documentDictionaryService = KRADServiceLocatorWeb.getDocumentDictionaryService();
        }
        return documentDictionaryService;
    }

    public void setDocumentDictionaryService(DocumentDictionaryService documentDictionaryService) {
        this.documentDictionaryService = documentDictionaryService;
    }
}
