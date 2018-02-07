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
package org.kuali.rice.kew.api.repository;


import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert
import org.junit.Test
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition
import org.kuali.rice.kew.api.repository.type.KewTypeDefinitionContract

/**
 * Exercises the immutable Country class, including XML (un)marshalling
 */
class KewTypeTest {

	private final shouldFail = new GroovyTestCase().&shouldFail

	private static final String NAMESPACE = "KEW_TEST"
    private static final String TYPE_ID="KC_MAP123"
    private static final String NAME="KC_UNIT"
    private static final String SERVICE_NAME="kcUnitService"
	private static final Long VERSION_NUMBER = new Long(1)
	
    private static final String ATTR_ID_1="UNIT_NUM"
    private static final String UNIT_NUM_ATTR_DEF_ID = "1000"
    private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
    
    private static final String ATTR_ID_2="CAMPUS"
    private static final String CAMPUS_ATTR_DEF_ID = "1002"
    private static final Integer SEQUENCE_NUMBER_2 = new Integer(2)
	
	String KC_UNIT_TYPE_XML = """
			<KEWType xmlns="http://rice.kuali.org/kew/v2_0">
				<id>KC_MAP123</id>
				<name>KC_UNIT</name>
				<namespace>KEW_TEST</namespace>
				<serviceName>kcUnitService</serviceName>
				<active>true</active>
				<attribute>
					<id>UNIT_NUM</id>
					<typeId>KC_MAP123</typeId>
					<attributeDefinitionId>1000</attributeDefinitionId>
					<sequenceNumber>1</sequenceNumber>
					<active>true</active>
				</attribute>
				<attribute>
					<id>CAMPUS</id>
					<typeId>KC_MAP123</typeId>
					<attributeDefinitionId>1002</attributeDefinitionId>
					<sequenceNumber>2</sequenceNumber>
					<active>true</active>
				</attribute>
			</KEWType>
	"""
	
    String PLAIN_TYPE_XML = """
    <KEWType xmlns="http://rice.kuali.org/kew/v2_0">
      <id>KC_MAP123</id>
      <name>KC_UNIT</name>
      <namespace>KEW_TEST</namespace>
      <active>true</active>
      <serviceName></serviceName>
    </KEWType>
    """


	@Test
	public void test_create_only_required() {
		KewTypeDefinition.Builder.create(KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE))
			.build();
	}

	@Test	
	public void test_create_with_service_name() {
		KewTypeDefinition.Builder.create(KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE))
			.serviceName(SERVICE_NAME)
			.build();
	}

	@Test
	public void testKewTypeBuilderPassedInParams() {
		//No assertions, just test whether the Builder gives us a KEW KewType object
		KewTypeDefinition myType = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE)
			.build()
	}

	@Test
	public void testKewTypeBuilderPassedInAttributes() {
		//No assertions, just test whether the Builder gives us a KEW KewType object
		// create chart attribute Builder
		KewTypeAttribute.Builder chartAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_1, TYPE_ID, UNIT_NUM_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
		KewTypeAttribute.Builder orgAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_2, TYPE_ID, CAMPUS_ATTR_DEF_ID, SEQUENCE_NUMBER_2)
		List<KewTypeAttribute.Builder> attrs = Arrays.asList(chartAttrBuilder, orgAttrBuilder)

		// create KewType builder and build
		KewTypeDefinition myType = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE)
				.attributes(attrs)
				.build()
	}

	@Test
	public void testTypeBuilderPassedInContract() {
		//No assertions, just test whether the Builder gives us a KEW KewType object
		KewTypeDefinition type = KewTypeDefinition.Builder.create(new KewTypeDefinitionContract() {
					String getId() { KewTypeTest.TYPE_ID }
					String getName() { KewTypeTest.NAME }
					String getNamespace() { KewTypeTest.NAMESPACE }
					String getServiceName() { KewTypeTest.SERVICE_NAME }
					boolean isActive() { true }
					List<KewTypeAttribute> getAttributes() {null}
					Long getVersionNumber() { KewTypeTest.VERSION_NUMBER }
				}).build()
	}

	@Test
	public void testKewTypeBuilderPassedInParamsAndServiceName() {
		//No assertions, just test whether the Builder gives us a KEW KewType object
		KewTypeDefinition myType = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE)
			.serviceName("MyFictionalService")
			.build()
	}

	@Test
	public void testTypeBuilderWhitespaceTypeId() {
		shouldFail(IllegalArgumentException.class) {
			KewTypeDefinition.Builder.create("  ", NAME, NAMESPACE)
		}
	}

	@Test
	public void testTypeBuilderEmptyTypeId() {
		shouldFail(IllegalArgumentException.class) {
			KewTypeDefinition.Builder.create("", NAME, NAMESPACE)
		}
	}

	@Test
	public void testXmlMarshaling() {
		JAXBContext jc = JAXBContext.newInstance(KewTypeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()

		KewTypeDefinition myType = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE).build()
		marshaller.marshal(myType, sw)
		String xml = sw.toString()

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(PLAIN_TYPE_XML))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal() {
		JAXBContext jc = JAXBContext.newInstance(KewTypeDefinition.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KewTypeDefinition myType = (KewTypeDefinition) unmarshaller.unmarshal(new StringReader(PLAIN_TYPE_XML))
		Assert.assertEquals(TYPE_ID,myType.id)
		Assert.assertEquals(NAME,myType.name)
		Assert.assertEquals(NAMESPACE, myType.namespace)
		Assert.assertEquals (true, myType.active)
	}
	
	@Test
	public void testXmlMarshalingWithAttributes() {
		JAXBContext jc = JAXBContext.newInstance(KewTypeDefinition.class, KewTypeAttribute.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()

		// create chart attribute Builder
		KewTypeAttribute.Builder unitNumAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_1, TYPE_ID, UNIT_NUM_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
		KewTypeAttribute.Builder campusAttrBuilder = KewTypeAttribute.Builder.create(ATTR_ID_2, TYPE_ID, CAMPUS_ATTR_DEF_ID, SEQUENCE_NUMBER_2)
		List<KewTypeAttribute.Builder> attrs = Arrays.asList(unitNumAttrBuilder, campusAttrBuilder)
		
		// create KewType builder and build
		KewTypeDefinition myType = KewTypeDefinition.Builder.create(TYPE_ID, NAME, NAMESPACE)
			.serviceName(SERVICE_NAME)
			.attributes(attrs)
			.build()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myType, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(KC_UNIT_TYPE_XML))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshalWithAttributes() {
		JAXBContext jc = JAXBContext.newInstance(KewTypeDefinition.class, KewTypeAttribute.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller()
		KewTypeDefinition myType = (KewTypeDefinition) unmarshaller.unmarshal(new StringReader(KC_UNIT_TYPE_XML))
		Assert.assertEquals(TYPE_ID, myType.id)
		Assert.assertEquals(NAME,myType.name)
		Assert.assertEquals(NAMESPACE, myType.namespace)
		Assert.assertEquals (true, myType.active)
		
		KewTypeAttribute chartAttr = KewTypeAttribute.Builder.create(ATTR_ID_1, TYPE_ID, UNIT_NUM_ATTR_DEF_ID, SEQUENCE_NUMBER_1).build()
		Assert.assertEquals(chartAttr, myType.attributes.get(0))
	}
	

}