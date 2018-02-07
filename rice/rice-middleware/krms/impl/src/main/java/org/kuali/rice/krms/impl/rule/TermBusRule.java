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
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.TermBo;
import org.kuali.rice.krms.impl.repository.TermBoService;
import org.kuali.rice.krms.impl.util.KRMSPropertyConstants;

import java.util.HashMap;
import java.util.Map;

public class TermBusRule extends MaintenanceDocumentRuleBase {

    @Override
    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;

        TermBo term = (TermBo) document.getNewMaintainableObject().getDataObject();
        isValid &= validateId(term);
        isValid &= validateDescriptionNamespace(term);
        isValid &= validateTermSpecId(term);

        return isValid;
    }

    private boolean validateTermSpecId(TermBo term) {
        if (StringUtils.isBlank(term.getSpecificationId())) {
            this.putFieldError(KRMSPropertyConstants.Term.TERM_SPECIFICATION_ID, "error.term.invalidTermSpecification");
            return false;
        }

        TermSpecificationDefinition termSpec =
                KrmsRepositoryServiceLocator.getTermBoService().getTermSpecificationById(term.getSpecificationId());

        if (termSpec == null) {
            this.putFieldError(KRMSPropertyConstants.Term.TERM_SPECIFICATION_ID, "error.term.invalidTermSpecification");
            return false;
        }

        return true;
    }

    private boolean validateId(TermBo term) {
        if (StringUtils.isNotBlank(term.getId())) {
            TermDefinition termInDatabase = getTermBoService().getTerm(term.getId());
            if ((termInDatabase  != null) && (!StringUtils.equals(termInDatabase.getId(), term.getId()))) {
                this.putFieldError(KRMSPropertyConstants.Term.TERM_ID, "error.term.duplicateId");
                return false;
            }
        }

        return true;
    }

    /**
     * Check if the name-namespace pair already exist.
     * @param term
     * @return true if the name-namespace pair is unique, false otherwise
     */
    private boolean validateDescriptionNamespace(TermBo term) {
        if (term.getSpecification() != null && StringUtils.isNotBlank(term.getDescription()) && StringUtils.isNotBlank(
                term.getSpecification().getNamespace())) {

            Map<String, String> criteria = new HashMap<String, String>();

            criteria.put("description", term.getDescription());
            criteria.put("specification.namespace", term.getSpecification().getNamespace());

            TermBo termInDatabase = getBoService().findByPrimaryKey(TermBo.class, criteria);

            if((termInDatabase != null) && (!StringUtils.equals(termInDatabase.getId(), term.getId()))) {
                this.putFieldError(KRMSPropertyConstants.Term.DESCRIPTION, "error.term.duplicateNameNamespace");
                return false;
            }
        }

        return true;
    }

    public TermBoService getTermBoService() {
        return KrmsRepositoryServiceLocator.getTermBoService();
    }

}
