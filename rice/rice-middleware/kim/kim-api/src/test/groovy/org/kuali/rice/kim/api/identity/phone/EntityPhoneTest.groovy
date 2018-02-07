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
package org.kuali.rice.kim.api.identity.phone

import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert
import org.kuali.rice.kim.api.identity.CodedAttribute
import org.kuali.rice.kim.api.identity.CodedAttributeContract


class EntityPhoneTest {
    private static final String ID = "1";
	private static final String ENTITY_TYPE_CODE = "PERSON";
	private static final String ENTITY_ID = "190192";
    private static final String TYPE_CODE = "Home"
    private static final String TYPE_NAME = "Home-y"
    private static final String TYPE_SORT_CODE = "0"
    private static final String TYPE_ACTIVE = "true"
    private static final Long TYPE_VERSION_NUMBER = new Integer(1)
	private static final String TYPE_OBJECT_ID = UUID.randomUUID()
	private static final String PHONE_NUMBER = "439-0116";
    private static final String COUNTRY_CODE = "1";
    private static final String EXTENSION_NUMBER ="12"
    private static final String SUPPRESS = "false"
    private static final String DEFAULT = "true"
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityPhone xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityTypeCode>${ENTITY_TYPE_CODE}</entityTypeCode>
        <entityId>${ENTITY_ID}</entityId>
        <phoneType>
            <code>${TYPE_CODE}</code>
            <name>${TYPE_NAME}</name>
            <active>${TYPE_ACTIVE}</active>
            <sortCode>${TYPE_SORT_CODE}</sortCode>
            <versionNumber>${TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${TYPE_OBJECT_ID}</objectId>
        </phoneType>
        <countryCode>${COUNTRY_CODE}</countryCode>
        <phoneNumber>${PHONE_NUMBER}</phoneNumber>
        <extensionNumber>${EXTENSION_NUMBER}</extensionNumber>
        <formattedPhoneNumber>${PHONE_NUMBER} x${EXTENSION_NUMBER}</formattedPhoneNumber>
        <countryCodeUnmasked>${COUNTRY_CODE}</countryCodeUnmasked>
        <phoneNumberUnmasked>${PHONE_NUMBER}</phoneNumberUnmasked>
        <extensionNumberUnmasked>${EXTENSION_NUMBER}</extensionNumberUnmasked>
        <formattedPhoneNumberUnmasked>${PHONE_NUMBER} x${EXTENSION_NUMBER}</formattedPhoneNumberUnmasked>
        <suppressPhone>${SUPPRESS}</suppressPhone>
        <defaultValue>${DEFAULT}</defaultValue>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityPhone>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityPhone.Builder builder = EntityPhone.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityPhone.Builder.create().build();
        def o2 = EntityPhone.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityPhone.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityPhone.class)
	}

    private create() {
		return EntityPhone.Builder.create(new EntityPhoneContract() {
			def String id = EntityPhoneTest.ID
            def String entityTypeCode = EntityPhoneTest.ENTITY_TYPE_CODE
            def String entityId = EntityPhoneTest.ENTITY_ID
			def CodedAttribute getPhoneType() { CodedAttribute.Builder.create(new CodedAttributeContract() {
				def String code = EntityPhoneTest.TYPE_CODE
				def String name = EntityPhoneTest.TYPE_NAME
				def boolean active = EntityPhoneTest.TYPE_ACTIVE
                def String sortCode = EntityPhoneTest.TYPE_SORT_CODE
                def Long versionNumber = EntityPhoneTest.TYPE_VERSION_NUMBER
				def String objectId = EntityPhoneTest.TYPE_OBJECT_ID
			}).build()}
            def String countryCode = EntityPhoneTest.COUNTRY_CODE
            def String phoneNumber = EntityPhoneTest.PHONE_NUMBER
            def String extensionNumber = EntityPhoneTest.EXTENSION_NUMBER
            def String countryCodeUnmasked = EntityPhoneTest.COUNTRY_CODE
            def String phoneNumberUnmasked = EntityPhoneTest.PHONE_NUMBER
            def String extensionNumberUnmasked = EntityPhoneTest.EXTENSION_NUMBER
            def String formattedPhoneNumber = EntityPhoneTest.PHONE_NUMBER + " x" + EntityPhoneTest.EXTENSION_NUMBER
            def String formattedPhoneNumberUnmasked = EntityPhoneTest.PHONE_NUMBER + " x" + EntityPhoneTest.EXTENSION_NUMBER
            def boolean suppressPhone = EntityPhoneTest.SUPPRESS.toBoolean()
            def boolean defaultValue = EntityPhoneTest.DEFAULT.toBoolean()
            def boolean active = EntityPhoneTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityPhoneTest.VERSION_NUMBER;
			def String objectId = EntityPhoneTest.OBJECT_ID
        }).build()

	}

}
