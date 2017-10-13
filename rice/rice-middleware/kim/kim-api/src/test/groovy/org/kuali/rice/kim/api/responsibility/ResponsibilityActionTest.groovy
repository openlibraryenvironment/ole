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
package org.kuali.rice.kim.api.responsibility

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test

class ResponsibilityActionTest {

    private static final String XML = """
    <responsibilityAction xmlns:ns2="http://rice.kuali.org/core/v2_0" xmlns="http://rice.kuali.org/kim/v2_0">
        <groupId>aGroupId</groupId>
        <memberRoleId>aMemberRoleId</memberRoleId>
        <responsibilityName>aResponsibilityName</responsibilityName>
        <responsibilityId>aResponsibilityId</responsibilityId>
        <responsibilityNamespaceCode>aResponsibilityNamespaceCode</responsibilityNamespaceCode>
        <forceAction>false</forceAction>
        <qualifier/>
        <roleId>aRoleId</roleId>
    </responsibilityAction>
		"""

    def static createValidBuilder() {
        def r = ResponsibilityAction.Builder.create()
        r.groupId = "aGroupId"
        r.memberRoleId = "aMemberRoleId"
        r.responsibilityName = "aResponsibilityName"
        r.responsibilityId = "aResponsibilityId"
        r.responsibilityNamespaceCode = "aResponsibilityNamespaceCode"
        r.qualifier = [:]
        r.delegates = Collections.emptyList()
        r.roleId = "aRoleId";
        return r;
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_roleId() {
        def r = createValidBuilder();
        r.roleId = null
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_delegates() {
        def r = createValidBuilder();
        r.delegates = null
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_responsibilityNamespaceCode() {
        def r = createValidBuilder();
        r.responsibilityNamespaceCode = null
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_responsibilityId() {
        def r = createValidBuilder();
        r.responsibilityId = null
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_memberRoleId() {
        def r = createValidBuilder();
        r.memberRoleId = null
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_null_responsibilityName() {
        def r = createValidBuilder();
        r.responsibilityName = null
        r.build();
    }


    @Test(expected = IllegalArgumentException.class)
    void test_empty_roleId() {
        def r = createValidBuilder();
        r.roleId = ""
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_empty_responsibilityNamespaceCode() {
        def r = createValidBuilder();
        r.responsibilityNamespaceCode = ""
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_empty_responsibilityId() {
        def r = createValidBuilder();
        r.responsibilityId = ""
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_empty_memberRoleId() {
        def r = createValidBuilder();
        r.memberRoleId = ""
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_empty_responsibilityName() {
        def r = createValidBuilder();
        r.responsibilityName = ""
        r.build();
    }



    @Test(expected = IllegalArgumentException.class)
    void test_spaces_roleId() {
        def r = createValidBuilder();
        r.roleId = "  "
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_spaces_responsibilityNamespaceCode() {
        def r = createValidBuilder();
        r.responsibilityNamespaceCode = "  "
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_spaces_responsibilityId() {
        def r = createValidBuilder();
        r.responsibilityId = "  "
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_spaces_memberRoleId() {
        def r = createValidBuilder();
        r.memberRoleId = "  "
        r.build();
    }

    @Test(expected = IllegalArgumentException.class)
    void test_spaces_responsibilityName() {
        def r = createValidBuilder();
        r.responsibilityName = "  "
        r.build();
    }




    @Test
    void happy_path() {
        def r = createValidBuilder();
        r.build();
    }

    @Test
    void test_only_groupId() {
        def r = createValidBuilder();
        r.groupId = "aGroupId"
        r.principalId = null
        r.build();
    }

    @Test
    void test_only_princpialId() {
        def r = createValidBuilder();
        r.groupId = null
        r.principalId = "aPrincipalId"
        r.build();
    }

    @Test(expected = IllegalStateException.class)
    void test_both_principalId_and_groupId() {
        def r = createValidBuilder();
        r.groupId = "aGroupId"
        r.principalId = "aPrincipalId"
        r.build();
    }

	@Test
	void test_copy() {
		def o1b = createValidBuilder()
		def o1 = o1b.build()

		def o2 = ResponsibilityAction.Builder.create(o1).build()

		Assert.assertEquals(o1, o2)
	}

	@Test
	public void test_Xml_Marshal_Unmarshal() {
	  JAXBContext jc = JAXBContext.newInstance(ResponsibilityAction.class)
	  Marshaller marshaller = jc.createMarshaller()
	  StringWriter sw = new StringWriter()

	  ResponsibilityAction responsibility = this.create()
	  marshaller.marshal(responsibility,sw)
	  String xml = sw.toString()
	  Unmarshaller unmarshaller = jc.createUnmarshaller();
	  Object actual = unmarshaller.unmarshal(new StringReader(xml))
	  Object expected = unmarshaller.unmarshal(new StringReader(XML))
	  Assert.assertEquals(expected,actual)
	}

	private ResponsibilityAction create() {
		createValidBuilder().build()
	}
}
