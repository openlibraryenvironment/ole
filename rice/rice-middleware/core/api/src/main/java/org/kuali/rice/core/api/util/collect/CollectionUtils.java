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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Yet another utility class to help work with Collections. This class contains helper methods not
 * found in any of the Collection utilities rice currently uses.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public final class CollectionUtils {

    /** do not call. */
    private CollectionUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    /**
     * Performs a "brute force" comparison of collections by testing whether the collections contain
     * each other. This circumvents any particular uniqueness or ordering constraints on the
     * collections (for instance, lists that are unordered but contain the same elements, where a
     * hashset would not suffice for comparison purposes because it enforces element uniqueness)
     */
    public static boolean collectionsEquivalent(Collection<?> a, Collection<?> b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null ^ b == null) {
            return false;
        }
        return a.containsAll(b) && b.containsAll(a);
    }

    /**
     * Creates an Iterable view of a Iterator. This allows Iterators to be used in a foreach loop.
     * 
     * <pre>
	 * {@code
	 *   Iterator<String> i ...
	 * 
	 *   for(String s : CollectionUtils.toIterable(i)) [
	 *     System.out.println("i love for each " + s);	
	 *   }
	 * }
	 * </pre>
     * 
     * @param <T> the type of the Iterable
     * @param i the iterator to wrap
     * @return the iterable
     */
    public static <T> Iterable<T> toIterable(Iterator<T> i) {
        return new IterableIterator<T>(i);
    }

    /**
     * Creates an Iterable view of a Enumeration. This allows Enumerations to be used in a foreach
     * loop.
     * 
     * <pre>
	 * {@code
	 *   Enumeration<String> e ...
	 * 
	 *   for(String s : CollectionUtils.toIterable(e)) [
	 *     System.out.println("i love for each " + s);	
	 *   }
	 * }
	 * </pre>
     * 
     * @param <T> the type of the Iterable
     * @param e the enumeration to wrap
     * @return the iterable
     */
    public static <T> Iterable<T> toIterable(Enumeration<T> e) {
        return new IterableEnumeration<T>(e);
    }

    /**
     * Functions as per {@link Collections#unmodifiableList(Collection)} with the exception that if the
     * given collection is null, this method will return an unmodifiable empty collection as per
     * {@link Collections#emptyList()}.
     *
     * @param collection the collection for which an unmodifiable view is to be returned
     * @return an unmodifiable view of the specified collection, or an unmodifiable empty collection if the
     *         given collection is null
     */
    public static <T> Collection<T> unmodifiableCollectionNullSafe(Collection<? extends T> collection) {
        if (collection == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(collection);
    }

    /**
     * Functions as per {@link Collections#unmodifiableList(List)} with the exception that if the
     * given list is null, this method will return an unmodifiable empty list as per
     * {@link Collections#emptyList()}.
     * 
     * @param list the list for which an unmodifiable view is to be returned
     * @return an unmodifiable view of the specified list, or an unmodifiable empty list if the
     *         given list is null
     */
    public static <T> List<T> unmodifiableListNullSafe(List<? extends T> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    /**
     * Functions as per {@link Collections#unmodifiableSet(Set)} with the exception that if the
     * given set is null, this method will return an unmodifiable empty set as per
     * {@link Collections#emptySet()}.
     * 
     * @param set the set for which an unmodifiable view is to be returned
     * @return an unmodifiable view of the specified set, or an unmodifiable empty set if the given
     *         set is null
     */
    public static <T> Set<T> unmodifiableSetNullSafe(Set<? extends T> set) {
        if (set == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(set);
    }

    /**
     * Functions as per {@link Collections#unmodifiableMap(Map)} with the exception that if the
     * given map is null, this method will return an unmodifiable empty map as per
     * {@link Collections#emptyMap()}.
     * 
     * @param map the map for which an unmodifiable view is to be returned
     * @return an unmodifiable view of the specified set, or an unmodifiable empty set if the given
     *         set is null
     */
    public static <K, V> Map<K, V> unmodifiableMapNullSafe(Map<? extends K, ? extends V> map) {
        if (map == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * This method will iterate over all the fields on an object searching for declared fields defined as
     * Collection, List, Set, Map type.  If those fields are null they will be set to empty unmodifiable
     * instances.  If those fields are not null then it will force them to be unmodifiable.
     *
     * This method does not handle nested types.
     *
     * @param o the object to modify.  a null object will do nothing.
     */
    public static void makeUnmodifiableAndNullSafe(Object o) throws IllegalAccessException {
        if (o == null) {
            return;
        }

        Class<?> targetClass = o.getClass();
        for (Field f : targetClass.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.getType().isAssignableFrom(List.class)) {
                    f.set(o, unmodifiableListNullSafe((List<?>) f.get(o)));
                } else if (f.getType().isAssignableFrom(Set.class)) {
                    f.set(o, unmodifiableSetNullSafe((Set<?>) f.get(o)));
                } else if (f.getType().isAssignableFrom(Collection.class)) {
                    f.set(o, unmodifiableCollectionNullSafe((Collection<?>) f.get(o)));
                } else if (f.getType().isAssignableFrom(Map.class)) {
                    f.set(o, unmodifiableMapNullSafe((Map<?, ?>) f.get(o)));
                }
            } finally {
                f.setAccessible(false);
            }
        }
    }

    /**
    * Combines multiple Enumeration into a single enumeration. The returned enumeration
    * has an enumeration that traverses the elements of each enumeration in
    * {@code inputs}. The input enumerations are not polled until necessary.
    *
    * @throws NullPointerException if any of the provided enumerations are null
    */
    public static <T> Enumeration<T> concat(Enumeration<? extends T>... inputs) {
    return new SimpleEnumeration<T>(inputs);
    }

    private static class SimpleEnumeration<T> implements Enumeration<T> {
        private final Iterator<T> iterator;

        private SimpleEnumeration(Enumeration<? extends T>... enumerations) {
            if (enumerations == null) {
                throw new NullPointerException("enumerations is null");
            }

            final List<T> internalList = new ArrayList<T>();
            for (Enumeration<? extends T> enumeration : enumerations) {
                if (enumeration == null) {
                    throw new NullPointerException("input is null");
                }

                while (enumeration.hasMoreElements()) {
                    internalList.add(enumeration.nextElement());
                }
            }
            iterator = internalList.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }
        @Override
        public T nextElement() {
            return iterator.next();
        }
    }

    /**
     * An adapter from an Enumeration to Iterable.
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     * 
     * @param <T> the type of Enumeration and Iterable
     */
    private static class IterableEnumeration<T> implements Iterable<T> {
        private final Enumeration<T> e;

        private IterableEnumeration(final Enumeration<T> e) {
            if (e == null) {
                throw new IllegalArgumentException("the enumeration is null");
            }

            this.e = e;
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                @Override
                public boolean hasNext() {
                    return e.hasMoreElements();
                }

                @Override
                public T next() {
                    return e.nextElement();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("this iterator does not support remove");
                }
            };
        }
    }

    /**
     * An adapter from an Iterator to Iterable.
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     * 
     * @param <T> the type of Iterator and Iterable
     */
    private static class IterableIterator<T> implements Iterable<T> {
        private final Iterator<T> i;

        private IterableIterator(final Iterator<T> i) {
            if (i == null) {
                throw new IllegalArgumentException("the iterator is null");
            }

            this.i = i;
        }

        @Override
        public Iterator<T> iterator() {
            return i;
        }
    }
}
