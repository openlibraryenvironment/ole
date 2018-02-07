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

import org.kuali.rice.core.api.exception.RiceRuntimeException;

/**
 * An exception which indicates that the type of data being evaluated in the
 * engine does not match the expected type.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class IncompatibleTypeException extends RiceRuntimeException {

	private static final long serialVersionUID = -8359509154258982033L;
	
	private final Object value;
	private final Class<?>[] validTypes;
	
	/**
	 * Constructs an IncompatibleTypeException with a reference to the object
	 * being checked and an array of valid Class objects which the type failed
	 * to match.
	 * 
	 * <p>A message describing the nature of the incompatible types will
	 * automatically be generated and can be retrieved through {@link #getMessage()}
	 * 
	 * @param value the object which was being evaluated against a set of valid types
	 * @param validTypes the valid types against which the value was being evaluated
	 * but failed to be compatible with
	 */
	public IncompatibleTypeException(Object value, Class<?>... validTypes) {
		this(null, value, validTypes);
	}
	
	/**
	 * Constructs an IncompatibleTypeException with a message, a reference to the object
	 * being checked, and an array of valid Class objects which the type failed
	 * to match.
	 * 
	 * <p>The additional message will be prepended to the front of an automatically
	 * generated message describing the nature of the incompatible types and can be
	 * retrieved through {@link #getMessage()}.
	 * 
	 * @param additionalMessage the additional message to prepend to the generated exception detail message
	 * @param value the object which was being evaluated against a set of valid types
	 * @param validTypes the valid types against which the value was being evaluated
	 * but failed to be compatible with
	 */
	public IncompatibleTypeException(String additionalMessage, Object value, Class<?>... validTypes) {
		super(constructMessage(additionalMessage, value, validTypes));
		this.value = value;
		this.validTypes = validTypes;
	}

    /**
     * construct a message using the given values
     * @param additionalMessage the start of the constructed message
     * @param value the Object that wasn't validly typed
     * @param validTypes valid types the Type should have been one of.
     * @return the additionMessaage, valid types, and the class that did not have a valid type.
     */
	private static String constructMessage(String additionalMessage, Object value, Class<?>... validTypes) {
		StringBuilder message = new StringBuilder();
		if (additionalMessage != null) {
			message.append(additionalMessage);
		}
		if (message.length() > 0) {
			message.append(" -> ");
		}
		if (validTypes != null && validTypes.length > 0) {	
			message.append("Type should have been one of [");
			for (Class<?> validType : validTypes) {
				message.append(validType.getName()).append(", ");
			}
			// trim off the last two character to get rid of the last ", "
			message.delete(message.length() - 2, message.length());
			message.append("] but was ").append(value == null ? "null" : value.getClass().getName());
		} else {
			message.append("Type was ").append(value == null ? "null" : value.getClass().getName());	
		}
		return message.toString();
	}
	
	/**
	 * Returns the object which triggered the incompatible type exception.
	 * 
	 * @return the value passed to this exception upon construction
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Returns the array of Class objects which include the types against
	 * which the object value was deemed incompatible.
	 * 
	 * @return the valid types passed to this exception upon construction
	 */
	public Class<?>[] getValidTypes() {
		return validTypes;
	}
	
}
