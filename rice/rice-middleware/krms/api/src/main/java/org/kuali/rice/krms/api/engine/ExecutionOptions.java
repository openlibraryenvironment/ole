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
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * The ExecutionOptions contain a set of options that can be passed to the KRMS
 * engine to control certain aspects related to it's execution.  It supports
 * two different types of options:
 * 
 * <ol>
 *   <li>flags - a map of pre-defined boolean {@link ExecutionFlag} instances which can be either true or false</li>
 *   <li>options - a general-purpose map of Strings which can be used to define optinos that have non-boolen values</li>
 * </ol>
 * 
 * <p>The options map can be used to pass user-defined or provider-specific options.  The ExecutionOptions are made
 * available as part of the {@link ExecutionEnvironment} during engine execution.
 * 
 * <p>Instances of ExecutionOptions are not safe for use by multiple threads.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class ExecutionOptions {

	private final Map<ExecutionFlag, Boolean> flags;
	private final Map<String, String> options;
	
	/**
	 * Construct an empty set of execution options.
	 */
	public ExecutionOptions() {
		flags = new HashMap<ExecutionFlag, Boolean>();
		options = new HashMap<String, String>();
	}
	
	/**
	 * Constructs a new set of execution options, initialized with all options
	 * and flags copied from the given set of execution options.
	 * 
	 * @param executionOptions the execution options from which to copy into the newly created instance.
	 * If the given execution options are null, then this constructor is equivalent to {@link ExecutionOptions#ExecutionOptions()}
	 */
	public ExecutionOptions(ExecutionOptions executionOptions) {
		this();
		if (executionOptions != null) {
			this.flags.putAll(executionOptions.getFlags());
			this.options.putAll(executionOptions.getOptions());
		}
	}
	
	/**
	 * Sets the value for the given flag to the given boolean value.
	 * 
	 * @param flag the flag to set
	 * @param value the flag value to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if the given flag is null
	 */
	public ExecutionOptions setFlag(ExecutionFlag flag, boolean value) {
		if (flag == null) {
			throw new IllegalArgumentException("flag was null");
		}
		flags.put(flag, value);
		return this;
	}
	
	/**
	 * Sets the value for the given option name to the given value.
	 * 
	 * @param optionName the name of the option to set
	 * @param value the value of the option to set
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if the given optionName is blank
	 */
	public ExecutionOptions setOption(String optionName, String value) {
		if (StringUtils.isBlank(optionName)) {
			throw new IllegalArgumentException("optionName was blank");
		}
		options.put(optionName, value);
		return this;
	}
	
	/**
	 * Removes the specified flag (if it has been set) from the set of execution options.
	 * If the flag is not currently set, this method will do nothing.
	 * 
	 * @param flag the flag to remove
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if the given flag is null
	 */
	public ExecutionOptions removeFlag(ExecutionFlag flag) {
		if (flag == null) {
			throw new IllegalArgumentException("flag was null");
		}
		flags.remove(flag);
		return this;
	}
	
	/**
	 * Removes the option with the specified name (if it has been set) from the set
	 * of execution options.  If the option is not currently set, this method will
	 * do nothing.
	 * 
	 * @param optionName the name of the option to remove
	 * @return a reference to this object
	 * 
	 * @throws IllegalArgumentException if the given optionName is blank
	 */
	public ExecutionOptions removeOption(String optionName) {
		if (StringUtils.isBlank(optionName)) {
			throw new IllegalArgumentException("optionName was blank");
		}
		options.remove(optionName);
		return this;
	}
	
	/**
	 * Returns the value the given flag.  If the specified flag has not been set
	 * on this object, then {@link ExecutionFlag#getDefaultValue()} will be returned.
	 * 
	 * @param flag the flag to check
	 * @return the value of the flag, or the flag's default value if the flag value
	 * is not currently set on this object
	 * 
	 * @throws IllegalArgumentException if the given flag is null
	 */
	public boolean getFlag(ExecutionFlag flag) {
		if (flag == null) {
			throw new IllegalArgumentException("flag is null");
		}
		if (isFlagSet(flag)) {
			return flags.get(flag).booleanValue();
		}
		return flag.getDefaultValue();
	}
	
	/**
	 * Returns the value for the option with the given name, or null
	 * if the option is not set.
	 * 
	 * @param optionName the name of the option for which to retrieve the value
	 * @return the value of the option, or null if the option is not set
	 * 
	 * @throws IllegalArgumentException if the given optionName is blank
	 */
	public String getOption(String optionName) {
		if (StringUtils.isBlank(optionName)) {
			throw new IllegalArgumentException("optionName is blank");
		}
		return options.get(optionName);
	}
	
	/**
	 * Checks whether or not the given flag is set.
	 * 
	 * @param flag the flag to check
	 * @return true if the flag is set, false if not
	 * 
	 * @throws IllegalArgumentException if the given flag is null
	 */
	public boolean isFlagSet(ExecutionFlag flag) {
		if (flag == null) {
			throw new IllegalArgumentException("flag is null");
		}
		return flags.containsKey(flag);
	}
	
	/**
	 * Checks whether or not the option with the given name has been set.
	 * 
	 * @param optionName the name of the option to check
	 * @return true if the option is set, false if not
	 * 
	 * @throws IllegalArgumentException if the given optionName is blank
	 */
	public boolean isOptionSet(String optionName) {
		if (StringUtils.isBlank(optionName)) {
			throw new IllegalArgumentException("optionName is blank");
		}
		return options.containsKey(optionName);
	}

	/**
	 * Returns an immutable map of the flags that have been set on this object.
	 * 
	 * @return the flags that have been set, this map be empty but will never be null
	 */
	public Map<ExecutionFlag, Boolean> getFlags() {
		return Collections.unmodifiableMap(new HashMap<ExecutionFlag, Boolean>(flags));
	}

	/**
	 * Returns an immutable map of the options that have been set on this object.
	 * 
	 * @return the options that have been set, this map be empty but will never be null
	 */	
	public Map<String, String> getOptions() {
		return Collections.unmodifiableMap(new HashMap<String, String>(options));
	}
	
}
