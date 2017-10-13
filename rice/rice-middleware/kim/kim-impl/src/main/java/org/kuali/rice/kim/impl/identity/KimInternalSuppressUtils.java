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
package org.kuali.rice.kim.impl.identity;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.Collections;

class KimInternalSuppressUtils {

    private static IdentityService identityService;
    private static PermissionService permissionService;

	private KimInternalSuppressUtils() {
		throw new UnsupportedOperationException("do not call");
	}

    public static boolean isSuppressName(String entityId) {
        EntityPrivacyPreferences privacy = null;
        EntityDefault entityInfo = getIdentityService().getEntityDefault(entityId);
        if (entityInfo != null) {
            privacy = entityInfo.getPrivacyPreferences();
        } else {
            return true;
        }
        UserSession userSession = GlobalVariables.getUserSession();

        boolean suppressName = false;
        if (privacy != null) {
            suppressName = privacy.isSuppressName();
        }

        return suppressName
                && userSession != null
                && !StringUtils.equals(userSession.getPerson().getEntityId(), entityId)
                && !canOverrideEntityPrivacyPreferences(entityInfo.getPrincipals().get(0).getPrincipalId());
    }

    public static boolean isSuppressEmail(String entityId) {
        EntityPrivacyPreferences privacy = null;
        EntityDefault entityInfo = getIdentityService().getEntityDefault(entityId);
        if (entityInfo != null) {
            privacy = entityInfo.getPrivacyPreferences();
        } else {
            return true;
        }
        UserSession userSession = GlobalVariables.getUserSession();

        boolean suppressEmail = false;
        if (privacy != null) {
            suppressEmail = privacy.isSuppressEmail();
        }
        return suppressEmail
                && userSession != null
                && !StringUtils.equals(userSession.getPerson().getEntityId(), entityId)
                && !canOverrideEntityPrivacyPreferences(entityInfo.getPrincipals().get(0).getPrincipalId());
    }

    public static boolean isSuppressAddress(String entityId) {
        EntityPrivacyPreferences privacy = null;
        EntityDefault entityInfo = getIdentityService().getEntityDefault(entityId);
        if (entityInfo != null) {
            privacy = entityInfo.getPrivacyPreferences();
        } else {
            return false;
        }
        UserSession userSession = GlobalVariables.getUserSession();

        boolean suppressAddress = false;
        if (privacy != null) {
            suppressAddress = privacy.isSuppressAddress();
        }
        return suppressAddress
                && userSession != null
                && !StringUtils.equals(userSession.getPerson().getEntityId(), entityId)
                && !canOverrideEntityPrivacyPreferences(entityInfo.getPrincipals().get(0).getPrincipalId());
    }

    public static boolean isSuppressPhone(String entityId) {
        EntityPrivacyPreferences privacy = null;
        EntityDefault entityInfo = getIdentityService().getEntityDefault(entityId);
        if (entityInfo != null) {
            privacy = entityInfo.getPrivacyPreferences();
        } else {
            return true;
        }
        UserSession userSession = GlobalVariables.getUserSession();

        boolean suppressPhone = false;
        if (privacy != null) {
            suppressPhone = privacy.isSuppressPhone();
        }
        return suppressPhone
                && userSession != null
                && !StringUtils.equals(userSession.getPerson().getEntityId(), entityId)
                && !canOverrideEntityPrivacyPreferences(entityInfo.getPrincipals().get(0).getPrincipalId());
    }

    public static boolean isSuppressPersonal(String entityId) {
        EntityPrivacyPreferences privacy = null;
        EntityDefault entityInfo = getIdentityService().getEntityDefault(entityId);
        if (entityInfo != null) {
            privacy = entityInfo.getPrivacyPreferences();
        } else {
            return true;
        }
        UserSession userSession = GlobalVariables.getUserSession();

        boolean suppressPersonal = false;
        if (privacy != null) {
            suppressPersonal = privacy.isSuppressPersonal();
        }
        return suppressPersonal
                && userSession != null
                && !StringUtils.equals(userSession.getPerson().getEntityId(), entityId)
                && !canOverrideEntityPrivacyPreferences(entityInfo.getPrincipals().get(0).getPrincipalId());
    }

    protected static boolean canOverrideEntityPrivacyPreferences( String principalId ){
		return getPermissionService().isAuthorized(
				GlobalVariables.getUserSession().getPrincipalId(),
				KimConstants.NAMESPACE_CODE,
				KimConstants.PermissionNames.OVERRIDE_ENTITY_PRIVACY_PREFERENCES,
				Collections.singletonMap(KimConstants.AttributeConstants.PRINCIPAL_ID, principalId) );
	}

    private static IdentityService getIdentityService() {
		if ( identityService == null ) {
			identityService = KimApiServiceLocator.getIdentityService();
		}
		return identityService;
	}

    private static PermissionService getPermissionService() {
		if ( permissionService == null ) {
			permissionService = KimApiServiceLocator.getPermissionService();
		}
		return permissionService;
    }
}
