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
package org.kuali.rice.ksb.test;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.resourceloader.ResourceLoader;
import org.kuali.rice.core.framework.persistence.ojb.BaseOjbConfigurer;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.ksb.api.KsbApiServiceLocator;
import org.kuali.rice.ksb.server.TestClient1;
import org.kuali.rice.ksb.server.TestClient2;
import org.kuali.rice.test.BaselineTestCase;

import javax.xml.namespace.QName;
import java.util.List;

@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.ROLLBACK)
public abstract class KSBTestCase extends BaselineTestCase {

    private static final String KSB_MODULE_NAME = "ksb";

    private TestClient1 testClient1;
    private TestClient2 testClient2;
    private ResourceLoader springContextResourceLoader;

    public KSBTestCase() {
		super(KSB_MODULE_NAME);
	}

    @Override
    public void setUp() throws Exception {

    	// because we're stopping and starting so many times we need to clear
        // the core before another set of RLs get put in the core. This is 
        // because we are sometimes using the GRL to fetch a specific servers
        // spring file out for testing purposes.
        ConfigContext.destroy();
        
		// Turn off http keep-alive. Repeated jetty start/stop using same port
		// results sockets held by client not to close properly, resulting in
        // cxf test failures.
		System.setProperty("http.keepAlive", "false");

        super.setUp();
        if (startClient1() || startClient2()) {
            KsbApiServiceLocator.getServiceBus().synchronize();
        }
    }
    
    @Override
    protected List<Lifecycle> getPerTestLifecycles() {
        List<Lifecycle> lifecycles = super.getSuiteLifecycles();
        if (this.disableJta()) {
            System.setProperty(BaseOjbConfigurer.OJB_PROPERTIES_PROP, "RiceNoJtaOJB.properties");
            this.springContextResourceLoader = new SpringResourceLoader(new QName("ksbtestharness"), "KSBTestHarnessNoJtaSpring.xml", null);
        } else {
            this.springContextResourceLoader = new SpringResourceLoader(new QName("ksbtestharness"), "KSBTestHarnessSpring.xml", null);
        }

        lifecycles.add(this.springContextResourceLoader);
        if (startClient1()) {
            this.testClient1 = new TestClient1();
            lifecycles.add(this.testClient1);
        }
        if (startClient2()) {
            this.testClient2 = new TestClient2();
            lifecycles.add(this.testClient2);
        }
        return lifecycles;
    }

    public boolean startClient1() {
        return false;
    }

    public boolean startClient2() {
        return false;
    }

    public TestClient1 getTestClient1() {
        return this.testClient1;
    }

    public TestClient2 getTestClient2() {
        return this.testClient2;
    }

    public ResourceLoader getSpringContextResourceLoader() {
        return this.springContextResourceLoader;
    }

    public void setSpringContextResourceLoader(ResourceLoader testHarnessResourceLoader) {
        this.springContextResourceLoader = testHarnessResourceLoader;
    }

    protected boolean disableJta() {
        return false;
    }
}
