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

import org.jdom.Element;
import org.jdom.Namespace;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.RuleExtensionBo;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Parses {@link org.kuali.rice.kew.rule.RuleExtensionBo}s from XML.
 *
 * @see org.kuali.rice.kew.rule.RuleExtensionBo
 * @see RuleExtensionValue
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleExtensionXmlParser {

    private static final Namespace NAMESPACE = Namespace.getNamespace("", "ns:workflow/Rule");
    private static final String RULE_EXTENSION = "ruleExtension";
    private static final String ATTRIBUTE = "attribute";
    private static final String RULE_TEMPLATE = "ruleTemplate";
    private static final String RULE_EXTENSION_VALUES = "ruleExtensionValues";
    private static final String RULE_EXTENSION_VALUE = "ruleExtensionValue";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public List parseRuleExtensions(Element element, RuleBaseValues rule) throws XmlException {
	List ruleExtensions = new ArrayList();
	Collection<Element> ruleElements = XmlHelper.findElements(element, RULE_EXTENSION);
	for (Iterator iterator = ruleElements.iterator(); iterator.hasNext();) {
	    ruleExtensions.add(parseRuleExtension((Element) iterator.next(), rule));
	}
	return ruleExtensions;
    }

    private RuleExtensionBo parseRuleExtension(Element element, RuleBaseValues rule) throws XmlException {
	String attributeName = element.getChildText(ATTRIBUTE, NAMESPACE);
	String templateName = element.getChildText(RULE_TEMPLATE, NAMESPACE);
	Element valuesElement = element.getChild(RULE_EXTENSION_VALUES, NAMESPACE);
	if (attributeName == null) {
	    throw new XmlException("Rule extension must have a valid attribute.");
	}
	if (templateName == null) {
	    throw new XmlException("Rule extension must have a valid rule template.");
	}
	RuleAttribute ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByName(attributeName);
	if (ruleAttribute == null) {
	    throw new XmlException("Could not locate attribute for the given name '" + attributeName + "'");
	}
	RuleTemplateBo ruleTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(templateName);
	if (ruleTemplate == null) {
	    throw new XmlException("Could not locate rule template for the given name '" + templateName + "'");
	}
	RuleExtensionBo extension = new RuleExtensionBo();
	extension.setRuleBaseValues(rule);
	boolean attributeFound = false;
	for (Iterator iter = ruleTemplate.getActiveRuleTemplateAttributes().iterator(); iter.hasNext();) {
	    RuleTemplateAttributeBo templateAttribute = (RuleTemplateAttributeBo) iter.next();
	    if (templateAttribute.getRuleAttributeId().equals(ruleAttribute.getId())) {
		extension.setRuleTemplateAttribute(templateAttribute);
		extension.setRuleTemplateAttributeId(templateAttribute.getId());
		attributeFound = true;
		break;
	    }
	}

	if (!attributeFound) {
	    // TODO: need test case for this
	    throw new XmlException("Attribute '" + attributeName + "' not found on template '" + ruleTemplate.getName() + "'");
	}

	extension.setExtensionValues(parseRuleExtensionValues(valuesElement, extension));
	return extension;
    }

    private List parseRuleExtensionValues(Element element, RuleExtensionBo ruleExtension) throws XmlException {
	List values = new ArrayList();
	if (element == null) {
	    return values;
	}
	Collection<Element> valueElements = XmlHelper.findElements(element, RULE_EXTENSION_VALUE);
	for (Iterator iterator = valueElements.iterator(); iterator.hasNext();) {
	    Element valueElement = (Element) iterator.next();
	    values.add(parseRuleExtensionValue(valueElement, ruleExtension));
	}
	return values;
    }

    private RuleExtensionValue parseRuleExtensionValue(Element element, RuleExtensionBo ruleExtension) throws XmlException {
	String key = element.getChildText(KEY, NAMESPACE);
	String value = element.getChildText(VALUE, NAMESPACE);
	if (org.apache.commons.lang.StringUtils.isEmpty(key)) {
	    throw new XmlException("RuleExtensionValue must have a non-empty key.");
	}
	if (value == null) {
	    throw new XmlException("RuleExtensionValue must have a non-null value.");
	}
	RuleExtensionValue extensionValue = new RuleExtensionValue(key, value);
	extensionValue.setExtension(ruleExtension);
	return extensionValue;
    }

}
