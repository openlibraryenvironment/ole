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
package org.kuali.rice.ken.web.spring;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.coreservice.api.namespace.Namespace;
import org.kuali.rice.coreservice.api.namespace.NamespaceService;
import org.kuali.rice.ken.exception.ErrorList;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for KEN controllers for sending notifications
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class BaseSendNotificationController extends MultiActionController {
    private static final Logger LOG = Logger.getLogger(BaseSendNotificationController.class);

    private static final String USER_RECIPS_PARAM = "userRecipients";
    private static final String WORKGROUP_RECIPS_PARAM = "workgroupRecipients";
    private static final String WORKGROUP_NAMESPACE_CODES_PARAM = "workgroupNamespaceCodes";
    private static final String SPLIT_REGEX = "(%2C|,)";
    
    private static IdentityService identityService;
    private static GroupService groupService;
    private static NamespaceService namespaceService;

    protected static IdentityService getIdentityService() {
        if ( identityService == null ) {
            identityService = KimApiServiceLocator.getIdentityService();
        }
        return identityService;
    }

    protected static GroupService getGroupService() {
        if ( groupService == null ) {
            groupService = KimApiServiceLocator.getGroupService();
        }
        return groupService;
    }
    
    protected static NamespaceService getNamespaceService() {
        if ( namespaceService == null ) {
            namespaceService = CoreServiceApiServiceLocator.getNamespaceService();
        }
        return namespaceService;
    }
    
    protected String[] parseUserRecipients(HttpServletRequest request) {
        return parseCommaSeparatedValues(request, USER_RECIPS_PARAM);
    }

    protected String[] parseWorkgroupRecipients(HttpServletRequest request) {
        return parseCommaSeparatedValues(request, WORKGROUP_RECIPS_PARAM);
    }

    protected String[] parseWorkgroupNamespaceCodes(HttpServletRequest request) {
    	return parseCommaSeparatedValues(request, WORKGROUP_NAMESPACE_CODES_PARAM);
    }
    
    protected String[] parseCommaSeparatedValues(HttpServletRequest request, String param) {
        String vals = request.getParameter(param);
        if (vals != null) {
            String[] split = vals.split(SPLIT_REGEX);
            List<String> strs = new ArrayList<String>();
            for (String component: split) {
                if (StringUtils.isNotBlank(component)) {
                    strs.add(component.trim());
                }
            }
            return strs.toArray(new String[strs.size()]);
        } else {
            return new String[0];
        }
    }

    protected boolean isUserRecipientValid(String user, ErrorList errors) {
        boolean valid = true;
        Principal principal = getIdentityService().getPrincipalByPrincipalName(user);
        if (principal == null) {
        	valid = false;
        	errors.addError("'" + user + "' is not a valid principal name");
        }

        return valid;
    }

    protected boolean isWorkgroupRecipientValid(String groupName, String namespaceCode, ErrorList errors) {
    	Namespace nSpace = getNamespaceService().getNamespace(namespaceCode);
    	if (nSpace == null) {
    		errors.addError((new StringBuilder()).append('\'').append(namespaceCode).append("' is not a valid namespace code").toString());
    		return false;
    	} else {
    		Group i = getGroupService().getGroupByNamespaceCodeAndName(namespaceCode, groupName);
       		if (i == null) {
       			errors.addError((new StringBuilder()).append('\'').append(groupName).append(
       					"' is not a valid group name for namespace code '").append(namespaceCode).append('\'').toString());
       			return false;
       		} else {
       			return true;
       		}
    	}
    }
    protected String getPrincipalIdFromIdOrName(String principalIdOrName) {
        Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal(principalIdOrName);
        if (principal == null) {
            principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalIdOrName);
        }
        if (principal == null) {
            throw new RiceIllegalArgumentException("Could not locate a principal as initiator with the given remoteUser of " + principalIdOrName);
        }
        return principal.getPrincipalId();
    }
}
