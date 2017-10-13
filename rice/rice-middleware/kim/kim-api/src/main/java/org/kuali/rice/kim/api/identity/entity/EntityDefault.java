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
package org.kuali.rice.kim.api.identity.entity;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.EntityUtils;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@XmlRootElement(name = EntityDefault.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = EntityDefault.Constants.TYPE_NAME, propOrder = {
    EntityDefault.Elements.ENTITY_ID,
    EntityDefault.Elements.NAME,
    EntityDefault.Elements.PRINCIPALS,
    EntityDefault.Elements.ENTITY_TYPE_CONTACT_INFOS,
    EntityDefault.Elements.AFFILIATIONS,
    EntityDefault.Elements.DEFAULT_AFFILIATION,
    EntityDefault.Elements.EMPLOYMENT,
    EntityDefault.Elements.EXTERNAL_IDENTIFIERS,
    EntityDefault.Elements.PRIVACY_PREFERENCES,
    EntityDefault.Elements.ACTIVE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public class EntityDefault extends AbstractDataTransferObject {
    @XmlElement(name = Elements.ENTITY_ID, required = false)
    private final String entityId;

    @XmlElement(name = Elements.NAME, required = false)
    private final EntityName name;

    @XmlElementWrapper(name = Elements.PRINCIPALS, required = false)
    @XmlElement(name = Elements.PRINCIPAL, required = false)
    private final List<Principal> principals;

    @XmlElementWrapper(name = Elements.ENTITY_TYPE_CONTACT_INFOS, required = false)
    @XmlElement(name = Elements.ENTITY_TYPE_CONTACT_INFO, required = false)
    private final List<EntityTypeContactInfoDefault> entityTypeContactInfos;

    @XmlElementWrapper(name = Elements.AFFILIATIONS, required = false)
    @XmlElement(name = Elements.AFFILIATION, required = false)
    private final List<EntityAffiliation> affiliations;

    @XmlElement(name = Elements.DEFAULT_AFFILIATION, required = false)
    private final EntityAffiliation defaultAffiliation;

    @XmlElement(name = Elements.EMPLOYMENT, required = false)
    private final EntityEmployment employment;

    @XmlElementWrapper(name = Elements.EXTERNAL_IDENTIFIERS, required = false)
    @XmlElement(name = Elements.EXTERNAL_IDENTIFIER, required = false)
    private final List<EntityExternalIdentifier> externalIdentifiers;

    @XmlElement(name = Elements.PRIVACY_PREFERENCES, required = false)
    private final EntityPrivacyPreferences privacyPreferences;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private EntityDefault() {
        entityId = null;
        name = null;
        principals = null;
        affiliations = null;
        defaultAffiliation = null;
        entityTypeContactInfos = null;
        employment = null;
        externalIdentifiers = null;
        privacyPreferences = null;
        active = false;
    }

    public EntityDefault(String entityId, EntityName name, List<Principal> principals,
            List<EntityTypeContactInfoDefault> entityTypes, List<EntityAffiliation> affiliations,
            EntityAffiliation defaultAffiliation, EntityEmployment employment,
            List<EntityExternalIdentifier> externalIdentifiers, EntityPrivacyPreferences privacyPreferences, boolean active) {
        this.entityId = entityId;
        this.name = name;
        this.principals = principals;
        this.entityTypeContactInfos = entityTypes;
        this.affiliations = affiliations;
        this.defaultAffiliation = defaultAffiliation;
        this.employment = employment;
        this.externalIdentifiers = externalIdentifiers;
        this.privacyPreferences = privacyPreferences;
        this.active = active;
    }

    public EntityDefault(Builder builder) {
        this.entityId = builder.entityId;
        this.name = builder.getName() == null ? null : builder.getName().build();
        this.principals = new ArrayList<Principal>();
        if (CollectionUtils.isNotEmpty(builder.getPrincipals())) {
            for (Principal.Builder principal : builder.getPrincipals()) {
                this.principals.add(principal.build());
            }
        }
        this.entityTypeContactInfos = new ArrayList<EntityTypeContactInfoDefault>();
        if (CollectionUtils.isNotEmpty(builder.getEntityTypeContactInfos())) {
            for (EntityTypeContactInfoDefault.Builder entityType : builder.getEntityTypeContactInfos()) {
                this.entityTypeContactInfos.add(entityType.build());
            }
        }
        this.affiliations = new ArrayList<EntityAffiliation>();
        if (CollectionUtils.isNotEmpty(builder.getAffiliations())) {
            for (EntityAffiliation.Builder affiliation : builder.getAffiliations()) {
                this.affiliations.add(affiliation.build());
            }
        }
        if (builder.getDefaultAffiliation() == null
                && CollectionUtils.isNotEmpty(this.affiliations)) {
            this.defaultAffiliation = EntityUtils.getDefaultItem(this.affiliations);
        } else {
            this.defaultAffiliation = builder.getDefaultAffiliation() == null ? null : builder.getDefaultAffiliation().build();
        }
        this.employment = builder.getEmployment() == null ? null : builder.getEmployment().build();
        this.externalIdentifiers = new ArrayList<EntityExternalIdentifier>();
        if (CollectionUtils.isNotEmpty(builder.getExternalIdentifiers())) {
            for (EntityExternalIdentifier.Builder externalId : builder.getExternalIdentifiers()) {
                this.externalIdentifiers.add(externalId.build());
            }
        }
        this.privacyPreferences = builder.getPrivacyPreferences() == null ? null : builder.getPrivacyPreferences().build();
        this.active = builder.isActive();
    }

    public String getEntityId() {
        return entityId;
    }

    public EntityName getName() {
        return name;
    }

    public List<Principal> getPrincipals() {
        return Collections.unmodifiableList(principals);
    }

    public List<EntityTypeContactInfoDefault> getEntityTypeContactInfos() {
        return Collections.unmodifiableList(entityTypeContactInfos);
    }

    public List<EntityAffiliation> getAffiliations() {
        return Collections.unmodifiableList(affiliations);
    }

    public EntityAffiliation getDefaultAffiliation() {
        return defaultAffiliation;
    }

    public EntityEmployment getEmployment() {
        return employment;
    }

    public List<EntityExternalIdentifier> getExternalIdentifiers() {
        return Collections.unmodifiableList(externalIdentifiers);
    }

    public EntityPrivacyPreferences getPrivacyPreferences() {
        return privacyPreferences;
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Gets this {@link EntityDefault}'s {@link EntityTypeContactInfoDefault} for the given type code.
     * @return the {@link org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault} for the given type code for this {@link EntityTypeContactInfoDefault},
     * or null if none has been assigned.
     */
    public EntityTypeContactInfoDefault getEntityType(String entityTypeCode) {
        if (entityTypeContactInfos == null) {
            return null;
        }
        for (EntityTypeContactInfoDefault entType : entityTypeContactInfos) {
            if (entType.getEntityTypeCode().equals(entityTypeCode)) {
                return entType;
            }
        }
        return null;
    }

   
    /**
     * A builder which can be used to construct {@link EntityDefault} instances.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder
    {
        private String entityId;
        private EntityName.Builder name;
        private List<Principal.Builder> principals;
        private List<EntityTypeContactInfoDefault.Builder> entityTypeContactInfos;
        private List<EntityAffiliation.Builder> affiliations;
        private EntityAffiliation.Builder defaultAffiliation;
        private EntityEmployment.Builder employment;
        private List<EntityExternalIdentifier.Builder> externalIdentifiers;
        private EntityPrivacyPreferences.Builder privacyPreferences;
        private boolean active;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(String entityId) {
            Builder builder = new Builder();
            builder.setEntityId(entityId);
            return builder;
        }
        
        public static Builder create(EntityContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = new Builder();
            builder.setEntityId(contract.getId());
            builder.setActive(contract.isActive());
            List<Principal.Builder> principalBuilders = new ArrayList<Principal.Builder>();
            for ( PrincipalContract p : contract.getPrincipals() ) {
              principalBuilders.add( Principal.Builder.create(p) );
            }
            builder.setPrincipals(principalBuilders);
          
            builder.setPrivacyPreferences(contract.getPrivacyPreferences() == null ?
                    EntityPrivacyPreferences.Builder.create(contract.getId()) : EntityPrivacyPreferences.Builder.create(contract.getPrivacyPreferences()));

            builder.setName(contract.getDefaultName() == null ? null : EntityName.Builder.create(contract.getDefaultName()));
            List<EntityTypeContactInfoDefault.Builder> typeBuilders = new ArrayList<EntityTypeContactInfoDefault.Builder>();
            for ( EntityTypeContactInfoContract etContract : contract.getEntityTypeContactInfos() ) {
                typeBuilders.add(EntityTypeContactInfoDefault.Builder.create(etContract));
            }
            builder.setEntityTypeContactInfos(typeBuilders);
          
            List<EntityAffiliation.Builder> affiliationBuilders = new ArrayList<EntityAffiliation.Builder>( );
            for ( EntityAffiliationContract aff : contract.getAffiliations() ) {
                if (aff.isActive()) {
                    affiliationBuilders.add(EntityAffiliation.Builder.create(aff));
                    if ( aff.isDefaultValue() ) {
                      builder.setDefaultAffiliation( EntityAffiliation.Builder.create(aff) );
                    }
                }
            }
            builder.setAffiliations(affiliationBuilders);
          
            builder.setEmployment(contract.getPrimaryEmployment() == null ? null : EntityEmployment.Builder.create(contract.getPrimaryEmployment()) );
            List<EntityExternalIdentifier.Builder> externalIdBuilders = new ArrayList<EntityExternalIdentifier.Builder>();
            for ( EntityExternalIdentifierContract id : contract.getExternalIdentifiers() ) {
                externalIdBuilders.add( EntityExternalIdentifier.Builder.create(id) );
            }
            builder.setExternalIdentifiers( externalIdBuilders );

            return builder;
        }

        public EntityDefault build() {
            return new EntityDefault(this);
        }

        public String getEntityId() {
            return entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        public EntityName.Builder getName() {
            return name;
        }

        public void setName(EntityName.Builder name) {
            this.name = name;
        }

        public List<Principal.Builder> getPrincipals() {
            return principals;
        }

        public void setPrincipals(List<Principal.Builder> principals) {
            this.principals = principals;
        }

        public List<EntityTypeContactInfoDefault.Builder> getEntityTypeContactInfos() {
            return entityTypeContactInfos;
        }

        public void setEntityTypeContactInfos(List<EntityTypeContactInfoDefault.Builder> entityTypeContactInfos) {
            this.entityTypeContactInfos = entityTypeContactInfos;
        }

        public List<EntityAffiliation.Builder> getAffiliations() {
            return affiliations;
        }

        public void setAffiliations(List<EntityAffiliation.Builder> affiliations) {
            this.affiliations = affiliations;
        }

        public EntityAffiliation.Builder getDefaultAffiliation() {
            return defaultAffiliation;
        }

        public void setDefaultAffiliation(EntityAffiliation.Builder defaultAffiliation) {
            this.defaultAffiliation = defaultAffiliation;
        }

        public EntityEmployment.Builder getEmployment() {
            return employment;
        }

        public void setEmployment(EntityEmployment.Builder employment) {
            this.employment = employment;
        }

        public List<EntityExternalIdentifier.Builder> getExternalIdentifiers() {
            return externalIdentifiers;
        }

        public void setExternalIdentifiers(List<EntityExternalIdentifier.Builder> externalIdentifiers) {
            this.externalIdentifiers = externalIdentifiers;
        }

        public EntityPrivacyPreferences.Builder getPrivacyPreferences() {
            return privacyPreferences;
        }

        public void setPrivacyPreferences(EntityPrivacyPreferences.Builder privacyPreferences) {
            this.privacyPreferences = privacyPreferences;
        }

        public boolean isActive() {
            return this.active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
    /**
     * Defines some internal constants used on this class.
     *
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entityDefault";
        final static String TYPE_NAME = "EntityDefaultType";
    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     *
     */
    static class Elements {
        final static String ENTITY_ID = "entityId";
        final static String NAME = "name";
        final static String PRINCIPALS = "principals";
        final static String PRINCIPAL = "principal";
        final static String ENTITY_TYPE_CONTACT_INFOS = "entityTypeContactInfos";
        final static String ENTITY_TYPE_CONTACT_INFO = "entityTypeContactInfo";
        final static String AFFILIATIONS = "affiliations";
        final static String AFFILIATION = "affiliation";
        final static String DEFAULT_AFFILIATION = "defaultAffiliation";
        final static String EMPLOYMENT = "employment";
        final static String EXTERNAL_IDENTIFIERS = "externalIdentifiers";
        final static String EXTERNAL_IDENTIFIER = "externalIdentifier";
        final static String PRIVACY_PREFERENCES = "privacyPreferences";
        final static String ACTIVE = "active";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + EntityDefault.Constants.TYPE_NAME;
    }
}
