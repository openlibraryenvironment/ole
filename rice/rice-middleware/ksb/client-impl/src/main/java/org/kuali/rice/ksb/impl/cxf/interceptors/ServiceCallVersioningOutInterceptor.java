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
package org.kuali.rice.ksb.impl.cxf.interceptors;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.List;
import java.util.Map;

/**
 * A CXF Interceptor that binds itself to the USER_PROTOCOL phase to be used on outbound
 * messages.   The role of this interceptor is to populate outgoing protocol headers
 * (for all intents and purposes, HTTP headers) with Kuali Rice and application version
 * information.
 * @see <a href="http://cxf.apache.org/docs/interceptors.html">CXF interceptors</a>
 */
public class ServiceCallVersioningOutInterceptor extends AbstractPhaseInterceptor<Message> {
    /**
     * Instantiates an ServiceCallVersioningOutInterceptor and adds it to the USER_PROTOCOL phase.
     */
    public ServiceCallVersioningOutInterceptor() {
        super(Phase.USER_PROTOCOL);
    }

    /**
     * Publishes the Kuali Rice Environment, Rice Version, Application Name and Application Version
     * in outbound protocol headers
     */
    @Override
    public void handleMessage(final Message message) throws Fault {
        Map<String, List<String>> headers = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
        if (headers != null) {
            ServiceCallVersioningHelper.populateVersionHeaders(headers);
        }
    }
}
