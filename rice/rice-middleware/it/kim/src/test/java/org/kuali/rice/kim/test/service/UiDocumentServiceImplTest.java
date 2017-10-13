/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kim.test.service;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.core.api.membership.MemberType;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.address.EntityAddressContract;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.email.EntityEmailContract;
import org.kuali.rice.kim.api.identity.employment.EntityEmploymentContract;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityNameContract;
import org.kuali.rice.kim.api.identity.phone.EntityPhoneContract;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;
import org.kuali.rice.kim.api.role.RoleMember;
import org.kuali.rice.kim.api.role.RoleMemberContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.bo.ui.KimDocumentRoleMember;
import org.kuali.rice.kim.bo.ui.PersonDocumentAddress;
import org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmail;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo;
import org.kuali.rice.kim.bo.ui.PersonDocumentName;
import org.kuali.rice.kim.bo.ui.PersonDocumentPhone;
import org.kuali.rice.kim.bo.ui.PersonDocumentPrivacy;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.identity.address.EntityAddressTypeBo;
import org.kuali.rice.kim.impl.identity.affiliation.EntityAffiliationTypeBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailTypeBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameTypeBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneTypeBo;
import org.kuali.rice.kim.impl.identity.privacy.EntityPrivacyPreferencesBo;
import org.kuali.rice.kim.impl.role.RoleMemberBo;
import org.kuali.rice.kim.impl.type.KimTypeAttributeBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.kns.kim.type.DataDictionaryTypeServiceBase;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.test.BaselineTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This is a description of what this class does - shyu don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.CLEAR_DB)
public class UiDocumentServiceImplTest extends KIMTestCase {

	private UiDocumentService uiDocumentService;

    @Override
	public void setUp() throws Exception {
		super.setUp();
		uiDocumentService = KIMServiceLocatorInternal.getUiDocumentService();
	}

	@Test
	public void testSaveToEntity() {
	    Person adminPerson = KimApiServiceLocator.getPersonService().getPersonByPrincipalName("admin");
		IdentityManagementPersonDocument personDoc = initPersonDoc();

		/*try {*/
            //personDoc.getDocumentHeader().setWorkflowDocument(KRADServiceLocatorWeb.getWorkflowDocumentService().createWorkflowDocument("TestDocumentType", adminPerson));
            WorkflowDocument document = WorkflowDocumentFactory.createDocument(adminPerson.getPrincipalId(),"TestDocumentType");
            personDoc.getDocumentHeader().setWorkflowDocument(document);
        /*} catch (WorkflowException e) {
            e.printStackTrace();
        }*/
		uiDocumentService.saveEntityPerson(personDoc);
        Map<String, String> criteria = new HashMap<String, String>();
        criteria.put("id", "entity124eId");
        criteria.put("entityTypeCode", "PERSON");

		Entity entity = KimApiServiceLocator.getIdentityService().getEntity(personDoc.getEntityId());
        EntityTypeContactInfo entityType = entity.getEntityTypeContactInfos().get(0);
        personDoc.getExternalIdentifiers();
		assertAddressTrue((PersonDocumentAddress)personDoc.getAddrs().get(0), entityType.getAddresses().get(0));
		assertPhoneTrue((PersonDocumentPhone)personDoc.getPhones().get(0), entityType.getPhoneNumbers().get(0));
		assertEmailTrue((PersonDocumentEmail)personDoc.getEmails().get(0), entityType.getEmailAddresses().get(0));
		assertNameTrue((PersonDocumentName) personDoc.getNames().get(0), entity.getNames().get(0));
		assertPrincipalTrue(personDoc, entity.getPrincipals().get(0));

		assertAffiliationTrue(personDoc.getAffiliations().get(0), entity.getAffiliations().get(0));
		assertEmpInfoTrue(personDoc.getAffiliations().get(0).getEmpInfos().get(0), entity.getEmploymentInformation().get(0));

		//verify that update doesn't cause external identifier to be encrypted twice
		// and that update doesn't cause any problems
		uiDocumentService.saveEntityPerson(personDoc);
		Entity entity2 = ((IdentityService) KIMServiceLocatorInternal.getService("kimIdentityDelegateService")).getEntity(
                personDoc.getEntityId());
        EntityTypeContactInfo entityType2 = entity2.getEntityTypeContactInfos().get(0);
        personDoc.getExternalIdentifiers();
        assertAddressTrue((PersonDocumentAddress)personDoc.getAddrs().get(0), entityType2.getAddresses().get(0));
        assertPhoneTrue((PersonDocumentPhone)personDoc.getPhones().get(0), entityType2.getPhoneNumbers().get(0));
        assertEmailTrue((PersonDocumentEmail)personDoc.getEmails().get(0), entityType2.getEmailAddresses().get(0));
        assertNameTrue((PersonDocumentName)personDoc.getNames().get(0), entity2.getNames().get(0));
        assertPrincipalTrue(personDoc, entity2.getPrincipals().get(0));



		//		List<String> groupIds = groupService.getDirectMemberGroupIds("g1");
//		System.out.println( groupIds );
//		assertTrue( "g1 must contain group g2", groupIds.contains( "g2" ) );
//		assertFalse( "g1 must not contain group g3", groupIds.contains( "g3" ) );
//
//		groupIds = groupService.getDirectMemberGroupIds("g2");
//		System.out.println( groupIds );
//		assertTrue( "g2 must contain group g3", groupIds.contains( "g3" ) );
//		assertFalse( "g2 must not contain group g4 (inactive)", groupIds.contains( "g4" ) );

	}

