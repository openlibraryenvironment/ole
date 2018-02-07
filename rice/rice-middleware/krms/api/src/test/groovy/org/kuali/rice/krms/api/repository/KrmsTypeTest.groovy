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
package org.kuali.rice.krms.api.repository;


import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert
import org.junit.Test
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinitionContract

/**
 * Exercises the immutable Country class, including XML (un)marshalling
 */
class KrmsTypeTest {

	private final shouldFail = new GroovyTestCase().&shouldFail

	private static final String NAMESPACE = "KRMS_TEST"
	private static final String TYPE_ID="1234ABCD"
	private static final String NAME="Chart_Org"
	private static final String SERVICE_NAME="chartOrgService"
	private static final Long VERSION_NUMBER = new Long(1)
	
	private static final String ATTR_ID_1="CHART_ATTR"
	private static final String CHART_ATTR_DEF_ID = "1000"
	private static final Integer SEQUENCE_NUMBER_1 = new Integer(1)
	
	private static final String ATTR_ID_2="ORG_ATTR"
	private static final String ORG_ATTR_DEF_ID = "1002"
	private static final Integer SEQUENCE_NUMBER_2 = new Integer(2)
	
	String CHART_ORG_TYPE_XML = """
			<KRMSType xmlns="http://rice.kuali.org/krms/v2_0">
				<id>1234ABCD</id>
				<name>Chart_Org</name>
				<namespace>KRMS_TEST</namespace>
				<serviceName>chartOrgService</serviceName>
				<active>true</active>
				<attribute>
					<id>CHART_ATTR</id>
					<typeId>1234ABCD</typeId>
					<attributeDefinitionId>1000</attributeDefinitionId>
					<sequenceNumber>1</sequenceNumber>
					<active>true</active>
				</attribute>
				<attribute>
					<id>ORG_ATTR</id>
					<typeId>1234ABCD</typeId>
					<attributeDefinitionId>1002</attributeDefinitionId>
					<sequenceNumber>2</sequenceNumber>
					<active>true</active>
				</attribute>
			</KRMSType>
	"""
	
	String PLAIN_TYPE_XML = """
	<KRMSType xmlns="http://rice.kuali.org/krms/v2_0">
	  <id>1234ABCD</id>
	  <name>Chart_Org</name>
	  <namespace>KRMS_TEST</namespace>
	  <active>true</active>
	  <serviceName></serviceName>
	</KRMSType>
	"""


	@Test
	public void test_create_only_required() {
		KrmsTypeDefinition.Builder.create(KrmsTypeDefinition.Builder.create(NAME, NAMESPACE))
			.build();
	}

	@Test	
	public void test_create_with_service_name() {
		KrmsTypeDefinition.Builder.create(KrmsTypeDefinition.Builder.create(NAME, NAMESPACE))
			.serviceName(SERVICE_NAME)
			.build();
	}

