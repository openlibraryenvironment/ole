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
package org.kuali.rice.kim.api.responsibility

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.common.attribute.KimAttribute
import org.kuali.rice.kim.api.common.template.Template
import org.kuali.rice.kim.api.common.template.TemplateTest
import org.kuali.rice.kim.api.type.KimType

class ResponsibilityTest {

	private static final String OBJECT_ID = UUID.randomUUID()
	private static final Long VERSION_NUMBER = new Long(1)
	private static final boolean ACTIVE = "true"
	
	private static final String ID = "50"
	private static final String NAMESPACE_CODE = "KUALI"
	private static final String NAME = "PermissionName"
	private static final String DESCRIPTION = "Some Permission Description"
	private static final Template.Builder TEMPLATE = Template.Builder.create(TemplateTest.create());

	private static final String ATTRIBUTES_1_ID = "1"
	private static final String ATTRIBUTES_1_PERMISSION_ID = "50"
	private static final String ATTRIBUTES_1_VALUE = "Some Attribute Value 1"
	private static final Long ATTRIBUTES_1_VER_NBR = new Long(1)
	private static final String ATTRIBUTES_1_OBJ_ID = UUID.randomUUID()
	
	private static final KimType KIM_TYPE_1
	private static final String KIM_TYPE_1_ID = "1"
	private static final String KIM_TYPE_1_OBJ_ID = UUID.randomUUID()
	static {
		KimType.Builder builder = KimType.Builder.create()
		builder.setId(KIM_TYPE_1_ID)
		builder.setNamespaceCode(NAMESPACE_CODE)
		builder.setActive(ACTIVE)
		builder.setVersionNumber(VERSION_NUMBER)
		builder.setObjectId(KIM_TYPE_1_OBJ_ID)
		KIM_TYPE_1 = builder.build()
	}
		
	private static final KimAttribute KIM_ATTRIBUTE_1
	private static final String KIM_ATTRIBUTE_1_ID = "1"
	private static final String KIM_ATTRIBUTE_1_COMPONENT_NAME = "the_component1"
	private static final String KIM_ATTRIBUTE_1_NAME = "the_attribute1"
	static {
		KimAttribute.Builder builder = KimAttribute.Builder.create(KIM_ATTRIBUTE_1_COMPONENT_NAME, KIM_ATTRIBUTE_1_NAME, NAMESPACE_CODE)
		builder.setId(KIM_ATTRIBUTE_1_ID)
		KIM_ATTRIBUTE_1 = builder.build()
	}
	
	private static final String XML = """
<responsibility xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kim/v2_0">
	<id>${ID}</id>
	<namespaceCode>${NAMESPACE_CODE}</namespaceCode>
	<name>${NAME}</name>
	<description>${DESCRIPTION}</description>
	<template>
		<id>${TemplateTest.ID}</id>
		<namespaceCode>${TemplateTest.NAMESPACE_CODE}</namespaceCode>
		<name>${TemplateTest.NAME}</name>
		<description>${TemplateTest.DESCRIPTION}</description>
		<kimTypeId>${TemplateTest.KIM_TYPE_ID}</kimTypeId>
		<active>${TemplateTest.ACTIVE}</active>
		<versionNumber>${TemplateTest.VERSION_NUMBER}</versionNumber>
		<objectId>${TemplateTest.OBJECT_ID}</objectId>
	</template>
	<active>${ACTIVE}</active>
	<attributes>
		<ns2:entry key="${KIM_ATTRIBUTE_1_NAME}">${ATTRIBUTES_1_VALUE}</ns2:entry>
	</attributes>
	<versionNumber>${VERSION_NUMBER}</versionNumber>
	<objectId>${OBJECT_ID}</objectId>
</responsibility>
		"""
	
    @Test
    void happy_path() {
        Responsibility.Builder.create(NAMESPACE_CODE, NAME).build()
    }

	@Test(expected = IllegalArgumentException.class)
	void test_Builder_fail_ver_num_less_than_1() {
		Responsibility.Builder.create(NAMESPACE_CODE, NAME).setVersionNumber(-1);
	}
	
	@Test
	void test_copy() {
		def o1b = Responsibility.Builder.create(NAMESPACE_CODE, NAME)
		o1b.description = DESCRIPTION

		def o1 = o1b.build()

		def o2 = Responsibility.Builder.create(o1).build()

		Assert.assertEquals(o1, o2)
	}
	
	@Test
	public void test_Xml_Marshal_Unmarshal() {
	  JAXBContext jc = JAXBContext.newInstance(Responsibility.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()

	  Responsibility responsibility = this.create()
	  marshaller.marshal(responsibility,sw)
	  String xml = sw.toString()
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(expected,actual)
	}
	
	private create() {
		Responsibility responsibility = Responsibility.Builder.create(new ResponsibilityContract() {
			String getId() {ResponsibilityTest.ID}
			String getNamespaceCode() {ResponsibilityTest.NAMESPACE_CODE}
			String getName() {ResponsibilityTest.NAME}
			String getDescription() {ResponsibilityTest.DESCRIPTION}
			Template getTemplate() {ResponsibilityTest.TEMPLATE.build()}
			Map<String, String> getAttributes() { [(ResponsibilityTest.KIM_ATTRIBUTE_1_NAME) : ResponsibilityTest.ATTRIBUTES_1_VALUE] }
			boolean isActive() {ResponsibilityTest.ACTIVE.toBoolean()}
			Long getVersionNumber() {ResponsibilityTest.VERSION_NUMBER}
			String getObjectId() {ResponsibilityTest.OBJECT_ID}
		}).build()
		
		return responsibility
	}
}
