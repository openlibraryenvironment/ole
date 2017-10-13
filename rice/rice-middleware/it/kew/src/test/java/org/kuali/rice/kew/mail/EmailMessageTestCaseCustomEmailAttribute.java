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
package org.kuali.rice.kew.mail;

import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.document.Document;


/**
 * Test CustomEmailAttribute for EmailMessageTestCase
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EmailMessageTestCaseCustomEmailAttribute implements CustomEmailAttribute {

    private ActionRequest actionRequestVO;
    private Document routeHeaderVO;
    
    public ActionRequest getActionRequestVO() {
        return this.actionRequestVO;
    }

    public String getCustomEmailBody() throws Exception {
        return "CUSTOM EMAIL BODY";
    }

    public String getCustomEmailSubject() throws Exception {
        return "CUSTOM EMAIL SUBJECT";
    }

    public Document getRouteHeaderVO() {
        return this.routeHeaderVO;
    }

    public void setActionRequestVO(ActionRequest actionRequestVO) {
        ActionRequest.Builder builder = ActionRequest.Builder.create(actionRequestVO);
        builder.setId("-1");
        this.actionRequestVO = builder.build();
    }

    public void setRouteHeaderVO(Document routeHeaderVO) {
        this.routeHeaderVO = routeHeaderVO;
        Document.Builder builder = Document.Builder.create(routeHeaderVO);
        builder.setApplicationDocumentId("setByEmailComp");
        this.routeHeaderVO = builder.build();
    }
}
