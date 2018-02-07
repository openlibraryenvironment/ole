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
package org.kuali.rice.location.framework.state;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.location.api.country.Country;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;
import org.kuali.rice.location.api.state.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StateValuesFinder extends KeyValuesBase {

    private String countryCode = "";

    @Override
	public List<KeyValue> getKeyValues() {
    	List<KeyValue> labels = new ArrayList<KeyValue>();
        if (StringUtils.isEmpty(this.countryCode)) {
            Country defaultCountry = LocationApiServiceLocator.getCountryService().getDefaultCountry();
            if (defaultCountry != null) {
                countryCode = defaultCountry.getCode();
            }
        }
        List<State> baseCodes = LocationApiServiceLocator.getStateService().findAllStatesInCountry(countryCode);
        List<State> codes = new ArrayList<State>( baseCodes );
        Collections.sort(codes, new Comparator<State> () {
            @Override
            public int compare(State o1, State o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        List<KeyValue> newLabels = new ArrayList<KeyValue>();
        newLabels.add(new ConcreteKeyValue("", ""));
        for (State state : codes) {
            if(state.isActive()) {
                newLabels.add(new ConcreteKeyValue(state.getCode(), state.getName()));
            }
        }
        labels = newLabels;

        return labels;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    @Override
    public void clearInternalCache() {
    }
}
