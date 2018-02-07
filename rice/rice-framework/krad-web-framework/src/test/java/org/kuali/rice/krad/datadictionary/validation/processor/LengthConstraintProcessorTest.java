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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.Company;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;

import java.util.Collections;

/**
 * Things this test should check:
 *
 * 1. string length within range (success) {@link #testNameWithinRangeSuccess()}
 * 2. string length at top of range (success) {@link #testNameAtTopOfRangeFailure()}
 * 3. string length at bottom of range (success) {@link #testNameAtBottomOfRangeSuccess()}
 * 4. string length below range (failure) {@link #testNameLengthBelowRangeFailure()}
 * 5. string length above range (failure) {@link #testNameLengthAboveRangeFailure()}
 * 6. no length constraints defined (success) {@link #testNameLengthUnconstrainedSuccess()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LengthConstraintProcessorTest {
    private LengthConstraintProcessor processor;

    private AttributeDefinition constrained0to2;
    private AttributeDefinition constrained0to3;
    private AttributeDefinition constrained2to4;
    private AttributeDefinition constrained3to6;
    private AttributeDefinition constrained5to12;
    private AttributeDefinition unconstrained;

    private Company companyWith3LetterName;

    @Before
    public void setUp() throws Exception {

        processor = new LengthConstraintProcessor();

        companyWith3LetterName = new Company("ABC");

        constrained0to2 = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(2);
                setMinLength(0);
            }

        };

        constrained0to3 = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(3);
                setMinLength(0);
            }
        };

        constrained2to4 = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(4);
                setMinLength(2);
            }
        };

        constrained3to6 = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(6);
                setMinLength(3);
            }
        };

        constrained5to12 = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(5);
                setMinLength(12);
            }
        };

        unconstrained = new AttributeDefinition() {

            @Override
            public DataType getDataType() {
                return DataType.STRING;
            }

            @Override
            public String getLabel() {
                return "Company Name";
            }

            @Override
            public String getName() {
                return "name";
            }

            {
                setMaxLength(null);
                setMinLength(null);
            }
        };
    }

    @Test
    public void testNameWithinRangeSuccess() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName, constrained2to4,
                "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.OK, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    /**
     * Since the top of the range is
     */
    @Test
    public void testNameAtTopOfRangeFailure() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName, constrained0to3,
                "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.OK, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    @Test
    public void testNameAtBottomOfRangeSuccess() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName, constrained3to6,
                "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.OK, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    /**
     * Verifies that a company object with a collection attribute 'contactEmails' that has 3 elements returns a
     * validation error when the collection
     * size is constrained to be between 5 and 12 element
     */
    @Test
    public void testNameLengthBelowRangeFailure() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName,
                constrained5to12, "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    /* * Verifies that a company object with a collection attribute 'contactEmails' that has 3 elements returns a validation error when the collection
      * size is constrained to be between 0 and 2 elements
     */
    @Test
    public void testNameLengthAboveRangeFailure() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName, constrained0to2,
                "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    @Test
    public void testNameLengthUnconstrainedSuccess() {
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
        ConstraintValidationResult result = process(dictionaryValidationResult, companyWith3LetterName, unconstrained,
                "name");
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(ErrorLevel.NOCONSTRAINT, result.getStatus());
        Assert.assertEquals(new LengthConstraintProcessor().getName(), result.getConstraintName());
    }

    private ConstraintValidationResult process(DictionaryValidationResult dictionaryValidationResult, Object object,
            AttributeDefinition definition, String attributeName) {
        BusinessObjectEntry entry = new BusinessObjectEntry();
        entry.setAttributes(Collections.singletonList(definition));

        AttributeValueReader attributeValueReader = new DictionaryObjectAttributeValueReader(object,
                "org.kuali.rice.kns.datadictionary.validation.MockCompany", entry);
        attributeValueReader.setAttributeName(attributeName);

        Object value = attributeValueReader.getValue();

        return processor.process(dictionaryValidationResult, value, definition.getSimpleConstraint(),
                attributeValueReader).getFirstConstraintValidationResult();
    }
}
