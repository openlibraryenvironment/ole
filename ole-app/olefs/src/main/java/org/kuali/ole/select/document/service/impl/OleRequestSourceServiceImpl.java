/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.select.businessobject.OleRequestSourceType;
import org.kuali.ole.select.document.service.OleRequestSourceService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleRequestSourceServiceImpl implements OleRequestSourceService {

    public Integer getRequestSourceTypeId(String requestSourceType) throws Exception {
        Integer requestSourceTypeId = null;
        Map requestorSourceTypeMap = new HashMap();
        requestorSourceTypeMap.put("requestSourceType", requestSourceType);
        List<OleRequestSourceType> requestorSourceTypeList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleRequestSourceType.class, requestorSourceTypeMap);
        if (requestorSourceTypeList.iterator().hasNext()) {
            requestSourceTypeId = requestorSourceTypeList.iterator().next().getRequestSourceTypeId();
        }
        return requestSourceTypeId;
    }
}
