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
package org.kuali.rice.krms.impl.repository;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageUsage;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the @{link NaturalLanguageUsageBoService} interface for accessing  {@link NaturalLanguageUsageBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class NaturalLanguageUsageBoServiceImpl
    implements NaturalLanguageUsageBoService
{

    private BusinessObjectService businessObjectService;
    private KrmsAttributeDefinitionService attributeDefinitionService;

    /**
     * Sets the value of BusinessObjectService to the given value.
     * 
     * @param businessObjectService the BusinessObjectService value to set.
     * 
     */
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    public void setAttributeDefinitionService(KrmsAttributeDefinitionService attributeDefinitionService) {
        this.attributeDefinitionService = attributeDefinitionService;
    }

    public KrmsAttributeDefinitionService getAttributeDefinitionService() {
        if (attributeDefinitionService == null) {
            attributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        }
        return attributeDefinitionService;
    }

    @Override
    public NaturalLanguageUsage createNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage) {
        incomingParamCheck(naturalLanguageUsage , "naturalLanguageUsage");
        final String naturalLanguageUsageIdKey = naturalLanguageUsage.getId();
        final NaturalLanguageUsage existing = getNaturalLanguageUsage(naturalLanguageUsageIdKey);
        if (existing != null){ throw new IllegalStateException("the NaturalLanguageUsage to create already exists: " + naturalLanguageUsage);	}
        NaturalLanguageUsageBo bo = (NaturalLanguageUsageBo)businessObjectService.save(from(naturalLanguageUsage));
        return NaturalLanguageUsageBo.to(bo);
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsage(String naturalLanguageUsageId) {
        incomingParamCheck(naturalLanguageUsageId , "naturalLanguageUsageId");
        NaturalLanguageUsageBo bo = businessObjectService.findBySinglePrimaryKey(NaturalLanguageUsageBo.class, naturalLanguageUsageId);
        return NaturalLanguageUsageBo.to(bo);
    }

    @Override
    public NaturalLanguageUsage getNaturalLanguageUsageByName(String namespace, String name) {
        if (StringUtils.isBlank(namespace)) {
            throw new RiceIllegalArgumentException("namespace was a null or blank value");
        }
        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name was a null or blank value");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("namespace", namespace);
        map.put("name", name);

        NaturalLanguageUsageBo usageBo = businessObjectService.findByPrimaryKey(NaturalLanguageUsageBo.class, Collections.unmodifiableMap(map));
        return NaturalLanguageUsageBo.to(usageBo);
    }

    @Override
    public void updateNaturalLanguageUsage(NaturalLanguageUsage naturalLanguageUsage) {
        incomingParamCheck(naturalLanguageUsage , "naturalLanguageUsage");
        final NaturalLanguageUsage existing = getNaturalLanguageUsage(naturalLanguageUsage.getId());
        if (existing == null){ throw new IllegalStateException("the NaturalLanguageUsage to update does not exists: " + naturalLanguageUsage);}
        final NaturalLanguageUsage toUpdate;
        if (!existing.getId().equals(naturalLanguageUsage.getId())){
            // if passed in id does not match existing id, correct it
            final NaturalLanguageUsage.Builder builder = NaturalLanguageUsage.Builder.create(naturalLanguageUsage);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        } else {
            toUpdate = naturalLanguageUsage;
        }

        // copy all updateable fields to bo
        NaturalLanguageUsageBo boToUpdate = from(toUpdate);

        // update the rule and create new attributes
         businessObjectService.save(boToUpdate);
    }

    @Override
    public void deleteNaturalLanguageUsage(String naturalLanguageUsageId) {
        incomingParamCheck(naturalLanguageUsageId , "naturalLanguageUsageId");
        final NaturalLanguageUsage existing = getNaturalLanguageUsage(naturalLanguageUsageId);
        if (existing == null){ throw new IllegalStateException("the NaturalLanguageUsage to delete does not exists: " + naturalLanguageUsageId);}
        businessObjectService.delete(from(existing));
    }

    @Override
    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByName(String name) {
        if (org.apache.commons.lang.StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        List<NaturalLanguageUsageBo> bos = (List<NaturalLanguageUsageBo>) businessObjectService.findMatching(NaturalLanguageUsageBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByDescription(String description) {
        if (org.apache.commons.lang.StringUtils.isBlank(description)) {
            throw new IllegalArgumentException("description is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("description", description);
        List<NaturalLanguageUsageBo> bos = (List<NaturalLanguageUsageBo>) businessObjectService.findMatching(NaturalLanguageUsageBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public List<NaturalLanguageUsage> findNaturalLanguageUsagesByNamespace(String namespace) {
        if (org.apache.commons.lang.StringUtils.isBlank(namespace)) {
            throw new IllegalArgumentException("namespace is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("namespace", namespace);
        List<NaturalLanguageUsageBo> bos = (List<NaturalLanguageUsageBo>) businessObjectService.findMatching(NaturalLanguageUsageBo.class, map);
        return convertBosToImmutables(bos);
    }

    public List<NaturalLanguageUsage> convertBosToImmutables(final Collection<NaturalLanguageUsageBo> naturalLanguageUsageBos) {
        List<NaturalLanguageUsage> immutables = new LinkedList<NaturalLanguageUsage>();
        if (naturalLanguageUsageBos != null) {
            NaturalLanguageUsage immutable = null;
            for (NaturalLanguageUsageBo bo : naturalLanguageUsageBos ) {
                immutable = to(bo);
                immutables.add(immutable);
            }
        }
        return Collections.unmodifiableList(immutables);
    }

    @Override
    public NaturalLanguageUsage to(NaturalLanguageUsageBo naturalLanguageUsageBo) {
        return NaturalLanguageUsageBo.to(naturalLanguageUsageBo);
    }

    public NaturalLanguageUsageBo from(NaturalLanguageUsage naturalLanguageUsage) {
        return NaturalLanguageUsageBo.from(naturalLanguageUsage);
    }

    private void incomingParamCheck(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " was null");
        } else if (object instanceof String
                && StringUtils.isBlank((String)object)) {
            throw new IllegalArgumentException(name + " was blank");
        }
    }

}
