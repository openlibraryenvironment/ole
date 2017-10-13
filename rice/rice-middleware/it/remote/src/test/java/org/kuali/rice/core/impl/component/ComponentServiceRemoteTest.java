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
package org.kuali.rice.core.impl.component;

import org.junit.After;
import org.junit.Before;
import org.kuali.rice.coreservice.api.component.ComponentService;
import org.kuali.rice.coreservice.impl.component.ComponentServiceImplTest;
import org.kuali.rice.test.remote.RemoteTestHarness;

public class ComponentServiceRemoteTest extends ComponentServiceImplTest {
    RemoteTestHarness harness = new RemoteTestHarness();

    @Before
    @Override
    public void setupServiceUnderTest() {
        super.setupServiceUnderTest();
        ComponentService remoteProxy = harness.publishEndpointAndReturnProxy(
                ComponentService.class, this.getServiceImpl());
        super.setService(remoteProxy);
    }

    @After
    public void unPublishEndpoint() {
        harness.stopEndpoint();
    }
}
