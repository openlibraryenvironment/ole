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
package org.kuali.rice.core.impl.criteria.org.kuali.rice.coreservice.impl.service;

import org.junit.Before;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.coreservice.impl.namespace.NamespaceBo;
import org.kuali.rice.coreservice.impl.service.CoreServiceRemoteModuleService;
import org.kuali.rice.krad.bo.ModuleConfiguration;
import org.kuali.rice.krad.service.KualiModuleService;
import org.kuali.rice.krad.service.ModuleService;

import java.util.List;

/**
 * Integration test for the CoreServiceRemoteModuleService
 */
public class CoreServiceRemoteModuleServiceIntTest extends CoreServiceModuleServiceIntTest {

    /**
     * Hack the KualiModuleService so that it holds a CoreServiceRemoteModuleService instead of a CoreServiceModuleService
     */
    @Before
    public void setupServiceUnderTest() {

        KualiModuleService kualiModuleService = GlobalResourceLoader.<KualiModuleService>getService("rice.coreService.import.kualiModuleService");

        CoreServiceRemoteModuleService coreServiceRemoteModuleService = new CoreServiceRemoteModuleService();

        coreServiceRemoteModuleService.setModuleConfiguration(GlobalResourceLoader.<ModuleConfiguration>getService("coreServiceServerModuleConfiguration"));
        coreServiceRemoteModuleService.setKualiModuleService(kualiModuleService);

        List<ModuleService> moduleServices = kualiModuleService.getInstalledModuleServices();
        moduleServices.remove(kualiModuleService.getResponsibleModuleService(NamespaceBo.class));

        moduleServices.add(coreServiceRemoteModuleService);

        super.setupServiceUnderTest();
    }

    // Tests are inherited

}
