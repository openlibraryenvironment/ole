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
import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition;
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute;


/**
 * This class tests out the buiding of a KewTypeAttribute object.
 * It also tests XML marshalling / unmarshalling
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
class KewTypeAttributeTest {
	
	private static final String NAMESPACE = "KEW_UNIT_TEST"
	
	private static final String ID="ORG_ATTR_1"
	private static final String TYPE_ID="1234ABCD"
	private static final String ATTR_DEF_ID = "1001"
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	
	private static final String ORG_NAME = "ORG"
	private static final String ORG_LABEL = "Organization"
	private static final String COMPONENT = "someOrgComponent"
	
	private static final String EXPECTED_XML = """
		<KewTypeAttribute xmlns="http://rice.kuali.org/kew/v2_0">
			<id>ORG_ATTR_1</id>
			<typeId>1234ABCD</typeId>
			<attributeDefinitionId>1001</attributeDefinitionId>
			<sequenceNumber>1</sequenceNumber>
			<active>1</active>
		</KewTypeAttribute>
	"""
    
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_all_null() {
		KewTypeAttribute.Builder.create(null, null, null, null)
	}
	
	@Test
	void test_Builder_create_null_id() {
        // null ID is neede to create
		KewTypeAttribute.Builder.create(null, TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_id() {
		KewTypeAttribute.Builder.create("", TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_id() {
		KewTypeAttribute.Builder.create("    ", TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test
	void test_Builder_create_null_type_id() {
        // type ID needs to be nullable so that it can be added to types that haven't been persisted yet 
		KewTypeAttribute.Builder.create(null, null, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty__type_id() {
		KewTypeAttribute.Builder.create(ID, "", ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace__type_id() {
		KewTypeAttribute.Builder.create(ID, "   ",ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_attr_def_id() {
		KewTypeAttribute.Builder.create(ID, TYPE_ID, null, SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_empty_attr_def_id() {
		KewTypeAttribute.Builder.create(ID, TYPE_ID, "", SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_whitespace_attr_def_id() {
		KewTypeAttribute.Builder.create(ID, TYPE_ID, "    ", SEQUENCE_NUMBER_1)
	}
	
	@Test(expected=IllegalArgumentException.class)
	void test_Builder_create_fail_null_sequence_number() {
		KewTypeAttribute.Builder.create(ID, TYPE_ID, ATTR_DEF_ID, null)
	}
	

	@Test
	void test_create_only_required(){
		KewTypeAttribute.Builder.create(ID, TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
	}

	@Test
	void test_create_and_build_only_required(){
		KewTypeAttribute myAttr = KewTypeAttribute.Builder
			.create(ID, TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
			.build()
		Assert.assertEquals(ID, myAttr.getId())
		Assert.assertEquals(TYPE_ID, myAttr.getTypeId())
		Assert.assertEquals(ATTR_DEF_ID, myAttr.getAttributeDefinitionId())
		Assert.assertEquals(SEQUENCE_NUMBER_1, myAttr.getSequenceNumber())
		Assert.assertTrue(myAttr.isActive())
	}

	@Test
	public void testXmlMarshaling() {
		KewTypeAttribute myAttr = KewTypeAttribute.Builder.create(ID, TYPE_ID, ATTR_DEF_ID, SEQUENCE_NUMBER_1)
				.build()
		JAXBContext jc = JAXBContext.newInstance(KewTypeAttribute.class)
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
		JAXBContext jc = JAXBContext.newInstance(KewTypeAttribute.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KewTypeAttribute myAttr = (KewTypeAttribute) unmarshaller.unmarshal(new StringReader(EXPECTED_XML))
		Assert.assertEquals(ID, myAttr.id)
		Assert.assertEquals(TYPE_ID, myAttr.typeId)
		Assert.assertEquals(ATTR_DEF_ID, myAttr.attributeDefinitionId)
		Assert.assertEquals(SEQUENCE_NUMBER_1, myAttr.sequenceNumber)
		Assert.assertTrue(myAttr.active)
	}

}
