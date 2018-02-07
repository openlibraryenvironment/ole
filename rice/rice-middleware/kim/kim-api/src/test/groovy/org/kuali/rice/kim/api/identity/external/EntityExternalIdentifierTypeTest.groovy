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

class EntityExternalIdentifierTypeTest {
    private static final String CODE = "UID"
    private static final String NAME = "University Id"
    private static final String SORT_CODE = "0"
    private static final String ACTIVE = "true"
    private static final String REQUIRES_ENCRYPTION = "false"
    private static final Long VERSION_NUMBER = new Integer(1)
	private static final String OBJECT_ID = UUID.randomUUID()

    private static final String XML = """
    <entityExternalIdentifierType xmlns="http://rice.kuali.org/kim/v2_0">
            <code>${CODE}</code>
            <name>${NAME}</name>
            <active>${ACTIVE}</active>
            <sortCode>${SORT_CODE}</sortCode>
            <encryptionRequired>${REQUIRES_ENCRYPTION}</encryptionRequired>
            <versionNumber>${VERSION_NUMBER}</versionNumber>
            <objectId>${OBJECT_ID}</objectId>
    </entityExternalIdentifierType>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityExternalIdentifierType.Builder builder = EntityExternalIdentifierType.Builder.create(" ");
    }

    @Test
    void test_copy() {
        def o1 = EntityExternalIdentifierType.Builder.create("ABC").build();
        def o2 = EntityExternalIdentifierType.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityExternalIdentifierType.Builder.create("ABC");
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityExternalIdentifierType.class)
	}

    public static create() {
		return EntityExternalIdentifierType.Builder.create(new EntityExternalIdentifierTypeContract() {
			def String code = EntityExternalIdentifierTypeTest.CODE
			def String name = EntityExternalIdentifierTypeTest.NAME
			def boolean active = EntityExternalIdentifierTypeTest.ACTIVE.toBoolean()
            def String sortCode = EntityExternalIdentifierTypeTest.SORT_CODE
            def boolean encryptionRequired = EntityExternalIdentifierTypeTest.REQUIRES_ENCRYPTION.toBoolean()
            def Long versionNumber = EntityExternalIdentifierTypeTest.VERSION_NUMBER
			def String objectId = EntityExternalIdentifierTypeTest.OBJECT_ID
        }).build()
	}
}
