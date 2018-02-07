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

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.api.repository.agenda.AgendaTreeDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * The rule repository contains all of the information about context definitions,
 * agendas, and business rules.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(name = "ruleRepositoryService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RuleRepositoryService {

	/**
	 * Locates a ContextDefinition based on the given map of context qualifiers. The requirements for valid selection
     * criteria are implementation dependent.  An IllegalArgumentException may be thrown if the implementation can't
     * operate with the given criteria.
	 * 
	 * @param contextSelectionCriteria
	 * @return
     * @see ContextSelectionCriteria
     * @throws RiceIllegalArgumentException if the implementation can't handle the given ContextSelectionCriteria
	 */
	@WebMethod(operationName = "selectContext")
	@WebResult(name = "contextDefinition")
	public ContextDefinition selectContext(@WebParam(name = "contextSelectionCriteria") ContextSelectionCriteria contextSelectionCriteria)
            throws RiceIllegalArgumentException;
	
	/**
	 * Retrieves the agenda tree for the given agendaId.  The agenda tree includes
	 * the entire agenda definition in the appropriate order and with the
	 * defined agenda branching.
	 * 
	 * @param agendaId the id of the agenda for which to retrieve the agenda tree
	 * @return the agenda tree, or null if no agenda could be located for the given agendaId
	 * 
	 * @throws RiceIllegalArgumentException if the given agendaId is null
	 */
	@WebMethod(operationName = "getAgendaTree")
	@WebResult(name = "agendaTree")
    @Cacheable(value= AgendaTreeDefinition.Cache.NAME, key="'agendaId=' + #p0")
	public AgendaTreeDefinition getAgendaTree(@WebParam(name = "agendaId") String agendaId)
            throws RiceIllegalArgumentException;

    /**
	 * Retrieves all of the agendas trees for the given list of agendaIds.  The agenda tree includes
	 * the entire agenda definition in the appropriate order and with the
	 * defined agenda branching.
	 * 
	 * <p>The list which is returned from this operation may not be the same size as the list
	 * which is passed to this method.  If an agenda doesn't exist for a given agenda id then
	 * no result for that id will be returned in the list.  As a result of this, the returned
	 * list can be empty, but it will never be null.
	 * 
	 * @param agendaIds the list of agenda ids for which to retrieve the agenda trees
	 * @return the list of agenda trees for the given ids, this list will only contain agenda trees for the ids
	 * that were resolved successfully, it will never return null but could return an empty list if no agenda
	 * trees could be loaded for the given set of ids
	 * 
	 * @throws RiceIllegalArgumentException if the given list of agendaIds is null
	 */
	@WebMethod(operationName = "getAgendaTrees")
    @XmlElementWrapper(name = "agendaTrees", required = true)
    @XmlElement(name = "agendaTree", required = false)
	@WebResult(name = "agendaTrees")
	public List<AgendaTreeDefinition> getAgendaTrees(@WebParam(name = "agendaIds") List<String> agendaIds)
            throws RiceIllegalArgumentException;

    /**
	 * Retrieves the rule for the given ruleId.  The rule includes the propositions
	 * which define the condition that is to be evaluated on the rule.  It also
	 * defines a collection of actions which will be invoked if the rule succeeds.
	 * 
	 * @param ruleId the id of the rule to retrieve
	 * @return the rule definition, or null if no rule could be located for the given ruleId
	 * 
	 * @throws RiceIllegalArgumentException if the given ruleId is null
	 */
	@WebMethod(operationName = "getRule")
	@WebResult(name = "rule")
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'ruleId=' + #p0")
	public RuleDefinition getRule(@WebParam(name = "ruleId") String ruleId) throws RiceIllegalArgumentException;
	
	/**
	 * Retrieves all of the rules for the given list of ruleIds.  The rule includes the propositions
	 * which define the condition that is to be evaluated on the rule.  It also
	 * defines a collection of actions which will be invoked if the rule succeeds.
	 * 
	 * <p>The list which is returned from this operation may not be the same size as the list
	 * which is passed to this method.  If a rule doesn't exist for a given rule id then
	 * no result for that id will be returned in the list.  As a result of this, the returned
	 * list can be empty, but it will never be null.
	 * 
	 * @param ruleIds the list of rule ids for which to retrieve the rules
	 * @return the list of rules for the given ids, this list will only contain rules for the ids
	 * that were resolved successfully, it will never return null but could return an empty list if no
	 * rules could be loaded for the given set of ids
	 * 
	 * @throws RiceIllegalArgumentException if the given list of ruleIds is null
	 */
	@WebMethod(operationName = "getRules")
    @XmlElementWrapper(name = "rules", required = true)
    @XmlElement(name = "rule", required = false)
	@WebResult(name = "rules")
	public List<RuleDefinition> getRules(@WebParam(name = "ruleIds") List<String> ruleIds)
            throws RiceIllegalArgumentException;

}
