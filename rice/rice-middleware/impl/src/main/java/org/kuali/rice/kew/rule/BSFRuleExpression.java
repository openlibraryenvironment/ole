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

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.engine.RouteContext;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * A rule expression implementation that uses Bean Scripting Framework.
 * The language is given by the type qualifier, e.g.:
 * &lt;expression type="BSF:groovy"&gt;...
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
//TODO: this should really be renamed since it is no longer using apache BSF
public class BSFRuleExpression implements RuleExpression {
    private static final Logger LOG = Logger.getLogger(BSFRuleExpression.class);

    public RuleExpressionResult evaluate(Rule rule, RouteContext context) {
        org.kuali.rice.kew.api.rule.RuleContract ruleDefinition = rule.getDefinition();
        String name = "" + ruleDefinition.getName();
        String type = ruleDefinition.getRuleExpressionDef().getType();
        String lang = parseLang(type, "groovy");
        String expression = ruleDefinition.getRuleExpressionDef().getExpression();
        RuleExpressionResult result;
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName(lang);
        try {
            declareBeans(engine, rule, context);
            result = (RuleExpressionResult) engine.eval(expression);
        } catch (ScriptException e) {
            String details =  ( e.getLineNumber() >= 0 ?  " line: " + e.getLineNumber() + " column: " + e.getColumnNumber() : "" );
            LOG.debug("Error evaluating rule '" + name + "' " + type +  " expression" + details + ": '" + expression + "'" + details, e);
            throw new RiceIllegalStateException("Error evaluating rule '" + name + "' " + type + " expression" + details, e);
        }
        if (result == null) {
            return new RuleExpressionResult(rule, false);
        } else {
            return result;
        }
    }

    /**
     * Parses the language component from the type string
     * @param type the type string
     * @param deflt the default language if none is present in the type string
     * @return the language component or null
     */
    protected String parseLang(String type, String deflt) {
        int colon = type.indexOf(':');
        if (colon > -1) {
            return type.substring(colon + 1);
        } else {
            return deflt;
        }
    }

    /**
     * Populates the BSFManager with beans that are accessible to BSF scripts.  May be overridden by
     * subclasses.  The standard implementation exposes the rule and routeContext
     * @param manager the BSFManager
     * @param rule the current Rule object
     * @param context the current RouteContext
     */
    protected void declareBeans(ScriptEngine engine, Rule rule, RouteContext context) throws ScriptException {
        engine.put("rule", rule);
        engine.put("routeContext", context);
        engine.put("workflow", new WorkflowRuleAPI(context));
    }

    /**
     * A helper bean that is declared for use by BSF scripts.
     * This functionality should really be part of a single internal API that can be exposed
     * to various pieces of code that are plugged into KEW.  For comparison EDocLite also
     * has its own such API that it exposes. 
     */
    protected static final class WorkflowRuleAPI {
        private final RouteContext context;
        WorkflowRuleAPI(RouteContext context) {
            this.context = context;
        }
        /**
         * Evaluates a named rule
         * @param name the rule name
         * @return the RuleExpressionResult
         * @throws WorkflowException 
         */
        public RuleExpressionResult invokeRule(String name) throws WorkflowException {
            org.kuali.rice.kew.api.rule.Rule rbv = KewApiServiceLocator.getRuleService().getRuleByName(name);
            if (rbv == null) throw new WorkflowRuntimeException("Could not find rule named \"" + name + "\"");
            Rule r = new RuleImpl(rbv);
            return r.evaluate(r, context);
        }
    }
}
