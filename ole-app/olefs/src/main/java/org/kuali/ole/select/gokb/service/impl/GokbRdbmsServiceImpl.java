package org.kuali.ole.select.gokb.service.impl;

import org.kuali.ole.docstore.common.util.DataSource;
import org.kuali.ole.select.gokb.*;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by premkumarv on 12/9/14.
 */
public class GokbRdbmsServiceImpl implements GokbRdbmsService {

    private static final Logger LOG = LoggerFactory.getLogger(GokbLocalServiceImpl.class);

    private Connection connection = null;

    private static Connection getConnection() throws SQLException {
        DataSource dataSource = null;
        try {
            dataSource = DataSource.getInstance();
        } catch (IOException e) {
            LOG.error("Exception While getting the connection: " + e);
        } catch (SQLException e) {
            LOG.error("Exception While getting the connection: " + e);
        } catch (PropertyVetoException e) {
            LOG.error("Exception While getting the connection: " + e);
        }
        return dataSource.getConnection();
    }

    /**
     * Insert or Update list of Packages
     *
     * @param oleGokbPackages
     */

    public void insertOrUpdatePackages(List<OleGokbPackage> oleGokbPackages) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_PACKAGE_PREPARED_STMT);
            for (OleGokbPackage oleGokbPack : oleGokbPackages) {
                buildPackage(pstmt, oleGokbPack);
                try {
                    pstmt.execute();
                } catch (SQLException ex) {
                    if (!ex.getMessage().isEmpty() && ex.getMessage().contains("Duplicate entry")) {
                        updatePackage(oleGokbPack);
                    }
                }
            }
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Package: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * @param oleGokbTipps Insert or Update list of Tipps
     */

    public void insertOrUpdateTipps(List<OleGokbTipp> oleGokbTipps) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_TIPP_PREPARED_STMT);
            for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                buildTipp(pstmt, oleGokbTipp);
                try {
                    pstmt.execute();
                } catch (SQLException ex) {
                    if (!ex.getMessage().isEmpty() && ex.getMessage().contains("Duplicate entry")) {
                        updateTipp(oleGokbTipp);
                    }
                }
            }
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Tipp: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);

            }
        }
    }

    /**
     * Insert or Update list of Titles
     *
     * @param oleGokbTitles
     */
    public void insertOrUpdateTitles(List<OleGokbTitle> oleGokbTitles) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_TITLE_PREPARED_STMT);
            for (OleGokbTitle oleGokbTitle : oleGokbTitles) {
                buildTitle(pstmt, oleGokbTitle);
                try {
                    pstmt.execute();
                } catch (SQLException ex) {
                    if (!ex.getMessage().isEmpty() && ex.getMessage().contains("Duplicate entry")) {
                        updateTitle(oleGokbTitle);
                    }
                }
            }
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Title: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Insert or Update list of Platforms
     *
     * @param oleGokbPlatforms
     */
    public void insertOrUpdatePlatforms(List<OleGokbPlatform> oleGokbPlatforms) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_PLATFORM_PREPARED_STMT);
            for (OleGokbPlatform oleGokbPlatform : oleGokbPlatforms) {
                buildPlatform(pstmt, oleGokbPlatform);
                try {
                    pstmt.execute();
                } catch (SQLException ex) {
                    if (!ex.getMessage().isEmpty() && ex.getMessage().contains("Duplicate entry")) {
                        updatePlatform(oleGokbPlatform);
                    }
                }
            }
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Platform: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Insert or Update list of Organizations
     *
     * @param oleGokbOrganizations
     */
    public void insertOrUpdateOrganizations(List<OleGokbOrganization> oleGokbOrganizations) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_ORG_PREPARED_STMT);
            for (OleGokbOrganization oleGokbOrganization : oleGokbOrganizations) {
                buildOrganization(pstmt, oleGokbOrganization);
                try {
                    pstmt.execute();
                } catch (SQLException ex) {
                    if (!ex.getMessage().isEmpty() && ex.getMessage().contains("Duplicate entry")) {
                        updateOrganization(oleGokbOrganization);
                    }
                }
            }
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Organization: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Insert or Update list of OrganizationRoles
     *
     * @param oleGokbOrganizationRoles
     */
    public void insertOrUpdateOrganizationRoles(List<OleGokbOrganizationRole> oleGokbOrganizationRoles) {
        Integer lastInsertedId = 0;
        boolean empty = true;
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(INSERT_ORG_ROLE_PREPARED_STMT);

            ResultSet  rs1 = stmt.executeQuery("SELECT GOKB_ORG_ROLE_ID FROM OLE_GOKB_ORG_ROLE_T ORDER BY GOKB_ORG_ROLE_ID DESC LIMIT 1");
            while (rs1.next()) {
                lastInsertedId = rs1.getInt(1);
            }


            for (OleGokbOrganizationRole oleGokbOrganizationRole : oleGokbOrganizationRoles) {
                ResultSet  rs = stmt.executeQuery("SELECT * FROM OLE_GOKB_ORG_ROLE_T WHERE GOKB_ORG_ID=" + oleGokbOrganizationRole.getGokbOrganizationId() + " AND ROLE='" + oleGokbOrganizationRole.getRole() + "'");
                while (!rs.next()) {
                    lastInsertedId++;
                    oleGokbOrganizationRole.setGokbOrgRoleId(lastInsertedId);
                    buildOrganizationRole(pstmt, oleGokbOrganizationRole);
                    pstmt.execute();
                    break;
                }
            }
            connection.commit();
            pstmt.close();
            stmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Inserting the Organization Role: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Update package, if it exist already
     *
     * @param oleGokbPack
     */
    public void updatePackage(OleGokbPackage oleGokbPack) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_PACKAGE_PREPARED_STMT);
            pstmt.setString(1, oleGokbPack.getPackageName());
            pstmt.setString(2, oleGokbPack.getVariantName());
            pstmt.setString(3, oleGokbPack.getStatus());
            pstmt.setString(4, oleGokbPack.getPackageScope());
            pstmt.setString(5, oleGokbPack.getBreakable());
            pstmt.setString(6, oleGokbPack.getFixed());
            pstmt.setString(7, oleGokbPack.getAvailability());
            pstmt.setTimestamp(8, oleGokbPack.getDateCreated());
            pstmt.setTimestamp(9, oleGokbPack.getDateUpdated());
            pstmt.setInt(10, oleGokbPack.getGokbPackageId());
            pstmt.execute();
            connection.commit();
            pstmt.close();

        } catch (SQLException ex) {
            LOG.error("Exception while Update the Package: " + ex);
        }

    }

    /**
     * Update Tipp, if it exist already
     *
     * @param oleGokbTipp
     */
    public void updateTipp(OleGokbTipp oleGokbTipp) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_TIPP_PREPARED_STMT);
            pstmt.setInt(1, oleGokbTipp.getGokbPackageId());
            pstmt.setInt(2, oleGokbTipp.getGokbTitleId());
            pstmt.setInt(3, oleGokbTipp.getGokbPlatformId());
            pstmt.setString(4, oleGokbTipp.getStatus());
            pstmt.setString(5, oleGokbTipp.getStatusReason());
            pstmt.setTimestamp(6, oleGokbTipp.getStartdate());
            pstmt.setString(7, oleGokbTipp.getStartVolume());
            pstmt.setString(8, oleGokbTipp.getStartIssue());
            pstmt.setTimestamp(9, oleGokbTipp.getEndDate());
            pstmt.setString(10, oleGokbTipp.getEndVolume());
            pstmt.setString(11, oleGokbTipp.getEndIssue());
            pstmt.setString(12, oleGokbTipp.getEmbarco());
            pstmt.setString(13, oleGokbTipp.getPlatformHostUrl());
            pstmt.setTimestamp(14, oleGokbTipp.getDateCreated());
            pstmt.setTimestamp(15, oleGokbTipp.getDateUpdated());
            pstmt.setInt(16, oleGokbTipp.getGokbTippId());
            pstmt.execute();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the Tipp: " + ex);
        }
    }

    /**
     * Update Title, if it exist already
     *
     * @param oleGokbTitle
     */
    public void updateTitle(OleGokbTitle oleGokbTitle) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_TITLE_PREPARED_STMT);
            pstmt.setString(1, oleGokbTitle.getTitleName());
            pstmt.setString(2, oleGokbTitle.getVariantName());
            pstmt.setString(3, oleGokbTitle.getMedium());
            pstmt.setString(4, oleGokbTitle.getPureQa());
            pstmt.setString(5, oleGokbTitle.getIssnOnline());
            pstmt.setString(6, oleGokbTitle.getIssnPrint());
            pstmt.setString(7, oleGokbTitle.getIssnL());
            pstmt.setInt(8, oleGokbTitle.getOclcNumber());
            pstmt.setString(9, oleGokbTitle.getDoi());
            pstmt.setInt(10, oleGokbTitle.getProprietaryId());
            pstmt.setString(11, oleGokbTitle.getSuncat());
            pstmt.setString(12, oleGokbTitle.getLccn());
            pstmt.setInt(13, oleGokbTitle.getPublisherId());
            pstmt.setInt(14, oleGokbTitle.getImprint());
            pstmt.setTimestamp(15, oleGokbTitle.getDateCreated());
            pstmt.setTimestamp(16, oleGokbTitle.getDateUpdated());
            pstmt.setInt(17, oleGokbTitle.getGokbTitleId());
            pstmt.execute();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the Title: " + ex);
        }
    }

    /**
     * Update Platform, if it exist already
     *
     * @param oleGokbPlatform
     */
    public void updatePlatform(OleGokbPlatform oleGokbPlatform) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_PLATFORM_PREPARED_STMT);
            pstmt.setString(1, oleGokbPlatform.getPlatformName());
            pstmt.setString(2, oleGokbPlatform.getStatus());
            pstmt.setInt(3, oleGokbPlatform.getPlatformProviderId());
            pstmt.setString(4, oleGokbPlatform.getAuthentication());
            pstmt.setString(5, oleGokbPlatform.getSoftwarePlatform());
            pstmt.setTimestamp(6, oleGokbPlatform.getDateCreated());
            pstmt.setTimestamp(7, oleGokbPlatform.getDateUpdated());
            pstmt.setInt(8, oleGokbPlatform.getGokbPlatformId());
            pstmt.execute();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the Platform: " + ex);
        }
    }

    /**
     * Update Organization, if it exist already
     *
     * @param oleGokbOrganization
     */
    public void updateOrganization(OleGokbOrganization oleGokbOrganization) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_ORG_PREPARED_STMT);
            pstmt.setString(1, oleGokbOrganization.getOrganizationName());
            pstmt.setString(2, oleGokbOrganization.getVariantName());
            pstmt.setTimestamp(3, oleGokbOrganization.getDateCreated());
            pstmt.setTimestamp(4, oleGokbOrganization.getDateUpdated());
            pstmt.setInt(5, oleGokbOrganization.getGokbOrganizationId());
            pstmt.execute();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the Organization: " + ex);
        }
    }


    /**
     * Update OrganizationRole, if it exist already
     *
     * @param oleGokbOrganizationRole
     */
    public void updateOrganizationRole(OleGokbOrganizationRole oleGokbOrganizationRole) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(UPDATE_ORG_ROLE_PREPARED_STMT);
            pstmt.setInt(1, oleGokbOrganizationRole.getGokbOrganizationId());
            pstmt.setString(2, oleGokbOrganizationRole.getRole());
            pstmt.setInt(3, oleGokbOrganizationRole.getGokbOrgRoleId());
            pstmt.execute();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the Organization Role: " + ex);
        }
    }


    /**
     * Deleting all rows from the tables
     */
    public void truncateTables() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("TRUNCATE OLE_GOKB_PKG_T");
            stmt.executeUpdate("TRUNCATE OLE_GOKB_TIPP_T");
            stmt.executeUpdate("TRUNCATE OLE_GOKB_TITLE_T");
            stmt.executeUpdate("TRUNCATE OLE_GOKB_PLTFRM_T");
            stmt.executeUpdate("TRUNCATE OLE_GOKB_ORG_T");
            stmt.executeUpdate("TRUNCATE OLE_GOKB_ORG_ROLE_T");
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Truncate the Tables: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Get updated date
     *
     * @return
     */
    public Timestamp getUpdatedDate() {
        Timestamp startTime = null;
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }

            Statement selectStmt = connection.createStatement();
            ResultSet rs = null;
            rs = selectStmt.executeQuery("SELECT START_TIME FROM OLE_GOKB_UPDATE_LOG_T ORDER BY ID DESC LIMIT 1");
            if (rs.next()) {
                startTime = rs.getTimestamp(1);
            }
            connection.commit();
            selectStmt.close();
            rs.close();
        } catch (SQLException ex) {
            LOG.error("Exception while getting Last Update date: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
        return startTime;
    }

    /**
     * Get recently inserted row id
     *
     * @return
     */
    public int insertStatus() {
        Integer lastInsertedId = 0;
        OleGokbUpdateLog oleGokbUpdateLog = new OleGokbUpdateLog();

        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            Date localTime = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Statement stmt = connection.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("SELECT ID FROM OLE_GOKB_UPDATE_LOG_T ORDER BY ID DESC LIMIT 1");
            while (rs.next()) {
                lastInsertedId = rs.getInt(1);
            }
            lastInsertedId = lastInsertedId + 1;
            String sqlStmt = "INSERT INTO OLE_GOKB_UPDATE_LOG_T (ID,START_TIME, STATUS) VALUES(" + lastInsertedId + ",'" + df.format(localTime).toString() + "', 'Running')";
            stmt.execute(sqlStmt);
            connection.commit();
            stmt.close();
            rs.close();
            oleGokbUpdateLog.setId(lastInsertedId);
        } catch (SQLException ex) {
            LOG.error("Exception while getting Last Inserted Id from Log table: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
        return Integer.parseInt(lastInsertedId.toString());
    }

    public void insertLogEndTime(int updatedId) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            Date localTime = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Statement stmt = connection.createStatement();
            String sqlStmt = "UPDATE OLE_GOKB_UPDATE_LOG_T SET STATUS='Completed', END_TIME='" + df.format(localTime).toString() + "' where ID=" + updatedId;
            stmt.execute(sqlStmt);
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert End time in Log table: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Update the status
     *
     * @param id
     * @param columnValue
     */
    public void updateStatus(int id, String columnValue) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            String sqlStmt = "UPDATE OLE_GOKB_UPDATE_LOG_T SET " + columnValue + " WHERE ID=" + id;
            Statement stmt = connection.createStatement();
            stmt.execute(sqlStmt);
            connection.commit();
            stmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Update the the Status: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Insert list of Packages
     *
     * @param oleGokbPackages
     */
    public void insertPackages(List<OleGokbPackage> oleGokbPackages) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_PACKAGE_PREPARED_STMT);
            for (OleGokbPackage oleGokbPack : oleGokbPackages) {
                buildPackage(pstmt, oleGokbPack);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Package: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * @param oleGokbTipps Insert list of Tipps
     */

    public void insertTipps(List<OleGokbTipp> oleGokbTipps) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_TIPP_PREPARED_STMT);
            for (OleGokbTipp oleGokbTipp : oleGokbTipps) {
                buildTipp(pstmt, oleGokbTipp);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Tipp: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Insert list of Titles
     *
     * @param oleGokbTitles
     */
    public void insertTitles(List<OleGokbTitle> oleGokbTitles) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_TITLE_PREPARED_STMT);
            for (OleGokbTitle oleGokbTitle : oleGokbTitles) {
                buildTitle(pstmt, oleGokbTitle);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Title: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Insert list of Platforms
     *
     * @param oleGokbPlatforms
     */
    public void insertPlatforms(List<OleGokbPlatform> oleGokbPlatforms) {
        try {
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_PLATFORM_PREPARED_STMT);
            for (OleGokbPlatform oleGokbPlatform : oleGokbPlatforms) {
                buildPlatform(pstmt, oleGokbPlatform);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Platform: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }


    /**
     * Insert list of Organizations
     *
     * @param oleGokbOrganizations
     */
    public void insertOrganizations(List<OleGokbOrganization> oleGokbOrganizations) {
        try {

            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }

            PreparedStatement pstmt = connection.prepareStatement(INSERT_ORG_PREPARED_STMT);
            for (OleGokbOrganization oleGokbOrganization : oleGokbOrganizations) {
                buildOrganization(pstmt, oleGokbOrganization);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Organization: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Insert list of OrganizationRoles
     *
     * @param oleGokbOrganizationRoles
     */
    public void insertOrganizationRoles(List<OleGokbOrganizationRole> oleGokbOrganizationRoles) {
        try {
            Integer lastInsertedId = 0;
            if (connection == null || connection.isClosed()) {
                connection = getConnection();
            }
            Statement stmt = connection.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("SELECT ID FROM OLE_GOKB_ORG_ROLE_T ORDER BY ID DESC LIMIT 1");
            while (rs.next()) {
                lastInsertedId = rs.getInt(1);
            }
            PreparedStatement pstmt = connection.prepareStatement(INSERT_ORG_ROLE_PREPARED_STMT);
            for (OleGokbOrganizationRole oleGokbOrganizationRole : oleGokbOrganizationRoles) {
                oleGokbOrganizationRole.setGokbOrgRoleId(lastInsertedId + 1);
                buildOrganizationRole(pstmt, oleGokbOrganizationRole);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            pstmt.close();
            stmt.close();
            rs.close();

        } catch (SQLException ex) {
            LOG.error("Exception while Insert the Organization Role: " + ex);
        } finally {
            try {
                closeConnections();
            } catch (Exception e) {
                LOG.error("Exception while close the Connection: " + e);
            }
        }
    }

    /**
     * Build Prepared Statement for Package
     * @param pstmt
     * @param oleGokbPack
     */
    private void buildPackage(PreparedStatement pstmt, OleGokbPackage oleGokbPack) {
        try {
            pstmt.setInt(1, oleGokbPack.getGokbPackageId());
            pstmt.setString(2, oleGokbPack.getPackageName());
            pstmt.setString(3, oleGokbPack.getVariantName());
            pstmt.setString(4, oleGokbPack.getStatus());
            pstmt.setString(5, oleGokbPack.getPackageScope());
            pstmt.setString(6, oleGokbPack.getBreakable());
            pstmt.setString(7, oleGokbPack.getFixed());
            pstmt.setString(8, oleGokbPack.getAvailability());
            pstmt.setTimestamp(9, oleGokbPack.getDateCreated());
            pstmt.setTimestamp(10, oleGokbPack.getDateUpdated());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Package: " + ex);
        }
    }

    /**
     * Build Prepared Statement for Tipp
     * @param pstmt
     * @param oleGokbTipp
     */
    private void buildTipp(PreparedStatement pstmt, OleGokbTipp oleGokbTipp) {
        try {
            pstmt.setInt(1, oleGokbTipp.getGokbTippId());
            pstmt.setInt(2, oleGokbTipp.getGokbPackageId());
            pstmt.setInt(3, oleGokbTipp.getGokbTitleId());
            pstmt.setInt(4, oleGokbTipp.getGokbPlatformId());
            pstmt.setString(5, oleGokbTipp.getStatus());
            pstmt.setString(6, oleGokbTipp.getStatusReason());
            pstmt.setTimestamp(7, oleGokbTipp.getStartdate());
            pstmt.setString(8, oleGokbTipp.getStartVolume());
            pstmt.setString(9, oleGokbTipp.getStartIssue());
            pstmt.setTimestamp(10, oleGokbTipp.getEndDate());
            pstmt.setString(11, oleGokbTipp.getEndVolume());
            pstmt.setString(12, oleGokbTipp.getEndIssue());
            pstmt.setString(13, oleGokbTipp.getEmbarco());
            pstmt.setString(14, oleGokbTipp.getPlatformHostUrl());
            pstmt.setTimestamp(15, oleGokbTipp.getDateCreated());
            pstmt.setTimestamp(16, oleGokbTipp.getDateUpdated());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Tipp: " + ex);
        }
    }

    /**
     * Build Prepared Statement for Title
     * @param pstmt
     * @param oleGokbTitle
     */
    private void buildTitle(PreparedStatement pstmt, OleGokbTitle oleGokbTitle) {
        try {
            pstmt.setInt(1, oleGokbTitle.getGokbTitleId());
            pstmt.setString(2, oleGokbTitle.getTitleName());
            pstmt.setString(3, oleGokbTitle.getVariantName());
            pstmt.setString(4, oleGokbTitle.getMedium());
            pstmt.setString(5, oleGokbTitle.getPureQa());
            pstmt.setString(6, oleGokbTitle.getIssnOnline());
            pstmt.setString(7, oleGokbTitle.getIssnPrint());
            pstmt.setString(8, oleGokbTitle.getIssnL());
            pstmt.setInt(9, oleGokbTitle.getOclcNumber());
            pstmt.setString(10, oleGokbTitle.getDoi());
            pstmt.setInt(11, oleGokbTitle.getProprietaryId());
            pstmt.setString(12, oleGokbTitle.getSuncat());
            pstmt.setString(13, oleGokbTitle.getLccn());
            pstmt.setInt(14, oleGokbTitle.getPublisherId());
            pstmt.setInt(15, oleGokbTitle.getImprint());
            pstmt.setTimestamp(16, oleGokbTitle.getDateUpdated());//oleGokbTitle.getDateCreated()
            pstmt.setTimestamp(17, oleGokbTitle.getDateUpdated());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Title: " + ex);
        }
    }

    /**
     * Build Prepared Statement for Platform
     * @param pstmt
     * @param oleGokbPlatform
     */
    private void buildPlatform(PreparedStatement pstmt, OleGokbPlatform oleGokbPlatform) {
        try {
            pstmt.setInt(1, oleGokbPlatform.getGokbPlatformId());
            pstmt.setString(2, oleGokbPlatform.getPlatformName());
            pstmt.setString(3, oleGokbPlatform.getStatus());
            pstmt.setInt(4, oleGokbPlatform.getPlatformProviderId());
            pstmt.setString(5, oleGokbPlatform.getAuthentication());
            pstmt.setString(6, oleGokbPlatform.getSoftwarePlatform());
            pstmt.setTimestamp(7, oleGokbPlatform.getDateCreated());
            pstmt.setTimestamp(8, oleGokbPlatform.getDateUpdated());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Platform: " + ex);
        }
    }

    /**
     * Build Prepared Statement for Organization
     * @param pstmt
     * @param oleGokbOrganization
     */
    private void buildOrganization(PreparedStatement pstmt, OleGokbOrganization oleGokbOrganization) {
        try {
            pstmt.setInt(1, oleGokbOrganization.getGokbOrganizationId());
            pstmt.setString(2, oleGokbOrganization.getOrganizationName());
            pstmt.setString(3, oleGokbOrganization.getVariantName());
            pstmt.setTimestamp(4, oleGokbOrganization.getDateCreated());
            pstmt.setTimestamp(5, oleGokbOrganization.getDateUpdated());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Organization: " + ex);
        }
    }

    /**
     * Build Prepared Statement for Organization Role
     * @param pstmt
     * @param oleGokbOrganizationRole
     */
    private void buildOrganizationRole(PreparedStatement pstmt, OleGokbOrganizationRole oleGokbOrganizationRole) {
        try {
            pstmt.setInt(1, oleGokbOrganizationRole.getGokbOrgRoleId());
            pstmt.setInt(2, oleGokbOrganizationRole.getGokbOrganizationId());
            pstmt.setString(3, oleGokbOrganizationRole.getRole());
        } catch (SQLException ex) {
            LOG.error("Exception while build the Organization Role: " + ex);
        }
    }


    /**
     * Close the SQL connections
     *
     * @throws java.sql.SQLException
     */
    public void closeConnections() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
