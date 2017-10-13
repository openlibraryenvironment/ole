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
package org.kuali.rice.krms.impl.ui;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.uif.control.UifKeyValuesFinderBase;
import org.kuali.rice.krad.uif.view.ViewModel;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.impl.repository.CategoryBo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helper class that returns all category types that are valid for the given context.
 */
public class CategoryValuesFinder extends UifKeyValuesFinderBase {

    private boolean blankOption;

    /**
     * @return the blankOption
     */
    public boolean isBlankOption() {
        return this.blankOption;
    }

    /**
     * @param blankOption the blankOption to set
     */
    public void setBlankOption(boolean blankOption) {
        this.blankOption = blankOption;
    }

    @Override
    public List<KeyValue> getKeyValues(ViewModel model) {
        List<KeyValue> keyValues = new ArrayList<KeyValue>();

        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm) model;
        AgendaEditor agendaEditor = ((AgendaEditor) maintenanceForm.getDocument().getNewMaintainableObject().getDataObject());

        // if we have an agenda w/ a selected context
        if (agendaEditor.getAgenda() != null && StringUtils.isNotBlank(agendaEditor.getAgenda().getContextId())) {

            // key off the namespace

            String namespace = agendaEditor.getNamespace();

            if(blankOption){
                keyValues.add(new ConcreteKeyValue("", ""));
            }

            Map<String, String> criteria = Collections.singletonMap("namespace", namespace);
            Collection<CategoryBo> categories =
                    KRADServiceLocator.getBusinessObjectService().findMatchingOrderBy(
                            CategoryBo.class, criteria, "name", true
                    );

            for (CategoryBo category : categories) {
                keyValues.add(new ConcreteKeyValue(category.getId(), category.getName()));
            }
        }

        return keyValues;
    }
}
