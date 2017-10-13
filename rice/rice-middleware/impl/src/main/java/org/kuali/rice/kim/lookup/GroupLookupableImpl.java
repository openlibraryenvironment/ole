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
package org.kuali.rice.kim.lookup;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kns.lookup.KualiLookupableImpl;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.Properties;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupLookupableImpl extends KualiLookupableImpl {

    private static final long serialVersionUID = -7862240710174441633L;

    public String getCreateNewUrl() {
        String url = "";
        if((getLookupableHelperService()).allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_TYPE_NAME)){
            Properties parameters = new Properties();
	        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.DOC_HANDLER_METHOD);
	        parameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
	        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_TYPE_NAME);
	        if (StringUtils.isNotBlank(getReturnLocation())) {
	        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
	        	}
            url = getCreateNewUrl(UrlFactory.parameterizeUrl(
            		KimCommonUtilsInternal.getKimBasePath()+ KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_ACTION, parameters));
        }
        return url;
    }

}
