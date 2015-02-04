package org.kuali.ole.deliver.api;

import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliationContract;
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenshipContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityContract;
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifierContract;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographicsContract;
import org.kuali.rice.kim.api.identity.personal.EntityEthnicityContract;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferencesContract;
import org.kuali.rice.kim.api.identity.residency.EntityResidencyContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract;
import org.kuali.rice.kim.api.identity.visa.EntityVisaContract;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/29/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class EntityHelper {
    private static final String ENTITY_ID = "190192";
    private static final EntityTypeContactInfo ENTITY_TYPE = EntityTypeContactInfoHelper.create();
    private static final EntityName NAME = EntityNameHelper.create() ;
    private static final boolean ACTIVE = true;
    private static final Long VERSION_NUMBER = new Long(1);
    private static final String OBJECT_ID = String.valueOf(UUID.randomUUID());
    public static Entity create() {
        final List<EntityTypeContactInfo> testEntityTypes = Collections.singletonList(EntityHelper.ENTITY_TYPE);

        final List<EntityName> testNames = Collections.singletonList(EntityHelper.NAME);

        return Entity.Builder.create(new EntityContract() {
            @Override
            public List<? extends EntityTypeContactInfoContract> getEntityTypeContactInfos() {
                return testEntityTypes;
            }

            @Override
            public List<? extends PrincipalContract> getPrincipals() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityExternalIdentifierContract> getExternalIdentifiers() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityAffiliationContract> getAffiliations() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityNameContract> getNames() {
                return testNames;
            }

            @Override
            public List<? extends EntityEmploymentContract> getEmploymentInformation() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EntityPrivacyPreferencesContract getPrivacyPreferences() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EntityBioDemographicsContract getBioDemographics() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityCitizenshipContract> getCitizenships() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EntityTypeContactInfoContract getEntityTypeContactInfoByTypeCode(String entityTypeCode) {
                return this.getEntityTypeContactInfoByTypeCode(entityTypeCode);
            }

            @Override
            public EntityEmploymentContract getPrimaryEmployment() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EntityAffiliationContract getDefaultAffiliation() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public EntityExternalIdentifierContract getEntityExternalIdentifier(String externalIdentifierTypeCode) {
                return this.getEntityExternalIdentifier(externalIdentifierTypeCode);
            }

            @Override
            public EntityNameContract getDefaultName() {
                return EntityHelper.NAME;
            }

            @Override
            public List<? extends EntityEthnicityContract> getEthnicities() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityResidencyContract> getResidencies() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<? extends EntityVisaContract> getVisas() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getObjectId() {
                return EntityHelper.OBJECT_ID;
            }

            @Override
            public String getId() {
                return EntityHelper.ENTITY_ID;
            }

            @Override
            public boolean isActive() {
                return EntityHelper.ACTIVE;
            }

            @Override
            public Long getVersionNumber() {
                return EntityHelper.VERSION_NUMBER;
            }
        }).build();

    }
}
