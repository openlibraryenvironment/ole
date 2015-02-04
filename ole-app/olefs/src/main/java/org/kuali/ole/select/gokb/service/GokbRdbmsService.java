package org.kuali.ole.select.gokb.service;

import org.kuali.ole.select.gokb.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by premkumarv on 12/18/14.
 */
public interface GokbRdbmsService {
    public static String INSERT_PACKAGE_PREPARED_STMT = "INSERT INTO OLE_GOKB_PKG_T (GOKB_PKG_ID, PKG_NAME, VARIANT_NAME, PKG_STATUS, PKG_SCOPE, BREAKABLE, FXD, AVLBLE, DATE_CREATED, DATE_UPDATED)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?)";

    public static String INSERT_TIPP_PREPARED_STMT = "INSERT INTO OLE_GOKB_TIPP_T (GOKB_TIPP_ID, GOKB_PKG_ID, GOKB_TITLE_ID, GOKB_PLTFRM_ID, TIPP_STATUS, STATUS_REASON, STRT_DT, STRT_VOL, STRT_ISSUE, END_DT, END_VOL, END_ISSUE, EMBARGO, PLTFRM_HOST_URL, DATE_CREATED, DATE_UPDATED)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static String INSERT_TITLE_PREPARED_STMT = "INSERT INTO OLE_GOKB_TITLE_T (GOKB_TITLE_ID, TITLE_NAME, VARIANT_NAME, MEDIUM, PURE_QA, TI_ISSN_ONLINE, TI_ISSN_PRNT, TI_ISSN_L, OCLC_NUM, TI_DOI, TI_PROPRIETARY_ID, TI_SUNCAT, TI_LCCN, PUBLSHR_ID, IMPRINT, DATE_CREATED, DATE_UPDATED)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static String INSERT_PLATFORM_PREPARED_STMT = "INSERT INTO OLE_GOKB_PLTFRM_T (GOKB_PLTFRM_ID, PLTFRM_NAME, PLTFRM_STATUS, PLTFRM_PRVDR_ID, AUTH, SOFTWARE_PLTFRM, DATE_CREATED, DATE_UPDATED)" +
            " VALUES(?,?,?,?,?,?,?,?)";

    public static String INSERT_ORG_PREPARED_STMT = "INSERT INTO OLE_GOKB_ORG_T (GOKB_ORG_ID, ORG_NAME, VARIANT_NAME, DATE_CREATED, DATE_UPDATED)" +
            " VALUES(?,?,?,?,?)";


    public static String INSERT_ORG_ROLE_PREPARED_STMT = "INSERT INTO OLE_GOKB_ORG_ROLE_T (GOKB_ORG_ROLE_ID, GOKB_ORG_ID, ROLE)" +
            " VALUES(?,?,?)";

//    public static String INSERT_GOKB_UPDATE_LOG_STMT =  "INSERT INTO OLE_GOKB_UPDATE_LOG_S values ()";



    public static String UPDATE_PACKAGE_PREPARED_STMT = "UPDATE OLE_GOKB_PKG_T SET PKG_NAME=?, VARIANT_NAME=?, PKG_STATUS=?, PKG_SCOPE=?, BREAKABLE=?, FXD=?, AVLBLE=?, DATE_CREATED=?, DATE_UPDATED=? WHERE GOKB_PKG_ID=?";

    public static String UPDATE_TIPP_PREPARED_STMT = "UPDATE OLE_GOKB_TIPP_T SET GOKB_PKG_ID=?, GOKB_TITLE_ID=?, GOKB_PLTFRM_ID=?, TIPP_STATUS=?, STATUS_REASON=?, STRT_DT=?, STRT_VOL=?, STRT_ISSUE=?, END_DT=?, END_VOL=?, END_ISSUE=?, EMBARGO=?, PLTFRM_HOST_URL=?, DATE_CREATED=?, DATE_UPDATED=? WHERE GOKB_TIPP_ID=?";

    public static String UPDATE_TITLE_PREPARED_STMT = "UPDATE OLE_GOKB_TITLE_T SET TITLE_NAME=?, VARIANT_NAME=?, MEDIUM=?, PURE_QA=?, TI_ISSN_ONLINE=?, TI_ISSN_PRNT=?, TI_ISSN_L=?, OCLC_NUM=?, TI_DOI=?, TI_PROPRIETARY_ID=?, TI_SUNCAT=?, TI_LCCN=?, PUBLSHR_ID=?, IMPRINT=?, DATE_CREATED=?, DATE_UPDATED=? WHERE GOKB_TITLE_ID=?";

    public static String UPDATE_PLATFORM_PREPARED_STMT = "UPDATE OLE_GOKB_PLTFRM_T SET PLTFRM_NAME=?, PLTFRM_STATUS=?, PLTFRM_PRVDR_ID=?, AUTH=?, SOFTWARE_PLTFRM=?, DATE_CREATED=?, DATE_UPDATED=? WHERE GOKB_PLTFRM_ID=?";

    public static String UPDATE_ORG_PREPARED_STMT = "UPDATE OLE_GOKB_ORG_T SET ORG_NAME=?, VARIANT_NAME=?, DATE_CREATED=?, DATE_UPDATED=? WHERE GOKB_ORG_ID=?";

    public static String UPDATE_ORG_ROLE_PREPARED_STMT = "UPDATE OLE_GOKB_ORG_ROLE_T GOKB_ORG_ID=?, SET ROLE=? WHERE GOKB_ORG_ROLE_ID=?";

    public void insertOrUpdatePackages(List<OleGokbPackage> oleGokbPackages);

    public void insertOrUpdateTipps(List<OleGokbTipp> oleGokbTipps);

    public void insertOrUpdateTitles(List<OleGokbTitle> oleGokbTitles);

    public void insertOrUpdatePlatforms(List<OleGokbPlatform> oleGokbPlatforms);

    public void insertOrUpdateOrganizations(List<OleGokbOrganization> oleGokbOrganizations);

    public void insertOrUpdateOrganizationRoles(List<OleGokbOrganizationRole> oleGokbOrganizationRoles);

    public void insertPackages(List<OleGokbPackage> oleGokbPackages);

    public void insertTipps(List<OleGokbTipp> oleGokbTipps);

    public void insertTitles(List<OleGokbTitle> oleGokbTitles);

    public void insertPlatforms(List<OleGokbPlatform> oleGokbPlatforms);

    public void insertOrganizations(List<OleGokbOrganization> oleGokbOrganizations);

    public void insertOrganizationRoles(List<OleGokbOrganizationRole> oleGokbOrganizationRoles);

    public void truncateTables();

    public Timestamp getUpdatedDate();

    public void updateStatus(int id, String columnValue);

    public int insertStatus();

    public void insertLogEndTime(int updatedId);


}
