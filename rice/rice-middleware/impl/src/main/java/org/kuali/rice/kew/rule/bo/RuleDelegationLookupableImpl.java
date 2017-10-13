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
package org.kuali.rice.kew.rule.bo;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.kns.lookup.KualiLookupableImpl;

/**
 * An implementation of KualiLookupableImpl for RuleBaseValues
 * so that we can override the create url.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleDelegationLookupableImpl extends KualiLookupableImpl {

	@Override
	public String getCreateNewUrl() {
        String url = "";
        if (getLookupableHelperService().allowsMaintenanceNewOrCopyAction()) {
        	String kewBaseUrl = ConfigContext.getCurrentContextConfig().getKEWBaseURL();
            url = "<a title=\"Create a new record\" href=\""+kewBaseUrl+"/DelegateRule.do\"><img src=\"images/tinybutton-createnew.gif\" alt=\"create new\" width=\"70\" height=\"15\"/></a>";
        }
        return url;
    }
	
}
