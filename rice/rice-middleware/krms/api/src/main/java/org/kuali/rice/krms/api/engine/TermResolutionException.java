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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.springframework.util.CollectionUtils;

/**
 * An Exception for {@link TermResolver} exceptions.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TermResolutionException extends RiceRuntimeException {

	private static final long serialVersionUID = 1L;

	public final String termResolverClassName;
	public final String outputTerm;
	public final Set<String> prereqs;
	public final Set<String> parameterNames;
	public final Map<String, String> parameters;

    /**
     * Builds the resolution info string from the given values.
     * @param tr {@link TermResolver} whose values to append to the result String if not null
     * @param parameters Map<String, String> whose keys and values will be appended to the result String
     * @return String representing the given values
     */
	private static String buildResolutionInfoString(TermResolver<?> tr, Map<String, String> parameters) {
		StringBuilder result = new StringBuilder();
		
		result.append("[");
		result.append(TermResolver.class.getSimpleName() + "=");

		if (tr == null) { 
			result.append("null");
		} else {
			result.append(tr.toString());
		}
		
		result.append(", parameters={");

		boolean firstEntry = true;
		if (!CollectionUtils.isEmpty(parameters)) {
			
			for (Entry<String,String> parameter : parameters.entrySet()){

				if (firstEntry) {
					firstEntry = false;
				} else { 
					result.append(",");
				}

				result.append(parameter.getKey());
				result.append("=");
				result.append(parameter.getValue());
			}
		}
		
		result.append("}]");
		return result.toString();
	}

    /**
     * Create a TermResolutionException with the given values
     * @param message the exception message
     * @param tr {@link TermResolver} to use to set values to if not null
     * @param parameters to set the parameters value to if not null
     * @param cause the root Throwable cause.
     */
	public TermResolutionException(String message, TermResolver<?> tr, Map<String, String> parameters, Throwable cause) {
		super(message + " " + buildResolutionInfoString(tr, parameters), cause);
		if (tr == null) {
			termResolverClassName = "";
			outputTerm = null;
			prereqs = null;
			parameterNames = null;
		} else {
			termResolverClassName = tr.getClass().getName();
			outputTerm = tr.getOutput();
			prereqs = tr.getPrerequisites();
			parameterNames = Collections.unmodifiableSet(new HashSet<String>(tr.getParameterNames()));
		}
		if (parameters != null){
			this.parameters = Collections.unmodifiableMap(new HashMap<String, String>(parameters));
		} else {
			this.parameters = null;
		}
	}

    /**
     * Create a TermResolutionException with the given values
     * @param message the exception message
     * @param tr {@link TermResolver} to use to set values to if not null
     * @param parameters to set the parameters value to if not null
     */
	public TermResolutionException(String message, TermResolver<?> tr, Map<String, String> parameters) {
		super(message + " " + buildResolutionInfoString(tr, parameters));
		if (tr == null) {
			termResolverClassName = "";
			outputTerm = null;
			prereqs = null;
			parameterNames = null;
		} else {
			termResolverClassName = tr.getClass().getName();
			outputTerm = tr.getOutput();
			prereqs = tr.getPrerequisites();
			parameterNames = Collections.unmodifiableSet(new HashSet<String>(tr.getParameterNames()));
		}
		if (parameters != null){
			this.parameters = Collections.unmodifiableMap(new HashMap<String, String>(parameters));
		} else {
			this.parameters = null;
		}
	}

}
