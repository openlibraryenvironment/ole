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
package org.kuali.rice.core.api.search;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;


/**
 * Canonical utilities for parsing incoming string search expressions
 */
public class SearchExpressionUtils {
    /**
     * Binary range operators, must be used to split an expression into values
     */
    static final Collection<SearchOperator> BINARY_RANGE_OPERATORS =
        Collections.unmodifiableCollection(Arrays.asList(SearchOperator.BETWEEN, SearchOperator.BETWEEN_EXCLUSIVE_LOWER, SearchOperator.BETWEEN_EXCLUSIVE_UPPER,
                                                         SearchOperator.BETWEEN_EXCLUSIVE_UPPER2, SearchOperator.BETWEEN_EXCLUSIVE));

    /**
     * Clause operators just join multiple expressions
     */
    public static final Collection<SearchOperator> CLAUSE_OPERATORS = Collections.unmodifiableCollection(Arrays.asList(SearchOperator.AND, SearchOperator.OR));
    private static final Pattern CLAUSE_OPERATORS_PATTERN = generateSplitPattern(CLAUSE_OPERATORS);

    /**
     * Operators that can be trimmed from the start of an expression to yield the value.
     */
    private final Collection<SearchOperator> PREFIX_UNARY_OPERATORS =
            Collections.unmodifiableCollection(Arrays.asList(SearchOperator.GREATER_THAN, SearchOperator.LESS_THAN, SearchOperator.GREATER_THAN_EQUAL, SearchOperator.LESS_THAN_EQUAL,
                                                             SearchOperator.NOT));
    /**
     * Operators that can be trimmed from the end of an expression to yield the value.
     */
    private final Collection<SearchOperator> POSTFIX_UNARY_OPERATORS =
            Collections.unmodifiableCollection(Arrays.asList(SearchOperator.LIKE_ONE, SearchOperator.LIKE_MANY, SearchOperator.LIKE_MANY_P));


    private SearchExpressionUtils() {
        throw new UnsupportedOperationException("do not call");
    }

    public static String parsePrefixUnaryOperatorValue(SearchOperator operator, String expression) {
        return StringUtils.removeStart(expression.trim(), operator.op()).trim();
    }

    public static String parsePostfixUnaryOperatorValue(SearchOperator operator, String expression) {
        return StringUtils.removeEnd(expression.trim(), operator.op()).trim();
    }

    public static String[] parseBinaryOperatorValues(SearchOperator operator, String expression) {
        return StringUtils.splitByWholeSeparator(expression.trim(), operator.op(), 2);
    }

    /**
     * Returns a regular expression that splits a string on the given operators
     * @param operators the specified operators
     * @return a pattern which splits a string on the operators
     */
    private static Pattern generateSplitPattern(Collection<SearchOperator> operators) {
        return Pattern.compile(Joiner.on("|").join(Iterables.transform(operators,
                new Function<SearchOperator, String>() {
                    public String apply(SearchOperator op) { return "(?:\\s*" + Pattern.quote(op.op()) + "\\s*)"; }
                })));
    }

    /**
     * Splits the expression by AND and OR operators.  Does not trim empty clauses (these are probably syntax errors)
     * @param expression the expression to parse
     * @return string array of clauses, not including AND or OR operators
     */
    public static String[] splitOnClauses(String expression) {
        return CLAUSE_OPERATORS_PATTERN.split(expression);
    }

    /**
     * Strips AND and OR operators from the expression string
     * @param expression the expression to parse
     * @return string not including AND or OR operators
     */
    public static String stripClauseOperators(String expression) {
        return CLAUSE_OPERATORS_PATTERN.matcher(expression).replaceAll("");
    }

    /**
     * Splits the expression by the given operators.  Does not trim empty parts.
     * @param expression the expression to parse
     * @return string array of clauses, not including specified operators
     */
    public static String[] splitOnOperators(String expression, Collection<SearchOperator> operators) {
        return generateSplitPattern(operators).split(expression);
    }

    /**
     * Splits the expression by the given operators.  Does not trim empty parts.
     * @param expression the expression to parse
     * @return string array of clauses, not including specified operators
     */
    public static String[] splitOnOperators(String expression, SearchOperator... operators) {
        return generateSplitPattern(Arrays.asList(operators)).split(expression);
    }

    /**
     * Strips AND and OR operators from the expression string
     * @param expression the expression to parse
     * @return string not including AND or OR operators
     */
    public static String stripOperators(String expression, Collection<SearchOperator> operators) {
        return generateSplitPattern(operators).matcher(expression).replaceAll("");
    }

