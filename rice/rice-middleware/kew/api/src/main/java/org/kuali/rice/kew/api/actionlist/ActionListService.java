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
package org.kuali.rice.kew.api.actionlist;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionItem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@WebService(name = "actionListService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ActionListService {

    /**
     * Gets the number of ActionItems for a given principal
     *
     * @param principalId unique Id for a principal in the system
     *
     * @return a count of ActionItems for a given principal
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code principalId} is null
     */
    @WebMethod(operationName = "getUserActionItemCount")
    @WebResult(name = "userActionItemCount")
	Integer getUserActionItemCount(
			@WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of all {@link ActionItem}s for a {@link org.kuali.rice.kew.api.document.Document}
     *
     * @param documentId unique id of the document to get the ActionItems for
     *
     * @return list of ActionItems for a document
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getAllActionItems")
    @WebResult(name = "actionItems")
    @XmlElementWrapper(name = "actionItems", required = true)
    @XmlElement(name = "actionItem", required = true)
	List<ActionItem> getAllActionItems(
			@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of {@link ActionItem}s for a {@link org.kuali.rice.kew.api.document.Document} that match one of the
     * passed in actionRequestCodes
     *
     * @param documentId unique id of the document to get the ActionItems for
     * @param actionRequestedCodes list of action request codes to match with the ActionItems
     *
     * @return list of ActionItems for a document with a given action request code
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getActionItems")
    @WebResult(name = "actionItems")
    @XmlElementWrapper(name = "actionItems", required = true)
    @XmlElement(name = "actionItem", required = true)
	List<ActionItem> getActionItems(
			@WebParam(name = "documentId") String documentId,
			@WebParam(name = "actionRequestedCodes") List<String> actionRequestedCodes)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of {@link ActionItem}s for a Principal in the system
     *
     * @param principalId unique Id for a principal in the system
     *
     * @return list of ActionItems for a given principal
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code principalId} is null
     */
    @WebMethod(operationName = "getActionItemsForPrincipal")
    @WebResult(name = "actionItems")
    @XmlElementWrapper(name = "actionItems", required = true)
    @XmlElement(name = "actionItem", required = true)
	List<ActionItem> getActionItemsForPrincipal(
			@WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;
	
}
