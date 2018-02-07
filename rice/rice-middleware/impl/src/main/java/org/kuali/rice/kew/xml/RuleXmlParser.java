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
package org.kuali.rice.kew.xml;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.api.action.ActionRequestPolicy;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleExpressionDef;
import org.kuali.rice.kew.rule.RuleResponsibilityBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;

/**
 * Parses rules from XML.
 *
 * @see RuleBaseValues
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleXmlParser {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleXmlParser.class);

    /**
     * Priority to use if rule responsibility omits priority
     */
    private static final int DEFAULT_RULE_PRIORITY = 1;
    /**
     * Value of Force Action flag if omitted; default to false, we will NOT force action for approvals
     */
    private static final boolean DEFAULT_FORCE_ACTION = false;
    /**
     * Default approve policy, if omitted; defaults to FIRST_APPROVE, the request will be satisfied by the first approval
     */
    private static final String DEFAULT_APPROVE_POLICY = ActionRequestPolicy.FIRST.getCode();
    /**
     * Default action requested, if omitted; defaults to "A"pprove
     */
    private static final String DEFAULT_ACTION_REQUESTED = KewApiConstants.ACTION_REQUEST_APPROVE_REQ;

    public List<RuleDelegationBo> parseRuleDelegations(InputStream input) throws IOException, XmlException {
    	try {
            Document doc = XmlHelper.trimSAXXml(input);
            Element root = doc.getRootElement();
            return parseRuleDelegations(root);
        } catch (JDOMException e) {
            throw new XmlException("Parse error.", e);
        } catch (SAXException e){
            throw new XmlException("Parse error.",e);
        } catch(ParserConfigurationException e){
            throw new XmlException("Parse error.",e);
        }
    }
    
    public List<RuleBaseValues> parseRules(InputStream input) throws IOException, XmlException {
        try {
            Document doc = XmlHelper.trimSAXXml(input);
            Element root = doc.getRootElement();
            return parseRules(root);
        } catch (JDOMException e) {
            throw new XmlException("Parse error.", e);
        } catch (SAXException e){
            throw new XmlException("Parse error.",e);
        } catch(ParserConfigurationException e){
            throw new XmlException("Parse error.",e);
        }
    }

    /**
     * Parses and saves rules
     * @param element top-level 'data' element which should contain a <rules> child element
     * @throws XmlException
     */
    public List<RuleBaseValues> parseRules(Element element) throws XmlException {
    	List<RuleBaseValues> rulesToSave = new ArrayList<RuleBaseValues>();
        for (Element rulesElement: (List<Element>) element.getChildren(RULES, RULE_NAMESPACE)) {
            for (Element ruleElement: (List<Element>) rulesElement.getChildren(RULE, RULE_NAMESPACE)) {
                RuleBaseValues rule = parseRule(ruleElement);
                rulesToSave.add(rule);
            }
        }
        checkForDuplicateRules(rulesToSave);
        return KEWServiceLocator.getRuleService().saveRules(rulesToSave, false);
    }
    
    /**
     * Parses and saves rule delegations
     * @param element top-level 'data' element which should contain a <rules> child element
     * @throws XmlException
     */
    public List<RuleDelegationBo> parseRuleDelegations(Element element) throws XmlException {
    	List<RuleDelegationBo> ruleDelegationsToSave = new ArrayList<RuleDelegationBo>();
        for (Element ruleDelegationsElement: (List<Element>) element.getChildren(RULE_DELEGATIONS, RULE_NAMESPACE)) {
            for (Element ruleDelegationElement: (List<Element>) ruleDelegationsElement.getChildren(RULE_DELEGATION, RULE_NAMESPACE)) {
                RuleDelegationBo ruleDelegation = parseRuleDelegation(ruleDelegationElement);
                ruleDelegationsToSave.add(ruleDelegation);
            }
        }
        //checkForDuplicateRuleDelegations(ruleDelegationsToSave);
        return KEWServiceLocator.getRuleService().saveRuleDelegations(ruleDelegationsToSave, false);
    }
    
    /**
     * Checks for rules in the List that duplicate other Rules already in the system 
     */
    private void checkForDuplicateRules(List<RuleBaseValues> rules) throws XmlException {
    	for (RuleBaseValues rule : rules) {
    		if (StringUtils.isBlank(rule.getName())) {
    			LOG.debug("Checking for rule duplication on an anonymous rule.");
    			checkRuleForDuplicate(rule);
    		}
    	}
    }
    
    /**
     * Checks for rule delegations in the List that duplicate other Rules already in the system 
     */
    private void checkForDuplicateRuleDelegations(List<RuleDelegationBo> ruleDelegations) throws XmlException {
    	for (RuleDelegationBo ruleDelegation : ruleDelegations) {
    		if (StringUtils.isBlank(ruleDelegation.getDelegationRule().getName())) {
    			LOG.debug("Checking for rule duplication on an anonymous rule delegation.");
    			checkRuleDelegationForDuplicate(ruleDelegation);
    		}
    	}
    }

    private RuleDelegationBo parseRuleDelegation(Element element) throws XmlException {
    	RuleDelegationBo ruleDelegation = new RuleDelegationBo();
    	Element parentResponsibilityElement = element.getChild(PARENT_RESPONSIBILITY, element.getNamespace());
    	if (parentResponsibilityElement == null) {
    		throw new XmlException("parent responsibility was not defined");
    	}
    	String parentResponsibilityId = parseParentResponsibilityId(parentResponsibilityElement);
    	String delegationType = element.getChildText(DELEGATION_TYPE, element.getNamespace());
        if (delegationType == null || DelegationType.parseCode(delegationType) == null) {
            throw new XmlException("Invalid delegation type specified for delegate rule '" + delegationType + "'");
        }
        
        ruleDelegation.setResponsibilityId(parentResponsibilityId);
        ruleDelegation.setDelegationType(DelegationType.fromCode(delegationType));
        
        Element ruleElement = element.getChild(RULE, element.getNamespace());
        RuleBaseValues rule = parseRule(ruleElement);
        rule.setDelegateRule(true);
        ruleDelegation.setDelegationRule(rule);
    	
    	return ruleDelegation;
    }
    
    private String parseParentResponsibilityId(Element element) throws XmlException {
    	String responsibilityId = element.getChildText(RESPONSIBILITY_ID, element.getNamespace());
    	if (!StringUtils.isBlank(responsibilityId)) {
    		return responsibilityId;
    	}
    	String parentRuleName = element.getChildText(PARENT_RULE_NAME, element.getNamespace());
    	if (StringUtils.isBlank(parentRuleName)) {
    		throw new XmlException("One of responsibilityId or parentRuleName needs to be defined");
    	}
    	RuleBaseValues parentRule = KEWServiceLocator.getRuleService().getRuleByName(parentRuleName);
    	if (parentRule == null) {
    		throw new XmlException("Could find the parent rule with name '" + parentRuleName + "'");
    	}
    	RuleResponsibilityBo ruleResponsibilityNameAndType = CommonXmlParser.parseResponsibilityNameAndType(element);
    	if (ruleResponsibilityNameAndType == null) {
    		throw new XmlException("Could not locate a valid responsibility declaration for the parent responsibility.");
    	}
    	String parentResponsibilityId = KEWServiceLocator.getRuleService().findResponsibilityIdForRule(parentRuleName, 
    			ruleResponsibilityNameAndType.getRuleResponsibilityName(),
    			ruleResponsibilityNameAndType.getRuleResponsibilityType());
    	if (parentResponsibilityId == null) {
    		throw new XmlException("Failed to locate parent responsibility for rule with name '" + parentRuleName + "' and responsibility " + ruleResponsibilityNameAndType);
    	}
    	return parentResponsibilityId;
    }
    
    /**
     * Parses, and only parses, a rule definition (be it a top-level rule, or a rule delegation).  This method will
     * NOT dirty or save any existing data, it is side-effect-free.
     * @param element the rule element
     * @return a new RuleBaseValues object which is not yet saved
     * @throws XmlException
     */
    private RuleBaseValues parseRule(Element element) throws XmlException {
        String name = element.getChildText(NAME, element.getNamespace());
        RuleBaseValues rule = createRule(name);
        
        setDefaultRuleValues(rule);
        rule.setName(name);
        
        String toDatestr = element.getChildText( TO_DATE, element.getNamespace());
        String fromDatestr = element.getChildText( FROM_DATE, element.getNamespace());
        rule.setToDateValue(formatDate("toDate", toDatestr));
        rule.setFromDateValue(formatDate("fromDate", fromDatestr));

        String description = element.getChildText(DESCRIPTION, element.getNamespace());
        if (StringUtils.isBlank(description)) {
            throw new XmlException("Rule must have a description.");
        }
                
        String documentTypeName = element.getChildText(DOCUMENT_TYPE, element.getNamespace());
        if (StringUtils.isBlank(documentTypeName)) {
        	throw new XmlException("Rule must have a document type.");
        }
        DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        if (documentType == null) {
        	throw new XmlException("Could not locate document type '" + documentTypeName + "'");
        }

        RuleTemplateBo ruleTemplate = null;
        String ruleTemplateName = element.getChildText(RULE_TEMPLATE, element.getNamespace());        
        Element ruleExtensionsElement = element.getChild(RULE_EXTENSIONS, element.getNamespace());
        if (!StringUtils.isBlank(ruleTemplateName)) {
        	ruleTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(ruleTemplateName);
        	if (ruleTemplate == null) {
        		throw new XmlException("Could not locate rule template '" + ruleTemplateName + "'");
        	}
        } else {
        	if (ruleExtensionsElement != null) {
        		throw new XmlException("Templateless rules may not have rule extensions");
        	}
        }

        RuleExpressionDef ruleExpressionDef = null;
        Element exprElement = element.getChild(RULE_EXPRESSION, element.getNamespace());
        if (exprElement != null) {
        	String exprType = exprElement.getAttributeValue("type");
        	if (StringUtils.isEmpty(exprType)) {
        		throw new XmlException("Expression type must be specified");
        	}
        	String expression = exprElement.getTextTrim();
        	ruleExpressionDef = new RuleExpressionDef();
        	ruleExpressionDef.setType(exprType);
        	ruleExpressionDef.setExpression(expression);
        }
        
        String forceActionValue = element.getChildText(FORCE_ACTION, element.getNamespace());
        Boolean forceAction = Boolean.valueOf(DEFAULT_FORCE_ACTION);
        if (!StringUtils.isBlank(forceActionValue)) {
            forceAction = Boolean.valueOf(forceActionValue);
        }

        rule.setDocTypeName(documentType.getName());
        if (ruleTemplate != null) {
            rule.setRuleTemplateId(ruleTemplate.getId());
            rule.setRuleTemplate(ruleTemplate);
        }
        if (ruleExpressionDef != null) {
            rule.setRuleExpressionDef(ruleExpressionDef);
        }
        rule.setDescription(description);
        rule.setForceAction(forceAction);

        Element responsibilitiesElement = element.getChild(RESPONSIBILITIES, element.getNamespace());
        rule.setRuleResponsibilities(parseResponsibilities(responsibilitiesElement, rule));
        rule.setRuleExtensions(parseRuleExtensions(ruleExtensionsElement, rule));

        return rule;
    }
    
    /**
     * Creates the rule that the parser will populate.  If a rule with the given name
     * already exists, it's keys and responsibilities will be copied over to the
     * new rule.  The calling code will then sort through the original responsibilities
     * and compare them with those being defined on the XML being parsed.
     */
    private RuleBaseValues createRule(String ruleName) {
    	RuleBaseValues rule = new RuleBaseValues();
    	RuleBaseValues existingRule = (ruleName != null) ? KEWServiceLocator.getRuleService().getRuleByName(ruleName) : null;
    	if (existingRule != null) {
    		// copy keys and responsibiliities from the existing rule
    		rule.setId(existingRule.getId());
    		rule.setPreviousRuleId(existingRule.getPreviousRuleId());
    		rule.setPreviousVersion(existingRule.getPreviousVersion());
    		rule.setRuleResponsibilities(existingRule.getRuleResponsibilities());
    	}
    	return rule;
    }

    /**
     * Checks to see whether this anonymous rule duplicates an existing rule.
     * Currently the uniqueness is on ruleResponsibilityName, and extension key/values.
     * @param rule the rule to check
     * @throws XmlException if this incoming rule duplicates an existing rule
     */
    private void checkRuleForDuplicate(RuleBaseValues rule) throws XmlException {
    	String ruleId = KEWServiceLocator.getRuleService().getDuplicateRuleId(rule);
        if (ruleId != null) {
        	throw new XmlException("Rule '" + rule.getDescription() + "' on doc '" + rule.getDocTypeName() + "' is a duplicate of rule with rule Id " + ruleId);
        }
    }
    
    private void checkRuleDelegationForDuplicate(RuleDelegationBo ruleDelegation) throws XmlException {
    	checkRuleForDuplicate(ruleDelegation.getDelegationRule());
    }

    private void setDefaultRuleValues(RuleBaseValues rule) throws XmlException {
        rule.setForceAction(Boolean.FALSE);
        rule.setActivationDate(new Timestamp(System.currentTimeMillis()));
        rule.setActive(Boolean.TRUE);
        rule.setCurrentInd(Boolean.TRUE);
        rule.setTemplateRuleInd(Boolean.FALSE);
        rule.setVersionNbr(new Integer(0));
        rule.setDelegateRule(false);
    }

    private List<RuleResponsibilityBo> parseResponsibilities(Element element, RuleBaseValues rule) throws XmlException {
        if (element == null) {
            return new ArrayList<RuleResponsibilityBo>(0);
        }
        List<RuleResponsibilityBo> existingResponsibilities = rule.getRuleResponsibilities();
        List<RuleResponsibilityBo> responsibilities = new ArrayList<RuleResponsibilityBo>();
        List<Element> responsibilityElements = (List<Element>)element.getChildren(RESPONSIBILITY, element.getNamespace());
        for (Element responsibilityElement : responsibilityElements) {
            RuleResponsibilityBo responsibility = parseResponsibility(responsibilityElement, rule);
            reconcileWithExistingResponsibility(responsibility, existingResponsibilities);
            responsibilities.add(responsibility);
        }
        if (responsibilities.size() == 0) {
            throw new XmlException("Rule responsibility list must have at least one responsibility.");
        }
        return responsibilities;
    }

    public RuleResponsibilityBo parseResponsibility(Element element, RuleBaseValues rule) throws XmlException {
        RuleResponsibilityBo responsibility = new RuleResponsibilityBo();
        responsibility.setRuleBaseValues(rule);
        String actionRequested = null;
        String priority = null;
        actionRequested = element.getChildText(ACTION_REQUESTED, element.getNamespace());
        if (StringUtils.isBlank(actionRequested)) {
        	actionRequested = DEFAULT_ACTION_REQUESTED;
        }
        priority = element.getChildText(PRIORITY, element.getNamespace());
        if (StringUtils.isBlank(priority)) {
        	priority = String.valueOf(DEFAULT_RULE_PRIORITY);
        }
        String approvePolicy = element.getChildText(APPROVE_POLICY, element.getNamespace());
        Element delegations = element.getChild(DELEGATIONS, element.getNamespace());
        if (actionRequested == null) {
            throw new XmlException("actionRequested is required on responsibility");
        }
        if (!actionRequested.equals(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ) && !actionRequested.equals(KewApiConstants.ACTION_REQUEST_APPROVE_REQ) && !actionRequested.equals(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ) && !actionRequested.equals(KewApiConstants.ACTION_REQUEST_FYI_REQ)) {
            throw new XmlException("Invalid action requested code '" + actionRequested + "'");
        }
        if (StringUtils.isBlank(approvePolicy)) {
            approvePolicy = DEFAULT_APPROVE_POLICY;
        }
        if (!approvePolicy.equals(ActionRequestPolicy.ALL.getCode()) && !approvePolicy.equals(ActionRequestPolicy.FIRST.getCode())) {
            throw new XmlException("Invalid approve policy '" + approvePolicy + "'");
        }
        Integer priorityNumber = Integer.valueOf(priority);
        responsibility.setActionRequestedCd(actionRequested);
        responsibility.setPriority(priorityNumber);
        responsibility.setApprovePolicy(approvePolicy);
        
        RuleResponsibilityBo responsibilityNameAndType = CommonXmlParser.parseResponsibilityNameAndType(element);
        if (responsibilityNameAndType == null) {
        	throw new XmlException("Could not locate a valid responsibility declaration on a responsibility on rule with description '" + rule.getDescription() + "'");
        }
        if (responsibilityNameAndType.getRuleResponsibilityType().equals(KewApiConstants.RULE_RESPONSIBILITY_GROUP_ID)
        		&& responsibility.getApprovePolicy().equals(ActionRequestPolicy.ALL.getCode())) {
        	throw new XmlException("Invalid approve policy '" + approvePolicy + "'.  This policy is not supported with Groups.");
        }
        responsibility.setRuleResponsibilityName(responsibilityNameAndType.getRuleResponsibilityName());
        responsibility.setRuleResponsibilityType(responsibilityNameAndType.getRuleResponsibilityType());
        
        return responsibility;
    }
    
    /**
     * Attempts to reconcile the given RuleResponsibility with the list of existing responsibilities (in the case of a
     * rule being updated via the XML).  This goal of this code is to copy responsibility ids from existing responsibilities
     * to the new responsibility where appropriate.  The code will attempt to find exact matches based on the values found
     * on the responsibilities.
     */
    private void reconcileWithExistingResponsibility(RuleResponsibilityBo responsibility, List<RuleResponsibilityBo> existingResponsibilities) {
    	if (existingResponsibilities == null || existingResponsibilities.isEmpty()) {
    		return;
    	}
    	RuleResponsibilityBo exactMatch = null;
    	for (RuleResponsibilityBo existingResponsibility : existingResponsibilities) {
    		if (isExactResponsibilityMatch(responsibility, existingResponsibility)) {
    			exactMatch = existingResponsibility;
    			break;
    		}
    	}
    	if (exactMatch != null) {
    		responsibility.setResponsibilityId(exactMatch.getResponsibilityId());
    	}
    }
    
    /**
     * Checks if the given responsibilities are exact matches of one another.
     */
    private boolean isExactResponsibilityMatch(RuleResponsibilityBo newResponsibility, RuleResponsibilityBo existingResponsibility) {
    	if (existingResponsibility.getResponsibilityId().equals(newResponsibility.getResponsibilityId())) {
    		return true;
    	}
    	if (existingResponsibility.getRuleResponsibilityName().equals(newResponsibility.getRuleResponsibilityName()) &&
    			existingResponsibility.getRuleResponsibilityType().equals(newResponsibility.getRuleResponsibilityType()) &&
    			existingResponsibility.getApprovePolicy().equals(newResponsibility.getApprovePolicy()) &&
    			existingResponsibility.getActionRequestedCd().equals(newResponsibility.getActionRequestedCd()) &&
    			existingResponsibility.getPriority().equals(newResponsibility.getPriority())) {
    		return true;
    	}
    	return false;
    }

    private List parseRuleExtensions(Element element, RuleBaseValues rule) throws XmlException {
        if (element == null) {
            return new ArrayList();
        }
        RuleExtensionXmlParser parser = new RuleExtensionXmlParser();
        return parser.parseRuleExtensions(element, rule);
    }
    
    public Timestamp formatDate(String dateLabel, String dateString) throws XmlException {
    	if (StringUtils.isBlank(dateString)) {
    		return null;
    	}
    	try {
    		return new Timestamp(RiceConstants.getDefaultDateFormat().parse(dateString).getTime());
    	} catch (ParseException e) {
    		throw new XmlException(dateLabel + " is not in the proper format.  Should have been: " + RiceConstants.DEFAULT_DATE_FORMAT_PATTERN);
    	}
    }
    
}
