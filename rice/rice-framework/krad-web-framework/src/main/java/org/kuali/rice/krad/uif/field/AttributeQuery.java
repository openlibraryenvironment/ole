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
package org.kuali.rice.krad.uif.field;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.datadictionary.validator.ErrorReport;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.MethodInvokerConfig;
import org.kuali.rice.krad.uif.util.CloneUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds configuration for executing a dynamic query on an <code>InputField</code> to
 * pull data for updating the UI
 *
 * <p>
 * There are two types of query types that can be configured and executed. The first is provided
 * completely by the framework using the <code>LookupService</code> and will perform a query
 * against the configured dataObjectClassName using the query parameters and return field mapping.
 * The second type will invoke a method that will perform the query. This can be configured using the
 * queryMethodToCall (if the method is on the view helper service), or using the queryMethodInvoker if
 * the method is on another class or object.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "attributeQueryConfig-bean", parent = "Uif-AttributeQueryConfig")
public class AttributeQuery extends UifDictionaryBeanBase implements Serializable {
    private static final long serialVersionUID = -4569905665441735255L;

    private String dataObjectClassName;

    private boolean renderNotFoundMessage;
    private String returnMessageText;
    private String returnMessageStyleClasses;

    private Map<String, String> queryFieldMapping;
    private Map<String, String> returnFieldMapping;
    private Map<String, String> additionalCriteria;

    private List<String> sortPropertyNames;

    private String queryMethodToCall;
    private List<String> queryMethodArgumentFieldList;
    private MethodInvokerConfig queryMethodInvokerConfig;

    public AttributeQuery() {
        renderNotFoundMessage = true;

        queryFieldMapping = new HashMap<String, String>();
        returnFieldMapping = new HashMap<String, String>();
        additionalCriteria = new HashMap<String, String>();
        sortPropertyNames = new ArrayList<String>();

        queryMethodArgumentFieldList = new ArrayList<String>();
        queryMethodInvokerConfig = new MethodInvokerConfig();
    }

    /**
     * Adjusts the path on the query field mapping from property to match the binding
     * path prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateQueryFieldMapping(BindingInfo bindingInfo) {
        Map<String, String> adjustedQueryFieldMapping = new HashMap<String, String>();
        for (String fromFieldPath : getQueryFieldMapping().keySet()) {
            String toField = getQueryFieldMapping().get(fromFieldPath);
            String adjustedFromFieldPath = bindingInfo.getPropertyAdjustedBindingPath(fromFieldPath);

            adjustedQueryFieldMapping.put(adjustedFromFieldPath, toField);
        }

        this.queryFieldMapping = adjustedQueryFieldMapping;
    }

    /**
     * Adjusts the path on the return field mapping to property to match the binding
     * path prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateReturnFieldMapping(BindingInfo bindingInfo) {
        Map<String, String> adjustedReturnFieldMapping = new HashMap<String, String>();
        for (String fromFieldPath : getReturnFieldMapping().keySet()) {
            String toFieldPath = getReturnFieldMapping().get(fromFieldPath);
            String adjustedToFieldPath = bindingInfo.getPropertyAdjustedBindingPath(toFieldPath);

            adjustedReturnFieldMapping.put(fromFieldPath, adjustedToFieldPath);
        }

        this.returnFieldMapping = adjustedReturnFieldMapping;
    }

    /**
     * Adjusts the path on the query method arguments field list to match the binding
     * path prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateQueryMethodArgumentFieldList(BindingInfo bindingInfo) {
        List<String> adjustedArgumentFieldList = new ArrayList<String>();
        for (String argumentFieldPath : getQueryMethodArgumentFieldList()) {
            String adjustedFieldPath = bindingInfo.getPropertyAdjustedBindingPath(argumentFieldPath);
            adjustedArgumentFieldList.add(adjustedFieldPath);
        }

        this.queryMethodArgumentFieldList = adjustedArgumentFieldList;
    }

    /**
     * Builds String for passing the queryFieldMapping Map as a Javascript object
     * parameter
     *
     * @return js parameter string
     */
    public String getQueryFieldMappingJsString() {
        String queryFieldMappingJs = "{";

        for (String queryField : queryFieldMapping.keySet()) {
            if (!StringUtils.equals(queryFieldMappingJs, "{")) {
                queryFieldMappingJs += ",";
            }

            queryFieldMappingJs += "\"" + queryField + "\":\"" + queryFieldMapping.get(queryField) + "\"";
        }

        queryFieldMappingJs += "}";

        return queryFieldMappingJs;
    }

