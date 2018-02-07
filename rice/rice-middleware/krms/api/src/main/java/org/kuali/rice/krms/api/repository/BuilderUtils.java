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
package org.kuali.rice.krms.api.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.mo.ModelObjectComplete;

/**
 * Utilities for implementing {@link ModelBuilder}s more easily.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class BuilderUtils {
	
	private static final String COULDNT_INVOKE_BUILDER_CREATE = "couldn't invoke Builder.create()";


	@SuppressWarnings("unchecked")
	public static <B> List<B> convertFromBuilderList(List<? extends ModelBuilder> toConvert) {
		if (CollectionUtils.isEmpty(toConvert)) {
			return Collections.emptyList();
		} else {
			List<B> results = new ArrayList<B>(toConvert.size());
			for (ModelBuilder elem : toConvert) {
				results.add((B)elem.build());
			}
			return Collections.unmodifiableList(results);
		}
	} 
	
	@SuppressWarnings("unchecked")
	public static <B> Set<B> convertFromBuilderSet(Set<? extends ModelBuilder> toConvert) {
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
	 * This method is useful for converting a List&lt;? extends BlahContract&gt; to a 
	 * List&lt;Blah.Builder&gt;.  You'll just need to implement Transformer to use it.
	 * 
	 * @param <A>
	 * @param <B>
	 * @param toConvert
	 * @param xform
	 * @return
	 */
	public static <A,B> List<B> transform(List<? extends A> toConvert, Transformer<A,B> xform) {
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
	
	public static <A,B> Set<B> transform(Set<? extends A> toConvert, Transformer<A,B> xform) {
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
	
}
