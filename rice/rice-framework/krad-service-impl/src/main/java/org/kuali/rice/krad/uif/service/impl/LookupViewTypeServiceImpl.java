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
package org.kuali.rice.krad.uif.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.service.ViewTypeService;
import org.springframework.beans.PropertyValues;

/**
 * Type service implementation for Lookup views
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class LookupViewTypeServiceImpl implements ViewTypeService {

	/**
	 * @see org.kuali.rice.krad.uif.service.ViewTypeService#getViewTypeName()
	 */
	public ViewType getViewTypeName() {
		return ViewType.LOOKUP;
	}
	
    /**
     * @see org.kuali.rice.krad.uif.service.ViewTypeService#getParametersFromViewConfiguration(org.springframework.beans.PropertyValues)
     */
    public Map<String, String> getParametersFromViewConfiguration(PropertyValues propertyValues) {
        Map<String, String> parameters = new HashMap<String, String>();

        String viewName = ViewModelUtils.getStringValFromPVs(propertyValues, UifParameters.VIEW_NAME);
        String dataObjectClassName = ViewModelUtils.getStringValFromPVs(propertyValues,
                UifParameters.DATA_OBJECT_CLASS_NAME);

        parameters.put(UifParameters.VIEW_NAME, viewName);
        parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClassName);

        return parameters;
    }

	/**
	 * @see org.kuali.rice.krad.uif.service.ViewTypeService#getParametersFromRequest(java.util.Map)
	 */
	public Map<String, String> getParametersFromRequest(Map<String, String> requestParameters) {
		Map<String, String> parameters = new HashMap<String, String>();

		if (requestParameters.containsKey(UifParameters.VIEW_NAME)) {
			parameters.put(UifParameters.VIEW_NAME, requestParameters.get(UifParameters.VIEW_NAME));
		}
		else {
			parameters.put(UifParameters.VIEW_NAME, UifConstants.DEFAULT_VIEW_NAME);
		}

		if (requestParameters.containsKey(UifParameters.DATA_OBJECT_CLASS_NAME)) {
			parameters.put(UifParameters.DATA_OBJECT_CLASS_NAME,
					requestParameters.get(UifParameters.DATA_OBJECT_CLASS_NAME));
		}

		return parameters;
	}

}
