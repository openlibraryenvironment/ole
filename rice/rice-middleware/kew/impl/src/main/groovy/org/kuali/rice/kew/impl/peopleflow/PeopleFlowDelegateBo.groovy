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
package org.kuali.rice.kew.impl.peopleflow

import org.kuali.rice.core.api.delegation.DelegationType
import org.kuali.rice.core.api.membership.MemberType
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDelegate

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

import org.kuali.rice.kew.api.peopleflow.PeopleFlowDelegateContract
import org.kuali.rice.kew.api.action.ActionRequestPolicy
import org.kuali.rice.kim.api.identity.Person
import org.kuali.rice.kim.framework.group.GroupEbo
import org.kuali.rice.kim.framework.role.RoleEbo
import org.kuali.rice.kim.api.services.KimApiServiceLocator

import org.kuali.rice.krad.service.ModuleService
import org.kuali.rice.krad.service.KRADServiceLocatorWeb
import org.apache.commons.lang.StringUtils

/**
 * mapped entity for PeopleFlow members
 */
class PeopleFlowDelegateBo extends PersistableBusinessObjectBase implements PeopleFlowDelegateContract {

    String id
    String peopleFlowMemberId
    String memberId
    String memberTypeCode
    String actionRequestPolicyCode
    String delegationTypeCode
    String responsibilityId

    // non-persisted
    String memberName;
    Person person;
    GroupEbo group;
    RoleEbo role;

    public Person getPerson() {
        if (MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
            if ((this.person == null) || !person.getPrincipalId().equals(memberId) || !person.getPrincipalName().equals(memberName)) {
                // use member name first
                if (StringUtils.isNotBlank(memberName)) {
                    this.person = KimApiServiceLocator.personService.getPersonByPrincipalName(memberName);
                } else {
                    this.person = KimApiServiceLocator.personService.getPerson(memberId);
                }
            }

            if (this.person != null) {
                memberId = person.getPrincipalId();
                memberName = person.getPrincipalName();

                return this.person;
            }
        }

        return KimApiServiceLocator.personService.personImplementationClass.newInstance();
    }

    public GroupEbo getGroup() {
        if (MemberType.GROUP.getCode().equals(memberTypeCode)) {
            ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(GroupEbo.class);
            group = eboModuleService.retrieveExternalizableBusinessObjectIfNecessary(this, group, "group");

            if (group != null) {
                memberId = group.id;
                memberName = group.namespaceCode + " : " + group.name;
            }
        }

        return group;
    }

    public RoleEbo getRole() {
        if (MemberType.ROLE.getCode().equals(memberTypeCode)) {
            ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(RoleEbo.class);
            role = eboModuleService.retrieveExternalizableBusinessObjectIfNecessary(this, role, "role");

            if (role != null) {
                memberId = role.id;
                memberName = role.namespaceCode + " : " + role.name;
            }
        }

        return role;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;

        // trigger update of related object (only person can be updated by name)
        if (MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
            getPerson();
        }
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
        updateRelatedObject()
    }

    // trigger update of related object
    public void updateRelatedObject() {
        if (MemberType.PRINCIPAL.getCode().equals(memberTypeCode)) {
            getPerson();
        } else if (MemberType.GROUP.getCode().equals(memberTypeCode)) {
            getGroup();
        } else if (MemberType.ROLE.getCode().equals(memberTypeCode)) {
            getRole();
        }
    }

    @Override
    MemberType getMemberType() {
        return MemberType.fromCode(memberTypeCode)
    }

    void setMemberType(MemberType type) {
        memberTypeCode = type.getCode()
    }

    @Override
    ActionRequestPolicy getActionRequestPolicy() {
        return ActionRequestPolicy.fromCode(actionRequestPolicyCode)
    }

    @Override
    DelegationType getDelegationType() {
        return DelegationType.fromCode(delegationTypeCode)
    }

    void setDelegationType(DelegationType delegationType) {
        this.delegationTypeCode = delegationType.getCode()
    }

    public static PeopleFlowDelegate to(PeopleFlowDelegateBo delegateBo) {
        if (delegateBo == null) {
            return null
        }
        PeopleFlowDelegate.Builder builder = PeopleFlowDelegate.Builder.create(delegateBo)
        return builder.build()
    }

    public static PeopleFlowDelegateBo from(PeopleFlowDelegate delegate) {
        if (delegate == null) {
            return null
        }
        PeopleFlowDelegateBo delegateBo = new PeopleFlowDelegateBo()
        delegateBo.setMemberId(delegate.getMemberId())
        delegateBo.setMemberType(delegate.getMemberType())
        if (delegate.getActionRequestPolicy() != null) {
            delegateBo.setActionRequestPolicyCode(delegate.getActionRequestPolicy().getCode())
        }
        if (delegate.getDelegationType() != null) {
            delegateBo.setDelegationTypeCode(delegate.getDelegationType().getCode())
        }
        delegateBo.setResponsibilityId(delegate.getResponsibilityId())
        return delegateBo
    }

}
