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

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.widget.Widget;

import java.util.Set;

/**
 * Performs user based authorization for actions and components contained in a {@link View}
 *
 * <p>
 * Note only user authorization is done by the authorizer class. For non-user based logic, use the
 * {@link ViewPresentationController}
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ViewAuthorizer {

    /**
     * Returns the set of action flags that are authorized for the given user
     *
     * <p>
     * Action flags are created for views to indicate some action or feature should be enabled. These flags can be
     * used within expressions for configuring the view content.
     *
     * For example:
     * <bean parent="Action" p:methodToCall="save" p:actionLabel="save"
     * p:render="@{#actionFlags[#Constants.KUALI_ACTION_CAN_SAVE]}"/>
     * </p>
     *
     * <p>
     * For each action flag, KIM is consulted to determine if a permission exist for the template associated with
     * the action flag. If so, a check is then made to determine if the user has that permission. If the permission
     * fails for the user, the action flag is removed from the returned set.
     * </p>
     *
     * <p>
     * The Set of available action flags should first be exported by the
     * {@link ViewPresentationController#getActionFlags(View, org.kuali.rice.krad.web.form.UifFormBase)} method. The
     * set returned from this method will be passed as the method argument here by the framework.
     * </p>
     *
     * @param view - view instance the action flags apply to
     * @param model - object containing the view data
     * @param user - user we are authorizing the actions for
     * @param actions - set of action flags to authorize
     * @return Set<String> set of action flags that have been authorized, this will be equal to or a subset of the
     *         actions passed in
     */
    public Set<String> getActionFlags(View view, ViewModel model, Person user, Set<String> actions);

    /**
     * Returns the set of edit modes that are authorized for the given user
     *
     * <p>
     * An edit mode is a string that identifies a set of editable fields within the view. These are generally used
     * when the entire view is not editable, but only certain fields. A field can be associated with an edit mode in
     * two ways. The first is by using the edit mode in an expression when setting the field readOnly property.
     *
     * For example:
     * <property name="readOnly" value="@{!#editModes['specialEdit'] and !fullEdit}" />
     *
     * The second way is with the
     * {@link ViewPresentationController#canEditField(View, ViewModel, org.kuali.rice.krad.uif.field.Field, String)}
     * method which can look at the edit modes map on the view to determine if the given field should be editable.
     * </p>
     *
     * <p>
     * For each edit mode, KIM is consulted to determine if a permission exist for the 'Use View' template and
     * the edit mode detail. If so, a check is then made to determine if the user has that permission. If the
     * permission
     * fails for the user, the edit mode is removed from the returned set.
     * </p>
     *
     * <p>
     * The Set of available edit modes should first be exported by the
     * {@link ViewPresentationController#getEditModes(View, org.kuali.rice.krad.web.form.UifFormBase)} method. The
     * set returned from this method will be passed as the method argument here by the framework.
     * </p>
     *
     * @param view - view instance the edit modes apply to
     * @param model - object containing the view data
     * @param user - user we are authorizing the actions for
     * @param editModes - set of edit modes to authorize
     * @return Set<String> set of edit modes that have been authorized, this will be equal to or a subset of the
     *         edit mode set passed in
     */
    public Set<String> getEditModes(View view, ViewModel model, Person user, Set<String> editModes);

    /**
     * Determines if the given user is authorized to open the given view
     *
     * @param view - view instance to check authorization for
     * @param model - object containing the view data
     * @param user - user to authorize
     * @return boolean true if the user is authorized to open the view, false otherwise
     */
    public boolean canOpenView(View view, ViewModel model, Person user);

    /**
     * Determines if the given user is authorized to edit the given view
     *
     * @param view - view instance to check authorization for
     * @param model - object containing the view data
     * @param user - user to authorize
     * @return boolean true if the user is authorized to edit the view, false otherwise
     */
    public boolean canEditView(View view, ViewModel model, Person user);

    /**
     * Checks whether the mask authorization exists for the given property and if so whether the given user has the
     * ability to unmask the value
     *
     * @param view - view instance the field belongs to
     * @param model - object containing the view data
     * @param field - field associated for the property and from which the
     * {@link org.kuali.rice.krad.uif.component.ComponentSecurity} will be retrieved
     * @param propertyName - name of the property associated with the field
     * @param user - user we are authorizing
     * @return boolean true if the value can be unmasked, false if it should be masked
     */
    public boolean canUnmaskField(View view, ViewModel model, DataField field, String propertyName, Person user);

    /**
     * Checks whether the partial mask authorization exists for the given property and if so whether the given user
     * has the ability to unmask the value
     *
     * @param view - view instance the field belongs to
     * @param model - object containing the view data
     * @param field - field associated for the property and from which the
     * {@link org.kuali.rice.krad.uif.component.ComponentSecurity} will be retrieved
     * @param propertyName - name of the property associated with the field
     * @param user - user we are authorizing
     * @return boolean true if the value can be unmasked, false if it should be partially masked
     */
    public boolean canPartialUnmaskField(View view, ViewModel model, DataField field, String propertyName, Person user);

    public boolean canEditField(View view, ViewModel model, Field field, String propertyName, Person user);

    public boolean canViewField(View view, ViewModel model, Field field, String propertyName, Person user);

    public boolean canEditGroup(View view, ViewModel model, Group group, String groupId, Person user);

    public boolean canViewGroup(View view, ViewModel model, Group group, String groupId, Person user);

    public boolean canEditWidget(View view, ViewModel model, Widget widget, String widgetId, Person user);

    public boolean canViewWidget(View view, ViewModel model, Widget widget, String widgetId, Person user);

    public boolean canPerformAction(View view, ViewModel model, Action action, String actionEvent,
            String actionId, Person user);

    public boolean canEditLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Person user);

    public boolean canViewLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Person user);

    public boolean canEditLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName, Person user);

    public boolean canViewLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName, Person user);

    public boolean canPerformLineAction(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Action action, String actionEvent, String actionId,
            Person user);

}
