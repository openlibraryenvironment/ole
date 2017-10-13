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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.api.identity.affiliation.EntityAffiliation;
import org.kuali.rice.kim.api.identity.entity.Entity;
import org.kuali.rice.kim.api.identity.entity.EntityDefault;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.privacy.EntityPrivacyPreferences;
import org.kuali.rice.kim.api.identity.type.EntityTypeContactInfo;

import org.kuali.rice.kim.service.KIMServiceLocatorInternal;
import org.kuali.rice.kim.test.KIMTestCase;

import org.kuali.rice.test.BaselineTestCase;
import org.kuali.rice.test.SQLDataLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.LdapTestUtils;

import javax.xml.namespace.QName;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BaselineTestCase.BaselineMode(BaselineTestCase.Mode.NONE)
public class LDAPIdentityServiceImplTest extends KIMTestCase {
    private static final DistinguishedName baseName = new DistinguishedName("o=Whoniverse");

    private static final String PRINCIPAL = "uid=admin,ou=system";
    private static final String CREDENTIALS = "secret";

    // This port MUST be free on local host for these unit tests to function.
    private static int PORT = 10389;
    private IdentityService identityService;


    @BeforeClass
    public static void startLDAPServer() throws Exception {
        LdapTestUtils.startApacheDirectoryServer(PORT, baseName.toString(), "test", PRINCIPAL, CREDENTIALS, null);
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://127.0.0.1:" + PORT);
        contextSource.setUserDn("");
        contextSource.setPassword("");
        contextSource.setPooled(false);
        contextSource.afterPropertiesSet();

        // Create the Sprint LDAP template
        LdapTemplate template = new LdapTemplate(contextSource);

        // Clear out any old data - and load the test data
        LdapTestUtils.cleanAndSetup(template.getContextSource(), baseName, new ClassPathResource("ldap/testdata.ldif"));
        System.out.println("____________Started LDAP_________");
    }

    @AfterClass
    public static void shutdownLDAP() throws Exception {
        LdapTestUtils.destroyApacheDirectoryServer(PRINCIPAL, CREDENTIALS);
         System.out.println("____________Shutdown LDAP_________");
    }


    public void setUp() throws Exception {
        super.setUp();
        identityService = (IdentityService) KIMServiceLocatorInternal.getBean("kimLDAPIdentityDelegateService");

    }


    @Test
    public void testGetEntity() {
        Entity entity = identityService.getEntity("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityByPrincipalId() {
        Entity entity = identityService.getEntityByPrincipalId("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityByPrincipalName() {
        Entity entity = identityService.getEntityByPrincipalName("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityDefault() {
        EntityDefault entity = identityService.getEntityDefault("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityDefaultByPrincipalId() {
        EntityDefault entity = identityService.getEntityDefaultByPrincipalId("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityDefaultByPrincipalName() {
        EntityDefault entity = identityService.getEntityDefaultByPrincipalName("williamh");
        assertNotNull("Entity must not be null", entity);
        assertEquals("Entity name matched expected result", "williamh", entity.getPrincipals().get(0)
                .getPrincipalName());
    }

    @Test
    public void testGetEntityPrivacyPreferences() {
        EntityPrivacyPreferences entityPrivacyPreferences = identityService.getEntityPrivacyPreferences("williamh");
        assertNotNull("Entity must not be null", entityPrivacyPreferences);

    }

    @Test
    public void testGetPrincipal() {
        Principal principal = identityService.getPrincipal("williamh");
        assertNotNull("Principal must not be null", principal);
        assertEquals("Principal name did not match expected result", "williamh", principal.getPrincipalName());
    }

    @Test
    public void testGetPrincipalByPrincipalName() {
        Principal principal = identityService.getPrincipalByPrincipalName("williamh");
        assertNotNull("principal must not be null", principal);
        assertEquals("Principal ID did not match expected result", "williamh", principal.getPrincipalId());
    }

    @Test
    public void testGetContainedAttributes() {
        Principal principal = identityService.getPrincipal("williamh");

        Entity entity = identityService.getEntity(principal.getEntityId());
        assertNotNull("Entity Must not be null", entity);
        EntityTypeContactInfo eet = entity.getEntityTypeContactInfoByTypeCode("PERSON");
        List<EntityAffiliation> ea = entity.getAffiliations();
        assertNotNull("PERSON EntityEntityType Must not be null", eet);
        assertEquals("there should be 1 email address", 1, eet.getEmailAddresses().size());
        assertEquals("Email address does not match", "williamh@test.edu",
                eet.getDefaultEmailAddress().getEmailAddressUnmasked());
        assertEquals("there should be 1 phone number", 1, eet.getPhoneNumbers().size());
        assertEquals("The Phone number does not match", "111-111-1111", eet.getPhoneNumbers().get(0)
                .getPhoneNumberUnmasked());
        assertEquals("there should be 1 phone number", 1, ea.size());
        assertEquals("The Affiliations do not match", "STAFF", ea.get(0).getAffiliationType().getCode());
    }

}
