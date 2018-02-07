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
package org.kuali.rice.kew.impl.document.search;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.framework.persistence.jdbc.sql.Criteria;
import org.kuali.rice.core.framework.persistence.jdbc.sql.SqlBuilder;
import org.kuali.rice.core.framework.persistence.platform.DatabasePlatform;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentStatus;
import org.kuali.rice.kew.api.document.DocumentStatusCategory;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.document.search.DocumentSearchResult;
import org.kuali.rice.kew.api.document.search.DocumentSearchResults;
import org.kuali.rice.kew.api.document.search.RouteNodeLookupLogic;
import org.kuali.rice.kew.docsearch.DocumentSearchInternalUtils;
import org.kuali.rice.kew.docsearch.QueryComponent;
import org.kuali.rice.kew.docsearch.SearchableAttributeValue;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.PerformanceLogger;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.PrincipalContract;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.MessageMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * Reference implementation of the {@code DocumentSearchGenerator}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DocumentSearchGeneratorImpl implements DocumentSearchGenerator {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(DocumentSearchGeneratorImpl.class);

    private static final String ROUTE_NODE_TABLE = "KREW_RTE_NODE_T";
    private static final String ROUTE_NODE_INST_TABLE = "KREW_RTE_NODE_INSTN_T";
    private static final String DATABASE_WILDCARD_CHARACTER_STRING = "%";
    private static final char DATABASE_WILDCARD_CHARACTER = DATABASE_WILDCARD_CHARACTER_STRING.toCharArray()[0];

    private org.kuali.rice.kew.api.doctype.DocumentTypeService apiDocumentTypeService;

    private DatabasePlatform dbPlatform;
    private MessageMap messageMap;

    private SqlBuilder sqlBuilder = null;

    @Override
    public DocumentSearchCriteria clearSearch(DocumentSearchCriteria criteria) {
        return DocumentSearchCriteria.Builder.create().build();
    }

    public DocumentType getValidDocumentType(String documentTypeFullName) {
        if (!org.apache.commons.lang.StringUtils.isEmpty(documentTypeFullName)) {
            DocumentType documentType = KEWServiceLocator.getDocumentTypeService().findByNameCaseInsensitive(documentTypeFullName);
            if (documentType == null) {
                throw new RuntimeException("No Valid Document Type Found for document type name '" + documentTypeFullName + "'");
            }
            return documentType;
        }
        return null;
    }

    @Override
    public List<RemotableAttributeError> validateSearchableAttributes(DocumentSearchCriteria.Builder criteria) {
        List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();
        DocumentType documentType = null;
        try{
              documentType = getValidDocumentType(criteria.getDocumentTypeName());
        }catch(RuntimeException re){
            errors.add(RemotableAttributeError.Builder.create("documentTypeName", re.getMessage()).build());
        }

        if (documentType != null) {
            errors = KEWServiceLocator.getDocumentSearchCustomizationMediator().validateLookupFieldParameters(documentType, criteria.build());
        } else {
            criteria.setDocumentAttributeValues(new HashMap<String, List<String>>());
        }
        return errors == null ? Collections.<RemotableAttributeError>emptyList() : Collections.unmodifiableList(errors);
    }

    public QueryComponent getSearchableAttributeSql(Map<String, List<String>> documentAttributeValues, List<RemotableAttributeField> searchFields, String whereClausePredicatePrefix) {

        StringBuilder fromSql = new StringBuilder();
        StringBuilder whereSql = new StringBuilder();

        //Map<String, List<SearchAttributeCriteriaComponent>> searchableAttributeRangeComponents = new HashMap<String,List<SearchAttributeCriteriaComponent>>();
        Criteria finalCriteria = null;
        int tableIndex = 1;
        SqlBuilder sqlBuilder = this.getSqlBuilder();

        for (String documentAttributeName : documentAttributeValues.keySet()) {
            String documentAttributeNameForSQL = documentAttributeName;
            if (documentAttributeName.contains(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX)) {
                documentAttributeNameForSQL = documentAttributeName.replaceFirst(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX, "");
            }
            List<String> searchValues = documentAttributeValues.get(documentAttributeName);
            if (CollectionUtils.isEmpty(searchValues) || documentAttributeName.contains(KRADConstants.CHECKBOX_PRESENT_ON_FORM_ANNOTATION)) {
                continue;
            }

            String tableAlias = "EXT" + tableIndex;
            RemotableAttributeField searchField = getSearchFieldByName(documentAttributeName, searchFields);
            String tableName = DocumentSearchInternalUtils.getAttributeTableName(searchField);
            boolean caseSensitive = DocumentSearchInternalUtils.isLookupCaseSensitive(searchField);

            Criteria crit = null;

            Class<?> dataTypeClass = DocumentSearchInternalUtils.getDataTypeClass(searchField);
            if (searchValues.size() > 1) {
                // if there's more than one entry, we need to do an "in"
                crit = new Criteria(tableName, tableAlias);
                crit.setDbPlatform(sqlBuilder.getDbPlatform());
                crit.in("VAL", searchValues, dataTypeClass);
            } else {
                crit = sqlBuilder.createCriteria("VAL", searchValues.get(0) , tableName, tableAlias, dataTypeClass, !caseSensitive);
            }

            sqlBuilder.addCriteria("KEY_CD", documentAttributeNameForSQL, String.class, false, false, crit); // this is always of type string.
            sqlBuilder.andCriteria("DOC_HDR_ID", tableAlias + ".DOC_HDR_ID", "KREW_DOC_HDR_T", "DOC_HDR", SqlBuilder.JoinType.class, false, false, crit);

            if (finalCriteria == null ){
                finalCriteria = crit;
            } else{
                sqlBuilder.andCriteria(finalCriteria, crit);
            }

            // - below is the old code
            // if where clause is empty then use passed in prefix... otherwise generate one
            String whereClausePrefix = (whereSql.length() == 0) ? whereClausePredicatePrefix : getGeneratedPredicatePrefix(whereSql.length());
            QueryComponent qc = generateSearchableAttributeSql(tableName, documentAttributeNameForSQL, whereClausePrefix, tableIndex);
            fromSql.append(qc.getFromSql());
            tableIndex++;
        }

        if (finalCriteria == null) {
            return new QueryComponent("", "", "");
        }

        String whereClausePrefix = (whereSql.length() == 0) ? whereClausePredicatePrefix : getGeneratedPredicatePrefix(whereSql.length());

        return new QueryComponent("", fromSql.toString(), whereClausePrefix + " " + finalCriteria.buildWhere());
    }

    private RemotableAttributeField getSearchFieldByName(String fieldName, List<RemotableAttributeField> searchFields) {
        for (RemotableAttributeField searchField : searchFields) {
            if (searchField.getName().equals(fieldName)
                    || searchField.getName().equals(KewApiConstants.DOCUMENT_ATTRIBUTE_FIELD_PREFIX + fieldName)) {
                return searchField;
            }
        }
        throw new IllegalStateException("Failed to locate a RemotableAttributeField for fieldName=" + fieldName);
    }

    public QueryComponent generateSearchableAttributeSql(String tableName, String documentAttributeName, String whereSqlStarter,int tableIndex) {
        String tableIdentifier = "EXT" + tableIndex;
        QueryComponent joinSqlComponent = getSearchableAttributeJoinSql(tableName, tableIdentifier, whereSqlStarter, documentAttributeName);
        return new QueryComponent("", joinSqlComponent.getFromSql(), joinSqlComponent.getWhereSql());
    }

    public QueryComponent getSearchableAttributeJoinSql(String tableName, String tableIdentifier, String whereSqlStarter, String attributeTableKeyColumnName) {
        return new QueryComponent("", generateSearchableAttributeFromSql(tableName, tableIdentifier).toString(), generateSearchableAttributeWhereClauseJoin(whereSqlStarter, tableIdentifier, attributeTableKeyColumnName).toString());
    }

    public StringBuilder generateSearchableAttributeWhereClauseJoin(String whereSqlStarter,String tableIdentifier,String attributeTableKeyColumnName) {
        StringBuilder whereSql = new StringBuilder(constructWhereClauseElement(whereSqlStarter, "DOC_HDR.DOC_HDR_ID", "=", getDbPlatform().escapeString(tableIdentifier + ".DOC_HDR_ID"), null, null));
        whereSql.append(constructWhereClauseElement(" and ", tableIdentifier + ".KEY_CD", "=",
                getDbPlatform().escapeString(attributeTableKeyColumnName), "'", "'"));
        return whereSql;
    }

    public StringBuilder generateSearchableAttributeFromSql(String tableName, String tableIdentifier) {
        if (StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("tableName was null or blank");
        }
        if (StringUtils.isBlank(tableIdentifier)) {
            throw new IllegalArgumentException("tableIdentifier was null or blank");
        }
        StringBuilder fromSql = new StringBuilder();
        fromSql.append(" ,").append(tableName).append(" ").append(getDbPlatform().escapeString(tableIdentifier)).append(" ");
        return fromSql;
    }

    public StringBuilder constructWhereClauseElement(String clauseStarter,String queryTableColumnName,String operand,String valueToSearch,String valuePrefix,String valueSuffix) {
        StringBuilder whereSql = new StringBuilder();
        valuePrefix = (valuePrefix != null) ? valuePrefix : "";
        valueSuffix = (valueSuffix != null) ? valueSuffix : "";
        whereSql.append(" " + clauseStarter + " ").append(getDbPlatform().escapeString(queryTableColumnName)).append(" " + operand + " ").append(valuePrefix).append(valueToSearch).append(valueSuffix).append(" ");
        return whereSql;
    }

    @Override
    public DocumentSearchResults.Builder processResultSet(DocumentSearchCriteria criteria, boolean criteriaModified, Statement searchAttributeStatement, ResultSet resultSet, int maxResultCap, int fetchLimit) throws SQLException {
        DocumentSearchCriteria.Builder criteriaBuilder = DocumentSearchCriteria.Builder.create(criteria);
        DocumentSearchResults.Builder results = DocumentSearchResults.Builder.create(criteriaBuilder);
        results.setCriteriaModified(criteriaModified);

        List<DocumentSearchResult.Builder> resultList = new ArrayList<DocumentSearchResult.Builder>();
        results.setSearchResults(resultList);
        Map<String, DocumentSearchResult.Builder> resultMap = new HashMap<String, DocumentSearchResult.Builder>();

        int startAt = (criteria.getStartAtIndex()==null) ? 0 : criteria.getStartAtIndex();
        int iteration = 0;
        boolean resultSetHasNext = resultSet.next();

        PerformanceLogger perfLog = new PerformanceLogger();

        while (resultSetHasNext && resultMap.size() < maxResultCap && iteration < fetchLimit && startAt >= 0) {
            if (iteration >= startAt) {
                DocumentSearchResult.Builder resultBuilder = processRow(criteria, searchAttributeStatement, resultSet);
                String documentId = resultBuilder.getDocument().getDocumentId();
                if (!resultMap.containsKey(documentId)) {
                    resultList.add(resultBuilder);
                    resultMap.put(documentId, resultBuilder);
                } else {
                    // handle duplicate rows with different search data
                    DocumentSearchResult.Builder previousEntry = resultMap.get(documentId);
                    handleMultipleDocumentRows(previousEntry, resultBuilder);
                }
            }

            iteration++;
            resultSetHasNext = resultSet.next();
        }

        perfLog.log("Time to read doc search results.", true);
        // if we have threshold+1 results, then we have more results than we are going to display
        results.setOverThreshold(resultSetHasNext);

        LOG.debug("Processed " + resultMap.size() + " document search result rows.");
        return results;
    }

    /**
     * Handles multiple document rows by collapsing them into the list of document attributes on the existing row.
     * The two rows must represent the same document.
     *
     * @param existingRow the existing row to combine the new row into
     * @param newRow the new row from which to combine document attributes with the existing row
     */
    private void handleMultipleDocumentRows(DocumentSearchResult.Builder existingRow, DocumentSearchResult.Builder newRow) {
        for (DocumentAttribute.AbstractBuilder<?> newDocumentAttribute : newRow.getDocumentAttributes()) {
            existingRow.getDocumentAttributes().add(newDocumentAttribute);
        }
    }

    /**
     * Processes the search result row, returning a DocumentSearchResult
     * @param criteria the original search criteria
     * @param searchAttributeStatement statement being used to call the database for queries
     * @param rs the search result set
     * @return a DocumentSearchResult representing the current ResultSet row
     * @throws SQLException
     */
    protected DocumentSearchResult.Builder processRow(DocumentSearchCriteria criteria, Statement searchAttributeStatement, ResultSet rs) throws SQLException {

        String documentId = rs.getString("DOC_HDR_ID");
        String initiatorPrincipalId = rs.getString("INITR_PRNCPL_ID");
        String documentTypeName = rs.getString("DOC_TYP_NM");
        org.kuali.rice.kew.api.doctype.DocumentType documentType =
                getApiDocumentTypeService().getDocumentTypeByName(documentTypeName);
        if (documentType == null) {
            throw new IllegalStateException("Failed to locate a document type with the given name: " + documentTypeName);
        }
        String documentTypeId = documentType.getId();

        Document.Builder documentBuilder = Document.Builder.create(documentId, initiatorPrincipalId, documentTypeName, documentTypeId);
        DocumentSearchResult.Builder resultBuilder = DocumentSearchResult.Builder.create(documentBuilder);

        String statusCode = rs.getString("DOC_HDR_STAT_CD");
        Timestamp createTimestamp = rs.getTimestamp("CRTE_DT");
        String title = rs.getString("TTL");
        String applicationDocumentStatus = rs.getString("APP_DOC_STAT");

        documentBuilder.setStatus(DocumentStatus.fromCode(statusCode));
        documentBuilder.setDateCreated(new DateTime(createTimestamp.getTime()));
        documentBuilder.setTitle(title);
        documentBuilder.setApplicationDocumentStatus(applicationDocumentStatus);
        documentBuilder.setApplicationDocumentStatusDate(new DateTime(rs.getTimestamp("APP_DOC_STAT_MDFN_DT")));
        documentBuilder.setDateApproved(new DateTime(rs.getTimestamp("APRV_DT")));
        documentBuilder.setDateFinalized(new DateTime(rs.getTimestamp("FNL_DT")));
        documentBuilder.setApplicationDocumentId(rs.getString("APP_DOC_ID"));
        documentBuilder.setDateLastModified(new DateTime(rs.getTimestamp("STAT_MDFN_DT")));
        documentBuilder.setRoutedByPrincipalId(rs.getString("RTE_PRNCPL_ID"));

        // TODO - KULRICE-5755 - should probably set as many properties on the document as we can
        documentBuilder.setDocumentHandlerUrl(rs.getString("DOC_HDLR_URL"));

        if (isUsingAtLeastOneSearchAttribute(criteria)) {
            populateDocumentAttributesValues(resultBuilder, searchAttributeStatement);
        }

        return resultBuilder;
    }

    /**
     * This method performs searches against the search attribute value tables (see classes implementing
     * {@link org.kuali.rice.kew.docsearch.SearchableAttributeValue}) to get data to fill in search attribute values on the given resultBuilder parameter
     *
     * @param resultBuilder - document search result object getting search attributes added to it
     * @param searchAttributeStatement - statement being used to call the database for queries
     * @throws SQLException
     */
    public void populateDocumentAttributesValues(DocumentSearchResult.Builder resultBuilder, Statement searchAttributeStatement) throws SQLException {
        searchAttributeStatement.setFetchSize(50);
        String documentId = resultBuilder.getDocument().getDocumentId();
        List<SearchableAttributeValue> attributeValues = DocumentSearchInternalUtils
                .getSearchableAttributeValueObjectTypes();
        PerformanceLogger perfLog = new PerformanceLogger(documentId);
        for (SearchableAttributeValue searchAttValue : attributeValues) {
            String attributeSql = "select KEY_CD, VAL from " + searchAttValue.getAttributeTableName() + " where DOC_HDR_ID = '" + documentId + "'";
            ResultSet attributeResultSet = null;
            try {
                attributeResultSet = searchAttributeStatement.executeQuery(attributeSql);
                while (attributeResultSet.next()) {
                    searchAttValue.setSearchableAttributeKey(attributeResultSet.getString("KEY_CD"));
                    searchAttValue.setupAttributeValue(attributeResultSet, "VAL");
                    if ( (!org.apache.commons.lang.StringUtils.isEmpty(searchAttValue.getSearchableAttributeKey())) && (searchAttValue.getSearchableAttributeValue() != null) ) {
                        DocumentAttribute documentAttribute = searchAttValue.toDocumentAttribute();
                        resultBuilder.getDocumentAttributes().add(DocumentAttributeFactory.loadContractIntoBuilder(
                                documentAttribute));
                    }
                }
            } finally {
                if (attributeResultSet != null) {
                    try {
                        attributeResultSet.close();
                    } catch (Exception e) {
                        LOG.warn("Could not close searchable attribute result set for class " + searchAttValue.getClass().getName(),e);
                    }
                }
            }
        }
        perfLog.log("Time to execute doc search search attribute queries.", true);
    }

    @SuppressWarnings("deprecation")
    public String generateSearchSql(DocumentSearchCriteria criteria, List<RemotableAttributeField> searchFields) {

        String docTypeTableAlias   = "DOC1";
        String docHeaderTableAlias = "DOC_HDR";

        String sqlPrefix = "Select * from (";
        String sqlSuffix = ") FINAL_SEARCH order by FINAL_SEARCH.CRTE_DT desc";
        
        // the DISTINCT here is important as it filters out duplicate rows which could occur as the result of doc search extension values...
        StringBuilder selectSQL = new StringBuilder("select DISTINCT("+ docHeaderTableAlias +".DOC_HDR_ID), "
                                                    + StringUtils.join(new String[] {
                                                        docHeaderTableAlias + ".INITR_PRNCPL_ID",
                                                        docHeaderTableAlias + ".DOC_HDR_STAT_CD",
                                                        docHeaderTableAlias + ".CRTE_DT",
                                                        docHeaderTableAlias + ".TTL",
                                                        docHeaderTableAlias + ".APP_DOC_STAT",
                                                        docHeaderTableAlias + ".STAT_MDFN_DT",
                                                        docHeaderTableAlias + ".APRV_DT",
                                                        docHeaderTableAlias + ".FNL_DT",
                                                        docHeaderTableAlias + ".APP_DOC_ID",
                                                        docHeaderTableAlias + ".RTE_PRNCPL_ID",
                                                        docHeaderTableAlias + ".APP_DOC_STAT_MDFN_DT",
                                                        docTypeTableAlias + ".DOC_TYP_NM",
                                                        docTypeTableAlias + ".LBL",
                                                        docTypeTableAlias + ".DOC_HDLR_URL",
                                                        docTypeTableAlias + ".ACTV_IND"
                                                    }, ", "));
        StringBuilder fromSQL = new StringBuilder(" from KREW_DOC_TYP_T "+ docTypeTableAlias +" ");
        StringBuilder fromSQLForDocHeaderTable = new StringBuilder(", KREW_DOC_HDR_T " + docHeaderTableAlias + " ");

        StringBuilder whereSQL = new StringBuilder();
        whereSQL.append(getDocumentIdSql(criteria.getDocumentId(), getGeneratedPredicatePrefix(whereSQL.length()), docHeaderTableAlias));
        // if principalId criteria exists ignore deprecated principalName search term
        String principalInitiatorIdSql = getInitiatorIdSql(criteria.getInitiatorPrincipalId(), getGeneratedPredicatePrefix(whereSQL.length()));
        if (StringUtils.isNotBlank(principalInitiatorIdSql)) {
            whereSQL.append(principalInitiatorIdSql);
        } else {
            whereSQL.append(getInitiatorSql(criteria.getInitiatorPrincipalName(), getGeneratedPredicatePrefix(whereSQL.length())));
        }
        whereSQL.append(getAppDocIdSql(criteria.getApplicationDocumentId(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDateCreatedSql(criteria.getDateCreatedFrom(), criteria.getDateCreatedTo(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDateLastModifiedSql(criteria.getDateLastModifiedFrom(), criteria.getDateLastModifiedTo(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDateApprovedSql(criteria.getDateApprovedFrom(), criteria.getDateApprovedTo(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDateFinalizedSql(criteria.getDateFinalizedFrom(), criteria.getDateFinalizedTo(), getGeneratedPredicatePrefix(whereSQL.length())));

        // flags for the table being added to the FROM class of the sql
        String principalViewerSql = getViewerSql(criteria.getViewerPrincipalName(), getGeneratedPredicatePrefix(whereSQL.length()));
        String principalViewerIdSql = getViewerIdSql(criteria.getViewerPrincipalId(), getGeneratedPredicatePrefix(whereSQL.length()));
        // if principalId criteria exists ignore deprecated principalName search term
        if (StringUtils.isNotBlank(principalViewerIdSql)){
            principalViewerSql = "";
        }
        String groupViewerSql = getGroupViewerSql(criteria.getGroupViewerId(), getGeneratedPredicatePrefix(whereSQL.length()));
        if (StringUtils.isNotBlank(principalViewerSql) || StringUtils.isNotBlank(groupViewerSql) || StringUtils.isNotBlank(principalViewerIdSql) ) {
            whereSQL.append(principalViewerSql);
            whereSQL.append(principalViewerIdSql);
            whereSQL.append(groupViewerSql);
            fromSQL.append(", KREW_ACTN_RQST_T ");
        }

        String principalApproverSql =  getApproverSql(criteria.getApproverPrincipalName(), getGeneratedPredicatePrefix(whereSQL.length()));
        String principalApproverIdSql = getApproverIdSql(criteria.getApproverPrincipalId(), getGeneratedPredicatePrefix(whereSQL.length()));
        // if principalId criteria exists ignore deprecated principalName search term
        if (StringUtils.isNotBlank(principalApproverIdSql)){
            principalApproverSql = "";
        }
        if (StringUtils.isNotBlank(principalApproverSql) || StringUtils.isNotBlank(principalApproverIdSql)) {
            whereSQL.append(principalApproverSql);
            whereSQL.append(principalApproverIdSql);
            fromSQL.append(", KREW_ACTN_TKN_T ");
        }



        String docRouteNodeSql = getDocRouteNodeSql(criteria.getDocumentTypeName(), criteria.getRouteNodeName(), criteria.getRouteNodeLookupLogic(), getGeneratedPredicatePrefix(whereSQL.length()));
        if (StringUtils.isNotBlank(docRouteNodeSql)) {
            whereSQL.append(docRouteNodeSql);
            fromSQL.append(", KREW_RTE_NODE_INSTN_T ");
            fromSQL.append(", KREW_RTE_NODE_T ");
        }

        if (!criteria.getDocumentAttributeValues().isEmpty()) {
            QueryComponent queryComponent = getSearchableAttributeSql(criteria.getDocumentAttributeValues(), searchFields, getGeneratedPredicatePrefix(
                    whereSQL.length()));
            selectSQL.append(queryComponent.getSelectSql());
            fromSQL.append(queryComponent.getFromSql());
            whereSQL.append(queryComponent.getWhereSql());
        }

        whereSQL.append(getDocTypeFullNameWhereSql(criteria, getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDocTitleSql(criteria.getTitle(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getDocumentStatusSql(criteria.getDocumentStatuses(), criteria.getDocumentStatusCategories(), getGeneratedPredicatePrefix(whereSQL.length())));
        whereSQL.append(getGeneratedPredicatePrefix(whereSQL.length())).append(" DOC_HDR.DOC_TYP_ID = DOC1.DOC_TYP_ID ");
        fromSQL.append(fromSQLForDocHeaderTable);

        // App Doc Status Value and Transition clauses
        String statusTransitionWhereClause = getStatusTransitionDateSql(criteria.getDateApplicationDocumentStatusChangedFrom(), criteria.getDateApplicationDocumentStatusChangedTo(), getGeneratedPredicatePrefix(whereSQL.length()));

        List<String> applicationDocumentStatuses = criteria.getApplicationDocumentStatuses();
        // deal with legacy usage of applicationDocumentStatus (which is deprecated)
        if (!StringUtils.isBlank(criteria.getApplicationDocumentStatus())) {
            if (!criteria.getApplicationDocumentStatuses().contains(criteria.getApplicationDocumentStatus())) {
                applicationDocumentStatuses = new ArrayList<String>(criteria.getApplicationDocumentStatuses());
                applicationDocumentStatuses.add(criteria.getApplicationDocumentStatus());
            }
        }

        whereSQL.append(getAppDocStatusesSql(applicationDocumentStatuses, getGeneratedPredicatePrefix(
                whereSQL.length()), statusTransitionWhereClause.length()));
        if (statusTransitionWhereClause.length() > 0){
        	whereSQL.append(statusTransitionWhereClause);
            whereSQL.append(getGeneratedPredicatePrefix(whereSQL.length())).append(" DOC_HDR.DOC_HDR_ID = STAT_TRAN.DOC_HDR_ID ");
        	fromSQL.append(", KREW_APP_DOC_STAT_TRAN_T STAT_TRAN ");
        }

        String finalizedSql = sqlPrefix + " " + selectSQL.toString() + " " + fromSQL.toString() + " " + whereSQL.toString() + " " + sqlSuffix;

        LOG.info("*********** SEARCH SQL ***************");
        LOG.info(finalizedSql);
        LOG.info("**************************************");
        return finalizedSql;
    }

    public String getDocumentIdSql(String documentId, String whereClausePredicatePrefix, String tableAlias) {
        if (StringUtils.isBlank(documentId)) {
            return "";
        } else {
        	// Using true for caseInsensitive causes bad performance for MYSQL databases since function indexes cannot be added.
        	// Due to this, false is passed for caseInsensitive
            Criteria crit = getSqlBuilder().createCriteria("DOC_HDR_ID", documentId, "KREW_DOC_HDR_T", tableAlias, String.class, false, true);
            return new StringBuilder(whereClausePredicatePrefix + crit.buildWhere()).toString();
        }
    }

    public String getDocTitleSql(String docTitle, String whereClausePredicatePrefix) {
        if (StringUtils.isBlank(docTitle)) {
            return "";
        } else {
            // quick and dirty ' replacement that isn't the best but should work for all dbs
            docTitle = docTitle.trim().replace("\'", "\'\'");
            SqlBuilder sqlBuild = new SqlBuilder();
            Criteria crit = new Criteria("KREW_DOC_HDR_T", "DOC_HDR");
            sqlBuild.addCriteria("TTL", docTitle, String.class, true, true, crit);
            return new StringBuilder(whereClausePredicatePrefix + crit.buildWhere()).toString();
        }
    }

    // special methods that return the sql needed to complete the search
    // or nothing if the field was not filled in
    public String getAppDocIdSql(String appDocId, String whereClausePredicatePrefix) {
        if (StringUtils.isBlank(appDocId)) {
            return "";
        } else {
            String tableAlias = "DOC_HDR";
            Criteria crit = getSqlBuilder().createCriteria("APP_DOC_ID", appDocId, "KREW_DOC_HDR_T", tableAlias,String.class);
            return new StringBuilder(whereClausePredicatePrefix + crit.buildWhere()).toString();
        }
    }

    public String getDateCreatedSql(DateTime fromDateCreated, DateTime toDateCreated, String whereClausePredicatePrefix) {
        return establishDateString(fromDateCreated, toDateCreated, "KREW_DOC_HDR_T", "DOC_HDR", "CRTE_DT", whereClausePredicatePrefix);
    }

    public String getDateApprovedSql(DateTime fromDateApproved, DateTime toDateApproved, String whereClausePredicatePrefix) {
        return establishDateString(fromDateApproved, toDateApproved, "KREW_DOC_HDR_T", "DOC_HDR", "APRV_DT", whereClausePredicatePrefix);
    }

    public String getDateFinalizedSql(DateTime fromDateFinalized, DateTime toDateFinalized, String whereClausePredicatePrefix) {
        return establishDateString(fromDateFinalized, toDateFinalized, "KREW_DOC_HDR_T", "DOC_HDR", "FNL_DT", whereClausePredicatePrefix);
    }

    public String getDateLastModifiedSql(DateTime fromDateLastModified, DateTime toDateLastModified, String whereClausePredicatePrefix) {
        return establishDateString(fromDateLastModified, toDateLastModified, "KREW_DOC_HDR_T", "DOC_HDR", "STAT_MDFN_DT", whereClausePredicatePrefix);
    }

	public String getStatusTransitionDateSql(DateTime fromStatusTransitionDate, DateTime toStatusTransitionDate, String whereClausePredicatePrefix) {
        return establishDateString(fromStatusTransitionDate, toStatusTransitionDate, "KREW_DOC_HDR_T", "DOC_HDR", "APP_DOC_STAT_MDFN_DT", whereClausePredicatePrefix);
    }

    public String getViewerSql(String viewer, String whereClausePredicatePrefix) {
        StringBuilder returnSql = new StringBuilder();
        if (StringUtils.isNotBlank(viewer)) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("principalName", viewer);

            // This will search for people with the ability for the valid operands.
            List<Person> personList = KimApiServiceLocator.getPersonService().findPeople(m, false);
            List<String> principalList = new ArrayList<String>();

            if(CollectionUtils.isEmpty(personList)) {
            	// findPeople allows for wildcards, but the person must be active.  If no one was found,
            	// check for an exact inactive user.
                PrincipalContract tempPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(viewer.trim());
        		if (tempPrincipal != null) {
                    principalList.add(tempPrincipal.getPrincipalId());
            	} else {
                    // they entered something that returned nothing... so we should return nothing

                    return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
            	}
            }

            for (Person person : personList){
                principalList.add(person.getPrincipalId());
            }

            Criteria crit = new Criteria("KREW_ACTN_RQST_T", "KREW_ACTN_RQST_T");
            crit.in("PRNCPL_ID", principalList, String.class);
            returnSql.append(whereClausePredicatePrefix + "( (DOC_HDR.DOC_HDR_ID = KREW_ACTN_RQST_T.DOC_HDR_ID and " + crit.buildWhere() + " )");

            Set<String> viewerGroupIds = new TreeSet<String>();

            if(CollectionUtils.isNotEmpty(principalList)) {
                for(String principalId: principalList){
                    viewerGroupIds.addAll(KimApiServiceLocator.getGroupService().getGroupIdsByPrincipalId(principalId));
                }
            }

            // Documents routed to users as part of a workgoup should be returned.
            // Use Chad's escape stuff
            if (viewerGroupIds != null && !viewerGroupIds.isEmpty()) {

                returnSql.append(" or ( " +
                    "DOC_HDR.DOC_HDR_ID = KREW_ACTN_RQST_T.DOC_HDR_ID " +
                    "and KREW_ACTN_RQST_T.GRP_ID in (");

                boolean first = true;
                for (String groupId : viewerGroupIds){
                    if(!first){
                        returnSql.append(",");
                    }
                    returnSql.append("'").append(groupId).append("'");
                    first = false;
                }
                returnSql.append("))");
            }
            returnSql.append(")");
        }
        return returnSql.toString();
    }

    public String getViewerIdSql(String viewerId, String whereClausePredicatePrefix) {
        StringBuilder returnSql = new StringBuilder();
        if (StringUtils.isNotBlank(viewerId)) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("principalId", viewerId);

            // This will search for people with the ability for the valid operands.
            List<Person> personList = KimApiServiceLocator.getPersonService().findPeople(m, false);
            List<String> principalList = new ArrayList<String>();

            if(CollectionUtils.isEmpty(personList)) {
                // they entered something that returned nothing... so we should return nothing
                return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
            }

            for (Person person : personList){
                principalList.add(person.getPrincipalId());
            }

            Criteria crit = new Criteria("KREW_ACTN_RQST_T", "KREW_ACTN_RQST_T");
            crit.in("PRNCPL_ID", principalList, String.class);
            returnSql.append(whereClausePredicatePrefix + "( DOC_HDR.DOC_HDR_ID = KREW_ACTN_RQST_T.DOC_HDR_ID and " + crit.buildWhere() + " )");
        }
        return returnSql.toString();
    }

    public String getGroupViewerSql(String groupId, String whereClausePredicatePrefix) {
        String sql = "";
        if (StringUtils.isNotBlank(groupId)) {
            sql = whereClausePredicatePrefix + " DOC_HDR.DOC_HDR_ID = KREW_ACTN_RQST_T.DOC_HDR_ID and KREW_ACTN_RQST_T.GRP_ID = '" + groupId + "'";
        }
        return sql;
    }

    public String getInitiatorSql(String initiatorPrincipalName, String whereClausePredicatePrefix) {

        if (StringUtils.isBlank(initiatorPrincipalName)) {
            return "";
        }

        String tableAlias = "DOC_HDR";

        Map<String, String> m = new HashMap<String, String>();
        m.put("principalName", initiatorPrincipalName);

        // This will search for people with the ability for the valid operands.
        List<Person> pList = KimApiServiceLocator.getPersonService().findPeople(m, false);
        List<String> principalList = new ArrayList<String>();

        if(pList == null || pList.isEmpty() ){
       		// findPeople allows for wildcards, but the person must be active.  If no one was found,
       		// check for an exact inactive user.
       		PrincipalContract tempPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(initiatorPrincipalName.trim());
       		if (tempPrincipal != null) {
       			principalList.add(tempPrincipal.getPrincipalId());
       		} else {
                // they entered something that returned nothing... so we should return nothing
                return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
        	}
        }

        for(Person p: pList){
            principalList.add(p.getPrincipalId());
        }

        Criteria crit = new Criteria("KREW_DOC_HDR_T", tableAlias);
        crit.in("INITR_PRNCPL_ID", principalList, String.class);

        return new StringBuilder(whereClausePredicatePrefix + crit.buildWhere()).toString();
    }

    public String getInitiatorIdSql(String initiatorPrincipalId, String whereClausePredicatePrefix) {

        if (StringUtils.isBlank(initiatorPrincipalId)) {
            return "";
        }

        String tableAlias = "DOC_HDR";

        Map<String, String> m = new HashMap<String, String>();
        m.put("principalId", initiatorPrincipalId);

        // This will search for people with the ability for the valid operands.
        List<Person> pList = KimApiServiceLocator.getPersonService().findPeople(m, false);
        List<String> principalList = new ArrayList<String>();

        if(pList == null || pList.isEmpty() ){
            // they entered something that returned nothing... so we should return nothing
            return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
        }

        for(Person p: pList){
            principalList.add(p.getPrincipalId());
        }

        Criteria crit = new Criteria("KREW_DOC_HDR_T", tableAlias);
        crit.in("INITR_PRNCPL_ID", principalList, String.class);

        return new StringBuilder(whereClausePredicatePrefix + crit.buildWhere()).toString();
    }

    public String getApproverSql(String approver, String whereClausePredicatePrefix) {
        String returnSql = "";
        if (StringUtils.isNotBlank(approver)) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("principalName", approver);

            // This will search for people with the ability for the valid operands.
            List<Person> pList = KimApiServiceLocator.getPersonService().findPeople(m, false);
            List<String> principalList = new ArrayList<String>();

            if(pList == null || pList.isEmpty() ){
           		// findPeople allows for wildcards, but the person must be active.  If no one was found,
           		// check for an exact inactive user.
                PrincipalContract tempPrincipal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(approver.trim());

                if (tempPrincipal != null) {
           			principalList.add(tempPrincipal.getPrincipalId());
                } else {
                    // they entered something that returned nothing... so we should return nothing
                    return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
                }
            }

            for(Person p: pList){
                principalList.add(p.getPrincipalId());
            }

            Criteria crit = new Criteria("KREW_ACTN_TKN_T", "KREW_ACTN_TKN_T");
            crit.in("PRNCPL_ID", principalList, String.class);

            returnSql = whereClausePredicatePrefix +
            " DOC_HDR.DOC_HDR_ID = KREW_ACTN_TKN_T.DOC_HDR_ID and upper(KREW_ACTN_TKN_T.ACTN_CD) in ('" +
            KewApiConstants.ACTION_TAKEN_APPROVED_CD + "','" + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + "')" +
            " and " + crit.buildWhere();
        }
        return returnSql;
    }

    public String getApproverIdSql(String approverId, String whereClausePredicatePrefix) {
        String returnSql = "";
        if (StringUtils.isNotBlank(approverId)) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("principalId", approverId);

            // This will search for people with the ability for the valid operands.
            List<Person> pList = KimApiServiceLocator.getPersonService().findPeople(m, false);
            List<String> principalList = new ArrayList<String>();

            if(pList == null || pList.isEmpty() ){
                 // they entered something that returned nothing... so we should return nothing
                    return new StringBuilder(whereClausePredicatePrefix + " 1 = 0 ").toString();
            }

            for(Person p: pList){
                principalList.add(p.getPrincipalId());
            }

            Criteria crit = new Criteria("KREW_ACTN_TKN_T", "KREW_ACTN_TKN_T");
            crit.in("PRNCPL_ID", principalList, String.class);

            returnSql = whereClausePredicatePrefix +
                    " DOC_HDR.DOC_HDR_ID = KREW_ACTN_TKN_T.DOC_HDR_ID and upper(KREW_ACTN_TKN_T.ACTN_CD) in ('" +
                    KewApiConstants.ACTION_TAKEN_APPROVED_CD + "','" + KewApiConstants.ACTION_TAKEN_BLANKET_APPROVE_CD + "')" +
                    " and " + crit.buildWhere();
        }
        return returnSql;
    }

    public String getDocTypeFullNameWhereSql(DocumentSearchCriteria criteria, String whereClausePredicatePrefix) {
        List<String> documentTypeNamesToSearch = new ArrayList<String>();
        String primaryDocumentTypeName = criteria.getDocumentTypeName();
        if (StringUtils.isNotBlank(primaryDocumentTypeName)) {
            documentTypeNamesToSearch.add(primaryDocumentTypeName);
        }
        documentTypeNamesToSearch.addAll(criteria.getAdditionalDocumentTypeNames());
        StringBuilder returnSql = new StringBuilder("");
        if (CollectionUtils.isNotEmpty(documentTypeNamesToSearch)) {
            int index = 0;
            for (String documentTypeName : documentTypeNamesToSearch) {
                if (StringUtils.isNotBlank(documentTypeName)) {
                    String clause = index++ == 0 ? "" : " or ";
                    DocumentTypeService docSrv = KEWServiceLocator.getDocumentTypeService();
                    DocumentType docType = docSrv.findByNameCaseInsensitive(documentTypeName.trim());
                    if (docType != null) {
                        if (documentTypeName.contains("*") || documentTypeName.contains("%")) {
                            addDocumentTypeLikeNameToSearchOn(returnSql, documentTypeName.trim(), clause);
                        } else {
                            addDocumentTypeNameToSearchOn(returnSql, documentTypeName.trim(), clause);
                        }
                        if (docType.getChildrenDocTypes() != null) {
                            addChildDocumentTypes(returnSql, docType.getChildrenDocTypes());
                        }
                    } else{
                        addDocumentTypeLikeNameToSearchOn(returnSql, documentTypeName.trim(), clause);
                    }
                }
            }
        }
        if (returnSql.length() > 0) {
            returnSql.insert(0, "(");
            returnSql.insert(0, whereClausePredicatePrefix);
            returnSql.append(")");
        }
        return returnSql.toString();
    }

    public void addChildDocumentTypes(StringBuilder whereSql, Collection<DocumentType> childDocumentTypes) {
        for (DocumentType child : childDocumentTypes) {
            addDocumentTypeNameToSearchOn(whereSql, child.getName());
            addChildDocumentTypes(whereSql, child.getChildrenDocTypes());
        }
    }

    public void addDocumentTypeNameToSearchOn(StringBuilder whereSql, String documentTypeName) {
        this.addDocumentTypeNameToSearchOn(whereSql, documentTypeName, " or ");
    }

    public void addDocumentTypeNameToSearchOn(StringBuilder whereSql, String documentTypeName, String clause) {
        whereSql.append(clause).append("upper(DOC1.DOC_TYP_NM) = '" + documentTypeName.toUpperCase() + "'");
    }
    public void addDocumentTypeLikeNameToSearchOn(StringBuilder whereSql, String documentTypeName, String clause) {
        documentTypeName = documentTypeName.replace('*', '%');
        whereSql.append(clause).append(" upper(DOC1.DOC_TYP_NM) LIKE '" + documentTypeName.toUpperCase() + "'");
    }

    public String getDocRouteNodeSql(String documentTypeFullName, String routeNodeName, RouteNodeLookupLogic docRouteLevelLogic, String whereClausePredicatePrefix) {
        // -1 is the default 'blank' choice from the route node drop down a number is used because the ojb RouteNode object is used to
        // render the node choices on the form.
        String returnSql = "";
        if (StringUtils.isNotBlank(routeNodeName)) {
            if (docRouteLevelLogic == null) {
                docRouteLevelLogic = RouteNodeLookupLogic.EXACTLY;
            }
            StringBuilder routeNodeCriteria = new StringBuilder("and " + ROUTE_NODE_TABLE + ".NM ");
            if (RouteNodeLookupLogic.EXACTLY == docRouteLevelLogic) {
        		routeNodeCriteria.append("= '" + getDbPlatform().escapeString(routeNodeName) + "' ");
            } else {
                routeNodeCriteria.append("in (");
                // below buffer used to facilitate the addition of the string ", " to separate out route node names
                StringBuilder routeNodeInCriteria = new StringBuilder();
                boolean foundSpecifiedNode = false;
                List<RouteNode> routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(getValidDocumentType(documentTypeFullName), true);
                for (RouteNode routeNode : routeNodes) {
                    if (routeNodeName.equals(routeNode.getRouteNodeName())) {
                        // current node is specified node so we ignore it outside of the boolean below
                        foundSpecifiedNode = true;
                        continue;
                    }
                    // below logic should be to add the current node to the criteria if we haven't found the specified node
                    // and the logic qualifier is 'route nodes before specified'... or we have found the specified node and
                    // the logic qualifier is 'route nodes after specified'
                    if ( (!foundSpecifiedNode && RouteNodeLookupLogic.BEFORE == docRouteLevelLogic) ||
                         (foundSpecifiedNode && RouteNodeLookupLogic.AFTER == docRouteLevelLogic) ) {
                        if (routeNodeInCriteria.length() > 0) {
                            routeNodeInCriteria.append(", ");
                        }
                        routeNodeInCriteria.append("'" + routeNode.getRouteNodeName() + "'");
                    }
                }
                if (routeNodeInCriteria.length() > 0) {
                    routeNodeCriteria.append(routeNodeInCriteria);
                } else {
                    routeNodeCriteria.append("''");
                }
                routeNodeCriteria.append(") ");
            }
            returnSql = whereClausePredicatePrefix + "DOC_HDR.DOC_HDR_ID = " + ROUTE_NODE_INST_TABLE + ".DOC_HDR_ID and " + ROUTE_NODE_INST_TABLE + ".RTE_NODE_ID = " + ROUTE_NODE_TABLE + ".RTE_NODE_ID and " + ROUTE_NODE_INST_TABLE + ".ACTV_IND = 1 " + routeNodeCriteria.toString() + " ";
        }
        return returnSql;
    }

    public String getDocumentStatusSql(List<DocumentStatus> documentStatuses, List<DocumentStatusCategory> categories, String whereClausePredicatePrefix) {
        if (CollectionUtils.isEmpty(documentStatuses) && CollectionUtils.isEmpty(categories)) {
            return whereClausePredicatePrefix + "DOC_HDR.DOC_HDR_STAT_CD != '" + DocumentStatus.INITIATED.getCode() + "'";
        } else {
            // include all given document statuses
            Set<DocumentStatus> statusesToInclude = new HashSet<DocumentStatus>(documentStatuses);

            // add all statuses from each category
            for (DocumentStatusCategory category : categories) {
                Set<DocumentStatus> categoryStatuses = DocumentStatus.getStatusesForCategory(category);
                statusesToInclude.addAll(categoryStatuses);
            }

            Set<String> statusCodes = new HashSet<String>();
            for (DocumentStatus statusToInclude : statusesToInclude) {
                statusCodes.add("'" + getDbPlatform().escapeString(statusToInclude.getCode()) + "'");
            }
            return whereClausePredicatePrefix + " DOC_HDR.DOC_HDR_STAT_CD in (" + StringUtils.join(statusCodes, ", ") +")";
        }
    }

    /**
     * This method generates the where clause fragment related to Application Document Status.
     * If the Status values only are defined, search for the appDocStatus value in the route header.
     * If either the transition from/to dates are defined, search agains the status transition history.
     */
    public String getAppDocStatusesSql(List<String> appDocStatuses, String whereClausePredicatePrefix, int statusTransitionWhereClauseLength) {
        if (CollectionUtils.isEmpty(appDocStatuses)) {
            return "";
        } else {
            String inList = buildAppDocStatusInList(appDocStatuses);

            if (statusTransitionWhereClauseLength > 0){
                return whereClausePredicatePrefix + " STAT_TRAN.APP_DOC_STAT_TO" + inList;
            } else {
                return whereClausePredicatePrefix + " DOC_HDR.APP_DOC_STAT" + inList;
            }
        }
    }

    private String buildAppDocStatusInList(List<String> appDocStatuses) {
        StringBuilder sql = new StringBuilder(" IN (");

        boolean first = true;
        for (String appDocStatus : appDocStatuses) {
            // commas before each element except the first one
            if (first) {
                first = false;
            } else {
                sql.append(",");
            }

            sql.append("'");
            sql.append(getDbPlatform().escapeString(appDocStatus.trim()));
            sql.append("'");
        }

        sql.append(")");

        return sql.toString();
    }

    public String getGeneratedPredicatePrefix(int whereClauseSize) {
        return (whereClauseSize > 0) ? " and " : " where ";
    }

    public String establishDateString(DateTime fromDate, DateTime toDate, String tableName, String tableAlias, String colName, String whereStatementClause) {

        String fromDateValue = null;
        if (fromDate != null) {
            fromDateValue = CoreApiServiceLocator.getDateTimeService().toDateString(fromDate.toDate());
        }

        String toDateValue = null;
        if (toDate != null) {
            toDateValue = CoreApiServiceLocator.getDateTimeService().toDateString(toDate.toDate());
            toDateValue += " 23:59:59";
        }

        String searchValue = null;
        if (fromDateValue != null && toDateValue != null) {
            searchValue = fromDateValue + " .. " + toDateValue;
        } else if (fromDateValue != null) {
            searchValue = ">= " + fromDateValue;
        } else if (toDateValue != null) {
            searchValue = "<= " + toDateValue;
        } else {
            return "";
        }

        Criteria crit = getSqlBuilder().createCriteria(colName, searchValue, tableName, tableAlias, java.sql.Date.class, true, true);
        return new StringBuilder(whereStatementClause).append(crit.buildWhere()).toString();

    }

    public DatabasePlatform getDbPlatform() {
        if (dbPlatform == null) {
            dbPlatform = (DatabasePlatform) GlobalResourceLoader.getService(RiceConstants.DB_PLATFORM);
        }
        return dbPlatform;
    }

    public SqlBuilder getSqlBuilder() {
        if(sqlBuilder == null){
            sqlBuilder = new SqlBuilder();
            sqlBuilder.setDbPlatform(getDbPlatform());
            sqlBuilder.setDateTimeService(CoreApiServiceLocator.getDateTimeService());
        }
        return this.sqlBuilder;
    }

    public void setSqlBuilder(SqlBuilder sqlBuilder) {
        this.sqlBuilder = sqlBuilder;
    }

    /**
     * A helper method for determining whether any searchable attributes are in use for the search.
     *
     * @return True if the search criteria contains at least one searchable attribute or the criteria's doc type name is
     * non-blank; false otherwise.
     */
    protected boolean isUsingAtLeastOneSearchAttribute(DocumentSearchCriteria criteria) {
        return criteria.getDocumentAttributeValues().size() > 0 || StringUtils.isNotBlank(criteria.getDocumentTypeName());
    }

    protected org.kuali.rice.kew.api.doctype.DocumentTypeService getApiDocumentTypeService() {
        if (apiDocumentTypeService == null) {
            apiDocumentTypeService = KewApiServiceLocator.getDocumentTypeService();
        }

        return apiDocumentTypeService;
    }

    protected void setApiDocumentTypeService(org.kuali.rice.kew.api.doctype.DocumentTypeService apiDocumentTypeService) {
        this.apiDocumentTypeService = apiDocumentTypeService;
    }

}
