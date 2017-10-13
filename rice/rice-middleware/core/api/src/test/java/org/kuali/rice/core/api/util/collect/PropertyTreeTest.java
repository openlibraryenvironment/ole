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
package org.kuali.rice.core.api.util.collect;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * This class tests the PropertyTreeTest methods.
 */
public class PropertyTreeTest {
    private static final String KNOWN_SIMPLE_KEY = "simple";
    private static final String KNOWN_SIMPLE_VALUE = "simple value";
    private static final String KNOWN_COMPLEX_KEY = "known.complex.key";
    private static final String KNOWN_COMPLEX_VALUE = "known complex value";
    private static final int MIXED_COUNT = 13;

    PropertiesMap.PropertyTree tree;

    @Before
    public void setUp() throws Exception {
        tree = new PropertiesMap.PropertyTree();
    }

    // entrySet
    @Test public void testEntrySet_readwrite() {
        Set entrySet = tree.entrySet();

        boolean failedAsExpected = false;

        try {
            entrySet.clear();
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testEntrySet_emptyTree() {
        Set entrySet = tree.entrySet();

        assertTrue(entrySet.isEmpty());
    }

    @Test public void testEntrySet_oneSimpleKey() {
        setOneSimpleKey();
        Set entrySet = tree.entrySet();

        assertEquals(1, entrySet.size());

        boolean foundSimple = false;
        for (Iterator i = entrySet.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (e.getKey().equals(KNOWN_SIMPLE_KEY)) {
                foundSimple = true;
                assertEquals(e.getValue(), KNOWN_SIMPLE_VALUE);
            }
        }
        assertTrue(foundSimple);
    }

    @Test public void testEntrySet_oneComplexKey() {
        setOneComplexKey();
        Set entrySet = tree.entrySet();

        assertEquals(1, entrySet.size());

        boolean foundComplex = false;
        for (Iterator i = entrySet.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (e.getKey().equals(KNOWN_COMPLEX_KEY)) {
                foundComplex = true;
                assertEquals(e.getValue(), KNOWN_COMPLEX_VALUE);
            }
        }
        assertTrue(foundComplex);
    }

    @Test public void testEntrySet_manyMixedKeys() {
        setManyMixedKeys();
        Set entrySet = tree.entrySet();

        assertEquals(MIXED_COUNT, entrySet.size());

        boolean foundSimple = false;
        boolean foundComplex = false;
        for (Iterator i = entrySet.iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            if (e.getKey().equals(KNOWN_SIMPLE_KEY)) {
                foundSimple = true;
                assertEquals(e.getValue(), KNOWN_SIMPLE_VALUE);
            }
            else if (e.getKey().equals(KNOWN_COMPLEX_KEY)) {
                foundComplex = true;
                assertEquals(e.getValue(), KNOWN_COMPLEX_VALUE);
            }
        }
        assertTrue(foundSimple);
        assertTrue(foundComplex);
    }

    // size()
    @Test public void testSize_emptyTree() {
        assertEquals(0, tree.size());
    }

    @Test public void testSize_oneSimpleKey() {
        setOneSimpleKey();

        assertEquals(1, tree.size());
    }

    @Test public void testSize_oneComplexKey() {
        setOneComplexKey();

        assertEquals(1, tree.size());
    }

    @Test public void testSize_manyMixedKeys() {
        setManyMixedKeys();

        assertEquals(MIXED_COUNT, tree.size());
    }

    // isEmpty
    @Test public void testIsEmpty_emptyTree() {
        assertTrue(tree.isEmpty());
    }

    @Test public void testIsEmpty_oneSimpleKey() {
        setOneSimpleKey();

        assertFalse(tree.isEmpty());
    }

    @Test public void testIsEmpty_oneComplexKey() {
        setOneComplexKey();

        assertFalse(tree.isEmpty());
    }

    @Test public void testIsEmpty_manyMixedKeys() {
        setManyMixedKeys();

        assertFalse(tree.isEmpty());
    }

    // values
    @Test public void testValues_readwrite() {
        Collection values = tree.values();

        boolean failedAsExpected = false;

        try {
            values.clear();
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testValues_emptyTree() {
        Collection values = tree.values();

        assertTrue(values.isEmpty());
    }

    @Test public void testValues_oneSimpleKey() {
        setOneSimpleKey();
        Collection values = tree.values();

        assertEquals(1, values.size());
        assertTrue(values.contains(KNOWN_SIMPLE_VALUE));
    }

    @Test public void testValues_oneComplexKey() {
        setOneComplexKey();
        Collection values = tree.values();

        assertEquals(1, values.size());
        assertTrue(values.contains(KNOWN_COMPLEX_VALUE));
    }

    @Test public void testValues_manyMixedKeys() {
        setManyMixedKeys();
        Collection values = tree.values();

        assertEquals(MIXED_COUNT, values.size());
        assertTrue(values.contains(KNOWN_SIMPLE_VALUE));
        assertTrue(values.contains(KNOWN_COMPLEX_VALUE));
    }

    // keySet
    @Test public void testKeySet_readwrite() {
        Set keys = tree.keySet();

        boolean failedAsExpected = false;

        try {
            keys.clear();
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testKeySet_emptyTree() {
        Set keys = tree.keySet();

        assertTrue(keys.isEmpty());
    }

    @Test public void testKeySet_oneSimpleKey() {
        setOneSimpleKey();
        Set keys = tree.keySet();

        assertEquals(1, keys.size());
        assertTrue(keys.contains(KNOWN_SIMPLE_KEY));
    }

    @Test public void testKeySet_oneComplexKey() {
        setOneComplexKey();
        Set keys = tree.keySet();

        assertEquals(1, keys.size());
        assertTrue(keys.contains(KNOWN_COMPLEX_KEY));
    }

    @Test public void testKeySet_manyMixedKeys() {
        setManyMixedKeys();
        Set keys = tree.keySet();

        assertEquals(MIXED_COUNT, keys.size());
        assertTrue(keys.contains(KNOWN_SIMPLE_KEY));
        assertTrue(keys.contains(KNOWN_COMPLEX_KEY));
    }


    // containsKey
    @Test public void testContainsKey_invalidKey() {
        boolean failedAsExpected = false;

        try {
            assertFalse(tree.containsKey(null));
        }
        catch (IllegalArgumentException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testContainsKey_emptyTree() {
        assertFalse(tree.containsKey(KNOWN_SIMPLE_KEY));
    }

    @Test public void testContainsKey_unknownKey() {
        setManyMixedKeys();

        assertFalse(tree.containsKey("hopefully unknown key"));
    }

    @Test public void testContainsKey_oneSimpleKey() {
        setOneSimpleKey();

        assertTrue(tree.containsKey(KNOWN_SIMPLE_KEY));
    }

    @Test public void testContainsKey_oneComplexKey() {
        setOneComplexKey();

        assertTrue(tree.containsKey(KNOWN_COMPLEX_KEY));
    }

    @Test public void testContainsKey_manyMixedKeys() {
        setManyMixedKeys();

        assertTrue(tree.containsKey(KNOWN_SIMPLE_KEY));
        assertTrue(tree.containsKey(KNOWN_COMPLEX_KEY));
    }


    // containsValue
    @Test public void testContainsValue_invalidValue() {
        boolean failedAsExpected = false;

        try {
            assertFalse(tree.containsValue(null));
        }
        catch (IllegalArgumentException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testContainsValue_emptyTree() {
        assertFalse(tree.containsValue(KNOWN_SIMPLE_VALUE));
    }

    @Test public void testContainsValue_unknownValue() {
        setManyMixedKeys();

        assertFalse(tree.containsValue("hopefully unknown value"));
    }

    @Test public void testContainsValue_oneSimpleKey() {
        setOneSimpleKey();

        assertTrue(tree.containsValue(KNOWN_SIMPLE_VALUE));
    }

    @Test public void testContainsValue_oneComplexKey() {
        setOneComplexKey();

        assertTrue(tree.containsValue(KNOWN_COMPLEX_VALUE));
    }

    @Test public void testContainsValue_manyMixedKeys() {
        setManyMixedKeys();

        assertTrue(tree.containsValue(KNOWN_SIMPLE_VALUE));
        assertTrue(tree.containsValue(KNOWN_COMPLEX_VALUE));
    }


    // get
    @Test public void testGet_invalidKey() {
        boolean failedAsExpected = false;

        try {
            tree.get(null);
        }
        catch (IllegalArgumentException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testGet_unknownKey() {
        setManyMixedKeys();

        assertNull(tree.get("hopefully unknown key"));
    }

    @Test public void testGet_oneSimpleKey() {
        setOneSimpleKey();

        assertEquals(KNOWN_SIMPLE_VALUE, tree.get(KNOWN_SIMPLE_KEY).toString());
    }

    @Test public void testGet_oneComplexKey() {
        setOneComplexKey();

        assertEquals(KNOWN_COMPLEX_VALUE, tree.get(KNOWN_COMPLEX_KEY).toString());
    }

    @Test public void testGet_manyMixedKeys() {
        setManyMixedKeys();

        assertEquals(KNOWN_SIMPLE_VALUE, tree.get(KNOWN_SIMPLE_KEY).toString());
        assertEquals(KNOWN_COMPLEX_VALUE, tree.get(KNOWN_COMPLEX_KEY).toString());
    }

    @Test public void testGet_chainedGet() throws Exception {
        setManyMixedKeys();

        String value = ((PropertiesMap.PropertyTree) ((PropertiesMap.PropertyTree) tree.get("known")).get("complex")).get("key").toString();

        assertNotNull(value);
        assertEquals(KNOWN_COMPLEX_VALUE, value);
    }

    /*
     * As close a simulation as possible of how the JSTL variable-reference will actually be implemented.
     */
    @Test public void testGet_jstlGet() throws Exception {
        setManyMixedKeys();

        Class[] getParamTypes = { Object.class };

        Object level1 = tree.get("known");

        Method m1 = BeanUtils.findMethod(level1.getClass(), "get", getParamTypes);
        Object level2 = m1.invoke(level1, new Object[] { "complex" });

        Method m2 = BeanUtils.findMethod(level2.getClass(), "get", getParamTypes);
        Object level3 = m2.invoke(level2, new Object[] { "key" });

        String value = level3.toString();

        assertNotNull(value);
        assertEquals(KNOWN_COMPLEX_VALUE, value);
    }

    // unsupported operations
    @Test public void testClear() {
        setManyMixedKeys();

        boolean failedAsExpected = false;

        try {
            tree.clear();
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testPut() {
        setManyMixedKeys();

        boolean failedAsExpected = false;

        try {
            tree.put("meaningless", "entry");
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testPutAll() {
        setManyMixedKeys();

        Properties p = new Properties();
        p.setProperty("meaningless", "value");

        boolean failedAsExpected = false;
        try {
            tree.putAll(p);
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }

    @Test public void testRemove() {
        setManyMixedKeys();

        boolean failedAsExpected = false;

        try {
            tree.remove(KNOWN_SIMPLE_KEY);
        }
        catch (UnsupportedOperationException e) {
            failedAsExpected = true;
        }

        assertTrue(failedAsExpected);
    }


    // support methods
    private void setOneSimpleKey() {
        Properties p = new Properties();
        p.setProperty(KNOWN_SIMPLE_KEY, KNOWN_SIMPLE_VALUE);

        tree = new PropertiesMap.PropertyTree(p);
    }

    private void setOneComplexKey() {
        Properties p = new Properties();
        p.setProperty(KNOWN_COMPLEX_KEY, KNOWN_COMPLEX_VALUE);

        tree = new PropertiesMap.PropertyTree(p);
    }

    private void setManyMixedKeys() {
        Properties p = new Properties();

        p.setProperty(KNOWN_SIMPLE_KEY, KNOWN_SIMPLE_VALUE);
        p.setProperty(KNOWN_COMPLEX_KEY, KNOWN_COMPLEX_VALUE);
        p.setProperty("a", "a");
        p.setProperty("b.b", "bb");
        p.setProperty("b.c", "cb");
        p.setProperty("c.b.c", "cbc");
        p.setProperty("c.b.d", "dbc");
        p.setProperty("c.c.a", "acc");
        p.setProperty("a.c.b", "bca");
        p.setProperty("a.c.c", "cca");
        p.setProperty("b.a", "ab");
        p.setProperty("b", "b");
        p.setProperty("d", "d");

        tree = new PropertiesMap.PropertyTree(p);
    }
}
