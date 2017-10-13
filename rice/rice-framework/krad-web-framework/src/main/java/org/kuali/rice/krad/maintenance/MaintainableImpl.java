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
package org.kuali.rice.krad.maintenance;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.metadata.ClassNotPersistenceCapableException;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.Note;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.exception.PessimisticLockingException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataObjectAuthorizationService;
import org.kuali.rice.krad.service.DataObjectMetaDataService;
import org.kuali.rice.krad.service.DocumentDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.LookupService;
import org.kuali.rice.krad.service.MaintenanceDocumentService;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of the <code>Maintainable</code> interface
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintainableImpl extends ViewHelperServiceImpl implements Maintainable {
    private static final long serialVersionUID = 9125271369161634992L;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintainableImpl.class);

    private String documentNumber;
    private Object dataObject;
    private Class<?> dataObjectClass;
    private String maintenanceAction;

    private transient LookupService lookupService;
    private transient DataObjectAuthorizationService dataObjectAuthorizationService;
    private transient DataObjectMetaDataService dataObjectMetaDataService;
    private transient DocumentDictionaryService documentDictionaryService;
    private transient EncryptionService encryptionService;
    private transient MaintenanceDocumentService maintenanceDocumentService;

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#retrieveObjectForEditOrCopy(MaintenanceDocument, java.util.Map)
     */
    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        Object dataObject = null;

        try {
            dataObject = getLookupService().findObjectBySearch(getDataObjectClass(), dataObjectKeys);
        } catch (ClassNotPersistenceCapableException ex) {
            if (!document.getOldMaintainableObject().isExternalBusinessObject()) {
                throw new RuntimeException("Data Object Class: "
                        + getDataObjectClass()
                        + " is not persistable and is not externalizable - configuration error");
            }
            // otherwise, let fall through
        }

        return dataObject;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#setDocumentNumber
     */
    @Override
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getDocumentTitle
     */
    @Override
    public String getDocumentTitle(MaintenanceDocument document) {
        // default implementation is to allow MaintenanceDocumentBase to
        // generate the doc title
        return "";
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getDataObject
     */
    @Override
    public Object getDataObject() {
        return dataObject;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#setDataObject
     */
    @Override
    public void setDataObject(Object object) {
        this.dataObject = object;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getDataObjectClass
     */
    @Override
    public Class getDataObjectClass() {
        return dataObjectClass;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#setDataObjectClass
     */
    @Override
    public void setDataObjectClass(Class dataObjectClass) {
        this.dataObjectClass = dataObjectClass;
    }

    /**
     * Persistable business objects are lockable
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#isLockable
     */
    @Override
    public boolean isLockable() {
        return KRADServiceLocator.getPersistenceStructureService().isPersistable(getDataObject().getClass());
    }

    /**
     * Returns the data object if its persistable, null otherwise
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#getPersistableBusinessObject
     */
    @Override
    public PersistableBusinessObject getPersistableBusinessObject() {
        if (KRADServiceLocator.getPersistenceStructureService().isPersistable(getDataObject().getClass())) {
            return (PersistableBusinessObject) getDataObject();
        } else {
            return null;
        }

    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getMaintenanceAction
     */
    @Override
    public String getMaintenanceAction() {
        return maintenanceAction;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#setMaintenanceAction
     */
    @Override
    public void setMaintenanceAction(String maintenanceAction) {
        this.maintenanceAction = maintenanceAction;
    }

    /**
     * Note: as currently implemented, every key field for a given
     * data object class must have a visible getter
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#generateMaintenanceLocks
     */
    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        List<MaintenanceLock> maintenanceLocks = new ArrayList<MaintenanceLock>();
        StringBuffer lockRepresentation = new StringBuffer(dataObjectClass.getName());
        lockRepresentation.append(KRADConstants.Maintenance.LOCK_AFTER_CLASS_DELIM);

        Object bo = getDataObject();
        List keyFieldNames = getDocumentDictionaryService().getLockingKeys(getDocumentTypeName());

        for (Iterator i = keyFieldNames.iterator(); i.hasNext(); ) {
            String fieldName = (String) i.next();
            Object fieldValue = ObjectUtils.getPropertyValue(bo, fieldName);
            if (fieldValue == null) {
                fieldValue = "";
            }

            // check if field is a secure
            if (getDataObjectAuthorizationService()
                    .attributeValueNeedsToBeEncryptedOnFormsAndLinks(dataObjectClass, fieldName)) {
                try {
                    if(CoreApiServiceLocator.getEncryptionService().isEnabled()) {
                        fieldValue = getEncryptionService().encrypt(fieldValue);
                    }
                } catch (GeneralSecurityException e) {
                    LOG.error("Unable to encrypt secure field for locking representation " + e.getMessage());
                    throw new RuntimeException(
                            "Unable to encrypt secure field for locking representation " + e.getMessage());
                }
            }

            lockRepresentation.append(fieldName);
            lockRepresentation.append(KRADConstants.Maintenance.LOCK_AFTER_FIELDNAME_DELIM);
            lockRepresentation.append(String.valueOf(fieldValue));
            if (i.hasNext()) {
                lockRepresentation.append(KRADConstants.Maintenance.LOCK_AFTER_VALUE_DELIM);
            }
        }

        MaintenanceLock maintenanceLock = new MaintenanceLock();
        maintenanceLock.setDocumentNumber(documentNumber);
        maintenanceLock.setLockingRepresentation(lockRepresentation.toString());
        maintenanceLocks.add(maintenanceLock);

        return maintenanceLocks;
    }

    /**
     * Retrieves the document type name from the data dictionary based on
     * business object class
     */
    protected String getDocumentTypeName() {
        return getDocumentDictionaryService().getMaintenanceDocumentTypeName(dataObjectClass);
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#saveDataObject
     */
    @Override
    public void saveDataObject() {
        if (dataObject instanceof PersistableBusinessObject) {
            getBusinessObjectService().linkAndSave((PersistableBusinessObject) dataObject);
        } else {
            throw new RuntimeException(
                    "Cannot save object of type: " + dataObjectClass + " with business object service");
        }
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#deleteDataObject
     */
    @Override
    public void deleteDataObject() {
        if (dataObject == null) {
            return;
        }

        if (dataObject instanceof PersistableBusinessObject) {
            getBusinessObjectService().delete((PersistableBusinessObject) dataObject);
            dataObject = null;
        } else {
            throw new RuntimeException(
                    "Cannot delete object of type: " + dataObjectClass + " with business object service");
        }
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#doRouteStatusChange
     */
    @Override
    public void doRouteStatusChange(DocumentHeader documentHeader) {
        // no default implementation
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getLockingDocumentId
     */
    @Override
    public String getLockingDocumentId() {
        return getMaintenanceDocumentService().getLockingDocumentId(this, documentNumber);
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#getWorkflowEngineDocumentIdsToLock
     */
    @Override
    public List<String> getWorkflowEngineDocumentIdsToLock() {
        return null;
    }

    /**
     * Default implementation simply returns false to indicate that custom
     * lock descriptors are not supported by MaintainableImpl. If custom
     * lock descriptors are needed, the appropriate subclasses should override
     * this method
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#useCustomLockDescriptors
     */
    @Override
    public boolean useCustomLockDescriptors() {
        return false;
    }

    /**
     * Default implementation just throws a PessimisticLockingException.
     * Subclasses of MaintainableImpl that need support for custom lock
     * descriptors should override this method
     *
     * @see org.kuali.rice.krad.maintenance.Maintainable#getCustomLockDescriptor
     */
    @Override
    public String getCustomLockDescriptor(Person user) {
        throw new PessimisticLockingException("The Maintainable for document " + documentNumber +
                " is using pessimistic locking with custom lock descriptors, but the Maintainable has not overridden the getCustomLockDescriptor method");
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#isNotesEnabled
     */
    @Override
    public boolean isNotesEnabled() {
        return getDataObjectMetaDataService().areNotesSupported(dataObjectClass);
    }

    /**
     * @see org.kuali.rice.krad.maintenance.MaintainableImpl#isExternalBusinessObject
     */
    @Override
    public boolean isExternalBusinessObject() {
        return false;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.MaintainableImpl#prepareExternalBusinessObject
     */
    @Override
    public void prepareExternalBusinessObject(BusinessObject businessObject) {
        // by default do nothing
    }

    /**
     * Checks whether the data object is not null and has its primary key values populated
     *
     * @see org.kuali.rice.krad.maintenance.MaintainableImpl#isOldDataObjectInDocument
     */
    @Override
    public boolean isOldDataObjectInDocument() {
        boolean isOldDataObjectInExistence = true;

        if (getDataObject() == null) {
            isOldDataObjectInExistence = false;
        } else {
            Map<String, ?> keyFieldValues = getDataObjectMetaDataService().getPrimaryKeyFieldValues(getDataObject());
            for (Object keyValue : keyFieldValues.values()) {
                if (keyValue == null) {
                    isOldDataObjectInExistence = false;
                } else if ((keyValue instanceof String) && StringUtils.isBlank((String) keyValue)) {
                    isOldDataObjectInExistence = false;
                }

                if (!isOldDataObjectInExistence) {
                    break;
                }
            }
        }

        return isOldDataObjectInExistence;
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#prepareForSave
     */
    @Override
    public void prepareForSave() {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterRetrieve
     */
    @Override
    public void processAfterRetrieve() {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.MaintainableImpl#setupNewFromExisting
     */
    @Override
    public void setupNewFromExisting(MaintenanceDocument document, Map<String, String[]> parameters) {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterCopy
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterEdit
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterNew
     */
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        // by default do nothing
    }

    /**
     * @see org.kuali.rice.krad.maintenance.Maintainable#processAfterPost
     */
    @Override
    public void processAfterPost(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        // by default do nothing
    }

    /**
     * In the case of edit maintenance adds a new blank line to the old side
     *
     * TODO: should this write some sort of missing message on the old side
     * instead?
     *
     * @see org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#processAfterAddLine(org.kuali.rice.krad.uif.view.View,
     *      org.kuali.rice.krad.uif.container.CollectionGroup, java.lang.Object,
     *      java.lang.Object)
     */
    @Override
    protected void processAfterAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine,
            boolean isValidLine) {
        super.processAfterAddLine(view, collectionGroup, model, addLine, isValidLine);

        // Check for maintenance documents in edit but exclude notes and ad hoc recipients
        if (model instanceof MaintenanceDocumentForm
                && KRADConstants.MAINTENANCE_EDIT_ACTION.equals(
                ((MaintenanceDocumentForm) model).getMaintenanceAction())
                && !(addLine instanceof Note)
                && !(addLine instanceof AdHocRoutePerson)
                && !(addLine instanceof AdHocRouteWorkgroup)) {
            MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
            MaintenanceDocument document = maintenanceForm.getDocument();

            // get the old object's collection
            //KULRICE-7970 support multiple level objects
            String bindingPrefix = collectionGroup.getBindingInfo().getBindByNamePrefix();
            String propertyPath = collectionGroup.getPropertyName();
            if (bindingPrefix != "" && bindingPrefix != null) {
                propertyPath = bindingPrefix + "." + propertyPath;
            }

            Collection<Object> oldCollection = ObjectPropertyUtils.getPropertyValue(
                    document.getOldMaintainableObject().getDataObject(), propertyPath);

            try {
                Object blankLine = collectionGroup.getCollectionObjectClass().newInstance();
                //Add a blank line to the top of the collection
                if (oldCollection instanceof List) {
                    ((List) oldCollection).add(0, blankLine);
                } else {
                    oldCollection.add(blankLine);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to create new line instance for old maintenance object", e);
            }
        }
    }

    /**
     * In the case of edit maintenance deleted the item on the old side
     *
     *
     * @see org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl#processAfterDeleteLine(View,
     *      org.kuali.rice.krad.uif.container.CollectionGroup, java.lang.Object,  int)
     */
    @Override
    protected void processAfterDeleteLine(View view, CollectionGroup collectionGroup, Object model, int lineIndex) {
        super.processAfterDeleteLine(view, collectionGroup, model, lineIndex);

        // Check for maintenance documents in edit but exclude notes and ad hoc recipients
        if (model instanceof MaintenanceDocumentForm
                && KRADConstants.MAINTENANCE_EDIT_ACTION.equals(((MaintenanceDocumentForm)model).getMaintenanceAction())
                && !collectionGroup.getCollectionObjectClass().getName().equals(Note.class.getName())
                && !collectionGroup.getCollectionObjectClass().getName().equals(AdHocRoutePerson.class.getName())
                && !collectionGroup.getCollectionObjectClass().getName().equals(AdHocRouteWorkgroup.class.getName())) {
            MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
            MaintenanceDocument document = maintenanceForm.getDocument();

            // get the old object's collection
            Collection<Object> oldCollection = ObjectPropertyUtils
                    .getPropertyValue(document.getOldMaintainableObject().getDataObject(),
                            collectionGroup.getPropertyName());
            try {
                // Remove the object at lineIndex from the collection
                oldCollection.remove(oldCollection.toArray()[lineIndex]);
            } catch (Exception e) {
                throw new RuntimeException("Unable to delete line instance for old maintenance object", e);
            }
        }
    }

    /**
     * Retrieves the document number configured on this maintainable
     *
     * @return String document number
     */
    protected String getDocumentNumber() {
        return this.documentNumber;
    }

    protected LookupService getLookupService() {
        if (lookupService == null) {
            lookupService = KRADServiceLocatorWeb.getLookupService();
        }
        return this.lookupService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
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

    protected DataObjectMetaDataService getDataObjectMetaDataService() {
        if (dataObjectMetaDataService == null) {
            this.dataObjectMetaDataService = KRADServiceLocatorWeb.getDataObjectMetaDataService();
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

    protected EncryptionService getEncryptionService() {
        if (encryptionService == null) {
            encryptionService = CoreApiServiceLocator.getEncryptionService();
        }
        return encryptionService;
    }

    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    protected MaintenanceDocumentService getMaintenanceDocumentService() {
        if (maintenanceDocumentService == null) {
            maintenanceDocumentService = KRADServiceLocatorWeb.getMaintenanceDocumentService();
        }
        return maintenanceDocumentService;
    }

    public void setMaintenanceDocumentService(MaintenanceDocumentService maintenanceDocumentService) {
        this.maintenanceDocumentService = maintenanceDocumentService;
    }
}
