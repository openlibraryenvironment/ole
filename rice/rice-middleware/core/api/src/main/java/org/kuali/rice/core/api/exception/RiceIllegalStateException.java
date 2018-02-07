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
package org.kuali.rice.core.api.exception;

import org.kuali.rice.core.api.CoreConstants;

import javax.xml.ws.WebFault;

/**
 * Subclass of IllegalStateException that has been annotated properly to be (un)marshalled as SOAP fault.
 * This class should be used in place of IllegalStateException for all services to be exposed remotely
 * within Rice.
 */
@WebFault(name = "IllegalStateFault", targetNamespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
@SuppressWarnings("unused")
public class RiceIllegalStateException extends IllegalStateException {
    public RiceIllegalStateException() {
    }

    public RiceIllegalStateException(String s) {
        super(s);
    }

    public RiceIllegalStateException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RiceIllegalStateException(Throwable throwable) {
        super(throwable);
    }
}
