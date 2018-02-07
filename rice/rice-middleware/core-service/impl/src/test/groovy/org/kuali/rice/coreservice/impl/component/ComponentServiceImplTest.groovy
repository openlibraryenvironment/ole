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
package org.kuali.rice.coreservice.impl.component

import groovy.mock.interceptor.MockFor
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kuali.rice.coreservice.api.component.Component
import org.kuali.rice.coreservice.api.component.ComponentService
import org.kuali.rice.krad.service.BusinessObjectService
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.fail
import java.sql.Timestamp
import org.kuali.rice.coreservice.impl.namespace.NamespaceServiceImpl
import org.kuali.rice.coreservice.api.namespace.Namespace

/**
 * Unit test for {@link org.kuali.rice.coreservice.impl.component.ComponentServiceImpl}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class ComponentServiceImplTest {

    private MockFor boServiceMock
    private MockFor componentSetDaoMock

    //importing the should fail method since I don't want to extend
    //GroovyTestCase which is junit 3 style
    private final shouldFail = new GroovyTestCase().&shouldFail

    org.kuali.rice.coreservice.impl.component.ComponentServiceImpl serviceImpl
    ComponentService service
    BusinessObjectService boService
    org.kuali.rice.coreservice.impl.component.ComponentSetDao componentSetDao

    static final Component component = createComponent()
    static final org.kuali.rice.coreservice.impl.component.ComponentBo componentBo = convertComponent(component)

    static final Component derivedComponent = createDerivedComponent()
    static final org.kuali.rice.coreservice.impl.component.DerivedComponentBo derivedComponentBo = org.kuali.rice.coreservice.impl.component.DerivedComponentBo.from(derivedComponent)

    @Before
    void setupServiceUnderTest() {
        service = serviceImpl = new org.kuali.rice.coreservice.impl.component.ComponentServiceImpl()
    }

    @Before
    void setupBoServiceMockContext() {
        boServiceMock = new MockFor(BusinessObjectService)
        componentSetDaoMock = new MockFor(org.kuali.rice.coreservice.impl.component.ComponentSetDao)
    }

    @After
    void verifyMocks() {
        if (boService != null) {
            boServiceMock.verify(boService)
        }
        if (componentSetDao != null) {
            componentSetDaoMock.verify(componentSetDao)
        }
    }

    void injectBusinessObjectService() {
        boService = boServiceMock.proxyDelegateInstance()
        serviceImpl.setBusinessObjectService(boService)
    }

    void injectComponentSetDao() {
        componentSetDao = componentSetDaoMock.proxyDelegateInstance()
        serviceImpl.setComponentSetDao(componentSetDao)
    }

    @Test
    void test_getComponentByCode_null_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode(null, "myComponentCode")
        }
    }

    @Test
    void test_getComponentByCode_empty_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode("", "myComponentCode")
        }
    }

    @Test
    void test_getComponentByCode_blank_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode("  ", "myComponentCode")
        }
    }

    @Test
    void test_getComponentByCode_null_componentCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode("myNamespaceCode", null)
        }
    }

    @Test
    void test_getComponentByCode_empty_componentCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode("myNamespaceCode", "")
        }
    }

    @Test
    void test_getComponentByCode_blank_componentCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getComponentByCode("myNamespaceCode", "  ")
        }
    }

    @Test
    void test_getComponentByCode_exists() {
        boServiceMock.demand.findByPrimaryKey { clazz, map -> componentBo }
        injectBusinessObjectService()
        assert component == service.getComponentByCode(NAMESPACE_CODE, CODE)
    }

    @Test
    void test_getComponentsByCode_not_exists() {
        boServiceMock.demand.findByPrimaryKey(2) { clazz, map -> null }
        injectBusinessObjectService()
        assert null == service.getComponentByCode("blah", "blah")
    }

    @Test
    void test_getAllComponentsByNamespaceCode_null_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getAllComponentsByNamespaceCode(null)
        }
    }

    @Test
    void test_getAllComponentsByNamespaceCode_empty_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getAllComponentsByNamespaceCode("")
        }
    }

    @Test
    void test_getAllComponentsByNamespaceCode_blank_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getAllComponentsByNamespaceCode("  ")
        }
    }

    @Test
    void test_getAllComponentsByNamespaceCode_exists() {
        boServiceMock.demand.findMatching { clazz, map -> [componentBo] }
        boServiceMock.demand.findMatching { clazz, map -> [] }
        injectBusinessObjectService()
        List<Component> components = service.getAllComponentsByNamespaceCode(NAMESPACE_CODE)
        assertNotNull components
        assert 1 == components.size()
        assert component == components[0]
        assertImmutableList(components, component)
    }

    @Test
    void test_getAllComponentsByNamespaceCode_not_exists() {
        boServiceMock.demand.findMatching(2) { clazz, map -> [] }
        injectBusinessObjectService()
        List<Component> components = service.getAllComponentsByNamespaceCode("blah")
        assertNotNull components
        assert 0 == components.size()
        assertImmutableList(components, component)
    }

    @Test
    void test_getAllComponentsByNamespaceCode_with_derived() {
        boServiceMock.demand.findMatching { clazz, map -> [componentBo] }
        boServiceMock.demand.findMatching { clazz, map -> [derivedComponentBo] }
        injectBusinessObjectService()
        List<Component> components = service.getAllComponentsByNamespaceCode(NAMESPACE_CODE)
        assertNotNull components
        assert 2 == components.size()
        assert component == components[0]
        assert derivedComponent == components[1]
        assertImmutableList(components, component)
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_null_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getActiveComponentsByNamespaceCode(null)
        }
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_empty_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getActiveComponentsByNamespaceCode("")
        }
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_blank_namespaceCode() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getActiveComponentsByNamespaceCode("  ")
        }
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_exists() {
        boServiceMock.demand.findMatching { clazz, map ->
            if (!map.containsKey("active")) fail("Did not pass active criteria")
            [componentBo]
        }
        boServiceMock.demand.findMatching { clazz, map -> null }
        injectBusinessObjectService()
        List<Component> components = service.getActiveComponentsByNamespaceCode(NAMESPACE_CODE)
        assertNotNull components
        assert 1 == components.size()
        assert component == components[0]
        assertImmutableList(components, component)
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_not_exists() {
        boServiceMock.demand.findMatching { clazz, map -> [] }
        boServiceMock.demand.findMatching { clazz, map -> null }
        injectBusinessObjectService()
        List<Component> components = service.getActiveComponentsByNamespaceCode("blah")
        assertNotNull components
        assert 0 == components.size()
        assertImmutableList(components, component)
    }

    @Test
    void test_getActiveComponentsByNamespaceCode_with_derived() {
        boServiceMock.demand.findMatching { clazz, map ->
            if (!map.containsKey("active")) fail("Did not pass active criteria")
            [componentBo]
        }
        boServiceMock.demand.findMatching { clazz, map -> [derivedComponentBo] }
        injectBusinessObjectService()
        List<Component> components = service.getActiveComponentsByNamespaceCode(NAMESPACE_CODE)
        assertNotNull components
        assert 2 == components.size()
        assert component == components[0]
        assert derivedComponent == components[1]
        assertImmutableList(components, component)
    }

    @Test
    void test_getDerivedComponentSet_null_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getDerivedComponentSet(null)
        }
    }

    @Test
    void test_getDerivedComponentSet_empty_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getDerivedComponentSet("")
        }
    }

    @Test
    void test_getDerivedComponentSet_blank_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.getDerivedComponentSet("  ")
        }
    }

    @Test
    void test_getDerivedComponentSet_not_exists() {
        boServiceMock.demand.findMatching { clazz, map -> [] }
        injectBusinessObjectService()
        List<Component> components = service.getDerivedComponentSet("blah")
        assert components != null
        assert components.isEmpty()
        assertImmutableList(components, component)
    }

    @Test
    void test_publishDerivedComponents_null_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.publishDerivedComponents(null, [ component ])
        }
    }

    @Test
    void test_publishDerivedComponents_empty_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.publishDerivedComponents("", [ component ])
        }
    }

    @Test
    void test_publishDerivedComponents_blank_componentSetId() {
        injectBusinessObjectService()
        shouldFail(IllegalArgumentException.class) {
            service.publishDerivedComponents("  ", [ component ])
        }
    }

    @Test
    void test_publishDerivedComponents_invalidComponentSetId_onComponents() {
        injectBusinessObjectService()
        Component.Builder builder = Component.Builder.create(component)
        builder.setComponentSetId("myComponentSet")
        // should fail, componentSetIds don't match!
        shouldFail(IllegalArgumentException.class) {
            service.publishDerivedComponents("blah", [ builder.build() ])
        }
    }

    @Test
    void test_publishDerivedComponents_null_components() {

        org.kuali.rice.coreservice.impl.component.ComponentSetBo savedComponentSet = null;
        componentSetDaoMock.demand.getComponentSet { id -> null }
        componentSetDaoMock.demand.saveIgnoreLockingFailure { cs -> savedComponentSet = cs; return true }
        boServiceMock.demand.deleteMatching { clazz, crit -> assert crit.containsKey("componentSetId") }
        boServiceMock.demand.findMatching { clazz, crit -> []}

        injectBusinessObjectService()
        injectComponentSetDao()

        service.publishDerivedComponents("myComponentSet", null)
        assert service.getDerivedComponentSet("myComponentSet").isEmpty()

        assert savedComponentSet != null
        assert savedComponentSet.checksum != null
        assert savedComponentSet.componentSetId == "myComponentSet"
        assert savedComponentSet.lastUpdateTimestamp != null
    }

    /**
     * Tests attempting to publish an empty list of components in a situation where there are already components for
     * the component set.
     */
    @Test
    void test_publishDerivedComponents_empty_components_withExisting_componentSet() {

        org.kuali.rice.coreservice.impl.component.ComponentSetBo existingComponentSet = new org.kuali.rice.coreservice.impl.component.ComponentSetBo(componentSetId:"myComponentSet", checksum:"blah",
                lastUpdateTimestamp:new Timestamp(System.currentTimeMillis()), versionNumber:500)
        org.kuali.rice.coreservice.impl.component.ComponentSetBo savedComponentSet = null;

        componentSetDaoMock.demand.getComponentSet { id -> existingComponentSet }
        componentSetDaoMock.demand.saveIgnoreLockingFailure { cs -> cs.versionNumber++; savedComponentSet = cs; return true }
        boServiceMock.demand.deleteMatching { clazz, crit -> assert crit.containsKey("componentSetId") }
        boServiceMock.demand.findMatching { clazz, crit -> []}

        injectBusinessObjectService()
        injectComponentSetDao()

        service.publishDerivedComponents("myComponentSet", [])
        assert service.getDerivedComponentSet("myComponentSet").isEmpty()

        assert savedComponentSet != null
        assert savedComponentSet.versionNumber == 501
        assert savedComponentSet.checksum != "blah"
    }

    @Test
    void test_publishDerivedComponents() {

        List<org.kuali.rice.coreservice.impl.component.ComponentBo> publishedComponentBos = []
        org.kuali.rice.coreservice.impl.component.ComponentSetBo componentSet = null;
        
        boServiceMock.demand.findMatching { clazz, crit -> publishedComponentBos }
        boServiceMock.demand.deleteMatching { clazz, crit -> assert crit.containsKey("componentSetId") }
        boServiceMock.demand.save { bos -> publishedComponentBos = bos }
        componentSetDaoMock.demand.getComponentSet { id -> componentSet }
        componentSetDaoMock.demand.saveIgnoreLockingFailure { cs -> cs.versionNumber = 1; componentSet = cs; return true }
        boServiceMock.demand.findMatching { clazz, crit -> publishedComponentBos }

        injectBusinessObjectService()
        injectComponentSetDao()
        
        List<Component> components = service.getDerivedComponentSet("myComponentSet")
        assert components.isEmpty()
        service.publishDerivedComponents("myComponentSet", [ component ])
        components = service.getDerivedComponentSet("myComponentSet")
        assert components.size() == 1

        assert component.namespaceCode == components[0].namespaceCode
        assert component.code == components[0].code
    }

    /**
     * Tests that the calculateChecksum method returns the same checksum regardless of the order of the elements given
     * to it.
     */
    @Test
    void test_calculateChecksum_orderIndependent() {
        Component component2 = Component.Builder.create("a", "b", "name2").build()
        Component component3 = Component.Builder.create("b", "a", "name3").build()
        Component component4 = Component.Builder.create("c", "c", "name4").build()

        List<Component> components1 = new ArrayList<Component>()
        components1.add(component)
        components1.add(component2)
        components1.add(component3)
        components1.add(component4)

        String checksum1 = serviceImpl.calculateChecksum(components1)
        assert checksum1 != null


        List<Component> components2 = new ArrayList<Component>()
        components2.add(component3)
        components2.add(component2)
        components2.add(component)
        components2.add(component4)

        String checksum2 = serviceImpl.calculateChecksum(components2)
        assert checksum2 != null

        assert checksum1 == checksum2
    }

    @Test
    void test_calculateChecksum_emptyList() {
        String checksum1 = serviceImpl.calculateChecksum(new ArrayList<Component>())
        String checksum2 = serviceImpl.calculateChecksum(new ArrayList<Component>())
        assert checksum1 != null
        assert checksum2 != null
        assert checksum1 == checksum2
    }

    @Test
    void test_translateCollections_nullList() {
        List<Component> components = serviceImpl.translateCollections(null, null)
        assert components != null
        assert components.isEmpty()
        assertImmutableList(components, component)
    }

    @Test
    void test_translateCollections_emptyList() {
        List<Component> components = serviceImpl.translateCollections(new ArrayList<Component>(), new ArrayList<Component>())
        assert components != null
        assert components.isEmpty()
        assertImmutableList(components, component)
    }

    @Test
    void test_translateCollections_components() {
        List<Component> components = serviceImpl.translateCollections([componentBo], null)
        assert components != null
        assert components.size() == 1
        assert components[0] == component
        assertImmutableList(components, component)
    }

    @Test
    void test_translateCollections_derivedComponents() {
        List<Component> components = serviceImpl.translateCollections(null, [derivedComponentBo])
        assert components != null
        assert components.size() == 1
        assert components[0] == derivedComponent
        assertImmutableList(components, derivedComponent)
    }

    @Test
    void test_translateCollections_both() {
        List<Component> components = serviceImpl.translateCollections([componentBo], [derivedComponentBo])
        assert components != null
        assert components.size() == 2
        assert components[0] == component
        assert components[1] == derivedComponent
        assertImmutableList(components, component)
    }

    private <T> void assertImmutableList(List<T> list, T object) {
        shouldFail(UnsupportedOperationException) {
            list.add(object)
        }
    }

    private static final String NAMESPACE_CODE = "MyNamespaceCode"
    private static final String CODE = "MyComponentCode"
    private static final String NAME = "This is my component!"

    private static createComponent() {
        Component.Builder builder = Component.Builder.create(NAMESPACE_CODE, CODE, NAME)
        return builder.build()
    }

    private static ComponentBo convertComponent(Component component) {
        ComponentBo.setNamespaceService(new NamespaceServiceImpl() {
            Namespace getNamespace(String code) {
                return new Namespace()
            }
        })
        return ComponentBo.from(component)
    }

    private static final String DERIVED_CODE = "MyDerivedComponentCode"
    private static final String DERIVED_NAME = "This is my derived component!"
    private static final String DERIVED_COMPONENT_SET_ID = "derivedComponentSetId"

    private static createDerivedComponent() {
        Component.Builder builder = Component.Builder.create(NAMESPACE_CODE, DERIVED_CODE, DERIVED_NAME)
        builder.setComponentSetId(DERIVED_COMPONENT_SET_ID)
        return builder.build()
    }

}
