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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An abstract KeyValuesBase for defining a values finder which produces a list of Countries.  Sub-classes should
 * extend this class and override {@link #retrieveCountriesForValuesFinder()} in order to produce a list of
 * countries to include.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AbstractCountryValuesFinderBase extends KeyValuesBase {

    @Override
    public List<KeyValue> getKeyValues() {
        Country defaultCountry = getDefaultCountry();
        List<Country> countries = new ArrayList<Country>(retrieveCountriesForValuesFinder());

        List<KeyValue> values = new ArrayList<KeyValue>(countries.size() + 1);
        values.add(new ConcreteKeyValue("", ""));
        if (defaultCountry != null) {
            values.add(new ConcreteKeyValue(defaultCountry.getCode(), defaultCountry.getName()));
        }

        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                // some institutions may prefix the country name with an asterisk if the country no longer exists
                // the country names will be compared without the asterisk
                String sortValue1 = StringUtils.trim(StringUtils.removeStart(country1.getName(), "*"));
                String sortValue2 = StringUtils.trim(StringUtils.removeStart(country2.getName(), "*"));
                return sortValue1.compareToIgnoreCase(sortValue2);
            }

        });

        // the default country may show up twice, but that's fine
        for (Country country : countries) {
            if (country.isActive()) {
                values.add(new ConcreteKeyValue(country.getCode(), country.getName()));
            }
        }
        return values;
    }

    /**
     * Returns a list of countries that will be added to the result of {@link #getKeyValues()}.  Note that the result
     * may be filtered by active status
     *
     * @return a List of countries to include in the values returned by this finder
     */
    protected abstract List<Country> retrieveCountriesForValuesFinder();

    /**
     * Returns the default country to use for this values finder.  If no default country is returned, none will be
     * used.  The default implementation of this method will defer to {@link org.kuali.rice.location.api.country.CountryService#getDefaultCountry()}.
     *
     * @return the default country to use for this values finder, or null if no default country should be used
     */
    protected Country getDefaultCountry() {
        return LocationApiServiceLocator.getCountryService().getDefaultCountry();
    }

}
