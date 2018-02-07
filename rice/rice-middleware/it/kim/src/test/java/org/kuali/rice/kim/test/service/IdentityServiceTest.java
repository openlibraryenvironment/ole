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

import org.junit.Test;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.api.KimApiConstants;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.entity.EntityDefaultQueryResults;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.principal.EntityNamePrincipalName;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoDefault;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.PersonServiceImpl;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.test.BaselineTestCase;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.kuali.rice.core.api.criteria.PredicateFactory.and;
import static org.kuali.rice.core.api.criteria.PredicateFactory.like;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class IdentityServiceTest extends KIMTestCase {

	private IdentityService identityService;

	public void setUp() throws Exception {
		super.setUp();
		if (null == identityService) {
			identityService = findIdSvc();
		}
	}
	
	@Test
	public void testGetPrincipal() {
		Principal principal = identityService.getPrincipal("KULUSER");
		assertNotNull("principal must not be null", principal);
		assertEquals("Principal name did not match expected result","kuluser", principal.getPrincipalName());
	}

    @Test
    public void testGetPrincipalNotFound() {
        Principal principal = identityService.getPrincipal("DoesNotExist");
        assertNull("principal should not be found", principal);
    }

    @Test
    public void testGetPrincipals() {
        List<String> principalIds = new ArrayList<String>();
        principalIds.add("KULUSER");
        List<Principal> validPrincipals = identityService.getPrincipals(principalIds);
        assertNotNull("validPrincipals must not be null", validPrincipals);
        assertEquals("validPrincipals name did not match expected result","kuluser", validPrincipals.get(0).getPrincipalName());
    }

	@Test
	public void testGetPrincipalByPrincipalName() {
		Principal principal = identityService.getPrincipalByPrincipalName("kuluser");
		assertNotNull("principal must not be null", principal);
		assertEquals("Principal ID did not match expected result","KULUSER", principal.getPrincipalId());
	}

    @Test
    public void testGetPrincipalByPrincipalNameNotFound() {
        Principal principal = identityService.getPrincipalByPrincipalName("DoesNotExist");
        assertNull("principal should not be found", principal);
    }
	
	@Test
	public void testGetDefaultEntityByPrincipalId() {
		String principalId = "KULUSER";
		EntityDefault info = identityService.getEntityDefaultByPrincipalId(principalId);
		assertNotNull("entity must not be null", info);
		assertNotNull("entity principals must not be null", info.getPrincipals());
		assertEquals("entity must have exactly 1 principal", 1, info.getPrincipals().size());
		for (Principal principalInfo : info.getPrincipals()) {
			assertEquals("Wrong principal id", principalId, principalInfo.getPrincipalId());
		}
		assertTrue("entity external identifiers must not be null", (info.getExternalIdentifiers() == null) || info.getExternalIdentifiers().isEmpty());
	}

	@Test
	public void testGetDefaultEntityByPrincipalName() {
		String principalName = "kuluser";
		EntityDefault info = identityService.getEntityDefaultByPrincipalName(principalName);
		assertNotNull("entity must not be null", info);
		assertNotNull("entity principals must not be null", info.getPrincipals());
		assertEquals("entity must have exactly 1 principal", 1, info.getPrincipals().size());
		for (Principal principalInfo : info.getPrincipals()) {
			assertEquals("Wrong principal name", principalName, principalInfo.getPrincipalName());
		}
		assertTrue("entity external identifiers must not be null", (info.getExternalIdentifiers() == null) || info.getExternalIdentifiers().isEmpty());
	}

	@Test
	public void testGetEntityByPrincipalId() {
		String principalId = "KULUSER";
		Entity info = identityService.getEntityByPrincipalId(principalId);
		assertNotNull("entity must not be null", info);
		assertNotNull("entity principals must not be null", info.getPrincipals());
		assertEquals("entity must have exactly 1 principal", 1, info.getPrincipals().size());
		for (Principal principalInfo : info.getPrincipals()) {
			assertEquals("Wrong principal id", principalId, principalInfo.getPrincipalId());
		}
		assertTrue("entity external identifiers must not be null", (info.getExternalIdentifiers() == null) || info.getExternalIdentifiers().isEmpty());
		assertTrue("entity residencies must not be null", (info.getResidencies() == null) || info.getResidencies().isEmpty());
	}

	@Test
	public void testGetEntityByPrincipalName() {
		String principalName = "kuluser";
		Entity info = identityService.getEntityByPrincipalName(principalName);
		assertNotNull("entity must not be null", info);
		assertNotNull("entity principals must not be null", info.getPrincipals());
		assertEquals("entity must have exactly 1 principal", 1, info.getPrincipals().size());
		for (Principal principalInfo : info.getPrincipals()) {
			assertEquals("Wrong principal name", principalName, principalInfo.getPrincipalName());
		}
		assertTrue("entity external identifiers must not be null", (info.getExternalIdentifiers() == null) || info.getExternalIdentifiers().isEmpty());
		assertTrue("entity residencies must not be null", (info.getResidencies() == null) || info.getResidencies().isEmpty());
	}

	@Test
	public void testGetContainedAttributes() {
		Principal principal = identityService.getPrincipal("p1");
		
		EntityDefault entity = identityService.getEntityDefault(principal.getEntityId());
		assertNotNull( "Entity Must not be null", entity );
		EntityTypeContactInfoDefault eet = entity.getEntityType( "PERSON" );
		assertNotNull( "PERSON EntityTypeData must not be null", eet );
		assertNotNull( "EntityEntityType's default email address must not be null", eet.getDefaultEmailAddress() );
		assertEquals( "p1@kuali.org", eet.getDefaultEmailAddress().getEmailAddressUnmasked() );
	}

	protected QueryByCriteria setUpEntityLookupCriteria(String principalId) {
		PersonServiceImpl personServiceImpl = (PersonServiceImpl) KIMServiceLocatorInternal.getService(KimApiServiceLocator.KIM_PERSON_SERVICE);
		Map<String,String> criteria = new HashMap<String,String>(1);
		criteria.put(KIMPropertyConstants.Person.PRINCIPAL_ID, principalId);
		Map<String, String> entityCriteria = personServiceImpl.convertPersonPropertiesToEntityProperties(criteria);
        entityCriteria.put("entityTypeContactInfos.entityTypeCode", "PERSON");
        QueryByCriteria.Builder query = QueryByCriteria.Builder.create();
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (String key : entityCriteria.keySet()) {
            predicates.add(like(key, entityCriteria.get(key)));
        }
        if (!predicates.isEmpty()) {
            query.setPredicates(and(predicates.toArray(new Predicate[] {})));
        }
        return query.build();
	}

	@Test
	public void testLookupEntityDefaultInfo() {
		String principalIdToTest = "p1";
		EntityDefaultQueryResults qr = identityService.findEntityDefaults(setUpEntityLookupCriteria(principalIdToTest));
        List<EntityDefault> results = qr.getResults();
		assertNotNull("Lookup results should never be null", results);
		assertEquals("Lookup result count is invalid", 1, results.size());
		for (EntityDefault kimEntityDefaultInfo : results) {
			assertEquals("Entity should have only one principal for this test", 1, kimEntityDefaultInfo.getPrincipals().size());
			assertEquals("Principal Ids should match", principalIdToTest, kimEntityDefaultInfo.getPrincipals().get(0).getPrincipalId());
		}
	}

	@Test
	public void testLookupEntityInfo() {
		String principalIdToTest = "p1";
		List<Entity> results = identityService.findEntities(setUpEntityLookupCriteria(principalIdToTest)).getResults();
		assertNotNull("Lookup results should never be null", results);
		assertEquals("Lookup result count is invalid", 1, results.size());
		for (Entity kimEntityInfo : results) {
			assertEquals("Entity should have only one principal for this test", 1, kimEntityInfo.getPrincipals().size());
			assertEquals("Principal Ids should match", principalIdToTest, kimEntityInfo.getPrincipals().get(0).getPrincipalId());
		}
	}

    @Test
    public void testGetEntityWithNameChangeDate() {
        String principalName = "testuser7";
        Entity info = identityService.getEntityByPrincipalName(principalName);
        List<EntityName> names = info.getNames();
        for (EntityName name : names) {
            assertNotNull("nameChangeDate should have been set for PrincipalName " + principalName,name.getNameChangedDate());
        }
    }

    protected IdentityService findIdSvc() throws Exception {
		return (IdentityService) GlobalResourceLoader.getService(
                new QName(KimApiConstants.Namespaces.KIM_NAMESPACE_2_0, KimApiConstants.ServiceNames.IDENTITY_SERVICE_SOAP));
	}

	protected void setIdentityService(IdentityService idSvc) {
		this.identityService = idSvc;
	}
}