	@Test
	public void testKrmsTypeBuilderPassedInParams() {
		//No assertions, just test whether the Builder gives us a KRMS KrmsType object
		KrmsTypeDefinition myType = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE)
			.build()
	}

	@Test
	public void testKrmsTypeBuilderPassedInAttributes() {
		//No assertions, just test whether the Builder gives us a KRMS KrmsType object
		// create chart attribute Builder
		KrmsTypeAttribute.Builder chartAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, CHART_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
		KrmsTypeAttribute.Builder orgAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, ORG_ATTR_DEF_ID, SEQUENCE_NUMBER_2)
		List<KrmsTypeAttribute.Builder> attrs = Arrays.asList(chartAttrBuilder, orgAttrBuilder)

		// create KrmsType builder and build
		KrmsTypeDefinition myType = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE)
				.attributes(attrs)
				.build()
	}

	@Test
	public void testTypeBuilderPassedInContract() {
		//No assertions, just test whether the Builder gives us a KRMS KrmsType object
		KrmsTypeDefinition type = KrmsTypeDefinition.Builder.create(new KrmsTypeDefinitionContract() {
					String getId() { KrmsTypeTest.TYPE_ID }
					String getName() { KrmsTypeTest.NAME }
					String getNamespace() { KrmsTypeTest.NAMESPACE }
					String getServiceName() { KrmsTypeTest.SERVICE_NAME }
					boolean isActive() { true }
					List<KrmsTypeAttribute> getAttributes() {null}
					Long getVersionNumber() { KrmsTypeTest.VERSION_NUMBER }
				}).build()
	}

	@Test
	public void testKrmsTypeBuilderPassedInParamsAndServiceName() {
		//No assertions, just test whether the Builder gives us a KRMS KrmsType object
		KrmsTypeDefinition myType = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE)
			.serviceName("MyFictionalService")
			.build()
	}

	@Test
	public void testXmlMarshaling() {
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeDefinition.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()

		KrmsTypeDefinition.Builder myTypeBuilder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE)
        myTypeBuilder.setId(TYPE_ID)
        KrmsTypeDefinition myType = myTypeBuilder.build()
		marshaller.marshal(myType, sw)
		String xml = sw.toString()

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(PLAIN_TYPE_XML))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshal() {
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeDefinition.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		KrmsTypeDefinition myType = (KrmsTypeDefinition) unmarshaller.unmarshal(new StringReader(PLAIN_TYPE_XML))
		Assert.assertEquals(TYPE_ID,myType.id)
		Assert.assertEquals(NAME,myType.name)
		Assert.assertEquals(NAMESPACE, myType.namespace)
		Assert.assertEquals (true, myType.active)
	}
	
	@Test
	public void testXmlMarshalingWithAttributes() {
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeDefinition.class, KrmsTypeAttribute.class)
		Marshaller marshaller = jc.createMarshaller()
		StringWriter sw = new StringWriter()

		// create chart attribute Builder
		KrmsTypeAttribute.Builder chartAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, CHART_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
        chartAttrBuilder.setId(ATTR_ID_1)
		KrmsTypeAttribute.Builder orgAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, ORG_ATTR_DEF_ID, SEQUENCE_NUMBER_2)
        orgAttrBuilder.setId(ATTR_ID_2)
		List<KrmsTypeAttribute.Builder> attrs = Arrays.asList(chartAttrBuilder, orgAttrBuilder)
		
		// create KrmsType builder and build
		KrmsTypeDefinition.Builder myTypeBuilder = KrmsTypeDefinition.Builder.create(NAME, NAMESPACE)
			.serviceName(SERVICE_NAME)
			.attributes(attrs)
        myTypeBuilder.setId(TYPE_ID)
        KrmsTypeDefinition myType = myTypeBuilder.build()
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
		marshaller.marshal(myType, sw)
		String xml = sw.toString()
		print xml
		
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object actual = unmarshaller.unmarshal(new StringReader(xml))
		Object expected = unmarshaller.unmarshal(new StringReader(CHART_ORG_TYPE_XML))
		Assert.assertEquals(expected, actual)
	}

	@Test
	public void testXmlUnmarshalWithAttributes() {
		JAXBContext jc = JAXBContext.newInstance(KrmsTypeDefinition.class, KrmsTypeAttribute.class)
		Unmarshaller unmarshaller = jc.createUnmarshaller()
		KrmsTypeDefinition myType = (KrmsTypeDefinition) unmarshaller.unmarshal(new StringReader(CHART_ORG_TYPE_XML))
		Assert.assertEquals(TYPE_ID, myType.id)
		Assert.assertEquals(NAME,myType.name)
		Assert.assertEquals(NAMESPACE, myType.namespace)
		Assert.assertEquals (true, myType.active)
		
		KrmsTypeAttribute.Builder chartAttrBuilder = KrmsTypeAttribute.Builder.create(TYPE_ID, CHART_ATTR_DEF_ID, SEQUENCE_NUMBER_1)
        chartAttrBuilder.setId(ATTR_ID_1)
        KrmsTypeAttribute chartAttr = chartAttrBuilder.build()
		Assert.assertEquals(chartAttr, myType.attributes.get(0))
	}
	

}