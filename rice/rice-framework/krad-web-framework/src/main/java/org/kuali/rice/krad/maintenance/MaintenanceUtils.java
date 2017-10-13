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
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.exception.KualiExceptionIncident;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Provides static utility methods for use within the maintenance framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceUtils {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MaintenanceUtils.class);

    /**
     * Determines if there is another maintenance document that has a lock on the same key as the given document, and
     * therefore will block the maintenance document from being submitted
     *
     * @param document - maintenance document instance to check locking for
     * @param throwExceptionIfLocked - indicates if an exception should be thrown in the case of found locking document,
     * if false only an error will be added
     */
    public static void checkForLockingDocument(MaintenanceDocument document, boolean throwExceptionIfLocked) {
        LOG.info("starting checkForLockingDocument (by MaintenanceDocument)");

        // get the docHeaderId of the blocking docs, if any are locked and blocking
        //String blockingDocId = getMaintenanceDocumentService().getLockingDocumentId(document);
        String blockingDocId = document.getNewMaintainableObject().getLockingDocumentId();
        checkDocumentBlockingDocumentId(blockingDocId, throwExceptionIfLocked);
    }

    public static void checkDocumentBlockingDocumentId(String blockingDocId, boolean throwExceptionIfLocked) {
        // if we got nothing, then no docs are blocking, and we're done
        if (StringUtils.isBlank(blockingDocId)) {
            return;
        }

        if (MaintenanceUtils.LOG.isInfoEnabled()) {
            MaintenanceUtils.LOG.info("Locking document found:  docId = " + blockingDocId + ".");
        }

        // load the blocking locked document
        WorkflowDocument lockedDocument = null;
        try {
            // need to perform this check to prevent an exception from being thrown by the
            // createWorkflowDocument call - the throw itself causes transaction rollback problems to
            // occur, even though the exception would be caught here
            if (KRADServiceLocatorWeb.getWorkflowDocumentService().workflowDocumentExists(blockingDocId)) {
                lockedDocument = KRADServiceLocatorWeb.getWorkflowDocumentService()
                        .loadWorkflowDocument(blockingDocId, GlobalVariables.getUserSession().getPerson());
            }
        } catch (Exception ex) {
            // clean up the lock and notify the admins
            MaintenanceUtils.LOG.error("Unable to retrieve locking document specified in the maintenance lock table: " +
                    blockingDocId, ex);

            cleanOrphanLocks(blockingDocId, ex);
            return;
        }
        if (lockedDocument == null) {
            MaintenanceUtils.LOG.warn("Locking document header for " + blockingDocId + "came back null.");
            cleanOrphanLocks(blockingDocId, null);
        }

        // if we can ignore the lock (see method notes), then exit cause we're done
        if (lockCanBeIgnored(lockedDocument)) {
            return;
        }

        // build the link URL for the blocking document
        Properties parameters = new Properties();
        parameters.put(KRADConstants.PARAMETER_DOC_ID, blockingDocId);
        parameters.put(KRADConstants.PARAMETER_COMMAND, KRADConstants.METHOD_DISPLAY_DOC_SEARCH_VIEW);
        String blockingUrl = UrlFactory.parameterizeUrl(
                CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                        KRADConstants.WORKFLOW_URL_KEY) +
                        "/" + KRADConstants.DOC_HANDLER_ACTION, parameters);
        if (MaintenanceUtils.LOG.isDebugEnabled()) {
            MaintenanceUtils.LOG.debug("blockingUrl = '" + blockingUrl + "'");
            MaintenanceUtils.LOG.debug("Maintenance record: " + lockedDocument.getApplicationDocumentId() + "is locked.");
        }
        String[] errorParameters = {blockingUrl, blockingDocId};

        // If specified, add an error to the ErrorMap and throw an exception; otherwise, just add a warning to the ErrorMap instead.
        if (throwExceptionIfLocked) {
            // post an error about the locked document
            GlobalVariables.getMessageMap()
                    .putError(KRADConstants.GLOBAL_ERRORS, RiceKeyConstants.ERROR_MAINTENANCE_LOCKED, errorParameters);
            throw new ValidationException("Maintenance Record is locked by another document.");
        } else {
            // Post a warning about the locked document.
            GlobalVariables.getMessageMap()
                    .putWarning(KRADConstants.GLOBAL_MESSAGES, RiceKeyConstants.WARNING_MAINTENANCE_LOCKED,
                            errorParameters);
        }
    }

    /**
     * Guesses whether the current user should be allowed to change a document even though it is locked. It
     * probably should use Authorization instead? See KULNRVSYS-948
     *
     * @param lockedDocument
     * @return
     * @throws org.kuali.rice.kew.api.exception.WorkflowException
     *
     */
    private static boolean lockCanBeIgnored(WorkflowDocument lockedDocument) {
        // TODO: implement real authorization for Maintenance Document Save/Route - KULNRVSYS-948
        if (lockedDocument == null) {
            return true;
        }

        // get the user-id. if no user-id, then we can do this test, so exit
        String userId = GlobalVariables.getUserSession().getPrincipalId().trim();
        if (StringUtils.isBlank(userId)) {
            return false; // dont bypass locking
        }

        // if the current user is not the initiator of the blocking document
        if (!userId.equalsIgnoreCase(lockedDocument.getInitiatorPrincipalId().trim())) {
            return false;
        }

        // if the blocking document hasn't been routed, we can ignore it
        return lockedDocument.isInitiated();
    }

    protected static void cleanOrphanLocks(String lockingDocumentNumber, Exception workflowException) {
        // put a try/catch around the whole thing - the whole reason we are doing this is to prevent data errors
        // from stopping a document
        try {
            // delete the locks for this document since it does not seem to exist
            KRADServiceLocatorWeb.getMaintenanceDocumentService().deleteLocks(lockingDocumentNumber);
            // notify the incident list
            Map<String, String> parameters = new HashMap<String, String>(1);
            parameters.put(KRADConstants.PARAMETER_DOC_ID, lockingDocumentNumber);
            KualiExceptionIncident kei = KRADServiceLocatorWeb.getKualiExceptionIncidentService()
                    .getExceptionIncident(workflowException, parameters);
            KRADServiceLocatorWeb.getKualiExceptionIncidentService().report(kei);
        } catch (Exception ex) {
            MaintenanceUtils.LOG.error("Unable to delete and notify upon locking document retrieval failure.", ex);
        }
    }

   /**
    * Determines if the maintenance document action creates a new record.
    *
    * <p>
    * When taking an action on a maintenance document, some actions cause the creation of a new record, while some don't.
    * For example, editing does not create a record, but copying does.
    * @param maintenanceAction
    * @return boolean
    * </p>
    */
    public static boolean isMaintenanceDocumentCreatingNewRecord(String maintenanceAction) {
        if (KRADConstants.MAINTENANCE_EDIT_ACTION.equalsIgnoreCase(maintenanceAction)) {
            return false;
        } else if (KRADConstants.MAINTENANCE_NEWWITHEXISTING_ACTION.equalsIgnoreCase(maintenanceAction)) {
            return false;
        } else if (KRADConstants.MAINTENANCE_DELETE_ACTION.equalsIgnoreCase(maintenanceAction)) {
            return false;
        } else if (KRADConstants.MAINTENANCE_NEW_ACTION.equalsIgnoreCase(maintenanceAction)) {
            return true;
        } else if (KRADConstants.MAINTENANCE_COPY_ACTION.equalsIgnoreCase(maintenanceAction)) {
            return true;
        } else {
            return true;
        }
    }
}
