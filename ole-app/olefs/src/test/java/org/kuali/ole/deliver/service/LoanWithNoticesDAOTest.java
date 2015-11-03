package org.kuali.ole.deliver.service;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 4/9/15.
 */
public class LoanWithNoticesDAOTest {

    @Test
    public void getLoanIdsForOverudeNoticesForMySQL() throws Exception {
        LoanWithNoticesDAO loanWithNoticesDAO = new MockLoanWithNoticesDAOForMySQL();
        List<String> overdueNotice = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType("3/3/2015", "Overdue Notice");
        assertNotNull(overdueNotice);
        assertTrue(overdueNotice.isEmpty());
    }

    @Test
    public void getLoanIdsForOverudeNoticesForMySQLWithNullDate() throws Exception {
        LoanWithNoticesDAO loanWithNoticesDAO = new MockLoanWithNoticesDAOForMySQL();
        List<String> overdueNotice = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(null, "Overdue Notice");
        assertNotNull(overdueNotice);
        assertTrue(overdueNotice.isEmpty());
    }

    @Test
    public void MockLoanWithNoticesDAOForOracle() throws Exception {
        LoanWithNoticesDAO loanWithNoticesDAO = new MockLoanWithNoticesDAOForOracle();
        List<String> overdueNotice = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType("3/3/2015", "Overdue Notice");
        assertNotNull(overdueNotice);
        assertTrue(overdueNotice.isEmpty());
    }

    @Test
    public void MockLoanWithNoticesDAOForOracleWithNullDate() throws Exception {
        LoanWithNoticesDAO loanWithNoticesDAO = new MockLoanWithNoticesDAOForOracle();
        List<String> overdueNotice = loanWithNoticesDAO.getLoanIdsForNoticesByNoticeType(null, "Overdue Notice");
        assertNotNull(overdueNotice);
        assertTrue(overdueNotice.isEmpty());
    }



    class MockLoanWithNoticesDAOForMySQL extends  LoanWithNoticesDAO {
        @Override
        protected String getProperty(String property) {
            if (property.equals("db.vendor")){
                return "mysql";
            }
            return null;
        }

        @Override
        protected List<Map<String, Object>> executeQuery(String query) {
            System.out.println(query);
            return new ArrayList<>();
        }
    }

    class MockLoanWithNoticesDAOForOracle extends  LoanWithNoticesDAO {
        @Override
        protected String getProperty(String property) {
            if (property.equals("db.vendor")){
                return "oracle";
            }
            return null;
        }

        @Override
        protected List<Map<String, Object>> executeQuery(String query) {
            System.out.println(query);
            return new ArrayList<>();
        }
    }
}