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


import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient

import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.joda.time.DateTime
import org.kuali.rice.kim.api.role.Role
import org.kuali.rice.kim.api.services.KimApiServiceLocator
import org.kuali.rice.kim.api.type.KimTypeInfoService
import org.kuali.rice.kim.api.type.KimType
import org.kuali.rice.kim.framework.role.RoleEbo
import org.kuali.rice.kim.impl.type.KimTypeBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.springframework.util.AutoPopulatingList

@Entity
@Table(name = "KRIM_ROLE_T")
public class RoleBo extends PersistableBusinessObjectBase implements RoleEbo {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ROLE_ID")
    String id;

    @Column(name = "ROLE_NM")
    String name;

    @Column(name = "DESC_TXT", length = 4000)
    String description;

    @Type(type = "yes_no")
    @Column(name = "ACTV_IND")
    boolean active;

    @Column(name = "KIM_TYP_ID")
    String kimTypeId;

    @Column(name = "NMSPC_CD")
    String namespaceCode;

    @OneToMany(targetEntity = RoleMemberBo.class, cascade = [CascadeType.MERGE, CascadeType.REFRESH], fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "ROLE_ID", insertable = false, updatable = false)
    List<RoleMemberBo> members = new AutoPopulatingList(RoleMemberBo.class);

    @Transient
    String principalName;

    @Transient
    String groupNamespaceCode;

    @Transient
    String groupName;

    @Transient
    String permNamespaceCode;

    @Transient
    String permName;

    @Transient
    String permTmplNamespaceCode;

    @Transient
    String permTmplName;

    @Transient
    String respNamespaceCode;

    @Transient
    String respName;

    @Transient
    String respTmplNamespaceCode;

    @Transient
    String respTmplName;

    protected List<String> getMembersOfType(String memberTypeCode) {
        List<String> roleMembers = new ArrayList<String>();
        for (RoleMemberBo member: getMembers()) {
            if (member.getMemberTypeCode().equals(memberTypeCode)
                    && member.isActive(new DateTime())) {
                roleMembers.add(member.getMemberId());
            }
        }
        return roleMembers;
    }

    public KimTypeBo getKimRoleType() {
        if (kimTypeId == null) {
            return null;
        }
        KimType type = getTypeInfoService().getKimType(kimTypeId)
        if (type == null) {
            return null;
        }
        return KimTypeBo.from(type);
    }

    private transient static KimTypeInfoService kimTypeInfoService;

    protected KimTypeInfoService getTypeInfoService() {
        if (kimTypeInfoService == null) {
            kimTypeInfoService = KimApiServiceLocator.getKimTypeInfoService();
        }
        return kimTypeInfoService;
    }


    public static Role to(RoleBo bo) {
        if (bo == null) {return null;}
        return Role.Builder.create(bo).build()
    }

    public static RoleBo from(Role immutable) {
        if (immutable == null) {return null}
        RoleBo bo = new RoleBo()
        bo.id = immutable.id;
        bo.name = immutable.name
        bo.namespaceCode = immutable.namespaceCode
        bo.description = immutable.description
        bo.kimTypeId = immutable.kimTypeId
        bo.active = immutable.active
        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo
    }
}
