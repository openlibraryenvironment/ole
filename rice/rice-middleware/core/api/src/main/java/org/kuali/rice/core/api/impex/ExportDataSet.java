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
package org.kuali.rice.core.api.impex;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;


/**
 * A set of data to be exported.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExportDataSet {

	private final Map<QName, Object> dataSets = new HashMap<QName, Object>(); 
	
    public Map<QName, Object> getDataSets() {
        return dataSets;
    }

    public void addDataSet(QName name, Object dataSet) {
    	dataSets.put(name, dataSet);
    }
    
}