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
package org.kuali.rice.krad.kim;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.krad.kim.NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ParameterPermissionTypeServiceImpl extends NamespaceWildcardAllowedAndOrStringExactMatchPermissionTypeServiceImpl {

    @Override
    protected List<String> getRequiredAttributes() {
        final List<String> attrs = new ArrayList<String>(super.getRequiredAttributes());
        attrs.add(KimConstants.AttributeConstants.PARAMETER_NAME);
        attrs.add(KimConstants.AttributeConstants.COMPONENT_NAME);
        return Collections.unmodifiableList(attrs);
    }

    @Override
    protected boolean isCheckRequiredAttributes() {
        return true;
    }
    
    @Override
    protected List<Permission> performPermissionMatches(Map<String, String> requestedDetails, List<Permission> permissionsList) {
        String requestedParameterName = requestedDetails.get(KimConstants.AttributeConstants.PARAMETER_NAME);
        String requestedComponentName = requestedDetails.get(KimConstants.AttributeConstants.COMPONENT_NAME);
        List<Permission> matchingPermissions = new ArrayList<Permission>();
        for (Permission kpi : permissionsList ) {
            PermissionBo bo = PermissionBo.from(kpi);
            String parameterName = bo.getDetails().get(KimConstants.AttributeConstants.PARAMETER_NAME);
            String componentName = bo.getDetails().get(KimConstants.AttributeConstants.COMPONENT_NAME);
            if ( (StringUtils.isBlank(parameterName)
                    || StringUtils.equals(requestedParameterName, parameterName)) 
                &&(StringUtils.isBlank(componentName)
                        || StringUtils.equals(requestedComponentName, componentName))) {
                matchingPermissions.add(kpi);
            }
        }
        return super.performPermissionMatches(requestedDetails, matchingPermissions);
    }    
}
