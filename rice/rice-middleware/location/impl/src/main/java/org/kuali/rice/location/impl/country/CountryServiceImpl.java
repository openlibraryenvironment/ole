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
package org.kuali.rice.location.impl.country;


import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.location.api.campus.Campus;
import org.kuali.rice.location.api.campus.CampusQueryResults;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.country.CountryQueryResults;
import org.kuali.rice.location.api.country.CountryService;
import org.kuali.rice.location.impl.campus.CampusBo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CountryServiceImpl implements CountryService {

    private BusinessObjectService businessObjectService;
    private ParameterService parameterService;
    private CriteriaLookupService criteriaLookupService;

    @Override
    public Country getCountry(final String code) {
        if (StringUtils.isBlank(code)) {
            throw new RiceIllegalArgumentException("code is blank");
        }

        CountryBo countryBo = businessObjectService.findByPrimaryKey(CountryBo.class, Collections.singletonMap(
                KRADPropertyConstants.POSTAL_COUNTRY_CODE, code));

        return CountryBo.to(countryBo);
    }

    @Override
    public Country getCountryByAlternateCode(final String alternateCode) {
        if (StringUtils.isBlank(alternateCode)) {
            throw new RiceIllegalArgumentException("alt code is blank");
        }

        Collection<CountryBo> countryList = businessObjectService.findMatching(CountryBo.class, Collections.singletonMap(
                KRADPropertyConstants.ALTERNATE_POSTAL_COUNTRY_CODE, alternateCode));
        if (countryList == null || countryList.isEmpty()) {
            return null;
        } else if (countryList.size() == 1) {
            return CountryBo.to(countryList.iterator().next());
        } else throw new RiceIllegalStateException("Multiple countries found with same alternateCode");
    }

    @Override
    public List<Country> findAllCountriesNotRestricted() {
        List<String> criteriaValues = new ArrayList<String>();
        criteriaValues.add(null);
        criteriaValues.add("N");

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(KRADPropertyConstants.POSTAL_COUNTRY_RESTRICTED_INDICATOR, criteriaValues);
        map.put("active", Boolean.TRUE);

        Collection<CountryBo> countryBos = businessObjectService.findMatching(CountryBo.class, Collections.unmodifiableMap(map));

        return convertListOfBosToImmutables(countryBos);
    }

    @Override
    public List<Country> findAllCountries() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("active", Boolean.TRUE);

        Collection<CountryBo> countryBos = businessObjectService.findMatching(CountryBo.class, Collections.unmodifiableMap(map));
        return convertListOfBosToImmutables(countryBos);
    }

    @Override
    public Country getDefaultCountry() {
        String defaultCountryCode = parameterService.getParameterValueAsString(KRADConstants.KNS_NAMESPACE,
                KRADConstants.DetailTypes.ALL_DETAIL_TYPE, KRADConstants.SystemGroupParameterNames.DEFAULT_COUNTRY);
        if (StringUtils.isBlank(defaultCountryCode)) {
            return null;
        }
        return getCountry(defaultCountryCode);
    }

    @Override
    public CountryQueryResults findCountries(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<CountryBo> results = criteriaLookupService.lookup(CountryBo.class, queryByCriteria);

        CountryQueryResults.Builder builder = CountryQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<Country.Builder> ims = new ArrayList<Country.Builder>();
        for (CountryBo bo : results.getResults()) {
            ims.add(Country.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    public ParameterService getParameterService() {
        return parameterService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    /**
     * Sets the businessObjectServiceMockFor attribute value.
     *
     * @param businessObjectService The businessObjectServiceMockFor to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    /**
     * Converts a List<CountryBo> to an Unmodifiable List<Country>
     *
     * @param countryBos a mutable List<CountryBo> to made completely immutable.
     * @return An unmodifiable List<Country>
     */
    List<Country> convertListOfBosToImmutables(final Collection<CountryBo> countryBos) {
        ArrayList<Country> countries = new ArrayList<Country>();
        for (CountryBo bo : countryBos) {
            Country country = CountryBo.to(bo);
            countries.add(country);
        }
        return Collections.unmodifiableList(countries);
    }

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new RiceIllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String) object)) {
            throw new RiceIllegalArgumentException(name + " was blank");
        }
    }

    /**
     * Sets the criteriaLookupService attribute value.
     *
     * @param criteriaLookupService The criteriaLookupService to set.
     */
    public void setCriteriaLookupService(final CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }
}
