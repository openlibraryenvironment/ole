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
package org.kuali.rice.krms.impl.authorization;

import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.impl.repository.ContextBoService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;

import java.util.HashMap;
import java.util.Map;

public class AgendaAuthorizationServiceImpl implements AgendaAuthorizationService {

    static final String NAMESPACE_CODE = "namespaceCode";

    @Override
    public boolean isAuthorized(String permissionName, String contextId) {
        String namespace = "";
        if (contextId != null) {
            ContextDefinition context = getContextBoService().getContextByContextId(contextId);
            if (context != null) { // business rules should have already reported this as an error.
                namespace = context.getNamespace();
            }
        }

        Map<String, String> qualification = new HashMap<String, String>();
        boolean isAuthorized = getPermissionService().isAuthorized(
                GlobalVariables.getUserSession().getPrincipalId(),
                namespace,
                permissionName,
                qualification);
        return isAuthorized;
    }

    /**
     * return the contextBoService
     */
    private ContextBoService getContextBoService() {
        return KrmsRepositoryServiceLocator.getContextBoService();
    }

    /**
     * returns the permissionService
     */
    private PermissionService getPermissionService() {
        return KimApiServiceLocator.getPermissionService();
    }
}