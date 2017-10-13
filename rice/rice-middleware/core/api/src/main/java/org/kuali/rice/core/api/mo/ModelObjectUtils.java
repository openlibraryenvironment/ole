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
package org.kuali.rice.core.api.mo;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A set of simple utilities to assist with common idioms in immutable model objects and their builders.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ModelObjectUtils {

    /**
     * Takes the given list of {@code ModelBuilder} objects and invokes the
     * {@link org.kuali.rice.core.api.mo.ModelBuilder#build()} method on each of them, adding them to a new list and
     * return an unmodifiable copy.  If the given list is empty or null, will return an empty and unmodifiable list.
     *
     * @param builderList the list of builders to build and add to resulting list, may be empty or null
     * @param <T> the type of the object that is built by the builders in the list, it is up to the caller of this
     *        method to ensure they define the proper parameterized list for the return type.
     * @return an unmodifiable list containing objects built from the given list of model builders
     */
    public static <T> List<T> buildImmutableCopy(List<? extends ModelBuilder> builderList) {
        if (CollectionUtils.isEmpty(builderList)) {
            return Collections.emptyList();
        }
        List<T> copy = new ArrayList<T>();
        for (ModelBuilder builder : builderList) {
            // since ModelBuilder is not parameterized, this code must assume that the appropriate type of object is built
            @SuppressWarnings("unchecked")
            T built = (T)builder.build();
            copy.add(built);
        }
        return Collections.unmodifiableList(copy);
    }

    @SuppressWarnings("unchecked")
    public static <B> Set<B> buildImmutableCopy(Set<? extends ModelBuilder> toConvert) {
        if (CollectionUtils.isEmpty(toConvert)) {
            return Collections.emptySet();
        } else {
            Set<B> results = new HashSet<B>(toConvert.size());
            for (ModelBuilder elem : toConvert) {
                results.add((B)elem.build());
            }
            return Collections.unmodifiableSet(results);
        }
    }

    /**
     * Takes the given list and returns an unmodifiable copy of that list containing the same elements as the original
     * list.  This method handles a null list being passed to it by returning an unmodifiable empty list.
     *
     * @param listToCopy the list to copy
     * @param <T> the type of the elements in the given list
     *
     * @return an unmodifiable copy containing the same elements as the given list
     */
    public static <T> List<T> createImmutableCopy(List<T> listToCopy) {
        if (CollectionUtils.isEmpty(listToCopy)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList<T>(listToCopy));
    }

    /**
     * Takes the given set and returns an unmodifiable copy of that set containing the same elements as the original
     * set.  This method handles a null set being passed to it by returning an unmodifiable empty set.
     *
     * @param setToCopy the set to copy
     * @param <T> the type of the elements in the given set
     *
     * @return an unmodifiable copy containing the same elements as the given set
     */
    public static <T> Set<T> createImmutableCopy(Set<T> setToCopy) {
        if (CollectionUtils.isEmpty(setToCopy)) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet<T>(setToCopy));
    }

    /**
     * Takes the given map and returns an unmodifiable copy of that map containing the same entries as the original
     * map.  This method handles a null map being passed to it by returning an unmodifiable empty map.
     *
     * @param mapToCopy the map to copy
     * @param <K, V> the type of the key and value elements in the given map
     *
     * @return an unmodifiable copy containing the same elements as the given set
     */
    public static <K, V> Map<K, V> createImmutableCopy(Map<K, V> mapToCopy) {
        if (mapToCopy == null || mapToCopy.isEmpty()) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new HashMap<K, V>(mapToCopy));
    }

	/**
	 * This method is useful for converting a List&lt;? extends BlahContract&gt; to a
	 * List&lt;Blah.Builder&gt;.  You'll just need to implement Transformer to use it.
	 *
	 * @param <A>
	 * @param <B>
	 * @param toConvert
	 * @param xform
	 * @return
	 */
	public static <A,B> List<B> transform(Collection<? extends A> toConvert, Transformer<A,B> xform) {
		if (CollectionUtils.isEmpty(toConvert)) {
			return new ArrayList<B>();
		} else {
			List<B> results = new ArrayList<B>(toConvert.size());
			for (A elem : toConvert) {
				results.add(xform.transform(elem));
			}
			return results;
		}
	}

    /**
     * This method is useful for converting a Set&lt;? extends BlahContract&gt; to a
     * Set&lt;Blah.Builder&gt;.  You'll just need to implement Transformer to use it.
     *
     * @param <A>
     * @param <B>
     * @param toConvert
     * @param xform
     * @return
     */
	public static <A,B> Set<B> transformSet(Collection<? extends A> toConvert, Transformer<A,B> xform) {
		if (CollectionUtils.isEmpty(toConvert)) {
			return new HashSet<B>();
		} else {
			Set<B> results = new HashSet<B>(toConvert.size());
			for (A elem : toConvert) {
				results.add(xform.transform(elem));
			}
			return results;
		}
	}

	public interface Transformer<A,B> {
		public B transform(A input);
	}

    private ModelObjectUtils() {
        throw new UnsupportedOperationException("Do not call.");
    }

}
