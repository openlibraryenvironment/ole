/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kim.impl.type;

import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.krad.bo.BusinessObject;

import java.util.Collections;

public class KimTypeInquirableImpl extends KualiInquirableImpl {

	protected final String KIM_TYPE_NAME = "name";
	
    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
		if(KIM_TYPE_NAME.equals(attributeName)){
			return getInquiryUrlForPrimaryKeys(KimTypeBo.class, businessObject, Collections.singletonList(KimConstants.PrimaryKeyConstants.KIM_TYPE_ID), null);
		}
        return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
    }

}
