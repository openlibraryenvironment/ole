/*
 * Copyright 2009 The Kuali Foundation.
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
package org.kuali.ole.sec.businessobject.lookup;

import java.util.List;
import java.util.Map;

import org.kuali.ole.coa.businessobject.lookup.KualiAccountLookupableHelperServiceImpl;
import org.kuali.ole.sec.SecKeyConstants;
import org.kuali.ole.sec.service.AccessSecurityService;
import org.kuali.ole.sec.util.SecUtil;
import org.kuali.rice.krad.util.GlobalVariables;


/**
 * Lookupable helper that provides Access Security integration
 */
public class AccessSecurityAccountLookupableHelperServiceImpl extends KualiAccountLookupableHelperServiceImpl {
    protected AccessSecurityService accessSecurityService;

    /**
     * Gets search results and passes to access security service to apply access restrictions
     * 
     * @see org.kuali.rice.kns.lookup.LookupableHelperService#getSearchResults(java.util.Map)
     */
    public List getSearchResults(Map<String, String> fieldValues) {
        List results = super.getSearchResults(fieldValues);

        int resultSizeBeforeRestrictions = results.size();
        accessSecurityService.applySecurityRestrictionsForLookup(results, GlobalVariables.getUserSession().getPerson());

        accessSecurityService.compareListSizeAndAddMessageIfChanged(resultSizeBeforeRestrictions, results, SecKeyConstants.MESSAGE_LOOKUP_RESULTS_RESTRICTED);

        return results;
    }

    /**
     * Gets search results and passes to access security service to apply access restrictions
     * 
     * @see org.kuali.rice.kns.lookup.LookupableHelperService#getSearchResultsUnbounded(java.util.Map)
     */
    public List getSearchResultsUnbounded(Map<String, String> fieldValues) {
        List results = super.getSearchResultsUnbounded(fieldValues);

        int resultSizeBeforeRestrictions = results.size();
        accessSecurityService.applySecurityRestrictionsForLookup(results, GlobalVariables.getUserSession().getPerson());
        
        accessSecurityService.compareListSizeAndAddMessageIfChanged(resultSizeBeforeRestrictions, results, SecKeyConstants.MESSAGE_LOOKUP_RESULTS_RESTRICTED);

        return results;
    }

    /**
     * Sets the accessSecurityService attribute value.
     * 
     * @param accessSecurityService The accessSecurityService to set.
     */
    public void setAccessSecurityService(AccessSecurityService accessSecurityService) {
        this.accessSecurityService = accessSecurityService;
    }

}
