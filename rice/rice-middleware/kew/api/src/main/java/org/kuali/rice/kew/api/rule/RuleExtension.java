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
package org.kuali.rice.kew.api.rule;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.ObjectUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.collect.CollectionUtils;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

@XmlRootElement(name = RuleExtension.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleExtension.Constants.TYPE_NAME, propOrder = {
    RuleExtension.Elements.RULE_TEMPLATE_ATTRIBUTE,
    RuleExtension.Elements.EXTENSION_VALUES_MAP,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleExtension
    extends AbstractDataTransferObject
    implements RuleExtensionContract
{

    @XmlElement(name = Elements.RULE_TEMPLATE_ATTRIBUTE, required = true)
    private final RuleTemplateAttribute ruleTemplateAttribute;
    @XmlElement(name = Elements.EXTENSION_VALUES_MAP, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> extensionValuesMap;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RuleExtension() {
        this.ruleTemplateAttribute = null;
        this.extensionValuesMap = null;
        this.versionNumber = null;
    }

    private RuleExtension(Builder builder) {
        this.ruleTemplateAttribute = builder.getRuleTemplateAttribute().build();
        this.extensionValuesMap = builder.getExtensionValuesMap();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public RuleTemplateAttributeContract getRuleTemplateAttribute() {
        return this.ruleTemplateAttribute;
    }

    @Override
    public Map<String, String> getExtensionValuesMap() {
        return this.extensionValuesMap;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    /**
     * A builder which can be used to construct {@link RuleExtension} instances.  Enforces the constraints of the {@link RuleExtensionContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleExtensionContract
    {

        private RuleTemplateAttribute.Builder ruleTemplateAttribute;
        private Map<String, String> extensionValuesMap;
        private Long versionNumber;

        private Builder(RuleTemplateAttribute.Builder ruleTemplateAttribute) {
            setRuleTemplateAttribute(ruleTemplateAttribute);
        }

        public static Builder create(RuleTemplateAttribute.Builder ruleTemplateAttribute) {
            return new Builder(ruleTemplateAttribute);
        }

        public static Builder create(RuleExtensionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(RuleTemplateAttribute.Builder.create(contract.getRuleTemplateAttribute()));
            builder.setExtensionValuesMap(contract.getExtensionValuesMap());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        public RuleExtension build() {
            return new RuleExtension(this);
        }

        @Override
        public RuleTemplateAttribute.Builder getRuleTemplateAttribute() {
            return this.ruleTemplateAttribute;
        }

        @Override
        public Map<String, String> getExtensionValuesMap() {
            return this.extensionValuesMap;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setRuleTemplateAttribute(RuleTemplateAttribute.Builder ruleTemplateAttribute) {
            if (ruleTemplateAttribute == null) {
                throw new RiceIllegalArgumentException("ruleTemplateAttribute was null");
            }
            this.ruleTemplateAttribute = ruleTemplateAttribute;
        }

        public void setExtensionValuesMap(Map<String, String> extensionValuesMap) {
            this.extensionValuesMap = extensionValuesMap;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

    }

    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof RuleExtension)) return false;
        RuleExtension pred = (RuleExtension) o;
        return ObjectUtils.equals(ruleTemplateAttribute, pred.getRuleTemplateAttribute()) &&
               CollectionUtils.collectionsEquivalent(extensionValuesMap.entrySet(), pred.getExtensionValuesMap().entrySet());
    }

    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleExtension";
        final static String TYPE_NAME = "RuleExtensionType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String RULE_TEMPLATE_ATTRIBUTE = "ruleTemplateAttribute";
        final static String EXTENSION_VALUES_MAP = "extensionValuesMap";

    }

}