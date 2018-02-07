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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.namespace.QName;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.impl.config.property.JAXBConfigImpl;
import org.kuali.rice.core.framework.config.property.SimpleConfig;
import org.kuali.rice.ksb.api.bus.support.JavaServiceConfiguration;
import org.kuali.rice.ksb.api.bus.support.JavaServiceDefinition;
import org.kuali.rice.ksb.messaging.serviceconnectors.HttpInvokerConnector;

/**
 * A test which tests the RemoteResourceServiceLocatorImpl class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class HttpInvokerConnectorConfigurationTest {

	private static String simpleConfig = "SIMPLE_CONFIG";
	private static String jaxbConfig = "JAXB_CONFIG";
	
	// We want to test with both impls
	protected Config getConfigObject(String configType){
		Config cRet = null;
		if(simpleConfig.equals(configType)){
			cRet = new SimpleConfig();
		}else if(jaxbConfig.equals(configType)){
			cRet = new JAXBConfigImpl();
		}
		return cRet;
	}
	
	/**
	 * Tests the configuration and initialization of the HttpClient which is
	 * used for the invocation of remote service calls.
	 */
	@Test public void testHttpClientConfiguration() throws Exception {
		testHttpClientConfigurationImpl(simpleConfig);
		testHttpClientConfigurationImpl(jaxbConfig);
	}
	protected void testHttpClientConfigurationImpl(String configType) throws Exception {
		
		JavaServiceDefinition javaServiceDefinition = new JavaServiceDefinition();
		javaServiceDefinition.setServiceName(new QName("test", "test"));
		JavaServiceConfiguration configuration = JavaServiceConfiguration.fromServiceDefinition(javaServiceDefinition);
		
		// test the default configuration
		ConfigContext.init(getConfigObject(configType));
		HttpInvokerConnector httpConnector = new HttpInvokerConnector(configuration, null);
		HttpClient httpClient = httpConnector.getHttpClient();
		
		HttpConnectionManager connectionManager = httpClient.getHttpConnectionManager();
		assertTrue("Should be a multi-threaded connection manager.", connectionManager instanceof MultiThreadedHttpConnectionManager);
		int defaultMaxConnectionsPerHost = connectionManager.getParams().getDefaultMaxConnectionsPerHost();
		assertEquals(20, defaultMaxConnectionsPerHost);
		assertEquals(20, connectionManager.getParams().getMaxTotalConnections());
		assertEquals(10000, connectionManager.getParams().getConnectionTimeout());
		assertEquals(10000, httpClient.getParams().getConnectionManagerTimeout());
		assertEquals(CookiePolicy.RFC_2109, httpClient.getParams().getCookiePolicy());
		
		// re-init the core with some of these paramters changed
		Config config = getConfigObject(configType);		
		config.putProperty("http.connection-manager.max-total", "500");
		config.putProperty("http.connection-manager.timeout", "5000");
		config.putProperty("http.connection.timeout", "15000");
		config.putProperty("http.somerandomproperty", "thisismyproperty");
		config.putProperty("http.authentication.preemptive", "false");
		ConfigContext.init(config);
		
		httpConnector = new HttpInvokerConnector(configuration, null);
		httpClient = httpConnector.getHttpClient();
		
		connectionManager = httpClient.getHttpConnectionManager();
		assertTrue("Should be a multi-threaded connection manager.", connectionManager instanceof MultiThreadedHttpConnectionManager);
		defaultMaxConnectionsPerHost = connectionManager.getParams().getDefaultMaxConnectionsPerHost();
		assertEquals(20, defaultMaxConnectionsPerHost);
		assertEquals(500, connectionManager.getParams().getMaxTotalConnections());
		assertEquals(15000, connectionManager.getParams().getConnectionTimeout());
		assertEquals(5000, httpClient.getParams().getConnectionManagerTimeout());
		assertEquals(CookiePolicy.RFC_2109, httpClient.getParams().getCookiePolicy());
		assertFalse(httpClient.getParams().isAuthenticationPreemptive());
		
		// do another one that checks that booleans are working properly
		config = getConfigObject(configType);		
		config.putProperty("http.authentication.preemptive", "true");
		ConfigContext.init(config);
		
		httpConnector = new HttpInvokerConnector(configuration, null);
		httpClient = httpConnector.getHttpClient();
		
		assertTrue(httpClient.getParams().isAuthenticationPreemptive());
		
		// check setting the classname of the connection manager
		config = getConfigObject(configType);		
		config.putProperty("http.connection-manager.class", MyHttpConnectionManager.class.getName());
		ConfigContext.init(config);
		
		httpConnector = new HttpInvokerConnector(configuration, null);
		httpClient = httpConnector.getHttpClient();
		
		connectionManager = httpClient.getHttpConnectionManager();
		assertTrue("Should be a MyHttpConnectionManager.", connectionManager instanceof MyHttpConnectionManager);
		
		// now try setting the retry handler which expects an object that we can't pipe through our
		// String-based configuration.  This is an illegal parameter to configure and we should
		// recieve a WorkflowRuntimeException
		config = getConfigObject(configType);		
		config.putProperty("http.method.retry-handler", "badparm");
		ConfigContext.init(config);
		
		try {
			httpConnector = new HttpInvokerConnector(configuration, null);
			fail("An exception should have been thrown because the retry handler is an illegal parameter.");
		} catch (Exception e) {
		    //nothing to do here
		}
		
	}
	
	public static class MyHttpConnectionManager extends SimpleHttpConnectionManager {
	    // nothing extra needed
	}
	
}
