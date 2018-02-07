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
import org.kuali.rice.krad.uif.UifConstants;

/**
 * Pattern for matching any printable character
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "utf8AnyCharacterPatternConstraint-bean", parent = "UTF8AnyCharacterPatternConstraint")
public class UTF8AnyCharacterPatternConstraint extends ValidCharactersPatternConstraint {
    protected boolean allowWhitespace = false;
    protected boolean omitNewline = false;

    /**
     * @return allowWhitespace
     */
    @BeanTagAttribute(name = "allowWhitespace")
    public boolean getAllowWhitespace() {
        return allowWhitespace;
    }

    /**
     * @param allowWhitespace
     */
    public void setAllowWhitespace(boolean allowWhitespace) {
        this.allowWhitespace = allowWhitespace;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.ValidationPattern#getRegexString()
     */
    protected String getRegexString() {
        StringBuffer regexString = new StringBuffer("[");

        regexString.append("\\u0000-\\uFFFF");
        if (allowWhitespace) {
            regexString.append("\\t\\v\\040");
            if (!omitNewline) {
                regexString.append("\\f\\r\\n");
            }
        }

        regexString.append("]");

        return regexString.toString();
    }

    /**
     * @see BaseConstraint#getMessageKey()
     */
    @Override
    public String getMessageKey() {
        String messageKey = super.getMessageKey();
        if (StringUtils.isNotEmpty(messageKey)) {
            return messageKey;
        }

        if (!allowWhitespace) {
            return UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "noWhitespace";
        } else {
            return UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "utf8AnyCharacterPattern";
        }
    }

    @BeanTagAttribute(name = "omitNewline")
    public boolean isOmitNewline() {
        return omitNewline;
    }

    /**
     * When set to true, omit new line characters from the set of valid characters.  This flag
     * will only have an effect if the allowWhitespace flag is true, otherwise all whitespace
     * including new lines characters are omitted.
     *
     * @param omitNewline
     */
    public void setOmitNewline(boolean omitNewline) {
        this.omitNewline = omitNewline;
    }
}
