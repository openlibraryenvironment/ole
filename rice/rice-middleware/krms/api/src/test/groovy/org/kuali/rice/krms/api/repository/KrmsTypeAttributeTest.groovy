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
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;


/**
 * This class tests out the buiding of a KrmsTypeAttribute object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class KrmsTypeAttributeTest {
	
	private static final String NAMESPACE = "KRMS_UNIT_TEST"
	
	private static final String ID="ORG_ATTR_1"
	private static final String TYPE_ID="1234ABCD"
	private static final String ATTR_DEF_ID = "1001"
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	
	private static final String ORG_NAME = "ORG"
	private static final String ORG_LABEL = "Organization"
	private static final String COMPONENT = "someOrgComponent"
	
	private static final String EXPECTED_XML = """
		<krmsTypeAttribute xmlns="http://rice.kuali.org/krms/v2_0">
			<typeId>1234ABCD</typeId>
			<attributeDefinitionId>1001</attributeDefinitionId>
			<sequenceNumber>1</sequenceNumber>
			<active>true</active>
		</krmsTypeAttribute>
	"""

	private static final String EXPECTED_XML_2 = """
		<krmsTypeAttribute xmlns="http://rice.kuali.org/krms/v2_0">
			<id>ORG_ATTR_1</id>
			<typeId>1234ABCD</typeId>
			<attributeDefinitionId>1001</attributeDefinitionId>
			<sequenceNumber>1</sequenceNumber>
			<active>true</active>
		</krmsTypeAttribute>
	"""

	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		KrmsTypeAttribute.Builder.create(null, null, null)
	}
	
	@Test
	void test_Builder_create_null_id() {
        // null ID is neede to create
		KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}

	@Test
	void test_Builder_create_null_type_id() {
        // type ID needs to be nullable so that it can be added to types that haven't been persisted yet 
		KrmsTypeAttribute.Builder.create(null, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty__type_id() {
		KrmsTypeAttribute.Builder.create("", ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace__type_id() {
		KrmsTypeAttribute.Builder.create("   ",ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_attr_def_id() {
		KrmsTypeAttribute.Builder.create(TYPE_ID, null, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_attr_def_id() {
		KrmsTypeAttribute.Builder.create(TYPE_ID, "", SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_attr_def_id() {
		KrmsTypeAttribute.Builder.create(TYPE_ID, "    ", SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_sequence_number() {
		KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, null)
	}
	

	@Test
	void test_create_only_required(){
		KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}

	@Test
	void test_create_and_build_only_required(){
		KrmsTypeAttribute myAttr = KrmsTypeAttribute.Builder
			.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
			.build()
		Assert.assertEquals(TYPE_ID, myAttr.getTypeId())
		Assert.assertEquals(ATTR_DEF_ID, myAttr.getAttributeDefinitionId())
		Assert.assertEquals(SEQUENCE_NUMBER_1, myAttr.getSequenceNumber())
		Assert.assertTrue(myAttr.isActive())
	}

	@Test
	void test_create_and_build_with_attribute_definition(){
		KrmsTypeAttribute.Builder myAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
        myAttrBuilder.setId(ID)
		KrmsTypeAttribute myAttr = myAttrBuilder.build()
		Assert.assertTrue(myAttr.isActive())
		Assert.assertEquals(ID, myAttr.getId())
		Assert.assertEquals(TYPE_ID, myAttr.getTypeId())
		Assert.assertEquals(ATTR_DEF_ID, myAttr.getAttributeDefinitionId())
		Assert.assertEquals(SEQUENCE_NUMBER_1, myAttr.getSequenceNumber())
		Assert.assertTrue(myAttr.isActive())
	}

	@Test
	public void testXmlMarshaling() {
		KrmsTypeAttribute myAttr = KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
				.build()
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeAttribute.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()
		marshaller.marshal(myAttr, sw)
		String xml = sw.toString()

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal() {
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeAttribute.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KrmsTypeAttribute myAttr = (KrmsTypeAttribute) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(TYPE_ID, myAttr.typeId)
		Assert.assertEquals(ATTR_DEF_ID, myAttr.attributeDefinitionId)
		Assert.assertEquals(SEQUENCE_NUMBER_1, myAttr.sequenceNumber)
		Assert.assertTrue(myAttr.active)
	}

	@Test
	public void testXmlMarshalingWithDefinition() {
		KrmsTypeAttribute.Builder myAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
        myAttrBuilder.setId(ID)
		KrmsTypeAttribute myAttr = myAttrBuilder.build()
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeAttribute.class, KrmsAttributeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		StringWriter sw = new StringWriter()
		marshaller.marshal(myAttr, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(EXPECTED_XML_2))
		Assert.assertEquals(expected, actual)
	}

}
