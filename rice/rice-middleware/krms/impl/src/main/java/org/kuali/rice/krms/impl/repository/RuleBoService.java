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

import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * This is the interface for accessing KRMS repository Rule related bos 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface RuleBoService {
    /**
     * This will create a {@link RuleDefinition} exactly like the parameter passed in.
     *
     * @param rule  The Rule to create
     * @throws IllegalArgumentException if the rule is null
     * @throws IllegalStateException if the rule already exists in the system
     */
    @CacheEvict(value={RuleDefinition.Cache.NAME, PropositionDefinition.Cache.NAME, ActionDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME}, allEntries = true)
	public RuleDefinition createRule(RuleDefinition rule);

    /**
     * This will update an existing {@link RuleDefinition}.
     *
     * @param rule  The Rule to update
     * @throws IllegalArgumentException if the Rule is null
     * @throws IllegalStateException if the Rule does not exists in the system
     */
    @CacheEvict(value={RuleDefinition.Cache.NAME, PropositionDefinition.Cache.NAME, ActionDefinition.Cache.NAME, AgendaItemDefinition.Cache.NAME}, allEntries = true)
	public void updateRule(RuleDefinition rule);

    /**
     * Delete the {@link RuleDefinition} with the given id.
     *
     * @param ruleId to delete.
     * @throws IllegalArgumentException if the Rule is null.
     * @throws IllegalStateException if the Rule does not exists in the system
     *
     */
    public void deleteRule(String ruleId);

    /**
     * Retrieves an Rule from the repository based on the given rule id.
     *
     * @param ruleId the id of the Rule to retrieve
     * @return an {@link RuleDefinition} identified by the given actionId.
     * A null reference is returned if an invalid or non-existent id is supplied.
     * @throws IllegalArgumentException if the ruleId is null or blank.
     */
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'ruleId=' + #p0")
	public RuleDefinition getRuleByRuleId(String ruleId);

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
    @Cacheable(value= RuleDefinition.Cache.NAME, key="'name=' + #p0 + '|' + 'namespace=' + #p1")
	public RuleDefinition getRuleByNameAndNamespace(String name, String namespace);
	
//	public void createRuleAttribute(RuleAttribute ruleAttribute);
//	public void updateRuleAttribute(RuleAttribute ruleAttribute);
//	
//	public RuleAttribute getRuleAttributeById(String attrId);

//	public void setBusinessObjectService(final BusinessObjectService businessObjectService);
//	public List<RuleDefinition> convertListOfBosToImmutables(final Collection<RuleBo> ruleBos);
}
