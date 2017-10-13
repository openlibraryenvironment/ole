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
package org.kuali.rice.kim.api.services;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.PersonService;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.type.KimTypeInfoService;

public class KimApiServiceLocator {

    private static final Logger LOG = Logger.getLogger(KimApiServiceLocator.class);

    public static final String KIM_GROUP_SERVICE = "kimGroupService";
    public static final String KIM_IDENTITY_SERVICE = "kimIdentityService";
    public static final String KIM_PERMISSION_SERVICE = "kimPermissionService";
    public static final String KIM_RESPONSIBILITY_SERVICE = "kimResponsibilityService";
    public static final String KIM_ROLE_SERVICE = "kimRoleService";
    public static final String KIM_PERSON_SERVICE = "personService";
    public static final String KIM_TYPE_INFO_SERVICE = "kimTypeInfoService";

    static <T> T getService(String serviceName) {
        return GlobalResourceLoader.<T>getService(serviceName);
    }

    public static KimTypeInfoService getKimTypeInfoService() {
        return getService(KIM_TYPE_INFO_SERVICE);
    }

    public static PersonService getPersonService() {
        return getService(KIM_PERSON_SERVICE);
    }

    public static RoleService getRoleService() {
        return getService(KIM_ROLE_SERVICE);
    }
    
    public static GroupService getGroupService() {
    	return getService(KIM_GROUP_SERVICE);
    }
    
    public static IdentityService getIdentityService() {
    	return getService(KIM_IDENTITY_SERVICE);
    }
    public static PermissionService getPermissionService() {
    	return getService(KIM_PERMISSION_SERVICE);
    }
    public static ResponsibilityService getResponsibilityService() {
    	return getService(KIM_RESPONSIBILITY_SERVICE);
    }
    
}
