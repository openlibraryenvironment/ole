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

import org.apache.log4j.Logger;
import org.kuali.ole.select.businessobject.OleRequestor;
import org.kuali.ole.select.businessobject.OleRequestorType;
import org.kuali.ole.select.document.service.OleRequestorService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OleRequestorServiceImpl implements OleRequestorService {
    private static Logger LOG = Logger.getLogger(OleRequestorServiceImpl.class);

    private BusinessObjectService businessObjectService;


    /**
     * @see org.kuali.ole.select.document.service.OleRequestorService#saveRequestor(org.kuali.ole.select.businessobject.OleRequestor)
     */
    public void saveRequestor(OleRequestor oleRequestor) {
        oleRequestor.setActive(true);
        businessObjectService.save(oleRequestor);
    }

    /**
     * @see org.kuali.ole.select.document.service.OleRequestorService#saveRequestorType(org.kuali.ole.select.businessobject.OleRequestorType)
     */
    public void saveRequestorType(OleRequestorType oleRequestorType) {
        businessObjectService.save(oleRequestorType);
    }

    /**
     * @see org.kuali.ole.select.document.service.OleRequestorService#getRequestorDetails(String)
     */
    public OleRequestor getRequestorDetails(String requestorId) {
        Map keys = new HashMap();
        keys.put("requestorId", requestorId);
        return (OleRequestor) businessObjectService.findByPrimaryKey(OleRequestor.class, keys);
    }

    /**
     * @see org.kuali.ole.select.document.service.OleRequestorService#getRequestorTypeDetails(String)
     */
    public OleRequestorType getRequestorTypeDetails(String requestorType) {
        Map keys = new HashMap();
        keys.put("requestorType", requestorType);
        return (OleRequestorType) businessObjectService.findByPrimaryKey(OleRequestorType.class, keys);
    }

    /**
     * @see org.kuali.ole.select.document.service.OleRequestorServicee#getRequestorTypeList(String)
     */
    public List<OleRequestorType> getRequestorTypeList(String requestorType) {
        if (LOG.isDebugEnabled())
            LOG.debug("Entering getRequestorTypeList for requestorType:" + requestorType);
        Map criteria = new HashMap();
        criteria.put("requestorType", requestorType);
        List<OleRequestorType> requestorTypeList = (List) businessObjectService.findMatching(OleRequestorType.class, criteria);
        LOG.debug("Exiting getRequestorTypeList.");
        return requestorTypeList;
    }

    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }
}
