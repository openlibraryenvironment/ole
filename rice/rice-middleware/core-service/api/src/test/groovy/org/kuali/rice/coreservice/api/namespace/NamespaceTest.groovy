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
package org.kuali.rice.coreservice.api.namespace

import org.junit.Test
import org.kuali.rice.coreservice.test.JAXBAssert

class NamespaceTest {
	private static final String CODE = "PC"
	private static final String APP_ID = "AC"
	private static final Long VERSION_NUMBER = new Long(1);
	private static final String OBJECT_ID = UUID.randomUUID();
	private static final String XML = """
        <namespace xmlns="http://rice.kuali.org/core/v2_0">
            <code>${CODE}</code>
            <applicationId>${APP_ID}</applicationId>
            <name>N</name>
            <active>true</active>
            <versionNumber>${VERSION_NUMBER}</versionNumber>
            <objectId>${OBJECT_ID}</objectId>
        </namespace>
    """

    

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_all_null() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(null, null);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_null() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(null, APP_ID);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_empty() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create("", APP_ID);
    }

    @Test(expected=IllegalArgumentException.class)
    void test_Builder_fail_first_whitespace() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create("  ", APP_ID);
    }

    @Test
    void test_Builder_fail_second_null() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(CODE, null);
    }

    @Test
    void test_Builder_fail_second_empty() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(CODE, "");
    }

    @Test
    void test_Builder_fail_second_whitespace() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(CODE, "");
    }

    @Test
    void happy_path() {
        org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(CODE, APP_ID);
    }

    @Test
	public void test_Xml_Marshal_Unmarshal() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(this.create(), XML, org.kuali.rice.coreservice.api.namespace.Namespace.class)
	}

    private create() {
		return org.kuali.rice.coreservice.api.namespace.Namespace.Builder.create(new org.kuali.rice.coreservice.api.namespace.NamespaceContract() {
				def String code = NamespaceTest.CODE
				def String applicationId = NamespaceTest.APP_ID
                def String name = "N"
                def boolean active = true
                def Long versionNumber = NamespaceTest.VERSION_NUMBER
				def String objectId = NamespaceTest.OBJECT_ID
			}).build()
	}
}
