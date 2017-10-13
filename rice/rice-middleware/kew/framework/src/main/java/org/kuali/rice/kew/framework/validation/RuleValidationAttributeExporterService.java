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
package org.kuali.rice.kew.framework.validation;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.validation.RuleValidationContext;
import org.kuali.rice.kew.api.validation.ValidationResults;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 *  Service responsible for exposing custom RuleValidationAttribute functionality.
 *  This service is exposed by the node hosting the specified custom attribute.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KewFrameworkServiceLocator.RULE_VALIDATION_ATTRIBUTE_EXPORTER_SERVICE, targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface RuleValidationAttributeExporterService {
    /**
     * Validates the rule within the given RuleValidationContext.
     *
     * @return a ValidationResults object representing the results of the validation, if this is
     * empty or <code>null</code> this signifies that validation was successful.
     */
    @WebMethod(operationName = "validate")
    @WebResult(name = "validationResults")
    @XmlElement(name = "validationResults", required = false)
    ValidationResults validate(
                                @WebParam(name = "attributeName") String attributeName,
                                @WebParam(name = "validationContext") RuleValidationContext validationContext) throws RiceIllegalArgumentException;
}