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
package org.kuali.rice.coreservice.api.parameter

import org.junit.Test
import org.kuali.rice.coreservice.test.JAXBAssert

class ParameterKeyTest {
        private static final String XML = """
        <parameterKey xmlns="http://rice.kuali.org/core/v2_0">
            <applicationId>AC</applicationId>
            <namespaceCode>NC</namespaceCode>
            <componentCode>CC</componentCode>
            <name>N</name>
        </parameterKey>
    """

    private static final String APPLICATION_ID = "AC"
    private static final String NAMESPACE_CODE = "NC"
    private static final String COMPONENT_CODE = "CC"
    private static final String NAME = "N"

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_all_null() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(null, null, null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_first_null() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(null, NAMESPACE_CODE, COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_first_empty() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create("", NAMESPACE_CODE, COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_first_whitespace() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(" ", NAMESPACE_CODE, COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_second_null() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, null, COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_second_empty() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, "", COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_second_whitespace() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, " ", COMPONENT_CODE, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_third_null() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, null, NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_third_empty() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, "", NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_third_whitespace() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, " ", NAME);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_fourth_null() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_fourth_empty() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, "");
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Key_fail_fourth_whitespace() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, " ");
    }

    @Test
    void happy_path() {
        org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, NAME);
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, org.kuali.rice.coreservice.api.parameter.ParameterKey.class)
	}

    private create() {
		return org.kuali.rice.coreservice.api.parameter.ParameterKey.create(APPLICATION_ID, NAMESPACE_CODE, COMPONENT_CODE, NAME);
	}
}
