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
package org.kuali.rice.kew.api.extension;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;


/**
 * A service which is used for retrieving information about extensions to various
 * pieces of Kuali Enterprise Workflow.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "extensionRepositoryService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ExtensionRepositoryService {
    /**
     * Returns the {@link ExtensionDefinition} of the {@Link RuleAttribute} for the given id.
     * @param id the id to search by.
     * @return the extension definition found for the matching rule attribute service
     * @throws RiceIllegalArgumentException if id is null or blank
     */
    @WebMethod(operationName = "getExtensionById")
    @WebResult(name = "extensionDefinition")
    @Cacheable(value= ExtensionDefinition.Cache.NAME, key="'id=' + #p0")
    ExtensionDefinition getExtensionById(String id) throws RiceIllegalArgumentException;

    /**
     * Returns the {@link ExtensionDefinition} of the {@Link RuleAttribute} for the given name.
     * @param name the name to search by.
     * @return the extension definition found for the matching rule attribute service
     * @throws RiceIllegalArgumentException if name is null or blank
     */
    @WebMethod(operationName = "getExtensionByName")
    @WebResult(name = "extensionDefinition")
    @Cacheable(value= ExtensionDefinition.Cache.NAME, key="'name=' + #p0")
    ExtensionDefinition getExtensionByName(String name) throws RiceIllegalArgumentException;

    /**
     * Returns the {@link ExtensionDefinition} of the {@Link RuleAttribute} for the given resourceDescriptor.
     * @param resourceDescriptor the resourceDescriptor to search by.
     * @return the extension definition found for the matching rule attribute service
     * @throws RiceIllegalArgumentException if resourceDescriptor is null or blank
     */
    @WebMethod(operationName = "getExtensionByResourceDescriptor")
    @WebResult(name = "extensionDefinitions")
    @Cacheable(value=ExtensionDefinition.Cache.NAME, key="'resourceDescriptor=' + #p0")
    List<ExtensionDefinition> getExtensionsByResourceDescriptor(String resourceDescriptor) throws RiceIllegalArgumentException;

}
