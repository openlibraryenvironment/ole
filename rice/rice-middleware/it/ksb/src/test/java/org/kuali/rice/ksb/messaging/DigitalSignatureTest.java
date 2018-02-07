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
package org.kuali.rice.ksb.messaging;

import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.test.KSBTestCase;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class DigitalSignatureTest extends KSBTestCase {


	/**
	 * This method tests the existing rice keystore file
	 * 
	 * @throws Exception
	 */
	@Test public void testSigning() throws Exception {
		
		Config config = ConfigContext.getCurrentContextConfig();
//		config.parseConfig(); 
//		
		Signature rsa = Signature.getInstance("SHA1withRSA");
		String keystoreLocation = config.getKeystoreFile();
		String keystoreAlias = config.getKeystoreAlias();
		String keystorePassword = config.getKeystorePassword();
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(keystoreLocation), keystorePassword.toCharArray());
		PrivateKey privateKey = (PrivateKey)keystore.getKey(keystoreAlias, keystorePassword.toCharArray());
        
		rsa.initSign(privateKey);
		
		String imLovinIt = "Ba-da-ba-ba-baa, I'm lovin' it!";
		rsa.update(imLovinIt.getBytes());
		
		byte[] sigToVerify = rsa.sign();
		
		
		PublicKey publicKey = keystore.getCertificate(keystoreAlias).getPublicKey();
	    Signature verifySig = Signature.getInstance("SHA1withRSA");
	    verifySig.initVerify(publicKey);
	    verifySig.update(imLovinIt.getBytes());
	    boolean verifies = verifySig.verify(sigToVerify);
	    System.out.println("signature verifies: " + verifies);
		
	}
	
}
