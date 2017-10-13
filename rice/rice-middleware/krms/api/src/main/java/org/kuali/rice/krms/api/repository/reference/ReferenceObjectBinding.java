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
package org.kuali.rice.krms.api.repository.reference;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.krms.api.KrmsConstants;

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
 * Generated using JVM arguments -DNOT_BLANK=krmsObjectId,krmsDiscriminatorType,referenceObjectId,referenceDiscriminatorType 
 * Concrete model object implementation, immutable. 
 * Instances can be (un)marshalled to and from XML.
 * 
 * @see ReferenceObjectBindingContract
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = ReferenceObjectBinding.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ReferenceObjectBinding.Constants.TYPE_NAME, propOrder = {
    ReferenceObjectBinding.Elements.COLLECTION_NAME,
    ReferenceObjectBinding.Elements.KRMS_DISCRIMINATOR_TYPE,
    ReferenceObjectBinding.Elements.KRMS_OBJECT_ID,
    ReferenceObjectBinding.Elements.NAMESPACE,
    ReferenceObjectBinding.Elements.REFERENCE_DISCRIMINATOR_TYPE,
    ReferenceObjectBinding.Elements.REFERENCE_OBJECT_ID,
    ReferenceObjectBinding.Elements.ID,
    ReferenceObjectBinding.Elements.ACTIVE,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ReferenceObjectBinding
    extends AbstractDataTransferObject
    implements ReferenceObjectBindingContract
{

    @XmlElement(name = Elements.COLLECTION_NAME, required = false)
    private final String collectionName;
    @XmlElement(name = Elements.KRMS_DISCRIMINATOR_TYPE, required = false)
    private final String krmsDiscriminatorType;
    @XmlElement(name = Elements.KRMS_OBJECT_ID, required = false)
    private final String krmsObjectId;
    @XmlElement(name = Elements.NAMESPACE, required = false)
    private final String namespace;
    @XmlElement(name = Elements.REFERENCE_DISCRIMINATOR_TYPE, required = false)
    private final String referenceDiscriminatorType;
    @XmlElement(name = Elements.REFERENCE_OBJECT_ID, required = false)
    private final String referenceObjectId;
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
    private ReferenceObjectBinding() {
        this.collectionName = null;
        this.krmsDiscriminatorType = null;
        this.krmsObjectId = null;
        this.namespace = null;
        this.referenceDiscriminatorType = null;
        this.referenceObjectId = null;
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
    private ReferenceObjectBinding(Builder builder) {
        this.collectionName = builder.getCollectionName();
        this.krmsDiscriminatorType = builder.getKrmsDiscriminatorType();
        this.krmsObjectId = builder.getKrmsObjectId();
        this.namespace = builder.getNamespace();
        this.referenceDiscriminatorType = builder.getReferenceDiscriminatorType();
        this.referenceObjectId = builder.getReferenceObjectId();
        this.id = builder.getId();
        this.active = builder.isActive();
        this.versionNumber = builder.getVersionNumber();
    }

    @Override
    public String getCollectionName() {
        return this.collectionName;
    }

    @Override
    public String getKrmsDiscriminatorType() {
        return this.krmsDiscriminatorType;
    }

    @Override
    public String getKrmsObjectId() {
        return this.krmsObjectId;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    @Override
    public String getReferenceDiscriminatorType() {
        return this.referenceDiscriminatorType;
    }

    @Override
    public String getReferenceObjectId() {
        return this.referenceObjectId;
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
     * A builder which can be used to construct {@link ReferenceObjectBinding} instances.  Enforces the constraints of the {@link ReferenceObjectBindingContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, ReferenceObjectBindingContract
    {

        private String collectionName;
        private String krmsDiscriminatorType;
        private String krmsObjectId;
        private String namespace;
        private String referenceDiscriminatorType;
        private String referenceObjectId;
        private String id;
        private boolean active;
        private Long versionNumber;

        private Builder(String krmsDiscriminatorType, String krmsObjectId, String namespace, String referenceDiscriminatorType, String referenceObjectId) {
            // TODO modify this constructor as needed to pass any required values and invoke the appropriate 'setter' methods
            setKrmsDiscriminatorType(krmsDiscriminatorType);
            setKrmsObjectId(krmsObjectId);
            setNamespace(namespace);
            setReferenceDiscriminatorType(referenceDiscriminatorType);
            setReferenceObjectId(referenceObjectId);
        }

        public static Builder create(String krmsDiscriminatorType, String krmsObjectId, String namespace, String referenceDiscriminatorType, String referenceObjectId) {
            // TODO modify as needed to pass any required values and add them to the signature of the 'create' method
            return new Builder(krmsDiscriminatorType, krmsObjectId, namespace, referenceDiscriminatorType, referenceObjectId);
        }

        public static Builder create(ReferenceObjectBindingContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            // TODO if create() is modified to accept required parameters, this will need to be modified
            Builder builder = create(contract.getKrmsDiscriminatorType(), contract.getKrmsObjectId(), contract.getNamespace(), contract.getReferenceDiscriminatorType(), contract.getReferenceObjectId());
            builder.setId(contract.getId());
            builder.setActive(contract.isActive());
            builder.setCollectionName(contract.getCollectionName());
            builder.setVersionNumber(contract.getVersionNumber());
            return builder;
        }

        /**
         * Builds an instance of a ReferenceObjectBinding based on the current state of the builder.
         * 
         * @return the fully-constructed ReferenceObjectBinding.
         * 
         */
        public ReferenceObjectBinding build() {
            return new ReferenceObjectBinding(this);
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getCollectionName() {
            return this.collectionName;
        }

        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public String getKrmsDiscriminatorType() {
            return this.krmsDiscriminatorType;
        }

        @Override
        public String getKrmsObjectId() {
            return this.krmsObjectId;
        }

        @Override
        public String getNamespace() {
            return this.namespace;
        }

        @Override
        public String getReferenceDiscriminatorType() {
            return this.referenceDiscriminatorType;
        }

        @Override
        public String getReferenceObjectId() {
            return this.referenceObjectId;
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
         * Sets the value of collectionName on this builder to the given value.
         * 
         * @param collectionName the collectionName value to set.
         * 
         */
        public void setCollectionName(String collectionName) {
            // TODO add validation of input value if required and throw IllegalArgumentException if needed
            this.collectionName = collectionName;
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
         * Sets the value of krmsDiscriminatorType on this builder to the given value.
         * 
         * @param krmsDiscriminatorType the krmsDiscriminatorType value to set., must not be null or blank
         * @throws IllegalArgumentException if the krmsDiscriminatorType is null or blank
         * 
         */
        public void setKrmsDiscriminatorType(String krmsDiscriminatorType) {
            if (org.apache.commons.lang.StringUtils.isBlank(krmsDiscriminatorType)) {
                throw new IllegalArgumentException("krmsDiscriminatorType is null or blank");
            }
            this.krmsDiscriminatorType = krmsDiscriminatorType;
        }

        /**
         * Sets the value of krmsObjectId on this builder to the given value.
         * 
         * @param krmsObjectId the krmsObjectId value to set., must not be null or blank
         * @throws IllegalArgumentException if the krmsObjectId is null or blank
         * 
         */
        public void setKrmsObjectId(String krmsObjectId) {
            if (org.apache.commons.lang.StringUtils.isBlank(krmsObjectId)) {
                throw new IllegalArgumentException("krmsObjectId is null or blank");
            }
            this.krmsObjectId = krmsObjectId;
        }

        /**
         * Sets the value of namespace on this builder to the given value.
         * 
         * @param namespace the namespace value to set., must not be null or blank
         * @throws IllegalArgumentException if the namespace is null or blank
         * 
         */
        public void setNamespace(String namespace) {
            if (org.apache.commons.lang.StringUtils.isBlank(namespace)) {
                throw new IllegalArgumentException("namespace is null or blank");
            }
            this.namespace = namespace;
        }

        /**
         * Sets the value of referenceDiscriminatorType on this builder to the given value.
         * 
         * @param referenceDiscriminatorType the referenceDiscriminatorType value to set., must not be null or blank
         * @throws IllegalArgumentException if the referenceDiscriminatorType is null or blank
         * 
         */
        public void setReferenceDiscriminatorType(String referenceDiscriminatorType) {
            if (org.apache.commons.lang.StringUtils.isBlank(referenceDiscriminatorType)) {
                throw new IllegalArgumentException("referenceDiscriminatorType is null or blank");
            }
            this.referenceDiscriminatorType = referenceDiscriminatorType;
        }

        /**
         * Sets the value of referenceObjectId on this builder to the given value.
         * 
         * @param referenceObjectId the referenceObjectId value to set., must not be null or blank
         * @throws IllegalArgumentException if the referenceObjectId is null or blank
         * 
         */
        public void setReferenceObjectId(String referenceObjectId) {
            if (org.apache.commons.lang.StringUtils.isBlank(referenceObjectId)) {
                throw new IllegalArgumentException("referenceObjectId is null or blank");
            }
            this.referenceObjectId = referenceObjectId;
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

        final static String ROOT_ELEMENT_NAME = "referenceObjectBinding";
        final static String TYPE_NAME = "ReferenceObjectBindingType";

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String COLLECTION_NAME = "collectionName";
        final static String KRMS_DISCRIMINATOR_TYPE = "krmsDiscriminatorType";
        final static String KRMS_OBJECT_ID = "krmsObjectId";
        final static String NAMESPACE = "namespace";
        final static String REFERENCE_DISCRIMINATOR_TYPE = "referenceDiscriminatorType";
        final static String REFERENCE_OBJECT_ID = "referenceObjectId";
        final static String ID = "id";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0 + "/" + ReferenceObjectBinding.Constants.TYPE_NAME;
    }

}
