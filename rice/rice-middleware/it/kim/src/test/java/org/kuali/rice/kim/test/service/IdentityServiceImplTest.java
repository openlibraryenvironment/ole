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
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.name.EntityName;
import org.kuali.rice.kim.api.identity.principal.Principal;

import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfoContract;
import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.impl.identity.IdentityServiceImpl;
import org.kuali.rice.kim.test.KIMTestCase;
import org.kuali.rice.test.BaselineTestCase;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class IdentityServiceImplTest extends KIMTestCase {

	private IdentityService identityService;

	public void setUp() throws Exception {
		super.setUp();
		identityService = (IdentityService) KIMServiceLocatorInternal.getBean("kimIdentityDelegateService");
	}

	@Test
	public void testGetPrincipal() {
		Principal principal = identityService.getPrincipal("KULUSER");
		assertNotNull("principal must not be null", principal);
		assertEquals("Principal name did not match expected result","kuluser", principal.getPrincipalName());
	}

    @Test
    public void testGetPrincipalsByEntityId() {
        List<Principal> principals = identityService.getPrincipalsByEntityId("1136");
        assertNotNull("principal must not be null", principals);
        for (Principal principal: principals) {
            assertEquals("Principal name did not match expected result","kuluser", principal.getPrincipalName());
        }
    }

    @Test
    public void testGetPrincipalsByEntityIdInactive() {
        List<Principal> principals = identityService.getPrincipalsByEntityId("1139");
        assertNotNull("principal must not be null", principals);
        for (Principal principal: principals) {
            assertEquals("Principal name did not match expected result","inactiveusernm", principal.getPrincipalName());
        }
    }

    @Test
    public void testGetPrincipalsByEmployeeId() {
        List<Principal> principals = identityService.getPrincipalsByEmployeeId("0000001138");
        assertNotNull("principal must not be null", principals);
        for (Principal principal: principals) {
            assertEquals("Principal name did not match expected result","activeusernm", principal.getPrincipalName());
            assertEquals("Entity Id did not match expected result","1138", principal.getEntityId());
        }
    }

    @Test
    public void testGetPrincipalsByEmployeeIdInactive() {
        List<Principal> principals = identityService.getPrincipalsByEmployeeId("0000001140");
        assertNotNull("principal must not be null", principals);
        for (Principal principal: principals) {
            assertEquals("Principal name did not match expected result","inactiveempid", principal.getPrincipalName());
            assertEquals("Entity Id did not match expected result","1140", principal.getEntityId());
        }
    }

	@Test
	public void testGetPrincipalByPrincipalName() {
		Principal principal = identityService.getPrincipalByPrincipalName("kuluser");
		assertNotNull("principal must not be null", principal);
		assertEquals("Principal ID did not match expected result","KULUSER", principal.getPrincipalId());
	}
	
	@Test
	public void testGetContainedAttributes() {
		Principal principal = identityService.getPrincipal("p1");
		
		Entity entity = identityService.getEntity(principal.getEntityId());
		assertNotNull( "Entity Must not be null", entity );
		EntityTypeContactInfoContract eet = entity.getEntityTypeContactInfoByTypeCode( "PERSON" );
		assertNotNull( "PERSON EntityEntityType Must not be null", eet );
		assertEquals( "there should be 1 email address", 1, eet.getEmailAddresses().size() );
		assertEquals( "email address does not match", "p1@kuali.org", eet.getDefaultEmailAddress().getEmailAddressUnmasked() );
	}

    @Test
    public void testEntityUpdate() {
        Principal principal = identityService.getPrincipal("p1");
        Entity entity = identityService.getEntity(principal.getEntityId());
        assertNotNull("Entity Must not be null", entity);

        assertEquals("Entity should have 1 name", 1, entity.getNames().size());
        Entity.Builder builder = Entity.Builder.create(entity);
        //lets add a "Name"

        List<EntityName.Builder> names = builder.getNames();
        names.add(getNewEntityName(entity.getId()));

        entity = identityService.updateEntity(builder.build());
        assertNotNull("Entity Must not be null", entity);
        assertEquals("Entity should have 2 names", 2, entity.getNames().size());

        //remove the old name - make sure collection items are removed.
        builder = Entity.Builder.create(entity);
        builder.setNames(Collections.singletonList(getNewEntityName(entity.getId())));

        entity = identityService.updateEntity(builder.build());
        assertNotNull("Entity Must not be null", entity);
        assertEquals("Entity should have 1 names", 1, entity.getNames().size());

    }

    private EntityName.Builder getNewEntityName(String entityId) {

        EntityName.Builder builder = EntityName.Builder.create();
        builder.setActive(true);
        builder.setDefaultValue(false);
        builder.setEntityId(entityId);
        builder.setFirstName("Bob");
        builder.setLastName("Bobbers");
        builder.setNamePrefix("Mr");

        CodedAttribute.Builder nameType = CodedAttribute.Builder.create(identityService.getNameType(
                KimConstants.NameTypes.PRIMARY));
        builder.setNameType(nameType);
        return builder;
    }

}
