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
package org.kuali.rice.location.api;

import org.kuali.rice.core.api.CoreConstants;

/**
 * <p>LocationConstants class.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class LocationConstants {

    public final static class PrimaryKeyConstants {
		public static final String CODE = "code";
        public static final String COUNTRY_CODE = "countryCode";
		public static final String STATE_CODE = "stateCode";

        private PrimaryKeyConstants() {
			throw new UnsupportedOperationException("do not call");
		}
    }

    public final static String LOCATION_DISTRIBUTED_CACHE = "locationDistributedCacheManager";
    public final static String SERVICE_PATH_SOAP = "soap/" + Namespaces.MODULE_NAME + "/" + CoreConstants.Versions.VERSION_2_0;

    public static final class Namespaces {
    	public static final String MODULE_NAME = "location";
        public static final String LOCATION_NAMESPACE_PREFIX = CoreConstants.Namespaces.ROOT_NAMESPACE_PREFIX + "/" + MODULE_NAME;
    	/**
    	 * Namespace for the core module which is compatible with Kuali Rice 2.0.x.
    	 */
    	public static final String LOCATION_NAMESPACE_2_0 = LOCATION_NAMESPACE_PREFIX + "/" + CoreConstants.Versions.VERSION_2_0;

        private Namespaces() {
		    throw new UnsupportedOperationException("do not call");
	    }
    }
}
