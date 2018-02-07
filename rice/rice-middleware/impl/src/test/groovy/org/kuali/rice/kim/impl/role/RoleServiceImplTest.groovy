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
package org.kuali.rice.kim.impl.role

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.core.api.criteria.CriteriaLookupService
import org.kuali.rice.core.api.criteria.GenericQueryResults
import org.kuali.rice.core.api.criteria.QueryByCriteria
import org.kuali.rice.core.api.exception.RiceIllegalStateException
import org.kuali.rice.kim.api.KimConstants
import org.kuali.rice.kim.api.role.Role
import org.kuali.rice.kim.api.role.RoleContract
import org.kuali.rice.kim.api.role.RoleQueryResults
import org.kuali.rice.kim.api.role.RoleResponsibilityAction
import org.kuali.rice.kim.api.role.RoleService
import org.kuali.rice.krad.bo.PersistableBusinessObject
import org.kuali.rice.krad.service.BusinessObjectService

import static org.kuali.rice.core.api.criteria.PredicateFactory.equal

/**
 * Some basic, largely read-only tests of interacting with RoleService.
 * This test is extended by the RoleServiceRemoteTest integration test in order
 * to verify remote usage.
 */
class RoleServiceImplTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    static Map<String, RoleBo> sampleRoles = new HashMap<String, RoleBo>()
    static Map<String, RoleBo> sampleRolesByName= new HashMap<String, RoleBo>()

    MockFor businessObjectServiceMockFor
    BusinessObjectService bos
    static GenericQueryResults queryResultsAll

    MockFor criteriaLookupMockFor
    CriteriaLookupService cls

    RoleServiceImpl roleServiceImpl
    RoleService roleService

    @BeforeClass
    static void createSampleRoleBOs() {
        Calendar calendarFrom = Calendar.getInstance();

        //Doing setup in a static context since bring up and down a server is an expensive operation
        RoleBoLite someRole = new RoleBoLite(active: true, id: "1", namespaceCode: "PUNK",
                name: "somerole", description: "this is some role", kimTypeId: "1")
        RoleBoLite otherRole = new RoleBoLite(active: true, id: "2", namespaceCode: "ROCK",
                name: "otherrole", description: "this is some other role", kimTypeId: "2")
        RoleBoLite  thirdRole = new RoleBoLite(active: true, id: "114", namespaceCode: "SOMETHING",
                name: "HMMM", description: "this is some weird role", kimTypeId: "1")
        for (bo in [someRole, otherRole, thirdRole]) {
            sampleRoles.put(bo.id, bo)
            sampleRolesByName.put(bo.namespaceCode + ";" + bo.name, bo)
        }

        //setup roleQueryResults
        GenericQueryResults.Builder builder = GenericQueryResults.Builder.create()

        builder.setResults(new ArrayList<RoleBo> (sampleRoles.values()))
        builder.setTotalRowCount(new Integer(3))
        queryResultsAll = builder.build()
    }

    protected RoleBoLite toRoleBoLite(RoleContract r) {
        def rbl = new RoleBoLite();
        rbl.id = r.id
        rbl.active =  r.active
        rbl.description = r.description
        rbl.kimTypeId = r.kimTypeId
        rbl.name = r.name
        rbl.namespaceCode = r.namespaceCode
        return rbl
    }

    @Before
    void setupMockContext() {
        businessObjectServiceMockFor = new MockFor(BusinessObjectService.class)
        criteriaLookupMockFor = new MockFor(CriteriaLookupService.class)
    }

    @Before
    void setupServiceUnderTest() {
        roleServiceImpl = new RoleServiceImpl()
        roleServiceImpl.@proxiedRoleService = roleServiceImpl
        roleService = roleServiceImpl //assign Interface type to implementation reference for unit test only
    }

    void injectBusinessObjectServiceIntoRoleService() {
        bos =  businessObjectServiceMockFor.proxyDelegateInstance()
        roleServiceImpl.setBusinessObjectService(bos)
    }

    void injectCriteriaLookupServiceIntoRoleService() {
        cls = criteriaLookupMockFor.proxyDelegateInstance()
        roleServiceImpl.setCriteriaLookupService(cls)
    }

    @Test
    public void test_getRole() {
        businessObjectServiceMockFor.demand.findBySinglePrimaryKey(1..sampleRoles.size()) {
            Class clazz, String primaryKey -> return toRoleBoLite(sampleRoles.get(primaryKey))
        }
        injectBusinessObjectServiceIntoRoleService()
        for (String id : sampleRoles.keySet()) {
            Role role = roleService.getRole(id)
            Assert.assertEquals(RoleBoLite.to(sampleRoles.get(id)), role)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getRoleNonExistent() {
        businessObjectServiceMockFor.demand.findBySinglePrimaryKey(1) {
            Class clazz, String primaryKey -> return null
        }
        injectBusinessObjectServiceIntoRoleService()
        Role role = roleService.getRole("badId")
        Assert.assertNull(role)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getRoleByName() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1..sampleRolesByName.size()) {
            Class clazz, Map map -> return toRoleBoLite(sampleRolesByName.get(map.get(KimConstants.UniqueKeyConstants.NAMESPACE_CODE) + ";" + map.get(KimConstants.UniqueKeyConstants.NAME)))
        }
        injectBusinessObjectServiceIntoRoleService()

        for (String name : sampleRolesByName.keySet()) {
            RoleBoLite tempGroup = sampleRolesByName.get(name)
            Role role = roleService.getRoleByNamespaceCodeAndName(tempGroup.namespaceCode, tempGroup.name)
            Assert.assertEquals(RoleBoLite.to(sampleRolesByName.get(name)), role)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_getRoleByNameNonExistent() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return null
        }
        injectBusinessObjectServiceIntoRoleService()

        Role role = roleService.getRoleByNamespaceCodeAndName("badNamespace", "noname")
        Assert.assertNull(role)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void test_lookupRoles() {
        criteriaLookupMockFor.demand.lookup(1) {
            Class clazz, QueryByCriteria query -> return queryResultsAll
        }

        injectCriteriaLookupServiceIntoRoleService()

        List<Role> expectedRoles = new ArrayList<Role>()
        for (RoleBoLite roleBo : sampleRoles.values()) {
            expectedRoles.add(RoleBoLite.to(roleBo))
        }

        QueryByCriteria.Builder query = QueryByCriteria.Builder.create()
        query.setPredicates(equal("active", "Y"))
        RoleQueryResults qr = roleService.findRoles(query.build())

        Assert.assertEquals(qr.getTotalRowCount(), sampleRoles.size())
        Assert.assertEquals(expectedRoles, qr.getResults())
        criteriaLookupMockFor.verify(cls)
    }

    @Test
    void test_getRoleMembers_null() {
        shouldFail(IllegalArgumentException.class) {
            roleService.getRoleMembers(null, null)
        }
    }

    @Test
    void test_createRoleNullRole(){
        injectBusinessObjectServiceIntoRoleService()

        shouldFail(IllegalArgumentException.class) {
            roleService.createRole(null)
        }
        businessObjectServiceMockFor.verify(bos)

    }

    @Test
    void test_updateRoleNullRole(){
        injectBusinessObjectServiceIntoRoleService()

        shouldFail(IllegalArgumentException.class) {
            roleService.updateRole(null)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_createRoleResponsibilityAction_inputs() {
        shouldFail(IllegalArgumentException.class) {
            roleService.createRoleResponsibilityAction(null)
        }

        def bo = new RoleResponsibilityActionBo()
        bo.setId("1234")

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return bo
        }

        injectBusinessObjectServiceIntoRoleService()

        // throws RiceIllegalStateException in unit tests
        // Exception/SOAPFaultException in remote tests
        shouldFail(Exception.class) {
            roleService.createRoleResponsibilityAction(RoleResponsibilityActionBo.to(bo))
        }

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_updateRoleResponsibilityAction_inputs() {
        shouldFail(IllegalArgumentException.class) {
            roleService.updateRoleResponsibilityAction(null)
        }

        def bo = new RoleResponsibilityActionBo()
        bo.setId("1234")

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return null
        }

        injectBusinessObjectServiceIntoRoleService()

        // throws RiceIllegalStateException in unit tests
        // Exception/SOAPFaultException in remote tests
        shouldFail(Exception.class) {
            roleService.updateRoleResponsibilityAction(RoleResponsibilityActionBo.to(bo))
        }

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_deleteRoleResponsibilityAction_null() {
        shouldFail(IllegalArgumentException.class) {
            roleService.deleteRoleResponsibilityAction(null)
        }

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return null
        }

        injectBusinessObjectServiceIntoRoleService()

        // throws RiceIllegalStateException in unit tests
        // Exception/SOAPFaultException in remote tests
        shouldFail(Exception.class) {
            roleService.deleteRoleResponsibilityAction("1234")
        }

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_createRoleResponsibilityAction() {
        def builder = RoleResponsibilityAction.Builder.create()
        builder.setId("1234")

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return null
        }

        businessObjectServiceMockFor.demand.save(1) {
            PersistableBusinessObject s -> return s
        }

        injectBusinessObjectServiceIntoRoleService()

        def saved = roleService.createRoleResponsibilityAction(builder.build())

        Assert.assertEquals(builder.build(), saved)

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_updateRoleResponsibilityAction() {
        def bo = new RoleResponsibilityActionBo()
        bo.setId("1234")

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return bo
        }

        businessObjectServiceMockFor.demand.save(1) {
            PersistableBusinessObject s -> return s
        }

        injectBusinessObjectServiceIntoRoleService()

        def saved = roleService.updateRoleResponsibilityAction(RoleResponsibilityActionBo.to(bo))

        Assert.assertEquals(RoleResponsibilityActionBo.to(bo), saved)

        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    void test_deleteRoleResponsibilityAction() {
        def bo = new RoleResponsibilityActionBo()
        bo.setId("1234")

        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return bo
        }

        businessObjectServiceMockFor.demand.delete(1) {
            PersistableBusinessObject s -> Assert.assertEquals(bo, s)
        }

        injectBusinessObjectServiceIntoRoleService()

        roleService.deleteRoleResponsibilityAction("1234")

        businessObjectServiceMockFor.verify(bos)
    }
}
