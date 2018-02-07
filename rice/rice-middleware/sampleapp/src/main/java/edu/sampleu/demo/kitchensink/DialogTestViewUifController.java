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

import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.view.DialogManager;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * a controller for the configuration test view
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/dialog-configuration-test")
public class DialogTestViewUifController extends UifControllerBase {

    @Override
    protected UifFormBase createInitialForm(HttpServletRequest request) {
        return new UifDialogTestForm();
    }

    /**
     * Displays page for testing dialogs
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        return super.start(form, result, request, response);
    }

    /**
     * Exercises the Dialog framework.
     *
     * <p>
     * Asks a series of questions of the user while processing a client request. Demonstrates the ability to go back
     * to the client bring up a Lightbox modal dialog.
     * <br>
     * Displays a few dialogs back to the user. First a yes/no dialog asking the user to pick an author.
     * Depending on which author was chosen, the user is asked to select a book.
     * The user is then given a chance to start the selection process over or continue on.
     * </p>
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=save")
    public ModelAndView save(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv;

        // dialog names
        String dialog1 = "chooseAuthorDialog";
        String dialog2a = "chooseEastmanBookDialog";
        String dialog2b = "chooseSeussBookDialog";
        String dialog3 = "myRestart";

        // local copies of dialog answers
        boolean whichAuthor = false;
        String chosenBook;
        boolean doRestart = false;

        // choose which author
        if (!hasDialogBeenAnswered(dialog1, form)){
            // redirect back to client to display lightbox
            return showDialog(dialog1, form, request, response);
        }
        whichAuthor = getBooleanDialogResponse(dialog1, form, request, response);

        // continue on here if they answered the the first question
        if (whichAuthor){
            form.setField1("You Selected: P.D. Eastman");

            // popup a 2nd consecutive dialog
            if (!hasDialogBeenAnswered(dialog2a,form)){
                // redirect back to client to display lightbox
                return showDialog(dialog2a, form, request, response);
            }
            chosenBook = form.getDialogManager().getDialogExplanation(dialog2a);

            // return back to the client displaying the choices
            form.setField1("You selected: "+chosenBook+" by P.D. Eastman. Here we go...");
        } else {
            form.setField1("You Selected: Dr. Seuss");
            // in this case, return to client and wait for them to submit again before showing next dialog
            // In the above case, the 1st and 2nd dialog are displayed consecutively before returning to the client.
            // But in this example, we return to the client and wait for them to hit the submit button again
            if (!hasDialogBeenDisplayed(dialog2b, form)){
                form.getDialogManager().addDialog(dialog2b, form.getMethodToCall());
                return getUIFModelAndView(form, "DialogView-Page1");
            }

            // now display the dialog to choose which book
            if (!hasDialogBeenAnswered(dialog2b, form)){
                return showDialog(dialog2b, form, request, response);
            }
            chosenBook = form.getDialogManager().getDialogExplanation(dialog2b);

            // display which story the user has selected
            form.setField1("You selected: "+chosenBook+"  by Dr. Seuss. Here we go...");
        }

        // Wait at the client page for another page submit
        if (!hasDialogBeenDisplayed(dialog3, form)) {
                form.getDialogManager().addDialog(dialog3, form.getMethodToCall());
                return getUIFModelAndView(form, "DialogView-Page1");
        };

        // Ask them if they want to start over
        if (!hasDialogBeenAnswered(dialog3,form)){
            return showDialog(dialog3, form, request, response);
        }
        doRestart = getBooleanDialogResponse(dialog3, form, request, response);

        // clear the dialog manager entries, so when we come back, we'll ask all the questions again
        if (doRestart){
            form.getDialogManager().removeAllDialogs();
            form.setField1("Ok, let's start over.");
            return getUIFModelAndView(form, "DialogView-Page1");
        }

        // we're done, go to the next page
        return getUIFModelAndView(form, "DialogView-Page2");
    }

    /**
     * not used at this time
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     */
    @RequestMapping(params = "methodToCall=goBack")
    public ModelAndView goBack(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

//      TODO: Put "Are Your Sure?" dialog here
        form.getDialogManager().removeAllDialogs();
        form.setField1("Ok, let's start over.");
        return getUIFModelAndView(form, "DialogView-Page1");
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "doRadioDialogExample")
    public ModelAndView doSomething(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dialog1 = "sampleRadioButtonDialog";
        if (!hasDialogBeenAnswered(dialog1, form)){
            // redirect back to client to display lightbox
            return showDialog(dialog1, form, request, response);
        }
        // Get value from chosen radio button
        String choice = form.getDialogManager().getDialogExplanation(dialog1);
        if (choice == null){
            form.setField1("You didn't select one of the radio buttons");
        } else {
            form.setField1("You chose Radio Option "+choice);
        }

        // clear dialog history so they can press the button again
        form.getDialogManager().removeDialog(dialog1);
        // reload page1
        return getUIFModelAndView(form, "DialogView-Page1");
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "doOkCancelExample")
    public ModelAndView doOKCancelExample(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dialog1 = "preDefinedDialogOkCancel";
        if (!hasDialogBeenAnswered(dialog1, form)){
            // redirect back to client to display lightbox
            return showDialog(dialog1, form, request, response);
        }
        // Get user choice
        boolean choice = getBooleanDialogResponse(dialog1, form, request, response);
        StringBuilder sb = new StringBuilder("You selected ");
        sb.append((choice)?"OK":"Cancel");
        form.setField1(sb.toString());

        // clear dialog history so they can press the button again
        form.getDialogManager().removeDialog(dialog1);
        // reload page1
        return getUIFModelAndView(form, "DialogView-Page1");
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "doRegularGroupAsDialog")
    public ModelAndView doRegularGroupAsDialog(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dialog1 = "myRegularGroup";
        if (!hasDialogBeenAnswered(dialog1, form)){
            // redirect back to client to display lightbox
            return showDialog(dialog1, form, request, response);
        }
        // Get value from chosen radio button
        boolean choice = getBooleanDialogResponse(dialog1, form, request, response);
        StringBuilder sb = new StringBuilder("You selected ");
        sb.append((choice)?"Yes":"No");
        form.setField1(sb.toString());

        // clear dialog history so they can press the button again
        form.getDialogManager().removeDialog(dialog1);
        // reload page1
        return getUIFModelAndView(form, "DialogView-Page1");
    }

