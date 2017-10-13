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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
 * 1. value with all valid characters. (success) {@link #testValueAllValidChars()}
 * 2. value with invalid characters. (failure) {@link #testValueNotValidChars()}
 * 3. value with all valid characters. Allowing white space.(success) {@link #testValueAllValidCharsAllowWhitespace()}
 * 4. value with invalid characters. Allowing white space. (failure) {@link #testValueNotValidCharsAllowWhitespace()}
 * 5. value with all valid characters. Allowing white space, period, underscore and parenthesis. (success) {@link #testValueAllValidCharsAllowWhitespaceAndPeriodAndUnderscoreAndParenthesis()}
 * 6. value with invalid characters. Allowing white space, period, underscore and parenthesis. (failure) {@link #testValueNotValidCharsAllowWhitespaceAndPeriodAndUnderscoreAndParenthesis()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AlphaNumericPatternConstraintTest {

	private AttributeDefinition street1Definition;
	private AttributeDefinition street2Definition;
	private AttributeDefinition postalCodeDefinition;

	private BusinessObjectEntry addressEntry;
	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private Address washingtonDCAddress = new Address("893 Presidential Ave", "(A_123) Suite 800.", "Washington", "DC", "NHW123A", "USA", null);
	private Address newYorkNYAddress = new Address("Presidential Street", "(A-123) Suite 800", "New York", "NY", "ZH 3456", "USA", null);
	private Address sydneyAUSAddress = new Address("Presidential Street-Ave.", "Suite_800.", "Sydney", "ZZ", "ZH-5656", "USA", null);

	private AlphaNumericPatternConstraint street1AlphaNumericPatternConstraint;
	private AlphaNumericPatternConstraint street2AlphaNumericPatternConstraint;
	private AlphaNumericPatternConstraint postalCodeAlphaNumericPatternConstraint;

	@Before
	public void setUp() throws Exception {

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		addressEntry = new BusinessObjectEntry();

		List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

		street1AlphaNumericPatternConstraint = new AlphaNumericPatternConstraint();
        street1AlphaNumericPatternConstraint.setMessageKey("validate.dummykey");
        street1AlphaNumericPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		street1AlphaNumericPatternConstraint.setAllowWhitespace(true);

		street1Definition = new AttributeDefinition();
		street1Definition.setName("street1");
		street1Definition.setValidCharactersConstraint(street1AlphaNumericPatternConstraint);
		attributes.add(street1Definition);


		street2AlphaNumericPatternConstraint = new AlphaNumericPatternConstraint();
        street2AlphaNumericPatternConstraint.setMessageKey("validate.dummykey");
        street2AlphaNumericPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		street2AlphaNumericPatternConstraint.setAllowWhitespace(true);
		street2AlphaNumericPatternConstraint.setAllowParenthesis(true);
		street2AlphaNumericPatternConstraint.setAllowPeriod(true);
		street2AlphaNumericPatternConstraint.setAllowUnderscore(true);

		street2Definition = new AttributeDefinition();
		street2Definition.setName("street2");
		street2Definition.setValidCharactersConstraint(street2AlphaNumericPatternConstraint);
		attributes.add(street2Definition);

		postalCodeAlphaNumericPatternConstraint = new AlphaNumericPatternConstraint();
        postalCodeAlphaNumericPatternConstraint.setMessageKey("validate.dummykey");
        postalCodeAlphaNumericPatternConstraint.setValidationMessageParams( new ArrayList<String>());

		postalCodeDefinition = new AttributeDefinition();
		postalCodeDefinition.setName("postalCode");
		postalCodeDefinition.setValidCharactersConstraint(postalCodeAlphaNumericPatternConstraint);
		attributes.add(postalCodeDefinition);

		addressEntry.setAttributes(attributes);
	}

	@Test
	public void testValueAllValidChars() {
		ConstraintValidationResult result = process(washingtonDCAddress, "postalCode", postalCodeAlphaNumericPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueNotValidChars() {
		ConstraintValidationResult result = process(newYorkNYAddress, "postalCode", postalCodeAlphaNumericPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueAllValidCharsAllowWhitespace() {
		ConstraintValidationResult result = process(newYorkNYAddress, "street1", street1AlphaNumericPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueNotValidCharsAllowWhitespace() {
		ConstraintValidationResult result = process(sydneyAUSAddress, "street1", street1AlphaNumericPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueAllValidCharsAllowWhitespaceAndPeriodAndUnderscoreAndParenthesis() {
		ConstraintValidationResult result = process(washingtonDCAddress, "street2", street2AlphaNumericPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueNotValidCharsAllowWhitespaceAndPeriodAndUnderscoreAndParenthesis() {
		ConstraintValidationResult result = process(newYorkNYAddress, "street2", street2AlphaNumericPatternConstraint);
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
}
