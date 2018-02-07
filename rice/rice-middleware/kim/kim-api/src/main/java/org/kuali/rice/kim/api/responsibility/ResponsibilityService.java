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
package org.kuali.rice.kim.api.responsibility;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.common.template.TemplateQueryResults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * This service provides operations for determining what responsibility actions
 * a principal has and for querying about responsibility data.  It also provides several
 * write operations.
 *
 * <p>
 * A responsibility represents an action that a principal is requested to
 * take.  This is used for defining workflow actions (such as approve,
 * acknowledge, fyi) that the principal has the responsibility to take.  The
 * workflow engine integrates with this service to provide responsibility-driven routing.
 * <p/>
 *
 * <p>
 * A responsibility is very similar to a permission in a couple of ways.
 * First of all, responsibilities are always granted to a role, never assigned
 * directly to a principal or group.  Furthermore, in a similar fashion to
 * permissions, a role has the concept of a responsibility template.  The
 * responsibility template specifies what additional responsibility details
 * need to be defined when the responsibility is created.
 * <p/>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "responsibilityService", targetNamespace = KimConstants.Namespaces.KIM_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ResponsibilityService {

    /**
     * This will create a {@link Responsibility} exactly like the responsibility passed in.
     *
     * @param responsibility the responsibility to create
     * @return the id of the newly created object.  will never be null.
     * @throws RiceIllegalArgumentException if the responsibility is null
     * @throws RiceIllegalStateException if the responsibility is already existing in the system
     */
    @WebMethod(operationName="createResponsibility")
    @WebResult(name = "responsibility")
    @CacheEvict(value={Responsibility.Cache.NAME, Template.Cache.NAME + "{Responsibility}"}, allEntries = true)
    Responsibility createResponsibility(@WebParam(name = "responsibility") Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * This will up ev a {@link Responsibility}.
     *
     * @param responsibility the responsibility to update
     * @throws RiceIllegalArgumentException if the responsibility is null
     * @throws RiceIllegalStateException if the responsibility does not exist in the system
     */
    @WebMethod(operationName="updateResponsibility")
    @WebResult(name = "responsibility")
    @CacheEvict(value={Responsibility.Cache.NAME, Template.Cache.NAME + "{Responsibility}"}, allEntries = true)
    Responsibility updateResponsibility(@WebParam(name = "responsibility") Responsibility responsibility)
            throws RiceIllegalArgumentException, RiceIllegalStateException;

    /**
     * Gets a {@link Responsibility} from an id.
     *
     * <p>
     *   This method will return null if the responsibility does not exist.
     * </p>
     *
     * @param id the unique id to retrieve the responsibility by. cannot be null or blank.
     * @return a {@link Responsibility} or null
     * @throws RiceIllegalArgumentException if the id is null or blank
     */
    @WebMethod(operationName = "getResponsibility")
    @WebResult(name = "responsibility")
    @Cacheable(value=Responsibility.Cache.NAME, key="'id=' + #p0")
    Responsibility getResponsibility(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Finds a {@link Responsibility} for namespaceCode and name.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param name the responsibility name. cannot be null or blank.
     * @return a {@link Responsibility} or null
     * @throws RiceIllegalArgumentException if the id or namespaceCode is null or blank
     */
    @WebMethod(operationName = "findRespByNamespaceCodeAndName")
    @WebResult(name = "responsibility")
    @Cacheable(value=Responsibility.Cache.NAME, key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    Responsibility findRespByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
                                                  @WebParam(name = "name") String name) throws RiceIllegalArgumentException;
    /**
     * Gets a {@link Template} from an id.
     *
     * <p>
     *   This method will return null if the template does not exist.
     * </p>
     *
     * @param id the unique id to retrieve the template by. cannot be null or blank.
     * @return a {@link Template} or null
     * @throws RiceIllegalArgumentException if the id is null or blank
     */
    @WebMethod(operationName = "getResponsibilityTemplate")
    @WebResult(name = "template")
    @Cacheable(value=Template.Cache.NAME + "{Responsibility}", key="'id=' + #p0")
    Template getResponsibilityTemplate(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Finds a {@link Template} for namespaceCode and name.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param name the template name. cannot be null or blank.
     * @return a {@link Template} or null
     * @throws RiceIllegalArgumentException if the id or namespaceCode is null or blank
     */
    @WebMethod(operationName = "findRespTemplateByNamespaceCodeAndName")
    @WebResult(name = "template")
    @Cacheable(value=Template.Cache.NAME + "{Responsibility}", key="'namespaceCode=' + #p0 + '|' + 'name=' + #p1")
    Template findRespTemplateByNamespaceCodeAndName(@WebParam(name = "namespaceCode") String namespaceCode,
                                                    @WebParam(name = "name") String name) throws RiceIllegalArgumentException;
    /**
     * Checks in a given principal id has a responsibility using the passed in responsibility information.
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param respName the responsibility name. cannot be null or blank.
     * @param qualification the qualification for the responsibility. cannot be null.
     * @return true is principal has responsibility
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, respName is null or blank
     * @throws RiceIllegalArgumentException if the qualification is null
     */
    @WebMethod(operationName = "hasResponsibility")
    @WebResult(name = "result")
    @Cacheable(value= Responsibility.Cache.NAME,
            key="'{hasResponsibility}' + 'principalId=' + #p0 + '|' + 'namespaceCode=' + #p1 + '|' + 'respName=' + #p2 + '|' + 'qualification=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p3)",
            condition="!T(org.kuali.rice.kim.api.cache.KimCacheUtils).isResponsibilityAssignedToDynamicRole(#p1, #p2)")
    boolean hasResponsibility(@WebParam(name = "principalId") String principalId,
                              @WebParam(name = "namespaceCode") String namespaceCode,
                              @WebParam(name = "respName") String respName,
                              @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                              @WebParam(name = "qualification") Map<String, String> qualification) throws RiceIllegalArgumentException;

    /**
     * Checks in a given principal id has a responsibility using the passed in responsibility <b>template</b> information.
     *
     * @param principalId the principal id to check.  cannot be null or blank.
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param respTemplateName the responsibility template name. cannot be null or blank.
     * @param qualification the qualification for the responsibility. cannot be null.
     * @param respDetails the responsibility details. cannot be null.
     * @return true is principal has responsibility
     * @throws RiceIllegalArgumentException if the principalId, namespaceCode, respName is null or blank
     * @throws RiceIllegalArgumentException if the qualification or responsibilityDetails is null
     */
    @WebMethod(operationName = "hasResponsibilityByTemplate")
    @WebResult(name = "result")
    @Cacheable(value= Responsibility.Cache.NAME,
            key="'{hasResponsibilityByTemplate}' + 'principalId=' + #p0 + '|' + 'namespaceCode=' + #p1 + '|' + 'respTemplateName=' + #p2 + '|' + 'qualification=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p3) + '|' + 'respDetails=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).mapKey(#p4)",
            condition="!T(org.kuali.rice.kim.api.cache.KimCacheUtils).isResponsibilityTemplateAssignedToDynamicRole(#p1, #p2)")
    boolean hasResponsibilityByTemplate(@WebParam(name = "principalId") String principalId,
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "respTemplateName") String respTemplateName,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "qualification") Map<String, String> qualification,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "respDetails") Map<String, String> respDetails) throws RiceIllegalArgumentException;

    /**
     * Gets a List of {@link ResponsibilityAction} based on passed in responsibility information.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param respName the responsibility name. cannot be null or blank.
     * @param qualification the qualification for the responsibility. cannot be null.
     * @return an immutable list of ResponsibilityAction. Will not return null.
     * @throws RiceIllegalArgumentException if the namespaceCode, respName is null or blank
     * @throws RiceIllegalArgumentException if the qualification or respDetails is null
     */
    @WebMethod(operationName = "getResponsibilityActions")
    @XmlElementWrapper(name = "responsibilityActions", required = true)
    @XmlElement(name = "responsibilityAction", required = false)
    @WebResult(name = "responsibilityActions")
    List<ResponsibilityAction> getResponsibilityActions(@WebParam(name = "namespaceCode") String namespaceCode,
                                                        @WebParam(name = "respName") String respName,
                                                        @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                        @WebParam(name = "qualification") Map<String, String> qualification) throws RiceIllegalArgumentException;

    /**
     * Gets a List of {@link ResponsibilityAction} based on passed in responsibility template information.
     *
     * @param namespaceCode the namespace code.  cannot be null or blank.
     * @param respTemplateName the responsibility name. cannot be null or blank.
     * @param qualification the qualification for the responsibility. cannot be null.
     * @param respDetails the responsibility details. can be null.
     * @return an immutable list of ResponsibilityAction. Will not return null.
     * @throws RiceIllegalArgumentException if the namespaceCode, respName is null or blank
     * @throws RiceIllegalArgumentException if the qualification or respDetails is null
     */
    @WebMethod(operationName = "getResponsibilityActionsByTemplate")
    @XmlElementWrapper(name = "responsibilityActions", required = true)
    @XmlElement(name = "responsibilityAction", required = false)
    @WebResult(name = "responsibilityActions")
    List<ResponsibilityAction> getResponsibilityActionsByTemplate(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "responsibilityTemplateName") String respTemplateName,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "qualification") Map<String, String> qualification,
            @XmlJavaTypeAdapter(value = MapStringStringAdapter.class) @WebParam(
                    name = "respDetails") Map<String, String> respDetails) throws RiceIllegalArgumentException;

    /**
     * Gets a List of roleIds that the responsibility is associated with.
     *
     * @param id the unique id to retrieve the roleIds for. cannot be null or blank.
     * @return an immutable list of roleIds. Will not return null.
     * @throws RiceIllegalArgumentException if the id is null or blank or if the qualification is null
     */
    @WebMethod(operationName = "getRoleIdsForResponsibility")
    @XmlElementWrapper(name = "roleIds", required = true)
    @XmlElement(name = "roleId", required = false)
    @WebResult(name = "roleIds")
    @Cacheable(value=Responsibility.Cache.NAME, key="'{getRoleIdsForResponsibility}' + 'id=' + #p0")
    List<String> getRoleIdsForResponsibility(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * This method find Responsibilities based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findResponsibilities")
    @WebResult(name = "results")
    ResponsibilityQueryResults findResponsibilities(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;


    /**
     * This method find Responsibility Templates based on a query criteria.  The criteria cannot be null.
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findResponsibilityTemplates")
    @WebResult(name = "results")
    TemplateQueryResults findResponsibilityTemplates(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * Return the responsibilities for the given unique combination of namespace,
     *  and responsibility template name.
     *
     * @since 2.1.1
     * @param namespaceCode namespace code for permission. cannot be null or blank.
     * @param templateName name of permission template.  cannot be null or blank.
     * @return a list of {@link org.kuali.rice.kim.api.permission.Permission} or null
     * @throws RiceIllegalArgumentException if the namespaceCode or name is null or blank
     */
    @WebMethod(operationName = "findResponsibilitiesByTemplate")
    @XmlElementWrapper(name = "responsibilities", required = true)
    @XmlElement(name = "responsibility", required = false)
    @WebResult(name = "responsibilities")
    @Cacheable(value=Responsibility.Cache.NAME, key="'namespaceCode=' + #p1 + '|' + 'templateName=' + #p2")
    List<Responsibility> findResponsibilitiesByTemplate(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "templateName") String templateName)
            throws RiceIllegalArgumentException;
}
