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

import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.widget.Widget;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.Set;

/**
 * Configured for a <code>View</code> instance to provide conditional authorization logic
 * based on any variable (view configuration, system parameters, ...) that does
 * not depend on the current user
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ViewPresentationController {

    public Set<String> getActionFlags(View view, UifFormBase model);

    public Set<String> getEditModes(View view, UifFormBase model);

    /**
     * Determines if the the given view and data is allowed to be edited
     *
     * @param view - view instance to check whether editing is allowed
     * @param model - object containing the view data
     * @return boolean true if editing on the view is allowed, false otherwise
     */
    public boolean canEditView(View view, ViewModel model);

    /**
     * Determines if the given field within the view is allowed to be edited
     *
     * @param view - view instance the field belongs to
     * @param model - object containing the view data
     * @param field - field instance to determine edit authorization for
     * @param propertyName - name of the property that field corresponds with (if field is data binding)
     * @return boolean true if editing on the field is allowed, false otherwise
     */
    public boolean canEditField(View view, ViewModel model, Field field, String propertyName);

    /**
     * Determines if the given field within the view is allowed to be viewed
     *
     * @param view - view instance the field belongs to
     * @param model - object containing the view data
     * @param field - field instance to determine view authorization for
     * @param propertyName - name of the property that field corresponds with (if field is data binding)
     * @return boolean true if viewing of the field is allowed, false otherwise
     */
    public boolean canViewField(View view, ViewModel model, Field field, String propertyName);

    /**
     * Determines if a value is required to be present for the given field (used to indicate in the client the
     * field must be completed)
     *
     * @param view - view instance the field belongs to
     * @param model - object containing the view data
     * @param field - field instance to determine required state for
     * @param propertyName - name of the property that field corresponds with (if field is data binding)
     * @return boolean true if field is required, false otherwise
     */
    public boolean fieldIsRequired(View view, ViewModel model, Field field, String propertyName);

    public boolean canEditGroup(View view, ViewModel model, Group group, String groupId);

    public boolean canViewGroup(View view, ViewModel model, Group group, String groupId);

    public boolean canEditWidget(View view, ViewModel model, Widget widget, String widgetId);

    public boolean canViewWidget(View view, ViewModel model, Widget widget, String widgetId);

    public boolean canPerformAction(View view, ViewModel model, Action action, String actionEvent,
            String actionId);

    public boolean canEditLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line);

    public boolean canViewLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line);

    public boolean canEditLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName);

    public boolean canViewLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName);

    public boolean canPerformLineAction(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Action action, String actionEvent, String actionId);

}
