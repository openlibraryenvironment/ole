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
package org.kuali.rice.kim.api.permission

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.common.attribute.KimAttribute
import org.kuali.rice.kim.api.common.template.Template
import org.kuali.rice.kim.api.common.template.TemplateTest
import org.kuali.rice.kim.api.type.KimType

class PermissionTest {

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
		<permission xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kim/v2_0">
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
            <attributes>
                <ns2:entry key="${KIM_ATTRIBUTE_1_NAME}">${ATTRIBUTES_1_VALUE}</ns2:entry>
            </attributes>
			<active>${ACTIVE}</active>
			<versionNumber>${VERSION_NUMBER}</versionNumber>
        	<objectId>${OBJECT_ID}</objectId>
		</permission>
		"""
	
    @Test
    void happy_path() {
        Permission.Builder.create(NAMESPACE_CODE, NAME)
    }

	@Test(expected = IllegalArgumentException.class)
	void test_Builder_fail_ver_num_less_than_1() {
		Permission.Builder.create(NAMESPACE_CODE, NAME).setVersionNumber(-1);
	}
	
	@Test
	void test_copy() {
		def o1b = Permission.Builder.create(NAMESPACE_CODE, NAME)

		def o1 = o1b.build()

		def o2 = Permission.Builder.create(o1).build()

		Assert.assertEquals(o1, o2)
	}
	
	@Test
	public void test_Xml_Marshal_Unmarshal() {
	  JAXBContext jc = JAXBContext.newInstance(Permission.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()

	  Permission permission = this.create()
	  marshaller.marshal(permission,sw)
	  String xml = sw.toString()
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(expected,actual)
	}
	
	private create() {
		Permission permission = Permission.Builder.create(new PermissionContract() {
			String getId() {PermissionTest.ID}
			String getNamespaceCode() {PermissionTest.NAMESPACE_CODE}
			String getName() {PermissionTest.NAME}
			String getDescription() {PermissionTest.DESCRIPTION}
			Template getTemplate() {PermissionTest.TEMPLATE.build()}
            Map<String, String> getAttributes() { [(PermissionTest.KIM_ATTRIBUTE_1_NAME) : PermissionTest.ATTRIBUTES_1_VALUE ] }
			boolean isActive() {PermissionTest.ACTIVE.toBoolean()}
			Long getVersionNumber() {PermissionTest.VERSION_NUMBER}
			String getObjectId() {PermissionTest.OBJECT_ID}
		}).build()
		
		return permission
	}
}
