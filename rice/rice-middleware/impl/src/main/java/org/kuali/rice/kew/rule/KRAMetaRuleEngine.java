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

import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.engine.RouteContext;

/**
 * Implements the KRA meta-rule processing and state machine engine 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KRAMetaRuleEngine {
    private static final Logger LOG = Logger.getLogger(KRAMetaRuleEngine.class);

    /**
     * KRA meta-rule processing flag
     */
    private static enum KRA_RULE_FLAG {
        NEXT, TRUE, FALSE
    }

    private final String expression;
    private final String[] statements;
    private int curStatement = 0;
    /**
     * Whether processing has indicated that we should stop
     */
    private boolean stop = false;
    
    public KRAMetaRuleEngine(String expression) throws ParseException {
        this.expression = expression;
        statements = expression.split("\\s*[;\r\n]\\s*");
        
        if (statements.length == 0) {
            throw new ParseException("No statements parsed in expression: " + expression, 0);
        }
    }

    /**
     * @return the expression the engine was initialized with
     */
    public String getExpression() {
        return expression;
    }

    /**
     * @return the parsed statements
     */
    public String[] getStatements() {
        return statements;
    }

    /**
     * @return the next statement the engine will process
     */
    public int getCurStatement() {
        return curStatement;
    }

    /**
     * @param statementNo the statement index at which to resume processing 
     */
    public void setCurStatement(int statementNo) {
        this.curStatement = statementNo;
    }

    /**
     * @return whether we are done processing
     */
    public boolean isDone() {
        return curStatement >= statements.length || stop;
    }

    /**
     * Processes a single statement and returns the result
     * @param context the current RouteContext
     * @return the expression result that resulted from the evaluation of a single statement
     * @throws ParseException if the statement could not be parsed
     */
    public RuleExpressionResult processSingleStatement(RouteContext context) throws ParseException {
        if (isDone()) {
            return null;
        }

        int stmtNum = curStatement + 1;
        String statement = statements[curStatement];
        LOG.debug("Processing statement: " + statement);
        String[] words = statement.split("\\s*:\\s*");
        if (words.length < 2) {
            throw new ParseException("Invalid statement (#" + stmtNum + "): " + statement, 0);
        }
        String ruleName = words[0];
        if (StringUtils.isEmpty(ruleName)) {
            throw new ParseException("Invalid rule in statement (#" + stmtNum + "): " + statement, 0);
        }
        String flag = words[1];
        LOG.debug(flag.toUpperCase());
        KRA_RULE_FLAG flagCode = KRA_RULE_FLAG.valueOf(flag.toUpperCase());
        if (flagCode == null) {
            throw new ParseException("Invalid flag in statement (#" + stmtNum + "): " + statement, 0);
        }
        org.kuali.rice.kew.api.rule.Rule nestedRule = KewApiServiceLocator.getRuleService().getRuleByName(ruleName);
        if (nestedRule == null) {
            throw new ParseException("Rule '" + ruleName + "' in statement (#" + stmtNum + ") not found: " + statement, 0);
        }

        Rule rule = new RuleImpl(nestedRule);
        RuleExpressionResult result;
        switch (flagCode) {
            case NEXT:
                result = rule.evaluate(rule, context);
                break;
            case TRUE:
                result = rule.evaluate(rule, context);
                if (!result.isSuccess()) {
                    stop = true;
                }
                break;
            case FALSE:
                result = rule.evaluate(rule, context);
                if (result.isSuccess()) {
                    stop = true;
                    // we need to just invert the ultimate expression result success, because in this case
                    // we wanted the expression to fail but it didn't
                    result = new RuleExpressionResult(rule, false, result.getResponsibilities());
                }
                break;
            default:
                throw new RiceIllegalStateException("Unhandled statement flag: " + flagCode);
        }

        curStatement++;
        LOG.debug("Result of statement '" + statement + "': " + result);
        return result;
    }
}
