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

import java.security.cert.X509Certificate;

import org.kuali.rice.core.api.security.credentials.Credentials;

public class X509Credentials implements Credentials {

	private final X509Certificate certificate;
	
	public X509Credentials(final X509Certificate certificate) {
		this.certificate = certificate;
	}
	
	public X509Certificate getX509Certificate() {
		return this.certificate;
	}
}
