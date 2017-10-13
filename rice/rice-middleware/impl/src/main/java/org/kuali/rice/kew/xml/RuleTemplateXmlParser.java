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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleDelegationBo;
import org.kuali.rice.kew.rule.RuleTemplateOptionBo;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;
/**
 * Parses {@link org.kuali.rice.kew.rule.bo.RuleTemplateBo}s from XML.
 *
 * @see org.kuali.rice.kew.rule.bo.RuleTemplateBo
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleTemplateXmlParser {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RuleTemplateXmlParser.class);

    /**
     * By default make attributes defined without a &lt;required&gt; element
     */
    private static final boolean DEFAULT_ATTRIBUTE_REQUIRED = true;
    private static final boolean DEFAULT_ATTRIBUTE_ACTIVE = true;

    /**
     * A dummy document type used in the default rule
     */
    private static final String DUMMY_DOCUMENT_TYPE = "dummyDocumentType";

    /**
     * Used to set the display order of attributes encountered in parsing runs during the lifetime of this object
     */
    private int templateAttributeCounter = 0;

    public List<RuleTemplateBo> parseRuleTemplates(InputStream input) throws IOException, XmlException {

        try {
            Document doc = XmlHelper.trimSAXXml(input);
            Element root = doc.getRootElement();
            return parseRuleTemplates(root);
        } catch (JDOMException e) {
            throw new XmlException("Parse error.", e);
        } catch (SAXException e) {
            throw new XmlException("Parse error.", e);
        } catch (ParserConfigurationException e) {
            throw new XmlException("Parse error.", e);
        }
    }

    public List<RuleTemplateBo> parseRuleTemplates(Element element) throws XmlException {
        List<RuleTemplateBo> ruleTemplates = new ArrayList<RuleTemplateBo>();

        // iterate over any RULE_TEMPLATES elements
        Collection<Element> ruleTemplatesElements = XmlHelper.findElements(element, RULE_TEMPLATES);
        Iterator ruleTemplatesIterator = ruleTemplatesElements.iterator();
        while (ruleTemplatesIterator.hasNext()) {
            Element ruleTemplatesElement = (Element) ruleTemplatesIterator.next();
            Collection<Element> ruleTemplateElements = XmlHelper.findElements(ruleTemplatesElement, RULE_TEMPLATE);
            for (Iterator iterator = ruleTemplateElements.iterator(); iterator.hasNext();) {
                ruleTemplates.add(parseRuleTemplate((Element) iterator.next(), ruleTemplates));
            }
        }
        return ruleTemplates;
    }

    private RuleTemplateBo parseRuleTemplate(Element element, List<RuleTemplateBo> ruleTemplates) throws XmlException {
        String name = element.getChildText(NAME, RULE_TEMPLATE_NAMESPACE);
        String description = element.getChildText(DESCRIPTION, RULE_TEMPLATE_NAMESPACE);
        Attribute allowOverwriteAttrib = element.getAttribute("allowOverwrite");

        boolean allowOverwrite = false;
        if (allowOverwriteAttrib != null) {
            allowOverwrite = Boolean.valueOf(allowOverwriteAttrib.getValue()).booleanValue();
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
            throw new XmlException("RuleTemplate must have a name");
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(description)) {
            throw new XmlException("RuleTemplate must have a description");
        }

        // look up the rule template by name first
        RuleTemplateBo ruleTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(name);

        if (ruleTemplate == null) {
            // if it does not exist create a new one
            ruleTemplate = new RuleTemplateBo();
        } else {
            // if it does exist, update it, only if allowOverwrite is set
            if (!allowOverwrite) {
                throw new RuntimeException("Attempting to overwrite template " + name + " without allowOverwrite set");
            }

            // the name should be equal if one was actually found
            assert(name.equals(ruleTemplate.getName())) : "Existing template definition name does not match incoming definition name";
        } 

        // overwrite simple properties
        ruleTemplate.setName(name);
        ruleTemplate.setDescription(description);

        // update the delegation template
        updateDelegationTemplate(element, ruleTemplate, ruleTemplates);

        // update the attribute relationships
        updateRuleTemplateAttributes(element, ruleTemplate);

        // save the rule template first so that the default/template rule that is generated
        // in the process of setting defaults is associated properly with this rule template
        KEWServiceLocator.getRuleTemplateService().save(ruleTemplate);

        // update the default options
        updateRuleTemplateDefaultOptions(element, ruleTemplate);

        KEWServiceLocator.getRuleTemplateService().save(ruleTemplate);

        return ruleTemplate;
    }

    /**
     * Updates the rule template default options.  Updates any existing options, removes any omitted ones.
     * @param ruleTemplateElement the rule template XML element
     * @param updatedRuleTemplate the RuleTemplate being updated
     * @throws XmlException
     */
    /*
     <element name="description" type="c:LongStringType"/>
     <element name="fromDate" type="c:ShortStringType" minOccurs="0"/>
     <element name="toDate" type="c:ShortStringType" minOccurs="0"/>
     <element name="forceAction" type="boolean"/>
     <element name="active" type="boolean"/>
     <element name="defaultActionRequested" type="c:ShortStringType"/>
     <element name="supportsComplete" type="boolean" default="true"/>
     <element name="supportsApprove" type="boolean" default="true"/>
     <element name="supportsAcknowledge" type="boolean" default="true"/>
     <element name="supportsFYI" type="boolean" default="true"/>
    */
    protected void updateRuleTemplateDefaultOptions(Element ruleTemplateElement, RuleTemplateBo updatedRuleTemplate) throws XmlException {
        Element defaultsElement = ruleTemplateElement.getChild(RULE_DEFAULTS, RULE_TEMPLATE_NAMESPACE);

        // update the rule defaults; this yields whether or not this is a delegation rule template
        boolean isDelegation = updateRuleDefaults(defaultsElement, updatedRuleTemplate);

        // update the rule template options
        updateRuleTemplateOptions(defaultsElement, updatedRuleTemplate, isDelegation);

    }

    /**
     * Updates the rule template defaults options with those in the defaults element
     * @param defaultsElement the ruleDefaults element
     * @param updatedRuleTemplate the Rule Template being updated
     */
    protected void updateRuleTemplateOptions(Element defaultsElement, RuleTemplateBo updatedRuleTemplate, boolean isDelegation) throws XmlException {
        // the possible defaults options
        // NOTE: the current implementation will remove any existing RuleTemplateOption records for any values which are null, i.e. not set in the incoming XML.
        // to pro-actively set default values for omitted options, simply set those values here, and records will be added if not present
        String defaultActionRequested = null;
        Boolean supportsComplete = null;
        Boolean supportsApprove = null;
        Boolean supportsAcknowledge = null;
        Boolean supportsFYI = null;
        
        // remove any RuleTemplateOptions the template may have but that we know we aren't going to update/reset
        // (not sure if this case even exists...does anything else set rule template options?)
        updatedRuleTemplate.removeNonDefaultOptions();
        
        // read in new settings
        if (defaultsElement != null) {

        	defaultActionRequested = defaultsElement.getChildText(DEFAULT_ACTION_REQUESTED, RULE_TEMPLATE_NAMESPACE);
            supportsComplete = BooleanUtils.toBooleanObject(defaultsElement.getChildText(SUPPORTS_COMPLETE, RULE_TEMPLATE_NAMESPACE));
            supportsApprove = BooleanUtils.toBooleanObject(defaultsElement.getChildText(SUPPORTS_APPROVE, RULE_TEMPLATE_NAMESPACE));
            supportsAcknowledge = BooleanUtils.toBooleanObject(defaultsElement.getChildText(SUPPORTS_ACKNOWLEDGE, RULE_TEMPLATE_NAMESPACE));
            supportsFYI = BooleanUtils.toBooleanObject(defaultsElement.getChildText(SUPPORTS_FYI, RULE_TEMPLATE_NAMESPACE));
        }

        if (!isDelegation) {
            // if this is not a delegation template, store the template options that govern rule action constraints
            // in the RuleTemplateOptions of the template
            // we have two options for this behavior:
            // 1) conditionally parse above, and then unconditionally set/unset the properties; this will have the effect of REMOVING
            //    any of these previously specified rule template options (and is arguably the right thing to do)
            // 2) unconditionally parse above, and then conditionally set/unset the properties; this will have the effect of PRESERVING
            //    the existing rule template options on this template if it is a delegation template (which of course will be overwritten
            //    by this very same code if they subsequently upload without the delegation flag)
            // This is a minor point, but the second implementation is chosen as it preserved the current behavior
            updateOrDeleteRuleTemplateOption(updatedRuleTemplate, KewApiConstants.ACTION_REQUEST_DEFAULT_CD, defaultActionRequested);
            updateOrDeleteRuleTemplateOption(updatedRuleTemplate, KewApiConstants.ACTION_REQUEST_APPROVE_REQ, supportsApprove);
            updateOrDeleteRuleTemplateOption(updatedRuleTemplate, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, supportsAcknowledge);
            updateOrDeleteRuleTemplateOption(updatedRuleTemplate, KewApiConstants.ACTION_REQUEST_FYI_REQ, supportsFYI);
            updateOrDeleteRuleTemplateOption(updatedRuleTemplate, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, supportsComplete);
        }

    }
    
    /**
     * 
     * Updates the default/template rule options with those in the defaults element
     * @param defaultsElement the ruleDefaults element
     * @param updatedRuleTemplate the Rule Template being updated
     * @return whether this is a delegation rule template
     */
    protected boolean updateRuleDefaults(Element defaultsElement, RuleTemplateBo updatedRuleTemplate) throws XmlException {
        // NOTE: implementation detail: in contrast with the other options, the delegate template, and the rule attributes,
        // we unconditionally blow away the default rule and re-create it (we don't update the existing one, if there is one)
        if (updatedRuleTemplate.getId() != null) {
            RuleBaseValues ruleDefaults = KEWServiceLocator.getRuleService().findDefaultRuleByRuleTemplateId(updatedRuleTemplate.getId());
            if (ruleDefaults != null) {
                List ruleDelegationDefaults = KEWServiceLocator.getRuleDelegationService().findByDelegateRuleId(ruleDefaults.getId());
                // delete the rule
                KEWServiceLocator.getRuleService().delete(ruleDefaults.getId());
                // delete the associated rule delegation defaults
                for (Iterator iterator = ruleDelegationDefaults.iterator(); iterator.hasNext();) {
                    RuleDelegationBo ruleDelegation = (RuleDelegationBo) iterator.next();
                    KEWServiceLocator.getRuleDelegationService().delete(ruleDelegation.getRuleDelegationId());
                }
            }
        }

        boolean isDelegation = false;

        if (defaultsElement != null) {
            String delegationTypeCode = defaultsElement.getChildText(DELEGATION_TYPE, RULE_TEMPLATE_NAMESPACE);
            DelegationType delegationType = null;
            isDelegation = !org.apache.commons.lang.StringUtils.isEmpty(delegationTypeCode);

            String description = defaultsElement.getChildText(DESCRIPTION, RULE_TEMPLATE_NAMESPACE);
            
            // would normally be validated via schema but might not be present if invoking RuleXmlParser directly
            if (description == null) {
                throw new XmlException("Description must be specified in rule defaults");
            }
            
            String fromDate = defaultsElement.getChildText(FROM_DATE, RULE_TEMPLATE_NAMESPACE);
            String toDate = defaultsElement.getChildText(TO_DATE, RULE_TEMPLATE_NAMESPACE);
            // toBooleanObject ensures that if the value is null (not set) that the Boolean object will likewise be null (will not default to a value)
            Boolean forceAction = BooleanUtils.toBooleanObject(defaultsElement.getChildText(FORCE_ACTION, RULE_TEMPLATE_NAMESPACE));
            Boolean active = BooleanUtils.toBooleanObject(defaultsElement.getChildText(ACTIVE, RULE_TEMPLATE_NAMESPACE));

            if (isDelegation) {
                delegationType = DelegationType.parseCode(delegationTypeCode);
                if (delegationType == null) {
                    throw new XmlException("Invalid delegation type '" + delegationType + "'." + "  Expected one of: "
                                            + DelegationType.PRIMARY.getCode() + "," + DelegationType.SECONDARY.getCode());
                }
            }
    
            // create our "default rule" which encapsulates the defaults for the rule
            RuleBaseValues ruleDefaults = new RuleBaseValues();
    
            // set simple values
            ruleDefaults.setRuleTemplate(updatedRuleTemplate);
            ruleDefaults.setDocTypeName(DUMMY_DOCUMENT_TYPE);
            ruleDefaults.setTemplateRuleInd(Boolean.TRUE);
            ruleDefaults.setCurrentInd(Boolean.TRUE);
            ruleDefaults.setVersionNbr(new Integer(0));
            ruleDefaults.setDescription(description);
    
            // these are non-nullable fields, so default them if they were not set in the defaults section
            ruleDefaults.setForceAction(Boolean.valueOf(BooleanUtils.isTrue(forceAction)));
            ruleDefaults.setActive(Boolean.valueOf(BooleanUtils.isTrue(active)));
        
            if (ruleDefaults.getActivationDate() == null) {
                ruleDefaults.setActivationDate(new Timestamp(System.currentTimeMillis()));
            }
    
            ruleDefaults.setFromDateValue(formatDate("fromDate", fromDate));
            ruleDefaults.setToDateValue(formatDate("toDate", toDate));
            
            // ok, if this is a "Delegate Template", then we need to set this other RuleDelegation object which contains
            // some delegation-related info
            RuleDelegationBo ruleDelegationDefaults = null;
            if (isDelegation) {
                ruleDelegationDefaults = new RuleDelegationBo();
                ruleDelegationDefaults.setDelegationRule(ruleDefaults);
                ruleDelegationDefaults.setDelegationType(delegationType);
                ruleDelegationDefaults.setResponsibilityId(KewApiConstants.ADHOC_REQUEST_RESPONSIBILITY_ID);
            }

            // explicitly save the new rule delegation defaults and default rule
            KEWServiceLocator.getRuleTemplateService().saveRuleDefaults(ruleDelegationDefaults, ruleDefaults);
        } else {
            // do nothing, rule defaults will be deleted if ruleDefaults element is omitted
        }
        
        return isDelegation;
    }


    /**
     * Updates or deletes a specified rule template option on the rule template
     * @param updatedRuleTemplate the RuleTemplate being updated
     * @param key the option key
     * @param value the option value
     */
    protected void updateOrDeleteRuleTemplateOption(RuleTemplateBo updatedRuleTemplate, String key, Object value) {
        if (value != null) {
            // if the option exists and the incoming value is non-null (it's set), update it
            RuleTemplateOptionBo option = updatedRuleTemplate.getRuleTemplateOption(key);
            if (option != null) {
                option.setValue(value.toString());
            } else {
                updatedRuleTemplate.getRuleTemplateOptions().add(new RuleTemplateOptionBo(key, value.toString()));
            }
        } else {
            // otherwise if the incoming value IS null (not set), then explicitly remove the entry (if it exists)
            Iterator<RuleTemplateOptionBo> options = updatedRuleTemplate.getRuleTemplateOptions().iterator();
            while (options.hasNext()) {
                RuleTemplateOptionBo opt = options.next();
                if (key.equals(opt.getCode())) {
                    options.remove();
                    break;
                }
            }
        }
    }

    /**
     * Updates the rule template delegation template with the one specified in the XML (if any)
     * @param ruleTemplateElement the XML ruleTemplate element
     * @param updatedRuleTemplate the rule template to update
     * @param parsedRuleTemplates the rule templates parsed in this parsing run
     * @throws XmlException if a delegation template was specified but could not be found
     */
    protected void updateDelegationTemplate(Element ruleTemplateElement, RuleTemplateBo updatedRuleTemplate, List<RuleTemplateBo> parsedRuleTemplates) throws XmlException {
        String delegateTemplateName = ruleTemplateElement.getChildText(DELEGATION_TEMPLATE, RULE_TEMPLATE_NAMESPACE);

        if (delegateTemplateName != null) {
            // if a delegateTemplate was set in the XML, then look it up and set it on the RuleTemplate object
            // first try looking up an existing delegateTemplate in the system
            RuleTemplateBo delegateTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(delegateTemplateName);

            // if not found, try the list of templates currently parsed
            if (delegateTemplate == null) {
                for (RuleTemplateBo rt: parsedRuleTemplates) {
                    if (delegateTemplateName.equalsIgnoreCase(rt.getName())) {
                        // set the expected next rule template id on the target delegateTemplate
                    	String ruleTemplateId = KEWServiceLocator.getRuleTemplateService().getNextRuleTemplateId();
                        rt.setId(ruleTemplateId);
                        delegateTemplate = rt;
                        break;
                    }
                }
            }

            if (delegateTemplate == null) {
                throw new XmlException("Cannot find delegation template " + delegateTemplateName);
            }

            updatedRuleTemplate.setDelegationTemplateId(delegateTemplate.getDelegationTemplateId());
            updatedRuleTemplate.setDelegationTemplate(delegateTemplate);           
        } else {
            // the previously referenced template is left in the system
        }
    }

    /**
     * Updates the attributes set on the RuleTemplate
     * @param ruleTemplateElement the XML ruleTemplate element
     * @param updatedRuleTemplate the RuleTemplate being updated
     * @throws XmlException if there was a problem parsing the rule template attributes
     */
    protected void updateRuleTemplateAttributes(Element ruleTemplateElement, RuleTemplateBo updatedRuleTemplate) throws XmlException {
        // add any newly defined rule template attributes to the rule template,
        // update the active and required flags of any existing ones.
        // if this is an update of an existing rule template, related attribute objects will be present in this rule template object,
        // otherwise none will be present (so they'll all be new)

        Element attributesElement = ruleTemplateElement.getChild(ATTRIBUTES, RULE_TEMPLATE_NAMESPACE);
        List<RuleTemplateAttributeBo> incomingAttributes = new ArrayList<RuleTemplateAttributeBo>();
        if (attributesElement != null) {
            incomingAttributes.addAll(parseRuleTemplateAttributes(attributesElement, updatedRuleTemplate));
        }

        // inactivate all current attributes
        for (RuleTemplateAttributeBo currentRuleTemplateAttribute: updatedRuleTemplate.getRuleTemplateAttributes()) {
            String ruleAttributeName = (currentRuleTemplateAttribute.getRuleAttribute() != null) ? currentRuleTemplateAttribute.getRuleAttribute().getName() : "(null)";
            LOG.debug("Inactivating rule template attribute with id " + currentRuleTemplateAttribute.getId() + " and rule attribute with name " + ruleAttributeName);
            currentRuleTemplateAttribute.setActive(Boolean.FALSE);
        }
        // NOTE: attributes are deactivated, not removed

        // add/update any new attributes
        for (RuleTemplateAttributeBo ruleTemplateAttribute: incomingAttributes) {
            RuleTemplateAttributeBo potentialExistingTemplateAttribute = updatedRuleTemplate.getRuleTemplateAttribute(ruleTemplateAttribute);
            if (potentialExistingTemplateAttribute != null) {
                // template attribute exists on rule template already; update the options
                potentialExistingTemplateAttribute.setActive(ruleTemplateAttribute.getActive());
                potentialExistingTemplateAttribute.setRequired(ruleTemplateAttribute.getRequired());
            } else {
                // template attribute does not yet exist on template so add it
                updatedRuleTemplate.getRuleTemplateAttributes().add(ruleTemplateAttribute);
            }
        }
    }

    /**
     * Parses the RuleTemplateAttributes defined on the rule template element
     * @param attributesElement the jdom Element object for the Rule Template attributes
     * @param ruleTemplate the RuleTemplate object
     * @return the RuleTemplateAttributes defined on the rule template element
     * @throws XmlException
     */
    private List<RuleTemplateAttributeBo> parseRuleTemplateAttributes(Element attributesElement, RuleTemplateBo ruleTemplate) throws XmlException {
        List<RuleTemplateAttributeBo> ruleTemplateAttributes = new ArrayList<RuleTemplateAttributeBo>();
        Collection<Element> attributeElements = XmlHelper.findElements(attributesElement, ATTRIBUTE);
        for (Iterator iterator = attributeElements.iterator(); iterator.hasNext();) {
            ruleTemplateAttributes.add(parseRuleTemplateAttribute((Element) iterator.next(), ruleTemplate));
        }
        return ruleTemplateAttributes;
    }

    /**
     * Parses a rule template attribute
     * @param element the attribute XML element
     * @param ruleTemplate the ruleTemplate to update
     * @return a parsed rule template attribute
     * @throws XmlException if the attribute does not exist
     */
    private RuleTemplateAttributeBo parseRuleTemplateAttribute(Element element, RuleTemplateBo ruleTemplate) throws XmlException {
        String attributeName = element.getChildText(NAME, RULE_TEMPLATE_NAMESPACE);
        String requiredValue = element.getChildText(REQUIRED, RULE_TEMPLATE_NAMESPACE);
        String activeValue = element.getChildText(ACTIVE, RULE_TEMPLATE_NAMESPACE);
        if (org.apache.commons.lang.StringUtils.isEmpty(attributeName)) {
            throw new XmlException("Attribute name must be non-empty");
        }
        boolean required = DEFAULT_ATTRIBUTE_REQUIRED;
        if (requiredValue != null) {
            required = Boolean.parseBoolean(requiredValue);
        }
        boolean active = DEFAULT_ATTRIBUTE_ACTIVE;
        if (activeValue != null) {
            active = Boolean.parseBoolean(activeValue);
        }
        RuleAttribute ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByName(attributeName);
        if (ruleAttribute == null) {
            throw new XmlException("Could not locate rule attribute for name '" + attributeName + "'");
        }
        RuleTemplateAttributeBo templateAttribute = new RuleTemplateAttributeBo();
        templateAttribute.setRuleAttribute(ruleAttribute);
        templateAttribute.setRuleAttributeId(ruleAttribute.getId());
        templateAttribute.setRuleTemplate(ruleTemplate);
        templateAttribute.setRequired(Boolean.valueOf(required));
        templateAttribute.setActive(Boolean.valueOf(active));
        templateAttribute.setDisplayOrder(new Integer(templateAttributeCounter++));
        return templateAttribute;
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
