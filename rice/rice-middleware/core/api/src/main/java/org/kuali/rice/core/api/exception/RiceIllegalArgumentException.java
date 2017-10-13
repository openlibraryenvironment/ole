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
 * Subclass of IllegalArgumentException that has been annotated properly using JAX-WS to be (un)marshalled as SOAP
 * fault.  This class should be used in place of IllegalArgumentException for all services to be exposed remotely
 * within Rice.
 *
 * <p>Note that even though this is a {@code RuntimeException}, in places where it is used it needs to be explicitly
 * declared in the {@code throws} declaration of the method.  This ensures that the JAX-WS annotation properly
 * marshals and unmarshals the exception to and from the appropriate SOAP fault.</p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
@WebFault(name = "IllegalArgumentFault", targetNamespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
public class RiceIllegalArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 3246611328810430837L;

    public RiceIllegalArgumentException() {
    }

    public RiceIllegalArgumentException(String s) {
        super(s);
    }

    public RiceIllegalArgumentException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public RiceIllegalArgumentException(Throwable throwable) {
        super(throwable);
    }
}
