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
package org.kuali.rice.kew.framework.postprocessor;

import org.kuali.rice.kew.api.action.ActionType;

import java.util.List;



/**
 * Hook for applications to perform logic due to workflow events from the engine.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface PostProcessor {

	/**
	 * Executed whenever the status of the document changes.
	 * 
	 * @return ProcessDocReport indicating if the status change succeeded
	 */
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception;
    
    /**
	 * Executed whenever the document transitions from one node to another.
	 * 
	 * @return ProcessDocReport indicating if the node transition succeeded
	 */
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception;
    
    /**
	 * Executed whenever a deletion of the document is required.
	 * 
	 * @return ProcessDocReport indicating if the deletion should be permitted to occur or not
	 */
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception;
    
    /**
     * Executed whenever an action is taken against the document.
     * 
     * @return ProcessDocReport indicating whether or not the action should succeed
     */
    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception;

    /**
     * Executed after an action is taken against the document.
     *
     * @return ProcessDocReport indicating whether or not the action was successful
     * @since 2.1
     */
    public ProcessDocReport afterActionTaken(ActionType actionPerformed, ActionTakenEvent event) throws Exception;
    
    /**
     * Executed prior to processing by the workflow engine.
     *
     * @return ProcessDocReport indicating whether or not the document processing should be allowed to proceed
     */
    public ProcessDocReport beforeProcess(BeforeProcessEvent processEvent) throws Exception;

    /**
     * Executed after processing by the workflow engine has completed.
     *
     * @return ProcessDocReport indicating whether or not the document was successfully processed
     */
    public ProcessDocReport afterProcess(AfterProcessEvent processEvent) throws Exception;
    
    /**
     * Executed prior to document locking in the workflow engine.  This method returns a List of document
     * ids to lock prior to execution of the document in the workflow engine.  If the engine processing is
     * going to result in updates to any other documents, they should be locked at the beginning of the engine
     * processing transaction.  This method facilitates that.
     * 
     * <p>Note that, by default, the id of the document that is being processed by the engine is always
     * locked.  So there is no need to return that document id in the list of document ids to lock.
     * 
     * @return a List of document ids to lock prior to execution of the workflow engine
     */
    public List<String> getDocumentIdsToLock(DocumentLockingEvent lockingEvent) throws Exception;
    
}
