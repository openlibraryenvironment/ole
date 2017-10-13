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
package org.kuali.rice.krad.uif.view;

import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the status of any modal dialogs that are used in the view.
 *
 * <p>
 * Keeps track of which modal dialogs have been asked and/or answered
 * during the life cycle of a view.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DialogManager implements Serializable {
    private static final long serialVersionUID = 4627667603510159528L;

    private static final String TRUE_VALUES = "/true/yes/y/on/1/";
    private static final String FALSE_VALUES = "/false/no/n/off/0/";

    /**
     * Status information record used to track dialog status
     */
    private static class DialogInfo implements Serializable{
        private static final long serialVersionUID = 2779403853894669510L;

        private String dialogId;
        private boolean asked;
        private boolean answered;
        private String answer;
        private String explanation;
        private String returnMethod;

        public DialogInfo(String dialogId, String returnMethod){
            this.dialogId = dialogId;
            this.asked = false;
            this.answered = false;
            this.answer = null;
            this.explanation = null;
            this.returnMethod = returnMethod;
        }
    }

    private String currentDialogId;
    private Map<String, DialogInfo> dialogs;

    /**
     * Constructs new instance
     */
    public DialogManager(){
        // init dialogs
        dialogs = new HashMap<String, DialogInfo>();
    }

    /**
     * Indicates whether the named dialog has already been presented to the user
     *
     * @param dialogId the key identifying the specific dialog
     * @return true if dialog has been displayed, false if not
     */
    public boolean hasDialogBeenDisplayed(String dialogId){
        if (dialogs.containsKey(dialogId)){
            return dialogs.get(dialogId).asked;
        }
        return false;
    }

    /**
     * Indicates whether the named dialog has alread been answered by the user
     *
     * @param dialogId name of the dialog in questions
     * @return true if the dialog has been answered by the user
     */
    public boolean hasDialogBeenAnswered(String dialogId){
        if (dialogs.containsKey(dialogId)){
            return dialogs.get(dialogId).answered;
        }
        return false;
    }

    /**
     * Gets the answer previously entered by the user when responding to this dialog
     *
     * <p>
     * Returns the key value of the option chosen by the user.
     * Returns null if the dialog has not yet been asked, or if the user has not yet
     * responded.
     * </p>
     *
     * @param dialogId a String identifying the dialog
     * @return the key String of the option KeyValue chosen by the user
     */
    public String getDialogAnswer(String dialogId){
        if (hasDialogBeenAnswered(dialogId)){
            return dialogs.get(dialogId).answer;
        }
        return null;
    }

    /**
     * Sets the answer chosen by the user when responding to the dialog
     *
     * @param dialogId id of the dialog
     * @param answer value chosen by the user
     */
    public void setDialogAnswer(String dialogId, String answer){
        DialogInfo dialogInfo = dialogs.get(dialogId);
        if (dialogInfo != null){
            dialogInfo.answer = answer;
            dialogInfo.answered = true;
            dialogs.put(dialogId,dialogInfo);
        }
    }

    /**
     * Gets the text String value of the explanation input field
     *
     * @param dialogId dialog identifier
     * @return String representing user text input entered into the dialog
     */
    public String getDialogExplanation(String dialogId){
        return dialogs.get(dialogId).explanation;
    }

    /**
     * Sets the exlanation text String obtained from the explanation input field
     * for the dialog
     *
     * @param dialogId identifier of the dialog
     * @param explanation text String from input field
     */
    public void setDialogExplanation(String dialogId, String explanation){
        DialogInfo dialogInfo = dialogs.get(dialogId);
        if (dialogInfo != null){
            dialogInfo.explanation = explanation;
            dialogs.put(dialogId,dialogInfo);
        }
    }

    /**
     * Indicates whethe the user answered affirmatively to the question
     *
     * <p>
     * The answer string is the key used for the option key/value pair selected by the user.
     * This assumes that the developer used one of the common keys used for yes/no questions.
     * The answer is checked to see if it is one of the acceptable values for "Yes". If so,
     * the method returns true. False if not.
     * Also returns false, if the question has not even been asked of the user.
     * </p>
     *
     * @param dialogId
     * @return true if the user answered the modal dialog affirmatively, false if answered negatively;
     *   also returns false if the questions hasn't yet been answered
     */
    public boolean wasDialogAnswerAffirmative(String dialogId){
        String answer = getDialogAnswer(dialogId);
        if (answer != null){
            StringBuilder builder = new StringBuilder();
            builder.append("/").append(answer.toLowerCase()).append("/");
            String input = builder.toString();
            if(TRUE_VALUES.contains(builder.toString())) {
                return true;
            }
        }

        // TODO: Should we return false if question not even asked yet?
        //       Or should we throw an exception?
        return false;
    }

    /**
     * Retrieves the target method to redirect to when returning from a lightbox
     *
     * @param dialogId identifies the dialog currently being handled
     * @return controller method to call
     */
    public String getDialogReturnMethod(String dialogId){
        if (hasDialogBeenAnswered(dialogId)){
            return dialogs.get(dialogId).returnMethod;
        }
        return null;
    }

    /**
     * sets the return method to call after returning from dialog
     *
     * @param dialogId
     * @param returnMethod
     */
    public void setDialogReturnMethod(String dialogId, String returnMethod){
        DialogInfo dialogInfo = dialogs.get(dialogId);
        dialogInfo.returnMethod = returnMethod;
        dialogs.put(dialogId, dialogInfo);
    }

    /**
     * Creates a new DialogInfo record and adds it to the list of dialogs
     * used in the view
     *
     * <p>
     * New dialog entry is initialized to asked=false, answered=false.
     * If the dialog already has a record, nothing is performed.
     * </p>
     *
     * @param dialogId String name identifying the dialog
     */
    public void addDialog(String dialogId, String returnMethod){
        DialogInfo dialogInfo = new DialogInfo(dialogId, returnMethod);
        dialogInfo.asked = true;
        dialogs.put(dialogId, dialogInfo);
        setCurrentDialogId(dialogId);
    }

    /**
     * Removes a dialog from the list of dialogs used in this vew.
     *
     * <p>
     * If the dialog is in the list, it is removed.
     * If the dialog is not in the list, nothing is performed.
     * </p>
     *
     * @param dialogId String identifying the dialog to be removed
     */
    public void removeDialog(String dialogId){
        if (dialogs.containsKey(dialogId)){
            dialogs.remove(dialogId);
        }
    }

    /**
     * Removes all dialogs from the list of dialogs used in this vew.
     *
     */
    public void removeAllDialogs(){
        dialogs.clear();
    }

    /**
     * Sets the status of the dialog tracking record to indicate that this dialog
     * has not yet been asked or answered.
     *
     * @param dialogId String identifier for the dialog
     */
    public void resetDialogStatus(String dialogId){
        String returnMethod = getDialogReturnMethod(dialogId);
        dialogs.put(dialogId, new DialogInfo(dialogId, returnMethod));
    }

    /**
     * Gets the Map used to track dialog interactions related to the view
     *
     * @return a Map of DialogInfo records
     */
    public Map<String, DialogInfo> getDialogs() {
        return dialogs;
    }

    /**
     * Sets the Map of DialogInfo records used to track modal dialog interactions
     * within a view
     *
     * @param dialogs a Map of DialogInfo records keyed by the dialog id
     */
    public void setDialogs(Map<String, DialogInfo> dialogs) {
        this.dialogs = dialogs;
    }

    /**
     * Gets the name of the currently active dialog
     *
     * @return the name of the current dialog
     */
    public String getCurrentDialogId() {
        return currentDialogId;
    }

    /**
     * Sets the name of the currently active dialog
     *
     * @param currentDialogId the name of the dialog
     */
    public void setCurrentDialogId(String currentDialogId) {
        this.currentDialogId = currentDialogId;
    }

}
