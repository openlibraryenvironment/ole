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
package org.kuali.rice.kim.service.impl

import groovy.mock.interceptor.MockFor
import java.text.SimpleDateFormat
import org.junit.Assert
import org.junit.Before

import org.junit.Test
import org.kuali.rice.core.api.criteria.CountFlag
import org.kuali.rice.core.api.criteria.CriteriaLookupService
import org.kuali.rice.core.api.criteria.CriteriaStringValue
import org.kuali.rice.core.api.criteria.EqualPredicate
import org.kuali.rice.core.api.criteria.GenericQueryResults
import org.kuali.rice.core.api.criteria.QueryByCriteria
import org.kuali.rice.core.api.exception.RiceIllegalStateException
import org.kuali.rice.kim.api.identity.IdentityService
import org.kuali.rice.kim.api.identity.address.EntityAddress
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation
import org.kuali.rice.kim.api.identity.citizenship.EntityCitizenship
import org.kuali.rice.kim.api.identity.email.EntityEmail
import org.kuali.rice.kim.api.identity.employment.EntityEmployment
import org.kuali.rice.kim.api.identity.entity.Entity
import org.kuali.rice.kim.api.identity.entity.EntityDefault
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults
import org.kuali.rice.kim.api.identity.entity.EntityQueryResults
import org.kuali.rice.kim.api.identity.external.EntityExternalIdentifier
import org.kuali.rice.kim.api.identity.name.EntityName
import org.kuali.rice.kim.api.identity.name.EntityNameQueryResults
import org.kuali.rice.kim.api.identity.personal.EntityBioDemographics
import org.kuali.rice.kim.api.identity.personal.EntityEthnicity
import org.kuali.rice.kim.api.identity.phone.EntityPhone
import org.kuali.rice.kim.api.identity.principal.Principal
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences
import org.kuali.rice.kim.api.identity.residency.EntityResidency
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo
import org.kuali.rice.kim.api.identity.visa.EntityVisa
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationBo
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipBo
import org.kuali.rice.kim.impl.identity.citizenship.EntityCitizenshipStatusBo
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentStatusBo
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentTypeBo
import org.kuali.rice.kim.impl.identity.entity.EntityBo
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierBo
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierTypeBo
import org.kuali.rice.kim.impl.identity.name.EntityNameBo
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo
import org.kuali.rice.kim.impl.identity.personal.EntityBioDemographicsBo
import org.kuali.rice.kim.impl.identity.personal.EntityEthnicityBo
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo
import org.kuali.rice.kim.impl.identity.privacy.EntityPrivacyPreferencesBo
import org.kuali.rice.kim.impl.identity.residency.EntityResidencyBo
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo
import org.kuali.rice.kim.impl.identity.visa.EntityVisaBo
import org.kuali.rice.krad.service.BusinessObjectService
import org.kuali.rice.krad.service.PersistenceService
import org.kuali.rice.kim.impl.identity.IdentityServiceImpl

class IdentityServiceImplTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    private MockFor mockBoService;
    private MockFor mockCriteriaLookupService;
    private BusinessObjectService boService;
    private PersistenceService persistenceService;
    private CriteriaLookupService criteriaLookupService;
    IdentityService identityService;
    IdentityServiceImpl identityServiceImpl;

    Map<String, EntityBo> sampleEntities = new HashMap<String, EntityBo>();
    Map<String, PrincipalBo> samplePrincipals = new HashMap<String, PrincipalBo>();
    Map<String, EntityTypeContactInfoBo> sampleEntityTypeContactInfos = new HashMap<String, EntityTypeContactInfoBo>();
    Map<String, EntityAddressBo> sampleEntityAddresses = new HashMap<String, EntityAddressBo>();
    Map<String, EntityEmailBo> sampleEntityEmails = new HashMap<String, EntityEmailBo>();
    Map<String, EntityPhoneBo> sampleEntityPhones = new HashMap<String, EntityPhoneBo>();
    Map<String, EntityExternalIdentifierBo> sampleEntityExternalIdentifiers = new HashMap<String, EntityExternalIdentifierBo>();
    Map<String, EntityAffiliationBo> sampleEntityAffiliations = new HashMap<String, EntityAffiliationBo>();
    Map<String, EntityPrivacyPreferencesBo> sampleEntityPrivacyPreferences = new HashMap<String, EntityPrivacyPreferencesBo>();
    Map<String, EntityCitizenshipBo> sampleEntityCitizenships = new HashMap<String, EntityCitizenshipBo>();
    Map<String, EntityEthnicityBo> sampleEntityEthnicities = new HashMap<String, EntityEthnicityBo>();
    Map<String, EntityResidencyBo> sampleEntityResidencies = new HashMap<String, EntityResidencyBo>();
    Map<String, EntityVisaBo> sampleEntityVisas = new HashMap<String, EntityVisaBo>();
    Map<String, EntityNameBo> sampleEntityNames = new HashMap<String, EntityNameBo>();
    Map<String, EntityEmploymentBo> sampleEntityEmployments = new HashMap<String, EntityEmploymentBo>();
    Map<String, EntityBioDemographicsBo> sampleEntityBioDemographics = new HashMap<String, EntityBioDemographicsBo>();

    @Before
    void createSampleBOs() {
        EntityPrivacyPreferencesBo firstEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo firstEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        PrincipalBo firstEntityPrincipal = new PrincipalBo(entityId: "AAA", principalId: "P1", active: true, principalName: "first", versionNumber: 1, password: "first_password");
        List<PrincipalBo> firstPrincipals = new ArrayList<PrincipalBo>();
        firstPrincipals.add(firstEntityPrincipal);
        EntityTypeContactInfoBo firstEntityTypeContactInfoBo = new EntityTypeContactInfoBo(entityId: "AAA", entityTypeCode: "typecodeone", active: true);
        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodeone");
        EntityAddressBo firstEntityAddressBo = new EntityAddressBo(entityId: "AAA", entityTypeCode: "typecodeone", addressType: firstAddressTypeBo, id: "addressidone", addressTypeCode: "addresscodeone", active: true);
        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodeone");
        EntityEmailBo firstEntityEmailBo = new EntityEmailBo(entityId: "AAA", entityTypeCode: "typecodeone", emailType: firstEmailTypeBo, id:"emailidone", emailTypeCode: "emailcodeone", active: true);
        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonecodeone");
        EntityPhoneBo firstEntityPhoneBo = new EntityPhoneBo(entityId: "AAA", entityTypeCode: "typecodeone", phoneType: firstPhoneType, id: "phoneidone", phoneTypeCode: "phonetypecodeone", active: true);
        EntityExternalIdentifierTypeBo firstExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodeone");
        EntityExternalIdentifierBo firstEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "AAA", externalIdentifierType: firstExternalIdentifierType, id: "exidone", externalIdentifierTypeCode: "exidtypecodeone");
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo firstEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo firstEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "AAA", id: "citizenshipidone", active: true, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");
        EntityEthnicityBo firstEntityEthnicityBo = new EntityEthnicityBo(entityId: "AAA", id: "ethnicityidone");
        EntityResidencyBo firstEntityResidencyBo = new EntityResidencyBo(entityId: "AAA", id: "residencyidone");
        EntityVisaBo firstEntityVisaBo = new EntityVisaBo(entityId: "AAA", id: "visaidone");
        EntityNameTypeBo firstEntityNameType = new EntityNameTypeBo(code: "namecodeone");
        EntityNameBo firstEntityNameBo = new EntityNameBo(entityId: "AAA", id: "nameidone", active: true, firstName: "John", lastName: "Smith", nameType: firstEntityNameType, nameCode: "namecodeone");
        EntityEmploymentTypeBo firstEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodeone");
        EntityEmploymentStatusBo firstEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatusone");
        EntityEmploymentBo firstEntityEmploymentBo = new EntityEmploymentBo(entityId: "AAA", id: "employmentidone", entityAffiliation: firstEntityAffiliationBo, entityAffiliationId: "affiliationidone", employeeType: firstEmploymentType, employeeTypeCode: "employmenttypecodeone", employeeStatus: firstEmploymentStatus, employeeStatusCode: "employmentstatusone", active: true, employeeId: "emplIdOne");
        List<EntityEmploymentBo> firstEmplymentBos = new ArrayList<EntityEmploymentBo>();
        firstEmplymentBos.add(firstEntityEmploymentBo);
        EntityBo firstEntityBo = new EntityBo(active: true, id: "AAA", privacyPreferences: firstEntityPrivacyPreferencesBo, bioDemographics: firstEntityBioDemographicsBo, principals: firstPrincipals, employmentInformation: firstEmplymentBos);

        EntityPrivacyPreferencesBo secondEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "BBB", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        EntityBioDemographicsBo secondEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "BBB", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        PrincipalBo secondEntityPrincipal = new PrincipalBo(entityId: "BBB", principalId: "P2", active: true, principalName: "second", versionNumber: 1, password: "second_password");
        List<PrincipalBo> secondPrincipals = new ArrayList<PrincipalBo>();
        secondPrincipals.add(secondEntityPrincipal);
        EntityTypeContactInfoBo secondEntityTypeContactInfoBo = new EntityTypeContactInfoBo(entityId: "BBB", entityTypeCode: "typecodetwo", active: true);
        EntityAddressTypeBo secondAddressTypeBo = new EntityAddressTypeBo(code: "addresscodetwo");
        EntityAddressBo secondEntityAddressBo = new EntityAddressBo(entityId: "BBB", entityTypeCode: "typecodetwo", addressType: secondAddressTypeBo, id: "addressidtwo", addressTypeCode: "addresscodetwo", active: true);
        EntityEmailTypeBo secondEmailTypeBo = new EntityEmailTypeBo(code: "emailcodetwo");
        EntityEmailBo secondEntityEmailBo = new EntityEmailBo(entityId: "BBB", entityTypeCode: "typecodetwo", emailType: secondEmailTypeBo, id:"emailidtwo", emailTypeCode: "emailcodetwo", active: true);
        EntityPhoneTypeBo secondPhoneType = new EntityPhoneTypeBo(code: "phonecodetwo");
        EntityPhoneBo secondEntityPhoneBo = new EntityPhoneBo(entityId: "BBB", entityTypeCode: "typecodetwo", phoneType: secondPhoneType, id: "phoneidtwo", phoneTypeCode: "phonetypecodetwo", active: true);
        EntityExternalIdentifierTypeBo secondExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodetwo");
        EntityExternalIdentifierBo secondEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "BBB", externalIdentifierType: secondExternalIdentifierType, id: "exidtwo", externalIdentifierTypeCode: "exidtypecodetwo");
        EntityAffiliationTypeBo secondAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodetwo");
        EntityAffiliationBo secondEntityAffiliationBo = new EntityAffiliationBo(entityId: "BBB", affiliationType: secondAffiliationType, id: "affiliationidtwo", affiliationTypeCode: "affiliationcodetwo", active: true);
        EntityCitizenshipStatusBo secondEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodetwo", name: "statusnametwo");
        EntityCitizenshipBo secondEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "BBB", id: "citizenshipidtwo", active: true, status: secondEntityCitizenshipStatus, statusCode: "statuscodetwo");
        EntityEthnicityBo secondEntityEthnicityBo = new EntityEthnicityBo(entityId: "BBB", id: "ethnicityidtwo");
        EntityResidencyBo secondEntityResidencyBo = new EntityResidencyBo(entityId: "BBB", id: "residencyidtwo");
        EntityVisaBo secondEntityVisaBo = new EntityVisaBo(entityId: "BBB", id: "visaidtwo");
        EntityNameTypeBo secondEntityNameType = new EntityNameTypeBo(code: "namecodetwo");
        EntityNameBo secondEntityNameBo = new EntityNameBo(entityId: "BBB", id: "nameidtwo", active: true, firstName: "Bill", lastName: "Wright", nameType: secondEntityNameType, nameCode: "namecodetwo");
        EntityEmploymentTypeBo secondEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodetwo");
        EntityEmploymentStatusBo secondEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatustwo");
        EntityEmploymentBo secondEntityEmploymentBo = new EntityEmploymentBo(entityId: "BBB", id: "employmentidtwo", entityAffiliation: secondEntityAffiliationBo, entityAffiliationId: "affiliationidtwo", employeeType: secondEmploymentType, employeeTypeCode: "employmenttypecodetwo", employeeStatus: secondEmploymentStatus, employeeStatusCode: "employmentstatustwo", active: true, employeeId: "emplIdTwo");
        List<EntityEmploymentBo> secondEmplymentBos = new ArrayList<EntityEmploymentBo>();
        secondEmplymentBos.add(secondEntityEmploymentBo);
        EntityBo secondEntityBo = new EntityBo(active: true, id: "BBB", privacyPreferences: secondEntityPrivacyPreferencesBo, bioDemographics: secondEntityBioDemographicsBo, principals: secondPrincipals, employmentInformation: secondEmplymentBos);

        for (bo in [firstEntityBo, secondEntityBo]) {
            sampleEntities.put(bo.id, bo)
        }

        for (bo in [firstEntityPrincipal, secondEntityPrincipal]) {
            samplePrincipals.put(bo.principalId, bo);
        }

        for (bo in [firstEntityAddressBo, secondEntityAddressBo]) {
            sampleEntityAddresses.put(bo.entityId, bo);
        }

        for (bo in [firstEntityTypeContactInfoBo, secondEntityTypeContactInfoBo]) {
            sampleEntityTypeContactInfos.put(bo.entityTypeCode, bo);
        }

        for (bo in [firstEntityEmailBo, secondEntityEmailBo]) {
            sampleEntityEmails.put(bo.entityId, bo);
        }

        for (bo in [firstEntityPhoneBo, secondEntityPhoneBo]) {
            sampleEntityPhones.put(bo.entityId, bo);
        }

        for (bo in [firstEntityExternalIdentifierBo, secondEntityExternalIdentifierBo]) {
            sampleEntityExternalIdentifiers.put(bo.entityId, bo);
        }

        for (bo in [firstEntityAffiliationBo, secondEntityAffiliationBo]) {
            sampleEntityAffiliations.put(bo.entityId, bo);
        }

        for (bo in [firstEntityPrivacyPreferencesBo, secondEntityPrivacyPreferencesBo]) {
            sampleEntityPrivacyPreferences.put(bo.entityId, bo);
        }

        for (bo in [firstEntityCitizenshipBo, secondEntityCitizenshipBo]) {
            sampleEntityCitizenships.put(bo.entityId, bo);
        }

        for (bo in [firstEntityEthnicityBo, secondEntityEthnicityBo]) {
            sampleEntityEthnicities.put(bo.entityId, bo);
        }

        for (bo in [firstEntityResidencyBo, secondEntityResidencyBo]) {
            sampleEntityResidencies.put(bo.entityId, bo);
        }

        for (bo in [firstEntityVisaBo, secondEntityVisaBo]) {
            sampleEntityVisas.put(bo.entityId, bo);
        }

        for (bo in [firstEntityNameBo, secondEntityNameBo]) {
            sampleEntityNames.put(bo.entityId, bo);
        }

        for (bo in [firstEntityEmploymentBo, secondEntityEmploymentBo]) {
            sampleEntityEmployments.put(bo.entityId, bo);
        }

        for (bo in [firstEntityBioDemographicsBo, secondEntityBioDemographicsBo]) {
            sampleEntityBioDemographics.put(bo.entityId, bo);
        }
    }
    
    @Before
    void setupMockContext() {
        mockBoService = new MockFor(BusinessObjectService.class);
        mockCriteriaLookupService = new MockFor(CriteriaLookupService.class);
    }

    @Before
    void setupServiceUnderTest() {
        identityServiceImpl = new IdentityServiceImpl()
        identityService = identityServiceImpl    //assign Interface type to implementation reference for unit test only
    }

    void injectBusinessObjectServiceIntoIdentityService() {
        boService = mockBoService.proxyDelegateInstance()
        identityServiceImpl.setBusinessObjectService(boService)
    }

    void injectCriteriaLookupServiceIntoIdentityService() {
        criteriaLookupService = mockCriteriaLookupService.proxyDelegateInstance();
        identityServiceImpl.setCriteriaLookupService(criteriaLookupService);
    }

    @Test
    void test_createIdentityNullIdentity(){
        injectBusinessObjectServiceIntoIdentityService()

        shouldFail(IllegalArgumentException.class) {
            identityService.createEntity(null)
        }
        mockBoService.verify(boService)
    }

    @Test
    void test_updateIdentityNullIdentity(){
        injectBusinessObjectServiceIntoIdentityService();

        shouldFail(IllegalArgumentException.class) {
            identityService.updateEntity(null)
        }
        mockBoService.verify(boService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityEmptyId() {
        Entity entity = identityService.getEntity("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityNullId() {
        Entity entity = identityService.getEntity(null);
    }

    @Test
    public void testGetEntity() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntities.size()) {
            Class clazz, Map map -> return sampleEntities.get(map.get("id"))
        }

        injectBusinessObjectServiceIntoIdentityService();

        for (EntityBo entityBo in sampleEntities.values()) {
            Assert.assertEquals(EntityBo.to(entityBo), identityService.getEntity(entityBo.id))
        }

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityByPrincipalIdWithEmptyIdFails() {
        Entity entity = identityService.getEntityByPrincipalId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityByPrincipalIdWithNullIdFails() {
        Entity entity = identityService.getEntityByPrincipalId(null);
    }

    @Test
    public void testGetEntityByPrincipalIdSucceeds() {
        mockBoService.demand.findMatching(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                for (PrincipalBo principalBo in entityBo.principals) {
                    if (principalBo.principalId.equals(map.get("principals.principalId")))
                    {
                        Collection<EntityBo> entities = new ArrayList<EntityBo>();
                        entities.add(entityBo);
                        return entities;
                    }
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        for (EntityBo entityBo in sampleEntities.values()) {
            Assert.assertEquals(EntityBo.to(entityBo), identityService.getEntityByPrincipalId(entityBo.principals[0].principalId));
        }

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityByPrincipalNameWithEmptyNameFails() {
        Entity entity = identityService.getEntityByPrincipalName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEntityByPrincipalNameWithNullNameFails() {
        Entity entity = identityService.getEntityByPrincipalName(null);
    }

    @Test
    public void testGetEntityByPrincipalNameSucceeds() {
        mockBoService.demand.findMatching(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                for (PrincipalBo principalBo in entityBo.principals) {
                    if (principalBo.principalName.equals(map.get("principals.principalName")))
                    {
                        Collection<EntityBo> entities = new ArrayList<EntityBo>();
                        entities.add(entityBo);
                        return entities;
                    }
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        for (EntityBo entityBo in sampleEntities.values()) {
            Assert.assertEquals(EntityBo.to(entityBo), identityService.getEntityByPrincipalName(entityBo.principals[0].principalName));
        }

        mockBoService.verify(boService)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalByPrincipalNameAndPasswordWithEmptyNameFails() {
        Principal principal = identityService.getPrincipalByPrincipalNameAndPassword("", "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalByPrincipalNameAndPasswordWithEmptyPasswordFails() {
        Principal principal = identityService.getPrincipalByPrincipalNameAndPassword("name", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalByPrincipalNameAndPasswordWithNullNameFails() {
        Principal principal = identityService.getPrincipalByPrincipalNameAndPassword(null, "password");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalByPrincipalNameAndPasswordWithNullPasswordFails() {
        Principal principal = identityService.getPrincipalByPrincipalNameAndPassword("name", null);
    }

    @Test
    public void testGetPrincipalByPrincipalNameAndPasswordSucceeds() {
        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.principalName.equals(map.get("principalName"))
                    && principalBo.password.equals(map.get("password"))
                    && principalBo.active)
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        String id = "P2";
        String name = "second";
        String password = "second_password";
        Assert.assertEquals(PrincipalBo.to(samplePrincipals.get(id)), identityService.getPrincipalByPrincipalNameAndPassword(name, password));

        mockBoService.verify(boService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalsByEntityIdWithEmptyEntityIdFails() {
        List<Principal> principals = identityService.getPrincipalsByEntityId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalsByEntityIdWithNullEntityIdFails() {
        List<Principal> principals = identityService.getPrincipalsByEntityId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalsByEmployeeIdWithEmptyEmployeeIdFails() {
        List<Principal> principals = identityService.getPrincipalsByEmployeeId("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPrincipalsByEmployeeIdWithNullEmployeeIdFails() {
        List<Principal> principals = identityService.getPrincipalsByEmployeeId(null);
    }

    @Test
    public void testGetPrincipalsByEmployeeIdSucceeds() {

        mockBoService.demand.findMatching(1..2) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                for (EntityEmploymentBo entityEmploymentBo in entityBo.employmentInformation) {
                    if (entityEmploymentBo.employeeId.equals(map.get("employeeId")))
                    {
                        Collection<EntityEmploymentBo> entityEmploymentBos = new ArrayList<EntityEmploymentBo>();
                        entityEmploymentBos.add(entityEmploymentBo);
                        return entityEmploymentBos;
                    }
                }

                for (PrincipalBo principalBo in samplePrincipals.values()) {
                    if (principalBo.entityId.equals(map.get("entityId"))
                        && principalBo.active)
                    {
                        Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                        principals.add(principalBo);
                        return principals;
                    }
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        String employeeId = "emplIdOne";
        String id = "P1";
        String entityId = 'AAA'
        EntityEmployment sampleEntityEmployment = EntityEmploymentBo.to(sampleEntityEmployments.get(entityId));
        Principal samplePrincipal = PrincipalBo.to(samplePrincipals.get(id));

        List<Principal> principals = identityService.getPrincipalsByEmployeeId(employeeId);
        Assert.assertNotNull(principals);
        for (Principal p : principals) {
            Assert.assertEquals(samplePrincipal.getPrincipalId(), p.getPrincipalId());
        }
        mockBoService.verify(boService);
    }

    @Test
    public void testGetPrincipalsByEntityIdSucceeds() {
        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.entityId.equals(map.get("entityId"))
                        && principalBo.active)
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        String entityId = "AAA";
        String id = "P1";
        Principal samplePrincipal = PrincipalBo.to(samplePrincipals.get(id));
        List<Principal> principals = identityService.getPrincipalsByEntityId(entityId);
        for (Principal p : principals) {
            Assert.assertEquals(samplePrincipal.getEntityId(), p.getEntityId());
        }
        mockBoService.verify(boService);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddPrincipalToEntityWithNullPrincipalFails()
    {
        Principal principal = identityService.addPrincipalToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddPrincipalToEntityWithBlankEntityIdFails()
    {
        PrincipalBo principalBo = new PrincipalBo(entityId: "", principalName: "name", principalId: "P1");
        principalBo = identityService.addPrincipalToEntity(PrincipalBo.to(principalBo));
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddPrincipalToEntityWithExistingPrincipalFails()
    {
        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.principalName.equals(map.get("principalName")))
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        PrincipalBo principalBo = new PrincipalBo(entityId: "ABC", principalName: "first", principalId: "P1");
        principalBo = identityService.addPrincipalToEntity(PrincipalBo.to(principalBo));
    }

    @Test
    public void testAddPrincipalToEntitySucceeds()
    {
        PrincipalBo newPrincipalBo = new PrincipalBo(entityId: "ABC", principalName: "new", principalId: "New");

        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.principalName.equals(map.get("principalName")))
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }

            return new ArrayList<PrincipalBo>();
        }

        mockBoService.demand.save(1..1) {
            PrincipalBo bo -> return newPrincipalBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal newPrincipal = identityService.addPrincipalToEntity(PrincipalBo.to(newPrincipalBo));

        Assert.assertEquals(PrincipalBo.to(newPrincipalBo), newPrincipal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePrincipalWithNullPrincipalFails()
    {
        Principal principal = identityService.updatePrincipal(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdatePrincipalWithBlankEntityIdFails()
    {
        PrincipalBo principalBo = new PrincipalBo(entityId: "", principalName: "name", principalId: "P1");
        principalBo = identityService.updatePrincipal(PrincipalBo.to(principalBo));
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdatePrincipalWithNonExistingPrincipalFails()
    {
        // create a matching scenario where no results are returned
        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> return new ArrayList<PrincipalBo>();
        }

        injectBusinessObjectServiceIntoIdentityService();

        PrincipalBo principalBo = new PrincipalBo(entityId: "CCC", principalName: "fifth", principalId: "P5");
        principalBo = identityService.updatePrincipal(PrincipalBo.to(principalBo));
    }

    @Test
    public void testUpdatePrincipalSucceeds()
    {
        PrincipalBo existingPrincipalBo = samplePrincipals.get("P1");

        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.principalName.equals(map.get("principalName")))
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }

            return new ArrayList<PrincipalBo>();
        }

        mockBoService.demand.save(1..1) {
            PrincipalBo bo -> return existingPrincipalBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal existingPrincipal = identityService.updatePrincipal(PrincipalBo.to(existingPrincipalBo));

        Assert.assertEquals(PrincipalBo.to(existingPrincipalBo), existingPrincipal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInactivatePrincipalWithEmptyIdFails() {
        Principal principal = identityService.inactivatePrincipal("");
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivatePrincipalWithNonExistentPrincipalFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal principal = identityService.inactivatePrincipal("New");
    }

    @Test
    public void testInactivatePrincipalSucceeds()
    {
        PrincipalBo existingPrincipalBo = samplePrincipals.get("P1");
        PrincipalBo inactivePrincipalBo = new PrincipalBo(entityId: "AAA", principalId: "P1", active: false, principalName: "first", versionNumber: 1, password: "first_password");

        mockBoService.demand.findByPrimaryKey(1..samplePrincipals.size()) {
            Class clazz, Map map -> return samplePrincipals.get(map.get("principalId"))
        }

        mockBoService.demand.save(1..1) {
            PrincipalBo bo -> return inactivePrincipalBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal inactivePrincipal = identityService.inactivatePrincipal(existingPrincipalBo.principalId);

        Assert.assertEquals(PrincipalBo.to(inactivePrincipalBo), inactivePrincipal);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivatePrincipalByNameWithNonExistentPrincipalFails() {
        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> return new ArrayList<PrincipalBo>();
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal principal = identityService.inactivatePrincipalByName("new");
    }

    @Test
    public void testInactivatePrincipalByNameSucceeds()
    {
        PrincipalBo existingPrincipalBo = samplePrincipals.get("P1");
        PrincipalBo inactivePrincipalBo = new PrincipalBo(entityId: "AAA", principalId: "P1", active: false, principalName: "first", versionNumber: 1, password: "first_password");

        mockBoService.demand.findMatching(1..samplePrincipals.size()) {
            Class clazz, Map map -> for (PrincipalBo principalBo in samplePrincipals.values()) {
                if (principalBo.principalName.equals(map.get("principalName")))
                {
                    Collection<PrincipalBo> principals = new ArrayList<PrincipalBo>();
                    principals.add(principalBo);
                    return principals;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            PrincipalBo bo -> return inactivePrincipalBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Principal inactivePrincipal = identityService.inactivatePrincipalByName(existingPrincipalBo.principalName);

        Assert.assertEquals(PrincipalBo.to(inactivePrincipalBo), inactivePrincipal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEntityTypeContactInfoToEntityWithNullFails() {
        EntityTypeContactInfo entityTypeContactInfo = identityService.addEntityTypeContactInfoToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddEntityTypeContactInfoToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityTypeContactInfos.size()) {
            Class clazz, Map map -> for (EntityTypeContactInfoBo entityTypeContactInfoBo in sampleEntityTypeContactInfos.values()) {
                if (entityTypeContactInfoBo.entityId.equals(map.get("entityId"))
                    && entityTypeContactInfoBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityTypeContactInfoBo.active)
                {
                    return entityTypeContactInfoBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfoBo newEntityTypeContactInfoBo = new EntityTypeContactInfoBo(active: true, entityId: "AAA", entityTypeCode: "typecodeone");
        EntityTypeContactInfo entityTypeContactInfo = identityService.addEntityTypeContactInfoToEntity(EntityTypeContactInfoBo.to(newEntityTypeContactInfoBo));
    }

    @Test
    public void testAddEntityTypeContactInfoToEntitySucceeds() {
        EntityTypeContactInfoBo newEntityTypeContactInfoBo = new EntityTypeContactInfoBo(active: true, entityId: "CCC", entityTypeCode: "typecodethree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityTypeContactInfos.size()) {
            Class clazz, Map map -> for (EntityTypeContactInfoBo entityTypeContactInfoBo in sampleEntityTypeContactInfos.values()) {
                if (entityTypeContactInfoBo.entityId.equals(map.get("entityId"))
                    && entityTypeContactInfoBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityTypeContactInfoBo.active)
                {
                    return entityTypeContactInfoBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityTypeContactInfoBo bo -> return newEntityTypeContactInfoBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfo entityTypeContactInfo = identityService.addEntityTypeContactInfoToEntity(EntityTypeContactInfoBo.to(newEntityTypeContactInfoBo));

        Assert.assertEquals(EntityTypeContactInfoBo.to(newEntityTypeContactInfoBo), entityTypeContactInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEntityTypeContactInfoWithNullFails() {
        EntityTypeContactInfo entityTypeContactInfo = identityService.updateEntityTypeContactInfo(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateEntityTypeContactInfoWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfoBo newEntityTypeContactInfoBo = new EntityTypeContactInfoBo(active: true, entityId: "AAA", entityTypeCode: "typecodeone");
        EntityTypeContactInfo entityTypeContactInfo = identityService.updateEntityTypeContactInfo(EntityTypeContactInfoBo.to(newEntityTypeContactInfoBo));
    }

    @Test
    public void testUpdateEntityTypeContactInfoSucceeds() {
        EntityTypeContactInfoBo existingEntityTypeContactInfoBo = new EntityTypeContactInfoBo(active: true, entityId: "AAA", entityTypeCode: "typecodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityTypeContactInfos.size()) {
            Class clazz, Map map -> for (EntityTypeContactInfoBo entityTypeContactInfoBo in sampleEntityTypeContactInfos.values()) {
                if (entityTypeContactInfoBo.entityId.equals(map.get("entityId"))
                    && entityTypeContactInfoBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityTypeContactInfoBo.active)
                {
                    return entityTypeContactInfoBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityTypeContactInfoBo bo -> return existingEntityTypeContactInfoBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfo entityTypeContactInfo = identityService.updateEntityTypeContactInfo(EntityTypeContactInfoBo.to(existingEntityTypeContactInfoBo));

        Assert.assertEquals(EntityTypeContactInfoBo.to(existingEntityTypeContactInfoBo), entityTypeContactInfo);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateEntityTypeContactInfoWithNonExistentObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfo entityTypeContactInfo = identityService.inactivateEntityTypeContactInfo("new", "new");
    }

    @Test
    public void testInactivateEntityTypeContactInfoSucceeds()
    {
        EntityTypeContactInfoBo existingEntityTypeContactInfoBo = sampleEntityTypeContactInfos.get("typecodeone");
        EntityTypeContactInfoBo inactiveEntityTypeContactInfoBo = new EntityTypeContactInfoBo(entityId: "AAA", entityTypeCode: "typecodeone", active: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityTypeContactInfos.size()) {
            Class clazz, Map map -> for (EntityTypeContactInfoBo entityTypeContactInfoBo in sampleEntityTypeContactInfos.values()) {
                if (entityTypeContactInfoBo.entityId.equals(map.get("entityId"))
                    && entityTypeContactInfoBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityTypeContactInfoBo.active)
                {
                    return entityTypeContactInfoBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityTypeContactInfoBo bo -> return inactiveEntityTypeContactInfoBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityTypeContactInfo inactiveEntityTypeContactInfo = identityService.inactivateEntityTypeContactInfo(existingEntityTypeContactInfoBo.entityId, existingEntityTypeContactInfoBo.entityTypeCode);

        Assert.assertEquals(EntityTypeContactInfoBo.to(inactiveEntityTypeContactInfoBo), inactiveEntityTypeContactInfo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAddressToEntityWithNullFails() {
        EntityAddress entityAddress = identityService.addAddressToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddAddressToEntityWithExistingAddressFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityAddresses.size()) {
            Class clazz, Map map -> for (EntityAddressBo entityAddressBo in sampleEntityAddresses.values()) {
                if (entityAddressBo.entityId.equals(map.get("entityId"))
                    && entityAddressBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityAddressBo.addressTypeCode.equals(map.get("addressTypeCode"))
                    && entityAddressBo.active)
                {
                    return entityAddressBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodeone");
        EntityAddressBo newEntityAddressBo = new EntityAddressBo(entityId: "AAA", entityTypeCode: "typecodeone", addressType: firstAddressTypeBo, addressTypeCode: "addresscodeone");
        EntityAddress entityAddress = identityService.addAddressToEntity(EntityAddressBo.to(newEntityAddressBo));
    }

    @Test
    public void testAddAddressToEntitySucceeds() {
        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodethree");
        EntityAddressBo newEntityAddressBo = new EntityAddressBo(entityId: "CCC", entityTypeCode: "typecodethree", addressType: firstAddressTypeBo, addressTypeCode: "addresscodethree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAddresses.size()) {
            Class clazz, Map map -> for (EntityAddressBo entityAddressBo in sampleEntityAddresses.values()) {
                if (entityAddressBo.entityId.equals(map.get("entityId"))
                    && entityAddressBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityAddressBo.addressTypeCode.equals(map.get("addressTypeCode"))
                    && entityAddressBo.active)
                {
                    return entityAddressBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAddressBo bo -> return newEntityAddressBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddress entityAddress = identityService.addAddressToEntity(EntityAddressBo.to(newEntityAddressBo));

        Assert.assertEquals(EntityAddressBo.to(newEntityAddressBo), entityAddress);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAddressWithNullFails() {
        EntityAddress entityAddress = identityService.updateAddress(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateAddressWithNonExistingAddressFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityAddresses.size()) {
            Class clazz, Map map -> for (EntityAddressBo entityAddressBo in sampleEntityAddresses.values()) {
                if (entityAddressBo.entityId.equals(map.get("entityId"))
                    && entityAddressBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityAddressBo.addressTypeCode.equals(map.get("addressTypeCode"))
                    && entityAddressBo.active)
                {
                    return entityAddressBo;
                }
            }

            return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodethree");
        EntityAddressBo newEntityAddressBo = new EntityAddressBo(entityId: "CCC", entityTypeCode: "typecodethree", addressType: firstAddressTypeBo, addressTypeCode: "addresscodethree");
        EntityAddress entityAddress = identityService.updateAddress(EntityAddressBo.to(newEntityAddressBo));
    }

    @Test
    public void testUpdateAddressSucceeds() {
        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodeone");
        EntityAddressBo existingEntityAddressBo = new EntityAddressBo(entityId: "AAA", entityTypeCode: "typecodeone", id: "addressidone", addressType: firstAddressTypeBo, addressTypeCode: "addresscodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAddresses.size()) {
            Class clazz, Map map -> for (EntityAddressBo entityAddressBo in sampleEntityAddresses.values()) {
                if (entityAddressBo.entityId.equals(map.get("entityId"))
                    && entityAddressBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityAddressBo.addressTypeCode.equals(map.get("addressTypeCode"))
                    && entityAddressBo.active)
                {
                    return entityAddressBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAddressBo bo -> return existingEntityAddressBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddress entityAddress = identityService.updateAddress(EntityAddressBo.to(existingEntityAddressBo));

        Assert.assertEquals(EntityAddressBo.to(existingEntityAddressBo), entityAddress);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateAddressWithNonExistentAddressFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddress entityAddress = identityService.inactivateAddress("new");
    }

    @Test
    public void testInactivateAddressSucceeds()
    {
        EntityAddressBo existingEntityAddressBo = sampleEntityAddresses.get("AAA");
        EntityAddressTypeBo firstAddressTypeBo = new EntityAddressTypeBo(code: "addresscodeone");
        EntityAddressBo inactiveEntityAddressBo = new EntityAddressBo(entityId: "AAA", entityTypeCode: "typecodeone", addressType: firstAddressTypeBo, id: "addressidone", addressTypeCode: "addresscodeone", active: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAddresses.size()) {
            Class clazz, Map map -> for (EntityAddressBo entityAddressBo in sampleEntityAddresses.values()) {
                if (entityAddressBo.id.equals(map.get("id"))) {
                    return entityAddressBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAddressBo bo -> return inactiveEntityAddressBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAddress inactiveEntityAddress = identityService.inactivateAddress(existingEntityAddressBo.id);

        Assert.assertEquals(EntityAddressBo.to(inactiveEntityAddressBo), inactiveEntityAddress);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaddEmailToEntityWithNullFails() {
        EntityEmail entityEmail = identityService.addEmailToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddEmailToEntityWithExistingEmailFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmails.size()) {
            Class clazz, Map map -> for (EntityEmailBo entityEmailBo in sampleEntityEmails.values()) {
                if (entityEmailBo.entityId.equals(map.get("entityId"))
                    && entityEmailBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityEmailBo.emailTypeCode.equals(map.get("emailTypeCode"))
                    && entityEmailBo.active)
                {
                    return entityEmailBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodeone");
        EntityEmailBo newEntityEmailBo = new EntityEmailBo(entityId: "AAA", entityTypeCode: "typecodeone", emailType: firstEmailTypeBo, emailTypeCode: "emailcodeone", active: true);
        EntityEmail entityEmail = identityService.addEmailToEntity(EntityEmailBo.to(newEntityEmailBo));
    }

    @Test
    public void testAddEmailToEntitySucceeds() {
        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodethree");
        EntityEmailBo newEntityEmailBo = new EntityEmailBo(entityId: "CCC", entityTypeCode: "typecodethree", emailType: firstEmailTypeBo, emailTypeCode: "emailcodethree", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmails.size()) {
            Class clazz, Map map -> for (EntityEmailBo entityEmailBo in sampleEntityEmails.values()) {
                if (entityEmailBo.entityId.equals(map.get("entityId"))
                    && entityEmailBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityEmailBo.emailTypeCode.equals(map.get("emailTypeCode"))
                    && entityEmailBo.active)
                {
                    return entityEmailBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmailBo bo -> return newEntityEmailBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmail entityEmail = identityService.addEmailToEntity(EntityEmailBo.to(newEntityEmailBo));

        Assert.assertEquals(EntityEmailBo.to(newEntityEmailBo), entityEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmailWithNullFails() {
        EntityEmail entityEmail = identityService.updateEmail(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateEmailWithNonExistingEmailFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmails.size()) {
            Class clazz, Map map -> for (EntityEmailBo entityEmailBo in sampleEntityEmails.values()) {
                if (entityEmailBo.entityId.equals(map.get("entityId"))
                    && entityEmailBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityEmailBo.emailTypeCode.equals(map.get("emailTypeCode"))
                    && entityEmailBo.active)
                {
                    return entityEmailBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodethree");
        EntityEmailBo newEntityEmailBo = new EntityEmailBo(entityId: "CCC", entityTypeCode: "typecodethree", emailType: firstEmailTypeBo, emailTypeCode: "emailcodethree", active: true);
        EntityEmail entityEmail = identityService.updateEmail(EntityEmailBo.to(newEntityEmailBo));
    }

    @Test
    public void testUpdateEmailSucceeds() {
        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodeone");
        EntityEmailBo existingEntityEmailBo = new EntityEmailBo(entityId: "AAA", entityTypeCode: "typecodeone", emailType: firstEmailTypeBo, id:"emailidone", emailTypeCode: "emailcodeone", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmails.size()) {
            Class clazz, Map map -> for (EntityEmailBo entityEmailBo in sampleEntityEmails.values()) {
                if (entityEmailBo.entityId.equals(map.get("entityId"))
                    && entityEmailBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityEmailBo.emailTypeCode.equals(map.get("emailTypeCode"))
                    && entityEmailBo.active)
                {
                    return entityEmailBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmailBo bo -> return existingEntityEmailBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmail entityEmail = identityService.updateEmail(EntityEmailBo.to(existingEntityEmailBo));

        Assert.assertEquals(EntityEmailBo.to(existingEntityEmailBo), entityEmail);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateEmailWithNonExistentEmailFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmail entityEmail = identityService.inactivateEmail("new");
    }

    @Test
    public void testInactivateEmailSucceeds()
    {
        EntityEmailBo existingEntityEmailBo = sampleEntityEmails.get("AAA");
        EntityEmailTypeBo firstEmailTypeBo = new EntityEmailTypeBo(code: "emailcodeone");
        EntityEmailBo inactiveEntityEmailBo = new EntityEmailBo(entityId: "AAA", entityTypeCode: "typecodeone", emailType: firstEmailTypeBo, id:"emailidone", emailTypeCode: "emailcodeone", active: false);


        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmails.size()) {
            Class clazz, Map map -> for (EntityEmailBo entityEmailBo in sampleEntityEmails.values()) {
                if (entityEmailBo.id.equals(map.get("id"))) {
                    return entityEmailBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmailBo bo -> return inactiveEntityEmailBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmail inactiveEntityEmail = identityService.inactivateEmail(existingEntityEmailBo.id);

        Assert.assertEquals(EntityEmailBo.to(inactiveEntityEmailBo), inactiveEntityEmail);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaddPhoneToEntityWithNullFails() {
        EntityPhone entityPhone = identityService.addPhoneToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddPhoneToEntityWithExistingPhoneFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityPhones.size()) {
            Class clazz, Map map -> for (EntityPhoneBo entityPhoneBo in sampleEntityPhones.values()) {
                if (entityPhoneBo.entityId.equals(map.get("entityId"))
                    && entityPhoneBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityPhoneBo.phoneTypeCode.equals(map.get("phoneTypeCode"))
                    && entityPhoneBo.active)
                {
                    return entityPhoneBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonetypecodeone");
        EntityPhoneBo newEntityPhoneBo = new EntityPhoneBo(entityId: "AAA", entityTypeCode: "typecodeone", phoneType: firstPhoneType, id: "phoneidone", phoneTypeCode: "phonetypecodeone", active: true);
        EntityPhone entityPhone = identityService.addPhoneToEntity(EntityPhoneBo.to(newEntityPhoneBo));
    }

    @Test
    public void testAddPhoneToEntitySucceeds() {
        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonetypecodethree");
        EntityPhoneBo newEntityPhoneBo = new EntityPhoneBo(entityId: "CCC", entityTypeCode: "typecodethree", phoneType: firstPhoneType, id: "phoneidthree", phoneTypeCode: "phonetypecodethree", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityPhones.size()) {
            Class clazz, Map map -> for (EntityPhoneBo entityPhoneBo in sampleEntityPhones.values()) {
                if (entityPhoneBo.entityId.equals(map.get("entityId"))
                    && entityPhoneBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityPhoneBo.phoneTypeCode.equals(map.get("phoneTypeCode"))
                    && entityPhoneBo.active)
                {
                    return entityPhoneBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityPhoneBo bo -> return newEntityPhoneBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhone entityPhone = identityService.addPhoneToEntity(EntityPhoneBo.to(newEntityPhoneBo));

        Assert.assertEquals(EntityPhoneBo.to(newEntityPhoneBo), entityPhone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePhoneWithNullFails() {
        EntityPhone entityPhone = identityService.updatePhone(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdatePhoneWithNonExistingPhoneFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityPhones.size()) {
            Class clazz, Map map -> for (EntityPhoneBo entityPhoneBo in sampleEntityPhones.values()) {
                if (entityPhoneBo.entityId.equals(map.get("entityId"))
                    && entityPhoneBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityPhoneBo.phoneTypeCode.equals(map.get("phoneTypeCode"))
                    && entityPhoneBo.active)
                {
                    return entityPhoneBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonetypecodethree");
        EntityPhoneBo newEntityPhoneBo = new EntityPhoneBo(entityId: "CCC", entityTypeCode: "typecodethree", phoneType: firstPhoneType, id: "phoneidthree", phoneTypeCode: "phonetypecodethree", active: true);
        EntityPhone entityPhone = identityService.updatePhone(EntityPhoneBo.to(newEntityPhoneBo));
    }

    @Test
    public void testUpdatePhoneSucceeds() {
        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonetypecodeone");
        EntityPhoneBo existingEntityPhoneBo = new EntityPhoneBo(entityId: "AAA", entityTypeCode: "typecodeone", phoneType: firstPhoneType, id: "phoneidone", phoneTypeCode: "phonetypecodeone", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityPhones.size()) {
            Class clazz, Map map -> for (EntityPhoneBo entityPhoneBo in sampleEntityPhones.values()) {
                if (entityPhoneBo.entityId.equals(map.get("entityId"))
                    && entityPhoneBo.entityTypeCode.equals(map.get("entityTypeCode"))
                    && entityPhoneBo.phoneTypeCode.equals(map.get("phoneTypeCode"))
                    && entityPhoneBo.active)
                {
                    return entityPhoneBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityPhoneBo bo -> return existingEntityPhoneBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhone entityPhone = identityService.updatePhone(EntityPhoneBo.to(existingEntityPhoneBo));

        Assert.assertEquals(EntityPhoneBo.to(existingEntityPhoneBo), entityPhone);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivatePhoneWithNonExistentPhoneFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhone entityPhone = identityService.inactivatePhone("new");
    }

    @Test
    public void testInactivatePhoneSucceeds()
    {
        EntityPhoneBo existingEntityPhoneBo = sampleEntityPhones.get("AAA");
        EntityPhoneTypeBo firstPhoneType = new EntityPhoneTypeBo(code: "phonetypecodeone");
        EntityPhoneBo inactiveEntityPhoneBo = new EntityPhoneBo(entityId: "AAA", entityTypeCode: "typecodeone", phoneType: firstPhoneType, id: "phoneidone", phoneTypeCode: "phonetypecodeone", active: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityPhones.size()) {
            Class clazz, Map map -> for (EntityPhoneBo entityPhoneBo in sampleEntityPhones.values()) {
                if (entityPhoneBo.id.equals(map.get("id"))) {
                    return entityPhoneBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityPhoneBo bo -> return inactiveEntityPhoneBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPhone inactiveEntityPhone = identityService.inactivatePhone(existingEntityPhoneBo.id);

        Assert.assertEquals(EntityPhoneBo.to(inactiveEntityPhoneBo), inactiveEntityPhone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaddExternalIdentifierToEntityWithNullFails() {
        EntityExternalIdentifier entityExternalIdentifier = identityService.addExternalIdentifierToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddExternalIdentifierToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityExternalIdentifiers.size()) {
            Class clazz, Map map -> for (EntityExternalIdentifierBo entityExternalIdentifierBo in sampleEntityExternalIdentifiers.values()) {
                if (entityExternalIdentifierBo.entityId.equals(map.get("entityId"))
                    && entityExternalIdentifierBo.externalIdentifierTypeCode.equals(map.get("externalIdentifierTypeCode")))
                {
                    return entityExternalIdentifierBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityExternalIdentifierTypeBo firstExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodeone");
        EntityExternalIdentifierBo newEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "AAA", externalIdentifierType: firstExternalIdentifierType, id: "exidone", externalIdentifierTypeCode: "exidtypecodeone");
        EntityExternalIdentifier entityExternalIdentifier = identityService.addExternalIdentifierToEntity(EntityExternalIdentifierBo.to(newEntityExternalIdentifierBo));
    }

    @Test
    public void testAddExternalIdentifierToEntitySucceeds() {
        EntityExternalIdentifierTypeBo firstExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodethree");
        EntityExternalIdentifierBo newEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "CCC", externalIdentifierType: firstExternalIdentifierType, id: "exidthree", externalIdentifierTypeCode: "exidtypecodethree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityExternalIdentifiers.size()) {
            Class clazz, Map map -> for (EntityExternalIdentifierBo entityExternalIdentifierBo in sampleEntityExternalIdentifiers.values()) {
                if (entityExternalIdentifierBo.entityId.equals(map.get("entityId"))
                    && entityExternalIdentifierBo.externalIdentifierTypeCode.equals(map.get("externalIdentifierTypeCode")))
                {
                    return entityExternalIdentifierBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityExternalIdentifierBo bo -> return newEntityExternalIdentifierBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityExternalIdentifier entityExternalIdentifier = identityService.addExternalIdentifierToEntity(EntityExternalIdentifierBo.to(newEntityExternalIdentifierBo));

        Assert.assertEquals(EntityExternalIdentifierBo.to(newEntityExternalIdentifierBo), entityExternalIdentifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateExternalIdentifierWithNullFails() {
        EntityExternalIdentifier entityExternalIdentifier = identityService.updateExternalIdentifier(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateExternalIdentifierWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityExternalIdentifiers.size()) {
            Class clazz, Map map -> for (EntityExternalIdentifierBo entityExternalIdentifierBo in sampleEntityExternalIdentifiers.values()) {
                if (entityExternalIdentifierBo.entityId.equals(map.get("entityId"))
                    && entityExternalIdentifierBo.externalIdentifierTypeCode.equals(map.get("externalIdentifierTypeCode")))
                {
                    return entityExternalIdentifierBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityExternalIdentifierTypeBo firstExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodethree");
        EntityExternalIdentifierBo newEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "CCC", externalIdentifierType: firstExternalIdentifierType, id: "exidthree", externalIdentifierTypeCode: "exidtypecodethree");
        EntityExternalIdentifier entityExternalIdentifier = identityService.updateExternalIdentifier(EntityExternalIdentifierBo.to(newEntityExternalIdentifierBo));
    }

    @Test
    public void testUpdateExternalIdentifierSucceeds() {
        EntityExternalIdentifierTypeBo firstExternalIdentifierType = new EntityExternalIdentifierTypeBo(code: "exidtypecodeone");
        EntityExternalIdentifierBo existingEntityExternalIdentifierBo = new EntityExternalIdentifierBo(entityId: "AAA", externalIdentifierType: firstExternalIdentifierType, id: "exidone", externalIdentifierTypeCode: "exidtypecodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityExternalIdentifiers.size()) {
            Class clazz, Map map -> for (EntityExternalIdentifierBo entityExternalIdentifierBo in sampleEntityExternalIdentifiers.values()) {
                if (entityExternalIdentifierBo.entityId.equals(map.get("entityId"))
                    && entityExternalIdentifierBo.externalIdentifierTypeCode.equals(map.get("externalIdentifierTypeCode")))
                {
                    return entityExternalIdentifierBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityExternalIdentifierBo bo -> return existingEntityExternalIdentifierBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityExternalIdentifier entityExternalIdentifier = identityService.updateExternalIdentifier(EntityExternalIdentifierBo.to(existingEntityExternalIdentifierBo));

        Assert.assertEquals(EntityExternalIdentifierBo.to(existingEntityExternalIdentifierBo), entityExternalIdentifier);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testaddAffiliationToEntityWithNullFails() {
        EntityAffiliation entityAffiliation = identityService.addAffiliationToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testaddAffiliationToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityAffiliations.size()) {
            Class clazz, Map map -> for (EntityAffiliationBo entityAffiliationBo in sampleEntityAffiliations.values()) {
                if (entityAffiliationBo.id.equals(map.get("id")))
                {
                    return entityAffiliationBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo newEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityAffiliation entityAffiliation = identityService.addAffiliationToEntity(EntityAffiliationBo.to(newEntityAffiliationBo));
    }

    @Test
    public void testAddAffiliationToEntitySucceeds() {
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodethree");
        EntityAffiliationBo newEntityAffiliationBo = new EntityAffiliationBo(entityId: "CCC", affiliationType: firstAffiliationType, id: "affiliationidthree", affiliationTypeCode: "affiliationcodethree", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAffiliations.size()) {
            Class clazz, Map map -> for (EntityAffiliationBo entityAffiliationBo in sampleEntityAffiliations.values()) {
                if (entityAffiliationBo.id.equals(map.get("id")))
                {
                    return entityAffiliationBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAffiliationBo bo -> return newEntityAffiliationBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliation entityAffiliation = identityService.addAffiliationToEntity(EntityAffiliationBo.to(newEntityAffiliationBo));

        Assert.assertEquals(EntityAffiliationBo.to(newEntityAffiliationBo), entityAffiliation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateAffiliationWithNullFails() {
        EntityAffiliation entityAffiliation = identityService.updateAffiliation(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateAffiliationWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityAffiliations.size()) {
            Class clazz, Map map -> for (EntityAffiliationBo entityAffiliationBo in sampleEntityAffiliations.values()) {
                if (entityAffiliationBo.id.equals(map.get("id")))
                {
                    return entityAffiliationBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodethree");
        EntityAffiliationBo newEntityAffiliationBo = new EntityAffiliationBo(entityId: "CCC", affiliationType: firstAffiliationType, id: "affiliationidthree", affiliationTypeCode: "affiliationcodethree", active: true);
        EntityAffiliation entityAffiliation = identityService.updateAffiliation(EntityAffiliationBo.to(newEntityAffiliationBo));
    }

    @Test
    public void testUpdateAffiliationSucceeds() {
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo existingEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAffiliations.size()) {
            Class clazz, Map map -> for (EntityAffiliationBo entityAffiliationBo in sampleEntityAffiliations.values()) {
                if (entityAffiliationBo.id.equals(map.get("id")))
                {
                    return entityAffiliationBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAffiliationBo bo -> return existingEntityAffiliationBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliation entityAffiliation = identityService.updateAffiliation(EntityAffiliationBo.to(existingEntityAffiliationBo));

        Assert.assertEquals(EntityAffiliationBo.to(existingEntityAffiliationBo), entityAffiliation);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateAffiliationWithNonExistentIdFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliation entityAffiliation = identityService.inactivateAffiliation("new");
    }

    @Test
    public void testInactivateAffiliationSucceeds()
    {

        EntityAffiliationBo existingEntityAffiliationBo = sampleEntityAffiliations.get("AAA");
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo inactiveEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityAffiliations.size()) {
            Class clazz, Map map -> for (EntityAffiliationBo entityAffiliationBo in sampleEntityAffiliations.values()) {
                if (entityAffiliationBo.id.equals(map.get("id")))
                {
                    return entityAffiliationBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityAffiliationBo bo -> return inactiveEntityAffiliationBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliation inactiveEntityAffiliation = identityService.inactivateAffiliation(existingEntityAffiliationBo.id);

        Assert.assertEquals(EntityAffiliationBo.to(inactiveEntityAffiliationBo), inactiveEntityAffiliation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEntitiesWithNullFails() {
        EntityQueryResults entityQueryResults = identityService.findEntities(null);
    }

    @Test
    public void testFindEntitiesSucceeds() {
        GenericQueryResults.Builder<EntityBo> genericQueryResults = new GenericQueryResults.Builder<EntityBo>();
        genericQueryResults.totalRowCount = 0;
        genericQueryResults.moreResultsAvailable = false;
        List<EntityBo> entities = new ArrayList<EntityBo>();
        entities.add(new EntityBo(active: true, id: "AAA"));
        genericQueryResults.results = entities;
        GenericQueryResults<EntityBo> results = genericQueryResults.build();

        mockCriteriaLookupService.demand.lookup(1..1) {
            Class<EntityBo> queryClass, QueryByCriteria criteria -> return results;
        }

        injectCriteriaLookupServiceIntoIdentityService();

        QueryByCriteria.Builder queryByCriteriaBuilder = new QueryByCriteria.Builder();
        queryByCriteriaBuilder.setStartAtIndex(0);
        queryByCriteriaBuilder.setCountFlag(CountFlag.NONE);
        EqualPredicate equalExpression = new EqualPredicate("entity.entityId", new CriteriaStringValue("AAA"));
        queryByCriteriaBuilder.setPredicates(equalExpression);
        EntityQueryResults entityQueryResults = identityService.findEntities(queryByCriteriaBuilder.build());

        Assert.assertEquals(entityQueryResults.results[0], EntityBo.to(entities[0]));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindEntityDefaultsWithNullFails() {
        EntityDefaultQueryResults entityDefaultQueryResults = identityService.findEntityDefaults(null);
    }

    @Test
    public void testFindEntityDefaultsSucceeds() {
        EntityDefault.Builder entityBuilder = EntityDefault.Builder.create("AAA");
        GenericQueryResults.Builder<EntityBo> genericQueryResults = new GenericQueryResults.Builder<EntityBo>();
        genericQueryResults.totalRowCount = 0;
        genericQueryResults.moreResultsAvailable = false;
        List<EntityBo> entities = new ArrayList<EntityBo>();
        entities.add(new EntityBo(active: true, id: "AAA"));
        genericQueryResults.results = entities;
        GenericQueryResults<EntityBo> results = genericQueryResults.build();

        mockCriteriaLookupService.demand.lookup(1..1) {
            Class<EntityBo> queryClass, QueryByCriteria criteria -> return results;
        }

        injectCriteriaLookupServiceIntoIdentityService();

        QueryByCriteria.Builder queryByCriteriaBuilder = new QueryByCriteria.Builder();
        queryByCriteriaBuilder.setStartAtIndex(0);
        queryByCriteriaBuilder.setCountFlag(CountFlag.NONE);
        EqualPredicate equalExpression = new EqualPredicate("entity.entityId", new CriteriaStringValue("AAA"));
        queryByCriteriaBuilder.setPredicates(equalExpression);
        EntityDefaultQueryResults entityDefaultQueryResults = identityService.findEntityDefaults(queryByCriteriaBuilder.build());

        // because findEntityDefaults builds the EntityDefault list from the results, we cannot compare entityBuilder.build() in its entirety to the results in their entirety
        Assert.assertEquals(entityDefaultQueryResults.results[0].entityId, entityBuilder.build().entityId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNamesWithNullFails() {
        EntityNameQueryResults entityNameQueryResults = identityService.findNames(null);
    }

    @Test
    public void testFindNamesSucceeds() {
        EntityName.Builder entityBuilder = EntityName.Builder.create();
        entityBuilder.setFirstName("John");
        entityBuilder.setLastName("Smith");
        GenericQueryResults.Builder<EntityNameBo> genericQueryResults = new GenericQueryResults.Builder<EntityNameBo>();
        genericQueryResults.totalRowCount = 0;
        genericQueryResults.moreResultsAvailable = false;
        List<EntityNameBo> entityNames = new ArrayList<EntityNameBo>();
        entityNames.add(new EntityNameBo(firstName: "John", lastName: "Smith"));
        genericQueryResults.results = entityNames;
        GenericQueryResults<EntityNameBo> results = genericQueryResults.build();

        mockCriteriaLookupService.demand.lookup(1..1) {
            Class<EntityNameBo> queryClass, QueryByCriteria criteria -> return results;
        }

        injectCriteriaLookupServiceIntoIdentityService();

        QueryByCriteria.Builder queryByCriteriaBuilder = new QueryByCriteria.Builder();
        queryByCriteriaBuilder.setStartAtIndex(0);
        queryByCriteriaBuilder.setCountFlag(CountFlag.NONE);
        EqualPredicate equalExpression = new EqualPredicate("entityName.lastName", new CriteriaStringValue("Smith"));
        queryByCriteriaBuilder.setPredicates(equalExpression);
        EntityNameQueryResults entityNameQueryResults = identityService.findNames(queryByCriteriaBuilder.build());

        Assert.assertEquals(entityNameQueryResults.results[0], entityBuilder.build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEntityWithNullFails() {
        Entity entity = identityService.createEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testCreateEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                if (entityBo.id.equals(map.get("id")))
                {
                    return entityBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPrivacyPreferencesBo firstEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo firstEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        PrincipalBo firstEntityPrincipal = new PrincipalBo(entityId: "AAA", principalId: "P1", active: true, principalName: "first", versionNumber: 1, password: "first_password");
        List<PrincipalBo> firstPrincipals = new ArrayList<PrincipalBo>();
        firstPrincipals.add(firstEntityPrincipal);
        EntityBo newEntityBo = new EntityBo(active: true, id: "AAA", privacyPreferences: firstEntityPrivacyPreferencesBo, bioDemographics: firstEntityBioDemographicsBo, principals: firstPrincipals);
        Entity entity = identityService.createEntity(EntityBo.to(newEntityBo));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEntityWithNullFails() {
        Entity entity = identityService.updateEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateEntityWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                if (entityBo.id.equals(map.get("id")))
                {
                    return entityBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityBo newEntityBo = new EntityBo(active: true, id: "CCC");
        Entity entity = identityService.updateEntity(EntityBo.to(newEntityBo));
    }

    @Test
    public void testUpdateEntitySucceeds() {
        EntityPrivacyPreferencesBo firstEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo firstEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        PrincipalBo firstEntityPrincipal = new PrincipalBo(entityId: "AAA", principalId: "P1", active: true, principalName: "first", versionNumber: 1, password: "first_password");
        List<PrincipalBo> firstPrincipals = new ArrayList<PrincipalBo>();
        firstPrincipals.add(firstEntityPrincipal);
        EntityBo existingEntityBo = new EntityBo(active: true, id: "AAA", privacyPreferences: firstEntityPrivacyPreferencesBo, bioDemographics: firstEntityBioDemographicsBo, principals: firstPrincipals);

        mockBoService.demand.findByPrimaryKey(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                if (entityBo.id.equals(map.get("id")))
                {
                    return entityBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityBo bo -> return existingEntityBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Entity entity = identityService.updateEntity(EntityBo.to(existingEntityBo));

        Assert.assertEquals(EntityBo.to(existingEntityBo), entity);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateEntityWithNonExistentEntityFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Entity entity = identityService.inactivateEntity("new");
    }

    @Test
    public void testInactivateEntitySucceeds()
    {
        EntityBo existingEntityBo = sampleEntities.get("AAA");
        EntityPrivacyPreferencesBo firstEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo firstEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        PrincipalBo firstEntityPrincipal = new PrincipalBo(entityId: "AAA", principalId: "P1", active: true, principalName: "first", versionNumber: 1, password: "first_password");
        List<PrincipalBo> firstPrincipals = new ArrayList<PrincipalBo>();
        firstPrincipals.add(firstEntityPrincipal);
        EntityBo inactiveEntityBo = new EntityBo(active: false, id: "AAA", privacyPreferences: firstEntityPrivacyPreferencesBo, bioDemographics: firstEntityBioDemographicsBo, principals: firstPrincipals);

        mockBoService.demand.findByPrimaryKey(1..sampleEntities.size()) {
            Class clazz, Map map -> for (EntityBo entityBo in sampleEntities.values()) {
                if (entityBo.id.equals(map.get("id")))
                {
                    return entityBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityBo bo -> return inactiveEntityBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        Entity inactiveEntity = identityService.inactivateEntity(existingEntityBo.id);

        Assert.assertEquals(EntityBo.to(inactiveEntityBo), inactiveEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPrivacyPreferencesToEntityWithNullFails() {
        EntityPrivacyPreferences entityPrivacyPreferences = identityService.addPrivacyPreferencesToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddPrivacyPreferencesToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityPrivacyPreferences.size()) {
            Class clazz, Map map -> for (EntityPrivacyPreferencesBo entityPrivacyPreferencesBo in sampleEntityPrivacyPreferences.values()) {
                if (entityPrivacyPreferencesBo.entityId.equals(map.get("entityId")))
                {
                    return entityPrivacyPreferencesBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPrivacyPreferencesBo newEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        EntityPrivacyPreferences entityPrivacyPreferences = identityService.addPrivacyPreferencesToEntity(EntityPrivacyPreferencesBo.to(newEntityPrivacyPreferencesBo));
    }

    @Test
    public void testAddPrivacyPreferencesToEntitySucceeds() {
        EntityPrivacyPreferencesBo newEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "CCC", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityPrivacyPreferences.size()) {
            Class clazz, Map map -> for (EntityPrivacyPreferencesBo entityPrivacyPreferencesBo in sampleEntityPrivacyPreferences.values()) {
                if (entityPrivacyPreferencesBo.entityId.equals(map.get("entityId")))
                {
                    return entityPrivacyPreferencesBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityPrivacyPreferencesBo bo -> return newEntityPrivacyPreferencesBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPrivacyPreferences entityPrivacyPreferences = identityService.addPrivacyPreferencesToEntity(EntityPrivacyPreferencesBo.to(newEntityPrivacyPreferencesBo));

        Assert.assertEquals(EntityPrivacyPreferencesBo.to(newEntityPrivacyPreferencesBo), entityPrivacyPreferences);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdatePrivacyPreferencesWithNullFails() {
        EntityPrivacyPreferences entityPrivacyPreferences = identityService.updatePrivacyPreferences(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdatePrivacyPreferencesWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityPrivacyPreferences.size()) {
            Class clazz, Map map -> for (EntityPrivacyPreferencesBo entityPrivacyPreferencesBo in sampleEntityPrivacyPreferences.values()) {
                if (entityPrivacyPreferencesBo.entityId.equals(map.get("entityId")))
                {
                    return entityPrivacyPreferencesBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPrivacyPreferencesBo newEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "CCC", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);
        EntityPrivacyPreferences entityPrivacyPreferences = identityService.updatePrivacyPreferences(EntityPrivacyPreferencesBo.to(newEntityPrivacyPreferencesBo));
    }

    @Test
    public void testUpdatePrivacyPreferencesSucceeds() {
        EntityPrivacyPreferencesBo existingEntityPrivacyPreferencesBo = new EntityPrivacyPreferencesBo(entityId: "AAA", suppressName: true, suppressEmail: true, suppressAddress: true, suppressPhone: true, suppressPersonal: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityPrivacyPreferences.size()) {
            Class clazz, Map map -> for (EntityPrivacyPreferencesBo entityPrivacyPreferencesBo in sampleEntityPrivacyPreferences.values()) {
                if (entityPrivacyPreferencesBo.entityId.equals(map.get("entityId")))
                {
                    return entityPrivacyPreferencesBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityPrivacyPreferencesBo bo -> return existingEntityPrivacyPreferencesBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityPrivacyPreferences entityPrivacyPreferences = identityService.updatePrivacyPreferences(EntityPrivacyPreferencesBo.to(existingEntityPrivacyPreferencesBo));

        Assert.assertEquals(EntityPrivacyPreferencesBo.to(existingEntityPrivacyPreferencesBo), entityPrivacyPreferences);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddCitizenshipToEntityWithNullFails() {
        EntityCitizenship entityCitizenship = identityService.addCitizenshipToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddCitizenshipToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityCitizenships.size()) {
            Class clazz, Map map -> for (EntityCitizenshipBo entityCitizenshipBo in sampleEntityCitizenships.values()) {
                if (entityCitizenshipBo.entityId.equals(map.get("entityId"))
                && entityCitizenshipBo.statusCode.equals(map.get("statusCode"))
                && entityCitizenshipBo.active)
                {
                    return entityCitizenshipBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo newEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "AAA", id: "citizenshipidone", active: true, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");
        EntityCitizenship entityCitizenship = identityService.addCitizenshipToEntity(EntityCitizenshipBo.to(newEntityCitizenshipBo));
    }

    @Test
    public void testAddCitizenshipToEntitySucceeds() {
        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo newEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "CCC", id: "citizenshipidthree", active: true, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityCitizenships.size()) {
            Class clazz, Map map -> for (EntityCitizenshipBo entityCitizenshipBo in sampleEntityCitizenships.values()) {
                if (entityCitizenshipBo.entityId.equals(map.get("entityId"))
                && entityCitizenshipBo.statusCode.equals(map.get("statusCode"))
                && entityCitizenshipBo.active)
                {
                    return entityCitizenshipBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityCitizenshipBo bo -> return newEntityCitizenshipBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenship entityCitizenship = identityService.addCitizenshipToEntity(EntityCitizenshipBo.to(newEntityCitizenshipBo));

        Assert.assertEquals(EntityCitizenshipBo.to(newEntityCitizenshipBo), entityCitizenship);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCitizenshipWithNullFails() {
        EntityCitizenship entityCitizenship = identityService.updateCitizenship(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateCitizenshipWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityCitizenships.size()) {
            Class clazz, Map map -> for (EntityCitizenshipBo entityCitizenshipBo in sampleEntityCitizenships.values()) {
                if (entityCitizenshipBo.entityId.equals(map.get("entityId"))
                && entityCitizenshipBo.statusCode.equals(map.get("statusCode"))
                && entityCitizenshipBo.active)
                {
                    return entityCitizenshipBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo newEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "CCC", id: "citizenshipidthree", active: true, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");
        EntityCitizenship entityCitizenship = identityService.updateCitizenship(EntityCitizenshipBo.to(newEntityCitizenshipBo));
    }

    @Test
    public void testUpdateCitizenshipSucceeds() {
        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo existingEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "AAA", id: "citizenshipidone", active: true, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityCitizenships.size()) {
            Class clazz, Map map -> for (EntityCitizenshipBo entityCitizenshipBo in sampleEntityCitizenships.values()) {
                if (entityCitizenshipBo.entityId.equals(map.get("entityId"))
                && entityCitizenshipBo.statusCode.equals(map.get("statusCode"))
                && entityCitizenshipBo.active)
                {
                    return entityCitizenshipBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityCitizenshipBo bo -> return existingEntityCitizenshipBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenship entityCitizenship = identityService.updateCitizenship(EntityCitizenshipBo.to(existingEntityCitizenshipBo));

        Assert.assertEquals(EntityCitizenshipBo.to(existingEntityCitizenshipBo), entityCitizenship);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateCitizenshipWithNonExistentEntityFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenship entityCitizenship = identityService.inactivateCitizenship("new");
    }

    @Test
    public void testInactivateCitizenshipSucceeds()
    {
        EntityCitizenshipBo existingEntityCitizenshipBo = sampleEntityCitizenships.get("AAA");
        EntityCitizenshipStatusBo firstEntityCitizenshipStatus = new EntityCitizenshipStatusBo(code: "statuscodeone", name: "statusnameone");
        EntityCitizenshipBo inactiveEntityCitizenshipBo = new EntityCitizenshipBo(entityId: "AAA", id: "citizenshipidone", active: false, status: firstEntityCitizenshipStatus, statusCode: "statuscodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityCitizenships.size()) {
            Class clazz, Map map -> for (EntityCitizenshipBo entityCitizenshipBo in sampleEntityCitizenships.values()) {
                if (entityCitizenshipBo.id.equals(map.get("id"))
                && entityCitizenshipBo.active)
                {
                    return entityCitizenshipBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityCitizenshipBo bo -> return inactiveEntityCitizenshipBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityCitizenship inactiveEntityCitizenship = identityService.inactivateCitizenship(existingEntityCitizenshipBo.id);

        Assert.assertEquals(EntityCitizenshipBo.to(inactiveEntityCitizenshipBo), inactiveEntityCitizenship);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEthnicityToEntityWithNullFails() {
        EntityEthnicity entityEthnicity = identityService.addEthnicityToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddEthnicityToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEthnicities.size()) {
            Class clazz, Map map -> for (EntityEthnicityBo entityEthnicityBo in sampleEntityEthnicities.values()) {
                if (entityEthnicityBo.id.equals(map.get("id")))
                {
                    return entityEthnicityBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEthnicityBo newEntityEthnicityBo = new EntityEthnicityBo(entityId: "AAA", id: "ethnicityidone");
        EntityEthnicity entityEthnicity = identityService.addEthnicityToEntity(EntityEthnicityBo.to(newEntityEthnicityBo));
    }

    @Test
    public void testAddEthnicityToEntitySucceeds() {
        EntityEthnicityBo newEntityEthnicityBo = new EntityEthnicityBo(entityId: "CCC", id: "ethnicityidthree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEthnicities.size()) {
            Class clazz, Map map -> for (EntityEthnicityBo entityEthnicityBo in sampleEntityEthnicities.values()) {
                if (entityEthnicityBo.id.equals(map.get("id")))
                {
                    return entityEthnicityBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEthnicityBo bo -> return newEntityEthnicityBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEthnicity entityEthnicity = identityService.addEthnicityToEntity(EntityEthnicityBo.to(newEntityEthnicityBo));

        Assert.assertEquals(EntityEthnicityBo.to(newEntityEthnicityBo), entityEthnicity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEthnicityWithNullFails() {
        EntityEthnicity entityEthnicity = identityService.updateEthnicity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateEthnicityWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEthnicities.size()) {
            Class clazz, Map map -> for (EntityEthnicityBo entityEthnicityBo in sampleEntityEthnicities.values()) {
                if (entityEthnicityBo.id.equals(map.get("id")))
                {
                    return entityEthnicityBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEthnicityBo newEntityEthnicityBo = new EntityEthnicityBo(entityId: "CCC", id: "ethnicityidthree");
        EntityEthnicity entityEthnicity = identityService.updateEthnicity(EntityEthnicityBo.to(newEntityEthnicityBo));
    }

    @Test
    public void testUpdateEthnicitySucceeds() {
        EntityEthnicityBo existingEntityEthnicityBo = new EntityEthnicityBo(entityId: "AAA", id: "ethnicityidone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEthnicities.size()) {
            Class clazz, Map map -> for (EntityEthnicityBo entityEthnicityBo in sampleEntityEthnicities.values()) {
                if (entityEthnicityBo.id.equals(map.get("id")))
                {
                    return entityEthnicityBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEthnicityBo bo -> return existingEntityEthnicityBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEthnicity entityEthnicity = identityService.updateEthnicity(EntityEthnicityBo.to(existingEntityEthnicityBo));

        Assert.assertEquals(EntityEthnicityBo.to(existingEntityEthnicityBo), entityEthnicity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddResidencyToEntityWithNullFails() {
        EntityResidency entityResidency = identityService.addResidencyToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddResidencyToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityResidencies.size()) {
            Class clazz, Map map -> for (EntityResidencyBo entityResidencyBo in sampleEntityResidencies.values()) {
                if (entityResidencyBo.id.equals(map.get("id")))
                {
                    return entityResidencyBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityResidencyBo newEntityResidencyBo = new EntityResidencyBo(entityId: "AAA", id: "residencyidone");
        EntityResidency entityResidency = identityService.addResidencyToEntity(EntityResidencyBo.to(newEntityResidencyBo));
    }

    @Test
    public void testAddResidencyToEntitySucceeds() {
        EntityResidencyBo newEntityResidencyBo = new EntityResidencyBo(entityId: "CCC", id: "residencyidthree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityResidencies.size()) {
            Class clazz, Map map -> for (EntityResidencyBo entityResidencyBo in sampleEntityResidencies.values()) {
                if (entityResidencyBo.id.equals(map.get("id")))
                {
                    return entityResidencyBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityResidencyBo bo -> return newEntityResidencyBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityResidency entityResidency = identityService.addResidencyToEntity(EntityResidencyBo.to(newEntityResidencyBo));

        Assert.assertEquals(EntityResidencyBo.to(newEntityResidencyBo), entityResidency);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateResidencyWithNullFails() {
        EntityResidency entityResidency = identityService.updateResidency(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateResidencyWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityResidencies.size()) {
            Class clazz, Map map -> for (EntityResidencyBo entityResidencyBo in sampleEntityResidencies.values()) {
                if (entityResidencyBo.id.equals(map.get("id")))
                {
                    return entityResidencyBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityResidencyBo newEntityResidencyBo = new EntityResidencyBo(entityId: "CCC", id: "residencyidthree");
        EntityResidency entityResidency = identityService.updateResidency(EntityResidencyBo.to(newEntityResidencyBo));
    }

    @Test
    public void testUpdateResidencySucceeds() {
        EntityResidencyBo existingEntityResidencyBo = new EntityResidencyBo(entityId: "AAA", id: "residencyidone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityResidencies.size()) {
            Class clazz, Map map -> for (EntityResidencyBo entityResidencyBo in sampleEntityResidencies.values()) {
                if (entityResidencyBo.id.equals(map.get("id")))
                {
                    return entityResidencyBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityResidencyBo bo -> return existingEntityResidencyBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityResidency entityResidency = identityService.updateResidency(EntityResidencyBo.to(existingEntityResidencyBo));

        Assert.assertEquals(EntityResidencyBo.to(existingEntityResidencyBo), entityResidency);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddVisaToEntityWithNullFails() {
        EntityVisa entityVisa = identityService.addVisaToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddVisaToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityVisas.size()) {
            Class clazz, Map map -> for (EntityVisaBo entityVisaBo in sampleEntityVisas.values()) {
                if (entityVisaBo.id.equals(map.get("id")))
                {
                    return entityVisaBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityVisaBo newEntityVisaBo = new EntityVisaBo(entityId: "AAA", id: "visaidone");
        EntityVisa entityVisa = identityService.addVisaToEntity(EntityVisaBo.to(newEntityVisaBo));
    }

    @Test
    public void testAddVisaToEntitySucceeds() {
        EntityVisaBo newEntityVisaBo = new EntityVisaBo(entityId: "CCC", id: "visaidthree");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityVisas.size()) {
            Class clazz, Map map -> for (EntityVisaBo entityVisaBo in sampleEntityVisas.values()) {
                if (entityVisaBo.id.equals(map.get("id")))
                {
                    return entityVisaBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityVisaBo bo -> return newEntityVisaBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityVisa entityVisa = identityService.addVisaToEntity(EntityVisaBo.to(newEntityVisaBo));

        Assert.assertEquals(EntityVisaBo.to(newEntityVisaBo), entityVisa);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateVisaWithNullFails() {
        EntityVisa entityVisa = identityService.updateVisa(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateVisaWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityVisas.size()) {
            Class clazz, Map map -> for (EntityVisaBo entityVisaBo in sampleEntityVisas.values()) {
                if (entityVisaBo.id.equals(map.get("id")))
                {
                    return entityVisaBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityVisaBo newEntityVisaBo = new EntityVisaBo(entityId: "CCC", id: "visaidthree");
        EntityVisa entityVisa = identityService.updateVisa(EntityVisaBo.to(newEntityVisaBo));
    }

    @Test
    public void testUpdateVisaSucceeds() {
        EntityVisaBo existingEntityVisaBo = new EntityVisaBo(entityId: "AAA", id: "visaidone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityVisas.size()) {
            Class clazz, Map map -> for (EntityVisaBo entityVisaBo in sampleEntityVisas.values()) {
                if (entityVisaBo.id.equals(map.get("id")))
                {
                    return entityVisaBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityVisaBo bo -> return existingEntityVisaBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityVisa entityVisa = identityService.updateVisa(EntityVisaBo.to(existingEntityVisaBo));

        Assert.assertEquals(EntityVisaBo.to(existingEntityVisaBo), entityVisa);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNameToEntityWithNullFails() {
        EntityName entityName = identityService.addNameToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddNameToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityNames.size()) {
            Class clazz, Map map -> for (EntityNameBo entityNameBo in sampleEntityNames.values()) {
                if (entityNameBo.id.equals(map.get("id")))
                {
                    return entityNameBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityNameBo newEntityNameBo = new EntityNameBo(entityId: "AAA", id: "nameidone", active: true, firstName: "John", lastName: "Smith");
        EntityName entityName = identityService.addNameToEntity(EntityNameBo.to(newEntityNameBo));
    }

    @Test
    public void testAddNameToEntitySucceeds() {
        EntityNameTypeBo firstEntityNameType = new EntityNameTypeBo(code: "namecodeone");
        EntityNameBo newEntityNameBo = new EntityNameBo(entityId: "CCC", id: "nameidthree", active: true, firstName: "Willard", lastName: "Jackson", nameType: firstEntityNameType, nameCode: "namecodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityNames.size()) {
            Class clazz, Map map -> for (EntityNameBo entityNameBo in sampleEntityNames.values()) {
                if (entityNameBo.id.equals(map.get("id")))
                {
                    return entityNameBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityNameBo bo -> return newEntityNameBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityName entityName = identityService.addNameToEntity(EntityNameBo.to(newEntityNameBo));

        Assert.assertEquals(EntityNameBo.to(newEntityNameBo), entityName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNameWithNullFails() {
        EntityName entityName = identityService.updateName(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateNameWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityNames.size()) {
            Class clazz, Map map -> for (EntityNameBo entityNameBo in sampleEntityNames.values()) {
                if (entityNameBo.id.equals(map.get("id")))
                {
                    return entityNameBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityNameTypeBo firstEntityNameType = new EntityNameTypeBo(code: "namecodeone");
        EntityNameBo newEntityNameBo = new EntityNameBo(entityId: "CCC", id: "nameidthree", active: true, firstName: "Willard", lastName: "Jackson", nameType: firstEntityNameType, nameCode: "namecodeone");
        EntityName entityName = identityService.updateName(EntityNameBo.to(newEntityNameBo));
    }

    @Test
    public void testUpdateNameSucceeds() {
        EntityNameTypeBo firstEntityNameType = new EntityNameTypeBo(code: "namecodeone");
        EntityNameBo existingEntityNameBo = new EntityNameBo(entityId: "AAA", id: "nameidone", active: true, firstName: "John", lastName: "Smith", nameType: firstEntityNameType, nameCode: "namecodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityNames.size()) {
            Class clazz, Map map -> for (EntityNameBo entityNameBo in sampleEntityNames.values()) {
                if (entityNameBo.id.equals(map.get("id")))
                {
                    return entityNameBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityNameBo bo -> return existingEntityNameBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityName entityName = identityService.updateName(EntityNameBo.to(existingEntityNameBo));

        Assert.assertEquals(EntityNameBo.to(existingEntityNameBo), entityName);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateNameWithNonExistentNameFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityName entityName = identityService.inactivateName("new");
    }

    @Test
    public void testInactivateNameSucceeds()
    {
        EntityNameBo existingEntityNameBo = sampleEntityNames.get("AAA");
        EntityNameTypeBo firstEntityNameType = new EntityNameTypeBo(code: "namecodeone");
        EntityNameBo inactiveEntityNameBo = new EntityNameBo(entityId: "AAA", id: "nameidone", active: false, firstName: "John", lastName: "Smith", nameType: firstEntityNameType, nameCode: "namecodeone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityNames.size()) {
            Class clazz, Map map -> for (EntityNameBo entityNameBo in sampleEntityNames.values()) {
                if (entityNameBo.id.equals(map.get("id")))
                {
                    return entityNameBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityNameBo bo -> return inactiveEntityNameBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityName inactiveEntityName = identityService.inactivateName(existingEntityNameBo.id);

        Assert.assertEquals(EntityNameBo.to(existingEntityNameBo), inactiveEntityName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddEmploymentToEntityWithNullFails() {
        EntityEmployment entityEmployment = identityService.addEmploymentToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddEmploymentToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmployments.size()) {
            Class clazz, Map map -> for (EntityEmploymentBo entityEmploymentBo in sampleEntityEmployments.values()) {
                if (entityEmploymentBo.id.equals(map.get("id")))
                {
                    return entityEmploymentBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmploymentBo newEntityEmploymentBo = new EntityEmploymentBo(entityId: "AAA", id: "employmentidone");
        EntityEmployment entityEmployment = identityService.addEmploymentToEntity(EntityEmploymentBo.to(newEntityEmploymentBo));
    }

    @Test
    public void testAddEmploymentToEntitySucceeds() {
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo firstEntityAffiliationBo = new EntityAffiliationBo(entityId: "CCC", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityEmploymentTypeBo firstEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodeone");
        EntityEmploymentStatusBo firstEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatusone");
        EntityEmploymentBo newEntityEmploymentBo = new EntityEmploymentBo(entityId: "CCC", id: "employmentidthree", entityAffiliation: firstEntityAffiliationBo, employeeType: firstEmploymentType, employeeStatus: firstEmploymentStatus);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmployments.size()) {
            Class clazz, Map map -> for (EntityEmploymentBo entityEmploymentBo in sampleEntityEmployments.values()) {
                if (entityEmploymentBo.id.equals(map.get("id")))
                {
                    return entityEmploymentBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmploymentBo bo -> return newEntityEmploymentBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmployment entityEmployment = identityService.addEmploymentToEntity(EntityEmploymentBo.to(newEntityEmploymentBo));

        Assert.assertEquals(EntityEmploymentBo.to(newEntityEmploymentBo), entityEmployment);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateEmploymentWithNullFails() {
        EntityEmployment entityEmployment = identityService.updateEmployment(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateEmploymentWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmployments.size()) {
            Class clazz, Map map -> for (EntityEmploymentBo entityEmploymentBo in sampleEntityEmployments.values()) {
                if (entityEmploymentBo.id.equals(map.get("id")))
                {
                    return entityEmploymentBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo firstEntityAffiliationBo = new EntityAffiliationBo(entityId: "CCC", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityEmploymentTypeBo firstEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodeone");
        EntityEmploymentStatusBo firstEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatusone");
        EntityEmploymentBo newEntityEmploymentBo = new EntityEmploymentBo(entityId: "CCC", id: "employmentidthree", entityAffiliation: firstEntityAffiliationBo, employeeType: firstEmploymentType, employeeStatus: firstEmploymentStatus);
        EntityEmployment entityEmployment = identityService.updateEmployment(EntityEmploymentBo.to(newEntityEmploymentBo));
    }

    @Test
    public void testUpdateEmploymentSucceeds() {
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo firstEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityEmploymentTypeBo firstEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodeone");
        EntityEmploymentStatusBo firstEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatusone");
        EntityEmploymentBo existingEntityEmploymentBo = new EntityEmploymentBo(entityId: "AAA", id: "employmentidone", entityAffiliation: firstEntityAffiliationBo, entityAffiliationId: "affiliationidone", employeeType: firstEmploymentType, employeeTypeCode: "employmenttypecodeone", employeeStatus: firstEmploymentStatus, employeeStatusCode: "employmentstatusone");

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmployments.size()) {
            Class clazz, Map map -> for (EntityEmploymentBo entityEmploymentBo in sampleEntityEmployments.values()) {
                if (entityEmploymentBo.entityId.equals(map.get("entityId"))
                && entityEmploymentBo.employeeTypeCode.equals(map.get("employeeTypeCode"))
                && entityEmploymentBo.employeeStatusCode.equals(map.get("employeeStatusCode"))
                && entityEmploymentBo.entityAffiliationId.equals(map.get("entityAffiliationId")))
                {
                    return entityEmploymentBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmploymentBo bo -> return existingEntityEmploymentBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmployment entityEmployment = identityService.updateEmployment(EntityEmploymentBo.to(existingEntityEmploymentBo));

        Assert.assertEquals(EntityEmploymentBo.to(existingEntityEmploymentBo), entityEmployment);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testInactivateEmploymentWithNonExistentObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..1) {
            Class clazz, Map map -> return null;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmployment entityEmployment = identityService.inactivateEmployment("new");
    }

    @Test
    public void testInactivateEmploymentSucceeds()
    {
        EntityEmploymentBo existingEntityEmploymentBo = sampleEntityEmployments.get("AAA");
        EntityAffiliationTypeBo firstAffiliationType = new EntityAffiliationTypeBo(code: "affiliationcodeone");
        EntityAffiliationBo firstEntityAffiliationBo = new EntityAffiliationBo(entityId: "AAA", affiliationType: firstAffiliationType, id: "affiliationidone", affiliationTypeCode: "affiliationcodeone", active: true);
        EntityEmploymentTypeBo firstEmploymentType = new EntityEmploymentTypeBo(code: "employmenttypecodeone");
        EntityEmploymentStatusBo firstEmploymentStatus = new EntityEmploymentStatusBo(code: "employmentstatusone");
        EntityEmploymentBo inactiveEntityEmploymentBo = new EntityEmploymentBo(entityId: "AAA", id: "employmentidone", entityAffiliation: firstEntityAffiliationBo, entityAffiliationId: "affiliationidone", employeeType: firstEmploymentType, employeeTypeCode: "employmenttypecodeone", employeeStatus: firstEmploymentStatus, employeeStatusCode: "employmentstatusone", active: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityEmployments.size()) {
            Class clazz, Map map -> for (EntityEmploymentBo entityEmploymentBo in sampleEntityEmployments.values()) {
                if (entityEmploymentBo.id.equals(map.get("id")))
                {
                    return entityEmploymentBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityEmploymentBo bo -> return inactiveEntityEmploymentBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityEmployment inactiveEntityEmployment = identityService.inactivateEmployment(existingEntityEmploymentBo.id);

        Assert.assertEquals(EntityEmploymentBo.to(existingEntityEmploymentBo).active, inactiveEntityEmployment.active);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBioDemographicsToEntityWithNullFails() {
        EntityBioDemographics entityBioDemographics = identityService.addBioDemographicsToEntity(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testAddBioDemographicsToEntityWithExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityBioDemographics.size()) {
            Class clazz, Map map -> for (EntityBioDemographicsBo entityBioDemographicsBo in sampleEntityBioDemographics.values()) {
                if (entityBioDemographicsBo.entityId.equals(map.get("entityId")))
                {
                    return entityBioDemographicsBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo newEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        EntityBioDemographics entityBioDemographics = identityService.addBioDemographicsToEntity(EntityBioDemographicsBo.to(newEntityBioDemographicsBo));
    }

    @Test
    public void testAddBioDemographicsToEntitySucceeds() {
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo newEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "CCC", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityBioDemographics.size()) {
            Class clazz, Map map -> for (EntityBioDemographicsBo entityBioDemographicsBo in sampleEntityBioDemographics.values()) {
                if (entityBioDemographicsBo.entityId.equals(map.get("entityId")))
                {
                    return entityBioDemographicsBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityBioDemographicsBo bo -> return newEntityBioDemographicsBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityBioDemographics entityBioDemographics = identityService.addBioDemographicsToEntity(EntityBioDemographicsBo.to(newEntityBioDemographicsBo));

        Assert.assertEquals(EntityBioDemographicsBo.to(newEntityBioDemographicsBo), entityBioDemographics);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateBioDemographicsWithNullFails() {
        EntityBioDemographics entityBioDemographics = identityService.updateBioDemographics(null);
    }

    @Test(expected = RiceIllegalStateException.class)
    public void testUpdateBioDemographicsWithNonExistingObjectFails() {
        mockBoService.demand.findByPrimaryKey(1..sampleEntityBioDemographics.size()) {
            Class clazz, Map map -> for (EntityBioDemographicsBo entityBioDemographicsBo in sampleEntityBioDemographics.values()) {
                if (entityBioDemographicsBo.entityId.equals(map.get("entityId")))
                {
                    return entityBioDemographicsBo;
                }
            }
        }

        injectBusinessObjectServiceIntoIdentityService();

        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo newEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "CCC", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);
        EntityBioDemographics entityBioDemographics = identityService.updateBioDemographics(EntityBioDemographicsBo.to(newEntityBioDemographicsBo));
    }

    @Test
    public void testUpdateBioDemographicsSucceeds() {
        String birthDateString = "01/01/2007";
        String deceasedDateString = "01/01/2087";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = formatter.parse(birthDateString);
        Date deceasedDate = formatter.parse(deceasedDateString);
        EntityBioDemographicsBo existingEntityBioDemographicsBo = new EntityBioDemographicsBo(entityId: "AAA", birthDateValue: birthDate, genderCode: "M", deceasedDateValue: deceasedDate, maritalStatusCode: "S", primaryLanguageCode: "EN", secondaryLanguageCode: "FR", birthCountry: "US", birthStateProvinceCode: "IN", birthCity: "Bloomington", geographicOrigin: "None", suppressPersonal: false);

        mockBoService.demand.findByPrimaryKey(1..sampleEntityBioDemographics.size()) {
            Class clazz, Map map -> for (EntityBioDemographicsBo entityBioDemographicsBo in sampleEntityBioDemographics.values()) {
                if (entityBioDemographicsBo.entityId.equals(map.get("entityId")))
                {
                    return entityBioDemographicsBo;
                }
            }
        }

        mockBoService.demand.save(1..1) {
            EntityBioDemographicsBo bo -> return existingEntityBioDemographicsBo;
        }

        injectBusinessObjectServiceIntoIdentityService();

        EntityBioDemographics entityBioDemographics = identityService.updateBioDemographics(EntityBioDemographicsBo.to(existingEntityBioDemographicsBo));

        Assert.assertEquals(EntityBioDemographicsBo.to(existingEntityBioDemographicsBo), entityBioDemographics);
    }
}
