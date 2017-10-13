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
package org.kuali.rice.krad.uif.service.impl;

import org.kuali.rice.krad.bo.AdHocRoutePerson;
import org.kuali.rice.krad.bo.AdHocRouteWorkgroup;
import org.kuali.rice.krad.rules.rule.event.AddAdHocRoutePersonEvent;
import org.kuali.rice.krad.rules.rule.event.AddAdHocRouteWorkgroupEvent;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.KualiRuleService;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.DocumentFormBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentViewHelperServiceImpl extends ViewHelperServiceImpl {

    private KualiRuleService kualiRuleService;

    /**
     * Performs validation on the new collection line before it is added to the
     * corresponding collection
     *
     * @param view view instance that the action was taken on
     * @param collectionGroup collection group component for the collection
     * @param addLine new line instance to validate
     * @param model object instance that contains the views data
     * @return true if the line is valid and it should be added to the
     *         collection, false if it was not valid and should not be added to
     *         the collection
     */
    @Override
    protected boolean performAddLineValidation(View view, CollectionGroup collectionGroup, Object model,
            Object addLine) {
        boolean isValidLine = true;

        if (model instanceof DocumentFormBase && addLine instanceof AdHocRoutePerson) {
            DocumentFormBase form = (DocumentFormBase) model;
            isValidLine = getKualiRuleService()
                    .applyRules(new AddAdHocRoutePersonEvent(form.getDocument(), (AdHocRoutePerson) addLine));
        } else if (model instanceof DocumentFormBase && addLine instanceof AdHocRouteWorkgroup) {
            DocumentFormBase form = (DocumentFormBase) model;
            isValidLine = getKualiRuleService()
                    .applyRules(new AddAdHocRouteWorkgroupEvent(form.getDocument(), (AdHocRouteWorkgroup) addLine));
        }

        return isValidLine;
    }

    /**
     * Gets the Kuali rule service
     *
     * @return Kuali rule service
     */
    protected KualiRuleService getKualiRuleService() {
        if (kualiRuleService == null) {
            kualiRuleService = KRADServiceLocatorWeb.getKualiRuleService();
        }
        return this.kualiRuleService;
    }
}
