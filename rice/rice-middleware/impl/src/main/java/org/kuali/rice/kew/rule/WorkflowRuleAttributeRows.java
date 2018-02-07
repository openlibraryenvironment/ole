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
package org.kuali.rice.kew.rule;

import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeFields;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Row;

import java.util.List;
import java.util.Map;

/**
 * This class wraps a {@link WorkflowRuleAttributeFields} object and provides a KNS-compatible view to the data
 * contained therein. Primarily, this means that RemotableAttributeField objects are transformed to KNS Row objects.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowRuleAttributeRows {

    private final WorkflowRuleAttributeFields fields;
    private final List<Row> rows;

    public WorkflowRuleAttributeRows(WorkflowRuleAttributeFields fields) {
        this.fields = fields;
        this.rows = convertToRows(fields.getAttributeFields());
    }

    private static List<Row> convertToRows(List<RemotableAttributeField> attributeFields) {
        return FieldUtils.convertRemotableAttributeFields(attributeFields);
    }

    public List<Row> getRows() {
        return rows;
    }

    public List<RemotableAttributeError> getValidationErrors() {
        return fields.getValidationErrors();
    }

    public Map<String, String> getRuleExtensionValues() {
        return fields.getRuleExtensionValues();
    }

}
