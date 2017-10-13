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
package org.kuali.rice.kew.validation;

import org.kuali.rice.kew.rule.RuleValidationAttribute;

/**
 * Service which mediates RuleValidationAttribute lookup and invocation.
 * Determines appropriate (possibly remote) {@link org.kuali.rice.kew.framework.validation.RuleValidationAttributeExporterService} endpoint and
 * returns a wrapper which delegates to it.
 *
 * @see org.kuali.rice.kew.rule.RuleValidationAttribute
 * @see org.kuali.rice.kew.framework.validation.RuleValidationAttributeExporterService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleValidationAttributeResolver {
    /**
	 * Resolves the RuleValidationAttribute by name, possibly resulting in delegation over the KSB.
	 *
	 * @return a RuleValidationAttribute suitable (only) for invocation of #validate
	 */
	public RuleValidationAttribute resolveRuleValidationAttribute(String attributeName, String applicationId) throws Exception;
}
