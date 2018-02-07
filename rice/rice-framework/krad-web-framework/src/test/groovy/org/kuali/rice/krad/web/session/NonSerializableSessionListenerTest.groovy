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
package org.kuali.rice.krad.web.session

import org.kuali.rice.krad.web.listener.NonSerializableSessionListener

import javax.servlet.http.HttpSessionBindingEvent
import org.junit.Test
import org.kuali.rice.core.api.config.property.Config
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.framework.config.property.SimpleConfig
import org.springframework.mock.web.MockHttpSession
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

/**
 * Tests that NonSerializableSessionListener serialization checks are disabled in production
 */
class NonSerializableSessionListenerTest {
    static class TestNonSerializableSessionListener extends NonSerializableSessionListener {
        private serializationChecked = false
        protected void checkSerialization(final HttpSessionBindingEvent se, String action) {
            serializationChecked = true
            super.checkSerialization(se, action)
        }
    }

    @Test
    void listenerIsExecutedInNonProductionEnvironment() {
        def config = new SimpleConfig()
        config.putProperty(Config.ENVIRONMENT, "dev")
        config.putProperty("enableSerializationCheck", "true");
        ConfigContext.init(config)

        def listener = new TestNonSerializableSessionListener()
        listener.attributeAdded(new HttpSessionBindingEvent(new MockHttpSession(), "attrib", "value"))
        assertTrue(listener.serializationChecked)
    }

    @Test
    void listenerIsNotExecutedInProductionEnvironment() {
        def config = new SimpleConfig()
        config.putProperty(Config.PROD_ENVIRONMENT_CODE, "prod")
        config.putProperty(Config.ENVIRONMENT, "prod")
        config.putProperty("enableSerializationCheck", "true");
        ConfigContext.init(config)

        def listener = new TestNonSerializableSessionListener()
        listener.attributeAdded(new HttpSessionBindingEvent(new MockHttpSession(), "attrib", "value"))
        assertFalse(listener.serializationChecked)
    }
}