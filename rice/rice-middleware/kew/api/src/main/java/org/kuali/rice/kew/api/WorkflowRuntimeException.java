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
package org.kuali.rice.kew.api;

import javax.xml.ws.WebFault;

/**
 * A generic runtime exception thrown from KEW.  Acts as the superclass for all runtime
 * exceptions in KEW.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebFault(name = "WorkflowRuntimeFault", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
public class WorkflowRuntimeException extends RuntimeException {
    
    private static final long serialVersionUID = 2012770642382150523L;
    
    public WorkflowRuntimeException(String message) {
        super(message);
    }

    public WorkflowRuntimeException() {
        super();
    }

    public WorkflowRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowRuntimeException(Throwable cause) {
        super(cause);
    }
}
