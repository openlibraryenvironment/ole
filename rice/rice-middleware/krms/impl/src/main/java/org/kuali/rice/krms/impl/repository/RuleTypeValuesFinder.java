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
package org.kuali.rice.krms.impl.repository;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.InquiryForm;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.impl.ui.AgendaEditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class returns all rule types of rules.
 */
public class RuleTypeValuesFinder extends UifKeyValuesFinderBase {

    @Override
	public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        AgendaEditor agendaEditor;
        if (model instanceof InquiryForm) {
            InquiryForm inquiryForm = (InquiryForm) model;
            agendaEditor = ((AgendaEditor) inquiryForm.getDataObject());

        } else {
            MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
            agendaEditor = ((AgendaEditor) maintenanceForm.getDocument().getNewMaintainableObject().getDataObject());
        }

        // if we have an agenda w/ a selected context
        if (agendaEditor.getAgenda() != null && StringUtils.isNotBlank(agendaEditor.getAgenda().getContextId())) {
            Collection<KrmsTypeDefinition> ruleTypes = getKrmsTypeRepositoryService().findAllRuleTypesByContextId(
                    agendaEditor.getAgenda().getContextId());
            for (KrmsTypeDefinition ruleType : ruleTypes) {
                keyValues.add(new ConcreteKeyValue(ruleType.getId(), ruleType.getName()));
            }
        }

        return keyValues;
    }

    public KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
    }

}
