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
package org.kuali.rice.location.impl.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.krad.bo.ExternalizableBusinessObject;
import org.kuali.rice.krad.service.impl.RemoteModuleServiceBase;
import org.kuali.rice.location.api.LocationConstants;
import org.kuali.rice.location.api.campus.Campus;
import org.kuali.rice.location.api.campus.CampusContract;
import org.kuali.rice.location.api.campus.CampusService;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.country.CountryContract;
import org.kuali.rice.location.api.country.CountryService;
import org.kuali.rice.location.api.county.County;
import org.kuali.rice.location.api.county.CountyContract;
import org.kuali.rice.location.api.county.CountyService;
import org.kuali.rice.location.api.postalcode.PostalCode;
import org.kuali.rice.location.api.postalcode.PostalCodeContract;
import org.kuali.rice.location.api.postalcode.PostalCodeService;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;
import org.kuali.rice.location.api.state.State;
import org.kuali.rice.location.api.state.StateContract;
import org.kuali.rice.location.api.state.StateService;
import org.kuali.rice.location.framework.campus.CampusEbo;
import org.kuali.rice.location.framework.country.CountryEbo;
import org.kuali.rice.location.framework.county.CountyEbo;
import org.kuali.rice.location.framework.postalcode.PostalCodeEbo;
import org.kuali.rice.location.framework.state.StateEbo;
import org.kuali.rice.location.impl.campus.CampusBo;
import org.kuali.rice.location.impl.country.CountryBo;
import org.kuali.rice.location.impl.county.CountyBo;
import org.kuali.rice.location.impl.postalcode.PostalCodeBo;
import org.kuali.rice.location.impl.state.StateBo;

public class LocationRemoteModuleService extends RemoteModuleServiceBase {

    private CampusService campusService;
    private StateService stateService;
    private CountryService countryService;
    private CountyService countyService;
    private PostalCodeService postalCodeService;


