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
package org.kuali.rice.krms.api.repository.function;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.KrmsConstants;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * The function repository contains information about custom functions which
 * can be used on propositions that are defined when constructing rules.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(name = "functionRepositoryService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface FunctionRepositoryService {
	
	/**
	 * Retrieves the function for the given functionId.  The function can be used when
	 * constructing propositions and defines the type of the parameters to the function
	 * as well as it's return type.
	 *
	 * @param functionId the id of the function to retrieve
	 * @return the function definition, or null if no function could be located for the given functionId
	 * 
	 * @throws RiceIllegalArgumentException if the given functionId is null
	 */
	@WebMethod(operationName = "getFunction")
	@WebResult(name = "function")
	public FunctionDefinition getFunction(@WebParam(name = "functionId") String functionId)
            throws RiceIllegalArgumentException;
	
	/**
	 * Retrieves all of the functions for the given list of functionIds.  The
	 * function can be used when constructing propositions and defines the type
	 * of the parameters to the function as well as it's return type.
	 * 
	 * <p>The list which is returned from this operation may not be the same size as the list
	 * which is passed to this method.  If a function doesn't exist for a given function id then
	 * no result for that id will be returned in the list.  As a result of this, the returned
	 * list can be empty, but it will never be null.
	 *
	 * @param functionIds the list of function ids for which to retrieve the functions
	 * @return the list of functions for the given ids, this list will only contain functions for the ids
	 * that were resolved successfully, it will never return null but could return an empty list if no
	 * functions could be loaded for the given set of ids
	 * 
	 * @throws RiceIllegalArgumentException if the given list of functionIds is null
	 */
	@WebMethod(operationName = "getFunctions")
    @XmlElementWrapper(name = "functions", required = true)
    @XmlElement(name = "function", required = false)
	@WebResult(name = "functions")
	public List<FunctionDefinition> getFunctions(@WebParam(name = "functionIds") List<String> functionIds)
            throws RiceIllegalArgumentException;

}
