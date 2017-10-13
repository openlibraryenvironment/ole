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
package org.kuali.rice.location.impl.state;


import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.country.CountryService;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;
import org.kuali.rice.location.api.state.State;
import org.kuali.rice.location.api.state.StateQueryResults;
import org.kuali.rice.location.api.state.StateService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateServiceImpl implements StateService {

    private BusinessObjectService businessObjectService;
    private CountryService countryService;
    private CriteriaLookupService criteriaLookupService;

    @Override
    public State getState(String countryCode, String code) {
        if (StringUtils.isBlank(countryCode)) {
            throw new RiceIllegalArgumentException(("countryCode is null"));
        }

        if (StringUtils.isBlank(code)) {
            throw new RiceIllegalArgumentException(("code is null"));
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("countryCode", countryCode);
        map.put("code", code);

        return StateBo.to(businessObjectService.findByPrimaryKey(StateBo.class, Collections.unmodifiableMap(map)));
    }

    @Override
    public List<State> findAllStatesInCountry(String countryCode) {
        if (StringUtils.isBlank(countryCode)) {
            throw new RiceIllegalArgumentException(("countryCode is null"));
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("countryCode", countryCode);
        map.put("active", Boolean.TRUE);

        final Collection<StateBo> bos = businessObjectService.findMatching(StateBo.class, Collections.unmodifiableMap(map));
        if (bos == null) {
            return Collections.emptyList();
        }

        final List<State> toReturn = new ArrayList<State>();
        for (StateBo bo : bos) {
            if (bo != null && bo.isActive()) {
                toReturn.add(StateBo.to(bo));
            }
        }

        return Collections.unmodifiableList(toReturn);
    }
    
    @Override
    public List<State> findAllStatesInCountryByAltCode(String alternateCode) {
        if (StringUtils.isBlank(alternateCode)) {
            throw new RiceIllegalArgumentException(("alternateCode is null"));
        }
        
        Country country = getCountryService().getCountryByAlternateCode(alternateCode);
        
        if(country == null) {
            throw new RiceIllegalStateException("The alternateCode is not a valid Alternate Postal Country Code.  alternateCode: " + alternateCode);
        }
        
        final List<State> toReturn = findAllStatesInCountry(country.getCode());
        return Collections.unmodifiableList(toReturn);
    }

    @Override
    public StateQueryResults findStates(QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException {
        incomingParamCheck(queryByCriteria, "queryByCriteria");

        GenericQueryResults<StateBo> results = criteriaLookupService.lookup(StateBo.class, queryByCriteria);

        StateQueryResults.Builder builder = StateQueryResults.Builder.create();
        builder.setMoreResultsAvailable(results.isMoreResultsAvailable());
        builder.setTotalRowCount(results.getTotalRowCount());

        final List<State.Builder> ims = new ArrayList<State.Builder>();
        for (StateBo bo : results.getResults()) {
            ims.add(State.Builder.create(bo));
        }

        builder.setResults(ims);
        return builder.build();
    }

    public CountryService getCountryService() {
        if (countryService == null) {
            countryService = LocationApiServiceLocator.getCountryService();
        }
        return countryService;
    }   

    public void setCountryService(CountryService countryService) {
        this.countryService = countryService;
    }
    
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
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
