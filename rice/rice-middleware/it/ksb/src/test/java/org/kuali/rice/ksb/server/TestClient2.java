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
package org.kuali.rice.ksb.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.BaseTestServer;

import java.util.HashMap;
import java.util.Map;

public class TestClient2 extends BaseTestServer {

	private static final Logger LOG = Logger.getLogger(TestClient2.class);

	private static final String CONTEXT = "/TestClient2";
	
	/**
	 * If this property is set on the environment then it will use the keystore at the specified location as a custom keystore
	 */
	public static final String CUSTOM_KEYSTORE = "CustomKeyStore";
	
	private static Map<String, Object> environment = new HashMap<String, Object>();
	
	@Override
	protected Server createServer() {
		
		Server server = new Server(new Integer(ConfigContext.getCurrentContextConfig().getProperty("ksb.client2.port")));
		String location = ConfigContext.getCurrentContextConfig().getProperty("client2.location");
		LOG.debug("#####################################");
		LOG.debug("#");
		LOG.debug("#  Starting Client2 using location " + location);
		LOG.debug("#");
		LOG.debug("#####################################");
		WebAppContext context = new WebAppContext(location, CONTEXT);	
		server.setHandler(context);
		return server;
	}
	
	public static Map<String, Object> getEnvironment() {
		return environment;
	}
}
