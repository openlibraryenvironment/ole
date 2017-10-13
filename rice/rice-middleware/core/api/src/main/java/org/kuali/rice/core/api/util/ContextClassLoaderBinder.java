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
package org.kuali.rice.core.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Utility class that binds/unbinds the context ClassLoader of the current Thread
 * using a ThreadLocal.
 * NOTE: {@link #doInContextClassLoader(ClassLoader, java.util.concurrent.Callable)} is the only safe way to use this class,
 * do not use deprecated {@link #bind(ClassLoader)} or {@link #unbind()} methods.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ContextClassLoaderBinder {
	
	/**
     * Stack of previous context classloaders that should be
     * restored on unbind
     * @deprecated since 2.1, use #doInContextClassLoader. storing references to classloaders is an incredibly bad idea!
     */
    private static final ThreadLocal<List<ClassLoader>> STACK = new ThreadLocal<List<ClassLoader>>() {
        protected List<ClassLoader> initialValue() {
            return new ArrayList<ClassLoader>(5);
        }
    };

    /**
     * @deprecated use #doInContextClassLoader
     */
    private static List<ClassLoader> getStack() {
        return STACK.get();
    }

    /**
     * @deprecated use #doInContextClassLoader
     */
    public static void bind(ClassLoader cl) {
        List<ClassLoader> stack = getStack();
        Thread current = Thread.currentThread();
        //log.debug("[bind] Switching CCL from " + current.getContextClassLoader() + " to " + cl);
        // push the current context classloader on the stack
        stack.add(current.getContextClassLoader());
        current.setContextClassLoader(cl);
    }

    /**
     * @deprecated use #doInContextClassLoader
     */
    public static void unbind() {
        List<ClassLoader> stack = getStack();
        if (stack.size() == 0) {
            throw new IllegalStateException("No context classloader to unbind!");
        }
        // pop the last context classloader off the stack
        ClassLoader lastClassLoader = stack.get(stack.size() - 1);
        //log.debug("[unbind] Switching CCL from " + Thread.currentThread().getContextClassLoader() + " to " + lastClassLoader);
        stack.remove(stack.size() - 1);
        Thread.currentThread().setContextClassLoader(lastClassLoader);
    }

    /**
     * Execute a runnable under a given context classloader
     * @param cl the classloader to set as the thread context classloader
     * @param callable the callable
     */
    public static <T> T doInContextClassLoader(ClassLoader cl, Callable<T> callable) throws Exception {
        Thread current = Thread.currentThread();
        ClassLoader prev = current.getContextClassLoader();
        try {
            current.setContextClassLoader(cl);
            return callable.call();
        } finally {
            current.setContextClassLoader(prev);
        }
    }
}