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
package org.kuali.rice.kew.framework.rule.attribute;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.framework.KewFrameworkServiceLocator;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * A remotable service which handles processing of a client application's document search customizations.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KewFrameworkServiceLocator.WORKFLOW_RULE_ATTRIBUTE_HANDLER_SERVICE, targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WorkflowRuleAttributeHandlerService {

    /**
     * Using the WorkflowRuleAttribute defined by the given extension definition, does the following: validates the
     * given rule attribute parameters, fetches the list of RemotableAttributeField objects to display for the rule, and
     * then determines the rule extension values produced by the attribute and returns this information as a
     * WorkflowRuleAttributeFields object.
     *
     * @param parameters the parameters against which to validate
     * @param extensionDefinition extension definition of the WorkflowRuleAttribute, cannot be null or blank.
     * @param required the required flag which should be passed to the WorkflowRuleAttribute prior to processing
     *
     * @return the WorkflowRuleAttributeFields, will not return null.
     *
     * @throws RiceIllegalArgumentException if the extensionDefinition is null or blank
     * @throws RiceIllegalArgumentException if the WorkflowRuleAttribute is not found
     */
    @WebMethod(operationName = "getRuleFields")
    @XmlElement(name = "ruleFields", required = true)
    @WebResult(name = "ruleFields")
    WorkflowRuleAttributeFields getRuleFields(@WebParam(name = "parameters")
                                              @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                              Map<String, String> parameters,
                                              @WebParam(name = "extensionDefinition")
                                              ExtensionDefinition extensionDefinition,
                                              @WebParam(name = "required")
                                              boolean required)
            throws RiceIllegalArgumentException;

    /**
     * Using the WorkflowRuleAttribute defined by the given extension definition, does the following: validates the
     * given rule attribute parameters, fetches the list of RemotableAttributeField objects to display for the rule
     * search, and then determines the rule extension values produced by the attribute and returns this information as a
     * WorkflowRuleAttributeFields object.
     *
     * @param parameters the parameters against which to validate
     * @param extensionDefinition extension definition of the WorkflowRuleAttribute, cannot be null or blank.
     * @param required the required flag which should be passed to the WorkflowRuleAttribute prior to processing
     *
     * @return the WorkflowRuleAttributeFields, will not return null.
     *
     * @throws RiceIllegalArgumentException if the extensionDefinition is null or blank
     * @throws RiceIllegalArgumentException if the WorkflowRuleAttribute is not found
     */
    @WebMethod(operationName = "getSearchFields")
    @XmlElement(name = "searchFields", required = true)
    @WebResult(name = "searchFields")
    WorkflowRuleAttributeFields getSearchFields(@WebParam(name = "parameters")
                                                @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                                Map<String, String> parameters,
                                                @WebParam(name = "extensionDefinition")
                                                ExtensionDefinition extensionDefinition,
                                                @WebParam(name = "required")
                                                boolean required)
            throws RiceIllegalArgumentException;

    /**
     * Using the WorkflowRuleAttribute defined by the given extension definition, does the following: validates the
     * given rule attribute parameters, fetches the list of RemotableAttributeField objects to display for rule 
     * reporting then determines the rule extension values produced by the attribute and returns this information as a
     * WorkflowRuleAttributeFields object.
     *
     * @param parameters the parameters against which to validate
     * @param extensionDefinition extension definition of the WorkflowRuleAttribute, cannot be null or blank.
     * @param required the required flag which should be passed to the WorkflowRuleAttribute prior to processing
     *
     * @return the WorkflowRuleAttributeFields, will not return null.
     *
     * @throws RiceIllegalArgumentException if the extensionDefinition is null or blank
     * @throws RiceIllegalArgumentException if the WorkflowRuleAttribute is not found
     */
    @WebMethod(operationName = "getRoutingDataFields")
    @XmlElement(name = "routingDataFields", required = true)
    @WebResult(name = "routingDataFields")
    WorkflowRuleAttributeFields getRoutingDataFields(@WebParam(name = "parameters")
                                              @XmlJavaTypeAdapter(value = MapStringStringAdapter.class)
                                              Map<String, String> parameters,
                                              @WebParam(name = "extensionDefinition")
                                              ExtensionDefinition extensionDefinition,
                                              @WebParam(name = "required")
                                              boolean required)
            throws RiceIllegalArgumentException;
 
    /**
     * Gets a List of {@link RoleName} objects produced by the RoleAttribute defined by the given extension definition.
     *
     * @param extensionDefinition extension definition of the RoleAttribute, cannot be null or blank.
     *
     * @return an immutable list of RoleName objects, if none are defined or the supplied extension definition is not
     * an instance of RoleAttribute, this will return an empty list. This method will never return null.
     *
     * @throws RiceIllegalArgumentException if the extensionDefinition is null or blank
     * @throws RiceIllegalArgumentException if the RoleAttribute is not found
     */
    @WebMethod(operationName = "getRoleNames")
    @XmlElementWrapper(name = "roleNames", required = true)
    @XmlElement(name = "roleName", required = false)
    @WebResult(name = "roleNames")
    List<RoleName> getRoleNames(@WebParam(name = "extensionDefinition")
                                ExtensionDefinition extensionDefinition)
            throws RiceIllegalArgumentException;

}