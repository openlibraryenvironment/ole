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
package org.kuali.rice.location.impl.campus

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.location.api.campus.Campus
import org.kuali.rice.location.api.campus.CampusService
import org.kuali.rice.location.api.campus.CampusType

class CampusServiceImplTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    private MockFor mockBoService
    private BusinessObjectService boService
    CampusService campusService
    CampusServiceImpl campusServiceImpl;

    static Map<String, CampusBo> sampleCampuses = new HashMap<String, CampusBo>()
    static Map<String, CampusTypeBo> sampleCampusTypes = new HashMap<String, CampusTypeBo>()

    @BeforeClass
    static void createSampleCountryBOs() {
        CampusBo amesCampusBo = new CampusBo(active: true, code: "AMES", name: "IOWA STATE UNIVERSITY - AMES", shortName: "ISU - AMES", campusTypeCode: "F")
        CampusBo indCampusBo = new CampusBo(active: true, code: "IND", name: "INDIANA UNIVERSITY - INDIANAPOLIS", shortName: "IU - IND", campusTypeCode: "B")
        CampusTypeBo fiscalTypeBo = new CampusTypeBo(active: true, code: "F", name: "FISCAL")
        CampusTypeBo bothTypeBo = new CampusTypeBo(active: true, code: "B", name: "BOTH")
        for (bo in [amesCampusBo, indCampusBo]) {
            sampleCampuses.put(bo.code, bo)
        }
        for (bo in [fiscalTypeBo, bothTypeBo]) {
            sampleCampusTypes.put(bo.code, bo)
        }
    }

    @Before
    void setupBoServiceMockContext() {
        mockBoService = new MockFor(BusinessObjectService)
    }

    @Before
    void setupServiceUnderTest() {
        campusServiceImpl = new CampusServiceImpl()
        campusService = campusServiceImpl
    }

    void injectBusinessObjectServiceIntoCampusService() {
        boService = mockBoService.proxyDelegateInstance()
        campusServiceImpl.setBusinessObjectService(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampusEmptyCode() {
        Campus c = campusService.getCampus("")
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampusNullCode() {
        Campus c = campusService.getCampus(null)
        Assert.assertNull(c)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampusTypeEmptyCode() {
        CampusType ct = campusService.getCampusType("")
        Assert.assertNull(ct)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCampusTypeNullCode() {
        CampusType ct = campusService.getCampusType(null)
        Assert.assertNull(ct)
    }

    @Test
    public void testGetCampus() {
        mockBoService.demand.findByPrimaryKey(1..sampleCampuses.size()) {
            Class clazz, Map map -> return sampleCampuses.get(map.get("code"))
        }
        injectBusinessObjectServiceIntoCampusService()
        for (CampusBo campusBo in sampleCampuses.values()) {
            Assert.assertEquals(CampusBo.to(campusBo), campusService.getCampus(campusBo.code))
        }
        mockBoService.verify(boService)
    }

    @Test
    public void testGetCampusInvalidCode() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null
        }
        injectBusinessObjectServiceIntoCampusService()

        Assert.assertNull(campusService.getCampus("badcode"))
        mockBoService.verify(boService)
    }

    @Test
    public void testGetCampusType() {
        mockBoService.demand.findByPrimaryKey(1..sampleCampusTypes.size()) {
            Class clazz, Map map -> return sampleCampusTypes.get(map.get("code"))
        }
        injectBusinessObjectServiceIntoCampusService()
        for (CampusTypeBo campusTypeBo in sampleCampusTypes.values()) {
            Assert.assertEquals(CampusTypeBo.to(campusTypeBo), campusService.getCampusType(campusTypeBo.code))
        }
        mockBoService.verify(boService)
    }

    @Test
    public void testGetCampusTypeInvalidCode() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null
        }
        injectBusinessObjectServiceIntoCampusService()

        Assert.assertNull(campusService.getCampusType("badcode"))
        mockBoService.verify(boService)
    }

    @Test
    public void testGetAllCampuses() {
        mockBoService.demand.findMatching(1..1) {
            Class clazz, Map map -> return new ArrayList<Campus>(sampleCampuses.values())
        }
        injectBusinessObjectServiceIntoCampusService()

        //create list of campuses
        List<Campus> campusList = new ArrayList<Campus>();
        for (CampusBo campusBo in sampleCampuses.values()) {
            campusList.add(CampusBo.to(campusBo))
        }

        //get list from service
        List<Campus> returnedCampuses = campusService.findAllCampuses()

        //compare lists
        Assert.assertEquals(campusList.size(), returnedCampuses.size())
        for (Campus campus in campusList) {
            Assert.assertTrue(returnedCampuses.contains(campus))
        }
        mockBoService.verify(boService)
    }

    public void testGetAllCampusTypes() {
        mockBoService.demand.findMatching(1..1) {
            Class clazz, Map map -> return new ArrayList<Campus>(sampleCampusTypes.values())
        }
        def boService = mockBoService.proxyDelegateInstance()
        campusService.setBusinessObjectService(boService);

        //create list of campuses
        List<CampusType> campusTypeList = new ArrayList<CampusType>();
        for (CampusTypeBo campusTypeBo in sampleCampusTypes.values()) {
            campusTypeList.add(CampusTypeBo.to(campusTypeBo))
        }

        //get list from service
        List<Campus> returnedCampusTypes = campusService.findAllCampusTypes()

        //compare lists
        Assert.assertEquals(campusTypeList.size(), returnedCampusTypes.size())
        for (CampusType campusType in campusTypeList) {
            Assert.assertTrue(returnedCampusTypes.contains(campusType))
        }
        mockBoService.verify(boService)
    }
}
