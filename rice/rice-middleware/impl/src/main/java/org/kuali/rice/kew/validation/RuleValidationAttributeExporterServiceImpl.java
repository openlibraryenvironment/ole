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
package org.kuali.rice.kew.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.extension.ExtensionRepositoryService;
import org.kuali.rice.kew.api.extension.ExtensionUtils;
import org.kuali.rice.kew.api.validation.RuleValidationContext;
import org.kuali.rice.kew.api.validation.ValidationResults;
import org.kuali.rice.kew.framework.validation.RuleValidationAttributeExporterService;
import org.kuali.rice.kew.rule.RuleValidationAttribute;
import org.springframework.beans.factory.annotation.Required;

/**
 * RuleValidationAttributeExporterService reference impl.  Delegates to the ExtensionRepositoryService
 * to load the custom RuleValidationAttribute.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RuleValidationAttributeExporterServiceImpl implements RuleValidationAttributeExporterService {
    private static final Logger LOG = Logger.getLogger(RuleValidationAttributeExporterServiceImpl.class);

    private ExtensionRepositoryService extensionRepositoryService;

    @Required
    public void setExtensionRepositoryService(ExtensionRepositoryService extensionRepositoryService) {
        this.extensionRepositoryService = extensionRepositoryService;
    }
    private ExtensionRepositoryService getExtensionRepositoryService() {
        return this.extensionRepositoryService;
    }

    @Override
    public ValidationResults validate(String attributeName, RuleValidationContext validationContext) {
        if (StringUtils.isBlank(attributeName)) {
            throw new RiceIllegalArgumentException("attribute name was null or blank");
        }
        RuleValidationAttribute attr = loadAttribute(attributeName);
        return attr.validate(validationContext);
    }

    /**
     * Loads RuleValidationAttribute implementation class via {@link ExtensionRepositoryService}
     * @param attributeName the RuleValidationAttribute name
     * @return instance of the RuleValidationAttribute implementation class
     * @throws RiceIllegalArgumentException if specified attribute name cannot be found or loaded
     */
    protected RuleValidationAttribute loadAttribute(String attributeName) {
        ExtensionDefinition extensionDefinition = getExtensionRepositoryService().getExtensionByName(attributeName);
        if (extensionDefinition == null) {
            throw new RiceIllegalArgumentException("Failed to locate a RuleValidationAttribute with the given name: " + attributeName);
        }
        RuleValidationAttribute attribute = ExtensionUtils.loadExtension(extensionDefinition);
        if (attribute == null) {
            throw new RiceIllegalArgumentException("Failed to load RuleValidationAttribute for: " + extensionDefinition);
        }
        return attribute;
    }
}