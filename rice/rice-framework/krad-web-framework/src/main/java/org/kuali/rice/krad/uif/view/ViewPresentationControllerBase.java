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

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.widget.Widget;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of {@link ViewPresentationController} that implements no logic by default
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "viewPresentationController-bean")
public class ViewPresentationControllerBase implements ViewPresentationController, Serializable {
    private static final long serialVersionUID = -3199587372204398503L;

    /**
     * @see ViewPresentationController#getActionFlags(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.web.form.UifFormBase)
     */
    public Set<String> getActionFlags(View view, UifFormBase model) {
        return new HashSet<String>();
    }

    /**
     * @see ViewPresentationController#getEditModes(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.web.form.UifFormBase)
     */
    public Set<String> getEditModes(View view, UifFormBase model) {
        return new HashSet<String>();
    }

    /**
     * @see ViewPresentationController#canEditView(org.kuali.rice.krad.uif.view.View, org.kuali.rice.krad.uif.view.ViewModel)
     */
    public boolean canEditView(View view, ViewModel model) {
        return true;
    }

    /**
     * @see ViewPresentationController#canEditField(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.field.Field, java.lang.String)
     */
    public boolean canEditField(View view, ViewModel model, Field field, String propertyName) {
        return true;
    }

    /**
     * @see ViewPresentationController#canViewField(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.field.Field, java.lang.String)
     */
    public boolean canViewField(View view, ViewModel model, Field field, String propertyName) {
        return true;
    }

    /**
     * @see ViewPresentationController#fieldIsRequired(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.field.Field, java.lang.String)
     */
    public boolean fieldIsRequired(View view, ViewModel model, Field field, String propertyName) {
        return false;
    }

    /**
     * @see ViewPresentationController#canEditGroup(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.Group, java.lang.String)
     */
    public boolean canEditGroup(View view, ViewModel model, Group group, String groupId) {
        return true;
    }

    /**
     * @see ViewPresentationController#canViewGroup(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.Group, java.lang.String)
     */
    public boolean canViewGroup(View view, ViewModel model, Group group, String groupId) {
        return true;
    }

    /**
     * @see ViewPresentationController#canEditWidget(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.widget.Widget, java.lang.String)
     */
    public boolean canEditWidget(View view, ViewModel model, Widget widget, String widgetId) {
        return true;
    }

    /**
     * @see ViewPresentationController#canViewWidget(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.widget.Widget, java.lang.String)
     */
    public boolean canViewWidget(View view, ViewModel model, Widget widget, String widgetId) {
        return true;
    }

    /**
     * @see ViewPresentationController#canPerformAction(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.element.Action, java.lang.String,
     * java.lang.String)
     */
    public boolean canPerformAction(View view, ViewModel model, Action action, String actionEvent,
            String actionId) {
        return true;
    }

    /**
     * @see ViewPresentationController#canEditLine(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.CollectionGroup,
     * java.lang.String, java.lang.Object)
     */
    public boolean canEditLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line) {
        return true;
    }

    /**
     * @see ViewPresentationController#canViewLine(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.CollectionGroup,
     * java.lang.String, java.lang.Object)
     */
    public boolean canViewLine(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line) {
        return true;
    }

    /**
     * @see ViewPresentationController#canEditLineField(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.CollectionGroup,
     * java.lang.String, java.lang.Object, org.kuali.rice.krad.uif.field.Field, java.lang.String)
     */
    public boolean canEditLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName) {
        return true;
    }

    /**
     * @see ViewPresentationController#canViewLineField(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.CollectionGroup,
     * java.lang.String, java.lang.Object, org.kuali.rice.krad.uif.field.Field, java.lang.String)
     */
    public boolean canViewLineField(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Field field, String propertyName) {
        return true;
    }

    /**
     * @see ViewPresentationController#canPerformLineAction(org.kuali.rice.krad.uif.view.View,
     * org.kuali.rice.krad.uif.view.ViewModel, org.kuali.rice.krad.uif.container.CollectionGroup,
     * java.lang.String, java.lang.Object, org.kuali.rice.krad.uif.element.Action, java.lang.String,
     * java.lang.String)
     */
    public boolean canPerformLineAction(View view, ViewModel model, CollectionGroup collectionGroup,
            String collectionPropertyName, Object line, Action action, String actionEvent, String actionId) {
        return true;
    }

}
