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
package edu.sampleu.demo.kitchensink;

import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditor;
import java.util.Map;
import java.util.Properties;

/**
 * Controller for the Test UI Page
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/uicomponents")
public class UifComponentsTestController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected UifComponentsTestForm createInitialForm(HttpServletRequest request) {
        return new UifComponentsTestForm();
    }

    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        UifComponentsTestForm uiTestForm = (UifComponentsTestForm) form;
        form.setState("state1");
        //for generated view:
        if (form.getView().getId().equals("UifGeneratedFields")) {
            for (int i = 0; i < 100; i++) {
                ((UifComponentsTestForm) form).getList1generated().add(new UITestObject("A" + i, "B" + i, "C" + i,
                        "D" + i));
            }
            for (int i = 0; i < 100; i++) {
                ((UifComponentsTestForm) form).getList2generated().add(new UITestObject("A" + i, "B" + i, "C" + i,
                        "D" + i));
            }
            for (int i = 0; i < 10; i++) {
                ((UifComponentsTestForm) form).getList3generated().add(new UITestObject("A" + i, "B" + i, "C" + i,
                        "D" + i));
                for (int j = 0; j < 10; j++) {
                    ((UifComponentsTestForm) form).getList3generated().get(i).getSubList().add(new UITestObject(
                            "i" + i + "-" + j, "i" + i + "-" + j, "i" + i + "-" + j, "i" + i + "-" + j));
                }
            }
        }

        GlobalVariables.getMessageMap().addGrowlMessage("Welcome!", "kitchenSink.welcome");

        return super.start(uiTestForm, result, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") UifComponentsTestForm uiTestForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        KRADServiceLocatorWeb.getViewValidationService().validateView(uiTestForm);
        return getUIFModelAndView(uiTestForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=close")
    public ModelAndView close(@ModelAttribute("KualiForm") UifComponentsTestForm uiTestForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        return getUIFModelAndView(uiTestForm, "UifCompView-Page1");
    }

    /**
     * Handles menu navigation between view pages
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=navigate")
    public ModelAndView navigate(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        String pageId = form.getActionParamaterValue(UifParameters.NAVIGATE_TO_PAGE_ID);

        if (pageId.equals("UifCompView-Page8")) {
            GlobalVariables.getMessageMap().putError("gField1", "serverTestError");
            GlobalVariables.getMessageMap().putError("gField1", "serverTestError2");
            GlobalVariables.getMessageMap().putError("gField2", "serverTestError");
            GlobalVariables.getMessageMap().putError("gField3", "serverTestError");
            GlobalVariables.getMessageMap().putWarning("gField1", "serverTestWarning");
            GlobalVariables.getMessageMap().putWarning("gField2", "serverTestWarning");
            GlobalVariables.getMessageMap().putInfo("gField2", "serverTestInfo");
            GlobalVariables.getMessageMap().putInfo("gField3", "serverTestInfo");
        }

        return getUIFModelAndView(form, pageId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refreshProgGroup")
    public ModelAndView refreshProgGroup(@ModelAttribute("KualiForm") UifComponentsTestForm uiTestForm,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) {

        return getUIFModelAndView(uiTestForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=refreshWithServerMessages")
    public ModelAndView refreshWithServerMessages(@ModelAttribute("KualiForm") UifComponentsTestForm uiTestForm,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        GlobalVariables.getMessageMap().putError("field45", "serverTestError");
        GlobalVariables.getMessageMap().putWarning("field45", "serverTestWarning");
        GlobalVariables.getMessageMap().putInfo("field45", "serverTestInfo");

        return getUIFModelAndView(uiTestForm);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=genCollectionServerMessages")
    public ModelAndView genCollectionServerMessages(@ModelAttribute("KualiForm") UifComponentsTestForm uiTestForm,
            BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {
        GlobalVariables.getMessageMap().putError("list2[0].field1", "serverTestError");
        GlobalVariables.getMessageMap().putWarning("list2[0].field1", "serverTestWarning");
        GlobalVariables.getMessageMap().putInfo("list2[0].field1", "serverTestInfo");

        GlobalVariables.getMessageMap().putError("list3[0].field1", "serverTestError");
        GlobalVariables.getMessageMap().putWarning("list3[0].field1", "serverTestWarning");
        GlobalVariables.getMessageMap().putInfo("list3[0].field1", "serverTestInfo");

        GlobalVariables.getMessageMap().putError("list5[0].subList[0].field1", "serverTestError");
        GlobalVariables.getMessageMap().putWarning("list5[0].subList[0].field1", "serverTestWarning");
        GlobalVariables.getMessageMap().putInfo("list5[0].subList[0].field1", "serverTestInfo");
        return refresh(uiTestForm, result, request, response);
    }

    /**
     * Adds errors to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addErrors")
    public ModelAndView addErrors(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageSectionMessages")) {
            GlobalVariables.getMessageMap().putError("Demo-ValidationLayout-Section1", "errorSectionTest");
            GlobalVariables.getMessageMap().putError("Demo-ValidationLayout-Section2", "errorSectionTest");
        } else if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageUnmatched")) {
            GlobalVariables.getMessageMap().putError("badKey", "unmatchedTest");
        } else if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SubSectionsPage")) {
            GlobalVariables.getMessageMap().putError("Uif-ValidationLayout-SubGroup", "errorSectionTest");
        }

        if (form.getPostedView().getId().equals("RichMessagesView")) {
            GlobalVariables.getMessageMap().putError("Demo-BasicMessagesSection", "richValidationMessageTest");
            GlobalVariables.getMessageMap().putError("field5", "richValidationMessageTest2");
        }

        Map<String, PropertyEditor> propertyEditors = form.getPostedView().getViewIndex().getFieldPropertyEditors();
        for (String key : propertyEditors.keySet()) {
            GlobalVariables.getMessageMap().putError(key, "error1Test");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Adds warnings to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addWarnings")
    public ModelAndView addWarnings(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageSectionMessages")) {
            GlobalVariables.getMessageMap().putWarning("Demo-ValidationLayout-Section1", "warningSectionTest");
            GlobalVariables.getMessageMap().putWarning("Demo-ValidationLayout-Section2", "warningSectionTest");
        } else if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageUnmatched")) {
            GlobalVariables.getMessageMap().putWarning("badKey", "unmatchedTest");
        }

        Map<String, PropertyEditor> propertyEditors = form.getPostedView().getViewIndex().getFieldPropertyEditors();
        for (String key : propertyEditors.keySet()) {
            GlobalVariables.getMessageMap().putWarning(key, "warning1Test");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Adds infos to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addInfo")
    public ModelAndView addInfo(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageSectionMessages")) {
            GlobalVariables.getMessageMap().putInfo("Demo-ValidationLayout-Section1", "infoSectionTest");
            GlobalVariables.getMessageMap().putInfo("Demo-ValidationLayout-Section2", "infoSectionTest");
        } else if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SectionsPageUnmatched")) {
            GlobalVariables.getMessageMap().putInfo("badKey", "unmatchedTest");
        }

        Map<String, PropertyEditor> propertyEditors = form.getPostedView().getViewIndex().getFieldPropertyEditors();
        for (String key : propertyEditors.keySet()) {
            GlobalVariables.getMessageMap().putInfo(key, "info1Test");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Adds all message types to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addAllMessages")
    public ModelAndView addAllMessages(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        this.addErrors(form, result, request, response);
        this.addWarnings(form, result, request, response);
        this.addInfo(form, result, request, response);

        return getUIFModelAndView(form);
    }

    /**
     * Adds error and warning messages to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addErrorWarnMessages")
    public ModelAndView addErrorWarnMessages(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        this.addErrors(form, result, request, response);
        this.addWarnings(form, result, request, response);

        return getUIFModelAndView(form);
    }

    /**
     * Adds error and info messages to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addErrorInfoMessages")
    public ModelAndView addErrorInfoMessages(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        this.addErrors(form, result, request, response);
        this.addInfo(form, result, request, response);

        return getUIFModelAndView(form);
    }

    /**
     * Adds only 1 error message for testing
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addSingleErrorMessage")
    public ModelAndView addSingleErrorMessage(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (form.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-SubSectionsPage")) {
            GlobalVariables.getMessageMap().putError("Uif-ValidationLayout-SubGroup", "errorSectionTest");
        } else {
            GlobalVariables.getMessageMap().putError("Demo-ValidationLayout-Section1", "errorSectionTest");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Adds warning and info messages to fields defined in the validationMessageFields array
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=addWarnInfoMessages")
    public ModelAndView addWarnInfoMessages(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        this.addWarnings(form, result, request, response);
        this.addInfo(form, result, request, response);

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=gotoState2")
    public ModelAndView gotoState2(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        KRADServiceLocatorWeb.getViewValidationService().validateView(form.getPostedView(), form, "state2");
        if (!GlobalVariables.getMessageMap().hasErrors()) {
            form.setState("state2");
        }

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=gotoState3")
    public ModelAndView gotoState3(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        KRADServiceLocatorWeb.getViewValidationService().validateView(form.getPostedView(), form, "state3");
        if (!GlobalVariables.getMessageMap().hasErrors()) {
            form.setState("state3");
        }

        return getUIFModelAndView(form);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=gotoState4")
    public ModelAndView gotoState4(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        KRADServiceLocatorWeb.getViewValidationService().validateView(form.getPostedView(), form, "state4");
        if (!GlobalVariables.getMessageMap().hasErrors()) {
            form.setState("state4");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Generates a fake incident report to test for errorCallback
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView model and view
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=errorCheck")
    public ModelAndView errorCheck(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        if (true) {
            throw new RuntimeException("Generate fake incident report");
        }

        return getUIFModelAndView(form);
    }

    /**
     * Test controller method to check for ajax redirect functionality. Redirects to the portal main page
     *
     * @param form
     * @param result
     * @param request
     * @param response
     * @return ModelAndView model and view
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=redirectCheck")
    public ModelAndView redirectCheck(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        Properties props = new Properties();
        props.put(UifParameters.VIEW_ID, form.getViewId());
        props.put(UifParameters.FORM_KEY, form.getFormKey());
        return performRedirect(form, "http://localhost:8080/kr-dev", props);
    }

    @Override
    public ModelAndView addLine(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        GlobalVariables.getMessageMap().addGrowlMessage("Greetings!", "kitchenSink.welcome");
        if (uifForm.getPostedView().getCurrentPageId().equals("Demo-ValidationLayout-CollectionsErrorPage")) {
            GlobalVariables.getMessageMap().putError("Demo-ValidationLayout-CollectionErrorSection",
                    "errorSectionTest");
            GlobalVariables.getMessageMap().putErrorForSectionId("Demo-ValidationLayout-CollectionErrorSection",
                    "errorSectionTest");
        }
        return super.addLine(uifForm, result, request, response);
    }

    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=customLineAction")
    public ModelAndView customLineAction(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        String actionParm1 = uifForm.getActionParamaterValue("field1");
        String actionParm2 = uifForm.getActionParamaterValue("field2");

        GlobalVariables.getMessageMap().addGrowlMessage("Action Parameters", "actionParms.message", actionParm1,
                actionParm2);

        return super.deleteLine(uifForm, result, request, response);
    }

    /**
     * Performs custom line action for collection 4 in kitchen sink collection demo.
     * Just puts out a growl message and returns.
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=updateOfficial")
    public ModelAndView updateOfficial(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        String actionParm1 = uifForm.getActionParamaterValue("field1");

        GlobalVariables.getMessageMap().addGrowlMessage("Action Parameters", "customLineAction.message", actionParm1);

        return getUIFModelAndView(uifForm);
    }

    /**
     * Changes the view to readOnly and returns.
     *
     * @param uifForm
     * @param result
     * @param request
     * @param response
     * @return readOnly View
     */
    @RequestMapping(method = RequestMethod.POST, params = "methodToCall=makeReadOnly")
    public ModelAndView makeReadOnly(@ModelAttribute("KualiForm") UifFormBase uifForm, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        //set View to readOnly
        uifForm.getView().setReadOnly(true);
        return getUIFModelAndView(uifForm);
    }
}