    /**
     * Test method for a controller that invokes a dialog to test expression evaluation.
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "testExpressionDialog")
    public ModelAndView testExpressionDialog(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dialog4 = "schedulingConfirmDialog";
        if (!hasDialogBeenAnswered(dialog4, form)) {
            // set the value for field3 which will be evaluated
            form.setField3("TestVal");

            // redirect back to client to display lightbox
            return showDialog(dialog4, form, request, response);
        }

        // clear dialog history so they can press the button again
        form.getDialogManager().removeDialog(dialog4);

        // reload page1
        return getUIFModelAndView(form, "DialogView-Page1");
    }

    /**
     * Test method for a controller that invokes a dialog lightbox.
     *
     * @param form - test form
     * @param result - Spring form binding result
     * @param request - http request
     * @param response - http response
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "methodToCall=" + "doExtendedDialog")
    public ModelAndView doExtendedDialog(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dialog1 = "extendedDialogGroup";
        if (!hasDialogBeenAnswered(dialog1, form)){
            // redirect back to client to display lightbox
            return showDialog(dialog1, form, request, response);
        }
        // Get value from chosen radio button
        boolean choice = getBooleanDialogResponse(dialog1, form, request, response);
        StringBuilder sb = new StringBuilder("You selected ");
        sb.append((choice)?"Yes":"No");
        form.setField1(sb.toString());

        // clear dialog history so they can press the button again
        form.getDialogManager().removeDialog(dialog1);
        // reload page1
        return getUIFModelAndView(form, "DialogView-Page1");
    }
    /**
         * Test method for a controller that displays the response in a ightbox.
         *
         * @param form - test form
         * @param result - Spring form binding result
         * @param request - http request
         * @param response - http response
         * @return
         * @throws Exception
         */
        @RequestMapping(params = "methodToCall=" + "doResponseInLightBox")
        public ModelAndView doResponseInLightBox(@ModelAttribute("KualiForm") UifDialogTestForm form, BindingResult result,
                HttpServletRequest request, HttpServletResponse response) throws Exception {
            String dialog1 = "myRegularGroup";
            if (!hasDialogBeenAnswered(dialog1, form)){
                // redirect back to client to display lightbox
                return showDialog(dialog1, form, request, response);
            }
            // Get value from chosen radio button
            boolean choice = getBooleanDialogResponse(dialog1, form, request, response);
            StringBuilder sb = new StringBuilder("You selected ");
            sb.append((choice)?"Yes":"No");
            form.setField1(sb.toString());

            // clear dialog history so they can press the button again
            form.getDialogManager().removeDialog(dialog1);
            // reload page1
            return getUIFModelAndView(form, "DialogView-Page1");
        }

}
