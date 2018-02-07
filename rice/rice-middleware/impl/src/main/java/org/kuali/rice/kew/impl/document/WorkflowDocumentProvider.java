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

import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentUpdate;

/**
 * Service provider interface for creation and loading of {@link WorkflowDocument}s.
 * NOTE: WorkflowDocumentFactory constructs a single global instance, so implementations of this interface
 * must be thread-safe.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowDocumentProvider {
     /**
     * Creates a new workflow document of the given type with the given initiator.
     *
     * @param principalId the document initiator
     * @param documentTypeName the document type
     * @param documentUpdate pre-constructed state with which to initialize the document
     * @param documentContentUpdate pre-constructed document content with which to initialize the document
     *
     * @return a WorkflowDocument object through which to interact with the new workflow document
     *
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException if documentTypeName does not represent a valid document type
     */
    WorkflowDocument createDocument(String principalId, String documentTypeName, DocumentUpdate documentUpdate, DocumentContentUpdate documentContentUpdate);

    /**
     * Loads an existing workflow document.
     * @param principalId the principal id under which to perform document actions
     * @param documentId the id of the document to load
     *
     * @return a WorkflowDocument object through which to interact with the loaded workflow document
     *
     * @throws IllegalArgumentException if principalId is null or blank
     * @throws IllegalArgumentException if documentTypeName is null or blank
     * @throws org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException if the specified document type is not active
     * @throws org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException if the specified document type does not support document
     *         creation (in other words, it's a document type that is only used as a parent)
     * @throws org.kuali.rice.kew.api.action.InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     * @see org.kuali.rice.kew.impl.document.WorkflowDocumentProvider#loadDocument(String, String)
     */
    WorkflowDocument loadDocument(String principalId, String documentId);
}