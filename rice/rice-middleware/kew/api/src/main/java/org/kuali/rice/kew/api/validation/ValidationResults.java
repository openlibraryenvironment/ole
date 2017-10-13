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

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.rule.RuleContract;
import org.kuali.rice.kew.api.rule.RuleDelegationContract;
import org.kuali.rice.kew.api.rule.RuleResponsibility;
import org.kuali.rice.kew.api.rule.RuleResponsibilityContract;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of results from validation of a field of data.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = ValidationResults.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ValidationResults.Constants.TYPE_NAME, propOrder = {
    ValidationResults.Elements.ERRORS,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class ValidationResults
    extends AbstractDataTransferObject
    implements ValidationResultsContract {

	public static final String GLOBAL = "org.kuali.rice.kew.api.validation.ValidationResults.GLOBAL";

    @XmlElement(name = Elements.ERRORS, required = false)
    @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
    private final Map<String, String> errors;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    private ValidationResults() {
        this.errors = Collections.emptyMap();
    }

    private ValidationResults(Builder builder) {
       this.errors = Collections.unmodifiableMap(builder.getErrors());
    }

    @Override
	public Map<String, String> getErrors() {
		return errors;
	}

    /**
     * A builder which can be used to construct {@link ValidationResults} instances.  Enforces the constraints of the {@link ValidationResultsContract}.
     *
     */
    public final static class Builder
        implements Serializable, ModelBuilder, ValidationResultsContract
    {

        private Map<String, String> errors = new HashMap<String, String>();

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(ValidationResultsContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getErrors() != null) {
                builder.setErrors(contract.getErrors());
            }
            return builder;
        }

        public ValidationResults build() {
            return new ValidationResults(this);
        }

        @Override
        public Map<String, String> getErrors() {
            return Collections.unmodifiableMap(this.errors);
        }

        public void setErrors(Map<String, String> errors) {
            this.errors = new HashMap<String, String>(errors);
        }

        /**
         * Convenience method for adding an error message
         * @param errorMessage
         */
        public void addError(String errorMessage) {
            addError(GLOBAL, errorMessage);
        }

        /**
         * Convenience method for adding an error message for a given field
         * @param errorMessage
         */
        public void addError(String fieldName, String errorMessage) {
            errors.put(fieldName, errorMessage);
        }
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "validationResults";
        final static String TYPE_NAME = "ValidationResultsType";
    }
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String ERRORS = "errors";
    }
}