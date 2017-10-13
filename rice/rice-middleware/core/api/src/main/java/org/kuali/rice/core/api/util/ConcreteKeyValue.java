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
package org.kuali.rice.core.api.util;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

/**
 * A mutable, comparable key value pair of Strings.
 *  
 * This class is not meant to be extended.
 * 
 * For extension see AbstractKeyValue & KeyValue.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ConcreteKeyValue extends AbstractKeyValue implements Comparable<KeyValue> {
	private static final long serialVersionUID = 1176799455504861488L;

	public ConcreteKeyValue() {
		super();
	}

	public ConcreteKeyValue(String key, String value) {
		super(key, value);
	}

	public ConcreteKeyValue(KeyValue keyValue) {
		super(keyValue);
	}
	
	public ConcreteKeyValue(Map.Entry<String, String> entry) {
		super(entry);
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int compareTo(KeyValue o) {
		if (o == null) {
			throw new NullPointerException("o is null");
		}

		return new CompareToBuilder()
			.append(this.getValue(), o.getValue(), String.CASE_INSENSITIVE_ORDER)
			.append(this.getKey(), o.getKey(), String.CASE_INSENSITIVE_ORDER)
			.toComparison();
	}

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
