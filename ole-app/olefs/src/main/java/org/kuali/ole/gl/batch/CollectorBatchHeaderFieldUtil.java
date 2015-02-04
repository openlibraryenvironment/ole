/*
 * Copyright 2007-2009 The Kuali Foundation
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
package org.kuali.ole.gl.batch;

import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils;
import org.kuali.rice.krad.bo.BusinessObject;

/**
 * This class has utility methods for parsing CollectorBatch header from Strings
 */
public class CollectorBatchHeaderFieldUtil extends BusinessObjectStringParserFieldUtils {

    /**
     * Returns the class to parse into - CollectorBatch
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getBusinessObjectClass()
     */
    @Override
    public Class<? extends BusinessObject> getBusinessObjectClass() {
        return CollectorBatch.class;
    }

    /**
     * Returns the fields to be parsed from a String, in order, to form a CollectorBatch
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getOrderedProperties()
     */
    @Override
    public String[] getOrderedProperties() {
        return new String[] {
                OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR,
                OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE,
                OLEPropertyConstants.ORGANIZATION_CODE,
                OLEPropertyConstants.TRANSMISSION_DATE,
                OLEPropertyConstants.COLLECTOR_BATCH_RECORD_TYPE,
                OLEPropertyConstants.BATCH_SEQUENCE_NUMBER,
                OLEPropertyConstants.KUALI_USER_PERSON_EMAIL_ADDRESS,
                OLEPropertyConstants.COLLECTOR_BATCH_PERSON_USER_ID,
                OLEPropertyConstants.DEPARTMENT_NAME,
                OLEPropertyConstants.MAILING_ADDRESS,
                OLEPropertyConstants.CAMPUS_CODE,
                OLEPropertyConstants.PHONE_NUMBER
        };
    }
}
