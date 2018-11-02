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

--  Release Database Lock
--  Release Database Lock
