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
import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;
import org.kuali.rice.krad.uif.UifConstants;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PrerequisiteConstraintProcessor extends BasePrerequisiteConstraintProcessor<PrerequisiteConstraint> {

    private static final String CONSTRAINT_NAME = "prerequisite constraint";
    private static final String FALLBACK_KEY = "prerequisiteFallback";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.capability.Validatable,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, PrerequisiteConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        if (ValidationUtils.isNullOrEmpty(value)) {
            return new ProcessorResult(result.addSkipped(attributeValueReader, CONSTRAINT_NAME));
        }

        ConstraintValidationResult constraintValidationResult = processPrerequisiteConstraint(constraint,
                attributeValueReader);

        if (constraint != null) {
            if (StringUtils.isNotBlank(constraint.getMessageKey())) {
                constraintValidationResult.setConstraintLabelKey(constraint.getMessageKey());
            } else {
                constraintValidationResult.setConstraintLabelKey(
                        UifConstants.Messages.VALIDATION_MSG_KEY_PREFIX + FALLBACK_KEY);
            }
            constraintValidationResult.setErrorParameters(constraint.getValidationMessageParamsArray());
        }

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
        return PrerequisiteConstraint.class;
    }

}
