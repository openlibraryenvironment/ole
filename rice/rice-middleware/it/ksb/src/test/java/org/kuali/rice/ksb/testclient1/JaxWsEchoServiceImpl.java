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
package org.kuali.rice.ksb.testclient1;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.kuali.rice.ksb.messaging.remotedservices.JaxWsEchoService;
import org.kuali.rice.ksb.messaging.remotedservices.ServiceCallInformationHolder;

import java.util.List;
import java.util.Map;

/**
 * This is the JaxWsEchoService implementation 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(endpointInterface="org.kuali.rice.ksb.messaging.remotedservices.JaxWsEchoService",
		serviceName="jaxwsEchoService",
		portName="jaxWsEchoService",
		targetNamespace="http://rice.kuali.org/")
public class JaxWsEchoServiceImpl implements JaxWsEchoService {
    @Resource WebServiceContext requestContext;

	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.ksb.messaging.remotedservices.JaxWsEchoService#doEcho(java.lang.String)
	 */
	public String doEcho(String inMsg) {
		// TODO Will Gomes - THIS METHOD NEEDS JAVADOCS
		return inMsg;
	}


    public void captureHeaders() {
        MessageContext mc = (MessageContext) requestContext.getMessageContext();
        ServiceCallInformationHolder.stuff.put("capturedHeaders", mc.get(MessageContext.HTTP_REQUEST_HEADERS));
    }
}