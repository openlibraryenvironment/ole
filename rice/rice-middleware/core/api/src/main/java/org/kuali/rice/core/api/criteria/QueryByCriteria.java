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

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.core.api.util.collect.CollectionUtils;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Defines a criteria-based query.  Consists of a {@link Predicate} definition
 * as well as a set of additional properties which control paging and other
 * aspects of the results which should be returned from the query.
 * 
 * <p>In order to construct a new {@link QueryByCriteria}, the {@link Builder}
 * should be used.  Use the {@link PredicateFactory} to construct
 * the predicate for use by the query.
 * 
 * <p>This class specifies nothing regarding how the query will be executed.
 * It is expected that an instance will be constructed and then passed to code
 * which understands how to execute the desired query.
 * 
 * <p>This class is mapped for use by JAXB and can therefore be used by clients
 * as part of remotable service definitions.
 * 
 * @see Predicate
 * @see PredicateFactory
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@XmlRootElement(name = QueryByCriteria.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = QueryByCriteria.Constants.TYPE_NAME, propOrder = {
		QueryByCriteria.Elements.PREDICATE,
		QueryByCriteria.Elements.START_AT_INDEX,
		QueryByCriteria.Elements.MAX_RESULTS,
		QueryByCriteria.Elements.COUNT_FLAG,
        QueryByCriteria.Elements.ORDER_BY_FIELDS,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class QueryByCriteria extends AbstractDataTransferObject {

	private static final long serialVersionUID = 2210627777648920180L;

    @XmlElements(value = {
        @XmlElement(name = AndPredicate.Constants.ROOT_ELEMENT_NAME, type = AndPredicate.class, required = false),
        @XmlElement(name = EqualPredicate.Constants.ROOT_ELEMENT_NAME, type = EqualPredicate.class, required = false),
        @XmlElement(name = EqualIgnoreCasePredicate.Constants.ROOT_ELEMENT_NAME, type = EqualIgnoreCasePredicate.class, required = false),
        @XmlElement(name = GreaterThanPredicate.Constants.ROOT_ELEMENT_NAME, type = GreaterThanPredicate.class, required = false),
        @XmlElement(name = GreaterThanOrEqualPredicate.Constants.ROOT_ELEMENT_NAME, type = GreaterThanOrEqualPredicate.class, required = false),
        @XmlElement(name = InPredicate.Constants.ROOT_ELEMENT_NAME, type = InPredicate.class, required = false),
        @XmlElement(name = InIgnoreCasePredicate.Constants.ROOT_ELEMENT_NAME, type = InIgnoreCasePredicate.class, required = false),
        @XmlElement(name = LessThanPredicate.Constants.ROOT_ELEMENT_NAME, type = LessThanPredicate.class, required = false),
        @XmlElement(name = LessThanOrEqualPredicate.Constants.ROOT_ELEMENT_NAME, type = LessThanOrEqualPredicate.class, required = false),
        @XmlElement(name = LikePredicate.Constants.ROOT_ELEMENT_NAME, type = LikePredicate.class, required = false),
        @XmlElement(name = NotEqualPredicate.Constants.ROOT_ELEMENT_NAME, type = NotEqualPredicate.class, required = false),
        @XmlElement(name = NotEqualIgnoreCasePredicate.Constants.ROOT_ELEMENT_NAME, type = NotEqualIgnoreCasePredicate.class, required = false),
        @XmlElement(name = NotInPredicate.Constants.ROOT_ELEMENT_NAME, type = NotInPredicate.class, required = false),
        @XmlElement(name = NotInIgnoreCasePredicate.Constants.ROOT_ELEMENT_NAME, type = NotInIgnoreCasePredicate.class, required = false),
        @XmlElement(name = NotLikePredicate.Constants.ROOT_ELEMENT_NAME, type = NotLikePredicate.class, required = false),
        @XmlElement(name = NotNullPredicate.Constants.ROOT_ELEMENT_NAME, type = NotNullPredicate.class, required = false),
        @XmlElement(name = NullPredicate.Constants.ROOT_ELEMENT_NAME, type = NullPredicate.class, required = false),
        @XmlElement(name = OrPredicate.Constants.ROOT_ELEMENT_NAME, type = OrPredicate.class, required = false)
    })
	private final Predicate predicate;
	
	@XmlElement(name = Elements.START_AT_INDEX, required = false)
	private final Integer startAtIndex;
		
	@XmlElement(name = Elements.MAX_RESULTS, required = false)
	private final Integer maxResults;
	
	@XmlJavaTypeAdapter(CountFlag.Adapter.class)
	@XmlElement(name = Elements.COUNT_FLAG, required = true)
	private final String countFlag;

    @XmlElementWrapper(name = Elements.ORDER_BY_FIELDS, required = false)
    @XmlElement(name = Elements.ORDER_BY_FIELD, required = false)
    private final List<OrderByField> orderByFields;


	@SuppressWarnings("unused")
	@XmlAnyElement
	private final Collection<Element> _futureElements = null;

	private QueryByCriteria() {
		this.predicate = null;
		this.startAtIndex = null;
		this.maxResults = null;
		this.countFlag = null;
        this.orderByFields = null;
	}

	private QueryByCriteria(Builder builder) {
		final Predicate[] preds = builder.predicates;
        if (preds != null && preds.length > 1) {
            //implicit "and"
            this.predicate = PredicateFactory.and(builder.predicates);
        } else if (preds != null && preds.length == 1) {
            this.predicate = builder.predicates[0];
        } else {
            this.predicate = null;
        }

		this.startAtIndex = builder.getStartAtIndex();
		this.maxResults = builder.getMaxResults();
		this.countFlag = builder.getCountFlag() == null ? null : builder.getCountFlag().getFlag();
        this.orderByFields = new ArrayList<OrderByField>(builder.getOrderByFields());
	}

	/**
	 * Returns the {@link Predicate} which will be used to execute the query.
	 * 
	 * @return can be null if no predicate was specified
	 */
	public Predicate getPredicate() {
		return this.predicate;
	}

	/**
	 * Returns the optional zero-based "start" index for rows returned.  When
	 * this query is executed, this property should be read to determine the
	 * first row which should be returned.  If the given index is beyond the
	 * end of the result set, then the resulting query should effectively
	 * return no rows (as opposed to producing an index-based out of bounds
	 * error).  If the value is null, then the results should start with the
	 * first row returned from the query.
	 *
     * <p>
     * Will never be less than 0
     *
	 * @return the starting row index requested by this query, or null if
	 * the results should start at the beginning of the result set
	 */
	public Integer getStartAtIndex() {
		return this.startAtIndex;
	}

	/**
	 * Returns the maximum number of results that this query is requesting
	 * to receive.  If null, then the query should return all rows, or as
	 * many as it can.  If the number request is larger than the number of
     * results then all results are returned.
	 *
     * <p>
     * Will never be less than 0
     *
	 * @return the maximum number of results to return from the query
	 */
	public Integer getMaxResults() {
		return this.maxResults;
	}

	/**
	 * Indicates whether or not a total row count should be returned with the
	 * query.  See {@link CountFlag} for more information on what each of these
	 * flags means.  This will never return null and defaults to
	 * {@link CountFlag#NONE}.
	 * 
	 * @return the flag specifying whether or not a total row count should be
	 * produced by the query
	 */
	public CountFlag getCountFlag() {
		return this.countFlag == null ? null : CountFlag.valueOf(this.countFlag);
	}

    /**
     * Returns the a list of fields that will be ordered depending on the orderDirection
     * when results are returned.
     *
     * @return List of field names that will affect the order of the returned rows
     */
    public List<OrderByField> getOrderByFields() {
        return CollectionUtils.unmodifiableListNullSafe(this.orderByFields);
    }

	public static final class Builder implements ModelBuilder, Serializable {

		private Predicate[] predicates;
		private Integer startAtIndex;
		private Integer maxResults;
		private CountFlag countFlag;
        private List<OrderByField> orderByFields;

		private Builder() {
			setCountFlag(CountFlag.NONE);
            setOrderByFields(new ArrayList<OrderByField>());
		}

		public static Builder create() {
            return new Builder();
		}

		public Integer getStartAtIndex() {
            return this.startAtIndex;
		}

		public void setStartAtIndex(Integer startAtIndex) {
            if (startAtIndex != null && startAtIndex < 0) {
                throw new IllegalArgumentException("startAtIndex < 0");
            }

            this.startAtIndex = startAtIndex;
		}

		public Integer getMaxResults() {
			return this.maxResults;
		}

		public void setMaxResults(Integer maxResults) {
			if (maxResults != null && maxResults < 0) {
                throw new IllegalArgumentException("maxResults < 0");
            }

            this.maxResults = maxResults;
		}

		public CountFlag getCountFlag() {
			return this.countFlag;
		}

		public void setCountFlag(CountFlag countFlag) {
			if (countFlag == null) {
                throw new IllegalArgumentException("countFlag was null");
            }

            this.countFlag = countFlag;
		}

        public List<OrderByField> getOrderByFields() {
            return this.orderByFields;
        }

        public void setOrderByFields(List<OrderByField> orderByFields) {
            if (orderByFields == null) {
                throw new IllegalArgumentException("orderByFields was null");
            }
            this.orderByFields = orderByFields;
        }

        /**
         * will return an array of the predicates.  may return null if no predicates were set.
         * @return the predicates
         */
		public Predicate[] getPredicates() {
			if (this.predicates == null) {
                return null;
            }

			//defensive copies on array
            return Arrays.copyOf(predicates, predicates.length);
		}

        /**
         * Sets the predicates. If multiple predicates are specified then they are wrapped
         * in an "and" predicate. If a null predicate is specified then there will be no
         * constraints on the query.
         * @param predicates the predicates to set.
         */
        public void setPredicates(Predicate... predicates) {
            //defensive copies on array
            this.predicates = predicates != null ? Arrays.copyOf(predicates, predicates.length) : null;
		}

        @Override
        public QueryByCriteria build() {
            return new QueryByCriteria(this);
        }

        /** convenience method to create an immutable criteria from one or more predicates. */
        public static QueryByCriteria fromPredicates(Predicate... predicates) {
            final Builder b = Builder.create();
            b.setPredicates(predicates);
            return b.build();
        }
    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "queryByCriteria";
		final static String TYPE_NAME = "QueryByCriteriaType";
	}

	/**
	 * A private class which exposes constants which define the XML element
	 * names to use when this object is marshaled to XML.
	 */
	static class Elements {
		final static String PREDICATE = "predicate";
		final static String START_AT_INDEX = "startAtIndex";
		final static String MAX_RESULTS = "maxResults";
		final static String COUNT_FLAG = "countFlag";
        final static String ORDER_BY_FIELDS = "orderByFields";
        final static String ORDER_BY_FIELD = "orderByField";
	}

}
