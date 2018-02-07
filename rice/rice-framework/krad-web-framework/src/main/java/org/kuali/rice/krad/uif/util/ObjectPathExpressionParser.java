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

import java.util.Deque;

/**
 * Provides modular support for the partial JSP EL path syntax using an arbitrary root bean as the
 * initial name space.
 * 
 * <p>
 * NOTE: This is not full JSP EL, only the path reference portion without support for floating point
 * literals. See this <a href= "http://jsp.java.net/spec/jsp-2_1-fr-spec-el.pdf"> JSP Reference</a>
 * for the full BNF.
 * </p>
 * 
 * <pre>
 * Value ::= ValuePrefix (ValueSuffix)*
 * ValuePrefix ::= Literal
 *     | NonLiteralValuePrefix
 * NonLiteralValuePrefix ::= '(' Expression ')'
 *     | Identifier
 * ValueSuffix ::= '.' Identifier
 *     | '[' Expression ']'
 * Identifier ::= Java language identifier
 * Literal ::= BooleanLiteral
 *     | IntegerLiteral
 *     | FloatingPointLiteral
 *     | StringLiteral
 *     | NullLiteral
 * BooleanLiteral ::= 'true'
 *     | 'false'
 * StringLiteral ::= '([^'\]|\'|\\)*'
 *     | "([^"\]|\"|\\)*"
 *   i.e., a string of any characters enclosed by
 *   single or double quotes, where \ is used to
 *   escape ', ",and \. It is possible to use single
 *   quotes within double quotes, and vice versa,
 *   without escaping.
 * IntegerLiteral ::= ['0'-'9']+
 * NullLiteral ::= 'null'
 * </pre>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ObjectPathExpressionParser {

    /**
     * Used by {@link #parsePathExpression(Object, String, PathEntry)} to track parse state without
     * the need to construct a new parser stack for each expression.
     */
    private static final ThreadLocal<ParseState> TL_EL_PARSE_STATE = new ThreadLocal<ParseState>();

    /**
     * Tracks parser state for
     * {@link ObjectPathExpressionParser#parsePathExpression(Object, String, PathEntry)}.
     */
    private static final class ParseState {

        /**
         * The continuation stack.
         * <p>
         * When evaluating subexpressions, the outer expression is pushed onto this stack.
         * </p>
         */
        private final Deque<Object> stack = new java.util.LinkedList<Object>();

        /**
         * The lexical index at which to begin the next lexical scan.
         */
        private int nextScanIndex;

        /**
         * The lexical index of the next path separator token.
         */
        private int nextTokenIndex;

        /**
         * The root continuation.
         */
        private Object root;

        /**
         * The continuation point of the parse expression currently being evaluation.
         */
        private Object currentContinuation;

        /**
         * Determine if this parse state is active.
         */
        private boolean isActive() {
            return (stack != null && !stack.isEmpty()) || currentContinuation != null;
        }

        /**
         * Reset parse state, allowing this state marker to be reused on the next expression.
         */
        private void reset() {
            stack.clear();
            currentContinuation = null;
        }

        /**
         * Prepare for the next lexical scan.
         * 
         * <p>
         * When a parenthetical expression occurs on the left hand side of the path, remove the
         * parentheses.
         * </p>
         * 
         * <p>
         * Upon returning from this method, the value of {@link #nextScanIndex} will point at the
         * position of the character formerly to the right of the removed parenthetical group, if
         * applicable. If no parenthetical group was removed, {@link #nextScanIndex} will be reset
         * to 0.
         * </p>
         * 
         * @param path The path expression from the current continuation point.
         * @return The path expression, with parentheses related to a grouping on the left removed.
         */
        public String prepareNextScan(String path) {
            nextScanIndex = 0;

            char firstChar = path.charAt(0);

            if (firstChar != '(' && firstChar != '\'' && firstChar != '\"') {
                return path;
            }

            int parenCount = firstChar == '(' ? 1 : 0;
            int pathLen = path.length() - 1;

            // Look back during lexical scanning to detect quote and escape characers.
            char lastChar = firstChar;
            char currentChar;

            // Track quote state.
            char inQuote = firstChar == '(' ? '\0' : firstChar;

            while (nextScanIndex < pathLen
                    && ((firstChar == '(' && parenCount > 0) || (firstChar != '(' && inQuote != '\0'))) {
                nextScanIndex++;
                currentChar = path.charAt(nextScanIndex);

                // Ignore escaped characters.
                if (lastChar == '\\') {
                    continue;
                }

                // Toggle quote state when a quote is encountered.
                if (inQuote == '\0') {
                    if (currentChar == '\'' || currentChar == '\"') {
                        inQuote = currentChar;
                    }
                } else if (currentChar == inQuote) {
                    // Emulate BeanWrapper bad quotes support. Automatically escape quotes where the close
                    // quote is not positioned next to a path separator token.
                    // i.e. aBean.aMap['aPoorly'quoted'key'] should reference "aPoorly'quoted'key" as the
                    // key in the map.
                    switch (nextScanIndex >= path.length() - 1 ? '\0' : path.charAt(nextScanIndex + 1)) {
                    // Only accept close quote if followed by a lexical separator token.
                        case ']':
                        case '.':
                        case '[':
                            inQuote = '\0';
                            break;
                    }
                }

                // Ignore quoted characters.
                if (inQuote != '\0') {
                    continue;
                }

                if (currentChar == '(') {
                    parenCount++;
                }

                if (currentChar == ')') {
                    parenCount--;
                }
            }

            if (parenCount > 0) {
                throw new IllegalArgumentException("Unmatched '(': " + path);
            }

            if (firstChar == '(') {
                assert path.charAt(nextScanIndex) == ')';
                if (nextScanIndex < pathLen) {
                    path = path.substring(1, nextScanIndex) + path.substring(nextScanIndex + 1);
                } else {
                    path = path.substring(1, nextScanIndex);
                }
                nextScanIndex--;
            } else {
                nextScanIndex++;
            }

            return path;
        }

        /**
         * Update current parse state with the lexical indexes of the next token break.
         * 
         * @param path The path being parsed, starting from the current continuation point.
         */
        public void scan(String path) {
            nextTokenIndex = -1;

            // Scan the character sequence, starting with the character following the open marker.
            for (int currentIndex = nextScanIndex; currentIndex < path.length(); currentIndex++) {
                switch (path.charAt(currentIndex)) {
                    case ')':
                        // should have been removed by prepareNextScan
                        throw new IllegalArgumentException("Unmatched ')': " + path);
                    case '\'':
                    case '\"':
                    case '(':
                    case '[':
                    case '.':
                    case ']':
                        if (nextTokenIndex == -1) {
                            nextTokenIndex = currentIndex;
                        }
                        return;
                }
            }
        }

        /**
         * Step to the next continuation point in the parse path.
         * 
         * <p>
         * Upon returning from this method, the value of {@link #currentContinuation} will reflect
         * the resolved state of parsing the path. When null is returned, then
         * {@link #currentContinuation} will be the reflect the result of parsing the expression.
         * </p>
         * 
         * @param path The path expression from the current continuation point.
         * @return The path expression for the next continuation point, null if the path has been
         *         completely parsed.
         */
        private String step(String path, PathEntry pathEntry) {

            if (nextTokenIndex == -1) {
                // Only a symbolic reference, resolve it and return.
                currentContinuation = pathEntry.parse(pathEntry.prepare(currentContinuation), path, false);
                return null;
            }

            char nextToken = path.charAt(nextTokenIndex);

            switch (nextToken) {

                case '[':
                    // Entering bracketed subexpression

                    // Resolve non-empty key reference as the current continuation
                    if (nextTokenIndex != 0) {
                        currentContinuation = pathEntry.parse(
                                pathEntry.prepare(currentContinuation),
                                path.substring(0, nextTokenIndex), false);
                    }

                    // Push current continuation down in the stack.
                    stack.push(currentContinuation);

                    // Reset the current continuation for evaluating the
                    // subexpression
                    currentContinuation = pathEntry.parse(root, null, false);
                    return path.substring(nextTokenIndex + 1);

                case '(':
                    // Approaching a parenthetical expression, not preceded by a subexpression,
                    // resolve the key reference as the current continuation
                    currentContinuation = pathEntry.parse(pathEntry.prepare(currentContinuation),
                            path.substring(0, nextTokenIndex), false);
                    return path.substring(nextTokenIndex); // Keep the left parenthesis

                case '.':
                    // Crossing a period, not preceded by a subexpression,
                    // resolve the key reference as the current continuation
                    currentContinuation = pathEntry.parse(pathEntry.prepare(currentContinuation),
                            path.substring(0, nextTokenIndex), false);
                    return path.substring(nextTokenIndex + 1); // Skip the period

                case ']':
                    if (nextTokenIndex > 0) {
                        // Approaching a right bracket, resolve the key reference as the current continuation
                        currentContinuation = pathEntry.parse(pathEntry.prepare(currentContinuation),
                                path.substring(0, nextTokenIndex), false);
                        return path.substring(nextTokenIndex); // Keep the right bracket

                    } else {
                        // Crossing a right bracket.

                        // Use the current continuation as the parameter for resolving
                        // the top continuation on the stack, then make the result the
                        // current continuation.
                        currentContinuation = pathEntry.parse(pathEntry.prepare(stack.pop()),
                                pathEntry.dereference(currentContinuation), true);
                        if (nextTokenIndex + 1 < path.length()) {
                            // short-circuit the next step, as an optimization for
                            // handling dot resolution without permitting double-dots
                            switch (path.charAt(nextTokenIndex + 1)) {
                                case '.':
                                    // crossing a dot, skip it
                                    return path.substring(nextTokenIndex + 2);
                                case '[':
                                case ']':
                                    // crossing to another subexpression, don't skip it.
                                    return path.substring(nextTokenIndex + 1);
                                default:
                                    throw new IllegalArgumentException(
                                            "Expected '.', '[', or ']': " + path);
                            }
                        } else {
                            return null;
                        }
                    }

                default:
                    throw new IllegalArgumentException("Unexpected '" + nextToken + "' :" + path);
            }
        }

    }

    /**
     * Path entry interface for use with
     * {@link ObjectPathExpressionParser#parsePathExpression(Object, String, PathEntry)}.
     */
    public static interface PathEntry {

        /**
         * Parse one node.
         * 
         * @param node The current parse node.
         * @param next The next path token.
         * @param inherit True indicates that the current node is the result of a subexpression,
         *        false indicates the next node in the same expression.
         * @return A reference to the next parse node.
         */
        Object parse(Object node, String next, boolean inherit);

        /**
         * Prepare the next parse node based on a reference returned from the prior node.
         * 
         * @param prev The reference data from the previous node.
         * @return The next parse node.
         */
        Object prepare(Object prev);

        /**
         * Resolve the next path element based on reference data from the previous node.
         * 
         * @param prev The reference data from the previous node.
         * @return the next path element based on the returned reference data.
         */
        String dereference(Object prev);
    }

    /**
     * Parse a path expression.
     * 
     * @param root The root object.
     * @param path The path expression.
     * @param pathEntry The path entry adaptor to use for processing parse node transition.
     * @param <T> Reference type representing the next parse node.
     * @param <S> The parse node type.
     * 
     * @return The valid of the bean property indicated by the given path expression, null if the
     *         path expression doesn't resolve to a valid property.
     * @see ObjectPathExpressionParser#getPropertyValue(Object, String)
     */
    public static Object parsePathExpression(Object root, String path,
            final PathEntry pathEntry) {

        // NOTE: This iterative parser allows support for subexpressions
        // without recursion. When a subexpression start token '[' is
        // encountered the current continuation is pushed onto a stack. When
        // the subexpression is resolved, the continuation is popped back
        // off the stack and resolved using the subexpression result as the
        // arg. All subexpressions start with the same root passed in as an
        // argument for this method. - MWF

        ParseState parseState = (ParseState) TL_EL_PARSE_STATE.get();
        boolean recycle;

        if (parseState == null) {
            TL_EL_PARSE_STATE.set(new ParseState());
            parseState = TL_EL_PARSE_STATE.get();
            recycle = true;
        } else if (parseState.isActive()) {
            ProcessLogger.ntrace("el-parse:", ":nested", 100);
            parseState = new ParseState();
            recycle = false;
        } else {
            recycle = true;
        }

        try {
            parseState.root = root;
            parseState.currentContinuation = pathEntry.parse(root, null, false);
            while (path != null) {
                path = parseState.prepareNextScan(path);
                parseState.scan(path);
                path = parseState.step(path, pathEntry);
            }
            return parseState.currentContinuation;
        } finally {
            assert !recycle || parseState == TL_EL_PARSE_STATE.get();
            parseState.reset();
        }
    }

    /**
     * Private constructor - utility class only.
     */
    private ObjectPathExpressionParser() {}

}
