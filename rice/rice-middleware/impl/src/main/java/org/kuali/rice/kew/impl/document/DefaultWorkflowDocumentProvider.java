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

import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.kuali.rice.kew.service.KEWServiceLocator;

/**
 * TODO 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class DefaultWorkflowDocumentProvider implements WorkflowDocumentProvider {

    @Override
    public WorkflowDocument createDocument(String principalId, String documentTypeName, DocumentUpdate documentUpdate, DocumentContentUpdate documentContentUpdate) {
        Document document = KewApiServiceLocator.getWorkflowDocumentActionsService().create(documentTypeName, principalId, documentUpdate, documentContentUpdate);
        return initializePrototype(principalId, document);
    }

    @Override
    public WorkflowDocument loadDocument(String principalId, String documentId) {
        Document document = KewApiServiceLocator.getWorkflowDocumentService().getDocument(documentId);
        if (document == null) {
            throw new IllegalArgumentException("Failed to locate workflow document for given documentId: " + documentId);
        }
        return initializePrototype(principalId, document);
    }
    
    private WorkflowDocumentPrototype initializePrototype(String principalId, Document document) {
        WorkflowDocumentPrototype prototype = KEWServiceLocator.getWorkflowDocumentPrototype();
        prototype.init(principalId, document);
        return prototype;
    }

}
