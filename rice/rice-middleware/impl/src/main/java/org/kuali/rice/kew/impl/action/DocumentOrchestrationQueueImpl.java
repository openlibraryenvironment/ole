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
package org.kuali.rice.kew.impl.action;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.actions.BlanketApproveAction;
import org.kuali.rice.kew.actions.MoveDocumentAction;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.DocumentOrchestrationQueue;
import org.kuali.rice.kew.api.document.DocumentProcessingOptions;
import org.kuali.rice.kew.api.document.OrchestrationConfig;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.identity.principal.Principal;

import javax.jws.WebParam;

/**
 * References implementation of the {@code DocumentOrchestrationQueue}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentOrchestrationQueueImpl implements DocumentOrchestrationQueue {
	
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentOrchestrationQueueImpl.class);

    @Override
    public void orchestrateDocument(String documentId, String principalId, OrchestrationConfig orchestrationConfig,
            DocumentProcessingOptions documentProcessingOptions) {
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId is null or blank");
        }

        if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId is null");
        }

        if (orchestrationConfig == null) {
            throw new RiceIllegalArgumentException("orchestrationConfig is null");
        }
        if (documentProcessingOptions == null) {
            documentProcessingOptions = DocumentProcessingOptions.createDefault();
        }

        LOG.info("Performing document orchestration on documentId=" + documentId);
        KEWServiceLocator.getRouteHeaderService().lockRouteHeader(documentId, true);
        DocumentRouteHeaderValue document = KEWServiceLocator.getRouteHeaderService().getRouteHeader(documentId);
		ActionTakenValue actionTaken = KEWServiceLocator.getActionTakenService().findByActionTakenId(orchestrationConfig.getActionTakenId());
		Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(principalId);
		BlanketApproveAction blanketApprove = new BlanketApproveAction(document, principal, "", orchestrationConfig.getNodeNames());
		try {
			blanketApprove.performDeferredBlanketApproveWork(actionTaken, documentProcessingOptions);
		} catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
			throw new WorkflowRuntimeException(e);
		}
		if (documentProcessingOptions.isIndexSearchAttributes()) {
            DocumentAttributeIndexingQueue queue = KewApiServiceLocator.getDocumentAttributeIndexingQueue(document.getDocumentType().getApplicationId());
            queue.indexDocument(documentId);
		}
        LOG.info("Document orchestration complete against documentId=" + documentId);
    }

}
