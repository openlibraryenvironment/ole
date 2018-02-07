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
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.Employee;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.processor.ValidCharactersConstraintProcessor;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;


/**
 * Things this test should check:
 *
 * 1. empty value check. (failure) {@link #testValueInvalidPhoneNumberEmpty()}
 * 2. value with valid phone number. (success) {@link #testValueValidPhoneNumber()}
 * 3. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber()}
 * 4. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber1()}
 * 5. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber2()}
 * 6. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber3()}
 * 7. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber4()}
 * 8. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber5()}
 * 9. value with invalid phone number. (failure) {@link #testValueInvalidPhoneNumber6()}
 *
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PhoneNumberPatternConstraintTest {

	private final String PATTERN_CONSTRAINT = "validationPatternRegex.phoneNumber";

	private AttributeDefinition contactPhoneDefinition;

	private BusinessObjectEntry addressEntry;
	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private Employee validPhoneEmployee = new Employee();
	private Employee invalidPhoneEmployeeEmpty = new Employee();
	private Employee invalidPhoneEmployee = new Employee();
	private Employee invalidPhoneEmployee1 = new Employee();
	private Employee invalidPhoneEmployee2 = new Employee();
	private Employee invalidPhoneEmployee3 = new Employee();
	private Employee invalidPhoneEmployee4 = new Employee();
	private Employee invalidPhoneEmployee5 = new Employee();
	private Employee invalidPhoneEmployee6 = new Employee();

	private ConfigurationBasedRegexPatternConstraint contactPhoneNumberPatternConstraint;

	@Before
	public void setUp() throws Exception {

		String regex = getProperty(PATTERN_CONSTRAINT);

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		addressEntry = new BusinessObjectEntry();

		List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

		validPhoneEmployee.setContactPhone("056-012-1200");
		invalidPhoneEmployeeEmpty.setContactPhone("");
		invalidPhoneEmployee.setContactPhone("09712248474");
		invalidPhoneEmployee1.setContactPhone("+19712248474");
		invalidPhoneEmployee2.setContactPhone("+1-972-232-3333");
		invalidPhoneEmployee3.setContactPhone("+1-(972)-23334444");
		invalidPhoneEmployee4.setContactPhone("+1-(972)-1223444 xtn.222");
		invalidPhoneEmployee5.setContactPhone("+1 056 012 1200");
		invalidPhoneEmployee6.setContactPhone("056\\-012\\-1200");

		contactPhoneNumberPatternConstraint = new ConfigurationBasedRegexPatternConstraint();
        contactPhoneNumberPatternConstraint.setMessageKey("validate.dummykey");
        contactPhoneNumberPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		contactPhoneNumberPatternConstraint.setValue(regex);

		contactPhoneDefinition = new AttributeDefinition();
		contactPhoneDefinition.setName("contactPhone");
		contactPhoneDefinition.setValidCharactersConstraint(contactPhoneNumberPatternConstraint);
		attributes.add(contactPhoneDefinition);

		addressEntry.setAttributes(attributes);
	}

	@Test
	public void testValueInvalidPhoneNumberEmpty() {
		ConstraintValidationResult result = process(invalidPhoneEmployeeEmpty, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidPhoneNumber() {
		ConstraintValidationResult result = process(validPhoneEmployee, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}


	@Test
	public void testValueInvalidPhoneNumber() {
		ConstraintValidationResult result = process(invalidPhoneEmployee, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber1() {
		ConstraintValidationResult result = process(invalidPhoneEmployee1, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber2() {
		ConstraintValidationResult result = process(invalidPhoneEmployee2, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber3() {
		ConstraintValidationResult result = process(invalidPhoneEmployee3, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber4() {
		ConstraintValidationResult result = process(invalidPhoneEmployee4, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber5() {
		ConstraintValidationResult result = process(invalidPhoneEmployee5, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidPhoneNumber6() {
		ConstraintValidationResult result = process(invalidPhoneEmployee6, "contactPhone", contactPhoneNumberPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(Object object, String attributeName, ValidCharactersConstraint constraint) {
		AttributeValueReader attributeValueReader = new DictionaryObjectAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", addressEntry);
		attributeValueReader.setAttributeName(attributeName);

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
