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

import java.util.List;


/**
 * 
 * This is a marker interface used to determine whether we are dealing with a GlobalBusinessObject or something else
 * 
 * If implementations of this class implement {@link PersistableBusinessObject} as well, then it is strongly recommended that
 * classes override {@link PersistableBusinessObject#buildListOfDeletionAwareLists()} as well.  If this is not done correctly, then
 * deleted collection elements will not be persisted, and upon reload from the DB, the deleted items will appear in the collection.
 */
public interface GlobalBusinessObject {

    /**
     * Gets the documentNumber attribute.
     * 
     * @return Returns the documentNumber
     * 
     */
    public String getDocumentNumber();

    /**
     * Sets the documentNumber attribute.
     * 
     * @param documentNumber The documentNumber to set.
     * 
     */
    public void setDocumentNumber(String documentNumber);

    /**
     * 
     * This method applies the global changed fields to the list of BOs contained within, and returns the list, with all the
     * relevant values updated.
     * 
     * @return Returns a List of BusinessObjects that are ready for persisting, with any relevant values changed
     * 
     */
    public List<PersistableBusinessObject> generateGlobalChangesToPersist();

    /**
     * 
     * This method generates a list of BusinessObjects that need to be deleted as part of the final processing for a global
     * maintenance document. These records should be deleted before the records from getGlobalChangesToPersist() are persisted.
     * 
     * @return A List of BusinessObjects that should be deleted as part of this global maint doc's final processing.
     * 
     */
    public List<PersistableBusinessObject> generateDeactivationsToPersist();

    /**
     * 
     * This method examines the underlying document and determines whether it can be persisted as part of the enclosing
     * MaintenanceDocument. If it returns false, then the Maintenance Document it is part of should not be saved, as a SQL Exception
     * is likely to result.
     * 
     * @return True if the document can be safely persisted, False if not.
     * 
     */
    public boolean isPersistable();

    /**
     * Returns a list of all global detail objects on this document.  This method needs to return all detail
     * objects, even if they are of different types.
     * 
     * @return
     */
    public List<? extends GlobalBusinessObjectDetail> getAllDetailObjects();
}
