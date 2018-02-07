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
package org.kuali.rice.kim.api.common.delegate

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import org.junit.Assert
import org.junit.Test
import java.sql.Timestamp
import java.text.SimpleDateFormat
import org.kuali.rice.core.api.membership.MemberType
import org.kuali.rice.core.api.delegation.DelegationType
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormat

class DelegateTypeTest {

    private final shouldFail = new GroovyTestCase().&shouldFail

    static final String ROLE_ID = "1"
    static final String DELEGATION_ID = "42"
    static final DelegationType DELEGATION_TYPE = DelegationType.PRIMARY
    static final MemberType DELEGATION_MEMBER_TYPE = MemberType.PRINCIPAL
    static final String KIM_TYPE_ID = "187"
    static final Map<String, String> ATTRIBUTES = [:]

    static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    static final String DELEGATION_MEMBER_ID = "1337"
    static final String MEMBER_ID = "17"
    static final String ROLE_MEMBER_ID = "256"
    static final String ACTIVE_FROM_STRING = "2011-01-01 12:00:00"
    static final DateTime ACTIVE_FROM = new DateTime(FORMATTER.parseDateTime(ACTIVE_FROM_STRING))
    static final String ACTIVE_TO_STRING = "2012-01-01 12:00:00"
    static final DateTime ACTIVE_TO = new DateTime(FORMATTER.parseDateTime(ACTIVE_TO_STRING))
    static final boolean ACTIVE = true;
    static final Long VERSION = 1L
    static final List<DelegateMember.Builder> DELEGATE_MEMBERS = [create_delegate_member()]

    private static DelegateMember.Builder create_delegate_member() {
        DelegateMember.Builder dmBuilder = DelegateMember.Builder.create()
        dmBuilder.delegationMemberId = DELEGATION_MEMBER_ID
        dmBuilder.delegationId = DELEGATION_ID
        dmBuilder.memberId = MEMBER_ID
        dmBuilder.roleMemberId = ROLE_MEMBER_ID
        dmBuilder.type = DELEGATION_MEMBER_TYPE
        dmBuilder.attributes = ATTRIBUTES
        dmBuilder.activeFromDate = ACTIVE_FROM
        dmBuilder.activeToDate = ACTIVE_TO
        dmBuilder.versionNumber = VERSION


        return  dmBuilder
    }

    private static final String XML = """
      <delegateType xmlns="http://rice.kuali.org/kim/v2_0">
        <roleId>${ROLE_ID}</roleId>
        <delegationId>${DELEGATION_ID}</delegationId>
        <delegationTypeCode>${DELEGATION_TYPE.code}</delegationTypeCode>
        <kimTypeId>${KIM_TYPE_ID}</kimTypeId>
        <members>
          <member>
            <delegationMemberId>${DELEGATION_MEMBER_ID}</delegationMemberId>
            <delegationId>${DELEGATION_ID}</delegationId>
            <memberId>${MEMBER_ID}</memberId>
            <roleMemberId>${ROLE_MEMBER_ID}</roleMemberId>
            <typeCode>${DELEGATION_MEMBER_TYPE.code}</typeCode>
            <attributes/>
            <roleMemberId>${ROLE_MEMBER_ID}</roleMemberId>
            <activeFromDate>${ACTIVE_FROM}</activeFromDate>
            <activeToDate>${ACTIVE_TO}</activeToDate>
            <active>${ACTIVE}</active>
            <versionNumber>${VERSION}</versionNumber>
          </member>
        </members>
        <active>${ACTIVE}</active>
      </delegateType>
    """

    @Test
    void testXmlUnmarshall() {
        List<DelegateMember> delegateMembers = new ArrayList<DelegateMember>();
        for (DelegateMember.Builder delegateMemberBuilder: DELEGATE_MEMBERS) {
            delegateMembers.add(delegateMemberBuilder.build());
        }

        JAXBContext jc = JAXBContext.newInstance(DelegateType.class)
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        DelegateType delegateType = (DelegateType) unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(ROLE_ID, delegateType.roleId)
        Assert.assertEquals(DELEGATION_ID, delegateType.delegationId)
        Assert.assertEquals(DELEGATION_TYPE, delegateType.delegationType)
        Assert.assertEquals(KIM_TYPE_ID, delegateType.kimTypeId)
        Assert.assertEquals(delegateMembers, delegateType.members)
        Assert.assertEquals(ACTIVE, delegateType.active)
    }

    @Test
    public void testXmlMarshalingAndUnMarshalling() {
        JAXBContext jc = JAXBContext.newInstance(DelegateType.class)
        Marshaller marshaller = jc.createMarshaller()
        StringWriter sw = new StringWriter()

        DelegateType.Builder builder = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS);
        builder.delegationId = DELEGATION_ID
        builder.kimTypeId = KIM_TYPE_ID
        marshaller.marshal(builder.build(), sw)
        String xml = sw.toString()

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        Object actual = unmarshaller.unmarshal(new StringReader(xml))
        Object expected = unmarshaller.unmarshal(new StringReader(XML))
        Assert.assertEquals(expected, actual)
    }


    @Test
    void test_builder() {
        DelegateType dt = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS).build()
    }

    @Test
    void test_immutableListOfDelegates() {
        DelegateType dt = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS).build()
        List<Delegate> delegates = dt.members;
        shouldFail(UnsupportedOperationException) {
            delegates.add(null)
        }
    }

    @Test
    void test_builderForContract() {
        DelegateType dt = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS).build()
        DelegateType clone = DelegateType.Builder.create(dt).build();
        Assert.assertEquals(dt, clone)
    }

    @Test
    void test_setRoleId_blank() {
        DelegateType.Builder b = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS)
        shouldFail(IllegalArgumentException) {
            b.roleId = " "
        }
    }

    @Test
    void test_setRoleId_null() {
        DelegateType.Builder b = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS)
        shouldFail(IllegalArgumentException) {
            b.roleId = null
        }
    }

    @Test
    void test_setDelegationTypeCode_blank() {
        DelegateType.Builder b = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS)
        shouldFail(IllegalArgumentException) {
            b.delegationType = null
        }
    }

    @Test
    void test_setDelegationTypeCode_null() {
        DelegateType.Builder b = DelegateType.Builder.create(ROLE_ID, DELEGATION_TYPE, DELEGATE_MEMBERS)
        shouldFail(IllegalArgumentException) {
            b.delegationType = null
        }
    }


}
