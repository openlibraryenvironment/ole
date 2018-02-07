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
package org.kuali.rice.kew.rule;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.kew.engine.RouteContext;

/**
 * {@link Rule} implementation 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class RuleImpl implements Rule {
    /**
     * The default type of rule expression implementation to use if none is explicitly
     * specified for the node.
     */
    public static final String DEFAULT_RULE_EXPRESSION = "WorkflowAttribute";
    /**
     * Package in which rule expression implementations live
     */
    private static final String RULE_EXPRESSION_PACKAGE = "org.kuali.rice.kew.rule";
    /**
     * The class name suffix all rule expressions should have; e.g. FooRuleExpression
     */
    private static final String RULE_EXPRESSION_SUFFIX= "RuleExpression";

    /**
     * The BO of the rule definition in the system
     */
    private final org.kuali.rice.kew.api.rule.Rule ruleDefinition;

    RuleImpl(org.kuali.rice.kew.api.rule.Rule ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public org.kuali.rice.kew.api.rule.Rule getDefinition() {
        return ruleDefinition;
    }

    // loads a RuleExpression implementation
    protected RuleExpression loadRuleExpression(String type) {
        if (type == null) {
            type = DEFAULT_RULE_EXPRESSION;
        }
        // type is of the format 'category:qualifier'
        // we just want the category
        int colon = type.indexOf(':');
        if (colon == -1) colon = type.length();
        type = type.substring(0, colon);
        type = StringUtils.capitalize(type);

        // load up the rule expression implementation
        String className = RULE_EXPRESSION_PACKAGE + "." + type + RULE_EXPRESSION_SUFFIX;
        Class<?> ruleExpressionClass;
        try {
            ruleExpressionClass = ClassLoaderUtils.getDefaultClassLoader().loadClass(className);
        } catch (ClassNotFoundException cnfe) {
            throw new RiceIllegalStateException("Rule expression implementation '" + className + "' not found", cnfe);
        }
        if (!RuleExpression.class.isAssignableFrom(ruleExpressionClass)) {
            throw new RiceIllegalStateException("Specified class '" + ruleExpressionClass + "' does not implement RuleExpression interface");
        }
        RuleExpression ruleExpression;
        try {
            ruleExpression = ((Class<RuleExpression>) ruleExpressionClass).newInstance();
        } catch (Exception e) {
        	if (e instanceof RuntimeException) {
        		throw (RuntimeException)e;
        	}
            throw new RiceIllegalStateException("Error instantiating rule expression implementation '" + ruleExpressionClass + "'", e);
        }

        return ruleExpression;
    }

    public RuleExpressionResult evaluate(Rule rule, RouteContext context) {
        org.kuali.rice.kew.api.rule.RuleExpression ruleExprDef = ruleDefinition.getRuleExpressionDef();
        String type = DEFAULT_RULE_EXPRESSION;
        if (ruleExprDef != null) {
            type = ruleExprDef.getType();
        }
        RuleExpression ruleExpression = loadRuleExpression(type);        
        return ruleExpression.evaluate(rule, context);
    }
}
