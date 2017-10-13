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
package org.kuali.rice.kim.impl.role;

import org.junit.After;
import org.junit.Before;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.type.KimTypeInfoService;
import org.kuali.rice.kim.impl.group.GroupServiceImplTest;
import org.kuali.rice.test.remote.RemoteTestHarness;

/**
 * Test for RoleService which invokes RoleService remotely over the bus
 */
public class RoleServiceRemoteTest extends RoleServiceImplTest {
    RemoteTestHarness harness = new RemoteTestHarness();

    @Before
    @Override
    public void setupServiceUnderTest() {
        super.setupServiceUnderTest();
        RoleService remoteProxy = harness.publishEndpointAndReturnProxy(RoleService.class, this.getRoleServiceImpl());
        super.setRoleService(remoteProxy);
    }

    @After
    public void unPublishEndpoint() {
        harness.stopEndpoint();
    }
}
