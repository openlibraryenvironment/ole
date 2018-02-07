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

/**
 * Defines the types of Credentials that the system is willing to accept.
 * These should coincide with what Spring Security is willing to accept.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public enum CredentialsType {
	CAS, USERNAME_PASSWORD, JAAS, X509
}
