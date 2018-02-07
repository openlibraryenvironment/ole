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
package org.kuali.rice.krms.framework.engine;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;

/**
 * A Rule evaluates a given {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment}, returning true if its conditions pass, false if they do not.
 *
 * @see org.kuali.rice.krms.api.repository.rule.RuleDefinitionContract
 * @see org.kuali.rice.krms.api.repository.rule.RuleDefinition
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Rule {

    /**
     * Evaluate a given {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment}, returning true if the rules conditions pass, false if they do not.
     * @param environment ExecutionEnvironment
     * @return true if given {@link org.kuali.rice.krms.api.engine.ExecutionEnvironment} the rule's conditions pass, false if they do not
     */
	public boolean evaluate(ExecutionEnvironment environment);
}
