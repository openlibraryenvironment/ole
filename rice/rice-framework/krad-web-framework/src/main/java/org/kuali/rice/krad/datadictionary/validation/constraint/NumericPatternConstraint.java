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
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.UifConstants;

/**
 * Pattern for matching numeric characters, difference between NumericPatternConstraint and IntegerPatternConstraint
 * is that a numeric pattern constraint is for matching numeric characters and can be mixed with other characters
 * by setting allow flags on, while integer is for only positive/negative numbers
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "numericPatternConstraint-bean", parent = "NumericPatternConstraint"),
        @BeanTag(name = "numericWithOperators-bean", parent = "NumericWithOperators")})
public class NumericPatternConstraint extends AllowCharacterConstraint {

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getRegexString()
     */
    protected String getRegexString() {
        StringBuilder regexString = new StringBuilder("[0-9");
        regexString.append(this.getAllowedCharacterRegex());
        regexString.append("]");

        return regexString.toString();
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.BaseConstraint#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        String messageKey = super.getMessageKey();
        if (StringUtils.isNotEmpty(messageKey)) {
            return messageKey;
        }

        return UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "numericPattern";
    }

}
