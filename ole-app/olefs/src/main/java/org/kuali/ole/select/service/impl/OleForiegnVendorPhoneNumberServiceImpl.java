/*
 * Copyright 2011 The Kuali Foundation.
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

import org.kuali.ole.select.service.OleForiegnVendorPhoneNumberService;
import org.kuali.ole.vnd.VendorParameterConstants;
import org.kuali.ole.vnd.businessobject.VendorDetail;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OleForiegnVendorPhoneNumberServiceImpl implements OleForiegnVendorPhoneNumberService {

    public ParameterService parameterService;
    public List<String> phoneNumberFormats;

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    @Override
    public boolean isValidForiegnVendorPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        String[] formats = parseFormats();
        for (int i = 0; i < formats.length; i++) {
            if (phoneNumber.matches(formats[i])) {
                return true;
            }
        }
        return false;
    }

    protected String[] parseFormats() {
        if (ObjectUtils.isNull(phoneNumberFormats)) {
            phoneNumberFormats = new ArrayList<String>(parameterService.getParameterValuesAsString(VendorDetail.class, VendorParameterConstants.FOREIGN_VENDOR_PHONE_NUMBER_FORMATS));
        }
        return phoneNumberFormats.toArray(new String[]{});
    }

}
