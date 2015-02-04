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
package org.kuali.ole.select.document.validation.impl;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.select.businessobject.OleDefaultValue;
import org.kuali.ole.select.constants.OleSelectPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.*;

//import org.kuali.rice.kim.impl.role.RoleBo;

public class OleDefaultValueRule extends MaintenanceDocumentRuleBase {

    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDefaultValueRule.class);

    @Override
    protected boolean processCustomRouteDocumentBusinessRules(MaintenanceDocument document) {
        // TODO Auto-generated method stub
        boolean valid = processValidation(document);
        return valid & super.processCustomRouteDocumentBusinessRules(document);
    }

    private boolean processValidation(MaintenanceDocument document) {
        boolean valid = true;
        valid &= processUsersValidation(document);
        valid &= processRolesValidation(document);
        valid &= processUserIdValidation(document);
        valid &= processDefaultValueForValidation(document);
        valid &= processSystemValidation(document);
        return valid;
    }

    private boolean processUserIdValidation(MaintenanceDocument document) {
        boolean valid = true;
        //String role = null;
        boolean roleFlag = false;
        String role = getRole();
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                roleFlag = true;
            }
        }
        if (!roleFlag) {
            OleDefaultValue oleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
            if (!GlobalVariables.getUserSession().getPrincipalId().equalsIgnoreCase(oleDefaultValue.getUserId())) {
                valid = false;
                putFieldError(OleSelectConstant.DEFAULT_VALUE_USERID, OleSelectPropertyConstants.ERROR_USERID_SHOULD_BE_LOGGED_IN_USERID);
            }
        }
        return valid;
    }

    private boolean processUsersValidation(MaintenanceDocument document) {
        boolean valid = true;
        OleDefaultValue oleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
        String role = getRole();
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (!KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                String userId = GlobalVariables.getUserSession().getPrincipalId();
                Map<String, Object> users = new HashMap<String, Object>();
                users.put("userId", userId);
                List<OleDefaultValue> usersList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultValue.class, users);
                if (usersList.size() > 0) {
                    for (int i = 0; i < usersList.size(); i++) {
                        if (usersList.get(i).getUserId().equalsIgnoreCase(oleDefaultValue.getUserId())) {
                            if (usersList.get(i).getDefaultTableColumnId().intValue() == oleDefaultValue.getDefaultTableColumnId().intValue()) {
                                if (oleDefaultValue.getDefaultValueId() == null) {
                                    valid = false;
                                    putFieldError(OleSelectConstant.DEFAULT_VALUE_USERID, OleSelectPropertyConstants.ERROR_USERID_EXIST);
                                } else {
                                    if (!usersList.get(i).getDefaultValueId().toString().equalsIgnoreCase(oleDefaultValue.getDefaultValueId().toString())) {
                                        valid = false;
                                        putFieldError(OleSelectConstant.DEFAULT_VALUE_USERID, OleSelectPropertyConstants.ERROR_USERID_EXIST);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return valid;
    }

    private boolean processRolesValidation(MaintenanceDocument document) {
        boolean valid = true;
        OleDefaultValue newOleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
        String role = getRole();
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                if (newOleDefaultValue.getDefaultValueFor().equalsIgnoreCase(OleSelectConstant.DEFAULT_VALUE_ROLE)) {
                    String defaultValueFor = OleSelectConstant.DEFAULT_VALUE_ROLE;
                    Map<String, Object> roles = new HashMap<String, Object>();
                    roles.put("defaultValueFor", defaultValueFor);
                    List<OleDefaultValue> rolesList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultValue.class, roles);
                    if (rolesList.size() > 0) {
                        for (int i = 0; i < rolesList.size(); i++) {
                            if (rolesList.get(i).getDefaultTableColumnId().intValue() == newOleDefaultValue.getDefaultTableColumnId().intValue() && rolesList.get(i).getRoleId().equalsIgnoreCase(newOleDefaultValue.getRoleId())) {
                                if (newOleDefaultValue.getDefaultValueId() == null) {
                                    valid = false;
                                    putFieldError(OleSelectConstant.DEFAULT_VALUE_ROLE_ID, OleSelectPropertyConstants.ERROR_ROLE_EXIST);
                                } else {
                                    if (!rolesList.get(i).getDefaultValueId().toString().equalsIgnoreCase(newOleDefaultValue.getDefaultValueId().toString())) {
                                        valid = false;
                                        putFieldError(OleSelectConstant.DEFAULT_VALUE_ROLE_ID, OleSelectPropertyConstants.ERROR_ROLE_EXIST);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return valid;
    }

    private boolean processDefaultValueForValidation(MaintenanceDocument document) {
        boolean valid = true;
        OleDefaultValue newOleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
        String role = getRole();
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                if (OleSelectConstant.DEFAULT_VALUE_ROLE.equalsIgnoreCase(newOleDefaultValue.getDefaultValueFor())) {
                    if (newOleDefaultValue.getUserId() != null) {
                        valid = false;
                        putFieldError(OleSelectConstant.DEFAULT_VALUE_USERID, OleSelectPropertyConstants.ERROR_USERID_SHOULD_BE_EMPTY_FOR_ROLE);
                    }
                    if (newOleDefaultValue.getRoleId() == null) {
                        putFieldError(OleSelectConstant.DEFAULT_VALUE_ROLE_ID, OleSelectPropertyConstants.ERROR_ROLEID_SHOULDNOT_BE_EMPTY);
                    }
                } else {
                    if (newOleDefaultValue.getUserId() != null || newOleDefaultValue.getRoleId() != null) {
                        valid = false;
                        putFieldError(OleSelectConstant.DEFAULT_VALUE_USERID, OleSelectPropertyConstants.ERROR_USERID_SHOULD_BE_EMPTY_FOR_SYSTEM);
                        putFieldError(OleSelectConstant.DEFAULT_VALUE_ROLE_ID, OleSelectPropertyConstants.ERROR_ROLEID_SHOULD_BE_EMPTY_FOR_SYSTEM);
                    }
                }
            }
        }
        return valid;
    }

    public boolean processSystemValidation(MaintenanceDocument document) {
        boolean valid = true;
        OleDefaultValue newOleDefaultValue = (OleDefaultValue) document.getNewMaintainableObject().getBusinessObject();
        String role = getRole();
        List<String> roleImpl = getRolesForPrincipal(GlobalVariables.getUserSession().getPrincipalId());
        Iterator itr = roleImpl.iterator();
        while (itr.hasNext()) {
            String kimrole = itr.next().toString();
            if (KimApiServiceLocator.getRoleService().getRole(kimrole).getName().equalsIgnoreCase(role)) {
                if (OleSelectConstant.DEFAULT_VALUE_SYSTEM.equalsIgnoreCase(newOleDefaultValue.getDefaultValueFor())) {
                    String defaultValueFor = OleSelectConstant.DEFAULT_VALUE_SYSTEM;
                    Map<String, Object> system = new HashMap<String, Object>();
                    system.put("defaultValueFor", defaultValueFor);
                    List<OleDefaultValue> systemList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleDefaultValue.class, system);
                    if (systemList.size() > 0) {
                        for (int i = 0; i < systemList.size(); i++) {
                            if (systemList.get(i).getDefaultTableColumnId().intValue() == newOleDefaultValue.getDefaultTableColumnId().intValue()) {
                                if (newOleDefaultValue.getDefaultValueId() == null) {
                                    valid = false;
                                    putFieldError(OleSelectConstant.DEFAULT_VALUE_FOR, OleSelectPropertyConstants.ERROR_SYSTEM_EXIST);
                                } else {
                                    if (!systemList.get(i).getDefaultValueId().toString().equalsIgnoreCase(newOleDefaultValue.getDefaultValueId().toString())) {
                                        valid = false;
                                        putFieldError(OleSelectConstant.DEFAULT_VALUE_FOR, OleSelectPropertyConstants.ERROR_SYSTEM_EXIST);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return valid;
    }

    @SuppressWarnings("unchecked")
    private List<String> getRolesForPrincipal(String principalId) {
        if (principalId == null) {
            return new ArrayList<String>();
        }
        //   Map<String,String> criteria = new HashMap<String,String>( 2 );
        //   criteria.put("members.memberId", principalId);
        //   criteria.put("members.memberTypeCode", MemberType.PRINCIPAL.getCode());
        //return (List<String>)SpringContext.getBean(BusinessObjectService.class).findMatching(RoleBo.class, criteria);
        return (List<String>) KimApiServiceLocator.getRoleService().getMemberParentRoleIds(MemberType.PRINCIPAL.getCode(), principalId);
    }

    public String getRole() {
        return OleSelectConstant.SYSTEM_USER_ROLE_NAME;
    }

}
