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
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.CollectionDefinition;
import org.kuali.rice.krad.datadictionary.validation.Address;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.Company;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.Employee;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * Things this test should check:
 *
 * 1. collection size within range (success) {@link #testSimpleCollectionSizeWithinRangeSuccess()}
 * 2. collection size at top of range (success) {@link #testSimpleCollectionSizeAtTopOfRangeSuccess()}
 * 3. collection size at bottom of range (success) {@link #testSimpleCollectionSizeAtBottomOfRangeSuccess()}
 * 4. collection size below range (failure) {@link #testSimpleCollectionSizeBelowRangeFailure()}
 * 5. collection size above range (failure) {@link #testSimpleCollectionSizeAboveRangeFailure()}
 * 6. no range constraints defined (success) {@link #testSimpleCollectionSizeUnconstrainedSuccess()}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CollectionSizeConstraintProcessorTest {

	private CollectionSizeConstraintProcessor processor;

	private CollectionDefinition constrained0to2;
	private CollectionDefinition constrained0to3;
	private CollectionDefinition constrained2to4;
	private CollectionDefinition constrained3to6;
	private CollectionDefinition constrained5to12;
	private CollectionDefinition unconstrained;
	private Company companyWithThreeAddressesAndThreeEmployees;

	@Before
	public void setUp() throws Exception {

		processor = new CollectionSizeConstraintProcessor();

		companyWithThreeAddressesAndThreeEmployees = new Company("3M");

		List<Address> addresses = new ArrayList<Address>();
		addresses.add(new Address("123 Broadway", "Suite 1200", "New York", "NY", "10005", "USA", null));
		addresses.add(new Address("124 Broadway", "Suite 1300", "New York", "NY", "10005", "USA", null));
		addresses.add(new Address("125 Broadway", "Suite 1400", "New York", "NY", "10005", "USA", null));
		companyWithThreeAddressesAndThreeEmployees.setLocations(addresses);

		List<Employee> employees = new ArrayList<Employee>();
		employees.add(new Employee());
		employees.add(new Employee());
		employees.add(new Employee());
		companyWithThreeAddressesAndThreeEmployees.setEmployees(employees);

		constrained0to2 = new CollectionDefinition() {


			@Override
			public String getLabel() {
				return "Employees(s)";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return Integer.valueOf(2);
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return Integer.valueOf(0);
			}
		};

		constrained0to3 = new CollectionDefinition() {

			@Override
			public String getLabel() {
				return "Employee(s)";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return Integer.valueOf(3);
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return Integer.valueOf(0);
			}
		};

		constrained2to4 = new CollectionDefinition() {

			@Override
			public String getLabel() {
				return "Employees";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return Integer.valueOf(4);
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return Integer.valueOf(2);
			}
		};

		constrained3to6 = new CollectionDefinition() {

			@Override
			public String getLabel() {
				return "Employees";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return Integer.valueOf(6);
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return Integer.valueOf(3);
			}
		};

		constrained5to12 = new CollectionDefinition() {

			@Override
			public String getLabel() {
				return "Employee(s)";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return Integer.valueOf(12);
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return Integer.valueOf(5);
			}
		};

		unconstrained = new CollectionDefinition() {

			@Override
			public String getLabel() {
				return "Empployee(s)";
			}

			@Override
			public String getName() {
				return "employees";
			}

			@Override
			public Integer getMaximumNumberOfElements() {
				return null;
			}

			@Override
			public Integer getMinimumNumberOfElements() {
				return null;
			}
		};
	}


	@Test
	public void testSimpleCollectionSizeWithinRangeSuccess() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, constrained2to4, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testSimpleCollectionSizeAtTopOfRangeSuccess() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, constrained0to3, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testSimpleCollectionSizeAtBottomOfRangeSuccess() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, constrained3to6, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	/*
	 * Verifies that a company object with a collection attribute 'contactEmails' that has 3 elements returns a validation error when the collection
	 * size is constrained to be between 5 and 12 elements
	 */
	@Test
	public void testSimpleCollectionSizeBelowRangeFailure() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, constrained5to12, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	/*
	 * Verifies that a company object with a collection attribute 'contactEmails' that has 3 elements returns a validation error when the collection
	 * size is constrained to be between 0 and 2 elements
	 */
	@Test
	public void testSimpleCollectionSizeAboveRangeFailure() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, constrained0to2, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(1, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.ERROR, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	@Test
	public void testSimpleCollectionSizeUnconstrainedSuccess() {
		DictionaryValidationResult dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);
		ConstraintValidationResult result = process(dictionaryValidationResult, companyWithThreeAddressesAndThreeEmployees, unconstrained, "employees");
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.NOCONSTRAINT, result.getStatus());
		Assert.assertEquals(new CollectionSizeConstraintProcessor().getName(), result.getConstraintName());
	}

	private ConstraintValidationResult process(DictionaryValidationResult dictionaryValidationResult, Object object, CollectionDefinition definition, String attributeName) {
		BusinessObjectEntry entry = new BusinessObjectEntry();
		entry.setCollections(Collections.singletonList((CollectionDefinition)definition));

		AttributeValueReader attributeValueReader = new DictionaryObjectAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.Company", entry);
		attributeValueReader.setAttributeName(attributeName);

		Collection<?> value = (Collection<?>)attributeValueReader.getValue();

		return processor.process(dictionaryValidationResult, value, definition, attributeValueReader).getFirstConstraintValidationResult();
	}
}
