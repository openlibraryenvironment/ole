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
package org.kuali.rice.krms.api.engine.expression;

import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.framework.engine.expression.EngineComparatorExtension;
import org.kuali.rice.krms.framework.engine.expression.StringCoercionExtension;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Interface for registering {@link EngineComparatorExtension} for use as a
 * {@link org.kuali.rice.krms.framework.engine.expression.ComparisonOperator} when comparing
 * {@link org.kuali.rice.krms.framework.engine.Proposition} {@link Term}s
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "comparisonOperatorService", targetNamespace = KrmsConstants.Namespaces.KRMS_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ComparisonOperatorService extends StringCoercionExtension {

    /**
     * The {@link List} of {@link EngineComparatorExtension}s.
     * @return List<EngineComparatorExtension> of configured {@link EngineComparatorExtension}s.
     */
    public List<EngineComparatorExtension> getOperators();

    /**
     * List<EngineComparatorExtension> to use.
     * @param operators
     */
    public void setOperators(List<EngineComparatorExtension> operators);

    @Override
    @WebMethod(operationName = "canCoerce")
    @WebResult(name = "coerced")
    public boolean canCoerce(String type, String value);

    @Override
    @WebMethod(operationName = "coerce")
    @WebResult(name = "coerced")
    public Object coerce(String type, String value);

    /**
     * Returns the int result of a compare between the lhs and rhs objects.
     * @param lhs left hand side object
     * @param rhs right hand side object
     * @return int result of compare between lhs and rhs objects
     */
    public int compare(Object lhs, Object rhs);

    /**
     * Does the service have an Extension that can compare the given objects?
     *
     * @param leftHandSide left hand side Object
     * @param rightHandSide right hand side Object
     * @return boolean true a configured {@link EngineComparatorExtension} can compare the lhs and rhs Objects.
     */
    public boolean canCompare(Object leftHandSide, Object rightHandSide);

    /**
     * {@link EngineComparatorExtension} that canCompare the given Objects
     * @param leftHandSide left hand side Object
     * @param rightHandSide right hand side Object
     * @return the EngineComparatorExtension that can compare the given Objects.
     */
    public EngineComparatorExtension findComparatorExtension(Object leftHandSide, Object rightHandSide);

    /**
     *
     * @return List<StringCoercionExtension>
     */
    // this would move to a StringCoercionExtensionService if we go that way
    public List<StringCoercionExtension> getStringCoercionExtensions();

    /**
     * The {@link List} of {@link StringCoercionExtension}s.
     * @param stringCoercionExtensions
     */
    // this would move to a StringCoercionExtensionService if we go that way
    public void setStringCoercionExtensions(List<StringCoercionExtension> stringCoercionExtensions);

    /**
     *
     * @param type to coerce
     * @param value to coerce
     * @return {@link StringCoercionExtension} that can coerce the given type and value
     */
    // this would move to a StringCoercionExtensionService if we go that way
    StringCoercionExtension findStringCoercionExtension(String type, String value);

}
