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


package org.kuali.rice.kim.impl.role;


import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.role.RoleResponsibility
import org.kuali.rice.kim.api.role.RoleResponsibilityContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import javax.persistence.FetchType
import org.kuali.rice.kim.impl.responsibility.ResponsibilityBo

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name = "KRIM_ROLE_RSP_T")
public class RoleResponsibilityBo extends PersistableBusinessObjectBase implements RoleResponsibilityContract {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_RSP_ID")
    String roleResponsibilityId;

    @Column(name = "ROLE_ID")
    String roleId;

    @Column(name = "RSP_ID")
    String responsibilityId;

    @Type(type = "yes_no")
    @Column(name = "ACTV_IND")
    boolean active;

    @ManyToOne(targetEntity = ResponsibilityBo.class, fetch = FetchType.EAGER, cascade = [])
    @JoinColumn(name = "RSP_ID", insertable = false, updatable = false)
    ResponsibilityBo kimResponsibility;


    public static RoleResponsibility to(RoleResponsibilityBo bo) {
        if (bo == null) { return null;}
        return RoleResponsibility.Builder.create(bo).build();
    }

    public static RoleResponsibilityBo from(RoleResponsibility immutable) {
        if (immutable == null) { return null; }

        RoleResponsibilityBo bo = new RoleResponsibilityBo();
        bo.roleResponsibilityId = immutable.roleResponsibilityId
        bo.roleId = immutable.roleId
        bo.responsibilityId = immutable.responsibilityId
        bo.active = immutable.active
        bo.versionNumber = immutable.versionNumber

        return bo
    }

}