    /**
     * Builds String for passing the returnFieldMapping Map as a Javascript object
     * parameter
     *
     * @return js parameter string
     */
    public String getReturnFieldMappingJsString() {
        String returnFieldMappingJs = "{";

        for (String fromField : returnFieldMapping.keySet()) {
            if (!StringUtils.equals(returnFieldMappingJs, "{")) {
                returnFieldMappingJs += ",";
            }

            returnFieldMappingJs += "\"" + returnFieldMapping.get(fromField) + "\":\"" + fromField + "\"";
        }

        returnFieldMappingJs += "}";

        return returnFieldMappingJs;
    }

    /**
     * Builds String for passing the queryMethodArgumentFieldList as a Javascript Object
     *
     * @return js parameter string
     */
    public String getQueryMethodArgumentFieldsJsString() {
        String queryMethodArgsJs = "{";

        for (String methodArg : queryMethodArgumentFieldList) {
            if (!StringUtils.equals(queryMethodArgsJs, "{")) {
                queryMethodArgsJs += ",";
            }

            queryMethodArgsJs += "\"" + methodArg + "\":\"" + methodArg + "\"";
        }

        queryMethodArgsJs += "}";

        return queryMethodArgsJs;
    }

    /**
     * Indicates whether this attribute query is configured to invoke a custom
     * method as opposed to running the general object query. If either the query method
     * to call is given, or the query method invoker is not null it is assumed the
     * intention is to call a custom method
     *
     * @return true if a custom method is configured, false if not
     */
    public boolean hasConfiguredMethod() {
        boolean configuredMethod = false;

        if (StringUtils.isNotBlank(getQueryMethodToCall())) {
            configuredMethod = true;
        } else if (getQueryMethodInvokerConfig() != null && (StringUtils.isNotBlank(
                getQueryMethodInvokerConfig().getTargetMethod()) || StringUtils.isNotBlank(
                getQueryMethodInvokerConfig().getStaticMethod()))) {
            configuredMethod = true;
        }

        return configuredMethod;
    }

    /**
     * Class name for the data object the query should be performed against
     *
     * @return data object class name
     */
    @BeanTagAttribute(name = "dataObjectClassName")
    public String getDataObjectClassName() {
        return dataObjectClassName;
    }

