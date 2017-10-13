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
package org.kuali.rice.ksb.messaging.remotedservices;

import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.core.api.util.jaxb.MultiValuedStringMapAdapter;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;
import java.util.Map;

/**
 * This is a jaxws annotated web service, used for testing web services on the ksb. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@WebService(targetNamespace = "http://rice.kuali.org/", name = "JaxWsEchoService")
public interface JaxWsEchoService {

    @WebResult(name = "outMsg")
    @RequestWrapper(localName = "Echo")
    @ResponseWrapper(localName = "EchoResponse")
    @WebMethod
    public String doEcho(
        @WebParam(name = "inMsg") String inMsg
    );

    @WebResult(name = "headers")
    @RequestWrapper(localName = "EchoHeaders")
    @ResponseWrapper(localName = "EchoHeaderResponse")
    @WebMethod
    public void captureHeaders();
}
