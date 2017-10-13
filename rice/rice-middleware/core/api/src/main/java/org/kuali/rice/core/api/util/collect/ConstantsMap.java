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

import org.kuali.rice.core.api.exception.RiceRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is a map for Constant properties.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
//FIXME: make class threadsafe
public final class ConstantsMap implements Map<String, Object> {
    
	private Map<String, Object> map;

    public void setConstantClass(Class<?> constantClass) {
        final Map<String, Object> m = new HashMap<String, Object>();

    	publishFields(m, constantClass);
    	map = Collections.unmodifiableMap(m);
	}

    /**
     * Publishes all of the static, final, non-private fields of the given Class as entries in the given HashMap instance
     * 
     * @param constantMap
     * @param c
     */
    private void publishFields(Map<String, Object> constantMap, Class<?> c) {
        final Field[] fields = c.getDeclaredFields();
        for (final Field field : fields) {
            final int modifier = field.getModifiers();

            // publish values of static, final, non-private members
            if (Modifier.isStatic(modifier) && Modifier.isFinal(modifier) && !Modifier.isPrivate(modifier)) {
                try {
                    final String fieldName = field.getName();

                    constantMap.put(fieldName, field.get(null));
                } catch (IllegalAccessException e) {
                	throw new ConstantExporterException(e);
                }
            }
        }

        // publish values of appropriate fields of member classes
        publishMemberClassFields(constantMap, c);
    }

    /**
     * Publishes all of the static, final, non-private fields of the non-anonymous member classes of the given Class as entries in
     * the given HashMap instance
     * 
     * @param constantMap
     * @param c
     */
    private void publishMemberClassFields(Map<String, Object> constantMap, Class<?> c) {
        final Class<?>[] memberClasses = c.getClasses();

        for (final Class<?> memberClass : memberClasses) {
            if (!memberClass.isAnonymousClass()) {
                final String memberPrefix = memberClass.getSimpleName();

                final Map<String, Object> subclassMap = new HashMap<String, Object>();
                publishFields(subclassMap, memberClass);
                constantMap.put(memberPrefix, Collections.unmodifiableMap(subclassMap));
            }
        }
    }
	
    //delegate methods
    
	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return this.map.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return this.map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return this.map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		this.map.putAll(m);
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	@Override
	public Set<String> keySet() {
		return this.map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return this.map.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return this.map.entrySet();
	}

	@Override
	public boolean equals(Object o) {
		return this.map.equals(o);
	}

	@Override
	public int hashCode() {
		return this.map.hashCode();
	}

	private static class ConstantExporterException extends RiceRuntimeException {

		private ConstantExporterException(Throwable t) {
			super(t);
		}
	}
}
