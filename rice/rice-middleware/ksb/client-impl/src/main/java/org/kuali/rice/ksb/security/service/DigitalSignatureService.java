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
package org.kuali.rice.ksb.security.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.Certificate;

/**
 * A service for aquiring Signatures for signing and verification of messages.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DigitalSignatureService {

	public Signature getSignatureForSigning() throws IOException, GeneralSecurityException;
	
	public Signature getSignatureForVerification(String verificationAlias) throws IOException, GeneralSecurityException;
	
    public Signature getSignatureForVerification(Certificate certificate) throws IOException, GeneralSecurityException;
}
