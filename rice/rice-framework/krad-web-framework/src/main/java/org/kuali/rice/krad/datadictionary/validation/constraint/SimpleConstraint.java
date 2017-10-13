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
package org.kuali.rice.krad.datadictionary.validation.constraint;

import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;

/**
 * A simple constraint stores 'basic' constraints for a field.  This constraint is meant to be used as a
 * constraint for WhenConstraints in CaseConstraint, and is also used internally in InputField.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "simpleContraint-bean", parent = "SimpleConstraint"),
        @BeanTag(name = "requiredConstraint-bean", parent = "RequiredConstraint")})
public class SimpleConstraint extends BaseConstraint implements ExistenceConstraint, RangeConstraint, LengthConstraint {

    private Boolean required;
    private Integer maxLength;
    private Integer minLength;
    private String exclusiveMin;
    private String inclusiveMax;

    //Don't know if we will support min/max occurs at this time
    private Integer minOccurs;
    private Integer maxOccurs;

    private DataType dataType;

    /**
     * If true the field is required
     *
     * @return the required
     */
    @BeanTagAttribute(name = "required")
    public Boolean getRequired() {
        return this.required;
    }

    /**
     * @param required the required to set
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ExistenceConstraint#isRequired()
     */
    @Override
    public Boolean isRequired() {
        return getRequired();
    }

    /**
     * The maximum amount of characters this field's value can be
     *
     * @return the maxLength
     */
    @BeanTagAttribute(name = "maxLength")
    public Integer getMaxLength() {
        return this.maxLength;
    }

    /**
     * @param maxLength the maxLength to set
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * The minimum amount of characters this field's value has to be
     *
     * @return the minLength
     */
    @BeanTagAttribute(name = "minLength")
    public Integer getMinLength() {
        return this.minLength;
    }

    /**
     * @param minLength the minLength to set
     */
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * Exclusive minimum value for this field
     *
     * @return the exclusiveMin
     */
    @BeanTagAttribute(name = "exclusiveMin")
    public String getExclusiveMin() {
        return this.exclusiveMin;
    }

    /**
     * @param exclusiveMin the exclusiveMin to set
     */
    public void setExclusiveMin(String exclusiveMin) {
        this.exclusiveMin = exclusiveMin;
    }

    /**
     * Inclusive max value for this field
     *
     * @return the inclusiveMax
     */
    @BeanTagAttribute(name = "inclusiveMax")
    public String getInclusiveMax() {
        return this.inclusiveMax;
    }

    /**
     * @param inclusiveMax the inclusiveMax to set
     */
    public void setInclusiveMax(String inclusiveMax) {
        this.inclusiveMax = inclusiveMax;
    }

    /**
     * The minimum amount of items in this fields list of values - not yet used/do not use
     *
     * @return the minOccurs
     */
    @BeanTagAttribute(name = "minOccurs")
    public Integer getMinOccurs() {
        return this.minOccurs;
    }

    /**
     * @param minOccurs the minOccurs to set
     */
    public void setMinOccurs(Integer minOccurs) {
        this.minOccurs = minOccurs;
    }

    /**
     * The maximum amount of items in this field's list of values - not yet used/do not use
     *
     * @return the maxOccurs
     */
    @BeanTagAttribute(name = "maxOccurs")
    public Integer getMaxOccurs() {
        return this.maxOccurs;
    }

    /**
     * @param maxOccurs the maxOccurs to set
     */
    public void setMaxOccurs(Integer maxOccurs) {
        this.maxOccurs = maxOccurs;
    }

    @BeanTagAttribute(name = "dataType", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}

