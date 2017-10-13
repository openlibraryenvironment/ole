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
package org.kuali.rice.krad.web.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.krad.lookup.CollectionIncomplete;
import org.kuali.rice.krad.lookup.LookupUtils;
import org.kuali.rice.krad.lookup.Lookupable;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that handles requests coming from a <code>LookupView</code>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/lookup")
public class LookupController extends UifControllerBase {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LookupController.class);

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected LookupForm createInitialForm(HttpServletRequest request) {
        return new LookupForm();
    }

    /**
     * Invoked to request an lookup view for a data object class
     *
     * <p>
     * Checks if the data object is externalizable and we need to redirect to the appropriate lookup URL, else
     * continues with the lookup view display
     * </p>
     */
    @RequestMapping(params = "methodToCall=start")
    @Override
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        LookupForm lookupForm = (LookupForm) form;

        Lookupable lookupable = lookupForm.getLookupable();
        if (lookupable == null) {
            LOG.error("Lookupable is null.");
            throw new RuntimeException("Lookupable is null.");
        }
        lookupable.initSuppressAction(lookupForm);

        if (request.getParameter(UifParameters.MESSAGE_TO_DISPLAY) != null) {
            lookupable.generateErrorMessageForResults(lookupForm, request.getParameter(
                    UifParameters.MESSAGE_TO_DISPLAY));
        }

        // if request is not a redirect, determine if we need to redirect for an externalizable object lookup
        if (!lookupForm.isRedirectedLookup()) {
            Class lookupObjectClass = null;
            try {
                lookupObjectClass = Class.forName(lookupForm.getDataObjectClassName());
            } catch (ClassNotFoundException e) {
                throw new RiceRuntimeException("Unable to get class for name: " + lookupForm.getDataObjectClassName());
            }

            ModuleService responsibleModuleService =
                    KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(lookupObjectClass);
            if (responsibleModuleService != null && responsibleModuleService.isExternalizable(lookupObjectClass)) {
                String lookupUrl = responsibleModuleService.getExternalizableDataObjectLookupUrl(lookupObjectClass,
                        KRADUtils.convertRequestMapToProperties(request.getParameterMap()));

                Properties redirectUrlProps = new Properties();
                redirectUrlProps.put(UifParameters.REDIRECTED_LOOKUP, "true");

                // clear current form from session
                GlobalVariables.getUifFormManager().removeSessionForm(form);

                return performRedirect(form, lookupUrl, redirectUrlProps);
            }
        }

        return super.start(lookupForm, result, request, response);
    }

    /**
     * Just returns as if return with no value was selected
     */
    @Override
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=cancel")
    public ModelAndView cancel(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        LookupForm lookupForm = (LookupForm) form;

        Lookupable lookupable = lookupForm.getLookupable();
        if (lookupable == null) {
            LOG.error("Lookupable is null.");
            throw new RuntimeException("Lookupable is null.");
        }
        lookupable.initSuppressAction(lookupForm);

        Properties props = new Properties();
        props.put(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.REFRESH);
        if (StringUtils.isNotBlank(lookupForm.getReturnFormKey())) {
            props.put(UifParameters.FORM_KEY, lookupForm.getReturnFormKey());
        }
        if (StringUtils.isNotBlank(lookupForm.getDocNum())) {
            props.put(UifParameters.DOC_NUM, lookupForm.getDocNum());
        }

        // clear current form from session
        GlobalVariables.getUifFormManager().removeSessionForm(form);

        return performRedirect(lookupForm, lookupForm.getReturnLocation(), props);
    }

    /**
     * clearValues - clears the values of all the fields on the jsp.
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=clearValues")
    public ModelAndView clearValues(@ModelAttribute("KualiForm") LookupForm lookupForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        Lookupable lookupable = lookupForm.getLookupable();
        if (lookupable == null) {
            LOG.error("Lookupable is null.");
            throw new RuntimeException("Lookupable is null.");
        }
        lookupable.initSuppressAction(lookupForm);
        lookupForm.setLookupCriteria(lookupable.performClear(lookupForm, lookupForm.getLookupCriteria()));

        return getUIFModelAndView(lookupForm);
    }

    /**
     * search - sets the values of the data entered on the form on the jsp into a map and then searches for the
     * results.
     */
    @RequestMapping(params = "methodToCall=search")
    public ModelAndView search(@ModelAttribute("KualiForm") LookupForm lookupForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        Lookupable lookupable = lookupForm.getLookupable();
        if (lookupable == null) {
            LOG.error("Lookupable is null.");
            throw new RuntimeException("Lookupable is null.");
        }
        lookupable.initSuppressAction(lookupForm);
        // Need to process date range fields before validating
        Map<String, String> searchCriteria = LookupUtils.preprocessDateFields(lookupForm.getLookupCriteria());
        // validate search parameters
        boolean searchValid = lookupable.validateSearchParameters(lookupForm, searchCriteria);

        if (searchValid) {
            Collection<?> displayList = lookupable.performSearch(lookupForm, searchCriteria, true);

            if (displayList instanceof CollectionIncomplete<?>) {
                request.setAttribute("reqSearchResultsActualSize",
                        ((CollectionIncomplete<?>) displayList).getActualSizeIfTruncated());
            } else {
                request.setAttribute("reqSearchResultsActualSize", new Integer(displayList.size()));
            }

            lookupForm.setLookupResults(displayList);
        }

        return getUIFModelAndView(lookupForm);
    }

    /**
     * Invoked from the UI to return the selected lookup results lines, parameters are collected to build a URL to
     * the caller and then a redirect is performed
     *
     * @param lookupForm - lookup form instance containing the selected results and lookup configuration
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=returnSelected")
    public String returnSelected(@ModelAttribute("KualiForm") LookupForm lookupForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response, final RedirectAttributes redirectAttributes) {

        Properties parameters = new Properties();

        // build string of select line identifiers
        String selectedLineValues = "";
        Set<String> selectedLines = lookupForm.getSelectedCollectionLines().get(UifPropertyPaths.LOOKUP_RESULTS);
        if (selectedLines != null) {
            for (String selectedLine : selectedLines) {
                selectedLineValues += selectedLine + ",";
            }
            selectedLineValues = StringUtils.removeEnd(selectedLineValues, ",");
        }

        //check to see what the redirect URL length would be
        parameters.put(UifParameters.SELECTED_LINE_VALUES, selectedLineValues);
        parameters.putAll(lookupForm.getInitialRequestParameters());
        String redirectUrl = UrlFactory.parameterizeUrl(lookupForm.getReturnLocation(), parameters);

        boolean lookupCameFromDifferentServer = areDifferentDomains(lookupForm.getReturnLocation(),
                lookupForm.getRequestUrl());

        if (redirectUrl.length() > RiceConstants.MAXIMUM_URL_LENGTH && !lookupCameFromDifferentServer) {
            redirectAttributes.addFlashAttribute(UifParameters.SELECTED_LINE_VALUES, selectedLineValues);
        }

        if (redirectUrl.length() > RiceConstants.MAXIMUM_URL_LENGTH && lookupCameFromDifferentServer) {
            HashMap<String, String> parms = (HashMap<String, String>) lookupForm.getInitialRequestParameters();
            parms.remove(UifParameters.RETURN_FORM_KEY);

            //add an error message to display to the user
            redirectAttributes.mergeAttributes(parms);
            redirectAttributes.addAttribute(UifParameters.MESSAGE_TO_DISPLAY,
                    RiceKeyConstants.INFO_LOOKUP_RESULTS_MV_RETURN_EXCEEDS_LIMIT);

            String formKeyParam = request.getParameter(UifParameters.FORM_KEY);
            redirectAttributes.addAttribute(UifParameters.FORM_KEY, formKeyParam);

            return UifConstants.REDIRECT_PREFIX + lookupForm.getRequestUrl();
        }

        if (redirectUrl.length() < RiceConstants.MAXIMUM_URL_LENGTH) {
            redirectAttributes.addAttribute(UifParameters.SELECTED_LINE_VALUES, selectedLineValues);
        }

        redirectAttributes.addAttribute(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.RETURN_METHOD_TO_CALL);

        if (StringUtils.isNotBlank(lookupForm.getReturnFormKey())) {
            redirectAttributes.addAttribute(UifParameters.FORM_KEY, lookupForm.getReturnFormKey());
        }

        redirectAttributes.addAttribute(KRADConstants.REFRESH_CALLER, lookupForm.getView().getId());
        redirectAttributes.addAttribute(KRADConstants.REFRESH_CALLER_TYPE,
                UifConstants.RefreshCallerTypes.MULTI_VALUE_LOOKUP);
        redirectAttributes.addAttribute(KRADConstants.REFRESH_DATA_OBJECT_CLASS, lookupForm.getDataObjectClassName());

        if (StringUtils.isNotBlank(lookupForm.getDocNum())) {
            redirectAttributes.addAttribute(UifParameters.DOC_NUM, lookupForm.getDocNum());
        }

        if (StringUtils.isNotBlank(lookupForm.getLookupCollectionName())) {
            redirectAttributes.addAttribute(UifParameters.LOOKUP_COLLECTION_NAME, lookupForm.getLookupCollectionName());
        }

        if (StringUtils.isNotBlank(lookupForm.getReferencesToRefresh())) {
            redirectAttributes.addAttribute(KRADConstants.REFERENCES_TO_REFRESH, lookupForm.getReferencesToRefresh());
        }

        // clear current form from session
        GlobalVariables.getUifFormManager().removeSessionForm(lookupForm);

        return UifConstants.REDIRECT_PREFIX + lookupForm.getReturnLocation();
    }

    /**
     * Convenience method for determining whether two URLs point at the same domain
     *
     * @param firstDomain
     * @param secondDomain
     * @return true if the domains are different, false otherwise
     */
    private boolean areDifferentDomains(String firstDomain, String secondDomain) {
        try {
            URL urlOne = new URL(firstDomain.toLowerCase());
            URL urlTwo = new URL(secondDomain.toLowerCase());
            if(urlOne.getHost().equals(urlTwo.getHost())){
                LOG.debug("Hosts " + urlOne.getHost() + " of domains " + firstDomain
                        + " and " + secondDomain + " were determined to be equal");
                return false;
            }
            else {
                LOG.debug("Hosts " + urlOne.getHost() + " of domains " + firstDomain
                        + " and " + secondDomain + " are not equal");
                return true;
            }
        } catch (MalformedURLException mue) {
            LOG.error("Unable to successfully compare domains " + firstDomain + " and " + secondDomain);
        }
        return true;
    }
}
