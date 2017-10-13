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
package org.kuali.rice.location.impl.country

import groovy.mock.interceptor.MockFor
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.krad.util.KRADPropertyConstants
import org.kuali.rice.location.api.country.Country
import org.kuali.rice.location.api.country.CountryService
import org.kuali.rice.coreservice.framework.parameter.ParameterService
import org.kuali.rice.coreservice.api.parameter.Parameter
import org.kuali.rice.coreservice.api.parameter.ParameterType
import org.kuali.rice.krad.util.KRADConstants

class CountryServiceImplTest {

    private final shouldFail = new GroovyTestCase().&shouldFail

    static Map<String, CountryBo> sampleCountries = new HashMap<String, CountryBo>()
    static Map<String, CountryBo> sampleCountriesKeyedByAltCode = new HashMap<String, CountryBo>()

    MockFor businessObjectServiceMockFor
    BusinessObjectService bos
    MockFor parameterServiceMockFor
    ParameterService parameterService
    CountryServiceImpl countryServiceImpl
    CountryService countryService

    @BeforeClass
    static void createSampleCountryBOs() {
        //Doing setup in a static context since bring up and down a server is an expensive operation
        CountryBo unitedStatesBo = new CountryBo(active: true, alternateCode: "USA", code: "US",
                name: "United States", restricted: false)
        CountryBo australiaBo = new CountryBo(active: true, alternateCode: "AU", code: "AUS",
                name: "Australia", restricted: false)
        CountryBo nowhereBo = new CountryBo(active: true, alternateCode: "ZZ", code: "ZZZ",
                name: "Australia", restricted: true)
        for (bo in [unitedStatesBo, australiaBo, nowhereBo]) {
            sampleCountries.put(bo.code, bo)
            sampleCountriesKeyedByAltCode.put(bo.alternateCode, bo)
        }
    }

    @Before
    void setupMockContext() {
        businessObjectServiceMockFor = new MockFor(BusinessObjectService.class)
        parameterServiceMockFor = new MockFor(ParameterService.class)
    }

    @Before
    void setupServiceUnderTest() {
        countryServiceImpl = new CountryServiceImpl()
        countryService = countryServiceImpl    //assign Interface type to implementation reference for unit test only
    }

    void injectBusinessObjectServiceIntoCountryService() {
        bos =  businessObjectServiceMockFor.proxyDelegateInstance()
        countryServiceImpl.setBusinessObjectService(bos)
    }

    void injectParameterService() {
        parameterService = parameterServiceMockFor.proxyDelegateInstance()
        countryServiceImpl.setParameterService(parameterService)
    }

