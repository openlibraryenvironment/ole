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
package org.kuali.rice.core.util;

import org.apache.log4j.Logger;
import org.kuali.rice.core.framework.util.ApplicationThreadLocal;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

/**
 * Bean which registers a listener that removes {@link ApplicationThreadLocal}s on context destruction.
 */
public class ApplicationThreadLocalCleaner implements ApplicationContextAware {
    private static final Logger LOG = Logger.getLogger(ApplicationThreadLocalCleaner.class);
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // register a context close handler
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
            context.addApplicationListener(new ApplicationListener<ContextClosedEvent>() {
                @Override
                public void onApplicationEvent(ContextClosedEvent e) {
                    LOG.info("Context '" + e.getApplicationContext().getDisplayName() + "' closed, removing registered ApplicationThreadLocals");
                    if (!ApplicationThreadLocal.clear()) {
                        LOG.error("Error(s) occurred removing registered ApplicationThreadLocals");
                    }
                }
            });
        }
    }
}
