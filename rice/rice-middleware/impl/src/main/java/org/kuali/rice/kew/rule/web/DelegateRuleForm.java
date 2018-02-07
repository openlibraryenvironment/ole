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
package org.kuali.rice.kew.rule.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.form.KualiForm;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Struts ActionForm for {@link DelegateRuleAction}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DelegateRuleForm extends KualiForm {

	private static final long serialVersionUID = 5412969516727713859L;

	private String parentRuleId;
	private String parentResponsibilityId;

	private RuleBaseValues parentRule;
	private RuleResponsibilityBo parentResponsibility;
		
	private List<String> reviewers = new ArrayList<String>();
	private List<String> responsibilityTypes = new ArrayList<String>();
	private List<String> actionRequestCodes = new ArrayList<String>();
	
	public String getParentRuleId() {
		return this.parentRuleId;
	}

	public void setParentRuleId(String parentRuleId) {
		this.parentRuleId = parentRuleId;
	}

	public String getParentResponsibilityId() {
		return this.parentResponsibilityId;
	}

	public void setParentResponsibilityId(String parentResponsibilityId) {
		this.parentResponsibilityId = parentResponsibilityId;
	}

	public RuleBaseValues getParentRule() {
		return this.parentRule;
	}

	public void setParentRule(RuleBaseValues parentRule) {
	    if (this.parentRule != null 
	            && parentRule != null
	            && this.parentResponsibility != null) {
	    	if (!StringUtils.equals(this.parentRule.getId(), parentRule.getId())) {
	            this.parentResponsibility = null;
	            this.parentResponsibilityId = null;
	        }
	    }
		this.parentRule = parentRule;
	}

	public RuleResponsibilityBo getParentResponsibility() {
		return this.parentResponsibility;
	}

	public void setParentResponsibility(RuleResponsibilityBo parentResponsibility) {
		this.parentResponsibility = parentResponsibility;
	}

	public List<String> getReviewers() {
		return this.reviewers;
	}

	public void setReviewers(List<String> reviewers) {
		this.reviewers = reviewers;
	}

	public List<String> getResponsibilityTypes() {
		return this.responsibilityTypes;
	}

	public void setResponsibilityTypes(List<String> responsibilityTypes) {
		this.responsibilityTypes = responsibilityTypes;
	}

	public List<String> getActionRequestCodes() {
		return this.actionRequestCodes;
	}

	public void setActionRequestCodes(List<String> actionRequestCodes) {
		this.actionRequestCodes = actionRequestCodes;
	}

	public String getRuleDescription() {
		if (getParentRule() == null) {
			return "";
		}
		return getParentRule().getDescription();
	}

	@Override
	public void populate(HttpServletRequest request) {
				
		super.populate(request);

		reviewers.clear();
		responsibilityTypes.clear();
		actionRequestCodes.clear();
		
		if (getParentRuleId() != null) {
			setParentRule(KEWServiceLocator.getRuleService().findRuleBaseValuesById(getParentRuleId()));
		}
		if (getParentResponsibilityId() != null && getParentRule() != null) {
			for (RuleResponsibilityBo responsibility : getParentRule().getRuleResponsibilities()) {
				if (responsibility.getResponsibilityId().equals(getParentResponsibilityId())) {
					setParentResponsibility(responsibility);
					break;
				}
			}
		}
		
		if (getParentRule() != null) {
			for (RuleResponsibilityBo responsibility : getParentRule().getRuleResponsibilities()) {
				if (KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID.equals(responsibility.getRuleResponsibilityType())) {
					Principal principal = KEWServiceLocator.getIdentityHelperService().getPrincipal(responsibility.getRuleResponsibilityName());
					if (principal != null) {
					    reviewers.add(principal.getPrincipalName());
					}
					responsibilityTypes.add(KewApiConstants.RULE_RESPONSIBILITY_WORKFLOW_ID_LABEL);
				} else if (KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID.equals(responsibility.getRuleResponsibilityType())) {
					Group group = KimApiServiceLocator.getGroupService().getGroup(responsibility.getRuleResponsibilityName());
					if (group != null) {
					    reviewers.add(group.getNamespaceCode() + " " + group.getName());
					}
					responsibilityTypes.add(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID_LABEL);
				} else if (KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID.equals(responsibility.getRuleResponsibilityType())) {
					reviewers.add(responsibility.getResolvedRoleName());
					responsibilityTypes.add(KewApiConstants.RULE_RESPONSIBILITY_ROLE_ID_LABEL);
				} else {
					throw new RiceRuntimeException("Encountered a responsibility with an invalid type, type value was " + responsibility.getRuleResponsibilityType());
				}
				actionRequestCodes.add(KewApiConstants.ACTION_REQUEST_CODES.get(responsibility.getActionRequestedCd()));
			}
		}
		
	}

	

}
