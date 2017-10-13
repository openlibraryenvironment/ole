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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * Service for {@link ValidationActions}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "validationActionService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ValidationActionService {

    /**
     * Return the {@link ActionDefinition} given the validationId
     * @param validationId of the {@link ActionDefinition} to return
     * @return {@link ActionDefinition} whose value is of the given validationId
     * @throws {@link RiceIllegalArgumentException}
     */
    @WebMethod(operationName = "getValidation")
    @WebResult(name = "validation")
    ActionDefinition getValidation(@WebParam(name = "validationId") String validationId)
            throws RiceIllegalArgumentException;

    /**
     * Return the {@link ActionDefinition} given the namespaceCode and name
     * @param namespaceCode of the {@link ActionDefinition} to return
     * @param name of the {@link ActionDefinition} to return
     * @return {@link ActionDefinition} whose namespaceCode and name are of those given
     * @throws {@link RiceIllegalArgumentException}
     */
    @WebMethod(operationName = "getValidationByName")
    @WebResult(name = "validation")
    ActionDefinition getValidationByName(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "name") String name)
        throws RiceIllegalArgumentException;

    /**
     * Create a Validation Action
     *
     * @param validation {@link ActionDefinition} to create 
     * @return {@link ActionDefinition} created
     *
     * @throws {@link RiceIllegalArgumentException} if the given Validation definition is null
     * @throws {@link RiceIllegalArgumentException} if the given Validation definition has a non-null id.  When creating a new
     * Validation definition, the ID will be generated.
     * @throws {@link RiceIllegalStateException} if a Validation with the given namespace code and name already exists
     */
    @WebMethod(operationName = "createValidationAction")
    @WebResult(name = "validation")
    ActionDefinition createValidation(@WebParam(name = "validation") ActionDefinition validation)
        throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Update a Validation Action
     *
     * @param validation {@link ActionDefinition} to create
     *
     * @return {@link ActionDefinition} updated
     *
     * @throws {@link RiceIllegalArgumentException} if the given Validation definition is null
     * @throws {@link RiceIllegalStateException} if the Validation does not exist in the system under the given validationId
     */
    @WebMethod(operationName = "updateValidationAction")
    @WebResult(name = "validation")
    ActionDefinition updateValidation(@WebParam(name = "validation") ActionDefinition validation)
        throws RiceIllegalArgumentException, RiceIllegalStateException;


}
