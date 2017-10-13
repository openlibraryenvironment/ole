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
package org.kuali.rice.kim.impl.group

import groovy.mock.interceptor.MockFor
import java.sql.Timestamp
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.core.api.criteria.CriteriaLookupService
import org.kuali.rice.core.api.criteria.GenericQueryResults
import org.kuali.rice.core.api.criteria.LookupCustomizer
import org.kuali.rice.core.api.criteria.QueryByCriteria
import org.kuali.rice.kim.api.KimConstants
import org.kuali.rice.kim.api.group.Group
import org.kuali.rice.kim.api.group.GroupMember
import org.kuali.rice.kim.api.group.GroupQueryResults
import org.kuali.rice.kim.api.group.GroupService
import org.kuali.rice.kim.impl.common.attribute.KimAttributeBo
import org.kuali.rice.kim.impl.type.KimTypeBo
import org.kuali.rice.krad.service.BusinessObjectService
import static org.kuali.rice.core.api.criteria.PredicateFactory.equal
import org.kuali.rice.core.api.membership.MemberType

class GroupServiceImplTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    static Map<String, GroupBo> sampleGroups = new HashMap<String, GroupBo>()
    static Map<String, GroupBo> sampleGroupsByName = new HashMap<String, GroupBo>()
    static GroupBo memberGroup
    static GroupMemberBo memberGroupMember
    static List<GroupMemberBo> group1Members = new ArrayList<GroupMemberBo>()
    static List<GroupMemberBo> group2Members = new ArrayList<GroupMemberBo>()

    MockFor businessObjectServiceMockFor
    BusinessObjectService bos
    static GenericQueryResults queryResults1
    static GenericQueryResults queryResultsAll
    static GenericQueryResults queryResultsEmpty

    //MockFor groupDaoMockFor
    //GroupDao groupDao

    MockFor criteriaLookupMockFor
    CriteriaLookupService cls

    GroupServiceImpl groupServiceImpl
    GroupService groupService

    @BeforeClass
    static void createSampleGroupBOs() {
        Calendar calendarFrom = Calendar.getInstance();

        //Doing setup in a static context since bring up and down a server is an expensive operation
        GroupMemberBo member1 = new GroupMemberBo(id: "1", memberId: "234", groupId: "1",
                typeCode: "P", activeFromDateValue: new Timestamp(new Long(1262402288000)),
                activeToDateValue: new Timestamp(new Long(4102543088000)))
        GroupMemberBo member2 = new GroupMemberBo(id: "2", memberId: "240", groupId: "1",
                typeCode: "P", activeFromDateValue: new Timestamp(new Long(1262402288000)),
                activeToDateValue: new Timestamp(new Long(4102543088000)))
        GroupMemberBo member3 = new GroupMemberBo(id: "3", memberId: "114", groupId: "1",
                typeCode: "G", activeFromDateValue: new Timestamp(new Long(1262402288000)),
                activeToDateValue: new Timestamp(new Long(4102543088000)))

        GroupMemberBo member4 = new GroupMemberBo(id: "4", memberId: "250", groupId: "2",
                typeCode: "P", activeFromDateValue: new Timestamp(new Long(1262402288000)),
                activeToDateValue: new Timestamp(new Long(4102543088000)))
        GroupMemberBo member5 = new GroupMemberBo(id: "5", memberId: "114", groupId: "2",
                typeCode: "G", activeFromDateValue: new Timestamp(new Long(1262402288000)),
                activeToDateValue: new Timestamp(new Long(4102543088000)))

        for (bo in [member1, member2, member3 ]) {
            group1Members.add(bo)
        }

        for (bo in [member4, member5 ]) {
            group2Members.add(bo)
        }

        KimTypeBo kimTypeBo = new KimTypeBo(id: "2", serviceName: "service", namespaceCode: "KUALI", name: "thisType",
                active: true)

        KimAttributeBo kimAttributeBo = new KimAttributeBo(id: "attrId1", componentName: "component1", attributeName: "attrName1",
                namespaceCode: "KUALI", attributeLabel: "label", active: true)

        GroupAttributeBo attribute1 = new GroupAttributeBo(id: "1", assignedToId: "2", kimAttributeId: "1",
                         attributeValue: "Y", kimTypeId: "2", kimType: kimTypeBo, kimAttribute: kimAttributeBo)


        List<GroupAttributeBo> attributesForOtherGroup = new ArrayList<GroupAttributeBo>()
        attributesForOtherGroup.add(attribute1)

        memberGroup = new GroupBo(active: true, id: "114", namespaceCode: "PUNK",
                name: "membergroup", description: "this is some member group", kimTypeId: "1")
        memberGroupMember = member3
        GroupBo someGroup = new GroupBo(active: true, id: "1", namespaceCode: "PUNK",
                name: "somegroup", description: "this is some group", kimTypeId: "1", members: group1Members)
        GroupBo otherGroup = new GroupBo(active: true, id: "2", namespaceCode: "ROCK",
                name: "othergroup", description: "this is some other group", kimTypeId: "2", members: group2Members,
                attributeDetails: attributesForOtherGroup)
        GroupBo thirdGroup = new GroupBo(active: true, id: "114", namespaceCode: "SOMETHING",
                name: "HMMM", description: "this is some weird group", kimTypeId: "1")
        for (bo in [someGroup, otherGroup, thirdGroup]) {
            sampleGroups.put(bo.id, bo)
            sampleGroupsByName.put(bo.namespaceCode + ";" + bo.name, bo)
        }

        //setup groupQueryResults
        GenericQueryResults.Builder builder = GenericQueryResults.Builder.create()
        builder.setMoreResultsAvailable(false)
        builder.setTotalRowCount(new Integer(0))
        builder.setResults(new ArrayList<GroupBo>())
        queryResultsEmpty =  builder.build()

        builder.setResults(Collections.singletonList(someGroup))

        builder.setTotalRowCount(new Integer(1))

        queryResults1 = builder.build()

        builder.setResults(new ArrayList<GroupBo> (sampleGroups.values()))
        builder.setTotalRowCount(new Integer(3))
        queryResultsAll = builder.build()
    }

    @Before
    void setupMockContext() {
        businessObjectServiceMockFor = new MockFor(BusinessObjectService.class)
        criteriaLookupMockFor = new MockFor(CriteriaLookupService.class)

    }

    @Before
    void setupServiceUnderTest() {
        groupServiceImpl = new GroupServiceImpl()
        groupService = groupServiceImpl    //assign Interface type to implementation reference for unit test only
    }

    void injectBusinessObjectServiceIntoGroupService() {
        bos =  businessObjectServiceMockFor.proxyDelegateInstance()
        groupServiceImpl.setBusinessObjectService(bos)
    }

    void injectCriteriaLookupServiceIntoGroupService() {
        cls = criteriaLookupMockFor.proxyDelegateInstance()
        groupServiceImpl.setCriteriaLookupService(cls)
    }

    @Test
    public void test_getGroup() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1..sampleGroups.size()) {
            Class clazz, Map primaryKey -> return sampleGroups.get(primaryKey.get("id"))
        }
        injectBusinessObjectServiceIntoGroupService()
        for (String id : sampleGroups.keySet()) {
            Group group = groupService.getGroup(id)
            Assert.assertEquals(GroupBo.to(sampleGroups.get(id)), group)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getGroupNonExistent() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map primaryKey -> return null
        }
        injectBusinessObjectServiceIntoGroupService()
        Group group = groupService.getGroup("badId")
        Assert.assertNull(group)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getGroupByName() {
        businessObjectServiceMockFor.demand.findMatching(1..sampleGroupsByName.size()) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroupsByName.get(map.get(KimConstants.UniqueKeyConstants.NAMESPACE_CODE) + ";" + map.get(KimConstants.UniqueKeyConstants.GROUP_NAME)))
        }
        injectBusinessObjectServiceIntoGroupService()

        for (String name : sampleGroupsByName.keySet()) {
            GroupBo tempGroup = sampleGroupsByName.get(name)
            Group group = groupService.getGroupByNamespaceCodeAndName(tempGroup.namespaceCode, tempGroup.name)
            Assert.assertEquals(GroupBo.to(sampleGroupsByName.get(name)), group)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getGroupByNameNonExistent() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()

        Group group = groupService.getGroupByNamespaceCodeAndName("badNamespace", "noname")
        Assert.assertNull(group)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_isMemberOfGroup() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map groupId -> return sampleGroups.get(groupId.get("id"))
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> group1Members
        }
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map groupId -> return sampleGroups.get(groupId.get("id"))
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> group2Members
        }
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map groupId -> return sampleGroups.get(groupId.get("id"))
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()

        // Should be member
        Assert.assertTrue("Principal '240' should be a member of 'somegroup'", groupService.isMemberOfGroup("240", "1"))

        //Should not be member
        Assert.assertFalse("Principal '240' should not be a member of 'othergroup'", groupService.isMemberOfGroup("240", "2"))

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getGroupsForPrincipal() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        // Should be member
        List<Group> groups = groupService.getGroupsByPrincipalId("240");
        Assert.assertEquals("Pricipal should only be a member of 1 group", 1, groups.size())
        List<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(GroupBo.to(sampleGroups.get("1")))
        Assert.assertEquals(expectedGroups, groups)
        businessObjectServiceMockFor.verify(bos)
    }

        @Test
    public void test_getGroupsForPrincipalByNamespace() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        // Should be member
        List<Group> groups = groupService.getGroupsByPrincipalIdAndNamespaceCode("240", "PUNK")
        Assert.assertEquals("Pricipal should only be a member of 1 group", 1, groups.size())
        List<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(GroupBo.to(sampleGroups.get("1")))
        Assert.assertEquals(expectedGroups, groups)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_findGroupIds() {
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResultsAll
        }
        injectCriteriaLookupServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()
        List<String> expectedIds = new ArrayList<String>()
        expectedIds.addAll(sampleGroups.keySet())

        QueryByCriteria.Builder query = QueryByCriteria.Builder.create()
        query.setPredicates(equal("active", "Y"))
        List<String> groupIds = groupService.findGroupIds(query.build())

        Assert.assertEquals(groupIds.size(), sampleGroups.size())
        Assert.assertEquals(expectedIds, groupIds)
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    public void test_lookupGroups() {
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResultsAll
        }
        injectCriteriaLookupServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()
        List<Group> expectedGroups = new ArrayList<Group>()
        for (GroupBo groupBo : sampleGroups.values()) {
            expectedGroups.add(GroupBo.to(groupBo))
        }

        QueryByCriteria.Builder query = QueryByCriteria.Builder.create()
        query.setPredicates(equal("active", "Y"))
        GroupQueryResults qr = groupService.findGroups(query.build())

        Assert.assertEquals(qr.getTotalRowCount(), sampleGroups.size())
        Assert.assertEquals(expectedGroups, qr.getResults())
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    public void test_isDirectMemberOfGroup() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()

        // Should be member
        Assert.assertTrue("Principal '240' should be a direct member of 'somegroup'", groupService.isDirectMemberOfGroup("240", "1"))

        //Should not be member
        Assert.assertFalse("Principal '240' should not be a member of 'othergroup'", groupService.isDirectMemberOfGroup("240", "2"))

        businessObjectServiceMockFor.verify(bos)
    }

    @Test void test_getGroupIdsForPrincipal() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        // Should be member
        List<String> groupIds = groupService.getGroupIdsByPrincipalId("240");
        Assert.assertEquals("PricipalId 240 should only be a member of 1 group", 1, groupIds.size())
        List<String> expectedGroupIds = Collections.singletonList("1");
        Assert.assertEquals(expectedGroupIds, groupIds)
        businessObjectServiceMockFor.verify(bos)
        criteriaLookupMockFor.verify(cls)

    }

    @Test
    void test_getGroupIdsForPrincipalByNamespace() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        // Should be member
        List<String> groupIds = groupService.getGroupIdsByPrincipalIdAndNamespaceCode("240", "PUNK");
        Assert.assertEquals("PricipalId 240 should only be a member of 1 group", 1, groupIds.size())
        List<String> expectedGroupIds = Collections.singletonList("1");
        Assert.assertEquals(expectedGroupIds, groupIds)
        businessObjectServiceMockFor.verify(bos)
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    void test_getDirectGroupIdsForPrincipal() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(sampleGroups.get("1").getMembers().get(0))
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        // Should be member
        List<String> groupIds = groupService.getDirectGroupIdsByPrincipalId("240");
        Assert.assertEquals("PricipalId 240 should only be a member of 1 group", 1, groupIds.size())
        List<String> expectedGroupIds = Collections.singletonList("1");
        Assert.assertEquals(expectedGroupIds, groupIds)
        businessObjectServiceMockFor.verify(bos)
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    void test_getMemberPrincipalIds() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(0..3) {
            Class clazz, Map primaryKey -> return sampleGroups.get(primaryKey.get("id"))
        }

        injectBusinessObjectServiceIntoGroupService()

        List<String> expectedIds = new ArrayList<String>()
        for (GroupMemberBo memberBo : sampleGroups.get("1").getMembers()){
            if (MemberType.PRINCIPAL.equals(memberBo.type)) {
                expectedIds.add(memberBo.memberId)
            }
        }
        List<String> actualIds = groupService.getMemberPrincipalIds("1")
        Assert.assertEquals("Should be " + expectedIds.size() + " ids returned", expectedIds.size(), actualIds.size())
        for (String id : actualIds) {
            Assert.assertTrue(id + "should be in List", expectedIds.contains(id) )
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_getDirectMemberPrincipalIds() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return group1Members
        }
        injectBusinessObjectServiceIntoGroupService()

        List<String> expectedIds = new ArrayList<String>()
        for (GroupMemberBo memberBo : sampleGroups.get("1").getMembers()){
            if (MemberType.PRINCIPAL.equals(memberBo.type)) {
                expectedIds.add(memberBo.memberId)
            }
        }
        List<String> actualIds = groupService.getDirectMemberPrincipalIds("1")
        Assert.assertEquals("Should be " + expectedIds.size() + "Ids returned", expectedIds.size(), actualIds.size())
        for (String id : actualIds) {
            Assert.assertTrue(id + "should be in List", expectedIds.contains(id) )
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_getMemberGroupIds() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(0..4) {
            Class clazz, Map primaryKey -> return sampleGroups.get(primaryKey.get("id"))
        }
        injectBusinessObjectServiceIntoGroupService()

        List<String> expectedIds = new ArrayList<String>()
        for (GroupMemberBo memberBo : sampleGroups.get("1").getMembers()){
            if (MemberType.GROUP.equals(memberBo.type)) {
                expectedIds.add(memberBo.memberId)
            }
        }
        List<String> actualIds = groupService.getMemberGroupIds("1")
        Assert.assertEquals("Should be " + expectedIds.size() + " Ids returned", expectedIds.size(), actualIds.size())
        for (String id : actualIds) {
            Assert.assertTrue(id + " should be in List", expectedIds.contains(id) )
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_getDirectMemberGroupIds() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return group1Members
        }
        injectBusinessObjectServiceIntoGroupService()

        List<String> expectedIds = new ArrayList<String>()
        for (GroupMemberBo memberBo : sampleGroups.get("1").getMembers()){
            if (MemberType.GROUP.equals(memberBo.type)) {
                expectedIds.add(memberBo.memberId)
            }
        }
        List<String> actualIds = groupService.getDirectMemberGroupIds("1")
        Assert.assertEquals("Should be " + expectedIds.size() + "Ids returned", expectedIds.size(), actualIds.size())
        for (String id : actualIds) {
            Assert.assertTrue(id + "should be in List", expectedIds.contains(id) )
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_getParentGroupIds() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(memberGroupMember)
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.emptyList()
        }

        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        List<String> expectedIds = Collections.singletonList(sampleGroups.get("1").id)
        List<String> actualIds = groupService.getParentGroupIds(memberGroupMember.memberId)
        Assert.assertEquals("Should be " + expectedIds.size() + "Ids returned", expectedIds.size(), actualIds.size())
        Assert.assertEquals(expectedIds, actualIds)
        businessObjectServiceMockFor.verify(bos)
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    void test_getDirectParentGroupIds() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return Collections.singletonList(memberGroupMember)
        }
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query, LookupCustomizer lc -> return queryResults1
        }
        injectBusinessObjectServiceIntoGroupService()
        injectCriteriaLookupServiceIntoGroupService()

        List<String> expectedIds = Collections.singletonList(sampleGroups.get("1").id)
        List<String> actualIds = groupService.getDirectParentGroupIds(memberGroupMember.memberId)
        Assert.assertEquals("Should be " + expectedIds.size() + "Ids returned", expectedIds.size(), actualIds.size())
        Assert.assertEquals(expectedIds, actualIds)
        businessObjectServiceMockFor.verify(bos)
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    void test_getMembers() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return group1Members
        }
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> return group2Members
        }
        /*businessObjectServiceMockFor.demand.findByPrimaryKey(1..sampleGroups.size()) {
            Class clazz, Map primaryKey -> return sampleGroups.get(primaryKey)
        }*/
        injectBusinessObjectServiceIntoGroupService()

        List<GroupMember> expected = new ArrayList<GroupMember>();
        for (GroupBo groupBo : sampleGroups.values()) {
            for (GroupMemberBo memberBo : groupBo.members) {
                expected.add(GroupMemberBo.to(memberBo))
            }
        }
        List<String> groupIds = ["1", "2"]
        List<GroupMember> actual = groupService.getMembers(groupIds)
        Assert.assertEquals("Should be " + expected.size() + "Ids returned", expected.size(), actual.size())
        for (GroupMember gm : actual) {
            Assert.assertTrue(gm.getId() + "should be in List", expected.contains(gm) )
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_getMembers_null() {
        shouldFail(IllegalArgumentException.class) {
            groupService.getMembers(null)
        }
    }

    @Test
    void test_getMembers_empty() {
        shouldFail(IllegalArgumentException.class) {
            groupService.getMembers([])
        }
    }


    @Test
    void test_createGroupNullGroup(){
        injectBusinessObjectServiceIntoGroupService()

        shouldFail(IllegalArgumentException.class) {
            groupService.createGroup(null)
        }
        businessObjectServiceMockFor.verify(bos)

    }

    @Test
    void test_updateGroupNullGroup(){
        injectBusinessObjectServiceIntoGroupService()

        shouldFail(IllegalArgumentException.class) {
            groupService.updateGroup(null, null)
        }
        businessObjectServiceMockFor.verify(bos)

    }
}
