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
package org.kuali.rice.coreservice.api.component

import org.junit.Test
import org.kuali.rice.coreservice.test.JAXBAssert

class ComponentTest {
	private static final String CODE = "PC"
	private static final String NAME = "Config"
	private static final String NAMESPACE_CODE = "NSC"
    private static final String COMPONENT_SET_ID = "DD:myAppId";
	private static final boolean ACTIVE = true
	private static final Long VERSION_NUMBER = new Long(1);
	private static final String OBJECT_ID = UUID.randomUUID();
    private static final String XML = """
        <component xmlns="http://rice.kuali.org/core/v2_0">
            <code>${CODE}</code>
            <name>${NAME}</name>
            <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
            <active>${ACTIVE}</active>
            <versionNumber>${VERSION_NUMBER}</versionNumber>
            <objectId>${OBJECT_ID}</objectId>
        </component>
    """

    private static final String XML_COMPONENT_SET = """
        <component xmlns="http://rice.kuali.org/core/v2_0">
            <code>${CODE}</code>
            <name>${NAME}</name>
            <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
            <componentSetId>${COMPONENT_SET_ID}</componentSetId>
            <active>${ACTIVE}</active>
            <versionNumber>${VERSION_NUMBER}</versionNumber>
            <objectId>${OBJECT_ID}</objectId>
        </component>
    """

    

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        Component.Builder.create(null, null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_null() {
        Component.Builder.create(null, CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_empty() {
        Component.Builder.create("", CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_whitespace() {
        Component.Builder.create("  ", CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_null() {
        Component.Builder.create(NAMESPACE_CODE, null, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_empty() {
        Component.Builder.create(NAMESPACE_CODE, "", NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_second_whitespace() {
        Component.Builder.create(NAMESPACE_CODE, " ", NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_null() {
        Component.Builder.create(NAMESPACE_CODE, CODE, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_empty() {
        Component.Builder.create(NAMESPACE_CODE, CODE, "");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_third_whitespace() {
        Component.Builder.create(NAMESPACE_CODE, CODE, "  ");
    }

    @Test
    void happy_path() {
        Component.Builder.create(NAMESPACE_CODE, CODE, NAME);
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, Component.class)
	}

    @Test
	public void test_Xml_Marshal_Unmarshal_with_componentSetId() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.createWithDefaultComponentSetId(), XML_COMPONENT_SET, Component.class)
	}

    private create() {
        return createWithComponentSetId(null)
    }

    private createWithDefaultComponentSetId() {
        return createWithComponentSetId(COMPONENT_SET_ID)
    }

    private createWithComponentSetId(String _componentSetId) {
		return Component.Builder.create(new ComponentContract() {
				String code = ComponentTest.CODE
				String name = ComponentTest.NAME
				String namespaceCode = ComponentTest.NAMESPACE_CODE
                String componentSetId = _componentSetId
                boolean active = ComponentTest.ACTIVE
                Long versionNumber = ComponentTest.VERSION_NUMBER
				String objectId = ComponentTest.OBJECT_ID
			}).build()
	}
}
