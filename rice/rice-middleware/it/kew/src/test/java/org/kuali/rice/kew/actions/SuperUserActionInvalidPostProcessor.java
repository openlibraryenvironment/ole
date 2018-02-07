/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.actions;

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.*;
import org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * This is a post processor class used for a Super User Test 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SuperUserActionInvalidPostProcessor implements PostProcessor {

    private static final String USER_AUTH_ID = "rkirkend";
    
    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent event) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), event.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }

    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport afterActionTaken(ActionType performed, org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent event) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), event.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }

    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), event.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }

    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), levelChangeEvent.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        if ("WorkflowDocument2".equals(levelChangeEvent.getNewNodeName())) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }

    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), statusChangeEvent.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }
    
    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport beforeProcess(BeforeProcessEvent beforeProcessEvent) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), beforeProcessEvent.getDocumentId()))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }
    
    /**
     * THIS METHOD WILL THROW AN EXCEPTION IF OLD ROUTE NODE IS 'WorkflowTemplate'
     */
    public ProcessDocReport afterProcess(AfterProcessEvent afterProcessEvent) throws Exception {
        if (isDocumentPostProcessable(WorkflowDocumentFactory.loadDocument(getPrincipalId(USER_AUTH_ID), afterProcessEvent.getDocumentId()), Arrays.asList(new String[]{"WorkflowDocument2"}))) {
            return new ProcessDocReport(true, "");
        }
        throw new WorkflowRuntimeException("Post Processor should never be called in this instance");
    }
    
    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#getDocumentIdsToLock(org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent)
     */
    public List<String> getDocumentIdsToLock(DocumentLockingEvent lockingEvent) throws Exception {
		return null;
	}

	private boolean isDocumentPostProcessable(WorkflowDocument doc) throws WorkflowException {
    	return isDocumentPostProcessable(doc, new ArrayList<String>());
    }
    
    private boolean isDocumentPostProcessable(WorkflowDocument doc, List<String> validNodeNames) throws WorkflowException {
        Set<String> nodeNames = doc.getNodeNames();
        if (nodeNames != null && nodeNames.size() > 0) {
        	String nodeName = nodeNames.iterator().next();
        	return validNodeNames.contains(nodeName) || (nodeName.equals("AdHoc")) || (nodeName.equals("WorkflowDocument"));
        }
        return false;
    }

    private String getPrincipalId(String principalName) {
        return KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalName).getPrincipalId();
    }
}
