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
package org.kuali.rice.krad.datadictionary.validation.constraint;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.SingleAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.processor.ValidCharactersConstraintProcessor;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;


/**
 * Things this test should check:
 *
 * 1. empty value check. (failure) {@link #testValueInvalidMonthEmpty()}
 * 2. value with valid month. (success) {@link #testValueValidMonth()}
 * 3. value with valid month. (success) {@link #testValueValidMonth1()}
 * 4. value with valid month. (success) {@link #testValueValidMonth2()}
 * 5. value with invalid month. (failure) {@link #testValueInvalidMonth()}
 * 6. value with invalid month. (failure) {@link #testValueInvalidMonth1()}
 * 7. value with invalid month. (failure) {@link #testValueInvalidMonth2()}
 * 8. value with invalid month. (failure) {@link #testValueInvalidMonth3()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MonthPatternConstraintTest {

	private final String PATTERN_CONSTRAINT = "validationPatternRegex.month";

	private AttributeDefinition monthDefinition;

	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private String validMonth;
	private String validMonth1;
	private String validMonth2;
	private String invalidMonthEmpty;
	private String invalidMonth;
	private String invalidMonth1;
	private String invalidMonth2;
	private String invalidMonth3;

	private ConfigurationBasedRegexPatternConstraint monthPatternConstraint;

	@Before
	public void setUp() throws Exception {

		String regex = getProperty(PATTERN_CONSTRAINT);

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		validMonth = "1";
		validMonth1 = "05";
		validMonth2 = "12";
		invalidMonthEmpty = "";
		invalidMonth = "00";
		invalidMonth1 = "0";
		invalidMonth2 = "13";
		invalidMonth3 = "JAN";

		monthPatternConstraint = new ConfigurationBasedRegexPatternConstraint();
        monthPatternConstraint.setMessageKey("validate.dummykey");
        monthPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		monthPatternConstraint.setValue(regex);

		monthDefinition = new AttributeDefinition();
		monthDefinition.setName("month");
		monthDefinition.setValidCharactersConstraint(monthPatternConstraint);
	}

	@Test
	public void testValueInvalidMonthEmpty() {
		ConstraintValidationResult result = process(invalidMonthEmpty, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidMonth() {
		ConstraintValidationResult result = process(validMonth, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidMonth1() {
		ConstraintValidationResult result = process(validMonth1, "Mmonth", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidMonth2() {
		ConstraintValidationResult result = process(validMonth2, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidMonth() {
		ConstraintValidationResult result = process(invalidMonth, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidMonth1() {
		ConstraintValidationResult result = process(invalidMonth1, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidMonth2() {
		ConstraintValidationResult result = process(invalidMonth2, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidMonth3() {
		ConstraintValidationResult result = process(invalidMonth3, "month", monthPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(Object object, String attributeName, ValidCharactersConstraint constraint) {
		AttributeValueReader attributeValueReader = new SingleAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", attributeName,  monthDefinition);
		Object value = attributeValueReader.getValue();
		return processor.process(dictionaryValidationResult, value, constraint, attributeValueReader).getFirstConstraintValidationResult();
	}

	private String getProperty(String key) {
		String value = null;
		String filePath = "org/kuali/rice/krad/ApplicationResources.properties";
		Properties properties = new Properties();
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
			properties.load(in);
			value = properties.getProperty(key);
		} catch (IOException e) {
		}
		return value;
	}
}
