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
package org.kuali.rice.krms.impl.provider.repository;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.type.RuleTypeService;
import org.kuali.rice.krms.impl.type.KrmsTypeResolver;

/**
 * A rule which doesn't load it's members until evaluation.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
final class LazyRule implements Rule {

	private final RuleDefinition ruleDefinition;
	private final KrmsTypeResolver typeResolver;
	
	private final Object mutex = new Object();
	
	// volatile for double-checked locking idiom
	private volatile Rule rule;

    /**
     *
     * @param ruleDefinition
     * @param typeResolver
     */
	LazyRule(RuleDefinition ruleDefinition, KrmsTypeResolver typeResolver) {
		this.ruleDefinition = ruleDefinition;
		this.typeResolver = typeResolver;
		this.rule = null;
	}
	
	@Override
	public boolean evaluate(ExecutionEnvironment environment) {
		return getRule().evaluate(environment);
	}

	/**
	 * Gets the rule using a lazy double-checked locking mechanism as documented in Effective Java Item 71.
	 */
	private Rule getRule() {
		Rule localRule = rule;
		if (localRule == null) {
			synchronized (mutex) {
				localRule = rule;
				if (localRule == null) {
					rule = localRule = constructRule();
				}
			}
		}
		return localRule;
	}

    /**
     * builder method
     * @return Rule
     */
	private Rule constructRule() {
		RuleTypeService ruleTypeService = typeResolver.getRuleTypeService(ruleDefinition);
		Rule rule = ruleTypeService.loadRule(ruleDefinition);
		if (rule == null) {
			rule = new Rule() {
				@Override
				public boolean evaluate(ExecutionEnvironment environment) {
                    return false;
				}
			};
		}
		return rule;
	}

	
}
