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
package org.kuali.rice.kim.api.identity.principal

import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert


class PrincipalTest {
    private static final String PRINCIPAL_ID = "1";
    private static final String PRINCIPAL_NAME = "pName"
    private static final String PASSWORD = "password"
	private static final String ENTITY_ID = "190192";
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <principal xmlns="http://rice.kuali.org/kim/v2_0">
        <principalId>${PRINCIPAL_ID}</principalId>
        <principalName>${PRINCIPAL_NAME}</principalName>
        <entityId>${ENTITY_ID}</entityId>
        <password>${PASSWORD}</password>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </principal>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_principalName_whitespace() {
        Principal.Builder builder = Principal.Builder.create("");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_principalName_null() {
        Principal.Builder builder = Principal.Builder.create(null);
    }

    @Test
    void test_copy() {
        def o1 = Principal.Builder.create(PRINCIPAL_NAME).build();
        def o2 = Principal.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        Principal.Builder.create(PRINCIPAL_NAME);
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, Principal.class)
	}

    public static create() {
		return Principal.Builder.create(new PrincipalContract() {
			def String principalId = PrincipalTest.PRINCIPAL_ID
            def String principalName = PrincipalTest.PRINCIPAL_NAME
            def String entityId = PrincipalTest.ENTITY_ID
            def String password = PrincipalTest.PASSWORD
            def boolean active = PrincipalTest.ACTIVE.toBoolean()
            def Long versionNumber = PrincipalTest.VERSION_NUMBER;
			def String objectId = PrincipalTest.OBJECT_ID
        }).build()

	}
}
