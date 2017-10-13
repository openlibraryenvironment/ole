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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.xml.namespace.QName;

import org.apache.commons.httpclient.URI;
import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.dynamic.DynamicClientFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.ksb.messaging.remotedservices.EchoService;
import org.kuali.rice.ksb.messaging.remotedservices.JaxWsEchoService;
import org.kuali.rice.ksb.messaging.remotedservices.SOAPService;
import org.kuali.rice.ksb.service.KSBServiceLocator;
import org.kuali.rice.ksb.test.KSBTestCase;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SOAPServiceTest extends KSBTestCase {
	
	public boolean startClient1() {
		return true;
	}
	
	private String getEndpointUrl() {
		return "http://localhost:" + 
			getClient1Port() +
			"/TestClient1/remoting/secure/soap-echoServiceSecure";
	}
	
	private String getWsdlUrl() {
		return "http://localhost:" +
			getClient1Port() +
			"/TestClient1/remoting/soap-echoService?wsdl";
	}
	
	private String getClient1Port() {
		return ConfigContext.getCurrentContextConfig().getProperty("ksb.client1.port");
	}

	
	@Test public void testSimpleSOAPService() throws Exception{
 
		
		EchoService echoService = (EchoService)GlobalResourceLoader.getService(new QName("TestCl1", "soap-echoService"));
		String result = echoService.trueEcho("Yo yo yo");
		assertNotNull(result);
		
		QName serviceName = new QName("testNameSpace", "soap-repeatTopic");
		SOAPService soapService = (SOAPService) GlobalResourceLoader.getService(serviceName);
		soapService.doTheThing("hello");
	}
	
	@Test
	public void testJaxWsSOAPService(){	
		
		JaxWsEchoService jaxwsEchoService = (JaxWsEchoService) GlobalResourceLoader.getService(new QName("TestCl1", "jaxwsEchoService"));
		String result = jaxwsEchoService.doEcho("Fi Fi Fo Fum");
		assertTrue(("Fi Fi Fo Fum").equals(result));
	}
	
	@Test 
	public void testBusSecureSoapService() throws Exception{
		//Create non-secure client to access secure service
		ClientProxyFactoryBean clientFactory;		
		clientFactory = new ClientProxyFactoryBean();

		clientFactory.setBus(KSBServiceLocator.getCXFBus());
		clientFactory.getServiceFactory().setDataBinding(new AegisDatabinding());	
		clientFactory.setServiceClass(EchoService.class);
		clientFactory.setServiceName(new QName("urn:TestCl1", "soap-echoServiceSecure"));
		clientFactory.setAddress(new URI(getEndpointUrl(), false).toString());
		clientFactory.getInInterceptors().add(new LoggingInInterceptor());
		clientFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		EchoService echoService = (EchoService)clientFactory.create();
		
		try{
			echoService.echo("I can't echo");
			fail("Expected failure using non-secure client with secure service");
		} catch (SoapFault sf){
			sf.printStackTrace();
			assertTrue("Non-secure client did not get expected exception.",
					sf.getMessage().startsWith("An error was discovered processing the <wsse:Security> header"));
		}
		
		//Now try a secure client
		echoService = (EchoService)GlobalResourceLoader.getService(new QName("urn:TestCl1", "soap-echoServiceSecure"));
		String result = echoService.echo("I can echo");
		assertTrue("I can echo".equals(result));		
	}

	@Test
	public void testWsdlGeneration() throws Exception {
		//This is similar to a KEW test, but good to have it as part of KSB tests.
		
		DynamicClientFactory dcf = DynamicClientFactory.newInstance(KSBServiceLocator.getCXFBus());
		
		Client client = dcf.createClient(new URI(getWsdlUrl(), false).toString());
		client.getInInterceptors().add(new LoggingInInterceptor());
		client.getOutInterceptors().add(new LoggingOutInterceptor());
		Object[] results = client.invoke("trueEcho", new Object[] { "testing" });
		assertNotNull(results);
		assertTrue(results.length > 0);
		
	}

}
