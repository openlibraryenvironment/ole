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

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SQLUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.util.CodeTranslator;
import org.kuali.rice.kew.exception.WorkflowServiceError;
import org.kuali.rice.kew.rule.KeyValueId;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.WorkflowRuleAttributeRows;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.service.RuleDelegationService;
import org.kuali.rice.kew.rule.service.RuleTemplateService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A decorator around a {@link RuleBaseValues} object which provides some
 * convienance functions for interacting with the bean from the web-tier.
 * This helps to alleviate some of the weaknesses of JSTL.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WebRuleBaseValues extends RuleBaseValues {

	private static final long serialVersionUID = 5938997470219200474L;
    private static final int TO_DATE_UPPER_LIMIT = 2100;
	private List rows = new ArrayList();
	private List fields = new ArrayList();
	private List roles = new ArrayList();
	private String fromDateString;
	private String toDateString;
	private String ruleTemplateName;
	private boolean hasExtensionValueErrors = false;

	public WebRuleBaseValues() {
	}

	public WebRuleBaseValues(RuleBaseValues rule) throws Exception {
		edit(rule);
	}

    private void loadFields() {
   		fields.clear();
   		if (getRuleTemplateId() != null) {
   			RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(getRuleTemplateId());
   			if (ruleTemplate != null) {
   				List ruleTemplateAttributes = ruleTemplate.getActiveRuleTemplateAttributes();
   				Collections.sort(ruleTemplateAttributes);
   				for (Iterator iter = ruleTemplateAttributes.iterator(); iter.hasNext();) {
   					RuleTemplateAttributeBo ruleTemplateAttribute = (RuleTemplateAttributeBo) iter.next();
   					if (!ruleTemplateAttribute.isWorkflowAttribute()) {
   						continue;
   					}
                    WorkflowRuleAttributeRows workflowRuleAttributeRows =
                            KEWServiceLocator.getWorkflowRuleAttributeMediator().getRuleRows(null, ruleTemplateAttribute);
   					for (Row row : workflowRuleAttributeRows.getRows()) {
   						for (Field field : row.getFields()) {
                               String fieldValue = "";
                               RuleExtensionValue extensionValue = getRuleExtensionValue(ruleTemplateAttribute.getId(), field.getPropertyName());
                               fieldValue = (extensionValue != null) ? extensionValue.getValue() : field.getPropertyValue();
                               fields.add(new KeyValueId(field.getPropertyName(), fieldValue, ruleTemplateAttribute.getId()));
                           }
   					}
   				}
   			}
   		}
   	}

	private void loadWebValues() {
		loadRows();
		loadDates();
		loadRuleTemplateName();
	}

    private void loadRows() {
   		getRoles().clear();
   		if (getRuleTemplateId() != null) {
   			RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(getRuleTemplateId());
   			if (ruleTemplate != null) {
   				setRuleTemplateName(ruleTemplate.getName());
   				List<RuleTemplateAttributeBo> ruleTemplateAttributes = ruleTemplate.getActiveRuleTemplateAttributes();
   				Collections.sort(ruleTemplateAttributes);
   				List<Row> rows = new ArrayList<Row>();
   				for (RuleTemplateAttributeBo ruleTemplateAttribute : ruleTemplateAttributes) {
   					if (!ruleTemplateAttribute.isWorkflowAttribute()) {
   						continue;
   					}
                    WorkflowRuleAttributeRows workflowRuleAttributeRows =
                            KEWServiceLocator.getWorkflowRuleAttributeMediator().getRuleRows(getFieldMap(ruleTemplateAttribute.getId()), ruleTemplateAttribute);
                    rows.addAll(workflowRuleAttributeRows.getRows());
                    getRoles().addAll(KEWServiceLocator.getWorkflowRuleAttributeMediator().getRoleNames(ruleTemplateAttribute));
   				}
   				setRows(rows);
   			}
   		}
   	}

	private void loadDates() {
		if (getFromDateString() != null) {
			setFromDateString(RiceConstants.getDefaultDateFormat().format(getFromDateValue()));
		}
		if (getToDateString() != null) {
			setToDateString(RiceConstants.getDefaultDateFormat().format(getToDateValue()));
		}
	}

	private void loadRuleTemplateName() {
		if (org.apache.commons.lang.StringUtils.isEmpty(getRuleTemplateName()) && getRuleTemplateId() != null) {
			RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(getRuleTemplateId());
			if (ruleTemplate != null) {
				setRuleTemplateName(ruleTemplate.getName());
			}
		}
	}

	public List getFields() {
		return fields;
	}

	public void setFields(List fields) {
		this.fields = fields;
	}

	public KeyValueId getField(int index) {
		while (getFields().size() <= index) {
			KeyValueId field = new KeyValueId();
			getFields().add(field);
		}
		return (KeyValueId) getFields().get(index);
	}

	public String getFromDateString() {
		return fromDateString;
	}

	public void setFromDateString(String fromDateString) {
		this.fromDateString = fromDateString;
	}

	public List<RoleName> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleName> roles) {
		this.roles = roles;
	}

	public List<RoleName> getRows() {
		return rows;
	}

	public void setRows(List ruleTemplateAttributes) {
		this.rows = ruleTemplateAttributes;
	}

    @Override
	public String getToDateString() {
		return this.toDateString;
	}

	public void setToDateString(String toDateString) {
		this.toDateString = toDateString;
	}

	@Override
	public String getRuleTemplateName() {
		return ruleTemplateName;
	}

	public void setRuleTemplateName(String ruleTemplateName) {
		this.ruleTemplateName = ruleTemplateName;
	}

	public boolean isHasExtensionValueErrors() {
		return hasExtensionValueErrors;
	}

	public void setHasExtensionValueErrors(boolean hasRuleExtensionValueErrors) {
		this.hasExtensionValueErrors = hasRuleExtensionValueErrors;
	}

	/** Web Logic * */

	/**
	 * Populates this WebRuleBaseValues object for editing the given rule.
	 */
	public void edit(RuleBaseValues rule) throws Exception {
		load(rule);
		initialize();
	}

	/**
	 * Loads the given rule into this WebRuleBaseValues.
	 */
	public void load(RuleBaseValues rule) throws Exception {
		PropertyUtils.copyProperties(this, rule);
		injectWebMembers();
	}

	public void initialize() throws Exception {
		loadFields();
		// setPreviousRuleId(getId());
		for (Object element : getRuleResponsibilities()) {
			WebRuleResponsibility responsibility = (WebRuleResponsibility) element;
			responsibility.initialize();
		}
		establishRequiredState();
	}

	private void injectWebMembers() throws Exception {
		List currentResponsibilities = getRuleResponsibilities();
		setRuleResponsibilities(new ArrayList());
		for (Iterator iterator = currentResponsibilities.iterator(); iterator.hasNext();) {
			RuleResponsibilityBo responsibility = (RuleResponsibilityBo) iterator.next();
			WebRuleResponsibility webResponsibility = createNewRuleResponsibility();
			webResponsibility.load(responsibility);
		}
	}

	/**
	 * Establishes any missing and required state in the WebRuleBaseValues.
	 */
	public void establishRequiredState() throws Exception {
		loadWebValues();
		if (getRuleResponsibilities().isEmpty()) {
			createNewRuleResponsibility();
		}
		for (Object element : getRuleResponsibilities()) {
			WebRuleResponsibility responsibility = (WebRuleResponsibility) element;
			responsibility.establishRequiredState();
		}
	}

	@Override
	public RuleResponsibilityBo getResponsibility(int index) {
		while (getRuleResponsibilities().size() <= index) {
			createNewRuleResponsibility();
		}
		return getRuleResponsibilities().get(index);
	}

	public int getResponsibilitiesSize() {
		return getRuleResponsibilities().size();
	}

	public WebRuleResponsibility createNewRuleResponsibility() {
		WebRuleResponsibility responsibility = new WebRuleResponsibility();
		responsibility.setRuleBaseValues(this);
		addRuleResponsibility(responsibility);
		return responsibility;
	}

	public Map getFieldMap(String ruleTemplateAttributeId) {
		Map fieldMap = new HashMap();
		for (Iterator iterator = getFields().iterator(); iterator.hasNext();) {
			KeyValueId field = (KeyValueId) iterator.next();
			if (ruleTemplateAttributeId.equals(field.getId())) {
				fieldMap.put(field.getKey(), field.getValue());
			}
		}
		return fieldMap;
	}

	public void populatePreviousRuleIds() {
		if (getPreviousRuleId() == null) {
			setPreviousRuleId(getId());
		}
		for (Object element : getRuleResponsibilities()) {
			WebRuleResponsibility responsibility = (WebRuleResponsibility) element;
			responsibility.populatePreviousRuleIds();
		}
	}

	/**
	 * This method is used to "materialize" the web rule before it gets saved, if we don't do this then certain fields will be saved as NULL. For example, ruleTemplate.
	 */
	public void materialize() {
		if (getRuleTemplate() == null && getRuleTemplateId() != null) {
			setRuleTemplate(getRuleTemplateService().findByRuleTemplateId(getRuleTemplateId()));
		}
	}

    /**
     * This will ensure that the toDate is never larger than 2100, currently
     * doesn't do any throttling on the from date
     */
    private void throttleDates() {
        if (getToDateValue() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(getToDateValue());
            if (calendar.get(Calendar.YEAR) > TO_DATE_UPPER_LIMIT) {
                calendar.set(Calendar.YEAR, TO_DATE_UPPER_LIMIT);
                setToDateValue(new Timestamp(calendar.getTimeInMillis()));
                setToDateString(new SimpleDateFormat("MM/dd/yyyy").format(getToDateValue()));
            }
        }
    }

	private void saveServiceErrors(String errorKey, Collection srvErrors, ActionErrors errors) {
		for (Iterator iterator = srvErrors.iterator(); iterator.hasNext();) {
			WorkflowServiceError error = (WorkflowServiceError) iterator.next();
			if (error.getArg1() == null && error.getArg2() == null) {
				errors.add(errorKey, new ActionMessage(error.getKey()));
			} else if (error.getArg1() != null && error.getArg2() == null) {
				errors.add(errorKey, new ActionMessage(error.getKey(), error.getArg1()));
			} else {
				errors.add(errorKey, new ActionMessage(error.getKey(), error.getArg1(), error.getArg2()));
			}
		}
	}

	private Timestamp decodeTimestamp(String dateValue) throws ParseException {
		if (org.apache.commons.lang.StringUtils.isEmpty(dateValue)) {
			return null;
		}
		/*  Not the best solution below but does allow our forcing of the 4 digit year
		 *  until KEW and use the KNS for it's date entry/validation
		 */
		String convertedDate = SQLUtils.getEntryFormattedDate(dateValue);
		if (convertedDate == null) {
		    throw new ParseException("Date entered as '" + dateValue + "' is in invalid format", 0);
		}
		Date date = RiceConstants.getDefaultDateFormat().parse(convertedDate);
		return new Timestamp(date.getTime());
	}

	private RuleTemplateService getRuleTemplateService() {
		return (RuleTemplateService) KEWServiceLocator.getService(KEWServiceLocator.RULE_TEMPLATE_SERVICE);
	}

	/**
	 * @return Returns the actionRequestCodes.
	 */
	public Map getActionRequestCodes() {
		Map actionRequestCodes = new HashMap();
		actionRequestCodes.putAll(CodeTranslator.arLabels);
		if (getRuleTemplateId() != null) {
			RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(getRuleTemplateId());
			if (ruleTemplate != null) {
				if (ruleTemplate.getAcknowledge() != null && "false".equals(ruleTemplate.getAcknowledge().getValue())) {
					actionRequestCodes.remove(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ);
				}
				if (ruleTemplate.getComplete() != null && "false".equals(ruleTemplate.getComplete().getValue())) {
					actionRequestCodes.remove(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ);
				}
				if (ruleTemplate.getApprove() != null && "false".equals(ruleTemplate.getApprove().getValue())) {
					actionRequestCodes.remove(KewApiConstants.ACTION_REQUEST_APPROVE_REQ);
				}
				if (ruleTemplate.getFyi() != null && "false".equals(ruleTemplate.getFyi().getValue())) {
					actionRequestCodes.remove(KewApiConstants.ACTION_REQUEST_FYI_REQ);
				}
			}
		}
		return actionRequestCodes;
	}

	public RuleDelegationBo getRuleDelegation() {
		if (getDelegateRule().booleanValue()) {
			List ruleDelegations = getRuleDelegationService().findByDelegateRuleId(getId());
			RuleDelegationBo currentRuleDelegation = (RuleDelegationBo) ruleDelegations.get(0);
			RuleBaseValues mostRecentRule = currentRuleDelegation.getRuleResponsibility().getRuleBaseValues();

			for (Iterator iter = ruleDelegations.iterator(); iter.hasNext();) {
				RuleDelegationBo ruleDelegation = (RuleDelegationBo) iter.next();
				RuleBaseValues parentRule = ruleDelegation.getRuleResponsibility().getRuleBaseValues();

				if (parentRule.getActivationDate().after(mostRecentRule.getActivationDate())) {
					mostRecentRule = ruleDelegation.getRuleResponsibility().getRuleBaseValues();
					currentRuleDelegation = ruleDelegation;
				}
			}
			return currentRuleDelegation;
		}
		return null;
	}

	public String getParentRuleId() {
		if (getDelegateRule().booleanValue()) {
			List ruleDelegations = getRuleDelegationService().findByDelegateRuleId(getId());
			RuleDelegationBo currentRuleDelegation = (RuleDelegationBo) ruleDelegations.get(0);
			RuleBaseValues mostRecentRule = currentRuleDelegation.getRuleResponsibility().getRuleBaseValues();

			for (Iterator iter = ruleDelegations.iterator(); iter.hasNext();) {
				RuleDelegationBo ruleDelegation = (RuleDelegationBo) iter.next();
				RuleBaseValues parentRule = ruleDelegation.getRuleResponsibility().getRuleBaseValues();

				if (parentRule.getActivationDate().after(mostRecentRule.getActivationDate())) {
					mostRecentRule = ruleDelegation.getRuleResponsibility().getRuleBaseValues();
				}
			}
			return mostRecentRule.getId();
		}
		return null;
	}

	private RuleDelegationService getRuleDelegationService() {
		return (RuleDelegationService) KEWServiceLocator.getService(KEWServiceLocator.RULE_DELEGATION_SERVICE);
	}
}
