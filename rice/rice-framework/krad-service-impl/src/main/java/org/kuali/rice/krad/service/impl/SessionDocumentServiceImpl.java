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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.UserSessionUtils;
import org.kuali.rice.krad.bo.SessionDocument;
import org.kuali.rice.krad.dao.SessionDocumentDao;
import org.kuali.rice.krad.datadictionary.DocumentEntry;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.DataDictionaryService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.SessionDocumentService;
import org.kuali.rice.krad.web.form.DocumentFormBase;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of <code>SessionDocumentService</code> that persists the document form
 * contents to the underlying database
 *
 * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Transactional
@Deprecated
public class SessionDocumentServiceImpl implements SessionDocumentService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SessionDocumentServiceImpl.class);

    protected static final String IP_ADDRESS = "ipAddress";
    protected static final String PRINCIPAL_ID = "principalId";
    protected static final String DOCUMENT_NUMBER = "documentNumber";
    protected static final String SESSION_ID = "sessionId";

    private EncryptionService encryptionService;

    private BusinessObjectService businessObjectService;
    private DataDictionaryService dataDictionaryService;
    private SessionDocumentDao sessionDocumentDao;

    /**
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     *
     */
    @Override
    @Deprecated
    public DocumentFormBase getDocumentForm(String documentNumber, String docFormKey, UserSession userSession,
            String ipAddress) {
        DocumentFormBase documentForm = null;

        LOG.debug("getDocumentForm DocumentFormBase from db");
        try {
            // re-create the DocumentFormBase object
            documentForm = (DocumentFormBase) retrieveDocumentForm(userSession, docFormKey, documentNumber, ipAddress);

            //re-store workFlowDocument into session
            WorkflowDocument workflowDocument =
                    documentForm.getDocument().getDocumentHeader().getWorkflowDocument();
            UserSessionUtils.addWorkflowDocument(userSession, workflowDocument);
        } catch (Exception e) {
            LOG.error("getDocumentForm failed for SessId/DocNum/PrinId/IP:" + userSession.getKualiSessionId() + "/" +
                    documentNumber + "/" + userSession.getPrincipalId() + "/" + ipAddress, e);
        }

        return documentForm;
    }

    @Deprecated
    protected Object retrieveDocumentForm(UserSession userSession, String sessionId, String documentNumber,
            String ipAddress) throws Exception {
        HashMap<String, String> primaryKeys = new HashMap<String, String>(4);
        primaryKeys.put(SESSION_ID, sessionId);
        if (documentNumber != null) {
            primaryKeys.put(DOCUMENT_NUMBER, documentNumber);
        }
        primaryKeys.put(PRINCIPAL_ID, userSession.getPrincipalId());
        primaryKeys.put(IP_ADDRESS, ipAddress);

        SessionDocument sessionDoc = getBusinessObjectService().findByPrimaryKey(SessionDocument.class, primaryKeys);
        if (sessionDoc != null) {
            byte[] formAsBytes = sessionDoc.getSerializedDocumentForm();
            if (sessionDoc.isEncrypted()) {
                formAsBytes = getEncryptionService().decryptBytes(formAsBytes);
            }
            ByteArrayInputStream baip = new ByteArrayInputStream(formAsBytes);
            ObjectInputStream ois = new ObjectInputStream(baip);

            return ois.readObject();
        }

        return null;
    }

    /**
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     *
     */
    @Override
    @Deprecated
    public WorkflowDocument getDocumentFromSession(UserSession userSession, String docId) {
        return UserSessionUtils.getWorkflowDocument(userSession, docId);
    }

    /**
     * @see org.kuali.rice.krad.service.SessionDocumentService#addDocumentToUserSession(org.kuali.rice.krad.UserSession,
     *      org.kuali.rice.kew.api.WorkflowDocument)
     *
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)     *
     */
    @Override
    @Deprecated
    public void addDocumentToUserSession(UserSession userSession, WorkflowDocument document) {
        UserSessionUtils.addWorkflowDocument(userSession, document);
    }

    /**
     * @see org.kuali.rice.krad.service.SessionDocumentService#purgeDocumentForm(String, String,
     *      org.kuali.rice.krad.UserSession, String)
     *
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     */
    @Override
    @Deprecated
    public void purgeDocumentForm(String documentNumber, String docFormKey, UserSession userSession, String ipAddress) {
        synchronized (userSession) {
            LOG.debug("purge document form from session");
            userSession.removeObject(docFormKey);
            try {
                LOG.debug("purge document form from database");
                HashMap<String, String> primaryKeys = new HashMap<String, String>(4);
                primaryKeys.put(SESSION_ID, userSession.getKualiSessionId());
                primaryKeys.put(DOCUMENT_NUMBER, documentNumber);
                primaryKeys.put(PRINCIPAL_ID, userSession.getPrincipalId());
                primaryKeys.put(IP_ADDRESS, ipAddress);
                getBusinessObjectService().deleteMatching(SessionDocument.class, primaryKeys);
            } catch (Exception e) {
                LOG.error("purgeDocumentForm failed for SessId/DocNum/PrinId/IP:" + userSession.getKualiSessionId() +
                        "/" + documentNumber + "/" + userSession.getPrincipalId() + "/" + ipAddress, e);
            }
        }
    }

    /**
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     *
     */
    @Override
    @Deprecated
    public void setDocumentForm(DocumentFormBase form, UserSession userSession, String ipAddress) {
        synchronized (userSession) {
            //formKey was set in KualiDocumentActionBase execute method
            String formKey = form.getFormKey();
            String key = userSession.getKualiSessionId() + "-" + formKey;

            String documentNumber = form.getDocument().getDocumentNumber();
            if (StringUtils.isNotBlank(formKey)) {
                //FIXME: Currently using formKey for sessionId
                persistDocumentForm(form, userSession, ipAddress, formKey, documentNumber);
            } else {
                LOG.warn("documentNumber is null on form's document: " + form);
            }
        }
    }

    @Deprecated
    protected void persistDocumentForm(DocumentFormBase form, UserSession userSession, String ipAddress,
            String sessionId, String documentNumber) {
        try {
            LOG.debug("set Document Form into database");

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(form);

            // serialize the DocumentFormBase object into a byte array
            byte[] formAsBytes = baos.toByteArray();
            boolean encryptContent = false;
            DocumentEntry documentEntry =
                    getDataDictionaryService().getDataDictionary().getDocumentEntry(form.getDocTypeName());
            if (documentEntry != null) {
                encryptContent = documentEntry.isEncryptDocumentDataInPersistentSessionStorage();
            }

            if (encryptContent) {
                formAsBytes = getEncryptionService().encryptBytes(formAsBytes);
            }

            // check if a record is already there in the database
            // this may only happen under jMeter testing, but there is no way to be sure
            HashMap<String, String> primaryKeys = new HashMap<String, String>(4);
            primaryKeys.put(SESSION_ID, sessionId);
            primaryKeys.put(DOCUMENT_NUMBER, documentNumber);
            primaryKeys.put(PRINCIPAL_ID, userSession.getPrincipalId());
            primaryKeys.put(IP_ADDRESS, ipAddress);

            SessionDocument sessionDocument =
                    getBusinessObjectService().findByPrimaryKey(SessionDocument.class, primaryKeys);
            if (sessionDocument == null) {
                sessionDocument = new SessionDocument();
                sessionDocument.setSessionId(sessionId);
                sessionDocument.setDocumentNumber(documentNumber);
                sessionDocument.setPrincipalId(userSession.getPrincipalId());
                sessionDocument.setIpAddress(ipAddress);
            }
            sessionDocument.setSerializedDocumentForm(formAsBytes);
            sessionDocument.setEncrypted(encryptContent);
            sessionDocument.setLastUpdatedDate(currentTime);

            businessObjectService.save(sessionDocument);
        } catch (Exception e) {
            final String className = form != null ? form.getClass().getName() : "null";
            LOG.error("setDocumentForm failed for SessId/DocNum/PrinId/IP/class:" + userSession.getKualiSessionId() +
                    "/" + documentNumber + "/" + userSession.getPrincipalId() + "/" + ipAddress + "/" + className, e);
        }
    }

    /**
     * @see org.kuali.rice.krad.service.SessionDocumentService#purgeAllSessionDocuments(java.sql.Timestamp)
     *
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     */
    @Override
    @Deprecated
    public void purgeAllSessionDocuments(Timestamp expirationDate) {
        sessionDocumentDao.purgeAllSessionDocuments(expirationDate);
    }

    @Deprecated
    protected SessionDocumentDao getSessionDocumentDao() {
        return this.sessionDocumentDao;
    }

    /**
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     *
     */
    @Deprecated
    public void setSessionDocumentDao(SessionDocumentDao sessionDocumentDao) {
        this.sessionDocumentDao = sessionDocumentDao;
    }

    @Deprecated
    protected BusinessObjectService getBusinessObjectService() {
        return this.businessObjectService;
    }

    /**
     * @deprecated (Deprecated and removed from use in KRAD  (KULRICE-9149)
     *
     */
    @Deprecated
    public void setBusinessObjectService(BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    @Deprecated
    protected EncryptionService getEncryptionService() {
        if (encryptionService == null) {
            encryptionService = CoreApiServiceLocator.getEncryptionService();
        }
        return encryptionService;
    }


    @Deprecated
    protected DataDictionaryService getDataDictionaryService() {
        if (dataDictionaryService == null) {
            dataDictionaryService = KRADServiceLocatorWeb.getDataDictionaryService();
        }
        return dataDictionaryService;
    }
}
