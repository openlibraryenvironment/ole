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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;
import org.kuali.rice.krad.uif.UifConstants;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MustOccurConstraintProcessor extends BasePrerequisiteConstraintProcessor<MustOccurConstraint> {

    private static final String CONSTRAINT_NAME = "must occur constraint";
    private static final String FALLBACK_KEY = "mustoccursFallback";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, MustOccurConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        if (ValidationUtils.isNullOrEmpty(value)) {
            return new ProcessorResult(result.addSkipped(attributeValueReader, CONSTRAINT_NAME));
        }

        ConstraintValidationResult constraintValidationResult = new ConstraintValidationResult(CONSTRAINT_NAME);
        if (StringUtils.isNotBlank(constraint.getMessageKey())) {
            constraintValidationResult.setConstraintLabelKey(constraint.getMessageKey());
        } else {
            constraintValidationResult.setConstraintLabelKey(
                    UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + FALLBACK_KEY);
        }

        constraintValidationResult.setErrorParameters(constraint.getValidationMessageParamsArray());

        // If the processing of this constraint is not successful then it's an error
        if (!processMustOccurConstraint(constraintValidationResult, constraint, attributeValueReader)) {
            // if attributeName is null, use the entry name since we are processing a must occur constraint that may be referencing multiple attributes
            if (attributeValueReader.getAttributeName() == null) {
                constraintValidationResult.setAttributeName(attributeValueReader.getEntryName());
            } else {
                constraintValidationResult.setAttributeName(attributeValueReader.getAttributeName());
                constraintValidationResult.setAttributePath(attributeValueReader.getPath());
            }
            constraintValidationResult.setError(RiceKeyConstants.ERROR_OCCURS);
        }

        // Store the label key (if one exists) for this constraint on the constraint validation result so it can be shown later

        // Add it to the DictionaryValidationResult object
        result.addConstraintValidationResult(attributeValueReader, constraintValidationResult);

        return new ProcessorResult(constraintValidationResult);

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
        return MustOccurConstraint.class;
    }

    protected boolean processMustOccurConstraint(ConstraintValidationResult topLevelResult,
            MustOccurConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        boolean isSuccessful = false;
        int trueCount = 0;

        List<PrerequisiteConstraint> prerequisiteConstraints = constraint.getPrerequisiteConstraints();
        if (prerequisiteConstraints != null) {
            for (PrerequisiteConstraint prerequisiteConstraint : prerequisiteConstraints) {
                ConstraintValidationResult constraintValidationResult = processPrerequisiteConstraint(
                        prerequisiteConstraint, attributeValueReader);
                constraintValidationResult.setConstraintLabelKey(prerequisiteConstraint.getMessageKey());
                constraintValidationResult.setErrorParameters(prerequisiteConstraint.getValidationMessageParamsArray());
                // Add the result of each prerequisite constraint validation to the top level result object as a child
                topLevelResult.addChild(constraintValidationResult);
                trueCount += (constraintValidationResult.getStatus().getLevel() <= ErrorLevel.WARN.getLevel()) ? 1 : 0;
            }
        }

        List<MustOccurConstraint> mustOccurConstraints = constraint.getMustOccurConstraints();
        if (mustOccurConstraints != null) {
            for (MustOccurConstraint mustOccurConstraint : mustOccurConstraints) {
                // Create a new constraint validation result for this must occur constraint and make it child of the top-level constraint,
                // then pass it in to the recursive call so that prerequisite constraints can be placed under it
                ConstraintValidationResult constraintValidationResult = new ConstraintValidationResult(CONSTRAINT_NAME);
                constraintValidationResult.setConstraintLabelKey(mustOccurConstraint.getMessageKey());
                constraintValidationResult.setErrorParameters(mustOccurConstraint.getValidationMessageParamsArray());
                topLevelResult.addChild(constraintValidationResult);
                trueCount += (processMustOccurConstraint(constraintValidationResult, mustOccurConstraint,
                        attributeValueReader)) ? 1 : 0;
            }
        }

        int minimum = constraint.getMin() != null ? constraint.getMin().intValue() : 0;
        int maximum = constraint.getMax() != null ? constraint.getMax().intValue() : 0;

        isSuccessful = (trueCount >= minimum && trueCount <= maximum) ? true : false;

        return isSuccessful;
    }

}
