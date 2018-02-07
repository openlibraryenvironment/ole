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
package org.kuali.rice.kim.api.identity.privacy

import org.junit.Test
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert

class EntityPrivacyPreferencesTest {
	private static final String ENTITY_ID = "190192";
    private static final Long VERSION_NUMBER = new Integer(1);
    private final static String SUPPRESS_NAME = "true";
    private final static String SUPPRESS_ADDRESS = "false";
    private final static String SUPPRESS_EMAIL = "false";
    private final static String SUPPRESS_PHONE = "true";
    private final static String SUPPRESS_PERSONAL = "true";
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityPrivacyPreferences xmlns="http://rice.kuali.org/kim/v2_0">
        <entityId>${ENTITY_ID}</entityId>
        <suppressName>${SUPPRESS_NAME}</suppressName>
        <suppressAddress>${SUPPRESS_ADDRESS}</suppressAddress>
        <suppressEmail>${SUPPRESS_EMAIL}</suppressEmail>
        <suppressPhone>${SUPPRESS_PHONE}</suppressPhone>
        <suppressPersonal>${SUPPRESS_PERSONAL}</suppressPersonal>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityPrivacyPreferences>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_entityId_whitespace() {
        EntityPrivacyPreferences.Builder builder = EntityPrivacyPreferences.Builder.create("");
    }
    
    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_entityId_null() {
        EntityPrivacyPreferences.Builder builder = EntityPrivacyPreferences.Builder.create(null);
    }

    @Test
    void test_copy() {
        def o1 = EntityPrivacyPreferences.Builder.create("10101").build();
        def o2 = EntityPrivacyPreferences.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityPrivacyPreferences.Builder.create("10101");
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityPrivacyPreferences.class)
	}

    public static create() {
		return EntityPrivacyPreferences.Builder.create(new EntityPrivacyPreferencesContract() {
            def String entityId = EntityPrivacyPreferencesTest.ENTITY_ID
			def boolean suppressName = EntityPrivacyPreferencesTest.SUPPRESS_NAME.toBoolean()
            def boolean suppressAddress = EntityPrivacyPreferencesTest.SUPPRESS_ADDRESS.toBoolean()
            def boolean suppressEmail = EntityPrivacyPreferencesTest.SUPPRESS_EMAIL.toBoolean()
            def boolean suppressPhone = EntityPrivacyPreferencesTest.SUPPRESS_PHONE.toBoolean()
            def boolean suppressPersonal = EntityPrivacyPreferencesTest.SUPPRESS_PERSONAL.toBoolean()
            def Long versionNumber = EntityPrivacyPreferencesTest.VERSION_NUMBER;
			def String objectId = EntityPrivacyPreferencesTest.OBJECT_ID
        }).build()

	}
}
