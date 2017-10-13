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
package org.kuali.rice.kew.impl.extension;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.service.RuleAttributeService;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Reference implementation of the {@code ExtensionRepositoryService}.  This implementation
 * essentially sits on top of the legacy "RuleAttribute" service.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = ExtensionRepositoryServiceImpl.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ExtensionRepositoryServiceImpl.Constants.TYPE_NAME, propOrder = {
    ExtensionRepositoryServiceImpl.Elements.RULE_ATTRIBUTE_SERVICE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class ExtensionRepositoryServiceImpl implements ExtensionRepositoryService {

    /**
     * Private constructor used only by JAXB.
     *
     */
    private ExtensionRepositoryServiceImpl() {
        this.ruleAttributeService = null;
    }

    @XmlElement(name = Elements.RULE_ATTRIBUTE_SERVICE, required = true)
    private RuleAttributeService ruleAttributeService;


    @Override
    public ExtensionDefinition getExtensionById(String id) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(id)) {
            throw new RiceIllegalArgumentException("id was null or blank");
        }
        RuleAttribute ruleAttribute = ruleAttributeService.findByRuleAttributeId(id);
        return translateFromRuleAttribute(ruleAttribute);
    }


    @Override
    public ExtensionDefinition getExtensionByName(String name) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name was null or blank");
        }
        RuleAttribute ruleAttribute = ruleAttributeService.findByName(name);
        return translateFromRuleAttribute(ruleAttribute);
    }

    @Override
    public List<ExtensionDefinition> getExtensionsByResourceDescriptor(
            String resourceDescriptor) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(resourceDescriptor)) {
            throw new RiceIllegalArgumentException("resourceDescriptor was null or blank");
        }
        List<RuleAttribute> ruleAttributes = ruleAttributeService.findByClassName(resourceDescriptor);
        if (CollectionUtils.isNotEmpty(ruleAttributes)) {
            List<ExtensionDefinition> definitions = new ArrayList<ExtensionDefinition>();
            for (RuleAttribute ruleAttribute : ruleAttributes) {
                definitions.add(translateFromRuleAttribute(ruleAttribute));
            }
            return Collections.unmodifiableList(definitions);
        }
        
        return Collections.emptyList();
    }

    private ExtensionDefinition translateFromRuleAttribute(RuleAttribute ruleAttribute) {
        return RuleAttribute.to(ruleAttribute);
    }

    /**
         * Sets the rule attribute service.
         * @param ruleAttributeService the rule attribute service to set.
         */
    public void setRuleAttributeService(RuleAttributeService ruleAttributeService) {
        this.ruleAttributeService = ruleAttributeService;
    }

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {
        static final String ROOT_ELEMENT_NAME = "extensionRepositoryService";
        static final String TYPE_NAME = "ExtensionRepositoryService";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {
        static final String RULE_ATTRIBUTE_SERVICE = "ruleAttributeService";
    }
}
