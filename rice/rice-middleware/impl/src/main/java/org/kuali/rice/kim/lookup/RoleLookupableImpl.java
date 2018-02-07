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
import org.kuali.rice.kim.impl.type.KimTypeBo;
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
public class RoleLookupableImpl extends KualiLookupableImpl {

    private static final long serialVersionUID = -543816772634893348L;

    @Override
    public String getCreateNewUrl() {
        String url = "";
        if((getLookupableHelperService()).allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_TYPE_NAME)){
            Properties parameters = new Properties();
            parameters.put(KRADConstants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, KimTypeBo.class.getName());
            //parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, KRADConstants.PORTAL_ACTION);
            parameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
            parameters.put(KRADConstants.DOC_FORM_KEY, KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_SHORT_KEY);
	        if (StringUtils.isNotBlank(getReturnLocation())) {
	        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
	        	}
            url = getCreateNewUrl(UrlFactory.parameterizeUrl(KRADConstants.LOOKUP_ACTION, parameters));
            //String url = "lookup.do?businessObjectClassName=org.kuali.rice.kim.bo.types.impl.KimTypeImpl&returnLocation=portal.do&docFormKey="+KimApiConstants.KimUIConstants.KIM_ROLE_DOCUMENT_SHORT_KEY;
        }
        return url;
    }

}
