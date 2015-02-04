/*
 * Copyright 2008 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.validation.impl;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.PurapParameterConstants;
import org.kuali.ole.module.purap.PurapRuleConstants;
import org.kuali.ole.module.purap.document.RequisitionDocument;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.ole.sys.document.validation.event.AttributedDocumentEvent;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

public class RequisitionNewIndividualItemValidation extends PurchasingNewIndividualItemValidation {

    public boolean validate(AttributedDocumentEvent event) {

        return super.validate(event);
    }

    @Override
    protected boolean commodityCodeIsRequired() {
        //if the ENABLE_COMMODITY_CODE_IND parameter is  N then we don't
        //need to check for the ITEMS_REQUIRE_COMMODITY_CODE_IND parameter anymore, just return false. 
        boolean enableCommodityCode = SpringContext.getBean(ParameterService.class).getParameterValueAsBoolean(PurapConstants.PURAP_NAMESPACE, "Document", PurapParameterConstants.ENABLE_COMMODITY_CODE_IND);
        if (!enableCommodityCode) {
            return false;
        } else {
            return super.getParameterService().getParameterValueAsBoolean(RequisitionDocument.class, PurapRuleConstants.ITEMS_REQUIRE_COMMODITY_CODE_IND);
        }
    }

}
