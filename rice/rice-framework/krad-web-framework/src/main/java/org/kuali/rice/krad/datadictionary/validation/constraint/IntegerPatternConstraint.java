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

import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Administrator don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "integerPatternConstraint-bean", parent = "IntegerPatternConstraint")
public class IntegerPatternConstraint extends ValidDataPatternConstraint {
    protected boolean allowNegative;
    protected boolean onlyNegative;
    protected boolean omitZero;

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersPatternConstraint#getRegexString()
     */
    @Override
    protected String getRegexString() {
        StringBuffer regex = new StringBuffer();

        if (isAllowNegative() && !onlyNegative) {
            regex.append("((-?");
        } else if (onlyNegative) {
            regex.append("((-");
        } else {
            regex.append("((");
        }
        if (omitZero) {
            regex.append("[1-9][0-9]*))");
        } else {
            regex.append("[1-9][0-9]*)|[0]*)");
        }

        return regex.toString();
    }

    /**
     * @return the allowNegative
     */
    @BeanTagAttribute(name = "allowNegative")
    public boolean isAllowNegative() {
        return this.allowNegative;
    }

    /**
     * @param allowNegative the allowNegative to set
     */
    public void setAllowNegative(boolean allowNegative) {
        this.allowNegative = allowNegative;
    }

    @BeanTagAttribute(name = "onlyNegative")
    public boolean isOnlyNegative() {
        return onlyNegative;
    }

    /**
     * When set to true, only allows negative numbers (and zero if allowZero is still true)
     *
     * @param onlyNegative
     */
    public void setOnlyNegative(boolean onlyNegative) {
        this.onlyNegative = onlyNegative;
    }

    @BeanTagAttribute(name = "omitZero")
    public boolean isOmitZero() {
        return omitZero;
    }

    /**
     * When set to true, zero is not allowed in the set of allowed numbers.
     *
     * @param omitZero
     */
    public void setOmitZero(boolean omitZero) {
        this.omitZero = omitZero;
    }

    /**
     * This overridden method ...
     *
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ValidDataPatternConstraint#getValidationMessageParams()
     */
    @Override
    public List<String> getValidationMessageParams() {
        if (validationMessageParams == null) {
            validationMessageParams = new ArrayList<String>();

            MessageService messageService = KRADServiceLocatorWeb.getMessageService();
            if (allowNegative && !onlyNegative) {
                if (omitZero) {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positiveOrNegative"));
                } else {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positiveOrNegativeOrZero"));
                }
            } else if (onlyNegative) {
                if (omitZero) {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "negative"));
                } else {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "negativeOrZero"));
                }
            } else {
                if (omitZero) {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positive"));
                } else {
                    validationMessageParams.add(messageService.getMessageText(
                            UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positiveOrZero"));
                }
            }
        }
        return validationMessageParams;
    }
}
