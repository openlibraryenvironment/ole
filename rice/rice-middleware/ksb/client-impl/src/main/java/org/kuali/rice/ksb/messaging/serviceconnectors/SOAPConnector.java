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
package org.kuali.rice.ksb.messaging.serviceconnectors;

import org.apache.cxf.aegis.databinding.AegisDatabinding;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.ksb.api.bus.support.SoapServiceConfiguration;
import org.kuali.rice.ksb.impl.cxf.interceptors.ImmutableCollectionsInInterceptor;
import org.kuali.rice.ksb.messaging.servicehandlers.BasicAuthenticationPasswordHandler;
import org.kuali.rice.ksb.security.soap.CXFWSS4JInInterceptor;
import org.kuali.rice.ksb.security.soap.CXFWSS4JOutInterceptor;
import org.kuali.rice.ksb.security.soap.CredentialsOutHandler;
import org.kuali.rice.ksb.service.BasicAuthenticationCredentials;
import org.kuali.rice.ksb.service.KSBServiceLocator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 */
public class SOAPConnector extends AbstractServiceConnector {

    private static final Logger LOG = Logger.getLogger(SOAPConnector.class);

	public SOAPConnector(final SoapServiceConfiguration serviceConfiguration, final URL alternateEndpointUrl) {
		super(serviceConfiguration, alternateEndpointUrl);
	}

    @Override
	public SoapServiceConfiguration getServiceConfiguration() {
		return (SoapServiceConfiguration) super.getServiceConfiguration();
	}
	
	/**
	 * This overridden method returns a CXF client praoxy for web service.
	 * 
	 * @see org.kuali.rice.ksb.messaging.serviceconnectors.ServiceConnector#getService()
	 */
	public Object getService() {
		ClientProxyFactoryBean clientFactory;
		
		//Use the correct bean factory depending on pojo service or jaxws service
		if (getServiceConfiguration().isJaxWsService()){
			clientFactory = new JaxWsProxyFactoryBean();
		} else {
			clientFactory = new ClientProxyFactoryBean();
			clientFactory.getServiceFactory().setDataBinding(new AegisDatabinding());
		}		

		try {
			clientFactory.setServiceClass(Class.forName(getServiceConfiguration().getServiceInterface()));
		} catch (ClassNotFoundException e) {
			throw new RiceRuntimeException("Failed to connect to soap service " + getServiceConfiguration().getServiceName() + " because failed to load interface class: " + getServiceConfiguration().getServiceInterface(), e);
		}
		clientFactory.setBus(KSBServiceLocator.getCXFBus());
		clientFactory.setServiceName(getServiceConfiguration().getServiceName());
		clientFactory.setAddress(getActualEndpointUrl().toExternalForm());
		
		//Set logging, transformation, and security interceptors
        clientFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        clientFactory.getOutFaultInterceptors().add(new LoggingOutInterceptor());
        CXFWSS4JOutInterceptor outSecurityInterceptor = new CXFWSS4JOutInterceptor(getServiceConfiguration().getBusSecurity());
        clientFactory.getOutInterceptors().add(outSecurityInterceptor);
        clientFactory.getOutFaultInterceptors().add(outSecurityInterceptor);
        if (getCredentialsSource() != null) {
			clientFactory.getOutInterceptors().add(new CredentialsOutHandler(getCredentialsSource(), getServiceConfiguration()));
		}

		/**
		 * Check the basic authentication service for credentials when connecting to a service which requires basic
		 * authentication.  If credentials are found in the service then this populates the WSSecurity header with
		 * the correct information.
		 */
		SoapServiceConfiguration soapServiceConfiguration = this.getServiceConfiguration();

		if (soapServiceConfiguration.isBasicAuthentication()) {
			BasicAuthenticationCredentials credentials =
					KSBServiceLocator.getBasicAuthenticationService().getConnectionCredentials(
							soapServiceConfiguration.getServiceName().getNamespaceURI(),
							soapServiceConfiguration.getServiceName().getLocalPart());
			if (credentials != null) {
				Map<String, Object> outProps = new HashMap<String, Object>();
				outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
				outProps.put(WSHandlerConstants.USER, credentials.getUsername());
				outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
				outProps.put(WSHandlerConstants.PW_CALLBACK_REF, new BasicAuthenticationPasswordHandler(
						credentials.getPassword()));
				WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
				clientFactory.getOutInterceptors().add(wssOut);
				clientFactory.getOutFaultInterceptors().add(wssOut);
			}
		}

	    clientFactory.getInInterceptors().add(new LoggingInInterceptor());
        clientFactory.getInFaultInterceptors().add(new LoggingInInterceptor());
        CXFWSS4JInInterceptor inSecurityInterceptor = new CXFWSS4JInInterceptor(getServiceConfiguration().getBusSecurity());
        clientFactory.getInInterceptors().add(inSecurityInterceptor);
        clientFactory.getInFaultInterceptors().add(inSecurityInterceptor);
        clientFactory.getInInterceptors().add(new ImmutableCollectionsInInterceptor());

		
		Object service = clientFactory.create();		
		return getServiceProxyWithFailureMode(service, getServiceConfiguration());
	}	
}
