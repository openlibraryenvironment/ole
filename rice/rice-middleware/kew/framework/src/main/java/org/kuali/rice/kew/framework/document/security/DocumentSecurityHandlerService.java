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
package org.kuali.rice.kew.framework.document.security;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * A remotable service which handles processing of a client application's custom security processing of workflow
 * documents.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KewFrameworkServiceLocator.DOCUMENT_SECURITY_HANDLER_SERVICE, targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface DocumentSecurityHandlerService {

    /**
     * Returns a list of document ids from the given list of document security directives for which the principal with
     * the given principal id is allowed to view.  Any document which is passed to this method as part of a document
     * security directive which is not included in the list of document ids that is returned from this method should
     * <strong>not</strong> be presented to the principal with the given principal id.
     *
     * <p>This method essentially invokes
     * {@link DocumentSecurityAttribute#isAuthorizedForDocument(String, org.kuali.rice.kew.api.document.Document)}
     * method for each of the security attributes supplied in the document security directives, passing the associated
     * list of document ids.</p>
     *
     * @param principalId the id of the principal against which to perform the authorization
     * @param documentSecurityDirectives the list of security directives which define the documents which should be
     * checked for authorization and the name of the {@code DocumentSecurityAttribute} extensions against which to
     * execute the authorization check.
     *
     * @return the list of document ids from the given document security directives for which the given principal is
     * authorized, if a null or empty list is returned, that means that the given principal is not authorized to view
     * information about any of the documents
     *
     * @throws RiceIllegalArgumentException if the given principalId is a null or blank value
     * @throws RiceIllegalArgumentException if any of the security attributes defined in the given list of security
     * directives cannot be located or loaded
     */
    @WebMethod(operationName = "getAuthorizedDocumentIds")
    @WebResult(name = "authorizedDocumentIds")
    @XmlElementWrapper(name = "authorizedDocumentIds", required = true)
    @XmlElement(name = "documentId", required = false)
    List<String> getAuthorizedDocumentIds(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documents") List<DocumentSecurityDirective> documentSecurityDirectives)
        throws RiceIllegalArgumentException;

}
