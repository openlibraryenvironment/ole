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
package org.kuali.rice.kew.impl.document.search;

import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A values finder implementation that loads the various workflow document status categories and their individual
 * statuses.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentStatusValuesFinder extends KeyValuesBase {

    private static final String CATEGORY_CODE_PREFIX = "category:";

    @Override
    public List<KeyValue> getKeyValues() {
        List<KeyValue> statuses = new ArrayList<KeyValue>();
        addCategory(statuses, DocumentStatusCategory.PENDING);
        addCategory(statuses, DocumentStatusCategory.SUCCESSFUL);
        addCategory(statuses, DocumentStatusCategory.UNSUCCESSFUL);
        return statuses;
    }

    private void addCategory(List<KeyValue> statuses, DocumentStatusCategory category) {
        statuses.add(new ConcreteKeyValue(CATEGORY_CODE_PREFIX + category.getCode(), category.getLabel() + " Statuses"));
        Set<DocumentStatus> documentStatuses = DocumentStatus.getStatusesForCategory(category);
        for (DocumentStatus documentStatus : documentStatuses) {
            statuses.add(new ConcreteKeyValue(documentStatus.getCode(), "- " + documentStatus.getLabel()));
        }
    }
}