    /**
     * Setter for the query data object class name
     *
     * @param dataObjectClassName
     */
    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    /**
     * Configures the query parameters by mapping fields in the view
     * to properties on the data object class for the query
     *
     * <p>
     * Each map entry configures one parameter for the query, where
     * the map key is the field name to pull the value from, and the
     * map value is the property name on the object the parameter should
     * populate.
     * </p>
     *
     * @return mapping of query parameters
     */
    @BeanTagAttribute(name = "queryFieldMapping", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getQueryFieldMapping() {
        return queryFieldMapping;
    }

    /**
     * Setter for the query parameter mapping
     *
     * @param queryFieldMapping
     */
    public void setQueryFieldMapping(Map<String, String> queryFieldMapping) {
        this.queryFieldMapping = queryFieldMapping;
    }

    /**
     * Maps properties from the result object of the query to
     * fields in the view
     *
     * <p>
     * Each map entry configures one return mapping, where the map
     * key is the field name for the field to populate, and the map
     * values is the name of the property on the result object to
     * pull the value from
     * </p>
     *
     * @return return field mapping
     */
    @BeanTagAttribute(name = "returnFieldMapping", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getReturnFieldMapping() {
        return returnFieldMapping;
    }

    /**
     * Setter for the return field mapping
     *
     * @param returnFieldMapping
     */
    public void setReturnFieldMapping(Map<String, String> returnFieldMapping) {
        this.returnFieldMapping = returnFieldMapping;
    }

    /**
     * Fixed criteria that will be appended to the dynamic criteria generated
     * for the query. Map key gives name of the property the criteria should
     * apply to, and the map value is the value (literal) for the criteria. Standard
     * lookup wildcards are allowed
     *
     * @return field name/value pairs for query criteria
     */
    @BeanTagAttribute(name = "additionalCriteria", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getAdditionalCriteria() {
        return additionalCriteria;
    }

    /**
     * Setter for the query's additional criteria map
     *
     * @param additionalCriteria
     */
    public void setAdditionalCriteria(Map<String, String> additionalCriteria) {
        this.additionalCriteria = additionalCriteria;
    }

    /**
     * List of property names to sort the query results by. The sort
     * will be performed on each property in the order they are contained
     * within the list. Each property must be a valid property of the
     * return query object (the data object in case of the general query)
     *
     * @return property names
     */
    @BeanTagAttribute(name = "sortPropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getSortPropertyNames() {
        return sortPropertyNames;
    }

    /**
     * Setter for the list of property names to sort results by
     *
     * @param sortPropertyNames
     */
    public void setSortPropertyNames(List<String> sortPropertyNames) {
        this.sortPropertyNames = sortPropertyNames;
    }

    /**
     * Indicates whether a message should be added to the query result
     * object and displayed when the query return object is null
     *
     * @return true if not found message should be added, false otherwise
     */
    @BeanTagAttribute(name = "renderNotFoundMessage")
    public boolean isRenderNotFoundMessage() {
        return renderNotFoundMessage;
    }

    /**
     * Setter for the render not found message indicator
     *
     * @param renderNotFoundMessage
     */
    public void setRenderNotFoundMessage(boolean renderNotFoundMessage) {
        this.renderNotFoundMessage = renderNotFoundMessage;
    }

    /**
     * Message text to display along with the query result
     *
     * @return literal message text
     */
    @BeanTagAttribute(name = "returnMessageText")
    public String getReturnMessageText() {
        return returnMessageText;
    }

    /**
     * Setter for the return message text
     *
     * @param returnMessageText
     */
    public void setReturnMessageText(String returnMessageText) {
        this.returnMessageText = returnMessageText;
    }

    /**
     * CSS Style classes that should be applied to the return message.
     * Multiple style classes should be delimited by a space
     *
     * @return style classes
     */
    @BeanTagAttribute(name = "returnMessageStyleClasses")
    public String getReturnMessageStyleClasses() {
        return returnMessageStyleClasses;
    }

    /**
     * Setter for the return messages style classes
     *
     * @param returnMessageStyleClasses
     */
    public void setReturnMessageStyleClasses(String returnMessageStyleClasses) {
        this.returnMessageStyleClasses = returnMessageStyleClasses;
    }

    /**
     * Configures the name of the method that should be invoked to perform
     * the query
     *
     * <p>
     * Should contain only the method name (no parameters or return type). If only
     * the query method name is configured it is assumed to be on the <code>ViewHelperService</code>
     * for the contained view.
     * </p>
     *
     * @return query method name
     */
    @BeanTagAttribute(name = "queryMethodToCall")
    public String getQueryMethodToCall() {
        return queryMethodToCall;
    }

    /**
     * Setter for the query method name
     *
     * @param queryMethodToCall
     */
    public void setQueryMethodToCall(String queryMethodToCall) {
        this.queryMethodToCall = queryMethodToCall;
    }

    /**
     * List of field names that should be passed as arguments to the query method
     *
     * <p>
     * Each entry in the list maps to a method parameter, in the other contained within
     * the list. The value for the field within the view will be pulled and passed
     * to the query method as an argument
     * </p>
     *
     * @return query method argument list
     */
    @BeanTagAttribute(name = "queryMethodArgumentFieldList", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getQueryMethodArgumentFieldList() {
        return queryMethodArgumentFieldList;
    }

    /**
     * Setter for the query method argument list
     *
     * @param queryMethodArgumentFieldList
     */
    public void setQueryMethodArgumentFieldList(List<String> queryMethodArgumentFieldList) {
        this.queryMethodArgumentFieldList = queryMethodArgumentFieldList;
    }

    /**
     * Configures the query method target class/object and method name
     *
     * <p>
     * When the query method is not contained on the <code>ViewHelperService</code>, this
     * can be configured for declaring the target class/object and method. The target class
     * can be set in which case a new instance will be created and the given method invoked.
     * Alternatively, the target object instance for the invocation can be given. Or finally
     * a static method can be configured
     * </p>
     *
     * @return query method config
     */
    @BeanTagAttribute(name = "queryMethodInvokerConfig", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MethodInvokerConfig getQueryMethodInvokerConfig() {
        return queryMethodInvokerConfig;
    }

    /**
     * Setter for the query method config
     *
     * @param queryMethodInvokerConfig
     */
    public void setQueryMethodInvokerConfig(MethodInvokerConfig queryMethodInvokerConfig) {
        this.queryMethodInvokerConfig = queryMethodInvokerConfig;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("AttributeQuery", ValidationTrace.NO_BEAN_ID);

        // Checks that at least one aspect is set
        if (getDataObjectClassName() == null
                && getQueryMethodToCall() == null
                && getQueryMethodInvokerConfig() == null) {
            String currentValues[] = {"dataObjectClassName = " + getDataObjectClassName(),
                    "queryMethodToCall = " + getQueryMethodToCall(),
                    "queryMethodInvokerConfig = " + getQueryMethodInvokerConfig()};
            tracer.createWarning(
                    "At least 1 should be set: dataObjectClass, queryMethodToCall or queryMethodInvokerConfig",
                    currentValues);
        }
    }

    /**
     * Returns a copy of the attribute query.
     *
     * @return AttributeQuery copy of the component
     */
    public <T> T copy() {
        T copiedClass = null;
        try {
            copiedClass = (T) this.getClass().newInstance();
        } catch (Exception exception) {
            throw new RuntimeException();
        }

        copyProperties(copiedClass);

        return copiedClass;
    }

    /**
     * Copies the properties over for the copy method.
     *
     * @param attributeQuery The AttributeQuery to copy
     */
    protected <T> void copyProperties(T attributeQuery) {
        super.copyProperties(attributeQuery);
        AttributeQuery attributeQueryCopy = (AttributeQuery) attributeQuery;

        if (this.additionalCriteria != null) {
            attributeQueryCopy.setAdditionalCriteria(new HashMap<String, String>(this.additionalCriteria));
        }

        attributeQueryCopy.setDataObjectClassName(this.dataObjectClassName);

        if (this.queryFieldMapping != null) {
            attributeQueryCopy.setQueryFieldMapping(new HashMap<String, String>(this.queryFieldMapping));
        }

        if (this.queryMethodArgumentFieldList != null) {
            attributeQueryCopy.setQueryMethodArgumentFieldList(new ArrayList<String>(
                    this.queryMethodArgumentFieldList));
        }

        attributeQueryCopy.setQueryMethodToCall(this.queryMethodToCall);
        attributeQueryCopy.setRenderNotFoundMessage(this.renderNotFoundMessage);

        if (this.returnFieldMapping != null) {
            attributeQueryCopy.setReturnFieldMapping(new HashMap<String, String>(this.returnFieldMapping));
        }

        attributeQueryCopy.setReturnMessageStyleClasses(this.returnMessageStyleClasses);
        attributeQueryCopy.setReturnMessageText(this.returnMessageText);

        if (this.sortPropertyNames != null) {
            attributeQueryCopy.setSortPropertyNames(new ArrayList<String>(this.sortPropertyNames));
        }

        if (this.queryMethodInvokerConfig != null) {
            ((AttributeQuery) attributeQuery).setQueryMethodInvokerConfig(CloneUtils.deepClone(
                    this.queryMethodInvokerConfig));
        }
    }
}
