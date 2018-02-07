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
package org.kuali.rice.edl.impl.lookupable;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.edl.impl.UserAction;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * This is a description of what this class does - sp20369 don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */

public class EDocLiteLookupableHelperServiceImpl  extends KualiLookupableHelperServiceImpl { //KualiLookupableHelperServiceImpl {

    private static final long serialVersionUID = 3157354920258155881L;

	/**
     * @returns links to "Create Document" action for the current edoclite
     */
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject businessObject, List pkNames) {
		List<HtmlData> anchorHtmlDataList = new ArrayList<HtmlData>();
    	anchorHtmlDataList.add(getCreateDocumentUrl((EDocLiteAssociation) businessObject));	
    	return anchorHtmlDataList;
	}
	
    protected HtmlData getCreateDocumentUrl(EDocLiteAssociation edlAssociation) {
    	String href = "";

        Properties parameters = new Properties();
        parameters.put("userAction", UserAction.ACTION_CREATE);
        parameters.put("edlName", edlAssociation.getEdlName());
        href = UrlFactory.parameterizeUrl(
        		ConfigContext.getCurrentContextConfig().getKEWBaseURL()+"/EDocLite", 
        		parameters);
        
        HtmlData.AnchorHtmlData anchorHtmlData = new HtmlData.AnchorHtmlData(href, null, "Create Document");
        return anchorHtmlData;
    }

	/**
	 * Since we don't have a maintenance document for EDocLiteAssociations, we need to
	 * set showMaintenanceLinks to true manually.  Otherwise our "Create Document" link
	 * won't show up.
	 */
	@Override
	public Collection performLookup(LookupForm lookupForm,
			Collection resultTable, boolean bounded) {
		lookupForm.setShowMaintenanceLinks(true);
		return super.performLookup(lookupForm, resultTable, bounded);
	}

}
