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
package org.kuali.rice.core.api.util.jaxb;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.mo.common.Coded;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * An abstract base class for use when marshaling enumeration values to and from XML.
 * This allows these values to be handled as plain strings (as opposed to xs:enumeration)
 * in the generated schema definitions.  This improves compatibility by allows for new
 * enumeration values to be added without breaking the schema.
 * 
 * <p>Subclasses need to indicate the concrete type of the Enum being adapted using the
 * {@link #getEnumClass()} method.
 * 
 * <p>If the enum implements the {@link Coded} interface, then the actual string value
 * that is produced by the adapter will be the result of the {@link Coded#getCode()}
 * method.  Otherwise the {@link Enum#name()} value is used.
 * 
 * <p>In situations where a string value is being requested to be unmarshalled to an
 * enum and the enum does not understand the value, then this adapter will unmarshal
 * the value to {@code null}.  This could happen in situations where a newer version
 * of an endpoint (which has added additional items to the enumeration) sends a message
 * to an older version client of the service.  In these cases, that older client will
 * not be aware of the new enumeration values and thus cannot effectively translate them.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public abstract class EnumStringAdapter<T extends Enum<T>> extends XmlAdapter<String, String> {	
		
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EnumStringAdapter.class);
	
	@Override
	public String marshal(String value) throws Exception {
		return processValue(value, false);
	}

	@Override
	public String unmarshal(String value) throws Exception {
		return processValue(value, true);
	}
	
	private String processValue(String value, boolean unmarshal) throws Exception {
		if (value == null) {
			return null;
		}
		Class<T> enumClass = getEnumClass();
		if (Coded.class.isAssignableFrom(enumClass)) {
			T[] enumConstants = enumClass.getEnumConstants();
			for (T enumConstant : enumConstants) {
				Coded codedEnumConstant = (Coded)enumConstant;
				if (codedEnumConstant.getCode().equals(value)) {
					// value is good, return value
					return value;
				}
			}
		} else {
			try {
				Enum.valueOf(enumClass, value);
				return value;
			} catch (IllegalArgumentException e) {
				// failed to unmarshal, will fall through to null return below...
			}
		}
		if (unmarshal) {
			LOG.warn("Failed to unmarshal enumeration value '" + value + "' for enum type: " + enumClass.getName());
			return null;
		} else {
			throw new RiceIllegalArgumentException("Failed to marshal enumeration value '" + value + "' for enum type: " + getEnumClass().getName());
		}
	}
	
	protected abstract Class<T> getEnumClass();
	
}
