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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.maintenance.Maintainable;

import java.util.List;
import java.util.Map;

/**
 * Provides methods for working with <code>MaintenanceDocument</code>(s)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface MaintenanceDocumentService {

	/**
	 * Prepares the <code>MaintenanceDocument</code> on initial request
	 *
	 * <p>
	 * This includes retrieving the data object for edit or copy, clearing
	 * fields
	 * </p>
	 *
	 * @param objectClassName
	 *            - class name for the object being maintained
	 * @param docTypeName
	 *            - workflow doc type for the maintenance document requested
	 * @param maintenanceAction
	 *            - indicates whether this is a new, copy, or edit maintenance
	 *            action
	 * @return MaintenanceDocument prepared document instance
	 */
	public MaintenanceDocument setupNewMaintenanceDocument(
			String objectClassName, String docTypeName, String maintenanceAction);

    /**
     * Called to setup the object being maintained
     *
     * <p>
     * For edit and copy actions, the old record is retrieved and prepared for
     * editing (in the case of a copy some fields are cleared). In addition some
     * authorization checks are performed and hooks for custom
     * <code>Maintainble</code> implementations are invoked.
     * </p>
     *
     * @param document - document instance for the maintenance object
     * @param maintenanceAction - the requested maintenance action (new, new with existing,
     * copy, edit)
     * @param requestParameters - Map of parameters from the request
     */
    public void setupMaintenanceObject(MaintenanceDocument document, String maintenanceAction,
            Map<String, String[]> requestParameters);

    /**
	 * Attempts to find any other active documents that are pending on the same
	 * maintenance record.
	 *
	 * If any are pending and locked, thereby blocking this document, then the
	 * docHeaderId/documentNumber of the blocking locked document is returned.
	 *
	 * Otherwise, if nothing is blocking, then null is returned.
	 *
	 * @param document
	 *            - document to test
	 * @return A String representing the docHeaderId of any blocking document,
	 *         or null if none are blocking
	 *
	 */
	public String getLockingDocumentId(MaintenanceDocument document);

	/**
	 * Attempts to find any other active documents that are pending on the same
	 * maintenance record.
	 *
	 * If any are pending and locked, thereby blocking this document, then the
	 * docHeaderId/documentNumber of the blocking locked document is returned.
	 *
	 * Otherwise, if nothing is blocking, then null is returned.
	 *
	 * @param maintainable
	 *            - maintainable representing the document to test
	 * @param documentNumber
	 *            - the documentNumber/docHeaderId of the document to test
	 * @return A String representing the docHeaderId of any blocking document,
	 *         or null if none are blocking
	 */
	public String getLockingDocumentId(Maintainable maintainable,
			String documentNumber);

	/**
	 * Call the same-named method in the Dao, since the service has access to
	 * the Dao, but the caller doesn't.
	 *
	 * This method deletes the locks for the given document number. It is called
	 * when the document is final, thus it can be unlocked, or when the locks
	 * need to be regenerated (thus they get cleared first).
	 *
	 * @param documentNumber
	 *            - document number whose locks should be deleted
	 */
	public void deleteLocks(String documentNumber);

	/**
	 * Call the same-named method in the Dao, since the service has access to
	 * the Dao, but the caller doesn't.
	 *
	 * This method stores the given list of maintenance locks. Typically these
	 * will all be for the same document.
	 *
	 * @param maintenanceLocks
	 *            - the list of maintenance locks to be stored
	 */
	public void storeLocks(List<MaintenanceLock> maintenanceLocks);

}
