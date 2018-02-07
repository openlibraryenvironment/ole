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
package org.kuali.rice.krms.api.repository.rule;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;
import org.kuali.rice.core.api.mo.common.active.Inactivatable;
import org.kuali.rice.krms.api.repository.action.ActionDefinitionContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinitionContract;

/**
 * Defines the contract for a {@link RuleDefinition}
 *
 * @see RuleDefinition
 * @see org.kuali.rice.krms.framework.engine.Rule
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleDefinitionContract extends Identifiable, Inactivatable, Versioned {
    /**
     * This is the name of the Rule 
     * <p>
     * name - the name of the Rule
     * </p>
     * @return the name of the Rule
     */
    public String getName();

    /**
     * This is the description of the Rule 
     * <p>
     * description - the description of the Rule
     * </p>
     * @return the description of the Rule
     */
    public String getDescription();

	/**
	 * This is the namespace of the Rule 
	 * <p>
	 * The namespace of the Rule
	 * </p>
	 * @return the namespace of the Rule
	 */
	public String getNamespace();

	/**
	 * This is the KrmsType of the Rule
	 *
	 * @return id for KRMS type related of the Rule
	 */
	public String getTypeId();
	
	/**
	 * This method returns the ID of the Proposition associated with the rule.
	 * <p>
	 * Each Rule has exactly one Proposition associated with it.
	 * <p>
	 * @return the id of the Proposition associated with the Rule
	 */
	public String getPropId();
	
	/**
	 * This method returns the Proposition associated with the rule.
	 * <p>
	 * Each Rule has exactly one Proposition associated with it.
	 * <p>
	 * @return an immutable representation of the Proposition associated with the Rule
	 */
	public PropositionDefinitionContract getProposition();
	
	/**
	 * This method returns a list of Actions associated with the Rule.
	 * <p>
	 * A Rule may have zero or more Actions associated with it.
	 * <p>
	 * @return An ordered list of Actions associated with a Rule.
	 */
	public List<? extends ActionDefinitionContract> getActions();

	/**
	 * This method returns a Map of attributes associated with the 
	 * Rule. The attributes are represented as name/value pairs.
	 * 
	 * @return a Map<String,String> of RuleAttribute objects.
	 */
	public Map<String, String> getAttributes();
	
}
