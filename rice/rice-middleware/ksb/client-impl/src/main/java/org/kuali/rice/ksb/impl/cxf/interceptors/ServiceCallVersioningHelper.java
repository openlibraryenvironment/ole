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
package org.kuali.rice.ksb.impl.cxf.interceptors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helps populate service call protocol headers with Rice version information.
 */
public class ServiceCallVersioningHelper {
    private static final Logger LOG = Logger.getLogger(ServiceCallVersioningOutInterceptor.class);

    public static final String KUALI_RICE_ENVIRONMENT_HEADER = "X-Kuali-Env";
    public static final String KUALI_RICE_VERSION_HEADER = "X-Kuali-Rice-Ver";
    public static final String KUALI_APP_NAME_HEADER = "X-Kuali-App-Name";
    public static final String KUALI_APP_VERSION_HEADER = "X-Kuali-App-Ver";

    private ServiceCallVersioningHelper() { /* static utility class */ }

    /**
     * Populates protocol headers represented by a map of list of strings with Kuali/Rice
     * versioning information, including Rice environment and version, and Rice application
     * name and version.
     * @param headers the protocol headers. let's be honest, they are just HTTP headers.
     */
    public static void populateVersionHeaders(Map<String, List<String>> headers) {
        Config config = ConfigContext.getCurrentContextConfig();
        if (config == null) {
            LOG.error("No configuration context found when handling outbound message");
            // commented for the sake of tests
            // assert config != null : "No configuration context found when handling outbound message";
            return;
        }

        String riceEnvironment = config.getEnvironment();
        assert StringUtils.isNotBlank(riceEnvironment) : "Rice environment should never be blank";
        if (StringUtils.isNotBlank(riceEnvironment)) {
            headers.put(KUALI_RICE_ENVIRONMENT_HEADER, Collections.singletonList(riceEnvironment));
        }

        String riceVersion = config.getRiceVersion();
        assert StringUtils.isNotBlank(riceVersion) : "Rice version should never be blank";
        if (StringUtils.isNotBlank(riceVersion)) {
            headers.put(KUALI_RICE_VERSION_HEADER, Collections.singletonList(riceVersion));
        }

        String appName = config.getApplicationName();
         if (StringUtils.isNotBlank(appName)) {
            headers.put(KUALI_APP_NAME_HEADER, Collections.singletonList(appName));
        }

        String appVersion = config.getApplicationVersion();
         if (StringUtils.isNotBlank(appVersion)) {
            headers.put(KUALI_APP_VERSION_HEADER, Collections.singletonList(appVersion));
        }
    }
}