/**
 * Copyright 2005-2011 The Kuali Foundation
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
package org.kuali.rice.core.test.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import org.apache.cxf.common.util.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.cache.CacheTarget;
import org.kuali.rice.core.impl.cache.DistributedCacheManagerDecorator;
import org.kuali.rice.core.test.CORETestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import static org.junit.Assert.assertTrue;


/**
 * Unit tests for the DistributedCacheManagerDecorator
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DistributedCacheManagerDecoratorTest extends CORETestCase {

    private static final String ROLE_RESPONSIBILITY_CACHE = "http://rice.kuali.org/kim/v2_0/RoleResponsibilityType";
    private static final String ROLE_TYPE_CACHE = "http://rice.kuali.org/kim/v2_0/RoleType";
    private static final String DELEGATE_TYPE_CACHE = "http://rice.kuali.org/kim/v2_0/DelegateTypeType";
    private static final String ROLE_MEMBER_TYPE= "http://rice.kuali.org/kim/v2_0/RoleMemberType";
    private static final String PERMISSION_TYPE = "http://rice.kuali.org/kim/v2_0/PermissionType";
    private static final String INNER_CLASS = "CacheMessageSendingTransactionSynchronization";

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test that duplicate cache flushes are filtered by
     * DistributedCacheManagerDecorator.CacheMessageSendingTransactionSynchronization.exhaustQueue(...)
     * to just the unique set, and that individual cache entry flushes  are filtered if the containing caches are also
     * being flushed in their entirety.
     */
    @Test
    public void testDuplicateCacheRemovalCase1() {

        // duplicate caches, we expect these to be filtered to the unique set
        Queue<CacheTarget> targets = Queues.newLinkedBlockingQueue(); 
        targets.add(CacheTarget.entireCache(ROLE_RESPONSIBILITY_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_RESPONSIBILITY_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_MEMBER_TYPE));
        targets.add(CacheTarget.entireCache(ROLE_MEMBER_TYPE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));

        // specific cache entries by key.  We expect these all to be filtered out because the entire caches
        // are being flushed based on the targets added above.
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"));
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key2"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key3"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key4"));

        // the expected result is the unique set of caches from targets
        ArrayList<CacheTarget> correctResults = Lists.newArrayList(
                CacheTarget.entireCache(ROLE_RESPONSIBILITY_CACHE),
                CacheTarget.entireCache(ROLE_MEMBER_TYPE),
                CacheTarget.entireCache(ROLE_TYPE_CACHE),
                CacheTarget.entireCache(DELEGATE_TYPE_CACHE),
                CacheTarget.entireCache(PERMISSION_TYPE));

        Collection<CacheTarget> results = new ArrayList<CacheTarget>(invokeExhaustQueue(targets));
        assertTrue(CollectionUtils.diff(correctResults, results).isEmpty());
    }

    /**
     * Test that duplicate cache flushes are filtered by
     * DistributedCacheManagerDecorator.CacheMessageSendingTransactionSynchronization.exhaustQueue(...)
     * to just the unique set, and that duplicate cache entry flushes are filtered to just the unique set as well.
     */
    @Test
    public void testDuplicateCacheRemovalCase2() {
        Queue<CacheTarget> targets = Queues.newLinkedBlockingQueue();

        // duplicate caches, we expect these to be filtered to the unique set
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));

        // cache entries -- we expect no filtering, since (1) the caches these entries are in are not being
        // flushed in their entirety, and (2) the cache + key combinations are unique
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"));
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key2"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key3"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key4"));

        // the expected result is the unique set of caches, and each of the specified cache entries
        ArrayList<CacheTarget> correctResults = Lists.newArrayList(
                CacheTarget.entireCache(ROLE_TYPE_CACHE),
                CacheTarget.entireCache(DELEGATE_TYPE_CACHE),
                CacheTarget.entireCache(PERMISSION_TYPE),
                CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"),
                CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key2"),
                CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key3"),
                CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key4"));

        Collection<CacheTarget> results = new ArrayList<CacheTarget>(invokeExhaustQueue(targets));
        assertTrue(CollectionUtils.diff(correctResults, results).isEmpty());
    }

    /**
     * Test that duplicate cache flushes are filtered by
     * DistributedCacheManagerDecorator.CacheMessageSendingTransactionSynchronization.exhaustQueue(...)
     * to just the unique set, and that duplicate cache entry flushes are filtered to just the unique set as well.
     */
    @Test
    public void testDuplicateCacheRemovalCase3() {
        Queue<CacheTarget> targets = Queues.newLinkedBlockingQueue();

        // duplicate caches, we expect these to be filtered to the unique set
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(ROLE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(DELEGATE_TYPE_CACHE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));
        targets.add(CacheTarget.entireCache(PERMISSION_TYPE));

        // duplicate cache entries, we expect these to be filtered down to the unique set.
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"));
        targets.add(CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key2"));
        targets.add(CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key2"));

        // the expected result is the unique set of caches, and the unique set of specified cache entries
        ArrayList<CacheTarget> correctResults = Lists.newArrayList(
                CacheTarget.entireCache(ROLE_TYPE_CACHE), 
                CacheTarget.entireCache(DELEGATE_TYPE_CACHE),
                CacheTarget.entireCache(PERMISSION_TYPE), 
                CacheTarget.singleEntry(ROLE_MEMBER_TYPE, "key1"),
                CacheTarget.singleEntry(ROLE_RESPONSIBILITY_CACHE, "key2"));

        Collection<CacheTarget> results = new ArrayList<CacheTarget>(invokeExhaustQueue(targets));
        assertTrue(CollectionUtils.diff(correctResults, results).isEmpty());
    }

    /*
     * Invoking the DistributedCacheManagerDecorator via reflection since the exhaustQueue method is a private method
     * in a private inner class.
     */
    protected Collection<CacheTarget> invokeExhaustQueue(Queue<CacheTarget> targets) {
        Collection<CacheTarget> results = null;

        Class<?> c = DistributedCacheManagerDecorator.class;
        Class<?>[] classes = c.getDeclaredClasses();
        Class<?> correctInnerClass = null;

        //Trying to find the correct inner class  
        for (Class<?> clazz : classes) {
            if (clazz.getName().endsWith(INNER_CLASS)) {
                correctInnerClass = clazz;
                break;
            }
        }

        try {
            // KULRICE-9622 - get the correct constructor in JDK7 by asking for it specifically
            Constructor<?> constructor = correctInnerClass.getDeclaredConstructor(DistributedCacheManagerDecorator.class);
            constructor.setAccessible(true);

            Object inner = constructor.newInstance(c.newInstance());
            Method method = inner.getClass().getDeclaredMethod("exhaustQueue", new Class[] {Queue.class});
            method.setAccessible(true);

            results = (Collection<CacheTarget>) method.invoke(inner, new Object[]{targets});
        } catch (Exception e) {
            // there are a number of reflection-related exceptions that can be thrown here.  We'll catch all and
            // throw a runtime exception since we will not be handling them.
            throw new RuntimeException("Unable to reflectively invoke exhaustQueue", e);
        }

        return results;
    }
}
