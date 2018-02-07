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
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.validation.Address;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Things this test should check:
 *
 * 1. value entered, all prerequisites also entered (success) {@link #testAllPrerequisitesSatisfiedSuccess()}
 * 2. value entered, one prerequisite empty (failure) {@link #testOnePrerequisiteMissingFailure()}
 * 3. value entered, all prerequisites empty (failure) {@link #testAllPrerequisitesMissingFailure()}
 * 4. value not entered, all prerequisites entered (no constraint) {@link #testAttributeEmptySkipped()}
 * 5. value entered, no prerequisite defined (exception) {@link #testAttributeWithoutConstraintNoConstraint()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PrerequisiteConstraintProcessorTest {

	private AttributeDefinition street1Definition;
	private AttributeDefinition street2Definition;
	private BusinessObjectEntry addressEntry;
	private DictionaryValidationResult dictionaryValidationResult;

	private PrerequisiteConstraintProcessor processor;

	private Address street1PostalCodeCityStateAddress = new Address("893 Presidential Ave", "Suite 800", "Washington", "DC", "", "USA", null);
	private Address noStreet1PostalCodeAddress = new Address("", "Suite 800", "Washington", "DC", "", "USA", null);
	private Address noStreet1CityStateAddress = new Address("", "Suite 800", "", "", "12340", "USA", null);
	private Address noStreet2Address = new Address("893 Presidential Ave", null, "Washington", "DC", "12340", "USA", null);

	private PrerequisiteConstraint street1Constraint, cityConstraint, stateConstraint;

	@Before
	public void setUp() throws Exception {

		processor = new PrerequisiteConstraintProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		addressEntry = new BusinessObjectEntry();

		street1Constraint = new PrerequisiteConstraint();
		street1Constraint.setPropertyName("street1");

		cityConstraint = new PrerequisiteConstraint();
		cityConstraint.setPropertyName("city");

		stateConstraint = new PrerequisiteConstraint();
		stateConstraint.setPropertyName("state");

		List<PrerequisiteConstraint> prerequisiteConstraints = new ArrayList<PrerequisiteConstraint>();
		prerequisiteConstraints.add(street1Constraint);
		prerequisiteConstraints.add(cityConstraint);
		prerequisiteConstraints.add(stateConstraint);


		List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

		street1Definition = new AttributeDefinition();
		street1Definition.setName("street1");
		attributes.add(street1Definition);

		street2Definition = new AttributeDefinition();
		street2Definition.setName("street2");
		street2Definition.setPrerequisiteConstraints(prerequisiteConstraints);
		attributes.add(street2Definition);

		AttributeDefinition cityDefinition = new AttributeDefinition();
		cityDefinition.setName("city");
		attributes.add(cityDefinition);

		AttributeDefinition stateDefinition = new AttributeDefinition();
		stateDefinition.setName("state");
		attributes.add(stateDefinition);

		AttributeDefinition postalCodeDefinition = new AttributeDefinition();
		postalCodeDefinition.setName("postalCode");
		attributes.add(postalCodeDefinition);

		AttributeDefinition countryDefinition = new AttributeDefinition();
		countryDefinition.setName("country");
		attributes.add(countryDefinition);

		addressEntry.setAttributes(attributes);
	}

	@Test
	public void testAllPrerequisitesSatisfiedSuccess() {
		ConstraintValidationResult result = process(street1PostalCodeCityStateAddress, "street2", street1Constraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new PrerequisiteConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testOnePrerequisiteMissingFailure() {
		ConstraintValidationResult result = process(noStreet1PostalCodeAddress, "street2", street1Constraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new PrerequisiteConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testAllPrerequisitesMissingFailure() {
		ConstraintValidationResult result = process(noStreet1CityStateAddress, "street2", street1Constraint);
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		result = process(noStreet1CityStateAddress, "street2", cityConstraint);
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		result = process(noStreet1CityStateAddress, "street2", stateConstraint);
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());

		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(3, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(new PrerequisiteConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testAttributeEmptySkipped() {
		ConstraintValidationResult result = process(noStreet2Address, "street2", street1Constraint);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new PrerequisiteConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testAttributeWithoutConstraintNoConstraint() {
		ConstraintValidationResult result = process(noStreet2Address, "street1", null);
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.NOCONSTRAINT, result.getStatus());
		Assert.assertEquals(new PrerequisiteConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(Object object, String attributeName, PrerequisiteConstraint constraint) {
		AttributeValueReader attributeValueReader = new DictionaryObjectAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", addressEntry);
		attributeValueReader.setAttributeName(attributeName);

		Object value = attributeValueReader.getValue();
		return processor.process(dictionaryValidationResult, value, constraint, attributeValueReader).getFirstConstraintValidationResult();
	}

}
