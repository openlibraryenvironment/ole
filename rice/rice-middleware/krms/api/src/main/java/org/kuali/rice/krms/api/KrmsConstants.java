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
package org.kuali.rice.krms.api;

import org.kuali.rice.core.api.CoreConstants;

/**
 * KRMS Constants
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KrmsConstants {

    public static final String KRMS_NAMESPACE = "KR-RULE";

    /**
     * MAINTAIN_KRMS_AGENDA
     */
    public static final String MAINTAIN_KRMS_AGENDA = "Maintain KRMS Agenda";

    /**
     * SERVICE_PATH_SOAP
     */
    public final static String SERVICE_PATH_SOAP = "soap/" + Namespaces.MODULE_NAME + "/" + CoreConstants.Versions.VERSION_2_0;

    /**
     * KRMS distributed cache bean id
     */
    public static final String KRMS_DISTRIBUTED_CACHE = "krmsDistributedCacheManager";

    /**
     * Namespaces
     */
	public final static class Namespaces {
        /**
         * Namespaces MODULE_NAME
         */
		public static final String MODULE_NAME = "krms";

        /**
         * Namespaces KRMS_NAMESPACE_PREFIX
         */
        public static final String KRMS_NAMESPACE_PREFIX = CoreConstants.Namespaces.ROOT_NAMESPACE_PREFIX + "/" + MODULE_NAME;

        /**
         * Namespace for the kew module which is compatible with Kuali Rice 2.0.x.
         */
        public static final String KRMS_NAMESPACE_2_0 = KRMS_NAMESPACE_PREFIX + "/" + CoreConstants.Versions.VERSION_2_0;
	}

}

