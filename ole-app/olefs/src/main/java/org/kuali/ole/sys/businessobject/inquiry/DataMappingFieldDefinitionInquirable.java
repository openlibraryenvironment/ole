/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.sys.businessobject.inquiry;

import java.util.Map;

import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.service.OleBusinessObjectMetaDataService;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.krad.bo.BusinessObject;

public class DataMappingFieldDefinitionInquirable extends KualiInquirableImpl {

    @Override
    public BusinessObject getBusinessObject(Map fieldValues) {
        return SpringContext.getBean(OleBusinessObjectMetaDataService.class).getDataMappingFieldDefinition((String) fieldValues.get(OLEPropertyConstants.COMPONENT_CLASS), (String) fieldValues.get(OLEPropertyConstants.PROPERTY_NAME));
    }
}