    /**
     * Strips AND and OR operators from the expression string
     * @param expression the expression to parse
     * @return string not including AND or OR operators
     */
    public static String stripOperators(String expression, SearchOperator... operators) {
        return generateSplitPattern(Arrays.asList(operators)).matcher(expression).replaceAll("");
    }

    public static Range parseRange(String rangeString) {
        if (StringUtils.isBlank(rangeString)) {
            throw new IllegalArgumentException("rangeString was null or blank");
        }
        Range range = new Range();
        rangeString = rangeString.trim();
        if (rangeString.startsWith(SearchOperator.LESS_THAN_EQUAL.op())) {
            rangeString = parsePrefixUnaryOperatorValue(SearchOperator.LESS_THAN_EQUAL, rangeString);
            range.setUpperBoundValue(rangeString);
            range.setUpperBoundInclusive(true);
        } else if (rangeString.startsWith(SearchOperator.LESS_THAN.op())) {
            rangeString = parsePrefixUnaryOperatorValue(SearchOperator.LESS_THAN, rangeString);
            range.setUpperBoundValue(rangeString);
            range.setUpperBoundInclusive(false);
        } else if (rangeString.startsWith(SearchOperator.GREATER_THAN_EQUAL.op())) {
            rangeString = parsePrefixUnaryOperatorValue(SearchOperator.GREATER_THAN_EQUAL, rangeString);
            range.setLowerBoundValue(rangeString);
            range.setLowerBoundInclusive(true);
        } else if (rangeString.startsWith(SearchOperator.GREATER_THAN.op())) {
            rangeString = parsePrefixUnaryOperatorValue(SearchOperator.GREATER_THAN, rangeString);
            range.setLowerBoundValue(rangeString);
            range.setLowerBoundInclusive(false);

        // these range separators are subsets of each other, so we need to be sure to check them in the right order
        } else if (rangeString.contains(SearchOperator.BETWEEN_EXCLUSIVE.op())) {
            String[] rangeBounds = parseBinaryOperatorValues(SearchOperator.BETWEEN_EXCLUSIVE, rangeString);
            range.setLowerBoundValue(rangeBounds[0]);
            range.setLowerBoundInclusive(false);
            range.setUpperBoundValue(rangeBounds[1]);
            range.setUpperBoundInclusive(false);
        } else if (rangeString.contains(SearchOperator.BETWEEN_EXCLUSIVE_UPPER.op())) {
            String[] rangeBounds = parseBinaryOperatorValues(SearchOperator.BETWEEN_EXCLUSIVE_UPPER, rangeString);
            range.setLowerBoundValue(rangeBounds[0]);
            range.setLowerBoundInclusive(true);
            range.setUpperBoundValue(rangeBounds[1]);
            range.setUpperBoundInclusive(false);
        } else if (rangeString.contains(SearchOperator.BETWEEN_EXCLUSIVE_UPPER2.op())) {
            String[] rangeBounds = parseBinaryOperatorValues(SearchOperator.BETWEEN_EXCLUSIVE_UPPER2, rangeString);
            range.setLowerBoundValue(rangeBounds[0]);
            range.setLowerBoundInclusive(true);
            range.setUpperBoundValue(rangeBounds[1]);
            range.setUpperBoundInclusive(false);
        } else if (rangeString.contains(SearchOperator.BETWEEN_EXCLUSIVE_LOWER.op())) {
            String[] rangeBounds = parseBinaryOperatorValues(SearchOperator.BETWEEN_EXCLUSIVE_LOWER, rangeString);
            range.setLowerBoundValue(rangeBounds[0]);
            range.setLowerBoundInclusive(false);
            range.setUpperBoundValue(rangeBounds[1]);
            range.setUpperBoundInclusive(true);
        } else if (rangeString.contains(SearchOperator.BETWEEN.op())) {
            String[] rangeBounds = parseBinaryOperatorValues(SearchOperator.BETWEEN, rangeString);
            range.setLowerBoundValue(rangeBounds[0]);
            range.setLowerBoundInclusive(true);
            range.setUpperBoundValue(rangeBounds[1]);
            range.setUpperBoundInclusive(true);
        } else {
            // if it has no range specification, return null
            return null;
        }
        return range;
    }

    public static Predicate parsePredicates(String expression, String property) {
        Set<Predicate> ored_predicates = new HashSet<Predicate>();
        for (String or_clause: splitOnOperators(expression, SearchOperator.OR)) {
            Set<Predicate> anded_predicates = new HashSet<Predicate>();
            for (String and_clause: splitOnOperators(or_clause, SearchOperator.AND)) {
                anded_predicates.add(parseSimplePredicate(property, and_clause));
            }
            ored_predicates.add(and(anded_predicates.toArray(new Predicate[0])));
        }
        return or(ored_predicates.toArray(new Predicate[0]));
    }

