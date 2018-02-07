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
package org.kuali.rice.kim.impl.common.delegate

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.common.delegate.DelegateType
import org.kuali.rice.kim.api.common.delegate.DelegateTypeContract
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.springframework.util.AutoPopulatingList
import org.kuali.rice.core.api.delegation.DelegationType

@Entity
@Table(name = "KRIM_DLGN_T")
public class DelegateTypeBo extends PersistableBusinessObjectBase implements DelegateTypeContract {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DLGN_ID")
    String delegationId;

    @Column(name = "ROLE_ID")
    String roleId;

    @Type(type = "yes_no")
    @Column(name = "ACTV_IND")
    boolean active = true;

    @Column(name = "KIM_TYP_ID")
    String kimTypeId;

    @Column(name = "DLGN_TYP_CD")
    String delegationTypeCode;

    @OneToMany(targetEntity = DelegateMemberBo.class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "DLGN_ID", insertable = false, updatable = false)
    List<DelegateMemberBo> members = new AutoPopulatingList(DelegateMemberBo.class);

    public void setDelegationType(DelegationType type) {
        this.delegationTypeCode = type.getCode();
    }

    public DelegationType getDelegationType() {
        return DelegationType.fromCode(this.delegationTypeCode);
    }

    public static DelegateType to(DelegateTypeBo bo) {
        return DelegateType.Builder.create(bo).build()
    }

    public static DelegateTypeBo from(DelegateType immutable) {
        // build list of DelegateMemberBo
        def tmpMembers = []
        immutable.members.each {tmpMembers.add(DelegateMemberBo.from(it))}

        return new DelegateTypeBo(
                delegationId: immutable.delegationId,
                roleId: immutable.roleId,
                active: immutable.active,
                kimTypeId: immutable.kimTypeId,
                delegationTypeCode: immutable.delegationTypeCode,
                members:  tmpMembers
        );
    }
}
