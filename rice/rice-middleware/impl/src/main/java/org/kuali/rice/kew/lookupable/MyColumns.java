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
package org.kuali.rice.kew.lookupable;

import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.rule.bo.RuleBaseValuesLookupableImpl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A bean which wraps a List of {@link <KeyValue>} objects.
 * 
 * @see RuleBaseValuesLookupableImpl
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MyColumns implements Serializable {

	private static final long serialVersionUID = -4669528607040709102L;
	private List<KeyValue> columns;

    public MyColumns() {
        columns = new ArrayList<KeyValue>();
    }

    public List<KeyValue> getColumns() {
        return columns;
    }
    public void setColumns(List<KeyValue> columns) {
        this.columns = columns;
    }
}
