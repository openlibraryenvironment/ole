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
package org.kuali.rice.krad.bo;

import org.kuali.rice.core.api.mo.common.GloballyUnique;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Declares a common interface for all {@link BusinessObject} classes which can have their
 * state persisted.  A business object which is persistable defines some additional methods
 * which allow for various operations to be executed that relate to the persistent nature of
 * the business object.  A persistable business object also has some additional data
 * attributes which include the version number, the object id, and the extension.
 * 
 * <p>The version number indicates the version of the business object when it was retrieved
 * from persistent storage.  This allows for services to check this version number
 * during persistence operations to prevent silent overwrites of business object state.
 * These kinds of scenarios might arise as a result of concurrent modification to the business
 * object in the persistent store (i.e. two web application users updating the same record
 * in a database).  The kind of check which would be performed using the version number is commonly
 * referred to as "optimistic locking".
 * 
 * <p>The object id represents a globally unique identifier for the business object.  In practice,
 * this can be used by other portions of the system to link to persistable business objects which
 * might be stored in different locations or even different persistent data stores.  In general, it
 * is not the responsibility of the client who implements a persistable business object to handle
 * generating this value.  The framework will handle this automatically at the point in time when
 * the business object is persisted.  If the client does need to do this themselves, however, care
 * should be taken that an appropriate globally unique value generator algorithm is used 
 * (such as the one provided by {@link UUID}).
 * 
 * <p>The extension object is primarily provided for the purposes of allowing implementer
 * customization of the business object without requiring the original business object to be
 * modified.  The additional {@link PersistableBusinessObjectExtension} which is linked with the
 * parent business object can contain additional data attributes and methods.  The framework will
 * automatically request that this extension object be persisted when the parent business object
 * is persisted.  This is generally the most useful in cases where an application is defining
 * business objects that will be used in redistributable software packages (such as the
 * actual Kuali Foundation projects themselves).  If using the framework for the purposes
 * of implementing an internal application, the use of a business object extensions
 * is likely unnecessary.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PersistableBusinessObject extends BusinessObject, Versioned, GloballyUnique {

    /**
     * Sets the business object's version number.  It is rarely advisable
     * for client code to manually set this value as the framework should
     * generally handle the management of version numbers internally.
     * 
     * @param versionNumber the version number to set on this business object
     */
    public void setVersionNumber(Long versionNumber);
    
    /**
     * Sets the unique identifier for the object
     * 
     * @param objectId
     */
    public void setObjectId(String objectId);
   
    public PersistableBusinessObjectExtension getExtension();

    public void setExtension(PersistableBusinessObjectExtension extension);
    
    /**
     * @see BusinessObject#refresh()
     */
    public abstract void refreshNonUpdateableReferences();

    /**
     * This method is used to refresh a reference object that hangs off of a document. For example, if the attribute's keys were
     * updated for a reference object, but the reference object wasn't, this method would go out and retrieve the reference object.
     * 
     * @param referenceObjectName
     */
    public abstract void refreshReferenceObject(String referenceObjectName);

    /**
     * If this method is not implemented appropriately for PersistableBusinessObject with collections, then PersistableBusinessObject with collections will not persist deletions correctly.
     * Elements that have been deleted will reappear in the DB after retrieval.
     * 
     * @return List of collections which need to be monitored for changes by OJB
     */
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists();

    /**
     * Returns the boolean indicating whether this record is a new record of a maintenance document collection.
     * Used to determine whether the record can be deleted on the document.
     */
    public boolean isNewCollectionRecord();
    
    /**
     * Sets the boolean indicating this record is a new record of a maintenance document collection.
     * Used to determine whether the record can be deleted on the document.
     */
    public void setNewCollectionRecord(boolean isNewCollectionRecord);

    /**
     * Hook to link in any editable user fields.
     */
    public void linkEditableUserFields();

    

}
