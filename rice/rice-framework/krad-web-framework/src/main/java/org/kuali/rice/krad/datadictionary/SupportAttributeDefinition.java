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
package org.kuali.rice.krad.datadictionary;

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.util.ExternalizableBusinessObjectUtils;

/**
 * Support attributes define additional attributes that can be used to generate
 * lookup field conversions and lookup parameters.
 *
 * Field conversions and lookup parameters are normally generated using foreign key relationships
 * defined within OJB and the DD.  Because Person objects are linked in a special way (i.e. they may
 * come from an external data source and not from the DB, such as LDAP), it is often necessary to define
 * extra fields that are related to each other, sort of like a supplemental foreign key.
 *
 * sourceName is the name of the POJO property of the business object
 * targetName is the name of attribute that corresponds to the sourceName in the looked up BO
 * identifier when true, only the field marked as an identifier will be passed in as a lookup parameter
 * at most one supportAttribute for each relationship should be defined as identifier="true"
 */
@BeanTag(name = "supportAttributeDefinition-bean")
public class SupportAttributeDefinition extends PrimitiveAttributeDefinition {
    private static final long serialVersionUID = -1719022365280776405L;

    protected boolean identifier;

    public SupportAttributeDefinition() {}

    @BeanTagAttribute(name = "identifier")
    public boolean isIdentifier() {
        return identifier;
    }

    /**
     * identifier when true, only the field marked as an identifier will be passed in as a lookup parameter
     * at most one supportAttribute for each relationship should be defined as identifier="true"
     */
    public void setIdentifier(boolean identifier) {
        this.identifier = identifier;
    }

    /**
     * Directly validate simple fields.
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryDefinition#completeValidation(java.lang.Class,
     *      java.lang.Object)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass) {
        if (!DataDictionary.isPropertyOf(rootBusinessObjectClass, getSourceName())) {
            throw new AttributeValidationException("unable to find attribute '"
                    + getSourceName()
                    + "' in relationship class '"
                    + rootBusinessObjectClass
                    + "' ("
                    + ""
                    + ")");
        }
        if (!DataDictionary.isPropertyOf(otherBusinessObjectClass, getTargetName())
                && !ExternalizableBusinessObjectUtils.isExternalizableBusinessObjectInterface(
                otherBusinessObjectClass)) {
            throw new AttributeValidationException(
                    "unable to find attribute '" + getTargetName() + "' in related class '" + otherBusinessObjectClass
                            .getName() + "' (" + "" + ")");
        }
    }

    /**
     * Directly validate simple fields
     *
     * @see org.kuali.rice.krad.datadictionary.DataDictionaryEntry#completeValidation(org.kuali.rice.krad.datadictionary.validator.ValidationTrace)
     */
    public void completeValidation(Class rootBusinessObjectClass, Class otherBusinessObjectClass,
            ValidationTrace tracer) {
        tracer.addBean(this.getClass().getSimpleName(), ValidationTrace.NO_BEAN_ID);
        try {
            if (!DataDictionary.isPropertyOf(rootBusinessObjectClass, getSourceName())) {
                String currentValues[] = {"attribute = " + getSourceName(), "class = " + rootBusinessObjectClass};
                tracer.createError("Unable to find attribute in class", currentValues);
            }
            if (!DataDictionary.isPropertyOf(otherBusinessObjectClass, getTargetName())
                    && !ExternalizableBusinessObjectUtils.isExternalizableBusinessObjectInterface(
                    otherBusinessObjectClass)) {

                String currentValues[] = {"attribute = " + getTargetName(), "class = " + otherBusinessObjectClass};
                tracer.createError("Unable to find attribute in class", currentValues);
            }
        } catch (RuntimeException ex) {
            String currentValues[] = {"Exception = " + ex.getMessage()};
            tracer.createError("Unable to validate attribute", currentValues);
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SupportAttributeDefinition (" + getSourceName() + "," + getTargetName() + "," + identifier + ")";
    }

}

