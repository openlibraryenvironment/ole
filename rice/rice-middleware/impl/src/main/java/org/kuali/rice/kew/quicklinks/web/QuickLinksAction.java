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
package org.kuali.rice.kew.quicklinks.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.quicklinks.service.QuickLinksService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.GlobalVariables;


/**
 * A Struts Action for interfacing with the Quick Links system
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class QuickLinksAction extends KewKualiAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(QuickLinksAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        initForm(request, form);
        return super.execute(mapping, form, request, response);
    }

    public void initForm(HttpServletRequest request, ActionForm form) throws Exception {
        QuickLinksForm quickLinksForm = (QuickLinksForm)form;
        String principalId = getUserSession().getPrincipalId();
        LOG.debug("getting Action List Stats");
        quickLinksForm.setActionListStats(getQuickLinksService().getActionListStats(principalId));
        LOG.debug("finished getting Action List Stats");

        LOG.debug("getting Initiated Document Types");
        quickLinksForm.setInitiatedDocumentTypes(getQuickLinksService().getInitiatedDocumentTypesList(principalId));
        LOG.debug("finished getting Initiated Document Types");

        LOG.debug("getting Named Searches");
        List namedSearches = new ArrayList();
        namedSearches.addAll(getQuickLinksService().getNamedSearches(principalId));
        quickLinksForm.setNamedSearches(namedSearches);
        request.setAttribute("namedSearches",namedSearches);
        LOG.debug("finished getting Named Searches");

        LOG.debug("getting Recent Searches");
        quickLinksForm.setRecentSearches(getQuickLinksService().getRecentSearches(principalId));
        LOG.debug("finished getting Recent Searches");

        LOG.debug("getting Watched Documents");
        quickLinksForm.setWatchedDocuments(getQuickLinksService().getWatchedDocuments(principalId));
        LOG.debug("finished getting Watched Documents");
    }



    private QuickLinksService getQuickLinksService() {
        return ((QuickLinksService)KEWServiceLocator.getService(KEWServiceLocator.QUICK_LINKS_SERVICE));
    }
    private static UserSession getUserSession() {
        return GlobalVariables.getUserSession();
    }

}
