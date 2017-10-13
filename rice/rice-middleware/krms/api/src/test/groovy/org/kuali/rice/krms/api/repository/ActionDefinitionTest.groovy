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
package org.kuali.rice.krms.api.repository

import java.util.List

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.krms.api.repository.action.ActionDefinition;


/**
 * This test is for the Action Definition!
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class ActionDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_TEST"
	private static final String TYPE_ID = "1234XYZ"
	private static final String RULE_ID_1 = "RULEID001"
	
	private static final String ACTION_ID_1 = "ACTIONID01"
	private static final String ACTION_NAME_1 = "Say Hello"
	private static final String ACTION_DESCRIPTION_1 = "Send spam email"
	private static final Integer SEQUENCE_1 = new Integer(1)

	private static final String ACTION_1 = """
<action xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/krms/v2_0">
    <id>ACTIONID01</id>
    <name>Say Hello</name>
    <namespace>KRMS_TEST</namespace>
    <description>Send spam email</description>
    <typeId>1234XYZ</typeId>
    <ruleId>RULEID001</ruleId>
    <sequenceNumber>1</sequenceNumber>
    <attributes>
        <ns2:entry key="Org">Math</ns2:entry>
    </attributes>
</action>
	"""


	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_all_null() {
		ActionDefinition.Builder.create(null, null, null, null, null, null)
	}	

	@Test
	void test_ActionDefinition_Builder_create_null_action_id() {
        // null ID is needed to be able to create
		ActionDefinition.Builder.create(null, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_empty_action_id() {
		ActionDefinition.Builder.create("", ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_whitespace_action_id() {
		ActionDefinition.Builder.create("  	", ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_null_name() {
		ActionDefinition.Builder.create(ACTION_ID_1, null, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_empty_name() {
		ActionDefinition.Builder.create(ACTION_ID_1, "", NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_whitespace_name() {
		ActionDefinition.Builder.create(ACTION_ID_1, "  	", NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_null_namespace() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, null, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_empty_namespace() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, "", TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_whitespace_namespace() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, "  	", TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_null_type_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, null,  RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_empty_type_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, "", RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_whitespace_type_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, "  	", RULE_ID_1, SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_null_rule_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, null,  SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_empty_rule_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, "", SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_whitespace_rule_id() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, "  	", SEQUENCE_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ActionDefinition_Builder_create_fail_null_sequence() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1,  null)
	}

	@Test
	void test_ActionDefinition_Builder_create_success() {
		ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
	}

	@Test
	void test_ActionDefinition_Builder_create_success_description() {
		ActionDefinition.Builder builder = ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
		builder.setDescription( ACTION_DESCRIPTION_1 )
	}

	@Test
	void test_ActionDefinition_Builder_create_success_attribute() {
		Map<String, String> attrs = new HashMap<String, String>();
		attrs.put("Org", "Math");
		ActionDefinition.Builder builder = ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
		builder.setDescription ACTION_DESCRIPTION_1
		builder.setAttributes attrs
	}
	
	@Test
	void test_ActionDefinition_Builder_create_and_build_success() {
		Map<String, String> attrs = new HashMap<String, String>();
		attrs.put("Org", "Math");
		ActionDefinition.Builder builder = ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
		builder.setDescription ACTION_DESCRIPTION_1
		builder.setAttributes attrs
		builder.build()
	}
	
	@Test
	public void testXmlMarshaling_ActionDefinition() {
		Map<String, String> attrs = new HashMap<String, String>();
		attrs.put("Org", "Math");
		ActionDefinition.Builder builder = ActionDefinition.Builder.create(ACTION_ID_1, ACTION_NAME_1, NAMESPACE, TYPE_ID, RULE_ID_1, SEQUENCE_1)
		builder.setDescription ACTION_DESCRIPTION_1
		builder.setAttributes attrs
		builder.build()
		ActionDefinition myAction = builder.build()

		JAXBContext jc = JAXBContext.newInstance(ActionDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myAction, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(ACTION_1))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_ActionDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(ActionDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  ActionDefinition myAction = (ActionDefinition) unmarshaller.unmarshal(new StringReader(ACTION_1))
	  Assert.assertEquals(ACTION_ID_1, myAction.getId())
	  Assert.assertEquals(RULE_ID_1, myAction.getRuleId())
	}

}
