/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.plugin;

import org.junit.Test;
import org.kuali.rice.core.api.util.ContextClassLoaderBinder;

import java.net.URL;
import java.net.URLClassLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests the ContextClassLoaderBinder
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ContextClassLoaderBinderTest {

    /**
     * @deprecated {@link ContextClassLoaderBinder#doInContextClassLoader(ClassLoader, java.util.concurrent.Callable)} is the safe way to
     * run code under a specific Context ClassLoader that ensures the classloader is always set and unset properly.
     */
	@Test public void testBinding() {
        try {
            ContextClassLoaderBinder.unbind();
            fail("unbind succeeded without any prior bind");
        } catch (IllegalStateException ise) {
            // expect illegal state
        }

        ClassLoader cl0 = new URLClassLoader(new URL[] {});
        ClassLoader cl1 = new URLClassLoader(new URL[] {});
        ClassLoader cl2 = new URLClassLoader(new URL[] {});

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        ContextClassLoaderBinder.bind(cl0);
        assertEquals(cl0, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(original, Thread.currentThread().getContextClassLoader());

        ContextClassLoaderBinder.bind(cl0);
        assertEquals(cl0, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.bind(cl1);
        assertEquals(cl1, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(cl0, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(original, Thread.currentThread().getContextClassLoader());

        ContextClassLoaderBinder.bind(cl0);
        assertEquals(cl0, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.bind(cl1);
        assertEquals(cl1, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.bind(cl2);
        assertEquals(cl2, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(cl1, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(cl0, Thread.currentThread().getContextClassLoader());
        ContextClassLoaderBinder.unbind();
        assertEquals(original, Thread.currentThread().getContextClassLoader());
    }
}
