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

public class TestClient1 extends BaseTestServer {
	
	private static final Logger LOG = Logger.getLogger(TestClient1.class);

	private static final String CONTEXT = "/TestClient1";
	
	@Override
	protected Server createServer() {
		Server server = new Server(new Integer(ConfigContext.getCurrentContextConfig().getProperty("ksb.client1.port")));
		String location = ConfigContext.getCurrentContextConfig().getProperty("client1.location");
		LOG.debug("#####################################");
		LOG.debug("#");
		LOG.debug("#  Starting Client1 using location " + location);
		LOG.debug("#");
		LOG.debug("#####################################");
		WebAppContext context = new WebAppContext(location, CONTEXT);	
		server.setHandler(context);
		return server;
	}
}
