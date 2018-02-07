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
package org.kuali.rice.krad.service.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.OptimisticLockException;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionType;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent;
import org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent;
import org.kuali.rice.kew.framework.postprocessor.DeleteEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentLockingEvent;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange;
import org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange;
import org.kuali.rice.kew.framework.postprocessor.ProcessDocReport;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.PostProcessorService;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * This class is the postProcessor for the Kuali application, and it is responsible for plumbing events up to documents using the
 * built into the document methods for handling route status and other routing changes that take place asyncronously and potentially
 * on a different server.
 */
@Transactional
public class PostProcessorServiceImpl implements PostProcessorService {

    private static Logger LOG = Logger.getLogger(PostProcessorServiceImpl.class);

    private DocumentService documentService;

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteStatusChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteStatusChange)
     */
    @Override
    public ProcessDocReport doRouteStatusChange(final DocumentRouteStatusChange statusChangeEvent) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(),
                new Callable<ProcessDocReport>() {
                    public ProcessDocReport call() throws Exception {

                        try {
                            if (LOG.isInfoEnabled()) {
                                LOG.info(new StringBuffer("started handling route status change from ").append(
                                        statusChangeEvent.getOldRouteStatus()).append(" to ").append(
                                        statusChangeEvent.getNewRouteStatus()).append(" for document ").append(
                                        statusChangeEvent.getDocumentId()));
                            }

                            Document document = documentService.getByDocumentHeaderId(
                                    statusChangeEvent.getDocumentId());
                            if (document == null) {
                                if (!KewApiConstants.ROUTE_HEADER_CANCEL_CD.equals(
                                        statusChangeEvent.getNewRouteStatus())) {
                                    throw new RuntimeException(
                                            "unable to load document " + statusChangeEvent.getDocumentId());
                                }
                            } else {
                                document.doRouteStatusChange(statusChangeEvent);
                                // PLEASE READ BEFORE YOU MODIFY:
                                // we dont want to update the document on a Save, as this will cause an
                                // OptimisticLockException in many cases, because the DB versionNumber will be
                                // incremented one higher than the document in the browser, so when the user then
                                // hits Submit or Save again, the versionNumbers are out of synch, and the
                                // OptimisticLockException is thrown. This is not the optimal solution, and will
                                // be a problem anytime where the user can continue to edit the document after a
                                // workflow state change, without reloading the form.
                                if (!document.getDocumentHeader().getWorkflowDocument().isSaved()) {
                                    documentService.updateDocument(document);
                                }
                            }
                            if (LOG.isInfoEnabled()) {
                                LOG.info(new StringBuffer("finished handling route status change from ").append(
                                        statusChangeEvent.getOldRouteStatus()).append(" to ").append(
                                        statusChangeEvent.getNewRouteStatus()).append(" for document ").append(
                                        statusChangeEvent.getDocumentId()));
                            }
                        } catch (Exception e) {
                            logAndRethrow("route status", e);
                        }
                        return new ProcessDocReport(true, "");
                    }
                });
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doRouteLevelChange(org.kuali.rice.kew.framework.postprocessor.DocumentRouteLevelChange)
     */
    public ProcessDocReport doRouteLevelChange(final DocumentRouteLevelChange levelChangeEvent) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(),
                new Callable<ProcessDocReport>() {
                    public ProcessDocReport call() throws Exception {

                        // on route level change we'll serialize the XML for the document. we
                        // are doing this here cause it's a heavy hitter, and we
                        // want to avoid the user waiting for this during sync processing
                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer("started handling route level change from ").append(
                                        levelChangeEvent.getOldNodeName()).append(" to ").append(
                                        levelChangeEvent.getNewNodeName()).append(" for document ").append(
                                        levelChangeEvent.getDocumentId()));
                            }

                            Document document = documentService.getByDocumentHeaderId(levelChangeEvent.getDocumentId());
                            if (document == null) {
                                throw new RuntimeException(
                                        "unable to load document " + levelChangeEvent.getDocumentId());
                            }
                            document.populateDocumentForRouting();
                            document.doRouteLevelChange(levelChangeEvent);
                            document.getDocumentHeader().getWorkflowDocument().saveDocumentData();
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer("finished handling route level change from ").append(
                                        levelChangeEvent.getOldNodeName()).append(" to ").append(
                                        levelChangeEvent.getNewNodeName()).append(" for document ").append(
                                        levelChangeEvent.getDocumentId()));
                            }
                        } catch (Exception e) {
                            logAndRethrow("route level", e);
                        }
                        return new ProcessDocReport(true, "");
                    }
                });
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doDeleteRouteHeader(org.kuali.rice.kew.framework.postprocessor.DeleteEvent)
     */
    @Override
    public ProcessDocReport doDeleteRouteHeader(DeleteEvent event) throws Exception {
        return new ProcessDocReport(true, "");
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#doActionTaken(org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport doActionTaken(final ActionTakenEvent event) throws Exception {
         return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(), new Callable<ProcessDocReport>() {
            public ProcessDocReport call() throws Exception {
                try {
                    if ( LOG.isDebugEnabled() ) {
                        LOG.debug(new StringBuffer("started doing action taken for action taken code").append(event.getActionTaken().getActionTaken()).append(" for document ").append(event.getDocumentId()));
                    }
                    Document document = documentService.getByDocumentHeaderId(event.getDocumentId());
                    if (ObjectUtils.isNull(document)) {
                        // only throw an exception if we are not cancelling
                        if (!KewApiConstants.ACTION_TAKEN_CANCELED.equals(event.getActionTaken())) {
                            LOG.warn("doActionTaken() Unable to load document with id " + event.getDocumentId() +
                                    " using action taken code '" + KewApiConstants.ACTION_TAKEN_CD.get(event.getActionTaken().getActionTaken()));
        //                    throw new RuntimeException("unable to load document " + event.getDocumentId());
                        }
                    } else {
                        document.doActionTaken(event);
                        if ( LOG.isDebugEnabled() ) {
                            LOG.debug(new StringBuffer("finished doing action taken for action taken code").append(event.getActionTaken().getActionTaken()).append(" for document ").append(event.getDocumentId()));
                        }
                    }
                }
                catch (Exception e) {
                    logAndRethrow("do action taken", e);
                }
                return new ProcessDocReport(true, "");

            }
        });
    }

    /**
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterActionTaken(org.kuali.rice.kew.api.action.ActionType, org.kuali.rice.kew.framework.postprocessor.ActionTakenEvent)
     */
    @Override
    public ProcessDocReport afterActionTaken(final ActionType performed, final ActionTakenEvent event) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(), new Callable<ProcessDocReport>() {
            public ProcessDocReport call() throws Exception {
                try {
                    if ( LOG.isDebugEnabled() ) {
                        LOG.debug(new StringBuffer("started doing after action taken for action performed code " + performed.getCode() + " and action taken code ").append(event.getActionTaken().getActionTaken()).append(" for document ").append(event.getDocumentId()));
                    }
                    Document document = documentService.getByDocumentHeaderId(event.getDocumentId());
                    if (ObjectUtils.isNull(document)) {
                        // only throw an exception if we are not cancelling
                        if (!KewApiConstants.ACTION_TAKEN_CANCELED.equals(event.getActionTaken())) {
                            LOG.warn("afterActionTaken() Unable to load document with id " + event.getDocumentId() +
                                    " using action taken code '" + KewApiConstants.ACTION_TAKEN_CD.get(event.getActionTaken().getActionTaken()));
                            //                    throw new RuntimeException("unable to load document " + event.getDocumentId());
                        }
                    } else {
                        document.afterActionTaken(performed, event);
                        if ( LOG.isDebugEnabled() ) {
                            LOG.debug(new StringBuffer("finished doing after action taken for action taken code").append(event.getActionTaken().getActionTaken()).append(" for document ").append(event.getDocumentId()));
                        }
                    }
                }
                catch (Exception e) {
                    logAndRethrow("do action taken", e);
                }
                return new ProcessDocReport(true, "");

            }
        });
    }

    /**
     * This method first checks to see if the document can be retrieved by the {@link DocumentService}. If the document is
     * found the {@link Document#afterWorkflowEngineProcess(boolean)} method will be invoked on it
     *
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#afterProcess(org.kuali.rice.kew.framework.postprocessor.AfterProcessEvent)
     */
    @Override
    public ProcessDocReport afterProcess(final AfterProcessEvent event) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(),
                new Callable<ProcessDocReport>() {
                    public ProcessDocReport call() throws Exception {

                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer("started after process method for document ").append(
                                        event.getDocumentId()));
                            }

                            Document document = documentService.getByDocumentHeaderId(event.getDocumentId());
                            if (ObjectUtils.isNull(document)) {
                                // no way to verify if this is the processing as a result of a cancel so assume null document is ok to process
                                LOG.warn("afterProcess() Unable to load document with id "
                                        + event.getDocumentId()
                                        + "... ignoring post processing");
                            } else {
                                document.afterWorkflowEngineProcess(event.isSuccessfullyProcessed());
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(new StringBuffer("finished after process method for document ").append(
                                            event.getDocumentId()));
                                }
                            }
                        } catch (Exception e) {
                            logAndRethrow("after process", e);
                        }
                        return new ProcessDocReport(true, "");
                    }
                });
    }

    /**
     * This method first checks to see if the document can be retrieved by the {@link DocumentService}. If the document is
     * found the {@link Document#beforeWorkflowEngineProcess()} method will be invoked on it
     *
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#beforeProcess(org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent)
     */
    @Override
    public ProcessDocReport beforeProcess(final BeforeProcessEvent event) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(),
                new Callable<ProcessDocReport>() {
                    public ProcessDocReport call() throws Exception {

                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer("started before process method for document ").append(
                                        event.getDocumentId()));
                            }
                            Document document = documentService.getByDocumentHeaderId(event.getDocumentId());
                            if (ObjectUtils.isNull(document)) {
                                // no way to verify if this is the processing as a result of a cancel so assume null document is ok to process
                                LOG.warn("beforeProcess() Unable to load document with id "
                                        + event.getDocumentId()
                                        + "... ignoring post processing");
                            } else {
                                document.beforeWorkflowEngineProcess();
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(new StringBuffer("finished before process method for document ").append(
                                            event.getDocumentId()));
                                }
                            }
                        } catch (Exception e) {
                            logAndRethrow("before process", e);
                        }
                        return new ProcessDocReport(true, "");
                    }
                });
    }

    /**
     * This method first checks to see if the document can be retrieved by the {@link DocumentService}. If the document is
     * found the {@link Document#beforeWorkflowEngineProcess()} method will be invoked on it
     *
     * @see org.kuali.rice.kew.framework.postprocessor.PostProcessor#beforeProcess(org.kuali.rice.kew.framework.postprocessor.BeforeProcessEvent)
     */
    public List<String> getDocumentIdsToLock(final DocumentLockingEvent event) throws Exception {
        return GlobalVariables.doInNewGlobalVariables(establishPostProcessorUserSession(),
                new Callable<List<String>>() {
                    public List<String> call() throws Exception {

                        try {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(new StringBuffer("started get document ids to lock method for document ")
                                        .append(event.getDocumentId()));
                            }
                            Document document = documentService.getByDocumentHeaderId(event.getDocumentId());
                            if (ObjectUtils.isNull(document)) {
                                // no way to verify if this is the processing as a result of a cancel so assume null document is ok to process
                                LOG.warn("getDocumentIdsToLock() Unable to load document with id "
                                        + event.getDocumentId()
                                        + "... ignoring post processing");
                            } else {
                                List<String> documentIdsToLock = document.getWorkflowEngineDocumentIdsToLock();
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(new StringBuffer("finished get document ids to lock method for document ")
                                            .append(event.getDocumentId()));
                                }
                                if (documentIdsToLock == null) {
                                    return null;
                                }
                                return documentIdsToLock;
                            }
                        } catch (Exception e) {
                            logAndRethrow("before process", e);
                        }
                        return null;
                    }
                });
    }

    private void logAndRethrow(String changeType, Exception e) throws RuntimeException {
        LOG.error("caught exception while handling " + changeType + " change", e);
        logOptimisticDetails(5, e);

        throw new RuntimeException("post processor caught exception while handling " + changeType + " change: " + e.getMessage(), e);
    }

    /**
     * Logs further details of OptimisticLockExceptions, using the given depth value to limit recursion Just In Case
     *
     * @param depth
     * @param t
     */
    private void logOptimisticDetails(int depth, Throwable t) {
        if ((depth > 0) && (t != null)) {
            if (t instanceof OptimisticLockException) {
                OptimisticLockException o = (OptimisticLockException) t;

                LOG.error("source of OptimisticLockException = " + o.getSourceObject().getClass().getName() + " ::= " + o.getSourceObject());
            }
            else {
                Throwable cause = t.getCause();
                if (cause != t) {
                    logOptimisticDetails(--depth, cause);
                }
            }
        }
    }

    /**
     * Sets the documentService attribute value.
     * @param documentService The documentService to set.
     */
    public final void setDocumentService(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Establishes the UserSession if one does not already exist.
     */
    protected UserSession establishPostProcessorUserSession() throws WorkflowException {
       if (GlobalVariables.getUserSession() == null) {
            return new UserSession(KRADConstants.SYSTEM_USER);
        } else {
            return GlobalVariables.getUserSession();
        }
    }
}
