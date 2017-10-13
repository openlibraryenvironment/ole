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
package org.kuali.rice.ksb.config.xfire;

/*
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.security.wss4j.WSS4JOutHandler;
*/


/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

//TODO: Replace this class with a cxf wss4j out interceptor
public class XFireWSS4JOutHandler {}

/*
public class XFireWSS4JOutHandler extends WSS4JOutHandler {

	private static final Logger LOG = Logger.getLogger(XFireWSS4JOutHandler.class);

	private ServiceInfo serviceInfo;

	public XFireWSS4JOutHandler(ServiceInfo serviceInfo) {
		this.setProperty(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
		this.setProperty(WSHandlerConstants.PW_CALLBACK_CLASS, CryptoPasswordCallbackHandler.class.getName());
		this.setProperty(WSHandlerConstants.SIG_KEY_ID, "IssuerSerial");
		this.setProperty(WSHandlerConstants.USER, ConfigContext.getCurrentContextConfig().getKeystoreAlias());
		this.serviceInfo = serviceInfo;
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

	@Override
	public void invoke(MessageContext context) throws Exception {
		if (getServiceInfo().getServiceDefinition().getBusSecurity()) {
			super.invoke(context);
		}
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

}
*/
