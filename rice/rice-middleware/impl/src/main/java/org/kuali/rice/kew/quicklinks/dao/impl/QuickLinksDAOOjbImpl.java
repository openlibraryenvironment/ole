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

import org.apache.ojb.broker.PersistenceBroker;
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
import org.springmodules.orm.ojb.PersistenceBrokerCallback;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


public class QuickLinksDAOOjbImpl extends PersistenceBrokerDaoSupport implements QuickLinksDAO {

    @Override
	public List<ActionListStats> getActionListStats(final String principalId) {
        return (List<ActionListStats>) this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            @Override
			public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement selectActionItems = null;
                PreparedStatement selectDocTypeLabel = null;
                ResultSet selectedActionItems = null;
                ResultSet selectedDocTypeLabel = null;
                List<ActionListStats> docTypes = new ArrayList<ActionListStats>();
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();

                    selectActionItems = connection.prepareStatement("select DOC_TYP_NM, COUNT(*) from KREW_ACTN_ITM_T where PRNCPL_ID = ? " +
                            "and (dlgn_typ is null or dlgn_typ != '" + DelegationType.SECONDARY.getCode() + "') group by DOC_TYP_NM");
                    selectDocTypeLabel = connection.prepareStatement("select LBL from KREW_DOC_TYP_T WHERE DOC_TYP_NM = ? and CUR_IND = 1");
                    selectActionItems.setString(1, principalId);
                    selectedActionItems = selectActionItems.executeQuery();
                    while (selectedActionItems.next()) {
                        String docTypeName = selectedActionItems.getString(1);
                        int count = selectedActionItems.getInt(2);
                        selectDocTypeLabel.setString(1, docTypeName);
                        selectedDocTypeLabel = selectDocTypeLabel.executeQuery();
                        if (selectedDocTypeLabel.next()) {
                            docTypes.add(new ActionListStats(docTypeName, selectedDocTypeLabel.getString(1), count));
                        }
                    }
                    Collections.sort(docTypes);
                    return docTypes;
                } catch (Exception e) {
                    throw new WorkflowRuntimeException("Error getting action list stats for user: " + principalId, e);
                } finally {
                    if (selectActionItems != null) {
                        try {
                            selectActionItems.close();
                        } catch (SQLException e) {
                        }
                    }

                    if (selectDocTypeLabel != null) {
                        try {
                            selectDocTypeLabel.close();
                        } catch (SQLException e) {
                        }
                    }

                    if (selectedActionItems != null) {
                        try {
                            selectedActionItems.close();
                        } catch (SQLException e) {
                        }
                    }

                    if (selectedDocTypeLabel != null) {
                        try {
                            selectedDocTypeLabel.close();
                        } catch (SQLException e) {
                        }
                    }

                }
            }
        });
    }

    @Override
	public List<InitiatedDocumentType> getInitiatedDocumentTypesList(final String principalId) {
        return (List<InitiatedDocumentType>)  this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {

            @Override
			public Object doInPersistenceBroker(PersistenceBroker broker) {
                PreparedStatement selectDistinctDocumentTypes = null;
                ResultSet selectedDistinctDocumentTypes = null;
                List<InitiatedDocumentType> documentTypesByName = new ArrayList<InitiatedDocumentType>();
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();
//                  select the doc type only if the SUPPORTS_QUICK_INITIATE policy is NULL or true
                    String sql = "select distinct B.DOC_TYP_NM, B.LBL from KREW_DOC_HDR_T A, KREW_DOC_TYP_T B "+
                    	"where A.INITR_PRNCPL_ID = ? and A.DOC_TYP_ID = B.DOC_TYP_ID and " +
                    	"B.ACTV_IND = 1 and B.CUR_IND = 1 " +
                    	"order by upper(B.LBL)";

                    selectDistinctDocumentTypes = connection.prepareStatement(sql);
                    selectDistinctDocumentTypes.setString(1, principalId);
                    selectedDistinctDocumentTypes = selectDistinctDocumentTypes.executeQuery();

                    String documentNames = CoreFrameworkServiceLocator.getParameterService().getParameterValueAsString(KewApiConstants.KEW_NAMESPACE, KRADConstants.DetailTypes.QUICK_LINK_DETAIL_TYPE, KewApiConstants.QUICK_LINKS_RESTRICT_DOCUMENT_TYPES);
                    if (documentNames != null) {
                        // TODO Should this happen???
                        documentNames = documentNames.trim();
                    }
                    if (documentNames == null || "none".equals(documentNames)) {
                    	documentNames = "";
                    }

                    List docTypesToRestrict = new ArrayList();
                    StringTokenizer st = new StringTokenizer(documentNames, ",");
                    while (st.hasMoreTokens()) {
                        docTypesToRestrict.add(st.nextToken());
                    }
                    while (selectedDistinctDocumentTypes.next()) {
                        String docTypeName = selectedDistinctDocumentTypes.getString(1);
                        String docTypeTopParent = "";
                        int firstPeriod = docTypeName.indexOf(".");
                        if (firstPeriod == -1) {
                            docTypeTopParent = docTypeName.substring(0);
                        } else {
                            docTypeTopParent = docTypeName.substring(0, firstPeriod);
                        }
                        if (!docTypesToRestrict.contains(docTypeTopParent)) {
                        	// the document types should be cached so this should be pretty quick
                        	DocumentType docType = KEWServiceLocator.getDocumentTypeService().findByName(docTypeName);
                        	DocumentTypePolicy quickInitiatePolicy = docType.getSupportsQuickInitiatePolicy();
                            if (quickInitiatePolicy.getPolicyValue().booleanValue()) {
                            	documentTypesByName.add(new InitiatedDocumentType(docTypeName, selectedDistinctDocumentTypes.getString(2)));
                            }
                        }
                    }
                    return documentTypesByName;
                } catch (Exception e) {
                    throw new WorkflowRuntimeException("Error getting initiated document types for user: " + principalId, e);
                } finally {
                    if (selectDistinctDocumentTypes != null) {
                        try {
                            selectDistinctDocumentTypes.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (selectedDistinctDocumentTypes != null) {
                        try {
                            selectedDistinctDocumentTypes.close();
                        } catch (SQLException e) {
                        }
                    }

                }

            }
        });
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
	public List<WatchedDocument> getWatchedDocuments(final String principalId) {
        return (List<WatchedDocument>) this.getPersistenceBrokerTemplate().execute(new PersistenceBrokerCallback() {
            @Override
			public Object doInPersistenceBroker(PersistenceBroker broker) {
                List<WatchedDocument> watchedDocuments = new ArrayList<WatchedDocument>();
                PreparedStatement selectWatchedDocuments = null;
                ResultSet selectedWatchedDocuments = null;
                try {
                    Connection connection = broker.serviceConnectionManager().getConnection();
                    selectWatchedDocuments = connection.prepareStatement("select DOC_HDR_ID, DOC_HDR_STAT_CD, TTL, CRTE_DT from KREW_DOC_HDR_T where INITR_PRNCPL_ID = ? and DOC_HDR_STAT_CD in ('"+ KewApiConstants.ROUTE_HEADER_ENROUTE_CD +"','"+ KewApiConstants.ROUTE_HEADER_EXCEPTION_CD +"') order by CRTE_DT desc");
                    selectWatchedDocuments.setString(1, principalId);
                    selectedWatchedDocuments = selectWatchedDocuments.executeQuery();
                    while (selectedWatchedDocuments.next()) {
                        watchedDocuments.add(new WatchedDocument(selectedWatchedDocuments.getString(1), KewApiConstants.DOCUMENT_STATUSES.get(selectedWatchedDocuments.getString(2)), selectedWatchedDocuments.getString(3)));
                    }
                    return watchedDocuments;
                } catch (Exception e) {
                    throw new WorkflowRuntimeException("Error getting initiated document types for user: " + principalId, e);
                } finally {
                    if (selectWatchedDocuments != null) {
                        try {
                            selectWatchedDocuments.close();
                        } catch (SQLException e) {
                        }
                    }
                    if (selectedWatchedDocuments != null) {
                        try {
                            selectedWatchedDocuments.close();
                        } catch (SQLException e) {
                        }
                    }

                }
            }
        });
    }

    public DocumentTypeService getDocumentTypeService() {
        return ((DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE));
    }

    public DocumentSearchService getDocumentSearchService() {
        return ((DocumentSearchService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_SEARCH_SERVICE));
    }

}
