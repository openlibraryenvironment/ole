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
package org.kuali.rice.krad.util.documentserializer;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.datadictionary.WorkflowProperties;
import org.kuali.rice.krad.datadictionary.WorkflowProperty;
import org.kuali.rice.krad.datadictionary.WorkflowPropertyGroup;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.List;

/**
 * This implementation of {@link PropertySerializabilityEvaluator} uses the &lt;workflowProperties&gt; defined within the data dictionary
 * for a document.  If the property being serialized corresponds to one of the properties in the data dictionary, then it will be serialized.
 * If a property specified in the data dictionary corresponds to a business object, then all primitives will be serialized of the business object.
 * All primitives of a primitive that has already been serialized will be serialized as well.   If a property specified in the data dictionary corresponds
 * to a collection, then all primitives of all collection elements will be serialized.
 *
 */
public class BusinessObjectPropertySerializibilityEvaluator extends PropertySerializabilityEvaluatorBase implements PropertySerializabilityEvaluator {

    /**
     * Reads the data dictionary to determine which properties of the document should be serialized.
     *
     * @see org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator#initializeEvaluator(org.kuali.rice.krad.document.Document)
     */
	@Override
    public void initializeEvaluatorForDocument(Document document) {
        DataDictionary dictionary = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary();
        DocumentEntry docEntry = dictionary.getDocumentEntry(document.getDocumentHeader().getWorkflowDocument().getDocumentTypeName());
        WorkflowProperties workflowProperties = docEntry.getWorkflowProperties();
        List<WorkflowPropertyGroup> groups = workflowProperties.getWorkflowPropertyGroups();

        serializableProperties = new PropertySerializerTrie();

        for (WorkflowPropertyGroup group : groups) {
            // the basepath of each workflow property group is serializable
            if (StringUtils.isEmpty(group.getBasePath())) {
                // automatically serialize all primitives of document when the base path is null or empty string
                serializableProperties.addSerializablePropertyName(document.getBasePathToDocumentDuringSerialization(), false);
            }
            else {
               serializableProperties.addSerializablePropertyName(group.getBasePath(), false);
            }

            for (WorkflowProperty property : group.getWorkflowProperties()) {
                String fullPath;
                if (StringUtils.isEmpty(group.getBasePath())) {
                    fullPath = document.getBasePathToDocumentDuringSerialization() + "." + property.getPath();
                }
                else {
                    fullPath = group.getBasePath() + "." + property.getPath();
                }
                serializableProperties.addSerializablePropertyName(fullPath, false);
            }
        }
    }

}
