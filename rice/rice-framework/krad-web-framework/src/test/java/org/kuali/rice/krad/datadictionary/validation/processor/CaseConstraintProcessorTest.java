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
import org.junit.Test;
import org.kuali.rice.krad.datadictionary.validation.Address;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

import java.util.List;



/**
 * CaseConstraintProcessorTest tests that CaseConstraintProcessor works as expected
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CaseConstraintProcessorTest extends BaseConstraintProcessorTest<CaseConstraintProcessor> {

	private Address londonAddress = new Address("812 Maiden Lane", "", "London", "", "", "UK", null);
	private Address noStateAddress = new Address("893 Presidential Ave", "Suite 800", "Washington", "", "92342", "USA", null);


	@Test
	public void testCaseConstraintNotInvoked() {
		ProcessorResult processorResult = processRaw(londonAddress, "country", countryIsUSACaseConstraint);
		ConstraintValidationResult result = processorResult.getFirstConstraintValidationResult();
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.INAPPLICABLE, result.getStatus());
		Assert.assertEquals(new CaseConstraintProcessor().getName(), result.getConstraintName());

		List<Constraint> constraints = processorResult.getConstraints();

		Assert.assertNotNull(constraints);
		Assert.assertEquals(0, constraints.size());
	}

	@Test
	public void testCaseConstraintInvoked() {
		ProcessorResult processorResult = processRaw(noStateAddress, "country", countryIsUSACaseConstraint);

		List<Constraint> constraints = processorResult.getConstraints();

		Assert.assertNotNull(constraints);
		Assert.assertEquals(1, constraints.size());

		Constraint constraint = constraints.get(0);

		Assert.assertTrue(constraint instanceof PrerequisiteConstraint);

		PrerequisiteConstraint prerequisiteConstraint = (PrerequisiteConstraint)constraint;

		Assert.assertEquals("state", prerequisiteConstraint.getPropertyName());

		ConstraintValidationResult result = processorResult.getFirstConstraintValidationResult();
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfWarnings());
		Assert.assertEquals(0, dictionaryValidationResult.getNumberOfErrors());
		Assert.assertEquals(ErrorLevel.OK, result.getStatus());
		Assert.assertEquals(new CaseConstraintProcessor().getName(), result.getConstraintName());
	}

	/**
	 * @see BaseConstraintProcessorTest#newProcessor()
	 */
	@Override
	protected CaseConstraintProcessor newProcessor() {
		return new CaseConstraintProcessor();
	}

}