    @Test
    public void test_getCountry() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return sampleCountries.get(map.get(KRADPropertyConstants.POSTAL_COUNTRY_CODE))
        }
        injectBusinessObjectServiceIntoCountryService()

        Country country = countryService.getCountry("US")
        Assert.assertEquals(CountryBo.to(sampleCountries.get("US")), country)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void testGetByPrimaryIdEmptyCountryCode() {
        shouldFail(IllegalArgumentException) {
            countryService.getCountry("")
        }
    }

    @Test
    public void testGetByPrimaryIdNullCountryCode() {
        shouldFail(IllegalArgumentException) {
            countryService.getCountry(null)
        }
    }

    @Test
    public void testGetByAlternateCode() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map ->
            [sampleCountriesKeyedByAltCode.get(
                    map.get(KRADPropertyConstants.ALTERNATE_POSTAL_COUNTRY_CODE))]
        }
        injectBusinessObjectServiceIntoCountryService()

        Country country = countryService.getCountryByAlternateCode("USA")
        Assert.assertEquals(CountryBo.to(sampleCountriesKeyedByAltCode.get("USA")), country)
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void testGetByAlternateCodeWhenNoneFound() {
        businessObjectServiceMockFor.demand.findMatching(1) {Class clazz, Map map -> []}
        injectBusinessObjectServiceIntoCountryService()

        Country country = countryService.getCountryByAlternateCode("ZZ")
        Assert.assertNull(country)
        businessObjectServiceMockFor.verify(bos)

    }

    @Test
    public void testGetByAlternateCodeWhenMultipleFound() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> [sampleCountries.get("US"), sampleCountries.get("AU")]
        }
        injectBusinessObjectServiceIntoCountryService()

        shouldFail(IllegalStateException) {
            countryService.getCountryByAlternateCode("US")
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void testGetByAlternateCodeWithEmptyCode() {
        shouldFail(IllegalArgumentException) {
            countryService.getCountryByAlternateCode(" ")
        }
    }

    @Test
    public void testGetByAlternateCodeWithNullCode() {
        shouldFail(IllegalArgumentException) {
            countryService.getCountryByAlternateCode(null)
        }
    }

    @Test
    public void findAllCountriesNotRestricted() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> [sampleCountries.get("US"), sampleCountries.get("AU")]
        }
        injectBusinessObjectServiceIntoCountryService()

        countryService.findAllCountriesNotRestricted()
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void findAllCountriesNotRestrictedReturnsImmutableList() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> [sampleCountries.get("US"), sampleCountries.get("AU")]
        }
        injectBusinessObjectServiceIntoCountryService()

        List<Country> countries = countryService.findAllCountriesNotRestricted()
        shouldFail(UnsupportedOperationException) {
            countries.add(null);
        }
        businessObjectServiceMockFor.verify(bos)

    }

    @Test
    public void testFindAllCountries() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> [sampleCountries.get("US"), sampleCountries.get("AU"), sampleCountries.get("ZZ")]
        }
        injectBusinessObjectServiceIntoCountryService()

        countryService.findAllCountries()
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void testFindAllCountriesReturnsImmutableList() {
        businessObjectServiceMockFor.demand.findMatching(1) {
            Class clazz, Map map -> [sampleCountries.get("US"), sampleCountries.get("AU"), sampleCountries.get("ZZ")]
        }
        injectBusinessObjectServiceIntoCountryService()

        List<Country> countries = countryService.findAllCountriesNotRestricted()
        shouldFail(UnsupportedOperationException) {
            countries.add(null)
        }
        businessObjectServiceMockFor.verify(bos)
    }

    @Test
    public void testGetDefaultCountry() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return sampleCountries.get(map.get(KRADPropertyConstants.POSTAL_COUNTRY_CODE))
        }
        injectBusinessObjectServiceIntoCountryService()
        parameterServiceMockFor.demand.getParameterValueAsString(1) {
            String namespaceCode, String componentCode, String parameterName -> "US"
        }
        injectParameterService()

        Country country = countryService.getDefaultCountry()
        assert country != null
        assert country.code == "US"

        businessObjectServiceMockFor.verify(bos)
        parameterServiceMockFor.verify(parameterService)
    }

    /**
     * If the default country code configured in the parameter service doesn't map to a valid country, then the method
     * should just return null
     */
    @Test
    public void testGetDefaultCountry_invalidDefaultCountryCode() {
        businessObjectServiceMockFor.demand.findByPrimaryKey(1) {
            Class clazz, Map map -> return sampleCountries.get(map.get(KRADPropertyConstants.POSTAL_COUNTRY_CODE))
        }
        injectBusinessObjectServiceIntoCountryService()
        parameterServiceMockFor.demand.getParameterValueAsString(1) {
            String namespaceCode, String componentCode, String parameterName -> "BLAH!!!"
        }
        injectParameterService()

        Country country = countryService.getDefaultCountry()
        assert country == null

        businessObjectServiceMockFor.verify(bos)
        parameterServiceMockFor.verify(parameterService)
    }

    @Test
    public void testGetDefaultCountry_nullDefaultCountryCode() {
        injectBusinessObjectServiceIntoCountryService()
        parameterServiceMockFor.demand.getParameterValueAsString(1) {
            String namespaceCode, String componentCode, String parameterName -> null
        }
        injectParameterService()

        Country country = countryService.getDefaultCountry()
        assert country == null

        businessObjectServiceMockFor.verify(bos)
        parameterServiceMockFor.verify(parameterService)
    }

    @Test
    public void testGetDefaultCountry_emptyDefaultCountryCode() {
        injectBusinessObjectServiceIntoCountryService()
        parameterServiceMockFor.demand.getParameterValueAsString(1) {
            String namespaceCode, String componentCode, String parameterName -> ""
        }
        injectParameterService()

        Country country = countryService.getDefaultCountry()
        assert country == null

        businessObjectServiceMockFor.verify(bos)
        parameterServiceMockFor.verify(parameterService)
    }

}
