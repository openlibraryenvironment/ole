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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for UIF expressions
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExpressionUtils {
    private static final Log LOG = LogFactory.getLog(ExpressionUtils.class);

    /**
     * Pulls expressions within the expressionConfigurable's expression graph and moves them to the property expressions
     * map for the expressionConfigurable or a nested expressionConfigurable (for the case of nested expression property names)
     *
     * <p>
     * Expressions that are configured on properties and pulled out by the {@link org.kuali.rice.krad.uif.util.UifBeanFactoryPostProcessor}
     * and put in the {@link org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getExpressionGraph()} for the bean that is
     * at root (non nested) level. Before evaluating the expressions, they need to be moved to the
     * {@link org.kuali.rice.krad.datadictionary.uif.UifDictionaryBean#getPropertyExpressions()} map for the expressionConfigurable that
     * property
     * is on.
     * </p>
     *
     * @param expressionConfigurable expressionConfigurable instance to process expressions for
     * @param buildRefreshGraphs indicates whether the expression graphs for component refresh should be built
     */
    public static void populatePropertyExpressionsFromGraph(UifDictionaryBean expressionConfigurable, boolean buildRefreshGraphs) {
        if (expressionConfigurable == null || expressionConfigurable.getExpressionGraph() == null) {
            return;
        }

        // will hold graphs to populate the refreshExpressionGraph property on each expressionConfigurable
        // key is the path to the expressionConfigurable and value is the map of nested property names to expressions
        Map<String, Map<String, String>> refreshExpressionGraphs = new HashMap<String, Map<String, String>>();

        Map<String, String> expressionGraph = expressionConfigurable.getExpressionGraph();
        for (Map.Entry<String, String> expressionEntry : expressionGraph.entrySet()) {
            String propertyName = expressionEntry.getKey();
            String expression = expressionEntry.getValue();

            // by default assume expression belongs with passed in expressionConfigurable
            UifDictionaryBean configurableWithExpression = expressionConfigurable;

            // if property name is nested, we need to move the expression to the last expressionConfigurable
            String adjustedPropertyName = propertyName;
            if (StringUtils.contains(propertyName, ".")) {
                String configurablePath = StringUtils.substringBeforeLast(propertyName, ".");
                adjustedPropertyName = StringUtils.substringAfterLast(propertyName, ".");

                Object nestedObject = ObjectPropertyUtils.getPropertyValue(expressionConfigurable, configurablePath);
                if ((nestedObject == null) || !(nestedObject instanceof UifDictionaryBean)) {
                    throw new RiceRuntimeException(
                            "Object for which expression is configured on is null or does not implement UifDictionaryBean: '"
                                    + configurablePath
                                    + "'");
                }

                // use nested object as the expressionConfigurable which will get the property expression
                configurableWithExpression = (UifDictionaryBean) nestedObject;

                // now add the expression to the refresh graphs
                if (buildRefreshGraphs) {
                    String currentPath = "";

                    String[] configurablePathNames = StringUtils.split(configurablePath, ".");
                    for (String configurablePathName : configurablePathNames) {
                        if (StringUtils.isNotBlank(currentPath)) {
                            currentPath += ".";
                        }
                        currentPath += configurablePathName;

                        Map<String, String> graphExpressions = null;
                        if (refreshExpressionGraphs.containsKey(currentPath)) {
                            graphExpressions = refreshExpressionGraphs.get(currentPath);
                        } else {
                            graphExpressions = new HashMap<String, String>();
                            refreshExpressionGraphs.put(currentPath, graphExpressions);
                        }

                        // property name in refresh graph should be relative to expressionConfigurable
                        String configurablePropertyName = StringUtils.substringAfter(propertyName, currentPath + ".");
                        graphExpressions.put(configurablePropertyName, expression);
                    }
                }
            }

            configurableWithExpression.getPropertyExpressions().put(adjustedPropertyName, expression);
        }

        // set the refreshExpressionGraph property on each expressionConfigurable an expression was found for
        if (buildRefreshGraphs) {
            for (String configurablePath : refreshExpressionGraphs.keySet()) {
                Object nestedObject = ObjectPropertyUtils.getPropertyValue(expressionConfigurable, configurablePath);
                // note if nested object is not a expressionConfigurable, then it can't be refresh and we can safely ignore
                if ((nestedObject != null) && (nestedObject instanceof UifDictionaryBean)) {
                    ((UifDictionaryBean) nestedObject).setRefreshExpressionGraph(refreshExpressionGraphs.get(
                            configurablePath));
                }
            }

            // the expression graph for the passed in expressionConfigurable will be its refresh graph as well
            expressionConfigurable.setRefreshExpressionGraph(expressionGraph);
        }
    }

    /**
     * Takes in an expression and a list to be filled in with names(property names)
     * of controls found in the expression. This method returns a js expression which can
     * be executed on the client to determine if the original exp was satisfied before
     * interacting with the server - ie, this js expression is equivalent to the one passed in.
     *
     * There are limitations on the Spring expression language that can be used as this method.
     * It is only used to parse expressions which are valid case statements for determining if
     * some action/processing should be performed.  ONLY Properties, comparison operators, booleans,
     * strings, matches expression, and boolean logic are supported.  Properties must
     * be a valid property on the form, and should have a visible control within the view.
     *
     * Example valid exp: account.name == 'Account Name'
     *
     * @param exp
     * @param controlNames
     * @return
     */
    public static String parseExpression(String exp, List<String> controlNames) {
        // clean up expression to ease parsing
        exp = exp.trim();
        if (exp.startsWith("@{")) {
            exp = StringUtils.removeStart(exp, "@{");
            if (exp.endsWith("}")) {
                exp = StringUtils.removeEnd(exp, "}");
            }
        }

        exp = StringUtils.replace(exp, "!=", " != ");
        exp = StringUtils.replace(exp, "==", " == ");
        exp = StringUtils.replace(exp, ">", " > ");
        exp = StringUtils.replace(exp, "<", " < ");
        exp = StringUtils.replace(exp, "<=", " <= ");
        exp = StringUtils.replace(exp, ">=", " >= ");

        String conditionJs = exp;
        String stack = "";

        boolean expectingSingleQuote = false;
        boolean ignoreNext = false;
        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);
            if (!expectingSingleQuote && !ignoreNext && (c == '(' || c == ' ' || c == ')')) {
                evaluateCurrentStack(stack.trim(), controlNames);
                //reset stack
                stack = "";
                continue;
            } else if (!ignoreNext && c == '\'') {
                stack = stack + c;
                expectingSingleQuote = !expectingSingleQuote;
            } else if (c == '\\') {
                stack = stack + c;
                ignoreNext = !ignoreNext;
            } else {
                stack = stack + c;
                ignoreNext = false;
            }
        }

        if (StringUtils.isNotEmpty(stack)) {
            evaluateCurrentStack(stack.trim(), controlNames);
        }

        conditionJs = conditionJs.replaceAll("\\s(?i:ne)\\s", " != ").replaceAll("\\s(?i:eq)\\s", " == ").replaceAll(
                "\\s(?i:gt)\\s", " > ").replaceAll("\\s(?i:lt)\\s", " < ").replaceAll("\\s(?i:lte)\\s", " <= ")
                .replaceAll("\\s(?i:gte)\\s", " >= ").replaceAll("\\s(?i:and)\\s", " && ").replaceAll("\\s(?i:or)\\s",
                        " || ").replaceAll("\\s(?i:not)\\s", " != ").replaceAll("\\s(?i:null)\\s?", " '' ").replaceAll(
                        "\\s?(?i:#empty)\\((.*?)\\)", "isValueEmpty($1)").replaceAll("\\s?(?i:#listContains)\\((.*?)\\)",
                        "listContains($1)").replaceAll("\\s?(?i:#emptyList)\\((.*?)\\)", "emptyList($1)");

        if (conditionJs.contains("matches")) {
            conditionJs = conditionJs.replaceAll("\\s+(?i:matches)\\s+'.*'", ".match(/" + "$0" + "/) != null ");
            conditionJs = conditionJs.replaceAll("\\(/\\s+(?i:matches)\\s+'", "(/");
            conditionJs = conditionJs.replaceAll("'\\s*/\\)", "/)");
        }

        List<String> removeControlNames = new ArrayList<String>();
        List<String> addControlNames = new ArrayList<String>();
        //convert property names to use coerceValue function and convert arrays to js arrays
        for (String propertyName : controlNames) {
            //array definitions are caught in controlNames because of the nature of the parse - convert them and remove
            if(propertyName.trim().startsWith("{") && propertyName.trim().endsWith("}")){
                String array = propertyName.trim().replace('{', '[');
                array = array.replace('}', ']');
                conditionJs = conditionJs.replace(propertyName, array);
                removeControlNames.add(propertyName);
                continue;
            }

            //handle not
            if (propertyName.startsWith("!")){
                String actualPropertyName = StringUtils.removeStart(propertyName, "!");
                conditionJs = conditionJs.replace(propertyName,
                        "!coerceValue(\"" + actualPropertyName + "\")");
                removeControlNames.add(propertyName);
                addControlNames.add(actualPropertyName);
            }
            else{
                conditionJs = conditionJs.replace(propertyName, "coerceValue(\"" + propertyName + "\")");
            }
        }

        controlNames.removeAll(removeControlNames);
        controlNames.addAll(addControlNames);

        return conditionJs;
    }

    /**
     * Used internally by parseExpression to evalute if the current stack is a property
     * name (ie, will be a control on the form)
     *
     * @param stack
     * @param controlNames
     */
    public static void evaluateCurrentStack(String stack, List<String> controlNames) {
        if (StringUtils.isNotBlank(stack)) {
            if (!(stack.equals("==")
                    || stack.equals("!=")
                    || stack.equals(">")
                    || stack.equals("<")
                    || stack.equals(">=")
                    || stack.equals("<=")
                    || stack.equalsIgnoreCase("ne")
                    || stack.equalsIgnoreCase("eq")
                    || stack.equalsIgnoreCase("gt")
                    || stack.equalsIgnoreCase("lt")
                    || stack.equalsIgnoreCase("lte")
                    || stack.equalsIgnoreCase("gte")
                    || stack.equalsIgnoreCase("matches")
                    || stack.equalsIgnoreCase("null")
                    || stack.equalsIgnoreCase("false")
                    || stack.equalsIgnoreCase("true")
                    || stack.equalsIgnoreCase("and")
                    || stack.equalsIgnoreCase("or")
                    || stack.contains("#empty")
                    || stack.equals("!")
                    || stack.contains("#emptyList")
                    || stack.contains("#listContains")
                    || stack.startsWith("'")
                    || stack.endsWith("'"))) {

                boolean isNumber = false;
                if ((StringUtils.isNumeric(stack.substring(0, 1)) || stack.substring(0, 1).equals("-"))) {
                    try {
                        Double.parseDouble(stack);
                        isNumber = true;
                    } catch (NumberFormatException e) {
                        isNumber = false;
                    }
                }

                if (!(isNumber)) {
                    //correct argument of a custom function ending in comma
                    if(StringUtils.endsWith(stack, ",")){
                        stack = StringUtils.removeEnd(stack, ",").trim();
                    }

                    if (!controlNames.contains(stack)) {
                        controlNames.add(stack);
                    }
                }
            }
        }
    }

}
