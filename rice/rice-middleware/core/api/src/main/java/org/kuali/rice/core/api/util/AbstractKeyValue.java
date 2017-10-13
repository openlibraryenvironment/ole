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

import java.util.Map;

/**
 * An abstract key value that can be extended by other classes (ex: DTOs).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class AbstractKeyValue implements KeyValue {
	private static final long serialVersionUID = -8093251322740055977L;
	
	protected String key;
	protected String value;
	
	public AbstractKeyValue() {
		super();
	}

	public AbstractKeyValue(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public AbstractKeyValue(KeyValue keyValue) {
		this.key = keyValue.getKey();
		this.value = keyValue.getValue();
	}
	
	public AbstractKeyValue(Map.Entry<String, String> entry) {
		this.key = entry.getKey();
		this.value = entry.getValue();
	}
	
	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}
}
