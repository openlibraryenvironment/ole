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

import org.apache.ojb.broker.metadata.DescriptorRepository;
import org.apache.ojb.broker.metadata.MetadataManager;
import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.PersonServiceImpl;
import org.kuali.rice.kim.impl.identity.external.EntityExternalIdentifierBo;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.kim.test.bo.BOContainingPerson;
import org.kuali.rice.kns.lookup.Lookupable;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.test.BaselineTestCase;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.AutoPopulatingList;

import javax.xml.namespace.QName;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class PersonServiceImplTest extends KIMTestCase {

	private PersonServiceImpl personService;

	public void setUp() throws Exception {
		super.setUp();
		personService = (PersonServiceImpl) GlobalResourceLoader.getService(new QName("personService"));
		
	}

	/**
	 * Test method for {@link org.kuali.rice.kim.impl.identity.PersonServiceImpl#getPersonByExternalIdentifier(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPersonByExternalIdentifier() {
		//insert external identifier
		Principal principal = KimApiServiceLocator.getIdentityService().getPrincipal("p1");
		
		SequenceAccessorService sas = KRADServiceLocator.getSequenceAccessorService();
		Long externalIdentifierId = sas.getNextAvailableSequenceNumber("KRIM_ENTITY_EXT_ID_ID_S", EntityExternalIdentifierBo.class);
		EntityExternalIdentifierBo externalIdentifier = new EntityExternalIdentifierBo();
		externalIdentifier.setId("externalIdentifierId");
		externalIdentifier.setEntityId(principal.getEntityId());
		externalIdentifier.setExternalId("000-00-0000");
		externalIdentifier.setExternalIdentifierTypeCode("SSN");
		KRADServiceLocator.getBusinessObjectService().save(externalIdentifier);
		
		List<Person> people = personService.getPersonByExternalIdentifier( "SSN", "000-00-0000" );
		assertNotNull( "result object must not be null", people );
		assertEquals( "exactly one record should be returned", 1, people.size() );
		assertEquals( "the returned principal is not correct", "p1", people.get(0).getPrincipalId() );
	}

//	@Test
//	public void testHasRole_Inherited() {
//		Person p = personService.getPersonByPrincipalName( "wwren" );
//		assertNotNull( "person object must not be null", p );
//		assertTrue( "person must be a member of PA_MAINTENANCE_USERS", personService.hasRole( p, org.kuali.rice.kim.util.KimApiConstants.KIM_GROUP_DEFAULT_NAMESPACE_CODE, "PA_AP_MAINTENANCE_USERS" ) );
//		assertTrue( "person must be NOT a member of PA_MAINTENANCE_USERS", !personService.hasRole( p, org.kuali.rice.kim.util.KimApiConstants.KIM_GROUP_DEFAULT_NAMESPACE_CODE, "PA_MAINTENANCE_USERS" ) );
//	}
//
//	@Test
//	public void testGetPersonRoles() {
//		Person p = personService.getPerson( "KULUSER" );
//		assertNotNull( "person object must not be null", p );
//		List<KimRole> roles = personService.getPersonRoles( p, null );
//		assertNotNull( "role list must not be null", roles );
//		System.out.println( roles );
//		assertTrue( "role list must have non-zero length", roles.size() > 0 );
//		KimRole r = KimImplServiceLocator.getAuthorizationService().getRoleByNamespaceCodeAndName( org.kuali.rice.kim.util.KimApiConstants.KIM_GROUP_DEFAULT_NAMESPACE_CODE, "SY_FUNCTIONAL_SUPER_USERS" );
//		assertTrue( "one of the roles must be SY_FUNCTIONAL_SUPER_USERS", roles.contains( r ) );
//	}
//
//	@Test
//	public void testHasRole() {
//		Person p = personService.getPerson( "KULUSER" );
//		assertTrue( "person must have role SY_FUNCTIONAL_SUPER_USERS", personService.hasRole( p, org.kuali.rice.kim.util.KimApiConstants.KIM_GROUP_DEFAULT_NAMESPACE_CODE, "SY_FUNCTIONAL_SUPER_USERS" ) );
//	}

	@Test
	public void testGetPerson() {
		Person p = personService.getPerson( "KULUSER" );
		assertNotNull( "person object must not be null", p );
		assertEquals( "class must match implementation class defined on service", personService.getPersonImplementationClass(), p.getClass() );
		assertEquals( "person name does not match", "KULUSER", p.getFirstNameUnmasked() );
		assertEquals( "principal name does not match", "kuluser", p.getPrincipalName() );
		assertEquals( "KULUSER should have no address record", "", p.getAddressLine1Unmasked() );
		assertEquals( "KULUSER should have no campus code", "", p.getCampusCode() );
		assertEquals( "KULUSER should have no email address", "", p.getEmailAddressUnmasked() );
		assertNotNull( "entity ID should be set", p.getEntityId() );
		assertNotNull( "principal ID should be set", p.getPrincipalId() );
		
		// check an employee id
		Person p1Person = personService.getPerson( "p1" );
		assertNotNull( "person object must not be null", p );
		assertEquals("employee ID does not match", "p1emplId", p1Person.getEmployeeId());
	}

	@Test
	public void testGetPersonInactive() {
		Person p = personService.getPerson( "inactiveuserid" );
		assertNotNull( "person object must not be null", p );
        assertEquals("principal ID does not match", "inactiveusernm", p.getPrincipalName());

	}
	
	@Test
	public void testGetPersonByPrincipalName() {
		Person p = personService.getPersonByPrincipalName( "kuluser" );
		assertNotNull( "person object must not be null", p );
		assertEquals( "person name does not match", "KULUSER", p.getFirstName() );
		assertEquals( "principal id does not match", "KULUSER", p.getPrincipalId() );
	}
	
	@Test
	public void testGetPersonByPrincipalNameInactive() {
		Person p = personService.getPersonByPrincipalName( "inactiveusernm" );
        assertEquals("principal ID does not match", "inactiveuserid", p.getPrincipalId());
    }
	
    @Test
    public void testGetPersonByEmployeeIdNoInfo() {
        Person p = personService.getPersonByEmployeeId( "" );
        assertNull( "person object will be null", p );
    }
    
    @Test
    public void testGetPersonByEmployeeIdNotFound() {
        Person p = personService.getPersonByEmployeeId( "NotFound" );
        assertNull( "person object will be null", p );
    }
    
    @Test
    public void testGetPersonByEmployeeIdActive() {
        Person p = personService.getPersonByEmployeeId( "0000001138" );
        assertNotNull( "person object must not be null", p );
        assertEquals( "person name does not match", "activeUserFirst", p.getFirstName() );
        assertEquals( "principal id does not match", "activeuserid", p.getPrincipalId() );
    }
    
    @Test
    public void testGetPersonByEmployeeIdInactiveUser() {
        Person p = personService.getPersonByEmployeeId( "0000001139" );
        assertNotNull( "person object must not be null", p );
        assertEquals( "person name does not match", "InactiveUserFirst", p.getFirstName() );
        assertEquals( "principal id does not match", "inactiveuserid", p.getPrincipalId() );
    }
    
    @Test
    public void testGetPersonByEmployeeIdInactiveEmp() {
        Person p = personService.getPersonByEmployeeId( "0000001140" );
        assertNotNull( "person object must not be null", p );
        assertEquals( "person name does not match", "InactiveEmplFirst", p.getFirstName() );
        assertEquals( "principal id does not match", "inactiveempid", p.getPrincipalId() );
    }
    
	@Test
	public void testConvertPersonPropertiesToEntityProperties() {
		HashMap<String,String> criteria = new HashMap<String,String>();
		criteria.put( "firstName", "System User" );
		Map<String,String> entityCriteria = personService.convertPersonPropertiesToEntityProperties( criteria );
		assertEquals( "number of criteria is not correct", 5, entityCriteria.size() );
		assertNotNull( "criteria must filter for active entity types", entityCriteria.get( "entityTypeContactInfos.active" ) );
		assertNotNull( "criteria must filter on entity type code", entityCriteria.get( "entityTypeContactInfos.entityTypeCode" ) );
		assertNotNull( "criteria must filter for first name", entityCriteria.get( "names.firstName" ) );
		assertNotNull( "criteria must filter for active names", entityCriteria.get( "names.active" ) );
		assertNotNull( "criteria must filter for the default name", entityCriteria.get( "names.defaultValue" ) );
	}

	/**
	 * Test method for {@link org.kuali.rice.kim.impl.identity.PersonServiceImpl#findPeople(Map)}.
	 */
    @Test
    public void testFindPeople() {
        HashMap<String,String> criteria = new HashMap<String,String>();
        criteria.put( "firstName", "KULUSER" );
        List<Person> people = personService.findPeople( criteria );
        assertNotNull( "result must not be null", people );
        assertEquals( "wrong number of people returned", 1, people.size() );
        Person p = people.get( 0 );
        assertEquals( "name must match criteria", "KULUSER", p.getFirstName() );
        assertEquals( "principal name must be kuluser", "kuluser", p.getPrincipalName() );
    }
	
    @Test
    public void testFindPeopleInactive() {
        HashMap<String,String> criteria = new HashMap<String,String>();
        criteria.put( "principals.active", "N" );
        List<Person> people = personService.findPeople( criteria );
        assertNotNull( "result must not be null", people );
        assertEquals( "wrong number of people returned", 1, people.size() );
    }

    @Test
    public void testFindPeopleBothInactiveAndActive() {
        HashMap<String,String> criteria = new HashMap<String,String>();
        criteria.put( "firstName", "InactiveUserFirst" );
        List<Person> people = personService.findPeople( criteria );
        assertNotNull( "result must not be null", people );
        assertEquals( "wrong number of people returned", 1, people.size() );
    }    
    
    @Test
    public void testFindPeopleByWildcard() {
        HashMap<String,String> criteria = new HashMap<String,String>();
        criteria.put( "principalName", "!quick*" );
        List<Person> people = personService.findPeople( criteria );
        assertNotNull( "result must not be null", people );
        assertEquals( "wrong number of people returned", 53, people.size() );
        for (Person p: people) {
            if (p.getPrincipalName().startsWith("quick")) {
                fail("Invalid wildcard search results");
            }
        }
    }

	@Test
	public void testResolvePrincipalNamesToPrincipalIds() throws Exception {
		
		KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().addConfigFileLocation("KR-KIM", "classpath:org/kuali/rice/kim/bo/datadictionary/test/SampleBO.xml" );
		KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().parseDataDictionaryConfigurationFiles( false );

		Map<String,String> criteria = new HashMap<String,String>();
		criteria.put( "anAttribute", "aValue" );
		criteria.put( "anotherAttribute", "anotherValue" );
		criteria.put( "personAttribute.principalName", "kuluser" );
		System.out.println( "Before: " + criteria );
		Map<String,String> newCritiera = personService.resolvePrincipalNamesToPrincipalIds( new SampleBO(), criteria );
		assertNotNull( "returned map must not be null", newCritiera );
		System.out.println( "After:  " + newCritiera );
		assertTrue( "new criteria must have a personPrincipalId entry", newCritiera.containsKey( "personPrincipalId" ) );
		assertEquals( "resulting principal ID is not that expected", "KULUSER", newCritiera.get( "personPrincipalId" ) );
		assertFalse( "new criteria must not contain the original PrincipalName entry", newCritiera.containsKey( "personAttribute.principalName" ) );

		// check when already has value in result field
		criteria.put( "personPrincipalId", "NOT KULUSER" );
		System.out.println( "Before: " + criteria );
		newCritiera = personService.resolvePrincipalNamesToPrincipalIds( new SampleBO(), criteria );
		assertNotNull( "returned map must not be null", newCritiera );
		System.out.println( "After:  " + newCritiera );
		assertTrue( "new criteria must have a personPrincipalId entry", newCritiera.containsKey( "personPrincipalId" ) );
		assertEquals( "resulting principal ID should have been changed", "KULUSER", newCritiera.get( "personPrincipalId" ) );
	}

	@Test
	public void testResolvePrincipalNamesToPrincipalIds_Nested() throws Exception {

        KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().addConfigFileLocation( "", "classpath:org/kuali/rice/kim/bo/datadictionary/test/SampleBO.xml" );
        KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().parseDataDictionaryConfigurationFiles( false );

		Map<String,String> criteria = new HashMap<String,String>();
		criteria.put( "add.childBos.childsPersonAttribute.principalName", "kuluser" );
		System.out.println( "Before: " + criteria );
		Map<String,String> newCritiera = personService.resolvePrincipalNamesToPrincipalIds( new SampleBO(), criteria );
		assertNotNull( "returned map must not be null", newCritiera );
		System.out.println( "After:  " + newCritiera );
		// TODO: property is being appended in the wrong place - fix
		assertTrue( "new criteria must have a childsPersonPrincipalId entry", newCritiera.containsKey( "add.childBos.childsPersonPrincipalId" ) );
		assertFalse( "new criteria must not contain the original PrincipalName entry", newCritiera.containsKey( "add.childBos.childsPersonAttribute.principalName" ) );
	}

	@Test
	public void testUpdateWhenNecessary() {
		SampleBO bo = new SampleBO();
		bo.setPersonPrincipalId( "KULUSER" );
		Person p = bo.getPersonAttribute();
		assertNotNull( "person object must not be null", p );
		assertEquals( "principal IDs do not match", bo.getPersonPrincipalId(), p.getPrincipalId() );
		assertSame( "second retrieval must return same object since ID not changed", p, bo.getPersonAttribute() );
	}

	@Test
	public void testLookupWithPersonJoin() throws Exception {
		
		// merge the OJB file in containing the OJB metadata
        InputStream is = new DefaultResourceLoader().getResource("classpath:org/kuali/rice/kim/test/OJB-repository-kimunittests.xml").getInputStream();
        MetadataManager mm = MetadataManager.getInstance();
        DescriptorRepository dr = mm.readDescriptorRepository(is);
        mm.mergeDescriptorRepository(dr);
		
		KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().addConfigFileLocation("KR-KIM", "classpath:org/kuali/rice/kim/bo/datadictionary/test/BOContainingPerson.xml" );
		KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().parseDataDictionaryConfigurationFiles( false );
		BusinessObjectService bos = KRADServiceLocator.getBusinessObjectService();
		bos.delete( new ArrayList(bos.findAll( BOContainingPerson.class )) );
		BOContainingPerson bo = new BOContainingPerson();
		bo.setBoPrimaryKey( "ONE" );
		bo.setPrincipalId( "p1" );
		bos.save( bo );
		bo = new BOContainingPerson();
		bo.setBoPrimaryKey( "TWO" );
		bo.setPrincipalId( "p2" );
		bos.save( bo );

		Lookupable l = KNSServiceLocator.getKualiLookupable();
		l.setBusinessObjectClass( BOContainingPerson.class );
		Map<String,String> criteria = new HashMap<String,String>();
		criteria.put( "person.principalName", "principal1" );
		List<BOContainingPerson> results = (List)l.getSearchResultsUnbounded( (Map)criteria );
		System.out.println( results );
		assertNotNull( "results may not be null", results );
		assertEquals( "number of results is incorrect", 1, results.size() );
		bo =  results.iterator().next();
		assertEquals( "principalId does not match", "p1", bo.getPrincipalId() );
	}

//	@Test
//	public void testConfirmOnlyPKUsed() {
//		HashMap<String,String> criteria = new HashMap<String,String>();
//		criteria.put( "lastName", "HUNTLEY" );
//		criteria.put( "firstName", "KEISHA" );
//		Collection<Person> people = (Collection<Person>)KRADServiceLocatorInternal.getLookupService().findCollectionBySearchUnbounded(Person.class, criteria);
//		personService.findPeople( criteria );
//		assertNotNull( "result must not be null", people );
//		assertEquals( "wrong number of people returned", 1, people.size() );
//		Person p = people.iterator().next();
//		assertEquals( "principal name does not match", "khuntley", p.getPrincipalName() );
//
//		criteria.put( "principalName", "kuluser" );
//		people = people = (Collection<Person>)KRADServiceLocatorInternal.getLookupService().findCollectionBySearchUnbounded(Person.class, criteria);
//		assertNotNull( "result must not be null", people );
//		assertEquals( "wrong number of people returned", 1, people.size() );
//		p = people.iterator().next();
//		assertEquals( "principal name must be kuluser", "kuluser", p.getPrincipalName() );
//	}

	public static class SampleBO implements BusinessObject {
		private String anAttribute;
		private String anotherAttribute;
		private String personPrincipalId;
		private Person personAttribute;
		private List<SampleChildBOWithPerson> childBos = new AutoPopulatingList(SampleChildBOWithPerson.class);
		public String getAnAttribute() {
			return this.anAttribute;
		}
		public void setAnAttribute(String anAttribute) {
			this.anAttribute = anAttribute;
		}
		public String getAnotherAttribute() {
			return this.anotherAttribute;
		}
		public void setAnotherAttribute(String anotherAttribute) {
			this.anotherAttribute = anotherAttribute;
		}
		public String getPersonPrincipalId() {
			return this.personPrincipalId;
		}
		public void setPersonPrincipalId(String personPrincipalId) {
			this.personPrincipalId = personPrincipalId;
		}
		public Person getPersonAttribute() {
			personAttribute = KimApiServiceLocator.getPersonService().updatePersonIfNecessary( personPrincipalId, personAttribute );
			return personAttribute;
		}
		public void setPersonAttribute(Person personAttribute) {
			this.personAttribute = personAttribute;
		}
		public void refresh() {}
		public List<SampleChildBOWithPerson> getChildBos() {
			return this.childBos;
		}
		public void setChildBos(List<SampleChildBOWithPerson> childBos) {
			this.childBos = childBos;
		}
	}

	public static class SampleChildBOWithPerson implements BusinessObject {

		private String childsAttribute;
		private String childsPersonPrincipalId;
		private Person childsPersonAttribute;



		public String getChildsAttribute() {
			return this.childsAttribute;
		}
		public void setChildsAttribute(String childsAttribute) {
			this.childsAttribute = childsAttribute;
		}
		public String getChildsPersonPrincipalId() {
			return this.childsPersonPrincipalId;
		}
		public void setChildsPersonPrincipalId(String childsPersonPrincipalId) {
			this.childsPersonPrincipalId = childsPersonPrincipalId;
		}
		public Person getChildsPersonAttribute() {
			childsPersonAttribute = KimApiServiceLocator.getPersonService().updatePersonIfNecessary( childsPersonPrincipalId, childsPersonAttribute );
			return childsPersonAttribute;
		}
		public void setChildsPersonAttribute(Person childsPersonAttribute) {
			this.childsPersonAttribute = childsPersonAttribute;
		}
		public void refresh() {}
	}
}
