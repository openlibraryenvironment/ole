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

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplateAttribute;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplateAttributeContract;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinitionContract;

/**
 * The mutable implementation of the @{link NaturalLanguageTemplateAttributeContract} interface, the counterpart to the immutable implementation {@link NaturalLanguageTemplateAttribute}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class NaturalLanguageTemplateAttributeBo
    extends PersistableBusinessObjectBase
    implements NaturalLanguageTemplateAttributeContract
{

    private String naturalLanguageTemplateId;
    private Long versionNumber;
    private String value;
    private String attributeDefinitionId;
    private KrmsAttributeDefinitionContract attributeDefinition;
    private String id;
    private SequenceAccessorService sequenceAccessorService;

    /**
     * Default Constructor
     * 
     */
    public NaturalLanguageTemplateAttributeBo() {
    }

    @Override
    public String getNaturalLanguageTemplateId() {
        return this.naturalLanguageTemplateId;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String getAttributeDefinitionId() {
        return this.attributeDefinitionId;
    }

    @Override
    public KrmsAttributeDefinitionContract getAttributeDefinition() {
        return this.attributeDefinition;
    }

    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Sets the value of naturalLanguageTemplateId on this builder to the given value.
     * 
     * @param naturalLanguageTemplateId the naturalLanguageTemplateId value to set.
     * 
     */
    public void setNaturalLanguageTemplateId(String naturalLanguageTemplateId) {
        this.naturalLanguageTemplateId = naturalLanguageTemplateId;
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
     * Sets the value of value on this builder to the given value.
     * 
     * @param value the value value to set.
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Sets the value of attributeDefinitionId on this builder to the given value.
     * 
     * @param attributeDefinitionId the attributeDefinitionId value to set.
     * 
     */
    public void setAttributeDefinitionId(String attributeDefinitionId) {
        this.attributeDefinitionId = attributeDefinitionId;
    }

    /**
     * Sets the value of attributeDefinition on this builder to the given value.
     * 
     * @param attributeDefinition the attributeDefinition value to set.
     * 
     */
    public void setAttributeDefinition(KrmsAttributeDefinitionContract attributeDefinition) {
        this.attributeDefinition = attributeDefinition;
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
     * Converts a mutable {@link NaturalLanguageTemplateAttributeBo} to its immutable counterpart, {@link NaturalLanguageTemplateAttribute}.
     * @param naturalLanguageTemplateAttributeBo the mutable business object.
     * @return a {@link NaturalLanguageTemplateAttribute} the immutable object.
     * 
     */
    public static NaturalLanguageTemplateAttribute to(NaturalLanguageTemplateAttributeBo naturalLanguageTemplateAttributeBo) {
        if (naturalLanguageTemplateAttributeBo == null) { return null; }
        return NaturalLanguageTemplateAttribute.Builder.create(naturalLanguageTemplateAttributeBo).build();
    }

    /**
     * Converts a immutable {@link NaturalLanguageTemplateAttribute} to its mutable {@link NaturalLanguageTemplateAttributeBo} counterpart.
     * @param naturalLanguageTemplateAttribute the immutable object.
     * @return a {@link NaturalLanguageTemplateAttributeBo} the mutable NaturalLanguageTemplateAttributeBo.
     * 
     */
    public static org.kuali.rice.krms.impl.repository.NaturalLanguageTemplateAttributeBo from(NaturalLanguageTemplateAttribute naturalLanguageTemplateAttribute) {
        if (naturalLanguageTemplateAttribute == null) return null;
        NaturalLanguageTemplateAttributeBo naturalLanguageTemplateAttributeBo = new NaturalLanguageTemplateAttributeBo();
        naturalLanguageTemplateAttributeBo.setNaturalLanguageTemplateId(naturalLanguageTemplateAttribute.getNaturalLanguageTemplateId());
        naturalLanguageTemplateAttributeBo.setVersionNumber(naturalLanguageTemplateAttribute.getVersionNumber());
        naturalLanguageTemplateAttributeBo.setValue(naturalLanguageTemplateAttribute.getValue());
        naturalLanguageTemplateAttributeBo.setAttributeDefinitionId(naturalLanguageTemplateAttribute.getAttributeDefinitionId());
        naturalLanguageTemplateAttributeBo.setAttributeDefinition(naturalLanguageTemplateAttribute.getAttributeDefinition());
        naturalLanguageTemplateAttributeBo.setId(naturalLanguageTemplateAttribute.getId());
        // TODO collections, etc.
        return naturalLanguageTemplateAttributeBo;
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

}
