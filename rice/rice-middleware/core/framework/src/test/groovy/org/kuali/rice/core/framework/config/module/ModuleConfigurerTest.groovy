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
package org.kuali.rice.core.framework.config.module

import org.junit.Test
import org.kuali.rice.core.api.CoreConstants
import org.kuali.rice.core.api.config.module.RunMode
import org.kuali.rice.core.api.config.property.ConfigContext
import org.kuali.rice.core.framework.config.property.SimpleConfig
import org.springframework.beans.factory.InitializingBean

import static org.junit.Assert.assertEquals

/**
 * Unit tests ModuleConfigurer
 */
class ModuleConfigurerTest {
    @Test
    void testGetCurrentContextConfigurers() {
        def cfg = new SimpleConfig()
        cfg.putProperty(CoreConstants.Config.APPLICATION_ID, "APPID");

        def configurers = [ "A", "B", "C" ].collect {
            def m = [ getPrimarySpringFiles: { [] } ] as ModuleConfigurer
            m.setValidRunModes([RunMode.LOCAL])
            m.setModuleName(it)
            cfg.putProperty(it.toLowerCase() + ".mode", m.getValidRunModes().first().name())
            m
        } as Collection<InitializingBean>

        ConfigContext.init(cfg)

        configurers.each {
            it.afterPropertiesSet()
        }

        assertEquals(configurers, ModuleConfigurer.getCurrentContextConfigurers())
    }
}
