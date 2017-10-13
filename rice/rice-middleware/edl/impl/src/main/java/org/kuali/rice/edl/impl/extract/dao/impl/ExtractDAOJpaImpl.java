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
package org.kuali.rice.edl.impl.extract.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.edl.impl.extract.Dump;
import org.kuali.rice.edl.impl.extract.Fields;
import org.kuali.rice.edl.impl.extract.dao.ExtractDAO;
import org.kuali.rice.kew.notes.Note;

public class ExtractDAOJpaImpl implements ExtractDAO {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ExtractDAOJpaImpl.class);

    @PersistenceContext(unitName = "kew-unit")
    private EntityManager entityManager;

    public Dump getDumpByDocumentId(String docId) {
        LOG.debug("finding Document Extract by documentId " + docId);
        Criteria crit = new Criteria(Dump.class.getName());
        crit.eq("docId", docId);
        return (Dump) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

    public List<Fields> getFieldsByDocumentId(String docId) {
        LOG.debug("finding Extract Fileds by documentId " + docId);
        Criteria crit = new Criteria(Fields.class.getName());
        crit.eq("documentId", docId);
        crit.orderBy("docId", true);

        return (List<Fields>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    public void saveDump(Dump dump) {
        LOG.debug("check for null values in Extract document");
        checkNull(dump.getDocId(), "Document ID");
        checkNull(dump.getDocCreationDate(), "Creation Date");
        checkNull(dump.getDocCurrentNodeName(), "Current Node Name");
        checkNull(dump.getDocModificationDate(), "Modification Date");
        checkNull(dump.getDocRouteStatusCode(), "Route Status Code");
        checkNull(dump.getDocInitiatorId(), "Initiator ID");
        checkNull(dump.getDocTypeName(), "Doc Type Name");
        LOG.debug("saving EDocLite document: routeHeader " + dump.getDocId());
        if (dump.getDocId() == null) {
            entityManager.persist(dump);
        } else {
            OrmUtils.merge(entityManager, dump);
        }
    }

    public void saveField(Fields field) {
        LOG.debug("saving EDocLite Extract fields");
        checkNull(field.getDocId(), "Document ID");
        checkNull(field.getFieldValue(), "Field Value");
        checkNull(field.getFiledName(), "Field Name");
        LOG.debug("saving Fields: routeHeader " + field.getFieldId());

        if (field.getFieldId() == null) {
            entityManager.persist(field);
        } else {
            OrmUtils.merge(entityManager, field);
        }
    }

    private void checkNull(Object value, String valueName) throws RuntimeException {
        if (value == null) {
            throw new RuntimeException("Null value for " + valueName);
        }
    }

    public void deleteDump(String documentId) {
        LOG.debug("deleting record form Extract Dump table");
        entityManager.remove(entityManager.find(Note.class, documentId));
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
