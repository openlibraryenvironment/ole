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
package org.kuali.rice.krms.api.repository.typerelation;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Collection;

/**
 * Generated using JVM arguments -DENUM=RelationshipType:UNKNOWN:USAGE_ALLOWED -DNOT_BLANK=fromTypeId,toTypeId,sequenceNumber,relationshipType -DFOREIGN_KEY=fromTypeId:org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition,toTypeId:org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition 
 * Concrete model object implementation, immutable. 
 * Instances can be (un)marshalled to and from XML.
 * 
 * @see TypeTypeRelationContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = TypeTypeRelation.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = TypeTypeRelation.Constants.TYPE_NAME, propOrder = {
    TypeTypeRelation.Elements.FROM_TYPE_ID,
    TypeTypeRelation.Elements.TO_TYPE_ID,
    TypeTypeRelation.Elements.RELATIONSHIP_TYPE,
    TypeTypeRelation.Elements.SEQUENCE_NUMBER,
    TypeTypeRelation.Elements.ID,
    TypeTypeRelation.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class TypeTypeRelation
    extends AbstractDataTransferObject
    implements TypeTypeRelationContract
{

    @XmlElement(name = Elements.FROM_TYPE_ID, required = false)
    private final String fromTypeId;
    @XmlElement(name = Elements.TO_TYPE_ID, required = false)
    private final String toTypeId;
    @XmlElement(name = Elements.RELATIONSHIP_TYPE, required = false)
    private final RelationshipType relationshipType;
    @XmlElement(name = Elements.SEQUENCE_NUMBER, required = false)
    private final Integer sequenceNumber;
    @XmlElement(name = Elements.ID, required = false)
    private final String id;
    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;
    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB. This constructor should never be called.
     * It is only present for use during JAXB unmarshalling.
     * 
     */
    private TypeTypeRelation() {
        this.fromTypeId = null;
        this.toTypeId = null;
        this.relationshipType = null;
        this.sequenceNumber = null;
        this.id = null;
        this.active = false;
        this.versionNumber = null;
    }

    /**
     * Constructs an object from the given builder.  This constructor is private and should only ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the object.
     * 
     */
    private TypeTypeRelation(Builder builder) {
        this.fromTypeId = builder.getFromTypeId();
        this.toTypeId = builder.getToTypeId();
        this.relationshipType = builder.getRelationshipType();
        this.sequenceNumber = builder.getSequenceNumber();
        this.id = builder.getId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
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
     * A builder which can be used to construct {@link TypeTypeRelation} instances.  Enforces the constraints of the {@link TypeTypeRelationContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, TypeTypeRelationContract
    {

        private String fromTypeId;
        private String toTypeId;
        private RelationshipType relationshipType;
        private Integer sequenceNumber;
        private String id;
        private boolean active;
        private Long versionNumber;

        private Builder(String fromTypeId, RelationshipType relationshipType, Integer sequenceNumber, String toTypeId) {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
            setFromTypeId(fromTypeId);
            setRelationshipType(relationshipType);
            setSequenceNumber(sequenceNumber);
            setToTypeId(toTypeId);
        }

        public static Builder create(String fromTypeId, RelationshipType relationshipType, Integer sequenceNumber, String toTypeId) {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder(fromTypeId, relationshipType, sequenceNumber, toTypeId);
        }

        public static Builder create(TypeTypeRelationContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getFromTypeId(), contract.getRelationshipType(), contract.getSequenceNumber(), contract.getToTypeId());
            builder.setId(contract.getId());
            builder.setActive(contract.isActive());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        /**
         * Builds an instance of a TypeTypeRelation based on the current state of the builder.
         * 
         * @return the fully-constructed TypeTypeRelation.
         * 
         */
        public TypeTypeRelation build() {
            return new TypeTypeRelation(this);
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getFromTypeId() {
            return this.fromTypeId;
        }

        @Override
        public String getId() {
            return this.id;
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
        public String getToTypeId() {
            return this.toTypeId;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
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
         * Sets the value of fromTypeId on this builder to the given value.
         * 
         * @param fromTypeId the fromTypeId value to set., must not be null or blank
         * @throws IllegalArgumentException if the fromTypeId is null or blank
         * 
         */
        public void setFromTypeId(String fromTypeId) {
            if (org.apache.commons.lang.StringUtils.isBlank(fromTypeId)) {
                throw new IllegalArgumentException("fromTypeId is null or blank");
            }
            this.fromTypeId = fromTypeId;
        }

        /**
         * Sets the value of id on this builder to the given value.
         * 
         * @param id the id value to set., may be null, representing the Object has not been persisted, but must not be blank.
         * @throws IllegalArgumentException if the id is blank
         * 
         */
        public void setId(String id) {
            if (id != null && org.apache.commons.lang.StringUtils.isBlank(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

        /**
         * Sets the value of relationshipType on this builder to the given value.
         * 
         * @param relationshipType the relationshipType value to set., must not be null
         * @throws IllegalArgumentException if the relationshipType is null
         * 
         */
        public void setRelationshipType(RelationshipType relationshipType) {
            if (relationshipType == null) {
                throw new IllegalArgumentException("relationshipType is null");
            }
            this.relationshipType = relationshipType;
        }

        /**
         * Sets the value of sequenceNumber on this builder to the given value.
         * 
         * @param sequenceNumber the sequenceNumber value to set., must not be null
         * @throws IllegalArgumentException if the sequenceNumber is null
         * 
         */
        public void setSequenceNumber(Integer sequenceNumber) {
            if (sequenceNumber == null) {
                throw new IllegalArgumentException("sequenceNumber is null");
            }
            this.sequenceNumber = sequenceNumber;
        }

        /**
         * Sets the value of toTypeId on this builder to the given value.
         * 
         * @param toTypeId the toTypeId value to set., must not be null or blank
         * @throws IllegalArgumentException if the toTypeId is null or blank
         * 
         */
        public void setToTypeId(String toTypeId) {
            if (org.apache.commons.lang.StringUtils.isBlank(toTypeId)) {
                throw new IllegalArgumentException("toTypeId is null or blank");
            }
            this.toTypeId = toTypeId;
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

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "typeTypeRelation";
        final static String TYPE_NAME = "TypeTypeRelationType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String FROM_TYPE_ID = "fromTypeId";
        final static String TO_TYPE_ID = "toTypeId";
        final static String RELATIONSHIP_TYPE = "relationshipType";
        final static String SEQUENCE_NUMBER = "sequenceNumber";
        final static String ID = "id";
        final static String ACTIVE = "active";

    }

}
