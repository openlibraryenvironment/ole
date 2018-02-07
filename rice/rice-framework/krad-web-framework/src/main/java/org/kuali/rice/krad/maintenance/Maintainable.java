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

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.DocumentHeader;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.uif.service.ViewHelperService;

import java.util.List;
import java.util.Map;

/**
 * Provides contract for implementing a maintenance object within the maintenance framework
 *
 * <p> Currently the <code>Maintainable</code> serves many purposes. First since all maintenance documents share the
 * same document class <code>MaintenanceDocumentBase</code> certain document callbacks such as workflow post processing
 * are invoked on the maintainable. Second the maintainable provides a hook for custom actions on the maintenance view.
 * Finally since the maintainable extends <code>ViewHelperService</code> it is used to customize <code>View</code>
 * configuration for <code>MaintenanceDocumentView</code> instances </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Maintainable extends ViewHelperService, java.io.Serializable {

    /**
     * Sets the document number on this maintainable for referencing back to the containing
     * <code>MaintenanceDocument</code>
     *
     * @param documentNumber - document number for the containing maintenance document
     */
    public void setDocumentNumber(String documentNumber);

    /**
     * Invoked when setting the title for the document instance in workflow (doc search results)
     * to customize the title
     *
     * @param document - maintenance document instance to build title for
     * @return String document title
     */
    public String getDocumentTitle(MaintenanceDocument document);

    /**
     * Returns instance of the data object that is being maintained
     *
     * @return Object data object instance
     */
    public Object getDataObject();

    /**
     * Sets an instance of a data object that should be maintained
     *
     * @param object - data object instance
     */
    public void setDataObject(Object object);

    /**
     * Returns the class for the data object being maintained
     *
     * @return Class data object class
     */
    public Class getDataObjectClass();

    /**
     * Sets the class for the data object that will be maintained
     *
     * @param dataObjectClass - class for maintenance data object
     */
    public void setDataObjectClass(Class dataObjectClass);

    /**
     * Indicates whether the object can be locked
     *
     * <p>
     * If this method is overridden, most likely  getPersistableBusinessObject() should be
     * overridden as well.
     * </p>
     *
     * @return true if maintenance is lockable, false otherwise
     */
    public boolean isLockable();

    /**
     * Returns the persistable business object or null if none exists.
     *
     * @return persistable buisness object
     */
    public PersistableBusinessObject getPersistableBusinessObject();

    /**
     * Returns the type of maintenance action this maintainable has been configured with
     *
     * @return String maintenance action string
     */
    public String getMaintenanceAction();

    /**
     * Sets the type of maintenance action to be performed (new, edit, or copy)
     *
     * @param maintenanceAction - string identifying the action type
     */
    public void setMaintenanceAction(String maintenanceAction);

    /**
     * Invoked to generating the list of maintenance locks used to block other edits
     * of the same data object record
     *
     * @return the locking representation(s) of this document, which are reproducible
     *         given the same keys and the same maintainable object
     */
    public List<MaintenanceLock> generateMaintenanceLocks();

    /**
     * Invoked to persist changes to the data object being maintained
     *
     * <p>
     * Called after the maintenance document has become final indicating
     * the changes should be applied
     * </p>
     */
    public void saveDataObject();

    /**
     * Invokes to delete the data object being maintained
     *
     * <p>
     * Called after the maintenance document has become final indicating
     * the changes should be applied
     * </p>
     */
    public void deleteDataObject();

    /**
     * Invoked do perform custom processing when the route status for the containing
     * maintenance document changes
     *
     * <p>
     * Usually used for determining when the document has become final so further actions
     * can take place in addition to the usual persistence of the object changes
     * </p>
     *
     * @param documentHeader - document header instance for containing maintenance document which
     * can be used to check the new status
     */
    public void doRouteStatusChange(DocumentHeader documentHeader);

    /**
     * Retrieves the locking document id for the maintainable which is used to create the
     * maintenance lock string
     *
     * @return String locking id
     */
    public String getLockingDocumentId();

    /**
     * Return an array of document ids to lock prior to processing this document
     * in the workflow engine
     *
     * @return List<String> list of document ids
     */
    public List<String> getWorkflowEngineDocumentIdsToLock();

    /**
     * Indicates whether or not this maintainable supports custom lock
     * descriptors for pessimistic locking.
     *
     * @return boolean true if the maintainable can generate custom lock descriptors,
     *         false otherwise
     * @see #getCustomLockDescriptor(org.kuali.rice.kim.api.identity.Person)
     */
    public boolean useCustomLockDescriptors();

    /**
     * Generates a custom lock descriptor for pessimistic locking. This method
     * should not be called unless {@link #useCustomLockDescriptors()} returns
     * true
     *
     * @param user - the user trying to establish the lock
     * @return String representing the lock descriptor
     * @see #useCustomLockDescriptors()
     * @see org.kuali.rice.krad.service.PessimisticLockService
     */
    public String getCustomLockDescriptor(Person user);

    /**
     * Indicates whether this maintainable supports notes on the maintenance object
     *
     * <p>
     * Note this is only applicable if the data object is an instance of <code>BusinessObject</code>
     * </p>
     *
     * @return boolean true if notes are supported, false if they are not supported
     */
    public boolean isNotesEnabled();

    /**
     * Indicates whether the object being maintained is an instance of <code>ExternalizableBusinessObject</code>
     *
     * <p>
     * For the case when we want to maintain a business object that doesn't
     * necessarily map to a single table in the database or may doesn't map to a
     * database at all
     * </p>
     *
     * @return boolean true if the data object is an external business object, false if not
     */
    public boolean isExternalBusinessObject();

    /**
     * Invoked to prepare a new <code>BusinessObject</code> instance that is external
     *
     * @param businessObject - new business object instance to prepare
     */
    public void prepareExternalBusinessObject(BusinessObject businessObject);

    /**
     * Indicates whether their is an old data object for the maintainable
     *
     * @return boolean true if old data object exists, false if not
     */
    public boolean isOldDataObjectInDocument();

    /**
     * Hook for performing any custom processing before the maintenance object is saved
     */
    public void prepareForSave();

    /**
     * Hook for performing any custom processing after the maintenance object is retrieved from persistence storage
     */
    public void processAfterRetrieve();

    /**
     * Called during setupMaintenanceObject to retrieve the original dataObject that is being
     * edited or copied.  Override this method for non BusinessObject external persistence,
     * Maintainable objects that extend BO should override isExternalBusinessObject and
     * prepareBusinessObject instead.
     *
     * Do not override this method and isExternalBusinessObject.
     *
     * @param document document instance for the maintenance object
     * @param dataObjectKeys Map of keys for the requested object
     * @return the object identified by the dataObjectKeys
     */
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys);

    /**
     * Performs the setting of some attributes that might be necessary
     * if we're creating a new business object using on an existing business object.
     * For example, create a division Vendor based on an existing parent Vendor.
     * (Please see VendorMaintainableImpl.java)
     *
     * @param document - maintenance document instance this maintainable belong to
     * @param parameters - map of request parameters sent for the request
     */
    public void setupNewFromExisting(MaintenanceDocument document, Map<String, String[]> parameters);

    /**
     * Hook for performing any custom processing after the maintenance object has been setup for a copy action
     *
     * @param document - maintenance document instance this maintainable belong to
     * @param requestParameters - map of request parameters sent for the copy request
     */
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters);

    /**
     * Hook for performing any custom processing after the maintenance object has been setup for a edit action
     *
     * @param document - maintenance document instance this maintainable belong to
     * @param requestParameters - map of request parameters sent for the copy request
     */
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters);

    /**
     * Hook for performing any custom processing after the maintenance object has been setup for a new action
     *
     * @param document - maintenance document instance this maintainable belong to
     * @param requestParameters - map of request parameters sent for the copy request
     */
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters);

    /**
     * Hook for performing any custom processing after each posting of the maintenance document (for various actions
     * like add line, refresh)
     *
     * @param document - maintenance document instance this maintainable belong to
     * @param requestParameters - map of request parameters from the post
     */
    public void processAfterPost(MaintenanceDocument document, Map<String, String[]> requestParameters);
}
