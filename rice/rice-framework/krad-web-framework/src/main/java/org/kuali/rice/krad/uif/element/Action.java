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
package org.kuali.rice.krad.uif.element;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentSecurity;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.view.FormView;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Field that presents an action that can be taken on the UI such as submitting
 * the form or invoking a script
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "action-bean", parent = "Uif-Action"),
        @BeanTag(name = "actionImage-bean", parent = "Uif-ActionImage"),
        @BeanTag(name = "primaryActionButton-bean", parent = "Uif-PrimaryActionButton"),
        @BeanTag(name = "secondaryActionButton-bean", parent = "Uif-SecondaryActionButton"),
        @BeanTag(name = "primaryActionButton-small-bean", parent = "Uif-PrimaryActionButton-Small"),
        @BeanTag(name = "secondaryActionButton-small-bean", parent = "Uif-SecondaryActionButton-Small"),
        @BeanTag(name = "actionLink-bean", parent = "Uif-ActionLink"),
        @BeanTag(name = "navigationActionLink-bean", parent = "Uif-NavigationActionLink"),
        @BeanTag(name = "navigationActionButton-bean", parent = "Uif-NavigationActionButton"),
        @BeanTag(name = "secondaryNavigationActionButton-bean", parent = "Uif-SecondaryNavigationActionButton"),
        @BeanTag(name = "helpAction-bean", parent = "Uif-HelpAction"),
        @BeanTag(name = "saveAction-bean", parent = "Uif-SaveAction"),
        @BeanTag(name = "backAction-bean", parent = "Uif-BackAction"),
        @BeanTag(name = "cancelAction-bean", parent = "Uif-CancelAction"),
        @BeanTag(name = "checkFormAction-bean", parent = "Uif-CheckFormAction"),
        @BeanTag(name = "addLineAction-bean", parent = "Uif-AddLineAction"),
        @BeanTag(name = "deleteLineAction-bean", parent = "Uif-DeleteLineAction"),
        @BeanTag(name = "saveLineAction-bean", parent = "Uif-SaveLineAction"),
        @BeanTag(name = "addBlankLineAction-bean", parent = "Uif-AddBlankLineAction"),
        @BeanTag(name = "addViaLightBoxAction-bean", parent = "Uif-AddViaLightBoxAction"),
        @BeanTag(name = "toggleRowDetailsAction-bean", parent = "Uif-ToggleRowDetailsAction"),
        @BeanTag(name = "expandDetailsAction-bean", parent = "Uif-ExpandDetailsAction"),
        @BeanTag(name = "expandDetailsImageAction-bean", parent = "Uif-ExpandDetailsImageAction"),
        @BeanTag(name = "jumpToTopLink-bean", parent = "Uif-JumpToTopLink"),
        @BeanTag(name = "jumpToBottomLink-bean", parent = "Uif-JumpToBottomLink"),
        @BeanTag(name = "expandDisclosuresButton-bean", parent = "Uif-ExpandDisclosuresButton"),
        @BeanTag(name = "collapseDisclosuresButton-bean", parent = "Uif-CollapseDisclosuresButton"),
        @BeanTag(name = "showInactiveCollectionItemsButton-bean", parent = "Uif-ShowInactiveCollectionItemsButton"),
        @BeanTag(name = "hideInactiveCollectionItemsButton-bean", parent = "Uif-HideInactiveCollectionItemsButton"),
        @BeanTag(name = "collectionQuickFinderAction-bean", parent = "Uif-CollectionQuickFinderAction")})
public class Action extends ContentElementBase {
    private static final long serialVersionUID = 1025672792657238829L;

    private String methodToCall;
    private String actionEvent;
    private String navigateToPageId;

    private String actionScript;

    private String actionLabel;
    private Image actionImage;
    private String actionImagePlacement;

    private String jumpToIdAfterSubmit;
    private String jumpToNameAfterSubmit;
    private String focusOnIdAfterSubmit;

    private boolean performClientSideValidation;
    private boolean performDirtyValidation;
    private boolean clearDirtyOnAction;
    private boolean dirtyOnAction;

    private String preSubmitCall;
    private boolean ajaxSubmit;

    private String ajaxReturnType;
    private String refreshId;
    private String refreshPropertyName;

    private String successCallback;
    private String errorCallback;

    private String loadingMessageText;
    private boolean disableBlocking;

    private Map<String, String> additionalSubmitData;
    private Map<String, String> actionParameters;

    private boolean evaluateDisabledOnKeyUp;

    private boolean disabled;
    private String disabledReason;
    private String disabledExpression;
    private String disabledConditionJs;
    private List<String> disabledConditionControlNames;

    private List<String> disabledWhenChangedPropertyNames;
    private List<String> enabledWhenChangedPropertyNames;

    public Action() {
        super();

        actionImagePlacement = UifConstants.Position.LEFT.name();

        ajaxSubmit = true;

        successCallback = "";
        errorCallback = "";
        preSubmitCall = "";

        additionalSubmitData = new HashMap<String, String>();
        actionParameters = new HashMap<String, String>();

        disabled = false;
        disabledWhenChangedPropertyNames = new ArrayList<String>();
        enabledWhenChangedPropertyNames = new ArrayList<String>();
    }

