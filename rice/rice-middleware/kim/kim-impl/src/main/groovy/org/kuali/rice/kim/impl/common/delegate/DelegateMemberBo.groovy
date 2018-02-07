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
import org.kuali.rice.kim.api.common.delegate.DelegateMember
import org.kuali.rice.kim.api.common.delegate.DelegateMemberContract
import org.kuali.rice.kim.impl.membership.AbstractMemberBo
import org.springframework.util.AutoPopulatingList
import java.sql.Timestamp
import javax.persistence.Transient
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo
import org.apache.commons.collections.CollectionUtils

@Entity
@Table(name = "KRIM_DLGN_MBR_T")
public class DelegateMemberBo extends AbstractMemberBo implements DelegateMemberContract {

    @Id
    @Column(name = "DLGN_MBR_ID")
    String delegationMemberId;
    @Column(name = "DLGN_ID")
    String delegationId;
    @Column(name = "ROLE_MBR_ID")
    String roleMemberId;

    @OneToMany(targetEntity = DelegateMemberAttributeDataBo.class, cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "DLGN_MBR_ID", referencedColumnName = "DLGN_MBR_ID", insertable = false, updatable = false)
    List<DelegateMemberAttributeDataBo> attributeDetails = new AutoPopulatingList(DelegateMemberAttributeDataBo.class)

    @Transient
    Map<String,String> attributes

    /**
     * Returns Attributes derived from the internal List of DelegateMemberAttributeDataBos.  This field is
     * not exposed in the DelegateMemberContract as it is not a required field in the DelegateMember DTO
     * @return
     */
    public Map<String,String> getQualifier() {
        Map<String,String> attribs = new HashMap<String,String>();

        if (attributeDetails == null) {
            return attribs;
        }
        for (DelegateMemberAttributeDataBo attr: attributeDetails) {
            attribs.put(attr.getKimAttribute().getAttributeName(), attr.getAttributeValue());
        }
        return attribs
    }

    List<DelegateMemberAttributeDataBo> getAttributeDetails() {
        if (this.attributeDetails == null) {
            return new AutoPopulatingList(DelegateMemberAttributeDataBo.class);
        }
        return this.attributeDetails;
    }

    void setAttributeDetails(List<DelegateMemberAttributeDataBo> attributeDetails) {
        this.attributeDetails = attributeDetails;
    }

    Map<String,String> getAttributes() {
        return CollectionUtils.isNotEmpty(attributeDetails) ? KimAttributeDataBo.toAttributes(attributeDetails) : attributes
    }



    public static DelegateMember to(DelegateMemberBo bo) {
        if (bo == null) {return null;}
        return DelegateMember.Builder.create(bo).build();
    }

    public static DelegateMemberBo from(DelegateMember immutable) {
        if (immutable == null) { return null; }

        return new DelegateMemberBo(
                delegationMemberId: immutable.delegationMemberId,
                activeFromDateValue: immutable.activeFromDate == null ? null : new Timestamp(immutable.activeFromDate.getMillis()),
                activeToDateValue: immutable.activeToDate == null ? null : new Timestamp(immutable.activeToDate.getMillis()),
                delegationId: immutable.delegationId,
                memberId: immutable.memberId,
                roleMemberId: immutable.roleMemberId,
                typeCode: immutable.typeCode,
                versionNumber: immutable.versionNumber,
                attributes: immutable.attributes
        )
    }

    @Override
    public List buildListOfDeletionAwareLists() {
        List managedLists = super.buildListOfDeletionAwareLists();

        managedLists.add(attributeDetails);
        return managedLists;
    }
}
