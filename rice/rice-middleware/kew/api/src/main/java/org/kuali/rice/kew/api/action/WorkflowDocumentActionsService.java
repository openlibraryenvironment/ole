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
package org.kuali.rice.kew.api.action;

import java.util.List;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kew.api.doctype.IllegalDocumentTypeException;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContentUpdate;
import org.kuali.rice.kew.api.document.DocumentDetail;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentUpdate;
import org.kuali.rice.kew.api.document.InvalidDocumentContentException;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.WorkflowDocumentService;

/**
 * This service defines various operations which are used to perform actions against a workflow
 * {@link Document}. These actions include creation, routing, approval, acknowledgment, saving,
 * updating document data, etc.
 * 
 * <p>
 * It also includes operations that allow for loading of information about which actions a given
 * principal is permitted to execute against a given document ({@link ValidActions}), as well as
 * providing information about what actions a particular principal has been requested to execute
 * against a given document ({@link RequestedActions}).
 * 
 * <p>
 * This service can be used in conjunction with the {@link WorkflowDocumentService} which provides
 * additional operations that relate to documents (but not document actions).
 * 
 * <p>
 * Unless otherwise specified, all parameters to all methods must not be null. If the argument is a
 * string value it must also not be "blank" (either the empty string or a string of only
 * whitespace). In the cases where this is violated, a {@link RiceIllegalArgumentException} will be
 * thrown. Additionally, unless otherwise specified, all methods will return non-null return values.
 * In the case of collections, an empty collection will be returned in preference to a null
 * collection value. All collections which are returned from methods on this service will be
 * unmodifiable collections.
 * 
 * <p>
 * Many of the actions trigger processing by the workflow engine. Unless otherwise specified, any
 * method on this service which performs an action against the document will also submit the
 * document to the workflow engine after performing the action. This may trigger the workflow
 * document to transition to the next node in the workflow process or activate additional action
 * requests depending on what the current state of the active node instance(s) is on the document.
 * 
 * <p>
 * Workflow engine processing may happen either asynchronously or synchronously depending on
 * configuration and capabilities of the back end workflow engine implementation. However,
 * asynchronous operation is suggested for most use cases. This means that when operating in
 * asynchronous mode, after an action is submitted against the document there may be a delay in
 * state changes to the document. For example, if a principal submits an approve against a document
 * that triggers transition to the next node in the workflow process (generating new action requests
 * in the process) those new actions requests will not be represented in the information returned in
 * the {@link DocumentActionResult} result object. Though future invocations of
 * {@link #determineRequestedActions(String, String)} and similar methods may yield such
 * information, though it may do so after an non-deterministic amount of time since the workflow
 * engine makes no guarantees about how quickly it will complete processing. Additionally,
 * asynchronous workflow processing is scheduled in a work queue and therefore it may not be picked
 * up for processing immediately depending on system load and other factors.
 * 
 * <p>
 * If there is an error during asynchronous workflow processing then the document will be put into
 * exception routing (which can be executed in various ways depending on how the document type
 * defines it's exception policy). Regardless of the exact process that gets triggered during
 * exception routing, the end result is a set of {@link ActionRequestType#COMPLETE} requests to
 * those principals who are capable of resolving the exception scenario as well as the document's
 * status being transitioned to {@link DocumentStatus#EXCEPTION}. Once they have resolved any
 * barriers to successful processing of the document, they can take the
 * {@link #complete(DocumentActionParameters)} action against the document in order to satisfy the
 * outstanding exception request(s) and send the document back through the workflow engine.
 * 
 * <p>
 * In contrast, when operating the workflow engine in synchronous mode, processing will happen
 * immediately and control will not be returned to the caller until all outstanding processing has
 * completed. As a result, the information returned in the {@link DocumentActionResult} will always
 * be in a consistent state after each action is performed. When operating in synchronous mode, the
 * process of exception routing does not occur when failures are encountered during workflow engine
 * processing, rather any exceptions that are raised during processing will instead be thrown back
 * to the calling code.
 * 
 * <p>
 * Implementations of this service are required to be thread-safe and should be able to be invoked
 * either locally or remotely.
 * 
 * @see WorkflowDocumentService
 * @see WorkflowDocument
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * 
 */
