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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.service.RouteNodeService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.permission.Permission;
import org.kuali.rice.kim.api.permission.PermissionService;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.responsibility.ResponsibilityService;
import org.kuali.rice.kim.api.role.Role;
import org.kuali.rice.kim.api.role.RoleService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.bo.impl.KimAttributes;
import org.kuali.rice.kim.impl.permission.PermissionBo;
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo;
import org.kuali.rice.kim.impl.responsibility.ResponsibilityBo;
import org.kuali.rice.kns.service.DocumentHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.service.MaintenanceDocumentDictionaryService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;


/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class DocumentConfigurationViewAction extends KewKualiAction {

	private static final Logger LOG = Logger.getLogger(DocumentConfigurationViewAction.class);
	
	private PermissionService permissionService;
	private RoleService roleService;
	private ResponsibilityService responsibilityService;
	private DocumentTypeService documentTypeService;
	private DataDictionaryService dataDictionaryService;
	private RouteNodeService routeNodeService;
	private MaintenanceDocumentDictionaryService maintenanceDocumentDictionaryService;
	private DocumentHelperService documentHelperService;
	
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	populateForm( (DocumentConfigurationViewForm)form );
        return mapping.findForward("basic");
    }
    
    protected void populateForm( DocumentConfigurationViewForm form ) {
    	if ( StringUtils.isNotEmpty( form.getDocumentTypeName() ) ) {
    		form.setDocumentType( getDocumentTypeService().findByName( form.getDocumentTypeName() ) ); 
        	if ( form.getDocumentType() != null ) {
	    		form.getDocumentType().getChildrenDocTypes();
	    		form.setAttributeLabels( new HashMap<String, String>() );
	    		populateRelatedDocuments( form );
	    		populatePermissions( form );
	    		populateRoutingResponsibilities( form );
	    		populateRoutingExceptionResponsibility( form );
	    		checkPermissions( form );
        	}
    	}
    }

    protected void checkPermissions( DocumentConfigurationViewForm form ) {
    	String docTypeDocumentType = getMaintenanceDocumentDictionaryService().getDocumentTypeName(DocumentType.class);
        try {
            if ((docTypeDocumentType != null) && getDocumentHelperService().getDocumentAuthorizer(docTypeDocumentType).canInitiate(docTypeDocumentType, GlobalVariables.getUserSession().getPerson())) {
                form.setCanInitiateDocumentTypeDocument( true );
            }
        } catch (Exception ex) {
			// just skip - and don't display links
        	LOG.error( "Unable to check DocumentType initiation permission for "+ docTypeDocumentType, ex );
		}
    	String permissionDocumentType = getMaintenanceDocumentDictionaryService().getDocumentTypeName(PermissionBo.class);
        try {
            if ((permissionDocumentType != null) && getDocumentHelperService().getDocumentAuthorizer(permissionDocumentType).canInitiate(permissionDocumentType, GlobalVariables.getUserSession().getPerson())) {
                form.setCanInitiatePermissionDocument( true );
            }
        } catch (Exception ex) {
			// just skip - and don't display links
        	LOG.error( "Unable to check Permission initiation permission for "+ permissionDocumentType, ex );
		}
    	String responsibilityDocumentType = getMaintenanceDocumentDictionaryService().getDocumentTypeName(ResponsibilityBo.class);
        try {
            if ((responsibilityDocumentType != null) && getDocumentHelperService().getDocumentAuthorizer(responsibilityDocumentType).canInitiate(responsibilityDocumentType, GlobalVariables.getUserSession().getPerson())) {
                form.setCanInitiateResponsibilityDocument( true );
            }
        } catch (Exception ex) {
			// just skip - and don't display links
        	LOG.error( "Unable to check Responsibility initiation permission for "+ responsibilityDocumentType, ex );
		}
    }
    
    @SuppressWarnings("unchecked")
	public void populateRelatedDocuments( DocumentConfigurationViewForm form ) {
    	form.setParentDocumentType( form.getDocumentType().getParentDocType() );
    	form.setChildDocumentTypes( new ArrayList<DocumentType>( form.getDocumentType().getChildrenDocTypes() ) );
    }
    
	public void populatePermissions( DocumentConfigurationViewForm form ) {
		
		DocumentType docType = form.getDocumentType();
		Map<String,List<Role>> permRoles = new HashMap<String, List<Role>>();
		// loop over the document hierarchy
		Set<String> seenDocumentPermissions = new HashSet<String>();
		while ( docType != null) {
			String documentTypeName = docType.getName();
            Predicate p = and(
                equal("active", "Y"),
                equal("attributes[" + KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME + "]", docType.getName()));
			List<Permission> perms = getPermissionService().findPermissions(QueryByCriteria.Builder.fromPredicates(p)).getResults();
			for ( Permission perm : perms ) {
                PermissionBo permBo = PermissionBo.from(perm);
				List<String> roleIds = getPermissionService().getRoleIdsForPermission(perm.getNamespaceCode(),
                        perm.getName());
                if (!roleIds.isEmpty()) {
				    permRoles.put( perm.getId(), getRoleService().getRoles(roleIds) );
                }
				for ( String attributeName : permBo.getDetails().keySet() ) {
					addAttributeLabel(form, attributeName);
				}
			}
			// show the section if the current document or permissions exist
			if ( perms.size() > 0 || documentTypeName.equals( form.getDocumentTypeName() ) ) {
				ArrayList<PermissionForDisplay> dispPerms = new ArrayList<PermissionForDisplay>( perms.size() );
				for ( Permission perm : perms ) {
                    PermissionBo permBo = PermissionBo.from(perm);
					if ( permBo.getDetails().size() == 1  ) { // only the document type
						// this is a document type-specific permission, check if seen earlier
						if ( seenDocumentPermissions.contains(perm.getTemplate().getNamespaceCode()+"|"+perm.getTemplate().getName()) ) {
							dispPerms.add( new PermissionForDisplay( permBo, true ) );
						} else {
							dispPerms.add( new PermissionForDisplay( permBo, false ) );
							seenDocumentPermissions.add(perm.getTemplate().getNamespaceCode()+"|"+perm.getTemplate().getName());
						}
					} else {
						// other attributes, can't determine whether this is overridden at another level
						dispPerms.add( new PermissionForDisplay( permBo, false ) );
					}
				}
				form.setPermissionsForDocumentType(documentTypeName, dispPerms );
				form.addDocumentType(documentTypeName);
			}
			docType = docType.getParentDocType();			
		}
		
		form.setPermissionRoles( permRoles );
	}
	
	protected void populateRoutingExceptionResponsibility( DocumentConfigurationViewForm form ) {	
		DocumentType docType = form.getDocumentType();
		List<ResponsibilityForDisplay> responsibilities = new ArrayList<ResponsibilityForDisplay>();
		while ( docType != null) {
            QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
            Predicate p = and(
                equal("template.namespaceCode", KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE),
                equal("template.name", KewApiConstants.EXCEPTION_ROUTING_RESPONSIBILITY_TEMPLATE_NAME),
                equal("active", "Y"),
                equal("attributes[documentTypeName]", docType.getName())
            );
            builder.setPredicates(p);
			List<Responsibility> resps = getResponsibilityService().findResponsibilities(builder.build()).getResults();
			
			for ( Responsibility r : resps ) {
				if ( responsibilities.isEmpty() ) {
					responsibilities.add( new ResponsibilityForDisplay( r, false ) );
				} else {
					responsibilities.add( new ResponsibilityForDisplay( r, true ) );
				}
			}
			docType = docType.getParentDocType();			
		}
		form.setExceptionResponsibilities( responsibilities );
		for ( ResponsibilityForDisplay responsibility : responsibilities ) {
			List<String> roleIds = getResponsibilityService().getRoleIdsForResponsibility(responsibility.getResp().getId());
            if (!roleIds.isEmpty()) {
			    form.getResponsibilityRoles().put( responsibility.getResponsibilityId(), getRoleService().getRoles(roleIds) );
            }
		}
	}

	protected void addAttributeLabel( DocumentConfigurationViewForm form, String attributeName ) {
		if ( !form.getAttributeLabels().containsKey(attributeName) ) {
			form.getAttributeLabels().put(attributeName, 
					getDataDictionaryService().getAttributeLabel(KimAttributes.class, attributeName) );
		}
	}
	
	// loop over nodes
	// if split node, push onto stack
		// note the number of children, this is the number of times the join node needs to be found
	// when join node found, return to last split on stack
		// move to next child of the split
	
	protected RouteNode flattenSplitNode( RouteNode splitNode, Map<String,RouteNode> nodes ) {
		nodes.put( splitNode.getRouteNodeName(), splitNode );
		RouteNode joinNode = null;
		
		for ( RouteNode nextNode : splitNode.getNextNodes() ) {
			joinNode = flattenRouteNodes(nextNode, nodes);
		}
		
		if ( joinNode != null ) {
			nodes.put( joinNode.getRouteNodeName(), joinNode );
		}
		return joinNode;
	}
	
	/**
	 * @param node
	 * @param nodes
	 * @return The last node processed by this method.
	 */
	protected RouteNode flattenRouteNodes( RouteNode node, Map<String,RouteNode> nodes ) {
		RouteNode lastProcessedNode = null;
        if (node != null) {
            // if we've seen the node before - skip, avoids infinite loop
            if ( nodes.containsKey(node.getRouteNodeName()) ) {
                return node;
            }
		
            if ( node.getNodeType().contains( "SplitNode" ) ) { // Hacky - but only way when the class may not be present in the KEW JVM
                lastProcessedNode = flattenSplitNode(node, nodes); // special handling to process all split children before continuing
                // now, process the join node's children
                if (lastProcessedNode != null) {
                    for ( RouteNode nextNode : lastProcessedNode.getNextNodes() ) {
                        lastProcessedNode = flattenRouteNodes(nextNode, nodes);
                    }
                }
            } else if ( node.getNodeType().contains( "JoinNode" ) ) {
                lastProcessedNode = node; // skip, handled by the split node
            } else {
                // normal node, add to list and process all children
                nodes.put(node.getRouteNodeName(), node);
                for ( RouteNode nextNode : node.getNextNodes() ) {
                    lastProcessedNode = flattenRouteNodes(nextNode, nodes);
                }
            }
        }
		return lastProcessedNode;
	}
	
	@SuppressWarnings("unchecked")
	public void populateRoutingResponsibilities( DocumentConfigurationViewForm form ) {
		// pull all the responsibilities
		// merge the data and attach to route levels
		// pull the route levels and store on form
		//List<RouteNode> routeNodes = getRouteNodeService().getFlattenedNodes(form.getDocumentType(), true);
        Map<String,List<Role>> respToRoleMap = new HashMap<String, List<Role>>();
        List<ProcessDefinitionBo> processes = (List<ProcessDefinitionBo>)form.getDocumentType().getProcesses();
        if (!(processes.isEmpty())) {
            RouteNode rootNode = processes.get(0).getInitialRouteNode();
            LinkedHashMap<String, RouteNode> routeNodeMap = new LinkedHashMap<String, RouteNode>();
            flattenRouteNodes(rootNode, routeNodeMap);
		
            form.setRouteNodes( new ArrayList<RouteNode>( routeNodeMap.values() ) );
            // pull all the responsibilities and store into a map for use by the JSP
		
            // FILTER TO THE "Review" template only
            // pull responsibility roles
            DocumentType docType = form.getDocumentType();
            Set<Responsibility> responsibilities = new HashSet<Responsibility>();
            Map<String,List<ResponsibilityForDisplay>> nodeToRespMap = new LinkedHashMap<String, List<ResponsibilityForDisplay>>();
            while ( docType != null) {
                QueryByCriteria.Builder builder = QueryByCriteria.Builder.create();
                Predicate p = and(
                        equal("template.namespaceCode", KRADConstants.KUALI_RICE_WORKFLOW_NAMESPACE),
                        equal("template.name", KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME),
                        equal("active", "Y"),
                        equal("attributes[documentTypeName]", docType.getName())
                );
                builder.setPredicates(p);
                List<Responsibility> resps = getResponsibilityService().findResponsibilities(builder.build()).getResults();
			
                for ( Responsibility r : resps ) {
                    String routeNodeName = r.getAttributes().get(KimConstants.AttributeConstants.ROUTE_NODE_NAME);
                    if ( StringUtils.isNotBlank(routeNodeName) ) {
                        if ( !nodeToRespMap.containsKey( routeNodeName ) ) {
                            nodeToRespMap.put(routeNodeName, new ArrayList<ResponsibilityForDisplay>() );
                            nodeToRespMap.get(routeNodeName).add( new ResponsibilityForDisplay( r, false ) );
                        } else {
                            // check if the responsibility in the existing list is for the current document
                            // if so, OK to add.  Otherwise, a lower level document has overridden this
                            // responsibility (since we are walking up the hierarchy
                            if ( nodeToRespMap.get(routeNodeName).get(0).getDetails().get( KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME ).equals(docType.getName() ) ) {
                                nodeToRespMap.get(routeNodeName).add( new ResponsibilityForDisplay( r, false ) );
                            } else { // doc type name did not match, mark as overridden
                                nodeToRespMap.get(routeNodeName).add( new ResponsibilityForDisplay( r, true ) );
                            }
                        }
                        responsibilities.add(r);
                    }
                }
                docType = docType.getParentDocType();			
            }
            form.setResponsibilityMap( nodeToRespMap );
		
		    for (Responsibility responsibility : responsibilities ) {
		        List<String> roleIds = getResponsibilityService().getRoleIdsForResponsibility(responsibility.getId());
                if (!roleIds.isEmpty()) {
		            respToRoleMap.put( responsibility.getId(), getRoleService().getRoles(roleIds) );
                }
		    }
        }
		form.setResponsibilityRoles( respToRoleMap );
	}

	@Override
	public ActionForward toggleTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Repopulating the form is necessary when toggling tab states on the server side.
		ActionForward actionForward = super.toggleTab(mapping, form, request, response);
		populateForm( (DocumentConfigurationViewForm)form );
		return actionForward;
	}

	/**
	 * Internal delegate class to wrap a responsibility and add an overridden flag.
	 */
	public static class ResponsibilityForDisplay {

		private Responsibility resp;
		private boolean overridden = false;
		
		public ResponsibilityForDisplay( Responsibility resp, boolean overridden ) {
			this.resp = resp;
			this.overridden = overridden;
		}
		
		/**
		 * @return the resp
		 */
		Responsibility getResp() {
			return this.resp;
		}
		
		public boolean isOverridden() {
			return this.overridden;
		}

		public void setOverridden(boolean overridden) {
			this.overridden = overridden;
		}

		public Map<String, String> getDetails() {
			return new HashMap<String, String>(this.resp.getAttributes());
		}

		public String getName() {
			return this.resp.getName();
		}

		public String getNamespaceCode() {
			return this.resp.getNamespaceCode();
		}

		public String getResponsibilityId() {
			return this.resp.getId();
		}
	}

	public static class PermissionForDisplay {
		private PermissionBo perm;
		private boolean overridden = false;
		
		public PermissionForDisplay( PermissionBo perm, boolean overridden ) {
			this.perm = perm;
			this.overridden = overridden;
		}
		public boolean isOverridden() {
			return this.overridden;
		}

		public void setOverridden(boolean overridden) {
			this.overridden = overridden;
		}
		public Map<String, String> getDetails() {
			return this.perm.getDetails();
		}
		public String getName() {
			return this.perm.getName();
		}
		public String getNamespaceCode() {
			return this.perm.getNamespaceCode();
		}
        public String getId() {
            return this.perm.getId();
        }
		public PermissionTemplateBo getTemplate() {
			return this.perm.getTemplate();
		}
		
	}
	
	/**
	 * @return the permissionService
	 */
	public PermissionService getPermissionService() {
		if ( permissionService == null ) {
			permissionService = KimApiServiceLocator.getPermissionService();
		}
		return permissionService;
	}

	/**
	 * @return the roleService
	 */
	public RoleService getRoleService() {
		if ( roleService == null ) {
			roleService = KimApiServiceLocator.getRoleService();
		}
		return roleService;
	}

	/**
	 * @return the responsibilityService
	 */
	public ResponsibilityService getResponsibilityService() {
		if ( responsibilityService == null ) {
			responsibilityService = KimApiServiceLocator.getResponsibilityService();
		}
		return responsibilityService;
	}

	/**
	 * @return the documentTypeService
	 */
	public DocumentTypeService getDocumentTypeService() {
		if ( documentTypeService == null ) {
			documentTypeService = KEWServiceLocator.getDocumentTypeService();
		}
		return documentTypeService;
	}

	public DataDictionaryService getDataDictionaryService() {
		if(dataDictionaryService == null){
			dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
		}
		return dataDictionaryService;
	}

	public RouteNodeService getRouteNodeService() {
		if ( routeNodeService == null ) {
			routeNodeService = KEWServiceLocator.getRouteNodeService();
		}
		return routeNodeService;
	}

	public DocumentHelperService getDocumentHelperService() {
		if(documentHelperService == null){
			documentHelperService = KNSServiceLocator.getDocumentHelperService();
		}
		return documentHelperService;
	}

	public MaintenanceDocumentDictionaryService getMaintenanceDocumentDictionaryService() {
		if(maintenanceDocumentDictionaryService == null){
			maintenanceDocumentDictionaryService = KNSServiceLocator.getMaintenanceDocumentDictionaryService();
		}
		return maintenanceDocumentDictionaryService;
	}
	
}
