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
package org.kuali.rice.kim.api.identity.external

import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert

class EntityExternalIdentifierTest {
    private static final String ID = "1";
	private static final String ENTITY_ID = "190192";
    private static final String TYPE_CODE = "UID"
    private static final String TYPE_NAME = "University Id"
    private static final String TYPE_SORT_CODE = "0"
    private static final String TYPE_ACTIVE = "true"
    private static final String REQUIRES_ENCRYPTION = "false"
    private static final Long TYPE_VERSION_NUMBER = new Integer(1)
	private static final String TYPE_OBJECT_ID = UUID.randomUUID()
	private static final String EXTERNAL_ID = "123456789";
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityExternalIdentifier xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <externalIdentifierTypeCode>${TYPE_CODE}</externalIdentifierTypeCode>
        <externalIdentifierType>
            <code>${TYPE_CODE}</code>
            <name>${TYPE_NAME}</name>
            <active>${TYPE_ACTIVE}</active>
            <sortCode>${TYPE_SORT_CODE}</sortCode>
            <encryptionRequired>${REQUIRES_ENCRYPTION}</encryptionRequired>
            <versionNumber>${TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${TYPE_OBJECT_ID}</objectId>
        </externalIdentifierType>
        <externalId>${EXTERNAL_ID}</externalId>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityExternalIdentifier>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityExternalIdentifier.Builder builder = EntityExternalIdentifier.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityExternalIdentifier.Builder.create().build();
        def o2 = EntityExternalIdentifier.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityExternalIdentifier.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityExternalIdentifier.class)
	}

    public static create() {
		return EntityExternalIdentifier.Builder.create(new EntityExternalIdentifierContract() {
			def String id = EntityExternalIdentifierTest.ID
            def String entityId = EntityExternalIdentifierTest.ENTITY_ID
            def String externalIdentifierTypeCode = EntityExternalIdentifierTest.TYPE_CODE
			def EntityExternalIdentifierType getExternalIdentifierType() { EntityExternalIdentifierType.Builder.create(new EntityExternalIdentifierTypeContract() {
				def String code = EntityExternalIdentifierTest.TYPE_CODE
				def String name = EntityExternalIdentifierTest.TYPE_NAME
				def boolean active = EntityExternalIdentifierTest.TYPE_ACTIVE.toBoolean()
                def String sortCode = EntityExternalIdentifierTest.TYPE_SORT_CODE
                def boolean encryptionRequired = EntityExternalIdentifierTest.REQUIRES_ENCRYPTION.toBoolean()
                def Long versionNumber = EntityExternalIdentifierTest.TYPE_VERSION_NUMBER
				def String objectId = EntityExternalIdentifierTest.TYPE_OBJECT_ID
			}).build()}
            def String externalId = EntityExternalIdentifierTest.EXTERNAL_ID
            def Long versionNumber = EntityExternalIdentifierTest.VERSION_NUMBER;
			def String objectId = EntityExternalIdentifierTest.OBJECT_ID
        }).build()

	}

}
