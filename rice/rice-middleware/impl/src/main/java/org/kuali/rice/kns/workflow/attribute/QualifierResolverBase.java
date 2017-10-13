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
package org.kuali.rice.kns.workflow.attribute;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.role.QualifierResolver;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;

import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class QualifierResolverBase implements QualifierResolver {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(QualifierResolverBase.class);

    protected static final String KIM_ATTRIBUTE_DOCUMENT_TYPE_NAME = KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME;
    protected static final String KIM_ATTRIBUTE_DOCUMENT_NUMBER = KimConstants.AttributeConstants.DOCUMENT_NUMBER;
    protected static final String KIM_ATTRIBUTE_ROUTE_LEVEL_NAME = KimConstants.AttributeConstants.ROUTE_NODE_NAME;

    private static DocumentService documentService;
    
    /**
     * Retrieves the document that the current route context is operating on
     * @param context the current route context
     * @return the document
     */
    protected Document getDocument(RouteContext context) {
        String documentID = getDocumentId(context);
        
        if (documentID != null) {
            try {
                return getDocumentService().getByDocumentHeaderIdSessionless(documentID);
            }
            catch (WorkflowException e) {
                LOG.error("Unable to retrieve document with system user.", e);
                return null;
            }
        }
        return null;
    }

    
    /**
     * Retrieves the id of the current document from the RouteContext
     * @param context the current route context
     * @return the id of the document
     */
    protected String getDocumentId(RouteContext context) {
        final String documentID = context.getNodeInstance().getDocumentId();
        return documentID != null ? documentID.toString() : null;
    }


	public DocumentService getDocumentService() {
		if ( documentService == null ) {
			documentService = KRADServiceLocatorWeb.getDocumentService();
		}
		return documentService;
	}
	
    /**
     * Add common qualifiers to every Map<String, String> in the given List of Map<String, String>
     * @param qualifiers a List of Map<String, String>s to add common qualifiers to
     * @param document the document currently being routed
     * @param documentEntry the data dictionary entry of the type of document currently being routed
     * @param routeLevel the document's current route level
     */
    protected void decorateWithCommonQualifiers(List<Map<String, String>> qualifiers, RouteContext context, String customDocTypeName) {
        for (Map<String, String> qualifier : qualifiers) {
            addCommonQualifiersToMap(qualifier, context, customDocTypeName);
        }
    }
    
    /**
     * Adds common qualifiers to a given Map<String, String>
     * @param qualifier an Map<String, String> to add common qualifiers to
     * @param document the document currently being routed
     * @param documentEntry the data dictionary entry of the type of document currently being routed
     * @param routeLevel the document's current route level
     */
    protected void addCommonQualifiersToMap(Map<String, String> qualifier, RouteContext context, String customDocTypeName) {
        qualifier.put(KIM_ATTRIBUTE_DOCUMENT_NUMBER, context.getDocument().getDocumentId() );
        if ( !qualifier.containsKey(KIM_ATTRIBUTE_DOCUMENT_TYPE_NAME) ) {
	        if ( StringUtils.isBlank(customDocTypeName)) {
	        	qualifier.put(KIM_ATTRIBUTE_DOCUMENT_TYPE_NAME, 
	        			context.getDocument().getDocumentType().getName() );
	        } else {
	        	qualifier.put(KIM_ATTRIBUTE_DOCUMENT_TYPE_NAME, customDocTypeName );        	
	        }
        }
        qualifier.put(KIM_ATTRIBUTE_ROUTE_LEVEL_NAME, context.getNodeInstance().getName());
    }
	
}
