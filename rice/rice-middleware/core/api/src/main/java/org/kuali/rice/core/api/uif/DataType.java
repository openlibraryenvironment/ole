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
package org.kuali.rice.core.api.uif;

import org.kuali.rice.core.api.util.jaxb.EnumStringAdapter;
import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.util.Date;

/**
 * A enum that defines the type of an attribute definition.  Some of these enum values may not be supported in certain contexts.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public enum DataType {
	STRING(String.class), MARKUP(String.class), DATE(Date.class), TRUNCATED_DATE(Date.class),
    BOOLEAN(Boolean.class), INTEGER(Integer.class), FLOAT(Float.class), DOUBLE(Double.class),
    LONG(Long.class),DATETIME(Date.class),CURRENCY(KualiDecimal.class);
	
	private final Class<?> type;
	
	private DataType(Class<?> type) {
		this.type = type;
	}
	
	public Class<?> getType() {
		return type;
	}

    public static final class Adapter extends EnumStringAdapter<DataType> {

        @Override
		protected Class<DataType> getEnumClass() {
			return DataType.class;
		}
	}
}
