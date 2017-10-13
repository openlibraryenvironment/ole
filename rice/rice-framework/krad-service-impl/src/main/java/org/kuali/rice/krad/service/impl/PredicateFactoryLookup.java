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
package org.kuali.rice.krad.service.impl;

import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.search.SearchOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;

/**
 * Contains methods used in the predicate factory related to the lookup framework.
 * ***************************************************************************************************************
 * FIXME: issues to talk to the group about.
 * http://kuali.org/rice/documentation/1.0.3/UG_Global/Documents/lookupwildcards.htm
 *
 * 1) Should we support isNotNull, isNull, as wildcards?  Then do we still translate
 * null values into isNull predicates.  I believe the lookup framework right now
 * barfs on null values but that is a guess.
 *
 * 2) We need to support case insensitivity in the old lookup framework.  Right now the lookup
 * framework looks at Data dictionary entries.  This can still be configured in the DD
 * but should be placed in the lookup criteria. We could have a "flag" section on a
 * lookup sequence like foo.bar=(?i)ba*|bing
 *
 * This would translate to
 *
 * or(like("foo.bar", "ba*"), equalsIgnoreCase("foo.bar", "bing"))
 *
 * Btw.  My flag format was stolen from regex but we could use anything really.
 * http://download.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE
 *
 * I'm currently supporting this.
 *
 * 3) In the above example, I used a case insensitive flag but Like doesn't support case
 * insensitive.  Should it?
 *
 * 4) Do we need to support escaping in the lookup framework & criteria api.  Right now the
 * lookup framework looks at Data dictionary entries.  This can still be configured in the DD
 * but should be placed in the lookup criteria.  Escaping is tricky and I worry if we support
 * it in the criteria api then it will make the criteria service much harder to make custom
 * implementations.  To me it seems it's better to make escaping behavior undefined.
 *
 * If we do support an escape character then we should probably also support a flag to treat
 * escape chars as literal like (?l) (that doesn't exist in java regex)
 *
 * http://download.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#LITERAL
 *
 * 5) Maybe we should just support what is in the {@link org.kuali.rice.core.framework.logic.SearchOperator} class
 *
 * 6) Maybe the predicate class could have a toLookupString, toLookupMap() methods on them to translate
 * to various formats of criteria?  Or maybe this factory method & related methods should get placed into
 * krad or somewhere else?
 * ***************************************************************************************************************
 */
class PredicateFactoryLookup {

    private static final Pattern FLAGS_PATTERN = Pattern.compile("^\\(\\?[a-z]+\\)");

    private PredicateFactoryLookup() {
        throw new IllegalArgumentException("do not call");
    }

    /**
     * This take a criteria map that is commonly used in krad-based
     * applications and transforms it to a predicate.
     *
     * The incoming map takes the form of the following possibilities:
     *
     * <ul>
     * <li>propertyPath=value</li>
     * <li>propertyPath=criteriaSequence</li>
     * </ul>
     *
     * <p>
     * The values in the map can either be a String or a Collection<Object>.
     * A String value is directly parsed into predicates.  If a collection
     * is found it is recursively parsed into predicates where each entry in the Collection
     * is anded together.
     * </p>
     *
     * <p>
     * Note: that the Collection can contain other collections or strings
     * but eventually must resolve to a string value or string criteria sequence.
     * </p>
     *
     * a simple example of a propertyPath=value:
     * <pre>
     * foo.bar=baz
     * </pre>
     * would yield
     * <pre>
     * equals("foo.bar", "baz")
     * </pre>
     *
     * a simple example of a propertyPath=criteriaSequence
     * <pre>
     * foo.bar=ba*|bing
     * </pre>
     * would yield
     * <pre>
     * or(like("foo.bar", "ba*"), equals("foo.bar", "bing"))
     * </pre>
     *
     * a compound example of a of a propertyPath=criteriaSequence
     * <pre>
     * foo.bar=[ba*,bing]      * note: [] shows a collection literal
     * </pre>
     * would yield
     * <pre>
     * and(like("foo.bar", "ba*"), equals("foo.bar", "bing"))
     * </pre>
     *
     * <p>
     * Related to null values:  Null values will be translated to isNull predicates.
     * </p>
     *
     * The criteria string may also contain flags similar to regex flags.  The current
     * supported flags are:
     *
     * <ul>
     *     <li>(?i) case insensitive</li>
     * </ul>
     *
     * To use the 'i' and 'm' flags prepend them to the criteria value, for example:
     *
     * (?im)foo
     *
     * Only the first flag sequence will be honored.  All others will be treated as literals.
     *
     * @param the root class to build the predicate on.  Cannot be null.
     * @param criteria the crtieria map.  Cannot be null or empty.
     * @throws IllegalArgumentException if clazz is null or criteria is null or empty
     */
    static Predicate fromCriteriaMap(Class<?> clazz, Map<String, ?> criteria) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        }

        if (criteria == null || criteria.isEmpty()) {
            throw new IllegalArgumentException("criteria is null or empty");
        }

        final List<Predicate> toAnd = new ArrayList<Predicate>();
        for (Map.Entry<String, ?> entry : criteria.entrySet()) {
            final String key = entry.getKey();
            if (key == null) {
                throw new IllegalArgumentException("criteria contains a null key");
            }
            toAnd.add(createPredicate(clazz, key, entry.getValue()));
        }

        return and(toAnd.toArray(new Predicate[]{}));
    }

    private static Predicate createPredicate(Class<?> clazz, final String key, final Object value) {
        if (value == null) {
            return isNull(key);
        } else if (value instanceof String) {
            final String flagStr = getFlagsStr((String) value);
            final String strValue = removeFlag((String) value, flagStr);
            return containsOperator(strValue) ? createFromComplexCriteriaValue(clazz, key, strValue, flagStr) :
                    createFromSimpleCriteriaValue(clazz, key, strValue, flagStr);
        } else if (value instanceof Collection) {
            final List<Predicate> ps = new ArrayList<Predicate>();
            for (Object v : (Collection<?>) value) {
                //recurs
                ps.add(createPredicate(clazz, key, v));
            }
            return and(ps.toArray(new Predicate[]{}));
        } else {
            throw new IllegalArgumentException(
                    "criteria map contained a value that was non supported :" + value.getClass().getName());
        }
    }

    private static Predicate createFromComplexCriteriaValue(Class<?> clazz, final String key, final String strValue,
            final String flagStr) {
        final boolean caseInsensitive = isCaseInsensitive(flagStr);

        return null;
    }

    private static Predicate createFromSimpleCriteriaValue(Class<?> clazz, final String key, final String strValue,
            final String flagStr) {
        final boolean caseInsensitive = isCaseInsensitive(flagStr);
        return caseInsensitive ? equalIgnoreCase(key, strValue) : equal(key, strValue);
    }

    private static String removeFlag(final String strValue, final String flagStr) {
        return flagStr.length() > 1 ? (strValue).substring(flagStr.length() - 1) : strValue;
    }

    //does not handle escaping, assumes non-null
    private static boolean containsOperator(String value) {
        for (SearchOperator o : SearchOperator.values()) {
            if (value.contains(o.op())) {
                return true;
            }
        }
        return false;
    }

    private static String getFlagsStr(String criteria) {
        Matcher m = FLAGS_PATTERN.matcher(criteria);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private static boolean isCaseInsensitive(String flagStr) {
        return flagStr.contains("i");
    }
}
