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
package org.kuali.rice.krad.uif.service;

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.springframework.beans.PropertyValues;

import java.util.Map;

/**
 * Provides methods handing <code>View</code> instance of certain types
 *
 * <p>
 * The type service is invoked to handle type parameters that can be used for
 * additional indexing of views and retrieval.
 * </p>
 *
 * <p>
 * As the view dictionary entries are indexed the associated view type will be
 * retrieved and if there is an associated <code>ViewTypeService</code> it will
 * be invoked to provide parameter information for further indexing. This is
 * useful to index a view based on other properties, like a class name.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ViewTypeService {

	/**
	 * Gives the view type name that is supported by the type service
	 *
	 * <p>
	 * The name is used to associated a type (and thus a view type service) with
	 * a view instance through the view type name property. Thus must be unique
	 * among all view types implemented
	 * </p>
	 *
	 * @return ViewType view type name
	 */
	public ViewType getViewTypeName();

	/**
	 * Pulls values for the supported parameters from the views configured property values. These
     * name/value pairs are used to index the view for later retrieval
	 *
	 * @param propertyValues - property values configured on the view bean definition
	 * @return Map<String, String> of parameters where map key is the parameter
	 *         name, and the map value is the parameter value
	 */
	public Map<String, String> getParametersFromViewConfiguration(PropertyValues propertyValues);

	/**
	 * Pulls entries from the given map that are supported parameters for the view type. In addition,
     * defaults can be set or additional parameters set as needed. Used by the <code>ViewService</code> to retrieve a
	 * <code>View</code> instance based on the incoming request parameters
	 *
	 * @param requestParameters
	 *            - Map of request parameters to pull view type parameters from
	 * @return Map<String, String> of parameters where map key is the parameter
	 *         name, and the map value is the parameter value
	 */
	public Map<String, String> getParametersFromRequest(Map<String, String> requestParameters);

}
