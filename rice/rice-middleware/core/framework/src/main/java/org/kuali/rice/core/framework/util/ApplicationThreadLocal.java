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
package org.kuali.rice.core.framework.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * ThreadLocal subclass that is capable of clearing instances of itself registered with threads in the VM.
 */
public class ApplicationThreadLocal<T> extends ThreadLocal<T> {
    /**
     * A list of weak references to existing ApplicationThreadLocals.  These instances will be remove()d from all threads when
     * {@link #clear()} is called.
     */
    private static final Collection<WeakReference<ApplicationThreadLocal>> threadLocals = Collections.synchronizedCollection(new ArrayList<WeakReference<ApplicationThreadLocal>>());

    public ApplicationThreadLocal() {
        threadLocals.add(new WeakReference(this));
    }

    /**
     * Perform brittle hacks to unset ThreadLocal for a thread.
     * NOTE: this method is not threadsafe. the underlying implementation probably reasonably assumes ThreadLocalMap remove is only
     * going to be called from the owning thread. since our explicit purpose is to forcibly yank these thread locals from arbitrary
     * threads, we are breaking this assumption. we're doing the best we can.
     * @param t the thread
     */
    protected boolean remove(Thread t) {
        return removeThreadLocal(t, this);
    }

    /**
     * Utility method for removing a specific ThreadLocal from a Thread
     * @param thread the thread
     * @param threadLocal the threadlocal to remove()
     * @return true if successful false otherwise
     */
    public static boolean removeThreadLocal(Thread thread, ThreadLocal threadLocal) {
        try {
            // call package method getMap on self
            Method getMap = ThreadLocal.class.getDeclaredMethod("getMap", Thread.class);
            getMap.setAccessible(true);
            Object map = getMap.invoke(threadLocal, thread);
            // if ThreadLocalMap has been set
            if (map != null) {
                // call private method remove on ThreadLocalMap
                Method remove = map.getClass().getDeclaredMethod("remove", ThreadLocal.class);
                remove.setAccessible(true);
                remove.invoke(map, threadLocal);
            }
            return true;
            // we don't really have any recourse here, so just print the stack trace
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        }
        return false;
    }

    /**
     * Remove all ApplicationThreadLocal instances from threads.
     */
    public static boolean clear() {
        // the default mutex for a SynchronizedCollection is self.
        Collection<WeakReference<ApplicationThreadLocal>> toRemove;
        synchronized (threadLocals) {
            toRemove = new ArrayList<WeakReference<ApplicationThreadLocal>>(threadLocals);
            // here we eagerly clear our list irrespective of the success of clearing individual ATLs (assuming there is really nothing we can do about it anyway)
            threadLocals.clear();
        }
        boolean success = true;
        Set<Thread> allThreads = Thread.getAllStackTraces().keySet();
        for (WeakReference<ApplicationThreadLocal> ref: toRemove) {
            ApplicationThreadLocal tl = ref.get();
            // maybe we're lucky and it was garbage-collectible and already collected
            if (tl != null) {
                // otherwise remove it from all Threads
                for (Thread t: allThreads) {
                   success &= tl.remove(t);
                }
            }
        }
        return success;
    }
}