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
package org.kuali.rice.kim.impl.permission;



import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.springframework.util.AutoPopulatingList;
import org.kuali.rice.kim.api.permission.Permission;

import java.util.List;

//strange - hacky non-PBO
public class UberPermissionBo extends PermissionBo {

    private static final long serialVersionUID = 1L;

    private List<RoleBo> assignedToRoles = new AutoPopulatingList(RoleBo.class);
    private String assignedToRoleNamespaceForLookup;
    private String assignedToRoleNameForLookup;
    private RoleBo assignedToRole;
    private String assignedToPrincipalNameForLookup;
    private Person assignedToPrincipal;
    private String assignedToGroupNamespaceForLookup;
    private String assignedToGroupNameForLookup;
    private GroupBo assignedToGroup;
    private String attributeName;
    private String attributeValue;
    private String detailCriteria;

    public static UberPermissionBo from(PermissionBo bo) {
        if (bo == null) {
            return null;
        }
        Permission.Builder builder =  Permission.Builder.create(bo);
        return (UberPermissionBo) PermissionBo.from(builder.build());

    }
    public String getAssignedToRolesToDisplay() {
        StringBuffer assignedToRolesText = new StringBuffer();
        for (RoleBo roleImpl: assignedToRoles) {
            assignedToRolesText.append(roleImpl.getNamespaceCode().trim())
                    .append(" ")
                    .append(roleImpl.getName().trim())
                    .append(KimConstants.KimUIConstants.COMMA_SEPARATOR);
        }
        return StringUtils.chomp(assignedToRolesText.toString(), KimConstants.KimUIConstants.COMMA_SEPARATOR);
    }

    public List<RoleBo> getAssignedToRoles() {
        return assignedToRoles;
    }

    public void setAssignedToRoles(List<RoleBo> assignedToRoles) {
        this.assignedToRoles = assignedToRoles;
    }

    public String getAssignedToRoleNamespaceForLookup() {
        return assignedToRoleNamespaceForLookup;
    }

    public void setAssignedToRoleNamespaceForLookup(String assignedToRoleNamespaceForLookup) {
        this.assignedToRoleNamespaceForLookup = assignedToRoleNamespaceForLookup;
    }

    public String getAssignedToRoleNameForLookup() {
        return assignedToRoleNameForLookup;
    }

    public void setAssignedToRoleNameForLookup(String assignedToRoleNameForLookup) {
        this.assignedToRoleNameForLookup = assignedToRoleNameForLookup;
    }

    public RoleBo getAssignedToRole() {
        return assignedToRole;
    }

    public void setAssignedToRole(RoleBo assignedToRole) {
        this.assignedToRole = assignedToRole;
    }

    public String getAssignedToPrincipalNameForLookup() {
        return assignedToPrincipalNameForLookup;
    }

    public void setAssignedToPrincipalNameForLookup(String assignedToPrincipalNameForLookup) {
        this.assignedToPrincipalNameForLookup = assignedToPrincipalNameForLookup;
    }

    public Person getAssignedToPrincipal() {
        return assignedToPrincipal;
    }

    public void setAssignedToPrincipal(Person assignedToPrincipal) {
        this.assignedToPrincipal = assignedToPrincipal;
    }

    public String getAssignedToGroupNamespaceForLookup() {
        return assignedToGroupNamespaceForLookup;
    }

    public void setAssignedToGroupNamespaceForLookup(String assignedToGroupNamespaceForLookup) {
        this.assignedToGroupNamespaceForLookup = assignedToGroupNamespaceForLookup;
    }

    public String getAssignedToGroupNameForLookup() {
        return assignedToGroupNameForLookup;
    }

    public void setAssignedToGroupNameForLookup(String assignedToGroupNameForLookup) {
        this.assignedToGroupNameForLookup = assignedToGroupNameForLookup;
    }

    public GroupBo getAssignedToGroup() {
        return assignedToGroup;
    }

    public void setAssignedToGroup(GroupBo assignedToGroup) {
        this.assignedToGroup = assignedToGroup;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getDetailCriteria() {
        return detailCriteria;
    }

    public void setDetailCriteria(String detailCriteria) {
        this.detailCriteria = detailCriteria;
    }


}

