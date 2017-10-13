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

import org.kuali.rice.core.api.mo.ModelBuilder;

import java.io.Serializable;

/**
 * This class works withe the lookup framework to customize a query.  It currently can do the following things:
 *
 * <ul>
 *  <li>transform/remove predicates</li>
 *  <li>transform/remove query results</li>
 * </ul>
 *
 * <p>
 * The predicate transform will applied to the predicates in the incoming query yielding a
 * a predicate.  If the predicate does not need to be transformed then the function can return the
 * incoming argument.  If the predicate should be removed the transform should return null.
 * This is a way to remove or change a predicate before the query is executed.
 * This is a good way to add new predicates to a query or to limit the result of the query before
 * it is executed. Transforms also allow a predicate referencing a property path that does not exist
 * on database mapped object to be changed to something that is valid.
 * </p>
 *
 * <p>
 * The result transform will be applied to the results of the query after the query is executed.
 * If the result does not need to be transformed then the function can return the
 * incoming argument. This is a way to remove or change a result after the query is executed.
 * </p>
 *
 * <p>transformers should not have to deal with null items</p>
 */
public class LookupCustomizer<T> {

    //FIXME: add wilcards to make predicate transform more flexible ie. EqualsPredicate to AndPredicate
    private final Transform<Predicate,Predicate> predicateTransform;
    private final Transform<T, T> resultTransform;

    private LookupCustomizer(Builder<T> builder) {
        this.predicateTransform = builder.getPredicateTransform() != null ? builder.getPredicateTransform() : IndentityTransform.<Predicate, Predicate>getInstance();
        this.resultTransform = builder.getResultTransform() != null ? builder.getResultTransform() : IndentityTransform.<T, T>getInstance();
    }

    public Transform<Predicate, Predicate> getPredicateTransform() {
        return predicateTransform;
    }

    public Transform<T, T> getResultTransform() {
        return resultTransform;
    }

    public static final class Builder<T> implements ModelBuilder, Serializable {

        private Transform<Predicate, Predicate> predicateTransform;
        private Transform<T, T> resultTransform;

        private Builder() {

        }

        public static <T> Builder<T> create() {
            return new Builder<T>();
        }

        public Transform<Predicate, Predicate> getPredicateTransform() {
            return predicateTransform;
        }

        public void setPredicateTransform(final Transform<Predicate, Predicate> predicateTransform) {
            this.predicateTransform = predicateTransform;
        }

        public Transform<T, T> getResultTransform() {
            return resultTransform;
        }

        public void setResultTransform(final Transform<T, T> resultTransform) {
            this.resultTransform = resultTransform;
        }

        @Override
        public LookupCustomizer<T> build() {
            return new LookupCustomizer<T>(this);
        }
    }
    public interface Transform<P, R> {
        R apply(P input);
    }

    /**
     * f: x -> x.  This function just returns the passed in parameter.
     *
     * @param <I> the type the function acts on.
     */
    private static final class IndentityTransform<I> implements Transform<I, I> {

        @SuppressWarnings("unchecked")
        private static final Transform INSTANCE = new IndentityTransform();

        @SuppressWarnings("unchecked")
        public static <P, R> Transform<P, R> getInstance() {
            return INSTANCE;
        }

        @Override
        public I apply(final I input) {
            return input;
        }
    }
}
