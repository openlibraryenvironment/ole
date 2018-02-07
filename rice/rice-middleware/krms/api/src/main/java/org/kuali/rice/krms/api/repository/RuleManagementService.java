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
package org.kuali.rice.krms.api.repository;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.reference.ReferenceObjectBinding;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.Set;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;

/**
 * The rule maintenance service operations facilitate management of rules and
 * associated information.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "ruleManagementService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RuleManagementService extends TranslateBusinessMethods {

    /**
     * Create RefObject-KRMS object binding
     *
     * @param referenceObjectDefinition  data for the new ReferenceObjectBinding to be created
     * @return newly created ReferenceObjectBinding
     * @throws RiceIllegalArgumentException if the given referenceObjectDefinition
     *                                      is null or invalid
     */
    @WebMethod(operationName = "createReferenceObjectBinding")
    @WebResult(name = "referenceObjectBinding")
    @CacheEvict(value={ReferenceObjectBinding.Cache.NAME}, allEntries = true)
    public ReferenceObjectBinding createReferenceObjectBinding(@WebParam(
            name = "referenceObjectDefinition") ReferenceObjectBinding referenceObjectDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieve referenceObjectBinding  given a specific id
     *
     * @param id identifier of the ReferenceObjectBinding to be retrieved
     * @return a ReferenceObjectBinding with the given id value
     * @throws RiceIllegalArgumentException if the given  id is blank or
     *                                      invalid
     */
    @WebMethod(operationName = "getReferenceObjectBinding")
    @WebResult(name = "referenceObjectBinding")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'id=' + #p0")
    public ReferenceObjectBinding getReferenceObjectBinding(@WebParam(
            name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieve list of ReferenceObjectBinding objects given ids
     *
     * @param ids identifiers of the ReferenceObjectBinding to be retrieved
     * @return list of ReferenceObjectBinding objects for the given ids
     * @throws RiceIllegalArgumentException if one or more ids in the give list
     *                                      is blank or invalid
     */
    @WebMethod(operationName = "getReferenceObjectBindings")
    @XmlElementWrapper(name = "referenceObjectBindings", required = true)
    @XmlElement(name = "referenceObjectBinding", required = false)
    @WebResult(name = "referenceObjectBindings")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'ids=' + #p0")
    List<ReferenceObjectBinding> getReferenceObjectBindings(@WebParam(
            name = "ids") List<String> ids) throws RiceIllegalArgumentException;

    /**
     * Retrieves list of ReferenceObjectBinding objects for the given ref obj
     * discriminator type
     *
     * @param referenceObjectReferenceDiscriminatorType  reference object type
     * @return list of ReferenceObjectBinding objects for the given discriminator
     *         type
     * @throws RiceIllegalArgumentException if the given  referenceObjectReferenceDiscriminatorType is
     *                                      blank or invalid
     */
    @WebMethod(operationName = "findReferenceObjectBindingsByReferenceDiscriminatorType")
    @XmlElementWrapper(name = "referenceObjectBindings", required = true)
    @XmlElement(name = "referenceObjectBinding", required = false)
    @WebResult(name = "referenceObjectBindings")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'referenceObjectReferenceDiscriminatorType=' + #p0")
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceDiscriminatorType(
            @WebParam(name = "referenceObjectReferenceDiscriminatorType") String referenceObjectReferenceDiscriminatorType) throws RiceIllegalArgumentException;

    /**
     * Retrieves list of ReferenceObjectBinding objects for the given krms obj
     * discriminator type
     *
     * @param referenceObjectKrmsDiscriminatorType  reference object type
     * @return list of ReferenceObjectBinding objects for the given discriminator
     *         type
     * @throws RiceIllegalArgumentException if the given  referenceObjectKrmsDiscriminatorType is
     *                                      blank or invalid
     */
    @WebMethod(operationName = "findReferenceObjectBindingsByKrmsDiscriminatorType")
    @XmlElementWrapper(name = "referenceObjectBindings", required = true)
    @XmlElement(name = "referenceObjectBinding", required = false)
    @WebResult(name = "referenceObjectBindings")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'referenceObjectKrmsDiscriminatorType=' + #p0")
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsDiscriminatorType(
            @WebParam(name = "referenceObjectKrmsDiscriminatorType") String referenceObjectKrmsDiscriminatorType) throws RiceIllegalArgumentException;

    
    /**
     * Retrieves list of ReferenceObjectBinding objects for the given obj
     * discriminator type and reference object id
     *
     * @param referenceObjectReferenceDiscriminatorType  reference object type
     * @param referenceObjectId reference object id
     * @return list of ReferenceObjectBinding objects for the given discriminator
     *         type
     * @throws RiceIllegalArgumentException if the given  referenceObjectKrmsDiscriminatorType or id is
     *                                      blank or invalid
     */
    @WebMethod(operationName = "findReferenceObjectBindingsByReferenceObject")
    @XmlElementWrapper(name = "referenceObjectBindings", required = true)
    @XmlElement(name = "referenceObjectBinding", required = false)
    @WebResult(name = "referenceObjectBindings")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'referenceObjectReferenceDiscriminatorType=' + #p0 + '|' + 'referenceObjectId=' + #p1")
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByReferenceObject (
            @WebParam(name = "referenceObjectReferenceDiscriminatorType") String referenceObjectReferenceDiscriminatorType, 
            @WebParam(name = "referenceObjectId") String referenceObjectId) 
            throws RiceIllegalArgumentException;
    
    /**
     * Retrieves list of ReferenceObjectBinding objects for the given KRMS obj
     * id.
     *
     * @param krmsObjectId identifier of the KRMS obj
     * @return list of ReferenceObjectBinding objects for the given KRMS obj
     * @throws RiceIllegalArgumentException if the given krmsObjectId is blank or
     *                                      invalid
     */
    @WebMethod(operationName = "findReferenceObjectBindingsByKrmsObjectId")
    @XmlElementWrapper(name = "referenceObjectBindings", required = true)
    @XmlElement(name = "referenceObjectBinding", required = false)
    @WebResult(name = "referenceObjectBindings")
    @Cacheable(value= ReferenceObjectBinding.Cache.NAME, key="'krmsObjectId=' + #p0")
    public List<ReferenceObjectBinding> findReferenceObjectBindingsByKrmsObject(
            @WebParam(name = "krmsObjectId") String krmsObjectId) throws RiceIllegalArgumentException;

    /**
     * Update the ReferenceObjectBinding object specified by the identifier in the
     * given DTO
     *
     * @param referenceObjectBindingDefinition DTO with updated info and id of the object to be updated
     * @throws RiceIllegalArgumentException if the given  referenceObjectBindingDefinition
     *                                      is null or invalid
     */
    @WebMethod(operationName = "updateReferenceObjectBinding")
    @CacheEvict(value={ReferenceObjectBinding.Cache.NAME}, allEntries = true)
    public void updateReferenceObjectBinding(ReferenceObjectBinding referenceObjectBindingDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified ReferenceObjectBinding object
     *
     * @param id identifier of the object to be deleted
     * @throws RiceIllegalArgumentException if the given  id is null or invalid
     */
    @WebMethod(operationName = "deleteReferenceObjectBinding")
    @CacheEvict(value={ReferenceObjectBinding.Cache.NAME}, allEntries = true)
    public void deleteReferenceObjectBinding(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;


    /**
     * Query for ReferenceObjectBinding ids based on the given search criteria
     * which is a Map of ReferenceObjectBinding field names to values. <p/> <p>
     * This method returns it's results as a List of ReferenceObjectBinding ids
     * that match the given search criteria. </p>
     *
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return a list of ids matching the given criteria properties.  An empty
     *         list is returned if an invalid or non-existent criteria is
     *         supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findReferenceObjectBindingIds")
    @XmlElementWrapper(name = "referenceObjectBindingIds", required = true)
    @XmlElement(name = "referenceObjectBindingId", required = false)
    @WebResult(name = "referenceObjectBindingIds")
    List<String> findReferenceObjectBindingIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    ////
    //// agenda methods
    ////
    /**
     * Create Agenda and an empty first item
     *
     * @param agendaDefinition data for the new Agenda to be created
     * @return newly created Agenda
     * @throws RiceIllegalArgumentException if the given agendaDefinition is
     *                                      null or invalid
     */
    @WebMethod(operationName = "createAgenda")
    @WebResult(name = "agenda")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public AgendaDefinition createAgenda(@WebParam(name = "AgendaDefinition") AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException;

    /**
     * Create Agenda if not found by contextId and name
     *
     * @param agendaDefinition data for the new Agenda to be created
     * @return newly created or found Agenda
     * @throws RiceIllegalArgumentException if the given agendaDefinition is
     *                                      null or invalid
     */
    @WebMethod(operationName = "findCreateAgenda")
    @WebResult(name = "agenda")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public AgendaDefinition findCreateAgenda(@WebParam(name = "AgendaDefinition") AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieve Agenda for the specified id
     *
     * @param id identifier for the Agenda
     * @return specified Agenda
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "getAgenda")
    @WebResult(name = "agenda")
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'id=' + #p0")
    public AgendaDefinition getAgenda(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieves an Agenda from the repository based on the provided agenda name
     * and context id.
     *
     * @param name the name of the Agenda to retrieve.
     * @param contextId the id of the context that the agenda belongs to.
     * @return an {@link AgendaDefinition} identified by the given name and namespace.  
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     */
    @WebMethod(operationName = "getAgendaByNameAndContextId")
    @WebResult(name = "agenda")
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'contextId=' + #p1")
    public AgendaDefinition getAgendaByNameAndContextId (@WebParam(name = "name") String name,
                                                         @WebParam(name = "contextId") String contextId);
    
    /**
     * Retrieve Agendas of the specified type
     *
     * @param typeId type of the Agenda
     * @return list of Agendas of the specified type
     * @throws RiceIllegalArgumentException if the given typeId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getAgendasByType")
    @XmlElementWrapper(name = "agendas", required = true)
    @XmlElement(name = "agenda", required = false)
    @WebResult(name = "agendas")
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'typeId=' + #p0")
    public List<AgendaDefinition> getAgendasByType(@WebParam(name = "typeId") String typeId) throws RiceIllegalArgumentException;

    /**
     * Retrieve Agendas associated with the specified context
     *
     * @param contextId  context of interest
     * @return list of Agendas associated with the context
     * @throws RiceIllegalArgumentException if the given contextId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getAgendasByContext")
    @XmlElementWrapper(name = "agendas", required = true)
    @XmlElement(name = "agenda", required = false)
    @WebResult(name = "agendas")
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'contextId=' + #p0")
    public List<AgendaDefinition> getAgendasByContext(@WebParam(name = "contextId") String contextId) throws RiceIllegalArgumentException;

    /**
     * Retrieve Agendas of the specified type and context
     *
     * @param typeId  type of the Agenda
     * @param contextId  context of interest
     * @return list of Agendas associated with the specified type and context
     * @throws RiceIllegalArgumentException if the given typeId or contextId
     *                                      null or invalid
     */
    @WebMethod(operationName = "getAgendasByTypeAndContext")
    @XmlElementWrapper(name = "agendas", required = true)
    @XmlElement(name = "agenda", required = false)
    @WebResult(name = "agendas")
    @Cacheable(value= AgendaDefinition.Cache.NAME, key="'typeId=' + #p0 + '|' + 'contextId=' + #p1")
    public List<AgendaDefinition> getAgendasByTypeAndContext(@WebParam(name = "typeId") String typeId,
            @WebParam(name = "contextId") String contextId) throws RiceIllegalArgumentException;

    /**
     * Update the Agenda specified by the identifier in the input DTO
     *
     * @param agendaDefinition DTO with updated info and identifier of the object to be updated
     * @throws RiceIllegalArgumentException if the given agendaDefinition is
     *                                      null or invalid
     */
    @WebMethod(operationName = "updateAgenda")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void updateAgenda(@WebParam(name = "agendaDefinition") AgendaDefinition agendaDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified Agenda
     *
     * @param id identifier of the object to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteAgenda")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void deleteAgenda(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    ////
    //// agenda item methods
    ////
    /**
     * Create AgendaItem
     *
     * @param agendaItemDefinition  data for the new AgendaItem to be created
     * @return newly created AgendaItem
     * @throws RiceIllegalArgumentException if the given agendaItemDefinition is
     *                                      null or invalid
     */
    @WebMethod(operationName = "createAgendaItem")
    @WebResult(name = "agendaItem")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public AgendaItemDefinition createAgendaItem(@WebParam(name = "AgendaItemDefinition") AgendaItemDefinition agendaItemDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieve AgendaItem by the specified identifier
     *
     * @param id identifier of the AgendaItem
     * @return AgendaItem specified by the identifier
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "getAgendaItem")
    @WebResult(name = "agendaItem")
    @Cacheable(value= AgendaItemDefinition.Cache.NAME, key="'id=' + #p0")
    public AgendaItemDefinition getAgendaItem(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieve AgendaItems by specified type
     *
     * @param typeId type of the AgendaItems
     * @return list of AgendaItems of the specified type
     * @throws RiceIllegalArgumentException if the given typeId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getAgendaItemsByType")
    @XmlElementWrapper(name = "agendaItems", required = true)
    @XmlElement(name = "agendaItem", required = false)
    @WebResult(name = "agendaItems")
    @Cacheable(value= AgendaItemDefinition.Cache.NAME, key="'typeId=' + #p0")
    public List<AgendaItemDefinition> getAgendaItemsByType(@WebParam(name = "typeId") String typeId) throws RiceIllegalArgumentException;

    /**
     * Retrieve AgendaItems associated with a context
     *
     * @param contextId context identifier
     * @return list of AgendaItems associated with a context
     * @throws RiceIllegalArgumentException if the given  contextId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getAgendaItemsByContext")
    @XmlElementWrapper(name = "agendaItems", required = true)
    @XmlElement(name = "agendaItem", required = false)
    @WebResult(name = "agendaItems")
    @Cacheable(value= AgendaItemDefinition.Cache.NAME, key="'contextId=' + #p0")
    public List<AgendaItemDefinition> getAgendaItemsByContext(@WebParam(name = "contextId") String contextId) throws RiceIllegalArgumentException;

    /**
     * Retrieve AgendaItems by type and context
     *
     * @param typeId type of the Agendas
     * @param contextId context with which the Agendas are associated
     * @return list of AgendaItems of the specified type and context
     * @throws RiceIllegalArgumentException if the given  typeId or contextId
     *                                      null or invalid
     */
    @WebMethod(operationName = "getAgendaItemsByTypeAndContext")
    @XmlElementWrapper(name = "agendaItems", required = true)
    @XmlElement(name = "agendaItem", required = false)
    @WebResult(name = "agendaItems")
    @Cacheable(value= AgendaItemDefinition.Cache.NAME, key="'typeId=' + #p0 + '|' + 'contextId=' + #p1")
    public List<AgendaItemDefinition> getAgendaItemsByTypeAndContext(@WebParam(name = "typeId") String typeId,
            @WebParam(name = "contextId") String contextId) throws RiceIllegalArgumentException;

    /**
     * Update an AgendaItem
     *
     * @param agendaItemDefinition  updated data for the AgendaItem, with id of the object to be updated
     * @throws RiceIllegalArgumentException if the given  agendaItemDefinition
     *                                      is null or invalid
     */
    @WebMethod(operationName = "updateAgendaItem")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void updateAgendaItem(@WebParam(name = "agendaItemDefinition") AgendaItemDefinition agendaItemDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified AgendaItem
     *
     * @param id identifier of the AgendaItem to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteAgendaItem")
    @CacheEvict(value={AgendaTreeDefinition.Cache.NAME, AgendaDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME, ContextDefinition.Cache.NAME}, allEntries = true)
    public void deleteAgendaItem(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    ////
    //// rule methods
    ////
    /**
     * Create Rule
     *
     * @param ruleDefinition data for the new Rule to be created
     * @return newly created Rule
     * @throws RiceIllegalArgumentException if the given ruleDefinition is null
     *                                      or invalid
     */
    @WebMethod(operationName = "createRule")
    @WebResult(name = "rule")
    @CacheEvict(value={RuleDefinition.Cache.NAME, PropositionDefinition.Cache.NAME, ActionDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME}, allEntries = true)
    public RuleDefinition createRule(@WebParam(name = "ruleDefinition") RuleDefinition ruleDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieves the rule for the given ruleId.  The rule includes the
     * propositions which define the condition that is to be evaluated on the
     * rule.  It also defines a collection of actions which will be invoked if
     * the rule succeeds.
     *
     * @param ruleId the id of the rule to retrieve
     * @return the rule definition, or null if no rule could be located for the
     *         given ruleId
     * @throws IllegalArgumentException if the given ruleId is null
     */
    @WebMethod(operationName = "getRule")
    @WebResult(name = "rule")
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'ruleId=' + #p0")
    public RuleDefinition getRule(@WebParam(name = "ruleId") String ruleId);

    /**
     * Retrieves an Rule from the repository based on the provided rule name
     * and namespace.
     *
     * @param name the name of the Rule to retrieve.
     * @param namespace the namespace that the rule is under.
     * @return an {@link RuleDefinition} identified by the given name and namespace.
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     * @throws IllegalArgumentException if the either the name or the namespace
     * is null or blank.
     */
    @WebMethod(operationName = "getRuleByNameAndNamespace")
    @WebResult(name = "rule")
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
    public RuleDefinition getRuleByNameAndNamespace(@WebParam(name = "name") String name,
                                                    @WebParam(name = "namespace") String namespace);
	
    /**
     * Retrieves all of the rules for the given list of ruleIds.  The rule
     * includes the propositions which define the condition that is to be
     * evaluated on the rule.  It also defines a collection of actions which
     * will be invoked if the rule succeeds.
     * <p/>
     * <p>The list which is returned from this operation may not be the same
     * size as the list which is passed to this method.  If a rule doesn't exist
     * for a given rule id then no result for that id will be returned in the
     * list.  As a result of this, the returned list can be empty, but it will
     * never be null.
     *
     * @param ruleIds the list of rule ids for which to retrieve the rules
     * @return the list of rules for the given ids, this list will only contain
     *         rules for the ids that were resolved successfully, it will never
     *         return null but could return an empty list if no rules could be
     *         loaded for the given set of ids
     * @throws IllegalArgumentException if the given list of ruleIds is null
     */
    @WebMethod(operationName = "getRules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = false)
    @WebResult(name = "rules")
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'ruleIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
    public List<RuleDefinition> getRules(@WebParam(name = "ruleIds") List<String> ruleIds);

    /**
     * Update the Rule specified by the identifier in the DTO
     *
     * @param ruleDefinition updated Rule information, object specified by the id
     * @throws RiceIllegalArgumentException if the given ruleDefinition is null
     *                                      or invalid
     */
    @WebMethod(operationName = "updateRule")
    @CacheEvict(value={RuleDefinition.Cache.NAME, PropositionDefinition.Cache.NAME, ActionDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME}, allEntries = true)
    public void updateRule(@WebParam(name = "ruleDefinition") RuleDefinition ruleDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified Rule
     *
     * @param id identifier of the Rule to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteRule")
    @CacheEvict(value={RuleDefinition.Cache.NAME, PropositionDefinition.Cache.NAME, ActionDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME}, allEntries = true)
    public void deleteRule(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    ////
    //// action methods
    ////
    /**
     * Create Action
     *
     * @param actionDefinition data for the new Action to be created
     * @return newly created Action
     * @throws RiceIllegalArgumentException if the given actionDefinition is null
     * or invalid
     */
    @WebMethod(operationName = "createAction")
    @WebResult(name = "action")
    @CacheEvict(value={ActionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public ActionDefinition createAction(@WebParam(name = "actionDefinition") ActionDefinition actionDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieves the action for the given actionId. The action includes the
     * propositions which define the condition that is to be evaluated on the
     * action. It also defines a collection of actions which will be invoked if
     * the action succeeds.
     *
     * @param actionId the id of the action to retrieve
     * @return the action definition, or null if no action could be located for the
     * given actionId
     * @throws IllegalArgumentException if the given actionId is null
     */
    @WebMethod(operationName = "getAction")
    @WebResult(name = "action")
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'actionId=' + #p0")
    public ActionDefinition getAction(@WebParam(name = "actionId") String actionId) throws RiceIllegalArgumentException;

    /**
     * Retrieves all of the actions for the given list of actionIds.
     * <p/>
     * <p>The list which is returned from this operation may not be the same
     * size as the list which is passed to this method. If a action doesn't exist
     * for a given action id then no result for that id will be returned in the
     * list. As a result of this, the returned list can be empty, but it will
     * never be null.
     *
     * @param actionIds the list of action ids for which to retrieve the actions
     * @return the list of actions for the given ids, this list will only contain
     * actions for the ids that were resolved successfully, it will never return
     * null but could return an empty list if no actions could be loaded for the
     * given set of ids
     * @throws IllegalArgumentException if the given list of actionIds is null
     */
    @WebMethod(operationName = "getActions")
    @XmlElementWrapper(name = "actions", required = true)
    @XmlElement(name = "action", required = false)
    @WebResult(name = "actions")
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'actionIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
    public List<ActionDefinition> getActions(@WebParam(name = "actionIds") List<String> actionIds)  throws RiceIllegalArgumentException;

    /**
     * Update the Action specified by the identifier in the DTO
     *
     * @param actionDefinition updated Action information, object specified by the
     * id
     * @throws RiceIllegalArgumentException if the given actionDefinition is null
     * or invalid
     */
    @WebMethod(operationName = "updateAction")
    @CacheEvict(value={ActionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public void updateAction(@WebParam(name = "actionDefinition") ActionDefinition actionDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified Action
     *
     * @param id identifier of the Action to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteAction")
    @CacheEvict(value={ActionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public void deleteAction(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;
  
    
    ////
    //// proposition methods
    ////
    /**
     * Create a Proposition
     *
     * @param propositionDefinition data for the new Proposition to be created
     * @return newly created Proposition
     * @throws RiceIllegalArgumentException if the given propositionDefinition
     *                                      is null or invalid
     */
    @WebMethod(operationName = "createProposition")
    @WebResult(name = "proposition")
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public PropositionDefinition createProposition(@WebParam(name = "propositionDefinition") PropositionDefinition propositionDefinition) throws RiceIllegalArgumentException;

    /**
     * Retrieve Proposition specified by the identifier
     *
     * @param id identifier of the Proposition to be retrieved
     * @return specified Proposition
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "getProposition")
    @WebResult(name = "proposition")
    @Cacheable(value= PropositionDefinition.Cache.NAME, key="'id=' + #p0")
    public PropositionDefinition getProposition(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieve Propositions of the specified type
     *
     * @param typeId type of the Propositions to be retrieved
     * @return list of Propositions of the specified type
     * @throws RiceIllegalArgumentException if the given typeId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getPropositionsByType")
    @XmlElementWrapper(name = "propositions", required = true)
    @XmlElement(name = "proposition", required = false)
    @WebResult(name = "propositions")
    @Cacheable(value= PropositionDefinition.Cache.NAME, key="'typeId=' + #p0")
    public Set<PropositionDefinition> getPropositionsByType(@WebParam(name = "typeId") String typeId) throws RiceIllegalArgumentException;

    /**
     * Retrieve Propositions associated with the specified Rule
     *
     * @param ruleId identifier of the Rule to which the Propositions are associated with
     * @return list of Propositions associated with the Rule
     * @throws RiceIllegalArgumentException if the given ruleId is null or
     *                                      invalid
     */
    @WebMethod(operationName = "getPropositionsByRule")
    @XmlElementWrapper(name = "propositions", required = true)
    @XmlElement(name = "proposition", required = false)
    @WebResult(name = "propositions")
    @Cacheable(value= PropositionDefinition.Cache.NAME, key="'ruleId=' + #p0")
    public Set<PropositionDefinition> getPropositionsByRule(@WebParam(name = "ruleId") String ruleId) throws RiceIllegalArgumentException;

    /**
     * Update the Proposition
     *
     * @param propositionDefinition updated data for the Proposition, id specifies the object to be updated
     * @throws RiceIllegalArgumentException if the given propositionDefinition
     *                                      is null or invalid
     */
    @WebMethod(operationName = "updateProposition")
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public void updateProposition(
            @WebParam(name = "propositionDefinition") PropositionDefinition propositionDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the Proposition
     *
     * @param id identifier of the Proposition to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteProposition")
    @CacheEvict(value={PropositionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
    public void deleteProposition(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    ////
    //// natural language usages
    ////
    /**
     * Create NaturalLanguageUsage
     *
     * @param naturalLanguageUsage data for the new NaturalLanguageUsage to be created
     * @return newly created NaturalLanguageUsage
     * @throws RiceIllegalArgumentException if the given naturalLanguageUsage is
     *                                      null or invalid
     */
    @WebMethod(operationName = "createNaturalLanguageUsage")
    @WebResult(name = "naturalLanguageUsage")
    @CacheEvict(value={NaturalLanguageUsage.Cache.NAME, NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public NaturalLanguageUsage createNaturalLanguageUsage(@WebParam(name = "naturalLanguageUsage") NaturalLanguageUsage naturalLanguageUsage) throws RiceIllegalArgumentException;

    /**
     * Retrieve NaturalLanguageUsage specified by the identifier
     *
     * @param id identifier of the NaturalLanguageUsage to be retrieved
     * @return NaturalLanguageUsage specified by the identifier
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "getNaturalLanguageUsage")
    @WebResult(name = "naturalLanguageUsage")
    @Cacheable(value= NaturalLanguageUsage.Cache.NAME, key="'id=' + #p0")
    public NaturalLanguageUsage getNaturalLanguageUsage(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieve NaturalLanguageUsage specified by name and namespace
     *
     * @param name the name of the natural language usage to retrieve.
     * @param namespace the namespace that the natural language usage is under.
     * @return an {@link NaturalLanguageUsage} identified by the given name and
     * namespace. A null reference is returned if an invalid or non-existent
     * name and namespace combination is supplied.
     * @throws RiceIllegalArgumentException if the either the name or the
     * namespace is null or blank.
     */
    @WebMethod(operationName = "getNaturalLanguageUsageByNameAndNamespace")
    @WebResult(name = "naturalLanguageUsage")
    @Cacheable(value= NaturalLanguageUsage.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
    public NaturalLanguageUsage getNaturalLanguageUsageByNameAndNamespace(@WebParam(name = "name") String name,
            @WebParam(name = "namespace") String namespace)
            throws RiceIllegalArgumentException;

    /**
     * Update NaturalLanguageUsage
     *
     * @param naturalLanguageUsage updated data for the NaturalLanguageUsage object specified by the id
     * @throws RiceIllegalArgumentException if the given naturalLanguageUsage is
     *                                      null or invalid
     */
    @WebMethod(operationName = "updateNaturalLanguageUsage")
    @CacheEvict(value={NaturalLanguageUsage.Cache.NAME, NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public void updateNaturalLanguageUsage(@WebParam(name = "naturalLanguageUsage") NaturalLanguageUsage naturalLanguageUsage) throws RiceIllegalArgumentException;

    /**
     * Delete NaturalLanguageUsage
     *
     * @param naturalLanguageUsageId  identifier of the NaturalLanguageUsage to be deleted
     * @throws RiceIllegalArgumentException  if the given naturalLanguageUsageId is null or invalid
     */
    @WebMethod(operationName = "deleteNaturalLanguageUsage")
    @CacheEvict(value={NaturalLanguageUsage.Cache.NAME, NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public void deleteNaturalLanguageUsage(@WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId) throws RiceIllegalArgumentException;

    /**
     * Translates and retrieves a NaturalLanguage for a given KRMS object (e.g, proposition
     * or agenda), NaturalLanguage usage type (context) and language into natural language
     *
     * @param namespace namespace to search on.
     * @return list of NaturalLanguageUsages in a particular namespace
     */
    @WebMethod(operationName = "getNaturalLanguageUsagesByNamespace")
    @XmlElementWrapper(name = "naturalLanguageUsages", required = true)
    @XmlElement(name = "naturalLanguageUsage", required = false)
    @WebResult(name = "naturalLanguageUsages")
    @Cacheable(value= NaturalLanguageUsage.Cache.NAME, key="'namespace=' + #p0")
    public List<NaturalLanguageUsage> getNaturalLanguageUsagesByNamespace(@WebParam(name = "namespace") String namespace)
            throws RiceIllegalArgumentException;

    ////
    //// context methods
    ////
    /**
     * Create Context
     *
     * @param contextDefinition data for the new Context to be created
     * @return newly created Context
     * @throws RiceIllegalArgumentException if the given contextDefinition is
     * null or invalid or already in use.
     */
    @WebMethod(operationName = "createContext")
    @WebResult(name = "context")
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
    public ContextDefinition createContext(@WebParam(name = "contextDefinition") ContextDefinition contextDefinition) throws RiceIllegalArgumentException;

    
    /**
     * find Create Context
     * 
     * Searches for an existing context with the same name and namespace and returns it
     * otherwise it creates the context.
     *
     * @param contextDefinition data for the new Context to be created
     * @return newly created Context
     * @throws RiceIllegalArgumentException if the given contextDefinition is
     * null or invalid
     */
    @WebMethod(operationName = "findCreateContext")
    @WebResult(name = "context")
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
    public ContextDefinition findCreateContext(@WebParam(name = "contextDefinition") ContextDefinition contextDefinition) throws RiceIllegalArgumentException;

    
    /**
     * Update the Context specified by the identifier in the input DTO
     *
     * @param contextDefinition DTO with updated info and identifier of the
     * object to be updated
     * @throws RiceIllegalArgumentException if the given contextDefinition is
     * null or invalid
     */
    @WebMethod(operationName = "updateContext")
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
    public void updateContext(@WebParam(name = "contextDefinition") ContextDefinition contextDefinition) throws RiceIllegalArgumentException;

    /**
     * Delete the specified Context
     *
     * @param id identifier of the object to be deleted
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "deleteContext")
    @CacheEvict(value={ContextDefinition.Cache.NAME}, allEntries = true)
    public void deleteContext(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieve Context for the specified id
     *
     * @param id identifier for the Context
     * @return specified Context
     * @throws RiceIllegalArgumentException if the given id is null or invalid
     */
    @WebMethod(operationName = "getContext")
    @WebResult(name = "context")
    @Cacheable(value= ContextDefinition.Cache.NAME, key="'id=' + #p0")
    public ContextDefinition getContext(@WebParam(name = "id") String id) throws RiceIllegalArgumentException;

    /**
     * Retrieves an Context from the repository based on the provided context
     * name and namespace.
     *
     * @param name the name of the Context to retrieve.
     * @param namespace the namespace that the context is under.
     * @return an {@link ContextDefinition} identified by the given name and
     * namespace. A null reference is returned if an invalid or non-existent
     * name and namespace combination is supplied.
     * @throws RiceIllegalArgumentException if the either the name or the
     * namespace is null or blank.
     */
    @WebMethod(operationName = "getContextByNameAndNamespace")
    @WebResult(name = "context")
    @Cacheable(value= ContextDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
    public ContextDefinition getContextByNameAndNamespace(@WebParam(name = "name") String name,
            @WebParam(name = "namespace") String namespace)
            throws RiceIllegalArgumentException;

    //// natural languatge template methods
    /**
     * This will create a {@link NaturalLanguageTemplate} exactly like the
     * parameter passed in except the id will be assigned and create date/user 
     * will be set.
     *
     * @param naturalLanguageTemplate The NaturalLanguageTemplate to create.
     * @throws RiceIllegalArgumentException if the NaturalLanguageTemplate is null.
     * @throws IllegalStateException if the NaturalLanguageTemplate already
     * exists in the system.
     * @return a {@link NaturalLanguageTemplate} exactly like the parameter
     * passed in.
     *
     */
    @WebMethod(operationName = "createNaturalLanguageTemplate")
    @WebResult(name = "naturalLanguageTemplate")
    @CacheEvict(value={NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public NaturalLanguageTemplate createNaturalLanguageTemplate(@WebParam(name = "naturalLanguageTemplate") NaturalLanguageTemplate naturalLanguageTemplate)
            throws RiceIllegalArgumentException;

    /**
     * Retrieves a NaturalLanguageTemplate from the repository based on the
     * given id.
     *
     * @param naturalLanguageTemplateId to retrieve.
     * @return a {@link NaturalLanguageTemplate} identified by the given id. 
     * @throws IllegalArgumentException if the given actionId is null     *
     */
    @WebMethod(operationName = "getNaturalLanguageTemplate")
    @WebResult(name = "naturalLanguageTemplate")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'naturalLanguageTemplateId=' + #p0")
    public NaturalLanguageTemplate getNaturalLanguageTemplate(@WebParam(name = "naturalLanguageTemplateId") String naturalLanguageTemplateId)
            throws RiceIllegalArgumentException;

    /**
     * This will update an existing {@link NaturalLanguageTemplate}.
     *
     * @param naturalLanguageTemplate The NaturalLanguageTemplate to update.
     * @throws RiceIllegalArgumentException if the NaturalLanguageTemplate is null.
     * exists in the system.
     *
     */
    @WebMethod(operationName = "updateNaturalLanguageTemplate")
    @CacheEvict(value={NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public void updateNaturalLanguageTemplate(@WebParam(name = "naturalLanguageTemplate") NaturalLanguageTemplate naturalLanguageTemplate)
            throws RiceIllegalArgumentException;

    /**
     * Delete the {@link NaturalLanguageTemplate} with the given id.
     *
     * @param naturalLanguageTemplateId to delete.
     * @throws RiceIllegalArgumentException if the NaturalLanguageTemplate is null.
     *
     */
    @WebMethod(operationName = "deleteNaturalLanguageTemplate")
    @CacheEvict(value={NaturalLanguageTemplate.Cache.NAME}, allEntries = true)
    public void deleteNaturalLanguageTemplate(@WebParam(name = "naturalLanguageTemplateId") String naturalLanguageTemplateId)
            throws RiceIllegalArgumentException;

    /**
     * Finds all the natural language templates for a particular language
     *
     * @param languageCode language on which to search
     * @return list of templates for that language
     */
    @WebMethod(operationName = "findNaturalLanguageTemplatesByLanguageCode")
    @XmlElementWrapper(name = "naturalLangaugeTemplates", required = true)
    @XmlElement(name = "naturalLangaugeTemplate", required = false)
    @WebResult(name = "naturalLangaugeTemplates")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'languageCode=' + #p0")
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByLanguageCode(@WebParam(name = "languageCode") String languageCode)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId")
    @WebResult(name = "naturalLangaugeTemplate")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'languageCode=' + #p0 + '|' + 'typeId=' + #p1 + '|' + 'naturalLanguageUsageId=' + #p2")
    public NaturalLanguageTemplate findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(@WebParam(name = "languageCode") String languageCode,
            @WebParam(name = "typeId") String typeId,
            @WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId)
            throws RiceIllegalArgumentException;

    /**
     * Find all the natural language templates for a particular usage
     *
     * @param naturalLanguageUsageId the usage on which to search
     * @return list of templates that match the usage
     */
    @WebMethod(operationName = "findNaturalLanguageTemplatesByNaturalLanguageUsage")
    @XmlElementWrapper(name = "naturalLangaugeTemplates", required = true)
    @XmlElement(name = "naturalLangaugeTemplate", required = false)
    @WebResult(name = "naturalLangaugeTemplates")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'naturalLanguageUsageId=' + #p0")
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByNaturalLanguageUsage(@WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId)
            throws RiceIllegalArgumentException;

    /**
     * Find all the natural language templates of a particular type
     *
     * Template types are keys that identify the message that is to be expressed
     * in different languages and in different usage scenarios
     *
     * @param typeId on which to search
     * @return list of templates matching that type
     */
    @WebMethod(operationName = "findNaturalLanguageTemplatesByType")
    @XmlElementWrapper(name = "naturalLangaugeTemplates", required = true)
    @XmlElement(name = "naturalLangaugeTemplate", required = false)
    @WebResult(name = "naturalLangaugeTemplates")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'typeId=' + #p0")
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByType(@WebParam(name = "typeId") String typeId)
            throws RiceIllegalArgumentException;

    /**
     * Find the natural language template using the actual text of the template.
     *
     * @param template text to match exactly
     * @return list of matching templates
     */
    @WebMethod(operationName = "findNaturalLanguageTemplatesByTemplate")
    @XmlElementWrapper(name = "naturalLangaugeTemplates", required = true)
    @XmlElement(name = "naturalLangaugeTemplate", required = false)
    @WebResult(name = "naturalLangaugeTemplates")
    @Cacheable(value= NaturalLanguageTemplate.Cache.NAME, key="'template=' + #p0")
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByTemplate(@WebParam(name = "template") String template)
            throws RiceIllegalArgumentException;

    ////
    //// translation methods
    ////
    /**
     * Translates and retrieves a NaturalLanguage for a given KRMS object (e.g,
     * proposition or agenda), NaturalLanguage usage type (context) and language
     * into natural language
     *
     * @param naturalLanguageUsageId Natural language usage information
     * @param typeId    KRMS object type id (for example, could refer to agenda
     *                  or proposition)
     * @param krmsObjectId KRMS object identifier
     * @param languageCode  desired
     * @return natural language corresponding to the NaturalLanguage usage, KRMS object id, KRMS object type and desired language
     * @throws RiceIllegalArgumentException if the given naturalLanguageUsageId, typeId,
     *                                      krmsObjectId or language is null or
     *                                      invalid
     */
    @WebMethod(operationName = "translateNaturalLanguageForObject")
    @WebResult(name = "naturalLanguage")
    @Override
    public String translateNaturalLanguageForObject(@WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId,
            @WebParam(name = "typeId") String typeId, @WebParam(name = "krmsObjectId") String krmsObjectId,
            @WebParam(name = "languageCode") String languageCode) throws RiceIllegalArgumentException;

    /**
     * Retrieve all the NaturalLanguageUsages
     *
     * @return list of NaturalLanguageUsages
     */
    @WebMethod(operationName = "translateNaturalLanguageForProposition")
    @WebResult(name = "naturalLanguage")
    @Override
    public String translateNaturalLanguageForProposition(@WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId,
            @WebParam(name = "propositionDefinintion") PropositionDefinition propositionDefinintion,
            @WebParam(name = "languageCode") String languageCode)
            throws RiceIllegalArgumentException;

    /**
     * Translates NaturalLanguage for a given proposition, returning a tree of
     * the NaturalLanguage.
     *
     * This method is used to "preview" the proposition in a tree structure
     * before saving.
     *
     * @param naturalLanguageUsageId Natural language usage information
     * @param propositionDefinintion proposition to be translated
     * @param languageCode desired
     * @return natural language tree corresponding to the NaturalLanguage usage,
     * proposition and desired language
     * @throws RiceIllegalArgumentException if the given naturalLanguageUsageId,
     * proposition, or language is null or invalid
     */
    @WebMethod(operationName = "translateNaturalLanguageTreeForProposition")
    @WebResult(name = "naturalLanguageTree")
    @Override
    public NaturalLanguageTree translateNaturalLanguageTreeForProposition(@WebParam(name = "naturalLanguageUsageId") String naturalLanguageUsageId,
            @WebParam(name = "propositionDefinintion") PropositionDefinition propositionDefinintion,
            @WebParam(name = "languageCode") String languageCode)
            throws RiceIllegalArgumentException;

  
    /**
     * Query for Context ids based on the given search criteria which is a Map
     * of Context field names to values.
     * <p/>
     * <p> This method returns it's results as a List of Context ids that match
     * the given search criteria. </p>
     *
     * @param queryByCriteria the criteria. Cannot be null.
     * @return a list of ids matching the given criteria properties. An empty
     * list is returned if an invalid or non-existent criteria is supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findContextIds")
    @XmlElementWrapper(name = "contextIds", required = true)
    @XmlElement(name = "context", required = false)
    @WebResult(name = "contextIds")
    List<String> findContextIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * Query for Agenda ids based on the given search criteria which is a Map of
     * Agenda field names to values.
     * <p/>
     * <p> This method returns it's results as a List of Agenda ids that match
     * the given search criteria. </p>
     *
     * @param queryByCriteria the criteria. Cannot be null.
     * @return a list of ids matching the given criteria properties. An empty
     * list is returned if an invalid or non-existent criteria is supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findAgendaIds")
    @XmlElementWrapper(name = "contextIds", required = true)
    @XmlElement(name = "agenda", required = false)
    @WebResult(name = "agendaIds")
    List<String> findAgendaIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * Query for Rule ids based on the given search criteria which is a Map of
     * Rule field names to values.
     * <p/>
     * <p> This method returns it's results as a List of Rule ids that match the
     * given search criteria. </p>
     *
     * @param queryByCriteria the criteria. Cannot be null.
     * @return a list of ids matching the given criteria properties. An empty
     * list is returned if an invalid or non-existent criteria is supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findRuleIds")
    @XmlElementWrapper(name = "ruleIds", required = true)
    @XmlElement(name = "rule", required = false)
    @WebResult(name = "ruleIds")
    List<String> findRuleIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * Query for Proposition ids based on the given search criteria which is a
     * Map of Proposition field names to values.
     * <p/>
     * <p> This method returns it's results as a List of Proposition ids that
     * match the given search criteria. </p>
     *
     * @param queryByCriteria the criteria. Cannot be null.
     * @return a list of ids matching the given criteria properties. An empty
     * list is returned if an invalid or non-existent criteria is supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findPropositionIds")
    @XmlElementWrapper(name = "propositionIds", required = true)
    @XmlElement(name = "proposition", required = false)
    @WebResult(name = "propositionIds")
    List<String> findPropositionIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

    /**
     * Query for Action ids based on the given search criteria which is a Map
     * of Action field names to values.
     * <p/>
     * <p> This method returns it's results as a List of Action ids that match
     * the given search criteria. </p>
     *
     * @param queryByCriteria the criteria. Cannot be null.
     * @return a list of ids matching the given criteria properties. An empty
     * list is returned if an invalid or non-existent criteria is supplied.
     * @throws RiceIllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findActionIds")
    @XmlElementWrapper(name = "actionIds", required = true)
    @XmlElement(name = "action", required = false)
    @WebResult(name = "actionIds")
    List<String> findActionIds(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;
    
    ////
    //// extra methods from RuleRepositoryService copied here so I can break the dependence on that service
    //// and then implement different caching schemes for each 
    ////
    
    
      /**
     * Locates a ContextDefinition based on the given map of context qualifiers.
     * The requirements for valid selection criteria are implementation
     * dependent. An IllegalArgumentException may be thrown if the
     * implementation can't operate with the given criteria.
     *
     * @param contextSelectionCriteria
     * @return
     * @see ContextSelectionCriteria
     * @throws RiceIllegalArgumentException if the implementation can't handle
     * the given ContextSelectionCriteria
     */
    @WebMethod(operationName = "selectContext")
    @WebResult(name = "contextDefinition")
    public ContextDefinition selectContext(@WebParam(name = "contextSelectionCriteria") ContextSelectionCriteria contextSelectionCriteria)
            throws RiceIllegalArgumentException;

    /**
     * Retrieves the agenda tree for the given agendaId. The agenda tree
     * includes the entire agenda definition in the appropriate order and with
     * the defined agenda branching.
     *
     * @param agendaId the id of the agenda for which to retrieve the agenda
     * tree
     * @return the agenda tree, or null if no agenda could be located for the
     * given agendaId
     *
     * @throws RiceIllegalArgumentException if the given agendaId is null
     */
    @WebMethod(operationName = "getAgendaTree")
    @WebResult(name = "agendaTree")
    @Cacheable(value= AgendaTreeDefinition.Cache.NAME, key="'agendaId=' + #p0")
    public AgendaTreeDefinition getAgendaTree(@WebParam(name = "agendaId") String agendaId)
            throws RiceIllegalArgumentException;

    /**
     * Retrieves all of the agendas trees for the given list of agendaIds. The
     * agenda tree includes the entire agenda definition in the appropriate
     * order and with the defined agenda branching.
     *
     * <p>The list which is returned from this operation may not be the same
     * size as the list which is passed to this method. If an agenda doesn't
     * exist for a given agenda id then no result for that id will be returned
     * in the list. As a result of this, the returned list can be empty, but it
     * will never be null.
     *
     * @param agendaIds the list of agenda ids for which to retrieve the agenda
     * trees
     * @return the list of agenda trees for the given ids, this list will only
     * contain agenda trees for the ids that were resolved successfully, it will
     * never return null but could return an empty list if no agenda trees could
     * be loaded for the given set of ids
     *
     * @throws RiceIllegalArgumentException if the given list of agendaIds is
     * null
     */
    @WebMethod(operationName = "getAgendaTrees")
    @XmlElementWrapper(name = "agendaTrees", required = true)
    @XmlElement(name = "agendaTree", required = false)
    @WebResult(name = "agendaTrees")
    @Cacheable(value= AgendaTreeDefinition.Cache.NAME, key="'agendaIds=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
    public List<AgendaTreeDefinition> getAgendaTrees(@WebParam(name = "agendaIds") List<String> agendaIds)
            throws RiceIllegalArgumentException;

   
}
