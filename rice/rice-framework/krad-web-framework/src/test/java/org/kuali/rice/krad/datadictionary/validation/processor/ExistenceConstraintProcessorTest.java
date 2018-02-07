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
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.validation.Address;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.SingleAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;

import java.util.Iterator;

/**
 * Things this test should check:
 *
 * 1. presence of a required field value (success) {@link #testPresenceOfRequiredSingleAttributeSuccess()}
 * 2. absence of a required field value (failure) {@link #testAbsenceOfRequiredSingleAttributeFailure()}
 * RequiredSingleAttributeFailure}
 * 3. presence of an unrequired field value (success) {@link #testPresenceNotRequiredSingleAttributeSuccess}
 * 4. absence of an unrequired field value (success) {@link #testAbsenceNotRequiredSingleAttributeSuccess}
 * 5. presence of a no constraint field value (success) {@link #testPresenceNoConstraintSingleAttributeSuccess}
 * 6. absence of a no constraint field value (success) {@link #testAbsenceNoConstraintSingleAttributeSuccess}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExistenceConstraintProcessorTest {

    private AttributeDefinition cityRequiredDefinition;
    private AttributeDefinition countryNotRequiredDefinition;
    private AttributeDefinition countryNoConstraintDefinition;
    private ExistenceConstraintProcessor processor;

    private Address noPostalCodeOrCityAddress = new Address("893 Presidential Ave", "Suite 800", "", "", "12340", "USA",
            null);
    private Address noPostalCodeAddress = new Address("893 Presidential Ave", "Suite 800", "Washington", "", "12340",
            "USA", null);
    private Address noPostalCodeOrCountryAddress = new Address("893 Presidential Ave", "Suite 800", "Washington", "",
            "12340", "", null);

    @Before
    public void setUp() throws Exception {
        cityRequiredDefinition = new AttributeDefinition() {

            @Override
            public String getLabel() {
                return "City";
            }

            @Override
            public String getName() {
                return "city";
            }

            {
                setDataType((DataType) null);
                setRequired(true);
            }

        };

        countryNotRequiredDefinition = new AttributeDefinition() {

            @Override
            public String getLabel() {
                return "Country";
            }

            @Override
            public String getName() {
                return "country";
            }

            {
                setDataType(DataType.STRING);
                setRequired(false);
            }

        };

        countryNoConstraintDefinition = new AttributeDefinition() {

            @Override
            public String getLabel() {
                return "Country";
            }

            @Override
            public String getName() {
                return "country";
            }

            {
                setDataType(DataType.STRING);
                setRequired(null);
            }

        };

        processor = new ExistenceConstraintProcessor();
    }

    @Test
    public void testPresenceOfRequiredSingleAttributeSuccess() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(noPostalCodeAddress.getCity(),
                "org.kuali.rice.kns.datadictionary.validation.MockAddress", "city", cityRequiredDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        // This means that we do track everything above and including 'ok' results
        dictionaryValidationResult.setErrorLevel(ErrorLevel.OK);
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                cityRequiredDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is OK
        Assert.assertEquals(ErrorLevel.OK, constraintValidationResult.getStatus());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
    }

    @Test
    public void testAbsenceOfRequiredSingleAttributeFailure() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(noPostalCodeOrCityAddress.getCity(),
                "org.kuali.rice.kns.datadictionary.validation.MockAddress", "city", cityRequiredDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                cityRequiredDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is ERROR
        Assert.assertEquals(ErrorLevel.ERROR, constraintValidationResult.getStatus());
        Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
        Assert.assertEquals(RiceKeyConstants.ERROR_REQUIRED, constraintValidationResult.getErrorKey());

        // Make sure that the iterator works too
        int countConstraints = 0;
        if (dictionaryValidationResult.getNumberOfErrors() > 0) {
            for (Iterator<ConstraintValidationResult> iterator = dictionaryValidationResult.iterator();
                 iterator.hasNext(); ) {
                ConstraintValidationResult r = iterator.next();
                if (r.getStatus().getLevel() >= ErrorLevel.WARN.getLevel()) {
                    countConstraints++;
                }
            }
        }
        Assert.assertEquals(1, countConstraints);
    }

    @Test
    public void testPresenceNotRequiredSingleAttributeSuccess() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(noPostalCodeAddress.getCountry(),
                "org.kuali.rice.kns.datadictionary.validation.MockAddress", "country", countryNotRequiredDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        // This means that we do track everything above and including 'ok' results
        dictionaryValidationResult.setErrorLevel(ErrorLevel.OK);
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                countryNotRequiredDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is OK
        Assert.assertEquals(ErrorLevel.INAPPLICABLE, constraintValidationResult.getStatus());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
    }

    @Test
    public void testAbsenceNotRequiredSingleAttributeSuccess() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(
                noPostalCodeOrCountryAddress.getCountry(), "org.kuali.rice.kns.datadictionary.validation.MockAddress",
                "country", countryNotRequiredDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        // This means that we do track everything above and including 'ok' results
        dictionaryValidationResult.setErrorLevel(ErrorLevel.OK);
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                countryNotRequiredDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is OK
        Assert.assertEquals(ErrorLevel.INAPPLICABLE, constraintValidationResult.getStatus());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
    }

    @Test
    public void testPresenceNoConstraintSingleAttributeSuccess() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(noPostalCodeAddress.getCountry(),
                "org.kuali.rice.kns.datadictionary.validation.MockAddress", "country", countryNoConstraintDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        // This means that we do track everything above and including 'ok' results
        dictionaryValidationResult.setErrorLevel(ErrorLevel.OK);
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                countryNoConstraintDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is OK
        Assert.assertEquals(ErrorLevel.NOCONSTRAINT, constraintValidationResult.getStatus());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
    }

    @Test
    public void testAbsenceNoConstraintSingleAttributeSuccess() {
        AttributeValueReader attributeValueReader = new SingleAttributeValueReader(
                noPostalCodeOrCountryAddress.getCountry(), "org.kuali.rice.kns.datadictionary.validation.MockAddress",
                "country", countryNoConstraintDefinition);
        Object value = attributeValueReader.getValue();
        DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
        // This means that we do track everything above and including 'ok' results
        dictionaryValidationResult.setErrorLevel(ErrorLevel.OK);
        ConstraintValidationResult constraintValidationResult = processor.process(dictionaryValidationResult, value,
                countryNoConstraintDefinition.getSimpleConstraint(), attributeValueReader)
                .getFirstConstraintValidationResult();

        // Make sure that the constraint we were looking for got run
        Assert.assertEquals(new ExistenceConstraintProcessor().getName(),
                constraintValidationResult.getConstraintName());
        // Make sure that it's status is OK
        Assert.assertEquals(ErrorLevel.NOCONSTRAINT, constraintValidationResult.getStatus());
        Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
    }
}