    public <T extends ExternalizableBusinessObject> T getExternalizableBusinessObject(Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        if(CampusContract.class.isAssignableFrom(businessObjectClass)){
            if(isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.CODE)){
                Campus campus = getCampusService().getCampus((String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.CODE));
                return (T) CampusBo.from(campus);
            }
        } else if(StateContract.class.isAssignableFrom(businessObjectClass)){
            if(isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.COUNTRY_CODE)
                    && isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.CODE)) {
                State state = getStateService().getState((String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.COUNTRY_CODE), (String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.CODE));
                return (T) StateBo.from(state);
            }
        } else if(CountryContract.class.isAssignableFrom(businessObjectClass)){
            if(isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.CODE)) {
                Country country = getCountryService().getCountry((String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.CODE));
                return (T) CountryBo.from(country);
            }
        } else if (CountyContract.class.isAssignableFrom(businessObjectClass)) {
            if (isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.CODE)
                    && isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.COUNTRY_CODE)
                    && isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.STATE_CODE)) {
                County county = getCountyService().getCounty((String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.COUNTRY_CODE), (String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.STATE_CODE), (String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.CODE));
                return (T)CountyBo.from(county);
            }
        } else if (PostalCodeContract.class.isAssignableFrom(businessObjectClass)) {
            if (isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.CODE)
                    && isNonBlankValueForKey(fieldValues, LocationConstants.PrimaryKeyConstants.COUNTRY_CODE)) {
                PostalCode postalCode = getPostalCodeService().getPostalCode((String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.COUNTRY_CODE), (String) fieldValues.get(
                        LocationConstants.PrimaryKeyConstants.CODE));
                return (T)PostalCodeBo.from(postalCode);
            }
        }
        return null;
    }

    @Override
    public <T extends ExternalizableBusinessObject> List<T> getExternalizableBusinessObjectsList(
            Class<T> businessObjectClass, Map<String, Object> fieldValues) {
        //convert fieldValues to Query
        QueryByCriteria.Builder queryBuilder = QueryByCriteria.Builder.create();
        Predicate predicate = PredicateUtils.convertObjectMapToPredicate(fieldValues);
        queryBuilder.setPredicates(predicate);

        return this.queryForEbos(businessObjectClass, queryBuilder.build());
    }

    @Override
    public boolean isExternalizable(Class boClass) {
        if(CampusContract.class.isAssignableFrom(boClass)){
            return true;
        } else if(StateContract.class.isAssignableFrom(boClass)){
            return true;
        } else if(CountryContract.class.isAssignableFrom(boClass)){
            return true;
        } else if (CountyContract.class.isAssignableFrom(boClass)) {
            return true;
        } else if (PostalCodeContract.class.isAssignableFrom(boClass)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExternalizableBusinessObjectLookupable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public boolean isExternalizableBusinessObjectInquirable(Class boClass) {
        return isExternalizable(boClass);
    }

    @Override
    public List<String> listPrimaryKeyFieldNames(Class boClass) {

        //TODO:  I strongly dislike hard-coding these values, but have this here because the OJB stuff
        //TODO: isn't available when loaded in REMOTE mode...  Need to find a better way....
        List<String> primaryKeys = new ArrayList<String>();
        primaryKeys.add("code");
        if(StateContract.class.isAssignableFrom(boClass)
                || PostalCodeContract.class.isAssignableFrom(boClass)){
            primaryKeys.add("countryCode");
        }
        return primaryKeys;
    }

    public <T extends ExternalizableBusinessObject> List<T> queryForEbos(
            Class<T> businessObjectClass, QueryByCriteria query) {

        if ( StateContract.class.isAssignableFrom( businessObjectClass ) ) {
            Collection<State> states = getStateService().findStates(query).getResults();
            List<StateEbo> stateEbos = new ArrayList<StateEbo>(states.size());
            for (State state : states) {
                stateEbos.add(StateBo.from(state));
            }
            return (List<T>)stateEbos;
        } else if ( CampusContract.class.isAssignableFrom( businessObjectClass ) ) {
            Collection<Campus> campuses = getCampusService().findCampuses(query).getResults();
            List<CampusEbo> campusEbos = new ArrayList<CampusEbo>(campuses.size());
            for (Campus campus : campuses) {
                campusEbos.add(CampusBo.from(campus));
            }
            return (List<T>)campusEbos;
        } else if ( CountryContract.class.isAssignableFrom( businessObjectClass ) ) {
            Collection<Country> countries = getCountryService().findCountries(query).getResults();
            List<CountryEbo> countryEbos = new ArrayList<CountryEbo>(countries.size());
            for (Country country : countries) {
                countryEbos.add(CountryBo.from(country));
            }
            return (List<T>)countryEbos;
        } else if ( CountyContract.class.isAssignableFrom( businessObjectClass ) ) {
            Collection<County> counties = getCountyService().findCounties(query).getResults();
            List<CountyEbo> countyEbos = new ArrayList<CountyEbo>(counties.size());
            for (County county : counties) {
                countyEbos.add(CountyBo.from(county));
            }
            return (List<T>)countyEbos;
        } else if ( PostalCodeContract.class.isAssignableFrom( businessObjectClass ) ) {
            Collection<PostalCode> postalCodes = getPostalCodeService().findPostalCodes(query).getResults();
            List<PostalCodeEbo> postalCodeEbos = new ArrayList<PostalCodeEbo>(postalCodes.size());
            for (PostalCode postalCode : postalCodes) {
                postalCodeEbos.add(PostalCodeBo.from(postalCode));
            }
            return (List<T>)postalCodeEbos;
        }
        // Not responsible for other types -- so return empty list?
        return Collections.emptyList();

    }
    protected CampusService getCampusService() {
        if (campusService == null) {
            campusService = LocationApiServiceLocator.getCampusService();
        }
        return campusService;
    }

    protected StateService getStateService() {
        if (stateService == null) {
            stateService = LocationApiServiceLocator.getStateService();
        }
        return stateService;
    }

    protected CountryService getCountryService() {
        if (countryService == null) {
            countryService = LocationApiServiceLocator.getCountryService();
        }
        return countryService;
    }

    protected CountyService getCountyService() {
        if (countyService == null) {
            countyService = LocationApiServiceLocator.getCountyService();
        }
        return countyService;
    }

    protected PostalCodeService getPostalCodeService() {
        if (postalCodeService == null) {
            postalCodeService = LocationApiServiceLocator.getPostalCodeService();
        }
        return postalCodeService;
    }
}
