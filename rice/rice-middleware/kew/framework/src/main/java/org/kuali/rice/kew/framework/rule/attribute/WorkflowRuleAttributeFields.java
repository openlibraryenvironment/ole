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
package org.kuali.rice.kew.framework.rule.attribute;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An immutable data transfer object used to hold a list of validation errors, attribute fields, and rule extension
 * values for a WorkflowRuleAttribute.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = WorkflowRuleAttributeFields.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = WorkflowRuleAttributeFields.Constants.TYPE_NAME, propOrder = {
        WorkflowRuleAttributeFields.Elements.VALIDATION_ERRORS,
        WorkflowRuleAttributeFields.Elements.ATTRIBUTE_FIELDS,
        WorkflowRuleAttributeFields.Elements.RULE_EXTENSION_VALUES,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})

public class WorkflowRuleAttributeFields extends AbstractDataTransferObject {

    @XmlElementWrapper(name = Elements.VALIDATION_ERRORS, required = true)
    @XmlElement(name = Elements.VALIDATION_ERROR, required = false)
    private final List<RemotableAttributeError> validationErrors;

    @XmlElementWrapper(name = Elements.ATTRIBUTE_FIELDS, required = true)
    @XmlElement(name = Elements.ATTRIBUTE_FIELD, required = false)
    private final List<RemotableAttributeField> attributeFields;

    @XmlElement(name = Elements.RULE_EXTENSION_VALUES, required = false)
    @XmlJavaTypeAdapter(MapStringStringAdapter.class)
    private final Map<String, String> ruleExtensionValues;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     */
    @SuppressWarnings("unused")
    private WorkflowRuleAttributeFields() {
        this.validationErrors = null;
        this.attributeFields = null;
        this.ruleExtensionValues = null;
    }

    private WorkflowRuleAttributeFields(List<RemotableAttributeError> validationErrors,
                                        List<RemotableAttributeField> attributeFields,
                                        Map<String, String> ruleExtensionValues) {
        this.validationErrors = ModelObjectUtils.createImmutableCopy(validationErrors);
        this.attributeFields = ModelObjectUtils.createImmutableCopy(attributeFields);
        this.ruleExtensionValues = ModelObjectUtils.createImmutableCopy(ruleExtensionValues);
    }

    /**
     * Construct a new instance of {@code WorkflowRuleAttributeFields} with the given validation errors, fields, and
     * rule extension values.
     *
     * @param validationErrors    any errors associated with these fields
     * @param attributeFields     the list of remotable attribute fields
     * @param ruleExtensionValues the rule extension values associated with these fields
     * @return a new WorkflowRuleAttributeFields instance containing the given values
     */
    public static WorkflowRuleAttributeFields create(List<RemotableAttributeError> validationErrors,
                                                     List<RemotableAttributeField> attributeFields,
                                                     Map<String, String> ruleExtensionValues) {
        if (validationErrors == null) {
            validationErrors = Collections.emptyList();
        }
        if (attributeFields == null) {
            attributeFields = Collections.emptyList();
        }
        if (ruleExtensionValues == null) {
            ruleExtensionValues = Collections.emptyMap();
        }
        return new WorkflowRuleAttributeFields(validationErrors, attributeFields, ruleExtensionValues);
    }

    public List<RemotableAttributeError> getValidationErrors() {
        return validationErrors;
    }

    public List<RemotableAttributeField> getAttributeFields() {
        return attributeFields;
    }

    public Map<String, String> getRuleExtensionValues() {
        return ruleExtensionValues;
    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "workflowRuleAttributeFields";
        final static String TYPE_NAME = "WorkflowRuleAttributeFieldsType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String VALIDATION_ERRORS = "validationErrors";
        final static String VALIDATION_ERROR = "validationError";
        final static String ATTRIBUTE_FIELDS = "attributeFields";
        final static String ATTRIBUTE_FIELD = "attributeField";
        final static String RULE_EXTENSION_VALUES = "ruleExtensionValues";

    }

}
