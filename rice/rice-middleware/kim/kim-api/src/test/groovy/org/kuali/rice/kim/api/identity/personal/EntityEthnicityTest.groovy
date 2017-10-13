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
package org.kuali.rice.kim.api.identity.personal

import java.text.SimpleDateFormat
import junit.framework.Assert
import org.junit.Test
import org.kuali.rice.kim.api.test.JAXBAssert

class EntityEthnicityTest {
	private static final String ENTITY_ID = "190192";
    private static final String ID = "1"
    private static final String ETHNICITY_CODE = "CODE"
    private static final String SUB_ETHNICITY_CODE = "SUBCODE"
    private static final String SUPPRESS_PERSONAL = "false";
    
    private static final Long VERSION_NUMBER = new Integer(1);
	private static final String OBJECT_ID = UUID.randomUUID();

    private static final String XML = """
    <entityEthnicity xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <entityId>${ENTITY_ID}</entityId>
        <ethnicityCode>${ETHNICITY_CODE}</ethnicityCode>
        <subEthnicityCode>${SUB_ETHNICITY_CODE}</subEthnicityCode>
        <ethnicityCodeUnmasked>${ETHNICITY_CODE}</ethnicityCodeUnmasked>
        <subEthnicityCodeUnmasked>${SUB_ETHNICITY_CODE}</subEthnicityCodeUnmasked>
        <suppressPersonal>${SUPPRESS_PERSONAL}</suppressPersonal>
        <versionNumber>${VERSION_NUMBER}</versionNumber>
        <objectId>${OBJECT_ID}</objectId>
    </entityEthnicity>
    """

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_id_whitespace() {
        EntityEthnicity.Builder builder = EntityEthnicity.Builder.create();
        builder.setId("")
    }

    @Test
    void test_copy() {
        def o1 = EntityEthnicity.Builder.create().build();
        def o2 = EntityEthnicity.Builder.create(o1).build();

        Assert.assertEquals(o1, o2);
    }

    @Test
    void happy_path() {
        EntityEthnicity.Builder.create();
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, EntityEthnicity.class)
	}

    public static create() {
		return EntityEthnicity.Builder.create(new EntityEthnicityContract() {
            def String id = EntityEthnicityTest.ID
            def String entityId = EntityEthnicityTest.ENTITY_ID
            def String ethnicityCode = EntityEthnicityTest.ETHNICITY_CODE
            def String subEthnicityCode = EntityEthnicityTest.SUB_ETHNICITY_CODE
            def String ethnicityCodeUnmasked = EntityEthnicityTest.ETHNICITY_CODE
            def String subEthnicityCodeUnmasked = EntityEthnicityTest.SUB_ETHNICITY_CODE
            def boolean suppressPersonal = EntityEthnicityTest.SUPPRESS_PERSONAL.toBoolean()
            def Long versionNumber = EntityEthnicityTest.VERSION_NUMBER;
			def String objectId = EntityEthnicityTest.OBJECT_ID
        }).build()

	}
    
}
