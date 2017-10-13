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
package mocks;

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

public class MockPostProcessor implements PostProcessor {

    private boolean processDocReportResult = true;
    private boolean actionTakenResult = true;
    private boolean processMethodsDocReportResult = true;
    private List<String> documentIdsToLockResult = null;
    
    public MockPostProcessor() {
        this(true);
    }
    
    public MockPostProcessor(boolean processDocReportResult) {
    	this(processDocReportResult, true);
    }
    
    public MockPostProcessor(boolean processDocReportResult, boolean actionTakenResult) {
        this(processDocReportResult, actionTakenResult, true);
    }
        
    public MockPostProcessor(boolean processDocReportResult, boolean actionTakenResult, boolean processMethodsDocReportResult) {
    	this(processDocReportResult, actionTakenResult, processMethodsDocReportResult, null);
    }
    
    public MockPostProcessor(boolean processDocReportResult, boolean actionTakenResult, boolean processMethodsDocReportResult, List<String> documentIdsToLockResult) {
        this.processDocReportResult = processDocReportResult;
        this.actionTakenResult = actionTakenResult;
        this.processMethodsDocReportResult = processMethodsDocReportResult;
        this.documentIdsToLockResult = documentIdsToLockResult;
    }
        
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        return new ProcessDocReport(processDocReportResult, "testing");
    }
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
        return new ProcessDocReport(processDocReportResult, "testing");
    }
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
        return new ProcessDocReport(processDocReportResult, "testing");
    }
    
    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception {
    	return new ProcessDocReport(actionTakenResult, "testing");
	}

    public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
        return new ProcessDocReport(actionTakenResult, "testing");
    }

    public ProcessDocReport beforeProcess(BeforeProcessEvent event) throws Exception {
        return new ProcessDocReport(processMethodsDocReportResult, "testing");
    }

    public ProcessDocReport afterProcess(AfterProcessEvent event) throws Exception {
        return new ProcessDocReport(processMethodsDocReportResult, "testing");
    }

    public List<String> getDocumentIdsToLock(DocumentLockingEvent lockingEvent) throws Exception {
		return documentIdsToLockResult;
	}

	public void setProcessDocReportResult(boolean processDocReportResult) {
        this.processDocReportResult = processDocReportResult;
    }

	public void setActionTakenResult(boolean actionTakenResult) {
		this.actionTakenResult = actionTakenResult;
	}

    public void setProcessMethodsDocReportResult(boolean processMethodsDocReportResult) {
        this.processMethodsDocReportResult = processMethodsDocReportResult;
    }
    
    public void setDocumentIdsToLockResult(List<String> documentIdsToLockResult) {
    	this.documentIdsToLockResult = documentIdsToLockResult;
    }

}
