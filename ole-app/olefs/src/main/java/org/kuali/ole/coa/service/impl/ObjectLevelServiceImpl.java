/*
 * Copyright 2005-2006 The Kuali Foundation
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
package org.kuali.ole.coa.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.kuali.ole.coa.businessobject.ObjectLevel;
import org.kuali.ole.coa.service.ObjectLevelService;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.krad.service.BusinessObjectService;

/**
 * This service implementation is the default implementation of the ObjLevel service that is delivered with Kuali.
 */

@NonTransactional
public class ObjectLevelServiceImpl implements ObjectLevelService {

    /**
     * @see org.kuali.ole.coa.service.ObjectLevelService#getByPrimaryId(java.lang.String, java.lang.String)
     */
    public ObjectLevel getByPrimaryId(String chartOfAccountsCode, String objectLevelCode) {
        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartOfAccountsCode);
        keys.put(OLEPropertyConstants.FINANCIAL_OBJECT_LEVEL_CODE, objectLevelCode);
        return (ObjectLevel)SpringContext.getBean(BusinessObjectService.class).findByPrimaryKey(ObjectLevel.class, keys);
    }

}
