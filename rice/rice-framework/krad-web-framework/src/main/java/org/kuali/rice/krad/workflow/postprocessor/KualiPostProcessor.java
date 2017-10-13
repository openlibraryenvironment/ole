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
package org.kuali.rice.krad.workflow.postprocessor;

import org.apache.log4j.Logger;
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
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.List;

/**
 * Public entry point by which workflow communicates status changes,
 * level changes, and other useful changes.
 *
 * Note that this class delegates all of these activities to the PostProcessorService,
 * which does the actual work.  This is done to ensure proper transaction scoping, and
 * to resolve some issues present otherwise.
 *
 * Because of this, its important to understand that a transaction will be started at
 * the PostProcessorService method call, so any work that needs to be done within the
 * same transaction needs to happen inside that service implementation, rather than
 * in here.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KualiPostProcessor implements PostProcessor {
    private static Logger LOG = Logger.getLogger(KualiPostProcessor.class);

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    @Override
    public ProcessDocReport doRouteStatusChange(DocumentRouteStatusChange statusChangeEvent) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().doRouteStatusChange(statusChangeEvent);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport doActionTaken(ActionTakenEvent event) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().doActionTaken(event);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterActionTaken(org.kuali.rice.kew.api.action.ActionType,
     *      org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport afterActionTaken(ActionType performed, ActionTakenEvent event) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().afterActionTaken(performed, event);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doDeleteRouteHeader(org.kuali.rice.kew.framework.postprocessor.DeleteEvent)
     */
    @Override
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().doDeleteRouteHeader(event);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    @Override
    public ProcessDocReport doRouteLevelChange(DocumentRouteLevelChange levelChangeEvent) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().doRouteLevelChange(levelChangeEvent);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#beforeProcess(org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent)
     */
    @Override
    public ProcessDocReport beforeProcess(BeforeProcessEvent beforeProcessEvent) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().beforeProcess(beforeProcessEvent);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterProcess(org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent)
     */
    @Override
    public ProcessDocReport afterProcess(AfterProcessEvent afterProcessEvent) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().afterProcess(afterProcessEvent);
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#getDocumentIdsToLock(org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent)
     */
    @Override
    public List<String> getDocumentIdsToLock(DocumentLockingEvent documentLockingEvent) throws Exception {
        return KRADServiceLocatorWeb.getPostProcessorService().getDocumentIdsToLock(documentLockingEvent);
    }

}
