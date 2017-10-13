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
package org.kuali.rice.kew.exception;

import java.util.ArrayList;
import java.util.Collection;

/**
 * RuntimeException thrown from Service level classes when business rule validation
 * fails.  This exception is caught by StrutsExceptionHandler.  If any service errors
 * have been set on in the serviceErrors collection these are stripped off of the 
 * exception put into ActionMessages in the Error que and the request is directed back to 
 * the original ActionMapping input page.
 */
public class WorkflowServiceErrorException extends RuntimeException {


	private static final long serialVersionUID = 2457592489303923040L;
	private Collection serviceErrors;
    
    public WorkflowServiceErrorException(String message) {
        this(message, (Throwable)null);
    }
    
    public WorkflowServiceErrorException(String message, Throwable throwable) {
        super(message, throwable);
        serviceErrors = new ArrayList();
    }
    
    public WorkflowServiceErrorException(String msg, WorkflowServiceError error) {
        super(msg);
        serviceErrors = new ArrayList();
        serviceErrors.add(error);
    }

        public WorkflowServiceErrorException(String msg, Throwable t, WorkflowServiceError error) {
        super(msg, t);
        serviceErrors = new ArrayList();
        serviceErrors.add(error);
    }
    
    
    public WorkflowServiceErrorException(String msg, Collection errors) {
        super(msg);
        setServiceErrors(errors);
    }
    
    public Collection getServiceErrors() {
        return serviceErrors;
    }

    public void setServiceErrors(Collection serviceErrors) {
        this.serviceErrors = serviceErrors;
    }

    public String toString() {
        if (serviceErrors != null) {
            return super.toString() + " " + serviceErrors;    
        } else {
            return super.toString() + " (no service errors)";
        }
    }
}
