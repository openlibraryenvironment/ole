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
package org.kuali.rice.core.api.util.type;

import com.google.common.collect.MapMaker;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;

/**
 * Provides utilities for checking the types of objects.
 */
public class TypeUtils {
    private static final Collection<Class<?>> BOOLEAN_CLASSES = add(Boolean.class, Boolean.TYPE);
    private static final Collection<Class<?>> INTEGRAL_CLASSES = add(Byte.class, Byte.TYPE, Short.class, Short.TYPE, Integer.class, Integer.TYPE, Long.class, Long.TYPE, BigInteger.class, KualiInteger.class);
    private static final Collection<Class<?>> DECIMAL_CLASSES = add(Float.class, Float.TYPE, Double.class, Double.TYPE, BigDecimal.class, AbstractKualiDecimal.class, KualiDecimal.class);
    private static final Collection<Class<?>> TEMPORAL_CLASSES = add(java.util.Date.class, java.sql.Date.class, java.sql.Timestamp.class, LocalDate.class, DateTime.class);
    private static final Collection<Class<?>> STRING_CLASSES = Collections.<Class<?>>singleton(CharSequence.class);
    private static final Collection<Class<?>> CLASS_CLASSES = Collections.<Class<?>>singleton(Class.class);
    @SuppressWarnings("unchecked")
    private static final Collection<Class<?>> SIMPLE_CLASSES = add(BOOLEAN_CLASSES, INTEGRAL_CLASSES, DECIMAL_CLASSES, TEMPORAL_CLASSES, STRING_CLASSES);

    private static final ConcurrentMap<Class<?>, Boolean> IS_BOOLEAN_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_INTEGRAL_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_DECIMAL_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_TEMPORAL_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_STRING_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_SIMPLE_CACHE = new MapMaker().softKeys().makeMap();
    private static final ConcurrentMap<Class<?>, Boolean> IS_CACHE_CACHE = new MapMaker().softKeys().makeMap();

    private TypeUtils() {
        throw new UnsupportedOperationException("do not call.");
    }

    /**
     * @param clazz class token
     * @return true if the given Class is an boolean type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isBooleanClass(Class<?> clazz) {
        return is(clazz, BOOLEAN_CLASSES, IS_BOOLEAN_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is an integral type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isIntegralClass(Class<?> clazz) {
        return is(clazz, INTEGRAL_CLASSES, IS_INTEGRAL_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is a decimal type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isDecimalClass(Class<?> clazz) {
        return is(clazz, DECIMAL_CLASSES, IS_DECIMAL_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is a temporal type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isTemporalClass(Class<?> clazz) {
        return is(clazz, TEMPORAL_CLASSES, IS_TEMPORAL_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is a string type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isStringClass(Class<?> clazz) {
        return is(clazz, STRING_CLASSES, IS_STRING_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is a Class type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isClassClass(Class<?> clazz) {
        return is(clazz, CLASS_CLASSES, IS_CACHE_CACHE);
    }

    /**
     * @param clazz class token
     * @return true if the given Class is a "simple" - one of the primitive
     *         types, their wrappers, or a temporal type
     * @throws IllegalArgumentException if the given Class is null
     */
    public static boolean isSimpleType(Class<?> clazz) {
        return is(clazz, SIMPLE_CLASSES, IS_SIMPLE_CACHE);
    }

    /**
     * Checks is a class is assignable to a class in the give collection.  Also handles caching.
     *
     * @param clazz the class to check
     * @param clazzes the collection of classes to check against
     * @param cache the cache to check against
     * @return a if the class is assignable
     * @throws IllegalArgumentException if any arg is null
     */
    private static boolean is(Class<?> clazz, Collection<Class<?>> clazzes, ConcurrentMap<Class<?>, Boolean> cache) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (clazzes == null) {
            throw new IllegalArgumentException("clazzes is null");
        }

        if (cache == null) {
            throw new IllegalArgumentException("cache is null");
        }

        Boolean result = cache.get(clazz);
        if (result == null) {
            result = Boolean.valueOf(isa(clazzes, clazz));
            cache.putIfAbsent(clazz, result);
        }
        return result.booleanValue();
    }

    /**
     * @param types class tokens to check against
     * @param type class token
     * @return true if the given Class is assignable from one of the classes in
     *         the given Class[]
     */
    private static boolean isa(Collection<Class<?>> types, Class<?> type) {
        for (Class<?> cur : types) {
            if (cur.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * adds classes to a collection which is returned.
     *
     * @param classes the classes to add
     * @return an unmodifiable collection
     */
    private static Collection<Class<?>> add(Class<?>... classes) {
        return Collections.unmodifiableCollection(Arrays.asList(classes));
    }

    /**
     * adds classes to a collection which is returned.
     *
     * @param classes the classes to add
     * @return an unmodifiable collection
     */
    private static Collection<Class<?>> add(Collection<Class<?>>... classes) {
        final Collection<Class<?>> temp = new ArrayList<Class<?>>();
        for (Collection<Class<?>> clazz : classes) {
            temp.addAll(clazz);
        }

        return Collections.unmodifiableCollection(temp);
    }
}
