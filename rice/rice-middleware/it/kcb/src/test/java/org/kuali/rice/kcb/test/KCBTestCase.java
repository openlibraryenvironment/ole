/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kcb.test;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.kcb.service.GlobalKCBServiceLocator;
import org.kuali.rice.kcb.service.KCBServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

/**
 * Base KCBTestCase 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class KCBTestCase extends BaselineTestCase {
    protected KCBServiceLocator services;

    public KCBTestCase() {
        super("kcb");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        services = GlobalKCBServiceLocator.getInstance();
    }
    
    @Override
	protected Lifecycle getLoadApplicationLifecycle() {
    	SpringResourceLoader springResourceLoader = new SpringResourceLoader(new QName("KCBTestHarnessApplicationResourceLoader"), "classpath:KCBTestHarnessSpringBeans.xml", null);
    	springResourceLoader.setParentSpringResourceLoader(getTestHarnessSpringResourceLoader());
    	return springResourceLoader;
	}

}
