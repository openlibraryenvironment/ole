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
package org.kuali.rice.edl.impl.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.core.framework.persistence.jpa.criteria.Criteria;
import org.kuali.rice.core.framework.persistence.jpa.criteria.QueryByCriteria;
import org.kuali.rice.edl.impl.bo.EDocLiteAssociation;
import org.kuali.rice.edl.impl.bo.EDocLiteDefinition;
import org.kuali.rice.edl.impl.dao.EDocLiteDAO;

/**
 * JPA implementation of the EDOcLiteDAO
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class EDocLiteDAOJpaImpl implements EDocLiteDAO {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(EDocLiteDAOJpaImpl.class);

    private static final String ACTIVE_IND_CRITERIA = "activeInd";
    private static final String NAME_CRITERIA = "name";

    @PersistenceContext(unitName = "kew-unit")
    private EntityManager entityManager;

    /**
     * Save a EDocLiteDefinition
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#saveEDocLiteDefinition(org.kuali.rice.edl.impl.bo.EDocLiteDefinition)
     */
    public void saveEDocLiteDefinition(final EDocLiteDefinition edocLiteData) {
        if (edocLiteData.getEDocLiteDefId() == null) {
            entityManager.persist(edocLiteData);
        } else {
            OrmUtils.merge(entityManager, edocLiteData);
        }
    }

    /**
     * Save a EDocLiteAssocitaion
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#saveEDocLiteAssociation(org.kuali.rice.edl.impl.bo.EDocLiteAssociation)
     */
    public void saveEDocLiteAssociation(final EDocLiteAssociation assoc) {
        if (assoc.getEdocLiteAssocId() == null) {
            entityManager.persist(assoc);
        } else {
            OrmUtils.merge(entityManager, assoc);
        }
    }

    /**
     * Get a EDocLiteDefinition
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#getEDocLiteDefinition(java.lang.String)
     */
    public EDocLiteDefinition getEDocLiteDefinition(final String defName) {
        final Criteria crit = new Criteria(EDocLiteDefinition.class.getName());
        crit.eq(NAME_CRITERIA, defName);
        crit.eq(ACTIVE_IND_CRITERIA, Boolean.TRUE);
        return (EDocLiteDefinition) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

    /**
     * Get a EDocLiteAssociation
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#getEDocLiteAssociation(java.lang.String)
     */
    public EDocLiteAssociation getEDocLiteAssociation(final String docTypeName) {
        final Criteria crit = new Criteria(EDocLiteAssociation.class.getName());
        crit.eq("edlName", docTypeName);
        crit.eq(ACTIVE_IND_CRITERIA, Boolean.TRUE);
        return (EDocLiteAssociation) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

    /**
     * Returns the names of all active Definitions
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#getEDocLiteDefinitions()
     */
    @SuppressWarnings("unchecked")
    public List<String> getEDocLiteDefinitions() {
        final Criteria crit = new Criteria(EDocLiteDefinition.class.getName());
        crit.eq(ACTIVE_IND_CRITERIA, Boolean.TRUE);

        final List<EDocLiteDefinition> defs = (List<EDocLiteDefinition>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
        final ArrayList<String> names = new ArrayList<String>(defs.size());
        for (EDocLiteDefinition def : defs) {
            names.add(def.getName());
        }
        return names;
    }

    /**
     * Returns all active Definitions
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#getEDocLiteAssociations()
     */
    @SuppressWarnings("unchecked")
    public List<EDocLiteAssociation> getEDocLiteAssociations() {
        final Criteria crit = new Criteria(EDocLiteAssociation.class.getName());
        crit.eq(ACTIVE_IND_CRITERIA, Boolean.TRUE);
        return (List<EDocLiteAssociation>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    /**
     * Finds matching Associations
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#search(org.kuali.rice.edl.impl.bo.EDocLiteAssociation)
     */
    @SuppressWarnings("unchecked")
    public List<EDocLiteAssociation> search(final EDocLiteAssociation edocLite) {
        final Criteria crit = new Criteria(EDocLiteAssociation.class.getName());
        if (edocLite.getActiveInd() != null) {
            crit.eq(ACTIVE_IND_CRITERIA, edocLite.getActiveInd());
        }
        if (edocLite.getDefinition() != null) {
            crit.like("UPPER(definition)", "%" + edocLite.getDefinition().toUpperCase() + "%");
        }
        if (edocLite.getEdlName() != null) {
            crit.like("UPPER(edlName)", "%" + edocLite.getEdlName().toUpperCase() + "%");
        }
        if (edocLite.getStyle() != null) {
            crit.like("UPPER(style)", "%" + edocLite.getStyle().toUpperCase() + "%");
        }
        return (List<EDocLiteAssociation>) new QueryByCriteria(entityManager, crit).toQuery().getResultList();
    }

    /**
     * Returns a specific Association
     *
     * @see org.kuali.rice.edl.impl.dao.EDocLiteDAO#getEDocLiteAssociation(java.lang.Long)
     */
    public EDocLiteAssociation getEDocLiteAssociation(final Long associationId) {
        final Criteria crit = new Criteria(EDocLiteAssociation.class.getName());
        crit.eq("edocLiteAssocId", associationId);
        return (EDocLiteAssociation) new QueryByCriteria(entityManager, crit).toQuery().getSingleResult();
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
