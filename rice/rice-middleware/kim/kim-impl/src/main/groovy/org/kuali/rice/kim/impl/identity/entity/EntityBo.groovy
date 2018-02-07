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
package org.kuali.rice.kim.impl.identity.entity

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import org.apache.commons.collections.CollectionUtils
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import org.kuali.rice.kim.api.identity.EntityUtils
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship
import org.kuali.rice.kim.api.identity.employment.EntityEmployment
import org.kuali.rice.kim.api.identity.entity.Entity
import org.kuali.rice.kim.api.identity.entity.EntityContract
import org.kuali.rice.kim.api.identity.entity.EntityDefault
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier
import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.identity.name.EntityNameContract
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity
import org.kuali.rice.kim.api.identity.principal.Principal
import org.kuali.rice.kim.api.identity.residency.EntityResidency
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo
import org.kuali.rice.kim.api.identity.visa.EntityVisa
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipBo
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierBo
import org.kuali.rice.kim.impl.identity.name.EntityNameBo
import org.kuali.rice.kim.impl.identity.personal.EntityBioDemographicsBo
import org.kuali.rice.kim.impl.identity.personal.EntityEthnicityBo
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo
import org.kuali.rice.kim.impl.identity.privacy.EntityPrivacyPreferencesBo
import org.kuali.rice.kim.impl.identity.residency.EntityResidencyBo
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo
import org.kuali.rice.kim.impl.identity.visa.EntityVisaBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

@javax.persistence.Entity
@Table(name = "KRIM_ENTITY_T")
class EntityBo extends PersistableBusinessObjectBase implements EntityContract {
    @Id
	@Column(name = "ENTITY_ID")
	String id;

	@OneToMany(fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityNameBo> names = new ArrayList<EntityNameBo>()

	@OneToMany(fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<PrincipalBo> principals = new ArrayList<PrincipalBo>()

	@OneToMany(fetch=FetchType.EAGER,cascade=[CascadeType.ALL])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name="ENTITY_ID", insertable = false, updatable = false)
    List<EntityExternalIdentifierBo> externalIdentifiers = new ArrayList<EntityExternalIdentifierBo>()

	@Fetch(value = FetchMode.SELECT)
	@OneToMany(fetch=FetchType.EAGER,cascade=[CascadeType.ALL])
	@JoinColumn(name="ENTITY_ID", insertable = false, updatable = false)
	List<EntityAffiliationBo> affiliations  = new ArrayList<EntityAffiliationBo>()

	@OneToMany(fetch=FetchType.EAGER, cascade=[CascadeType.ALL])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name="ENTITY_ID", insertable = false, updatable = false)
	List<EntityEmploymentBo> employmentInformation = new ArrayList<EntityEmploymentBo>()

	@OneToMany(fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityTypeContactInfoBo> entityTypeContactInfos  = new ArrayList<EntityTypeContactInfoBo>()
	
	@OneToOne(targetEntity=EntityPrivacyPreferencesBo.class, fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.REFRESH])
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	EntityPrivacyPreferencesBo privacyPreferences

	@OneToOne(targetEntity=EntityBioDemographicsBo.class, fetch = FetchType.EAGER, cascade = [])
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	EntityBioDemographicsBo bioDemographics
	
