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
package org.kuali.rice.kew.quicklinks.dao.impl;

import org.kuali.rice.core.api.delegation.DelegationType;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.docsearch.service.DocumentSearchService;
import org.kuali.rice.kew.doctype.DocumentTypePolicy;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.quicklinks.ActionListStats;
import org.kuali.rice.kew.quicklinks.InitiatedDocumentType;
import org.kuali.rice.kew.quicklinks.WatchedDocument;
import org.kuali.rice.kew.quicklinks.dao.QuickLinksDAO;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.krad.util.KRADConstants;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class QuickLinksDAOJpaImpl implements QuickLinksDAO {

    @PersistenceContext(unitName = "kew-unit")
    private EntityManager entityManager;

    @Override
	@SuppressWarnings("unchecked")
    public List<ActionListStats> getActionListStats(final String principalId) {
        try {
            final List<Object[]> stats = entityManager.createNamedQuery("ActionItem.QuickLinks.FindActionListStatsByPrincipalId").setParameter("principalId", principalId).setParameter("delegationType", DelegationType
                    .SECONDARY.getCode()).getResultList();
            final List<ActionListStats> docTypes = new ArrayList<ActionListStats>(stats.size());
            for (Object[] res : stats) {
                final String docTypeName = (String) res[0];
                final Long count = (Long) res[1];

                final List<String> docTypeLabel = entityManager.createNamedQuery("DocumentType.QuickLinks.FindLabelByTypeName").setParameter("docTypeName", docTypeName).getResultList();
                if (docTypeLabel.size() > 0) {
                    docTypes.add(new ActionListStats(docTypeName, docTypeLabel.get(0), count.intValue()));
                }
            }
            Collections.sort(docTypes);
            return docTypes;
        } catch (Exception e) {
            throw new WorkflowRuntimeException("Error getting action list stats for user: " + principalId, e);
        }
    }

    @Override
	@SuppressWarnings("unchecked")
    public List<InitiatedDocumentType> getInitiatedDocumentTypesList(final String principalId) {
        String documentNames = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.QUICK_LINK_DETAIL_TYPE, KewApiConstants.QUICK_LINKS_RESTRICT_DOCUMENT_TYPES);
        if (documentNames != null) {
            documentNames = documentNames.trim();
        }
        if (documentNames == null || "none".equals(documentNames)) {
            documentNames = "";
        }

        final StringTokenizer st = new StringTokenizer(documentNames, ",");
        final List<String> docTypesToRestrict = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            docTypesToRestrict.add(st.nextToken());
        }

        try {
            final List<Object[]> list = entityManager.createNamedQuery("DocumentType.QuickLinks.FindInitiatedDocumentTypesListByInitiatorWorkflowId").setParameter("initiatorWorkflowId", principalId).getResultList();
            final List<InitiatedDocumentType> documentTypesByName = new ArrayList<InitiatedDocumentType>(list.size());
            for (Object[] doc : list) {
                final String docTypeName = (String) doc[0];
                final String label = (String) doc[1];

                final String docTypeTopParent;
                final int firstPeriod = docTypeName.indexOf(".");
                if (firstPeriod == -1) {
                    docTypeTopParent = docTypeName.substring(0);
                } else {
                    docTypeTopParent = docTypeName.substring(0, firstPeriod);
                }
                if (!docTypesToRestrict.contains(docTypeTopParent)) {
                    // the document types should be cached so this should be pretty quick
                    final DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(docTypeName);
                    final DocumentTypePolicy quickInitiatePolicy = docType.getSupportsQuickInitiatePolicy();
                    if (quickInitiatePolicy.getPolicyValue().booleanValue()) {
                        documentTypesByName.add(new InitiatedDocumentType(docTypeName, label));
                    }
                }
            }
            return documentTypesByName;
        } catch (Exception e) {
            throw new WorkflowRuntimeException("Error getting initiated document types for user: " + principalId, e);
        }
    }

    @Override
	public List<KeyValue> getNamedSearches(String principalId) {
        return getDocumentSearchService().getNamedSearches(principalId);
    }

    @Override
	public List<KeyValue> getRecentSearches(String principalId) {
        return getDocumentSearchService().getMostRecentSearches(principalId);
    }

    @Override
	@SuppressWarnings("unchecked")
    public List<WatchedDocument> getWatchedDocuments(final String principalId) {
        try {
            return entityManager.createNamedQuery("DocumentRouteHeaderValue.QuickLinks.FindWatchedDocumentsByInitiatorWorkflowId").setParameter("initiatorWorkflowId", principalId).getResultList();
        } catch (Exception e) {
            throw new WorkflowRuntimeException("Error getting watched documents for user: " + principalId, e);
        }
    }

    public DocumentTypeService getDocumentTypeService() {
        return ((DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE));
    }

    public DocumentSearchService getDocumentSearchService() {
        return ((DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE));
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
