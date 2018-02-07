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
import org.kuali.rice.core.api.util.collect.CollectionUtils;

import java.util.List;

/**
 * A CXF Interceptor that binds itself to the USER_LOGICAL phase to be used on inbound
 * messages.  This interceptor is invoked in the interceptor chain after unmarshalling
 * from XML to Java has occurred.  The role of this interceptor is to ensure that any
 * Collection (and specifically List, Set, or Map) used in a @WebMethod is ultimately of the
 * expected immutable type returned from the local service.
 */
@SuppressWarnings("unused")
public class ImmutableCollectionsInInterceptor extends AbstractPhaseInterceptor<Message> {

    /**
     * Instantiates an ImmutableCollectionsInInterceptor and adds it to the USER_LOGICAL phase.
     */
    public ImmutableCollectionsInInterceptor() {
        super(Phase.USER_LOGICAL);
    }

    @Override
    public void handleMessage(final Message message) throws Fault {
        try {
            List contents = message.getContent(List.class);
            if (contents != null) {
                for (Object o : contents) {
                    CollectionUtils.makeUnmodifiableAndNullSafe(o);
                }
            }
        } catch (IllegalAccessException e) {
            throw new Fault(e);
        }
    }
}
