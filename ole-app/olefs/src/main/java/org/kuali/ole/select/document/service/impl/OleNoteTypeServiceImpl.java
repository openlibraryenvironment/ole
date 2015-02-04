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
import org.kuali.ole.select.businessobject.OleNoteType;
import org.kuali.ole.select.document.service.OleNoteTypeService;
import org.kuali.rice.krad.service.BusinessObjectService;

import java.util.HashMap;
import java.util.Map;

public class OleNoteTypeServiceImpl implements OleNoteTypeService {

    private static Logger LOG = Logger.getLogger(OleRequestorServiceImpl.class);

    private BusinessObjectService businessObjectService;


    @Override
    public void saveNoteType(OleNoteType oleNotetype) {
        oleNotetype.setActive(true);
        businessObjectService.save(oleNotetype);
    }

    @Override
    public OleNoteType getNoteTypeDetails(Integer noteTypeId) {
        Map keys = new HashMap();
        keys.put("noteTypeId", noteTypeId);
        return (OleNoteType) businessObjectService.findByPrimaryKey(OleNoteType.class, keys);
    }


    public BusinessObjectService getBusinessObjectService() {
        return businessObjectService;
    }

    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

}
