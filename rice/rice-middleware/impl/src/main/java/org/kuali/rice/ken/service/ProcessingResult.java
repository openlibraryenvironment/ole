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
package org.kuali.rice.ken.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates the number of successes and failures in a giving processing run
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ProcessingResult {
    /**
     * List of failures
     */
    private List<Object> failures = new ArrayList<Object>();
    /**
     * List of successes
     */
    private List<Object> successes = new ArrayList<Object>();

    /**
     * Returns the list of failures
     * @return the list of failures
     */
    public List<?> getFailures() {
        return failures;
    }

    /**
     * Returns the list of successes
     * @return the list of successes
     */
    public List<?> getSuccesses() {
        return successes;
    }

    /**
     * Adds a failure
     * @param o an object representing a failure
     */
    public void addFailure(Object o) {
        failures.add(o);
    }

    /**
     * Adds a collection of failures
     * @param Collection a collection of failures
     */
    public void addAllFailures(Collection c) {
        failures.addAll(c);
    }

    /**
     * Adds a success
     * @param o an object representing a success
     */
    public void addSuccess(Object o) {
        successes.add(o);
    }
    
    /**
     * Adds a collectin of successes
     * @param Collection a collection of successes
     */
    public void addAllSuccesses(Collection c) {
        successes.addAll(c);
    }
    
    /**
     * Adds the contents of the specified ProcessingResult to this ProcessingResult
     * @param result the result to append to this result
     */
    public void add(ProcessingResult result) {
        failures.addAll(result.getFailures());
        successes.addAll(result.getSuccesses());
    }
    
    /**
     * Returns a string representation of this ProcessingResults object
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "[ProcessingResults: successes(" + successes.size() + ")=" + successes +
                                 ", failures(" + failures.size() + ")=" + failures + "]";
    }
}
