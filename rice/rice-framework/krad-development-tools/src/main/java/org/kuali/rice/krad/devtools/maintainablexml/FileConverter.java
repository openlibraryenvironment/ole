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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krad.devtools.maintainablexml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Used to upgrade the maintenance document xml stored in krns_maint_doc_t.doc_cntnt
 * to be able to still open and use any maintenance documents that were enroute at the time of an upgrade to Rice 2.0.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */

public class FileConverter {

    private JdbcTemplate jdbcTemplate;
    private int totalDocs = 0;

    /**
     * Selects all the encrypted xml documents from krns_maint_doc_t, decrypts them, runs the rules to upgrade them,
     * encrypt them and update krns_maint_doc_t with the upgraded xml.
     *
     * @param settingsMap - the settings
     * @throws Exception
     */
    public void runFileConversion(HashMap settingsMap, final String runMode, final String fromRange,
            final String toRange, final boolean hasRangeParameters) throws Exception {

        final EncryptionService encryptService = new EncryptionService((String) settingsMap.get("encryption.key"));

        String docSQL = "SELECT DOC_HDR_ID, DOC_CNTNT FROM krns_maint_doc_t ";

        // If user entered range add the sql parameters and filter results because DOC_HDR_ID is a varchar field.
        if (hasRangeParameters) {
            docSQL = docSQL.concat(" WHERE DOC_HDR_ID >= '" + fromRange + "' AND DOC_HDR_ID <= '" + toRange + "'");
        }
        System.out.println("SQL to run:"  + docSQL);

        jdbcTemplate = new JdbcTemplate(getDataSource(settingsMap));
        jdbcTemplate.query(docSQL, new RowCallbackHandler() {

            public void processRow(ResultSet rs) throws SQLException {
                // Check that all docId's is in range
                if (hasRangeParameters) {
                    int docId = Integer.parseInt(rs.getString(1));
                    if (docId >= Integer.parseInt(fromRange) && docId <= Integer.parseInt(toRange)) {
                        processDocumentRow(rs.getString(1), rs.getString(2), encryptService, runMode);
                    }
                } else {
                    processDocumentRow(rs.getString(1), rs.getString(2), encryptService, runMode);
                }
            }
        });

        System.out.println(totalDocs + " maintenance documents upgraded.");

    }

    /**
     * Creates the data source from the settings map
     *
     * @param settingsMap - the settingMap containing the db connection settings
     * @return the DataSource object
     */
    public static DataSource getDataSource(HashMap settingsMap) {
        String driver;
        if ("MySQL".equals(settingsMap.get("datasource.ojb.platform"))) {
            driver = "com.mysql.jdbc.Driver";
        } else if ("Oracle9i".equals(settingsMap.get("datasource.ojb.platform"))) {
            driver = "oracle.jdbc.driver.OracleDriver";
        } else {
            driver = (String) settingsMap.get("datasource.driver.name");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl((String) settingsMap.get("datasource.url"));
        dataSource.setUsername((String) settingsMap.get("datasource.username"));
        dataSource.setPassword((String) settingsMap.get("datasource.password"));
        return dataSource;
    }

    /**
     * Called for each row in the processRow method of the spring query. Upgrades the xml and update the
     * krns_maint_doc_t table.
     *
     * @param docId - the document id string
     * @param docCntnt - the old xml string
     * @param encryptServ - the encryption service used to encrypt/decrypt the xml
     */
    public void processDocumentRow(String docId, String docCntnt, EncryptionService encryptServ, String runMode) {
        System.out.println(docId);
        try {
            String oldXml = docCntnt;
            if (encryptServ.isEnabled()) {
                oldXml = encryptServ.decrypt(docCntnt);
            }
            if ("2".equals(runMode)) {
                System.out.println("------ ORIGINAL DOC XML --------");
                System.out.println(oldXml);
                System.out.println("--------------------------------");
            }

            MaintainableXMLConversionServiceImpl maintainableXMLConversionServiceImpl = new MaintainableXMLConversionServiceImpl();
            String newXML = maintainableXMLConversionServiceImpl.transformMaintainableXML(oldXml);

            if ("2".equals(runMode)) {
                System.out.println("******* UPGRADED DOC XML ********");
                System.out.println(newXML);
                System.out.println("*********************************\n");
            }
            if ("1".equals(runMode)) {
                 if (encryptServ.isEnabled()) {
                    jdbcTemplate.update("update krns_maint_doc_t set DOC_CNTNT = ? where DOC_HDR_ID = ?",
                        new Object[]{encryptServ.encrypt(newXML), docId});
                 } else {
                     jdbcTemplate.update("update krns_maint_doc_t set DOC_CNTNT = ? where DOC_HDR_ID = ?",
                             new Object[]{newXML, docId});
                 }
            }
            totalDocs++;
        } catch (Exception ex) {
            Logger.getLogger(FileConverter.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }
}
