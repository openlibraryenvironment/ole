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

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;


/**
 * This is a description of what this class does - dseibert don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class AgendaDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_TEST"
	private static final String AGENDA_ID_1 = "AGENDAID001"
	private static final String AGENDA_NAME = "Agenda1"
	private static final String TYPE_ID = null;
	private static final String CONTEXT_ID_1 = "CONTEXT-001"
	private static final String AGENDA_ITEM_ID_1 = "ITEM01"

	private static final String ATTR_NAME_1 = "Department"
	private static final String ATTR_VALUE_1 = "Biology"
	private static final String ATTR_NAME_2 = "Fund"
	private static final String ATTR_VALUE_2 = "19900A"

	private static final String SMALL_AGENDA = """
<agenda xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/krms/v2_0">
    <id>AGENDAID001</id>
    <name>Agenda1</name>
    <namespace>KRMS_TEST</namespace>
    <contextId>CONTEXT-001</contextId>
    <active>true</active>
    <firstItemId>ITEM01</firstItemId>
    <attributes>
        <ns2:entry key="Fund">19900A</ns2:entry>
        <ns2:entry key="Department">Biology</ns2:entry>
    </attributes>
</agenda>
"""



	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_all_null() {
		AgendaDefinition.Builder.create(null, null, null, null)
	}	

	@Test
	void test_AgendaDefinition_Builder_create_null_agenda_id() {
        // null ID is legit
		AgendaDefinition.Builder.create(null, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_empty_agenda_id() {
		AgendaDefinition.Builder.create("", AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_whitespace_agenda_id() {
		AgendaDefinition.Builder.create("  	", AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_null_agenda_name() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, null, TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_empty_agenda_name() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, "", TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_whitespace_agenda_name() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, "  	", TYPE_ID, CONTEXT_ID_1)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_null_context_id() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, null)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_empty_context_id() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, "")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_AgendaDefinition_Builder_create_fail_whitespace_context_id() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, "    ")
	}

	@Test
	void test_AgendaDefinition_Builder_create() {
		AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
	}
	
	@Test
	void test_AgendaDefinition_Builder_create_with_item() {
		AgendaDefinition.Builder builder = AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
		builder.setFirstItemId(AGENDA_ITEM_ID_1)
	}
	
	@Test
	void test_AgendaDefinition_Builder_create_with_attributes() {
		Map<String,String> myAttrs = new HashMap<String,String>()
		myAttrs.put(ATTR_NAME_1, ATTR_VALUE_1)
		AgendaDefinition.Builder builder = AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
		builder.setFirstItemId(AGENDA_ITEM_ID_1)
		builder.setAttributes(myAttrs);
	}
	
	@Test
	void test_AgendaDefinition_Builder_create_and_build() {
		Map<String,String> myAttrs = new HashMap<String,String>()
		myAttrs.put(ATTR_NAME_1, ATTR_VALUE_1)
		AgendaDefinition.Builder builder = AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
		builder.setFirstItemId(AGENDA_ITEM_ID_1)
		builder.setAttributes(myAttrs);
		builder.build()
	}
	
	@Test
	public void testXmlMarshaling_small_AgendaDefinition() {
		Map<String,String> myAttrs = new HashMap<String,String>()
		myAttrs.put(ATTR_NAME_1, ATTR_VALUE_1)
		myAttrs.put(ATTR_NAME_2, ATTR_VALUE_2)
		AgendaDefinition.Builder builder = AgendaDefinition.Builder.create(AGENDA_ID_1, AGENDA_NAME, TYPE_ID, CONTEXT_ID_1)
		builder.setFirstItemId(AGENDA_ITEM_ID_1)
		builder.setAttributes(myAttrs);
		AgendaDefinition myAgenda = builder.build()

		JAXBContext jc = JAXBContext.newInstance(AgendaDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myAgenda, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_AGENDA))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal_small_AgendaDefinition() {
	  JAXBContext jc = JAXBContext.newInstance(AgendaDefinition.class)
	  Unmarshaller unmarshaller = jc.createUnmarshaller()
	  AgendaDefinition myAgenda = (AgendaDefinition) unmarshaller.unmarshal(new StringReader(SMALL_AGENDA))
	  Assert.assertEquals(AGENDA_ID_1, myAgenda.getId())
	}
}
