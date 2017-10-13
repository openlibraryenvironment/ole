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
package org.kuali.rice.kew.stats.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.stats.Stats;
import org.kuali.rice.kew.stats.service.StatsService;
import org.kuali.rice.kew.web.KewKualiAction;


/**
 * A Struts Action for compiling and displaying statistics about the KEW application.
 *
 * @see Stats
 * @see StatsService
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StatsAction extends KewKualiAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        initForm(mapping, request, form);
        return super.execute(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

            StatsForm statForm = (StatsForm) form;

            statForm.determineBeginDate();
            statForm.determineEndDate();

            StatsService statsService = this.getStatsService();
            statsService.NumUsersReport(statForm.getStats());
            statsService.DocumentsRoutedReport(statForm.getStats(), statForm.getBeginningDate(), statForm.getEndingDate());
            statsService.NumActiveItemsReport(statForm.getStats());
            statsService.NumberOfDocTypesReport(statForm.getStats());
            statsService.NumInitiatedDocsByDocTypeReport(statForm.getStats());

            return mapping.findForward("basic");

    }

    public void initForm(ActionMapping mapping, HttpServletRequest request, ActionForm form) {
        StatsForm statForm = (StatsForm) form;
        Map dropDownMap = statForm.makePerUnitOfTimeDropDownMap();
        request.setAttribute("timeUnitDropDown", dropDownMap);
        statForm.validateDates();
    }

    public StatsService getStatsService() {
        return (StatsService) KEWServiceLocator.getService(KEWServiceLocator.STATS_SERVICE);
    }

}
