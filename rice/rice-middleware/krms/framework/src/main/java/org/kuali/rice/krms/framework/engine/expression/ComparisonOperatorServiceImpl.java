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
package org.kuali.rice.krms.framework.engine.expression;

import org.kuali.rice.krms.api.engine.expression.ComparisonOperatorService;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * {@link ComparisonOperatorService} Implementation.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComparisonOperatorServiceImpl implements ComparisonOperatorService {

    private List<EngineComparatorExtension> operators = new LinkedList<EngineComparatorExtension>();

    private List<StringCoercionExtension> stringCoercionExtensions = new LinkedList<StringCoercionExtension>();

    private ComparisonOperatorServiceImpl() {}

    /**
     * Factory method for getting a {@link ComparisonOperatorService}
     * @return a {@link ComparisonOperatorService}
     */
    public static ComparisonOperatorService getInstance() {
        return new ComparisonOperatorServiceImpl();
    }

    @Override
    public List<StringCoercionExtension> getStringCoercionExtensions() {
        return stringCoercionExtensions;
    }

    @Override
    public void setStringCoercionExtensions(List<StringCoercionExtension> stringCoercionExtensions) {
        this.stringCoercionExtensions = stringCoercionExtensions;
    }

    @Override
    public List<EngineComparatorExtension> getOperators() {
        return operators;
    }

    @Override
    public void setOperators(List<EngineComparatorExtension> operators) {
        this.operators = operators;
    }

    /**
     * Returns the {@link EngineComparatorExtension} that can compare the lhs and rhs objects.  If none, then returns the
     * {@link DefaultComparisonOperator}
     * @param lhs left hand side object
     * @param rhs right hand side object
     * @return an EngineComparatorExtension that can compare the lhs and rhs
     */
    @Override
    public EngineComparatorExtension findComparatorExtension(Object lhs, Object rhs) {
        EngineComparatorExtension extension;
        Iterator<EngineComparatorExtension> opIter = operators.iterator();
        while (opIter.hasNext()) {
            extension = opIter.next();
            if (extension.canCompare(lhs, rhs)) {
                return extension;
            }
        }
        return new DefaultComparisonOperator();
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        return findComparatorExtension(lhs, rhs).compare(lhs, rhs);
    }

    @Override
    public boolean canCompare(Object lhs, Object rhs) {
        return findComparatorExtension(lhs, rhs) != null;
    }

    /**
     * Returns the {@link EngineComparatorExtension} that can coerce the lhs and rhs objects.  If none, then returns the
     * {@link DefaultComparisonOperator}, which also handles default coercion
     * @param type class type to attempt to coerce to
     * @param value value to attempt to coerce the given type with
     * @return an EngineComparatorExtension that can coerce the type and value
     */
    @Override
    public StringCoercionExtension findStringCoercionExtension(String type, String value) {
        StringCoercionExtension extension;
        Iterator<StringCoercionExtension> opIter = stringCoercionExtensions.iterator();
        while (opIter.hasNext()) {
            extension = opIter.next();
            if (extension.canCoerce(type, value)) {
                return extension;
            }
        }
        return new DefaultComparisonOperator(); // default coercion is also in the DefaultComparisonOperator
    }

    @Override
    public boolean canCoerce(String type, String value) {
        return findStringCoercionExtension(type, value) != null;
    }

    @Override
    public Object coerce(String type, String value) {
        return findStringCoercionExtension(type, value).coerce(type, value);
    }
}
