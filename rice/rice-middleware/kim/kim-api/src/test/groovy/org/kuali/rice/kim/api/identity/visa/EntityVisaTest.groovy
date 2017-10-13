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
package org.kuali.rice.kim.api.identity.visa

import org.junit.Test
import org.kuali.rice.kim.api.test.JAXBAssert
import org.junit.Assert


class EntityVisaTest {
    private static final String ID = "1"
    private static final String ENTITY_ID = "190192";
    private static final String VISA_ID = "1111111111111111"
    private static final String VISA_TYPE_KEY = "VISA"
    private static final String VISA_ENTRY = "ENTRY"
    private static final Long VERSION_NUMBER = new Integer(1);
    private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityVisa xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <visaTypeKey>${VISA_TYPE_KEY}</visaTypeKey>
        <visaEntry>${VISA_ENTRY}</visaEntry>
        <visaId>${VISA_ID}</visaId>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityVisa>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityVisa.Builder builder = EntityVisa.Builder.create();
        builder.setId("")
    }

    @Test
    void test_copy() {
        def o1 = EntityVisa.Builder.create().build();
        def o2 = EntityVisa.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityVisa.Builder.create();
    }

    @Test
    public void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityVisa.class)
    }

    public static create() {
        return EntityVisa.Builder.create(new EntityVisaContract() {
            def String entityId = EntityVisaTest.ENTITY_ID
            def String visaTypeKey = EntityVisaTest.VISA_TYPE_KEY
            def String visaEntry = EntityVisaTest.VISA_ENTRY
            def String visaId = EntityVisaTest.VISA_ID
            def String id = EntityVisaTest.ID
            def Long versionNumber = EntityVisaTest.VERSION_NUMBER;
            def String objectId = EntityVisaTest.OBJECT_ID
        }).build()

    }
    
}
