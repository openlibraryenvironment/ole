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
import org.kuali.rice.krad.datadictionary.validation.Address;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.processor.ValidCharactersConstraintProcessor;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;


/**
 * Things this test should check:
 *
 * 1. value with valid zipcode. (success) {@link #testValueValidZipcode()}
 * 2. value with valid zipcode. (success) {@link #testValueValidZipcode1()}
 * 3. value with invalid zipcode. (failure) {@link #testValueInvalidZipcode()}
 * 4. value with invalid zipcode. (failure) {@link #testValueInvalidZipcode1()}
 * 5. value with invalid zipcode. (failure) {@link #testValueInvalidZipcode2()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ZipcodePatternConstraintTest {

	private final String PATTERN_CONSTRAINT = "validationPatternRegex.zipcode";

	private AttributeDefinition postalCodeDefinition;

	private BusinessObjectEntry addressEntry;
	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private Address washingtonDCAddress = new Address("893 Presidential Ave", "(A_123) Suite 800.", "Washington", "DC", "12345", "USA", null);
	private Address newYorkNYAddress = new Address("Presidential Street", "(A-123) Suite 800", "New York", "NY", "12345-1234", "USA", null);
	private Address sydneySDAddress = new Address("Presidential Street-Ave.", "Suite_800.", "Sydney", "SD", "ZH-123456", "Australia", null);
	private Address holandHDAddress = new Address("Presidential Street-Ave.", "Suite_800.", "Holand", "HD", "12345-123", "Holand", null);
	private Address bombayMHAddress = new Address("Presidential Street-Ave.", "Suite_800.", "Bombay", "MH", "380002", "India", null);

	private ConfigurationBasedRegexPatternConstraint postalCodePatternConstraint;

	@Before
	public void setUp() throws Exception {

		String regex = getProperty(PATTERN_CONSTRAINT);

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		addressEntry = new BusinessObjectEntry();

		List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

		postalCodePatternConstraint = new ConfigurationBasedRegexPatternConstraint();
        postalCodePatternConstraint.setMessageKey("validate.dummykey");
        postalCodePatternConstraint.setValidationMessageParams( new ArrayList<String>());
		postalCodePatternConstraint.setValue(regex);

		postalCodeDefinition = new AttributeDefinition();
		postalCodeDefinition.setName("postalCode");
		postalCodeDefinition.setValidCharactersConstraint(postalCodePatternConstraint);
		attributes.add(postalCodeDefinition);

		addressEntry.setAttributes(attributes);
	}

	@Test
	public void testValueValidZipcode() {
		ConstraintValidationResult result = process(washingtonDCAddress, "postalCode", postalCodePatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidZipcode1() {
		ConstraintValidationResult result = process(newYorkNYAddress, "postalCode", postalCodePatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidZipcode() {
		ConstraintValidationResult result = process(sydneySDAddress, "postalCode", postalCodePatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidZipcode1() {
		ConstraintValidationResult result = process(holandHDAddress, "postalCode", postalCodePatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidZipcode2() {
		ConstraintValidationResult result = process(bombayMHAddress, "postalCode", postalCodePatternConstraint);
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
