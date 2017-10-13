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
package org.kuali.rice.krad.datadictionary.validation.result;

import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.capability.Constrainable;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * defines the information expected when a processor has processed a constraint
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see ConstraintProcessor
 */
public class ProcessorResult {

    private final transient List<Constraint> constraints;
    private final transient Constrainable definition;
    private final transient AttributeValueReader attributeValueReader;

    private final List<ConstraintValidationResult> constraintValidationResults;

    /**
     * creates a processor result from the provided constraint validation result
     *
     * @param constraintValidationResult - the constraint validation result
     */
    public ProcessorResult(ConstraintValidationResult constraintValidationResult) {
        this(constraintValidationResult, null, null);
    }

    /**
     * creates a processor result from the parameters provided
     *
     * @param constraintValidationResult - the constraint validation result
     * @param definition - a Data Dictionary definition e.g. {@code ComplexAttributeDefinition} or {@code
     * CollectionDefinition}
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraints - optional constraints to use
     */
    public ProcessorResult(ConstraintValidationResult constraintValidationResult, Constrainable definition,
            AttributeValueReader attributeValueReader, Constraint... constraints) {
        this.constraintValidationResults = Collections.singletonList(constraintValidationResult);
        this.definition = definition;
        this.attributeValueReader = attributeValueReader;
        this.constraints = Arrays.asList(constraints);
    }

    /**
     * creates a processor result from the parameters provided
     *
     * @param constraintValidationResult - the constraint validation result
     * @param definition - a Data Dictionary definition e.g. {@code ComplexAttributeDefinition} or {@code
     * CollectionDefinition}
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraints - the list of constraints to use
     */
    public ProcessorResult(ConstraintValidationResult constraintValidationResult, Constrainable definition,
            AttributeValueReader attributeValueReader, List<Constraint> constraints) {
        this.constraintValidationResults = Collections.singletonList(constraintValidationResult);
        this.definition = definition;
        this.attributeValueReader = attributeValueReader;
        this.constraints = constraints;
    }

    /**
     * creates a processor result from the provided constraint validation results
     *
     * @param constraintValidationResults - the constraint validation results to use
     */
    public ProcessorResult(List<ConstraintValidationResult> constraintValidationResults) {
        this(constraintValidationResults, null, null);
    }

    /**
     * creates a processor result from the parameters provided
     *
     * @param constraintValidationResults - the constraint validation results
     * @param definition - a Data Dictionary definition e.g. {@code ComplexAttributeDefinition} or {@code
     * CollectionDefinition}
     * @param attributeValueReader - provides access to the attribute being validated
     * @param constraints - optional constraints to use
     */
    public ProcessorResult(List<ConstraintValidationResult> constraintValidationResults, Constrainable definition,
            AttributeValueReader attributeValueReader, Constraint... constraints) {
        this.constraintValidationResults = constraintValidationResults;
        this.definition = definition;
        this.attributeValueReader = attributeValueReader;
        this.constraints = Arrays.asList(constraints);
    }

    /**
     * checks whether this object has a single constraint validation result
     *
     * @return true if only one result found, false if not
     */
    public boolean isSingleConstraintResult() {
        return this.constraintValidationResults.size() == 1;
    }

    /**
     * checks whether a Data Dictionary definition is present
     *
     * @return true if definition is present, false otherwise
     */
    public boolean isDefinitionProvided() {
        return definition != null;
    }

    /**
     * checks whether an attribute value reader is present
     *
     * @return true if reader is present, false otherwise
     */
    public boolean isAttributeValueReaderProvided() {
        return attributeValueReader != null;
    }

    /**
     * gets the first constraint validation result
     *
     * @return null if there are no results
     */
    public ConstraintValidationResult getFirstConstraintValidationResult() {
        return this.constraintValidationResults.isEmpty() ? null : this.constraintValidationResults.get(0);
    }

    /**
     * @return the constraintValidationResults
     */
    public List<ConstraintValidationResult> getConstraintValidationResults() {
        return this.constraintValidationResults;
    }

    /**
     * @return the definition
     */
    public Constrainable getDefinition() {
        return this.definition;
    }

    /**
     * @return the attributeValueReader
     */
    public AttributeValueReader getAttributeValueReader() {
        return this.attributeValueReader;
    }

    /**
     * @return the constraints
     */
    public List<Constraint> getConstraints() {
        return this.constraints;
    }

}
