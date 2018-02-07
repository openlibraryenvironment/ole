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
package org.kuali.rice.krms.impl.repository.language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is a registry of template contexts which the requirement 
 * component translator uses to generate natural language.
 */
public class TranslationContextRegistry<T extends TranslationContext>  {

	/** Registry context map */
	private Map<String, List<T>> registry = new HashMap<String, List<T>>();

	/**
	 * Constructor.
	 */
	public TranslationContextRegistry() {
	}

	/**
	 * Constructor. Adds a context registry as a map.
	 * 
	 * @param registry Context registry
	 */
	public TranslationContextRegistry(final Map<String, List<T>> registry) {
		this.registry = registry;
	}

	/**
	 * Adds a context to the registry. Key is usually a TermParameterType key.
	 * 
	 * @param key Context key
	 * @param context Context
	 */
	public void add(final String key, final T context) {
		if(this.registry.containsKey(key)) {
			this.registry.get(key).add(context);
		} else {
			List<T> list = new ArrayList<T>();
			list.add(context);
			this.registry.put(key, list);
		}
	}

	/**
	 * Gets a context from the registry. Key is usually a TermParameterType key.
	 * 
	 * @param key Context key
	 * @return A context
	 */
	public List<T> get(final String key) {
		return this.registry.get(key);
	}

	/**
	 * Returns true if a context exists for <code>key</code>; otherwise false.
	 * 
	 * @param key Context key
	 * @return True if a context exists otherwise false
	 */
	public boolean containsKey(final String key) {
		return this.registry.containsKey(key);
	}

	/**
	 * Remove a context from the registry. Key is usually a 
	 *
	 * @param key
	 * @return
	 */
	public List<T> remove(final String key) {
		return this.registry.remove(key);
	}

	/**
	 * Returns the number of keys of the registry.
	 * 
	 * @return Number of keys in the registry
	 */
	public int size() {
		return this.registry.size();
	}

	@Override
	public String toString() {
		return this.registry.toString();
	}
}
