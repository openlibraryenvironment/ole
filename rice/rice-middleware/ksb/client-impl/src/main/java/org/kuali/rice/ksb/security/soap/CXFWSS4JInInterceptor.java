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
package org.kuali.rice.ksb.security.soap;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.ksb.config.wss4j.CryptoPasswordCallbackHandler;

import java.util.Properties;

//import javax.xml.ws.handler.MessageContext;


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

//TODO: Replace this class with cxf wss4j in interceptor
public class CXFWSS4JInInterceptor extends WSS4JInInterceptor{

	private static final Logger LOG = Logger.getLogger(CXFWSS4JInInterceptor.class);

	private final boolean busSecurity;
	
	public CXFWSS4JInInterceptor(boolean busSecurity) {
		this.busSecurity = busSecurity;
        if (busSecurity) {
		    this.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
		    this.setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, CryptoPasswordCallbackHandler.class.getName());
		    this.setProperty(WSHandlerConstants.SIG_KEY_ID, "IssuerSerial");
		    this.setProperty(WSHandlerConstants.USER, ConfigContext.getCurrentContextConfig().getKeystoreAlias());
        }
	}

	@Override
	public Crypto loadSignatureCrypto(RequestData reqData) {
		try {
			return new Merlin(getMerlinProperties(), ClassLoaderUtils.getDefaultClassLoader());
		} catch (Exception e) {
			throw new RiceRuntimeException(e);
		}
	}

	@Override
	public Crypto loadDecryptionCrypto(RequestData reqData) {
		return loadSignatureCrypto(reqData);
	}

	protected Properties getMerlinProperties() {
		Properties props = new Properties();
		props.put("org.apache.ws.security.crypto.merlin.keystore.type", "jks");
		props.put("org.apache.ws.security.crypto.merlin.keystore.password", ConfigContext.getCurrentContextConfig().getKeystorePassword());
		props.put("org.apache.ws.security.crypto.merlin.alias.password", ConfigContext.getCurrentContextConfig().getKeystorePassword());
		props.put("org.apache.ws.security.crypto.merlin.keystore.alias", ConfigContext.getCurrentContextConfig().getKeystoreAlias());
		props.put("org.apache.ws.security.crypto.merlin.file", ConfigContext.getCurrentContextConfig().getKeystoreFile());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Using keystore location " + ConfigContext.getCurrentContextConfig().getKeystoreFile());
		}
		return props;
	}

	/**
	 * This overridden method will not apply security headers if bus security is disabled.
	 * 
	 * @see org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor#handleMessage(org.apache.cxf.binding.soap.SoapMessage)
	 */
	@Override
	public void handleMessage(SoapMessage mc)  {
		if (busSecurity) {
			super.handleMessage(mc);
		}
	}

}