	@Test
	public void testLoadToPersonDocument() {

		Entity entity = KimApiServiceLocator.getIdentityService().getEntity("entity123eId");
		assertNotNull(entity);
		IdentityManagementPersonDocument personDoc = new IdentityManagementPersonDocument();
		uiDocumentService.loadEntityToPersonDoc(personDoc, "entity123pId");
        EntityTypeContactInfo entityType = entity.getEntityTypeContactInfos().get(0);
        personDoc.getExternalIdentifiers();
		assertAddressTrue((PersonDocumentAddress)personDoc.getAddrs().get(0), entityType.getAddresses().get(0));
		assertPhoneTrue((PersonDocumentPhone)personDoc.getPhones().get(0), entityType.getPhoneNumbers().get(0));
		assertEmailTrue((PersonDocumentEmail)personDoc.getEmails().get(0), entityType.getEmailAddresses().get(0));
		assertNameTrue((PersonDocumentName)personDoc.getNames().get(0), entity.getNames().get(0));
		//assertPrincipalTrue(personDoc, identity.getPrincipals().get(0));
		assertAffiliationTrue(personDoc.getAffiliations().get(0), entity.getAffiliations().get(0));
		assertEmpInfoTrue(personDoc.getAffiliations().get(0).getEmpInfos().get(0), entity.getEmploymentInformation().get(0));

	}

	// test principal membership
	@Test
	@Ignore
	public void testSetAttributeEntry() throws Exception {
		PersonDocumentRole personDocRole = initPersonDocRole();
        KimTypeService kimTypeService = (DataDictionaryTypeServiceBase) KIMServiceLocatorInternal.getService(personDocRole.getKimRoleType().getServiceName());
		personDocRole.setDefinitions(kimTypeService.getAttributeDefinitions(personDocRole.getKimTypeId()));

		personDocRole.setAttributeEntry( uiDocumentService.getAttributeEntries( personDocRole.getDefinitions() ) );
		for (Object key : personDocRole.getAttributeEntry().keySet()) {
			if (key.equals(KimConstants.AttributeConstants.NAMESPACE_CODE)) {
				Map value = (Map)personDocRole.getAttributeEntry().get(key);
				assertEquals("Parameter Namespace Code", value.get("label"));
				assertEquals("Nmspc Cd", value.get("shortLabel"));
				assertEquals(new Integer(20), value.get("maxLength"));
			} else if (key.equals("campusCode")) {
				Map value = (Map)personDocRole.getAttributeEntry().get(key);
				assertEquals("Campus Code", value.get("label"));
				assertEquals("Campus Code", value.get("shortLabel"));
				assertEquals(new Integer(2), value.get("maxLength"));
			} else {
				assertFalse("Should not have this key "+key, true);
			}
		}
	}

