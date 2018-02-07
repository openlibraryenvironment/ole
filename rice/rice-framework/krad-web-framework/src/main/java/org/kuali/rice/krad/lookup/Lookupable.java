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
package org.kuali.rice.krad.lookup;

import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.web.form.LookupForm;

import java.util.Collection;
import java.util.Map;

/**
 * Provides contract for implementing a lookup within the lookup framework
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Lookupable extends ViewHelperService, java.io.Serializable {

    /**
     * Initialize the suppressAction indicator on the LookupForm.
     *
     * <p>
     * The suppress action is set to true if the user is not authorized to initiate these documents.  The indicator
     * is then used to hide irrelevant actions such as creating a new document or editing existing ones.
     * </p>
     *
     * @param lookupForm on which to initialize the suppressAction indicator
     */
    public void initSuppressAction(LookupForm lookupForm);

    /**
     * Invoked to carry out the lookup search based on the given map of key/value search
     * values
     *
     * @param form - lookup form instance containing the lookup data
     * @param searchCriteria - map of criteria currently set
     * @param bounded - indicates whether the results should be limited (if necessary) to the max search
     * result limit configured
     * @return the list of result objects, possibly bounded
     */
    public Collection<?> performSearch(LookupForm form, Map<String, String> searchCriteria, boolean bounded);

    /**
     * Invoked when the clear action is requested to result the search fields to
     * their initial default values
     *
     * @param form - lookup form instance containing the lookup data
     * @param searchCriteria - map of criteria currently set
     * @return map of criteria with field values reset to defaults
     */
    public Map<String, String> performClear(LookupForm form, Map<String, String> searchCriteria);

    /**
     * Invoked to perform validation on the search criteria before the search is performed
     *
     * @param form - lookup form instance containing the lookup data
     * @param searchCriteria - map of criteria where key is search property name and value is
     * search value (which can include wildcards)
     * @return  boolean true if validation was successful, false if there were errors and the search
     * should not be performed
     */
    public boolean validateSearchParameters(LookupForm form, Map<String, String> searchCriteria);

    /**
     * Sets the class for the data object the lookup will be provided on
     *
     * @param dataObjectClass - data object class for lookup
     */
    public void setDataObjectClass(Class<?> dataObjectClass);

    /**
     * Returns the class for the data object the lookup is configured with
     *
     * @return Class<?> data object class
     */
    public Class<?> getDataObjectClass();

    /**
     * Invoked to build the return URL for a result row
     *
     * <p>
     * Based on the line contained in the field context, the URL for returning the role is constructed and
     * set as the action for the action link. If a return link cannot be constructed the action link should be set
     * to not render
     * </p>
     *
     * @param returnLink - action link that will be used to render the return URL
     * @param model - lookup form containing the data
     */
    public void getReturnUrlForResults(Action returnLink, Object model);

    /**
     * Invoked to build a maintenance URL for a result row
     *
     * <p>
     * Based on the line contained in the field context and the given maintenance method that should be called a
     * URL is constructed and set as the action on the action link. If a maintenance link cannot be constructed the
     * action link should be set to not render
     * </p>
     *
     * @param actionLink - action link that will be used to return the maintenance URL
     * @param model - lookup form containing the data
     * @param maintenanceMethodToCall - name of the method that should be invoked in the maintenance controller
     */
    public void getMaintenanceActionLink(Action actionLink, Object model, String maintenanceMethodToCall);

    /**
     * Set the value for the input field control to contain the field conversion values for the line
     *
     * <p>
     * Creates and populate the value of the input field control.  This value is build according to
     * {@link LookupForm#getFieldConversions} and allows for client side population of the returned fields without
     * having to do an additional server call.
     * </p>
     *
     * @param selectField - the InputField used to mark the lookup row as selected
     * @param model - lookup form containing the data
     */
    public void setMultiValueLookupSelect(InputField selectField, Object model);

    /**
     * Set an error message to be displayed in the lookup results field
     *
     * @param form
     * @param messageToDisplay
     */
    public void generateErrorMessageForResults(LookupForm form, String messageToDisplay);

}
