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
package org.kuali.rice.ksb.security.credentials;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 *
 */
public class X509CredentialsSourceTest {

	private X509CredentialsSource credentialsSource;
	
	private X509Certificate cert = new KualiX509Certificate();

    @Before
	public void setUp() throws Exception {
		this.credentialsSource = new X509CredentialsSource(cert);
	}

    @Test
	public void testX509Certificate() {
		final X509Credentials context = (X509Credentials) this.credentialsSource.getCredentials("test");
		assertNotNull(context);
		final X509Certificate cert = context.getX509Certificate();
		
		assertEquals(this.cert, cert);
	}
	
	public static class KualiX509Certificate extends X509Certificate {
		
		protected KualiX509Certificate() {
			// nothing to do
		}

		public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
			// nothing to do
		}

		public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
			// nothing to do
		}

		public int getBasicConstraints() {
			return 0;
		}

		public Principal getIssuerDN() {
			return null;
		}

		public boolean[] getIssuerUniqueID() {
			return null;
		}

		public boolean[] getKeyUsage() {
			return null;
		}

		public Date getNotAfter() {
			return null;
		}

		public Date getNotBefore() {
			return null;
		}

		public BigInteger getSerialNumber() {
			return null;
		}

		public String getSigAlgName() {
			return null;
		}

		public String getSigAlgOID() {
			return null;
		}

		public byte[] getSigAlgParams() {
			return null;
		}

		public byte[] getSignature() {
			return null;
		}

		public Principal getSubjectDN() {
			return null;
		}

		public boolean[] getSubjectUniqueID() {
			return null;
		}

		public byte[] getTBSCertificate() throws CertificateEncodingException {
			return null;
		}

		public int getVersion() {
			return 0;
		}

		public Set<String> getCriticalExtensionOIDs() {
			return null;
		}

		public byte[] getExtensionValue(String arg0) {
			return null;
		}

		public Set<String> getNonCriticalExtensionOIDs() {
			return null;
		}

		public boolean hasUnsupportedCriticalExtension() {
			return false;
		}

		public byte[] getEncoded() throws CertificateEncodingException {
			return null;
		}

		public PublicKey getPublicKey() {
			return null;
		}

		public String toString() {
			return null;
		}

		public void verify(PublicKey arg0, String arg1) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
			// nothing to do
		}

		public void verify(PublicKey arg0) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
			// nothing to do
		}
	}
}
