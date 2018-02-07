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
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.impl.util.KrmsImplConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.impl.repository.language.SimpleNaturalLanguageTemplater;

/**
 * Implementation of the @{link NaturalLanguageTemplateBoService} interface for accessing  {@link NaturalLanguageTemplateBo} related business objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class NaturalLanguageTemplateBoServiceImpl
    implements NaturalLanguageTemplateBoService
{

    private BusinessObjectService businessObjectService;
    private KrmsAttributeDefinitionService attributeDefinitionService;
    private NaturalLanguageTemplaterContract naturalLanguageTemplater = new SimpleNaturalLanguageTemplater ();

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

    public NaturalLanguageTemplaterContract getNaturalLanguageTemplater() {
        return naturalLanguageTemplater;
    }

    public void setNaturalLanguageTemplater(NaturalLanguageTemplaterContract naturalLanguageTemplater) {
        this.naturalLanguageTemplater = naturalLanguageTemplater;
    }

    @Override
    public NaturalLanguageTemplate createNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate) {
        incomingParamCheck(naturalLanguageTemplate , "naturalLanguageTemplate");
        final String naturalLanguageTemplateIdKey = naturalLanguageTemplate.getId();
        final NaturalLanguageTemplate existing = getNaturalLanguageTemplate(naturalLanguageTemplateIdKey);
        if (existing != null){ throw new IllegalStateException("the NaturalLanguageTemplate to create already exists: " + naturalLanguageTemplate);	}
        NaturalLanguageTemplateBo bo = (NaturalLanguageTemplateBo)businessObjectService.save(from(naturalLanguageTemplate));
        return NaturalLanguageTemplateBo.to(bo);
    }

    @Override
    public NaturalLanguageTemplate getNaturalLanguageTemplate(String naturalLanguageTemplateId) {
        incomingParamCheck(naturalLanguageTemplateId , "naturalLanguageTemplateId");
        NaturalLanguageTemplateBo bo = businessObjectService.findBySinglePrimaryKey(NaturalLanguageTemplateBo.class, naturalLanguageTemplateId);
        return NaturalLanguageTemplateBo.to(bo);
    }

    @Override
    public void updateNaturalLanguageTemplate(NaturalLanguageTemplate naturalLanguageTemplate) {
        incomingParamCheck(naturalLanguageTemplate , "naturalLanguageTemplate");
        final NaturalLanguageTemplate existing = getNaturalLanguageTemplate(naturalLanguageTemplate.getId());
        if (existing == null){ throw new IllegalStateException("the NaturalLanguageTemplate to update does not exists: " + naturalLanguageTemplate);}
        final NaturalLanguageTemplate toUpdate;
        if (!existing.getId().equals(naturalLanguageTemplate.getId())){
            // if passed in id does not match existing id, correct it
            final NaturalLanguageTemplate.Builder builder = NaturalLanguageTemplate.Builder.create(naturalLanguageTemplate);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        } else {
            toUpdate = naturalLanguageTemplate;
        }

        // copy all updateable fields to bo
        NaturalLanguageTemplateBo boToUpdate = from(toUpdate);

        // delete any old, existing attributes
        Map<String,String> fields = new HashMap<String,String>(1);
        fields.put(KrmsImplConstants.PropertyNames.NaturalLanguageTemplate.NATURAL_LANGUAGE_TEMPLATE_ID, toUpdate.getId()); // TODO verify PropertyNames.NaturalLanguageTemplate.NATURAL_LANGUAGE_TEMPLATE_ID
        businessObjectService.deleteMatching(NaturalLanguageTemplateAttributeBo.class, fields);

        // update the rule and create new attributes
         businessObjectService.save(boToUpdate);
    }

    @Override
    public void deleteNaturalLanguageTemplate(String naturalLanguageTemplateId) {
        incomingParamCheck(naturalLanguageTemplateId , "naturalLanguageTemplateId");
        final NaturalLanguageTemplate existing = getNaturalLanguageTemplate(naturalLanguageTemplateId);
        if (existing == null){ throw new IllegalStateException("the NaturalLanguageTemplate to delete does not exists: " + naturalLanguageTemplateId);}

        // delete any existing attributes
        Map<String,String> fields = new HashMap<String,String>(1);
        fields.put(KrmsImplConstants.PropertyNames.NaturalLanguageTemplate.NATURAL_LANGUAGE_TEMPLATE_ID,existing.getId());
        businessObjectService.deleteMatching(NaturalLanguageTemplateAttributeBo.class, fields);

        businessObjectService.delete(from(existing));
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByAttributes(Map attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes is null");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("attributes", attributes);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByLanguageCode(String languageCode) {
        if (org.apache.commons.lang.StringUtils.isBlank(languageCode)) {
            throw new IllegalArgumentException("languageCode is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("languageCode", languageCode);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public NaturalLanguageTemplate findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(String languageCode,
            String typeId,
            String naturalLanguageUsageId) {
        if (org.apache.commons.lang.StringUtils.isBlank(languageCode)) {
            throw new IllegalArgumentException("languageCode is null or blank");
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("languageCode", languageCode);
        map.put("naturalLanguageUsageId", naturalLanguageUsageId);
        map.put("typeId", typeId);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);

        if (bos.isEmpty()) {
            return null;
        }

        if (bos.size() > 1) {
            throw new RiceIllegalArgumentException (languageCode + typeId +  naturalLanguageUsageId + " is supposed to be unique");
        }

        return convertBosToImmutables(bos).get(0);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByNaturalLanguageUsage(String naturalLanguageUsageId) {
        if (org.apache.commons.lang.StringUtils.isBlank(naturalLanguageUsageId)) {
            throw new IllegalArgumentException("naturalLanguageUsageId is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("naturalLanguageUsageId", naturalLanguageUsageId);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByType(String typeId) {
        if (org.apache.commons.lang.StringUtils.isBlank(typeId)) {
            throw new IllegalArgumentException("typeId is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);
        return convertBosToImmutables(bos);
    }

    @Override
    public List<NaturalLanguageTemplate> findNaturalLanguageTemplatesByTemplate(String template) {
        if (org.apache.commons.lang.StringUtils.isBlank(template)) {
            throw new IllegalArgumentException("template is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("template", template);
        List<NaturalLanguageTemplateBo> bos = (List<NaturalLanguageTemplateBo>) businessObjectService.findMatching(NaturalLanguageTemplateBo.class, map);
        return convertBosToImmutables(bos);
    }

    public List<NaturalLanguageTemplate> convertBosToImmutables(final Collection<NaturalLanguageTemplateBo> naturalLanguageTemplateBos) {
        List<NaturalLanguageTemplate> immutables = new LinkedList<NaturalLanguageTemplate>();
        if (naturalLanguageTemplateBos != null) {
            NaturalLanguageTemplate immutable = null;
            for (NaturalLanguageTemplateBo bo : naturalLanguageTemplateBos ) {
                immutable = to(bo);
                immutables.add(immutable);
            }
        }
        return Collections.unmodifiableList(immutables);
    }

    @Override
    public NaturalLanguageTemplate to(NaturalLanguageTemplateBo naturalLanguageTemplateBo) {
        return NaturalLanguageTemplateBo.to(naturalLanguageTemplateBo);
    }

    public NaturalLanguageTemplateBo from(NaturalLanguageTemplate naturalLanguageTemplate) {
        return NaturalLanguageTemplateBo.from(naturalLanguageTemplate);
    }

    private Collection<NaturalLanguageTemplateAttributeBo> buildAttributes(NaturalLanguageTemplate im, Collection<NaturalLanguageTemplateAttributeBo> attributes) {
        // build a map from attribute name to definition
        Map<String, KrmsAttributeDefinition> attributeDefinitionMap = new HashMap<String, KrmsAttributeDefinition>();

        List<KrmsAttributeDefinition> attributeDefinitions = getAttributeDefinitionService().findAttributeDefinitionsByType(im.getTypeId());

        for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
            attributeDefinitionMap.put(attributeDefinition.getName(), attributeDefinition);
        }

        // for each entry, build a NaturalLanguageTemplateAttributeBo and add it
        if (im.getAttributes() != null) {
            for (Map.Entry<String,String> entry  : im.getAttributes().entrySet()) {
                KrmsAttributeDefinition attrDef = attributeDefinitionMap.get(entry.getKey());

                if (attrDef != null) {
                    NaturalLanguageTemplateAttributeBo attributeBo = new NaturalLanguageTemplateAttributeBo();
                    attributeBo.setNaturalLanguageTemplateId( im.getId() );
                    attributeBo.setAttributeDefinitionId(attrDef.getId());
                    attributeBo.setValue(entry.getValue());
                    attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attrDef));
                    attributes.add(attributeBo);
                } else {
                    throw new RiceIllegalStateException("there is no attribute definition with the name '" +
                                 entry.getKey() + "' that is valid for the naturalLanguageTemplate type with id = '" + im.getTypeId() +"'");
                }
            }
        }
        return attributes;
    }

    private Set<NaturalLanguageTemplateAttributeBo> buildAttributeBoSet(NaturalLanguageTemplate im) {
        Set<NaturalLanguageTemplateAttributeBo> attributes = new HashSet<NaturalLanguageTemplateAttributeBo>();
        return (Set)buildAttributes(im, attributes);
    }

    private List<NaturalLanguageTemplateAttributeBo> buildAttributeBoList(NaturalLanguageTemplate im) {
        List<NaturalLanguageTemplateAttributeBo> attributes = new LinkedList<NaturalLanguageTemplateAttributeBo>();
        return (List)buildAttributes(im, attributes);
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
