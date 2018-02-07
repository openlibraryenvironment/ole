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
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelationContract;

/**
 * The mutable implementation of the @{link TypeTypeRelationContract} interface, the counterpart to the immutable implementation {@link TypeTypeRelation}.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class TypeTypeRelationBo
    extends PersistableBusinessObjectBase
    implements TypeTypeRelationContract
{

    private String fromTypeId;
    private String toTypeId;
    private RelationshipType relationshipType;
    private Integer sequenceNumber;
    private String id;
    private boolean active;
    private Long versionNumber;
    private SequenceAccessorService sequenceAccessorService;

    private KrmsTypeBo fromType;
    private KrmsTypeBo toType;



    /**
     * Default Constructor
     * 
     */
    public TypeTypeRelationBo() {
    }

    @Override
    public String getFromTypeId() {
        return this.fromTypeId;
    }

    @Override
    public String getToTypeId() {
        return this.toTypeId;
    }

    @Override
    public RelationshipType getRelationshipType() {
        return this.relationshipType;
    }

    @Override
    public Integer getSequenceNumber() {
        return this.sequenceNumber;
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
     * Sets the value of fromTypeId on this builder to the given value.
     * 
     * @param fromTypeId the fromTypeId value to set.
     * 
     */
    public void setFromTypeId(String fromTypeId) {
        this.fromTypeId = fromTypeId;
    }

    /**
     * Sets the value of toTypeId on this builder to the given value.
     * 
     * @param toTypeId the toTypeId value to set.
     * 
     */
    public void setToTypeId(String toTypeId) {
        this.toTypeId = toTypeId;
    }

    /**
     * Sets the value of relationshipType on this builder to the given value.
     * 
     * @param relationshipType the relationshipType value to set.
     * 
     */
    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * Sets the value of sequenceNumber on this builder to the given value.
     * 
     * @param sequenceNumber the sequenceNumber value to set.
     * 
     */
    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
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
     * Converts a mutable {@link TypeTypeRelationBo} to its immutable counterpart, {@link TypeTypeRelation}.
     * @param typeTypeRelationBo the mutable business object.
     * @return a {@link TypeTypeRelation} the immutable object.
     * 
     */
    public static TypeTypeRelation to(TypeTypeRelationBo typeTypeRelationBo) {
        if (typeTypeRelationBo == null) { return null; }
        return TypeTypeRelation.Builder.create(typeTypeRelationBo).build();
    }

    /**
     * Converts a immutable {@link TypeTypeRelation} to its mutable {@link TypeTypeRelationBo} counterpart.
     * @param typeTypeRelation the immutable object.
     * @return a {@link TypeTypeRelationBo} the mutable TypeTypeRelationBo.
     * 
     */
    public static org.kuali.rice.krms.impl.repository.TypeTypeRelationBo from(TypeTypeRelation typeTypeRelation) {
        if (typeTypeRelation == null) return null;
        TypeTypeRelationBo typeTypeRelationBo = new TypeTypeRelationBo();
        typeTypeRelationBo.setFromTypeId(typeTypeRelation.getFromTypeId());
        typeTypeRelationBo.setToTypeId(typeTypeRelation.getToTypeId());
        typeTypeRelationBo.setRelationshipType(typeTypeRelation.getRelationshipType());
        typeTypeRelationBo.setSequenceNumber(typeTypeRelation.getSequenceNumber());
        typeTypeRelationBo.setId(typeTypeRelation.getId());
        typeTypeRelationBo.setActive(typeTypeRelation.isActive());
        typeTypeRelationBo.setVersionNumber(typeTypeRelation.getVersionNumber());
        // TODO collections, etc.
        return typeTypeRelationBo;
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

    public KrmsTypeBo getFromType() {
        return fromType;
    }

    public void setFromType(KrmsTypeBo fromType) {
        this.fromType = fromType;
    }

    public KrmsTypeBo getToType() {
        return toType;
    }

    public void setToType(KrmsTypeBo toType) {
        this.toType = toType;
    }

}
