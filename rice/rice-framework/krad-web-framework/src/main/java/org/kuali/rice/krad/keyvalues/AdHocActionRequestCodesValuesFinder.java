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
package org.kuali.rice.krad.keyvalues;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.DocumentFormBase;

import java.util.ArrayList;
import java.util.List;

public class AdHocActionRequestCodesValuesFinder extends UifKeyValuesFinderBase {
    private static final long serialVersionUID = 8457876358655872234L;

    /**
     * @see org.kuali.rice.krad.uif.control.UifKeyValuesFinder#getKeyValues(org.kuali.rice.krad.uif.view.ViewModel)
     */
    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        DocumentFormBase documentFormBase = (DocumentFormBase) model;
        Document document = documentFormBase.getDocument();
        List<KeyValue> keyValues = new ArrayList<KeyValue>();
        keyValues.add(new ConcreteKeyValue(KewApiConstants.ACTION_REQUEST_FYI_REQ, KewApiConstants.ACTION_REQUEST_FYI_REQ_LABEL));
        keyValues.add(new ConcreteKeyValue(KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ, KewApiConstants.ACTION_REQUEST_ACKNOWLEDGE_REQ_LABEL));
        keyValues.add(new ConcreteKeyValue(KewApiConstants.ACTION_REQUEST_APPROVE_REQ, KewApiConstants.ACTION_REQUEST_APPROVE_REQ_LABEL));

        if ((document.getDocumentHeader().getWorkflowDocument().isInitiated() || document.getDocumentHeader().getWorkflowDocument().isSaved())) {
            keyValues.add(new ConcreteKeyValue(KewApiConstants.ACTION_REQUEST_COMPLETE_REQ, KewApiConstants.ACTION_REQUEST_COMPLETE_REQ_LABEL));
        }
        return keyValues;
    }
}