	@OneToMany(targetEntity = EntityCitizenshipBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityCitizenshipBo> citizenships = new ArrayList<EntityCitizenshipBo>()

	@OneToMany(targetEntity = EntityEthnicityBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityEthnicityBo> ethnicities  = new ArrayList<EntityEthnicityBo>()

	@OneToMany(targetEntity = EntityResidencyBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityResidencyBo> residencies = new ArrayList<EntityResidencyBo>()

	@OneToMany(targetEntity = EntityVisaBo.class, fetch = FetchType.EAGER, cascade = [ CascadeType.ALL ])
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "ENTITY_ID", insertable = false, updatable = false)
	List<EntityVisaBo> visas  = new ArrayList<EntityVisaBo>()

    @Type(type="yes_no")
    @Column(name="ACTV_IND")
    boolean active;

    /*
     * Converts a mutable EntityBo to an immutable Entity representation.
     * @param bo
     * @return an immutable Entity
     */
    static Entity to(EntityBo bo) {
        if (bo == null) { return null }
        return Entity.Builder.create(bo).build()
    }

    static EntityDefault toDefault(EntityBo bo) {
        if (bo == null) { return null }
        return EntityDefault.Builder.create(bo).build()
    }

    static EntityBo from(Entity immutable) {
        return fromAndUpdate(immutable, null)
    }

    /**
     * Creates a EntityBo business object from an immutable representation of a Entity.
     * @param an immutable Entity
     * @return a EntityBo
     */
    static EntityBo fromAndUpdate(Entity immutable, EntityBo toUpdate) {
        if (immutable == null) {return null}

        EntityBo bo = toUpdate
        if (toUpdate == null) {
            bo = new EntityBo()
        }

        bo.active = immutable.active
        bo.id = immutable.id


        bo.names = new ArrayList<EntityNameBo>()
        if (CollectionUtils.isNotEmpty(immutable.names)) {
            for (EntityName name : immutable.names) {
                bo.names.add(EntityNameBo.from(name))
            }
        }

    	bo.principals = new ArrayList<PrincipalBo>()
        if (CollectionUtils.isNotEmpty(immutable.principals)) {
            for (Principal principal : immutable.principals) {
                bo.principals.add(PrincipalBo.from(principal))
            }
        }

        bo.externalIdentifiers = new ArrayList<EntityExternalIdentifierBo>();
        if (CollectionUtils.isNotEmpty(immutable.externalIdentifiers)) {
            for (EntityExternalIdentifier externalId : immutable.externalIdentifiers) {
                bo.externalIdentifiers.add(EntityExternalIdentifierBo.from(externalId))
            }
        }

        bo.affiliations = new ArrayList<EntityAffiliationBo>()
        if (CollectionUtils.isNotEmpty(immutable.affiliations)) {
            for (EntityAffiliation affiliation : immutable.affiliations) {
                bo.affiliations.add(EntityAffiliationBo.from(affiliation))
            }
        }

        bo.employmentInformation = new ArrayList<EntityEmploymentBo>()
        if (CollectionUtils.isNotEmpty(immutable.employmentInformation)) {
            for (EntityEmployment employment : immutable.employmentInformation) {
                bo.employmentInformation.add(EntityEmploymentBo.from(employment))
            }
        }

        bo.entityTypeContactInfos = new ArrayList<EntityTypeContactInfoBo>()
        if (CollectionUtils.isNotEmpty(immutable.entityTypeContactInfos)) {
            for (EntityTypeContactInfo entityType : immutable.entityTypeContactInfos) {
                bo.entityTypeContactInfos.add(EntityTypeContactInfoBo.from(entityType))
            }
        }

        if (immutable.privacyPreferences != null) {
            bo.privacyPreferences = EntityPrivacyPreferencesBo.from(immutable.privacyPreferences)
        }

        if (immutable.bioDemographics != null) {
            bo.bioDemographics = EntityBioDemographicsBo.from(immutable.bioDemographics)
        }

        bo.citizenships = new ArrayList<EntityCitizenshipBo>()
        if (CollectionUtils.isNotEmpty(immutable.citizenships)) {
            for (EntityCitizenship citizenship : immutable.citizenships) {
                bo.citizenships.add(EntityCitizenshipBo.from(citizenship))
            }
        }

        bo.ethnicities = new ArrayList<EntityEthnicityBo>()
        if (CollectionUtils.isNotEmpty(immutable.ethnicities)) {
            for (EntityEthnicity ethnicity : immutable.ethnicities) {
                bo.ethnicities.add(EntityEthnicityBo.from(ethnicity))
            }
        }

        bo.residencies = new ArrayList<EntityResidencyBo>()
        if (CollectionUtils.isNotEmpty(immutable.residencies)) {
            for (EntityResidency residency : immutable.residencies) {
                bo.residencies.add(EntityResidencyBo.from(residency))
            }
        }

        bo.visas = new ArrayList<EntityVisaBo>()
        if (CollectionUtils.isNotEmpty(immutable.visas)) {
            for (EntityVisa visa : immutable.visas) {
                bo.visas.add(EntityVisaBo.from(visa))
            }
        }

        bo.versionNumber = immutable.versionNumber
        bo.objectId = immutable.objectId

        return bo;
    }


    EntityTypeContactInfoBo getEntityTypeContactInfoByTypeCode(String entityTypeCode) {
        if (CollectionUtils.isEmpty(this.entityTypeContactInfos)) {
            return null
        }
        for (EntityTypeContactInfoBo entType : this.entityTypeContactInfos) {
            if (entType.getEntityTypeCode().equals(entityTypeCode)) {
                return entType
            }
        }
        return null
    }

    EntityEmploymentBo getPrimaryEmployment() {
        if (CollectionUtils.isEmpty(this.employmentInformation)) {
            return null
        }
        for (EntityEmploymentBo employment : this.employmentInformation) {
            if (employment.isPrimary() && employment.isActive()) {
                return employment
            }
        }
        return null
    }

    EntityAffiliationBo getDefaultAffiliation() {
        return EntityUtils.getDefaultItem(this.affiliations)
    }

    EntityExternalIdentifierBo getEntityExternalIdentifier(String externalIdentifierTypeCode) {
        if (CollectionUtils.isEmpty(this.externalIdentifiers)) {
            return null
        }
        for (EntityExternalIdentifierBo externalId : this.externalIdentifiers) {
            if (externalId.getExternalIdentifierTypeCode().equals(externalIdentifierTypeCode)) {
                return externalId
            }
        }
        return null
    }

    EntityNameContract getDefaultName() {
        return EntityUtils.getDefaultItem(this.names)
    }

    @Override
    public EntityPrivacyPreferencesBo getPrivacyPreferences() {
        return this.privacyPreferences
    }

    @Override
    public EntityBioDemographicsBo getBioDemographics() {
        return this.bioDemographics
    }
}
