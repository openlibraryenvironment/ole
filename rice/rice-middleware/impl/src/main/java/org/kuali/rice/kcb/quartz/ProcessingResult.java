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
package org.kuali.rice.kcb.quartz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Encapsulates the number of successes and failures in a giving processing run
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ProcessingResult<T> {
    /**
     * Represents a failure of a work item
     */
    public static class Failure<T> {
        private T item;
        private Throwable exception;
        private String message;
        
        public Failure(T item) {
            this.item = item;
        }
        
        public Failure(String message) {
            this(null, null, message);
        }

        public Failure(Throwable exception) {
            this(null, exception, null);
        }

        public Failure(Throwable exception, String message) {
            this(null, exception, message);
        }


        public Failure(T item, Throwable exception) {
            this(item, exception, null);
        }
        
        public Failure(T item, String message) {
            this(item, null, message);
        }
        
        public Failure(T item, Throwable exception, String message) {
            this.item = item;
            this.exception = exception;
            this.message = message;
        }

        public T getItem() {
            return this.item;
        }
        public Throwable getException() {
            return this.exception;
        }
        public String getMessage() {
            return this.message;
        }
    }

    /**
     * List of failures
     */
    private List<Failure<T>> failures = new ArrayList<Failure<T>>();
    /**
     * List of successes
     */
    private List<T> successes = new ArrayList<T>();

    /**
     * Returns the list of failures
     * @return the list of failures
     */
    public List<Failure<T>> getFailures() {
        return failures;
    }

    /**
     * Returns the list of successes
     * @return the list of successes
     */
    public List<T> getSuccesses() {
        return successes;
    }

    /**
     * Adds a failure
     * @param o an object representing a failure
     */
    public void addFailure(Failure<T> o) {
        failures.add(o);
    }

    /**
     * Adds a collection of failures
     * @param Collection a collection of failures
     */
    public void addAllFailures(Collection<Failure<T>> c) {
        failures.addAll(c);
    }

    /**
     * Adds a success
     * @param o an object representing a success
     */
    public void addSuccess(T o) {
        successes.add(o);
    }
    
    /**
     * Adds a collectin of successes
     * @param Collection a collection of successes
     */
    public void addAllSuccesses(Collection<T> c) {
        successes.addAll(c);
    }
    
    /**
     * Adds the contents of the specified ProcessingResult to this ProcessingResult
     * @param result the result to append to this result
     */
    public void add(ProcessingResult<T> result) {
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
