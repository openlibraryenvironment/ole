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
package org.kuali.rice.kim.api.identity.email

import org.junit.Assert
import org.junit.Test
import org.kuali.rice.kim.api.identity.CodedAttribute
import org.kuali.rice.kim.api.identity.CodedAttributeContract
import org.kuali.rice.kim.api.test.JAXBAssert

class EntityEmailTest {
    private static final String ID = "1";
	private static final String ENTITY_TYPE_CODE = "PERSON";
	private static final String ENTITY_ID = "190192";
    private static final String TYPE_CODE = "Home"
    private static final String TYPE_NAME = "Home-y"
    private static final String TYPE_SORT_CODE = "0"
    private static final String TYPE_ACTIVE = "true"
    private static final Long TYPE_VERSION_NUMBER = new Integer(1)
	private static final String TYPE_OBJECT_ID = UUID.randomUUID()
	private static final String EMAIL_ADDRESS = "test@kuali.org";
    private static final String SUPPRESS = "false"
    private static final String DEFAULT = "true"
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityEmail xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityTypeCode>${ENTITY_TYPE_CODE}</entityTypeCode>
        <entityId>${ENTITY_ID}</entityId>
        <emailType>
            <code>${TYPE_CODE}</code>
            <name>${TYPE_NAME}</name>
            <active>${TYPE_ACTIVE}</active>
            <sortCode>${TYPE_SORT_CODE}</sortCode>
            <versionNumber>${TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${TYPE_OBJECT_ID}</objectId>
        </emailType>
        <emailAddress>${EMAIL_ADDRESS}</emailAddress>
        <emailAddressUnmasked>${EMAIL_ADDRESS}</emailAddressUnmasked>
        <suppressEmail>${SUPPRESS}</suppressEmail>
        <defaultValue>${DEFAULT}</defaultValue>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityEmail>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityEmail.Builder builder = EntityEmail.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityEmail.Builder.create().build();
        def o2 = EntityEmail.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityEmail.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityEmail.class)
	}

    private create() {
		return EntityEmail.Builder.create(new EntityEmailContract() {
			def String id = EntityEmailTest.ID
            def String entityTypeCode = EntityEmailTest.ENTITY_TYPE_CODE
            def String entityId = EntityEmailTest.ENTITY_ID
			def CodedAttribute getEmailType() { CodedAttribute.Builder.create(new CodedAttributeContract() {
				def String code = EntityEmailTest.TYPE_CODE
				def String name = EntityEmailTest.TYPE_NAME
				def boolean active = EntityEmailTest.TYPE_ACTIVE.toBoolean()
                def String sortCode = EntityEmailTest.TYPE_SORT_CODE
                def Long versionNumber = EntityEmailTest.TYPE_VERSION_NUMBER
				def String objectId = EntityEmailTest.TYPE_OBJECT_ID
			}).build()}
            def String emailAddress = EntityEmailTest.EMAIL_ADDRESS
            def String emailAddressUnmasked = EntityEmailTest.EMAIL_ADDRESS
            def boolean suppressEmail = EntityEmailTest.SUPPRESS.toBoolean()
            def boolean defaultValue = EntityEmailTest.DEFAULT.toBoolean()
            def boolean active = EntityEmailTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityEmailTest.VERSION_NUMBER;
			def String objectId = EntityEmailTest.OBJECT_ID
        }).build()

	}

}