	private PersonDocumentRole initPersonDocRole() throws Exception {
//		Map pkMap = new HashMap();
//		pkMap.put("roleId", "r1");
//		PersonDocumentRole docRole = (PersonDocumentRole)uiDocumentService.getBusinessObjectService().findByPrimaryKey(PersonDocumentRole.class, pkMap);
		PersonDocumentRole docRole = new PersonDocumentRole();
		docRole.setKimTypeId("roleType1");
		docRole.setRoleId("r1");
		KimTypeBo kimType = new KimTypeBo();
		kimType.setId("roleType1");
		kimType.setServiceName("kimRoleTypeService");
		List<KimTypeAttributeBo> attributeDefinitions = new ArrayList<KimTypeAttributeBo>();
		Map pkMap = new HashMap();
		pkMap.put("kimTypeAttributeId", "kimAttr3");
		KimTypeAttributeBo attr1 = (KimTypeAttributeBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(KimTypeAttributeBo.class, pkMap);

//		attr1.setKimAttributeId("kimAttrDefn2");
//		attr1.setSortCode("a");
//		attr1.setKimTypeAttributeId("kimAttr3");

		attributeDefinitions.add(attr1);
//		attr1 = new KimTypeAttributeBo();
//		attr1.setKimAttributeId("kimAttrDefn3");
//		attr1.setSortCode("b");
//		attr1.setKimTypeAttributeId("kimAttr4");

		pkMap.put("kimTypeAttributeId", "kimAttr4");
		KimTypeAttributeBo attr2 = (KimTypeAttributeBo) KRADServiceLocator.getBusinessObjectService().findByPrimaryKey(KimTypeAttributeBo.class, pkMap);

		attributeDefinitions.add(attr2);
		kimType.setAttributeDefinitions(attributeDefinitions);

        Field fld = PersonDocumentRole.class.getField("kimRoleType");
        fld.set(docRole, kimType);

		return docRole;
	}

	// init section
	private IdentityManagementPersonDocument initPersonDoc() {
		IdentityManagementPersonDocument personDoc = new IdentityManagementPersonDocument();
		personDoc.setEntityId("ent123");
		personDoc.setDocumentNumber("1");
		personDoc.setPrincipalId("pid123");
		personDoc.setPrincipalName("test");
//		personDoc.setUnivId("1234567890");
		personDoc.setAffiliations(initAffiliations());
		personDoc.setNames(initNames());
		personDoc.setAddrs(initAddresses());
		//personDoc.setRoles(initRoles());
		//personDoc.setGroups();
		personDoc.setPhones(initPhones());
		personDoc.setEmails(initEmails());
		return personDoc;
	}

	private List<PersonDocumentName> initNames() {
		List<PersonDocumentName> docNames = new ArrayList<PersonDocumentName>();
			PersonDocumentName docName = new PersonDocumentName();
			docName.setEntityNameId("nameId123");
			docName.setNameCode("PRFR");
            docName.setEntityNameType(
                    EntityNameTypeBo.from(KimApiServiceLocator.getIdentityService().getNameType("PRFR")));
			docName.setFirstName("John");
			docName.setLastName("Doe");
			docName.setMiddleName("M");
			docName.setNamePrefix("Mr");
			docName.setNameSuffix("Jr");
			docName.setActive(true);
			docName.setDflt(true);
			docNames.add(docName);
		return docNames;
	}

	private List<PersonDocumentAffiliation> initAffiliations() {
		List<PersonDocumentAffiliation> docAffiliations = new ArrayList<PersonDocumentAffiliation>();
			PersonDocumentAffiliation docAffiliation = new PersonDocumentAffiliation();
			docAffiliation.setAffiliationTypeCode("FCLTY");
            docAffiliation.setAffiliationType(
                    EntityAffiliationTypeBo.from(KimApiServiceLocator.getIdentityService().getAffiliationType("FCLTY")));
			docAffiliation.setEntityAffiliationId("aflID123");
			docAffiliation.setCampusCode("BL");
			docAffiliation.setActive(true);
			docAffiliation.setDflt(true);

			// EntityAffiliationImpl does not define empinfos as collection
			docAffiliations.add(docAffiliation);
			List<PersonDocumentEmploymentInfo> docEmploymentInformations = new ArrayList<PersonDocumentEmploymentInfo>();
				PersonDocumentEmploymentInfo docEmpInfo = new PersonDocumentEmploymentInfo();
				docEmpInfo.setEmployeeId("12345");
				docEmpInfo.setEntityAffiliationId(docAffiliation.getEntityAffiliationId());
				docEmpInfo.setEntityEmploymentId("empId123");
				docEmpInfo.setEmploymentRecordId("1");
				docEmpInfo.setBaseSalaryAmount(new KualiDecimal(8000));
				docEmpInfo.setPrimaryDepartmentCode("BL-CHEM");
				docEmpInfo.setEmploymentStatusCode("A");
				docEmpInfo.setEmploymentTypeCode("P");
				docEmpInfo.setActive(true);
				docEmploymentInformations.add(docEmpInfo);
			docAffiliation.setEmpInfos(docEmploymentInformations);

		return docAffiliations;

	}

	private PersonDocumentPrivacy initPrivacyReferences(EntityPrivacyPreferencesBo privacyPreferences) {
		PersonDocumentPrivacy docPrivacy = new PersonDocumentPrivacy();
		docPrivacy.setSuppressAddress(true);
		docPrivacy.setSuppressEmail(false);
		docPrivacy.setSuppressName(false);
		docPrivacy.setSuppressPhone(false);
		docPrivacy.setSuppressPersonal(true);
		return docPrivacy;
	}
	private List<PersonDocumentPhone> initPhones() {
		List<PersonDocumentPhone> docPhones = new ArrayList<PersonDocumentPhone>();
			PersonDocumentPhone docPhone = new PersonDocumentPhone();
			docPhone.setPhoneTypeCode("HM");
            docPhone.setPhoneType(EntityPhoneTypeBo.from(KimApiServiceLocator.getIdentityService().getPhoneType("HM")));
			docPhone.setEntityPhoneId("phoneId123");
			docPhone.setEntityTypeCode("PERSON");
			docPhone.setPhoneNumber("123-45'6789");
			docPhone.setExtensionNumber("123");
			docPhone.setActive(true);
			docPhone.setDflt(true);
			docPhones.add(docPhone);
		return  docPhones;

	}

	private List<PersonDocumentEmail> initEmails() {
		List<PersonDocumentEmail> emails = new ArrayList<PersonDocumentEmail>();
			PersonDocumentEmail docEmail = new PersonDocumentEmail();
			//docEmail.setEntityId(email.getEntityId());
			docEmail.setEntityEmailId("emailId123");
			docEmail.setEntityTypeCode("PERSON");
			docEmail.setEmailTypeCode("HM");
            docEmail.setEmailType(
                    EntityEmailTypeBo.from(KimApiServiceLocator.getIdentityService().getEmailType("HM")));
			docEmail.setEmailAddress("test@abc.com");
			docEmail.setActive(true);
			docEmail.setDflt(true);
			emails.add(docEmail);
		return emails;
	}

	private  List<PersonDocumentAddress> initAddresses() {
		List<PersonDocumentAddress> docAddresses = new ArrayList<PersonDocumentAddress>();
			PersonDocumentAddress docAddress = new PersonDocumentAddress();
			docAddress.setEntityTypeCode("PERSON");
			docAddress.setEntityAddressId("addrId123");
			docAddress.setAddressTypeCode("HM");
            docAddress.setAddressType(EntityAddressTypeBo.from(KimApiServiceLocator.getIdentityService().getAddressType("HM")));
			docAddress.setLine1("PO box 123");
			docAddress.setStateProvinceCode("IN");
			docAddress.setPostalCode("46123");
			docAddress.setCountryCode("US");
			docAddress.setCity("Indianapolis");
			docAddress.setActive(true);
			docAddress.setDflt(true);
			docAddresses.add(docAddress);
		return docAddresses;
	}

// assert section

	private void assertPrincipalTrue(IdentityManagementPersonDocument personDoc, PrincipalContract principal) {
		assertEquals(personDoc.getPrincipalId(), principal.getPrincipalId());
		assertEquals(personDoc.getPrincipalName(), principal.getPrincipalName());
	}

	private void assertAddressTrue(PersonDocumentAddress docAddress, EntityAddressContract entityAddress) {

		assertEquals(docAddress.getAddressTypeCode(), entityAddress.getAddressType().getCode());
		assertEquals(docAddress.getCountryCode(), entityAddress.getCountryCode());
		assertEquals(docAddress.getLine1(), entityAddress.getLine1());
		assertEquals(docAddress.getCity(), entityAddress.getCity());
		assertEquals(docAddress.getPostalCode(), entityAddress.getPostalCode());
		assertEquals(docAddress.getStateProvinceCode(), entityAddress.getStateProvinceCode());
	}

	private void assertEmailTrue(PersonDocumentEmail docEmail, EntityEmailContract entityEmail) {
		assertEquals(docEmail.getEntityEmailId(), entityEmail.getId());
		assertEquals(docEmail.getEmailAddress(), entityEmail.getEmailAddressUnmasked());
		assertEquals(docEmail.getEmailTypeCode(), entityEmail.getEmailType().getCode());
	}

	private void assertPhoneTrue(PersonDocumentPhone docPhone, EntityPhoneContract entityPhone) {
		assertEquals(docPhone.getEntityPhoneId(), entityPhone.getId());
		assertEquals(docPhone.getCountryCode(), entityPhone.getCountryCode());
		assertEquals(docPhone.getPhoneNumber(), entityPhone.getPhoneNumber());
		assertEquals(docPhone.getExtensionNumber(), entityPhone.getExtensionNumber());
		assertEquals(docPhone.getPhoneTypeCode(), entityPhone.getPhoneType().getCode());
	}

	private void assertNameTrue(PersonDocumentName docName, EntityNameContract entityName) {
		assertEquals(docName.getEntityNameId(), entityName.getId());
		assertEquals(docName.getFirstName(), entityName.getFirstName());
		assertEquals(docName.getLastName(), entityName.getLastName());
		assertEquals(docName.getNameCode(), entityName.getNameType().getCode());
		assertEquals(docName.getNameSuffix(), entityName.getNameSuffix());
		assertEquals(docName.getNamePrefix(), entityName.getNamePrefix());
	}

	private void assertAffiliationTrue(PersonDocumentAffiliation docAffln, EntityAffiliation entityAffln) {
		assertEquals(docAffln.getAffiliationTypeCode(), entityAffln.getAffiliationType().getCode());
		assertEquals(docAffln.getCampusCode(), entityAffln.getCampusCode());
		assertEquals(docAffln.getEntityAffiliationId(), entityAffln.getId());
	}

	private void assertEmpInfoTrue(PersonDocumentEmploymentInfo docEmpInfo, EntityEmploymentContract entityEmpInfo) {
		assertEquals(docEmpInfo.getEmployeeId(), entityEmpInfo.getEmployeeId());
		assertEquals(docEmpInfo.getEmploymentTypeCode(), entityEmpInfo.getEmployeeType().getCode());
		assertEquals(docEmpInfo.getEmploymentStatusCode(), entityEmpInfo.getEmployeeStatus().getCode());
		assertEquals(docEmpInfo.getEmploymentRecordId(), entityEmpInfo.getEmploymentRecordId());
		assertEquals(docEmpInfo.getBaseSalaryAmount(), entityEmpInfo.getBaseSalaryAmount());
	}

    @Test
    public void test_KimDocumentRoleMember_copyProperties() {
        //
        // KimDocumentRoleMember.copyProperties is called by the UiDocumentServiceImpl.getRoleMembers
        // method.  This test verifies that KimDocumentRoleMember.copyProperties is working as
        // expected and also will fail if fields are added to KimDocumentRoleMember to ensure that
        // KimDocumentRoleMember.copyProperties is kept up to date.
        //

        DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String ROLE_MEMBER_ID = "1";
        String ROLE_ID = "23";
        Map<String, String> ATTRIBUTES = new HashMap<String, String>();
        String MEMBER_NAME = "WorkflowAdmin";
        String MEMBER_NAMESPACE_CODE = "KR-WKFLW";
        String MEMBER_ID = "1";
        MemberType MEMBER_TYPE = MemberType.GROUP;
        String ACTIVE_FROM_STRING = "2011-01-01 12:00:00";
        DateTime ACTIVE_FROM = new DateTime(FORMATTER.parseDateTime(ACTIVE_FROM_STRING));
        String ACTIVE_TO_STRING = "2115-01-01 12:00:00";
        DateTime ACTIVE_TO = new DateTime(FORMATTER.parseDateTime(ACTIVE_TO_STRING));
        String OBJ_ID = "123";
        long VER_NUM = 4;

        KimDocumentRoleMember newKimDocumentRoleMember = new KimDocumentRoleMember();

        RoleMemberContract rmc = RoleMember.Builder.create(ROLE_ID, ROLE_MEMBER_ID, MEMBER_ID, MEMBER_TYPE, ACTIVE_FROM,
                ACTIVE_TO, ATTRIBUTES, MEMBER_NAME, MEMBER_NAMESPACE_CODE).build();
        RoleMember rm = RoleMember.Builder.create(rmc).build();
        RoleMemberBo roleMemberBo = RoleMemberBo.from(rm);
        roleMemberBo.setObjectId(OBJ_ID);
        roleMemberBo.setVersionNumber(VER_NUM);

        KimDocumentRoleMember.copyProperties(newKimDocumentRoleMember, roleMemberBo);

        assertTrue("newKimDocumentRoleMember should have a roleId of " + ROLE_ID,
                newKimDocumentRoleMember.getRoleId().equals(ROLE_ID));
        assertTrue("newKimDocumentRoleMember should have a MemberId of " + MEMBER_ID,
                newKimDocumentRoleMember.getMemberId().equals(MEMBER_ID));
        assertTrue("newKimDocumentRoleMember should have a MemberName of " + MEMBER_NAME,
                newKimDocumentRoleMember.getMemberName().equals(MEMBER_NAME));
        assertTrue("newKimDocumentRoleMember should have a MemberNamespaceCode of " + MEMBER_NAMESPACE_CODE,
                newKimDocumentRoleMember.getMemberNamespaceCode().equals(MEMBER_NAMESPACE_CODE));
        assertTrue("newKimDocumentRoleMember should be active.",
                newKimDocumentRoleMember.isActive());
        assertTrue("newKimDocumentRoleMember should have a ActiveToDate of " + MEMBER_NAMESPACE_CODE,
                newKimDocumentRoleMember.getActiveToDate().equals(roleMemberBo.getActiveToDateValue()));
        assertTrue("newKimDocumentRoleMember should have a ActiveFromDate of " + ACTIVE_FROM_STRING,
                newKimDocumentRoleMember.getActiveFromDate().equals(roleMemberBo.getActiveFromDateValue()));
        assertTrue("newKimDocumentRoleMember should have a VersionNumber of " + VER_NUM,
                newKimDocumentRoleMember.getVersionNumber().equals(VER_NUM));
        assertTrue("newKimDocumentRoleMember should have a ObjectId of " + OBJ_ID,
                newKimDocumentRoleMember.getObjectId().equals(OBJ_ID));

        Class<?> c = newKimDocumentRoleMember.getClass();
        Field[] fields = c.getDeclaredFields();
        List<Field> fieldsList = Arrays.asList(fields);

        int numberOfFieldsInKimDocumentRoleMember = 11;
        assertTrue("KimDocumentRoleMember.copyProperties may need to be updated if the number of fields"
                + "in KimDocumentRoleMember does not equal " + numberOfFieldsInKimDocumentRoleMember + ".  Size is " +
                fieldsList.size(), fieldsList.size() == numberOfFieldsInKimDocumentRoleMember);
    }
}
