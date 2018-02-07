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
package org.kuali.rice.location.framework.country;

import java.util.List;

import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.country.CountryService;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;

/**
 * This class returns list of active and non-restricted country value pairs.
 */
@SuppressWarnings("unused")
public class CountryNotRestrictedValuesFinder extends AbstractCountryValuesFinderBase {
	/**
	 * Returns all non-restricted countries, regardless of active status
	 */
	@Override
	protected List<Country> retrieveCountriesForValuesFinder() {
        CountryService countryService = LocationApiServiceLocator.getCountryService();
		return countryService.findAllCountriesNotRestricted();
	}
}
