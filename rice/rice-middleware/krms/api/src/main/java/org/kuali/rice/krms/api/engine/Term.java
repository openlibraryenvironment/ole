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
package org.kuali.rice.krms.api.engine;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.util.CollectionUtils;

/**
 * Identifies a (hopefully) resolvable {@link Term}.  For resolution in the {@link TermResolutionEngine}, The
 * appropriate {@link TermResolver} will be selected by matching the name and parameters of the {@link Term} with
 * the output and parameter names of the {@link TermResolver}. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class Term implements Comparable<Term> {

	private final String name;
	
	private final Map<String, String> parameters;
	
	private static final TreeMapComparator<String,String> treeMapComparator = new TreeMapComparator<String, String>();

    /**
     * Constructor
     * @param name of the term
     */
	public Term(String name) {
		this(name, null);
	}	
	
	/**
	 * This constructs a Term, which is a named piece of data that is usually obtainable
	 * through the {@link TermResolutionEngine}
	 *
     * @param name the term name
     * @param parameters an optional map of properties that may be used to allow a single TermResolver to resolve multiple Terms
     */
	public Term(String name, Map<String, String> parameters) {
		this.name = name;
		if (parameters == null) {
			this.parameters = Collections.emptyMap();
		} else {
			// using TreeMap for ordered iteration since we're comparable
			this.parameters = Collections.unmodifiableMap(new TreeMap<String, String>(parameters));
		}
	}

    /**
     * Return the name of the term
     * @return name of the term
     */
	public String getName() { return this.name; }
	public Map<String, String> getProperties() { return parameters; }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Term other = (Term) obj;
		return this.compareTo(other) == 0;
	}

	@Override
	public int compareTo(Term o) {
		if (o == null) return 1;
		if (this == o) return 0;
		return (treeMapComparator.compare(this.parameters, o.parameters));
	}
	
	/**
     * Return an unmodifiable Map of parameters specified on this Term.
	 * @return an unmodifiable Map of parameters specified on this Term.  Guaranteed non-null.
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	@Override
	public String toString() {
		// TODO make this pretty
		StringBuilder sb = new StringBuilder();
		if (parameters != null) for (Entry<String,String> parameter : parameters.entrySet()) {
			sb.append(", ");
			sb.append(parameter.getKey());
			sb.append("=");
			sb.append(parameter.getValue());
		}
		return getClass().getSimpleName()+"(["+ name + "]" +  sb.toString() + ")";
	}

	@SuppressWarnings("rawtypes")
	private static class TreeMapComparator<T extends Comparable, V extends Comparable> implements Comparator<Map<T,V>>, Serializable {
		
		private static final long serialVersionUID = 1L;

		/**
		 * This overridden method compares two {@link TreeMap}s whose keys and elements are both {@link Comparable}
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@SuppressWarnings("unchecked")
        @Override
		public int compare(Map<T,V> o1, Map<T,V> o2) {
			if (CollectionUtils.isEmpty(o1)) {
				if (CollectionUtils.isEmpty(o2)) return 0;
				return -1;
			} else if (CollectionUtils.isEmpty(o2)) {
				return 1;
			}
			
			// neither one is empty.  Iterate through both.

			Iterator<Entry<T,V>> o1Iter = o1.entrySet().iterator();
			Iterator<Entry<T,V>> o2Iter = o2.entrySet().iterator();
			
			while (o1Iter.hasNext() && o2Iter.hasNext()) {
				Entry<T,V> o1Elem = o1Iter.next(); 
				Entry<T,V> o2Elem = o2Iter.next();
				if (o1Elem == null) {
					if (o2Elem == null) continue;
					return -1;
				} 
				
				T o1ElemKey = o1Elem.getKey();
				T o2ElemKey = o2Elem.getKey();
				if (o1ElemKey == null) {
					if (o2ElemKey != null) return -1;
					// if they're both null, fall through
				} else {
					int elemKeyCompare = o1ElemKey.compareTo(o2ElemKey);
					if (elemKeyCompare != 0) return elemKeyCompare;
				}
				
				V o1ElemValue = o1Elem.getValue();
				V o2ElemValue = o2Elem.getValue();
				if (o1ElemValue == null) {
					if (o2ElemValue != null) return -1;
					// if they're both null, fall through
				} else {
					int elemValueCompare = o1ElemValue.compareTo(o2ElemValue);
					if (elemValueCompare != 0) return elemValueCompare;
				}
			}
			
			if (o1Iter.hasNext()) return 1;
			if (o2Iter.hasNext()) return -1;
			return 0;
		}

	}

	
}
