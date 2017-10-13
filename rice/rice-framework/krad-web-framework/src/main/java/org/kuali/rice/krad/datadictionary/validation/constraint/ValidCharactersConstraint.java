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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;

/**
 * This is a constraint that limits attribute values to some subset of valid characters or to match a particular
 * regular
 * expression.
 *
 * For example:
 * - To limit to both upper and lower-case letters, value can be set to "[A-Za-z]*"
 * - To limit to any character except carriage returns and line feeds, value can be set to "[^\n\r]*"
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "validCharactersConstraint-bean", parent="ValidCharactersConstraint")
public class ValidCharactersConstraint extends BaseConstraint {

    protected String value;

    /**
     * The Java based regex for valid characters
     * This value should include the ^ and $ symbols if needed
     *
     * @return the value
     */
    @BeanTagAttribute(name = "value")
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("ValidCharacterConstraint", getMessageKey());

        if (getValue() == null) {
            String currentValues[] = {"getValue =" + getValue()};
            tracer.createWarning("GetValue should return something", currentValues);
        }

        super.completeValidation(tracer.getCopy());
    }
}