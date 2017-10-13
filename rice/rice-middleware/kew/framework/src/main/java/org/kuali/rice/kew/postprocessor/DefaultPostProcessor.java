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
package org.kuali.rice.kew.postprocessor;

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

import java.util.List;



/**
 * A simple default implementation of the PostProcessor which can be used
 * as a superclass for post processor which don't want to implement all
 * the methods on the interface.  Simply returns a "true"
 * ProcessDocReport for all events exception for deletion.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DefaultPostProcessor implements PostProcessor {

    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        return new ProcessDocReport(false, "");
    }

    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport beforeProcess(BeforeProcessEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    public ProcessDocReport afterProcess(AfterProcessEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

	public List<String> getDocumentIdsToLock(DocumentLockingEvent lockingEvent)
			throws Exception {
		return null;
	}
    
    

}
