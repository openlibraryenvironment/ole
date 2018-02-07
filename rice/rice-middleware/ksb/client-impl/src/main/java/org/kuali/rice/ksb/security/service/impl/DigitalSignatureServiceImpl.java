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
package org.kuali.rice.ksb.security.service.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.security.admin.service.JavaSecurityManagementService;
import org.kuali.rice.ksb.security.service.DigitalSignatureService;
import org.kuali.rice.ksb.util.KSBConstants;

public class DigitalSignatureServiceImpl implements DigitalSignatureService {

	public Signature getSignatureForSigning() throws IOException, GeneralSecurityException {
		Signature signature = getSignature();
		signature.initSign(getJavaSecurityManagementService().getModulePrivateKey());
		return signature;
	}

    public Signature getSignatureForVerification(String verificationAlias) throws IOException, GeneralSecurityException {
        Certificate cert = getJavaSecurityManagementService().getCertificate(verificationAlias);
        return getSignatureForVerification(cert);
    }

    public Signature getSignatureForVerification(Certificate certificate) throws IOException, GeneralSecurityException {
        if (certificate == null) {
            throw new CertificateException("Could not find certificate");
        }
        PublicKey publicKey = certificate.getPublicKey();
        if (publicKey == null) {
            throw new KeyException("Could not find the public key from valid certificate");
        }
        Signature signature = getSignature();
        signature.initVerify(publicKey);
        return signature;
    }
    
	protected Signature getSignature() throws GeneralSecurityException {
		return Signature.getInstance(getJavaSecurityManagementService().getModuleSignatureAlgorithm());
	}
	
	protected JavaSecurityManagementService getJavaSecurityManagementService() {
		return (JavaSecurityManagementService)GlobalResourceLoader.getService(KSBConstants.ServiceNames.JAVA_SECURITY_MANAGEMENT_SERVICE);
	}


}
