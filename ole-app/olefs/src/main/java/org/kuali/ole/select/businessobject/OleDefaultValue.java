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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.core.api.mo.common.active.MutableInactivatable;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.math.BigDecimal;


public class OleDefaultValue extends PersistableBusinessObjectBase implements MutableInactivatable {

    private BigDecimal defaultValueId;
    private BigDecimal defaultTableColumnId;
    private String defaultValue;
    private String defaultValueFor;
    private String roleId;
    private String userId;
    private boolean active;
    private OleDefaultTableColumn oleDefaultTableColumn;
    private RoleBo roles;
    private Person users;

    public Person getUsers() {
        return users;
    }

    public void setUsers(Person users) {
        this.users = users;
    }

    /* private String documentColumn;


    public String getDocumentColumn() {
        return documentColumn;
    }

    public void setDocumentColumn(String documentColumn) {
        this.documentColumn = documentColumn;
    }
*/
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public RoleBo getRoles() {
        return roles;
    }

    public void setRoles(RoleBo roles) {
        this.roles = roles;
    }

    public BigDecimal getDefaultValueId() {
        return defaultValueId;
    }

    public void setDefaultValueId(BigDecimal defaultValueId) {
        this.defaultValueId = defaultValueId;
    }

    public BigDecimal getDefaultTableColumnId() {
        return defaultTableColumnId;
    }

    public void setDefaultTableColumnId(BigDecimal defaultTableColumnId) {
        this.defaultTableColumnId = defaultTableColumnId;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValueFor() {
        return defaultValueFor;
    }

    public void setDefaultValueFor(String defaultValueFor) {
        this.defaultValueFor = defaultValueFor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OleDefaultTableColumn getOleDefaultTableColumn() {
        return oleDefaultTableColumn;
    }

    public void setOleDefaultTableColumn(OleDefaultTableColumn oleDefaultTableColumn) {
        this.oleDefaultTableColumn = oleDefaultTableColumn;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
