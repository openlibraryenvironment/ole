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
package org.kuali.rice.kew.impl.peopleflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDefinition;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDelegate;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowMember;
import org.kuali.rice.kew.api.peopleflow.PeopleFlowService;
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition;
import org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService;
import org.kuali.rice.kew.impl.KewImplConstants;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.kew.responsibility.service.ResponsibilityIdService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PeopleFlowServiceImpl implements PeopleFlowService {

    private BusinessObjectService businessObjectService;
    private KewTypeRepositoryService kewTypeRepositoryService;
    private ResponsibilityIdService responsibilityIdService;

    @Override
    public PeopleFlowDefinition getPeopleFlow(String peopleFlowId) {
        if (StringUtils.isBlank(peopleFlowId)) {
            throw new RiceIllegalArgumentException("peopleFlowId is null or blank");
        }

        return PeopleFlowBo.to(getPeopleFlowBo(peopleFlowId));
    }

    @Override
    public PeopleFlowDefinition getPeopleFlowByName(String namespaceCode, String name) {
        if (StringUtils.isBlank(namespaceCode)) {
            throw new RiceIllegalArgumentException("namespaceCode is null or blank");
        }

        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name is null or blank");
        }

        return PeopleFlowBo.to(getPeopleFlowBoByName(namespaceCode, name));
    }

    @Override
    public PeopleFlowDefinition createPeopleFlow(PeopleFlowDefinition peopleFlow) {
        validateForCreate(peopleFlow);
        KewTypeDefinition kewTypeDefinition = loadKewTypeDefinition(peopleFlow);
        PeopleFlowBo peopleFlowBo = PeopleFlowBo.from(peopleFlow, kewTypeDefinition);
        peopleFlowBo = savePeopleFlow(peopleFlowBo);
        return PeopleFlowBo.to(peopleFlowBo);
    }

    @Override
    public PeopleFlowDefinition updatePeopleFlow(PeopleFlowDefinition peopleFlow) {
        PeopleFlowBo existingBo = validateForUpdate(peopleFlow);
        KewTypeDefinition kewTypeDefinition = loadKewTypeDefinition(peopleFlow);
        PeopleFlowBo peopleFlowBo = PeopleFlowBo.fromAndUpdate(peopleFlow, kewTypeDefinition, existingBo);
        peopleFlowBo = savePeopleFlow(peopleFlowBo);
        return PeopleFlowBo.to(peopleFlowBo);
    }

    protected KewTypeDefinition loadKewTypeDefinition(PeopleFlowDefinition peopleFlow) {
        KewTypeDefinition kewTypeDefinition = null;
        if (peopleFlow.getTypeId() != null) {
            kewTypeDefinition = getKewTypeRepositoryService().getTypeById(peopleFlow.getTypeId());
            if (kewTypeDefinition == null) {
                throw new RiceIllegalArgumentException("Failed to locate a KewTypeDefinition for the given type id of '" + peopleFlow.getTypeId() + "'");
            }
        }
        return kewTypeDefinition;
    }

    protected void validateForCreate(PeopleFlowDefinition peopleFlow) {
        if (peopleFlow == null) {
            throw new RiceIllegalArgumentException("peopleFlow is null");
        }
        if (StringUtils.isNotBlank(peopleFlow.getId())) {
            throw new RiceIllegalArgumentException("Attempted to create a new PeopleFlow definition with a specified peopleFlowId of '"
                    + peopleFlow.getId() + "'.  This is not allowed, when creating a new PeopleFlow definition, id must be null.");
        }
        if (peopleFlow.getVersionNumber() != null) {
            throw new RiceIllegalArgumentException("The version number on the given PeopleFlow definition was not null, value was " + peopleFlow.getVersionNumber() +
                    "  When creating a new PeopleFlow, the given version number must be null.");
        }
        validatePeopleFlowMembersForCreate(peopleFlow);
        if (getPeopleFlowBoByName(peopleFlow.getNamespaceCode(), peopleFlow.getName()) != null) {
            throw new RiceIllegalStateException("A PeopleFlow definition with the namespace code '" + peopleFlow.getNamespaceCode() +
            "' and name '" + peopleFlow.getName() + "' already exists.");
        }
    }

    protected void validatePeopleFlowMembersForCreate(PeopleFlowDefinition peopleFlowDefinition) {
        for (PeopleFlowMember member : peopleFlowDefinition.getMembers()) {
            if (StringUtils.isNotBlank(member.getResponsibilityId())) {
                throw new RiceIllegalArgumentException("Attempted to create a new PeopleFlow with a member that already had a responsibility id of '" +
                        member.getResponsibilityId() + "' specified.  All members must have a null responsibility id upon creation.");
            }
            for (PeopleFlowDelegate delegate : member.getDelegates()) {
                if (StringUtils.isNotBlank(delegate.getResponsibilityId())) {
                    throw new RiceIllegalArgumentException("Attempted to create a new PeopleFlow with a delegate that already had a responsibility id of '" +
                            delegate.getResponsibilityId() + "' specified.  All delegates must have a null responsibility id upon creation.");
                }
            }
        }
    }

    protected PeopleFlowBo validateForUpdate(PeopleFlowDefinition peopleFlow) {
        if (peopleFlow == null) {
            throw new RiceIllegalArgumentException("peopleFlow is null");
        }
        if (StringUtils.isBlank(peopleFlow.getId())) {
            throw new RiceIllegalArgumentException("Attempted to update a PeopleFlow definition without a specified peopleFlowId, the id is required when performing an update.");
        }
        if (peopleFlow.getVersionNumber() == null) {
            throw new RiceIllegalArgumentException("The version number on the given PeopleFlow definition was null, a version number must be supplied when updating a PeopleFlow.");
        }
        PeopleFlowBo peopleFlowBo = getPeopleFlowBo(peopleFlow.getId());
        if (peopleFlowBo == null) {
            throw new RiceIllegalArgumentException("Failed to locate an existing PeopleFlow definition with the given id of '" + peopleFlow.getId() + "'");
        }
        return peopleFlowBo;
    }

    protected PeopleFlowBo getPeopleFlowBo(String peopleFlowId) {
        if (StringUtils.isBlank(peopleFlowId)) {
            throw new RiceIllegalArgumentException("peopleFlowId was a null or blank value");
        }
        return businessObjectService.findBySinglePrimaryKey(PeopleFlowBo.class, peopleFlowId);
    }

    protected PeopleFlowBo getPeopleFlowBoByName(String namespaceCode, String name) {
        if (StringUtils.isBlank(namespaceCode)) {
            throw new RiceIllegalArgumentException("namespaceCode was a null or blank value");
        }
        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name was a null or blank value");
        }
        Map<String,String> criteria = new HashMap<String,String>();
		criteria.put(KewImplConstants.PropertyConstants.NAMESPACE_CODE, namespaceCode);
        criteria.put(KewImplConstants.PropertyConstants.NAME, name);
		Collection<PeopleFlowBo> peopleFlows = businessObjectService.findMatching(PeopleFlowBo.class, criteria);
        if (CollectionUtils.isEmpty(peopleFlows)) {
            return null;
        } else if (peopleFlows.size() > 1) {
            throw new RiceIllegalStateException("Found more than one PeopleFlow with the given namespace code '" + namespaceCode + "' and name '" + name + "'");
		}
        return peopleFlows.iterator().next();
    }

    protected PeopleFlowBo savePeopleFlow(PeopleFlowBo peopleFlowBo) {
		if ( peopleFlowBo == null ) {
			return null;
		}
        assignResponsibilityIds(peopleFlowBo);
        return businessObjectService.save(peopleFlowBo);
    }

    protected void assignResponsibilityIds(PeopleFlowBo peopleFlowBo) {
        if (CollectionUtils.isNotEmpty(peopleFlowBo.getMembers())) {
            for (PeopleFlowMemberBo memberBo : peopleFlowBo.getMembers()) {
                if (StringUtils.isBlank(memberBo.getResponsibilityId())) {
                    memberBo.setResponsibilityId(responsibilityIdService.getNewResponsibilityId());
                }
                if (CollectionUtils.isNotEmpty(memberBo.getDelegates())) {
                    for (PeopleFlowDelegateBo delegateBo : memberBo.getDelegates()) {
                        if (StringUtils.isBlank(delegateBo.getResponsibilityId())) {
                            delegateBo.setResponsibilityId(responsibilityIdService.getNewResponsibilityId());
                        }
                    }
                }
            }
        }
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public KewTypeRepositoryService getKewTypeRepositoryService() {
        return kewTypeRepositoryService;
    }

    public void setKewTypeRepositoryService(KewTypeRepositoryService kewTypeRepositoryService) {
        this.kewTypeRepositoryService = kewTypeRepositoryService;
    }

    public ResponsibilityIdService getResponsibilityIdService() {
        return responsibilityIdService;
    }

    public void setResponsibilityIdService(ResponsibilityIdService responsibilityIdService) {
        this.responsibilityIdService = responsibilityIdService;
    }
    
}
