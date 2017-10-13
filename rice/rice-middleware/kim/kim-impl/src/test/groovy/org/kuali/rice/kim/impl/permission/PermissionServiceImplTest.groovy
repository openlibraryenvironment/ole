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
//
//
//package org.kuali.rice.kim.impl.permission
//
//import groovy.mock.interceptor.MockFor
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//import org.kuali.rice.kim.api.type.KimType
//import org.kuali.rice.kim.api.type.KimTypeAttributeContract
//import org.kuali.rice.kim.api.type.KimTypeContract
//import org.kuali.rice.krad.service.BusinessObjectService
//
//class PermissionServiceImplTest {
//
//    private def MockFor mock
//
//    //importing the should fail method since I don't want to extend
//    //GroovyTestCase which is junit 3 style
//    private final shouldFail = new GroovyTestCase().&shouldFail
//
//    private static final KimType kimType = create();
//    private static final String key = "the_id";
//    private KimTypeBo bo = KimTypeBo.from(kimType)
//
//    private KimTypeInfoServiceImpl ktiservice;
//
//    @Before
//    void setupBoServiceMockContext() {
//        mock = new MockFor(BusinessObjectService)
//        ktiservice = new KimTypeInfoServiceImpl()
//    }
//
//    @Test
//    void test_get_kim_type_null_id() {
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//
//        shouldFail(IllegalArgumentException.class) {
//            ktiservice.getKimType(null)
//        }
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_get_kim_type_exists() {
//        mock.demand.findBySinglePrimaryKey (1) { clazz, obj -> bo }
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//        Assert.assertEquals (kimType, ktiservice.getKimType(key))
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_kim_type_by_name_namespace_null_first() {
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//
//        shouldFail(IllegalArgumentException.class) {
//            ktiservice.findKimTypeByNameAndNamespace(null, "the_name")
//        }
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_kim_type_by_name_namespace_null_second() {
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//
//        shouldFail(IllegalArgumentException.class) {
//            ktiservice.findKimTypeByNameAndNamespace("ns", null)
//        }
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_kim_type_by_name_namespace_exists() {
//        mock.demand.findMatching (1) { clazz, map -> [bo] }
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//        Assert.assertEquals (kimType, ktiservice.findKimTypeByNameAndNamespace("ns", "the_name"))
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_kim_type_by_name_namespace_multiple() {
//        mock.demand.findMatching (1) { clazz, map -> [bo, bo] }
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//        shouldFail(IllegalStateException.class) {
//            ktiservice.findKimTypeByNameAndNamespace("ns", "the_name")
//        }
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_all_kim_types_none() {
//        mock.demand.findMatching (1) { clazz, map -> null }
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//        def values = ktiservice.findAllKimTypes();
//        Assert.assertTrue(values.isEmpty())
//
//        //is this unmodifiable?
//        shouldFail(UnsupportedOperationException.class) {
//            values.add("")
//        }
//        mock.verify(boService)
//    }
//
//    @Test
//    void test_find_all_kim_types_exists() {
//        mock.demand.findMatching (1) { clazz, map -> [bo, bo] }
//        def boService = mock.proxyDelegateInstance()
//        ktiservice.setBusinessObjectService(boService);
//        def values = ktiservice.findAllKimTypes();
//        Assert.assertTrue(values.size() == 2)
//        Assert.assertEquals(KimTypeBo.to(bo), new ArrayList(values)[0]);
//        Assert.assertEquals(KimTypeBo.to(bo), new ArrayList(values)[1]);
//
//        //is this unmodifiable?
//        shouldFail(UnsupportedOperationException.class) {
//            values.add("")
//        }
//        mock.verify(boService)
//    }
//
//    private static create() {
//        return KimType.Builder.create(new KimTypeContract() {
//            String id = "the_id"
//            String serviceName = "fooService"
//            String namespaceCode = "ns"
//            String name = "the_name"
//            List<KimTypeAttributeContract> attributeDefinitions = Collections.emptyList()
//            boolean active = true
//            Long versionNumber = 1
//            String objectId = "S:dsadas"
//        }).build()
//    }
//}
