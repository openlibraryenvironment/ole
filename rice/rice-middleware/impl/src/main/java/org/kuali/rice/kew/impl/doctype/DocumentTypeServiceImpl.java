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
package org.kuali.rice.kew.impl.doctype;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.kew.api.doctype.DocumentType;
import org.kuali.rice.kew.api.doctype.DocumentTypeService;
import org.kuali.rice.kew.api.doctype.ProcessDefinition;
import org.kuali.rice.kew.api.doctype.RoutePath;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;
import org.kuali.rice.kew.doctype.dao.DocumentTypeDAO;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Reference implementation of the {@link DocumentTypeService}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
public class DocumentTypeServiceImpl implements DocumentTypeService {

    private static final Logger LOG = Logger.getLogger(DocumentTypeServiceImpl.class);

    private DocumentTypeDAO documentTypeDao;

    @Override
    public String getIdByName(String documentTypeName) {
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was null or blank");
        }
        return documentTypeDao.findDocumentTypeIdByName(documentTypeName);
    }

    @Override
    public String getNameById(String documentTypeId) {
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        return documentTypeDao.findDocumentTypeNameById(documentTypeId);
    }

    @Override
    public DocumentType getDocumentTypeById(String documentTypeId) {
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType documentTypeBo = documentTypeDao.findById(documentTypeId);
        return org.kuali.rice.kew.doctype.bo.DocumentType.to(documentTypeBo);
    }

    @Override
    public org.kuali.rice.kew.api.doctype.DocumentType getDocumentTypeByName(String documentTypeName) {
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType documentTypeBo = documentTypeDao.findByName(documentTypeName);
        return org.kuali.rice.kew.doctype.bo.DocumentType.to(documentTypeBo);
    }

    @Override
    public List<org.kuali.rice.kew.api.doctype.DocumentType> findAllDocumentTypes() {
        List<org.kuali.rice.kew.doctype.bo.DocumentType> documentTypeBos = documentTypeDao.findAllCurrent();
        List<org.kuali.rice.kew.api.doctype.DocumentType> currentDocTypes = new ArrayList<org.kuali.rice.kew.api.doctype.DocumentType>();
        for (org.kuali.rice.kew.doctype.bo.DocumentType dt : documentTypeBos) {
            currentDocTypes.add(org.kuali.rice.kew.doctype.bo.DocumentType.to(dt));        
        }
        return Collections.unmodifiableList(currentDocTypes);
    }

    @Override
    public boolean isSuperUserForDocumentTypeId(String principalId, String documentTypeId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Determining super user status [principalId=" + principalId + ", documentTypeId="
                    + documentTypeId + "]");
        }
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId was null or blank");
        }
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findById(documentTypeId);
        boolean isSuperUser = KEWServiceLocator.getDocumentTypePermissionService().canAdministerRouting(principalId,
                documentType);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Super user status is " + isSuperUser + ".");
        }
        return isSuperUser;

    }

    @Override
    public boolean isSuperUserForDocumentTypeName(String principalId, String documentTypeName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Determining super user status [principalId=" + principalId + ", documentTypeName="
                    + documentTypeName + "]");
        }
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId was null or blank");
        }
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        boolean isSuperUser = KEWServiceLocator.getDocumentTypePermissionService().canAdministerRouting(principalId,
                documentType);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Super user status is " + isSuperUser + ".");
        }
        return isSuperUser;

    }

    @Override
    public boolean canSuperUserApproveSingleActionRequest( String principalId, String documentTypeName, List<RouteNodeInstance> routeNodeInstances, String routeStatusCode ) {

        checkSuperUserInput( principalId, documentTypeName );

        org.kuali.rice.kew.doctype.bo.DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        List<org.kuali.rice.kew.engine.node.RouteNodeInstance> currentNodeInstances = null;
        if (routeNodeInstances != null && !routeNodeInstances.isEmpty()) {
            currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(routeNodeInstances.get(0).getDocumentId());
        }

        boolean isSuperUser = KEWServiceLocator.getDocumentTypePermissionService().canSuperUserApproveSingleActionRequest( principalId, documentType,
                currentNodeInstances, routeStatusCode);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Super user approve single action request status is " + isSuperUser + ".");
        }
        return isSuperUser;
    }

    @Override
    public boolean canSuperUserApproveDocument( String principalId, String documentTypeName, List<RouteNodeInstance> routeNodeInstances, String routeStatusCode ) {
        checkSuperUserInput( principalId, documentTypeName );

        org.kuali.rice.kew.doctype.bo.DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        List<org.kuali.rice.kew.engine.node.RouteNodeInstance> currentNodeInstances = null;
        if (routeNodeInstances != null && !routeNodeInstances.isEmpty()) {
            currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(routeNodeInstances.get(0).getDocumentId());
        }

        boolean isSuperUser = KEWServiceLocator.getDocumentTypePermissionService().canSuperUserApproveDocument(
                principalId, documentType, currentNodeInstances, routeStatusCode);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Super user approve document status is " + isSuperUser + ".");
        }
        return isSuperUser;
    }

    @Override
    public boolean canSuperUserDisapproveDocument( String principalId, String documentTypeName, List<RouteNodeInstance> routeNodeInstances, String routeStatusCode ) {
        checkSuperUserInput( principalId, documentTypeName );

        org.kuali.rice.kew.doctype.bo.DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);

        List<org.kuali.rice.kew.engine.node.RouteNodeInstance> currentNodeInstances = null;
        if (routeNodeInstances != null && !routeNodeInstances.isEmpty()) {
            currentNodeInstances = KEWServiceLocator.getRouteNodeService().getCurrentNodeInstances(routeNodeInstances.get(0).getDocumentId());
        }

        boolean isSuperUser = KEWServiceLocator.getDocumentTypePermissionService().canSuperUserDisapproveDocument( principalId, documentType,
                currentNodeInstances, routeStatusCode);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Super user disapprove document status is " + isSuperUser + ".");
        }
        return isSuperUser;
    }

    private void checkSuperUserInput( String principalId, String documentTypeName ) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Determining super user status [principalId=" + principalId + ", documentTypeName="
                    + documentTypeName + "]");
        }
        if (StringUtils.isBlank(principalId)) {
            throw new RiceIllegalArgumentException("principalId was null or blank");
        }
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
    }

    @Override
    public boolean hasRouteNodeForDocumentTypeName(String routeNodeName, String documentTypeName)
            throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(routeNodeName)) {
            throw new RiceIllegalArgumentException("routeNodeName was null or blank");
        }
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was null or blank");
        }

        DocumentType documentType = getDocumentTypeByName(documentTypeName);
        if (documentType == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document type for the given name: " + documentTypeName);
        }
        RouteNode routeNode = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(documentType.getId(), routeNodeName);

        if (routeNode == null) {
            if (documentType.getParentId() == null) {
                return false;
            } else {
                return hasRouteNodeForDocumentTypeId(routeNodeName, documentType.getParentId());
            }
        } else {
            return true;
        }
    }
    
    @Override
    public boolean hasRouteNodeForDocumentTypeId(String routeNodeName, String documentTypeId)
            throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(routeNodeName)) {
            throw new RiceIllegalArgumentException("routeNodeName was null or blank");
        }
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }

        DocumentType documentType = getDocumentTypeById(documentTypeId);
        if (documentType == null) {
            throw new RiceIllegalArgumentException("Failed to locate a document type for the given id: " + documentTypeId);
        }
        RouteNode routeNode = KEWServiceLocator.getRouteNodeService().findRouteNodeByName(documentType.getId(), routeNodeName);

        if (routeNode == null) {
            if (documentType.getParentId() == null) {
                return false;
            } else {
                return hasRouteNodeForDocumentTypeId(routeNodeName, documentType.getParentId());
            }
        } else {
            return true;
        }
    }
    
    @Override
    public boolean isActiveById(String documentTypeId) {
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType docType = KEWServiceLocator.getDocumentTypeService().findById(documentTypeId);
        return docType != null && docType.isActive();
    }

    @Override
    public boolean isActiveByName(String documentTypeName) {
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        return docType != null && docType.isActive();
    }
    
    @Override
    public RoutePath getRoutePathForDocumentTypeId(String documentTypeId) {
        if (StringUtils.isBlank(documentTypeId)) {
            throw new RiceIllegalArgumentException("documentTypeId was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType docType = KEWServiceLocator.getDocumentTypeService().findById(documentTypeId);
        if (docType == null) {
            return null;
        }
        RoutePath.Builder builder = RoutePath.Builder.create();
        List<ProcessDefinitionBo> processes = docType.getProcesses();
        for (ProcessDefinitionBo process : processes) {
            builder.getProcessDefinitions().add(ProcessDefinition.Builder.create(process));
        }
        return builder.build();
    }
    
    @Override
    public RoutePath getRoutePathForDocumentTypeName(String documentTypeName) {
        if (StringUtils.isBlank(documentTypeName)) {
            throw new RiceIllegalArgumentException("documentTypeName was null or blank");
        }
        org.kuali.rice.kew.doctype.bo.DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        if (docType == null) {
            return null;
        }
        RoutePath.Builder builder = RoutePath.Builder.create();
        List<ProcessDefinitionBo> processes = docType.getProcesses();
        for (ProcessDefinitionBo process : processes) {
            builder.getProcessDefinitions().add(ProcessDefinition.Builder.create(process));
        }
        return builder.build();

    }

    public void setDocumentTypeDao(DocumentTypeDAO documentTypeDao) {
        this.documentTypeDao = documentTypeDao;
    }

}
