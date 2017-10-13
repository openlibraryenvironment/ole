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
package org.kuali.rice.ksb.api.bus.support

import java.lang.reflect.Field

import javax.xml.bind.JAXBContext
import javax.xml.bind.Unmarshaller
import javax.xml.namespace.QName

import org.junit.Test
import org.kuali.rice.core.api.security.credentials.CredentialsType
import org.kuali.rice.ksb.api.registry.ServiceRegistry
import org.kuali.rice.ksb.test.JAXBAssert

class SoapServiceConfigurationTest {

	private static final QName SERVICE_NAME = new QName(APPLICATION_ID, "myRadService");
	private static final String APPLICATION_ID = "TEST";
	private static final URL ENDPOINT_URL = new URL("http://this.is.my.url");
	private static final String SERVICE_VERSION = "1.0";
	private static final String SERVICE_INTERFACE = ServiceRegistry.class.getName();
	private static final String MESSAGE_EXCEPTION_HANDLER = "MyMessageExceptionHandler";
	
	private static final String XML_REQUIRED_ONLY = """
<soapServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>SOAP</type>
	<queue>true</queue>
	<busSecurity>true</busSecurity>
	<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
	<jaxWsService>false</jaxWsService>
</soapServiceConfiguration>
	"""
	
	private static final String XML_WITH_JAXWS = """
<soapServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>SOAP</type>
	<queue>true</queue>
	<busSecurity>true</busSecurity>
	<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
	<jaxWsService>true</jaxWsService>
</soapServiceConfiguration>
	"""
	
	private static final String XML_FULL = """
<soapServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>SOAP</type>
	<queue>false</queue>
	<priority>10</priority>
	<retryAttempts>3</retryAttempts>
	<millisToLive>10000</millisToLive>
	<messageExceptionHandler>MyMessageExceptionHandler</messageExceptionHandler>
	<busSecurity>false</busSecurity>
	<credentialsType>X509</credentialsType>
	<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
	<jaxWsService>true</jaxWsService>
</soapServiceConfiguration>
		"""
	
	private static final String XML_WITH_FUTURE_ELEMENTS = """
<soapServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>SOAP</type>
	<queue>false</queue>
	<priority>10</priority>
	<retryAttempts>3</retryAttempts>
	<millisToLive>10000</millisToLive>
	<messageExceptionHandler>MyMessageExceptionHandler</messageExceptionHandler>
	<busSecurity>false</busSecurity>
	<credentialsType>X509</credentialsType>
	<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
	<jaxWsService>true</jaxWsService>
	<thisIsTotallyNew brandNew="true">some content from a new version here</thisIsTotallyNew>
	<unmarshallingShouldStillEatThisJustFine/>
</soapServiceConfiguration>
		"""
		
	private SoapServiceDefinition createBaseDefinition() {
		SoapServiceDefinition definition = new SoapServiceDefinition();
		definition.setServiceName(SERVICE_NAME)
		definition.setEndpointUrl(ENDPOINT_URL);
		definition.setApplicationId(APPLICATION_ID);
		definition.setServiceVersion(SERVICE_VERSION);
		definition.setServiceInterface(SERVICE_INTERFACE);
		return definition;
	}
	
	private SoapServiceConfiguration create_requiredOnly() {
		SoapServiceDefinition definition = createBaseDefinition();
		return SoapServiceConfiguration.fromServiceDefinition(definition);
	}
	
	private SoapServiceConfiguration create_withJaxWs() {
		SoapServiceDefinition definition = createBaseDefinition();
		definition.setJaxWsService(true);
		return SoapServiceConfiguration.fromServiceDefinition(definition);
	}
	
	private SoapServiceConfiguration create_full() {
		// twiddles with the definition a bit and adds some additional values to test serialization
		// of everything from AbstractServiceDefinition and SoapServiceDefinition
		SoapServiceDefinition definition = createBaseDefinition();
		definition.setJaxWsService(true);
		definition.setQueue(false);
		definition.setBusSecurity(false);
		definition.setPriority(10);
		definition.setRetryAttempts(3);
		definition.setMillisToLive(10000);
		definition.setMessageExceptionHandler(MESSAGE_EXCEPTION_HANDLER);
		definition.setCredentialsType(CredentialsType.X509);
		return SoapServiceConfiguration.fromServiceDefinition(definition);
	}
	
	@Test
	void testXml_Marshal_Unmarshal_requiredOnly() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create_requiredOnly(), XML_REQUIRED_ONLY, SoapServiceConfiguration.class)
	}
	
	@Test
	void testXml_Marshal_Unmarshal_withJaxWs() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create_withJaxWs(), XML_WITH_JAXWS, SoapServiceConfiguration.class)
	}
	
	@Test
	void testXml_Marshal_Unmarshal_full() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create_full(), XML_FULL, SoapServiceConfiguration.class)
	}
	
	@Test
	void testXml_Unmarshal_withFutureElements() {
		JAXBContext context = JAXBContext.newInstance(SoapServiceConfiguration.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		SoapServiceConfiguration configuration = (SoapServiceConfiguration)unmarshaller.unmarshal(new StringReader(XML_WITH_FUTURE_ELEMENTS));
		assert configuration != null;
			
		assert configuration.getServiceName() == SERVICE_NAME;
		assert configuration.getApplicationId() == APPLICATION_ID;
		assert configuration.getEndpointUrl() == ENDPOINT_URL;
		assert configuration.getServiceVersion() == SERVICE_VERSION;
		
		Field _futureElements = AbstractServiceConfiguration.class.getDeclaredField("_futureElements");
		_futureElements.setAccessible(true);
		assert !((List<?>)_futureElements.get(configuration)).isEmpty();
		
	}
	
	// TODO could use some additional tests here which test other pieces of SoapServiceConfiguration
	
}
