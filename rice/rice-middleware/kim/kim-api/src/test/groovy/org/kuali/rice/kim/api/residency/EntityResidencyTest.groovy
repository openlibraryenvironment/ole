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
package org.kuali.rice.kim.api.residency

import org.junit.Test
import org.kuali.rice.kim.api.identity.residency.EntityResidency
import org.junit.Assert
import org.kuali.rice.kim.api.test.JAXBAssert
import org.kuali.rice.kim.api.identity.residency.EntityResidencyContract


class EntityResidencyTest {
    private static final String ENTITY_ID = "190192";
    private static final String ID = "1"
    private static final String DETERMINATION_METHOD = "hmm"
    private static final String IN_STATE = "TX"
    private static final String SUPPRESS_PERSONAL = "false";
    
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityResidency xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <determinationMethod>${DETERMINATION_METHOD}</determinationMethod>
        <inState>${IN_STATE}</inState>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityResidency>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityResidency.Builder builder = EntityResidency.Builder.create();
        builder.setId("")
    }

    @Test
    void test_copy() {
        def o1 = EntityResidency.Builder.create().build();
        def o2 = EntityResidency.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityResidency.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityResidency.class)
	}

    public static create() {
		return EntityResidency.Builder.create(new EntityResidencyContract() {
            def String id = EntityResidencyTest.ID
            def String entityId = EntityResidencyTest.ENTITY_ID
            def String determinationMethod = EntityResidencyTest.DETERMINATION_METHOD
            def String inState = EntityResidencyTest.IN_STATE
            def Long versionNumber = EntityResidencyTest.VERSION_NUMBER;
			def String objectId = EntityResidencyTest.OBJECT_ID
        }).build()

	}
}
