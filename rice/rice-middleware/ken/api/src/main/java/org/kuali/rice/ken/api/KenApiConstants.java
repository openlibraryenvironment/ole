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
package org.kuali.rice.ken.api;

import org.kuali.rice.core.api.CoreConstants;

/**
 * Names of services published on the bus 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class KenApiConstants {
    public static final String KENAPI_SERVICE = "KEN-KENAPIService";

    public final static String SERVICE_PATH_SOAP = "soap/" + Namespaces.MODULE_NAME + "/" + CoreConstants.Versions.VERSION_2_0;


    public static final class Namespaces {
        public static final String MODULE_NAME = "ken";

        public static final String KEN_NAMESPACE_PREFIX = CoreConstants.Namespaces.ROOT_NAMESPACE_PREFIX + "/ken";

        /**
         * Namespace for the core module which is compatible with Kuali Rice 2.0.x.
         */
        public static final String KEN_NAMESPACE_2_0 = KEN_NAMESPACE_PREFIX + "/" + CoreConstants.Versions.VERSION_2_0;

        private Namespaces() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    public static final class ServiceNames {
        public static final String SEND_NOTIFICATION_SERVICE = "sendNotificationService";
    }

    public static final class NotificationConstants {

    }

    public static final class RESPONSE_STATUSES {
        public static final String SUCCESS = "Success";
        public static final String FAILURE = "Failure";

        private RESPONSE_STATUSES() {
            throw new UnsupportedOperationException("do not call");
        }
    }

    private KenApiConstants() {
        throw new UnsupportedOperationException("do not call");
    }
}
