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
package org.kuali.rice.core.framework.persistence.ojb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.ojb.broker.OptimisticLockException;
import org.springframework.dao.OptimisticLockingFailureException;

/**
 * Provides some simple utilities for working with data access exceptions.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class DataAccessUtils {

    private static final Set<Class<? extends Throwable>> OPTIMISTIC_LOCK_EXCEPTION_CLASSES = new HashSet<Class<? extends Throwable>>();
    
        private DataAccessUtils() {
                throw new UnsupportedOperationException("do not call");
        }

    // add some standard optimistic lock exception classes
    static {
        addOptimisticLockExceptionClass(OptimisticLockException.class);
        addOptimisticLockExceptionClass(OptimisticLockingFailureException.class);
    }

    public static synchronized boolean isOptimisticLockFailure(Throwable exception) {
        if (exception == null) {
            return false;
        }
        for (final Class<?> exceptionClass : getOptimisticLockExceptionClasses()) {
            if (ExceptionUtils.indexOfType(exception, exceptionClass) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static synchronized void addOptimisticLockExceptionClass(Class<? extends Throwable> exceptionClass) {
        OPTIMISTIC_LOCK_EXCEPTION_CLASSES.add(exceptionClass);
    }

    public static synchronized Set<Class<? extends Throwable>> getOptimisticLockExceptionClasses() {
        return Collections.unmodifiableSet(OPTIMISTIC_LOCK_EXCEPTION_CLASSES);
    }

}