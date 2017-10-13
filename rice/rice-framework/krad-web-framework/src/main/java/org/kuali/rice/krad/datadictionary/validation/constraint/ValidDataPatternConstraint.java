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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "validDataPatternConstraint-bean")
public abstract class ValidDataPatternConstraint extends ValidCharactersConstraint {

    /**
     * Warning: This value should NOT be set on this class as the value is built dynamically from the
     * flags set on the constraint - if this value IS set it will override any automatic generation and only
     * use that which was set through this method for server side validation
     *
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint#getValue()
     */
    @Override
    @BeanTagAttribute(name = "value")
    public String getValue() {
        if (StringUtils.isEmpty(value)) {
            return "^" + getRegexString() + "$";
        }
        return value;

    }

    /**
     * This method returns a string representing a regex with characters to match, this string should not
     * include the start(^) and end($) symbols
     */
    abstract protected String getRegexString();

}
