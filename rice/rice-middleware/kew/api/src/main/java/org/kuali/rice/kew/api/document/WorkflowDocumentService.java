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
package org.kuali.rice.kew.api.document;

import org.joda.time.DateTime;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.util.jaxb.DateTimeAdapter;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionRequest;
import org.kuali.rice.kew.api.action.ActionTaken;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.document.node.RouteNodeInstance;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * TODO ... annotate for JAX-WS! 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = "workflowDocumentService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WorkflowDocumentService {

    /**
     * Gets a {@link Document} from a documentId.
     *
     * <p>
     *   This method will return null if the Document does not exist.
     * </p>
     *
     * @param documentId the unique id of the document to return
     * @return the document with the passed in id value
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocument")
    @WebResult(name = "document")
	Document getDocument(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;

    /**
     * Returns a boolean depending on if a {@link Document} exists with the specified documentId
     *
     * <p>
     *   This method will return false if the responsibility does not exist.
     * </p>
     *
     * @param documentId the unique id of the document to check for existence
     * @return boolean value representative of whether the document exists
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "doesDocumentExist")
    @WebResult(name = "document")
	boolean doesDocumentExist(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets {@link DocumentContent} from a documentId.
     *
     * <p>
     *   This method will return null if the document does not exist.
     * </p>
     *
     * @param documentId the unique id of the document content to return
     * @return the documentContent with the passed in id value
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocumentContent")
    @WebResult(name = "documentContent")
	DocumentContent getDocumentContent(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of root ActionRequests for a given documentId
     *
     * @param documentId the unique id of a document
     *
     * @return the list of root ActionRequests for a given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getRootActionRequests")
    @XmlElementWrapper(name = "rootActionRequests", required = true)
    @XmlElement(name = "rootActionRequest", required = false)
    @WebResult(name = "rootActionRequests")
	List<ActionRequest> getRootActionRequests(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;
    
    /**
     * Gets a list of ActionRequests which are pending for a given documentId
     * 
     * @since 2.1
     * @param documentId the unique id of a document
     * @return the list of pending ActionRequests for a given documentId
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getPendingActionRequests")
    @XmlElementWrapper(name = "pendingActionRequests", required = true)
    @XmlElement(name = "pendingActionRequest", required = false)
    @WebResult(name = "pendingActionRequests")
    List<ActionRequest> getPendingActionRequests(String documentId);

    /**
     * Gets a list of ActionRequests for a given documentId, nodeName and principalId
     *
     * @param documentId the unique id of a document
     * @param nodeName the name of a RouteNode
     * @param principalId the unique id of a principal
     *
     * @return the list of ActionRequests for a given documentId, nodeName, and principalId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getActionRequestsForPrincipalAtNode")
    @XmlElementWrapper(name = "actionRequests", required = true)
    @XmlElement(name = "actionRequests", required = false)
    @WebResult(name = "actionRequests")
	List<ActionRequest> getActionRequestsForPrincipalAtNode(@WebParam(name = "documentId") String documentId,
            @WebParam(name = "nodeName") String nodeName, @WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of past {@link ActionTaken} of a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a document
     *
     * @return the list of past ActionTakens for a given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getActionsTaken")
    @XmlElementWrapper(name = "actionsTaken", required = true)
    @XmlElement(name = "actionTaken", required = false)
    @WebResult(name = "actionsTaken")
	List<ActionTaken> getActionsTaken(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * @deprecated mistaken operation name...use getActionsTaken instead
     *
     * @param documentId the unique id of a document
     *
     * @return the list of past ActionTakens for a given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getActionRequests")
    @XmlElementWrapper(name = "actionsTaken", required = true)
    @XmlElement(name = "actionTaken", required = false)
    @WebResult(name = "actionsTaken")
    @Deprecated
    List<ActionTaken> _getActionsTaken(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;


    /**
    * Gets a list of all {@link ActionTaken} of a {@link Document} with the given documentId
    *
    * @since 2.0.2
    *
    * @param documentId the unique id of a document
    *
    * @return the list of ActionTakens (both current and not) for a given documentId
    *
    * @throws RiceIllegalArgumentException if {@code documentId} is null
    */
      @WebMethod(operationName = "getAllActionRequests")
      @XmlElementWrapper(name = "actionsTaken", required = true)
      @XmlElement(name = "actionTaken", required = false)
      @WebResult(name = "actionsTaken")
      List<ActionTaken> getAllActionsTaken(@WebParam(name = "documentId") String documentId)
              throws RiceIllegalArgumentException;


    /**
     * Gets a {@link DocumentDetail} of a {@link Document} with the given documentTypeName and appId
     *
     * @param documentTypeName the name of the DocumentType
     * @param appId the unique id of the application
     *
     * @return a {@link DocumentDetail} for with the given documentTypeName and appId
     *
     * @throws RiceIllegalArgumentException if {@code documentTypeName} is null
     * @throws RiceIllegalArgumentException if {@code appId} is null
     */
    @WebMethod(operationName = "getDocumentDetailByAppId")
    @WebResult(name = "documentDetail")
    DocumentDetail getDocumentDetailByAppId(@WebParam(name = "documentTypeName") String documentTypeName,
                                            @WebParam(name = "appId") String appId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a {@link DocumentDetail} of a {@link Document} with the given documentId
     *
     * @param documentId the unique id of the Document
     *
     * @return a {@link DocumentDetail} for with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocumentDetail")
    @WebResult(name = "documentDetail")
	DocumentDetail getDocumentDetail(@WebParam(name = "documentId") String documentId);


    /**
     * Gets a {@link RouteNodeInstance} with the given nodeInstanceId
     *
     * @param routeNodeInstanceId the unique id of the {@link RouteNodeInstance}
     *
     * @return a {@link DocumentDetail} for with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code nodeInstanceId} is null
     */
    @WebMethod(operationName = "getRouteNodeInstance")
    @WebResult(name = "routeNodeInstance")
	RouteNodeInstance getRouteNodeInstance(@WebParam(name = "routeNodeInstanceId") String routeNodeInstanceId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a value application document id of a {@link Document} with the given documentId
     *
     * @param documentId the unique id of the Document
     *
     * @return the value of the applicationDocumentId for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getApplicationDocumentId")
    @WebResult(name = "applicationDocumentId")
	String getApplicationDocumentId(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a value application document status of a {@link org.kuali.rice.kew.api.document.Document} with the given documentId
     *
     * @param documentId the unique id of the Document
     *
     * @return the value of the applicationDocumentStatus for the {@link org.kuali.rice.kew.api.document.Document} with the given documentId
     *
     * @throws org.kuali.rice.core.api.exception.RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getApplicationDocumentStatus")
    @WebResult(name = "applicationDocumentStatus")
    String getApplicationDocumentStatus(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Executes a search for workflow documents using the given criteria and as the principal with the given id.  Since
     * documents can define security which permits access to view certain search results, the given principal id will
     * be used when evaluating which documents should be filtered from the results because of lack of access.
     *
     * @param principalId the id of the principal to execute the search as, if this value is non-null then security
     * filtering will be executed against the results, if it is null then no filtering will be performed
     * @param criteria the criteria to use when executing the search
     *
     * @return the results of the search, this will never be null but may contain an empty list of results
     *
     * @throws RiceIllegalArgumentException if the given criteria is null
     */
    @WebMethod(operationName = "documentSearch")
    @WebResult(name = "documentSearchResults")
    DocumentSearchResults documentSearch(
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "criteria") DocumentSearchCriteria criteria)
        throws RiceIllegalArgumentException;

    /**
     * Gets a list of all {@link RouteNodeInstance} for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the list of {@link RouteNodeInstance}s for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getRouteNodeInstances")
    @XmlElementWrapper(name = "routeNodeInstances", required = true)
    @XmlElement(name = "routeNodeInstance", required = false)
    @WebResult(name = "routeNodeInstances")
	List<RouteNodeInstance> getRouteNodeInstances(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of active {@link RouteNodeInstance} for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the list of active {@link RouteNodeInstance}s for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getActiveRouteNodeInstances")
    @XmlElementWrapper(name = "routeNodeInstances", required = true)
    @XmlElement(name = "routeNodeInstance", required = false)
    @WebResult(name = "routeNodeInstances")
	List<RouteNodeInstance> getActiveRouteNodeInstances(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of terminal {@link RouteNodeInstance}s for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the list of terminal {@link RouteNodeInstance}s for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getTerminalRouteNodeInstances")
    @XmlElementWrapper(name = "routeNodeInstances", required = true)
    @XmlElement(name = "routeNodeInstance", required = false)
    @WebResult(name = "routeNodeInstances")
	List<RouteNodeInstance> getTerminalRouteNodeInstances(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of current {@link RouteNodeInstance}s for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the list of current {@link RouteNodeInstance}s for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getCurrentRouteNodeInstances")
    @XmlElementWrapper(name = "routeNodeInstances", required = true)
    @XmlElement(name = "routeNodeInstance", required = false)
    @WebResult(name = "routeNodeInstances")
	List<RouteNodeInstance> getCurrentRouteNodeInstances(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of all previous {@link RouteNodeInstance}'s node names for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the list of all previous {@link RouteNodeInstance}'s node names for the {@link Document} with the
     * given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getPreviousRouteNodeNames")
    @XmlElementWrapper(name = "previousRouteNodeNames", required = true)
    @XmlElement(name = "previousRouteNodeName", required = false)
    @WebResult(name = "previousRouteNodeNames")
	List<String> getPreviousRouteNodeNames(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

	/**
     * Gets the status value for a {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the current status of the {@link Document} with the
     * given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocumentStatus")
    @WebResult(name = "documentStatus")
	DocumentStatus getDocumentStatus(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;


    /**
     * Gets a list of principalId values for a {@link Document} with the given documentId
     * and action request code that have pending action requests
     *
     * @param actionRequestedCd code for the pending action requested
     * @param documentId the unique id of a Document
     *
     * @return a list of principalIds for the {@link Document} with the
     * given parameters and have a pending action request
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code actionRequestCd} is null
     */
    @WebMethod(operationName = "getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId")
    @XmlElementWrapper(name = "principalIds", required = true)
    @XmlElement(name = "principalId", required = false)
    @WebResult(name = "principalIds")
	List<String> getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(
                    @WebParam(name = "actionRequestedCd") String actionRequestedCd,
		            @WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;


    /**
     * Gets the {@link Document} initiator's principalId with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return the {@link Document} initiator's principalId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocumentInitiatorPrincipalId")
    @WebResult(name = "principalId")
	String getDocumentInitiatorPrincipalId(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Gets the {@link Document}'s 'routed by' principalId with the given documentId
     * Returns null if the document is not found
     *
     * @param documentId the unique id of a Document
     *
     * @return the {@link Document}'s 'routed by' principalId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getRoutedByPrincipalIdByDocumentId")
    @WebResult(name = "principalId")
	String getRoutedByPrincipalIdByDocumentId(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;

    /**
     * Does a direct search for searchableAttributes without going through the document search
     * This returns a list of String values for String searchableAttributes
     *
     * @param documentId the unique id of a Document
     * @param key the searchableAttributes key value
     *
     * @return a list of String values for the {@link Document} with the
     * given documentId and searchable attribute key
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code key} is null
     */
    @WebMethod(operationName = "getSearchableAttributeStringValuesByKey")
    @XmlElementWrapper(name = "searchableAttributeStringValues", required = true)
    @XmlElement(name = "searchableAttributeStringValue", required = false)
    @WebResult(name = "searchableAttributeStringValues")
	List<String> getSearchableAttributeStringValuesByKey(@WebParam(name = "documentId") String documentId,
			                                             @WebParam(name = "key") String key)
            throws RiceIllegalArgumentException;

	/**
     * Does a direct search for searchableAttributes without going through the document search
     * This returns a list of DateTime values for date/time searchableAttributes
     *
     * @param documentId the unique id of a Document
     * @param key the searchableAttributes key value
     *
     * @return a list of DateTime values for the {@link Document} with the
     * given documentId and searchable attribute key
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code key} is null
     */
    @WebMethod(operationName = "getSearchableAttributeDateTimeValuesByKey")
    @XmlElementWrapper(name = "searchableAttributeDateTimeValues", required = true)
    @XmlElement(name = "searchableAttributeDateTimeValue", required = false)
    @WebResult(name = "searchableAttributeDateTimeValues")
	@XmlJavaTypeAdapter(value = DateTimeAdapter.class)
	List<DateTime> getSearchableAttributeDateTimeValuesByKey(@WebParam(name = "documentId") String documentId,
			                                                 @WebParam(name = "key") String key)
            throws RiceIllegalArgumentException;

	/**
     * Does a direct search for searchableAttributes without going through the document search
     * This returns a list of BigDecimal values for decimal searchableAttributes
     *
     * @param documentId the unique id of a Document
     * @param key the searchableAttributes key value
     *
     * @return a list of BigDecimal values for the {@link Document} with the
     * given documentId and searchable attribute key
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code key} is null
     */
    @WebMethod(operationName = "getSearchableAttributeFloatValuesByKey")
    @XmlElementWrapper(name = "searchableAttributeBigDecimalValues", required = true)
    @XmlElement(name = "searchableAttributeBigDecimalValue", required = false)
    @WebResult(name = "searchableAttributeBigDecimalValues")
	List<BigDecimal> getSearchableAttributeFloatValuesByKey(@WebParam(name = "documentId") String documentId,
			                                                @WebParam(name = "key") String key)
            throws RiceIllegalArgumentException;

	/**
     * Does a direct search for searchableAttributes without going through the document search
     * This returns a list of Long values for long searchableAttributes
     *
     * @param documentId the unique id of a Document
     * @param key the searchableAttributes key value
     *
     * @return a list of BigDecimal values for the {@link Document} with the
     * given documentId and searchable attribute key
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code key} is null
     */
    @WebMethod(operationName = "getSearchableAttributeLongValuesByKey")
    @XmlElementWrapper(name = "searchableAttributeLongValues", required = true)
    @XmlElement(name = "searchableAttributeLongValue", required = false)
    @WebResult(name = "searchableAttributeLongValues")
	List<Long> getSearchableAttributeLongValuesByKey(@WebParam(name = "documentId") String documentId,
			                                         @WebParam(name = "key") String key)
            throws RiceIllegalArgumentException;

    /**
     * Gets a list of DocumentStatusTransitions for the {@link Document} with the given documentId
     *
     * @param documentId the unique id of a Document
     *
     * @return a list of DocumentStatusTransitions for the {@link Document} with the
     * given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getDocumentStatusTransitionHistory")
    @XmlElementWrapper(name = "documentStatusTransitions", required = true)
    @XmlElement(name = "documentStatusTransition", required = false)
    @WebResult(name = "documentStatusTransitions")
	List<DocumentStatusTransition> getDocumentStatusTransitionHistory(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;


    /**
     * Saves the passed in {@link DocumentLink}.  If the {@link DocumentLink}'s id field is created.  This method
     * actually creates two different links in the database (one from the document being
	 * linked to the target and vice-versa).  If the links already exist, then the call is ignored.
     *
     * @param documentLink the unique id of a Document
     *
     * @return the newly saved {@link DocumentLink}
     *
     * @throws RiceIllegalArgumentException if {@code documentLink} is null
     * @throws RiceIllegalArgumentException if {@code documentLink}'s is id value is populated
     */
    @WebMethod(operationName = "addDocumentLink")
    @WebResult(name = "documentLink")
	DocumentLink addDocumentLink(@WebParam(name = "documentLink") DocumentLink documentLink) throws RiceIllegalArgumentException;

    /**
     * Removes the  {@link DocumentLink} with the given documentLinkId.
     *
     * @param documentLinkId the unique id of a Document
     *
     * @return the deleted {@link DocumentLink}
     *
     * @throws RiceIllegalArgumentException if {@code documentLink} is null
     * @throws RiceIllegalStateException if no DocumentLink with the passed in{@code documentLink} exist
     */
    @WebMethod(operationName = "deleteDocumentLink")
    @WebResult(name = "documentLink")
	DocumentLink deleteDocumentLink(@WebParam(name = "documentLinkId") String documentLinkId) throws RiceIllegalArgumentException;


    /**
     * Removes all {@link DocumentLink}s for the given {@link Document} with the given originatingDocumentId.
     *
     * @param originatingDocumentId the unique id of the originating Document of the document links to delete
     *
     * @return a list of the deleted {@link DocumentLink}s
     *
     * @throws RiceIllegalArgumentException if {@code documentLink} is null
     */
    @WebMethod(operationName = "deleteDocumentLinksByDocumentId")
    @XmlElementWrapper(name = "documentLinks", required = true)
    @XmlElement(name = "documentLink", required = false)
    @WebResult(name = "documentLinks")
    List<DocumentLink> deleteDocumentLinksByDocumentId(@WebParam(name = "originatingDocumentId") String originatingDocumentId) throws RiceIllegalArgumentException;

    /**
     * Gets a list of all {@link DocumentLink}s for outgoing links from the {@link Document} with the given documentId.
     *
     * @param originatingDocumentId the unique id of the originating Document of the document links to retrieve
     *
     * @return a list of the outgoing {@link DocumentLink}s for the originating document
     *
     * @throws RiceIllegalArgumentException if {@code originatingDocumentId} is null
     */
    @WebMethod(operationName = "getOutgoingDocumentLinks")
    @XmlElementWrapper(name = "documentLinks", required = true)
    @XmlElement(name = "documentLink", required = false)
    @WebResult(name = "documentLinks")
    List<DocumentLink> getOutgoingDocumentLinks(@WebParam(name = "originatingDocumentId") String originatingDocumentId) throws RiceIllegalArgumentException;

    /**
     * Gets a list of all {@link DocumentLink}s for incoming links from the {@link Document} with the given documentId.
     *
     * @param originatingDocumentId the unique id of the incoming Document of the document links to retrieve
     *
     * @return a list of the outgoing {@link DocumentLink}s for the incoming document
     *
     * @throws RiceIllegalArgumentException if {@code originatingDocumentId} is null
     */
    @WebMethod(operationName = "getIncomingDocumentLinks")
    @XmlElementWrapper(name = "documentLinks", required = true)
    @XmlElement(name = "documentLink", required = false)
    @WebResult(name = "documentLinks")
    List<DocumentLink> getIncomingDocumentLinks(@WebParam(name = "originatingDocumentId") String originatingDocumentId) throws RiceIllegalArgumentException;

    /**
     * Gets the {@link DocumentLink} for  with the given documentLinkId.
     *
     * @param documentLinkId the unique id of the {@link DocumentLink} to retrieve
     *
     * @return a {@link DocumentLink} with the passed in documentLinkId
     *
     * @throws RiceIllegalArgumentException if {@code documentLinkId} is null
     */
    @WebMethod(operationName = "getDocumentLink")
    @WebResult(name = "documentLinks")
    DocumentLink getDocumentLink(@WebParam(name = "documentLinkId") String documentLinkId) throws RiceIllegalArgumentException;
    
    /**
     * Gets a list of active route node names for a {@link Document} with the given documentId.   Will never return null but an empty collection to indicate no results.
     *
     * @param documentId the unique id of a Document
     *
     * @return an unmodifiable list of active route node names for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null or blank
     * 
     * @since rice 2.2
     */
    @WebMethod(operationName = "getActiveRouteNodeNames")
    @XmlElementWrapper(name = "nodes", required = true)
    @XmlElement(name = "node", required = false)
    @WebResult(name = "nodes")
    List<String> getActiveRouteNodeNames(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;
    
    /**
     * Gets a list of terminal route node names for a {@link Document} with the given documentId.   Will never return null but an empty collection to indicate no results.
     *
     * @param documentId the unique id of a Document
     *
     * @return an unmodifiable list of terminal route node names for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null or blank
     * 
     * @since rice 2.2
     */
    @WebMethod(operationName = "getTerminalRouteNodeNames")
    @XmlElementWrapper(name = "nodes", required = true)
    @XmlElement(name = "node", required = false)
    @WebResult(name = "nodes")
    List<String> getTerminalRouteNodeNames(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;

    /**
     * Gets a list of current route node names for a {@link Document} with the given documentId.  Will never return null but an empty collection to indicate no results.
     *
     * @param documentId the unique id of a Document
     *
     * @return an unmodifiable list of current route node names for the {@link Document} with the given documentId
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null or blank
     * 
     * @since rice 2.2
     */
    @WebMethod(operationName = "getCurrentRouteNodeNames")
    @XmlElementWrapper(name = "nodes", required = true)
    @XmlElement(name = "node", required = false)
    @WebResult(name = "nodes")
    List<String> getCurrentRouteNodeNames(@WebParam(name = "documentId") String documentId) throws RiceIllegalArgumentException;
}
