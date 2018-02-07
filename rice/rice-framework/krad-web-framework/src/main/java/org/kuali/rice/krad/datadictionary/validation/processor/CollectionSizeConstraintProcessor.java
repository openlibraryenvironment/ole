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
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils;
import org.kuali.rice.krad.datadictionary.validation.ValidationUtils.Result;
import org.kuali.rice.krad.datadictionary.validation.constraint.CollectionSizeConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

import java.util.Collection;

/**
 * This class validates attributes that are collection size constrained - ones that can only have between x and y
 * number
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionSizeConstraintProcessor implements CollectionConstraintProcessor<Collection<?>, CollectionSizeConstraint> {

    private static final String CONSTRAINT_NAME = "collection size constraint";

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#process(org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult,
     *      Object, org.kuali.rice.krad.datadictionary.validation.constraint.Constraint,
     *      org.kuali.rice.krad.datadictionary.validation.AttributeValueReader)
     */
    @Override
    public ProcessorResult process(DictionaryValidationResult result, Collection<?> collection,
            CollectionSizeConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {

        // To accommodate the needs of other processors, the ConstraintProcessor.process() method returns a list of ConstraintValidationResult objects
        // but since a definition that is collection size constrained only provides a single max and minimum, there is effectively a single constraint
        // being imposed.
        return new ProcessorResult(processSingleCollectionSizeConstraint(result, collection, constraint,
                attributeValueReader));
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
        return CollectionSizeConstraint.class;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.validation.processor.ConstraintProcessor#isOptional()
     */
    @Override
    public boolean isOptional() {
        return false;
    }

    protected ConstraintValidationResult processSingleCollectionSizeConstraint(DictionaryValidationResult result,
            Collection<?> collection, CollectionSizeConstraint constraint,
            AttributeValueReader attributeValueReader) throws AttributeValidationException {
        Integer sizeOfCollection = new Integer(0);
        if (collection != null) {
            sizeOfCollection = Integer.valueOf(collection.size());
        }

        Integer maxOccurances = constraint.getMaximumNumberOfElements();
        Integer minOccurances = constraint.getMinimumNumberOfElements();

        Result lessThanMax = ValidationUtils.isLessThanOrEqual(sizeOfCollection, maxOccurances);
        Result greaterThanMin = ValidationUtils.isGreaterThanOrEqual(sizeOfCollection, minOccurances);

        // It's okay for one end of the range to be undefined - that's not an error. It's only an error if one of them is invalid
        if (lessThanMax != Result.INVALID && greaterThanMin != Result.INVALID) {
            // Of course, if they're both undefined then we didn't actually have a real constraint
            if (lessThanMax == Result.UNDEFINED && greaterThanMin == Result.UNDEFINED) {
                return result.addNoConstraint(attributeValueReader, CONSTRAINT_NAME);
            }

            // In this case, we've succeeded
            return result.addSuccess(attributeValueReader, CONSTRAINT_NAME);
        }

        String maxErrorParameter = maxOccurances != null ? maxOccurances.toString() : null;
        String minErrorParameter = minOccurances != null ? minOccurances.toString() : null;

        // If both comparisons happened then if either comparison failed we can show the end user the expected range on both sides.
        if (lessThanMax != Result.UNDEFINED && greaterThanMin != Result.UNDEFINED) {
            return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_QUANTITY_RANGE,
                    minErrorParameter, maxErrorParameter);
        }
        // If it's the max comparison that fails, then just tell the end user what the max can be
        else if (lessThanMax == Result.INVALID) {
            return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_MAX_OCCURS,
                    maxErrorParameter);
        }
        // Otherwise, just tell them what the min can be
        else {
            return result.addError(attributeValueReader, CONSTRAINT_NAME, RiceKeyConstants.ERROR_MIN_OCCURS,
                    minErrorParameter);
        }

        // Obviously the last else above is unnecessary, since anything after it is dead code, but keeping it seems clearer than dropping it
    }

}
