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

/**
 * This is a default exception class that others inherit. When an exception is thrown,
 * the exception handler looks for an instance of this class and processes as Kuali
 * exception. Otherwise, generic system exception is processed. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public abstract class KualiException extends RuntimeException {

    private static final long serialVersionUID = 2395877766757834813L;
    private boolean hideIncidentReport = false;
    
    /**
     * This constructs an instance of java.lang.RuntimeException
     * 
     * @param message
     */
    public KualiException(String message) {
        super(message);
    }
    
    /**
     * This constructs an instance of java.lang.RuntimeException
     * 
     * @param message
     * @param hideIncidentReport
     */
    public KualiException(String message, boolean hideIncidentReport){
    	super(message);
    	this.hideIncidentReport = hideIncidentReport;
    }

    /**
     * This constructs an instance of java.lang.RuntimeException
     * 
     * @param message
     * @param t
     */
    public KualiException(String message, Throwable t) {
        super(message, t);
    }
    
    /**
     * This constructs an instance of java.lang.RuntimeException
     * 
     * @param t
     */
    public KualiException(Throwable t) {
        super(t);
    }

	/**
	 * @param hideIncidentReport the hideIncidentReport to set
	 */
	public void setHideIncidentReport(boolean hideIncidentReport) {
		this.hideIncidentReport = hideIncidentReport;
	}

	/**
	 * @return the hideIncidentReport
	 */
	public boolean isHideIncidentReport() {
		return this.hideIncidentReport;
	}
	
	
    
    
}
