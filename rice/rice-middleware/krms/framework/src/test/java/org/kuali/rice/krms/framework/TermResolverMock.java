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
package org.kuali.rice.krms.framework;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.kuali.rice.krms.api.engine.TermResolver;

/**
 * Cheesy {@link TermResolver} implementation for testing purposes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class TermResolverMock<T> implements TermResolver<T> {
	
	private T result;
	private String outputName;
	private Set<String> params;
	
	public TermResolverMock(String outputName, T result) {
		this(outputName, null, result);
	}
	
	public TermResolverMock(String outputName, String [] params, T result) {
		this.outputName = outputName;
		this.result = result;
		if (ArrayUtils.isEmpty(params)) {
			this.params = Collections.emptySet(); 
		} else {
			this.params = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(params)));
		}
	}
	
	@Override
	public int getCost() { return 1; }
	
	@Override
	public String getOutput() { return outputName; }
	
	@Override
	public Set<String> getPrerequisites() { return Collections.emptySet(); }
	
	@Override
	public Set<String> getParameterNames() {
		return params;
	}
	
	@Override
	public T resolve(Map<String, Object> resolvedPrereqs, Map<String, String> parameters) {
		return result;
	}
};