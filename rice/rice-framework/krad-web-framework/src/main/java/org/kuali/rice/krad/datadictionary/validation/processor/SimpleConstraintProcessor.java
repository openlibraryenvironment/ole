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

import org.kuali.rice.krad.datadictionary.exception.AttributeValidationException;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.SimpleConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor for simple constraint which takes out each constraining value it contains and calls the appropriate
 * processor
 */
public class SimpleConstraintProcessor extends MandatoryElementConstraintProcessor<SimpleConstraint> {

    private static final String CONSTRAINT_NAME = "simple constraint";

    RangeConstraintProcessor rangeConstraintProcessor = new RangeConstraintProcessor();
    LengthConstraintProcessor lengthConstraintProcessor = new LengthConstraintProcessor();
    ExistenceConstraintProcessor existenceConstraintProcessor = new ExistenceConstraintProcessor();
    DataTypeConstraintProcessor dataTypeConstraintProcessor = new DataTypeConstraintProcessor();

    /**
     * Processes the SimpleConstraint by calling process on the other smaller constraints it represents and
     * putting the results together in ProcessorResult
     *
     * @return
     * @throws AttributeValidationException
     * @see MandatoryElementConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Object value, final SimpleConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        ProcessorResult dataTypePR = dataTypeConstraintProcessor.process(result, value, constraint,
                attributeValueReader);
        ProcessorResult existencePR = existenceConstraintProcessor.process(result, value, constraint,
                attributeValueReader);
        ProcessorResult rangePR = rangeConstraintProcessor.process(result, value, constraint, attributeValueReader);
        ProcessorResult lengthPR = lengthConstraintProcessor.process(result, value, constraint, attributeValueReader);
        List<ConstraintValidationResult> cvrList = new ArrayList<ConstraintValidationResult>();
        cvrList.addAll(existencePR.getConstraintValidationResults());
        cvrList.addAll(rangePR.getConstraintValidationResults());
        cvrList.addAll(lengthPR.getConstraintValidationResults());
        cvrList.addAll(dataTypePR.getConstraintValidationResults());
        return new ProcessorResult(cvrList);
    }

    @Override
    public String getName() {
        return CONSTRAINT_NAME;
    }

    @Override
    public Class<? extends Constraint> getConstraintType() {
        return SimpleConstraint.class;
    }
}
