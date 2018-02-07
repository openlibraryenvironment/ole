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
package org.kuali.rice.core.api.security.credentials;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * CredentialsSourceFactory constructs or returns an existing instance of a
 * CredentialsSource based on the type of Credentials requested.
 * <p>
 * It will return null if it cannot find or create an instance of the required
 * type.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 * 
 */
public final class CredentialsSourceFactory implements InitializingBean {

	private final Log log = LogFactory.getLog(this.getClass());

	private List<CredentialsSource> credentialsSources;

	private Map<CredentialsType, CredentialsSource> credentialsSourcesByType = new HashMap<CredentialsType, CredentialsSource>();

	public CredentialsSource getCredentialsForType(
			final CredentialsType credentialsType) {
		return credentialsSourcesByType.get(credentialsType);
	}

	public void afterPropertiesSet() throws Exception {
		if (credentialsSources != null) {
			for (final CredentialsSource credentialsSource : this.credentialsSources) {
				this.credentialsSourcesByType.put(credentialsSource
						.getSupportedCredentialsType(), credentialsSource);
			}
		} else {
			log
					.warn("No CredentialsSources set.  No security will be provided on the bus.");
		}
	}

	public void setCredentialsSources(
			final List<CredentialsSource> credentialsSources) {
		this.credentialsSources = credentialsSources;
	}
}
