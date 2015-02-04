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

import org.kuali.ole.coa.document.validation.impl.MaintenancePreRulesBase;
import org.kuali.ole.module.purap.document.ReceivingDocument;
import org.kuali.ole.module.purap.document.service.ReceivingService;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.krad.document.Document;

public class LineItemReceivingDocumentPreRule extends MaintenancePreRulesBase {

    @Override
    public boolean doPrompts(Document document) {
        SpringContext.getBean(ReceivingService.class).createNoteForReturnedAndDamagedItems((ReceivingDocument) document);
        return true;
    }

}
