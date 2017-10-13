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
package org.kuali.rice.kcb.exception;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class is a value added datastructure that is used to house a list of Exceptions and is 
 * recognized as an Exception so that it can be thrown from methods and handled like an Exception.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ErrorList extends Exception {
    private static final long serialVersionUID = -8045847343472018601L;
    
    private List<String> errorList;
    
    /**
     * Constructs a ErrorList instance.
     */
    public ErrorList() {
    	errorList = new ArrayList<String>();
    }
    
    /**
     * This method checks to see if the list is empty or not.
     * @return boolean
     */
    public boolean isEmpty() {
    	return errorList.isEmpty();
    }
    
    /**
     * This method adds errors to the error list.
     * @param error
     */
    public void addError(String error) {
    	this.errorList.add(error);
    }
    
    /**
     * This method retreives all of the errors in the list.
     * @return List
     */
    public List<String> getErrors() {
    	return this.errorList;
    }
    
    /**
     * This method adds a list of errors to the error list.
     * @param errors
     */
    public void addErrors(ErrorList errors) {
    	this.errorList.addAll(errors.errorList);
    }
    
    /**
     * This method returns a string representation of all of the errors in the error list.
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
    	return toString();
    }
    
    /**
     * This method is responsible for concatenating all of the errors in the error list together.
     * @see java.lang.Throwable#toString()
     */
    public String toString() {
    	StringBuffer buf = new StringBuffer("errors=(");
    
    	for (Iterator<String> i = errorList.iterator(); i.hasNext();) {
    		buf.append(i.next());
    		if (i.hasNext()) {
    			buf.append(";");
    		}
    	}
    	buf.append(")");
    
    	return buf.toString();
    }
}
