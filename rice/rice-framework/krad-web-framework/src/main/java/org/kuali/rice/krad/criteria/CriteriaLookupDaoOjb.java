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
package org.kuali.rice.krad.criteria;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.criteria.AndPredicate;
import org.kuali.rice.core.api.criteria.CompositePredicate;
import org.kuali.rice.core.api.criteria.CountFlag;
import org.kuali.rice.core.api.criteria.CriteriaValue;
import org.kuali.rice.core.api.criteria.EqualIgnoreCasePredicate;
import org.kuali.rice.core.api.criteria.EqualPredicate;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.GreaterThanOrEqualPredicate;
import org.kuali.rice.core.api.criteria.GreaterThanPredicate;
import org.kuali.rice.core.api.criteria.InIgnoreCasePredicate;
import org.kuali.rice.core.api.criteria.InPredicate;
import org.kuali.rice.core.api.criteria.LessThanOrEqualPredicate;
import org.kuali.rice.core.api.criteria.LessThanPredicate;
import org.kuali.rice.core.api.criteria.LikePredicate;
import org.kuali.rice.core.api.criteria.LookupCustomizer;
import org.kuali.rice.core.api.criteria.MultiValuedPredicate;
import org.kuali.rice.core.api.criteria.NotEqualIgnoreCasePredicate;
import org.kuali.rice.core.api.criteria.NotEqualPredicate;
import org.kuali.rice.core.api.criteria.NotInIgnoreCasePredicate;
import org.kuali.rice.core.api.criteria.NotInPredicate;
import org.kuali.rice.core.api.criteria.NotLikePredicate;
import org.kuali.rice.core.api.criteria.NotNullPredicate;
import org.kuali.rice.core.api.criteria.NullPredicate;
import org.kuali.rice.core.api.criteria.OrPredicate;
import org.kuali.rice.core.api.criteria.OrderByField;
import org.kuali.rice.core.api.criteria.OrderDirection;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PropertyPathPredicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.criteria.SingleValuedPredicate;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CriteriaLookupDaoOjb extends PlatformAwareDaoBaseOjb implements CriteriaLookupDao {

    @Override
    public <T> GenericQueryResults<T> lookup(final Class<T> queryClass, final QueryByCriteria criteria) {
        return lookup(queryClass, criteria, LookupCustomizer.Builder.<T>create().build());
    }

    @Override
    public <T> GenericQueryResults<T> lookup(final Class<T> queryClass, final QueryByCriteria criteria, LookupCustomizer<T> customizer) {
        if (queryClass == null) {
            throw new IllegalArgumentException("queryClass is null");
        }

        if (criteria == null) {
            throw new IllegalArgumentException("criteria is null");
        }

        if (customizer == null) {
            throw new IllegalArgumentException("customizer is null");
        }

        final Criteria parent = new Criteria();

        if (criteria.getPredicate() != null) {
            addPredicate(criteria.getPredicate(), parent, customizer.getPredicateTransform());
        }

        switch (criteria.getCountFlag()) {
            case ONLY:
                return forCountOnly(queryClass, criteria, parent);
            case NONE:
                return forRowResults(queryClass, criteria, parent, criteria.getCountFlag(), customizer.getResultTransform());
            case INCLUDE:
                return forRowResults(queryClass, criteria, parent, criteria.getCountFlag(), customizer.getResultTransform());
            default: throw new UnsupportedCountFlagException(criteria.getCountFlag());
        }
    }

    /** gets results where the actual rows are requested. */
    private <T> GenericQueryResults<T> forRowResults(final Class<T> queryClass, final QueryByCriteria criteria, final Criteria ojbCriteria, CountFlag flag, LookupCustomizer.Transform<T, T> transform) {
        final org.apache.ojb.broker.query.QueryByCriteria ojbQuery = QueryFactory.newQuery(queryClass, ojbCriteria);
        final GenericQueryResults.Builder<T> results = GenericQueryResults.Builder.<T>create();

        if (flag == CountFlag.INCLUDE) {
            results.setTotalRowCount(getPersistenceBrokerTemplate().getCount(ojbQuery));
        }

        //ojb's is 1 based, our query api is zero based
        final int startAtIndex = criteria.getStartAtIndex() != null ? criteria.getStartAtIndex() + 1 : 1;
        ojbQuery.setStartAtIndex(startAtIndex);

        if (criteria.getMaxResults() != null) {
            //not subtracting one from MaxResults in order to retrieve
            //one extra row so that the MoreResultsAvailable field can be set
            ojbQuery.setEndAtIndex(criteria.getMaxResults() + startAtIndex);
        }

        if (CollectionUtils.isNotEmpty(criteria.getOrderByFields())) {
            for (OrderByField orderByField : criteria.getOrderByFields()) {
                if (OrderDirection.ASCENDING.equals(orderByField.getOrderDirection())) {
                    ojbQuery.addOrderBy(orderByField.getFieldName(), true);
                } else if (OrderDirection.DESCENDING.equals(orderByField.getOrderDirection())) {
                    ojbQuery.addOrderBy(orderByField.getFieldName(), false);
                }
            }
        }

        @SuppressWarnings("unchecked")
        final List<T> rows = new ArrayList<T>(getPersistenceBrokerTemplate().getCollectionByQuery(ojbQuery));

        if (criteria.getMaxResults() != null && rows.size() > criteria.getMaxResults()) {
            results.setMoreResultsAvailable(true);
            //remove the extra row that was returned
            rows.remove(criteria.getMaxResults().intValue());
        }

        results.setResults(transformResults(rows, transform));
        return results.build();
    }

    private static <T> List<T> transformResults(List<T> results, LookupCustomizer.Transform<T, T> transform) {
        final List<T> list = new ArrayList<T>();
        for (T r : results) {
            list.add(transform.apply(r));
        }
        return list;
    }

    /** gets results where only the count is requested. */
    private <T> GenericQueryResults<T> forCountOnly(final Class<T> queryClass, final QueryByCriteria criteria, final Criteria ojbCriteria) {
        final Query ojbQuery = QueryFactory.newQuery(queryClass, ojbCriteria);
        final GenericQueryResults.Builder<T> results = GenericQueryResults.Builder.<T>create();
        results.setTotalRowCount(getPersistenceBrokerTemplate().getCount(ojbQuery));

        return results.build();
    }

    /** adds a predicate to a Criteria.*/
    private void addPredicate(Predicate p, Criteria parent, LookupCustomizer.Transform<Predicate, Predicate> transform) {
        p = transform.apply(p);

        if (p instanceof PropertyPathPredicate) {
            final String pp = ((PropertyPathPredicate) p).getPropertyPath();
            if (p instanceof NotNullPredicate) {
                parent.addNotNull(pp);
            } else if (p instanceof NullPredicate) {
                parent.addIsNull(pp);
            } else if (p instanceof SingleValuedPredicate) {
                addSingleValuePredicate((SingleValuedPredicate) p, parent);
            } else if (p instanceof MultiValuedPredicate) {
                addMultiValuePredicate((MultiValuedPredicate) p, parent);
            } else {
                throw new UnsupportedPredicateException(p);
            }
        } else if (p instanceof CompositePredicate) {
            addCompositePredicate((CompositePredicate) p, parent, transform);
        } else {
            throw new UnsupportedPredicateException(p);
        }
    }

    /** adds a single valued predicate to a Criteria. */
    private void addSingleValuePredicate(SingleValuedPredicate p, Criteria parent) {
        final Object value = getVal(p.getValue());
        final String pp = p.getPropertyPath();
        if (p instanceof EqualPredicate) {
            parent.addEqualTo(pp, value);
        } else if (p instanceof EqualIgnoreCasePredicate) {
            parent.addEqualTo(genUpperFunc(pp), ((String) value).toUpperCase());
        } else if (p instanceof GreaterThanOrEqualPredicate) {
            parent.addGreaterOrEqualThan(pp, value);
        } else if (p instanceof GreaterThanPredicate) {
            parent.addGreaterThan(pp, value);
        } else if (p instanceof LessThanOrEqualPredicate) {
            parent.addLessOrEqualThan(pp, value);
        } else if (p instanceof LessThanPredicate) {
            parent.addLessThan(pp, value);
        } else if (p instanceof LikePredicate) {
            //no need to convert * or ? since ojb handles the conversion/escaping
            parent.addLike(genUpperFunc(pp), ((String) value).toUpperCase());
        } else if (p instanceof NotEqualPredicate) {
            parent.addNotEqualTo(pp, value);
        } else if (p instanceof NotEqualIgnoreCasePredicate) {
            parent.addNotEqualTo(genUpperFunc(pp), ((String) value).toUpperCase());
        } else if (p instanceof NotLikePredicate) {
            parent.addNotLike(pp, value);
        } else {
            throw new UnsupportedPredicateException(p);
        }
    }

    /** adds a multi valued predicate to a Criteria. */
    private void addMultiValuePredicate(MultiValuedPredicate p, Criteria parent) {
        final String pp = p.getPropertyPath();
        if (p instanceof InPredicate) {
            final Set<?> values = getVals(p.getValues());
            parent.addIn(pp, values);
        } else if (p instanceof InIgnoreCasePredicate) {
            final Set<String> values = toUpper(getValsUnsafe(((InIgnoreCasePredicate) p).getValues()));
            parent.addIn(genUpperFunc(pp), values);
        } else if (p instanceof NotInPredicate) {
            final Set<?> values = getVals(p.getValues());
            parent.addNotIn(pp, values);
        } else if (p instanceof NotInIgnoreCasePredicate) {
            final Set<String> values = toUpper(getValsUnsafe(((NotInIgnoreCasePredicate) p).getValues()));
            parent.addNotIn(genUpperFunc(pp), values);
        } else {
            throw new UnsupportedPredicateException(p);
        }
    }

    /** adds a composite predicate to a Criteria. */
    private void addCompositePredicate(final CompositePredicate p, final Criteria parent,  LookupCustomizer.Transform<Predicate, Predicate> transform) {
        for (Predicate ip : p.getPredicates()) {
            final Criteria inner = new Criteria();
            addPredicate(ip, inner, transform);
            if (p instanceof AndPredicate) {
                parent.addAndCriteria(inner);
            } else if (p instanceof OrPredicate) {
                parent.addOrCriteria(inner);
            } else {
                throw new UnsupportedPredicateException(p);
            }
        }
    }

    private static <U extends CriteriaValue<?>> Object getVal(U toConv) {
        Object o = toConv.getValue();
        if (o instanceof DateTime) {
            return new Timestamp(((DateTime) o).getMillis());
        }
        return o;
    }

    //this is unsafe b/c values could be converted resulting in a classcast exception
    @SuppressWarnings("unchecked")
    private static <T, U extends CriteriaValue<T>> Set<T> getValsUnsafe(Set<? extends U> toConv) {
        return (Set<T>) getVals(toConv);
    }

    private static Set<?> getVals(Set<? extends CriteriaValue<?>> toConv) {
        final Set<Object> values = new HashSet<Object>();
        for (CriteriaValue<?> value : toConv) {
            values.add(getVal(value));
        }
        return values;
    }

    //eliding performance for function composition....
    private static Set<String> toUpper(Set<String> strs) {
        final Set<String> values = new HashSet<String>();
        for (String value : strs) {
            values.add(value.toUpperCase());
        }
        return values;
    }

    private String getUpperFunction() {
        return getDbPlatform().getUpperCaseFunction();
    }

    private String genUpperFunc(String pp) {
        return new StringBuilder(getUpperFunction()).append("(").append(pp).append(")").toString();
    }

    /** this is a fatal error since this implementation should support all known predicates. */
    private static class UnsupportedPredicateException extends RuntimeException {
        private UnsupportedPredicateException(Predicate predicate) {
            super("Unsupported predicate [" + String.valueOf(predicate) + "]");
        }
    }

    /** this is a fatal error since this implementation should support all known count flags. */
    private static class UnsupportedCountFlagException extends RuntimeException {
        private UnsupportedCountFlagException(CountFlag flag) {
            super("Unsupported predicate [" + String.valueOf(flag) + "]");
        }
    }
}
