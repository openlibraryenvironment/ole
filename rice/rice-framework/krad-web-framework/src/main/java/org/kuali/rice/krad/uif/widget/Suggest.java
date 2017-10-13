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
package org.kuali.rice.krad.uif.widget;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.MethodInvokerConfig;
import org.kuali.rice.krad.uif.field.AttributeQuery;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget that provides dynamic select options to the user as they
 * are entering the value (also known as auto-complete)
 *
 * <p>
 * Widget is backed by an <code>AttributeQuery</code> that provides
 * the configuration for executing a query server side that will retrieve
 * the valid option values
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "suggest-bean", parent = "Uif-Suggest")
public class Suggest extends WidgetBase {
    private static final long serialVersionUID = 7373706855319347225L;

    private AttributeQuery suggestQuery;

    private String valuePropertyName;
    private String labelPropertyName;
    private List<String> additionalPropertiesToReturn;

    private boolean returnFullQueryObject;

    private boolean retrieveAllSuggestions;
    private List<Object> suggestOptions;

    private String suggestOptionsJsString;

    public Suggest() {
        super();
    }

    /**
     * The following updates are done here:
     *
     * <ul>
     * <li>Invoke expression evaluation on the suggestQuery</li>
     * </ul>
     */
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (suggestQuery != null) {
            view.getViewHelperService().getExpressionEvaluator().evaluateExpressionsOnConfigurable(view,
                    suggestQuery, getContext());
        }
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Adjusts the query field mappings on the query based on the binding configuration of the field</li>
     * <li>TODO: determine query if render is true and query is not set</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // check for necessary configuration
        if (!isSuggestConfigured()) {
           setRender(false);
        }

        if (!isRender()) {
            return;
        }