    public static Predicate parseSimplePredicate(String property, String value) {
        if (value.contains(SearchOperator.NULL.op())) {
            if (isNot(value)) {
                return isNotNull(property);
            } else {
                return isNull(property);
            }
        } else if (value.contains(SearchOperator.BETWEEN_EXCLUSIVE_UPPER.op())) {
            String[] betweenVals = parseBinaryOperatorValues(SearchOperator.BETWEEN_EXCLUSIVE_UPPER, value);
            return and(greaterThanOrEqual(property, betweenVals[0]), lessThan(property, betweenVals[1]));
        } else if (value.contains(SearchOperator.BETWEEN.op())) {
            String[] betweenVals = parseBinaryOperatorValues(SearchOperator.BETWEEN, value);
             return and(greaterThanOrEqual(property, betweenVals[0]), lessThanOrEqual(property, betweenVals[1]));
        } else if (value.contains(SearchOperator.GREATER_THAN_EQUAL.op())) {
            return greaterThanOrEqual(property, stripOperators(value, SearchOperator.GREATER_THAN_EQUAL));
        } else if (value.contains(SearchOperator.LESS_THAN_EQUAL.op())) {
            return lessThanOrEqual(property, stripOperators(value, SearchOperator.LESS_THAN_EQUAL));
        } else if (value.contains(SearchOperator.GREATER_THAN.op())) {
            return greaterThan(property, stripOperators(value, SearchOperator.GREATER_THAN));
        } else if (value.contains(SearchOperator.LESS_THAN.op())) {
            return lessThan(property, stripOperators(value, SearchOperator.LESS_THAN));
        } else if (value.contains(SearchOperator.NOT.op())) {
            String[] notValues = splitOnOperators(value, SearchOperator.NOT);
            List<Predicate> notPreds = new ArrayList<Predicate>(notValues.length);
            for (String notValue : notValues) {
                notPreds.add(notEqual(property, SearchExpressionUtils.stripOperators(notValue, SearchOperator.NOT)));
            }
            return and(notPreds.toArray(new Predicate[notPreds.size()]));
        } else if (value.contains(SearchOperator.LIKE_MANY.op()) || (value.contains(SearchOperator.LIKE_ONE.op()))) {
            if (isNot(value)) {
                return notLike(property, value);
            } else {
                return like(property, value );
            }
        } else {
            if (isNot(value)) {
                return notEqual(property, value);
            } else {
                return equal(property, value);
            }
        }
    }

    private static boolean isNot(String value) {
        if (value == null) {
            return false;
        }
        return value.contains(SearchOperator.NOT.op());
    }

    /**
    * Splits the valueEntered on locical operators and, or, and between
    *
    * @param valueEntered
    * @return
    */
    private static List<String> getSearchableValues(String valueEntered) {
        List<String> lRet = new ArrayList<String>();
        getSearchableValueRecursive(valueEntered, lRet);
        return lRet;
    }

    private static void getSearchableValueRecursive(String valueEntered, List lRet) {
        if(valueEntered == null) {
            return;
        }

        valueEntered = valueEntered.trim();

        if(lRet == null){
            throw new NullPointerException("The list passed in is by reference and should never be null.");
        }

        if (StringUtils.contains(valueEntered, SearchOperator.BETWEEN.op())) {
            List<String> l = Arrays.asList(SearchExpressionUtils.parseBinaryOperatorValues(SearchOperator.BETWEEN, valueEntered));
            for(String value : l){
                getSearchableValueRecursive(value,lRet);
            }
            return;
        }
        if (StringUtils.contains(valueEntered, SearchOperator.OR.op())) {
            List<String> l = Arrays.asList(StringUtils.split(valueEntered, SearchOperator.OR.op()));
            for(String value : l){
                getSearchableValueRecursive(value,lRet);
            }
            return;
        }
        if (StringUtils.contains(valueEntered, SearchOperator.AND.op())) {
            //splitValueList.addAll(Arrays.asList(StringUtils.split(valueEntered, KRADConstants.AND.op())));
            List<String> l = Arrays.asList(StringUtils.split(valueEntered, SearchOperator.AND.op()));
            for(String value : l){
                getSearchableValueRecursive(value,lRet);
            }
            return;
        }

        // lRet is pass by ref and should NEVER be null
        lRet.add(valueEntered);
   }
}
