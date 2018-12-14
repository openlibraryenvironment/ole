--  *********************************************************************
--  Update Database Script
--  *********************************************************************
--  Change Log: org/kuali/ole/3.0.15/db.changelog-20181005.xml
--  *********************************************************************

--  Lock Database
--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::ALTER_OLE_DS_HOLDINGS_URI_T_URI_MYSQL::ole
ALTER TABLE OLE_DS_HOLDINGS_URI_T MODIFY URI VARCHAR(2400)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ALTER_OLE_DS_HOLDINGS_URI_T_URI_MYSQL', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 1, '7:c435f14d3061232d056ceb834492b356', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::ALTER_OLE_DS_HOLDINGS_URI_T_TEXT_MYSQL::ole
ALTER TABLE OLE_DS_HOLDINGS_URI_T MODIFY TEXT VARCHAR(2400)
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ALTER_OLE_DS_HOLDINGS_URI_T_TEXT_MYSQL', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 2, '7:54eacad949365876140c4d1ab784692d', 'sql', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::ole_cat_bib_record_stat_t::ole
INSERT INTO ole_cat_bib_record_stat_t (BIB_RECORD_STAT_ID, BIB_RECORD_STAT_CD, BIB_RECORD_STAT_NM, SRC, SRC_DT, ROW_ACT_IND, OBJ_ID, VER_NBR) VALUES ('20', 'circ', 'Circulation', 'UC', '2018-10-31 00:00:00', 'Y', 'objabc123qwe123', '1')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('ole_cat_bib_record_stat_t', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 3, '7:c17a12680513eafce1feb5e7968f8c63', 'loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::OLE_LOAD_KRCR_PARM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRCR_PARM_T (NMSPC_CD, CMPNT_CD, PARM_NM, OBJ_ID, VER_NBR, PARM_TYP_CD, VAL, PARM_DESC_TXT, EVAL_OPRTR_CD, APPL_ID) VALUES ('OLE-DLVR', 'Deliver', 'NCIP_BIB_STATUS', 'OLE-9396', '1', 'CONFG', 'Circulation', 'This Parameter is used to configure the bib status for NCIP Request', 'A', 'KUALI')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_LOAD_KRCR_PARM_T_mysql', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 4, '7:fceda5239a279bc2e7540789903dcb9b', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::OLE_KRIM_PERM_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1001', 'OLELOAN1001', '1', '56', 'OLE-DLVR', 'View Alter Due Date Button', 'Allows users to viea and access Alter Due Date Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1002', 'OLELOAN1002', '1', '56', 'OLE-DLVR', 'View Replace Copy Button', 'Allows users to view and access Replace Copy Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1003', 'OLELOAN1003', '1', '56', 'OLE-DLVR', 'View Claims Return Button', 'Allows users to view and access Claims Return Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1004', 'OLELOAN1004', '1', '56', 'OLE-DLVR', 'View Bill Item as Lost Button', 'Allows users to view and access Bill Item as Lost Operation..', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1005', 'OLELOAN1005', '1', '56', 'OLE-DLVR', 'View Missing Piece Button', 'Allows users to view and access Missing Piece Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1006', 'OLELOAN1006', '1', '56', 'OLE-DLVR', 'View Damaged Button', 'Allows users to view and access Damaged Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1007', 'OLELOAN1007', '1', '56', 'OLE-DLVR', 'View Cancel Button', 'Allows users to view and access Cancel Operation..', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1008', 'OLELOAN1008', '1', '56', 'OLE-DLVR', 'View Cancel Credit Button', 'Allows users to view and access Cancel Credit Operation.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1009', 'OLELOAN1009', '1', '56', 'OLE-DLVR', 'View Error Button', 'Allows users to view and access Error Operation', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1010', 'OLELOAN1010', '1', '56', 'OLE-DLVR', 'View Claims Return Screen', 'Allows users to view and access Claims Return Screen.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1011', 'OLELOAN1011', '1', '56', 'OLE-DLVR', 'Override Patron Block', 'Allows users to view and access Override Patron.', 'Y')
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID, VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND) VALUES ('OLELOAN1012', 'OLELOAN1012', '1', '42', 'OLE-DLVR', 'View Create Bill Screen', 'Allows users to view and access Create Bill Screen.', 'Y')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_PERM_T_mysql', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 5, '7:f5a9afa66cf73d05f0c13f963b855911', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Changeset org/kuali/ole/3.0.15/db.changelog-20181005.xml::OLE_KRIM_PERM_ATTR_DATA_T_mysql::ole
SET sql_mode='NO_BACKSLASH_ESCAPES'
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLELOAN1001', 'OLELOAN1001', '1', 'OLELOAN1012', '56', '13', 'PatronBillMaintenanceDocument')
/

INSERT INTO KRIM_PERM_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ('OLELOAN1002', 'OLELOAN1002', '1', 'OLELOAN1012', '56', '7', 'FALSE')
/

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, LIQUIBASE) VALUES ('OLE_KRIM_PERM_ATTR_DATA_T_mysql', 'ole', 'org/kuali/ole/3.0.15/db.changelog-20181005.xml', NOW(), 6, '7:b165a7f0fed51e884a94a8174f9c0ecd', 'sql, loadData', '', 'EXECUTED', '3.2.0')
/

--  Release Database Lock
--  Release Database Lock