        if (retrieveAllSuggestions) {
            if (suggestOptions == null || suggestOptions.isEmpty()) {
                // execute query method to retrieve up front suggestions
                if (suggestQuery.hasConfiguredMethod()) {
                    retrieveSuggestOptions(view);
                }
            } else {
                suggestOptionsJsString = ScriptUtils.translateValue(suggestOptions);
            }
        } else {
            // adjust from side on query field mapping to match parent fields path
            InputField field = (InputField) parent;

            BindingInfo bindingInfo = field.getBindingInfo();
            suggestQuery.updateQueryFieldMapping(bindingInfo);
        }
    }

    /**
     * Indicates whether the suggest widget has the necessary configuration to render
     *
     * @return true if the necessary configuration is present, false if not
     */
    public boolean isSuggestConfigured() {
        if (StringUtils.isNotBlank(valuePropertyName) ||
                suggestQuery.hasConfiguredMethod() ||
                (suggestOptions != null && !suggestOptions.isEmpty())) {
            return true;
        }

        return false;
    }

    /**
     * Invokes the configured query method and sets the returned method value as the suggest options or
     * suggest options JS string
     *
     * @param view view instance the suggest belongs to, used to get the view helper service if needed
     */
    protected void retrieveSuggestOptions(View view) {
        String queryMethodToCall = suggestQuery.getQueryMethodToCall();
        MethodInvokerConfig queryMethodInvoker = suggestQuery.getQueryMethodInvokerConfig();

        if (queryMethodInvoker == null) {
            queryMethodInvoker = new MethodInvokerConfig();
        }

        // if method not set on invoker, use queryMethodToCall, note staticMethod could be set(don't know since
        // there is not a getter), if so it will override the target method in prepare
        if (StringUtils.isBlank(queryMethodInvoker.getTargetMethod())) {
            queryMethodInvoker.setTargetMethod(queryMethodToCall);
        }

        // if target class or object not set, use view helper service
        if ((queryMethodInvoker.getTargetClass() == null) && (queryMethodInvoker.getTargetObject() == null)) {
            queryMethodInvoker.setTargetObject(view.getViewHelperService());
        }

        try {
            queryMethodInvoker.prepare();

            Object methodResult = queryMethodInvoker.invoke();
            if (methodResult instanceof String) {
                suggestOptionsJsString = (String) methodResult;
            } else if (methodResult instanceof List) {
                suggestOptions = (List<Object>) methodResult;
                suggestOptionsJsString = ScriptUtils.translateValue(suggestOptions);
            } else {
                throw new RuntimeException("Suggest query method did not return List<String> for suggestions");
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke query method: " + queryMethodInvoker.getTargetMethod(), e);
        }
    }

    /**
     * Attribute query instance the will be executed to provide
     * the suggest options
     *
     * @return AttributeQuery
     */
    @BeanTagAttribute(name = "suggestQuery", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public AttributeQuery getSuggestQuery() {
        return suggestQuery;
    }

    /**
     * Setter for the suggest attribute query
     *
     * @param suggestQuery
     */
    public void setSuggestQuery(AttributeQuery suggestQuery) {
        this.suggestQuery = suggestQuery;
    }

    /**
     * Name of the property on the query result object that provides
     * the options for the suggest, values from this field will be
     * collected and sent back on the result to provide as suggest options.
     *
     * <p>If a labelPropertyName is also set,
     * the property specified by it will be used as the label the user selects (the suggestion), but the value will
     * be the value retrieved by this property.  If only one of labelPropertyName or valuePropertyName is set,
     * the property's value on the object will be used for both the value inserted on selection and the suggestion
     * text (most default cases only a valuePropertyName would be set).</p>
     *
     * @return source property name
     */
    @BeanTagAttribute(name = "valuePropertyName")
    public String getValuePropertyName() {
        return valuePropertyName;
    }

    /**
     * Setter for the value property name
     *
     * @param valuePropertyName
     */
    public void setValuePropertyName(String valuePropertyName) {
        this.valuePropertyName = valuePropertyName;
    }

    /**
     * Name of the property on the query result object that provides the label for the suggestion.
     *
     * <p>This should
     * be set when the label that the user selects is different from the value that is inserted when a user selects a
     * suggestion. If only one of labelPropertyName or valuePropertyName is set,
     * the property's value on the object will be used for both the value inserted on selection and the suggestion
     * text (most default cases only a valuePropertyName would be set).</p>
     *
     * @return labelPropertyName representing the property to use for the suggestion label of the item
     */
    @BeanTagAttribute(name = "labelPropertyName")
    public String getLabelPropertyName() {
        return labelPropertyName;
    }

    /**
     * Set the labelPropertyName
     *
     * @param labelPropertyName
     */
    public void setLabelPropertyName(String labelPropertyName) {
        this.labelPropertyName = labelPropertyName;
    }

    /**
     * List of additional properties to return in the result objects to the plugin's success callback.
     *
     * <p>In most cases, this should not be set.  The main use case
     * of setting this list is to use additional properties in the select function on the plugin's options, so
     * it is only recommended that this property be set when doing heavy customization to the select function.
     * This list is not used if the full result object is already being returned.</p>
     *
     * @return the list of additional properties to send back
     */
    @BeanTagAttribute(name = "additionalPropertiesToReturn", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalPropertiesToReturn() {
        return additionalPropertiesToReturn;
    }

    /**
     * Set the list of additional properties to return to the plugin success callback results
     *
     * @param additionalPropertiesToReturn
     */
    public void setAdditionalPropertiesToReturn(List<String> additionalPropertiesToReturn) {
        this.additionalPropertiesToReturn = additionalPropertiesToReturn;
    }

    /**
     * When set to true the results of a query method will be sent back as-is (in translated form) with all properties
     * intact.
     *
     * <p>
     * Note this is not supported for highly complex objects (ie, most auto-query objects - will throw exception).
     * Intended usage of this flag is with custom query methods which return simple data objects.
     * The query method can return a list of Strings which will be used for the suggestions, a list of objects
     * with 'label' and 'value' properties, or a custom object.  In the case of using a customObject
     * labelPropertyName or valuePropertyName MUST be specified (or both) OR the custom object must contain a
     * property named "label" or "value" (or both) for the suggestions to appear.  In cases where this is not used,
     * the data sent back represents a slim subset of the properties on the object.
     * </p>
     *
     * @return true if the query method results should be used as the suggestions, false to assume
     *         objects are returned and suggestions are formed using the source property name
     */
    @BeanTagAttribute(name = "returnFullQueryObject")
    public boolean isReturnFullQueryObject() {
        return returnFullQueryObject;
    }

    /**
     * Setter for the for returning the full object of the query
     *
     * @param returnFullQueryObject
     */
    public void setReturnFullQueryObject(boolean returnFullQueryObject) {
        this.returnFullQueryObject = returnFullQueryObject;
    }

    /**
     * Indicates whether all suggest options should be retrieved up front and provide to the suggest
     * widget as options locally
     *
     * <p>
     * Use this for a small list of options to improve performance. The query will be performed on the client
     * to filter the provider options based on the users input instead of doing a query each time
     * </p>
     *
     * <p>
     * When a query method is configured and this option set to true the method will be invoked to set the
     * options. The query method should not take any arguments and should return the suggestion options
     * List or the JS String as a result. If a query method is not configured the suggest options can be
     * set through configuration or a view helper method (for example a component finalize method)
     * </p>
     *
     * @return true to provide the suggest options initially, false to use ajax retrieval based on the
     *         user's input
     */
    @BeanTagAttribute(name = "retrieveAllSuggestions")
    public boolean isRetrieveAllSuggestions() {
        return retrieveAllSuggestions;
    }

    /**
     * Setter for the retrieve all suggestions indicator
     *
     * @param retrieveAllSuggestions
     */
    public void setRetrieveAllSuggestions(boolean retrieveAllSuggestions) {
        this.retrieveAllSuggestions = retrieveAllSuggestions;
    }

    /**
     * When {@link #isRetrieveAllSuggestions()} is true, this list provides the full list of suggestions
     *
     * <p>
     * If a query method is configured that method will be invoked to populate this list, otherwise the
     * list should be populated through configuration or the view helper
     * </p>
     *
     * <p>
     * The suggest options can either be a list of Strings, in which case the strings will be the suggested
     * values. Or a list of objects. If the object does not have 'label' and 'value' properties, a custom render
     * and select method must be provided
     * </p>
     *
     * @return list of suggest options
     */
    @BeanTagAttribute(name = "suggestOptions", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Object> getSuggestOptions() {
        return suggestOptions;
    }

    /**
     * Setter for the list of suggest options
     *
     * @param suggestOptions
     */
    public void setSuggestOptions(List<Object> suggestOptions) {
        this.suggestOptions = suggestOptions;
    }

    /**
     * Returns the suggest options as a JS String (set by the framework from method invocation)
     *
     * @return suggest options JS string
     */
    public String getSuggestOptionsJsString() {
        if (StringUtils.isNotBlank(suggestOptionsJsString)) {
            return this.suggestOptionsJsString;
        }

        return "null";
    }

    /**
     * Sets suggest options javascript string
     *
     * @param suggestOptionsJsString
     */
    public void setSuggestOptionsJsString(String suggestOptionsJsString) {
        this.suggestOptionsJsString = suggestOptionsJsString;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Suggest suggestCopy = (Suggest) component;
        suggestCopy.setValuePropertyName(this.getValuePropertyName());
        suggestCopy.setLabelPropertyName(this.getLabelPropertyName());

        if(additionalPropertiesToReturn != null) {
            suggestCopy.setAdditionalPropertiesToReturn(new ArrayList<String> (additionalPropertiesToReturn));
        }

        suggestCopy.setReturnFullQueryObject(this.isReturnFullQueryObject());
        suggestCopy.setRetrieveAllSuggestions(this.isRetrieveAllSuggestions());

        if (this.suggestQuery != null) {
            suggestCopy.setSuggestQuery((AttributeQuery)this.suggestQuery.copy());
        }

        suggestCopy.setSuggestOptions(this.getSuggestOptions());
        suggestCopy.setSuggestOptionsJsString(this.suggestOptionsJsString);
    }
}
