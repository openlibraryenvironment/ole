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
package org.kuali.rice.coreservice.api.parameter;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.w3c.dom.Element;

/**
 * An immutable representation of a {@link ParameterContract}.
 * <p/>
 * <p>To construct an instance of a Parameter, use the {@link Parameter.Builder} class.
 *
 * @see ParameterContract
 */
@XmlRootElement(name = Parameter.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Parameter.Constants.TYPE_NAME, propOrder = {
        Parameter.Elements.APPLICATION_ID,
        Parameter.Elements.NAMESPACE_CODE,
        Parameter.Elements.COMPONENT_CODE,
        Parameter.Elements.NAME,
        Parameter.Elements.VALUE,
        Parameter.Elements.DESCRIPTION,
        Parameter.Elements.PARAMETER_TYPE,
        Parameter.Elements.EVALUATION_OPERATOR,
        CoreConstants.CommonElements.VERSION_NUMBER,
        CoreConstants.CommonElements.OBJECT_ID,
        CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Parameter extends AbstractDataTransferObject implements ParameterContract {

    private static final long serialVersionUID = 6097498602725305353L;

    @XmlElement(name = Elements.APPLICATION_ID, required = true)
    private final String applicationId;

    @XmlElement(name = Elements.NAMESPACE_CODE, required = true)
    private final String namespaceCode;

    @XmlElement(name = Elements.COMPONENT_CODE, required = true)
    private final String componentCode;

    @XmlElement(name = Elements.NAME, required = true)
    private final String name;

    @XmlElement(name = Elements.VALUE, required = false)
    private final String value;

    @XmlElement(name = Elements.DESCRIPTION, required = false)
    private final String description;

    @XmlElement(name = Elements.PARAMETER_TYPE, required = true)
    private final ParameterType parameterType;

    @XmlJavaTypeAdapter(EvaluationOperator.Adapter.class)
    @XmlElement(name = Elements.EVALUATION_OPERATOR, required = false)
    private final String evaluationOperator;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * This constructor should never be called except during JAXB unmarshalling.
     */
    private Parameter() {
        this.applicationId = null;
        this.namespaceCode = null;
        this.componentCode = null;
        this.name = null;
        this.value = null;
        this.description = null;
        this.parameterType = null;
        this.evaluationOperator = null;
        this.versionNumber = null;
        this.objectId = null;
    }

    private Parameter(Builder builder) {
        applicationId = builder.getApplicationId();
        namespaceCode = builder.getNamespaceCode();
        componentCode = builder.getComponentCode();
        name = builder.getName();
        value = builder.getValue();
        description = builder.getDescription();
        parameterType = builder.parameterType.build();
        EvaluationOperator evaluationOperatorEnum = builder.getEvaluationOperator();
        evaluationOperator = evaluationOperatorEnum == null ? null : evaluationOperatorEnum.getCode(); 
        versionNumber = builder.getVersionNumber();
        objectId = builder.getObjectId();
    }

    @Override
    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public String getNamespaceCode() {
        return namespaceCode;
    }

    @Override
    public String getComponentCode() {
        return componentCode;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public ParameterType getParameterType() {
        return parameterType;
    }

    @Override
    public EvaluationOperator getEvaluationOperator() {
        return EvaluationOperator.fromCode(evaluationOperator);
    }

    @Override
    public Long getVersionNumber() {
        return versionNumber;
    }

    @Override
    public String getObjectId() {
        return objectId;
    }
    
    public ParameterKey getParameterKey() {
        return ParameterKey.create(this.applicationId, this.namespaceCode, this.componentCode, this.name);
    }

    /**
     * This builder constructs an Parameter enforcing the constraints of the {@link ParameterContract}.
     */
    public static final class Builder implements ParameterContract, ModelBuilder, Serializable {

        private static final long serialVersionUID = 7077484401017765844L;

        private String applicationId;
        private String namespaceCode;
        private String componentCode;
        private String name;
        private String value;
        private String description;
        private ParameterType.Builder parameterType;
        private EvaluationOperator evaluationOperator;
        private Long versionNumber;
        private String objectId;

        private Builder(String applicationId, String namespaceCode, String componentCode, String name, ParameterType.Builder parameterType) {
            setApplicationId(applicationId);
            setNamespaceCode(namespaceCode);
            setComponentCode(componentCode);
            setName(name);
            setParameterType(parameterType);
        }

        /**
         * creates a Parameter with the required fields.
         */
        public static Builder create(String applicationId, String namespaceCode, String componentCode, String name, ParameterType.Builder parameterType) {
            return new Builder(applicationId, namespaceCode, componentCode, name, parameterType);
        }

        /**
         * creates a Parameter from an existing {@link ParameterContract}.
         */
        public static Builder create(ParameterContract contract) {
            Builder builder = new Builder(contract.getApplicationId(), contract.getNamespaceCode(), contract.getComponentCode(), contract.getName(), ParameterType.Builder.create(contract.getParameterType()));
            builder.setValue(contract.getValue());
            builder.setDescription(contract.getDescription());
            builder.setEvaluationOperator(contract.getEvaluationOperator());
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            return builder;
        }

        public void setApplicationId(String applicationId) {
            if (StringUtils.isBlank(applicationId)) {
                throw new IllegalArgumentException("applicationId is blank");
            }
            this.applicationId = applicationId;
        }

        public void setNamespaceCode(String namespaceCode) {
            if (StringUtils.isBlank(namespaceCode)) {
                throw new IllegalArgumentException("namespaceCode is blank");
            }
            this.namespaceCode = namespaceCode;
        }

        public void setComponentCode(String componentCode) {
            if (StringUtils.isBlank(componentCode)) {
                throw new IllegalArgumentException("componentCode is blank");
            }
            this.componentCode = componentCode;
        }

        public void setName(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("name is blank");
            }
            this.name = name;
        }

        public void setParameterType(ParameterType.Builder parameterType) {
            if (parameterType == null) {
                throw new IllegalArgumentException("parameterType is null");
            }
            this.parameterType = parameterType;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setEvaluationOperator(EvaluationOperator evaluationOperator) {
            this.evaluationOperator = evaluationOperator;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        @Override
        public String getApplicationId() {
            return applicationId;
        }

        @Override
        public String getNamespaceCode() {
            return namespaceCode;
        }

        @Override
        public String getComponentCode() {
            return componentCode;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public EvaluationOperator getEvaluationOperator() {
            return evaluationOperator;
        }

        @Override
        public Parameter build() {
            return new Parameter(this);
        }

        @Override
        public ParameterType.Builder getParameterType() {
            return parameterType;
        }

        @Override
        public Long getVersionNumber() {
            return versionNumber;
        }

        @Override
        public String getObjectId() {
            return objectId;
        }

    }

    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "parameter";
        final static String TYPE_NAME = "ParameterType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use
     * when this object is marshalled to XML.
     */
    static class Elements {
        final static String APPLICATION_ID = "applicationId";
        final static String NAMESPACE_CODE = "namespaceCode";
        final static String COMPONENT_CODE = "componentCode";
        final static String NAME = "name";
        final static String VALUE = "value";
        final static String DESCRIPTION = "description";
        final static String PARAMETER_TYPE = "parameterType";
        final static String EVALUATION_OPERATOR = "evaluationOperator";
    }

    public static class Cache {
        public static final String NAME = CoreConstants.Namespaces.CORE_NAMESPACE_2_0 + "/" + Parameter.Constants.TYPE_NAME;
    }

}
