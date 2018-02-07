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
package org.kuali.rice.kew.impl.rule.attribute;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeFields;
import org.kuali.rice.kew.framework.rule.attribute.WorkflowRuleAttributeHandlerService;
import org.kuali.rice.kew.rule.RoleAttribute;
import org.kuali.rice.kew.rule.RuleExtensionValue;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.WorkflowRuleSearchAttribute;
import org.kuali.rice.kew.rule.XmlConfiguredAttribute;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kns.util.FieldUtils;
import org.kuali.rice.kns.web.ui.Row;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reference implementation of {@code WorkflowRuleAttributeHandlerService}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class WorkflowRuleAttributeHandlerServiceImpl implements WorkflowRuleAttributeHandlerService {

    private ExtensionRepositoryService extensionRepositoryService;

    @Override
    public WorkflowRuleAttributeFields getRuleFields(Map<String, String> parameters,
                                                     ExtensionDefinition extensionDefinition,
                                                     boolean required)
            throws RiceIllegalArgumentException {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        WorkflowRuleAttribute attribute = loadAttribute(extensionDefinition);
        attribute.setRequired(required);
        List<RemotableAttributeError> errors = attribute.validateRuleData(parameters);
        List<RemotableAttributeField> fields = FieldUtils.convertRowsToAttributeFields(attribute.getRuleRows());
        List<RuleExtensionValue> ruleExtensionValues = attribute.getRuleExtensionValues();
        Map<String, String> ruleExtensionValuesMap = new HashMap<String, String>();
        for (RuleExtensionValue ruleExtensionValue : ruleExtensionValues) {
            ruleExtensionValuesMap.put(ruleExtensionValue.getKey(), ruleExtensionValue.getValue());
        }
        return WorkflowRuleAttributeFields.create(errors, fields, ruleExtensionValuesMap);
    }

    @Override
    public WorkflowRuleAttributeFields getSearchFields(Map<String, String> parameters,
                                                       ExtensionDefinition extensionDefinition,
                                                       boolean required)
            throws RiceIllegalArgumentException {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        WorkflowRuleAttribute attribute = loadAttribute(extensionDefinition);
        attribute.setRequired(required);
        List<RemotableAttributeError> errors;
        List<Row> searchRows;
        if (attribute instanceof WorkflowRuleSearchAttribute) {
            errors = ((WorkflowRuleSearchAttribute)attribute).validateSearchData(parameters);
            searchRows = ((WorkflowRuleSearchAttribute)attribute).getSearchRows();
        } else {
            errors = attribute.validateRuleData(parameters);
            searchRows = attribute.getRuleRows();
        }
        List<RemotableAttributeField> fields = FieldUtils.convertRowsToAttributeFields(searchRows);
        List<RuleExtensionValue> ruleExtensionValues = attribute.getRuleExtensionValues();
        Map<String, String> ruleExtensionValuesMap = new HashMap<String, String>();
        for (RuleExtensionValue ruleExtensionValue : ruleExtensionValues) {
            ruleExtensionValuesMap.put(ruleExtensionValue.getKey(), ruleExtensionValue.getValue());
        }
        return WorkflowRuleAttributeFields.create(errors, fields, ruleExtensionValuesMap);
    }

    @Override
    public WorkflowRuleAttributeFields getRoutingDataFields( Map<String, String> parameters,
                                                    ExtensionDefinition extensionDefinition,
                                                    boolean required)
          throws RiceIllegalArgumentException {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        WorkflowRuleAttribute attribute = loadAttribute(extensionDefinition);
        attribute.setRequired(required);
        List<RemotableAttributeError> allErrors = attribute.validateRoutingData(parameters);
        List<RemotableAttributeError> ruleDataErrors = attribute.validateRuleData(parameters);
        for (RemotableAttributeError error : ruleDataErrors) {
            allErrors.add(error);
        }
        List<RemotableAttributeField> fields = FieldUtils.convertRowsToAttributeFields(attribute.getRoutingDataRows());
        List<RuleExtensionValue> ruleExtensionValues = attribute.getRuleExtensionValues();
        Map<String, String> ruleExtensionValuesMap = new HashMap<String, String>();
        for (RuleExtensionValue ruleExtensionValue : ruleExtensionValues) {
            ruleExtensionValuesMap.put(ruleExtensionValue.getKey(), ruleExtensionValue.getValue());
        }
        return WorkflowRuleAttributeFields.create(allErrors, fields, ruleExtensionValuesMap);
    }
    
    @Override
    public List<RoleName> getRoleNames(ExtensionDefinition extensionDefinition) {
        WorkflowRuleAttribute attribute = loadAttribute(extensionDefinition);
        if (attribute instanceof RoleAttribute) {
            RoleAttribute roleAttribute = (RoleAttribute) attribute;
            List<RoleName> roleNames = roleAttribute.getRoleNames();
            if (CollectionUtils.isNotEmpty(roleNames)) {
                return ModelObjectUtils.createImmutableCopy(roleNames);
            }
      	}
        return Collections.emptyList();
    }

    private WorkflowRuleAttribute loadAttribute(ExtensionDefinition extensionDefinition) {
        if (extensionDefinition == null) {
            throw new RiceIllegalArgumentException("extensionDefinition was null or blank");
        }
        Object attribute = ExtensionUtils.loadExtension(extensionDefinition);
        if (attribute == null) {
            throw new RiceIllegalArgumentException("Failed to load WorkflowRuleAttribute for: " + extensionDefinition);
        }
        if (!WorkflowRuleAttribute.class.isAssignableFrom(attribute.getClass())) {
            throw new RiceIllegalArgumentException("Failed to locate a WorkflowRuleAttribute with the given name: " + extensionDefinition.getName());
        }
        if (attribute instanceof XmlConfiguredAttribute) {
            ((XmlConfiguredAttribute) attribute).setExtensionDefinition(extensionDefinition);
        }
        return (WorkflowRuleAttribute) attribute;
    }


    protected ExtensionRepositoryService getExtensionRepositoryService() {
        return extensionRepositoryService;
    }

    public void setExtensionRepositoryService(ExtensionRepositoryService extensionRepositoryService) {
        this.extensionRepositoryService = extensionRepositoryService;
    }
}
