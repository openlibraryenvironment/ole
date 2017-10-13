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
package org.kuali.rice.kew.impl.document;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.document.DocumentProcessingOptions;
import org.kuali.rice.kew.api.document.DocumentProcessingQueue;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeIndexingQueue;
import org.kuali.rice.kew.engine.OrchestrationConfig;
import org.kuali.rice.kew.engine.WorkflowEngine;
import org.kuali.rice.kew.engine.WorkflowEngineFactory;

import javax.jws.WebParam;
import java.util.Collections;

/**
 * Reference implementation of the {@code DocumentProcessingQueue}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentProcessingQueueImpl implements DocumentProcessingQueue {

    private static final Logger LOG = Logger.getLogger(DocumentProcessingQueueImpl.class);

    private WorkflowEngineFactory workflowEngineFactory;
    private DocumentAttributeIndexingQueue documentAttributeIndexingQueue;

    @Override
    public void process(@WebParam(name = "documentId") String documentId) {
        processWithOptions(documentId, null);
    }

    @Override
    public void processWithOptions(@WebParam(name = "documentId") String documentId,
            @WebParam(name = "options") DocumentProcessingOptions options) {
        if (StringUtils.isBlank(documentId)) {
            throw new RiceIllegalArgumentException("documentId was a null or blank value");
        }
        if (options == null) {
            options = DocumentProcessingOptions.createDefault();
        }
        OrchestrationConfig config = new OrchestrationConfig(OrchestrationConfig.EngineCapability.STANDARD,
                Collections.<String>emptySet(), null, options.isSendNotifications(), options.isRunPostProcessor());
        WorkflowEngine engine = getWorkflowEngineFactory().newEngine(config);
        try {
			engine.process(documentId, null);
		} catch (Exception e) {
			LOG.error("Failed to process document through the workflow engine", e);
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
			throw new WorkflowRuntimeException(e);
		}
        if (options.isIndexSearchAttributes()) {
            getDocumentAttributeIndexingQueue().indexDocument(documentId);
        }
    }

    public WorkflowEngineFactory getWorkflowEngineFactory() {
        return workflowEngineFactory;
    }

    public void setWorkflowEngineFactory(WorkflowEngineFactory workflowEngineFactory) {
        this.workflowEngineFactory = workflowEngineFactory;
    }

    public DocumentAttributeIndexingQueue getDocumentAttributeIndexingQueue() {
        return documentAttributeIndexingQueue;
    }

    public void setDocumentAttributeIndexingQueue(DocumentAttributeIndexingQueue documentAttributeIndexingQueue) {
        this.documentAttributeIndexingQueue = documentAttributeIndexingQueue;
    }

}
