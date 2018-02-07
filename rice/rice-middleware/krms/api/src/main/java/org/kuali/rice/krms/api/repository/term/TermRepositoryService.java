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
package org.kuali.rice.krms.api.repository.term;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.KrmsConstants;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;

/**
 * The TermRepositoryService provides the basic access to terms and term resolvers in the repository needed
 * for executing rules.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(name = "termRepositoryService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TermRepositoryService {


    /**
     * Retrieves all {@link TermResolverDefinition}s for the given namespace.
     *
     * @since 2.1.1
     * @param namespace the namespace for which to get all term resolvers.
     * @return the List of {@link TermResolverDefinition}s for the given namespace. May be empty, but never null.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the namespace is null or blank.
     */
    @WebMethod(operationName = "findTermResolversByNamespace")
    @XmlElementWrapper(name = "termResolvers", required = true)
    @XmlElement(name = "termResolver", required = false)
    @WebResult(name = "termResolvers")
    @Cacheable(value = TermResolverDefinition.Cache.NAME, key = "'namespace=' + #p0")
    List<TermResolverDefinition> findTermResolversByNamespace(@WebParam(name = "namespace") String namespace) throws RiceIllegalArgumentException;

     /**
     * Retrieves the {@link TermDefinition} with the given termId.
     *
     * @since 2.1.1
     * @param termId the identifier of the term to retrieve.
     * @return the {@link TermDefinition} with the given termId.  May be null if there is no term with the given termId
     * in the repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the termId is null or blank.
     */
    @WebMethod(operationName = "getTerm")
    @WebResult(name = "term")
    @Cacheable(value= TermDefinition.Cache.NAME, key="'id=' + #p0")
    TermDefinition getTerm(@WebParam(name = "termId") String termId) throws RiceIllegalArgumentException;;

    /**
     * Retrieves the {@link TermSpecificationDefinition} with the given TermSpecificationId.
     *
     * @since 2.2.1
     * @param id the identifier of the term specification to retrieve.
     * @return the {@link TermSpecificationDefinition} with the given id.  May be null if there is no term specification 
     * with the given id in the repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the id is null or blank.
     */
    @WebMethod(operationName = "getTermSpecificationById")
    @WebResult(name = "termSpecification")
    @Cacheable(value = TermSpecificationDefinition.Cache.NAME, key = "'id=' + #p0")
    TermSpecificationDefinition getTermSpecificationById(@WebParam(name = "id") String id)
            throws RiceIllegalArgumentException;

    /**
     * Creates a {@link TermSpecificationDefinition}
     *
     * @since 2.2.1
     * @param termSpec the term specification to be created
     * @return the {@link TermSpecificationDefinition} after it has been created
     * in the repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException
     * termSpec is null or invalid
     */
    @WebMethod(operationName = "createTermSpecification")
    @WebResult(name = "termSpecification")
    @CacheEvict(value = {TermSpecificationDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    TermSpecificationDefinition createTermSpecification(@WebParam(name = "termSpec") TermSpecificationDefinition termSpec)
            throws RiceIllegalArgumentException;

     /**
     * Updates a {@link TermSpecificationDefinition}
     *
     * @since 2.2.1
     * @param termSpec the term specification to be updated
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException
     * termSpec is null or invalid
     */
    @WebMethod(operationName = "updateTermSpecification")
    @CacheEvict(value = {TermSpecificationDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    void updateTermSpecification(@WebParam(name = "termSpec") TermSpecificationDefinition termSpec)
            throws RiceIllegalArgumentException;

     /**
     * Deletes a {@link TermSpecificationDefinition}
     *
     * @since 2.2.1
     * @param id the id of the term specification to be deleted
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException
     * id is null or invalid
     */
    @WebMethod(operationName = "deleteTermSpecification")
    @CacheEvict(value = {TermSpecificationDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    void deleteTermSpecification(@WebParam(name = "id") String id)
            throws RiceIllegalArgumentException;  
    
    
    /**
     * Create a {@link TermDefinition}
     *
     * @since 2.2.1
     * @param termDef to be created
     * @return the {@link TermDefinition} term definition after it has been
     * created in the repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the termDef is null or blank.
     */
    @WebMethod(operationName = "createTerm")
    @WebResult(name = "term")
    @CacheEvict(value = {TermDefinition.Cache.NAME}, allEntries = true)
    TermDefinition createTerm(@WebParam(name = "termDef") TermDefinition termDef)
            throws RiceIllegalArgumentException;

    /**
     * Update a {@link TermDefinition}
     *
     * @since 2.2.1
     * @param termDef to be updated
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the termDef is null or blank.
     */
    @WebMethod(operationName = "updateTerm")
    @WebResult(name = "term")
    @CacheEvict(value = {TermDefinition.Cache.NAME}, allEntries = true)
    void updateTerm(@WebParam(name = "termDef") TermDefinition termDef)
            throws RiceIllegalArgumentException;


    /**
     * Delete a {@link TermDefinition}
     *
     * @since 2.2.1
     * @param id of the termDefinition to be deleted
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the id is null or blank.
     */
    @WebMethod(operationName = "deleteTerm")
    @WebResult(name = "term")
    @CacheEvict(value = {TermDefinition.Cache.NAME}, allEntries = true)
    void deleteTerm(@WebParam(name = "id") String id)
            throws RiceIllegalArgumentException;


    /**
     * Retrieves the {@link TermResolverDefinition} with the given id.
     *
     * @since 2.2.1
     * @param id the identifier of the term to retrieve.
     * @return the {@link TermResolverDefinition} with the given id. May be null
     * if there is no term with the given id in the repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the id is null or blank.
     */
    @WebMethod(operationName = "getTermResolverById")
    @WebResult(name = "termResolver")
    @Cacheable(value = TermResolverDefinition.Cache.NAME, key = "'id=' + #p0")
    TermResolverDefinition getTermResolverById(@WebParam(name = "id") String id)
            throws RiceIllegalArgumentException;

    /**
     * Get the {@link TermResolverDefinition}s for any term resolvers in the
     * specified namespace that have the given term specification as their
     * output.
     *
     * @param id the id for the term specification
     * @param namespace the namespace to search
     * @return the List of term resolvers found. If none are found, an empty
     * list will be returned.
     */
    @WebMethod(operationName = "findTermResolversByOutputId")
    @XmlElementWrapper(name = "termResolvers", required = true)
    @XmlElement(name = "termResolver", required = false)
    @WebResult(name = "termResolvers")
    @Cacheable(value = TermResolverDefinition.Cache.NAME, key = "'id=' + #p0 + '|' + 'namespace=' + #p1")
    List<TermResolverDefinition> findTermResolversByOutputId(@WebParam(name = "id") String id,
            @WebParam(name = "namespace") String namespace)
            throws RiceIllegalArgumentException;

    /**
     * Creates the {@link TermResolverDefinition}.
     *
     * @since 2.1.1
     * @param termResolver to be created
     * @return the {@link TermResolver} after it has been created in the
     * repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the termResolver is null or blank.
     */
    @WebMethod(operationName = "createTermResolver")
    @WebResult(name = "termResolver")
    @CacheEvict(value = {TermResolverDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    TermResolverDefinition createTermResolver(@WebParam(name = "termResolver") TermResolverDefinition termResolver)
            throws RiceIllegalArgumentException;
    
    
    /**
     * Updates the {@link TermResolverDefinition}.
     *
     * @since 2.1.1
     * @param termResolver to be created
     * @return the {@link TermResolverDefinition} after it has been created in the
     * repository.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the termResolver is null or blank.
     */
    @WebMethod(operationName = "updateTermResolver")
    @CacheEvict(value = {TermResolverDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    void updateTermResolver(@WebParam(name = "termResolver") TermResolverDefinition termResolver)
            throws RiceIllegalArgumentException;
    
    
    /**
     * deletes the {@link TermResolverDefinition} with the given id
     *
     * @since 2.1.1
     * @param id of the term resolver to be deleted
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if
     * the termResolver is null or blank.
     */
    @WebMethod(operationName = "deleteTermResolver")
    @CacheEvict(value = {TermResolverDefinition.Cache.NAME, TermDefinition.Cache.NAME}, allEntries = true)
    void deleteTermResolver(@WebParam(name = "id") String id)
            throws RiceIllegalArgumentException;
    
    
    /**
     * Retrieves the {@link TermResolverDefinition} for the given name and namespace
     *
     * @param name the name for which to get term resolver
     * @param namespace the namespace for which to get term resolver
     * @return the {@link TermResolverDefinition} for the given name and namespace, null if none exist.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the name or namespace is null or blank.
     */
    @WebMethod(operationName = "getTermResolverByNameAndNamespace")
    @WebResult(name = "termResolver")
            // TODO: set the cache right
//    @Cacheable(value = TermResolverDefinition.Cache.NAME, key = "'namespace=' + #p0")
    TermResolverDefinition getTermResolverByNameAndNamespace(@WebParam(name = "name") String name, 
            @WebParam(name = "namespace") String namespace) throws RiceIllegalArgumentException;

 
    /**
     * Retrieves the {@link TermSpecificationDefinition} for the given name and namespace
     *
     * @param name the name for which to get term specification
     * @param namespace the namespace for which to get term resolver
     * @return the {@link TermSpecificationDefinition} for the given name and namespace, null if none exist.
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the name or namespace is null or blank.
     */
    @WebMethod(operationName = "getTermSpecificationByNameAndNamespace")
    @WebResult(name = "termSpecification")
            // TODO: set the cache right
//    @Cacheable(value = TermSpecificationDefinition.Cache.NAME, key = "'namespace=' + #p0")
    TermSpecificationDefinition getTermSpecificationByNameAndNamespace(@WebParam(name = "name") String name, 
            @WebParam(name = "namespace") String namespace) throws RiceIllegalArgumentException;

    
    /**
     * Retrieves all the {@link TermSpecificationDefinition}s that are valid for the context with the given contextId.
     *
     * @since 2.1.4
     * @param contextId the identifier for the context whose valid {@link TermSpecificationDefinition}s are to be retrieved. 
     * @return all the {@link TermSpecificationDefinition}s that are valid for the context with the given contextId. May be empty but never null
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if the contextId is null or blank.
     */
    @WebMethod(operationName = "findAllTermSpecificationsByContextId")
    @XmlElementWrapper(name = "termSpecifications", required = true)
    @XmlElement(name = "termSpecification", required = false)
    @WebResult(name = "termSpecifications")
    @Cacheable(value= TermSpecificationDefinition.Cache.NAME, key="'id=' + #p0")
    List<TermSpecificationDefinition> findAllTermSpecificationsByContextId(@WebParam(name = "contextId") String contextId) throws RiceIllegalArgumentException;;

}
