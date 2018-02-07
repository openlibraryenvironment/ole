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
package org.kuali.rice.kew.api.document.attribute;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Defines the contract for a message queue that handles indexing of workflow documents.  The indexing process is
 * intended to look at the content associated with the workflow document and extra requested attributes for indexing
 * alongside the document.  These values can then be accessed and/or searched.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "documentAttributeIndexingQueue", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface DocumentAttributeIndexingQueue {

    /**
     * Perform document attribute indexing on the document with the given id.  The given document id must be valid,
     * otherwise a {@code RiceIllegalArgumentException} will be thrown.
     *
     * @param documentId the id of the document to index, cannot be a null or blank value and must be an accurate
     * identifier for an existing document
     *
     * @throws RiceIllegalArgumentException if the given document id is a null or blank value, or if it does not
     * resolve to a valid document
     */
    @WebMethod(operationName = "indexDocument")
	void indexDocument(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;

}
