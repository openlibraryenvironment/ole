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
package org.kuali.rice.kew.web;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;


/**
 * A simple bean for storing key/value pairs that can be used for a number of
 * tasks. Right now it is used to hold information that will be display on a jsp
 * for drop down boxes.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KeyValueSort implements KeyValue {

	private static final long serialVersionUID = 3575440091286391804L;

	private String userDisplayValue;
	private Object sortValue;
    private Class sortClass;
    private SearchableAttributeValue searchableAttributeValue;
    private final ConcreteKeyValue keyValue;
    
    
	public KeyValueSort() {
		keyValue = new ConcreteKeyValue();
	}

	public KeyValueSort(String key, String value) {
		keyValue = new ConcreteKeyValue(key, value);
	}

    public KeyValueSort(String key, String value, Object sortValue, SearchableAttributeValue searchableAttributeValue) {
        this(key,value);
        this.sortValue = sortValue;
        this.searchableAttributeValue = searchableAttributeValue;
    }

    public KeyValueSort(String key, String value, String userDisplayValue, Object sortValue, SearchableAttributeValue searchableAttributeValue) {
    	this(key,value,sortValue,searchableAttributeValue);
        this.userDisplayValue = userDisplayValue;
    }

    public KeyValueSort(KeyValueSort kvs) {
        this(kvs.getKey(),kvs.getValue(),kvs.getUserDisplayValue(),kvs.getSortValue(),kvs.getSearchableAttributeValue());
    }

	public Object getSortValue() {
		return sortValue;
	}

	public void setSortValue(Object sortValue) {
		this.sortValue = sortValue;
        this.sortClass = sortValue.getClass();
	}

    public Class getSortClass() {
        return sortClass;
    }

    public SearchableAttributeValue getSearchableAttributeValue() {
        return searchableAttributeValue;
    }
    
    public String getUserDisplayValue() {
    	if (StringUtils.isNotBlank(userDisplayValue)) {
    		return userDisplayValue;
    	}
    	return getValue();
    }

	@Override
	public String getKey() {
		return keyValue.getKey();
	}

	@Override
	public String getValue() {
		return keyValue.getValue();
	}

	public void setKey(String k) {
		keyValue.setKey(k);
	}

	public void setValue(String v) {
		keyValue.setValue(v);
	}

}
