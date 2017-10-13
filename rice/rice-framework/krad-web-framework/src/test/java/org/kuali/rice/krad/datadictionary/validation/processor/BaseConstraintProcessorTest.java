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

import org.junit.Before;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.datadictionary.validation.AttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.DictionaryObjectAttributeValueReader;
import org.kuali.rice.krad.datadictionary.validation.ErrorLevel;
import org.kuali.rice.krad.datadictionary.validation.constraint.CaseConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.Constraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.MustOccurConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.PrerequisiteConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.ValidCharactersConstraint;
import org.kuali.rice.krad.datadictionary.validation.constraint.WhenConstraint;
import org.kuali.rice.krad.datadictionary.validation.result.ConstraintValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.datadictionary.validation.result.ProcessorResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BaseConstraintProcessorTest is a base class that is contains a setup method that prepares test data dictionary
 * attribute definitions and constraints
 *
 * <p>Constraints initialized are PrerequisiteConstraint, MustOccurConstraint, CaseConstraint, WhenConstraint, ValidCharactersConstraint</p>
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class BaseConstraintProcessorTest<P extends ConstraintProcessor> {

	protected AttributeDefinition street1Definition;
	protected AttributeDefinition street2Definition;
	protected AttributeDefinition stateDefinition;
	protected AttributeDefinition postalCodeDefinition;
	protected AttributeDefinition countryDefinition;
	protected BusinessObjectEntry addressEntry;
	protected DictionaryValidationResult dictionaryValidationResult;
	protected P processor;

	protected CaseConstraint countryIsUSACaseConstraint;
	protected MustOccurConstraint topLevelConstraint;

	@SuppressWarnings("boxing")
	@Before
	public void setUp() throws Exception {

		processor = newProcessor();

		dictionaryValidationResult = new DictionaryValidationResult();
		dictionaryValidationResult.setErrorLevel(ErrorLevel.NOCONSTRAINT);

		addressEntry = new BusinessObjectEntry();


		List<MustOccurConstraint> mustOccurConstraints = new ArrayList<MustOccurConstraint>();

		PrerequisiteConstraint postalCodeConstraint = new PrerequisiteConstraint();
		postalCodeConstraint.setPropertyName("postalCode");

		PrerequisiteConstraint cityConstraint = new PrerequisiteConstraint();
		cityConstraint.setPropertyName("city");

		PrerequisiteConstraint stateConstraint = new PrerequisiteConstraint();
		stateConstraint.setPropertyName("state");

		List<PrerequisiteConstraint> cityStateDependencyConstraints = new ArrayList<PrerequisiteConstraint>();
		cityStateDependencyConstraints.add(cityConstraint);
		cityStateDependencyConstraints.add(stateConstraint);

		MustOccurConstraint cityStateConstraint = new MustOccurConstraint();
		cityStateConstraint.setMin(2);
		cityStateConstraint.setMax(2);
		cityStateConstraint.setPrerequisiteConstraints(cityStateDependencyConstraints);

		// This basically means that at least one of the two child constraints must be satisfied... either the postal code must be entered or _both_ the city and state
		topLevelConstraint = new MustOccurConstraint();
		topLevelConstraint.setMax(2);
		topLevelConstraint.setMin(1);
		topLevelConstraint.setPrerequisiteConstraints(Collections.singletonList(postalCodeConstraint));
		topLevelConstraint.setMustOccurConstraints(Collections.singletonList(cityStateConstraint));

		mustOccurConstraints.add(topLevelConstraint);

		addressEntry.setMustOccurConstraints(mustOccurConstraints);

		List<WhenConstraint> whenConstraints = new ArrayList<WhenConstraint>();

		PrerequisiteConstraint prerequisiteConstraint = new PrerequisiteConstraint();
		prerequisiteConstraint.setPropertyName("state");

		WhenConstraint whenConstraint1 = new WhenConstraint();
		whenConstraint1.setValue("USA");
		whenConstraint1.setConstraint(prerequisiteConstraint);

		whenConstraints.add(whenConstraint1);

		countryIsUSACaseConstraint = new CaseConstraint();
		countryIsUSACaseConstraint.setCaseSensitive(false);
//		countryIsUSACaseConstraint.setFieldPath("country");
		countryIsUSACaseConstraint.setWhenConstraint(whenConstraints);


		List<AttributeDefinition> attributes = new ArrayList<AttributeDefinition>();

		ValidCharactersConstraint street1ValidCharactersConstraint = new ValidCharactersConstraint();
		street1ValidCharactersConstraint.setValue("regex:\\d{3}\\s+\\w+\\s+Ave");

		street1Definition = new AttributeDefinition();
		street1Definition.setName("street1");
		street1Definition.setValidCharactersConstraint(street1ValidCharactersConstraint);
		attributes.add(street1Definition);

		street2Definition = new AttributeDefinition();
		street2Definition.setName("street2");
		attributes.add(street2Definition);

		AttributeDefinition cityDefinition = new AttributeDefinition();
		cityDefinition.setName("city");
		attributes.add(cityDefinition);

		ValidCharactersConstraint stateValidCharactersConstraint = new ValidCharactersConstraint();
		stateValidCharactersConstraint.setValue("ABCD");

		stateDefinition = new AttributeDefinition();
		stateDefinition.setName("state");
		stateDefinition.setValidCharactersConstraint(stateValidCharactersConstraint);
		attributes.add(stateDefinition);

		postalCodeDefinition = new AttributeDefinition();
		postalCodeDefinition.setName("postalCode");
		postalCodeDefinition.setExclusiveMin("1000");
		postalCodeDefinition.setInclusiveMax("99999");
		postalCodeDefinition.setDataType(DataType.LONG);
		attributes.add(postalCodeDefinition);

		countryDefinition = new AttributeDefinition();
		countryDefinition.setName("country");
		countryDefinition.setCaseConstraint(countryIsUSACaseConstraint);
		attributes.add(countryDefinition);

		addressEntry.setAttributes(attributes);
	}


	protected ConstraintValidationResult process(Object object, String attributeName, Constraint constraint) {
		return processRaw(object, attributeName, constraint).getFirstConstraintValidationResult();
	}

	protected ProcessorResult processRaw(Object object, String attributeName, Constraint constraint) {
		AttributeValueReader attributeValueReader = new DictionaryObjectAttributeValueReader(object, "org.kuali.rice.kns.datadictionary.validation.MockAddress", addressEntry);
		attributeValueReader.setAttributeName(attributeName);

		Object value = attributeName != null ? attributeValueReader.getValue() : object;

		return processor.process(dictionaryValidationResult, value, constraint, attributeValueReader);
	}

	protected abstract P newProcessor();
}
