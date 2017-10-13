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
import org.kuali.rice.ksb.api.bus.ServiceBus
import org.kuali.rice.ksb.api.registry.ServiceRegistry
import org.kuali.rice.ksb.test.JAXBAssert

class JavaServiceConfigurationTest {

	private static final QName SERVICE_NAME = new QName(APPLICATION_ID, "myRadService");
	private static final String APPLICATION_ID = "TEST";
	private static final URL ENDPOINT_URL = new URL("http://this.is.my.url");
	private static final String SERVICE_VERSION = "1.0";
	
	private static final String XML_REQUIRED_ONLY = """
<javaServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>httpInvoker</type>
	<queue>true</queue>
	<busSecurity>true</busSecurity>
	<serviceInterfaces />
</javaServiceConfiguration>
	"""
	private static final String XML_WITH_SERVICE_INTERFACES = """
<javaServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
	<serviceName>{TEST}myRadService</serviceName>
	<endpointUrl>http://this.is.my.url</endpointUrl>
	<applicationId>TEST</applicationId>
	<serviceVersion>1.0</serviceVersion>
	<type>httpInvoker</type>
	<queue>true</queue>
	<busSecurity>true</busSecurity>
	<serviceInterfaces>
		<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
		<serviceInterface>org.kuali.rice.ksb.api.bus.ServiceBus</serviceInterface>
	</serviceInterfaces>
</javaServiceConfiguration>
	"""
	
	private static final String XML_WITH_FUTURE_ELEMENTS = """
	<javaServiceConfiguration xmlns="http://rice.kuali.org/ksb/v2_0">
		<serviceName>{TEST}myRadService</serviceName>
		<endpointUrl>http://this.is.my.url</endpointUrl>
		<applicationId>TEST</applicationId>
		<serviceVersion>1.0</serviceVersion>
		<type>httpInvoker</type>
		<queue>true</queue>
		<busSecurity>true</busSecurity>
		<serviceInterfaces>
			<serviceInterface>org.kuali.rice.ksb.api.registry.ServiceRegistry</serviceInterface>
			<serviceInterface>org.kuali.rice.ksb.api.bus.ServiceBus</serviceInterface>
		</serviceInterfaces>
		<thisIsTotallyNew brandNew="true">some content from a new version here</thisIsTotallyNew>
		<unmarshallingShouldStillEatThisJustFine/>
	</javaServiceConfiguration>
		"""
		
	
	
	private JavaServiceConfiguration create_requiredOnly() {
		JavaServiceDefinition definition = new JavaServiceDefinition();
		definition.setServiceName(SERVICE_NAME)
		definition.setEndpointUrl(ENDPOINT_URL);
		definition.setApplicationId(APPLICATION_ID);
		definition.setServiceVersion(SERVICE_VERSION);
		return JavaServiceConfiguration.fromServiceDefinition(definition);
	}
	
	private JavaServiceConfiguration create_withServiceInterfaces() {
		JavaServiceDefinition definition = new JavaServiceDefinition();
		definition.setServiceName(SERVICE_NAME)
		definition.setEndpointUrl(ENDPOINT_URL);
		definition.setApplicationId(APPLICATION_ID);
		definition.setServiceVersion(SERVICE_VERSION);
		List<String> serviceInterfaces = new ArrayList<String>();
		serviceInterfaces.add(ServiceRegistry.class.getName());
		serviceInterfaces.add(ServiceBus.class.getName());
		definition.setServiceInterfaces(serviceInterfaces);
		return JavaServiceConfiguration.fromServiceDefinition(definition);
	}
	
	@Test
	void testXml_Marshal_Unmarshal_requiredOnly() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create_requiredOnly(), XML_REQUIRED_ONLY, JavaServiceConfiguration.class)
	}
	
	@Test
	void testXml_Marshal_Unmarshal_withServiceInterfaces() {
		JAXBAssert.assertEqualXmlMarshalUnmarshal(create_withServiceInterfaces(), XML_WITH_SERVICE_INTERFACES, JavaServiceConfiguration.class)
	}
	
	@Test
	void testXml_Unmarshal_withFutureElements() {
		JAXBContext context = JAXBContext.newInstance(JavaServiceConfiguration.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		JavaServiceConfiguration configuration = (JavaServiceConfiguration)unmarshaller.unmarshal(new StringReader(XML_WITH_FUTURE_ELEMENTS));
		assert configuration != null;
			
		assert configuration.getServiceName() == SERVICE_NAME;
		assert configuration.getApplicationId() == APPLICATION_ID;
		assert configuration.getEndpointUrl() == ENDPOINT_URL;
		assert configuration.getServiceVersion() == SERVICE_VERSION;
		
		Field _futureElements = AbstractServiceConfiguration.class.getDeclaredField("_futureElements");
		_futureElements.setAccessible(true);
		assert !((List<?>)_futureElements.get(configuration)).isEmpty();
		
	}
	
	// TODO could use some additional tests here which test other pieces of JavaServiceConfiguration
	
}
