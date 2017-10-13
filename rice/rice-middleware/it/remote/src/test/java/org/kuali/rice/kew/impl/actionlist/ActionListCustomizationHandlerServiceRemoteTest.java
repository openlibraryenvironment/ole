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
package org.kuali.rice.kew.impl.actionlist;

import org.junit.After;
import org.junit.Before;
import org.kuali.rice.kew.framework.actionlist.ActionListCustomizationHandlerService;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.test.remote.RemoteTestHarness;

/**
 * Tests ActionListCustomizationHandlerService remoting
 */
public class ActionListCustomizationHandlerServiceRemoteTest extends ActionListCustomizationHandlerServiceImplTest {
    RemoteTestHarness harness = new RemoteTestHarness();

    @Override
    public void setupServiceUnderTest() {
        super.setupServiceUnderTest();
        ActionListCustomizationHandlerService remoteProxy =
                harness.publishEndpointAndReturnProxy(ActionListCustomizationHandlerService.class, this.getHandlerServiceImpl());
        super.setHandlerService(remoteProxy);
    }

    @After
    public void unPublishEndpoint() {
        harness.stopEndpoint();
    }
}
