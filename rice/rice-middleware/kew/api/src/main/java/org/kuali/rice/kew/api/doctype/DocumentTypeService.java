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
package org.kuali.rice.kew.api.doctype;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.api.rule.Rule;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * TODO ...
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "documentTypeService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface DocumentTypeService {

    @WebMethod(operationName = "getIdByName")
    @WebResult(name = "documentTypeId")
    @XmlElement(name = "documentTypeId", required = false)
    String getIdByName(@WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getNameById")
    @WebResult(name = "documentTypeName")
    @XmlElement(name = "documentTypeName", required = false)
    String getNameById(@WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getDocumentTypeById")
    @WebResult(name = "documentType")
    @XmlElement(name = "documentType", required = false)
    @Cacheable(value= DocumentType.Cache.NAME, key="'documentTypeId=' + #p0")
    DocumentType getDocumentTypeById(@WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getDocumentTypeByName")
    @WebResult(name = "documentType")
    @XmlElement(name = "documentType", required = false)
    @Cacheable(value= DocumentType.Cache.NAME, key="'documentTypeName=' + #p0")
    DocumentType getDocumentTypeByName(@WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "findAllDocumentTypes")
    @WebResult(name = "documentTypes")
    @XmlElement(name = "documentTypes", required = false)
    @Cacheable(value= DocumentType.Cache.NAME, key="'all'")
    List<DocumentType> findAllDocumentTypes();

    @WebMethod(operationName = "isSuperUserForDocumentTypeId")
    @WebResult(name = "isSuperUser")
    @XmlElement(name = "isSuperUser", required = true)
    boolean isSuperUserForDocumentTypeId(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "isSuperUserForDocumentTypeName")
    @WebResult(name = "isSuperUser")
    @XmlElement(name = "isSuperUser", required = true)
    boolean isSuperUserForDocumentTypeName(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;
    
    @WebMethod(operationName = "canSuperUserApproveSingleActionRequest")
    @WebResult(name = "isSuperUser")
    @XmlElement(name = "isSuperUser", required = true)
    boolean canSuperUserApproveSingleActionRequest(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "routeNodeInstances") List<RouteNodeInstance> routeNodeInstances,
            @WebParam(name = "routeStatusCode") String routeStatusCode)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "canSuperUserApproveDocument")
    @WebResult(name = "isSuperUser")
    @XmlElement(name = "isSuperUser", required = true)
    boolean canSuperUserApproveDocument(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "routeNodeInstances") List<RouteNodeInstance> routeNodeInstances,
            @WebParam(name = "routeStatusCode") String routeStatusCode)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "canSuperUserDisapproveDocument")
    @WebResult(name = "isSuperUser")
    @XmlElement(name = "isSuperUser", required = true)
    boolean canSuperUserDisapproveDocument(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "routeNodeInstances") List<RouteNodeInstance> routeNodeInstances,
            @WebParam(name = "routeStatusCode") String routeStatusCode)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "hasRouteNodeForDocumentTypeName")
    @WebResult(name = "hasRouteNode")
    @XmlElement(name = "hasRouteNode", required = true)
    boolean hasRouteNodeForDocumentTypeName(
            @WebParam(name = "routeNodeName") String routeNodeName,
            @WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "hasRouteNodeForDocumentTypeId")
    @WebResult(name = "hasRouteNode")
    @XmlElement(name = "hasRouteNode", required = true)
    boolean hasRouteNodeForDocumentTypeId(
            @WebParam(name = "routeNodeName") String routeNodeName,
            @WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "isActiveById")
    @WebResult(name = "isActive")
    @XmlElement(name = "isActive", required = true)
    boolean isActiveById(@WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "isActiveByName")
    @WebResult(name = "isActive")
    @XmlElement(name = "isActive", required = true)
    boolean isActiveByName(@WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;

    @WebMethod(operationName = "getRoutePathForDocumentTypeId")
    @WebResult(name = "routePath")
    @XmlElement(name = "routePath", required = false)
    @Cacheable(value= RoutePath.Cache.NAME, key="'documentTypeId=' + #p0")
    RoutePath getRoutePathForDocumentTypeId(@WebParam(name = "documentTypeId") String documentTypeId)
            throws RiceIllegalArgumentException;
    
    @WebMethod(operationName = "getRoutePathForDocumentTypeName")
    @WebResult(name = "routePath")
    @XmlElement(name = "routePath", required = false)
    @Cacheable(value= RoutePath.Cache.NAME, key="'documentTypeName=' + #p0")
    RoutePath getRoutePathForDocumentTypeName(@WebParam(name = "documentTypeName") String documentTypeName)
            throws RiceIllegalArgumentException;

}
