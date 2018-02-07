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
package org.kuali.rice.krms.impl;


import org.kuali.rice.core.api.criteria.CriteriaValue;
import org.kuali.rice.core.api.criteria.LookupCustomizer;
import org.kuali.rice.core.api.criteria.MultiValuedPredicate;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PropertyPathPredicate;
import org.kuali.rice.core.api.criteria.SingleValuedPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;

/**
 * this is an internal class used by krms (copied from kim) service implementations that have generic lookup methods for classes
 * with "attributes".
 */
public final class AttributeTransform implements LookupCustomizer.Transform<Predicate, Predicate> {

    private static final LookupCustomizer.Transform<Predicate, Predicate> INSTANCE = new AttributeTransform();
    private static final String ATTRIBUTE_DETAILS_ATTRIBUTE_VALUE = "attributeDetails.attributeValue";
    private static final String ATTRIBUTE_DETAILS_ATTRIBUTE_NAME = "attributeDetails.krmsAttribute.attributeName";
    private static final String ATTRIBUTES_REGEX = "^attributes\\[\\w*\\]$";
    private static final Pattern ATTRIBUTES_PATTERN = Pattern.compile(ATTRIBUTES_REGEX);

    private AttributeTransform() {

    }

    @Override
    public Predicate apply(final Predicate input) {
        if (input instanceof PropertyPathPredicate) {
            String pp = ((PropertyPathPredicate) input).getPropertyPath();
            if (isAttributesPredicate(pp)) {
                final String attributeName = pp.substring(pp.indexOf('[') + 1, pp.indexOf(']'));

                final Predicate attrValue;
                if (input instanceof SingleValuedPredicate) {
                    final CriteriaValue<?> value = ((SingleValuedPredicate) input).getValue();
                    attrValue = dynConstruct(input.getClass().getSimpleName(), ATTRIBUTE_DETAILS_ATTRIBUTE_VALUE, value.getValue());
                } else if (input instanceof MultiValuedPredicate) {
                    final Set<? extends CriteriaValue<?>> values = ((MultiValuedPredicate) input).getValues();
                    List<Object> l = new ArrayList<Object>();
                    for (CriteriaValue<?> v : values) {
                        l.add(v.getValue());
                    }

                    attrValue = dynConstruct(input.getClass().getSimpleName(), ATTRIBUTE_DETAILS_ATTRIBUTE_VALUE, l.toArray());
                } else {
                    attrValue = dynConstruct(input.getClass().getSimpleName(), ATTRIBUTE_DETAILS_ATTRIBUTE_VALUE);
                }
                return and(equal(ATTRIBUTE_DETAILS_ATTRIBUTE_NAME, attributeName), attrValue);
            }
        }

        return input;
    }

    private boolean isAttributesPredicate(String pp) {
        Matcher matcher = ATTRIBUTES_PATTERN.matcher(pp);
        return matcher.matches();
    }

    public static LookupCustomizer.Transform<Predicate, Predicate> getInstance() {
        return INSTANCE;
    }
}