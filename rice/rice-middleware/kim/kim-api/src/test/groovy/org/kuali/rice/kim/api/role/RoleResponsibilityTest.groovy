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

import org.junit.Test
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import junit.framework.Assert

class RoleResponsibilityTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    private static final ROLE_RESPONSIBILITY_ID = "42"
    private static final ROLE_ID = "1"
    private static final RESPONSIBILITY_ID = "13"
    private static final ACTIVE = true
    private static Long VERSION = 1L;

    private static final String XML = """
      <roleResponsibility xmlns="http://rice.kuali.org/kim/v2_0">
        <roleResponsibilityId>${ROLE_RESPONSIBILITY_ID}</roleResponsibilityId>
        <roleId>${ROLE_ID}</roleId>
        <responsibilityId>${RESPONSIBILITY_ID}</responsibilityId>
        <active>true</active>
        <versionNumber>1</versionNumber>
      </roleResponsibility>
  """

    @Test
    public void testXmlMarshalingAndUnMarshalling() {
        JAXBContext jc = JAXBContext.newInstance(RoleResponsibility.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()

        RoleResponsibility.Builder builder = RoleResponsibility.Builder.create()
        builder.roleResponsibilityId = ROLE_RESPONSIBILITY_ID
        builder.roleId = ROLE_ID
        builder.responsibilityId = RESPONSIBILITY_ID
        builder.active = ACTIVE
        builder.versionNumber = VERSION

        RoleResponsibility rr = builder.build()
        marshaller.marshal(rr, sw)
        String xml = sw.toString()

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml))
        Object expected = unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(expected, actual)
    }

    @Test
    public void testXmlUnmarshal() {
        JAXBContext jc = JAXBContext.newInstance(RoleResponsibility.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        RoleResponsibility rr = (RoleResponsibility) unmarshaller.unmarshal(new StringReader(XML))

        Assert.assertEquals(ROLE_RESPONSIBILITY_ID, rr.roleResponsibilityId)
        Assert.assertEquals(ROLE_ID, rr.roleId)
        Assert.assertEquals(RESPONSIBILITY_ID, rr.responsibilityId)
        Assert.assertEquals(ACTIVE, rr.active)
        Assert.assertEquals(VERSION, rr.versionNumber)
    }

    @Test
    public void test_Builder() {
        RoleResponsibility.Builder b =
        RoleResponsibility.Builder.create(ROLE_ID, RESPONSIBILITY_ID)
        b.versionNumber = VERSION
        b.build()
    }

    @Test
    public void test_Builder_create_from_contract() {
        RoleResponsibility.Builder b =
        RoleResponsibility.Builder.create(ROLE_ID, RESPONSIBILITY_ID)
        b.roleResponsibilityId = ROLE_RESPONSIBILITY_ID
        b.versionNumber = VERSION
        RoleResponsibility rr = b.build()

        rr.equals(RoleResponsibility.Builder.create(rr).build());
    }
}
