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
import org.kuali.rice.krad.messages.MessageService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO delyea don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "fixedPointPatternConstraint-bean", parent = "FixedPointPatternConstraint")
public class FixedPointPatternConstraint extends ValidDataPatternConstraint {

    protected boolean allowNegative;
    protected int precision;
    protected int scale;

    /**
     * Overriding retrieval of
     *
     * @see org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersPatternConstraint#getRegexString()
     */
    @Override
    protected String getRegexString() {
        StringBuilder regex = new StringBuilder();

        if (getPrecision() < 0 || getScale() < 0 || getPrecision() - getScale() < 0){
            throw new RuntimeException("Precision and scale cannot be negative AND scale cannot be greater than "
                    + "precision for FixedPointPatternConstraints!");
        }

        if (isAllowNegative()) {
            regex.append("-?");
        }
        // final pattern will be: -?([0-9]{0,p-s}\.[0-9]{1,s}|[0-9]{1,p-s}) where p = precision, s=scale

        regex.append("(");
        if(getPrecision() - getScale() > 0){
            regex.append("[0-9]{0," + (getPrecision() - getScale()) + "}");
        }
        regex.append("\\.");
        regex.append("[0-9]{1," + getScale() + "}");
        if(getPrecision() - getScale() > 0){
            regex.append("|[0-9]{1," + (getPrecision() - getScale()) + "}");
        }
        regex.append(")");
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

    /**
     * @return the precision
     */
    @BeanTagAttribute(name = "precision")
    public int getPrecision() {
        return this.precision;
    }

    /**
     * @param precision the precision to set
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * @return the scale
     */
    @BeanTagAttribute(name = "scale")
    public int getScale() {
        return this.scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(int scale) {
        this.scale = scale;
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
            if (allowNegative) {
                validationMessageParams.add(messageService.getMessageText(
                        UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positiveOrNegative"));
            } else {
                validationMessageParams.add(messageService.getMessageText(
                        UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + "positive"));
            }

            validationMessageParams.add(Integer.toString(precision));
            validationMessageParams.add(Integer.toString(scale));
        }
        return validationMessageParams;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer Record of component's location
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("FixedPointPatternConstraint", getMessageKey());

        if (getPrecision() <= getScale()) {
            String currentValues[] = {"precision =" + getPrecision(), "scale = " + getScale()};
            tracer.createError("Precision should greater than Scale", currentValues);
        }

        super.completeValidation(tracer.getCopy());
    }

}
