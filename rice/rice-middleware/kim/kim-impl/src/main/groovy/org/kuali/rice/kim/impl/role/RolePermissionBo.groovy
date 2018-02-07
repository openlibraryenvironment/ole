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
package org.kuali.rice.kim.impl.role

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn;


import javax.persistence.OneToOne;
import javax.persistence.Table

import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.role.RolePermission
import org.kuali.rice.kim.api.role.RolePermissionContract
import org.kuali.rice.kim.impl.permission.PermissionBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@Entity
@Table(name="KRIM_ROLE_PERM_T")
public class RolePermissionBo extends PersistableBusinessObjectBase implements RolePermissionContract {
    private static final long serialVersionUID = 1L;

    @Id
	@Column(name="ROLE_PERM_ID")
	String id

	@Column(name="ROLE_ID")
	String roleId;
	
	@Column(name="PERM_ID")
	String permissionId
	
	@Column(name="ACTV_IND")
	@Type(type="yes_no")
	boolean active

	@OneToOne(targetEntity=PermissionBo.class,cascade=[],fetch=FetchType.EAGER)
	@JoinColumn(name = "PERM_ID", insertable = false, updatable = false)
	PermissionBo permission;
		
    /**
     * Converts a mutable bo to its immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static RolePermission to(RolePermissionBo bo) {
        if (bo == null) {
            return null
        }

        return RolePermission.Builder.create(bo).build();
    }

    /**
     * Converts a immutable object to its mutable counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static RolePermissionBo from(RolePermission im) {
        if (im == null) {
            return null
        }

        RolePermissionBo bo = new RolePermissionBo()
        bo.id = im.id
		bo.roleId = im.roleId
		bo.permissionId = im.permissionId
		bo.active = im.active
        bo.versionNumber = im.versionNumber
		bo.objectId = im.objectId;

        return bo
    }

}
