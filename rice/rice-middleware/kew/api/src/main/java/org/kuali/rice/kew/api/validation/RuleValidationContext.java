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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.rule.Rule;
import org.kuali.rice.kew.api.rule.RuleContract;
import org.kuali.rice.kew.api.rule.RuleDelegation;
import org.kuali.rice.kew.api.rule.RuleDelegationContract;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

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
@XmlRootElement(name = RuleValidationContext.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = RuleValidationContext.Constants.TYPE_NAME, propOrder = {
    RuleValidationContext.Elements.RULE,
    RuleValidationContext.Elements.RULE_DELEGATION,
    RuleValidationContext.Elements.RULE_AUTHOR_PRINCIPAL_ID,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class RuleValidationContext
    extends AbstractDataTransferObject
    implements RuleValidationContextContract {

    @XmlElement(name = Elements.RULE, required = true)
	private final Rule rule;
    @XmlElement(name = Elements.RULE_DELEGATION, required = true)
	private final RuleDelegation ruleDelegation;
    @XmlElement(name = Elements.RULE_AUTHOR_PRINCIPAL_ID, required = false)
	private final String ruleAuthorPrincipalId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private RuleValidationContext() {
        this.rule = null;
        this.ruleDelegation = null;
        this.ruleAuthorPrincipalId = null;
    }

    private RuleValidationContext(Builder builder) {
        this.rule = builder.getRule().build();
        if (builder.getRuleDelegation() != null) {
            this.ruleDelegation = builder.getRuleDelegation().build();
        } else {
            this.ruleDelegation = null;
        }
        this.ruleAuthorPrincipalId = builder.getRuleAuthorPrincipalId();
    }

	/**
	 * Retrieve the rule which is being validated.
	 */
    @Override
	public Rule getRule() {
		return rule;
	}

	/**
	 * Retrieve the principal id of the individual entering the rule into the system.  May be null in the
	 * case of an XML rule import. 
	 */
    @Override
	public String getRuleAuthorPrincipalId() {
		return ruleAuthorPrincipalId;
	}

	/**
	 * Retrieve the RuleDelegation representing the parent of the rule being validated.  If the rule is
	 * not a delegation rule, then this will return null;
	 */
    @Override
	public RuleDelegation getRuleDelegation() {
		return ruleDelegation;
	}

    /**
     * A builder which can be used to construct {@link RuleValidationContext} instances.  Enforces the constraints of the {@link org.kuali.rice.kew.api.validation.RuleValidationContextContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, RuleValidationContextContract
    {

        private Rule.Builder rule;
	    private RuleDelegation.Builder ruleDelegation;
        private String ruleAuthorPrincipalId;

        private Builder() {
        }

        public static Builder create(RuleContract rule) {
            if (rule == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = new Builder();
            builder.setRule(Rule.Builder.create(rule));
            return builder;
        }

        public static Builder create(RuleValidationContextContract contract) {
            return Builder.create(contract.getRule(), contract.getRuleDelegation(), contract.getRuleAuthorPrincipalId());
        }

        /**
         * Construct a RuleValidationContext under which to validate a rule.  The rule must be non-null, the delegation
         * and author can be <code>null</code> given the circumstances defined in the description of this class.
         */
        public static Builder create(RuleContract rule, RuleDelegationContract ruleDelegation, String ruleAuthorPrincipalId) {
            Builder builder = Builder.create(rule);
            if (ruleDelegation != null) {
                builder.setRuleDelegation(RuleDelegation.Builder.create(ruleDelegation));
            }
            builder.setRuleAuthorPrincipalId(ruleAuthorPrincipalId);
            return builder;
        }

        public RuleValidationContext build() {
            return new RuleValidationContext(this);
        }

        @Override
        public Rule.Builder getRule() {
            return this.rule;
        }

        @Override
        public RuleDelegation.Builder getRuleDelegation() {
            return this.ruleDelegation;
        }

        @Override
        public String getRuleAuthorPrincipalId() {
            return this.ruleAuthorPrincipalId;
        }

        public void setRule(Rule.Builder rule) {
            this.rule = rule;
        }

        public void setRuleDelegation(RuleDelegation.Builder ruleDelegation) {
            this.ruleDelegation = ruleDelegation;
        }

        public void setRuleAuthorPrincipalId(String ruleAuthorPrincipalId) {
            this.ruleAuthorPrincipalId = ruleAuthorPrincipalId;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "ruleValidationContext";
        final static String TYPE_NAME = "RuleValidationContextType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String RULE = "rule";
        final static String RULE_DELEGATION = "ruleDelegation";
        final static String RULE_AUTHOR_PRINCIPAL_ID = "ruleAuthorPrincipalId";
    }
}