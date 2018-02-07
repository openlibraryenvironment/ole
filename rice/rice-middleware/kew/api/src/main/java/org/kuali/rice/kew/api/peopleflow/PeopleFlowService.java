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
package org.kuali.rice.kew.api.peopleflow;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "peopleFlowService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PeopleFlowService {

    @WebMethod(operationName = "getPeopleFlow")
    @WebResult(name = "peopleFlow")
    PeopleFlowDefinition getPeopleFlow(@WebParam(name = "peopleFlowId") String peopleFlowId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getPeopleFlowByName")
    @WebResult(name = "peopleFlow")
    PeopleFlowDefinition getPeopleFlowByName(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name)
        throws RiceIllegalArgumentException;

    /**
     * TODO...
     *
     * @param peopleFlow
     *
     * @return
     *
     * @throws RiceIllegalArgumentException if the given PeopleFlow definition is null
     * @throws RiceIllegalArgumentException if the given PeopleFlow definition has a non-null id.  When creating a new
     * PeopleFlow definition, the ID will be generated.
     * @throws RiceIllegalStateException if a PeopleFlow with the given namespace code and name already exists
     */
    @WebMethod(operationName = "createPeopleFlow")
    @WebResult(name = "peopleFlow")
    PeopleFlowDefinition createPeopleFlow(@WebParam(name = "peopleFlow") PeopleFlowDefinition peopleFlow)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     *
     * @param peopleFlow
     *
     * @return
     *
     * @throws RiceIllegalArgumentException
     * @throws RiceIllegalStateException if the PeopleFlow does not exist in the system under the given peopleFlowId
     */
    @WebMethod(operationName = "updatePeopleFlow")
    @WebResult(name = "peopleFlow")
    PeopleFlowDefinition updatePeopleFlow(@WebParam(name = "peopleFlow") PeopleFlowDefinition peopleFlow)
        throws RiceIllegalArgumentException, RiceIllegalStateException;


}
