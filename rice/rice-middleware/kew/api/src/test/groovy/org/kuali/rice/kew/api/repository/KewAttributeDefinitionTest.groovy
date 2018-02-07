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
package org.kuali.rice.kew.api.repository

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition.Builder


/**
 * This class tests out the buiding of a KewAttributeDefinition object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class KewAttributeDefinitionTest {
	
	private static final String NAMESPACE = "KEW_UNIT_TEST"
	private static final String ID_1 = "1001"
	private static final String ORG_NAME = "ORG"
	private static final String ORG_LABEL = "Organization"
	private static final String COMPONENT = "someOrgComponent"
	
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	private static final String EXPECTED_XML = """
		<KewAttributeDefinition xmlns="http://rice.kuali.org/kew/v2_0">
			<id>1001</id>
			<name>ORG</name>
			<namespace>KEW_UNIT_TEST</namespace>
			<label>Organization</label>
			<active>1</active>
			<componentName>someOrgComponent</componentName>
		</KewAttributeDefinition>
	"""

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		KewAttributeDefinition.Builder.create(null, null, null)
	}
	
	@Test
	void test_Builder_create_null_id() {
        // null ID is needed for creation
		KewAttributeDefinition.Builder.create(null, ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_id() {
		KewAttributeDefinition.Builder.create("", ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_id() {
		KewAttributeDefinition.Builder.create("    ", ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_name() {
		KewAttributeDefinition.Builder.create(ID_1, null, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty__name() {
		KewAttributeDefinition.Builder.create(ID_1, "", NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace__name() {
		KewAttributeDefinition.Builder.create(ID_1, "   ", NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_namespace() {
		KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, null)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_namespace() {
		KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, "")
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_namespace() {
		KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, "    ")
	}
	
	
	@Test
	void test_create_only_required(){
		KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
	}

	@Test
	void test_create_and_build_only_required(){
		KewAttributeDefinition myDef = KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE).build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_label(){
		KewAttributeDefinition myDef =
				KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
				.label(ORG_LABEL)
				.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(ORG_LABEL, myDef.label)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_component(){
		KewAttributeDefinition myDef = KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
				.componentName(COMPONENT)
				.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(COMPONENT, myDef.componentName)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_label_and_component_fluent(){
		KewAttributeDefinition myDef = KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
				.label(ORG_LABEL)
				.componentName(COMPONENT)
				.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(ORG_LABEL, myDef.label)
		Assert.assertEquals(COMPONENT, myDef.componentName)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_label_and_component(){
		KewAttributeDefinition.Builder myBuilder = KewAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
		myBuilder.setLabel(ORG_LABEL)
		myBuilder.setComponentName(COMPONENT)
		KewAttributeDefinition myDef = myBuilder.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(ORG_LABEL, myDef.label)
		Assert.assertEquals(COMPONENT, myDef.componentName)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	public void testXmlMarshaling() {
        KewAttributeDefinition myAttr = buildFullKewAttributeDefinition()
		JAXBContext jc = JAXBContext.newInstance(KewAttributeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myAttr, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(expected, actual)
	}

    public static KewAttributeDefinition buildFullKewAttributeDefinition() {
        KewAttributeDefinition myAttr = Builder.create(ID_1, ORG_NAME, NAMESPACE).label(ORG_LABEL).componentName(COMPONENT).build()
        return myAttr
    }

    @Test
	public void testXmlUnmarshal() {
		JAXBContext jc = JAXBContext.newInstance(KewAttributeDefinition.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KewAttributeDefinition myAttr = (KewAttributeDefinition) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(ID_1, myAttr.id)
		Assert.assertEquals(ORG_NAME, myAttr.name)
		Assert.assertEquals(NAMESPACE, myAttr.namespace)
		Assert.assertEquals(ORG_LABEL, myAttr.label)
		Assert.assertEquals(COMPONENT, myAttr.componentName)
	}
}
