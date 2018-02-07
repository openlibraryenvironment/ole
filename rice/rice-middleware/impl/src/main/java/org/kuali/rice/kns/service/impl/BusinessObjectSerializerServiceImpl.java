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
package org.kuali.rice.kns.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.kns.datadictionary.MaintainableSectionDefinition;
import org.kuali.rice.kns.util.documentserlializer.MaintenanceDocumentPropertySerializibilityEvaluator;
import org.kuali.rice.krad.datadictionary.MaintenanceDocumentEntry;
import org.kuali.rice.krad.util.documentserializer.AlwaysTruePropertySerializibilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BusinessObjectSerializerServiceImpl extends org.kuali.rice.krad.service.impl.BusinessObjectSerializerServiceImpl {

    @Override
    public PropertySerializabilityEvaluator getPropertySerizabilityEvaluator(Object businessObject) {
        PropertySerializabilityEvaluator evaluator = null;

        String docTypeName = getDocumentDictionaryService().getMaintenanceDocumentTypeName(businessObject.getClass());
        MaintenanceDocumentEntry maintenanceDocumentEntry =
                getDocumentDictionaryService().getMaintenanceDocumentEntry(docTypeName);

        if (maintenanceDocumentEntry instanceof org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry) {
            List<MaintainableSectionDefinition> maintainableSectionDefinitions =
                    ((org.kuali.rice.kns.datadictionary.MaintenanceDocumentEntry) maintenanceDocumentEntry).getMaintainableSections();
            if (CollectionUtils.isEmpty(maintainableSectionDefinitions)) {
                evaluator = new AlwaysTruePropertySerializibilityEvaluator();
            } else {
                evaluator = new MaintenanceDocumentPropertySerializibilityEvaluator();
                evaluator.initializeEvaluatorForDataObject(businessObject);
            }
        }
        else {
           evaluator = new AlwaysTruePropertySerializibilityEvaluator();
        }

        return evaluator;
    }
}
