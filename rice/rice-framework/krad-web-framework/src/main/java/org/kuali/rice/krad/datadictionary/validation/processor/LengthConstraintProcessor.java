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

import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils.Result;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.LengthConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LengthConstraintProcessor extends MandatoryElementConstraintProcessor<LengthConstraint> {

    private static final String MIN_LENGTH_KEY = "validation.minLengthConditional";
    private static final String MAX_LENGTH_KEY = "validation.maxLengthConditional";
    private static final String RANGE_KEY = "validation.lengthRange";

    private static final String CONSTRAINT_NAME = "length constraint";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, LengthConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        // To accommodate the needs of other processors, the ConstraintProcessor.process() method returns a list of ConstraintValidationResult objects
        // but since a definition that is length constrained only constrains a single field, there is effectively always a single constraint
        // being imposed
        return new ProcessorResult(processSingleLengthConstraint(result, value, constraint, attributeValueReader));
    }

    @Override
    public String getName() {
        return CONSTRAINT_NAME;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#getConstraintType()
     */
    @Override
    public Class<? extends Constraint> getConstraintType() {
        return LengthConstraint.class;
    }

    protected ConstraintValidationResult processSingleLengthConstraint(DictionaryValidationResult result, Object value,
            LengthConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {
        // Can't process any range constraints on null values
        if (ValidationUtils.isNullOrEmpty(value)) {
            return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
        }

        DataType dataType = constraint.getDataType();
        Object typedValue = value;

        if (dataType != null) {
            typedValue = ValidationUtils.convertToDataType(value, dataType, dateTimeService);
        }

        // The only thing that can have a length constraint currently is a string.
        if (typedValue instanceof String) {
            return validateLength(result, (String) typedValue, constraint, attributeValueReader);
        }

        return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
    }

    protected ConstraintValidationResult validateLength(DictionaryValidationResult result, String value,
            LengthConstraint constraint, AttributeValueReader attributeValueReader) throws IllegalArgumentException {
        Integer valueLength = Integer.valueOf(value.length());

        Integer maxLength = constraint.getMaxLength();
        Integer minLength = constraint.getMinLength();

        Result lessThanMax = ValidationUtils.isLessThanOrEqual(valueLength, maxLength);
        Result greaterThanMin = ValidationUtils.isGreaterThanOrEqual(valueLength, minLength);

        // It's okay for one end of the range to be undefined - that's not an error. It's only an error if one of them is invalid 
        if (lessThanMax != Result.INVALID && greaterThanMin != Result.INVALID) {
            // Of course, if they're both undefined then we didn't actually have a real constraint
            if (lessThanMax == Result.UNDEFINED && greaterThanMin == Result.UNDEFINED) {
                return result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME);
            }

            // In this case, we've succeeded
            return result.addSuccess(attributeValueReader, CONSTRAINT_NAME);
        }

        String maxErrorParameter = maxLength != null ? maxLength.toString() : null;
        String minErrorParameter = minLength != null ? minLength.toString() : null;

        // If both comparisons happened then if either comparison failed we can show the end user the expected range on both sides.
        if (lessThanMax != Result.UNDEFINED && greaterThanMin != Result.UNDEFINED) {
            return result.addError(RANGE_KEY, attributeValueReader, CONSTRAINT_NAME,
                    RiceKeyConstants.ERROR_OUT_OF_RANGE, minErrorParameter, maxErrorParameter);
        }
        // If it's the max comparison that fails, then just tell the end user what the max can be
        else if (lessThanMax == Result.INVALID) {
            return result.addError(MAX_LENGTH_KEY, attributeValueReader, CONSTRAINT_NAME,
                    RiceKeyConstants.ERROR_INCLUSIVE_MAX, maxErrorParameter);
        }
        // Otherwise, just tell them what the min can be
        else {
            return result.addError(MIN_LENGTH_KEY, attributeValueReader, CONSTRAINT_NAME,
                    RiceKeyConstants.ERROR_EXCLUSIVE_MIN, minErrorParameter);
        }

    }

}
