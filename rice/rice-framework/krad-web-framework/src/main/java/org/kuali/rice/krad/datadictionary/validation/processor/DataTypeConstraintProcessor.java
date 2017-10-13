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
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.DataTypeConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

/**
 * DataTypeConstraintProcessor processes constraints of type {@link DataTypeConstraint}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataTypeConstraintProcessor extends MandatoryElementConstraintProcessor<DataTypeConstraint> {

    private static final String CONSTRAINT_NAME = "data type constraint";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, DataTypeConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        DataType dataType = constraint.getDataType();

        return new ProcessorResult(processDataTypeConstraint(result, dataType, value, attributeValueReader));
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
        return DataTypeConstraint.class;
    }

    /**
     * validates the value provided using {@code DataTypeConstraint}
     *
     * @param result - a holder for any already run validation results
     * @param dataType - the expected data type
     * @param value - the value to validate
     * @param attributeValueReader - provides access to the attribute being validated
     * @return the passed in result, updated with the results of the processing
     */
    protected ConstraintValidationResult processDataTypeConstraint(DictionaryValidationResult result, DataType dataType,
            Object value, AttributeValueReader attributeValueReader) {
        if (dataType == null) {
            return result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME);
        }

        if (ValidationUtils.isNullOrEmpty(value)) {
            return result.addSkipped(attributeValueReader, CONSTRAINT_NAME);
        }

        try {
            ValidationUtils.convertToDataType(value, dataType, dateTimeService);
        } catch (Exception e) {
            switch (dataType) {
                case BOOLEAN:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_BOOLEAN);
                case INTEGER:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_INTEGER);
                case LONG:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_LONG);
                case DOUBLE:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_BIG_DECIMAL);
                case FLOAT:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_BIG_DECIMAL);
                case TRUNCATED_DATE:
                case DATE:
                    return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_BIG_DECIMAL);
                case STRING:
            }
        }

        // If we get here then it was a success!
        return result.addSuccess(attributeValueReader, CONSTRAINT_NAME);
    }

}
