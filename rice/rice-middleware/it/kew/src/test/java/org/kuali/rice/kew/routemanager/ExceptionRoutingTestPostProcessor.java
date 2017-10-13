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
package org.kuali.rice.kew.routemanager;

import java.util.List;

import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.PostProcessor;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.kew.api.KewApiConstants;


public class ExceptionRoutingTestPostProcessor implements PostProcessor {
	
	public static boolean THROW_ROUTE_STATUS_CHANGE_EXCEPTION;
	public static boolean THROW_ROUTE_STATUS_LEVEL_EXCEPTION;
	public static boolean THROW_ROUTE_DELETE_ROUTE_HEADER_EXCEPTION;
    public static boolean THROW_DO_ACTION_TAKEN_EXCEPTION;
    public static boolean THROW_BEFORE_PROCESS_EXCEPTION;
    public static boolean THROW_AFTER_PROCESS_EXCEPTION;
    public static boolean THROW_DOCUMENT_LOCKING_EXCEPTION;
	public static boolean TRANSITIONED_OUT_OF_EXCEPTION_ROUTING = false;
	public static boolean BLOW_UP_ON_TRANSITION_INTO_EXCEPTION = false;
	
	public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
	        // defend against re-entrancy by only throwing the route status change exception if the status change we are undergoing is not a transition into exception state!
	        // if we don't do this, this postprocessor will blow up when it is subsequently notified about the transition into exception state that it previously caused
	        // which will result in the document never actually transitioning into exception state
	        boolean transitioningIntoException = !KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equals(statusChangeEvent.getOldRouteStatus()) &&
                                                      KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equals(statusChangeEvent.getNewRouteStatus());
		if (THROW_ROUTE_STATUS_CHANGE_EXCEPTION && !transitioningIntoException) {
			throw new RuntimeException("I am the doRouteStatusChange exploder");
		}
		if (BLOW_UP_ON_TRANSITION_INTO_EXCEPTION && transitioningIntoException) {
			throw new RuntimeException("Throwing an exception when transitioning into exception status.");
		}
		if (KewApiConstants.ROUTE_HEADER_EXCEPTION_CD.equals(statusChangeEvent.getOldRouteStatus())) {
			TRANSITIONED_OUT_OF_EXCEPTION_ROUTING = true;
		}
		return new ProcessDocReport(true, "");
	}

	public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
		if (THROW_ROUTE_STATUS_LEVEL_EXCEPTION) {
			throw new RuntimeException("I am the doRouteLevelChange exploder");
		}
		return new ProcessDocReport(true, "");
	}

	public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
		if (THROW_ROUTE_DELETE_ROUTE_HEADER_EXCEPTION) {
			throw new RuntimeException("I am the doDeleteRouteHeader exploder");
		}
		return new ProcessDocReport(true, "");
	}

	public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception {
		if (THROW_DO_ACTION_TAKEN_EXCEPTION) {
			throw new RuntimeException("I am the doActionTaken exploder");
		}
		return new ProcessDocReport(true, "");
	}

    public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
        if (THROW_DO_ACTION_TAKEN_EXCEPTION) {
            throw new RuntimeException("I am the afterActionTaken exploder");
        }
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport beforeProcess(BeforeProcessEvent event) throws Exception {
        if (THROW_BEFORE_PROCESS_EXCEPTION) {
            throw new RuntimeException("I am the beforeProcess exploder");
        }
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport afterProcess(AfterProcessEvent event) throws Exception {
        if (THROW_AFTER_PROCESS_EXCEPTION) {
            throw new RuntimeException("I am the afterProcess exploder");
        }
        return new ProcessDocReport(true, "");
    }

	public List<String> getDocumentIdsToLock(DocumentLockingEvent lockingEvent)
			throws Exception {
		if (THROW_DOCUMENT_LOCKING_EXCEPTION) {
			throw new RuntimeException("I am the getDocumentIdsToLock exploder");
		}
		return null;
	}
    
    

}
