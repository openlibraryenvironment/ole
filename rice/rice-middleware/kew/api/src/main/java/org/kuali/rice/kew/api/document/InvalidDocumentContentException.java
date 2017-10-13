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
package org.kuali.rice.kew.api.document;

import javax.xml.ws.WebFault;

import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;

@WebFault(name = "InvalidDocumentContentFault", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
public class InvalidDocumentContentException extends WorkflowRuntimeException {
    
    private static final long serialVersionUID = -7192699210643743641L;

    public InvalidDocumentContentException() {
        super();
    }
    
    public InvalidDocumentContentException(String message) {
        super(message);
    }

    public InvalidDocumentContentException(Throwable t) {
        super(t);
    }

    public InvalidDocumentContentException(String message, Throwable t) {
        super(message, t);
    }
}
