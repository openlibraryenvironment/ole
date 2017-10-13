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
package org.kuali.rice.core.impl.util.spring;

import org.objectweb.jotm.TimerManager;
import org.springframework.transaction.jta.JotmFactoryBean;

import javax.naming.NamingException;

/**
 * Specialization of the old Spring JotmFactoryBean which calls TimerManager.stop()
 * to stop Jotm threads on destruction.
 * (Spring AOP apparently cannot hook bean destruction, so there is not a cleaner AOP way to do this)
 */
public class RiceJotmFactoryBean extends JotmFactoryBean {
    public RiceJotmFactoryBean() throws NamingException { }

    @Override
    public void destroy() {
        super.destroy();
        TimerManager.stop();
    }
}