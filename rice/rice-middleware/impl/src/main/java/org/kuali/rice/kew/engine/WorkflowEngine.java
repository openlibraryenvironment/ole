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
package org.kuali.rice.kew.engine;

import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;

/**
 * Defines the contract to the core workflow engine.  The standard unit of work of the engine
 * is the process method.  Document must also be initialized by the WorkflowEngine when they
 * are initially created.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface WorkflowEngine {
        
    public void process(String documentId, String nodeInstanceId) throws Exception;
    
    /**
     * @throws IllegalDocumentTypeException if the given document could not be initialized successfully
     */
    public void initializeDocument(DocumentRouteHeaderValue document);

    
}
