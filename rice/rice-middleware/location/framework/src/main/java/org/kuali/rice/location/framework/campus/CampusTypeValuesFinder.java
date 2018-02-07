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
package org.kuali.rice.location.framework.campus;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;
import org.kuali.rice.location.api.campus.CampusService;
import org.kuali.rice.location.api.campus.CampusType;
import org.kuali.rice.location.api.services.LocationApiServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CampusTypeValuesFinder extends KeyValuesBase {

    @Override
	public List<KeyValue> getKeyValues() {

        // get a list of all CampusTypes
    	CampusService campusService = LocationApiServiceLocator.getCampusService();
    	List<CampusType> campusTypes = campusService.findAllCampusTypes();
       
    	//copy list for sorting (list from service is unmodifiable
    	List<CampusType> campusTypeSort = new ArrayList<CampusType>(campusTypes);
        // sort using comparator.
        Collections.sort(campusTypeSort, CampusTypeComparator.INSTANCE);

        // create a new list (code, descriptive-name)
        List<KeyValue> labels = new ArrayList<KeyValue>();

        for (CampusType campusType : campusTypeSort) {
            labels.add(new ConcreteKeyValue(campusType.getCode(), campusType.getCode() + " - " + campusType.getName()));
        }

        return labels;
    }
}
