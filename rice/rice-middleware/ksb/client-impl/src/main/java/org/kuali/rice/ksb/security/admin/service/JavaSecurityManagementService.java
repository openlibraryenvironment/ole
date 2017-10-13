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
package org.kuali.rice.ksb.security.admin.service;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.List;

import org.kuali.rice.ksb.security.admin.KeyStoreEntryDataContainer;

/**
 * This is an interface for the Java Security Management piece of the KSB module
 * of Rice. It is used for modifying and creating keystores and certificates
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public interface JavaSecurityManagementService {
 
    public void removeClientCertificate(String alias) throws KeyStoreException;
    
    public KeyStore generateClientKeystore(String alias, String passphrase) throws GeneralSecurityException;
    
    public List<KeyStoreEntryDataContainer> getListOfModuleKeyStoreEntries();
    
    public String getModuleKeyStoreAlias();
    
    public String getModuleKeyStoreLocation();
    
    public String getModuleSignatureAlgorithm();
    
    public Certificate getCertificate(String alias) throws KeyStoreException;
    
    public PrivateKey getModulePrivateKey();
    
    public boolean isAliasInKeystore(String alias) throws KeyStoreException;
    
    public String getCertificateAlias(Certificate certificate) throws KeyStoreException;
    
}
