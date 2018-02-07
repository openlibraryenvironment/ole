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
package org.kuali.rice.core.api.util.io;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * {@code SerializationUtils} is a set of utilities to add in the serialization
 * of java objects.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class SerializationUtils {

	/**
	 * Serializes the given {@link Serializable} object and then executes a
	 * Base 64 encoding on it, returning the encoded value as a String.
	 * 
	 * @param object the object to serialize, cannot be null
	 * @return a base 64-encoded representation of the serialized object
	 * 
	 * @throws IllegalArgumentException if the given object is null
	 * @throws SerializationException if the serialization fails
	 */
	public static String serializeToBase64(Serializable object) {
		if (object == null) {
			throw new IllegalArgumentException("Cannot serialize a null object");
		}
		byte[] serializedBytes = org.apache.commons.lang.SerializationUtils.serialize(object);
		return new Base64().encodeAsString(serializedBytes);
    }
	
	/**
	 * Deserializes the given base 64-encoded string value to it's Serializable
	 * object representation.
	 * 
	 * @param base64Value the base 64-encoded value to deserialize, must not be null or a blank string
	 * @return the deserialized object
	 * 
	 * @throws IllegalArgumentException if the given value is is null or blank
	 * @throws SerializationException if the deserialization fails
	 */
	public static Serializable deserializeFromBase64(String base64Value) {
		if (StringUtils.isBlank(base64Value)) {
			throw new IllegalArgumentException("Cannot deserialize a null or blank base64 string value.");
		}
        byte[] decoded = new Base64().decode(base64Value);
		return (Serializable)org.apache.commons.lang.SerializationUtils.deserialize(decoded);
	}
	
	private SerializationUtils() {
		throw new UnsupportedOperationException("Should never be invoked.");
	}
	
}
