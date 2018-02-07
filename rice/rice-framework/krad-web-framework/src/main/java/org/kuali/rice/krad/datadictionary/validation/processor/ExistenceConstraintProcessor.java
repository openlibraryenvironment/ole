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
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ExistenceConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExistenceConstraintProcessor extends OptionalElementConstraintProcessor<ExistenceConstraint> {

    private static final String CONSTRAINT_NAME = "existence constraint";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader) \
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, ExistenceConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        // To accommodate the needs of other processors, the ConstraintProcessor.process() method returns a list of ConstraintValidationResult objects
        // but since a definition that is existence constrained only provides a single isRequired field, there is effectively a single constraint
        // being imposed.
        return new ProcessorResult(processSingleExistenceConstraint(result, value, constraint, attributeValueReader));
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
        return ExistenceConstraint.class;
    }

    protected ConstraintValidationResult processSingleExistenceConstraint(DictionaryValidationResult result,
            Object value, ExistenceConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {
        // If it's not set, then there's no constraint
        if (constraint.isRequired() == null) {
            return result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME);
        }

        if (constraint.isRequired().booleanValue() && !skipConstraint(attributeValueReader)) {
            // If this attribute is required and the value is null then
            if (ValidationUtils.isNullOrEmpty(value)) {
                return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_REQUIRED,
                        attributeValueReader.getLabel(attributeValueReader.getAttributeName()));
            }
            return result.addSuccess(attributeValueReader, CONSTRAINT_NAME);
        }

        return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
    }

    /**
     * Checks to see if existence constraint should be skipped.  Required constraint should be skipped if it is an
     * attribute of a complex
     * attribute and the complex attribute is not required.
     *
     * @param attributeValueReader
     * @return
     */
    private boolean skipConstraint(AttributeValueReader attributeValueReader) {
        boolean skipConstraint = false;
        if (attributeValueReader instanceof DictionaryObjectAttributeValueReader) {
            DictionaryObjectAttributeValueReader dictionaryValueReader =
                    (DictionaryObjectAttributeValueReader) attributeValueReader;
            skipConstraint = dictionaryValueReader.isNestedAttribute() && dictionaryValueReader.isParentAttributeNull();
        }
        return skipConstraint;
    }

}
