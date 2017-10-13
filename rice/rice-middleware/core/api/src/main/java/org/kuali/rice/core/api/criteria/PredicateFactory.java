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
package org.kuali.rice.core.api.criteria;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * This is a factory class to construct {@link Predicate Predicates}.
 *
 * <p>
 *    For more readable predicate construction it is recommended that this class
 *    is statically imported.
 * <code>
 *    import static org.kuali.rice.core.api.criteria.PredicateFactory.*;
 * </code>
 * </p>
 *
 * to create a simple predicate where the property
 * foo.bar equals "baz" do the following:
 * <code>
 *     Predicate simple = equals("foo.bar", "baz");
 * </code>
 *
 * to create a compound predicate where the property
 * foo.bar equals "baz" and foo.id equals 1 do the following:
 * <code>
 *     Predicate compound = and(equals("foo.bar", "baz"), equals("foo.id", 1))
 * </code>
 *
 * to create a deeply nested predicate where lots of
 * properties are evaluated do the following:
 *
 * Predicate apiAuthors =
 *  and(
 *      like("display", "*Eric*"),
 *		greaterThan("birthDate", gtBirthDate),
 * 		lessThan("birthDate", ltBirthDate),
 *      or(
 *         equal("name.first", "Travis"),
 *         equal("name.last", "Schneeberger")))
 *
 * <p>
 *     <strong>WARNING:</strong> this class does automatic reductions
 *     such that you cannot assume the concrete {@link Predicate} type
 *     returned from this factory.
 * </p>
 *
 * @see QueryByCriteria
 */
public final class PredicateFactory {
    private PredicateFactory() {
        throw new IllegalArgumentException("do not call");
    }

    /**
	 * Creates an predicate representing equals comparison.  Defines that the property
     * represented by the given path should be equal to the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>character data</li>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate equal(String propertyPath, Object value) {
		return new EqualPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Creates a predicate representing not equals comparison.  Defines that the property
     * represented by the given path should <strong>not</strong> be
	 * equal to the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>character data</li>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate notEqual(String propertyPath, Object value) {
		return new NotEqualPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

    /**
	 * Creates an equals ignore case predicate.  Defines that the property
     * represented by the given path should be equal to the specified value ignoring
     * the case of the value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>character data</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate equalIgnoreCase(String propertyPath, CharSequence value) {
		return new EqualIgnoreCasePredicate(propertyPath, new CriteriaStringValue(value));
	}

	/**
	 * Creates a not equals ignore case predicate.  Defines that the property
     * represented by the given path should <strong>not</strong> be
	 * equal to the specified value ignoring the case of the value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>character data</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate notEqualIgnoreCase(String propertyPath, CharSequence value) {
		return new NotEqualIgnoreCasePredicate(propertyPath, new CriteriaStringValue(value));
	}

	/**
	 * Creates a like predicate.  Defines that the property
     * represented by the given path should match the specified value,
	 * but supports the use of wildcards in the given value.
	 *
	 * <p>The supported wildcards include:
	 *
	 * <ul>
	 *   <li><strong>?</strong> - matches on any single character</li>
	 *   <li><strong>*</strong> - matches any string of any length (including zero length)</li>
	 * </ul>
	 *
	 * <p>Because of this, the like predicate only supports character data
	 * for the passed-in value.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null
	 */
	public static Predicate like(String propertyPath, CharSequence value) {
		return new LikePredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

    /**
	 * Creates a not like predicate.  Defines that the property
     * represented by the given path should <strong>not</strong> match the specified value,
	 * but supports the use of wildcards in the given value.
	 *
	 * <p>The supported wildcards include:
	 *
	 * <ul>
	 *   <li><strong>?</strong> - matches on any single character</li>
	 *   <li><strong>*</strong> - matches any string of any length (including zero length)</li>
	 * </ul>
	 *
	 * <p>Because of this, the like predicate only supports character data
	 * for the passed-in value.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null
	 */
	public static Predicate notLike(String propertyPath, CharSequence value) {
		return new NotLikePredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Create an in predicate.  Defines that the property
     * represented by the given path should be contained within the
	 * specified list of values.
	 *
	 * <p>Supports any of the valid types of value in the value list, with the
	 * restriction that all items in the list of values must be of the same type.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param values the values to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the values list is null, empty,
	 * contains object of different types, or includes objects of an invalid type
	 */
	public static <T> Predicate in(String propertyPath, T... values) {
		return new InPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValueList(values));
	}

	/**
	 * Create a not in predicate.  Defines that the property
     * represented by the given path should <strong>not</strong> be
	 * contained within the specified list of values.
	 *
	 * <p>Supports any of the valid types of value in the value list, with the
	 * restriction that all items in the list of values must be of the same type.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param values the values to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the values list is null, empty,
	 * contains object of different types, or includes objects of an invalid type
	 */
	public static <T> Predicate notIn(String propertyPath, T... values) {
		return new NotInPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValueList(values));
	}

    /**
	 * Create an in ignore case predicate.  Defines that the property
     * represented by the given path should be contained within the
	 * specified list of values ignoring the case of the values.
	 *
	 * <p>Supports any of CharSequence value in the value list, with the
	 * restriction that all items in the list of values must be of the same type.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param values the values to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the values list is null, empty,
	 * contains object of different types, or includes objects of an invalid type
	 */
	public static <T extends CharSequence> Predicate inIgnoreCase(String propertyPath, T... values) {
		return new InIgnoreCasePredicate(propertyPath, CriteriaSupportUtils.createCriteriaStringValueList(values));
	}

