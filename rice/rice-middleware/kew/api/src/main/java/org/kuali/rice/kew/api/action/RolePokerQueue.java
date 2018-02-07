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
package org.kuali.rice.kew.api.action;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Defines the contract of a message queue for "poking" a role for a specified document.  When a role is "poked" it will
 * attempt to re-resolve the members of a role and update action requests as appropriate.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "rolePokerQueue", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RolePokerQueue {

    @WebMethod(operationName = "reResolveRole")
    void reResolveRole(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "roleName") String roleName
    ) throws RiceIllegalArgumentException;

    @WebMethod(operationName = "reResolveQualifiedRole")
	void reResolveQualifiedRole(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "roleName") String roleName,
            @WebParam(name = "qualifiedRoleNameLabel") String qualifiedRoleNameLabel
    ) throws RiceIllegalArgumentException;
    


}