@WebService(name = "workflowDocumentActionsService", targetNamespace = KewApiConstants.Namespaces.KEW_NAMESPACE_2_0)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WorkflowDocumentActionsService {

    /**
     * Creates a new document instance from the given document type. The initiator of the resulting
     * document will be the same as the initiator that is passed to this method. Optional
     * {@link DocumentUpdate} and {@link DocumentContentUpdate} parameters can be supplied in order
     * to create the document with these additional pieces of data already set.
     * 
     * <p>
     * By default, if neither the {@link DocumentUpdate} or {@link DocumentContentUpdate} is passed
     * to this method, the document that is created and returned from this operation will have the
     * following initial state:
     * 
     * <ul>
     * <ol>
     * {@code status} set to {@link DocumentStatus#INITIATED}
     * </ol>
     * <ol>
     * {@code createDate} and {@code dateLastModified} set to the current date and time
     * </ol>
     * <ol>
     * {@code current} set to 'true'
     * </ol>
     * <ol>
     * {@code documentContent} set to the default and empty content
     * </ol>
     * </ul>
     * 
     * <p>
     * Additionally, the initial {@link org.kuali.rice.kew.api.document.node.RouteNodeInstance} for the workflow process on the document
     * will be created and linked to the document as it's initial node. Once the document is
     * created, the {@link #route(DocumentActionParameters)} operation must be invoked in order to
     * submit it to the workflow engine for initial processing.
     * 
     * <p>
     * In certain situations, the given principal may not be permitted to initiate documents of the
     * given type. In these cases an {@link InvalidActionTakenException} will be thrown.
     * 
     * @param documentTypeName the name of the document type from which to create this document
     * @param initiatorPrincipalId the id of the principal who is initiating this document
     * @param documentUpdate specifies additional document to set on the document upon creation,
     *        this is optional and if null is passed then the document will be created with the
     *        default document state
     * @param documentContentUpdate defines what the initial document content for the document
     *        should be, this is optional if null is passed then the document will be created with
     *        the default empty document content
     * 
     * @return the document that was created
     * 
     * @throws RiceIllegalArgumentException if {@code principalId} is null or blank
     * @throws RiceIllegalArgumentException if {@code principalId} does not identify a valid
     *         principal
     * @throws RiceIllegalArgumentException if {@code documentTypeName} is null or blank
     * @throws RiceIllegalArgumentException if {@code documentTypeName} does not identify an
     *         existing document type
     * @throws IllegalDocumentTypeException if the specified document type is not active
     * @throws IllegalDocumentTypeException if the specified document type does not support document
     *         creation (in other words, it's a document type that is only used as a parent)
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "create")
    @WebResult(name = "document")
    @XmlElement(name = "document", required = true)
    Document create(
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "initiatorPrincipalId") String initiatorPrincipalId,
            @WebParam(name = "documentUpdate") DocumentUpdate documentUpdate,
            @WebParam(name = "documentContentUpdate") DocumentContentUpdate documentContentUpdate)
            throws RiceIllegalArgumentException, IllegalDocumentTypeException, InvalidActionTakenException;

    /**
     * Determines which actions against the document with the given id are valid for the principal
     * with the given id.
     * 
     * @param documentId the id of the document for which to determine valid actions
     * @param principalId the id of the principal for which to determine valid actions against the
     *        given document
     * 
     * @return a {@link ValidActions} object which contains the valid actions for the given
     *         principal against the given document
     * 
     * @throws RiceIllegalArgumentException if {@code documentId} is null or blank
     * @throws RiceIllegalArgumentException if document with the given {@code documentId} does not
     *         exist
     * @throws RiceIllegalArgumentException if {@code principalId} is null or blank
     * @throws RiceIllegalArgumentException if principal with the given {@code principalId} does not
     *         exist
     */
    @WebMethod(operationName = "determineValidActions")
    @WebResult(name = "validActions")
    @XmlElement(name = "validActions", required = true)
    ValidActions determineValidActions(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;

    /**
     * Determines which actions are requested against the document with the given id for the
     * principal with the given id. These are generally derived based on action requests that are
     * currently pending against the document.
     * 
     * <p>
     * This method is distinguished from {@link #determineValidActions(String, String)} in that fact
     * that valid actions are the actions that the principal is permitted to take, while requested
     * actions are those that the principal is specifically being asked to take. Note that the
     * actions that are requested are used when assembling valid actions but are not necessarily the
     * only authoritative source for determination of valid actions for the principal, as
     * permissions and other configuration can also come into play.
     * 
     * @param documentId the id of the document for which to determine requested actions
     * @param principalId the id of the principal for which to determine requested actions against
     *        the given document
     * 
     * @return a {@link RequestedActions} object which contains the actions that are being requested
     *         from the given principal against the given document
     * 
     * @throws RiceIllegalArgumentException if {@code documentId} is null or blank
     * @throws RiceIllegalArgumentException if document with the given {@code documentId} does not
     *         exist
     * @throws RiceIllegalArgumentException if {@code principalId} is null or blank
     * @throws RiceIllegalArgumentException if principal with the given {@code principalId} does not
     *         exist
     */
    @WebMethod(operationName = "determineRequestedActions")
    @WebResult(name = "requestedActions")
    @XmlElement(name = "requestedActions", required = true)
    RequestedActions determineRequestedActions(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;

    /**
     * Executes an {@link ActionType#ACKNOWLEDGE} action for the given principal and document
     * specified in the supplied parameters. When a principal acknowledges a document, any of the
     * principal's pending action requests at or below the acknowledge level (which includes fyi
     * requests as well) will be satisfied by the principal's action. The principal's action should
     * be recorded with the document as an {@link ActionTaken}.
     * 
     * <p>
     * Depending on document type policy, a pending action request at or below the acknowledge level
     * may have to exist on the document in order for the principal to take this action. Otherwise
     * an {@link InvalidActionTakenException} may be thrown. In order to determine if an acknowledge
     * action is valid, the {@link ValidActions} or {@link RequestedActions} for the document can be
     * checked.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "acknowledge")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult acknowledge(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#APPROVE} action for the given principal and document specified
     * in the supplied parameters. When a principal approves a document, any of the principal's
     * pending action requests at or below the approve level (which includes complete, acknowledge,
     * and fyi requests as well) will be satisfied by the principal's action. The principal's action
     * should be recorded with the document as an {@link ActionTaken}.
     * 
     * <p>
     * Depending on document type policy, a pending action request at or below the approve level may
     * have to exist on the document in order for the principal to take this action. Otherwise an
     * {@link InvalidActionTakenException} may be thrown. In order to determine if an approve action
     * is valid, the {@link ValidActions} or {@link RequestedActions} for the document can be
     * checked.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "approve")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult approve(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST} action for the given principal and document
     * specified in the supplied parameters to create an ad hoc action request to the target
     * principal specified in the {@code AdHocToPrincipal}.
     * 
     * <p>
     * Operates as per {@link #adHocToGroup(DocumentActionParameters, AdHocToGroup)} with the
     * exception that this method is used to send an adhoc request to principal instead of a group.
     * 
     * <p>
     * Besides this difference, the same rules that are in play for sending ad hoc requests to group
     * apply for sending ad hoc requests to principals.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param adHocToPrincipal defines various pieces of information that informs what type of ad
     *        hoc request should be created
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code adHocToPrincipal} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     * @throws InvalidActionTakenException if the target principal is not permitted to receive ad
     *         hoc requests on documents of this type
     * @throws InvalidActionTakenException if the specified ad hoc request cannot be generated
     *         because the current state of the document would result in an illegal request being
     *         generated
     * 
     * @see #adHocToGroup(DocumentActionParameters, AdHocToGroup)
     */
    @WebMethod(operationName = "adHocToPrincipal_v2_1_3")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult adHocToPrincipal(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "adHocToPrincipal") AdHocToPrincipal adHocToPrincipal)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;


    /**
     * Executes an {@link ActionType#ADHOC_REQUEST} action for the given principal and document
     * specified in the supplied parameters to create an ad hoc action request to the target
     * principal specified in the {@code AdHocToPrincipal}.
     *
     * <p>
     * Operates as per {@link #adHocToGroup(DocumentActionParameters, AdHocToGroup)} with the
     * exception that this method is used to send an adhoc request to principal instead of a group.
     *
     * <p>
     * Besides this difference, the same rules that are in play for sending ad hoc requests to group
     * apply for sending ad hoc requests to principals.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param adHocToPrincipal defines various pieces of information that informs what type of ad
     *        hoc request should be created
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code adHocToPrincipal} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     * @throws InvalidActionTakenException if the target principal is not permitted to receive ad
     *         hoc requests on documents of this type
     * @throws InvalidActionTakenException if the specified ad hoc request cannot be generated
     *         because the current state of the document would result in an illegal request being
     *         generated
     *
     * @see #adHocToGroup(DocumentActionParameters, AdHocToGroup)
     * @since 2.1.3
     */
    @Deprecated
    @WebMethod(operationName = "adHocToPrincipal")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult adHocToPrincipal(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "adHocToPrincipal") AdHocToPrincipal_v2_1_2 adHocToPrincipal)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST} action for the given group and document
     * specified in the supplied parameters to create an ad hoc action request to the target group
     * specified in the {@code AdHocToGroup}. The {@code AdHocToGroup} contains additional
     * information on how the ad hoc action request should be created, including the type of request
     * to generate, at which node it should generated, etc.
     * 
     * <p>
     * The policy for how ad hoc actions handle request generation and interact with the workflow
     * engine is different depending on the state of the document when the request to generate the
     * ad hoc is submitted. There are also different scenarios under which certain types of ad hoc
     * actions are allowed to be executed (throwing {@link InvalidActionTakenException} in
     * situations where the actions are not permitted). The rules are defined as follows:
     * 
     * <ol>
     * <li>If the status of the document is {@link DocumentStatus#INITIATED} then the action request
     * will be generated with a status of {@link ActionRequestStatus#INITIALIZED} and no processing
     * directives will be submitted to the workflow engine. When the document is routed, these ad
     * hoc requests will get activated</li>
     * <li>If the ad hoc request being created is an {@link ActionRequestType#COMPLETE} or
     * {@link ActionRequestType#APPROVE} and the document is in a "terminal" state (either
     * {@link DocumentStatus#CANCELED}, {@link DocumentStatus#DISAPPROVED},
     * {@link DocumentStatus#PROCESSED}, {@link DocumentStatus#FINAL}) or is in
     * {@link DocumentStatus#EXCEPTION} status, then an {@link InvalidActionTakenException} will be
     * thrown. This is because submitting such an action with a document in that state would result
     * in creation of an illegal action request.</li>
     * <li>If the document is in a "terminal" state (see above for definition) then the request will
     * be immediately (and synchronously) activated.</li>
     * <li>Otherwise, after creating the ad hoc request it will be in the
     * {@link ActionRequestStatus#INITIALIZED} status, and the document will be immediately
     * forwarded to the workflow engine for processing at which point the ad hoc request will
     * activated at the appropriate time.</li>
     * </ol>
     * 
     * <p>
     * Unlink other actions, ad hoc actions don't result in the recording of an {@link ActionTaken}
     * against the document. Instead, only the requested ad hoc {@link ActionRequest} is created.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param adHocToGroup defines various pieces of information that informs what type of ad hoc
     *        request should be created
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code adHocToGroup} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principals i is not allowed to execute
     *         this action
     * @throws InvalidActionTakenException if any of the principals in the target group are not
     *         permitted to receive ad hoc requests on documents of this type
     * @throws InvalidActionTakenException if the specified ad hoc request cannot be generated
     *         because the current state of the document would result in an illegal request being
     *         generated
     */
    @WebMethod(operationName = "adHocToGroup_v2_1_3")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult adHocToGroup(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "adHocToGroup") AdHocToGroup adHocToGroup)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST} action for the given group and document
     * specified in the supplied parameters to create an ad hoc action request to the target group
     * specified in the {@code AdHocToGroup}. The {@code AdHocToGroup} contains additional
     * information on how the ad hoc action request should be created, including the type of request
     * to generate, at which node it should generated, etc.
     *
     * <p>
     * The policy for how ad hoc actions handle request generation and interact with the workflow
     * engine is different depending on the state of the document when the request to generate the
     * ad hoc is submitted. There are also different scenarios under which certain types of ad hoc
     * actions are allowed to be executed (throwing {@link InvalidActionTakenException} in
     * situations where the actions are not permitted). The rules are defined as follows:
     *
     * <ol>
     * <li>If the status of the document is {@link DocumentStatus#INITIATED} then the action request
     * will be generated with a status of {@link ActionRequestStatus#INITIALIZED} and no processing
     * directives will be submitted to the workflow engine. When the document is routed, these ad
     * hoc requests will get activated</li>
     * <li>If the ad hoc request being created is an {@link ActionRequestType#COMPLETE} or
     * {@link ActionRequestType#APPROVE} and the document is in a "terminal" state (either
     * {@link DocumentStatus#CANCELED}, {@link DocumentStatus#DISAPPROVED},
     * {@link DocumentStatus#PROCESSED}, {@link DocumentStatus#FINAL}) or is in
     * {@link DocumentStatus#EXCEPTION} status, then an {@link InvalidActionTakenException} will be
     * thrown. This is because submitting such an action with a document in that state would result
     * in creation of an illegal action request.</li>
     * <li>If the document is in a "terminal" state (see above for definition) then the request will
     * be immediately (and synchronously) activated.</li>
     * <li>Otherwise, after creating the ad hoc request it will be in the
     * {@link ActionRequestStatus#INITIALIZED} status, and the document will be immediately
     * forwarded to the workflow engine for processing at which point the ad hoc request will
     * activated at the appropriate time.</li>
     * </ol>
     *
     * <p>
     * Unlink other actions, ad hoc actions don't result in the recording of an {@link ActionTaken}
     * against the document. Instead, only the requested ad hoc {@link ActionRequest} is created.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param adHocToGroup defines various pieces of information that informs what type of ad hoc
     *        request should be created
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code adHocToGroup} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principals i is not allowed to execute
     *         this action
     * @throws InvalidActionTakenException if any of the principals in the target group are not
     *         permitted to receive ad hoc requests on documents of this type
     * @throws InvalidActionTakenException if the specified ad hoc request cannot be generated
     *         because the current state of the document would result in an illegal request being
     *         generated
     * @since 2.1.3
     */
    @Deprecated
    @WebMethod(operationName = "adHocToGroup")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult adHocToGroup(@WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "adHocToGroup") AdHocToGroup_v2_1_2 adHocToGroup)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST_REVOKE} action for the given principal and
     * document specified in the supplied parameters against the action request with the given id.
     * The process of revoking an ad hoc request simply deactivates the request associating the
     * generated {@link ActionTaken} of the revoke action with the deactivated request (this allows
     * for it to be determined what caused the ad hoc request to be deactivated). As with other
     * actions, this action taken is then recorded with the document.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param actionRequestId the id of the action request to revoke
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code actionRequestId} is null or blank
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     * @throws InvalidActionTakenException if a pending ad hoc request with the given
     *         {@code actionRequestId} does not exist on the specified document, this could mean
     *         that the action request id is invalid, or that the action request has already been
     *         deactivated and is no longer pending
     */
    @WebMethod(operationName = "revokeAdHocRequestById")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult revokeAdHocRequestById(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "actionRequestId") String actionRequestId)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST_REVOKE} action which revokes all pending ad hoc
     * action requests that match the supplied {@link AdHocRevoke} criteria for the given principal
     * and document specified in the supplied parameters. The process of revoking an ad hoc requests
     * simply deactivates all ad hoc requests that match the given {@code AdHocRevoke} criteria,
     * associating the generated {@link ActionTaken} of the revoke action with the deactivated
     * requests (this allows for it to be determined what caused the ad hoc request to be
     * deactivated). As with other actions, this action taken is then recorded with the document.
     * 
     * <p>
     * It's possible that the given ad hoc revoke command will match no action requests on the
     * document, in which case this method will complete successfully but no requests will be
     * deactivated.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param revoke the criteria for matching ad hoc action requests on the specified document that
     *        should be revoked
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if {@code revoke} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "revokeAdHocRequests")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult revokeAdHocRequests(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "revoke") AdHocRevoke revoke)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#ADHOC_REQUEST_REVOKE} action which revokes all pending ad hoc
     * action requests for the given principal and document specified in the supplied parameters.
     * This process of revoking all ad hoc requests will simply deactivate all ad hoc requests on
     * the specified document, associating the generated {@link ActionTaken} of the revoke action
     * with the deactivated requests (this allows for it to be determined what caused the ad hoc
     * request to be deactivated). As with other actions, this action taken is then recorded with
     * the document.
     * 
     * <p>
     * It's possible that the specified document will have no pending adhoc requests, in which case
     * this method will complete successfully but no requests will be deactivated.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "revokeAllAdHocRequests")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult revokeAllAdHocRequests(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes a {@link ActionType#CANCEL} action for the given principal and document specified in
     * the supplied parameters. When a principal cancels a document, all pending action requests on
     * the document are deactivated and the the principal's action will be recorded on the document
     * as an {@link ActionTaken}. Additionally, the document will be (synchronously) transitioned to
     * the {@link DocumentStatus#CANCELED} status.
     * 
     * <p>
     * In order to cancel a document, the principal must have permission to cancel documents of the
     * appropriate type, and one of the following must hold true:
     * 
     * <ol>
     * <li>The document must have a status of {@link DocumentStatus#INITIATED} <strong>or</strong></li>
     * <li>The document must have a status of {@link DocumentStatus#SAVED} <strong>or</strong></li>
     * <li>The principal must have a pending "complete" or "approve" request on the document.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "cancel")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult cancel(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes a {@link ActionType#RECALL} action for the given principal and document specified in
     * the supplied parameters. When a principal cancels a document, all pending action requests on
     * the document are deactivated and the the principal's action will be recorded on the document
     * as an {@link ActionTaken}. Additionally, the document will be (synchronously) transitioned to
     * the {@link DocumentStatus#RECALLED} status.
     *
     * TODO: FILL IN DOCS FOR RECALL ACTION
     * <p>
     * In order to cancel a document, the principal must have permission to cancel documents of the
     * appropriate type, and one of the following must hold true:
     *
     * <ol>
     * <li>The document must have a status of {@link DocumentStatus#INITIATED} <strong>or</strong></li>
     * <li>The document must have a status of {@link DocumentStatus#SAVED} <strong>or</strong></li>
     * <li>The principal must have a pending "complete" or "approve" request on the document.
     *
     * @since 2.1
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param cancel whether to recall & cancel or recall & return to action list
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "recall")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult recall(@WebParam(name = "parameters") DocumentActionParameters parameters,
                                @WebParam(name = "cancel") boolean cancel)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#FYI} action for the given principal and document specified in
     * the supplied parameters. When a principal clears fyis on a document, any of the principal's
     * pending fyis will be satisfied by the principal's action. The principal's action should be
     * recorded with the document as an {@link ActionTaken}.
     * 
     * <p>
     * Depending on document type policy, a pending fyi request may have to exist on the document in
     * order for the principal to take this action. Otherwise an {@link InvalidActionTakenException}
     * may be thrown. In order to determine if an fyi action is valid, the {@link ValidActions} or
     * {@link RequestedActions} for the document can be checked.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "clearFyi")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult clearFyi(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes an {@link ActionType#COMPLETE} action for the given principal and document specified
     * in the supplied parameters. When a principal completes a document, any of the principal's
     * pending action requests at or below the complete level (which includes approve, acknowledge,
     * and fyi requests as well) will be satisfied by the principal's action. The principal's action
     * should be recorded with the document as an {@link ActionTaken}.
     * 
     * <p>
     * Depending on document type policy, a pending action request at or below the complete level
     * may have to exist on the document in order for the principal to take this action. Otherwise
     * an {@link InvalidActionTakenException} may be thrown. In order to determine if an complete
     * action is valid, the {@link ValidActions} or {@link RequestedActions} for the document can be
     * checked.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "complete")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult complete(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Executes a {@link ActionType#DISAPPROVE} action for the given principal and document
     * specified in the supplied parameters. When a principal disapproves a document, all pending
     * action requests on the document are deactivated and the the principal's action will be
     * recorded on the document as an {@link ActionTaken}. Additionally, the document will be
     * (synchronously) transitioned to the {@link DocumentStatus#DISAPPROVED} status.
     * 
     * <p>
     * Depending on document type policy and configuration, notifications may be sent to past
     * approvers of the document. By default, an "acknowledge" request will be sent to each
     * principal who took an "approve" or "complete" action on the document previously.
     * 
     * <p>
     * In order to disapprove a document, the principal must have a pending approve or complete
     * request on the document.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "disapprove")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult disapprove(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Submits a document that is in either the "initiated" or "saved" state to the workflow engine
     * for processing. The route action triggers the beginning of the routing process and
     * (synchronously) switches the status of the document to {@link DocumentStatus#ENROUTE}. It
     * then queues up a request to the workflow engine to process the document.
     * 
     * <p>
     * When the route action is executed, an {@link ActionType#COMPLETE} action is recorded on the
     * document for the principal who executed the route action. At this point in time, any action
     * requests that are currently on the document in an "initialized" state will be activated.
     * Requests of this nature can commonly exist if ad hoc requests have been attached to the
     * document prior to execution of the route action.
     * 
     * <p>
     * By default, the principal who initiated the document is the same principal who must submit
     * the route command. However, a document type policy can be set which will relax this
     * constraint.
     * 
     * <p>
     * The route action should ideally only ever be executed once for a given document. Depending on
     * document type policy, attempting to execute a "route" action against a document which is
     * already enroute or in a terminal state may result in an {@link InvalidActionTakenException}
     * being thrown.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "route")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult route(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a full {@link ActionType#BLANKET_APPROVE} action for the given
     * principal and document specified in the supplied parameters. Blanket approval will
     * orchestrate a document from it's current node all the way to the end of the document's
     * workflow process. During this process, it will automatically act on all "approve" and
     * "complete" requests, effectively bypassing them. When it does this, it will notify the
     * original recipients of these requests by routing acknowledge requests to them.
     * 
     * <p>
     * Blanket approve processing is handled by a special mode of the workflow engine which runs the
     * document through it's full processing lifecycle, ensuring that it makes it's way to the end
     * of it's route path (by bypassing any steps that would cause the process to halt, such as
     * approval requests). Because of this nature, blanket approve processing behavior is governed
     * by the same configuration as the rest of the workflow engine. So depending on whether the
     * engine is configured or synchronous or asynchronous operation, the blanket approve processing
     * will behave in the same manner.
     * 
     * <p>
     * In order to execute a blanket approve operation, the principal must have permissions to do
     * so.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "blanketApprove")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult blanketApprove(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#BLANKET_APPROVE} action which orchestrates the
     * document to the given set of node names for the given principal and document specified in the
     * supplied parameters. This method functions the same as
     * {@link #blanketApprove(DocumentActionParameters)} with the exception that the blanket approve
     * process will be halted once all node names in the given set have been reached.
     * 
     * <p>
     * If null or an empty set is passed for {@code nodeNames} on this method, it's behavior will be
     * equivalent to {@link #blanketApprove(DocumentActionParameters)}.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param nodeNames a set of node names to which to blanket approve the given document
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "blanketApproveToNodes")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult blanketApproveToNodes(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "nodeName") Set<String> nodeNames)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#RETURN_TO_PREVIOUS} action for the given
     * principal and document specified in the supplied parameters. Return a document to a previous
     * node will allow for the document to be pushed back to an earlier node in the process based on
     * the criteria present in the {@link ReturnPoint} that is passed to this method.
     * 
     * <p>
     * The document is synchronously returned to the suggested return point (assuming the desired
     * return point can be identified for the given document), and then the document will be
     * submitted to the engine for further processing (effectively, re-establishing the flow of the
     * document from the target return point).
     * 
     * <p>
     * Return the document to the first node in the document is treated as a special case and,
     * rather then transitioning the document back to the "initiated" status, will route a
     * "complete" request to the initiator of the document. The effectively enacts a return to the
     * document initiator in these cases.
     * 
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * 
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     * 
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "returnToPreviousNode")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult returnToPreviousNode(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "returnPoint") ReturnPoint returnPoint)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;


    /**
     * Triggers the execution of a {@link ActionType#MOVE} action for the given
     * principal and document specified in the supplied parameters. Move a document to a
     * node will allow for the document to be pushed to a different node in the process based on
     * the criteria present in the {@link MovePoint} that is passed to this method.
     *
     * <p />
     * The document is synchronously moved to the suggested move point (assuming the desired
     * move point can be identified for the given document), and then the document will be
     * submitted to the engine for further processing (effectively, re-establishing the flow of the
     * document from the target return point).
     *
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param movePoint the point to move the document
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "move")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult move(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "movePoint") MovePoint movePoint)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#TAKE_GROUP_AUTHORITY} action for the given
     * principal and document specified in the supplied parameters. Takes authority of a group by a
     * member of that group.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param groupId the group id to take authority of
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "takeGroupAuthority")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult takeGroupAuthority(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "groupId") String groupId)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#RELEASE_GROUP_AUTHORITY} action for the given
     * principal and document specified in the supplied parameters. Releases authority of a group by a
     * member of that group.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param groupId the group id to take authority of
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "releaseGroupAuthority")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult releaseGroupAuthority(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "groupId") String groupId)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SAVE} action for the given
     * principal and document specified in the supplied parameters. Saves a document to a
     * at the current point.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "save")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult save(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SAVE} action for the given
     * principal and document specified in the supplied parameters. Saves the current document data for
     * the document.  Note that passing an annotation to this will have no effect because it is not
     * recorded in the route log
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "saveDocumentData")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult saveDocumentData(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Deletes the document.
     *
     * @param documentId the unique id of the document to delete
     * @param principalId
     *
     * @return the document that was removed from the system
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} exists
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "delete")
    @WebResult(name = "document")
    @XmlElement(name = "document", required = true)
    Document delete(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException, InvalidActionTakenException;

    /**
     * Records the non-routed document action. - Checks to make sure the document status
     * allows the action. Records the action.
     *
     * @param documentId the unique id of the document to delete
     * @param principalId
     *
     * @return the document that was removed from the system
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     * @throws RiceIllegalArgumentException if {@code annotation} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} exists
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "logAnnotation")
    void logAnnotation(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "annotation") String annotation)
            throws RiceIllegalArgumentException, InvalidActionTakenException;

    /**
     * Initiates the process of document attribute indexing for the document with the given id.
     * Calling this method does not trigger processing of the workflow engine, though it may occur
     * asynchronously or synchronously depending on configuration of the implementation.
     *
     * @param documentId the unique id of the document for which to initiate indexing
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "initiateIndexing")
    void initiateIndexing(@WebParam(name = "documentId") String documentId)
            throws RiceIllegalArgumentException;


    /**
     * Triggers the execution of a {@link ActionType#SU_BLANKET_APPROVE} action for the given
     * principal and document specified in the supplied parameters. Does a blanket approve for a super user
     * and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserBlanketApprove")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserBlanketApprove(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SU_APPROVE} action for the given
     * principal and document specified in the supplied parameters. Does an approve for a super user
     * on a node and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserNodeApprove")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserNodeApprove(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor,
            @WebParam(name = "nodeName") String nodeName)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SU_APPROVE} action for the given
     * actionRequestId and principal and document specified in the supplied parameters. Does an approve for a super user
     * on a node and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param actionRequestId unique Id of an action request to take action on
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if (@code actionRequestId}
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserTakeRequestedAction")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserTakeRequestedAction(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor,
            @WebParam(name = "actionRequestId") String actionRequestId)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SU_DISAPPROVE} action for the given
     * principal and document specified in the supplied parameters. Does a disapprove for a super user
     * on a node and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserDisapprove")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserDisapprove(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SU_CANCEL} action for the given
     * principal and document specified in the supplied parameters. Does an cancel for a super user
     * on a node and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserCancel")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserCancel(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Triggers the execution of a {@link ActionType#SU_RETURN_TO_PREVIOUS} action for the given
     * principal and document specified in the supplied parameters. Returns the document to the
     * previous node for a super user on a node and runs post-processing depending on {@code executePostProcessor}
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     * @param executePostProcessor boolean value determining if the post-processor should be run or not
     * @param returnPoint point to return to
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "superUserReturnToPreviousNode")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult superUserReturnToPreviousNode(
            @WebParam(name = "parameters") DocumentActionParameters parameters,
            @WebParam(name = "executePostProcessor") boolean executePostProcessor,
            @WebParam(name = "returnPoint") ReturnPoint returnPoint)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Places a document in exception routing or the given principal and document specified in the supplied parameters.
     *
     * @param parameters the parameters which indicate which principal is executing the action
     *        against which document, as well as additional operations to take against the document,
     *        such as updating document data
     *
     * @return the result of executing the action, including a view on the updated state of the
     *         document and related actions
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "placeInExceptionRouting")
    @WebResult(name = "documentActionResult")
    @XmlElement(name = "documentActionResult", required = true)
    DocumentActionResult placeInExceptionRouting(@WebParam(name = "parameters") DocumentActionParameters parameters)
            throws RiceIllegalArgumentException, InvalidDocumentContentException, InvalidActionTakenException;

    /**
     * Validates a workflow attribute definition and returns a list of validation errors
     *
     * @param definition WorkflowAttributeDefinition to validate
     *
     * @return a list of RemotableAttributeErrors caused by validation of the passed in {@code definition}
     *
     * @throws RiceIllegalArgumentException if {@code parameters} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     * @throws InvalidDocumentContentException if the document content on the
     *         {@link DocumentContentUpdate} supplied with the {@code parameters} is invalid.
     * @throws InvalidActionTakenException if the supplied principal is not allowed to execute this
     *         action
     */
    @WebMethod(operationName = "validateWorkflowAttributeDefinition")
    @WebResult(name = "validationErrors")
    @XmlElementWrapper(name = "validationErrors", required = true)
    @XmlElement(name = "validationError", required = true)
    List<RemotableAttributeError> validateWorkflowAttributeDefinition(
            @WebParam(name = "definition") WorkflowAttributeDefinition definition)
            throws RiceIllegalArgumentException;

    // TODO add, annotate, and javadoc the following methods to this service
    /**
     * Determines if a passed in user exists in a document's route log or future route depending on the passed in
     * {@code lookFuture} value
     *
     * @param documentId unique Id of document
     * @param principalId unique Id of Principal to look for in document's route log
     * @param lookFuture boolean value determines whether or not to look at the future route log
     *
     * @return boolean value representing if a principal exists in a Document's route log
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     */
    @WebMethod(operationName = "isUserInRouteLog")
    @WebResult(name = "userInRouteLog")
    boolean isUserInRouteLog(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "lookFuture") boolean lookFuture)
            throws RiceIllegalArgumentException;

    /**
     * Determines if a passed in user exists in a document's route log or future route depending on the passed in
     * {@code lookFuture} value and {@code flattenNodes}
     *
     * @param documentId unique Id of document
     * @param principalId unique Id of Principal to look for in document's route log
     * @param lookFuture boolean value determines whether or not to look at the future route log
     *
     * @return boolean value representing if a principal exists in a Document's route log
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     * @throws RiceIllegalArgumentException if no document with the {@code documentId} specified in
     *         {@code parameters} exists
     * @throws RiceIllegalArgumentException if no principal with the {@code principalId} specified
     *         in {@code parameters} exists
     */
    @WebMethod(operationName = "isUserInRouteLogWithOptionalFlattening")
    @WebResult(name = "userInRouteLogWithOptionalFlattening")
    boolean isUserInRouteLogWithOptionalFlattening(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "lookFuture") boolean lookFuture,
            @WebParam(name = "flattenNodes") boolean flattenNodes)
            throws RiceIllegalArgumentException;

    /**
     * Re-resolves the given role for all documents for the given document type (including children).
     *
     * @param documentTypeName documentTypeName of DocuemntType for role
     * @param roleName name of Role to reresolve
     * @param qualifiedRoleNameLabel qualified role name label
     *
     * @throws RiceIllegalArgumentException if {@code documentTypeName} is null
     * @throws RiceIllegalArgumentException if {@code roleName} is null
     * @throws RiceIllegalArgumentException if {@code qualifiedRoleNameLable} is null
     */
    @WebMethod(operationName = "reResolveRoleByDocTypeName")
    void reResolveRoleByDocTypeName(
            @WebParam(name = "documentTypeName") String documentTypeName,
            @WebParam(name = "roleName") String roleName,
            @WebParam(name = "qualifiedRoleNameLabel") String qualifiedRoleNameLabel)
            throws RiceIllegalArgumentException;

    /**
     * Re-resolves the given role for all documents for the given document id (including children).
     *
     * @param documentId documentId of Docuemnt for role
     * @param roleName name of Role to reresolve
     * @param qualifiedRoleNameLabel qualified role name label
     *
     * @throws RiceIllegalArgumentException if {@code documentTypeName} is null
     * @throws RiceIllegalArgumentException if {@code roleName} is null
     * @throws RiceIllegalArgumentException if {@code qualifiedRoleNameLable} is null
     */
    @WebMethod(operationName = "reResolveRoleByDocumentId")
    void reResolveRoleByDocumentId(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "roleName") String roleName,
            @WebParam(name = "qualifiedRoleNameLabel") String qualifiedRoleNameLabel)
            throws RiceIllegalArgumentException;

    /**
     * Executes a simulation of a document to get all previous and future route information
     *
     * @param reportCriteria criteria for the simulation to follow
     *
     * @return DocumentDetail object representing the results of the simulation
     *
     * @throws RiceIllegalArgumentException if {@code reportCriteria} is null
     */
    @WebMethod(operationName = "executeSimulation")
    @WebResult(name = "documentDetail")
    DocumentDetail executeSimulation(
            @WebParam(name = "reportCriteria") RoutingReportCriteria reportCriteria)
            throws RiceIllegalArgumentException;

    /**
     * Determines if a passed in user is the final approver for a document
     *
     * @param documentId unique Id of the document
     * @param principalId unique Id of Principal to look for in document's route log
     *
     * @return boolean value representing if a principal is the final approver for a document
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     */
    @WebMethod(operationName = "isFinalApprover")
    @WebResult(name = "finalApprover")
    boolean isFinalApprover(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId)
            throws RiceIllegalArgumentException;

    /**
     * Determines if a passed in user is the last approver at a specified route node
     *
     * @param documentId unique Id of the document
     * @param principalId unique Id of Principal to look for in document's route log
     * @param nodeName name of route node to determine last approver for
     *
     * @return boolean value representing if a principal is the last approver at the specified route node
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     * @throws RiceIllegalArgumentException if {@code principalId} is null
     * @throws RiceIllegalArgumentException if {@code nodeName} is null
     */
    @WebMethod(operationName = "isLastApproverAtNode")
    @WebResult(name = "lastApproverAtNode")
    boolean isLastApproverAtNode(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "principalId") String principalId,
            @WebParam(name = "nodeName") String nodeName)
            throws RiceIllegalArgumentException;

    /**
     * Determines if a route node has an 'approve action' request
     *
     * @param docType document type of document
     * @param docContent string representing content of document
     * @param nodeName name of route node to determine if approve action request exists
     *
     * @return boolean value representing if a route node has an 'approve action' request
     *
     * @throws RiceIllegalArgumentException if {@code docType} is null
     * @throws RiceIllegalArgumentException if {@code docContent} is null
     * @throws RiceIllegalArgumentException if {@code nodeName} is null
     */
    @WebMethod(operationName = "routeNodeHasApproverActionRequest")
    @WebResult(name = "routeNodeHasApproverActionRequest")
    boolean routeNodeHasApproverActionRequest(
            @WebParam(name = "docType") String docType,
            @WebParam(name = "docContent") String docContent,
            @WebParam(name = "nodeName") String nodeName)
            throws RiceIllegalArgumentException;

    /**
     * Determines if a document has at least one action request
     *
     * @param reportCriteria criteria for routing report
     * @param actionRequestedCodes list of action request codes to see if they exist for the document
     * @param ignoreCurrentActionRequests boolean value to determine if current action requests should be ignored
     *
     * @return boolean value representing if a document will have at least one action request
     *
     * @throws RiceIllegalArgumentException if {@code docType} is null
     * @throws RiceIllegalArgumentException if {@code docContent} is null
     * @throws RiceIllegalArgumentException if {@code nodeName} is null
     */
    @WebMethod(operationName = "documentWillHaveAtLeastOneActionRequest")
    @WebResult(name = "documentWillHaveAtLeastOneActionRequest")
    boolean documentWillHaveAtLeastOneActionRequest(
            @WebParam(name = "reportCriteria") RoutingReportCriteria reportCriteria,
            @WebParam(name = "actionRequestedCodes") List<String> actionRequestedCodes,
            @WebParam(name = "ignoreCurrentActionRequests") boolean ignoreCurrentActionRequests)
            throws RiceIllegalArgumentException;

    /**
     * Returns a list of principal Ids that exist in a route log
     *
     * @param documentId unique id of the document to get the route log for
     * @param lookFuture boolean value that determines if the method should look at future action requests
     *
     * @return list of principal ids that exist in a route log
     *
     * @throws RiceIllegalArgumentException if {@code documentId} is null
     */
    @WebMethod(operationName = "getPrincipalIdsInRouteLog")
    @WebResult(name = "principalIds")
    @XmlElementWrapper(name = "principalIds", required = true)
    @XmlElement(name = "principalId", required = true)
    List<String> getPrincipalIdsInRouteLog(
            @WebParam(name = "documentId") String documentId,
            @WebParam(name = "lookFuture") boolean lookFuture)
            throws RiceIllegalArgumentException;

}
