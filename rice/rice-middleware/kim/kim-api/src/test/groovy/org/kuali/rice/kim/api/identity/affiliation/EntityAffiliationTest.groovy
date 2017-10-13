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
package org.kuali.rice.kim.api.identity.affiliation

import org.junit.Test
import org.kuali.rice.kim.api.test.JAXBAssert
import org.junit.Assert

class EntityAffiliationTest {
    private static final String ID = "1";
	private static final String ENTITY_ID = "190192";
    private static final String TYPE_CODE = "Home"
    private static final String TYPE_NAME = "Home-y"
    private static final String TYPE_EMPLOYEE = "false";
    private static final String TYPE_SORT_CODE = "0"
    private static final String TYPE_ACTIVE = "true"
    private static final Long TYPE_VERSION_NUMBER = new Integer(1)
	private static final String TYPE_OBJECT_ID = UUID.randomUUID()
	private static final String CAMPUS_CODE = "IU"
    private static final String DEFAULT = "true"
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityAffiliation xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <affiliationType>
            <code>${TYPE_CODE}</code>
            <name>${TYPE_NAME}</name>
            <active>${TYPE_ACTIVE}</active>
            <sortCode>${TYPE_SORT_CODE}</sortCode>
            <employmentAffiliationType>${TYPE_EMPLOYEE}</employmentAffiliationType>
            <versionNumber>${TYPE_VERSION_NUMBER}</versionNumber>
            <objectId>${TYPE_OBJECT_ID}</objectId>
        </affiliationType>
        <campusCode>${CAMPUS_CODE}</campusCode>
        <defaultValue>${DEFAULT}</defaultValue>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityAffiliation>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityAffiliation.Builder builder = EntityAffiliation.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityAffiliation.Builder.create().build();
        def o2 = EntityAffiliation.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityAffiliation.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityAffiliation.class)
	}

    public static create() {
		return EntityAffiliation.Builder.create(new EntityAffiliationContract() {
			def String id = EntityAffiliationTest.ID
            def String entityId = EntityAffiliationTest.ENTITY_ID
			def EntityAffiliationType getAffiliationType() { EntityAffiliationType.Builder.create(new EntityAffiliationTypeContract() {
				def String code = EntityAffiliationTest.TYPE_CODE
				def String name = EntityAffiliationTest.TYPE_NAME
                def boolean employmentAffiliationType = EntityAffiliationTest.TYPE_EMPLOYEE.toBoolean()
				def boolean active = EntityAffiliationTest.TYPE_ACTIVE.toBoolean()
                def String sortCode = EntityAffiliationTest.TYPE_SORT_CODE
                def Long versionNumber = EntityAffiliationTest.TYPE_VERSION_NUMBER
				def String objectId = EntityAffiliationTest.TYPE_OBJECT_ID
			}).build()}
            def String campusCode = EntityAffiliationTest.CAMPUS_CODE
            def boolean defaultValue = EntityAffiliationTest.DEFAULT.toBoolean()
            def boolean active = EntityAffiliationTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityAffiliationTest.VERSION_NUMBER;
			def String objectId = EntityAffiliationTest.OBJECT_ID
        }).build()

	}
}
