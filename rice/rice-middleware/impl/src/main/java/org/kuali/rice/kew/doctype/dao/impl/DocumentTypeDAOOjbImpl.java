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
package org.kuali.rice.kew.doctype.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.ReportQueryRsIterator;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.broker.query.QueryFactory;
import org.apache.ojb.broker.query.ReportQueryByCriteria;
import org.kuali.rice.kew.doctype.DocumentTypeAttributeBo;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.dao.DocumentTypeDAO;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.springmodules.orm.ojb.OjbFactoryUtils;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;


public class DocumentTypeDAOOjbImpl extends PersistenceBrokerDaoSupport implements DocumentTypeDAO {

	public static final Logger LOG = Logger.getLogger(DocumentTypeDAOOjbImpl.class);

	public void delete(DocumentType documentType) {
		this.getPersistenceBrokerTemplate().delete(documentType);
	}

	public DocumentType findById(String docTypeId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("documentTypeId", docTypeId);
		return (DocumentType) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(DocumentType.class, crit));
	}

	public DocumentType findByName(String name) {
		return findByName(name, true);
	}

    public DocumentType findByName(String name, boolean caseSensitive) {
        Criteria crit = new Criteria();
        if (!caseSensitive) {
            if (name.contains("*") || name.contains("%")) {
                name.replace("*", "%");
                crit.addLike("UPPER(name)", name.trim().toUpperCase());
            } else {
                crit.addEqualTo("UPPER(name)", name.trim().toUpperCase());
            }
        } else {
            if (name.contains("*")) {
                name.replace("*", "%");
                crit.addLike("name", name.trim().toUpperCase());
            } else {
                crit.addEqualTo("name", name);
            }

        }
        crit.addEqualTo("currentInd", Boolean.TRUE);
        DocumentType docType = (DocumentType) this.getPersistenceBrokerTemplate().getObjectByQuery(new QueryByCriteria(
                DocumentType.class, crit));
        return docType;
    }

    public Integer getMaxVersionNumber(String docTypeName) {
		return getMostRecentDocType(docTypeName).getVersion();
	}

	public List<String> getChildDocumentTypeIds(String parentDocumentTypeId) {
		List<String> childrenIds = new ArrayList<String>();
		PersistenceBroker broker = getPersistenceBroker(false);
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			conn = broker.serviceConnectionManager().getConnection();
			st = conn.prepareStatement("select DOC_TYP_ID from KREW_DOC_TYP_T where CUR_IND = 1 and PARNT_ID = ?");
			st.setString(1, parentDocumentTypeId);
			rs = st.executeQuery();
			while (rs.next()) {
				childrenIds.add(rs.getString("DOC_TYP_ID"));
			}
		} catch (Exception e) {
			LOG.error("Error occured fetching children document type ids for document type " + parentDocumentTypeId, e);
			throw new RuntimeException(e);
		} finally {
			try {
				st.close();
			} catch (Exception e) {
				LOG.warn("Failed to close Statement", e);
			}

			try {
				rs.close();
			} catch (Exception e) {
				LOG.warn("Failed to close Resultset", e);
			}

        	if (broker != null) {
        		try {
        			OjbFactoryUtils.releasePersistenceBroker(broker, this.getPersistenceBrokerTemplate().getPbKey());
        		} catch (Exception e) {
        			LOG.error("Failed closing connection: " + e.getMessage(), e);
        		}
        	}
		}
		return childrenIds;
	}

	protected DocumentType getMostRecentDocType(String docTypeName) {
		Criteria crit = new Criteria();
		crit.addEqualTo("name", docTypeName);
		QueryByCriteria query = new QueryByCriteria(DocumentType.class, crit);
		query.addOrderByDescending("version");

		Iterator docTypes = this.getPersistenceBrokerTemplate().getCollectionByQuery(query).iterator();
		while (docTypes.hasNext()) {
			return (DocumentType) docTypes.next();
		}
		return null;
	}

	public void save(DocumentType documentType) {
		this.getPersistenceBrokerTemplate().store(documentType);
	}

	public List findByDocumentId(String documentId) {
		Criteria crit = new Criteria();
		crit.addEqualTo("documentId", documentId);
		return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(DocumentType.class, crit));
	}

	public Collection<DocumentType> find(DocumentType documentType, DocumentType docTypeParent, boolean climbHierarchy) {
		LOG.debug("documentType: "+ documentType);
		LOG.debug("docTypeParent: "+ docTypeParent);
		LOG.debug("climbHierarchy: " + climbHierarchy);

		Criteria crit = new Criteria();
		if (documentType != null && !org.apache.commons.lang.StringUtils.isEmpty(documentType.getLabel())) {
			crit.addLike("UPPER(label)", documentType.getLabel().trim().toUpperCase());
		}
		if (documentType != null && !org.apache.commons.lang.StringUtils.isEmpty(documentType.getName())) {
			String docTypeName = documentType.getName();
			crit.addLike("UPPER(name)", ("%" + docTypeName.trim() + "%").toUpperCase());
		}
		if (documentType != null && documentType.getActive() != null) {
			crit.addEqualTo("active", documentType.getActive());
		}
		if (documentType != null && documentType.getDocumentTypeId() != null) {
			crit.addEqualTo("documentTypeId", documentType.getDocumentTypeId());
		}
		if (documentType != null && documentType.getActualApplicationId() != null){
			crit.addEqualTo("actualApplicationIde", documentType.getActualApplicationId());
		}
		if (docTypeParent != null) {
			if (!"".equals(docTypeParent.getName()) && docTypeParent.getName() != null) {
				Criteria parentCrit = new Criteria();
				//addParentNameOrCriteria(docTypeParent.getName(), parentCrit);
				addParentIdOrCriteria(docTypeParent.getDocumentTypeId(), parentCrit);
				if (climbHierarchy) {
					assembleChildrenCriteria(docTypeParent.getChildrenDocTypes(), parentCrit);
				}
				parentCrit.addEqualTo("currentInd", Boolean.TRUE);
				crit.addAndCriteria(parentCrit);
			}
		} else {
			if (documentType != null && !org.apache.commons.lang.StringUtils.isEmpty(documentType.getName())) {
				DocumentType searchDocumentType = findByName(documentType.getName());
				if ((searchDocumentType != null) && climbHierarchy) {
					LOG.debug("searchDocumentType: "+ searchDocumentType);
					Criteria criteria = new Criteria();
					//addParentNameOrCriteria(searchDocumentType.getName(), criteria);
                    addParentIdOrCriteria(searchDocumentType.getDocumentTypeId(), criteria);
                    assembleChildrenCriteria(searchDocumentType.getChildrenDocTypes(), criteria);
					criteria.addEqualTo("currentInd", Boolean.TRUE);
					crit.addOrCriteria(criteria);
				}
			}
		}
		crit.addEqualTo("currentInd", Boolean.TRUE);
		return (Collection<DocumentType>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(DocumentType.class, crit));
	}

    private void addParentIdOrCriteria(String parentId, Criteria mainCriteria) {
        Criteria parentCriteria = new Criteria();
        parentCriteria.addEqualTo("docTypeParentId", parentId);
        mainCriteria.addOrCriteria(parentCriteria);
    }

	private void assembleChildrenCriteria(Collection childrenDocTypes, Criteria crit) {
		if (childrenDocTypes != null) {
			Iterator childrenDocTypesIter = childrenDocTypes.iterator();
			while (childrenDocTypesIter.hasNext()) {
				DocumentType child = (DocumentType) childrenDocTypesIter.next();
				addParentIdOrCriteria(child.getDocumentTypeId(), crit);
				assembleChildrenCriteria(child.getChildrenDocTypes(), crit);
			}
		}
	}

	public List<DocumentType> findAllCurrentRootDocuments() {
		Criteria crit = new Criteria();
		crit.addIsNull("docTypeParentId");
		return findAllCurrent(crit);
	}

    public List<DocumentType> findAllCurrent() {
	return findAllCurrent(new Criteria());
    }

    public List<DocumentType> findAllCurrentByName(String name) {
	Criteria crit = new Criteria();
	crit.addEqualTo("name", name);
	return findAllCurrent(crit);
    }

    public List<DocumentType> findPreviousInstances(String documentTypeName) {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", documentTypeName);
        crit.addEqualTo("currentInd", Boolean.FALSE);
        return findAll(crit);
    }

    private List<DocumentType> findAllCurrent(Criteria crit) {
        crit.addEqualTo("currentInd", Boolean.TRUE);
        return findAll(crit);
    }

    private List<DocumentType> findAll(Criteria crit) {
        return (List<DocumentType>) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(DocumentType.class, crit));
    }

    public List findDocumentTypeAttributes(RuleAttribute ruleAttribute) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("ruleAttributeId", ruleAttribute.getId());
    	return (List) this.getPersistenceBrokerTemplate().getCollectionByQuery(new QueryByCriteria(DocumentTypeAttributeBo.class, crit));
    }

    public String findDocumentTypeIdByDocumentId(String documentId) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("documentId", documentId);
    	ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentRouteHeaderValue.class, crit);
    	query.setAttributes(new String[] { "documentTypeId" });

    	ReportQueryRsIterator iter = (ReportQueryRsIterator)getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        try {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                return (String)row[0];
            }
    	} finally {
    	    iter.releaseDbResources();
    	}
    	return null;
    }
    
    public String findDocumentTypeIdByName(String documentTypeName) {
    	Criteria crit = new Criteria();
    	crit.addEqualTo("name", documentTypeName);
        crit.addEqualTo("currentInd", Boolean.TRUE);
    	ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentType.class, crit);
    	query.setAttributes(new String[] { "documentTypeId" });

    	ReportQueryRsIterator iter = (ReportQueryRsIterator)getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
    	try {
    		while (iter.hasNext()) {
    			Object[] row = (Object[]) iter.next();
    			return (String)row[0];
    		}
    	} finally {
    		iter.releaseDbResources();
    	}
    	return null;
    }
    
    public String findDocumentTypeNameById(String documentTypeId) {
        Criteria crit = new Criteria();
        crit.addEqualTo("documentTypeId", documentTypeId);
        ReportQueryByCriteria query = QueryFactory.newReportQuery(DocumentType.class, crit);
        query.setAttributes(new String[] { "name" });

        ReportQueryRsIterator iter = (ReportQueryRsIterator)getPersistenceBrokerTemplate().getReportQueryIteratorByQuery(query);
        try {
            while (iter.hasNext()) {
                Object[] row = (Object[]) iter.next();
                return (String)row[0];
            }
        } finally {
            iter.releaseDbResources();
        }
        return null;
    }

}
