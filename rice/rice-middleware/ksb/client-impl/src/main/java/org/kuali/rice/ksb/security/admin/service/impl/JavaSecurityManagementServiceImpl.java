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
package org.kuali.rice.ksb.security.admin.service.impl;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.security.admin.KeyStoreEntryDataContainer;
import org.kuali.rice.ksb.security.admin.service.JavaSecurityManagementService;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * This is an implementation of the {@link JavaSecurityManagementService} interface used by the KSB module 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JavaSecurityManagementServiceImpl implements JavaSecurityManagementService, InitializingBean {

    protected final String CLIENT_KEY_GENERATOR_ALGORITHM = "RSA";
    protected final String CLIENT_SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    protected final int CLIENT_KEY_PAIR_KEY_SIZE = 512;
    private final int CLIENT_CERT_EXPIRATION_DAYS = 9999;

    private static final String MODULE_SHA_RSA_ALGORITHM = "SHA1withRSA";
    private static final String MODULE_JKS_TYPE = "JKS";

    private String moduleKeyStoreLocation;
    private String moduleKeyStoreAlias;
    private String moduleKeyStorePassword;

    private KeyStore moduleKeyStore;
    private PrivateKey modulePrivateKey;

    /**
     * Load the module's keystore and private key for this "application"
     */
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(getModuleKeyStoreLocation())) {
            setModuleKeyStoreLocation(ConfigContext.getCurrentContextConfig().getKeystoreFile());
        }
        if (StringUtils.isEmpty(getModuleKeyStoreAlias())) {
            setModuleKeyStoreAlias(ConfigContext.getCurrentContextConfig().getKeystoreAlias());
        }
        if (StringUtils.isEmpty(getModuleKeyStorePassword())) {
            setModuleKeyStorePassword(ConfigContext.getCurrentContextConfig().getKeystorePassword());
        }
        verifyConfiguration();
        this.moduleKeyStore = loadKeyStore();
        this.modulePrivateKey = loadPrivateKey();
    }

    /**
     * Verifies the configuration of this service and throws an exception if it is not configured properly.
     */
    protected void verifyConfiguration() {
        if (StringUtils.isEmpty(getModuleKeyStoreLocation())) {
            throw new RuntimeException("Value for configuration parameter '" + Config.KEYSTORE_FILE + "' could not be found.  Please ensure that the keystore is configured properly.");
        }
        if (StringUtils.isEmpty(getModuleKeyStoreAlias())) {
            throw new RuntimeException("Value for configuration parameter '" + Config.KEYSTORE_ALIAS + "' could not be found.  Please ensure that the keystore is configured properly.");
        }
        if (StringUtils.isEmpty(getModuleKeyStorePassword())) {
            throw new RuntimeException("Value for configuration parameter '" + Config.KEYSTORE_PASSWORD + "' could not be found.  Please ensure that the keystore is configured properly.");
        }
        File keystoreFile = new File(getModuleKeyStoreLocation());
        if (!keystoreFile.exists()) {
            throw new RuntimeException("Value for configuration parameter '" + Config.KEYSTORE_FILE + "' is invalid.  The file does not exist on the filesystem, location was: '" + getModuleKeyStoreLocation() + "'");
        }
        if (!keystoreFile.canRead()) {
            throw new RuntimeException("Value for configuration parameter '" + Config.KEYSTORE_FILE + "' is invalid.  The file exists but is not readable (please check permissions), location was: '" + getModuleKeyStoreLocation() + "'");
        }
    }

    protected KeyStore loadKeyStore() throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance(getModuleKeyStoreType());
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(getModuleKeyStoreLocation());
            keyStore.load(stream, getModuleKeyStorePassword().toCharArray());
            stream.close();
        } catch (Exception e) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception ignored) {
                }
            }
        }
        return keyStore;
    }

    protected PrivateKey loadPrivateKey() throws GeneralSecurityException {
        return (PrivateKey)getModuleKeyStore().getKey(getModuleKeyStoreAlias(), getModuleKeyStorePassword().toCharArray());
    }

    public void removeClientCertificate(String alias) throws KeyStoreException {
        KeyStore moduleKeyStore = getModuleKeyStore();
        if (!moduleKeyStore.entryInstanceOf(alias, KeyStore.TrustedCertificateEntry.class)) {
            throw new RuntimeException("Only entries of type " + KeyStoreEntryDataContainer.DISPLAYABLE_ENTRY_TYPES.get(KeyStore.TrustedCertificateEntry.class) + " can be removed");
        }
        getModuleKeyStore().deleteEntry(alias);
    }
    
    protected void addClientCertificateToModuleKeyStore(String alias, Certificate clientCertificate) throws KeyStoreException {
        getModuleKeyStore().setEntry(alias, new KeyStore.TrustedCertificateEntry(clientCertificate), null);
    }
    
    public boolean isAliasInKeystore(String alias) throws KeyStoreException {
        return getModuleKeyStore().containsAlias(alias);
    }
    
    public String getCertificateAlias(Certificate certificate) throws KeyStoreException {
        return getModuleKeyStore().getCertificateAlias(certificate);
    }
    
    public KeyStore generateClientKeystore(String alias, String clientPassphrase) throws GeneralSecurityException {
        if (isAliasInKeystore(alias)) {
            throw new KeyStoreException("Alias '" + alias + "' already exists in module keystore");
        }
//        Certificate[] clientCertificateChain = {};
//        PrivateKey clientPrivateKey = null;
        KeyStore ks = null;
        try {
            // generate a key pair for the client
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(CLIENT_KEY_GENERATOR_ALGORITHM);
//            SecureRandom random = SecureRandom.getInstance(CLIENT_SECURE_RANDOM_ALGORITHM);
            keyGen.initialize(CLIENT_KEY_PAIR_KEY_SIZE);
//            keyGen.initialize(new RSAKeyGenParameterSpec(512,RSAKeyGenParameterSpec.F0));
            KeyPair pair = keyGen.generateKeyPair();

//            PublicKey clientPublicKey = pair.getPublic();
//            clientPrivateKey = pair.getPrivate();
//            // generate the Certificate
//            X509V3CertificateGenerator certificateGenerator = new X509V3CertificateGenerator();
////            X509Name nameInfo = new X509Name(false,"CN=" + alias);
//            certificateGenerator.setSignatureAlgorithm("MD5WithRSA");
//            certificateGenerator.setSerialNumber(new java.math.BigInteger("1"));
//            X509Principal nameInfo = new X509Principal("CN=" + alias);
//            certificateGenerator.setIssuerDN(nameInfo);
//            certificateGenerator.setSubjectDN(nameInfo);                       // note: same as issuer
//            certificateGenerator.setNotBefore(new Date());
//            Calendar c = Calendar.getInstance();
//            c.add(Calendar.DATE, CLIENT_CERT_EXPIRATION_DAYS);
//            certificateGenerator.setNotAfter(c.getTime());
//            certificateGenerator.setPublicKey(clientPublicKey);
//            X509Certificate cert = certificateGenerator.generateX509Certificate(clientPrivateKey);
//            clientCertificateChain = new Certificate[]{cert};
//
//            // generate client keyStore file
//            ks = KeyStore.getInstance(getModuleKeyStoreType());
//            ks.load(null, clientPassphrase.toCharArray());
//            // set client private key on keyStore file
//            ks.setEntry(alias, new KeyStore.PrivateKeyEntry(clientPrivateKey, clientCertificateChain), new KeyStore.PasswordProtection(clientPassphrase.toCharArray()));
            Certificate cert = generateCertificate(pair, alias);
            ks = generateKeyStore(cert, pair.getPrivate(), alias, clientPassphrase);
            
            // set the module certificate on the client keyStore file
            ks.setEntry(getModuleKeyStoreAlias(), new KeyStore.TrustedCertificateEntry(getCertificate(getModuleKeyStoreAlias())),  null);

            // add the client certificate to the module keyStore
            addClientCertificateToModuleKeyStore(alias, cert);
            
            return ks;
        } catch (IOException e) {
            throw new RuntimeException("Could not create new KeyStore",e);
        }
    }
    
    protected Certificate generateCertificate(KeyPair keyPair, String alias) throws GeneralSecurityException {
      
      //test that Bouncy Castle provider is present and add it if it's not
      if( Security.getProvider(org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME) == null) {
    	  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
      }
      X509V3CertificateGenerator certificateGenerator = new X509V3CertificateGenerator();
//      X509Name nameInfo = new X509Name(false,"CN=" + alias);
      certificateGenerator.setSignatureAlgorithm("MD5WithRSA");
      certificateGenerator.setSerialNumber(new java.math.BigInteger("1"));
      X509Principal nameInfo = new X509Principal("CN=" + alias);
      certificateGenerator.setIssuerDN(nameInfo);
      certificateGenerator.setSubjectDN(nameInfo);  // note: same as issuer for self signed
      certificateGenerator.setNotBefore(new Date());
      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, CLIENT_CERT_EXPIRATION_DAYS);
      certificateGenerator.setNotAfter(c.getTime());
      certificateGenerator.setPublicKey(keyPair.getPublic());
      return certificateGenerator.generate(keyPair.getPrivate(), org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME);
    }
    
    protected KeyStore generateKeyStore(Certificate cert, PrivateKey privateKey, String alias, String keyStorePassword) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(getModuleKeyStoreType());
        ks.load(null, keyStorePassword.toCharArray());
        // set client private key on keyStore file
        ks.setEntry(alias, new KeyStore.PrivateKeyEntry(privateKey, new Certificate[]{cert}), new KeyStore.PasswordProtection(keyStorePassword.toCharArray()));
        return ks;
    }

    public List<KeyStoreEntryDataContainer> getListOfModuleKeyStoreEntries() {
        List<KeyStoreEntryDataContainer> keyStoreEntries = new ArrayList<KeyStoreEntryDataContainer>();
        try {
            KeyStore moduleKeyStore = getModuleKeyStore();

            // List the aliases
            for (Enumeration<String> enumer = moduleKeyStore.aliases(); enumer.hasMoreElements();) {
                String alias = (String) enumer.nextElement();
                KeyStoreEntryDataContainer dataContainer = new KeyStoreEntryDataContainer(alias,moduleKeyStore.getCreationDate(alias));
                KeyStore.PasswordProtection passwordProtection = null;
                if (moduleKeyStore.isKeyEntry(alias)) {
                    passwordProtection = new KeyStore.PasswordProtection(getModuleKeyStorePassword().toCharArray());
                }
                KeyStore.Entry entry = moduleKeyStore.getEntry(alias, passwordProtection);
                dataContainer.setType(entry.getClass());
                keyStoreEntries.add(dataContainer);
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return keyStoreEntries;
    }

    public String getModuleSignatureAlgorithm() {
        return getModuleAlgorithm();
    }

    /**
     * @see java.security.KeyStore#getCertificate(java.lang.String)
     */
    public Certificate getCertificate(String alias) throws KeyStoreException {
        return getModuleKeyStore().getCertificate(alias);
    }
    
    protected String getModuleKeyStoreType() {
        return MODULE_JKS_TYPE;
    }

    protected String getModuleAlgorithm() {
        return MODULE_SHA_RSA_ALGORITHM;
    }

    public String getModuleKeyStoreLocation() {
        return this.moduleKeyStoreLocation;
    }

    public void setModuleKeyStoreLocation(String moduleKeyStoreLocation) {
        this.moduleKeyStoreLocation = moduleKeyStoreLocation;
    }

    public String getModuleKeyStoreAlias() {
        return this.moduleKeyStoreAlias;
    }

    public void setModuleKeyStoreAlias(String moduleKeyStoreAlias) {
        this.moduleKeyStoreAlias = moduleKeyStoreAlias;
    }

    public String getModuleKeyStorePassword() {
        return this.moduleKeyStorePassword;
    }

    public void setModuleKeyStorePassword(String moduleKeyStorePassword) {
        this.moduleKeyStorePassword = moduleKeyStorePassword;
    }

    public KeyStore getModuleKeyStore() {
        return this.moduleKeyStore;
    }
    
    public PrivateKey getModulePrivateKey() {
        return this.modulePrivateKey;
    }
    
}
