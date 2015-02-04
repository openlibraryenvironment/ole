/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject.defaultvalue;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.valuefinder.ValueFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.kuali.rice.kim.impl.role.RoleBo;

public class DefaultValueLoggedInUser implements ValueFinder {

    @Override
    public String getValue() {
        // TODO Auto-generated method stub
        boolean flag = false;
        String role = getRole();
        String principalName = null;
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (KimApiServiceLocator.getRoleService().getRole(kimrole) != null) {
                if (KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                    flag = true;
                    principalName = null;
                }
            }
        }
        if (!flag) {
            principalName = GlobalVariables.getUserSession().getPrincipalId();
        }
        return principalName;
    }

    @SuppressWarnings("unchecked")
    private List<String> getRolesForPrincipal(String principalId) {
        if (principalId == null) {
            return new ArrayList<String>();
        }
        /*Map<String,String> criteria = new HashMap<String,String>( 2 );
        criteria.put("members.memberId", principalId);
        criteria.put("members.memberTypeCode", MemberType.PRINCIPAL.getCode());
        return (List<RoleBo>)SpringContext.getBean(BusinessObjectService.class).findMatching(RoleBo.class, criteria);*/
        return (List<String>) KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.PRINCIPAL.getCode(), principalId);
    }

    public String getRole() {
        return OleSelectConstant.SYSTEM_USER_ROLE_NAME;
    }

}
