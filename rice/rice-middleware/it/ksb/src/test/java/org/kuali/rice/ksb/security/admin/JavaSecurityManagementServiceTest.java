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
package org.kuali.rice.ksb.security.admin;

import org.junit.Test;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.test.KSBTestCase;

import javax.xml.namespace.QName;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a test class used to test the KSB java security operation using certificates and keystores 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JavaSecurityManagementServiceTest extends KSBTestCase {
    
    private static final String TEST_CLIENT_ALIAS = "test_alias";
    private static final String TEST_CLIENT_PASSWORD = "test_password";
    private static final String MOCK_JAVA_SECURITY_MANAGEMENT_SERVICE_BEAN_ID = "testJavaSecurityManagementService";

    private MockJavaSecurityManagementService getMockJavaSecurityManagementService() {
        QName serviceName = new QName("KEW", MOCK_JAVA_SECURITY_MANAGEMENT_SERVICE_BEAN_ID);
        return (MockJavaSecurityManagementService)GlobalResourceLoader.getService(serviceName);
    }

    @Test 
    public void testCertificatesExistInKeyStores() throws Exception {
        MockJavaSecurityManagementService securityService = getMockJavaSecurityManagementService();
        String moduleKeyStoreAlias = securityService.getModuleKeyStoreAlias();
        
        // generate the client keystore file
        KeyStore clientKeyStore = securityService.generateClientKeystore(TEST_CLIENT_ALIAS, TEST_CLIENT_PASSWORD);

        // verify that the module cert is in the client keystore file
        verifyKeyStoreContents(clientKeyStore, "client", moduleKeyStoreAlias, TEST_CLIENT_ALIAS);
        assertEquals("Certs do not match in client keystore file", securityService.getCertificate(moduleKeyStoreAlias), clientKeyStore.getCertificate(moduleKeyStoreAlias));
        
        // verify that the client cert is in the module keystore file
        verifyKeyStoreContents(securityService.getModuleKeyStore(), "module", TEST_CLIENT_ALIAS, securityService.getModuleKeyStoreAlias());
        assertEquals("Certs do not match in module keystore file", clientKeyStore.getCertificate(moduleKeyStoreAlias), securityService.getCertificate(moduleKeyStoreAlias));
    }
    
    private void verifyKeyStoreContents(KeyStore keyStore, String keyStoreQualifier, String certificateEntryAlias, String privateKeyEntryAlias) throws GeneralSecurityException {
        assertTrue("Alias for Certificate Entry '" + certificateEntryAlias + "' should exist in " + keyStoreQualifier + " keystore file", keyStore.containsAlias(certificateEntryAlias));
        assertTrue("Alias '" + certificateEntryAlias + "' should be Certificate Entry in " + keyStoreQualifier + " keystore file", keyStore.isCertificateEntry(certificateEntryAlias));
        assertTrue("Alias for Private Key Entry '" + privateKeyEntryAlias + "' should exist in " + keyStoreQualifier + " keystore file", keyStore.containsAlias(privateKeyEntryAlias));
        assertTrue("Alias '" + privateKeyEntryAlias + "' should be Private Key Entry in " + keyStoreQualifier + " keystore file", keyStore.entryInstanceOf(privateKeyEntryAlias, KeyStore.PrivateKeyEntry.class));
    }

}
