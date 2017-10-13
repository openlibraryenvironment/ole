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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

@XmlRootElement(name = RuleTemplateOption.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleTemplateOption.Constants.TYPE_NAME, propOrder = {
    RuleTemplateOption.Elements.VALUE,
    RuleTemplateOption.Elements.RULE_TEMPLATE_ID,
    RuleTemplateOption.Elements.CODE,
    RuleTemplateOption.Elements.ID,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class RuleTemplateOption
    extends AbstractDataTransferObject
    implements RuleTemplateOptionContract
{

    @XmlElement(name = Elements.VALUE, required = false)
    private final String value;
    @XmlElement(name = Elements.RULE_TEMPLATE_ID, required = false)
    private final String ruleTemplateId;
    @XmlElement(name = Elements.CODE, required = false)
    private final String code;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private RuleTemplateOption() {
        this.value = null;
        this.ruleTemplateId = null;
        this.code = null;
        this.id = null;
        this.versionNumber = null;
    }

    private RuleTemplateOption(Builder builder) {
        this.value = builder.getValue();
        this.ruleTemplateId = builder.getRuleTemplateId();
        this.code = builder.getCode();
        this.id = builder.getId();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getRuleTemplateId() {
        return this.ruleTemplateId;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }


    /**
     * A builder which can be used to construct {@link RuleTemplateOption} instances.  Enforces the constraints of the {@link RuleTemplateOptionContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleTemplateOptionContract
    {

        private String value;
        private String ruleTemplateId;
        private String code;
        private String id;
        private Long versionNumber;

        private Builder() {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
        }

        public static Builder create() {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder();
        }

        public static Builder create(RuleTemplateOptionContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create();
            builder.setValue(contract.getValue());
            builder.setRuleTemplateId(contract.getRuleTemplateId());
            builder.setCode(contract.getCode());
            builder.setId(contract.getId());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        public RuleTemplateOption build() {
            return new RuleTemplateOption(this);
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getRuleTemplateId() {
            return this.ruleTemplateId;
        }

        @Override
        public String getCode() {
            return this.code;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        public void setValue(String value) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.value = value;
        }

        public void setRuleTemplateId(String ruleTemplateId) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.ruleTemplateId = ruleTemplateId;
        }

        public void setCode(String code) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.code = code;
        }

        public void setId(String id) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.id = id;
        }

        public void setVersionNumber(Long versionNumber) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.versionNumber = versionNumber;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "ruleTemplateOption";
        final static String TYPE_NAME = "RuleTemplateOptionType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String VALUE = "value";
        final static String RULE_TEMPLATE_ID = "ruleTemplateId";
        final static String CODE = "code";
        final static String ID = "id";

    }

}