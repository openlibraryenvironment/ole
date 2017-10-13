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
package org.kuali.rice.krms.impl.rule;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.impl.repository.CategoryBo;
import org.kuali.rice.krms.impl.repository.ContextBo;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.krms.impl.repository.TermSpecificationBo;
import org.kuali.rice.krms.impl.util.KRMSPropertyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TermSpecBusRule extends MaintenanceDocumentRuleBase {

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;

        TermSpecificationBo termSpec = (TermSpecificationBo) document.getNewMaintainableObject().getDataObject();
        isValid &= validateId(termSpec);
        isValid &= validateCategory(termSpec);
        isValid &= validateContext(termSpec);
        isValid &= validateNameNamespace(termSpec);

        return isValid;
    }

    private boolean validateCategory(TermSpecificationBo termSpecificationBo) {
        List<CategoryBo> categories = termSpecificationBo.getCategories();
        List<String> categoryIds = new ArrayList<String>();

        boolean valid = true;
        for (CategoryBo category: categories) {
            if (categoryIds.contains(category.getId())) {
                this.putFieldError(KRMSPropertyConstants.TermSpecification.CATEGORY, "error.termSpecification.duplicateCategory");
                valid = false;
            } else {
                categoryIds.add(category.getId());
            }
        }
        return valid;
    }
    
    private boolean validateContext(TermSpecificationBo termSpec) {
        List<ContextBo> termSpecContexts = termSpec.getContexts();
        List<String> contextIds = new ArrayList<String>();
        boolean valid = true;
        for (ContextBo context: termSpecContexts) {
            if (contextIds.contains(context.getId())) {
                this.putFieldError(KRMSPropertyConstants.TermSpecification.CONTEXT, "error.termSpecification.duplicateContext");
                valid = false;
            } else {
                contextIds.add(context.getId());
            }
        }
        return valid;
    }

    private boolean validateId(TermSpecificationBo termSpec) {
        if (StringUtils.isNotBlank(termSpec.getId())) {
            TermSpecificationDefinition termSpecInDatabase = getTermBoService().getTermSpecificationById(termSpec.getId());
            if ((termSpecInDatabase  != null) && (!StringUtils.equals(termSpecInDatabase.getId(), termSpec.getId()))) {
                this.putFieldError(KRMSPropertyConstants.TermSpecification.TERM_SPECIFICATION_ID, "error.termSpecification.duplicateId");
                return false;
            }
        }

        return true;
    }

    /**
     * Check if the name-namespace pair already exist.
     * @param termSpec
     * @return true if the name-namespace pair is unique, false otherwise
     */
    private boolean validateNameNamespace(TermSpecificationBo termSpec) {
        if (StringUtils.isNotBlank(termSpec.getName()) && StringUtils.isNotBlank(
                termSpec.getNamespace())) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put("name", termSpec.getName());
            criteria.put("namespace", termSpec.getNamespace());

            TermSpecificationBo termSpecInDatabase = getBoService().findByPrimaryKey(TermSpecificationBo.class, criteria);

            if((termSpecInDatabase != null) && (!StringUtils.equals(termSpecInDatabase.getId(), termSpec.getId()))) {
                this.putFieldError(KRMSPropertyConstants.TermSpecification.NAME, "error.term.duplicateNameNamespace");
                return false;
            }
        }

        return true;
    }

    public TermBoService getTermBoService() {
        return KrmsRepositoryServiceLocator.getTermBoService();
    }

}
