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
package org.kuali.rice.kcb.service;

import java.lang.reflect.Proxy;

import org.apache.log4j.Logger;
import org.kuali.rice.kcb.util.BeanFactoryInvocationHandler;
import org.springframework.beans.factory.BeanFactory;

/**
 * Class that holds a singleton reference to KCBServiceLocator 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class GlobalKCBServiceLocator {
    private static final Logger LOG = Logger.getLogger(GlobalKCBServiceLocator.class);

    /**
     * The KCBServiceLocator singleton
     */
    private static KCBServiceLocator locator;

    /**
     * The global initializer that constructs the KCBServiceLocator singleton
     * @param beanFactory the beanFactory from which to construct the KCBServiceLocator
     */
    public static synchronized void init(BeanFactory beanFactory) {
//        LOG.error("INITIALIZING", new Exception());
        if (locator != null) {
            throw new IllegalStateException("GlobalKCBServiceLocator already initialized");
        }

        locator = (KCBServiceLocator) Proxy.newProxyInstance(GlobalKCBServiceLocator.class.getClassLoader(),
                                                             new Class[] { KCBServiceLocator.class },
                                                             new BeanFactoryInvocationHandler(beanFactory));
    }

    /**
     * Returns whether the GlobalKCBServiceLocator has already been initialized (classloader scoped)
     * @return whether the GlobalKCBServiceLocator has already been initialized (classloader scoped)
     */
    public static synchronized boolean isInitialized() {
        return locator != null;
    }

    /**
     * Un-sets the KCBServiceLocator singleton, in order to fulfill a "lifecycle"
     * contract (whereby init may be called again in the same class loader), specifically for
     * unit tests.
     */
    public static synchronized void destroy() {
        //LOG.debug("DESTROYING", new Exception());
        locator = null;
    }

    /**
     * Returns the KCBServiceLocator singleton
     * @return the KCBServiceLocator singleton
     */
    public static synchronized KCBServiceLocator getInstance() {
        return locator;
    }
}
