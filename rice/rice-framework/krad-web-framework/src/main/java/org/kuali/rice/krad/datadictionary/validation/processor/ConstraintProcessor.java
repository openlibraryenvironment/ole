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
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

/**
 * ConstraintProcessor must be implemented by constraint processors, which validate individual constraints in the
 * data dictionary
 *
 * <p>The idea is that each constraint has its own processor, and that the validation service can be configured
 * via dependency injection with a list of processors. This gives institutions the ability to easily modify how
 * validation
 * should be handled and to add arbitrary new constraints and constraint processors.</p>
 *
 * <p>An alternative might have been to put
 * the process() method into the Constraint marker interface and have each Constraint define its own processing, but
 * that would
 * have forced business logic into what are naturally API classes (classes that implement Constraint). This strategy
 * separates
 * the two functions.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ConstraintProcessor<T, C extends Constraint> {

    /**
     * process the provided constraint
     *
     * @param result - holds dictionary validation results
     * @param value - the value of the attribute
     * @param constraint - the constraint to process
     * @param attributeValueReader - - provides access to the attribute being validated
     * @return the result of the constraint processing
     * @throws AttributeValidationException
     */
    public ProcessorResult process(DictionaryValidationResult result, T value, C constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException;

    /**
     * gets a descriptive name of this constraint processor
     *
     * <p>e.g. @see CollectionSizeConstraintProcessor.CONSTRAINT_NAME</p>
     *
     * @return a descriptive name
     */
    public String getName();

    /**
     * gets the java class type of the constraint that this contraint processor handles
     *
     * @return an instance of {@code Constraint}
     */
    public Class<? extends Constraint> getConstraintType();

    /**
     * returns true if the processing of this constraint is something that can be opted out of by some pieces of code.
     * The only example of this in the version under development (1.1) is the existence constraint.
     *
     * @return true if this processor can be turned off by some pieces of code, false otherwise
     */
    public boolean isOptional();

}
