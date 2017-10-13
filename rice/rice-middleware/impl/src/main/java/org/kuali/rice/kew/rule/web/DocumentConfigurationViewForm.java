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
package org.kuali.rice.kew.rule.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.rule.web.DocumentConfigurationViewAction.PermissionForDisplay;
import org.kuali.rice.kew.rule.web.DocumentConfigurationViewAction.ResponsibilityForDisplay;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kns.web.struts.form.KualiForm;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentConfigurationViewForm extends KualiForm {

	private static final long serialVersionUID = -6251534168125209176L;
	
	protected String documentTypeName;
	protected DocumentType documentType; 
	protected DocumentType parentDocumentType; 
	protected List<DocumentType> childDocumentTypes; 
//    protected List<KimPermissionInfo> permissions = null;
    protected List<String> docTypeHierarchyList = new ArrayList<String>();
    protected LinkedHashMap<String,List<PermissionForDisplay>> permissionsByDocumentType = new LinkedHashMap<String, List<PermissionForDisplay>>();
    protected Map<String,List<Role>> permissionRoles = new HashMap<String, List<Role>>();
    protected Map<String,String> attributeLabels;
    protected List<RouteNode> routeNodes;
    protected Map<String,List<ResponsibilityForDisplay>> responsibilityMap;
    protected Map<String,List<Role>> responsibilityRoles;
    protected List<ResponsibilityForDisplay> exceptionResponsibilities;
    protected boolean canInitiateDocumentTypeDocument = false;
    protected boolean canInitiatePermissionDocument = false;
    protected boolean canInitiateResponsibilityDocument = false;

	public Map<String, List<Role>> getPermissionRoles() {
		return this.permissionRoles;
	}

	public void setPermissionRoles(Map<String, List<Role>> permissionRoles) {
		this.permissionRoles = permissionRoles;
	}

	public String getDocumentTypeName() {
		return this.documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public DocumentType getDocumentType() {
		return this.documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public DocumentType getParentDocumentType() {
		return this.parentDocumentType;
	}

	public void setParentDocumentType(DocumentType parentDocumentType) {
		this.parentDocumentType = parentDocumentType;
	}

	public List<DocumentType> getChildDocumentTypes() {
		return this.childDocumentTypes;
	}

	public void setChildDocumentTypes(List<DocumentType> childDocumentTypes) {
		this.childDocumentTypes = childDocumentTypes;
	}

	public Map<String, String> getAttributeLabels() {
		return this.attributeLabels;
	}

	public void setAttributeLabels(Map<String, String> attributeLabels) {
		this.attributeLabels = attributeLabels;
	}

	public List<RouteNode> getRouteNodes() {
		return this.routeNodes;
	}

	public void setRouteNodes(List<RouteNode> routeNodes) {
		this.routeNodes = routeNodes;
	}

	public Map<String, List<ResponsibilityForDisplay>> getResponsibilityMap() {
		return this.responsibilityMap;
	}

	public void setResponsibilityMap(
			Map<String, List<ResponsibilityForDisplay>> responsibilityMap) {
		this.responsibilityMap = responsibilityMap;
	}

	public List<String> getDocTypeHierarchyList() {
		return this.docTypeHierarchyList;
	}

	public void setDocTypeHierarchyList(List<String> docTypeHierarchyList) {
		this.docTypeHierarchyList = docTypeHierarchyList;
	}

	public LinkedHashMap<String, List<PermissionForDisplay>> getPermissionsByDocumentType() {
		return this.permissionsByDocumentType;
	}

	public void setPermissionsByDocumentType(
			LinkedHashMap<String, List<PermissionForDisplay>> permissionsByDocumentType) {
		this.permissionsByDocumentType = permissionsByDocumentType;
	}

	public void addDocumentType( String documentTypeName ) {
		docTypeHierarchyList.add(documentTypeName);		
	}
	
	public void setPermissionsForDocumentType( String documentTypeName, List<PermissionForDisplay> perms ) {
		permissionsByDocumentType.put(documentTypeName, perms);
	}

	public boolean isCanInitiateDocumentTypeDocument() {
		return this.canInitiateDocumentTypeDocument;
	}

	public void setCanInitiateDocumentTypeDocument(
			boolean canInitiateDocumentTypeDocument) {
		this.canInitiateDocumentTypeDocument = canInitiateDocumentTypeDocument;
	}

	public Map<String, List<Role>> getResponsibilityRoles() {
		return this.responsibilityRoles;
	}

	public void setResponsibilityRoles(
			Map<String, List<Role>> responsibilityRoles) {
		this.responsibilityRoles = responsibilityRoles;
	}

	public boolean isCanInitiatePermissionDocument() {
		return this.canInitiatePermissionDocument;
	}

	public void setCanInitiatePermissionDocument(
			boolean canInitiatePermissionDocument) {
		this.canInitiatePermissionDocument = canInitiatePermissionDocument;
	}

	public boolean isCanInitiateResponsibilityDocument() {
		return this.canInitiateResponsibilityDocument;
	}

	public void setCanInitiateResponsibilityDocument(
			boolean canInitiateResponsibilityDocument) {
		this.canInitiateResponsibilityDocument = canInitiateResponsibilityDocument;
	}

	/**
	 * @return the exceptionResponsibilities
	 */
	public List<ResponsibilityForDisplay> getExceptionResponsibilities() {
		return this.exceptionResponsibilities;
	}

	/**
	 * @param exceptionResponsibilities the exceptionResponsibilities to set
	 */
	public void setExceptionResponsibilities(
			List<ResponsibilityForDisplay> exceptionResponsibilities) {
		this.exceptionResponsibilities = exceptionResponsibilities;
	}

}
