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
import org.kuali.rice.core.api.security.credentials.Credentials;
import org.kuali.rice.core.api.security.credentials.CredentialsType;

import static org.junit.Assert.*;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 * 
 */
public class UsernamePasswordCredentialsSourceTest {

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private UsernamePasswordCredentialsSource credentialsSource;

    @Before
	public void setUp() throws Exception {
		this.credentialsSource = new UsernamePasswordCredentialsSource(
				USERNAME, PASSWORD);
	}

    @Test
	public void testGetter() {
		final Credentials c = this.credentialsSource
				.getCredentials("http://www.cnn.com");
		assertNotNull(c);
		assertTrue(c instanceof UsernamePasswordCredentials);

		final UsernamePasswordCredentials upc = (UsernamePasswordCredentials) c;
		assertEquals(USERNAME, upc.getUsername());
		assertEquals(PASSWORD, upc.getPassword());

		assertEquals(CredentialsType.USERNAME_PASSWORD,
				this.credentialsSource.getSupportedCredentialsType());
	}
}
