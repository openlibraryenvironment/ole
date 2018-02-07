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
package org.kuali.rice.core.api.config;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;

/**
 * A utility class to help with retrieving commong configuration parameters. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class CoreConfigHelper {

    /**
	 * Returns the ID of this instance.
	 *
	 * @return the ID of this instance, should never return a null or blank value
	 *
	 * @throws IllegalStateException if the instance ID is not configured properly
	 */
	public static String getInstanceId() {
		String applicationId = ConfigContext.getCurrentContextConfig().getProperty(CoreConstants.Config.INSTANCE_ID);
		if (StringUtils.isBlank(applicationId)) {
			throw new IllegalStateException(CoreConstants.Config.INSTANCE_ID + " configuration parameter was not configured, please configure a proper non-empty value.");
		}
		return applicationId;
	}

	/**
	 * Returns the ID of this application.
	 * 
	 * @return the ID of this application, should never return a null or blank value 
	 * 
	 * @throws IllegalStateException if the application ID is not configured properly
	 */
	public static String getApplicationId() {
		String applicationId = ConfigContext.getCurrentContextConfig().getProperty(CoreConstants.Config.APPLICATION_ID);
		if (StringUtils.isBlank(applicationId)) {
			throw new IllegalStateException(CoreConstants.Config.APPLICATION_ID + " configuration parameter was not configured, please configure a proper non-empty value.");
		}
		return applicationId;
	}
	
	private CoreConfigHelper() {
		throw new UnsupportedOperationException("Should never be invoked!");
	}
	
}
