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

import org.kuali.ole.select.businessobject.OleLicensingRequirement;
import org.kuali.ole.select.document.service.OleLicensingRequirementService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleLicensingRequirementServiceImpl implements OleLicensingRequirementService {

    @Override
    public String getLicensingRequirement(String licensingRequirementCode) throws Exception {
        String licensingRequirementDesc = null;
        Map licensingRequirementMap = new HashMap();
        licensingRequirementMap.put("licensingRequirementCode", licensingRequirementCode);
        List<OleLicensingRequirement> licensingRequirementList = (List) SpringContext.getBean(BusinessObjectService.class).findMatching(OleLicensingRequirement.class, licensingRequirementMap);
        if (licensingRequirementList.iterator().hasNext()) {
            licensingRequirementDesc = licensingRequirementList.iterator().next().getLicensingRequirementDesc();
        }
        return licensingRequirementDesc;
    }
}
