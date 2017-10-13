/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.docsearch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.WorkflowDocumentFactory;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.framework.document.security.DocumentSecurityAttribute;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 * This is a test class to verify the operation of the custom security attributes
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class CustomSecurityFilterAttribute implements DocumentSecurityAttribute {

    private static final long serialVersionUID = -8487944372203594080L;

    public static final Map<String,String> VIEWERS_BY_STATUS = new HashMap<String,String>();
    static {
        VIEWERS_BY_STATUS.put(KewApiConstants.ROUTE_HEADER_SAVED_CD, "user3");
        VIEWERS_BY_STATUS.put(KewApiConstants.ROUTE_HEADER_ENROUTE_CD, "dewey");
        VIEWERS_BY_STATUS.put(KewApiConstants.ROUTE_HEADER_PROCESSED_CD, "jitrue");
        VIEWERS_BY_STATUS.put(KewApiConstants.ROUTE_HEADER_FINAL_CD, "user2");
    }

    @Override
    public boolean isAuthorizedForDocument(String principalId, Document document) {
        try {
            WorkflowDocument doc = WorkflowDocumentFactory.loadDocument(principalId, document.getDocumentId());
            String networkId = VIEWERS_BY_STATUS.get(doc.getStatus().getCode());
            String principalName = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(principalId).getPrincipalName();
            return StringUtils.isNotBlank(networkId) && networkId.equals(principalName);
        } catch (Exception e) {
            throw new RuntimeException("Unable to process custom security filter attribute", e);
        }
    }

}
