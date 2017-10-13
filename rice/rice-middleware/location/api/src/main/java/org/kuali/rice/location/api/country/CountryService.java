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
package org.kuali.rice.location.api.country;

import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.location.api.LocationConstants;
import org.springframework.cache.annotation.Cacheable;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;


/**
 * <p>CountryService interface.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "CountryService", targetNamespace = LocationConstants.Namespaces.LOCATION_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface CountryService {

    /**
     * Lookup a country object based on the given country code.
     *
     * @param code the given country code
     * @return a country object with the given country code.  A null reference is returned if an invalid or
     *         non-existant code is supplied.
     * @throws RiceIllegalArgumentException if the code is blank or null
     */
    @WebMethod(operationName = "getCountry")
    @WebResult(name = "country")
    @Cacheable(value=Country.Cache.NAME, key="'code=' + #p0")
    Country getCountry(@WebParam(name = "code") String code) throws RiceIllegalArgumentException;

    /**
     * Get a country object based on an alternate country code
     *
     * @param alternateCode the given alternate country code
     * @return A country object with the given alternate country code if a country with that alternate country code
     *         exists.  Otherwise, null is returned.
     * @throws RiceIllegalStateException if multiple Countries exist with the same passed in alternateCode
     * @throws RiceIllegalArgumentException if alternateCode is null or is a whitespace only string.
     */
    @WebMethod(operationName = "getCountryByAlternateCode")
    @WebResult(name = "country")
    @Cacheable(value=Country.Cache.NAME, key="'alternateCode=' + #p0")
    Country getCountryByAlternateCode(@WebParam(name = "alternateCode") String alternateCode)
            throws RiceIllegalStateException, RiceIllegalArgumentException;

    /**
     * Returns all Countries that are not restricted.
     *
     * @return all countries that are not restricted
     */
    @WebMethod(operationName = "findAllCountriesNotRestricted")
    @XmlElementWrapper(name = "countriesNotRestricted", required = false)
    @XmlElement(name = "countryNotRestricted", required = false)
    @WebResult(name = "countriesNotRestricted")
    @Cacheable(value=Country.Cache.NAME, key="'allRestricted'")
    List<Country> findAllCountriesNotRestricted();

    /**
     * Returns all Countries
     *
     * @return all countries
     */
    @WebMethod(operationName = "findAllCountries")
    @XmlElementWrapper(name = "countries", required = false)
    @XmlElement(name = "country", required = false)
    @WebResult(name = "countries")
    @Cacheable(value=Country.Cache.NAME, key="'all'")
    List<Country> findAllCountries();

    /**
     * Returns the system default country.  This is simply meant to be informational for applications which need the
     * ability to utilize a default country (such as for defaulting of certain fields during data entry).  This method
     * may return null in situations where no default country is configured.
     *
     * @return the default country, or null if no default country is defined
     */
    @WebMethod(operationName = "getDefaultCountry")
    @WebResult(name = "country")
    @Cacheable(value = Country.Cache.NAME,  key="'default'")
    Country getDefaultCountry();

    /**
     * This method find Countries based on a query criteria.  The criteria cannot be null.
     *
     * @since 2.0.1
     * @param queryByCriteria the criteria.  Cannot be null.
     * @return query results.  will never return null.
     * @throws IllegalArgumentException if the queryByCriteria is null
     */
    @WebMethod(operationName = "findCountries")
    @WebResult(name = "results")
    CountryQueryResults findCountries(@WebParam(name = "query") QueryByCriteria queryByCriteria) throws RiceIllegalArgumentException;

}
