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
 * Defines a criteria-based query.  Consists of a {@link org.kuali.rice.core.api.criteria.Predicate} definition
 * as well as a set of additional properties which control paging and other
 * aspects of the results which should be returned from the query.
 *
 * <p>In order to construct a new {@link org.kuali.rice.core.api.criteria.OrderByField}, the {@link Builder}
 * should be used.  Use the {@link org.kuali.rice.core.api.criteria.PredicateFactory} to construct
 * the predicate for use by the query.
 *
 * <p>This class specifies nothing regarding how the query will be executed.
 * It is expected that an instance will be constructed and then passed to code
 * which understands how to execute the desired query.
 *
 * <p>This class is mapped for use by JAXB and can therefore be used by clients
 * as part of remotable service definitions.
 *
 * @see org.kuali.rice.core.api.criteria.Predicate
 * @see org.kuali.rice.core.api.criteria.PredicateFactory
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = OrderByField.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = OrderByField.Constants.TYPE_NAME, propOrder = {
		OrderByField.Elements.FIELD_NAME,
        OrderByField.Elements.ORDER_DIRECTION,
		CoreConstants.CommonElements.FUTURE_ELEMENTS })
public final class OrderByField extends AbstractDataTransferObject {

	private static final long serialVersionUID = 2210627777648920185L;

	@XmlElement(name = Elements.FIELD_NAME, required = true)
	private final String fieldName;

    @XmlJavaTypeAdapter(OrderDirection.Adapter.class)
    @XmlElement(name = Elements.ORDER_DIRECTION, required = true)
    private final String orderDirection;

	@SuppressWarnings("unused")
	@XmlAnyElement
	private final Collection<Element> _futureElements = null;

	private OrderByField() {
        this.fieldName = null;
        this.orderDirection = null;
	}

	private OrderByField(Builder builder) {
		this.fieldName = builder.getFieldName();
        this.orderDirection = builder.getOrderDirection() == null ? null : builder.getOrderDirection().getDirection();
	}


    /**
     * Returns the a name of the field to order results on
     *
     * @return field names that will affect the order of the returned rows
     */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Indicates what direction row ordering is used (ascending or descending)
     *
     * @return the flag specifying whether or not ascending or descending order is used
     */
    public OrderDirection getOrderDirection() {
        return this.orderDirection == null ? null : OrderDirection.valueOf(this.orderDirection);
    }


	public static final class Builder implements ModelBuilder, Serializable {

        private String fieldName;
        private OrderDirection orderDirection;

		private Builder() {
            setOrderDirection(OrderDirection.ASCENDING);
		}

        private Builder(String fieldName, OrderDirection orderDirection) {
            setOrderDirection(orderDirection);
            setFieldName(fieldName);
        }

		public static Builder create() {
            return new Builder();
		}

        public static Builder create(String fieldName, OrderDirection orderDirection) {
            return new Builder(fieldName, orderDirection);
        }

        public OrderDirection getOrderDirection() {
            return this.orderDirection;
        }

        public void setOrderDirection(OrderDirection orderDirection) {
            if (orderDirection == null) {
                throw new IllegalArgumentException("orderDirection was null");
            }
            this.orderDirection = orderDirection;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public void setFieldName(String fieldName) {
            if (fieldName == null) {
                throw new IllegalArgumentException("fieldName was null");
            }
            this.fieldName = fieldName;
        }



        @Override
        public OrderByField build() {
            return new OrderByField(this);
        }

    }

	/**
	 * Defines some internal constants used on this class.
	 */
	static class Constants {
		final static String ROOT_ELEMENT_NAME = "orderByField";
		final static String TYPE_NAME = "OrderByFieldType";
	}

	/**
	 * A private class which exposes constants which define the XML element
	 * names to use when this object is marshaled to XML.
	 */
	static class Elements {
        final static String FIELD_NAME = "fieldName";
        final static String ORDER_DIRECTION = "orderDirection";
	}

}
