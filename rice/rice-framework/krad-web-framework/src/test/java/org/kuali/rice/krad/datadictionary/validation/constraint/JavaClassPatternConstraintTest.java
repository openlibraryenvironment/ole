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
 * 1. empty value check. (failure) {@link #testValueInvalidJavaClassEmpty()}
 * 2. value with valid java class. (success) {@link #testValueValidJavaClass()}
 * 3. value with valid java class. (success) {@link #testValueValidJavaClass1()}
 * 4. value with valid java class. (success) {@link #testValueValidJavaClass2()}
 * 5. value with invalid java class. (failure) {@link #testValueInvalidJavaClass()}
 * 6. value with invalid java class. (failure) {@link #testValueInvalidJavaClass1()}
 * 7. value with invalid java class. (failure) {@link #testValueInvalidJavaClass2()}
 * 8. value with invalid java class. (failure) {@link #testValueInvalidJavaClass3()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JavaClassPatternConstraintTest {

	private final String PATTERN_CONSTRAINT = "validationPatternRegex.javaClass";

	private AttributeDefinition javaClassDefinition;

	private DictionaryValidationResult dictionaryValidationResult;

	private ValidCharactersConstraintProcessor processor;

	private String validJavaClass;
	private String validJavaClass1;
	private String validJavaClass2;
	private String invalidJavaClassEmpty;
	private String invalidJavaClass;
	private String invalidJavaClass1;
	private String invalidJavaClass2;
	private String invalidJavaClass3;

	private ConfigurationBasedRegexPatternConstraint javaClassPatternConstraint;

	@Before
	public void setUp() throws Exception {

		String regex = getProperty(PATTERN_CONSTRAINT);

		processor = new ValidCharactersConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		validJavaClass = Integer.class.getName();
		validJavaClass1 = "org.kuali.rice.krad.datadictionary.validation.constraint.JavaClassPattternConstraintTest";
		validJavaClass2 = "String";
		invalidJavaClassEmpty = "";
		invalidJavaClass = "123.mypackage.com";
		invalidJavaClass1 = "mypackage.com.";
		invalidJavaClass2 = "123 mypackage";
		invalidJavaClass3 = "something.mypackage:com";

		javaClassPatternConstraint = new ConfigurationBasedRegexPatternConstraint();
        javaClassPatternConstraint.setMessageKey("validate.dummykey");
        javaClassPatternConstraint.setValidationMessageParams( new ArrayList<String>());
		javaClassPatternConstraint.setValue(regex);

		javaClassDefinition = new AttributeDefinition();
		javaClassDefinition.setName("javaClass");
		javaClassDefinition.setValidCharactersConstraint(javaClassPatternConstraint);
	}

	@Test
	public void testValueInvalidJavaClassEmpty() {
		ConstraintValidationResult result = process(invalidJavaClassEmpty, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidJavaClass() {
		ConstraintValidationResult result = process(validJavaClass, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidJavaClass1() {
		ConstraintValidationResult result = process(validJavaClass1, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueValidJavaClass2() {
		ConstraintValidationResult result = process(validJavaClass2, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidJavaClass() {
		ConstraintValidationResult result = process(invalidJavaClass, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidJavaClass1() {
		ConstraintValidationResult result = process(invalidJavaClass1, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidJavaClass2() {
		ConstraintValidationResult result = process(invalidJavaClass2, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testValueInvalidJavaClass3() {
		ConstraintValidationResult result = process(invalidJavaClass3, "javaClass", javaClassPatternConstraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new ValidCharactersConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(Object object, String attributeName, ValidCharactersConstraint constraint) {
		AttributeValueReader attributeValueReader = new SingleAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", attributeName,  javaClassDefinition);
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
