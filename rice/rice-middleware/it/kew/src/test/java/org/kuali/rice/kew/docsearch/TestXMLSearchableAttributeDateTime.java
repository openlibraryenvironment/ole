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

import org.joda.time.DateTime;
import org.kuali.rice.core.api.uif.AttributeLookupSettings;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableAttributeLookupSettings;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestXMLSearchableAttributeDateTime implements SearchableAttribute {

    private static final long serialVersionUID = 1479059967548234181L;

    public static final String SEARCH_STORAGE_KEY = "testDateTimeKey";
    public static final Long SEARCH_STORAGE_VALUE_IN_MILLS = (new Long("1173995646535"));
    public static final DateTime SEARCH_STORAGE_VALUE = new DateTime(SEARCH_STORAGE_VALUE_IN_MILLS.longValue());

    @Override
    public String generateSearchContent(ExtensionDefinition extensionDefinition,
            String documentTypeName,
            WorkflowAttributeDefinition attributeDefinition) {
        return "TestXMLSearchableAttributeDateTime";
    }

    @Override
    public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition,
            DocumentWithContent documentWithContent) {
        List<DocumentAttribute> savs = new ArrayList<DocumentAttribute>();
        savs.add(DocumentAttributeFactory.createDateTimeAttribute(SEARCH_STORAGE_KEY, SEARCH_STORAGE_VALUE));
        return savs;
    }

    @Override
    public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition,
            String documentTypeName) {
        List<RemotableAttributeField> fields = new ArrayList<RemotableAttributeField>();
        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(SEARCH_STORAGE_KEY);
        builder.setLongLabel("title");
        builder.setDataType(DataType.DATE);

        RemotableAttributeLookupSettings.Builder settings = RemotableAttributeLookupSettings.Builder.create();
        settings.setRanged(true);
        settings.setUpperBoundInclusive(false);

        // test result table column visibility
        // see: DocumentSearchResultAttributeVisibilityTest
        settings.setInResults(false);

        builder.setAttributeLookupSettings(settings);
		fields.add(builder.build());
        return fields;
    }

    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition,
            DocumentSearchCriteria documentSearchCriteria) {
        return Collections.emptyList();
    }

}
