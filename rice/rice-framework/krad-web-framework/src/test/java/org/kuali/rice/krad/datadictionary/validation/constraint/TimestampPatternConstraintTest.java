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
 * 1. empty value check. (failure) {@link #testValueInvalidTimestampEmpty()}
 * 2. value with valid timestamp. (success) {@link #testValueValidTimestamp()}
 * 3. value with valid timestamp. (success) {@link #testValueValidTimestamp1()}
 * 4. value with invalid timestamp. (failure) {@link #testValueInvalidTimestamp()}
 * 5. value with invalid timestamp. (failure) {@link #testValueInvalidTimestamp1()}
 * 6. value with invalid timestamp. (failure) {@link #testValueInvalidTimestamp2()}
 * 7. value with invalid timestamp. (failure) {@link #testValueInvalidTimestamp3()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TimestampPatternConstraintTest {

	private final String PATTERN_CONSTRAINT = "validationPatternRegex.timestamp";

	private AttributeDefinition timestampDefinition;

	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private String validTimestamp;
	private String validTimestamp1;
	private String invalidTimestampEmpty;
	private String invalidTimestamp;
	private String invalidTimestamp1;
	private String invalidTimestamp2;
	private String invalidTimestamp3;

	private ConfigurationBasedRegexPatternConstraint timestampPatternConstraint;

	@Before
	public void setUp() throws Exception {

		String regex = getProperty(PATTERN_CONSTRAINT);

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		validTimestamp = "2011-07-28 15:10:36.300";
		validTimestamp1 = "1936-07-28 15:10:36.9999999";
		invalidTimestampEmpty = "";
		invalidTimestamp = "2011/07/28 15:10:36.300";
		invalidTimestamp1 = "2011-07-28 15:10:36.300 IST";
		invalidTimestamp2 = "2011-07-28";
		invalidTimestamp3 = "28-07-2011 15:10:36.300";

		timestampPatternConstraint = new ConfigurationBasedRegexPatternConstraint();
        timestampPatternConstraint.setMessageKey("validate.dummykey");
        timestampPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		timestampPatternConstraint.setValue(regex);

		timestampDefinition = new AttributeDefinition();
		timestampDefinition.setName("timestamp");
		timestampDefinition.setValidCharactersConstraint(timestampPatternConstraint);
	}

	@Test
	public void testValueInvalidTimestampEmpty() {
		ConstraintValidationResult result = process(invalidTimestampEmpty, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidTimestamp() {
		ConstraintValidationResult result = process(validTimestamp, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidTimestamp1() {
		ConstraintValidationResult result = process(validTimestamp1, "Mtimestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidTimestamp() {
		ConstraintValidationResult result = process(invalidTimestamp, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidTimestamp1() {
		ConstraintValidationResult result = process(invalidTimestamp1, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidTimestamp2() {
		ConstraintValidationResult result = process(invalidTimestamp2, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidTimestamp3() {
		ConstraintValidationResult result = process(invalidTimestamp3, "timestamp", timestampPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(Object object, String attributeName, ValidCharactersConstraint constraint) {
		AttributeValueReader attributeValueReader = new SingleAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", attributeName,  timestampDefinition);
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
