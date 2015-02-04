/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.module.purap.document.dataaccess.impl;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.kuali.ole.module.purap.businessobject.NegativeInvoiceApprovalLimit;
import org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.core.framework.persistence.ojb.dao.PlatformAwareDaoBaseOjb;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * OJB Implementation of NegativeInvoiceApprovalLimitDao.
 */
@Transactional
public class NegativeInvoiceApprovalLimitDaoOjb extends PlatformAwareDaoBaseOjb implements NegativeInvoiceApprovalLimitDao {
    private static Logger LOG = Logger.getLogger(NegativeInvoiceApprovalLimitDaoOjb.class);

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao#findByChart(String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChart(String chartCode) {
        LOG.debug("Entering findByChart(String)");
        Criteria criteria = new Criteria();
        criteria.addEqualTo(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        criteria.addIsNull(OLEPropertyConstants.ORGANIZATION_CODE);
        criteria.addIsNull(OLEPropertyConstants.ACCOUNT_NUMBER);
        criteria.addAndCriteria(buildActiveCriteria());
        Query query = new QueryByCriteria(NegativeInvoiceApprovalLimit.class, criteria);
        LOG.debug("Leaving findByChart(String)");

        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao#findByChartAndAccount(String,
     *      String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChartAndAccount(String chartCode, String accountNumber) {
        LOG.debug("Entering findByChartAndAccount(String, String)");
        Criteria criteria = new Criteria();
        criteria.addEqualTo(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        criteria.addEqualTo(OLEPropertyConstants.ACCOUNT_NUMBER, accountNumber);
        criteria.addIsNull(OLEPropertyConstants.ORGANIZATION_CODE);
        criteria.addAndCriteria(buildActiveCriteria());
        Query query = new QueryByCriteria(NegativeInvoiceApprovalLimit.class, criteria);
        LOG.debug("Leaving findByChartAndAccount(String, String)");

        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao#findByChartAndOrganization(String,
     *      String)
     */
    public Collection<NegativeInvoiceApprovalLimit> findByChartAndOrganization(String chartCode, String organizationCode) {
        LOG.debug("Entering findByChartAndOrganization(String, String)");
        Criteria criteria = new Criteria();
        criteria.addEqualTo(OLEPropertyConstants.CHART_OF_ACCOUNTS_CODE, chartCode);
        criteria.addEqualTo(OLEPropertyConstants.ORGANIZATION_CODE, organizationCode);
        criteria.addIsNull(OLEPropertyConstants.ACCOUNT_NUMBER);
        criteria.addAndCriteria(buildActiveCriteria());
        Query query = new QueryByCriteria(NegativeInvoiceApprovalLimit.class, criteria);
        LOG.debug("Leaving findByChartAndOrganization(String, String)");

        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao#findAboveLimit(org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    public Collection<NegativeInvoiceApprovalLimit> findAboveLimit(KualiDecimal limit) {
        LOG.debug("Entering findAboveLimit(KualiDecimal)");
        Criteria criteria = new Criteria();
        criteria.addGreaterThan("negativeInvoiceApprovalLimitAmount", limit);
        criteria.addAndCriteria(buildActiveCriteria());
        Query query = new QueryByCriteria(NegativeInvoiceApprovalLimit.class, criteria);
        LOG.debug("Leaving findAboveLimit(KualiDecimal)");

        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * @see org.kuali.ole.module.purap.document.dataaccess.NegativeInvoiceApprovalLimitDao#findBelowLimit(org.kuali.rice.core.api.util.type.KualiDecimal)
     */
    public Collection<NegativeInvoiceApprovalLimit> findBelowLimit(KualiDecimal limit) {
        LOG.debug("Entering findBelowLimit(KualiDecimal)");
        Criteria criteria = new Criteria();
        criteria.addLessThan("negativeInvoiceApprovalLimitAmount", limit);
        criteria.addAndCriteria(buildActiveCriteria());
        Query query = new QueryByCriteria(NegativeInvoiceApprovalLimit.class, criteria);
        LOG.debug("Leaving findBelowLimit(KualiDecimal)");

        return getPersistenceBrokerTemplate().getCollectionByQuery(query);
    }

    /**
     * Builds a Criteria object for activeIndicator field set to true
     *
     * @return Criteria
     */
    protected Criteria buildActiveCriteria() {
        Criteria criteria = new Criteria();
        criteria.addEqualTo(KRADPropertyConstants.ACTIVE, true);

        return criteria;
    }
}
