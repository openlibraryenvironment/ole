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
package org.kuali.rice.kew.api.document;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Set;

/**
 * Defines the contract for a message queue which handles orchestrating documents through the blanket approval process.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "documentOrchestrationQueue", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface DocumentOrchestrationQueue {

    /**
     * Orchestrates the document with the given id through the blanket approval process to the specified list of node
     * names.  If the node names are empty, then the document will be orchestrated all the way to the end of its
     * workflow process.
     *
     * <p>This orchestration can also optional index search attributes after orchestration has complete if the value
     * for {@code shouldSearchIndex} is "true".</p>
     *
     * @param documentId the id of the document to orchestrate through the blanket approval process
     * @param principalId the id of the principal who initiated the blanket approval
     * @param orchestrationConfig contains configuration for how the orchestration should be performed
     *
     * @throws RiceIllegalArgumentException if documentId is a null or blank value
     * @throws RiceIllegalArgumentException if principalId is a null or blank value
     * @throws RiceIllegalArgumentException if orchestrationConfig is null
     */
    @WebMethod(operationName = "orchestrateDocument")
	void orchestrateDocument(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "orchestrationConfig") OrchestrationConfig orchestrationConfig,
            @WebParam(name = "documentProcessingOptions") DocumentProcessingOptions documentProcessingOptions
    ) throws RiceIllegalArgumentException;

}
