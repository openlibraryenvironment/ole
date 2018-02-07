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
package org.kuali.rice.coreservice.api.component;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Defines the contract for a service which can be used to interact with the Rice core component store.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "componentService", targetNamespace = CoreConstants.Namespaces.CORE_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface ComponentService {


    /**
     * This will return a {@link org.kuali.rice.coreservice.api.component.Component} with the given namespaceCode and
     * componentCode
     *
     * @param namespaceCode the namespaceCode of the component
     * @param componentCode the componentCode of the component
     * @return the component with the given namespaceCode and componentCode
     * @throws RiceIllegalArgumentException if the namespaceCode or componentCode is null or blank
     */
    @WebMethod(operationName = "getComponentByCode")
    @WebResult(name = "component")
    Component getComponentByCode(
            @WebParam(name = "namespaceCode") String namespaceCode,
            @WebParam(name = "componentCode") String componentCode
    ) throws RiceIllegalArgumentException;


    /**
     * This will return a list of all {@link org.kuali.rice.coreservice.api.component.Component} with the
     * given namespaceCode
     *
     * @param namespaceCode the namespaceCode of the component
     * @return a list of components with the given namespaceCode
     * @throws RiceIllegalArgumentException if the namespaceCode is null or blank
     */
    @WebMethod(operationName = "getAllComponentsByNamespaceCode")
    @WebResult(name = "components")
    @XmlElementWrapper(name = "components", required = true)
	@XmlElement(name = "component", required = false)
    List<Component> getAllComponentsByNamespaceCode(
            @WebParam(name = "namespaceCode") String namespaceCode
    ) throws RiceIllegalArgumentException;


    /**
     * This will return a list of active {@link org.kuali.rice.coreservice.api.component.Component} with the
     * given namespaceCode
     *
     * @param namespaceCode the namespaceCode of the component
     * @return a list of active components with the given namespaceCode
     * @throws RiceIllegalArgumentException if the namespaceCode is null or blank
     */
    @WebMethod(operationName = "getActiveComponentsByNamespaceCode")
    @WebResult(name = "components")
    @XmlElementWrapper(name = "components", required = true)
	@XmlElement(name = "component", required = false)
    List<Component> getActiveComponentsByNamespaceCode(
            @WebParam(name = "namespaceCode") String namespaceCode
    ) throws RiceIllegalArgumentException;


    /**
     * This will return a list of derived {@link org.kuali.rice.coreservice.api.component.Component} with the
     * given componentSetId
     *
     * @param componentSetId the componentSetId of the component
     * @return a list of components with the given componentSetId
     * @throws RiceIllegalArgumentException if the componentSetId is null or blank
     */
    @WebMethod(operationName = "getDerivedComponentSet")
    @WebResult(name = "components")
    @XmlElementWrapper(name = "components", required = true)
	@XmlElement(name = "component", required = false)
    List<Component> getDerivedComponentSet(@WebParam(name = "componentSetId") String componentSetId) throws RiceIllegalArgumentException;

    /**
     * Publishes the given set of derived components to make them available to the component system.  It should only
     * ever be necessary to invoke this service whenever published components for an application change.  However, it is
     * always safe to invoke this method even if the client cannot make this determination as the implementation of this
     * service should be responsible for handling the given information and ignoring it if no publication needs to
     * occur.  To this end, this method should be idempotent.
     *
     * <p>When invoked, the set of components known to the component system for the given component set id will be
     * replaced with the given list.  Any previously published components for the component set id which are not
     * contained within the given list will be deleted.</p>
     *
     * <p>The {@code componentSetId} should be an identifier generated by the client application which uniquely
     * identifies the component set being published (either newly published or updated).  A simple value to use would
     * be the {@code applicationId} of the client application but this would mean an application could only ever publish
     * a single custom component set.  So the {@code applicationId} could also be combined with some additional
     * information on the source of the components being published if a particular application needs to publish custom
     * components from multiple sources.</p>
     *
     * <p>The {@code componentSetId} on each of the components supplied in the list must either be null or equal to the
     * component set id that is passed to this method, otherwise a {@code RiceIllegalArgumentException} will be thrown.</p>
     *
     * @param componentSetId an id that uniquely identifies this set of components being checked.  The service will use
     * this to track the components being published
     * @param components the components to publish, may be empty or null, in which case all published components for the
     * given component set id will be deleted from the component system if any exist
     *
     * @throws RiceIllegalArgumentException if componentSetId is a null or blank value
     * @throws RiceIllegalArgumentException if any of the components in the given list have a non-null componentSetId
     * which does not match the componentSetId parameter supplied to this method
     */
    @WebMethod(operationName = "publishDerivedComponents")
    void publishDerivedComponents(@WebParam(name = "componentSetId") String componentSetId,
            @WebParam(name = "components") List<Component> components) throws RiceIllegalArgumentException;

}
