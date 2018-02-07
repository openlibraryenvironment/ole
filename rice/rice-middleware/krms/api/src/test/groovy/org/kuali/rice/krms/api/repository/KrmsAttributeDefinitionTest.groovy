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
import org.junit.Test
import org.junit.Assert
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition.Builder;


/**
 * This class tests out the buiding of a KrmsAttributeDefinition object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class KrmsAttributeDefinitionTest {
	
	private static final String NAMESPACE = "KRMS_UNIT_TEST"
	private static final String ID_1 = "1001"
	private static final String ORG_NAME = "ORG"
	private static final String ORG_LABEL = "Organization"
	private static final String COMPONENT = "someOrgComponent"
	
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	private static final String EXPECTED_XML = """
		<KrmsAttributeDefinition xmlns="http://rice.kuali.org/krms/v2_0">
			<id>1001</id>
			<name>ORG</name>
			<namespace>KRMS_UNIT_TEST</namespace>
			<label>Organization</label>
			<active>1</active>
			<componentName>someOrgComponent</componentName>
		</KrmsAttributeDefinition>
	"""

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		KrmsAttributeDefinition.Builder.create(null, null, null)
	}
	
	@Test
	void test_Builder_create_null_id() {
        // null ID is needed for creation
		KrmsAttributeDefinition.Builder.create(null, ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_id() {
		KrmsAttributeDefinition.Builder.create("", ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_id() {
		KrmsAttributeDefinition.Builder.create("    ", ORG_NAME, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_name() {
		KrmsAttributeDefinition.Builder.create(ID_1, null, NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty__name() {
		KrmsAttributeDefinition.Builder.create(ID_1, "", NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace__name() {
		KrmsAttributeDefinition.Builder.create(ID_1, "   ", NAMESPACE)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_namespace() {
		KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, null)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_namespace() {
		KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, "")
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_namespace() {
		KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, "    ")
	}
	
	
	@Test
	void test_create_only_required(){
		KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
	}

	@Test
	void test_create_and_build_only_required(){
		KrmsAttributeDefinition myDef = KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE).build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_label(){
		KrmsAttributeDefinition myDef =
				KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
				.label(ORG_LABEL)
				.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(ORG_LABEL, myDef.label)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_component(){
		KrmsAttributeDefinition myDef = KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
				.componentName(COMPONENT)
				.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(COMPONENT, myDef.componentName)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	void test_create_and_build_with_label_and_component_fluent(){
		KrmsAttributeDefinition myDef = KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
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
		KrmsAttributeDefinition.Builder myBuilder = KrmsAttributeDefinition.Builder.create(ID_1, ORG_NAME, NAMESPACE)
		myBuilder.setLabel(ORG_LABEL)
		myBuilder.setComponentName(COMPONENT)
		KrmsAttributeDefinition myDef = myBuilder.build()
		Assert.assertEquals(ORG_NAME, myDef.getName())
		Assert.assertEquals(NAMESPACE, myDef.getNamespace())
		Assert.assertEquals(ORG_LABEL, myDef.label)
		Assert.assertEquals(COMPONENT, myDef.componentName)
		Assert.assertTrue(myDef.isActive())
	}

	@Test
	public void testXmlMarshaling() {
        KrmsAttributeDefinition myAttr = buildFullKrmsAttributeDefinition()
		JAXBContext jc = JAXBContext.newInstance(KrmsAttributeDefinition.class)
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

    public static KrmsAttributeDefinition buildFullKrmsAttributeDefinition() {
        KrmsAttributeDefinition myAttr = Builder.create(ID_1, ORG_NAME, NAMESPACE).label(ORG_LABEL).componentName(COMPONENT).build()
        return myAttr
    }

    @Test
	public void testXmlUnmarshal() {
		JAXBContext jc = JAXBContext.newInstance(KrmsAttributeDefinition.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KrmsAttributeDefinition myAttr = (KrmsAttributeDefinition) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(ID_1, myAttr.id)
		Assert.assertEquals(ORG_NAME, myAttr.name)
		Assert.assertEquals(NAMESPACE, myAttr.namespace)
		Assert.assertEquals(ORG_LABEL, myAttr.label)
		Assert.assertEquals(COMPONENT, myAttr.componentName)
	}
}
