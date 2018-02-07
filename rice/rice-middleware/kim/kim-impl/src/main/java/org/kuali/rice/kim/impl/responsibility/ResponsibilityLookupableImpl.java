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
package org.kuali.rice.kim.impl.responsibility;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kns.lookup.KualiLookupableImpl;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.Properties;

public class ResponsibilityLookupableImpl extends KualiLookupableImpl {

    @Override
    public String getCreateNewUrl() {
        String url = "";

        if (getLookupableHelperService().allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_REVIEW_RESPONSIBILITY_DOCUMENT_TYPE_NAME)) {
            Properties parameters = new Properties();
            parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.MAINTENANCE_NEW_METHOD_TO_CALL);
            parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, ReviewResponsibilityBo.class.getName());
            if (StringUtils.isNotBlank(getReturnLocation())) {
                parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
            }
            url = UrlFactory.parameterizeUrl(KRADConstants.MAINTENANCE_ACTION, parameters);
            url = "<a title=\"Create a new record\" href=\"" + url + "\"><img src=\"images/tinybutton-createnew.gif\" alt=\"create new\" width=\"70\" height=\"15\"/></a>";
        }

        return url;
    }
}
