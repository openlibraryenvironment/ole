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
package edu.sampleu.kew.krad.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.stats.service.StatsService;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import edu.sampleu.kew.krad.form.StatsForm;

/**
 * This is a description of what this class does - Venkat don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Controller
@RequestMapping(value = "/stats")
public class StatsController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected StatsForm createInitialForm(HttpServletRequest request) {
        return new StatsForm();
    }

	@Override
	@RequestMapping(params = "methodToCall=start")
	public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {

		StatsForm statForm = (StatsForm) form;

		try{
	        statForm.determineBeginDate();
	        statForm.determineEndDate();
	
	        StatsService statsService = this.getStatsService();
	        statsService.NumUsersReport(statForm.getStats());
	        statsService.DocumentsRoutedReport(statForm.getStats(), statForm.getBeginningDate(), statForm.getEndingDate());
	        statsService.NumActiveItemsReport(statForm.getStats());
	        statsService.NumberOfDocTypesReport(statForm.getStats());
	        statsService.NumInitiatedDocsByDocTypeReport(statForm.getStats());
	        
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
        
		return super.start(statForm, result, request, response);
	}
	
	 public StatsService getStatsService() {
        return (StatsService) KEWServiceLocator.getService(KEWServiceLocator.STATS_SERVICE);
    }
}
