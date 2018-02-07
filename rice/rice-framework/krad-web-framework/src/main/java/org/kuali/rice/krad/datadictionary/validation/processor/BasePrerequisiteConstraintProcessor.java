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
package org.kuali.rice.krad.datadictionary.validation.processor;

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;

import java.util.Collection;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BasePrerequisiteConstraintProcessor<C extends Constraint> extends MandatoryElementConstraintProcessor<C> {

    protected ConstraintValidationResult processPrerequisiteConstraint(PrerequisiteConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {
        ConstraintValidationResult constraintValidationResult = new ConstraintValidationResult(getName());

        if (constraint == null) {
            constraintValidationResult.setStatus(ErrorLevel.NOCONSTRAINT);
            return constraintValidationResult;
        }

        // TODO: Does this code need to be able to look at more than just the other immediate members of the object?
        String attributeName = constraint.getPropertyName();

        if (ValidationUtils.isNullOrEmpty(attributeName)) {
            throw new AttributeValidationException(
                    "Prerequisite constraints must include the name of the attribute that is required");
        }

        Object value = attributeValueReader.getValue(attributeName);

        boolean isSuccessful = true;

        if (value instanceof java.lang.String) {
            isSuccessful = ValidationUtils.hasText((String) value);
        } else if (value instanceof Collection) {
            isSuccessful = (((Collection<?>) value).size() > 0);
        } else {
            isSuccessful = (null != value) ? true : false;
        }

        if (!isSuccessful) {
            String label = attributeValueReader.getLabel(attributeName);
            if (label != null) {
                attributeName = label;
            }

            constraintValidationResult.setError(RiceKeyConstants.ERROR_REQUIRES_FIELD, attributeName);
            constraintValidationResult.setConstraintLabelKey(constraint.getMessageKey());
            constraintValidationResult.setErrorParameters(constraint.getValidationMessageParamsArray());
        }

        return constraintValidationResult;
    }

}
