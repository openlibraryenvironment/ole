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
package org.kuali.rice.kew.xml;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.core.api.util.xml.XmlJotter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.exception.InvalidParentDocTypeException;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatus;
import org.kuali.rice.kew.doctype.ApplicationDocumentStatusCategory;
import org.kuali.rice.kew.doctype.DocumentTypeAttributeBo;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.document.DocumentTypeMaintainable;
import org.kuali.rice.kew.engine.node.ActivationTypeEnum;
import org.kuali.rice.kew.engine.node.BranchPrototype;
import org.kuali.rice.kew.engine.node.NodeType;
import org.kuali.rice.kew.engine.node.ProcessDefinitionBo;
import org.kuali.rice.kew.engine.node.RoleNode;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeConfigParam;
import org.kuali.rice.kew.export.KewExportDataSet;
import org.kuali.rice.kew.role.RoleRouteModule;
import org.kuali.rice.kew.rule.FlexRM;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.xmlrouting.XPathHelper;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.util.MaintenanceUtils;
import org.kuali.rice.krad.exception.GroupNotFoundException;
import org.kuali.rice.krad.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.kuali.rice.core.api.impex.xml.XmlConstants.*;


/**
 * A parser for parsing an XML file into {@link DocumentType}s.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentTypeXmlParser {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentTypeXmlParser.class);

    private static final String NEXT_NODE_EXP = "./@nextNode";
    private static final String PARENT_NEXT_NODE_EXP = "../@nextNode";
    private static final String NEXT_DOC_STATUS_EXP = "./@nextAppDocStatus";
    /**
     * Default route node activation type to use if omitted
     */
    private static final String DEFAULT_ACTIVATION_TYPE = "S";

    public List docTypeRouteNodes;
    private Map nodesMap;
    private XPath xpath;
    private Group defaultExceptionWorkgroup;
    
    protected XPath getXPath() {
        if (this.xpath == null) {
            this.xpath = XPathHelper.newXPath();
        }
        return xpath;
    }
    
    public List<DocumentType> parseDocumentTypes(InputStream input) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, WorkflowException, GroupNotFoundException {
        Document routeDocument=XmlHelper.trimXml(input);
        Map<String, DocumentType> documentTypesByName = new HashMap();
        for (DocumentType type:  parseAllDocumentTypes(routeDocument)) {
            documentTypesByName.put(type.getName(), type);
        }
        return new ArrayList<DocumentType>(documentTypesByName.values());
    }

    /**
     * Parses all document types, both standard and routing.
     * 
     * @param routeDocument The DOM document to parse.
     * @return A list containing the desired document types.
     */
    private List<DocumentType> parseAllDocumentTypes(Document routeDocument) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, WorkflowException, GroupNotFoundException {
    	// A mapping from the names of uninitialized parent doc types to the child nodes that depend on the parent doc.
    	Map<String,List<DocTypeNode>> pendingChildDocs = new HashMap<String,List<DocTypeNode>>();
    	// A mapping from the names of uninitialized parent doc types to the names of the dependent children.
    	Map<String,List<String>> pendingChildNames = new HashMap<String,List<String>>();
    	// A stack containing Iterators over the various lists of unprocessed nodes; this allows for faster parent-child resolution
    	// without having to use recursion.
        List<Iterator<DocTypeNode>> docInitStack = new ArrayList<Iterator<DocTypeNode>>();
        // The first List of document types.
        List<DocTypeNode> initialList = new ArrayList<DocTypeNode>();
    	// The current size of the stack.
    	int stackLen = 0;
    	// The current Iterator instance.
    	Iterator<DocTypeNode> currentIter = null;
        // The current document type node.
        DocTypeNode currDocNode = null;

        List<DocumentType> docTypeBeans = new ArrayList<DocumentType>();
                
        // Acquire the "standard" and "routing" document types.
        NodeList initialNodes = null;
        xpath = XPathHelper.newXPath();
        initialNodes = (NodeList) getXPath().evaluate("/" + DATA_ELEMENT + "/" + DOCUMENT_TYPES + "/" + DOCUMENT_TYPE, routeDocument, XPathConstants.NODESET);
        // Take each NodeList's nodes and insert them into a List implementation.
        for (int j = 0; j < initialNodes.getLength(); j++) {
            Node documentTypeNode = initialNodes.item(j);
            boolean docIsStandard = true;
            try {
                String xpathModeExpression = "./@" + DOCUMENT_TYPE_OVERWRITE_MODE;
                if (XmlHelper.pathExists(xpath, xpathModeExpression, documentTypeNode)) {
                    String overwriteMode = (String) getXPath().evaluate(xpathModeExpression, documentTypeNode, XPathConstants.STRING);
                    docIsStandard = !StringUtils.equalsIgnoreCase("true", overwriteMode);
                }
            } catch (XPathExpressionException xpee) {
                LOG.error("Error trying to check for '" + DOCUMENT_TYPE_OVERWRITE_MODE + "' attribute on document type element", xpee);
                throw xpee;
            }
        	initialList.add(new DocTypeNode(documentTypeNode, docIsStandard));
        }

        // Setup the Iterator instance to start with.
        currentIter = initialList.iterator();
        
        // Keep looping until all Iterators are complete or an uncaught exception is thrown.
        while (stackLen >= 0) {
        	// Determine the action to take based on whether there are remaining nodes in the present iterator.
        	if (currentIter.hasNext()) {
        		// If the current iterator still has more nodes, process the next one.
        		String newParentName = null;
        		currDocNode = currentIter.next();
        		// Initialize the document, and catch any child initialization problems.
        		try {
        			// Take appropriate action based on whether the document is a standard one or a routing one.
        			DocumentType docType = parseDocumentType(!currDocNode.isStandard, currDocNode.docNode); 
        			// Insert into appropriate position in the final list, based on the doc type's location in the XML file's list.
      				docTypeBeans.add(docType);
            		// Store the document's name for reference.
            		newParentName = docType.getName();
        		}
        		catch (InvalidParentDocTypeException exc) {
        			// If the parent document has not been processed yet, then store the child document.
            		List<DocTypeNode> tempList = null;
            		List<String> tempStrList = null;
            		String parentName = exc.getParentName();
            		String childName = exc.getChildName();
            		if (parentName == null || childName == null) { // Make sure the parent & child documents' names are defined.
            			throw exc;
            		}
            		tempList = pendingChildDocs.get(parentName);
            		tempStrList = pendingChildNames.get(parentName);
            		if (tempList == null) { // Initialize a new child document list if necessary.
            			tempList = new ArrayList<DocTypeNode>();
            			tempStrList = new ArrayList<String>();
            			pendingChildDocs.put(parentName, tempList);
            			pendingChildNames.put(parentName, tempStrList);
            		}
        			tempList.add(currDocNode);
        			tempStrList.add(childName);
        		}
        		
            	// Check for any delayed child documents that are dependent on the current document.
        		List<DocTypeNode> childrenToProcess = pendingChildDocs.remove(newParentName);
        		pendingChildNames.remove(newParentName);
        		if (childrenToProcess != null) {
        			LOG.info("'" + newParentName + "' has children that were delayed; now processing them...");
        			// If there are any pending children, push the old Iterator onto the stack and process the new Iterator on the next
        			// iteration of the loop.
        			stackLen++;
        			docInitStack.add(currentIter);
        			currentIter = childrenToProcess.iterator();
        		}
        	}
        	else {
        		// If the current Iterator has reached its end, discard it and pop the next one (if any) from the stack.
        		stackLen--;
        		currentIter = ((stackLen >= 0) ? docInitStack.remove(stackLen) : null);
         	}
        }
        
        // Throw an error if there are still any uninitialized child documents.
        if (pendingChildDocs.size() > 0) {
        	StringBuilder errMsg = new StringBuilder("Invalid parent document types: ");
        	// Construct the error message.
        	for (Iterator<String> unknownParents = pendingChildNames.keySet().iterator(); unknownParents.hasNext();) {
        		String currParent = unknownParents.next();
        		errMsg.append("Invalid parent doc type '").append(currParent).append("' is needed by child doc types ");
        		for (Iterator<String> failedChildren = pendingChildNames.get(currParent).iterator(); failedChildren.hasNext();) {
        			String currChild = failedChildren.next();
        			errMsg.append('\'').append(currChild).append((failedChildren.hasNext()) ? "', " : "'; ");
        		}
        	}
        	// Throw the exception.
        	throw new InvalidParentDocTypeException(null, null, errMsg.toString());
        }
        
    	return docTypeBeans;
    }

    private DocumentType parseDocumentType(boolean isOverwrite, Node documentTypeNode) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, WorkflowException, GroupNotFoundException {
        DocumentType documentType = getFullDocumentType(isOverwrite, documentTypeNode);
//        parseStructure(isOverwrite, documentTypeNode, documentType, new RoutePathContext());
        // reset variables
        docTypeRouteNodes = null;
        nodesMap = null;
        xpath = null;
        defaultExceptionWorkgroup = null;

        LOG.debug("Saving document type " + documentType.getName());
        routeDocumentType(documentType);
        return documentType;
    }

    private DocumentType getFullDocumentType(boolean isOverwrite, Node documentTypeNode) throws XPathExpressionException, GroupNotFoundException, XmlException, WorkflowException, SAXException, IOException, ParserConfigurationException {
        DocumentType documentType = getDocumentType(isOverwrite, documentTypeNode);
        /*
         * The following code does not need to apply the isOverwrite mode logic because it already checks to see if each node
         * is available on the ingested XML. If the node is ingested then it doesn't matter if we're in overwrite mode or not
         * the ingested code should save.
         */
        NodeList policiesList = (NodeList) getXPath().evaluate("./" + POLICIES, documentTypeNode, XPathConstants.NODESET);
        if (policiesList.getLength() > 1) {
            // more than one <policies> tag is invalid
            throw new XmlException("More than one " + POLICIES + " node is present in a document type node");
        }
        else if (policiesList.getLength() > 0) {
            // if there is exactly one <policies> tag then parse it and use the values
            NodeList policyNodes = (NodeList) getXPath().evaluate("./" + POLICY, policiesList.item(0), XPathConstants.NODESET);
            documentType.setDocumentTypePolicies(getDocumentTypePolicies(policyNodes, documentType));
        }

        NodeList attributeList = (NodeList) getXPath().evaluate("./attributes", documentTypeNode, XPathConstants.NODESET);
        if (attributeList.getLength() > 1) {
            throw new XmlException("More than one attributes node is present in a document type node");
        }
        else if (attributeList.getLength() > 0) {
            NodeList attributeNodes = (NodeList) getXPath().evaluate("./attribute", attributeList.item(0), XPathConstants.NODESET);
            documentType.setDocumentTypeAttributes(getDocumentTypeAttributes(attributeNodes, documentType));
        }

        NodeList securityList = (NodeList) getXPath().evaluate("./" + SECURITY, documentTypeNode, XPathConstants.NODESET);
        if (securityList.getLength() > 1) {
            throw new XmlException("More than one " + SECURITY + " node is present in a document type node");
        }
        else if (securityList.getLength() > 0) {
           try {
             Node securityNode = securityList.item(0);
             String securityText = XmlJotter.jotNode(securityNode);
             documentType.setDocumentTypeSecurityXml(securityText);
           }
           catch (Exception e) {
             throw new XmlException(e);
           }
        }
        parseStructure(isOverwrite, documentTypeNode, documentType, new RoutePathContext());
        return documentType;
    }

    private void parseStructure(boolean isOverwrite, Node documentTypeNode, DocumentType documentType, RoutePathContext context) throws XPathExpressionException, XmlException, GroupNotFoundException {
        // TODO have a validation function that takes an xpath statement and blows chunks if that
        // statement returns false
        boolean hasRoutePathsElement = false;
        try {
            hasRoutePathsElement = XmlHelper.pathExists(xpath, "./" + ROUTE_PATHS, documentTypeNode);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + ROUTE_PATHS, xpee);
            throw xpee;
        }
        boolean hasRouteNodesElement = false;
        try {
            hasRouteNodesElement = XmlHelper.pathExists(xpath, "./" + ROUTE_NODES, documentTypeNode);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + ROUTE_NODES, xpee);
            throw xpee;
        }

        // check to see if we're in overwrite mode
        if (isOverwrite) {
            // since we're in overwrite mode, if we don't have a routeNodes element or a routePaths element we simply return
            if (!hasRouteNodesElement && !hasRoutePathsElement) {
                return;
            }
            // if we have route nodes and route paths we're going to overwrite all existing processes so we can clear the current list
            else if (hasRouteNodesElement && hasRoutePathsElement) {
                documentType.setProcesses(new ArrayList());
            }
            // check to see if we have one but not the other element of routePaths and routeNodes
            else if (!hasRouteNodesElement || !hasRoutePathsElement) {
                // throw an exception since an ingestion can only have neither or both of the routePaths and routeNodes elements
                throw new XmlException("A overwriting document type ingestion can not have only one of the " + ROUTE_PATHS + " and " + ROUTE_NODES + " elements.  Either both or neither should be defined.");
            }
        }

        NodeList processNodes;

        try {
            if (XmlHelper.pathExists(xpath, "./" + ROUTE_PATHS + "/" + ROUTE_PATH, documentTypeNode)) {
                processNodes = (NodeList) getXPath().evaluate("./" + ROUTE_PATHS + "/" + ROUTE_PATH, documentTypeNode, XPathConstants.NODESET);
            } else {
                if (hasRoutePathsElement) {
                    createEmptyProcess(documentType);
                }
                return;
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type routePaths", xpee);
            throw xpee;
        }

        createProcesses(processNodes, documentType);

        NodeList nodeList = null;
        try {
            nodeList = (NodeList) getXPath().evaluate("./" + ROUTE_PATHS + "/" + ROUTE_PATH + "/start", documentTypeNode, XPathConstants.NODESET);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type routePath start", xpee);
            throw xpee;
        }
        if (nodeList.getLength() > 1) {
            throw new XmlException("More than one start node is present in route path");
        } else if (nodeList.getLength() == 0) {
            throw new XmlException("No start node is present in route path");
        }
        try {
            nodeList = (NodeList) getXPath().evaluate(".//" + ROUTE_NODES, documentTypeNode, XPathConstants.NODESET);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type routeNodes", xpee);
            throw xpee;
        }
        if (nodeList.getLength() > 1) {
            throw new XmlException("More than one routeNodes node is present in documentType node");
        } else if (nodeList.getLength() == 0) {
            throw new XmlException("No routeNodes node is present in documentType node");
        }
        Node routeNodesNode = nodeList.item(0);
        checkForOrphanedRouteNodes(documentTypeNode, routeNodesNode);

        // passed validation.
        nodesMap = new HashMap();
        for (int index = 0; index < processNodes.getLength(); index++) {
            Node processNode = processNodes.item(index);
            String startName;
            try {
                startName = (String) getXPath().evaluate("./start/@name", processNode, XPathConstants.STRING);
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining routePath start name attribute", xpee);
                throw xpee;
            }
            String processName = KewApiConstants.PRIMARY_PROCESS_NAME;
            if (org.apache.commons.lang.StringUtils.isEmpty(startName)) {
                try {
                    startName = (String) getXPath().evaluate("./@" + INITIAL_NODE, processNode, XPathConstants.STRING);
                } catch (XPathExpressionException xpee) {
                    LOG.error("Error obtaining routePath initialNode attribute", xpee);
                    throw xpee;
                }
                try {
                    processName = (String) getXPath().evaluate("./@" + PROCESS_NAME, processNode, XPathConstants.STRING);
                } catch (XPathExpressionException xpee) {
                    LOG.error("Error obtaining routePath processName attribute", xpee);
                    throw xpee;
                }
                if (org.apache.commons.lang.StringUtils.isEmpty(startName)) {
                    throw new XmlException("Invalid routePath: no initialNode attribute defined!");
                }
            }
            RouteNode routeNode = createRouteNode(null, startName, processNode, routeNodesNode, documentType, context);
            if (routeNode != null) {
                ProcessDefinitionBo process = documentType.getNamedProcess(processName);
                process.setInitialRouteNode(routeNode);
            }
        }

    }

    private DocumentType getDocumentType(boolean isOverwrite, Node documentTypeNode) throws XPathExpressionException, GroupNotFoundException, XmlException, WorkflowException, SAXException, IOException, ParserConfigurationException {
        DocumentType documentType = null;
        String documentTypeName = getDocumentTypeNameFromNode(documentTypeNode);
        DocumentType previousDocumentType = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        if (isOverwrite) {
            // we don't need the isOverwrite value to be passed here because we're only temporarily creating this document type in memory
            documentType = generateNewDocumentTypeFromExisting(documentTypeName);
            // export the document type that exists in the database
        }
        // if we don't have a valid value for documentType create a brand new instance
        if (ObjectUtils.isNull(documentType)) {
            documentType = new DocumentType();
        }
        documentType.setName(documentTypeName);

        // set the description on the document type
        // the description is always taken from the previous document type unless specified in the ingested file
        String description = null;
        try {
            description = (String) getXPath().evaluate("./" + DESCRIPTION, documentTypeNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type description", xpee);
            throw xpee;
        }
        // if the ingestion has produced a valid value then set it on the document
        if (StringUtils.isNotBlank(description)) {
            documentType.setDescription(description);
        }
        // at this point we know the ingested value is blank
        else if (!isOverwrite) {
            // if this is not an overwrite we need to check the previous document type version for a value to pull forward
            if ( (ObjectUtils.isNotNull(previousDocumentType)) && (StringUtils.isNotBlank(previousDocumentType.getDescription())) ) {
                // keep the same value from the previous version of the document type from the database
                description = previousDocumentType.getDescription();
            }
            documentType.setDescription(description);
        }

        // set the label on the document type
        String label = null;
        try {
            label = (String) getXPath().evaluate("./" + LABEL, documentTypeNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type label", xpee);
            throw xpee;
        }
        // if the ingestion has produced a valid value then set it on the document
        if (StringUtils.isNotBlank(label)) {
            documentType.setLabel(label);
        }
        // at this point we know the ingested value is blank
        else if (!isOverwrite) {
            // if this is not an overwrite we need to check the previous document type version for a value to pull forward
            if (ObjectUtils.isNotNull(previousDocumentType) && StringUtils.isNotBlank(previousDocumentType.getLabel())) {
                // keep the same value from the previous version of the document type from the database
                label = previousDocumentType.getLabel();
            } else {
                // otherwise set it to undefined
                label = KewApiConstants.DEFAULT_DOCUMENT_TYPE_LABEL;
            }
            documentType.setLabel(label);
        }

        // set the post processor class on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given, even if it's empty 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
            if (XmlHelper.pathExists(xpath, "./" + POST_PROCESSOR_NAME, documentTypeNode)) {
                String postProcessor = (String) getXPath().evaluate("./" + POST_PROCESSOR_NAME, documentTypeNode, XPathConstants.STRING);
                if (StringUtils.isEmpty(postProcessor)) {
                    documentType.setPostProcessorName(KewApiConstants.POST_PROCESSOR_NON_DEFINED_VALUE);
                } else {
                    documentType.setPostProcessorName((String) getXPath().evaluate("./" + POST_PROCESSOR_NAME, documentTypeNode, XPathConstants.STRING));
                }    
            }
                
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type postProcessorName", xpee);
            throw xpee;
        }

        // set the DocumentTypeAuthorizer
        try {
            if (XmlHelper.pathExists(xpath, "./" + AUTHORIZER, documentTypeNode)) {
                String authorizer = (String) getXPath().evaluate("./" + AUTHORIZER, documentTypeNode, XPathConstants.STRING);
                if (StringUtils.isNotBlank(authorizer)) {
                    documentType.setAuthorizer(authorizer);
                }
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type authorizer", xpee);
            throw xpee;
        }

        // set the document handler URL on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given, even if it's empty 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
            if (XmlHelper.pathExists(xpath, "./" + DOC_HANDLER, documentTypeNode)) {
                documentType.setUnresolvedDocHandlerUrl((String) getXPath().evaluate("./" + DOC_HANDLER, documentTypeNode, XPathConstants.STRING));
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type docHandler", xpee);
            throw xpee;
        }

        // set the help definition URL on the document type
        String helpDefUrl = null;
        try {
            helpDefUrl = (String) getXPath().evaluate("./" + HELP_DEFINITION_URL, documentTypeNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type help definition url", xpee);
            throw xpee;
        }
        // if the ingestion has produced a valid value then set it on the document
        if (StringUtils.isNotBlank(helpDefUrl)) {
            documentType.setUnresolvedHelpDefinitionUrl(helpDefUrl);
        }
        // at this point we know the ingested value is blank
        else if (!isOverwrite) {
            // if this is not an overwrite, we need to check the previous document type version for a value to pull forward
            if ( (ObjectUtils.isNotNull(previousDocumentType)) && (StringUtils.isNotBlank(previousDocumentType.getUnresolvedHelpDefinitionUrl())) ) {
                // keep the same value from the previous version of the document type from the database
                helpDefUrl = previousDocumentType.getUnresolvedHelpDefinitionUrl();
            }
            documentType.setUnresolvedHelpDefinitionUrl(helpDefUrl);
        }
        
        // set the doc search help URL on the document type
        String docSearchHelpUrl = null;
        try {
            docSearchHelpUrl = (String) getXPath().evaluate("./" + DOC_SEARCH_HELP_URL, documentTypeNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type document search help url", xpee);
            throw xpee;
        }
        // if the ingestion has produced a valid value then set it on the document
        if (StringUtils.isNotBlank(docSearchHelpUrl)) {
            documentType.setUnresolvedDocSearchHelpUrl(docSearchHelpUrl);
        }
        // at this point we know the ingested value is blank
        else if (!isOverwrite) {
            // if this is not an overwrite, we need to check the previous document type version for a value to pull forward
            if ( (ObjectUtils.isNotNull(previousDocumentType)) && (StringUtils.isNotBlank(previousDocumentType.getUnresolvedDocSearchHelpUrl())) ) {
                // keep the same value from the previous version of the document type from the database
                docSearchHelpUrl = previousDocumentType.getUnresolvedDocSearchHelpUrl();
            }
            documentType.setUnresolvedDocSearchHelpUrl(docSearchHelpUrl);
        }

        // set the application id on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given, even if it's empty 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
        	if (XmlHelper.pathExists(xpath, "./" + APPLICATION_ID, documentTypeNode)) {
        		documentType.setActualApplicationId((String) getXPath().evaluate("./" + APPLICATION_ID, documentTypeNode, XPathConstants.STRING));
        	} else if (XmlHelper.pathExists(xpath, "./" + SERVICE_NAMESPACE, documentTypeNode)) {
                documentType.setActualApplicationId((String) getXPath().evaluate("./" + SERVICE_NAMESPACE, documentTypeNode, XPathConstants.STRING));
                LOG.warn(SERVICE_NAMESPACE + " element was set on document type XML but is deprecated and will be removed in a future version, please use " + APPLICATION_ID + " instead.");
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type applicationId", xpee);
            throw xpee;
        }

        // set the notification from address on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given, even if it's empty 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
            if (XmlHelper.pathExists(xpath, "./" + NOTIFICATION_FROM_ADDRESS, documentTypeNode)) {
                documentType.setActualNotificationFromAddress((String) getXPath().evaluate("./" + NOTIFICATION_FROM_ADDRESS, documentTypeNode, XPathConstants.STRING));
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + NOTIFICATION_FROM_ADDRESS, xpee);
            throw xpee;
        }

        try {
            /*
             * - if the element tag is ingested... take whatever value is given, even if it's empty 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
            if (XmlHelper.pathExists(xpath, "./" + CUSTOM_EMAIL_STYLESHEET, documentTypeNode)) {
                documentType.setCustomEmailStylesheet((String) getXPath().evaluate("./" + CUSTOM_EMAIL_STYLESHEET, documentTypeNode, XPathConstants.STRING));
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + CUSTOM_EMAIL_STYLESHEET, xpee);
            throw xpee;
        }

        // any ingested document type by default becomes the current document type
        documentType.setCurrentInd(Boolean.TRUE);

        // set up the default exception workgroup for the document type
        String exceptionWg = null;
        String exceptionWgName = null;
        String exceptionWgNamespace = null;
        try {
        	if (XmlHelper.pathExists(xpath, "./" + DEFAULT_EXCEPTION_GROUP_NAME, documentTypeNode)) {
        		exceptionWgName = (String) getXPath().evaluate("./" + DEFAULT_EXCEPTION_GROUP_NAME, documentTypeNode, XPathConstants.STRING);
        		exceptionWgNamespace = (String) getXPath().evaluate("./" + DEFAULT_EXCEPTION_GROUP_NAME + "/@" + NAMESPACE, documentTypeNode, XPathConstants.STRING);
        		exceptionWg = exceptionWgName;
        	} else {
        		exceptionWg = (String) getXPath().evaluate("./" + DEFAULT_EXCEPTION_WORKGROUP_NAME, documentTypeNode, XPathConstants.STRING);
        	}
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + DEFAULT_EXCEPTION_GROUP_NAME, xpee);
            throw xpee;
        }
        // we don't need to take the isOverwrite into account here because this ingestion method is a shortcut to use the same workgroup in all route nodes
        if (StringUtils.isNotBlank(exceptionWg)) {
        	if (StringUtils.isNotBlank(exceptionWgName)) { // Found a "defaultExceptionGroupName" element.
        		// allow core config parameter replacement in documenttype workgroups
        		exceptionWgName = Utilities.substituteConfigParameters(exceptionWgName).trim();
        		exceptionWgNamespace = Utilities.substituteConfigParameters(exceptionWgNamespace).trim();
        	} else { // Found a deprecated "defaultExceptionWorkgroupName" element.
        		LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(DEFAULT_EXCEPTION_WORKGROUP_NAME).append(
        				"', please use '").append(DEFAULT_EXCEPTION_GROUP_NAME).append("' instead.").toString());
            	// allow core config parameter replacement in documenttype workgroups
            	exceptionWg = Utilities.substituteConfigParameters(exceptionWg);
            	exceptionWgName = Utilities.parseGroupName(exceptionWg);
            	exceptionWgNamespace = Utilities.parseGroupNamespaceCode(exceptionWg);
        	}
            Group exceptionGroup = getGroupService().getGroupByNamespaceCodeAndName(exceptionWgNamespace,
                    exceptionWgName);
            if(exceptionGroup == null) {
               	throw new WorkflowRuntimeException("Exception workgroup name " + exceptionWgName + " does not exist");
            }
            documentType.setDefaultExceptionWorkgroup(exceptionGroup);
            defaultExceptionWorkgroup = exceptionGroup;
        }

        // set up the active indicator on the document type
        try {
             // if the element tag is ingested... take whatever value is given
            if (XmlHelper.pathExists(xpath, "./" + ACTIVE, documentTypeNode)) {
                documentType.setActive(Boolean.valueOf((String) getXPath().evaluate("./" + ACTIVE, documentTypeNode, XPathConstants.STRING)));
            } 
            // if isOverwrite is false set the default value
            else if (!isOverwrite) {
                documentType.setActive(Boolean.TRUE);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type active flag", xpee);
            throw xpee;
        }

        // check for a parent document type for the ingested document type
        boolean parentElementExists = false;
        try {
            parentElementExists = XmlHelper.pathExists(xpath, "./" + PARENT, documentTypeNode);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type parent", xpee);
            throw xpee;
        }
        /*
         * - if the element tag is ingested... take whatever value is given 
         * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
         * then the documentType should already carry the value from the previous document type
         */
        if (parentElementExists) {
            // the tag was ingested so we'll use whatever the user attempted to set
            String parentDocumentTypeName = null;
            try {
                parentDocumentTypeName = (String) getXPath().evaluate("./" + PARENT, documentTypeNode, XPathConstants.STRING);
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining document type parent", xpee);
                throw xpee;
            }
            DocumentType parentDocumentType = KEWServiceLocator.getDocumentTypeService().findByName(parentDocumentTypeName);
            if (parentDocumentType == null) {
                //throw new XmlException("Invalid parent document type: '" + parentDocumentTypeName + "'");
                LOG.info("Parent document type '" + parentDocumentTypeName +
                        "' could not be found; attempting to delay processing of '" + documentTypeName + "'...");
                throw new InvalidParentDocTypeException(parentDocumentTypeName, documentTypeName,
                        "Invalid parent document type: '" + parentDocumentTypeName + "'");
            }
            documentType.setDocTypeParentId(parentDocumentType.getDocumentTypeId());
        }

        // set the super user workgroup name on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
        	if (XmlHelper.pathExists(xpath, "./" + SUPER_USER_GROUP_NAME, documentTypeNode)) {
        		documentType.setSuperUserWorkgroupNoInheritence(retrieveValidKimGroup("./" + SUPER_USER_GROUP_NAME, documentTypeNode, false));
        	}
        	else if (XmlHelper.pathExists(xpath, "./" + SUPER_USER_WORKGROUP_NAME, documentTypeNode)) {
        		LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(SUPER_USER_WORKGROUP_NAME).append(
					"', please use '").append(SUPER_USER_GROUP_NAME).append("' instead.").toString());
                documentType.setSuperUserWorkgroupNoInheritence(retrieveValidKimGroup("./" + SUPER_USER_WORKGROUP_NAME, documentTypeNode, true));
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + SUPER_USER_GROUP_NAME, xpee);
            throw xpee;
        }

        // set the blanket approve workgroup name on the document type
        String blanketWorkGroup = null;
        String blanketGroupName = null;
        String blanketNamespace = null;
        String blanketApprovePolicy = null;
        try {
            // check if the blanket approve workgroup name element tag was set on the ingested document type and get value if it was
        	if (XmlHelper.pathExists(xpath, "./" + BLANKET_APPROVE_GROUP_NAME, documentTypeNode)) {
        		blanketGroupName = (String) getXPath().evaluate("./" + BLANKET_APPROVE_GROUP_NAME, documentTypeNode, XPathConstants.STRING);
        		blanketNamespace = (String) getXPath().evaluate("./" + BLANKET_APPROVE_GROUP_NAME + "/@" + NAMESPACE, documentTypeNode, XPathConstants.STRING);
        		blanketWorkGroup = blanketGroupName;
        	}
        	else if (XmlHelper.pathExists(xpath, "./" + BLANKET_APPROVE_WORKGROUP_NAME, documentTypeNode)) {
                blanketWorkGroup = (String) getXPath().evaluate("./" + BLANKET_APPROVE_WORKGROUP_NAME, documentTypeNode, XPathConstants.STRING);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + BLANKET_APPROVE_GROUP_NAME, xpee);
            throw xpee;
        }
        try {
            // check if the blanket approve policy element tag was set on the ingested document type and get value if it was
            if (XmlHelper.pathExists(xpath, "./" + BLANKET_APPROVE_POLICY, documentTypeNode)) {
                blanketApprovePolicy =(String) getXPath().evaluate("./" + BLANKET_APPROVE_POLICY, documentTypeNode, XPathConstants.STRING);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + BLANKET_APPROVE_POLICY, xpee);
            throw xpee;
        }
        // first check to see if the user ingested both a workgroup name and a policy
        if (StringUtils.isNotBlank(blanketWorkGroup) && StringUtils.isNotBlank(blanketApprovePolicy)) {
            throw new XmlException("Only one of the blanket approve xml tags can be set");
        }
        else if (StringUtils.isNotBlank(blanketWorkGroup)) {
            if (isOverwrite) {
                // if overwrite mode is on we need to make sure we clear out the blanket approve policy in case that was the previous document type's method
                documentType.setBlanketApprovePolicy(null);
            }
            if (StringUtils.isNotBlank(blanketGroupName)) { // Found a "blanketApproveGroupName" element.
            	documentType.setBlanketApproveWorkgroup(retrieveValidKimGroupUsingGroupNameAndNamespace(blanketGroupName, blanketNamespace));
            } else { // Found a deprecated "blanketApproveWorkgroupName" element.
            	LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(BLANKET_APPROVE_WORKGROUP_NAME).append(
					"', please use '").append(BLANKET_APPROVE_GROUP_NAME).append("' instead.").toString());
            	documentType.setBlanketApproveWorkgroup(retrieveValidKimGroupUsingUnparsedGroupName(blanketWorkGroup));
            }
        }
        else if (StringUtils.isNotBlank(blanketApprovePolicy)) {
            if (isOverwrite) {
                // if overwrite mode is on we need to make sure we clear out the blanket approve workgroup in case that was the previous document type's method
                documentType.setBlanketApproveWorkgroup(null);
            }
            documentType.setBlanketApprovePolicy(blanketApprovePolicy);
        }

        // set the reporting workgroup name on the document type
        try {
        	if (XmlHelper.pathExists(xpath, "./" + REPORTING_GROUP_NAME, documentTypeNode)) {
        		documentType.setReportingWorkgroup(retrieveValidKimGroup("./" + REPORTING_GROUP_NAME, documentTypeNode, false));
        	}
        	else if (XmlHelper.pathExists(xpath, "./" + REPORTING_WORKGROUP_NAME, documentTypeNode)) {
        		LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(REPORTING_WORKGROUP_NAME).append(
					"', please use '").append(REPORTING_GROUP_NAME).append("' instead.").toString());
                documentType.setReportingWorkgroup(retrieveValidKimGroup("./" + REPORTING_WORKGROUP_NAME, documentTypeNode, true));
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type " + REPORTING_GROUP_NAME, xpee);
            throw xpee;
        }

        // set the routing version on the document type
        try {
            /*
             * - if the element tag is ingested... take whatever value is given 
             * - we disregard the isOverwrite because if the element tag does not exist in the ingestion
             * then the documentType should already carry the value from the previous document type
             */
            if (XmlHelper.pathExists(xpath, "./" + ROUTING_VERSION, documentTypeNode)) {
                String version;
                try {
                    version = (String) getXPath().evaluate("./" + ROUTING_VERSION, documentTypeNode, XPathConstants.STRING);
                } catch (XPathExpressionException xpee) {
                    LOG.error("Error obtaining document type routingVersion", xpee);
                    throw xpee;
                }
                // verify that the routing version is one of the two valid values
                if (!(version.equals(KewApiConstants.ROUTING_VERSION_ROUTE_LEVEL) || version.equals(KewApiConstants.ROUTING_VERSION_NODAL))) {
                    throw new WorkflowRuntimeException("Invalid routing version on document type: " + version);
                }
                documentType.setRoutingVersion(version);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type routingVersion", xpee);
            throw xpee;
        }

        // KULRICE-7786: Document Specific Doc Search Application Document Status should be available (and groupable)
        // on the basic version of search.
        //
        // Side-effect: populates the application statuses and categories on the documentType
        ApplicationDocumentStatusParser.parseValidApplicationStatuses(documentType, documentTypeNode, getXPath());

        return documentType;
    }

    private Group retrieveValidKimGroup(String xpathExpression, Node documentTypeNode, boolean deprecatedGroupElement) throws XPathExpressionException, GroupNotFoundException {
        String groupName;
        String groupNamespace = null;
        try {
            groupName = (String) getXPath().evaluate(xpathExpression, documentTypeNode, XPathConstants.STRING);
            // If not using the deprecated "namespaceCode:GroupName" format for group names, obtain the namespace from the element's "namespace" attribute.
            if (!deprecatedGroupElement) {
            	groupNamespace = (String) getXPath().evaluate(xpathExpression + "/@" + NAMESPACE, documentTypeNode, XPathConstants.STRING);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type workgroup using xpath expression: " + xpathExpression, xpee);
            throw xpee;
        }
        // Use the appropriate method to retrieve the group, based on whether or not the deprecated "namespaceCode:groupName" naming pattern is in use. 
        return (deprecatedGroupElement) ?
        		retrieveValidKimGroupUsingUnparsedGroupName(groupName) : retrieveValidKimGroupUsingGroupNameAndNamespace(groupName, groupNamespace);
    }

    private Group retrieveValidKimGroupUsingGroupNameAndNamespace(String groupName, String groupNamespace) throws GroupNotFoundException {
    	// allow core config parameter replacement in documenttype workgroups
    	groupName = Utilities.substituteConfigParameters(groupName).trim();
    	groupNamespace = Utilities.substituteConfigParameters(groupNamespace).trim();
    	return retrieveValidKimGroupUsingProcessedGroupNameAndNamespace(groupName, groupNamespace);
    }
    
    private Group retrieveValidKimGroupUsingUnparsedGroupName(String unparsedGroupName) throws GroupNotFoundException {
        // allow core config parameter replacement in documenttype workgroups
        unparsedGroupName = Utilities.substituteConfigParameters(unparsedGroupName);
        String groupName = Utilities.parseGroupName(unparsedGroupName);
        String groupNamespace = Utilities.parseGroupNamespaceCode(unparsedGroupName);
        return retrieveValidKimGroupUsingProcessedGroupNameAndNamespace(groupName, groupNamespace);
    }

    private Group retrieveValidKimGroupUsingProcessedGroupNameAndNamespace(String groupName, String groupNamespace) throws GroupNotFoundException {
        if (StringUtils.isBlank(groupNamespace) || StringUtils.isBlank(groupName)) {
            throw new GroupNotFoundException("Valid Workgroup could not be found... Namespace: " + groupNamespace + "  Name: " + groupName);
        }
        Group workgroup = getGroupService().getGroupByNamespaceCodeAndName(groupNamespace, groupName);
        if (workgroup == null) {
            throw new GroupNotFoundException("Valid Workgroup could not be found... Namespace: " + groupNamespace + "  Name: " + groupName);
        }
        return workgroup;
    }
    
    public DocumentType generateNewDocumentTypeFromExisting(String documentTypeName) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, GroupNotFoundException, WorkflowException {
        // export the document type that exists in the database
        DocumentType docTypeFromDatabase = KEWServiceLocator.getDocumentTypeService().findByName(documentTypeName);
        if (ObjectUtils.isNotNull(docTypeFromDatabase)) {
            KewExportDataSet kewExportDataSet = new KewExportDataSet();
            kewExportDataSet.getDocumentTypes().add(docTypeFromDatabase);
            byte[] xmlBytes = CoreApiServiceLocator.getXmlExporterService().export(kewExportDataSet.createExportDataSet());
            // use the exported document type from the database to generate the new document type
            Document tempDocument = XmlHelper.trimXml(new BufferedInputStream(new ByteArrayInputStream(xmlBytes)));
            Node documentTypeNode = (Node) getXPath().evaluate("/" + DATA_ELEMENT + "/" + DOCUMENT_TYPES + "/" + DOCUMENT_TYPE, tempDocument, XPathConstants.NODE);
            return getFullDocumentType(false, documentTypeNode);
        }
        return null;
    }

    private void routeDocumentType(DocumentType documentType) {
        DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(documentType.getName());
        // if the docType exists then check locking
        if (ObjectUtils.isNotNull(docType)) {
            Maintainable docTypeMaintainable = new DocumentTypeMaintainable();
            docTypeMaintainable.setBusinessObject(docType);
            docTypeMaintainable.setBoClass(docType.getClass());
            // below will throw a ValidationException if a valid locking document exists
            MaintenanceUtils.checkForLockingDocument(docTypeMaintainable, true);
        }
        KEWServiceLocator.getDocumentTypeService().versionAndSave(documentType);
    }

    private String getDocumentTypeNameFromNode(Node documentTypeNode) throws XPathExpressionException {
        try {
            return (String) getXPath().evaluate("./name", documentTypeNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining document type name", xpee);
            throw xpee;
        }
    }

    /**
     * Checks for route nodes that are declared but never used and throws an XmlException if one is discovered.
     */
    private void checkForOrphanedRouteNodes(Node documentTypeNode, Node routeNodesNode) throws XPathExpressionException, XmlException {
        NodeList nodesInPath = (NodeList) getXPath().evaluate("./routePaths/routePath//*/@name", documentTypeNode, XPathConstants.NODESET);
        List<String> nodeNamesInPath = new ArrayList<String>(nodesInPath.getLength());
        for (int index = 0; index < nodesInPath.getLength(); index++) {
            Node nameNode = nodesInPath.item(index);
            nodeNamesInPath.add(nameNode.getNodeValue());
        }

        NodeList declaredNodes = (NodeList) getXPath().evaluate("./*/@name", routeNodesNode, XPathConstants.NODESET);
        List<String> declaredNodeNames = new ArrayList<String>(declaredNodes.getLength());
        for (int index = 0; index < declaredNodes.getLength(); index++) {
            Node nameNode = declaredNodes.item(index);
            declaredNodeNames.add(nameNode.getNodeValue());
        }

        // now compare the declared nodes to the ones actually used
        List<String> orphanedNodes = new ArrayList<String>();
        for (String declaredNode : declaredNodeNames) {
            boolean foundNode = false;
            for (String nodeInPath : nodeNamesInPath) {
                if (nodeInPath.equals(declaredNode)) {
                    foundNode = true;
                    break;
                }
            }
            if (!foundNode) {
                orphanedNodes.add(declaredNode);
            }
        }
        if (!orphanedNodes.isEmpty()) {
            String message = "The following nodes were declared but never used: ";
            for (Iterator iterator = orphanedNodes.iterator(); iterator.hasNext();) {
                String orphanedNode = (String) iterator.next();
                message += orphanedNode + (iterator.hasNext() ? ", " : "");
            }
            throw new XmlException(message);
        }
    }

    private void createProcesses(NodeList processNodes, DocumentType documentType) {
        for (int index = 0; index < processNodes.getLength(); index++) {
            Node processNode = processNodes.item(index);
            NamedNodeMap attributes = processNode.getAttributes();
            Node processNameNode = attributes.getNamedItem(PROCESS_NAME);
            String processName = (processNameNode == null ? null : processNameNode.getNodeValue());
            ProcessDefinitionBo process = new ProcessDefinitionBo();
            if (org.apache.commons.lang.StringUtils.isEmpty(processName)) {
                process.setInitial(true);
                process.setName(KewApiConstants.PRIMARY_PROCESS_NAME);
            } else {
                process.setInitial(false);
                process.setName(processName);
            }
            process.setDocumentType(documentType);
            documentType.addProcess(process);
        }
    }
    
    private void createEmptyProcess(DocumentType documentType) {
            ProcessDefinitionBo process = new ProcessDefinitionBo();
            
            process.setInitial(true);
            process.setName(KewApiConstants.PRIMARY_PROCESS_NAME);

            process.setDocumentType(documentType);
            documentType.addProcess(process);
    }

    private RoutePathContext findNodeOnXPath( String nodeName, RoutePathContext context,  Node routePathNode ) throws XPathExpressionException, XmlException {
        Node currentNode;

        while (context.nodeQName.length() > 1) {
            context.nodeXPath = context.nodeXPath.substring(0,context.nodeXPath.lastIndexOf("//"));
            context.nodeQName = context.nodeQName.substring(0,context.nodeQName.lastIndexOf(":", context.nodeQName.lastIndexOf(":")-1)+1);
            if (StringUtils.isBlank( context.nodeQName)) {
                context.nodeQName = ":";
            }

            try {
                 currentNode = (Node) getXPath().evaluate(context.nodeXPath + "//" + "*[@name = '" + nodeName +"']" , routePathNode, XPathConstants.NODE);
            } catch (XPathExpressionException xpee) {
                 LOG.error("Error obtaining routePath for routeNode", xpee);
                 throw xpee;
            }

            if (currentNode != null) {
                 return  context;
            }
        }

        return context;
    }

    private RouteNode createRouteNode(RouteNode previousRouteNode, String nodeName, Node routePathNode, Node routeNodesNode, DocumentType documentType, RoutePathContext context) throws XPathExpressionException, XmlException, GroupNotFoundException {
        if (nodeName == null) return null;

        Node currentNode;
        context.nodeXPath += "//" + "*[@name = '" + nodeName +"']";
        context.nodeQName += nodeName + ":";

        try {
            currentNode = (Node) getXPath().evaluate(context.nodeXPath + "//" + "*[@name = '" + nodeName +"']" , routePathNode, XPathConstants.NODE);
            if (currentNode == null) {
                findNodeOnXPath( nodeName, context,  routePathNode );
                currentNode = (Node) getXPath().evaluate(context.nodeXPath + "//" + "*[@name = '" + nodeName +"']" , routePathNode, XPathConstants.NODE);
            }
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining routePath for routeNode", xpee);
            throw xpee;
        }

        if (currentNode == null) {
            String message = "Next node '" + nodeName + "' for node '" + previousRouteNode.getRouteNodeName() + "' not found!";
            LOG.error(message);
            throw new XmlException(message);
        }

        context.nodeXPath += "//" + "*[@name = '" + nodeName +"']";
        context.nodeQName += nodeName + ":";
        LOG.debug("nodeQNme:"+context.nodeQName);
        boolean nodeIsABranch;

        try {
            nodeIsABranch = ((Boolean) getXPath().evaluate("self::node()[local-name() = 'branch']", currentNode, XPathConstants.BOOLEAN)).booleanValue();
        } catch (XPathExpressionException xpee) {
            LOG.error("Error testing whether node is a branch", xpee);
            throw xpee;
        }

        if (nodeIsABranch) {
            throw new XmlException("Next node cannot be a branch node");
        }

        String localName;

        try {
            localName = (String) getXPath().evaluate("local-name(.)", currentNode, XPathConstants.STRING);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining node local-name", xpee);
            throw xpee;
        }

        RouteNode currentRouteNode = null;

        if (nodesMap.containsKey(context.nodeQName)) {
            currentRouteNode = (RouteNode) nodesMap.get(context.nodeQName);
        } else {
            String nodeExpression = ".//*[@name='" + nodeName + "']";
            currentRouteNode = makeRouteNodePrototype(localName, nodeName, nodeExpression, routeNodesNode, documentType, context);
        }

        if ("split".equalsIgnoreCase(localName)) {
            getSplitNextNodes(currentNode, routePathNode, currentRouteNode, routeNodesNode, documentType,  cloneContext(context));
        }

        if (previousRouteNode != null) {
            previousRouteNode.getNextNodes().add(currentRouteNode);
            nodesMap.put(context.previousNodeQName, previousRouteNode);
            currentRouteNode.getPreviousNodes().add(previousRouteNode);
        }

        String nextNodeName = null;
        boolean hasNextNodeAttrib;

        try {
            hasNextNodeAttrib = ((Boolean) getXPath().evaluate(NEXT_NODE_EXP, currentNode, XPathConstants.BOOLEAN)).booleanValue();
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining node nextNode attrib", xpee);
            throw xpee;
        }

        if (hasNextNodeAttrib) {
            // if the node has a nextNode but is not a split node, the nextNode is used for its node
            if (!"split".equalsIgnoreCase(localName)) {
                try {
                    nextNodeName = (String) getXPath().evaluate(NEXT_NODE_EXP, currentNode, XPathConstants.STRING);
                } catch (XPathExpressionException xpee) {
                    LOG.error("Error obtaining node nextNode attrib", xpee);
                    throw xpee;
                }

                context.previousNodeQName = context.nodeQName;
                createRouteNode(currentRouteNode, nextNodeName, routePathNode, routeNodesNode, documentType, cloneContext(context));
            } else {
                // if the node has a nextNode but is a split node, the nextNode must be used for that split node's join node
                nodesMap.put(context.nodeQName, currentRouteNode);
            }

        } else {
            // if the node has no nextNode of its own and is not a join which gets its nextNode from its parent split node
            if (!"join".equalsIgnoreCase(localName)) {
                nodesMap.put(context.nodeQName, currentRouteNode);

            } else {    // if join has a parent nextNode (on its split node) and join has not already walked this path
                boolean parentHasNextNodeAttrib;
                try {
                    parentHasNextNodeAttrib = ((Boolean) getXPath().evaluate(PARENT_NEXT_NODE_EXP, currentNode, XPathConstants.BOOLEAN)).booleanValue();
                } catch (XPathExpressionException xpee) {
                    LOG.error("Error obtaining parent node nextNode attrib", xpee);
                    throw xpee;
                }

                if (parentHasNextNodeAttrib && !nodesMap.containsKey(context.nodeQName)) {
                    try {
                        nextNodeName = (String) getXPath().evaluate(PARENT_NEXT_NODE_EXP, currentNode, XPathConstants.STRING);
                    } catch (XPathExpressionException xpee) {
                        LOG.error("Error obtaining parent node nextNode attrib", xpee);
                        throw xpee;
                    }

                    context.previousNodeQName = context.nodeQName;
                    createRouteNode(currentRouteNode, nextNodeName, routePathNode, routeNodesNode, documentType, cloneContext(context));
                } else {
                    // if join's parent split node does not have a nextNode
                    nodesMap.put(context.nodeQName, currentRouteNode);
                }
            }
        }

        // handle nextAppDocStatus route node attribute
        String nextDocStatusName = null;
        boolean hasNextDocStatus;

        try {
            hasNextDocStatus = ((Boolean) getXPath().evaluate(NEXT_DOC_STATUS_EXP, currentNode, XPathConstants.BOOLEAN)).booleanValue();
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining node nextAppDocStatus attrib", xpee);
            throw xpee;
        }

        if (hasNextDocStatus){
        	try {
        		nextDocStatusName = (String) getXPath().evaluate(NEXT_DOC_STATUS_EXP, currentNode, XPathConstants.STRING);
        	} catch (XPathExpressionException xpee) {
        		LOG.error("Error obtaining node nextNode attrib", xpee);
        		throw xpee;
        	}
        	
        	//validate against allowable values if defined
        	if (documentType.getValidApplicationStatuses() != null  && documentType.getValidApplicationStatuses().size() > 0){
        		Iterator iter = documentType.getValidApplicationStatuses().iterator();
        		boolean statusValidated = false;

                while (iter.hasNext())
        		{
        			ApplicationDocumentStatus myAppDocStat = (ApplicationDocumentStatus) iter.next();
        			if (nextDocStatusName.compareToIgnoreCase(myAppDocStat.getStatusName()) == 0)
        			{
        				statusValidated = true;
        				break;
        			}
        		}

        		if (!statusValidated){
        				XmlException xpee = new XmlException("AppDocStatus value " +  nextDocStatusName + " not allowable.");
        				LOG.error("Error validating nextAppDocStatus name: " +  nextDocStatusName + " against acceptable values.", xpee);
        				throw xpee; 
        		}
        	}

        	currentRouteNode.setNextDocStatus(nextDocStatusName);
        }

        return currentRouteNode;
    }

    private void getSplitNextNodes(Node splitNode, Node routePathNode, RouteNode splitRouteNode, Node routeNodesNode, DocumentType documentType, RoutePathContext context) throws XPathExpressionException, XmlException, GroupNotFoundException {
        NodeList splitBranchNodes;

        try {
            splitBranchNodes = (NodeList) getXPath().evaluate("./branch", splitNode, XPathConstants.NODESET);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error obtaining split node branch", xpee);
            throw xpee;
        }

        String splitQName = context.nodeQName;
        String splitXPath = context.nodeXPath;

        for (int i = 0; i < splitBranchNodes.getLength(); i++) {
            String branchName;
            try {
                branchName = (String) getXPath().evaluate("./@name", splitBranchNodes.item(i), XPathConstants.STRING);
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining branch name attribute", xpee);
                throw xpee;
            }

            String name;

            try {
                name = (String) getXPath().evaluate("./*[1]/@name", splitBranchNodes.item(i), XPathConstants.STRING);
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining first split branch node name", xpee);
                throw xpee;
            }

            context.nodeQName = splitQName + branchName +":";
            context.nodeXPath = splitXPath  + "//" + "*[@name = '" + branchName +"']";
            context.branch = new BranchPrototype();
            context.branch.setName(branchName);
            context.previousNodeQName = splitQName;

            createRouteNode(splitRouteNode, name, routePathNode, routeNodesNode, documentType, cloneContext(context));
        }
    }

    private RouteNode makeRouteNodePrototype(String nodeTypeName, String nodeName, String nodeExpression, Node routeNodesNode, DocumentType documentType, RoutePathContext context) throws XPathExpressionException, GroupNotFoundException, XmlException {
        NodeList nodeList;
        try {
            nodeList = (NodeList) getXPath().evaluate(nodeExpression, routeNodesNode, XPathConstants.NODESET);
        } catch (XPathExpressionException xpee) {
            LOG.error("Error evaluating node expression: '" + nodeExpression + "'");
            throw xpee;
        }

        if (nodeList.getLength() > 1) {
            throw new XmlException("More than one node under routeNodes has the same name of '" + nodeName + "'");
        }

        if (nodeList.getLength() == 0) {
            throw new XmlException("No node definition was found with the name '" + nodeName + "'");
        }

        Node node = nodeList.item(0);

        RouteNode routeNode = new RouteNode();
        // set fields that all route nodes of all types should have defined
        routeNode.setDocumentType(documentType);
        routeNode.setRouteNodeName((String) getXPath().evaluate("./@name", node, XPathConstants.STRING));
        routeNode.setContentFragment(XmlJotter.jotNode(node));

        if (XmlHelper.pathExists(xpath, "./activationType", node)) {
            routeNode.setActivationType(ActivationTypeEnum.parse((String) getXPath().evaluate("./activationType", node, XPathConstants.STRING)).getCode());
        } else {
            routeNode.setActivationType(DEFAULT_ACTIVATION_TYPE);
        }

        Group exceptionWorkgroup = defaultExceptionWorkgroup;

        String exceptionWg = null;
        String exceptionWorkgroupName = null;
        String exceptionWorkgroupNamespace = null;

        if (XmlHelper.pathExists(xpath, "./" + EXCEPTION_GROUP_NAME, node)) {
        	exceptionWorkgroupName = Utilities.substituteConfigParameters(
        			(String) getXPath().evaluate("./" + EXCEPTION_GROUP_NAME, node, XPathConstants.STRING)).trim();
        	exceptionWorkgroupNamespace = Utilities.substituteConfigParameters(
        			(String) getXPath().evaluate("./" + EXCEPTION_GROUP_NAME + "/@" + NAMESPACE, node, XPathConstants.STRING)).trim();
        }

        if (org.apache.commons.lang.StringUtils.isEmpty(exceptionWorkgroupName) && XmlHelper.pathExists(xpath, "./" + EXCEPTION_WORKGROUP_NAME, node)) {
        	LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(EXCEPTION_WORKGROUP_NAME).append(
				"', please use '").append(EXCEPTION_GROUP_NAME).append("' instead.").toString());
        	// for backward compatibility we also need to be able to support exceptionWorkgroupName
        	exceptionWg = Utilities.substituteConfigParameters((String) getXPath().evaluate("./" + EXCEPTION_WORKGROUP_NAME, node, XPathConstants.STRING));
        	exceptionWorkgroupName = Utilities.parseGroupName(exceptionWg);
        	exceptionWorkgroupNamespace = Utilities.parseGroupNamespaceCode(exceptionWg);
        }

        if (org.apache.commons.lang.StringUtils.isEmpty(exceptionWorkgroupName) && XmlHelper.pathExists(xpath, "./" + EXCEPTION_WORKGROUP, node)) {
        	LOG.warn((new StringBuilder(160)).append("Document Type XML is using deprecated element '").append(EXCEPTION_WORKGROUP).append(
				"', please use '").append(EXCEPTION_GROUP_NAME).append("' instead.").toString());
            // for backward compatibility we also need to be able to support exceptionWorkgroup
            exceptionWg = Utilities.substituteConfigParameters((String) getXPath().evaluate("./" + EXCEPTION_WORKGROUP, node, XPathConstants.STRING));
            exceptionWorkgroupName = Utilities.parseGroupName(exceptionWg);
            exceptionWorkgroupNamespace = Utilities.parseGroupNamespaceCode(exceptionWg);
        }

        if (org.apache.commons.lang.StringUtils.isEmpty(exceptionWorkgroupName)) {
            if (routeNode.getDocumentType().getDefaultExceptionWorkgroup() != null) {
                exceptionWorkgroupName = routeNode.getDocumentType().getDefaultExceptionWorkgroup().getName();
                exceptionWorkgroupNamespace = routeNode.getDocumentType().getDefaultExceptionWorkgroup().getNamespaceCode();
            }
        }

        if (org.apache.commons.lang.StringUtils.isNotEmpty(exceptionWorkgroupName)
                && org.apache.commons.lang.StringUtils.isNotEmpty(exceptionWorkgroupNamespace)) {
            exceptionWorkgroup = getGroupService().getGroupByNamespaceCodeAndName(exceptionWorkgroupNamespace,
                    exceptionWorkgroupName);
            if (exceptionWorkgroup == null) {
                throw new GroupNotFoundException("Could not locate exception workgroup with namespace '" + exceptionWorkgroupNamespace + "' and name '" + exceptionWorkgroupName + "'");
            }
        } else {
            if (StringUtils.isEmpty(exceptionWorkgroupName) ^ StringUtils.isEmpty(exceptionWorkgroupNamespace)) {
                throw new GroupNotFoundException("Could not locate exception workgroup with namespace '" + exceptionWorkgroupNamespace + "' and name '" + exceptionWorkgroupName + "'");
            }
        }

        if (exceptionWorkgroup != null) {
            routeNode.setExceptionWorkgroupName(exceptionWorkgroup.getName());
            routeNode.setExceptionWorkgroupId(exceptionWorkgroup.getId());
        }

        if (((Boolean) getXPath().evaluate("./mandatoryRoute", node, XPathConstants.BOOLEAN)).booleanValue()) {
            routeNode.setMandatoryRouteInd(Boolean.valueOf((String)getXPath().evaluate("./mandatoryRoute", node, XPathConstants.STRING)));
        } else {
            routeNode.setMandatoryRouteInd(Boolean.FALSE);
        }

        if (((Boolean) getXPath().evaluate("./finalApproval", node, XPathConstants.BOOLEAN)).booleanValue()) {
            routeNode.setFinalApprovalInd(Boolean.valueOf((String)getXPath().evaluate("./finalApproval", node, XPathConstants.STRING)));
        } else {
            routeNode.setFinalApprovalInd(Boolean.FALSE);
        }

        // for every simple child element of the node, store a config parameter of the element name and text content
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n instanceof Element) {
                Element e = (Element) n;
                String name = e.getNodeName();
                String content = getTextContent(e);
                routeNode.getConfigParams().add(new RouteNodeConfigParam(routeNode, name, content));
            }
        }

        // make sure a default rule selector is set
        Map<String, String> cfgMap = Utilities.getKeyValueCollectionAsMap(routeNode.getConfigParams());
        if (!cfgMap.containsKey(RouteNode.RULE_SELECTOR_CFG_KEY)) {
            routeNode.getConfigParams().add(new RouteNodeConfigParam(routeNode, RouteNode.RULE_SELECTOR_CFG_KEY, FlexRM.DEFAULT_RULE_SELECTOR));
        }

        if (((Boolean) getXPath().evaluate("./ruleTemplate", node, XPathConstants.BOOLEAN)).booleanValue()) {
            String ruleTemplateName = (String) getXPath().evaluate("./ruleTemplate", node, XPathConstants.STRING);
            RuleTemplateBo ruleTemplate = KEWServiceLocator.getRuleTemplateService().findByRuleTemplateName(ruleTemplateName);

            if (ruleTemplate == null) {
                throw new XmlException("Rule template for node '" + routeNode.getRouteNodeName() + "' not found: " + ruleTemplateName);
            }

            routeNode.setRouteMethodName(ruleTemplateName);
            routeNode.setRouteMethodCode(KewApiConstants.ROUTE_LEVEL_FLEX_RM);
        } else if (((Boolean) getXPath().evaluate("./routeModule", node, XPathConstants.BOOLEAN)).booleanValue()) {
            routeNode.setRouteMethodName((String) getXPath().evaluate("./routeModule", node, XPathConstants.STRING));
            routeNode.setRouteMethodCode(KewApiConstants.ROUTE_LEVEL_ROUTE_MODULE);
        } else if (((Boolean) getXPath().evaluate("./peopleFlows", node, XPathConstants.BOOLEAN)).booleanValue()) {
            routeNode.setRouteMethodCode(KewApiConstants.ROUTE_LEVEL_PEOPLE_FLOW);
        } else if (((Boolean) getXPath().evaluate("./rulesEngine", node, XPathConstants.BOOLEAN)).booleanValue()) {
            // validate that the element has at least one of the two required attributes, XML schema does not provide a way for us to
            // check this so we must do so programatically
            Element rulesEngineElement = (Element)getXPath().evaluate("./rulesEngine", node, XPathConstants.NODE);
            String executorName = rulesEngineElement.getAttribute("executorName");
            String executorClass = rulesEngineElement.getAttribute("executorClass");

            if (StringUtils.isBlank(executorName) && StringUtils.isBlank(executorClass)) {
                throw new XmlException("The rulesEngine declaration must have at least one of 'executorName' or 'executorClass' attributes.");
            } else if (StringUtils.isNotBlank(executorName) && StringUtils.isNotBlank(executorClass)) {
                throw new XmlException("Only one of 'executorName' or 'executorClass' may be declared on rulesEngine configuration, but both were declared.");
            }

            routeNode.setRouteMethodCode(KewApiConstants.ROUTE_LEVEL_RULES_ENGINE);
        }

        String nodeType = null;

        if (((Boolean) getXPath().evaluate("./type", node, XPathConstants.BOOLEAN)).booleanValue()) {
            nodeType = (String) getXPath().evaluate("./type", node, XPathConstants.STRING);
        } else {
            String localName = (String) getXPath().evaluate("local-name(.)", node, XPathConstants.STRING);

            if ("start".equalsIgnoreCase(localName)) {
                nodeType = "org.kuali.rice.kew.engine.node.InitialNode";
            } else if ("split".equalsIgnoreCase(localName)) {
                nodeType = "org.kuali.rice.kew.engine.node.SimpleSplitNode";
            } else if ("join".equalsIgnoreCase(localName)) {
                nodeType = "org.kuali.rice.kew.engine.node.SimpleJoinNode";
            } else if ("requests".equalsIgnoreCase(localName)) {
                nodeType = "org.kuali.rice.kew.engine.node.RequestsNode";
            } else if ("process".equalsIgnoreCase(localName)) {
                nodeType = "org.kuali.rice.kew.engine.node.SimpleSubProcessNode";
            } else if (NodeType.ROLE.getName().equalsIgnoreCase(localName)) {
                nodeType = RoleNode.class.getName();
            }

        }

        if (org.apache.commons.lang.StringUtils.isEmpty(nodeType)) {
            throw new XmlException("Could not determine node type for the node named '" + routeNode.getRouteNodeName() + "'");
        }

        routeNode.setNodeType(nodeType);

        String localName = (String) getXPath().evaluate("local-name(.)", node, XPathConstants.STRING);

        if ("split".equalsIgnoreCase(localName)) {
            context.splitNodeStack.addFirst(routeNode);
        } else if ("join".equalsIgnoreCase(localName) && context.splitNodeStack.size() != 0) {
            // join node should have same branch prototype as split node
            RouteNode splitNode = (RouteNode)context.splitNodeStack.removeFirst();
            context.branch = splitNode.getBranch();
        } else if (NodeType.ROLE.getName().equalsIgnoreCase(localName)) {
            routeNode.setRouteMethodName(RoleRouteModule.class.getName());
            routeNode.setRouteMethodCode(KewApiConstants.ROUTE_LEVEL_ROUTE_MODULE);
        }

        routeNode.setBranch(context.branch);

        return routeNode;
    }

    private static String getTextContent(org.w3c.dom.Element element) {
		NodeList children = element.getChildNodes();
        if (children.getLength() == 0) {
            return "";
        }
		Node node = children.item(0);
		return node.getNodeValue();
	}

    private List getDocumentTypePolicies(NodeList documentTypePolicies, DocumentType documentType) throws XPathExpressionException, XmlException, ParserConfigurationException {
        List policies = new ArrayList();
        Set policyNames = new HashSet();

        for (int i = 0; i < documentTypePolicies.getLength(); i++) {
            DocumentTypePolicy policy = new DocumentTypePolicy();
            policy.setDocumentType(documentType);
            try {
                String policyName = (String) getXPath().evaluate("./name", documentTypePolicies.item(i), XPathConstants.STRING);
                policy.setPolicyName(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.fromCode(policyName).getCode().toUpperCase());
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining document type policy name", xpee);
                throw xpee;
            }
            try {
                if (((Boolean) getXPath().evaluate("./value", documentTypePolicies.item(i), XPathConstants.BOOLEAN)).booleanValue()) {
                    policy.setPolicyValue(Boolean.valueOf((String) getXPath().evaluate("./value", documentTypePolicies.item(i), XPathConstants.STRING)));
                } else {
                    policy.setPolicyValue(Boolean.FALSE);
                }
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining document type policy value", xpee);
                throw xpee;
            }
            try {
            	String policyStringValue = (String) getXPath().evaluate("./stringValue", documentTypePolicies.item(i), XPathConstants.STRING);
            	if (!StringUtils.isEmpty(policyStringValue)){
            		policy.setPolicyStringValue(policyStringValue.toUpperCase());
            		policy.setPolicyValue(Boolean.TRUE);
            		// if DocumentStatusPolicy, check against allowable values
            		if (KewApiConstants.DOCUMENT_STATUS_POLICY.equalsIgnoreCase(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.fromCode(policy.getPolicyName()).getCode())){
            			boolean found = false;
            			for (int index=0; index<KewApiConstants.DOCUMENT_STATUS_POLICY_VALUES.length; index++) {
            				if (KewApiConstants.DOCUMENT_STATUS_POLICY_VALUES[index].equalsIgnoreCase(policyStringValue)){
            					found = true;
            					break;
            				}            					
            			}
            			if (!found){
            				throw new XmlException("Application Document Status string value: " + policyStringValue + " is invalid.");
            			}
            		}
            		
            	} else {
            		//DocumentStatusPolicy requires a <stringValue> tag
            		if (KewApiConstants.DOCUMENT_STATUS_POLICY.equalsIgnoreCase(org.kuali.rice.kew.api.doctype.DocumentTypePolicy.fromCode(policy.getPolicyName()).getCode())){
            			throw new XmlException("Application Document Status Policy requires a <stringValue>");
            		}

                    String customConfig = parseDocumentPolicyCustomXMLConfig(documentTypePolicies.item(i));
                    if (customConfig != null) {
                        policy.setPolicyStringValue(customConfig);
                    }
            	}
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining document type policy string value", xpee);
                throw xpee;
            }
            
            if (!policyNames.add(policy.getPolicyName())) {
                throw new XmlException("Policy '" + policy.getPolicyName() + "' has already been defined on this document");
            } else {
                policies.add(policy);
            }
        }

        return policies;
    }

    /**
     * Parses any custom XML configuration if present and returns it as an XML string.
     * If no custom XML elements are present in the policy configuration, null is returned.
     * @param policyNode the document type policy Node
     * @return XML configuration string or null
     * @throws ParserConfigurationException
     */
    private static String parseDocumentPolicyCustomXMLConfig(Node policyNode) throws ParserConfigurationException {
        final Collection<String> KNOWN_POLICY_ELEMENTS = Arrays.asList(new String[] { "name", "value", "stringValue" });
        // Store any other elements as XML in the policy string value
        Document policyConfig = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = policyConfig.createElement("config");
        policyConfig.appendChild(root);
        // add unknown nodes from the config document
        NodeList children = policyNode.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node c = children.item(j);
            if (c instanceof Element && !KNOWN_POLICY_ELEMENTS.contains(c.getNodeName())) {
                root.appendChild(policyConfig.importNode(c, true));
            }
        }
        // if there are in-fact custom xml configuration nodes, then go ahead and save the config doc as XML
        return (root.getChildNodes().getLength() > 0) ? XmlJotter.jotDocument(policyConfig) : null;
    }

    private List getDocumentTypeAttributes(NodeList documentTypeAttributes, DocumentType documentType) throws XPathExpressionException, WorkflowException {
        List attributes = new ArrayList();

        for (int i = 0; i < documentTypeAttributes.getLength(); i++) {
            DocumentTypeAttributeBo attribute = new DocumentTypeAttributeBo();
            attribute.setDocumentType(documentType);
            String ruleAttributeName;

            try {
                ruleAttributeName = (String) getXPath().evaluate("./name", documentTypeAttributes.item(i), XPathConstants.STRING);
            } catch (XPathExpressionException xpee) {
                LOG.error("Error obtaining rule attribute name", xpee);
                throw xpee;
            }

            RuleAttribute ruleAttribute = KEWServiceLocator.getRuleAttributeService().findByName(ruleAttributeName);

            if (ruleAttribute == null) {
                throw new WorkflowException("Could not find rule attribute: " + ruleAttributeName);
            }

            attribute.setDocumentType(documentType);
            attribute.setRuleAttribute(ruleAttribute);
            attribute.setOrderIndex(i+1);
            attributes.add(attribute);
        }

        return attributes;
    }

    private class RoutePathContext {
        public BranchPrototype branch;
        public LinkedList splitNodeStack = new LinkedList();
        public String nodeXPath = ".";
        public String nodeQName = ":";
        public String previousNodeQName = "";
    }

    private RoutePathContext cloneContext(RoutePathContext context){
        RoutePathContext localContext = new RoutePathContext();
        localContext.branch            = context.branch;
        localContext.nodeQName         = context.nodeQName;
        localContext.nodeXPath         = context.nodeXPath;
        localContext.previousNodeQName = context.previousNodeQName;

        return localContext;
    }

    protected GroupService getGroupService() {
        return KimApiServiceLocator.getGroupService();
    }

    /**
     * This is a helper class for indicating if an unprocessed document type node is "standard" or "routing."
     * 
     * @author Kuali Rice Team (rice.collab@kuali.org)
     */
    private class DocTypeNode {
        /** The Node that needs to be converted into a doc type. */
        public final Node docNode;
        /** A flag for indicating the document's type; true indicates "standard," false indicates "routing." */
        public final boolean isStandard;
        
        /**
         * Constructs a DocTypeNode instance containing the specified Node and flag.
         * 
         * @param newNode The unprocessed document type.
         * @param newFlag An indicator of what type of document this is (true for "standard," false for "routing").
         */
        public DocTypeNode(Node newNode, boolean newFlag) {
            docNode = newNode;
            isStandard = newFlag;
        }
    }

    /**
     * Utility class used to produce an ascending sequence of integer values starting with 0
     */
    private static class Counter {
        private int value = 0;

        public int nextValue() {
            return value++;
        }
    }

    /**
     * <p>Helper class encapsulating parsing logic for building ApplicationDocumentStatus and
     * ApplicationDocumentStatusCategory from the document type DOM.</p>
     *
     * <p>Note that this class should not be instantiated.  Instead use the static utility method 
     * {@link org.kuali.rice.kew.xml.DocumentTypeXmlParser.ApplicationDocumentStatusParser#parseValidApplicationStatuses(org.kuali.rice.kew.doctype.bo.DocumentType, org.w3c.dom.Node, javax.xml.xpath.XPath)}.</p>
     */
    private static class ApplicationDocumentStatusParser {

        // set used to make sure there are no problematic or confusing name collisions
        private final Set<String> uniqueStatusNames = new HashSet<String>();

        private final List<ApplicationDocumentStatus> parsedStatuses = new ArrayList<ApplicationDocumentStatus>();
        private final List<ApplicationDocumentStatusCategory> parsedCategories = new ArrayList<ApplicationDocumentStatusCategory>();

        Counter newStatusSequence = new Counter(); // used to order the statuses

        /**
         * Do not instantiate, instead use the static utility method
         * {@link #parseValidApplicationStatuses(org.kuali.rice.kew.doctype.bo.DocumentType, org.w3c.dom.Node, javax.xml.xpath.XPath)}
         */
        @Deprecated  // deprecated to discourage improper use since private is not strong enough in an inner class
        private ApplicationDocumentStatusParser() { }

        /**
         *
         * @param documentType
         * @param documentTypeNode
         * @param xpath
         * @throws XPathExpressionException
         */
        @SuppressWarnings("deprecated")
        public static void parseValidApplicationStatuses(DocumentType documentType, Node documentTypeNode, XPath xpath) throws XPathExpressionException {
            ApplicationDocumentStatusParser parser = new ApplicationDocumentStatusParser();
            parser.parseValidApplicationStatusesHelper(documentType, documentTypeNode, xpath);
        }

        /**
         * Parses the validApplicationStatuses in the doc type (if there are any) and (<b>SIDE EFFECT</b>) adds the
         * results to the given documentType.
         *
         * @param documentType the documentType being built
         * @param documentTypeNode the DOM {@link Node} for the xml representation of the document type
         * @param xpath the XPath object to use for searching within the documentTypeNode
         * @throws XPathExpressionException
         */
        private void parseValidApplicationStatusesHelper(DocumentType documentType, Node documentTypeNode, XPath xpath)
                throws XPathExpressionException {
            /*
            * The following code does not need to apply the isOverwrite mode logic because it already checks to see if each node
            * is available on the ingested XML. If the node is ingested then it doesn't matter if we're in overwrite mode or not
            * the ingested code should save.
            */
            NodeList appDocStatusList = (NodeList) xpath.evaluate("./" + APP_DOC_STATUSES, documentTypeNode,
                    XPathConstants.NODESET);
            if (appDocStatusList.getLength() > 1) {
                // more than one <validDocumentStatuses> tag is invalid
                throw new XmlException("More than one " + APP_DOC_STATUSES + " node is present in a document type node");

            } else if (appDocStatusList.getLength() > 0) {

                // if there is exactly one <validDocumentStatuses> tag then parse it and use the values
                parseCategoriesAndStatusesHelper(documentType, appDocStatusList);
            }
        }

        /**
         * <p>Parses the application document status content (both statuses and categories) from the given NodeList.</p>
         *
         * <p>SIDE EFFECT: sets the ValidApplicationStatuses and ApplicationStatusCategories on the DocumentType
         * argument</p>
         *
         * @param documentType the documentType that is being built from the DOM content
         * @param appDocStatusList the NodeList holding the children of the validApplicationStatuses element
         */
        private void parseCategoriesAndStatusesHelper(DocumentType documentType, NodeList appDocStatusList) {

            NodeList appDocStatusChildren = appDocStatusList.item(0).getChildNodes();

            for (int appDocStatusChildrenIndex = 0; appDocStatusChildrenIndex < appDocStatusChildren.getLength();
                 appDocStatusChildrenIndex ++) {

                Node child = appDocStatusChildren.item(appDocStatusChildrenIndex);

                if (STATUS.equals(child.getNodeName())) {

                    ApplicationDocumentStatus status =
                            parseApplicationDocumentStatusHelper(documentType, child);

                    parsedStatuses.add(status);

                } else if (CATEGORY.equals(child.getNodeName())) {

                    Node categoryNameAttribute = child.getAttributes().getNamedItem("name");
                    String categoryName = categoryNameAttribute.getNodeValue();

                    // validate category names are all unique
                    validateUniqueName(categoryName);

                    ApplicationDocumentStatusCategory category = new ApplicationDocumentStatusCategory();
                    category.setCategoryName(categoryName);
                    category.setDocumentType(documentType);

                    // iterate through children
                    NodeList categoryChildren = child.getChildNodes();
                    for (int categoryChildIndex = 0; categoryChildIndex < categoryChildren.getLength();
                         categoryChildIndex++) {
                        Node categoryChild = categoryChildren.item(categoryChildIndex);
                        if (Node.ELEMENT_NODE == categoryChild.getNodeType()) {
                            ApplicationDocumentStatus status =
                                    parseApplicationDocumentStatusHelper(documentType, categoryChild);
                            status.setCategoryName(category.getCategoryName());

                            parsedStatuses.add(status);
                        }
                    }

                    parsedCategories.add(category);
                }
            }

            documentType.setValidApplicationStatuses(parsedStatuses);
            documentType.setApplicationStatusCategories(parsedCategories);
        }

        /**
         * Parse an element Node containg a single application document status name and return an
         * ApplicationDocumentStatus object for it.
         *
         * @param documentType the document type we are building
         * @param node the status element node
         * @return the ApplicationDocumentStatus constructed
         */
        private ApplicationDocumentStatus parseApplicationDocumentStatusHelper(DocumentType documentType, Node node) {
            String statusName = node.getTextContent();
            validateUniqueName(statusName);

            ApplicationDocumentStatus status = new ApplicationDocumentStatus();
            status.setStatusName(statusName);
            status.setDocumentType(documentType);
            // order the statuses according to the order we encounter them
            status.setSequenceNumber(newStatusSequence.nextValue());

            return status;
        }

        /**
         * <p>check that the name has not been used already.
         *
         * <p>Side-effect: This check adds the name to the uniqueStatusNames Set.
         * @param name
         * @throws XmlException if the name has already been used for another status or category
         */
        private void validateUniqueName(String name) throws XmlException {
            // validate category names are all unique
            if (!uniqueStatusNames.add(name)) {
                throw new XmlException("duplicate application document status / category name in document type: "+name);
            }
        }

    }
}
