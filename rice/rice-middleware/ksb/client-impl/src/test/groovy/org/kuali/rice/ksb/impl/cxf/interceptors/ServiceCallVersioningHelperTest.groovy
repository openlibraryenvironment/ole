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
package org.kuali.rice.ksb.impl.cxf.interceptors

import org.junit.Test
import org.kuali.rice.core.api.config.property.Config
import org.junit.Before
import org.kuali.rice.core.api.config.property.ConfigContext
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import org.kuali.rice.core.framework.config.property.SimpleConfig

/**
 * Ensures ServiceCallVersioningHelper is populating headers correctly
 */
class ServiceCallVersioningHelperTest {
    static final String FAKE_RICE_VERSION = "11.11-SNAPSHOT"
    static final String FAKE_ENV = "fake-env"
    static final String FAKE_APP_NAME = "fake-app"
    static final String FAKE_APP_VERSION = "99.99-SNAPSHOT"

    def config

    @Before
    void initConfig() {
        config = new SimpleConfig()
        config.putProperty(Config.RICE_VERSION, FAKE_RICE_VERSION)
        config.putProperty(Config.ENVIRONMENT, FAKE_ENV)
        ConfigContext.init(config)
    }

    @Test void testIAmSettingUpTheConfigCorrectly() {
        Config cfg = ConfigContext.getCurrentContextConfig()
        assertNotNull(cfg)
        assertEquals(FAKE_ENV, cfg.getEnvironment())
    }

    @Test void testOmitApplicationInfo() {
        HashMap<String, List<String>> headers = new HashMap<String, List<String>>()
        org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.populateVersionHeaders(headers)

        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_VERSION_HEADER).contains(FAKE_RICE_VERSION))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_ENVIRONMENT_HEADER).contains(FAKE_ENV))
        assertEquals(null, headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_NAME_HEADER))
        assertEquals(null, headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_VERSION_HEADER))
    }

    @Test void testFallbackParams() {
        config.putProperty(Config.MODULE_NAME, FAKE_APP_NAME)
        config.putProperty(Config.VERSION, FAKE_APP_VERSION)

        HashMap<String, List<String>> headers = new HashMap<String, List<String>>()
        org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.populateVersionHeaders(headers)

        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_VERSION_HEADER).contains(FAKE_RICE_VERSION))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_ENVIRONMENT_HEADER).contains(FAKE_ENV))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_NAME_HEADER).contains(FAKE_APP_NAME))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_VERSION_HEADER).contains(FAKE_APP_VERSION))
    }

    /**
     * Tests that the new application.name and application.version parameters take priority
     * I suppose this is really just a test of the Config object
     */
    @Test void testPreferredParams() {
        config.putProperty(Config.MODULE_NAME, "bogus value")
        config.putProperty(Config.VERSION, "bogus value")
        config.putProperty(Config.APPLICATION_NAME, FAKE_APP_NAME)
        config.putProperty(Config.APPLICATION_VERSION, FAKE_APP_VERSION)

        HashMap<String, List<String>> headers = new HashMap<String, List<String>>()
        org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.populateVersionHeaders(headers)

        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_VERSION_HEADER).contains(FAKE_RICE_VERSION))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_RICE_ENVIRONMENT_HEADER).contains(FAKE_ENV))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_NAME_HEADER).contains(FAKE_APP_NAME))
        assertTrue(headers.get(org.kuali.rice.ksb.impl.cxf.interceptors.ServiceCallVersioningHelper.KUALI_APP_VERSION_HEADER).contains(FAKE_APP_VERSION))
    }
}
