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
package org.kuali.rice.core.api.membership

import org.junit.Assert

import org.junit.Test
import org.kuali.rice.core.test.JAXBAssert

import org.kuali.rice.core.api.membership.MemberType

/**
 * Unit test for MemberType
 */
class MemberTypeTest {

    private static final String ROLE_MEMBER_TYPE_XML = """<memberType xmlns="http://rice.kuali.org/core/v2_0">R</memberType>"""
    private static final String GROUP_MEMBER_TYPE_XML = """<memberType xmlns="http://rice.kuali.org/core/v2_0">G</memberType>"""
    private static final String PRINCIPAL_MEMBER_TYPE_XML = """<memberType xmlns="http://rice.kuali.org/core/v2_0">P</memberType>"""

    @Test
    void test_fromCode_null() {
        Assert.assertNull(MemberType.fromCode(null))
    }

    @Test(expected=IllegalArgumentException.class)
    void test_fromCode_illegal_value() {
        MemberType.fromCode("blah!");
    }

    @Test
    void test_fromCode_valid() {
        assert MemberType.ROLE == MemberType.fromCode(MemberType.ROLE.getCode())
        assert MemberType.GROUP == MemberType.fromCode(MemberType.GROUP.getCode())
        assert MemberType.PRINCIPAL == MemberType.fromCode(MemberType.PRINCIPAL.getCode())
    }

    @Test
 	void test_Xml_Marshal_Unmarshal() {
        JAXBAssert.assertEqualXmlMarshalUnmarshal(MemberType.ROLE, ROLE_MEMBER_TYPE_XML, MemberType.class)
        JAXBAssert.assertEqualXmlMarshalUnmarshal(MemberType.GROUP, GROUP_MEMBER_TYPE_XML, MemberType.class)
        JAXBAssert.assertEqualXmlMarshalUnmarshal(MemberType.PRINCIPAL, PRINCIPAL_MEMBER_TYPE_XML, MemberType.class)
 	}

}