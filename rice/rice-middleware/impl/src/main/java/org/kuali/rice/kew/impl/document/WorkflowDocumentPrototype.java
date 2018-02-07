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
import org.kuali.rice.kew.api.document.Document;

/**
 * An implementation/framework interface that extends WorkflowDocument with an initialization
 * method used internally for construction.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface WorkflowDocumentPrototype extends WorkflowDocument {

    /**
     * Initialize the WorkflowDocument
     * @param principalId the interacting/consuming user principal id
     * @param document the DTO of the document this WorkflowDocument represents
     */
    public void init(String principalId, Document document);
}