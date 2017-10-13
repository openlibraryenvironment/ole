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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.KeepExpression;
import org.kuali.rice.krad.uif.component.PropertyReplacer;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.util.CloneUtils;
import org.kuali.rice.krad.uif.util.ExpressionFunctions;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluates expression language statements using the Spring EL engine
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DefaultExpressionEvaluator implements ExpressionEvaluator {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            DefaultExpressionEvaluator.class);

    private StandardEvaluationContext evaluationContext;

    private Map<String, Expression> cachedExpressions;

    protected static ExpressionParser parser = new SpelExpressionParser();

    private static Method isAssignableFrom;
    private static Method empty;
    private static Method emptyList;
    private static Method listContains;
    private static Method getName;
    private static Method getParm;
    private static Method getParmInd;
    private static Method hasPerm;
    private static Method hasPermDtls;
    private static Method hasPermTmpl;
    private static Method sequence;
    private static Method getDataObjectKey;

    static {
        try{
            isAssignableFrom = ExpressionFunctions.class.getDeclaredMethod("isAssignableFrom", new Class[]{Class.class, Class.class});
            empty = ExpressionFunctions.class.getDeclaredMethod("empty", new Class[]{Object.class});
            emptyList = ExpressionFunctions.class.getDeclaredMethod("emptyList", new Class[]{List.class});
            listContains = ExpressionFunctions.class.getDeclaredMethod("listContains", new Class[]{List.class, Object[].class});
            getName = ExpressionFunctions.class.getDeclaredMethod("getName", new Class[]{Class.class});
            getParm = ExpressionFunctions.class.getDeclaredMethod("getParm", new Class[]{String.class, String.class, String.class});
            getParmInd = ExpressionFunctions.class.getDeclaredMethod("getParmInd", new Class[]{String.class, String.class, String.class});
            hasPerm = ExpressionFunctions.class.getDeclaredMethod("hasPerm", new Class[]{String.class, String.class});
            hasPermDtls = ExpressionFunctions.class.getDeclaredMethod("hasPermDtls", new Class[]{String.class, String.class, Map.class, Map.class});
            hasPermTmpl = ExpressionFunctions.class.getDeclaredMethod("hasPermTmpl", new Class[]{String.class, String.class, Map.class, Map.class});
            sequence = ExpressionFunctions.class.getDeclaredMethod("sequence", new Class[]{String.class});
            getDataObjectKey = ExpressionFunctions.class.getDeclaredMethod("getDataObjectKey", new Class[]{String.class});
        }catch(NoSuchMethodException e){
            LOG.error("Custom function for el expressions not found: " + e.getMessage());
            throw new RuntimeException("Custom function for el expressions not found: " + e.getMessage(), e);
        }
    }

    /**
     * Default constructor
     */
    public DefaultExpressionEvaluator() {
        cachedExpressions = new HashMap<String, Expression>();
    }

    /**
     * org.kuali.rice.krad.uif.view.ExpressionEvaluator#initializeEvaluationContext(java.lang.Object
     * )
     */
    public void initializeEvaluationContext(Object contextObject) {
        evaluationContext = new StandardEvaluationContext(contextObject);

        addCustomFunctions(evaluationContext);
    }

    /**
     * org.kuali.rice.krad.uif.view.ExpressionEvaluator#evaluateExpressionsOnConfigurable(
     * org.kuali.rice.krad.uif.view.View, org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean,
     * java.util.Map<java.lang.String,java.lang.Object>)
     */
    public void evaluateExpressionsOnConfigurable(View view, UifDictionaryBean expressionConfigurable,
            Map<String, Object> evaluationParameters) {
        if ((expressionConfigurable instanceof Component) || (expressionConfigurable instanceof LayoutManager)) {
            evaluatePropertyReplacers(view, expressionConfigurable, evaluationParameters);
        }
        evaluatePropertyExpressions(view, expressionConfigurable, evaluationParameters);
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ExpressionEvaluator#evaluateExpression(java.util.Map,
     *      String)
     */
    public Object evaluateExpression(Map<String, Object> evaluationParameters, String expressionStr) {
        Object result = null;

        // if expression contains placeholders remove before evaluating
        if (StringUtils.startsWith(expressionStr, UifConstants.EL_PLACEHOLDER_PREFIX) && StringUtils.endsWith(
                expressionStr, UifConstants.EL_PLACEHOLDER_SUFFIX)) {
            expressionStr = StringUtils.removeStart(expressionStr, UifConstants.EL_PLACEHOLDER_PREFIX);
            expressionStr = StringUtils.removeEnd(expressionStr, UifConstants.EL_PLACEHOLDER_SUFFIX);
        }

        try {
            Expression expression = retrieveCachedExpression(expressionStr);

            if (evaluationParameters != null) {
                evaluationContext.setVariables(evaluationParameters);
            }

            result = expression.getValue(evaluationContext);
        } catch (Exception e) {
            LOG.error("Exception evaluating expression: " + expressionStr);
            throw new RuntimeException("Exception evaluating expression: " + expressionStr, e);
        }

        return result;
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ExpressionEvaluator#evaluateExpressionTemplate(java.util.Map,
     *      String)
     */
    public String evaluateExpressionTemplate(Map<String, Object> evaluationParameters, String expressionTemplate) {
        String result = null;

        try {
            Expression expression = retrieveCachedExpression(expressionTemplate);

            if (evaluationParameters != null) {
                evaluationContext.setVariables(evaluationParameters);
            }

            result = expression.getValue(evaluationContext, String.class);
        } catch (Exception e) {
            LOG.error("Exception evaluating expression: " + expressionTemplate);
            throw new RuntimeException("Exception evaluating expression: " + expressionTemplate, e);
        }

        return result;
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ExpressionEvaluator#evaluatePropertyExpression(View,
     *      java.util.Map, org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean, String,
     *      boolean)
     */
    public void evaluatePropertyExpression(View view, Map<String, Object> evaluationParameters,
            UifDictionaryBean expressionConfigurable, String propertyName, boolean removeExpression) {

        Map<String, String> propertyExpressions = expressionConfigurable.getPropertyExpressions();
        if ((propertyExpressions == null) || !propertyExpressions.containsKey(propertyName)) {
            return;
        }

        String expression = propertyExpressions.get(propertyName);

        // check whether expression should be evaluated or property should retain the expression
        if (CloneUtils.fieldHasAnnotation(expressionConfigurable.getClass(), propertyName, KeepExpression.class)) {
            // set expression as property value to be handled by the component
            ObjectPropertyUtils.setPropertyValue(expressionConfigurable, propertyName, expression);
            return;
        }

        Object propertyValue = null;

        // replace binding prefixes (lp, dp, fp) in expression before evaluation
        String adjustedExpression = replaceBindingPrefixes(view, expressionConfigurable, expression);

        // determine whether the expression is a string template, or evaluates to another object type
        if (StringUtils.startsWith(adjustedExpression, UifConstants.EL_PLACEHOLDER_PREFIX) && StringUtils.endsWith(
                adjustedExpression, UifConstants.EL_PLACEHOLDER_SUFFIX)
                && (StringUtils.countMatches(adjustedExpression,
                        UifConstants.EL_PLACEHOLDER_PREFIX) == 1)) {
            propertyValue = evaluateExpression(evaluationParameters, adjustedExpression);
        } else {
            // treat as string template
            propertyValue = evaluateExpressionTemplate(evaluationParameters, adjustedExpression);
        }

        // if property name has the special indicator then we need to add the expression result to the property
        // value instead of replace
        if (StringUtils.endsWith(propertyName, ExpressionEvaluator.EMBEDDED_PROPERTY_NAME_ADD_INDICATOR)) {
            StringUtils.removeEnd(propertyName, ExpressionEvaluator.EMBEDDED_PROPERTY_NAME_ADD_INDICATOR);

            Collection collectionValue = ObjectPropertyUtils.getPropertyValue(expressionConfigurable, propertyName);
            if (collectionValue == null) {
                throw new RuntimeException("Property name: "
                        + propertyName
                        + " with collection type was not initialized. Cannot add expression result");
            }
            collectionValue.add(propertyValue);
        } else {
            ObjectPropertyUtils.setPropertyValue(expressionConfigurable, propertyName, propertyValue);
        }

        if (removeExpression) {
            propertyExpressions.remove(propertyName);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ExpressionEvaluator#containsElPlaceholder(String)
     */
    public boolean containsElPlaceholder(String value) {
        boolean containsElPlaceholder = false;

        if (StringUtils.isNotBlank(value)) {
            String elPlaceholder = StringUtils.substringBetween(value, UifConstants.EL_PLACEHOLDER_PREFIX,
                    UifConstants.EL_PLACEHOLDER_SUFFIX);
            if (StringUtils.isNotBlank(elPlaceholder)) {
                containsElPlaceholder = true;
            }
        }

        return containsElPlaceholder;
    }

    /**
     * @see org.kuali.rice.krad.uif.view.ExpressionEvaluator#replaceBindingPrefixes(View, Object,
     *      String)
     */
    public String replaceBindingPrefixes(View view, Object object, String expression) {
        String adjustedExpression = StringUtils.replace(expression, UifConstants.NO_BIND_ADJUST_PREFIX, "");

        // replace the field path prefix for DataFields
        if (StringUtils.contains(adjustedExpression, UifConstants.FIELD_PATH_BIND_ADJUST_PREFIX)) {
            if (object instanceof DataField) {
                // Get the binding path from the object
                BindingInfo bindingInfo = ((DataField) object).getBindingInfo();

                Pattern pattern = Pattern.compile("("
                        + Pattern.quote(UifConstants.FIELD_PATH_BIND_ADJUST_PREFIX)
                        + "[\\.\\w]+"
                        + ")");
                Matcher matcher = pattern.matcher(adjustedExpression);
                while (matcher.find()) {
                    String path = matcher.group();

                    String adjustedPath = bindingInfo.getPropertyAdjustedBindingPath(path);
                    adjustedExpression = StringUtils.replace(adjustedExpression, path, adjustedPath);
                }
            } else {
                adjustedExpression = StringUtils.replace(adjustedExpression,
                        UifConstants.FIELD_PATH_BIND_ADJUST_PREFIX,
                        "");
            }
        }

        // replace the default path prefix if there is one set on the view
        if (StringUtils.isNotBlank(view.getDefaultBindingObjectPath())) {
            adjustedExpression = StringUtils.replace(adjustedExpression, UifConstants.DEFAULT_PATH_BIND_ADJUST_PREFIX,
                    view.getDefaultBindingObjectPath() + ".");

        } else {
            adjustedExpression = StringUtils.replace(adjustedExpression, UifConstants.DEFAULT_PATH_BIND_ADJUST_PREFIX,
                    "");
        }

        // replace line path binding prefix with the actual line path
        if (adjustedExpression.contains(UifConstants.LINE_PATH_BIND_ADJUST_PREFIX) && (object instanceof Component)) {
            String linePath = getLinePathPrefixValue((Component) object);

            adjustedExpression = StringUtils.replace(adjustedExpression, UifConstants.LINE_PATH_BIND_ADJUST_PREFIX,
                    linePath + ".");
        }

        // replace node path binding prefix with the actual node path
        if (adjustedExpression.contains(UifConstants.NODE_PATH_BIND_ADJUST_PREFIX) && (object instanceof Component)) {
            String nodePath = "";

            Map<String, Object> context = ((Component) object).getContext();
            if (context != null && context.containsKey(UifConstants.ContextVariableNames.NODE_PATH)) {
                nodePath = (String) context.get(UifConstants.ContextVariableNames.NODE_PATH);
            }

            adjustedExpression = StringUtils.replace(adjustedExpression, UifConstants.NODE_PATH_BIND_ADJUST_PREFIX,
                    nodePath + ".");
        }

        return adjustedExpression;
    }

    /**
     * Attempts to retrieve the {@link Expression} instance for the given expression template, if
     * not found one is created and added to the cache
     * 
     * @param expressionTemplate template string for the expression
     * @return Expression instance
     */
    protected Expression retrieveCachedExpression(String expressionTemplate) {
        Expression expression = null;

        // return from the expression from cache if present
        if (cachedExpressions.containsKey(expressionTemplate)) {
            return cachedExpressions.get(expressionTemplate);
        }

        // not in cache, create the expression object
        if (StringUtils.contains(expressionTemplate, UifConstants.EL_PLACEHOLDER_PREFIX)) {
            expression = parser.parseExpression(expressionTemplate, new TemplateParserContext(
                    UifConstants.EL_PLACEHOLDER_PREFIX, UifConstants.EL_PLACEHOLDER_SUFFIX));
        } else {
            expression = parser.parseExpression(expressionTemplate);
        }

        cachedExpressions.put(expressionTemplate, expression);

        return expression;
    }

    /**
     * Registers custom functions for el expressions with the given context
     * 
     * @param context - context instance to register functions to
     */
    protected void addCustomFunctions(StandardEvaluationContext context) {
        context.registerFunction("isAssignableFrom", isAssignableFrom);
        context.registerFunction("empty", empty);
        context.registerFunction("emptyList", emptyList);
        context.registerFunction("listContains", listContains);
        context.registerFunction("getName", getName);
        context.registerFunction("getParm", getParm);
        context.registerFunction("getParmInd", getParmInd);
        context.registerFunction("hasPerm", hasPerm);
        context.registerFunction("hasPermDtls", hasPermDtls);
        context.registerFunction("hasPermTmpl", hasPermTmpl);
        context.registerFunction("sequence", sequence);
        context.registerFunction("getDataObjectKey", getDataObjectKey);
    }

    /**
     * Iterates through any configured <code>PropertyReplacer</code> instances for the component and
     * evaluates the given condition. If the condition is met, the replacement value is set on the
     * corresponding property
     * 
     * @param view - view instance being rendered
     * @param expressionConfigurable - expressionConfigurable instance with property replacers list,
     *        should be either a component or layout manager
     * @param evaluationParameters - parameters for el evaluation
     */
    protected void evaluatePropertyReplacers(View view, UifDictionaryBean expressionConfigurable,
            Map<String, Object> evaluationParameters) {
        List<PropertyReplacer> replacers = null;
        if (Component.class.isAssignableFrom(expressionConfigurable.getClass())) {
            replacers = ((Component) expressionConfigurable).getPropertyReplacers();
        } else if (LayoutManager.class.isAssignableFrom(expressionConfigurable.getClass())) {
            replacers = ((LayoutManager) expressionConfigurable).getPropertyReplacers();
        }

        if (replacers != null) {
            for (PropertyReplacer propertyReplacer : replacers) {
                String expression = propertyReplacer.getCondition();
                String adjustedExpression = replaceBindingPrefixes(view, expressionConfigurable, expression);

                String conditionEvaluation = evaluateExpressionTemplate(evaluationParameters, adjustedExpression);
                boolean conditionSuccess = Boolean.parseBoolean(conditionEvaluation);
                if (conditionSuccess) {
                    ObjectPropertyUtils.setPropertyValue(expressionConfigurable, propertyReplacer.getPropertyName(),
                            propertyReplacer.getReplacement());
                }
            }
        }
    }

    /**
     * Iterates through the keys of the property expressions map and invokes
     * {@link #evaluatePropertyExpression(org.kuali.rice.krad.uif.view.View, java.util.Map, org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean, String, boolean)}
     * 
     * <p>
     * If the expression is an el template (part static text and part expression), only the
     * expression part will be replaced with the result. More than one expressions may be contained
     * within the template
     * </p>
     * 
     * @param view - view instance that is being rendered
     * @param expressionConfigurable - object instance to evaluate expressions for
     * @param evaluationParameters - map of additional parameters that may be used within the
     *        expressions
     */
    protected void evaluatePropertyExpressions(View view, UifDictionaryBean expressionConfigurable,
            Map<String, Object> evaluationParameters) {
        Map<String, String> propertyExpressions = expressionConfigurable.getPropertyExpressions();
        for (String propertyName : propertyExpressions.keySet()) {
            evaluatePropertyExpression(view, evaluationParameters, expressionConfigurable, propertyName, false);
        }
    }

    /**
     * Determines the value for the
     * {@link org.kuali.rice.krad.uif.UifConstants#LINE_PATH_BIND_ADJUST_PREFIX} binding prefix
     * based on collection group found in the component context
     * 
     * @param component - component instance for which the prefix is configured on
     * @return String line binding path or empty string if path not found
     */
    protected static String getLinePathPrefixValue(Component component) {
        String linePath = "";

        Map<String, Object> componentContext = component.getContext();
        CollectionGroup collectionGroup = componentContext == null ? null : (CollectionGroup) (componentContext.get(
                UifConstants.ContextVariableNames.COLLECTION_GROUP));
        if (collectionGroup == null) {
            LOG.warn("collection group not found for " + component + "," + component.getId() + ", " + component
                    .getComponentTypeName());
            return linePath;
        }

        Object indexObj = componentContext == null ? null : componentContext
                .get(UifConstants.ContextVariableNames.INDEX);
        if (indexObj != null) {
            int index = (Integer) indexObj;
            boolean addLine = false;
            Object addLineObj = componentContext == null ? null : componentContext
                    .get(UifConstants.ContextVariableNames.IS_ADD_LINE);

            if (addLineObj != null) {
                addLine = (Boolean) addLineObj;
            }

            if (addLine) {
                linePath = collectionGroup.getAddLineBindingInfo().getBindingPath();
            } else {
                linePath = collectionGroup.getBindingInfo().getBindingPath() + "[" + index + "]";
            }
        }

        return linePath;
    }

}
