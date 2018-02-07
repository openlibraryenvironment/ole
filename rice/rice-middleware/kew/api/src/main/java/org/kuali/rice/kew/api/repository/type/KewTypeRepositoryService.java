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
package org.kuali.rice.kew.api.repository.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.KewApiConstants;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

@WebService(name = "KEWTypeService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface KewTypeRepositoryService {

    /**
     * This will create a {@link KewTypeDefinition} exactly like the parameter passed in.
     *
     * @param kewType - KewType
     * @throws IllegalArgumentException if the kewType is null
     * @throws IllegalStateException if the kewType already exists in the system
     */
    @WebMethod(operationName="createKewType")
    KewTypeDefinition createKewType(@WebParam(name = "kewType") KewTypeDefinition kewType) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update an existing {@link KewTypeDefinition}
     *
     * @param kewType - KewType
     * @throws IllegalArgumentException if the kewType is null
     * @throws IllegalStateException if the KewType does not exist in the system
     */
    @WebMethod(operationName="updateKewType")
    void updateKewType(@WebParam(name = "kewType") KewTypeDefinition kewType) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Lookup a kew type based on the given id.
     *
     * @param id the given kew type id
     * @return a KewType object with the given id.  A null reference is returned if an invalid or
     *         non-existant id is supplied.
     */
    @WebMethod(operationName = "getTypeById")
    @WebResult(name = "type")
    KewTypeDefinition getTypeById(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Get a kew type object based on name and namespace
     *
     * @param name the given type name
     * @param namespace the given type namespace
     * @return A kew type object with the given namespace and name if one with that name and namespace
     *         exists.  Otherwise, null is returned.
     * @throws IllegalStateException if multiple kew types exist with the same name and namespace
     */
    @WebMethod(operationName = "getTypeByNameAndNamespace")
    @WebResult(name = "type")
    KewTypeDefinition getTypeByNameAndNamespace(
            @WebParam(name = "name") String name,
            @WebParam(name = "namespace") String namespace) throws RiceIllegalArgumentException, RiceIllegalStateException;

   /**
     * Returns all KEW types that for a given namespace.
     *
     * @return all KEW types for a namespace
     */
    @WebMethod(operationName = "findAllTypesByNamespace")
    @WebResult(name = "namespaceTypes")
    @XmlElementWrapper(name = "namespaceTypes", required = false)
    @XmlElement(name = "namespaceType", required = false)
    List<KewTypeDefinition> findAllTypesByNamespace(
    		@WebParam(name = "namespace") String namespace) throws RiceIllegalArgumentException;

    /**
     * Returns all KEW types
     *
     * @return all KEW types
     */
    @WebMethod(operationName = "findAllTypes")
    @WebResult(name = "types")
    @XmlElementWrapper(name = "types", required = false)
    @XmlElement(name = "type", required = false)
    List<KewTypeDefinition> findAllTypes();
    
    /**
     * This will create a {@link KewTypeAttribute} exactly like the parameter passed in.
     *
     * @param kewTypeAttribute - KewTypeAttribute
     * @throws IllegalArgumentException if the kewTypeAttribute is null
     * @throws IllegalStateException if the KewTypeAttribute already exists in the system
     */
    @WebMethod(operationName="createKewTypeAttribute")
    void createKewTypeAttribute(@WebParam(name = "kewTypeAttribute") KewTypeAttribute kewTypeAttribute) throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will update an existing {@link KewTypeAttribute}
     *
     * @param kewTypeAttribute - KewTypeAttribute
     * @throws IllegalArgumentException if the kewTypeAttribute is null
     * @throws IllegalStateException if the KewTypeAttribute does not exist in the system
     */
    @WebMethod(operationName="updateKewTypeAttribute")
    void updateKewTypeAttribute(@WebParam(name = "kewTypeAttribute") KewTypeAttribute kewTypeAttribute) throws RiceIllegalArgumentException, RiceIllegalStateException;


}
