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
package org.kuali.ole.gl.businessobject;

import java.util.Map;

import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils;
import org.kuali.rice.krad.bo.BusinessObject;

/**
 * This class has utility methods for parsing OriginEntries from Strings
 */
public class CollectorDetailFieldUtil extends BusinessObjectStringParserFieldUtils {

    /**
     * Returns the class to parse into - OriginEntryFull
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getBusinessObjectClass()
     */
    @Override
    public Class<? extends BusinessObject> getBusinessObjectClass() {
        return CollectorDetail.class;
    }

    /**
     * Returns the fields to be parsed from a String, in order, to form an OriginEntryFull
     * @see org.kuali.ole.sys.businessobject.BusinessObjectStringParserFieldUtils#getOrderedProperties()
     */
    @Override
    public String[] getOrderedProperties() {
        return new String[] {
                OLEPropertyConstants.UNIVERSITY_FISCAL_YEAR,
                OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE,
                OLEPropertyConstants.ACCOUNT_NUMBER,
                OLEPropertyConstants.SUB_ACCOUNT_NUMBER,
                OLEPropertyConstants.FINANCIAL_OBJECT_CODE,
                OLEPropertyConstants.FINANCIAL_SUB_OBJECT_CODE,
                OLEPropertyConstants.FINANCIAL_BALANCE_TYPE_CODE,
                OLEPropertyConstants.FINANCIAL_OBJECT_TYPE_CODE,
                OLEPropertyConstants.COLLECTOR_DETAIL_SEQUENCE_NUMBER,
                OLEPropertyConstants.FINANCIAL_DOCUMENT_TYPE_CODE,
                OLEPropertyConstants.FINANCIAL_SYSTEM_ORIGINATION_CODE,
                OLEPropertyConstants.DOCUMENT_NUMBER,
                OLEPropertyConstants.COLLECTOR_DETAIL_AMOUNT,
                OLEPropertyConstants.COLLECTOR_DETAIL_GL_CREDIT_CODE,
                OLEPropertyConstants.COLLECTOR_DETAIL_NOTE_TEXT
            };
    }

    /**
     *
     */

    public int getDetailLineTotalLength() {
        int totalLength = 0;
        Map<String, Integer> lengthMap = getFieldLengthMap();
        for (String property : getOrderedProperties()) {
            totalLength += lengthMap.get(property).intValue();
        }
        return totalLength;
    }
}
