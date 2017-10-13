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
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition
import org.kuali.rice.krms.api.repository.context.ContextDefinition.Builder;


/**
 * This is a description of what this class does - dseibert don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class ContextDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_TEST"
	private static final String CONTEXT_ID_1 = "CONTEXTID001"
    private static final String CONTEXT_NAME = "Context1"
    private static final String CONTEXT_DESCRIPTION = "Context1 Description ..."

	private static final String AGENDA_ID = "500Agenda"
	private static final String AGENDA_ITEM_ID_1 = "AgendaItem1"

	private static final String SMALL_CONTEXT = """
<ns2:context xmlns:ns2="http://rice.kuali.org/krms/v2_0">
    <ns2:id>CONTEXTID001</ns2:id>
    <ns2:namespace>Context1</ns2:namespace>
    <ns2:name>KRMS_TEST</ns2:name>
    <ns2:agendas/>
    <ns2:attributes/>
</ns2:context> """

	private static final String SMALL_CONTEXT_2 = """
<blah:context xmlns:blah="http://rice.kuali.org/krms/v2_0">
    <blah:id>CONTEXTID001</blah:id>
    <blah:namespace>Context1</blah:namespace>
    <blah:name>KRMS_TEST</blah:name>
    <blah:description>Context1 Description ...</blah:description>
    <blah:agendas/>
    <blah:attributes/>
</blah:context> """

	private static final String SMALL_CONTEXT_NO_NAMESPACE = """
<context xmlns:="http://rice.kuali.org/krms/v2_0">
    <id>CONTEXTID001</id>
    <namespace>Context1</namespace>
    <name>KRMS_TEST</name>
    <description>Context1 Description ...</description>
    <agendas/>
    <attributes/>
</context> """

	private static final String FULL_CONTEXT = """
		<context xmlns="http://rice.kuali.org/krms">
			<id>CONTEXTID001</id>
			<name>Context1</name>
			<namespace>KRMS_TEST</namespace>
			<agendas>
    		</agendas>
			<attributes>
			</attributes>
		</context>	"""


	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_all_null() {
		ContextDefinition.Builder.create(null, null)
	}	

	@Test
	void test_ContextDefinition_Builder_create_name_namespace() {
		ContextDefinition.Builder.create(CONTEXT_NAME, NAMESPACE)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_null_context_name() {
		ContextDefinition.Builder.create(null, NAMESPACE)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_empty_context_name() {
		ContextDefinition.Builder.create("", NAMESPACE)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_whitespace_context_name() {
		ContextDefinition.Builder.create("  	", NAMESPACE)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_null_context_namespace() {
		ContextDefinition.Builder.create(CONTEXT_NAME, null)
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_empty_context_namespace() {
		ContextDefinition.Builder.create(CONTEXT_NAME,"")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_whitespace_context_namespace() {
		ContextDefinition.Builder.create(CONTEXT_NAME, "  	")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_blank_id() {
		ContextDefinition.Builder builder = ContextDefinition.Builder.create(CONTEXT_NAME, NAMESPACE)
		builder.setId("")
	}

	@Test(expected=IllegalArgumentException.class)
	void test_ContextDefinition_Builder_create_fail_whitespace_id() {
		ContextDefinition.Builder builder = ContextDefinition.Builder.create(CONTEXT_NAME, NAMESPACE)
		builder.setId("      ")
	}

	@Test
	void test_ContextDefinition_Builder_create() {
		ContextDefinition.Builder builder = ContextDefinition.Builder.create(CONTEXT_NAME, NAMESPACE)
		builder.setId(CONTEXT_ID_1)
	}
	
	@Test
	void test_ContextDefinition_Builder_create_and_build() {
		ContextDefinition.Builder builder = ContextDefinition.Builder.create(CONTEXT_NAME, NAMESPACE)
		builder.setId(CONTEXT_ID_1)
		builder.build()
	}
	
	@Test
	public void testXmlMarshaling_small_ContextDefinition() {
        ContextDefinition myContext = buildFullContextDefinition()

		JAXBContext jc = JAXBContext.newInstance(ContextDefinition.class, AgendaDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
//		marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new CustomNamespacePrefixMapper());
		marshaller.marshal(myContext, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(SMALL_CONTEXT_2))
		Assert.assertEquals(expected, actual)

		// TODO: unmarshall without namespaces
//		expected = unmarshaller.unmarshal(new StringReader(SMALL_CONTEXT_NO_NAMESPACE))
//		Assert.assertEquals(expected, actual)
	}

    public static ContextDefinition buildFullContextDefinition() {
        Builder builder = Builder.create(CONTEXT_NAME, NAMESPACE)
        builder.setId(CONTEXT_ID_1)
        builder.setDescription(CONTEXT_DESCRIPTION);
        ContextDefinition myContext = builder.build()
        return myContext
    }

    @Test
	public void testXmlUnmarshal_small_ContextDefinition() {
//	  JAXBContext jc = JAXBContext.newInstance(ContextDefinition.class)
//	  Unmarshaller unmarshaller = jc.createUnmarshaller()
//	  ContextDefinition myContext = (ContextDefinition) unmarshaller.unmarshal(new StringReader(SMALL_CONTEXT))
//	  Assert.assertEquals(CONTEXT_ID_1, myContext.getId())
//	  Assert.assertEquals(myContext.proposition.description, "is Campus Bloomington")
	}

  
}
