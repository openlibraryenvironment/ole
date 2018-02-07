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
package org.kuali.rice.kew.api.validation;

import org.kuali.rice.kew.api.rule.RuleContract;
import org.kuali.rice.kew.api.rule.RuleDelegationContract;

/**
 * The RuleValidationContext represents the context under which to validate a Rule which is being entered
 * into the system, be it through the web-based Rule GUI or via an XML import.
 *
 * The ruleAuthor is the UserSession of the individual who is entering or editing the rule.  This may
 * be <code>null</code> if the rule is being run through validation from the context of an XML rule
 * import.
 *
 * The RuleDelegation represents the pointer to the rule from it's parent rule's RuleResponsibility.
 * This will be <code>null</code> if the rule being entered is not a delegation rule.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface RuleValidationContextContract {
	/**
	 * Retrieve the rule which is being validated.
	 */
	public RuleContract getRule();

	/**
	 * Retrieve the principal of the rule author.  May be null in the
	 * case of an XML rule import.
	 */
	public String getRuleAuthorPrincipalId();

	/**
	 * Retrieve the RuleDelegation representing the parent of the rule being validated.  If the rule is
	 * not a delegation rule, then this will return null;
	 */
	public RuleDelegationContract getRuleDelegation();
}