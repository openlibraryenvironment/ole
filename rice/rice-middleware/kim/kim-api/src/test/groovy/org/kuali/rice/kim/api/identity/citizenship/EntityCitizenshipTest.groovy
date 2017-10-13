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
package org.kuali.rice.kim.api.identity.citizenship

import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.junit.Test
import org.kuali.rice.kim.api.identity.CodedAttribute
import org.kuali.rice.kim.api.identity.CodedAttributeContract
import org.kuali.rice.kim.api.test.JAXBAssert
import org.junit.Assert
import org.joda.time.DateTime


class EntityCitizenshipTest {
    private static final String ID = "1";
	private static final String ENTITY_ID = "190192";
    private static final String STATUS_CODE = "Home"
    private static final String STATUS_NAME = "Home-y"
    private static final String STATUS_SORT_CODE = "0"
    private static final String STATUS_ACTIVE = "true"
    private static final Long STATUS_VERSION_NUMBER = new Integer(1)
	private static final String STATUS_OBJECT_ID = UUID.randomUUID()
    private static final String COUNTRY_CODE = "MX";
    static final String START_DATE_STRING = "2011-01-01"
    static final DateTime START_DATE = new DateTime(START_DATE_STRING)
    static final String END_DATE_STRING = "2012-01-01"
    static final DateTime END_DATE = new DateTime(END_DATE_STRING)
    private static final String ACTIVE = "true"
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityCitizenship xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <status>
            <code>${STATUS_CODE}</code>
            <name>${STATUS_NAME}</name>
            <active>${STATUS_ACTIVE}</active>
            <sortCode>${STATUS_SORT_CODE}</sortCode>
            <versionNumber>${STATUS_VERSION_NUMBER}</versionNumber>
            <objectId>${STATUS_OBJECT_ID}</objectId>
        </status>
        <countryCode>${COUNTRY_CODE}</countryCode>
        <startDate>${START_DATE_STRING}</startDate>
        <endDate>${END_DATE_STRING}</endDate>
        <active>${ACTIVE}</active>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityCitizenship>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityCitizenship.Builder builder = EntityCitizenship.Builder.create();
        builder.setId(" ")
    }

    @Test
    void test_copy() {
        def o1 = EntityCitizenship.Builder.create().build();
        def o2 = EntityCitizenship.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityCitizenship.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityCitizenship.class)
	}

    public static create() {
		return EntityCitizenship.Builder.create(new EntityCitizenshipContract() {
			def String id = EntityCitizenshipTest.ID
            def String entityId = EntityCitizenshipTest.ENTITY_ID
			def CodedAttribute getStatus() { CodedAttribute.Builder.create(new CodedAttributeContract() {
				def String code = EntityCitizenshipTest.STATUS_CODE
				def String name = EntityCitizenshipTest.STATUS_NAME
				def boolean active = EntityCitizenshipTest.STATUS_ACTIVE.toBoolean()
                def String sortCode = EntityCitizenshipTest.STATUS_SORT_CODE
                def Long versionNumber = EntityCitizenshipTest.STATUS_VERSION_NUMBER
				def String objectId = EntityCitizenshipTest.STATUS_OBJECT_ID
			}).build()
            }
            def String countryCode = EntityCitizenshipTest.COUNTRY_CODE
            def DateTime startDate = EntityCitizenshipTest.START_DATE
            def DateTime endDate = EntityCitizenshipTest.END_DATE
            def boolean active = EntityCitizenshipTest.ACTIVE.toBoolean()
            def Long versionNumber = EntityCitizenshipTest.VERSION_NUMBER;
			def String objectId = EntityCitizenshipTest.OBJECT_ID
        }).build()

	}
}
