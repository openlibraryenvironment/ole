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
package org.kuali.rice.ksb.testclient2;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.kuali.rice.ksb.messaging.remotedservices.EchoService;
import org.kuali.rice.ksb.messaging.remotedservices.ServiceCallInformationHolder;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

public class EchoService2Impl implements EchoService {
	public String echo(String string) {
		return string;
	}

	public String trueEcho(String string) {
		return string;
	}

    public void captureHeaders() {
        ServiceCallInformationHolder.stuff.put("capturedHeaders", PhaseInterceptorChain.getCurrentMessage().get(Message.PROTOCOL_HEADERS));
    }

}
