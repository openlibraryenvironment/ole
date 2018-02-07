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
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Pattern for matching any character in the given list (String)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "charsetPatternConstraint-bean", parent = "CharsetPatternConstraint")
public class CharsetPatternConstraint extends ValidCharactersPatternConstraint {
    protected String validChars;

    /**
     * @return String containing all valid chars for this charset
     */
    @BeanTagAttribute(name = "validChars")
    public String getValidChars() {
        return validChars;
    }

    /**
     * @param validChars for this charset
     */
    public void setValidChars(String validChars) {
        if (StringUtils.isEmpty(validChars)) {
            throw new IllegalArgumentException("invalid (empty) validChars");
        }

        this.validChars = validChars;
    }

    /**
     * Escapes every special character I could think of, to limit potential misuse of this pattern.
     *
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getRegexString()
     */
    protected String getRegexString() {
        if (StringUtils.isEmpty(validChars)) {
            throw new IllegalStateException("validChars is empty");
        }

        // filter out and escape chars which would confuse the pattern-matcher
        Pattern filteringChars = Pattern.compile("([\\-\\[\\]\\{\\}\\$\\.\\^\\(\\)\\*\\&\\|])");
        String filteredChars = filteringChars.matcher(validChars).replaceAll("\\\\$1");

        StringBuffer regexString = new StringBuffer("[");
        regexString.append(filteredChars);
        if (filteredChars.endsWith("\\")) {
            regexString.append("\\");
        }
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

        return (UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "charsetPattern");
    }

    /**
     * Parameters to be used in the string retrieved by this constraint's messageKey
     *
     * @return the validationMessageParams
     */
    public List<String> getValidationMessageParams() {
        if (validationMessageParams == null) {
            validationMessageParams = new ArrayList<String>();
            if (StringUtils.isNotBlank(validChars)) {
                validationMessageParams.add(validChars);
            }

        }
        return this.validationMessageParams;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     * @return A list of ErrorReports detailing errors found within the component and referenced within it
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("CharsetPatternConstraint", getMessageKey());

        if (getValidChars() == null) {
            String currentValues[] = {"validChars =" + getValidChars()};
            tracer.createError("ValidChars must be set", currentValues);
        }

        super.completeValidation(tracer.getCopy());
    }
}