    /**
     * Sets the disabledExpression, if any, evaluates it and sets the disabled property
     *
     * @param view view instance to which the component belongs
     * @param model top level object containing the data (could be the form or a
     * @param parent
     */
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        disabledExpression = this.getPropertyExpression("disabled");
        if (disabledExpression != null) {
            ExpressionEvaluator expressionEvaluator = view.getViewHelperService().getExpressionEvaluator();

            disabledExpression = expressionEvaluator.replaceBindingPrefixes(view, this, disabledExpression);
            disabled = (Boolean) expressionEvaluator.evaluateExpression(this.getContext(), disabledExpression);
        }
    }

    /**
     * The following finalization is performed:
     *
     * <ul>
     * <li>Add methodToCall action parameter if set and setup event code for
     * setting action parameters</li>
     * <li>Invoke method to build the data attributes and submit data for the action</li>
     * <li>Compose the final onclick script for the action</li>
     * <li>Parses the disabled expressions, if any, to equivalent javascript and evaluates the disable/enable when
     * changed property names</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        ExpressionEvaluator expressionEvaluator = view.getViewHelperService().getExpressionEvaluator();

        if (StringUtils.isNotEmpty(disabledExpression)
                && !disabledExpression.equalsIgnoreCase("true")
                && !disabledExpression.equalsIgnoreCase("false")) {
            disabledConditionControlNames = new ArrayList<String>();
            disabledConditionJs = ExpressionUtils.parseExpression(disabledExpression, disabledConditionControlNames);
        }

        List<String> adjustedDisablePropertyNames = new ArrayList<String>();
        for (String propertyName : disabledWhenChangedPropertyNames) {
            adjustedDisablePropertyNames.add(expressionEvaluator.replaceBindingPrefixes(view, this, propertyName));
        }
        disabledWhenChangedPropertyNames = adjustedDisablePropertyNames;

        List<String> adjustedEnablePropertyNames = new ArrayList<String>();
        for (String propertyName : enabledWhenChangedPropertyNames) {
            adjustedEnablePropertyNames.add(expressionEvaluator.replaceBindingPrefixes(view, this, propertyName));
        }
        enabledWhenChangedPropertyNames = adjustedEnablePropertyNames;

        // clear alt text to avoid screen reader confusion when using image in button with text
        if (actionImage != null && StringUtils.isNotBlank(actionImagePlacement) && StringUtils.isNotBlank(
                actionLabel)) {
            actionImage.setAltText("");
        }

        if (!actionParameters.containsKey(UifConstants.UrlParams.ACTION_EVENT) && StringUtils.isNotBlank(actionEvent)) {
            actionParameters.put(UifConstants.UrlParams.ACTION_EVENT, actionEvent);
        }

        if (StringUtils.isNotBlank(navigateToPageId)) {
            actionParameters.put(UifParameters.NAVIGATE_TO_PAGE_ID, navigateToPageId);
            if (StringUtils.isBlank(methodToCall)) {
                this.methodToCall = UifConstants.MethodToCallNames.NAVIGATE;
            }
        }

        if (!actionParameters.containsKey(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME) && StringUtils
                .isNotBlank(methodToCall)) {
            actionParameters.put(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME, methodToCall);
        }

        setupRefreshAction(view);

        buildActionData(view, model, parent);

        // build final onclick script
        String onClickScript = this.getOnClickScript();
        if (StringUtils.isNotBlank(actionScript)) {
            onClickScript = ScriptUtils.appendScript(onClickScript, actionScript);
        } else {
            onClickScript = ScriptUtils.appendScript(onClickScript, "actionInvokeHandler(this);");
        }

        // add dirty check if it is enabled for the view and the action requires it
        if (view instanceof FormView) {
            if (((FormView) view).isApplyDirtyCheck() && performDirtyValidation) {
                onClickScript = "if (dirtyFormState.checkDirty(e) == false) { " + onClickScript + " ; } ";
            }
        }

        //stop action if the action is disabled
        if (disabled) {
            this.addStyleClass("disabled");
            this.setSkipInTabOrder(true);
        }
        onClickScript = "if(jQuery(this).hasClass('disabled')){ return false; }" + onClickScript;

        //on click script becomes a data attribute for use in a global handler on the client
        this.addDataAttribute(UifConstants.DataAttributes.ONCLICK, KRADUtils.convertToHTMLAttributeSafeString(
                "e.preventDefault();" + onClickScript));
    }

    /**
     * When the action is updating a component sets up the refresh script for the component (found by the
     * given refresh id or refresh property name)
     *
     * @param view view instance the action belongs to
     */
    protected void setupRefreshAction(View view) {
        // if refresh property or id is given, make return type update component
        // TODO: what if the refresh id is the page id? we should set the return type as update page
        if (StringUtils.isNotBlank(refreshPropertyName) || StringUtils.isNotBlank(refreshId)) {
            ajaxReturnType = UifConstants.AjaxReturnTypes.UPDATECOMPONENT.getKey();
        }

        // if refresh property name is given, adjust the binding and then attempt to find the
        // component in the view index
        Component refreshComponent = null;
        if (StringUtils.isNotBlank(refreshPropertyName)) {
            // TODO: does this support all binding prefixes?
            if (refreshPropertyName.startsWith(UifConstants.NO_BIND_ADJUST_PREFIX)) {
                refreshPropertyName = StringUtils.removeStart(refreshPropertyName, UifConstants.NO_BIND_ADJUST_PREFIX);
            } else if (StringUtils.isNotBlank(view.getDefaultBindingObjectPath())) {
                refreshPropertyName = view.getDefaultBindingObjectPath() + "." + refreshPropertyName;
            }

            DataField dataField = view.getViewIndex().getDataFieldByPath(refreshPropertyName);
            if (dataField != null) {
                refreshComponent = dataField;
                refreshId = refreshComponent.getId();
            }
        } else if (StringUtils.isNotBlank(refreshId)) {
            Component component = view.getViewIndex().getComponentById(refreshId);
            if (component != null) {
                refreshComponent = component;
            }
        }

        if (refreshComponent != null) {
            refreshComponent.setRefreshedByAction(true);

            // update initial state
            Component initialComponent = view.getViewIndex().getInitialComponentStates().get(
                    refreshComponent.getBaseId());
            if (initialComponent != null) {
                initialComponent.setRefreshedByAction(true);
                view.getViewIndex().getInitialComponentStates().put(refreshComponent.getBaseId(), initialComponent);
            }
        }
    }

    /**
     * Builds the data attributes that will be read client side to determine how to
     * handle the action and the additional data that should be submitted with the action
     *
     * <p>
     * Note these data attributes will be exposed as a data map client side. The simple attributes (non object
     * value) are also written out as attributes on the action element.
     * </p>
     *
     * @param view view instance the action belongs to
     * @param model model object containing the view data
     * @param parent component the holds the action
     */
    protected void buildActionData(View view, Object model, Component parent) {
        // map properties to data attributes
        addDataAttribute("ajaxsubmit", Boolean.toString(ajaxSubmit));
        addDataAttributeIfNonEmpty("successcallback", this.successCallback);
        addDataAttributeIfNonEmpty("errorcallback", this.errorCallback);
        addDataAttributeIfNonEmpty("presubmitcall", this.preSubmitCall);
        addDataAttributeIfNonEmpty("loadingmessage", this.loadingMessageText);
        addDataAttributeIfNonEmpty("disableblocking", Boolean.toString(this.disableBlocking));
        addDataAttributeIfNonEmpty("ajaxreturntype", this.ajaxReturnType);
        addDataAttributeIfNonEmpty("refreshid", this.refreshId);
        addDataAttribute("validate", Boolean.toString(this.performClientSideValidation));
        addDataAttribute("dirtyOnAction", Boolean.toString(this.dirtyOnAction));
        addDataAttribute("clearDirtyOnAction", Boolean.toString(this.clearDirtyOnAction));

        // all action parameters should be submitted with action
        Map<String, String> submitData = new HashMap<String, String>();
        for (String key : actionParameters.keySet()) {
            String parameterPath = key;
            if (!key.equals(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME)) {
                parameterPath = UifPropertyPaths.ACTION_PARAMETERS + "[" + key + "]";
            }
            submitData.put(parameterPath, actionParameters.get(key));
        }

        for (String key : additionalSubmitData.keySet()) {
            submitData.put(key, additionalSubmitData.get(key));
        }

        // if focus id not set default to focus on action
        if (focusOnIdAfterSubmit == null) {
            focusOnIdAfterSubmit = UifConstants.Order.SELF.toString();
        }

        if (focusOnIdAfterSubmit.equalsIgnoreCase(UifConstants.Order.SELF.toString())) {
            focusOnIdAfterSubmit = this.getId();
            submitData.put("focusId", focusOnIdAfterSubmit);
        } else if (focusOnIdAfterSubmit.equalsIgnoreCase(UifConstants.Order.NEXT_INPUT.toString())) {
            focusOnIdAfterSubmit = UifConstants.Order.NEXT_INPUT.toString() + ":" + this.getId();
            submitData.put("focusId", focusOnIdAfterSubmit);
        } else {
            // Use the id passed in
            submitData.put("focusId", focusOnIdAfterSubmit);
        }

        // if jump to not set default to jump to location of the action
        if (StringUtils.isBlank(jumpToIdAfterSubmit) && StringUtils.isBlank(jumpToNameAfterSubmit)) {
            jumpToIdAfterSubmit = this.getId();
            submitData.put("jumpToId", jumpToIdAfterSubmit);
        } else if (StringUtils.isNotBlank(jumpToIdAfterSubmit)) {
            submitData.put("jumpToId", jumpToIdAfterSubmit);
        } else {
            submitData.put("jumpToName", jumpToNameAfterSubmit);
        }

        addDataAttribute(UifConstants.DataAttributes.SUBMIT_DATA, ScriptUtils.toJSON(submitData));
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(actionImage);

        return components;
    }

    /**
     * Name of the method that should be called when the action is selected
     *
     * <p>
     * For a server side call (clientSideCall is false), gives the name of the
     * method in the mapped controller that should be invoked when the action is
     * selected. For client side calls gives the name of the script function
     * that should be invoked when the action is selected
     * </p>
     *
     * @return name of method to call
     */
    @BeanTagAttribute(name = "methodToCall")
    public String getMethodToCall() {
        return this.methodToCall;
    }

    /**
     * Setter for the actions method to call
     *
     * @param methodToCall
     */
    public void setMethodToCall(String methodToCall) {
        this.methodToCall = methodToCall;
    }

    /**
     * Label text for the action
     *
     * <p>
     * The label text is used by the template renderers to give a human readable
     * label for the action. For buttons this generally is the button text,
     * while for an action link it would be the links displayed text
     * </p>
     *
     * @return label for action
     */
    @BeanTagAttribute(name = "actionLabel")
    public String getActionLabel() {
        return this.actionLabel;
    }

    /**
     * Setter for the actions label
     *
     * @param actionLabel
     */
    public void setActionLabel(String actionLabel) {
        this.actionLabel = actionLabel;
    }

    /**
     * Image to use for the action
     *
     * <p>
     * When the action image component is set (and render is true) the image will be
     * used to present the action as opposed to the default (input submit). For
     * action link templates the image is used for the link instead of the
     * action link text
     * </p>
     *
     * @return action image
     */
    @BeanTagAttribute(name = "actionImage", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Image getActionImage() {
        return this.actionImage;
    }

    /**
     * Setter for the action image field
     *
     * @param actionImage
     */
    public void setActionImage(Image actionImage) {
        this.actionImage = actionImage;
    }

    /**
     * For an <code>Action</code> that is part of a
     * <code>NavigationGroup</code>, the navigate to page id can be set to
     * configure the page that should be navigated to when the action is
     * selected
     *
     * <p>
     * Support exists in the <code>UifControllerBase</code> for handling
     * navigation between pages
     * </p>
     *
     * @return id of page that should be rendered when the action item is
     *         selected
     */
    @BeanTagAttribute(name = "navigateToPageId")
    public String getNavigateToPageId() {
        return this.navigateToPageId;
    }

    /**
     * Setter for the navigate to page id
     *
     * @param navigateToPageId
     */
    public void setNavigateToPageId(String navigateToPageId) {
        this.navigateToPageId = navigateToPageId;
    }

    /**
     * Name of the event that will be set when the action is invoked
     *
     * <p>
     * Action events can be looked at by the view or components in order to render differently depending on
     * the action requested.
     * </p>
     *
     * @return action event name
     * @see org.kuali.rice.krad.uif.UifConstants.ActionEvents
     */
    @BeanTagAttribute(name = "actionEvent")
    public String getActionEvent() {
        return actionEvent;
    }

    /**
     * Setter for the action event
     *
     * @param actionEvent
     */
    public void setActionEvent(String actionEvent) {
        this.actionEvent = actionEvent;
    }

    /**
     * Map of additional data that will be posted when the action is invoked
     *
     * <p>
     * Each entry in this map will be sent as a request parameter when the action is chosen. Note this in
     * addition to the form data that is sent. For example, suppose the model contained a property named
     * number and a boolean named showActive, we can send values for this properties by adding the following
     * entries to this map:
     * {'number':'a13', 'showActive', 'true'}
     * </p>
     *
     * <p>
     * The additionalSubmitData map is different from the actionParameters map. All name/value pairs given as
     * actionParameters populated the form map actionParameters. While name/value pair given in additionalSubmitData
     * populate different form (model) properties
     * </p>
     *
     * @return additional key/value pairs to submit
     */
    @BeanTagAttribute(name = "additionalSubmitData", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalSubmitData() {
        return additionalSubmitData;
    }

    /**
     * Setter for map holding additional data to post
     *
     * @param additionalSubmitData
     */
    public void setAdditionalSubmitData(Map<String, String> additionalSubmitData) {
        this.additionalSubmitData = additionalSubmitData;
    }

    /**
     * Parameters that should be sent when the action is invoked
     *
     * <p>
     * Action renderer will decide how the parameters are sent for the action
     * (via script generated hiddens, or script parameters, ...)
     * </p>
     *
     * <p>
     * Can be set by other components such as the <code>CollectionGroup</code>
     * to provide the context the action is in (such as the collection name and
     * line the action applies to)
     * </p>
     *
     * @return action parameters
     */
    @BeanTagAttribute(name = "actionParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getActionParameters() {
        return this.actionParameters;
    }

    /**
     * Setter for the action parameters
     *
     * @param actionParameters
     */
    public void setActionParameters(Map<String, String> actionParameters) {
        this.actionParameters = actionParameters;
    }

    /**
     * Convenience method to add a parameter to the action parameters Map
     *
     * @param parameterName name of parameter to add
     * @param parameterValue value of parameter to add
     */
    public void addActionParameter(String parameterName, String parameterValue) {
        if (actionParameters == null) {
            this.actionParameters = new HashMap<String, String>();
        }

        this.actionParameters.put(parameterName, parameterValue);
    }

    /**
     * Get an actionParameter by name
     */
    public String getActionParameter(String parameterName) {
        return this.actionParameters.get(parameterName);
    }

    /**
     * Action Security object that indicates what authorization (permissions) exist for the action
     *
     * @return ActionSecurity instance
     */
    public ActionSecurity getActionSecurity() {
        return (ActionSecurity) super.getComponentSecurity();
    }

    /**
     * Override to assert a {@link ActionSecurity} instance is set
     *
     * @param componentSecurity instance of ActionSecurity
     */
    @Override
    public void setComponentSecurity(ComponentSecurity componentSecurity) {
        if ((componentSecurity != null) && !(componentSecurity instanceof ActionSecurity)) {
            throw new RiceRuntimeException("Component security for Action should be instance of ActionSecurity");
        }

        super.setComponentSecurity(componentSecurity);
    }

    @Override
    protected Class<? extends ComponentSecurity> getComponentSecurityClass() {
        return ActionSecurity.class;
    }

    /**
     * @return the jumpToIdAfterSubmit
     */
    @BeanTagAttribute(name = "jumpToIdAfterSubmit")
    public String getJumpToIdAfterSubmit() {
        return this.jumpToIdAfterSubmit;
    }

    /**
     * The id to jump to in the next page, the element with this id will be
     * jumped to automatically when the new page is retrieved after a submit.
     * Using "TOP" or "BOTTOM" will jump to the top or the bottom of the
     * resulting page. Passing in nothing for both jumpToIdAfterSubmit and
     * jumpToNameAfterSubmit will result in this Action being jumped to by
     * default if it is present on the new page. WARNING: jumpToIdAfterSubmit
     * always takes precedence over jumpToNameAfterSubmit, if set.
     *
     * @param jumpToIdAfterSubmit the jumpToIdAfterSubmit to set
     */
    public void setJumpToIdAfterSubmit(String jumpToIdAfterSubmit) {
        this.jumpToIdAfterSubmit = jumpToIdAfterSubmit;
    }

    /**
     * The name to jump to in the next page, the element with this name will be
     * jumped to automatically when the new page is retrieved after a submit.
     * Passing in nothing for both jumpToIdAfterSubmit and jumpToNameAfterSubmit
     * will result in this Action being jumped to by default if it is
     * present on the new page. WARNING: jumpToIdAfterSubmit always takes
     * precedence over jumpToNameAfterSubmit, if set.
     *
     * @return the jumpToNameAfterSubmit
     */
    @BeanTagAttribute(name = "jumpToNameAfterSubmit")
    public String getJumpToNameAfterSubmit() {
        return this.jumpToNameAfterSubmit;
    }

    /**
     * @param jumpToNameAfterSubmit the jumpToNameAfterSubmit to set
     */
    public void setJumpToNameAfterSubmit(String jumpToNameAfterSubmit) {
        this.jumpToNameAfterSubmit = jumpToNameAfterSubmit;
    }

    /**
     * The element to place focus on in the new page after the new page
     * is retrieved.
     *
     * <p>The following are allowed:
     * <ul>
     * <li>A valid element id</li>
     * <li>"FIRST" will focus on the first visible input element on the form</li>
     * <li>"SELF" will result in this Action being focused (action bean defaults to "SELF")</li>
     * <li>"LINE_FIRST" will result in the first input of the collection line to be focused (if available)</li>
     * <li>"NEXT_INPUT" will result in the next available input that exists after this Action to be focused
     * (only if this action still exists on the page)</li>
     * </ul>
     * </p>
     *
     * @return the focusOnAfterSubmit
     */
    @BeanTagAttribute(name = "focusOnIdAfterSubmit")
    public String getFocusOnIdAfterSubmit() {
        return this.focusOnIdAfterSubmit;
    }

    /**
     * @param focusOnIdAfterSubmit the focusOnAfterSubmit to set
     */
    public void setFocusOnIdAfterSubmit(String focusOnIdAfterSubmit) {
        this.focusOnIdAfterSubmit = focusOnIdAfterSubmit;
    }

    /**
     * Indicates whether the form data should be validated on the client side
     *
     * return true if validation should occur, false otherwise
     */
    @BeanTagAttribute(name = "performClientSideValidation")
    public boolean isPerformClientSideValidation() {
        return this.performClientSideValidation;
    }

    /**
     * Setter for the client side validation flag
     *
     * @param performClientSideValidation
     */
    public void setPerformClientSideValidation(boolean performClientSideValidation) {
        this.performClientSideValidation = performClientSideValidation;
    }

    /**
     * Client side javascript to be executed when this actionField is clicked
     *
     * <p>
     * This overrides the default action for this Action so the method
     * called must explicitly submit, navigate, etc. through js, if necessary.
     * In addition, this js occurs AFTER onClickScripts set on this field, it
     * will be the last script executed by the click event. Sidenote: This js is
     * always called after hidden actionParameters and methodToCall methods are
     * written by the js to the html form.
     * </p>
     *
     * @return the actionScript
     */
    @BeanTagAttribute(name = "actionScript")
    public String getActionScript() {
        return this.actionScript;
    }

    /**
     * @param actionScript the actionScript to set
     */
    public void setActionScript(String actionScript) {
        if (StringUtils.isNotBlank(actionScript) && !StringUtils.endsWith(actionScript, ";")) {
            actionScript = actionScript + ";";
        }

        this.actionScript = actionScript;
    }

    /**
     * @param performDirtyValidation the blockValidateDirty to set
     */
    public void setPerformDirtyValidation(boolean performDirtyValidation) {
        this.performDirtyValidation = performDirtyValidation;
    }

    /**
     * @return the blockValidateDirty
     */
    @BeanTagAttribute(name = "performDirtyValidation")
    public boolean isPerformDirtyValidation() {
        return performDirtyValidation;
    }

    /**
     * True to make this action clear the dirty flag before submitting
     *
     * <p>This will clear both the dirtyForm flag on the form and the count of fields considered dirty on the
     * client-side.  This will only be performed if this action is a request based action.</p>
     *
     * @return true if the dirty
     */
    @BeanTagAttribute(name = "clearDirtyOnAction")
    public boolean isClearDirtyOnAction() {
        return clearDirtyOnAction;
    }

    /**
     * Set clearDirtyOnAction
     *
     * @param clearDirtyOnAction
     */
    public void setClearDirtyOnAction(boolean clearDirtyOnAction) {
        this.clearDirtyOnAction = clearDirtyOnAction;
    }

    /**
     * When true, this action will mark the form dirty by incrementing the dirty field count, but if this action
     * refreshes the entire view this will be lost (most actions only refresh the page)
     *
     * <p>This will increase count of fields considered dirty on the
     * client-side by 1.  This will only be performed if this action is a request based action.</p>
     *
     * @return true if this action is considered dirty, false otherwise
     */
    @BeanTagAttribute(name = "dirtyOnAction")
    public boolean isDirtyOnAction() {
        return dirtyOnAction;
    }

    /**
     * Set to true, if this action is considered one that changes the form's data (makes the form dirty)
     *
     * @param dirtyOnAction
     */
    public void setDirtyOnAction(boolean dirtyOnAction) {
        this.dirtyOnAction = dirtyOnAction;
    }

    /**
     * Indicates whether the action (input or button) is disabled (doesn't allow interaction)
     *
     * @return true if the action field is disabled, false if not
     */
    @BeanTagAttribute(name = "disabled")
    public boolean isDisabled() {
        return disabled;
    }

    /**
     * Setter for the disabled indicator
     *
     * @param disabled
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * If the action field is disabled, gives a reason for why which will be displayed as a tooltip
     * on the action field (button)
     *
     * @return disabled reason text
     * @see #isDisabled()
     */
    @BeanTagAttribute(name = "disabledReason")
    public String getDisabledReason() {
        return disabledReason;
    }

    /**
     * Setter for the disabled reason text
     *
     * @param disabledReason
     */
    public void setDisabledReason(String disabledReason) {
        this.disabledReason = disabledReason;
    }

    @BeanTagAttribute(name = "actionImagePlacement")
    public String getActionImagePlacement() {
        return actionImagePlacement;
    }

    /**
     * Set to TOP, BOTTOM, LEFT, RIGHT to position image at that location within the button.
     * For the subclass ActionLinkField only LEFT and RIGHT are allowed.  When set to blank/null/IMAGE_ONLY, the image
     * itself will be the Action, if no value is set the default is ALWAYS LEFT, you must explicitly set
     * blank/null/IMAGE_ONLY to use ONLY the image as the Action.
     *
     * @return
     */
    public void setActionImagePlacement(String actionImagePlacement) {
        this.actionImagePlacement = actionImagePlacement;
    }

    /**
     * Gets the script which needs to be invoked before the form is submitted
     *
     * <p>
     * The preSubmitCall can carry out custom logic for the action before the submit occurs. The value should
     * be given as one or more lines of script and should return a boolean. If false is returned from the call,
     * the submit is not carried out. Furthermore, the preSubmitCall can refer to the request object through the
     * variable 'kradRequest' or 'this'. This gives full access over the request for doing such things as
     * adding additional data
     * </p>
     *
     * <p>
     * Examples 'return doFunction(kradRequest);', 'var valid=true;return valid;'
     * </p>
     *
     * <p>
     * The preSubmit call will be invoked both for ajax and non-ajax submits
     * </p>
     *
     * @return script text that will be invoked before form submission
     */
    @BeanTagAttribute(name = "preSubmitCall")
    public String getPreSubmitCall() {
        return preSubmitCall;
    }

    /**
     * Setter for preSubmitCall
     *
     * @param preSubmitCall
     */
    public void setPreSubmitCall(String preSubmitCall) {
        this.preSubmitCall = preSubmitCall;
    }

    /**
     * When this property is set to true it will submit the form using Ajax instead of the browser submit. Will default
     * to updating the page contents
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "ajaxSubmit")
    public boolean isAjaxSubmit() {
        return ajaxSubmit;
    }

    /**
     * Setter for ajaxSubmit
     *
     * @param ajaxSubmit
     */
    public void setAjaxSubmit(boolean ajaxSubmit) {
        this.ajaxSubmit = ajaxSubmit;
    }

    /**
     * Gets the return type for the ajax call
     *
     * <p>
     * The ajax return type indicates how the response content will be handled in the client. Typical
     * examples include updating a component, the page, or doing a redirect.
     * </p>
     *
     * @return return type
     * @see org.kuali.rice.krad.uif.UifConstants.AjaxReturnTypes
     */
    @BeanTagAttribute(name = "ajaxReturnType")
    public String getAjaxReturnType() {
        return this.ajaxReturnType;
    }

    /**
     * Setter for the type of ajax return
     *
     * @param ajaxReturnType
     */
    public void setAjaxReturnType(String ajaxReturnType) {
        this.ajaxReturnType = ajaxReturnType;
    }

    /**
     * Indicates if the action response should be displayed in a lightbox
     *
     * @return true if response should be rendered in a lightbox, false if not
     */
    @BeanTagAttribute(name = "displayResponseInLightBox")
    public boolean isDisplayResponseInLightBox() {
        return StringUtils.equals(this.ajaxReturnType, UifConstants.AjaxReturnTypes.DISPLAYLIGHTBOX.getKey());
    }

    /**
     * Setter for indicating the response should be rendered in a lightbox
     *
     * @param displayResponseInLightBox
     */
    public void setDisplayResponseInLightBox(boolean displayResponseInLightBox) {
        if (displayResponseInLightBox) {
            this.ajaxReturnType = UifConstants.AjaxReturnTypes.DISPLAYLIGHTBOX.getKey();
        }
        // if display lightbox is false and it was previously true, set to default of update page
        else if (StringUtils.equals(this.ajaxReturnType, UifConstants.AjaxReturnTypes.DISPLAYLIGHTBOX.getKey())) {
            this.ajaxReturnType = UifConstants.AjaxReturnTypes.UPDATEPAGE.getKey();
        }
    }

    /**
     * Gets the script which will be invoked on a successful ajax call
     *
     * <p>
     * The successCallback can carry out custom logic after a successful ajax submission has been made. The
     * value can contain one or more script statements. In addition, the response contents can be accessed
     * through the variable 'responseContents'
     * </p>
     *
     * <p>
     * Examples 'handleSuccessfulUpdate(responseContents);'
     * </p>
     *
     * <p>
     * The successCallback may only be specified when {@link #isAjaxSubmit()} is true
     * </p>
     *
     * @return script to be executed when the action is successful
     */
    @BeanTagAttribute(name = "successCallback")
    public String getSuccessCallback() {
        return successCallback;
    }

    /**
     * Setter for successCallback
     *
     * @param successCallback
     */
    public void setSuccessCallback(String successCallback) {
        this.successCallback = successCallback;
    }

    /**
     * Gets the script which will be invoked when the action fails due to problems in the ajax call or
     * the return of an incident report
     *
     * <p>
     * The errorCallback can carry out custom logic after a failed ajax submission. The
     * value can contain one or more script statements. In addition, the response contents can be accessed
     * through the variable 'responseContents'
     * </p>
     *
     * <p>
     * Examples 'handleFailedUpdate(responseContents);'
     * </p>
     *
     * <p>
     * The errorCallback may only be specified when {@link #isAjaxSubmit()} is true
     * </p>
     *
     * @return script to be executed when the action is successful
     */
    @BeanTagAttribute(name = "errorCallback")
    public String getErrorCallback() {
        return errorCallback;
    }

    /**
     * Setter for errorCallback
     *
     * @param errorCallback
     */
    public void setErrorCallback(String errorCallback) {
        this.errorCallback = errorCallback;
    }

    /**
     * Id for the component that should be refreshed after the action completes
     *
     * <p>
     * Either refresh id or refresh property name can be set to configure the component that should
     * be refreshed after the action completes. If both are blank, the page will be refreshed
     * </p>
     *
     * @return valid component id
     */
    @BeanTagAttribute(name = "refreshId")
    public String getRefreshId() {
        return refreshId;
    }

    /**
     * Setter for the component refresh id
     *
     * @param refreshId
     */
    public void setRefreshId(String refreshId) {
        this.refreshId = refreshId;
    }

    /**
     * Property name for the {@link org.kuali.rice.krad.uif.field.DataField} that should be refreshed after the action
     * completes
     *
     * <p>
     * Either refresh id or refresh property name can be set to configure the component that should
     * be refreshed after the action completes. If both are blank, the page will be refreshed
     * </p>
     *
     * <p>
     * Property name will be adjusted to use the default binding path unless it contains the form prefix
     * </p>
     *
     * @return valid property name with an associated DataField
     * @see org.kuali.rice.krad.uif.UifConstants#NO_BIND_ADJUST_PREFIX
     */
    @BeanTagAttribute(name = "refreshPropertyName")
    public String getRefreshPropertyName() {
        return refreshPropertyName;
    }

    /**
     * Setter for the property name of the DataField that should be refreshed
     *
     * @param refreshPropertyName
     */
    public void setRefreshPropertyName(String refreshPropertyName) {
        this.refreshPropertyName = refreshPropertyName;
    }

    /**
     * Gets the loading message used by action's blockUI
     *
     * @returns String if String is not null, used in place of loading message
     */
    @BeanTagAttribute(name = "loadingMessageText")
    public String getLoadingMessageText() {
        return loadingMessageText;
    }

    /**
     * When this property is set, it is used in place of the loading message text used by the blockUI
     *
     * @param loadingMessageText
     */
    public void setLoadingMessageText(String loadingMessageText) {
        this.loadingMessageText = loadingMessageText;
    }

    /**
     * Indicates whether blocking for the action should be disabled
     *
     * <p>
     * By default when an action is invoked part of the page or the entire window is blocked until
     * the action completes. If this property is set to true the blocking will not be displayed.
     * </p>
     *
     * <p>
     * Currently if an action returns a file download, this property should be set to true. If not, the blocking
     * will never get unblocked (because the page does not get notification a file was downloaded)
     * </p>
     *
     * @return true if blocking should be disabled, false if not
     */
    @BeanTagAttribute(name = "disableBlocking")
    public boolean isDisableBlocking() {
        return disableBlocking;
    }

    /**
     * Setter for disabling blocking when the action is invoked
     *
     * @param disableBlocking
     */
    public void setDisableBlocking(boolean disableBlocking) {
        this.disableBlocking = disableBlocking;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that a label or image ui is presence
        if (getActionLabel() == null && getActionImage() == null) {
            String currentValues[] = {"actionLabel =" + getActionLabel(), "actionImage =" + getActionImage()};
            tracer.createError("ActionLabel and/or actionImage must be set", currentValues);
        }

        // Checks that an action is set
        if (getJumpToIdAfterSubmit() != null && getJumpToNameAfterSubmit() != null) {
            String currentValues[] = {"jumpToIdAfterSubmit =" + getJumpToIdAfterSubmit(),
                    "jumpToNameAfterSubmit =" + getJumpToNameAfterSubmit()};
            tracer.createWarning("Only 1 jumpTo property should be set", currentValues);
        }
        super.completeValidation(tracer.getCopy());
    }

    /**
     * Evaluate the disable condition on controls which disable it on each key up event
     *
     * @return true if evaluate on key up, false otherwise
     */
    @BeanTagAttribute(name = "evaluateDisabledOnKeyUp")
    public boolean isEvaluateDisabledOnKeyUp() {
        return evaluateDisabledOnKeyUp;
    }

    /**
     * Set evaluateDisableOnKeyUp
     *
     * @param evaluateDisabledOnKeyUp
     */
    public void setEvaluateDisabledOnKeyUp(boolean evaluateDisabledOnKeyUp) {
        this.evaluateDisabledOnKeyUp = evaluateDisabledOnKeyUp;
    }

    /**
     * Get the disable condition js derived from the springEL, cannot be set.
     *
     * @return the disableConditionJs javascript to be evaluated
     */
    public String getDisabledConditionJs() {
        return disabledConditionJs;
    }

    /**
     * Sets the disabled condition javascript
     *
     * @param disabledConditionJs
     */
    protected void setDisabledConditionJs(String disabledConditionJs) {
        this.disabledConditionJs = disabledConditionJs;
    }

    /**
     * Control names to add handlers to for disable functionality, cannot be set
     *
     * @return control names to add handlers to for disable
     */
    public List<String> getDisabledConditionControlNames() {
        return disabledConditionControlNames;
    }

    /**
     * Set disabled condition control names
     *
     * @param disabledConditionControlNames
     */
    public void setDisabledConditionControlNames(List<String> disabledConditionControlNames) {
        this.disabledConditionControlNames = disabledConditionControlNames;
    }

    /**
     * Gets the property names of fields that when changed, will disable this component
     *
     * @return the property names to monitor for change to disable this component
     */
    @BeanTagAttribute(name = "disabledWhenChangedPropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getDisabledWhenChangedPropertyNames() {
        return disabledWhenChangedPropertyNames;
    }

    /**
     * Sets the property names of fields that when changed, will disable this component
     *
     * @param disabledWhenChangedPropertyNames
     */
    public void setDisabledWhenChangedPropertyNames(List<String> disabledWhenChangedPropertyNames) {
        this.disabledWhenChangedPropertyNames = disabledWhenChangedPropertyNames;
    }

    /**
     * Gets the property names of fields that when changed, will enable this component
     *
     * @return the property names to monitor for change to enable this component
     */
    @BeanTagAttribute(name = "enabledWhenChangedPropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getEnabledWhenChangedPropertyNames() {
        return enabledWhenChangedPropertyNames;
    }

    /**
     * Sets the property names of fields that when changed, will enable this component
     *
     * @param enabledWhenChangedPropertyNames
     */
    public void setEnabledWhenChangedPropertyNames(List<String> enabledWhenChangedPropertyNames) {
        this.enabledWhenChangedPropertyNames = enabledWhenChangedPropertyNames;
    }

    /**
     * Sets the disabled expression
     *
     * @param disabledExpression
     */
    protected void setDisabledExpression(String disabledExpression) {
        this.disabledExpression = disabledExpression;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Action actionCopy = (Action) component;
        actionCopy.setActionEvent(this.actionEvent);

        if (this.actionImage != null) {
            actionCopy.setActionImage((Image) this.actionImage.copy());
        }

        actionCopy.setActionImagePlacement(this.actionImagePlacement);
        actionCopy.setActionLabel(this.actionLabel);

        if (this.actionParameters != null) {
            actionCopy.setActionParameters(new HashMap<String, String>(this.actionParameters));
        }

        if (this.additionalSubmitData != null) {
            actionCopy.setAdditionalSubmitData(new HashMap<String, String>(this.additionalSubmitData));
        }

        actionCopy.setActionScript(this.actionScript);
        actionCopy.setAjaxReturnType(this.ajaxReturnType);
        actionCopy.setAjaxSubmit(this.ajaxSubmit);
        actionCopy.setClearDirtyOnAction(this.clearDirtyOnAction);
        actionCopy.setDirtyOnAction(this.dirtyOnAction);
        actionCopy.setDisableBlocking(this.disableBlocking);
        actionCopy.setDisabled(this.disabled);
        actionCopy.setDisabledConditionJs(this.disabledConditionJs);

        if (this.disabledConditionControlNames != null) {
            actionCopy.setDisabledConditionControlNames(new ArrayList<String>(this.disabledConditionControlNames));
        }

        actionCopy.setDisabledExpression(this.disabledExpression);
        actionCopy.setDisabledReason(this.disabledReason);

        if (this.disabledWhenChangedPropertyNames != null) {
            actionCopy.setDisabledWhenChangedPropertyNames(new ArrayList<String>(
                    this.disabledWhenChangedPropertyNames));
        }

        if (this.enabledWhenChangedPropertyNames != null) {
            actionCopy.setEnabledWhenChangedPropertyNames(new ArrayList<String>(this.enabledWhenChangedPropertyNames));
        }

        actionCopy.setErrorCallback(this.errorCallback);
        actionCopy.setEvaluateDisabledOnKeyUp(this.evaluateDisabledOnKeyUp);
        actionCopy.setFocusOnIdAfterSubmit(this.focusOnIdAfterSubmit);
        actionCopy.setJumpToIdAfterSubmit(this.jumpToIdAfterSubmit);
        actionCopy.setJumpToNameAfterSubmit(this.jumpToNameAfterSubmit);
        actionCopy.setLoadingMessageText(this.loadingMessageText);
        actionCopy.setMethodToCall(this.methodToCall);
        actionCopy.setNavigateToPageId(this.navigateToPageId);
        actionCopy.setPerformClientSideValidation(this.performClientSideValidation);
        actionCopy.setPerformDirtyValidation(this.performDirtyValidation);
        actionCopy.setPreSubmitCall(this.preSubmitCall);
        actionCopy.setRefreshId(this.refreshId);
        actionCopy.setRefreshPropertyName(this.refreshPropertyName);
        actionCopy.setSuccessCallback(this.successCallback);
    }
}
