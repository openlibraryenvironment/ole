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
package org.kuali.rice.krms.impl.repository;

import java.util.List;

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * This is the interface for accessing KRMS repository Action related
 * business objects. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface ActionBoService {

    /**
     * This will create a {@link ActionDefinition} exactly like the parameter passed in.
     *
     * @param action  The Action to create
     * @throws IllegalArgumentException if the action is null
     * @throws IllegalStateException if the action already exists in the system
     */
    @CacheEvict(value={ActionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
	public ActionDefinition createAction(ActionDefinition action);
	
    /**
     * This will update an existing {@link ActionDefinition}.
     *
     * @param action  The Action to update
     * @throws IllegalArgumentException if the Action is null
     * @throws IllegalStateException if the Action does not exists in the system
     */
    @CacheEvict(value={ActionDefinition.Cache.NAME, RuleDefinition.Cache.NAME}, allEntries = true)
	public void updateAction(ActionDefinition action);
	
    /**
     * Retrieves an Action from the repository based on the given action id.
     *
     * @param actionId the id of the Action to retrieve
     * @return an {@link ActionDefinition} identified by the given actionId.  
     * A null reference is returned if an invalid or non-existent id is supplied.
     * @throws IllegalArgumentException if the actionId is null or blank.
     */
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'actionId=' + #p0")
	public ActionDefinition getActionByActionId(String actionId);
	
    /**
     * Retrieves an Action from the repository based on the provided action name
     * and namespace.
     *
     * @param name the name of the Action to retrieve.
     * @param namespace the namespace that the action is under.
     * @return an {@link ActionDefinition} identified by the given name and namespace.  
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     * @throws IllegalArgumentException if the either the name or the namespace
     * is null or blank.
     */
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
	public ActionDefinition getActionByNameAndNamespace(String name, String namespace);

    /**
     * Retrieves an ordered List of Actions associated with a
     * {@link org.kuali.rice.krms.api.repository.rule.RuleDefinition}.
     * The order of the list is determined by the sequenceNumber property
     * of the Actions.
     *
     * @param ruleId the id of the rule
     * @return a list of {@link ActionDefinition} associated with the given rule.  
     * A null reference is returned if an invalid or ruleId is supplied.
     * @throws IllegalArgumentException if the ruleId is null or blank.
     */
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'ruleId=' + #p0")
	public List<ActionDefinition> getActionsByRuleId(String ruleId);

    /**
     * Retrieves an specific Action associated with a Rule.
     *
     * @param ruleId the id of the rule
     * @param sequenceNumber an Integer that represents the sequence number of the action.
     * @return an {@link ActionDefinition} identified associated with the 
     * Rule and identified by the given sequenceNumber
     * A null reference is returned if an invalid or non-existent name and
     * namespace combination is supplied.
     * @throws IllegalArgumentException if the ruleId is null or blank. Or 
     * if the sequenceNumber is null.
     */
    @Cacheable(value= ActionDefinition.Cache.NAME, key="'ruleId=' + #p0 + '|' + 'sequenceNumber=' + #p1")
	public ActionDefinition getActionByRuleIdAndSequenceNumber(String ruleId, Integer sequenceNumber);
	
//	public ActionAttribute createActionAttribute(ActionAttribute actionAttribute);
//	public void updateActionAttribute(ActionAttribute actionAttribute);
	
}
