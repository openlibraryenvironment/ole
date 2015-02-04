/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import org.kuali.ole.select.service.OleGenericService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleGenericServiceImpl implements OleGenericService {

    public Object getObject(String fieldName, String fieldValue, Class clas) {
        Map defaultMap = new HashMap();
        defaultMap.put(fieldName, fieldValue);
        List<Object> defaultList = new ArrayList((SpringContext.getBean(BusinessObjectService.class)).findMatching(clas, defaultMap));
        return defaultList.get(0);
    }

}

