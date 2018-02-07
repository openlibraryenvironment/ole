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
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmployment;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicityContract;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.residency.EntityResidency;
import org.kuali.rice.kim.api.identity.residency.EntityResidencyContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract;
import org.kuali.rice.kim.api.identity.visa.EntityVisa;
import org.kuali.rice.kim.api.identity.visa.EntityVisaContract;
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
import java.util.List;

@XmlRootElement(name = Entity.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = Entity.Constants.TYPE_NAME, propOrder = {
    Entity.Elements.ID,
    Entity.Elements.PRINCIPALS,
    Entity.Elements.ENTITY_TYPE_CONTACT_INFOS,
    Entity.Elements.EXTERNAL_IDENTIFIERS,
    Entity.Elements.AFFILIATIONS,
    Entity.Elements.NAMES,
    Entity.Elements.EMPLOYMENT_INFORMATION,
    Entity.Elements.PRIVACY_PREFERENCES,
    Entity.Elements.BIO_DEMOGRAPHICS,
    Entity.Elements.CITIZENSHIPS,
    Entity.Elements.PRIMARY_EMPLOYMENT,
    Entity.Elements.DEFAULT_AFFILIATION,
    Entity.Elements.DEFAULT_NAME,
    Entity.Elements.ETHNICITIES,
    Entity.Elements.RESIDENCIES,
    Entity.Elements.VISAS,
    CoreConstants.CommonElements.VERSION_NUMBER,
    CoreConstants.CommonElements.OBJECT_ID,
    Entity.Elements.ACTIVE,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class Entity extends AbstractDataTransferObject
    implements EntityContract
{
    @XmlElement(name = Elements.ID, required = false)
    private final String id;

    @XmlElementWrapper(name = Elements.PRINCIPALS, required = false)
    @XmlElement(name = Elements.PRINCIPAL, required = false)
    private final List<Principal> principals;

    @XmlElementWrapper(name = Elements.ENTITY_TYPE_CONTACT_INFOS, required = false)
    @XmlElement(name = Elements.ENTITY_TYPE_CONTACT_INFO, required = false)
    private final List<EntityTypeContactInfo> entityTypeContactInfos;

    @XmlElementWrapper(name = Elements.EXTERNAL_IDENTIFIERS, required = false)
    @XmlElement(name = Elements.EXTERNAL_IDENTIFIER, required = false)
    private final List<EntityExternalIdentifier> externalIdentifiers;

    @XmlElementWrapper(name = Elements.AFFILIATIONS, required = false)
    @XmlElement(name = Elements.AFFILIATION, required = false)
    private final List<EntityAffiliation> affiliations;

    @XmlElementWrapper(name = Elements.NAMES, required = false)
    @XmlElement(name = Elements.NAME, required = false)
    private final List<EntityName> names;

    @XmlElementWrapper(name = Elements.EMPLOYMENT_INFORMATION, required = false)
    @XmlElement(name = Elements.EMPLOYMENT, required = false)
    private final List<EntityEmployment> employmentInformation;

    @XmlElement(name = Elements.PRIVACY_PREFERENCES, required = false)
    private final EntityPrivacyPreferences privacyPreferences;

    @XmlElement(name = Elements.BIO_DEMOGRAPHICS, required = false)
    private final EntityBioDemographics bioDemographics;

    @XmlElementWrapper(name = Elements.CITIZENSHIPS, required = false)
    @XmlElement(name = Elements.CITIZENSHIP, required = false)
    private final List<EntityCitizenship> citizenships;

    @XmlElement(name = Elements.PRIMARY_EMPLOYMENT, required = false)
    private final EntityEmployment primaryEmployment;

    @XmlElement(name = Elements.DEFAULT_AFFILIATION, required = false)
    private final EntityAffiliation defaultAffiliation;

    @XmlElement(name = Elements.DEFAULT_NAME, required = false)
    private final EntityName defaultName;

    @XmlElementWrapper(name = Elements.ETHNICITIES, required = false)
    @XmlElement(name = Elements.ETHNICITY, required = false)
    private final List<EntityEthnicity> ethnicities;

    @XmlElementWrapper(name = Elements.RESIDENCIES, required = false)
    @XmlElement(name = Elements.RESIDENCY, required = false)
    private final List<EntityResidency> residencies;

    @XmlElementWrapper(name = Elements.VISAS, required = false)
    @XmlElement(name = Elements.VISA, required = false)
    private final List<EntityVisa> visas;

    @XmlElement(name = CoreConstants.CommonElements.VERSION_NUMBER, required = false)
    private final Long versionNumber;

    @XmlElement(name = CoreConstants.CommonElements.OBJECT_ID, required = false)
    private final String objectId;

    @XmlElement(name = Elements.ACTIVE, required = false)
    private final boolean active;

    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    /**
     * Private constructor used only by JAXB.
     * 
     */
    private Entity() {
        this.principals = null;
        this.entityTypeContactInfos = null;
        this.externalIdentifiers = null;
        this.affiliations = null;
        this.names = null;
        this.employmentInformation = null;
        this.privacyPreferences = null;
        this.bioDemographics = null;
        this.citizenships = null;
        this.primaryEmployment = null;
        this.defaultAffiliation = null;
        this.defaultName = null;
        this.ethnicities = null;
        this.residencies = null;
        this.visas = null;
        this.versionNumber = null;
        this.objectId = null;
        this.active = false;
        this.id = null;
    }

    private Entity(Builder builder) {
        this.principals = new ArrayList<Principal>();
        if (CollectionUtils.isNotEmpty(builder.getPrincipals())) {
            for (Principal.Builder principal : builder.getPrincipals()) {
                this.principals.add(principal.build());
            }
        }
        this.entityTypeContactInfos = new ArrayList<EntityTypeContactInfo>();
        if (CollectionUtils.isNotEmpty(builder.getEntityTypeContactInfos())) {
            for (EntityTypeContactInfo.Builder entityTypeData : builder.getEntityTypeContactInfos()) {
                this.entityTypeContactInfos.add(entityTypeData.build());
            }
        }
        this.externalIdentifiers = new ArrayList<EntityExternalIdentifier>();
        if (CollectionUtils.isNotEmpty(builder.getExternalIdentifiers())) {
            for (EntityExternalIdentifier.Builder externalId : builder.getExternalIdentifiers()) {
                this.externalIdentifiers.add(externalId.build());
            }
        }
        this.affiliations = new ArrayList<EntityAffiliation>();
        if (CollectionUtils.isNotEmpty(builder.getAffiliations())) {
            for (EntityAffiliation.Builder affiliation : builder.getAffiliations()) {
                this.affiliations.add(affiliation.build());
            }
        }
        this.names = new ArrayList<EntityName>();
        if (CollectionUtils.isNotEmpty(builder.getNames())) {
            for (EntityName.Builder name : builder.getNames()) {
                this.names.add(name.build());
            }
        }
        this.employmentInformation = new ArrayList<EntityEmployment>();
        if (CollectionUtils.isNotEmpty(builder.getEmploymentInformation())) {
            for (EntityEmployment.Builder employment : builder.getEmploymentInformation()) {
                this.employmentInformation.add(employment.build());
            }
        }
        this.privacyPreferences = builder.getPrivacyPreferences() == null ? null : builder.getPrivacyPreferences().build();
        this.bioDemographics = builder.getBioDemographics() == null ? null : builder.getBioDemographics().build();
        this.citizenships = new ArrayList<EntityCitizenship>();
        if (CollectionUtils.isNotEmpty(builder.getCitizenships())) {
            for (EntityCitizenship.Builder citizenship : builder.getCitizenships()) {
                this.citizenships.add(citizenship.build());
            }
        }
        this.primaryEmployment = builder.getPrimaryEmployment() == null ? null : builder.getPrimaryEmployment().build();
        this.defaultAffiliation = builder.getDefaultAffiliation() == null ? null : builder.getDefaultAffiliation().build();
        this.defaultName = builder.getDefaultName() == null ? null : builder.getDefaultName().build();
        this.ethnicities = new ArrayList<EntityEthnicity>();
        if (CollectionUtils.isNotEmpty(builder.getEthnicities())) {
            for (EntityEthnicity.Builder ethnicity : builder.getEthnicities()) {
                this.ethnicities.add(ethnicity.build());
            }
        }
        this.residencies = new ArrayList<EntityResidency>();
        if (CollectionUtils.isNotEmpty(builder.getResidencies())) {
            for (EntityResidency.Builder residency : builder.getResidencies()) {
                this.residencies.add(residency.build());
            }
        }
        this.visas = new ArrayList<EntityVisa>();
        if (CollectionUtils.isNotEmpty(builder.getVisas())) {
            for (EntityVisa.Builder visa : builder.getVisas()) {
                this.visas.add(visa.build());
            }
        }
        this.versionNumber = builder.getVersionNumber();
        this.objectId = builder.getObjectId();
        this.active = builder.isActive();
        this.id = builder.getId();
    }

    @Override
    public List<Principal> getPrincipals() {
        return this.principals;
    }

    @Override
    public List<EntityTypeContactInfo> getEntityTypeContactInfos() {
        return this.entityTypeContactInfos;
    }

    @Override
    public List<EntityExternalIdentifier> getExternalIdentifiers() {
        return this.externalIdentifiers;
    }

    @Override
    public List<EntityAffiliation> getAffiliations() {
        return this.affiliations;
    }

    @Override
    public List<EntityName> getNames() {
        return this.names;
    }

    @Override
    public List<EntityEmployment> getEmploymentInformation() {
        return this.employmentInformation;
    }

    @Override
    public EntityPrivacyPreferences getPrivacyPreferences() {
        return this.privacyPreferences;
    }

    @Override
    public EntityBioDemographics getBioDemographics() {
        return this.bioDemographics;
    }

    @Override
    public List<EntityCitizenship> getCitizenships() {
        return this.citizenships;
    }

    @Override
    public EntityEmployment getPrimaryEmployment() {
        return this.primaryEmployment;
    }

    @Override
    public EntityAffiliation getDefaultAffiliation() {
        return this.defaultAffiliation;
    }

    @Override
    public EntityExternalIdentifier getEntityExternalIdentifier(String externalIdentifierTypeCode) {
        if (externalIdentifiers == null) {
            return null;
        }
        for (EntityExternalIdentifier externalId : externalIdentifiers) {
            if (externalId.getExternalIdentifierTypeCode().equals(externalIdentifierTypeCode)) {
                return externalId;
            }
        }
        return null;
    }

    @Override
    public EntityNameContract getDefaultName() {
        return this.defaultName;
    }

    @Override
    public List<EntityEthnicity> getEthnicities() {
        return this.ethnicities;
    }

    @Override
    public List<EntityResidency> getResidencies() {
        return this.residencies;
    }

    @Override
    public List<EntityVisa> getVisas() {
        return this.visas;
    }

    @Override
    public Long getVersionNumber() {
        return this.versionNumber;
    }

    @Override
    public String getObjectId() {
        return this.objectId;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public String getId() {
        return this.id;
    }

    public EntityTypeContactInfo getEntityTypeContactInfoByTypeCode(String entityTypeCode) {
        if (entityTypeContactInfos == null) {
            return null;
        }
        for (EntityTypeContactInfo entType : entityTypeContactInfos) {
            if (entType.getEntityTypeCode().equals(entityTypeCode)) {
                return entType;
            }
        }
        return null;
    }

    /**
     * A builder which can be used to construct {@link Entity} instances.  Enforces the constraints of the {@link EntityContract}.
     * 
     */
    public final static class Builder
        implements Serializable, ModelBuilder, EntityContract
    {

        private List<Principal.Builder> principals;
        private List<EntityTypeContactInfo.Builder> entityTypeContactInfos;
        private List<EntityExternalIdentifier.Builder> externalIdentifiers;
        private List<EntityAffiliation.Builder> affiliations;
        private List<EntityName.Builder> names;
        private List<EntityEmployment.Builder> employmentInformation;
        private EntityPrivacyPreferences.Builder privacyPreferences;
        private EntityBioDemographics.Builder bioDemographics;
        private List<EntityCitizenship.Builder> citizenships;
        private List<EntityEthnicity.Builder> ethnicities;
        private List<EntityResidency.Builder> residencies;
        private List<EntityVisa.Builder> visas;
        private Long versionNumber;
        private String objectId;
        private boolean active;
        private String id;

        private Builder() { }

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(EntityContract contract) {
            if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            if (contract.getPrincipals() != null) {
                List<Principal.Builder> tempPrincipals = new ArrayList<Principal.Builder>();
                for (PrincipalContract principal : contract.getPrincipals()) {
                    tempPrincipals.add(Principal.Builder.create(principal));
                }
                builder.setPrincipals(tempPrincipals);
            }
            if (contract.getEntityTypeContactInfos() != null) {
                List<EntityTypeContactInfo.Builder> tempTypeData = new ArrayList<EntityTypeContactInfo.Builder>();
                for (EntityTypeContactInfoContract typeData : contract.getEntityTypeContactInfos()) {
                    tempTypeData.add(EntityTypeContactInfo.Builder.create(typeData));
                }
                builder.setEntityTypes(tempTypeData);
            }
            if (contract.getExternalIdentifiers() != null) {
                List<EntityExternalIdentifier.Builder> externalIds = new ArrayList<EntityExternalIdentifier.Builder>();
                for (EntityExternalIdentifierContract externalId : contract.getExternalIdentifiers()) {
                    externalIds.add(EntityExternalIdentifier.Builder.create(externalId));
                }
                builder.setExternalIdentifiers(externalIds);
            }
            if (contract.getAffiliations() != null) {
                List<EntityAffiliation.Builder> affils = new ArrayList<EntityAffiliation.Builder>();
                for (EntityAffiliationContract affil : contract.getAffiliations()) {
                    affils.add(EntityAffiliation.Builder.create(affil));
                }
                builder.setAffiliations(affils);
            }
            if (contract.getNames() != null) {
                List<EntityName.Builder> nms = new ArrayList<EntityName.Builder>();
                for (EntityNameContract nm : contract.getNames()) {
                    nms.add(EntityName.Builder.create(nm));
                }
                builder.setNames(nms);
            }
            if (contract.getEmploymentInformation() != null) {
                List<EntityEmployment.Builder> emps = new ArrayList<EntityEmployment.Builder>();
                for (EntityEmploymentContract emp : contract.getEmploymentInformation()) {
                    emps.add(EntityEmployment.Builder.create(emp));
                }
                builder.setEmploymentInformation(emps);
            }
            builder.setPrivacyPreferences(contract.getPrivacyPreferences() == null ? null : EntityPrivacyPreferences.Builder.create(contract.getPrivacyPreferences()));
            builder.setBioDemographics(contract.getBioDemographics() == null ? null : EntityBioDemographics.Builder.create(contract.getBioDemographics()));
            if (contract.getCitizenships() != null) {
                List<EntityCitizenship.Builder> cits = new ArrayList<EntityCitizenship.Builder>();
                for (EntityCitizenshipContract cit : contract.getCitizenships()) {
                    cits.add(EntityCitizenship.Builder.create(cit));
                }
                builder.setCitizenships(cits);
            }
            if (contract.getEthnicities() != null) {
                List<EntityEthnicity.Builder> ethnctys = new ArrayList<EntityEthnicity.Builder>();
                for (EntityEthnicityContract ethncty : contract.getEthnicities()) {
                    ethnctys.add(EntityEthnicity.Builder.create(ethncty));
                }
                builder.setEthnicities(ethnctys);
            }
            if (contract.getResidencies() != null) {
                List<EntityResidency.Builder> residencyBuilders = new ArrayList<EntityResidency.Builder>();
                for (EntityResidencyContract residency : contract.getResidencies()) {
                    residencyBuilders.add(EntityResidency.Builder.create(residency));
                }
                builder.setResidencies(residencyBuilders);
            }
            if (contract.getVisas() != null) {
                List<EntityVisa.Builder> visaBuilders = new ArrayList<EntityVisa.Builder>();
                for (EntityVisaContract visa : contract.getVisas()) {
                    visaBuilders.add(EntityVisa.Builder.create(visa));
                }
                builder.setVisas(visaBuilders);
            }
            builder.setVersionNumber(contract.getVersionNumber());
            builder.setObjectId(contract.getObjectId());
            builder.setActive(contract.isActive());
            builder.setId(contract.getId());
            return builder;
        }

        public Entity build() {
            return new Entity(this);
        }

        @Override
        public List<Principal.Builder> getPrincipals() {
            return this.principals;
        }

        @Override
        public List<EntityTypeContactInfo.Builder> getEntityTypeContactInfos() {
            return this.entityTypeContactInfos;
        }

        @Override
        public List<EntityExternalIdentifier.Builder> getExternalIdentifiers() {
            return this.externalIdentifiers;
        }

        @Override
        public List<EntityAffiliation.Builder> getAffiliations() {
            return this.affiliations;
        }

        @Override
        public List<EntityName.Builder> getNames() {
            return this.names;
        }

        @Override
        public List<EntityEmployment.Builder> getEmploymentInformation() {
            return this.employmentInformation;
        }

        @Override
        public EntityPrivacyPreferences.Builder getPrivacyPreferences() {
            return this.privacyPreferences;
        }

        @Override
        public EntityBioDemographics.Builder getBioDemographics() {
            return this.bioDemographics;
        }

        @Override
        public List<EntityCitizenship.Builder> getCitizenships() {
            return this.citizenships;
        }

        @Override
        public EntityTypeContactInfo.Builder getEntityTypeContactInfoByTypeCode(String entityTypeCode) {
            if (CollectionUtils.isEmpty(this.entityTypeContactInfos)) {
                return null;
            }
            for (EntityTypeContactInfo.Builder builder : this.entityTypeContactInfos) {
                if (builder.getEntityTypeCode().equals(entityTypeCode) && builder.isActive()) {
                    return builder;
                }
            }
            return null;
        }

        @Override
        public EntityEmployment.Builder getPrimaryEmployment() {
            if (CollectionUtils.isEmpty(this.employmentInformation)) {
                return null;
            }
            for (EntityEmployment.Builder builder : this.employmentInformation) {
                if (builder.isPrimary()
                        && builder.isActive()) {
                    return builder;
                }
            }
            return null;
        }

        @Override
        public EntityAffiliation.Builder getDefaultAffiliation() {
            if (CollectionUtils.isEmpty(this.affiliations)) {
                return null;
            }
            for (EntityAffiliation.Builder builder : this.affiliations) {
                if (builder.isDefaultValue()
                        && builder.isActive()) {
                    return builder;
                }
            }
            return null;
        }

        @Override
        public EntityExternalIdentifier.Builder getEntityExternalIdentifier(String externalIdentifierTypeCode) {
            if (CollectionUtils.isEmpty(this.externalIdentifiers)) {
                return null;
            }
            for (EntityExternalIdentifier.Builder builder : this.externalIdentifiers) {
                if (builder.getExternalIdentifierTypeCode().equals(externalIdentifierTypeCode)) {
                    return builder;
                }
            }
            return null;
        }

        @Override
        public EntityName.Builder getDefaultName() {
            if (CollectionUtils.isEmpty(this.names)) {
                return null;
            }
            for (EntityName.Builder builder : this.names) {
                if (builder.isDefaultValue()
                        && builder.isActive()) {
                    return builder;
                }
            }
            return null;
        }

        @Override
        public List<EntityEthnicity.Builder> getEthnicities() {
            return this.ethnicities;
        }

        @Override
        public List<EntityResidency.Builder> getResidencies() {
            return this.residencies;
        }

        @Override
        public List<EntityVisa.Builder> getVisas() {
            return this.visas;
        }

        @Override
        public Long getVersionNumber() {
            return this.versionNumber;
        }

        @Override
        public String getObjectId() {
            return this.objectId;
        }

        @Override
        public boolean isActive() {
            return this.active;
        }

        @Override
        public String getId() {
            return this.id;
        }

        public void setPrincipals(List<Principal.Builder> principals) {
            this.principals = principals;
        }

        public void setEntityTypes(List<EntityTypeContactInfo.Builder> entityTypeContactInfos) {
            this.entityTypeContactInfos = entityTypeContactInfos;
        }

        public void setExternalIdentifiers(List<EntityExternalIdentifier.Builder> externalIdentifiers) {
            this.externalIdentifiers = externalIdentifiers;
        }

        public void setAffiliations(List<EntityAffiliation.Builder> affiliations) {
            this.affiliations = affiliations;
        }

        public void setNames(List<EntityName.Builder> names) {
            this.names = names;
        }

        public void setEmploymentInformation(List<EntityEmployment.Builder> employmentInformation) {
            this.employmentInformation = employmentInformation;
        }

        public void setPrivacyPreferences(EntityPrivacyPreferences.Builder privacyPreferences) {
            this.privacyPreferences = privacyPreferences;
        }

        public void setBioDemographics(EntityBioDemographics.Builder bioDemographics) {
            this.bioDemographics = bioDemographics;
        }

        public void setCitizenships(List<EntityCitizenship.Builder> citizenships) {
            this.citizenships = citizenships;
        }

        public void setEthnicities(List<EntityEthnicity.Builder> ethnicities) {
            this.ethnicities = ethnicities;
        }

        public void setResidencies(List<EntityResidency.Builder> residencies) {
            this.residencies = residencies;
        }

        public void setVisas(List<EntityVisa.Builder> visas) {
            this.visas = visas;
        }

        public void setVersionNumber(Long versionNumber) {
            this.versionNumber = versionNumber;
        }

        public void setObjectId(String objectId) {
            this.objectId = objectId;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public void setId(String id) {
            if (StringUtils.isWhitespace(id)) {
                throw new IllegalArgumentException("id is blank");
            }
            this.id = id;
        }

    }


    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "entity";
        final static String TYPE_NAME = "EntityType";
        final static String[] HASH_CODE_EQUALS_EXCLUDE = new String[] {CoreConstants.CommonElements.FUTURE_ELEMENTS };

    }


    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String PRINCIPALS = "principals";
        final static String PRINCIPAL = "principal";
        final static String ENTITY_TYPE_CONTACT_INFOS = "entityTypeContactInfos";
        final static String ENTITY_TYPE_CONTACT_INFO = "entityTypeContactInfo";
        final static String EXTERNAL_IDENTIFIERS = "externalIdentifiers";
        final static String EXTERNAL_IDENTIFIER = "externalIdentifier";
        final static String AFFILIATIONS = "affiliations";
        final static String AFFILIATION = "affiliation";
        final static String NAMES = "names";
        final static String NAME = "name";
        final static String EMPLOYMENT_INFORMATION = "employmentInformation";
        final static String EMPLOYMENT = "employment";
        final static String PRIVACY_PREFERENCES = "privacyPreferences";
        final static String BIO_DEMOGRAPHICS = "bioDemographics";
        final static String CITIZENSHIPS = "citizenships";
        final static String CITIZENSHIP = "citizenship";
        final static String PRIMARY_EMPLOYMENT = "primaryEmployment";
        final static String DEFAULT_AFFILIATION = "defaultAffiliation";
        final static String DEFAULT_NAME = "defaultName";
        final static String ETHNICITIES = "ethnicities";
        final static String ETHNICITY = "ethnicity";
        final static String RESIDENCIES = "residencies";
        final static String RESIDENCY = "residency";
        final static String VISAS = "visas";
        final static String VISA = "visa";
        final static String ACTIVE = "active";
        final static String ID = "id";

    }

    public static class Cache {
        public static final String NAME = KimConstants.Namespaces.KIM_NAMESPACE_2_0 + "/" + Entity.Constants.TYPE_NAME;
    }
}