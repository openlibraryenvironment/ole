/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.select.util;

import org.kuali.ole.select.OleSelectConstant;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kew.api.exception.WorkflowException;


public class OleLicenseRequestView {

    public String getDocumentLabel() throws WorkflowException {
        return "License Request";
    }

    public String getUrl() {
        String url = SpringContext.getBean(ConfigurationService.class).getPropertyValueAsString(
                OleSelectConstant.RICE2_URL);
        url += "/kew/DocHandler.do?command=displayDocSearchView&docId=";
        return url;
    }

}
