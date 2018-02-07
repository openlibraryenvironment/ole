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
package org.kuali.rice.core.api.cache;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * A utility class that can be used to generate cache keys for more complex method signatures.  Currently, the utilities
 * on this class focus on generating cache keys for collections of objects.  Since the caching infrastructure only
 * supports a single @{code String} value as a key for cache entries, this utility helps to provide a standard way to
 * generate such compound caching keys.
 *
 * <p>It is possible to use this utility class when specifying keys for cached objects using Spring's caching
 * abstraction (which is what the Rice caching infrastructure is built on).  This is possible using the Spring
 * Expression Language (SPEL).  An example might look something like the following:</p>
 *
 * <pre>
 * {@code @Cacheable(value = "StuffCache", key="'ids=' + T(org.kuali.rice.core.api.cache.CacheKeyUtils).key(#p0)")
 * List<Stuff> getStuff(List<String> stuffIds); }
 * </pre>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 2.0
 */
public final class CacheKeyUtils {

    private CacheKeyUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a String key value out of a Collection.  It accomplishes this by first sorting the given collection
     * (entries in the collection must implement Comparable) and then construct a key based on the {@code .toString()}
     * values of each item in the sorted collection.
     *
     * <p>The sorting of the given list happens on a copy of the list, so this method does not side-affect the given
     * list.</p>
     *
     * @param col the collection.  if null will return "", if empty, will return "[]"
     * @param <K> the col type
     * 
     * @return the collection as a string value
     */
    public static <K extends Comparable<K>> String key(Collection<K> col) {
        if (col == null) {
            return "";
        }

        final List<K> sorted = new ArrayList<K>(col);

        if (col.size() > 1) {
            Collections.sort(sorted);
        }

        final StringBuilder b = new StringBuilder("[");
        for (K entry : sorted) {
            if (entry != null) {
                b.append(entry);
                b.append(",");
            }
        }
        b.append("]");
        return b.toString();
    }

    /**
     * Create a String key value out of a Map.  It accomplishes this by first sorting the given map on it's keys
     * (keys in the map must implement Comparable) and then construct a key based on the {@code .toString()}
     * values of each item in the sorted collection.
     *
     * <p>The sorting of the given map happens on a copy of the map, so this method does not side-affect the given
     * map.</p>
     *
     * @param col the map.  if null will return "", if empty, will return "[]"
     * @param <K> the col type
     *
     * @return the map as a string value
     */
    public static <K extends Comparable<K>> String mapKey(Map<K, ?> col) {
        if (col == null) {
            return "";
        }

        final List<K> sorted = new ArrayList<K>(col.keySet());

        if (col.size() > 1) {
            Collections.sort(sorted);
        }

        final StringBuilder b = new StringBuilder("[");
        for (K entry : sorted) {
            if (entry != null) {
                b.append(entry);
                b.append("|");
                b.append(col.get(entry));
                b.append(",");
            }
        }
        b.append("]");
        return b.toString();
    }

}
