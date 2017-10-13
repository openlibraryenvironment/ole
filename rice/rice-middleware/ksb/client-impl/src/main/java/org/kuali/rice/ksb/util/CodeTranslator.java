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
package org.kuali.rice.ksb.util;

import java.util.HashMap;
import java.util.Map;


/**
 * Utility class to translate the various codes into labels and vice versa.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CodeTranslator {

    public static final Map<String, String> routeQueueStatusLabels = getRouteQueueStatusLabels();

    private static Map<String, String> getRouteQueueStatusLabels() {
        Map<String, String> routeQueueStatusLabels = new HashMap<String, String>();
        routeQueueStatusLabels.put(KSBConstants.ROUTE_QUEUE_EXCEPTION, KSBConstants.ROUTE_QUEUE_EXCEPTION_LABEL);
        routeQueueStatusLabels.put(KSBConstants.ROUTE_QUEUE_QUEUED, KSBConstants.ROUTE_QUEUE_QUEUED_LABEL);
        routeQueueStatusLabels.put(KSBConstants.ROUTE_QUEUE_ROUTING, KSBConstants.ROUTE_QUEUE_ROUTING_LABEL);
        return routeQueueStatusLabels;
    }

    static public String getRouteQueueStatusLabel(String routeQueueStatusCode) {
        return routeQueueStatusLabels.get(routeQueueStatusCode);
    }

}
