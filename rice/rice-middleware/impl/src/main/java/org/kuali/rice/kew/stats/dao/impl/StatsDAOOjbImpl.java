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
package org.kuali.rice.kew.stats.dao.impl;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.stats.Stats;
import org.kuali.rice.kew.stats.dao.StatsDAO;
import org.kuali.rice.kew.api.KewApiConstants;
import org.springmodules.orm.ojb.support.PersistenceBrokerDaoSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatsDAOOjbImpl extends PersistenceBrokerDaoSupport implements StatsDAO {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(StatsDAOOjbImpl.class);

    public static final String SQL_NUM_ACTIVE_ITEMS = "select count(*) from krew_actn_itm_t";
    public static final String SQL_NUM_DOC_TYPES_REPORT = "select count(*) as num from krew_doc_typ_t where cur_ind = 1";
    public static final String SQL_DOCUMENTS_ROUTED = "select count(*) as count, krew_doc_hdr_t.doc_hdr_stat_cd from krew_doc_hdr_t where krew_doc_hdr_t.crte_dt between ? and ? group by doc_hdr_stat_cd";
    public static final String SQL_NUM_USERS = "select count(distinct prncpl_id) as prsn_count from KREW_USR_OPTN_T";
    public static final String SQL_NUM_DOCS_INITIATED = "select count(*), krew_doc_typ_t.doc_typ_nm from krew_doc_hdr_t, krew_doc_typ_t where krew_doc_hdr_t.crte_dt > ? and krew_doc_hdr_t.doc_typ_id = krew_doc_typ_t.doc_typ_id group by krew_doc_typ_t.doc_typ_nm";

    @Override
	public void NumActiveItemsReport(Stats stats) throws SQLException, LookupException {

        LOG.debug("NumActiveItemsReport()");
        PersistenceBroker broker = this.getPersistenceBroker(false);
        Connection conn = broker.serviceConnectionManager().getConnection();
        PreparedStatement ps = conn.prepareStatement(StatsDAOOjbImpl.SQL_NUM_ACTIVE_ITEMS);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stats.setNumActionItems(new Integer(rs.getInt(1)).toString());
        }

        closeDatabaseObjects(rs, ps, conn/*, broker*/);
    }

    @Override
	public void NumberOfDocTypesReport(Stats stats) throws SQLException, LookupException {

        LOG.debug("NumberOfDocTypesReport()");
        PersistenceBroker broker = this.getPersistenceBroker(false);
        Connection conn = broker.serviceConnectionManager().getConnection();
        PreparedStatement ps = conn.prepareStatement(StatsDAOOjbImpl.SQL_NUM_DOC_TYPES_REPORT);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stats.setNumDocTypes(new Integer(rs.getInt(1)).toString());
        }

        closeDatabaseObjects(rs, ps, conn/*, broker*/);
    }

    @Override
	public void DocumentsRoutedReport(Stats stats, Date begDate, Date endDate) throws SQLException, LookupException {

        LOG.debug("DocumentsRoutedReport()");
        PersistenceBroker broker = this.getPersistenceBroker(false);
        Connection conn = broker.serviceConnectionManager().getConnection();
        PreparedStatement ps = conn.prepareStatement(StatsDAOOjbImpl.SQL_DOCUMENTS_ROUTED);
        ps.setTimestamp(1, new Timestamp(begDate.getTime()));
        ps.setTimestamp(2, new Timestamp(endDate.getTime()));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String actionType = rs.getString(2);
            String number = new Integer(rs.getInt(1)).toString();
            if (actionType.equals(KewApiConstants.ROUTE_HEADER_CANCEL_CD)) {
                stats.setCanceledNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_DISAPPROVED_CD)) {
                stats.setDisapprovedNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_ENROUTE_CD)) {
                stats.setEnrouteNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_EXCEPTION_CD)) {
                stats.setExceptionNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_FINAL_CD)) {
                stats.setFinalNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_INITIATED_CD)) {
                stats.setInitiatedNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_PROCESSED_CD)) {
                stats.setProcessedNumber(number);
            } else if (actionType.equals(KewApiConstants.ROUTE_HEADER_SAVED_CD)) {
                stats.setSavedNumber(number);
            }
        }

        closeDatabaseObjects(rs, ps, conn/*, broker*/);
    }

    @Override
	public void NumUsersReport(Stats stats) throws SQLException, LookupException {

        LOG.debug("NumUsersReport()");
        PersistenceBroker broker = this.getPersistenceBroker(false);
        Connection conn = broker.serviceConnectionManager().getConnection();
        PreparedStatement ps = conn.prepareStatement(StatsDAOOjbImpl.SQL_NUM_USERS);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            stats.setNumUsers(new Integer(rs.getInt("prsn_count")).toString());
        }

        closeDatabaseObjects(rs, ps, conn/*, broker*/);
    }

    @Override
	public void NumInitiatedDocsByDocTypeReport(Stats stats) throws SQLException, LookupException {

        LOG.debug("NumInitiatedDocsByDocType()");
        PersistenceBroker broker = this.getPersistenceBroker(false);
        Connection conn = broker.serviceConnectionManager().getConnection();
        PreparedStatement ps = conn.prepareStatement(StatsDAOOjbImpl.SQL_NUM_DOCS_INITIATED);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -29);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        ps.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
        ResultSet rs = ps.executeQuery();

        List<KeyValue> numDocs = new ArrayList<KeyValue>();

        while (rs.next()) {
            numDocs.add(new ConcreteKeyValue(rs.getString(2), new Integer(rs.getInt(1)).toString()));
        }
        stats.setNumInitiatedDocsByDocType(numDocs);

        closeDatabaseObjects(rs, ps, conn/*, broker*/);

    }

    private void closeDatabaseObjects(ResultSet rs, PreparedStatement ps, Connection conn/*, PersistenceBroker broker*/) {

        try {
            rs.close();
        } catch (SQLException ex) {
            LOG.warn("Failed to close ResultSet.", ex);
        }

        try {
            ps.close();
        } catch (SQLException ex) {
            LOG.warn("Failed to close PreparedStatement.", ex);
        }

        try {
            conn.close();
        } catch (SQLException ex) {
            LOG.warn("Failed to close Connection.", ex);
        }
/*
        try {
            broker.close();
        } catch (Exception ex) {
            LOG.warn("Failed to close broker.", ex);
        }
*/
    }

}
