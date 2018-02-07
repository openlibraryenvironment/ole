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

import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplateContract;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The mutable implementation of the @{link NaturalLanguageTemplateContract} interface, the counterpart to the immutable implementation {@link NaturalLanguageTemplate}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class NaturalLanguageTemplateBo
    extends PersistableBusinessObjectBase
    implements NaturalLanguageTemplateContract
{

    private Map<String, String> attributes;
    private String languageCode;
    private String naturalLanguageUsageId;
    private String typeId;
    private String template;
    private String id;
    private boolean active;
    private Long versionNumber;
    private SequenceAccessorService sequenceAccessorService;
    private Set<NaturalLanguageTemplateAttributeBo> attributeBos;
    private static KrmsAttributeDefinitionService attributeDefinitionService;
    private static KrmsTypeRepositoryService typeRepositoryService;

    /**
     * Default Constructor
     * 
     */
    public NaturalLanguageTemplateBo() {
    }

    @Override
    public String getLanguageCode() {
        return this.languageCode;
    }

    @Override
    public String getNaturalLanguageUsageId() {
        return this.naturalLanguageUsageId;
    }

    @Override
    public String getTypeId() {
        return this.typeId;
    }

    @Override
    public String getTemplate() {
        return this.template;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    /**
     * Sets the value of languageCode on this builder to the given value.
     * 
     * @param languageCode the languageCode value to set.
     * 
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * Sets the value of naturalLanguageUsageId on this builder to the given value.
     * 
     * @param naturalLanguageUsageId the naturalLanguageUsageId value to set.
     * 
     */
    public void setNaturalLanguageUsageId(String naturalLanguageUsageId) {
        this.naturalLanguageUsageId = naturalLanguageUsageId;
    }

    /**
     * Sets the value of typeId on this builder to the given value.
     * 
     * @param typeId the typeId value to set.
     * 
     */
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    /**
     * Sets the value of template on this builder to the given value.
     * 
     * @param template the template value to set.
     * 
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Sets the value of id on this builder to the given value.
     * 
     * @param id the id value to set.
     * 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the value of active on this builder to the given value.
     * 
     * @param active the active value to set.
     * 
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the value of versionNumber on this builder to the given value.
     * 
     * @param versionNumber the versionNumber value to set.
     * 
     */
    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     * Sets the value of AttributeBos on this builder to the given value.
     * 
     * @param attributeBos the AttributeBos value to set.
     * 
     */
    public void setAttributeBos(List<NaturalLanguageTemplateAttributeBo> attributeBos) {
        this.attributeBos = new HashSet<NaturalLanguageTemplateAttributeBo>(attributeBos);
    }

    /**
     * Sets the value of AttributeBos on this builder to the given value.
     * 
     * @param attributeBos the AttributeBos value to set.
     * 
     */
    public void setAttributeBos(Set<NaturalLanguageTemplateAttributeBo> attributeBos) {
        this.attributeBos = new HashSet<NaturalLanguageTemplateAttributeBo>(attributeBos);
    }

    /**
     * Converts a mutable {@link NaturalLanguageTemplateBo} to its immutable counterpart, {@link NaturalLanguageTemplate}.
     * @param naturalLanguageTemplateBo the mutable business object.
     * @return a {@link NaturalLanguageTemplate} the immutable object.
     * 
     */
    public static NaturalLanguageTemplate to(NaturalLanguageTemplateBo naturalLanguageTemplateBo) {
        if (naturalLanguageTemplateBo == null) { return null; }
        return NaturalLanguageTemplate.Builder.create(naturalLanguageTemplateBo).build();
    }

    /**
     * Converts a immutable {@link NaturalLanguageTemplate} to its mutable {@link NaturalLanguageTemplateBo} counterpart.
     * @param naturalLanguageTemplate the immutable object.
     * @return a {@link NaturalLanguageTemplateBo} the mutable NaturalLanguageTemplateBo.
     * 
     */
    public static org.kuali.rice.krms.impl.repository.NaturalLanguageTemplateBo from(NaturalLanguageTemplate naturalLanguageTemplate) {
        if (naturalLanguageTemplate == null) return null;
        NaturalLanguageTemplateBo naturalLanguageTemplateBo = new NaturalLanguageTemplateBo();
        naturalLanguageTemplateBo.setLanguageCode(naturalLanguageTemplate.getLanguageCode());
        naturalLanguageTemplateBo.setNaturalLanguageUsageId(naturalLanguageTemplate.getNaturalLanguageUsageId());
        naturalLanguageTemplateBo.setTypeId(naturalLanguageTemplate.getTypeId());
        naturalLanguageTemplateBo.setTemplate(naturalLanguageTemplate.getTemplate());
        naturalLanguageTemplateBo.setId(naturalLanguageTemplate.getId());
        naturalLanguageTemplateBo.setActive(naturalLanguageTemplate.isActive());
        naturalLanguageTemplateBo.setVersionNumber(naturalLanguageTemplate.getVersionNumber());
        // TODO collections, etc.
        naturalLanguageTemplateBo.setAttributeBos(buildAttributeBoSet(naturalLanguageTemplate));
        //naturalLanguageTemplateBo.setAttributeBos(buildAttributeBoList(naturalLanguageTemplate));
        return naturalLanguageTemplateBo;
    }

    /**
     * Returns the next available id for the given table and class.
     * @return String the next available id for the given table and class.
     * 
     */
    private String getNewId(String table, Class clazz) {
        if (sequenceAccessorService == null) {
            sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        Long id = sequenceAccessorService.getNextAvailableSequenceNumber(table, clazz);
        return id.toString();
    }

    /**
     * Set the SequenceAccessorService, useful for testing.
     * @param sas SequenceAccessorService to use for getNewId.
     * 
     */
    public void setSequenceAccessorService(SequenceAccessorService sas) {
        sequenceAccessorService = sas;
    }

    public SequenceAccessorService getSequenceAccessorService() {
        return sequenceAccessorService;
    }

    @Override
    public Map<String, String> getAttributes() {
        if (attributeBos == null) return Collections.emptyMap();

        HashMap<String, String> attributes = new HashMap<String, String>(attributeBos.size());
        for (NaturalLanguageTemplateAttributeBo attr: attributeBos) {
            attributes.put(attr.getAttributeDefinition().getName(), attr.getValue());
        }
        return attributes;
    }

    /**
     * TODO
     * 
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributeBos  = new HashSet<NaturalLanguageTemplateAttributeBo>();
        if (!org.apache.commons.lang.StringUtils.isBlank(this.typeId)) {
            List<KrmsAttributeDefinition> attributeDefinitions = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService().findAttributeDefinitionsByType(this.getTypeId());
            Map<String, KrmsAttributeDefinition> attributeDefinitionsByName = new HashMap<String, KrmsAttributeDefinition>(attributeDefinitions.size());
            if (attributeDefinitions != null) for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
                attributeDefinitionsByName.put(attributeDefinition.getName(), attributeDefinition);
            }
            for (Map.Entry<String, String> attr : attributes.entrySet()) {
                KrmsAttributeDefinition attributeDefinition = attributeDefinitionsByName.get(attr.getKey());
                NaturalLanguageTemplateAttributeBo attributeBo = new NaturalLanguageTemplateAttributeBo();
                attributeBo.setNaturalLanguageTemplateId(this.getId());
                attributeBo.setAttributeDefinitionId((attributeDefinition == null) ? null : attributeDefinition.getId());
                attributeBo.setValue(attr.getValue());
                attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attributeDefinition));
                attributeBos.add(attributeBo);
            }
        }
    }

    private static Collection<NaturalLanguageTemplateAttributeBo> buildAttributes(NaturalLanguageTemplate im, Collection<NaturalLanguageTemplateAttributeBo> attributes) {

        KrmsTypeDefinition krmsTypeDefinition = getTypeRepositoryService().getTypeById(im.getTypeId());

        // for each entry, build a NaturalLanguageTemplateAttributeBo and add it
        if (im.getAttributes() != null) {
            for (Map.Entry<String,String> entry  : im.getAttributes().entrySet()) {

                KrmsAttributeDefinition attrDef =
                        getAttributeDefinitionService().getAttributeDefinitionByNameAndNamespace(entry.getKey(),
                                krmsTypeDefinition.getNamespace());

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

    private static Set<NaturalLanguageTemplateAttributeBo> buildAttributeBoSet(NaturalLanguageTemplate im) {
        Set<NaturalLanguageTemplateAttributeBo> attributes = new HashSet<NaturalLanguageTemplateAttributeBo>();
        return (Set)buildAttributes(im, attributes);
    }

    private static List<NaturalLanguageTemplateAttributeBo> buildAttributeBoList(NaturalLanguageTemplate im) {
        List<NaturalLanguageTemplateAttributeBo> attributes = new LinkedList<NaturalLanguageTemplateAttributeBo>();
        return (List)buildAttributes(im, attributes);
    }

    public static void setAttributeDefinitionService(KrmsAttributeDefinitionService attributeDefinitionService) {
        NaturalLanguageTemplateBo.attributeDefinitionService = attributeDefinitionService; // TODO gen
    }

    public static KrmsTypeRepositoryService getTypeRepositoryService() {
        if (typeRepositoryService == null) {
            typeRepositoryService = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
        }
        return typeRepositoryService;
    }

    public static void setTypeRepositoryService(KrmsTypeRepositoryService typeRepositoryService) {
        NaturalLanguageTemplateBo.typeRepositoryService = typeRepositoryService;
    }

    public static KrmsAttributeDefinitionService getAttributeDefinitionService() {
        if (attributeDefinitionService == null) {
            attributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        }
        return attributeDefinitionService;
    }

}
