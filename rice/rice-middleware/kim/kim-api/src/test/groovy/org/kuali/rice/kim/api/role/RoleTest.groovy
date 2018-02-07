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
package org.kuali.rice.kim.api.role

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert
import org.junit.Test

class RoleTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    private static final String ID = "1"
    private static final String NAME = "User"
    private static final String DESCRIPTION = "This role derives its members from the users in the Principal table..."
    private static final String NAMESPACE_CODE = "KUALI"
    private static final String KIM_TYPE_ID = "2"
    private static final boolean ACTIVE = true
    private static final Long VERSION = 1L


    private static final String XML = """
      <role xmlns="http://rice.kuali.org/kim/v2_0">
        <id>${ID}</id>
        <name>${NAME}</name>
        <namespaceCode>${NAMESPACE_CODE}</namespaceCode>
        <description>${DESCRIPTION}</description>
        <kimTypeId>${KIM_TYPE_ID}</kimTypeId>
        <active>true</active>
        <versionNumber>1</versionNumber>
      </role>
  """

    @Test
    public void testXmlMarshalingAndUnMarshalling() {
        JAXBContext jc = JAXBContext.newInstance(Role.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()

        Role.Builder builder = Role.Builder.create(ID, NAME, NAMESPACE_CODE, DESCRIPTION, KIM_TYPE_ID)
        builder.setVersionNumber(VERSION)
        Role role = builder.build()
        marshaller.marshal(role, sw)
        String xml = sw.toString()

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml))
        Object expected = unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(expected, actual)
    }

    @Test
    public void testXmlUnmarshal() {
        JAXBContext jc = JAXBContext.newInstance(Role.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Role role = (Role) unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(ID, role.id)
        Assert.assertEquals(NAME, role.name)
        Assert.assertEquals(DESCRIPTION, role.description)
        Assert.assertEquals(NAMESPACE_CODE, role.namespaceCode)
        Assert.assertEquals(KIM_TYPE_ID, role.kimTypeId)
        Assert.assertEquals(ACTIVE, role.active)
        Assert.assertEquals(VERSION, role.versionNumber)
    }

    @Test
    public void test_setId_whitespace() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setId("  ")
        }
    }

    @Test
    public void test_setId_blank() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setId("      ")
        }
    }

    @Test
    public void test_setName_null() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setName(null)
        }
    }

    @Test
    public void test_setName_blank() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setName("      ")
        }
    }

    @Test
    public void test_setNamespaceCode_null() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setNamespaceCode(null)
        }
    }

    @Test
    public void test_setNamespaceCode_blank() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setNamespaceCode("")
        }
    }

    @Test
    public void test_setKimTypeId_null() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setKimTypeId(null)
        }
    }

    @Test
    public void test_setKimTypeId_blank() {
        Role.Builder b = Role.Builder.create()
        shouldFail(IllegalArgumentException) {
            b.setKimTypeId("      ")
        }
    }


}