	/**
	 * Create a not in ignore case.  Defines that the property
     * represented by the given path should <strong>not</strong> be
	 * contained within the specified list of values ignoring the case of the values.
	 *
	 * <p>Supports any CharSequence value in the value list, with the
	 * restriction that all items in the list of values must be of the same type.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param values the values to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the values list is null, empty,
	 * contains object of different types, or includes objects of an invalid type
	 */
	public static <T extends CharSequence> Predicate notInIgnoreCase(String propertyPath, T... values) {
		return new NotInIgnoreCasePredicate(propertyPath, CriteriaSupportUtils.createCriteriaStringValueList(values));
	}

	/**
	 * Creates a greater than predicate.  Defines that the property
     * represented by the given path should be greater than the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate greaterThan(String propertyPath, Object value) {
		return new GreaterThanPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Creates a greater than or equal predicate.  Defines that the
     * property represented by the given path should be greater than
	 * or equal to the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate greaterThanOrEqual(String propertyPath, Object value) {
		return new GreaterThanOrEqualPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Creates a less than predicate.  Defines that the property
     * represented by the given path should be less than the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate lessThan(String propertyPath, Object value) {
		return new LessThanPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Creates a less than or equal predicate.  Defines that the
     * property represented by the given path should be less than
	 * or equal to the specified value.
	 *
	 * <p>Supports the following types of values:
	 *
	 * <ul>
	 *   <li>decimals</li>
	 *   <li>integers</li>
	 *   <li>date-time</li>
	 * </ul>
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 * @param value the value to compare with the property value located at the
	 * propertyPath
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 * @throws IllegalArgumentException if the value is null or of an invalid type
	 */
	public static Predicate lessThanOrEqual(String propertyPath, Object value) {
		return new LessThanOrEqualPredicate(propertyPath, CriteriaSupportUtils.determineCriteriaValue(value));
	}

	/**
	 * Creates an is null predicate.  Defines that the
	 * property represented by the given path should be null.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 */
	public static Predicate isNull(String propertyPath) {
		return new NullPredicate(propertyPath);
	}

	/**
	 * Creates an is not null predicate.  Defines that the property
     * represented by the given path should <strong>not</strong> be null.
	 *
	 * @param propertyPath the path to the property which should be evaluated
	 *
	 * @return a predicate
	 *
	 * @throws IllegalArgumentException if the propertyPath is null
	 */
	public static Predicate isNotNull(String propertyPath) {
		return new NotNullPredicate(propertyPath);
	}

	/**
	 * Creates an and predicate that is used to "and" predicates together.
	 *
	 * <p>An "and" predicate will evaluate the truth value of of it's
	 * internal predicates and, if all of them evaluate to true, then
	 * the and predicate itself should evaluate to true.  The implementation
     * of an and predicate may short-circuit.
     *
     * <p>
     *     This factory method does automatic reductions.
     * </p>
	 *
     * @param predicates to "and" together
     *
	 * @return a predicate
	 */
	public static Predicate and(Predicate... predicates) {
        //reduce single item compound
        if (predicates != null && predicates.length == 1 && predicates[0] != null) {
            return predicates[0];
        }
        final Set<Predicate> predicateSet = new HashSet<Predicate>();
        CollectionUtils.addAll(predicateSet, predicates);
        return new AndPredicate(predicateSet);
	}

	/**
	 * Creates an  or predicate that is used to "or" predicate together.
	 *
	 * <p>An "or" predicate will evaluate the truth value of of it's
	 * internal predicates and, if any one of them evaluate to true, then
	 * the or predicate itself should evaluate to true.  If all predicates
	 * contained within the "or" evaluate to false, then the or iself will
	 * evaluate to false.   The implementation of an or predicate may
     * short-circuit.
	 *
     * <p>
     *     This factory method does automatic reductions.
     * </p>
     * @param predicates to "or" together
     *
	 * @return a predicate
	 */
	public static Predicate or(Predicate... predicates) {
        //reduce single item compound
        if (predicates != null && predicates.length == 1 && predicates[0] != null) {
            return predicates[0];
        }

        final Set<Predicate> predicateSet = new HashSet<Predicate>();
        CollectionUtils.addAll(predicateSet, predicates);
		return new OrPredicate(predicateSet);
	}

    /**
     * This method will construct a predicate based on the predicate name.
     *
     * ex: "or", Or, "OrPredicate" passing the arguments to the construction method.
     *
     * @param name the name of the predicate to create.
     * @param args the args required to construct the predicate
     * @return the Predicate
     */
    public static Predicate dynConstruct(String name, Object... args) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name is blank");
        }

        final String correctedName = CriteriaSupportUtils.findDynName(name);
        for (Method m : PredicateFactory.class.getMethods()) {

            //currently this class does NOT overload therefore this method doesn't have to worry about
            //overload resolution - just get the Method handle based on the passed in name
            if (m.getName().equals(correctedName)) {
                try {
                    return (Predicate) m.invoke(null, args);
                } catch (InvocationTargetException e) {
                    throw new DynPredicateException(e);
                } catch (IllegalAccessException e) {
                    throw new DynPredicateException(e);
                }
            }
        }

        throw new DynPredicateException("predicate: " + name + " doesn't exist");
    }

    //this is really a fatal error (programming error)
    //and therefore is just a private exception not really meant to be caught
    private static class DynPredicateException extends RuntimeException {
        DynPredicateException(Throwable t) {
            super(t);
        }

        DynPredicateException(String s) {
            super(s);
        }
    }
}
